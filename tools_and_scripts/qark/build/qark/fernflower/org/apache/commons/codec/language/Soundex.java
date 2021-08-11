package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Soundex implements StringEncoder {
   public static final char SILENT_MARKER = '-';
   public static final Soundex US_ENGLISH = new Soundex();
   public static final Soundex US_ENGLISH_GENEALOGY = new Soundex("-123-12--22455-12623-1-2-2");
   private static final char[] US_ENGLISH_MAPPING = "01230120022455012623010202".toCharArray();
   public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
   public static final Soundex US_ENGLISH_SIMPLIFIED = new Soundex("01230120022455012623010202", false);
   @Deprecated
   private int maxLength = 4;
   private final char[] soundexMapping;
   private final boolean specialCaseHW;

   public Soundex() {
      this.soundexMapping = US_ENGLISH_MAPPING;
      this.specialCaseHW = true;
   }

   public Soundex(String var1) {
      char[] var2 = var1.toCharArray();
      this.soundexMapping = var2;
      this.specialCaseHW = this.hasMarker(var2) ^ true;
   }

   public Soundex(String var1, boolean var2) {
      this.soundexMapping = var1.toCharArray();
      this.specialCaseHW = var2;
   }

   public Soundex(char[] var1) {
      char[] var2 = new char[var1.length];
      this.soundexMapping = var2;
      System.arraycopy(var1, 0, var2, 0, var1.length);
      this.specialCaseHW = this.hasMarker(this.soundexMapping) ^ true;
   }

   private boolean hasMarker(char[] var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var1[var2] == '-') {
            return true;
         }
      }

      return false;
   }

   private char map(char var1) {
      int var2 = var1 - 65;
      if (var2 >= 0) {
         char[] var3 = this.soundexMapping;
         if (var2 < var3.length) {
            return var3[var2];
         }
      }

      StringBuilder var4 = new StringBuilder();
      var4.append("The character is not mapped: ");
      var4.append(var1);
      var4.append(" (index=");
      var4.append(var2);
      var4.append(")");
      throw new IllegalArgumentException(var4.toString());
   }

   public int difference(String var1, String var2) throws EncoderException {
      return SoundexUtils.difference(this, var1, var2);
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.soundex((String)var1);
      } else {
         throw new EncoderException("Parameter supplied to Soundex encode is not of type java.lang.String");
      }
   }

   public String encode(String var1) {
      return this.soundex(var1);
   }

   @Deprecated
   public int getMaxLength() {
      return this.maxLength;
   }

   @Deprecated
   public void setMaxLength(int var1) {
      this.maxLength = var1;
   }

   public String soundex(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = SoundexUtils.clean(var1);
         if (var1.length() == 0) {
            return var1;
         } else {
            char[] var8 = new char[]{'0', '0', '0', '0'};
            char var2 = var1.charAt(0);
            int var3 = 0 + 1;
            var8[0] = var2;
            char var6 = this.map(var2);

            int var4;
            for(int var5 = 1; var5 < var1.length() && var3 < var8.length; var3 = var4) {
               char var7;
               label40: {
                  var2 = var1.charAt(var5);
                  if (this.specialCaseHW) {
                     var7 = var6;
                     var4 = var3;
                     if (var2 == 'H') {
                        break label40;
                     }

                     if (var2 == 'W') {
                        var7 = var6;
                        var4 = var3;
                        break label40;
                     }
                  }

                  var2 = this.map(var2);
                  if (var2 == '-') {
                     var7 = var6;
                     var4 = var3;
                  } else {
                     var4 = var3;
                     if (var2 != '0') {
                        var4 = var3;
                        if (var2 != var6) {
                           var8[var3] = var2;
                           var4 = var3 + 1;
                        }
                     }

                     var7 = var2;
                  }
               }

               ++var5;
               var6 = var7;
            }

            return new String(var8);
         }
      }
   }
}
