package org.apache.commons.text;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

public final class AlphabetConverter {
   private static final String ARROW = " -> ";
   private final int encodedLetterLength;
   private final Map encodedToOriginal;
   private final Map originalToEncoded;

   private AlphabetConverter(Map var1, Map var2, int var3) {
      this.originalToEncoded = var1;
      this.encodedToOriginal = var2;
      this.encodedLetterLength = var3;
   }

   private void addSingleEncoding(int var1, String var2, Collection var3, Iterator var4, Map var5) {
      if (var1 <= 0) {
         Integer var9;
         for(var9 = (Integer)var4.next(); var5.containsKey(var9); var9 = (Integer)var4.next()) {
            String var11 = codePointToString(var9);
            this.originalToEncoded.put(var9, var11);
            this.encodedToOriginal.put(var11, var11);
            if (!var4.hasNext()) {
               return;
            }
         }

         String var10 = codePointToString(var9);
         this.originalToEncoded.put(var9, var2);
         this.encodedToOriginal.put(var2, var10);
      } else {
         Iterator var7 = var3.iterator();

         while(true) {
            int var6;
            do {
               if (!var7.hasNext()) {
                  return;
               }

               var6 = (Integer)var7.next();
               if (!var4.hasNext()) {
                  return;
               }
            } while(var1 == this.encodedLetterLength && var5.containsKey(var6));

            StringBuilder var8 = new StringBuilder();
            var8.append(var2);
            var8.append(codePointToString(var6));
            this.addSingleEncoding(var1 - 1, var8.toString(), var3, var4, var5);
         }
      }
   }

   private static String codePointToString(int var0) {
      return Character.charCount(var0) == 1 ? String.valueOf((char)var0) : new String(Character.toChars(var0));
   }

   private static Integer[] convertCharsToIntegers(Character[] var0) {
      if (var0 != null && var0.length != 0) {
         Integer[] var2 = new Integer[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = Integer.valueOf(var0[var1]);
         }

         return var2;
      } else {
         return new Integer[0];
      }
   }

   public static AlphabetConverter createConverter(Integer[] var0, Integer[] var1, Integer[] var2) {
      LinkedHashSet var10 = new LinkedHashSet(Arrays.asList(var0));
      LinkedHashSet var7 = new LinkedHashSet(Arrays.asList(var1));
      LinkedHashSet var12 = new LinkedHashSet(Arrays.asList(var2));
      LinkedHashMap var5 = new LinkedHashMap();
      LinkedHashMap var6 = new LinkedHashMap();
      HashMap var15 = new HashMap();
      Iterator var8 = var12.iterator();

      int var3;
      StringBuilder var11;
      while(var8.hasNext()) {
         var3 = (Integer)var8.next();
         if (!var10.contains(var3)) {
            var11 = new StringBuilder();
            var11.append("Can not use 'do not encode' list because original alphabet does not contain '");
            var11.append(codePointToString(var3));
            var11.append("'");
            throw new IllegalArgumentException(var11.toString());
         }

         if (!var7.contains(var3)) {
            var11 = new StringBuilder();
            var11.append("Can not use 'do not encode' list because encoding alphabet does not contain '");
            var11.append(codePointToString(var3));
            var11.append("'");
            throw new IllegalArgumentException(var11.toString());
         }

         var15.put(var3, codePointToString(var3));
      }

      if (var7.size() >= var10.size()) {
         Iterator var17 = var7.iterator();
         var8 = var10.iterator();

         while(true) {
            while(var8.hasNext()) {
               var3 = (Integer)var8.next();
               String var9 = codePointToString(var3);
               if (var15.containsKey(var3)) {
                  var5.put(var3, var9);
                  var6.put(var9, var9);
               } else {
                  Integer var13;
                  for(var13 = (Integer)var17.next(); var12.contains(var13); var13 = (Integer)var17.next()) {
                  }

                  String var16 = codePointToString(var13);
                  var5.put(var3, var16);
                  var6.put(var16, var9);
               }
            }

            return new AlphabetConverter(var5, var6, 1);
         }
      } else if (var7.size() - var12.size() < 2) {
         var11 = new StringBuilder();
         var11.append("Must have at least two encoding characters (excluding those in the 'do not encode' list), but has ");
         var11.append(var7.size() - var12.size());
         throw new IllegalArgumentException(var11.toString());
      } else {
         int var4 = (var10.size() - var12.size()) / (var7.size() - var12.size());

         for(var3 = 1; var4 / var7.size() >= 1; ++var3) {
            var4 /= var7.size();
         }

         ++var3;
         AlphabetConverter var14 = new AlphabetConverter(var5, var6, var3);
         var14.addSingleEncoding(var3, "", var7, var10.iterator(), var15);
         return var14;
      }
   }

