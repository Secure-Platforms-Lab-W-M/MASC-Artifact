package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.helper.StringUtil;

public class DocumentType extends Node {
   private static final String NAME = "name";
   private static final String PUBLIC_ID = "publicId";
   private static final String SYSTEM_ID = "systemId";

   public DocumentType(String var1, String var2, String var3, String var4) {
      super(var4);
      this.attr("name", var1);
      this.attr("publicId", var2);
      this.attr("systemId", var3);
   }

   private boolean has(String var1) {
      return !StringUtil.isBlank(this.attr(var1));
   }

   public String nodeName() {
      return "#doctype";
   }

   void outerHtmlHead(Appendable var1, int var2, Document.OutputSettings var3) throws IOException {
      if (var3.syntax() == Document.OutputSettings.Syntax.html && !this.has("publicId") && !this.has("systemId")) {
         var1.append("<!doctype");
      } else {
         var1.append("<!DOCTYPE");
      }

      if (this.has("name")) {
         var1.append(" ").append(this.attr("name"));
      }

      if (this.has("publicId")) {
         var1.append(" PUBLIC \"").append(this.attr("publicId")).append('"');
      }

      if (this.has("systemId")) {
         var1.append(" \"").append(this.attr("systemId")).append('"');
      }

      var1.append('>');
   }

   void outerHtmlTail(Appendable var1, int var2, Document.OutputSettings var3) {
   }
}
