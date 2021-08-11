package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
   protected static final byte ESCAPE_CHAR = 37;
   @Deprecated
   protected static final BitSet WWW_FORM_URL;
   private static final BitSet WWW_FORM_URL_SAFE = new BitSet(256);
   @Deprecated
   protected volatile String charset;

   static {
      int var0;
      for(var0 = 97; var0 <= 122; ++var0) {
         WWW_FORM_URL_SAFE.set(var0);
      }

      for(var0 = 65; var0 <= 90; ++var0) {
         WWW_FORM_URL_SAFE.set(var0);
      }

      for(var0 = 48; var0 <= 57; ++var0) {
         WWW_FORM_URL_SAFE.set(var0);
      }

      WWW_FORM_URL_SAFE.set(45);
      WWW_FORM_URL_SAFE.set(95);
      WWW_FORM_URL_SAFE.set(46);
      WWW_FORM_URL_SAFE.set(42);
      WWW_FORM_URL_SAFE.set(32);
      WWW_FORM_URL = (BitSet)WWW_FORM_URL_SAFE.clone();
   }

   public URLCodec() {
      this("UTF-8");
   }

   public URLCodec(String var1) {
      this.charset = var1;
   }

   public static final byte[] decodeUrl(byte[] var0) throws DecoderException {
      if (var0 == null) {
         return null;
      } else {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();

         for(int var1 = 0; var1 < var0.length; ++var1) {
            byte var2 = var0[var1];
            if (var2 == 43) {
               var3.write(32);
            } else if (var2 == 37) {
               ++var1;

               ArrayIndexOutOfBoundsException var10000;
               label49: {
                  boolean var10001;
                  int var7;
                  try {
                     var7 = Utils.digit16(var0[var1]);
                  } catch (ArrayIndexOutOfBoundsException var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label49;
                  }

                  ++var1;

                  try {
                     var3.write((char)((var7 << 4) + Utils.digit16(var0[var1])));
                     continue;
                  } catch (ArrayIndexOutOfBoundsException var4) {
                     var10000 = var4;
                     var10001 = false;
                  }
               }

               ArrayIndexOutOfBoundsException var6 = var10000;
               throw new DecoderException("Invalid URL encoding: ", var6);
            } else {
               var3.write(var2);
            }
         }

         return var3.toByteArray();
      }
   }

   public static final byte[] encodeUrl(BitSet var0, byte[] var1) {
      if (var1 == null) {
         return null;
      } else {
         BitSet var6 = var0;
         if (var0 == null) {
            var6 = WWW_FORM_URL_SAFE;
         }

         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         int var5 = var1.length;

         for(int var3 = 0; var3 < var5; ++var3) {
            byte var4 = var1[var3];
            int var2 = var4;
            if (var4 < 0) {
               var2 = var4 + 256;
            }

            if (var6.get(var2)) {
               int var9 = var2;
               if (var2 == 32) {
                  var9 = 43;
               }

               var7.write(var9);
            } else {
               var7.write(37);
               char var10 = Utils.hexDigit(var2 >> 4);
               char var8 = Utils.hexDigit(var2);
               var7.write(var10);
               var7.write(var8);
            }
         }

         return var7.toByteArray();
      }
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof byte[]) {
         return this.decode((byte[])((byte[])var1));
      } else if (var1 instanceof String) {
         return this.decode((String)var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Objects of type ");
         var2.append(var1.getClass().getName());
         var2.append(" cannot be URL decoded");
         throw new DecoderException(var2.toString());
      }
   }

   public String decode(String var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else {
         try {
            var1 = this.decode(var1, this.getDefaultCharset());
            return var1;
         } catch (UnsupportedEncodingException var2) {
            throw new DecoderException(var2.getMessage(), var2);
         }
      }
   }

   public String decode(String var1, String var2) throws DecoderException, UnsupportedEncodingException {
      return var1 == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(var1)), var2);
   }

   public byte[] decode(byte[] var1) throws DecoderException {
      return decodeUrl(var1);
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof byte[]) {
         return this.encode((byte[])((byte[])var1));
      } else if (var1 instanceof String) {
         return this.encode((String)var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Objects of type ");
         var2.append(var1.getClass().getName());
         var2.append(" cannot be URL encoded");
         throw new EncoderException(var2.toString());
      }
   }

   public String encode(String var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else {
         try {
            var1 = this.encode(var1, this.getDefaultCharset());
            return var1;
         } catch (UnsupportedEncodingException var2) {
            throw new EncoderException(var2.getMessage(), var2);
         }
      }
   }

   public String encode(String var1, String var2) throws UnsupportedEncodingException {
      return var1 == null ? null : StringUtils.newStringUsAscii(this.encode(var1.getBytes(var2)));
   }

   public byte[] encode(byte[] var1) {
      return encodeUrl(WWW_FORM_URL_SAFE, var1);
   }

   public String getDefaultCharset() {
      return this.charset;
   }

   @Deprecated
   public String getEncoding() {
      return this.charset;
   }
}
