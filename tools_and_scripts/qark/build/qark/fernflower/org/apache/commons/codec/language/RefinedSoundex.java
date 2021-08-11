package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class RefinedSoundex implements StringEncoder {
   public static final RefinedSoundex US_ENGLISH = new RefinedSoundex();
   private static final char[] US_ENGLISH_MAPPING = "01360240043788015936020505".toCharArray();
   public static final String US_ENGLISH_MAPPING_STRING = "01360240043788015936020505";
   private final char[] soundexMapping;

   public RefinedSoundex() {
      this.soundexMapping = US_ENGLISH_MAPPING;
   }

   public RefinedSoundex(String var1) {
      this.soundexMapping = var1.toCharArray();
   }

   public RefinedSoundex(char[] var1) {
      char[] var2 = new char[var1.length];
      this.soundexMapping = var2;
      System.arraycopy(var1, 0, var2, 0, var1.length);
   }

   public int difference(String var1, String var2) throws EncoderException {
      return SoundexUtils.difference(this, var1, var2);
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.soundex((String)var1);
      } else {
         throw new EncoderException("Parameter supplied to RefinedSoundex encode is not of type java.lang.String");
      }
   }

   public String encode(String var1) {
      return this.soundex(var1);
   }

   char getMappingCode(char var1) {
      return !Character.isLetter(var1) ? '\u0000' : this.soundexMapping[Character.toUpperCase(var1) - 65];
   }

   public String soundex(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = SoundexUtils.clean(var1);
         if (var1.length() == 0) {
            return var1;
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append(var1.charAt(0));
            char var4 = '*';

            for(int var3 = 0; var3 < var1.length(); ++var3) {
               char var2 = this.getMappingCode(var1.charAt(var3));
               if (var2 != var4) {
                  if (var2 != 0) {
                     var5.append(var2);
                  }

                  var4 = var2;
               }
            }

            return var5.toString();
         }
      }
   }
}
