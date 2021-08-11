package org.jsoup.parser;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.helper.Validate;

public class Tag {
   private static final String[] blockTags;
   private static final String[] emptyTags;
   private static final String[] formListedTags;
   private static final String[] formSubmitTags;
   private static final String[] formatAsInlineTags;
   private static final String[] inlineTags;
   private static final String[] preserveWhitespaceTags;
   private static final Map tags;
   private boolean canContainBlock = true;
   private boolean canContainInline = true;
   private boolean empty = false;
   private boolean formList = false;
   private boolean formSubmit = false;
   private boolean formatAsBlock = true;
   private boolean isBlock = true;
   private boolean preserveWhitespace = false;
   private boolean selfClosing = false;
   private String tagName;

   static {
      byte var1 = 0;
      tags = new HashMap();
      blockTags = new String[]{"html", "head", "body", "frameset", "script", "noscript", "style", "meta", "link", "title", "frame", "noframes", "section", "nav", "aside", "hgroup", "header", "footer", "p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "pre", "div", "blockquote", "hr", "address", "figure", "figcaption", "form", "fieldset", "ins", "del", "s", "dl", "dt", "dd", "li", "table", "caption", "thead", "tfoot", "tbody", "colgroup", "col", "tr", "th", "td", "video", "audio", "canvas", "details", "menu", "plaintext", "template", "article", "main", "svg", "math"};
      inlineTags = new String[]{"object", "base", "font", "tt", "i", "b", "u", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd", "var", "cite", "abbr", "time", "acronym", "mark", "ruby", "rt", "rp", "a", "img", "br", "wbr", "map", "q", "sub", "sup", "bdo", "iframe", "embed", "span", "input", "select", "textarea", "label", "button", "optgroup", "option", "legend", "datalist", "keygen", "output", "progress", "meter", "area", "param", "source", "track", "summary", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track", "data", "bdi"};
      emptyTags = new String[]{"meta", "link", "base", "frame", "img", "br", "wbr", "embed", "hr", "input", "keygen", "col", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track"};
      formatAsInlineTags = new String[]{"title", "a", "p", "h1", "h2", "h3", "h4", "h5", "h6", "pre", "address", "li", "th", "td", "script", "style", "ins", "del", "s"};
      preserveWhitespaceTags = new String[]{"pre", "plaintext", "title", "textarea"};
      formListedTags = new String[]{"button", "fieldset", "input", "keygen", "object", "output", "select", "textarea"};
      formSubmitTags = new String[]{"input", "keygen", "object", "select", "textarea"};
      String[] var3 = blockTags;
      int var2 = var3.length;

      int var0;
      for(var0 = 0; var0 < var2; ++var0) {
         register(new Tag(var3[var0]));
      }

      var3 = inlineTags;
      var2 = var3.length;

      Tag var4;
      for(var0 = 0; var0 < var2; ++var0) {
         var4 = new Tag(var3[var0]);
         var4.isBlock = false;
         var4.canContainBlock = false;
         var4.formatAsBlock = false;
         register(var4);
      }

      var3 = emptyTags;
      var2 = var3.length;

      String var5;
      for(var0 = 0; var0 < var2; ++var0) {
         var5 = var3[var0];
         var4 = (Tag)tags.get(var5);
         Validate.notNull(var4);
         var4.canContainBlock = false;
         var4.canContainInline = false;
         var4.empty = true;
      }

      var3 = formatAsInlineTags;
      var2 = var3.length;

      for(var0 = 0; var0 < var2; ++var0) {
         var5 = var3[var0];
         var4 = (Tag)tags.get(var5);
         Validate.notNull(var4);
         var4.formatAsBlock = false;
      }

      var3 = preserveWhitespaceTags;
      var2 = var3.length;

      for(var0 = 0; var0 < var2; ++var0) {
         var5 = var3[var0];
         var4 = (Tag)tags.get(var5);
         Validate.notNull(var4);
         var4.preserveWhitespace = true;
      }

      var3 = formListedTags;
      var2 = var3.length;

      for(var0 = 0; var0 < var2; ++var0) {
         var5 = var3[var0];
         var4 = (Tag)tags.get(var5);
         Validate.notNull(var4);
         var4.formList = true;
      }

      var3 = formSubmitTags;
      var2 = var3.length;

      for(var0 = var1; var0 < var2; ++var0) {
         var5 = var3[var0];
         var4 = (Tag)tags.get(var5);
         Validate.notNull(var4);
         var4.formSubmit = true;
      }

   }

   private Tag(String var1) {
      this.tagName = var1.toLowerCase();
   }

   public static boolean isKnownTag(String var0) {
      return tags.containsKey(var0);
   }

   private static void register(Tag var0) {
      tags.put(var0.tagName, var0);
   }

   public static Tag valueOf(String var0) {
      Validate.notNull(var0);
      Tag var2 = (Tag)tags.get(var0);
      Tag var1 = var2;
      if (var2 == null) {
         String var4 = var0.trim().toLowerCase();
         Validate.notEmpty(var4);
         Tag var3 = (Tag)tags.get(var4);
         var1 = var3;
         if (var3 == null) {
            var1 = new Tag(var4);
            var1.isBlock = false;
            var1.canContainBlock = true;
         }
      }

      return var1;
   }

   public boolean canContainBlock() {
      return this.canContainBlock;
   }

   public boolean equals(Object var1) {
      boolean var3 = true;
      boolean var4 = false;
      boolean var2;
      if (this == var1) {
         var2 = true;
      } else {
         var2 = var4;
         if (var1 instanceof Tag) {
            Tag var5 = (Tag)var1;
            var2 = var4;
            if (this.tagName.equals(var5.tagName)) {
               var2 = var4;
               if (this.canContainBlock == var5.canContainBlock) {
                  var2 = var4;
                  if (this.canContainInline == var5.canContainInline) {
                     var2 = var4;
                     if (this.empty == var5.empty) {
                        var2 = var4;
                        if (this.formatAsBlock == var5.formatAsBlock) {
                           var2 = var4;
                           if (this.isBlock == var5.isBlock) {
                              var2 = var4;
                              if (this.preserveWhitespace == var5.preserveWhitespace) {
                                 var2 = var4;
                                 if (this.selfClosing == var5.selfClosing) {
                                    var2 = var4;
                                    if (this.formList == var5.formList) {
                                       if (this.formSubmit == var5.formSubmit) {
                                          var2 = var3;
                                       } else {
                                          var2 = false;
                                       }

                                       return var2;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   public boolean formatAsBlock() {
      return this.formatAsBlock;
   }

   public String getName() {
      return this.tagName;
   }

   public int hashCode() {
      byte var9 = 1;
      int var10 = this.tagName.hashCode();
      byte var1;
      if (this.isBlock) {
         var1 = 1;
      } else {
         var1 = 0;
      }

      byte var2;
      if (this.formatAsBlock) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      byte var3;
      if (this.canContainBlock) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      byte var4;
      if (this.canContainInline) {
         var4 = 1;
      } else {
         var4 = 0;
      }

      byte var5;
      if (this.empty) {
         var5 = 1;
      } else {
         var5 = 0;
      }

      byte var6;
      if (this.selfClosing) {
         var6 = 1;
      } else {
         var6 = 0;
      }

      byte var7;
      if (this.preserveWhitespace) {
         var7 = 1;
      } else {
         var7 = 0;
      }

      byte var8;
      if (this.formList) {
         var8 = 1;
      } else {
         var8 = 0;
      }

      if (!this.formSubmit) {
         var9 = 0;
      }

      return ((((((((var10 * 31 + var1) * 31 + var2) * 31 + var3) * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var8) * 31 + var9;
   }

   public boolean isBlock() {
      return this.isBlock;
   }

   public boolean isData() {
      return !this.canContainInline && !this.isEmpty();
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public boolean isFormListed() {
      return this.formList;
   }

   public boolean isFormSubmittable() {
      return this.formSubmit;
   }

   public boolean isInline() {
      return !this.isBlock;
   }

   public boolean isKnownTag() {
      return tags.containsKey(this.tagName);
   }

   public boolean isSelfClosing() {
      return this.empty || this.selfClosing;
   }

   public boolean preserveWhitespace() {
      return this.preserveWhitespace;
   }

   Tag setSelfClosing() {
      this.selfClosing = true;
      return this;
   }

   public String toString() {
      return this.tagName;
   }
}
