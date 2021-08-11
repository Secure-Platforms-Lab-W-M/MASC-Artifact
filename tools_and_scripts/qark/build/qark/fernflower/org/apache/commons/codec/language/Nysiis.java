package org.apache.commons.codec.language;

import java.util.regex.Pattern;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Nysiis implements StringEncoder {
   private static final char[] CHARS_A = new char[]{'A'};
   private static final char[] CHARS_AF = new char[]{'A', 'F'};
   private static final char[] CHARS_C = new char[]{'C'};
   private static final char[] CHARS_FF = new char[]{'F', 'F'};
   private static final char[] CHARS_G = new char[]{'G'};
   private static final char[] CHARS_N = new char[]{'N'};
   private static final char[] CHARS_NN = new char[]{'N', 'N'};
   private static final char[] CHARS_S = new char[]{'S'};
   private static final char[] CHARS_SSS = new char[]{'S', 'S', 'S'};
   private static final Pattern PAT_DT_ETC = Pattern.compile("(DT|RT|RD|NT|ND)$");
   private static final Pattern PAT_EE_IE = Pattern.compile("(EE|IE)$");
   private static final Pattern PAT_K = Pattern.compile("^K");
   private static final Pattern PAT_KN = Pattern.compile("^KN");
   private static final Pattern PAT_MAC = Pattern.compile("^MAC");
   private static final Pattern PAT_PH_PF = Pattern.compile("^(PH|PF)");
   private static final Pattern PAT_SCH = Pattern.compile("^SCH");
   private static final char SPACE = ' ';
   private static final int TRUE_LENGTH = 6;
   private final boolean strict;

   public Nysiis() {
      this(true);
   }

   public Nysiis(boolean var1) {
      this.strict = var1;
   }

   private static boolean isVowel(char var0) {
      return var0 == 'A' || var0 == 'E' || var0 == 'I' || var0 == 'O' || var0 == 'U';
   }

   private static char[] transcodeRemaining(char var0, char var1, char var2, char var3) {
      if (var1 == 'E' && var2 == 'V') {
         return CHARS_AF;
      } else if (isVowel(var1)) {
         return CHARS_A;
      } else if (var1 == 'Q') {
         return CHARS_G;
      } else if (var1 == 'Z') {
         return CHARS_S;
      } else if (var1 == 'M') {
         return CHARS_N;
      } else if (var1 == 'K') {
         return var2 == 'N' ? CHARS_NN : CHARS_C;
      } else if (var1 == 'S' && var2 == 'C' && var3 == 'H') {
         return CHARS_SSS;
      } else if (var1 == 'P' && var2 == 'H') {
         return CHARS_FF;
      } else if (var1 == 'H' && (!isVowel(var0) || !isVowel(var2))) {
         return new char[]{var0};
      } else {
         return var1 == 'W' && isVowel(var0) ? new char[]{var0} : new char[]{var1};
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.nysiis((String)var1);
      } else {
         throw new EncoderException("Parameter supplied to Nysiis encode is not of type java.lang.String");
      }
   }

   public String encode(String var1) {
      return this.nysiis(var1);
   }

   public boolean isStrict() {
      return this.strict;
   }

   public String nysiis(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = SoundexUtils.clean(var1);
         if (var1.length() == 0) {
            return var1;
         } else {
            var1 = PAT_MAC.matcher(var1).replaceFirst("MCC");
            var1 = PAT_KN.matcher(var1).replaceFirst("NN");
            var1 = PAT_K.matcher(var1).replaceFirst("C");
            var1 = PAT_PH_PF.matcher(var1).replaceFirst("FF");
            var1 = PAT_SCH.matcher(var1).replaceFirst("SSS");
            var1 = PAT_EE_IE.matcher(var1).replaceFirst("Y");
            String var6 = PAT_DT_ETC.matcher(var1).replaceFirst("D");
            StringBuilder var8 = new StringBuilder(var6.length());
            var8.append(var6.charAt(0));
            char[] var11 = var6.toCharArray();
            int var5 = var11.length;

            for(int var4 = 1; var4 < var5; ++var4) {
               char var3 = ' ';
               char var2;
               if (var4 < var5 - 1) {
                  var2 = var11[var4 + 1];
               } else {
                  var2 = ' ';
               }

               if (var4 < var5 - 2) {
                  var3 = var11[var4 + 2];
               }

               char[] var7 = transcodeRemaining(var11[var4 - 1], var11[var4], var2, var3);
               System.arraycopy(var7, 0, var11, var4, var7.length);
               if (var11[var4] != var11[var4 - 1]) {
                  var8.append(var11[var4]);
               }
            }

            if (var8.length() > 1) {
               char var10 = var8.charAt(var8.length() - 1);
               char var9 = var10;
               if (var10 == 'S') {
                  var8.deleteCharAt(var8.length() - 1);
                  var9 = var8.charAt(var8.length() - 1);
               }

               if (var8.length() > 2 && var8.charAt(var8.length() - 2) == 'A' && var9 == 'Y') {
                  var8.deleteCharAt(var8.length() - 2);
               }

               if (var9 == 'A') {
                  var8.deleteCharAt(var8.length() - 1);
               }
            }

            var1 = var8.toString();
            if (this.isStrict()) {
               return var1.substring(0, Math.min(6, var1.length()));
            } else {
               return var1;
            }
         }
      }
   }
}
