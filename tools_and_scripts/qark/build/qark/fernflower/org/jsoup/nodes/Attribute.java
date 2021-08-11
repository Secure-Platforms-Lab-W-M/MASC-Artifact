package org.jsoup.nodes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;

public class Attribute implements Entry, Cloneable {
   private static final String[] booleanAttributes = new String[]{"allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed", "seamless", "selected", "sortable", "truespeed", "typemustmatch"};
   private String key;
   private String value;

   public Attribute(String var1, String var2) {
      Validate.notEmpty(var1);
      Validate.notNull(var2);
      this.key = var1.trim().toLowerCase();
      this.value = var2;
   }

   public static Attribute createFromEncoded(String var0, String var1) {
      return new Attribute(var0, Entities.unescape(var1, true));
   }

   public Attribute clone() {
      try {
         Attribute var1 = (Attribute)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof Attribute)) {
            return false;
         } else {
            Attribute var2;
            label30: {
               var2 = (Attribute)var1;
               if (this.key != null) {
                  if (this.key.equals(var2.key)) {
                     break label30;
                  }
               } else if (var2.key == null) {
                  break label30;
               }

               return false;
            }

            if (this.value != null) {
               if (this.value.equals(var2.value)) {
                  return true;
               }
            } else if (var2.value == null) {
               return true;
            }

            return false;
         }
      } else {
         return true;
      }
   }

   public String getKey() {
      return this.key;
   }

   public String getValue() {
      return this.value;
   }

   public int hashCode() {
      int var2 = 0;
      int var1;
      if (this.key != null) {
         var1 = this.key.hashCode();
      } else {
         var1 = 0;
      }

      if (this.value != null) {
         var2 = this.value.hashCode();
      }

      return var1 * 31 + var2;
   }

   public String html() {
      StringBuilder var1 = new StringBuilder();

      try {
         this.html(var1, (new Document("")).outputSettings());
      } catch (IOException var2) {
         throw new SerializationException(var2);
      }

      return var1.toString();
   }

   protected void html(Appendable var1, Document.OutputSettings var2) throws IOException {
      var1.append(this.key);
      if (!this.shouldCollapseAttribute(var2)) {
         var1.append("=\"");
         Entities.escape(var1, this.value, var2, true, false, false);
         var1.append('"');
      }

   }

   protected boolean isBooleanAttribute() {
      return Arrays.binarySearch(booleanAttributes, this.key) >= 0;
   }

   protected boolean isDataAttribute() {
      return this.key.startsWith("data-") && this.key.length() > "data-".length();
   }

   public void setKey(String var1) {
      Validate.notEmpty(var1);
      this.key = var1.trim().toLowerCase();
   }

   public String setValue(String var1) {
      Validate.notNull(var1);
      String var2 = this.value;
      this.value = var1;
      return var2;
   }

   protected final boolean shouldCollapseAttribute(Document.OutputSettings var1) {
      return ("".equals(this.value) || this.value.equalsIgnoreCase(this.key)) && var1.syntax() == Document.OutputSettings.Syntax.html && this.isBooleanAttribute();
   }

   public String toString() {
      return this.html();
   }
}
