package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

@Deprecated
public class Caverphone implements StringEncoder {
   private final Caverphone2 encoder = new Caverphone2();

   public String caverphone(String var1) {
      return this.encoder.encode(var1);
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof String) {
         return this.caverphone((String)var1);
      } else {
         throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String");
      }
   }

   public String encode(String var1) {
      return this.caverphone(var1);
   }

   public boolean isCaverphoneEqual(String var1, String var2) {
      return this.caverphone(var1).equals(this.caverphone(var2));
   }
}
