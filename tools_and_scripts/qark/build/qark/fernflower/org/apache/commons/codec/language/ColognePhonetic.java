package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder {
   private static final char[] AEIJOUY = new char[]{'A', 'E', 'I', 'J', 'O', 'U', 'Y'};
   private static final char[] AHKLOQRUX = new char[]{'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'};
   private static final char[] AHKOQUX = new char[]{'A', 'H', 'K', 'O', 'Q', 'U', 'X'};
   private static final char CHAR_IGNORE = '-';
   private static final char[] CKQ = new char[]{'C', 'K', 'Q'};
   private static final char[] CSZ = new char[]{'C', 'S', 'Z'};
   private static final char[] DTX = new char[]{'D', 'T', 'X'};
   private static final char[] FPVW = new char[]{'F', 'P', 'V', 'W'};
   private static final char[] GKQ = new char[]{'G', 'K', 'Q'};
   // $FF: renamed from: SZ char[]
   private static final char[] field_170 = new char[]{'S', 'Z'};

   private static boolean arrayContains(char[] var0, char var1) {
      int var3 = var0.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var0[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   private char[] preprocess(String var1) {
      char[] var4 = var1.toUpperCase(Locale.GERMAN).toCharArray();

      for(int var2 = 0; var2 < var4.length; ++var2) {
         char var3 = var4[var2];
         if (var3 != 196) {
            if (var3 != 214) {
               if (var3 == 220) {
                  var4[var2] = 'U';
               }
            } else {
               var4[var2] = 'O';
            }
         } else {
            var4[var2] = 'A';
         }
      }

      return var4;
   }

   public String colognePhonetic(String var1) {
      if (var1 == null) {
         return null;
      } else {
         ColognePhonetic.CologneInputBuffer var6 = new ColognePhonetic.CologneInputBuffer(this.preprocess(var1));
         ColognePhonetic.CologneOutputBuffer var5 = new ColognePhonetic.CologneOutputBuffer(var6.length() * 2);
         char var2 = '-';

         while(true) {
            char var3;
            char var4;
            do {
               do {
                  if (var6.length() <= 0) {
                     return var5.toString();
                  }

                  var4 = var6.removeNext();
                  if (var6.length() > 0) {
                     var3 = var6.getNextChar();
                  } else {
                     var3 = '-';
                  }
               } while(var4 < 'A');
            } while(var4 > 'Z');

            if (arrayContains(AEIJOUY, var4)) {
               var5.put('0');
            } else if (var4 != 'B' && (var4 != 'P' || var3 == 'H')) {
               if ((var4 == 'D' || var4 == 'T') && !arrayContains(CSZ, var3)) {
                  var5.put('2');
               } else if (arrayContains(FPVW, var4)) {
                  var5.put('3');
               } else if (arrayContains(GKQ, var4)) {
                  var5.put('4');
               } else if (var4 == 'X' && !arrayContains(CKQ, var2)) {
                  var5.put('4');
                  var5.put('8');
               } else if (var4 != 'S' && var4 != 'Z') {
                  if (var4 == 'C') {
                     if (var5.length() == 0) {
                        if (arrayContains(AHKLOQRUX, var3)) {
                           var5.put('4');
                        } else {
                           var5.put('8');
                        }
                     } else if (!arrayContains(field_170, var2) && arrayContains(AHKOQUX, var3)) {
                        var5.put('4');
                     } else {
                        var5.put('8');
                     }
                  } else if (arrayContains(DTX, var4)) {
                     var5.put('8');
                  } else if (var4 == 'R') {
                     var5.put('7');
                  } else if (var4 == 'L') {
                     var5.put('5');
                  } else if (var4 != 'M' && var4 != 'N') {
                     if (var4 == 'H') {
                        var5.put('-');
                     }
                  } else {
                     var5.put('6');
                  }
               } else {
                  var5.put('8');
               }
            } else {
               var5.put('1');
            }

            var2 = var4;
         }
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.encode((String)var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("This method's parameter was expected to be of the type ");
         var2.append(String.class.getName());
         var2.append(". But actually it was of the type ");
         var2.append(var1.getClass().getName());
         var2.append(".");
         throw new EncoderException(var2.toString());
      }
   }

   public String encode(String var1) {
      return this.colognePhonetic(var1);
   }

   public boolean isEncodeEqual(String var1, String var2) {
      return this.colognePhonetic(var1).equals(this.colognePhonetic(var2));
   }

   private abstract class CologneBuffer {
      protected final char[] data;
      protected int length = 0;

      public CologneBuffer(int var2) {
         this.data = new char[var2];
         this.length = 0;
      }

      public CologneBuffer(char[] var2) {
         this.data = var2;
         this.length = var2.length;
      }

      protected abstract char[] copyData(int var1, int var2);

      public int length() {
         return this.length;
      }

      public String toString() {
         return new String(this.copyData(0, this.length));
      }
   }

   private class CologneInputBuffer extends ColognePhonetic.CologneBuffer {
      public CologneInputBuffer(char[] var2) {
         super(var2);
      }

      protected char[] copyData(int var1, int var2) {
         char[] var3 = new char[var2];
         System.arraycopy(this.data, this.data.length - this.length + var1, var3, 0, var2);
         return var3;
      }

      public char getNextChar() {
         return this.data[this.getNextPos()];
      }

      protected int getNextPos() {
         return this.data.length - this.length;
      }

      public char removeNext() {
         char var1 = this.getNextChar();
         --this.length;
         return var1;
      }
   }

   private class CologneOutputBuffer extends ColognePhonetic.CologneBuffer {
      private char lastCode = '/';

      public CologneOutputBuffer(int var2) {
         super(var2);
      }

      protected char[] copyData(int var1, int var2) {
         char[] var3 = new char[var2];
         System.arraycopy(this.data, var1, var3, 0, var2);
         return var3;
      }

      public void put(char var1) {
         if (var1 != '-' && this.lastCode != var1 && (var1 != '0' || this.length == 0)) {
            this.data[this.length] = var1;
            ++this.length;
         }

         this.lastCode = var1;
      }
   }
}
