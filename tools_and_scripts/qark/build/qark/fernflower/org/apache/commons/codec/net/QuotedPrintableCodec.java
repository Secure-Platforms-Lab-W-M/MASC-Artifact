package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class QuotedPrintableCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
   // $FF: renamed from: CR byte
   private static final byte field_166 = 13;
   private static final byte ESCAPE_CHAR = 61;
   // $FF: renamed from: LF byte
   private static final byte field_167 = 10;
   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
   private static final int SAFE_LENGTH = 73;
   private static final byte SPACE = 32;
   private static final byte TAB = 9;
   private final Charset charset;
   private final boolean strict;

   static {
      int var0;
      for(var0 = 33; var0 <= 60; ++var0) {
         PRINTABLE_CHARS.set(var0);
      }

      for(var0 = 62; var0 <= 126; ++var0) {
         PRINTABLE_CHARS.set(var0);
      }

      PRINTABLE_CHARS.set(9);
      PRINTABLE_CHARS.set(32);
   }

   public QuotedPrintableCodec() {
      this(StandardCharsets.UTF_8, false);
   }

   public QuotedPrintableCodec(String var1) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
      this(Charset.forName(var1), false);
   }

   public QuotedPrintableCodec(Charset var1) {
      this(var1, false);
   }

   public QuotedPrintableCodec(Charset var1, boolean var2) {
      this.charset = var1;
      this.strict = var2;
   }

   public QuotedPrintableCodec(boolean var1) {
      this(StandardCharsets.UTF_8, var1);
   }

   public static final byte[] decodeQuotedPrintable(byte[] var0) throws DecoderException {
      if (var0 == null) {
         return null;
      } else {
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();

         int var2;
         for(int var1 = 0; var1 < var0.length; var1 = var2 + 1) {
            byte var3 = var0[var1];
            if (var3 == 61) {
               var2 = var1 + 1;
               if (var0[var2] != 13) {
                  ArrayIndexOutOfBoundsException var10000;
                  label51: {
                     boolean var10001;
                     try {
                        var1 = Utils.digit16(var0[var2]);
                     } catch (ArrayIndexOutOfBoundsException var6) {
                        var10000 = var6;
                        var10001 = false;
                        break label51;
                     }

                     ++var2;

                     try {
                        var4.write((char)((var1 << 4) + Utils.digit16(var0[var2])));
                        continue;
                     } catch (ArrayIndexOutOfBoundsException var5) {
                        var10000 = var5;
                        var10001 = false;
                     }
                  }

                  ArrayIndexOutOfBoundsException var7 = var10000;
                  throw new DecoderException("Invalid quoted-printable encoding", var7);
               }
            } else {
               var2 = var1;
               if (var3 != 13) {
                  var2 = var1;
                  if (var3 != 10) {
                     var4.write(var3);
                     var2 = var1;
                  }
               }
            }
         }

         return var4.toByteArray();
      }
   }

   private static int encodeByte(int var0, boolean var1, ByteArrayOutputStream var2) {
      if (var1) {
         return encodeQuotedPrintable(var0, var2);
      } else {
         var2.write(var0);
         return 1;
      }
   }

   private static final int encodeQuotedPrintable(int var0, ByteArrayOutputStream var1) {
      var1.write(61);
      char var2 = Utils.hexDigit(var0 >> 4);
      char var3 = Utils.hexDigit(var0);
      var1.write(var2);
      var1.write(var3);
      return 3;
   }

   public static final byte[] encodeQuotedPrintable(BitSet var0, byte[] var1) {
      return encodeQuotedPrintable(var0, var1, false);
   }

   public static final byte[] encodeQuotedPrintable(BitSet var0, byte[] var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         BitSet var8 = var0;
         if (var0 == null) {
            var8 = PRINTABLE_CHARS;
         }

         ByteArrayOutputStream var9 = new ByteArrayOutputStream();
         int var3 = 0;
         int var4;
         if (var2) {
            var3 = 1;
            var4 = 0;

            while(true) {
               int var5 = var1.length;
               boolean var7 = true;
               if (var4 >= var5 - 3) {
                  var4 = getUnsignedOctet(var1.length - 3, var1);
                  if (!var8.get(var4) || isWhitespace(var4) && var3 > 68) {
                     var2 = true;
                  } else {
                     var2 = false;
                  }

                  if (var3 + encodeByte(var4, var2, var9) > 71) {
                     var9.write(61);
                     var9.write(13);
                     var9.write(10);
                  }

                  for(var3 = var1.length - 2; var3 < var1.length; ++var3) {
                     var4 = getUnsignedOctet(var3, var1);
                     if (!var8.get(var4) || var3 > var1.length - 2 && isWhitespace(var4)) {
                        var2 = true;
                     } else {
                        var2 = false;
                     }

                     encodeByte(var4, var2, var9);
                  }
                  break;
               }

               var5 = getUnsignedOctet(var4, var1);
               if (var3 < 73) {
                  var3 += encodeByte(var5, var8.get(var5) ^ true, var9);
               } else {
                  var2 = var7;
                  if (var8.get(var5)) {
                     if (isWhitespace(var5)) {
                        var2 = var7;
                     } else {
                        var2 = false;
                     }
                  }

                  encodeByte(var5, var2, var9);
                  var9.write(61);
                  var9.write(13);
                  var9.write(10);
                  var3 = 1;
               }

               ++var4;
            }
         } else {
            for(int var6 = var1.length; var3 < var6; ++var3) {
               byte var10 = var1[var3];
               var4 = var10;
               if (var10 < 0) {
                  var4 = var10 + 256;
               }

               if (var8.get(var4)) {
                  var9.write(var4);
               } else {
                  encodeQuotedPrintable(var4, var9);
               }
            }
         }

         return var9.toByteArray();
      }
   }

   private static int getUnsignedOctet(int var0, byte[] var1) {
      byte var2 = var1[var0];
      var0 = var2;
      if (var2 < 0) {
         var0 = var2 + 256;
      }

      return var0;
   }

   private static boolean isWhitespace(int var0) {
      return var0 == 32 || var0 == 9;
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
         var2.append(" cannot be quoted-printable decoded");
         throw new DecoderException(var2.toString());
      }
   }

   public String decode(String var1) throws DecoderException {
      return this.decode(var1, this.getCharset());
   }

   public String decode(String var1, String var2) throws DecoderException, UnsupportedEncodingException {
      return var1 == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(var1)), var2);
   }

   public String decode(String var1, Charset var2) throws DecoderException {
      return var1 == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(var1)), var2);
   }

   public byte[] decode(byte[] var1) throws DecoderException {
      return decodeQuotedPrintable(var1);
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
         var2.append(" cannot be quoted-printable encoded");
         throw new EncoderException(var2.toString());
      }
   }

   public String encode(String var1) throws EncoderException {
      return this.encode(var1, this.getCharset());
   }

   public String encode(String var1, String var2) throws UnsupportedEncodingException {
      return var1 == null ? null : StringUtils.newStringUsAscii(this.encode(var1.getBytes(var2)));
   }

   public String encode(String var1, Charset var2) {
      return var1 == null ? null : StringUtils.newStringUsAscii(this.encode(var1.getBytes(var2)));
   }

   public byte[] encode(byte[] var1) {
      return encodeQuotedPrintable(PRINTABLE_CHARS, var1, this.strict);
   }

   public Charset getCharset() {
      return this.charset;
   }

   public String getDefaultCharset() {
      return this.charset.name();
   }
}
