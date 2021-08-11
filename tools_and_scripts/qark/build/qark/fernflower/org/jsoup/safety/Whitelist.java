package org.jsoup.safety;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class Whitelist {
   private Map attributes = new HashMap();
   private Map enforcedAttributes = new HashMap();
   private boolean preserveRelativeLinks = false;
   private Map protocols = new HashMap();
   private Set tagNames = new HashSet();

   public static Whitelist basic() {
      return (new Whitelist()).addTags("a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub", "sup", "u", "ul").addAttributes("a", "href").addAttributes("blockquote", "cite").addAttributes("q", "cite").addProtocols("a", "href", "ftp", "http", "https", "mailto").addProtocols("blockquote", "cite", "http", "https").addProtocols("cite", "cite", "http", "https").addEnforcedAttribute("a", "rel", "nofollow");
   }

   public static Whitelist basicWithImages() {
      return basic().addTags("img").addAttributes("img", "align", "alt", "height", "src", "title", "width").addProtocols("img", "src", "http", "https");
   }

   private boolean isValidAnchor(String var1) {
      return var1.startsWith("#") && !var1.matches(".*\\s.*");
   }

   public static Whitelist none() {
      return new Whitelist();
   }

   public static Whitelist relaxed() {
      return (new Whitelist()).addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul").addAttributes("a", "href", "title").addAttributes("blockquote", "cite").addAttributes("col", "span", "width").addAttributes("colgroup", "span", "width").addAttributes("img", "align", "alt", "height", "src", "title", "width").addAttributes("ol", "start", "type").addAttributes("q", "cite").addAttributes("table", "summary", "width").addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width").addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope", "width").addAttributes("ul", "type").addProtocols("a", "href", "ftp", "http", "https", "mailto").addProtocols("blockquote", "cite", "http", "https").addProtocols("cite", "cite", "http", "https").addProtocols("img", "src", "http", "https").addProtocols("q", "cite", "http", "https");
   }

   public static Whitelist simpleText() {
      return (new Whitelist()).addTags("b", "em", "i", "strong", "u");
   }

   private boolean testValidProtocol(Element var1, Attribute var2, Set var3) {
      String var4 = var1.absUrl(var2.getKey());
      String var5 = var4;
      if (var4.length() == 0) {
         var5 = var2.getValue();
      }

      if (!this.preserveRelativeLinks) {
         var2.setValue(var5);
      }

      Iterator var6 = var3.iterator();

      while(var6.hasNext()) {
         String var7 = ((Whitelist.Protocol)var6.next()).toString();
         if (var7.equals("#")) {
            if (this.isValidAnchor(var5)) {
               return true;
            }
         } else {
            var7 = var7 + ":";
            if (var5.toLowerCase().startsWith(var7)) {
               return true;
            }
         }
      }

      return false;
   }

   public Whitelist addAttributes(String var1, String... var2) {
      int var3 = 0;
      Validate.notEmpty(var1);
      Validate.notNull(var2);
      boolean var5;
      if (var2.length > 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "No attributes supplied.");
      Whitelist.TagName var8 = Whitelist.TagName.valueOf(var1);
      if (!this.tagNames.contains(var8)) {
         this.tagNames.add(var8);
      }

      HashSet var6 = new HashSet();

      for(int var4 = var2.length; var3 < var4; ++var3) {
         String var7 = var2[var3];
         Validate.notEmpty(var7);
         var6.add(Whitelist.AttributeKey.valueOf(var7));
      }

      if (this.attributes.containsKey(var8)) {
         ((Set)this.attributes.get(var8)).addAll(var6);
         return this;
      } else {
         this.attributes.put(var8, var6);
         return this;
      }
   }

   public Whitelist addEnforcedAttribute(String var1, String var2, String var3) {
      Validate.notEmpty(var1);
      Validate.notEmpty(var2);
      Validate.notEmpty(var3);
      Whitelist.TagName var5 = Whitelist.TagName.valueOf(var1);
      if (!this.tagNames.contains(var5)) {
         this.tagNames.add(var5);
      }

      Whitelist.AttributeKey var6 = Whitelist.AttributeKey.valueOf(var2);
      Whitelist.AttributeValue var7 = Whitelist.AttributeValue.valueOf(var3);
      if (this.enforcedAttributes.containsKey(var5)) {
         ((Map)this.enforcedAttributes.get(var5)).put(var6, var7);
         return this;
      } else {
         HashMap var4 = new HashMap();
         var4.put(var6, var7);
         this.enforcedAttributes.put(var5, var4);
         return this;
      }
   }

   public Whitelist addProtocols(String var1, String var2, String... var3) {
      Validate.notEmpty(var1);
      Validate.notEmpty(var2);
      Validate.notNull(var3);
      Whitelist.TagName var7 = Whitelist.TagName.valueOf(var1);
      Whitelist.AttributeKey var6 = Whitelist.AttributeKey.valueOf(var2);
      Object var8;
      if (this.protocols.containsKey(var7)) {
         var8 = (Map)this.protocols.get(var7);
      } else {
         var8 = new HashMap();
         this.protocols.put(var7, var8);
      }

      if (((Map)var8).containsKey(var6)) {
         var8 = (Set)((Map)var8).get(var6);
      } else {
         HashSet var9 = new HashSet();
         ((Map)var8).put(var6, var9);
         var8 = var9;
      }

      int var5 = var3.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         var2 = var3[var4];
         Validate.notEmpty(var2);
         ((Set)var8).add(Whitelist.Protocol.valueOf(var2));
      }

      return this;
   }

   public Whitelist addTags(String... var1) {
      Validate.notNull(var1);
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String var4 = var1[var2];
         Validate.notEmpty(var4);
         this.tagNames.add(Whitelist.TagName.valueOf(var4));
      }

      return this;
   }

   Attributes getEnforcedAttributes(String var1) {
      Attributes var2 = new Attributes();
      Whitelist.TagName var4 = Whitelist.TagName.valueOf(var1);
      if (this.enforcedAttributes.containsKey(var4)) {
         Iterator var5 = ((Map)this.enforcedAttributes.get(var4)).entrySet().iterator();

         while(var5.hasNext()) {
            Entry var3 = (Entry)var5.next();
            var2.put(((Whitelist.AttributeKey)var3.getKey()).toString(), ((Whitelist.AttributeValue)var3.getValue()).toString());
         }
      }

      return var2;
   }

   protected boolean isSafeAttribute(String var1, Element var2, Attribute var3) {
      boolean var4 = true;
      Whitelist.TagName var6 = Whitelist.TagName.valueOf(var1);
      Whitelist.AttributeKey var5 = Whitelist.AttributeKey.valueOf(var3.getKey());
      if (this.attributes.containsKey(var6) && ((Set)this.attributes.get(var6)).contains(var5)) {
         if (this.protocols.containsKey(var6)) {
            Map var7 = (Map)this.protocols.get(var6);
            if (var7.containsKey(var5) && !this.testValidProtocol(var2, var3, (Set)var7.get(var5))) {
               var4 = false;
            } else {
               var4 = true;
            }
         }
      } else if (var1.equals(":all") || !this.isSafeAttribute(":all", var2, var3)) {
         return false;
      }

      return var4;
   }

   protected boolean isSafeTag(String var1) {
      return this.tagNames.contains(Whitelist.TagName.valueOf(var1));
   }

   public Whitelist preserveRelativeLinks(boolean var1) {
      this.preserveRelativeLinks = var1;
      return this;
   }

   public Whitelist removeAttributes(String var1, String... var2) {
      int var3 = 0;
      Validate.notEmpty(var1);
      Validate.notNull(var2);
      boolean var5;
      if (var2.length > 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "No attributes supplied.");
      Whitelist.TagName var7 = Whitelist.TagName.valueOf(var1);
      HashSet var6 = new HashSet();

      for(int var4 = var2.length; var3 < var4; ++var3) {
         String var8 = var2[var3];
         Validate.notEmpty(var8);
         var6.add(Whitelist.AttributeKey.valueOf(var8));
      }

      if (this.tagNames.contains(var7) && this.attributes.containsKey(var7)) {
         Set var10 = (Set)this.attributes.get(var7);
         var10.removeAll(var6);
         if (var10.isEmpty()) {
            this.attributes.remove(var7);
         }
      }

      if (var1.equals(":all")) {
         Iterator var9 = this.attributes.keySet().iterator();

         while(var9.hasNext()) {
            Whitelist.TagName var11 = (Whitelist.TagName)var9.next();
            Set var12 = (Set)this.attributes.get(var11);
            var12.removeAll(var6);
            if (var12.isEmpty()) {
               this.attributes.remove(var11);
            }
         }
      }

      return this;
   }

   public Whitelist removeEnforcedAttribute(String var1, String var2) {
      Validate.notEmpty(var1);
      Validate.notEmpty(var2);
      Whitelist.TagName var4 = Whitelist.TagName.valueOf(var1);
      if (this.tagNames.contains(var4) && this.enforcedAttributes.containsKey(var4)) {
         Whitelist.AttributeKey var5 = Whitelist.AttributeKey.valueOf(var2);
         Map var3 = (Map)this.enforcedAttributes.get(var4);
         var3.remove(var5);
         if (var3.isEmpty()) {
            this.enforcedAttributes.remove(var4);
         }
      }

      return this;
   }

   public Whitelist removeProtocols(String var1, String var2, String... var3) {
      Validate.notEmpty(var1);
      Validate.notEmpty(var2);
      Validate.notNull(var3);
      Whitelist.TagName var9 = Whitelist.TagName.valueOf(var1);
      Whitelist.AttributeKey var10 = Whitelist.AttributeKey.valueOf(var2);
      if (this.protocols.containsKey(var9)) {
         Map var6 = (Map)this.protocols.get(var9);
         if (var6.containsKey(var10)) {
            Set var7 = (Set)var6.get(var10);
            int var5 = var3.length;

            for(int var4 = 0; var4 < var5; ++var4) {
               String var8 = var3[var4];
               Validate.notEmpty(var8);
               var7.remove(Whitelist.Protocol.valueOf(var8));
            }

            if (var7.isEmpty()) {
               var6.remove(var10);
               if (var6.isEmpty()) {
                  this.protocols.remove(var9);
               }
            }
         }
      }

      return this;
   }

   public Whitelist removeTags(String... var1) {
      Validate.notNull(var1);
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         String var4 = var1[var2];
         Validate.notEmpty(var4);
         Whitelist.TagName var5 = Whitelist.TagName.valueOf(var4);
         if (this.tagNames.remove(var5)) {
            this.attributes.remove(var5);
            this.enforcedAttributes.remove(var5);
            this.protocols.remove(var5);
         }
      }

      return this;
   }

   static class AttributeKey extends Whitelist.TypedValue {
      AttributeKey(String var1) {
         super(var1);
      }

      static Whitelist.AttributeKey valueOf(String var0) {
         return new Whitelist.AttributeKey(var0);
      }
   }

   static class AttributeValue extends Whitelist.TypedValue {
      AttributeValue(String var1) {
         super(var1);
      }

      static Whitelist.AttributeValue valueOf(String var0) {
         return new Whitelist.AttributeValue(var0);
      }
   }

   static class Protocol extends Whitelist.TypedValue {
      Protocol(String var1) {
         super(var1);
      }

      static Whitelist.Protocol valueOf(String var0) {
         return new Whitelist.Protocol(var0);
      }
   }

   static class TagName extends Whitelist.TypedValue {
      TagName(String var1) {
         super(var1);
      }

      static Whitelist.TagName valueOf(String var0) {
         return new Whitelist.TagName(var0);
      }
   }

   abstract static class TypedValue {
      private String value;

      TypedValue(String var1) {
         Validate.notNull(var1);
         this.value = var1;
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (var1 == null) {
               return false;
            }

            if (this.getClass() != var1.getClass()) {
               return false;
            }

            Whitelist.TypedValue var2 = (Whitelist.TypedValue)var1;
            if (this.value == null) {
               if (var2.value != null) {
                  return false;
               }
            } else if (!this.value.equals(var2.value)) {
               return false;
            }
         }

         return true;
      }

      public int hashCode() {
         int var1;
         if (this.value == null) {
            var1 = 0;
         } else {
            var1 = this.value.hashCode();
         }

         return var1 + 31;
      }

      public String toString() {
         return this.value;
      }
   }
}
