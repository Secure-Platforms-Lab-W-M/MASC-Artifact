package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;

public class QCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
   private static final BitSet PRINTABLE_CHARS;
   private static final byte SPACE = 32;
   private static final byte UNDERSCORE = 95;
   private final Charset charset;
   private boolean encodeBlanks;

   static {
      BitSet var1 = new BitSet(256);
      PRINTABLE_CHARS = var1;
      var1.set(32);
      PRINTABLE_CHARS.set(33);
      PRINTABLE_CHARS.set(34);
      PRINTABLE_CHARS.set(35);
      PRINTABLE_CHARS.set(36);
      PRINTABLE_CHARS.set(37);
      PRINTABLE_CHARS.set(38);
      PRINTABLE_CHARS.set(39);
      PRINTABLE_CHARS.set(40);
      PRINTABLE_CHARS.set(41);
      PRINTABLE_CHARS.set(42);
      PRINTABLE_CHARS.set(43);
      PRINTABLE_CHARS.set(44);
      PRINTABLE_CHARS.set(45);
      PRINTABLE_CHARS.set(46);
      PRINTABLE_CHARS.set(47);

      int var0;
      for(var0 = 48; var0 <= 57; ++var0) {
         PRINTABLE_CHARS.set(var0);
      }

      PRINTABLE_CHARS.set(58);
      PRINTABLE_CHARS.set(59);
      PRINTABLE_CHARS.set(60);
      PRINTABLE_CHARS.set(62);
      PRINTABLE_CHARS.set(64);

      for(var0 = 65; var0 <= 90; ++var0) {
         PRINTABLE_CHARS.set(var0);
      }

      PRINTABLE_CHARS.set(91);
      PRINTABLE_CHARS.set(92);
      PRINTABLE_CHARS.set(93);
      PRINTABLE_CHARS.set(94);
      PRINTABLE_CHARS.set(96);

      for(var0 = 97; var0 <= 122; ++var0) {
         PRINTABLE_CHARS.set(var0);
      }

      PRINTABLE_CHARS.set(123);
      PRINTABLE_CHARS.set(124);
      PRINTABLE_CHARS.set(125);
      PRINTABLE_CHARS.set(126);
   }

   public QCodec() {
      this(StandardCharsets.UTF_8);
   }

   public QCodec(String var1) {
      this(Charset.forName(var1));
   }

   public QCodec(Charset var1) {
      this.encodeBlanks = false;
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
         var2.append(" cannot be decoded using Q codec");
         throw new DecoderException(var2.toString());
      }
   }

   public String decode(String var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else {
         try {
            var1 = this.decodeText(var1);
            return var1;
         } catch (UnsupportedEncodingException var2) {
            throw new DecoderException(var2.getMessage(), var2);
         }
      }
   }

   protected byte[] doDecoding(byte[] var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else {
         boolean var5 = false;
         int var6 = var1.length;
         int var3 = 0;

         boolean var4;
         while(true) {
            var4 = var5;
            if (var3 >= var6) {
               break;
            }

            if (var1[var3] == 95) {
               var4 = true;
               break;
            }

            ++var3;
         }

         if (var4) {
            byte[] var7 = new byte[var1.length];

            for(var3 = 0; var3 < var1.length; ++var3) {
               byte var2 = var1[var3];
               if (var2 != 95) {
                  var7[var3] = var2;
               } else {
                  var7[var3] = 32;
               }
            }

            return QuotedPrintableCodec.decodeQuotedPrintable(var7);
         } else {
            return QuotedPrintableCodec.decodeQuotedPrintable(var1);
         }
      }
   }

   protected byte[] doEncoding(byte[] var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, var1);
         if (this.encodeBlanks) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (var1[var2] == 32) {
                  var1[var2] = 95;
               }
            }
         }

         return var1;
      }
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
         var2.append(" cannot be encoded using Q codec");
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
      return "Q";
   }

   public boolean isEncodeBlanks() {
      return this.encodeBlanks;
   }

   public void setEncodeBlanks(boolean var1) {
      this.encodeBlanks = var1;
   }
}
