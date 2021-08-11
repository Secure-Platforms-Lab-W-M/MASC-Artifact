package org.jsoup.nodes;

import java.io.IOException;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class TextNode extends Node {
   private static final String TEXT_KEY = "text";
   String text;

   public TextNode(String var1, String var2) {
      this.baseUri = var2;
      this.text = var1;
   }

   public static TextNode createFromEncoded(String var0, String var1) {
      return new TextNode(Entities.unescape(var0), var1);
   }

   private void ensureAttributes() {
      if (this.attributes == null) {
         this.attributes = new Attributes();
         this.attributes.put("text", this.text);
      }

   }

   static boolean lastCharIsWhitespace(StringBuilder var0) {
      return var0.length() != 0 && var0.charAt(var0.length() - 1) == ' ';
   }

   static String normaliseWhitespace(String var0) {
      return StringUtil.normaliseWhitespace(var0);
   }

   static String stripLeadingWhitespace(String var0) {
      return var0.replaceFirst("^\\s+", "");
   }

   public String absUrl(String var1) {
      this.ensureAttributes();
      return super.absUrl(var1);
   }

   public String attr(String var1) {
      this.ensureAttributes();
      return super.attr(var1);
   }

   public Node attr(String var1, String var2) {
      this.ensureAttributes();
      return super.attr(var1, var2);
   }

   public Attributes attributes() {
      this.ensureAttributes();
      return super.attributes();
   }

   public String getWholeText() {
      return this.attributes == null ? this.text : this.attributes.get("text");
   }

   public boolean hasAttr(String var1) {
      this.ensureAttributes();
      return super.hasAttr(var1);
   }

   public boolean isBlank() {
      return StringUtil.isBlank(this.getWholeText());
   }

   public String nodeName() {
      return "#text";
   }

   void outerHtmlHead(Appendable var1, int var2, Document.OutputSettings var3) throws IOException {
      if (var3.prettyPrint() && (this.siblingIndex() == 0 && this.parentNode instanceof Element && ((Element)this.parentNode).tag().formatAsBlock() && !this.isBlank() || var3.outline() && this.siblingNodes().size() > 0 && !this.isBlank())) {
         this.indent(var1, var2, var3);
      }

      boolean var4;
      if (var3.prettyPrint() && this.parent() instanceof Element && !Element.preserveWhitespace(this.parent())) {
         var4 = true;
      } else {
         var4 = false;
      }

      Entities.escape(var1, this.getWholeText(), var3, false, var4, false);
   }

   void outerHtmlTail(Appendable var1, int var2, Document.OutputSettings var3) {
   }

   public Node removeAttr(String var1) {
      this.ensureAttributes();
      return super.removeAttr(var1);
   }

   public TextNode splitText(int var1) {
      boolean var2;
      if (var1 >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Split offset must be not be negative");
      if (var1 < this.text.length()) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Split offset must not be greater than current text length");
      String var3 = this.getWholeText().substring(0, var1);
      String var4 = this.getWholeText().substring(var1);
      this.text(var3);
      TextNode var5 = new TextNode(var4, this.baseUri());
      if (this.parent() != null) {
         this.parent().addChildren(this.siblingIndex() + 1, var5);
      }

      return var5;
   }

   public String text() {
      return normaliseWhitespace(this.getWholeText());
   }

   public TextNode text(String var1) {
      this.text = var1;
      if (this.attributes != null) {
         this.attributes.put("text", var1);
      }

      return this;
   }

   public String toString() {
      return this.outerHtml();
   }
}
