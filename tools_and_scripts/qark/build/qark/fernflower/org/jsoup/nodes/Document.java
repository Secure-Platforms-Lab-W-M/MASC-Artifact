package org.jsoup.nodes;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class Document extends Element {
   private String location;
   private Document.OutputSettings outputSettings = new Document.OutputSettings();
   private Document.QuirksMode quirksMode;
   private boolean updateMetaCharset;

   public Document(String var1) {
      super(Tag.valueOf("#root"), var1);
      this.quirksMode = Document.QuirksMode.noQuirks;
      this.updateMetaCharset = false;
      this.location = var1;
   }

   public static Document createShell(String var0) {
      Validate.notNull(var0);
      Document var2 = new Document(var0);
      Element var1 = var2.appendElement("html");
      var1.appendElement("head");
      var1.appendElement("body");
      return var2;
   }

   private void ensureMetaCharsetElement() {
      if (this.updateMetaCharset) {
         Document.OutputSettings.Syntax var1 = this.outputSettings().syntax();
         if (var1 == Document.OutputSettings.Syntax.html) {
            Element var2 = this.select("meta[charset]").first();
            if (var2 != null) {
               var2.attr("charset", this.charset().displayName());
            } else {
               var2 = this.head();
               if (var2 != null) {
                  var2.appendElement("meta").attr("charset", this.charset().displayName());
               }
            }

            this.select("meta[name=charset]").remove();
         } else if (var1 == Document.OutputSettings.Syntax.xml) {
            Node var3 = (Node)this.childNodes().get(0);
            XmlDeclaration var4;
            if (!(var3 instanceof XmlDeclaration)) {
               var4 = new XmlDeclaration("xml", this.baseUri, false);
               var4.attr("version", "1.0");
               var4.attr("encoding", this.charset().displayName());
               this.prependChild(var4);
               return;
            }

            var4 = (XmlDeclaration)var3;
            if (!var4.attr("declaration").equals("xml")) {
               var4 = new XmlDeclaration("xml", this.baseUri, false);
               var4.attr("version", "1.0");
               var4.attr("encoding", this.charset().displayName());
               this.prependChild(var4);
               return;
            }

            var4.attr("encoding", this.charset().displayName());
            if (var4.attr("version") != null) {
               var4.attr("version", "1.0");
               return;
            }
         }
      }

   }

   private Element findFirstElementByTagName(String var1, Node var2) {
      if (var2.nodeName().equals(var1)) {
         return (Element)var2;
      } else {
         Iterator var4 = var2.childNodes.iterator();

         Element var3;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            var3 = this.findFirstElementByTagName(var1, (Node)var4.next());
         } while(var3 == null);

         return var3;
      }
   }

   private void normaliseStructure(String var1, Element var2) {
      Elements var4 = this.getElementsByTag(var1);
      Element var8 = var4.first();
      if (var4.size() > 1) {
         ArrayList var5 = new ArrayList();

         for(int var3 = 1; var3 < var4.size(); ++var3) {
            Node var6 = (Node)var4.get(var3);
            Iterator var7 = var6.childNodes.iterator();

            while(var7.hasNext()) {
               var5.add((Node)var7.next());
            }

            var6.remove();
         }

         Iterator var9 = var5.iterator();

         while(var9.hasNext()) {
            var8.appendChild((Node)var9.next());
         }
      }

      if (!var8.parent().equals(var2)) {
         var2.appendChild(var8);
      }

   }

   private void normaliseTextNodes(Element var1) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.childNodes.iterator();

      while(var4.hasNext()) {
         Node var5 = (Node)var4.next();
         if (var5 instanceof TextNode) {
            TextNode var7 = (TextNode)var5;
            if (!var7.isBlank()) {
               var3.add(var7);
            }
         }
      }

      for(int var2 = var3.size() - 1; var2 >= 0; --var2) {
         Node var6 = (Node)var3.get(var2);
         var1.removeChild(var6);
         this.body().prependChild(new TextNode(" ", ""));
         this.body().prependChild(var6);
      }

   }

   public Element body() {
      return this.findFirstElementByTagName("body", this);
   }

   public Charset charset() {
      return this.outputSettings.charset();
   }

   public void charset(Charset var1) {
      this.updateMetaCharsetElement(true);
      this.outputSettings.charset(var1);
      this.ensureMetaCharsetElement();
   }

   public Document clone() {
      Document var1 = (Document)super.clone();
      var1.outputSettings = this.outputSettings.clone();
      return var1;
   }

   public Element createElement(String var1) {
      return new Element(Tag.valueOf(var1), this.baseUri());
   }

   public Element head() {
      return this.findFirstElementByTagName("head", this);
   }

   public String location() {
      return this.location;
   }

   public String nodeName() {
      return "#document";
   }

   public Document normalise() {
      Element var2 = this.findFirstElementByTagName("html", this);
      Element var1 = var2;
      if (var2 == null) {
         var1 = this.appendElement("html");
      }

      if (this.head() == null) {
         var1.prependElement("head");
      }

      if (this.body() == null) {
         var1.appendElement("body");
      }

      this.normaliseTextNodes(this.head());
      this.normaliseTextNodes(var1);
      this.normaliseTextNodes(this);
      this.normaliseStructure("head", var1);
      this.normaliseStructure("body", var1);
      this.ensureMetaCharsetElement();
      return this;
   }

   public String outerHtml() {
      return super.html();
   }

   public Document.OutputSettings outputSettings() {
      return this.outputSettings;
   }

   public Document outputSettings(Document.OutputSettings var1) {
      Validate.notNull(var1);
      this.outputSettings = var1;
      return this;
   }

   public Document.QuirksMode quirksMode() {
      return this.quirksMode;
   }

   public Document quirksMode(Document.QuirksMode var1) {
      this.quirksMode = var1;
      return this;
   }

   public Element text(String var1) {
      this.body().text(var1);
      return this;
   }

   public String title() {
      Element var1 = this.getElementsByTag("title").first();
      return var1 != null ? StringUtil.normaliseWhitespace(var1.text()).trim() : "";
   }

   public void title(String var1) {
      Validate.notNull(var1);
      Element var2 = this.getElementsByTag("title").first();
      if (var2 == null) {
         this.head().appendElement("title").text(var1);
      } else {
         var2.text(var1);
      }
   }

   public void updateMetaCharsetElement(boolean var1) {
      this.updateMetaCharset = var1;
   }

   public boolean updateMetaCharsetElement() {
      return this.updateMetaCharset;
   }

   public static class OutputSettings implements Cloneable {
      private Charset charset;
      private CharsetEncoder charsetEncoder;
      private Entities.EscapeMode escapeMode;
      private int indentAmount;
      private boolean outline;
      private boolean prettyPrint;
      private Document.OutputSettings.Syntax syntax;

      public OutputSettings() {
         this.escapeMode = Entities.EscapeMode.base;
         this.charset = Charset.forName("UTF-8");
         this.charsetEncoder = this.charset.newEncoder();
         this.prettyPrint = true;
         this.outline = false;
         this.indentAmount = 1;
         this.syntax = Document.OutputSettings.Syntax.html;
      }

      public Charset charset() {
         return this.charset;
      }

      public Document.OutputSettings charset(String var1) {
         this.charset(Charset.forName(var1));
         return this;
      }

      public Document.OutputSettings charset(Charset var1) {
         this.charset = var1;
         this.charsetEncoder = var1.newEncoder();
         return this;
      }

      public Document.OutputSettings clone() {
         Document.OutputSettings var1;
         try {
            var1 = (Document.OutputSettings)super.clone();
         } catch (CloneNotSupportedException var2) {
            throw new RuntimeException(var2);
         }

         var1.charset(this.charset.name());
         var1.escapeMode = Entities.EscapeMode.valueOf(this.escapeMode.name());
         return var1;
      }

      CharsetEncoder encoder() {
         return this.charsetEncoder;
      }

      public Document.OutputSettings escapeMode(Entities.EscapeMode var1) {
         this.escapeMode = var1;
         return this;
      }

      public Entities.EscapeMode escapeMode() {
         return this.escapeMode;
      }

      public int indentAmount() {
         return this.indentAmount;
      }

      public Document.OutputSettings indentAmount(int var1) {
         boolean var2;
         if (var1 >= 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         Validate.isTrue(var2);
         this.indentAmount = var1;
         return this;
      }

      public Document.OutputSettings outline(boolean var1) {
         this.outline = var1;
         return this;
      }

      public boolean outline() {
         return this.outline;
      }

      public Document.OutputSettings prettyPrint(boolean var1) {
         this.prettyPrint = var1;
         return this;
      }

      public boolean prettyPrint() {
         return this.prettyPrint;
      }

      public Document.OutputSettings.Syntax syntax() {
         return this.syntax;
      }

      public Document.OutputSettings syntax(Document.OutputSettings.Syntax var1) {
         this.syntax = var1;
         return this;
      }

      public static enum Syntax {
         html,
         xml;
      }
   }

   public static enum QuirksMode {
      limitedQuirks,
      noQuirks,
      quirks;
   }
}
