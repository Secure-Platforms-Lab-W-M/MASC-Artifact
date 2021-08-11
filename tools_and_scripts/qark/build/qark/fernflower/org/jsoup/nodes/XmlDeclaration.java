package org.jsoup.nodes;

import java.io.IOException;

public class XmlDeclaration extends Node {
   static final String DECL_KEY = "declaration";
   private final boolean isProcessingInstruction;

   public XmlDeclaration(String var1, String var2, boolean var3) {
      super(var2);
      this.attributes.put("declaration", var1);
      this.isProcessingInstruction = var3;
   }

   public String getWholeDeclaration() {
      String var1 = this.attributes.get("declaration");
      if (var1.equals("xml") && this.attributes.size() > 1) {
         StringBuilder var3 = new StringBuilder(var1);
         String var2 = this.attributes.get("version");
         if (var2 != null) {
            var3.append(" version=\"").append(var2).append("\"");
         }

         var2 = this.attributes.get("encoding");
         if (var2 != null) {
            var3.append(" encoding=\"").append(var2).append("\"");
         }

         return var3.toString();
      } else {
         return this.attributes.get("declaration");
      }
   }

   public String nodeName() {
      return "#declaration";
   }

   void outerHtmlHead(Appendable var1, int var2, Document.OutputSettings var3) throws IOException {
      Appendable var5 = var1.append("<");
      String var4;
      if (this.isProcessingInstruction) {
         var4 = "!";
      } else {
         var4 = "?";
      }

      var5.append(var4).append(this.getWholeDeclaration()).append(">");
   }

   void outerHtmlTail(Appendable var1, int var2, Document.OutputSettings var3) {
   }

   public String toString() {
      return this.outerHtml();
   }
}
