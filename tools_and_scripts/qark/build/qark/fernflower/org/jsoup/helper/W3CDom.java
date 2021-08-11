package org.jsoup.helper;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

public class W3CDom {
   protected DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

   public String asString(Document var1) {
      try {
         DOMSource var5 = new DOMSource(var1);
         StringWriter var2 = new StringWriter();
         StreamResult var3 = new StreamResult(var2);
         TransformerFactory.newInstance().newTransformer().transform(var5, var3);
         String var6 = var2.toString();
         return var6;
      } catch (TransformerException var4) {
         throw new IllegalStateException(var4);
      }
   }

   public void convert(org.jsoup.nodes.Document var1, Document var2) {
      if (!StringUtil.isBlank(var1.location())) {
         var2.setDocumentURI(var1.location());
      }

      Element var3 = var1.child(0);
      (new NodeTraversor(new W3CDom.W3CBuilder(var2))).traverse(var3);
   }

   public Document fromJsoup(org.jsoup.nodes.Document var1) {
      Validate.notNull(var1);

      try {
         this.factory.setNamespaceAware(true);
         Document var2 = this.factory.newDocumentBuilder().newDocument();
         this.convert(var1, var2);
         return var2;
      } catch (ParserConfigurationException var3) {
         throw new IllegalStateException(var3);
      }
   }

   protected static class W3CBuilder implements NodeVisitor {
      private static final String xmlnsKey = "xmlns";
      private static final String xmlnsPrefix = "xmlns:";
      private org.w3c.dom.Element dest;
      private final Document doc;
      private final HashMap namespaces = new HashMap();

      public W3CBuilder(Document var1) {
         this.doc = var1;
      }

      private void copyAttributes(Node var1, org.w3c.dom.Element var2) {
         Iterator var4 = var1.attributes().iterator();

         while(var4.hasNext()) {
            Attribute var3 = (Attribute)var4.next();
            var2.setAttribute(var3.getKey(), var3.getValue());
         }

      }

      private String updateNamespaces(Element var1) {
         Iterator var4 = var1.attributes().iterator();

         while(true) {
            String var3;
            Attribute var5;
            while(true) {
               if (!var4.hasNext()) {
                  int var2 = var1.tagName().indexOf(":");
                  if (var2 > 0) {
                     return var1.tagName().substring(0, var2);
                  }

                  return "";
               }

               var5 = (Attribute)var4.next();
               var3 = var5.getKey();
               if (var3.equals("xmlns")) {
                  var3 = "";
                  break;
               }

               if (var3.startsWith("xmlns:")) {
                  var3 = var3.substring("xmlns:".length());
                  break;
               }
            }

            this.namespaces.put(var3, var5.getValue());
         }
      }

      public void head(Node var1, int var2) {
         if (var1 instanceof Element) {
            Element var4 = (Element)var1;
            String var3 = this.updateNamespaces(var4);
            var3 = (String)this.namespaces.get(var3);
            org.w3c.dom.Element var10 = this.doc.createElementNS(var3, var4.tagName());
            this.copyAttributes(var4, var10);
            if (this.dest == null) {
               this.doc.appendChild(var10);
            } else {
               this.dest.appendChild(var10);
            }

            this.dest = var10;
         } else {
            Text var6;
            if (var1 instanceof TextNode) {
               TextNode var9 = (TextNode)var1;
               var6 = this.doc.createTextNode(var9.getWholeText());
               this.dest.appendChild(var6);
               return;
            }

            if (var1 instanceof Comment) {
               Comment var7 = (Comment)var1;
               org.w3c.dom.Comment var8 = this.doc.createComment(var7.getData());
               this.dest.appendChild(var8);
               return;
            }

            if (var1 instanceof DataNode) {
               DataNode var5 = (DataNode)var1;
               var6 = this.doc.createTextNode(var5.getWholeData());
               this.dest.appendChild(var6);
               return;
            }
         }

      }

      public void tail(Node var1, int var2) {
         if (var1 instanceof Element && this.dest.getParentNode() instanceof org.w3c.dom.Element) {
            this.dest = (org.w3c.dom.Element)this.dest.getParentNode();
         }

      }
   }
}
