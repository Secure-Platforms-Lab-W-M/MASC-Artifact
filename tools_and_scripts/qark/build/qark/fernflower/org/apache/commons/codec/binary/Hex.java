package org.apache.commons.codec.binary;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class Hex implements BinaryEncoder, BinaryDecoder {
   public static final Charset DEFAULT_CHARSET;
   public static final String DEFAULT_CHARSET_NAME = "UTF-8";
   private static final char[] DIGITS_LOWER;
   private static final char[] DIGITS_UPPER;
   private final Charset charset;

   static {
      DEFAULT_CHARSET = StandardCharsets.UTF_8;
      DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
      DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   }

   public Hex() {
      this.charset = DEFAULT_CHARSET;
   }

   public Hex(String var1) {
      this(Charset.forName(var1));
   }

   public Hex(Charset var1) {
      this.charset = var1;
   }

   public static byte[] decodeHex(String var0) throws DecoderException {
      return decodeHex(var0.toCharArray());
   }

   public static byte[] decodeHex(char[] var0) throws DecoderException {
      int var3 = var0.length;
      if ((var3 & 1) != 0) {
         throw new DecoderException("Odd number of characters.");
      } else {
         byte[] var6 = new byte[var3 >> 1];
         int var1 = 0;

         for(int var2 = 0; var2 < var3; ++var1) {
            int var4 = toDigit(var0[var2], var2);
            ++var2;
            int var5 = toDigit(var0[var2], var2);
            ++var2;
            var6[var1] = (byte)((var4 << 4 | var5) & 255);
         }

         return var6;
      }
   }

   public static char[] encodeHex(ByteBuffer var0) {
      return encodeHex(var0, true);
   }

   public static char[] encodeHex(ByteBuffer var0, boolean var1) {
      char[] var2;
      if (var1) {
         var2 = DIGITS_LOWER;
      } else {
         var2 = DIGITS_UPPER;
      }

      return encodeHex(var0, var2);
   }

   protected static char[] encodeHex(ByteBuffer var0, char[] var1) {
      return encodeHex(toByteArray(var0), var1);
   }

   public static char[] encodeHex(byte[] var0) {
      return encodeHex(var0, true);
   }

   public static char[] encodeHex(byte[] var0, boolean var1) {
      char[] var2;
      if (var1) {
         var2 = DIGITS_LOWER;
      } else {
         var2 = DIGITS_UPPER;
      }

      return encodeHex(var0, var2);
   }

   protected static char[] encodeHex(byte[] var0, char[] var1) {
      int var4 = var0.length;
      char[] var6 = new char[var4 << 1];
      int var2 = 0;

      for(int var3 = 0; var2 < var4; ++var2) {
         int var5 = var3 + 1;
         var6[var3] = var1[(var0[var2] & 240) >>> 4];
         var3 = var5 + 1;
         var6[var5] = var1[var0[var2] & 15];
      }

      return var6;
   }

   public static String encodeHexString(ByteBuffer var0) {
      return new String(encodeHex(var0));
   }

   public static String encodeHexString(ByteBuffer var0, boolean var1) {
      return new String(encodeHex(var0, var1));
   }

   public static String encodeHexString(byte[] var0) {
      return new String(encodeHex(var0));
   }

   public static String encodeHexString(byte[] var0, boolean var1) {
      return new String(encodeHex(var0, var1));
   }

   private static byte[] toByteArray(ByteBuffer var0) {
      int var1 = var0.remaining();
      byte[] var2;
      if (var0.hasArray()) {
         var2 = var0.array();
         if (var1 == var2.length) {
            var0.position(var1);
            return var2;
         }
      }

      var2 = new byte[var1];
      var0.get(var2);
      return var2;
   }

   protected static int toDigit(char var0, int var1) throws DecoderException {
      int var2 = Character.digit(var0, 16);
      if (var2 != -1) {
         return var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Illegal hexadecimal character ");
         var3.append(var0);
         var3.append(" at index ");
         var3.append(var1);
         throw new DecoderException(var3.toString());
      }
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 instanceof String) {
         return this.decode((Object)((String)var1).toCharArray());
      } else if (var1 instanceof byte[]) {
         return this.decode((byte[])((byte[])var1));
      } else if (var1 instanceof ByteBuffer) {
         return this.decode((ByteBuffer)var1);
      } else {
         try {
            byte[] var3 = decodeHex((char[])((char[])var1));
            return var3;
         } catch (ClassCastException var2) {
            throw new DecoderException(var2.getMessage(), var2);
         }
      }
   }

   public byte[] decode(ByteBuffer var1) throws DecoderException {
      return decodeHex((new String(toByteArray(var1), this.getCharset())).toCharArray());
   }

   public byte[] decode(byte[] var1) throws DecoderException {
      return decodeHex((new String(var1, this.getCharset())).toCharArray());
   }

   public Object encode(Object var1) throws EncoderException {
      byte[] var3;
      if (var1 instanceof String) {
         var3 = ((String)var1).getBytes(this.getCharset());
      } else if (var1 instanceof ByteBuffer) {
         var3 = toByteArray((ByteBuffer)var1);
      } else {
         try {
            var3 = (byte[])((byte[])var1);
         } catch (ClassCastException var2) {
            throw new EncoderException(var2.getMessage(), var2);
         }
      }

      return encodeHex(var3);
   }

   public byte[] encode(ByteBuffer var1) {
      return encodeHexString(var1).getBytes(this.getCharset());
   }

   public byte[] encode(byte[] var1) {
      return encodeHexString(var1).getBytes(this.getCharset());
   }

   public Charset getCharset() {
      return this.charset;
   }

   public String getCharsetName() {
      return this.charset.name();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("[charsetName=");
      var1.append(this.charset);
      var1.append("]");
      return var1.toString();
   }
}