   public static AlphabetConverter createConverterFromChars(Character[] var0, Character[] var1, Character[] var2) {
      return createConverter(convertCharsToIntegers(var0), convertCharsToIntegers(var1), convertCharsToIntegers(var2));
   }

   public static AlphabetConverter createConverterFromMap(Map var0) {
      var0 = Collections.unmodifiableMap(var0);
      LinkedHashMap var3 = new LinkedHashMap();
      int var1 = 1;

      int var2;
      for(Iterator var4 = var0.entrySet().iterator(); var4.hasNext(); var1 = var2) {
         Entry var5 = (Entry)var4.next();
         String var6 = codePointToString((Integer)var5.getKey());
         var3.put(var5.getValue(), var6);
         var2 = var1;
         if (((String)var5.getValue()).length() > var1) {
            var2 = ((String)var5.getValue()).length();
         }
      }

      return new AlphabetConverter(var0, var3, var1);
   }

   public String decode(String var1) throws UnsupportedEncodingException {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder();
         int var2 = 0;

         while(var2 < var1.length()) {
            Integer var4 = var1.codePointAt(var2);
            String var5 = codePointToString(var4);
            if (var5.equals(this.originalToEncoded.get(var4))) {
               var3.append(var5);
               ++var2;
            } else {
               if (this.encodedLetterLength + var2 > var1.length()) {
                  var3 = new StringBuilder();
                  var3.append("Unexpected end of string while decoding ");
                  var3.append(var1);
                  throw new UnsupportedEncodingException(var3.toString());
               }

               String var6 = var1.substring(var2, this.encodedLetterLength + var2);
               var5 = (String)this.encodedToOriginal.get(var6);
               if (var5 == null) {
                  var3 = new StringBuilder();
                  var3.append("Unexpected string without decoding (");
                  var3.append(var6);
                  var3.append(") in ");
                  var3.append(var1);
                  throw new UnsupportedEncodingException(var3.toString());
               }

               var3.append(var5);
               var2 += this.encodedLetterLength;
            }
         }

         return var3.toString();
      }
   }

   public String encode(String var1) throws UnsupportedEncodingException {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var4 = new StringBuilder();

         int var3;
         for(int var2 = 0; var2 < var1.length(); var2 += Character.charCount(var3)) {
            var3 = var1.codePointAt(var2);
            String var5 = (String)this.originalToEncoded.get(var3);
            if (var5 == null) {
               var4 = new StringBuilder();
               var4.append("Couldn't find encoding for '");
               var4.append(codePointToString(var3));
               var4.append("' in ");
               var4.append(var1);
               throw new UnsupportedEncodingException(var4.toString());
            }

            var4.append(var5);
         }

         return var4.toString();
      }
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (!(var1 instanceof AlphabetConverter)) {
         return false;
      } else {
         AlphabetConverter var2 = (AlphabetConverter)var1;
         return this.originalToEncoded.equals(var2.originalToEncoded) && this.encodedToOriginal.equals(var2.encodedToOriginal) && this.encodedLetterLength == var2.encodedLetterLength;
      }
   }

   public int getEncodedCharLength() {
      return this.encodedLetterLength;
   }

   public Map getOriginalToEncoded() {
      return Collections.unmodifiableMap(this.originalToEncoded);
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.originalToEncoded, this.encodedToOriginal, this.encodedLetterLength});
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.originalToEncoded.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append(codePointToString((Integer)var3.getKey()));
         var1.append(" -> ");
         var1.append((String)var3.getValue());
         var1.append(System.lineSeparator());
      }

      return var1.toString();
   }
}
