package org.apache.commons.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Locale.Category;
import org.apache.commons.text.matcher.StringMatcherFactory;

public class ExtendedMessageFormat extends MessageFormat {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   private static final String DUMMY_PATTERN = "";
   private static final char END_FE = '}';
   private static final int HASH_SEED = 31;
   private static final char QUOTE = '\'';
   private static final char START_FE = '{';
   private static final char START_FMT = ',';
   private static final long serialVersionUID = -2362048321261811743L;
   private final Map registry;
   private String toPattern;

   public ExtendedMessageFormat(String var1) {
      this(var1, Locale.getDefault(Category.FORMAT));
   }

   public ExtendedMessageFormat(String var1, Locale var2) {
      this(var1, var2, (Map)null);
   }

   public ExtendedMessageFormat(String var1, Locale var2, Map var3) {
      super("");
      this.setLocale(var2);
      this.registry = var3;
      this.applyPattern(var1);
   }

   public ExtendedMessageFormat(String var1, Map var2) {
      this(var1, Locale.getDefault(Category.FORMAT), var2);
   }

   private StringBuilder appendQuotedString(String var1, ParsePosition var2, StringBuilder var3) {
      if (var3 != null) {
         var3.append('\'');
      }

      this.next(var2);
      int var5 = var2.getIndex();
      char[] var6 = var1.toCharArray();

      for(int var4 = var2.getIndex(); var4 < var1.length(); ++var4) {
         if (var6[var2.getIndex()] == '\'') {
            this.next(var2);
            if (var3 == null) {
               return null;
            }

            var3.append(var6, var5, var2.getIndex() - var5);
            return var3;
         }

         this.next(var2);
      }

      StringBuilder var7 = new StringBuilder();
      var7.append("Unterminated quoted string at position ");
      var7.append(var5);
      throw new IllegalArgumentException(var7.toString());
   }

   private boolean containsElements(Collection var1) {
      if (var1 != null) {
         if (var1.isEmpty()) {
            return false;
         } else {
            Iterator var2 = var1.iterator();

            do {
               if (!var2.hasNext()) {
                  return false;
               }
            } while(var2.next() == null);

            return true;
         }
      } else {
         return false;
      }
   }

   private Format getFormat(String var1) {
      if (this.registry != null) {
         String var3 = var1;
         String var4 = null;
         int var2 = var1.indexOf(44);
         if (var2 > 0) {
            var3 = var1.substring(0, var2).trim();
            var4 = var1.substring(var2 + 1).trim();
         }

         FormatFactory var5 = (FormatFactory)this.registry.get(var3);
         if (var5 != null) {
            return var5.getFormat(var3, var4, this.getLocale());
         }
      }

      return null;
   }

   private void getQuotedString(String var1, ParsePosition var2) {
      this.appendQuotedString(var1, var2, (StringBuilder)null);
   }

   private String insertFormats(String var1, ArrayList var2) {
      if (!this.containsElements(var2)) {
         return var1;
      } else {
         StringBuilder var8 = new StringBuilder(var1.length() * 2);
         ParsePosition var9 = new ParsePosition(0);
         int var5 = -1;

         int var6;
         for(int var4 = 0; var9.getIndex() < var1.length(); var5 = var6) {
            char var3 = var1.charAt(var9.getIndex());
            if (var3 != '\'') {
               if (var3 != '{') {
                  if (var3 == '}') {
                     --var4;
                  }

                  var8.append(var3);
                  this.next(var9);
                  var6 = var5;
               } else {
                  int var7 = var4 + 1;
                  var8.append('{');
                  var8.append(this.readArgumentIndex(var1, this.next(var9)));
                  var6 = var5;
                  var4 = var7;
                  if (var7 == 1) {
                     var6 = var5 + 1;
                     String var10 = (String)var2.get(var6);
                     if (var10 != null) {
                        var8.append(',');
                        var8.append(var10);
                     }

                     var4 = var7;
                  }
               }
            } else {
               this.appendQuotedString(var1, var9, var8);
               var6 = var5;
            }
         }

         return var8.toString();
      }
   }

   private ParsePosition next(ParsePosition var1) {
      var1.setIndex(var1.getIndex() + 1);
      return var1;
   }

   private String parseFormatDescription(String var1, ParsePosition var2) {
      int var4 = var2.getIndex();
      this.seekNonWs(var1, var2);
      int var5 = var2.getIndex();
      int var3 = 1;

      while(var2.getIndex() < var1.length()) {
         char var6 = var1.charAt(var2.getIndex());
         if (var6 != '\'') {
            if (var6 != '{') {
               if (var6 != '}') {
                  this.next(var2);
               } else {
                  --var3;
                  if (var3 == 0) {
                     return var1.substring(var5, var2.getIndex());
                  }

                  this.next(var2);
               }
            } else {
               ++var3;
               this.next(var2);
            }
         } else {
            this.getQuotedString(var1, var2);
         }
      }

      StringBuilder var7 = new StringBuilder();
      var7.append("Unterminated format element at position ");
      var7.append(var4);
      throw new IllegalArgumentException(var7.toString());
   }

