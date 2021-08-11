package org.jsoup.safety;

import java.util.Iterator;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class Cleaner {
   private Whitelist whitelist;

   public Cleaner(Whitelist var1) {
      Validate.notNull(var1);
      this.whitelist = var1;
   }

   private int copySafeNodes(Element var1, Element var2) {
      Cleaner.CleaningVisitor var3 = new Cleaner.CleaningVisitor(var1, var2);
      (new NodeTraversor(var3)).traverse(var1);
      return var3.numDiscarded;
   }

   private Cleaner.ElementMeta createSafeElement(Element var1) {
      String var3 = var1.tagName();
      Attributes var4 = new Attributes();
      Element var5 = new Element(Tag.valueOf(var3), var1.baseUri(), var4);
      int var2 = 0;
      Iterator var6 = var1.attributes().iterator();

      while(var6.hasNext()) {
         Attribute var7 = (Attribute)var6.next();
         if (this.whitelist.isSafeAttribute(var3, var1, var7)) {
            var4.put(var7);
         } else {
            ++var2;
         }
      }

      var4.addAll(this.whitelist.getEnforcedAttributes(var3));
      return new Cleaner.ElementMeta(var5, var2);
   }

   public Document clean(Document var1) {
      Validate.notNull(var1);
      Document var2 = Document.createShell(var1.baseUri());
      if (var1.body() != null) {
         this.copySafeNodes(var1.body(), var2.body());
      }

      return var2;
   }

   public boolean isValid(Document var1) {
      Validate.notNull(var1);
      Document var2 = Document.createShell(var1.baseUri());
      return this.copySafeNodes(var1.body(), var2.body()) == 0;
   }

   private final class CleaningVisitor implements NodeVisitor {
      private Element destination;
      private int numDiscarded;
      private final Element root;

      private CleaningVisitor(Element var2, Element var3) {
         this.numDiscarded = 0;
         this.root = var2;
         this.destination = var3;
      }

      // $FF: synthetic method
      CleaningVisitor(Element var2, Element var3, Object var4) {
         this(var2, var3);
      }

      public void head(Node var1, int var2) {
         if (var1 instanceof Element) {
            Element var3 = (Element)var1;
            if (Cleaner.this.whitelist.isSafeTag(var3.tagName())) {
               Cleaner.ElementMeta var6 = Cleaner.this.createSafeElement(var3);
               var3 = var6.field_36;
               this.destination.appendChild(var3);
               this.numDiscarded += var6.numAttribsDiscarded;
               this.destination = var3;
            } else if (var1 != this.root) {
               ++this.numDiscarded;
               return;
            }

         } else if (var1 instanceof TextNode) {
            TextNode var5 = new TextNode(((TextNode)var1).getWholeText(), var1.baseUri());
            this.destination.appendChild(var5);
         } else if (var1 instanceof DataNode && Cleaner.this.whitelist.isSafeTag(var1.parent().nodeName())) {
            DataNode var4 = new DataNode(((DataNode)var1).getWholeData(), var1.baseUri());
            this.destination.appendChild(var4);
         } else {
            ++this.numDiscarded;
         }
      }

      public void tail(Node var1, int var2) {
         if (var1 instanceof Element && Cleaner.this.whitelist.isSafeTag(var1.nodeName())) {
            this.destination = this.destination.parent();
         }

      }
   }

   private static class ElementMeta {
      // $FF: renamed from: el org.jsoup.nodes.Element
      Element field_36;
      int numAttribsDiscarded;

      ElementMeta(Element var1, int var2) {
         this.field_36 = var1;
         this.numAttribsDiscarded = var2;
      }
   }
}
