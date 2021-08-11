package org.jsoup.nodes;

import java.io.IOException;

public class Comment extends Node {
   private static final String COMMENT_KEY = "comment";

   public Comment(String var1, String var2) {
      super(var2);
      this.attributes.put("comment", var1);
   }

   public String getData() {
      return this.attributes.get("comment");
   }

   public String nodeName() {
      return "#comment";
   }

   void outerHtmlHead(Appendable var1, int var2, Document.OutputSettings var3) throws IOException {
      if (var3.prettyPrint()) {
         this.indent(var1, var2, var3);
      }

      var1.append("<!--").append(this.getData()).append("-->");
   }

   void outerHtmlTail(Appendable var1, int var2, Document.OutputSettings var3) {
   }

   public String toString() {
      return this.outerHtml();
   }
}