   private int readArgumentIndex(String var1, ParsePosition var2) {
      int var6 = var2.getIndex();
      this.seekNonWs(var1, var2);
      StringBuilder var7 = new StringBuilder();

      boolean var5;
      for(var5 = false; !var5 && var2.getIndex() < var1.length(); this.next(var2)) {
         char var4 = var1.charAt(var2.getIndex());
         char var3 = var4;
         if (Character.isWhitespace(var4)) {
            this.seekNonWs(var1, var2);
            var4 = var1.charAt(var2.getIndex());
            var3 = var4;
            if (var4 != ',') {
               var3 = var4;
               if (var4 != '}') {
                  var5 = true;
                  continue;
               }
            }
         }

         if ((var3 == ',' || var3 == '}') && var7.length() > 0) {
            try {
               int var11 = Integer.parseInt(var7.toString());
               return var11;
            } catch (NumberFormatException var9) {
            }
         }

         var5 = Character.isDigit(var3) ^ true;
         var7.append(var3);
      }

      if (var5) {
         var7 = new StringBuilder();
         var7.append("Invalid format argument index at position ");
         var7.append(var6);
         var7.append(": ");
         var7.append(var1.substring(var6, var2.getIndex()));
         throw new IllegalArgumentException(var7.toString());
      } else {
         StringBuilder var10 = new StringBuilder();
         var10.append("Unterminated format element at position ");
         var10.append(var6);
         throw new IllegalArgumentException(var10.toString());
      }
   }

   private void seekNonWs(String var1, ParsePosition var2) {
      char[] var4 = var1.toCharArray();

      int var3;
      do {
         var3 = StringMatcherFactory.INSTANCE.splitMatcher().isMatch(var4, var2.getIndex(), 0, var4.length);
         var2.setIndex(var2.getIndex() + var3);
      } while(var3 > 0 && var2.getIndex() < var1.length());

   }

   public final void applyPattern(String var1) {
      if (this.registry == null) {
         super.applyPattern(var1);
         this.toPattern = super.toPattern();
      } else {
         ArrayList var9 = new ArrayList();
         ArrayList var10 = new ArrayList();
         StringBuilder var11 = new StringBuilder(var1.length());
         ParsePosition var12 = new ParsePosition(0);
         char[] var13 = var1.toCharArray();
         int var2 = 0;

         Format var6;
         while(var12.getIndex() < var1.length()) {
            char var3 = var13[var12.getIndex()];
            if (var3 != '\'') {
               if (var3 == '{') {
                  ++var2;
                  this.seekNonWs(var1, var12);
                  int var16 = var12.getIndex();
                  int var4 = this.readArgumentIndex(var1, this.next(var12));
                  var11.append('{');
                  var11.append(var4);
                  this.seekNonWs(var1, var12);
                  var6 = null;
                  String var5 = null;
                  if (var13[var12.getIndex()] == ',') {
                     String var7 = this.parseFormatDescription(var1, this.next(var12));
                     Format var8 = this.getFormat(var7);
                     var6 = var8;
                     var5 = var7;
                     if (var8 == null) {
                        var11.append(',');
                        var11.append(var7);
                        var5 = var7;
                        var6 = var8;
                     }
                  }

                  var9.add(var6);
                  if (var6 == null) {
                     var5 = null;
                  }

                  var10.add(var5);
                  if (var9.size() != var2) {
                     throw new IllegalArgumentException("The validated expression is false");
                  }

                  if (var10.size() != var2) {
                     throw new IllegalArgumentException("The validated expression is false");
                  }

                  if (var13[var12.getIndex()] != '}') {
                     StringBuilder var14 = new StringBuilder();
                     var14.append("Unreadable format element at position ");
                     var14.append(var16);
                     throw new IllegalArgumentException(var14.toString());
                  }
               }

               var11.append(var13[var12.getIndex()]);
               this.next(var12);
            } else {
               this.appendQuotedString(var1, var12, var11);
            }
         }

         super.applyPattern(var11.toString());
         this.toPattern = this.insertFormats(super.toPattern(), var10);
         if (this.containsElements(var9)) {
            Format[] var15 = this.getFormats();
            var2 = 0;

            for(Iterator var17 = var9.iterator(); var17.hasNext(); ++var2) {
               var6 = (Format)var17.next();
               if (var6 != null) {
                  var15[var2] = var6;
               }
            }

            super.setFormats(var15);
         }

      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!Objects.equals(this.getClass(), var1.getClass())) {
         return false;
      } else {
         ExtendedMessageFormat var2 = (ExtendedMessageFormat)var1;
         if (!Objects.equals(this.toPattern, var2.toPattern)) {
            return false;
         } else {
            return !super.equals(var1) ? false : Objects.equals(this.registry, var2.registry);
         }
      }
   }

   public int hashCode() {
      return (super.hashCode() * 31 + Objects.hashCode(this.registry)) * 31 + Objects.hashCode(this.toPattern);
   }

   public void setFormat(int var1, Format var2) {
      throw new UnsupportedOperationException();
   }

   public void setFormatByArgumentIndex(int var1, Format var2) {
      throw new UnsupportedOperationException();
   }

   public void setFormats(Format[] var1) {
      throw new UnsupportedOperationException();
   }

   public void setFormatsByArgumentIndex(Format[] var1) {
      throw new UnsupportedOperationException();
   }

   public String toPattern() {
      return this.toPattern;
   }
}
