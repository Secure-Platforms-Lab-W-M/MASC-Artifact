package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;

public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
   private final Charset charset;

   public BCodec() {
      this(StandardCharsets.UTF_8);
   }

   public BCodec(String var1) {
      this(Charset.forName(var1));
   }

   public BCodec(Charset var1) {
      this.charset = var1;
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof String) {
         return this.decode((String)var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Objects of type ");
         var2.append(var1.getClass().getName());
         var2.append(" cannot be decoded using BCodec");
         throw new DecoderException(var2.toString());
      }
   }

   public String decode(String var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else {
         Object var4;
         try {
            var1 = this.decodeText(var1);
            return var1;
         } catch (UnsupportedEncodingException var2) {
            var4 = var2;
         } catch (IllegalArgumentException var3) {
            var4 = var3;
         }

         throw new DecoderException(((Exception)var4).getMessage(), (Throwable)var4);
      }
   }

   protected byte[] doDecoding(byte[] var1) {
      return var1 == null ? null : Base64.decodeBase64(var1);
   }

   protected byte[] doEncoding(byte[] var1) {
      return var1 == null ? null : Base64.encodeBase64(var1);
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof String) {
         return this.encode((String)var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Objects of type ");
         var2.append(var1.getClass().getName());
         var2.append(" cannot be encoded using BCodec");
         throw new EncoderException(var2.toString());
      }
   }

   public String encode(String var1) throws EncoderException {
      return var1 == null ? null : this.encode(var1, this.getCharset());
   }

   public String encode(String var1, String var2) throws EncoderException {
      if (var1 == null) {
         return null;
      } else {
         try {
            var1 = this.encodeText(var1, var2);
            return var1;
         } catch (UnsupportedEncodingException var3) {
            throw new EncoderException(var3.getMessage(), var3);
         }
      }
   }

   public String encode(String var1, Charset var2) throws EncoderException {
      return var1 == null ? null : this.encodeText(var1, var2);
   }

   public Charset getCharset() {
      return this.charset;
   }

   public String getDefaultCharset() {
      return this.charset.name();
   }

   protected String getEncoding() {
      return "B";
   }
}
