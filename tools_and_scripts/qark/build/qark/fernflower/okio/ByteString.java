package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ByteString implements Serializable, Comparable {
   public static final ByteString EMPTY = method_6();
   static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   private static final long serialVersionUID = 1L;
   final byte[] data;
   transient int hashCode;
   transient String utf8;

   ByteString(byte[] var1) {
      this.data = var1;
   }

   static int codePointIndexToCharIndex(String var0, int var1) {
      int var2 = 0;
      int var3 = 0;

      int var5;
      for(int var4 = var0.length(); var2 < var4; var2 += Character.charCount(var5)) {
         if (var3 == var1) {
            return var2;
         }

         var5 = var0.codePointAt(var2);
         if (Character.isISOControl(var5) && var5 != 10 && var5 != 13 || var5 == 65533) {
            return -1;
         }

         ++var3;
      }

      return var0.length();
   }

   @Nullable
   public static ByteString decodeBase64(String var0) {
      if (var0 != null) {
         byte[] var1 = Base64.decode(var0);
         return var1 != null ? new ByteString(var1) : null;
      } else {
         throw new IllegalArgumentException("base64 == null");
      }
   }

   public static ByteString decodeHex(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("hex == null");
      } else if (var0.length() % 2 != 0) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Unexpected hex string: ");
         var3.append(var0);
         throw new IllegalArgumentException(var3.toString());
      } else {
         byte[] var2 = new byte[var0.length() / 2];

         for(int var1 = 0; var1 < var2.length; ++var1) {
            var2[var1] = (byte)((decodeHexDigit(var0.charAt(var1 * 2)) << 4) + decodeHexDigit(var0.charAt(var1 * 2 + 1)));
         }

         return method_6(var2);
      }
   }

   private static int decodeHexDigit(char var0) {
      if (var0 >= '0' && var0 <= '9') {
         return var0 - 48;
      } else if (var0 >= 'a' && var0 <= 'f') {
         return var0 - 97 + 10;
      } else if (var0 >= 'A' && var0 <= 'F') {
         return var0 - 65 + 10;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Unexpected hex digit: ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   private ByteString digest(String var1) {
      try {
         ByteString var3 = method_6(MessageDigest.getInstance(var1).digest(this.data));
         return var3;
      } catch (NoSuchAlgorithmException var2) {
         throw new AssertionError(var2);
      }
   }

   public static ByteString encodeString(String var0, Charset var1) {
      if (var0 != null) {
         if (var1 != null) {
            return new ByteString(var0.getBytes(var1));
         } else {
            throw new IllegalArgumentException("charset == null");
         }
      } else {
         throw new IllegalArgumentException("s == null");
      }
   }

   public static ByteString encodeUtf8(String var0) {
      if (var0 != null) {
         ByteString var1 = new ByteString(var0.getBytes(Util.UTF_8));
         var1.utf8 = var0;
         return var1;
      } else {
         throw new IllegalArgumentException("s == null");
      }
   }

   private ByteString hmac(String var1, ByteString var2) {
      try {
         Mac var3 = Mac.getInstance(var1);
         var3.init(new SecretKeySpec(var2.toByteArray(), var1));
         ByteString var6 = method_6(var3.doFinal(this.data));
         return var6;
      } catch (NoSuchAlgorithmException var4) {
         throw new AssertionError(var4);
      } catch (InvalidKeyException var5) {
         throw new IllegalArgumentException(var5);
      }
   }

   // $FF: renamed from: of (java.nio.ByteBuffer) okio.ByteString
   public static ByteString method_5(ByteBuffer var0) {
      if (var0 != null) {
         byte[] var1 = new byte[var0.remaining()];
         var0.get(var1);
         return new ByteString(var1);
      } else {
         throw new IllegalArgumentException("data == null");
      }
   }

   // $FF: renamed from: of (byte[]) okio.ByteString
   public static ByteString method_6(byte... var0) {
      if (var0 != null) {
         return new ByteString((byte[])var0.clone());
      } else {
         throw new IllegalArgumentException("data == null");
      }
   }

   // $FF: renamed from: of (byte[], int, int) okio.ByteString
   public static ByteString method_7(byte[] var0, int var1, int var2) {
      if (var0 != null) {
         Util.checkOffsetAndCount((long)var0.length, (long)var1, (long)var2);
         byte[] var3 = new byte[var2];
         System.arraycopy(var0, var1, var3, 0, var2);
         return new ByteString(var3);
      } else {
         throw new IllegalArgumentException("data == null");
      }
   }

   public static ByteString read(InputStream var0, int var1) throws IOException {
      if (var0 != null) {
         if (var1 >= 0) {
            byte[] var4 = new byte[var1];

            int var3;
            for(int var2 = 0; var2 < var1; var2 += var3) {
               var3 = var0.read(var4, var2, var1 - var2);
               if (var3 == -1) {
                  throw new EOFException();
               }
            }

            return new ByteString(var4);
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("byteCount < 0: ");
            var5.append(var1);
            throw new IllegalArgumentException(var5.toString());
         }
      } else {
         throw new IllegalArgumentException("in == null");
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException {
      ByteString var5 = read(var1, var1.readInt());

      try {
         Field var2 = ByteString.class.getDeclaredField("data");
         var2.setAccessible(true);
         var2.set(this, var5.data);
      } catch (NoSuchFieldException var3) {
         throw new AssertionError();
      } catch (IllegalAccessException var4) {
         throw new AssertionError();
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeInt(this.data.length);
      var1.write(this.data);
   }

   public ByteBuffer asByteBuffer() {
      return ByteBuffer.wrap(this.data).asReadOnlyBuffer();
   }

   public String base64() {
      return Base64.encode(this.data);
   }

   public String base64Url() {
      return Base64.encodeUrl(this.data);
   }

   public int compareTo(ByteString var1) {
      int var3 = this.size();
      int var4 = var1.size();
      int var2 = 0;

      for(int var5 = Math.min(var3, var4); var2 < var5; ++var2) {
         int var6 = this.getByte(var2) & 255;
         int var7 = var1.getByte(var2) & 255;
         if (var6 != var7) {
            if (var6 < var7) {
               return -1;
            }

            return 1;
         }
      }

      if (var3 == var4) {
         return 0;
      } else if (var3 < var4) {
         return -1;
      } else {
         return 1;
      }
   }

   public final boolean endsWith(ByteString var1) {
      return this.rangeEquals(this.size() - var1.size(), (ByteString)var1, 0, var1.size());
   }

   public final boolean endsWith(byte[] var1) {
      return this.rangeEquals(this.size() - var1.length, (byte[])var1, 0, var1.length);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         if (var1 instanceof ByteString) {
            int var2 = ((ByteString)var1).size();
            byte[] var3 = this.data;
            if (var2 == var3.length && ((ByteString)var1).rangeEquals(0, (byte[])var3, 0, var3.length)) {
               return true;
            }
         }

         return false;
      }
   }

   public byte getByte(int var1) {
      return this.data[var1];
   }

   public int hashCode() {
      int var1 = this.hashCode;
      if (var1 != 0) {
         return var1;
      } else {
         var1 = Arrays.hashCode(this.data);
         this.hashCode = var1;
         return var1;
      }
   }

   public String hex() {
      byte[] var6 = this.data;
      char[] var7 = new char[var6.length * 2];
      int var2 = 0;
      int var3 = var6.length;

      for(int var1 = 0; var1 < var3; ++var1) {
         byte var4 = var6[var1];
         int var5 = var2 + 1;
         char[] var8 = HEX_DIGITS;
         var7[var2] = var8[var4 >> 4 & 15];
         var2 = var5 + 1;
         var7[var5] = var8[var4 & 15];
      }

      return new String(var7);
   }

   public ByteString hmacSha1(ByteString var1) {
      return this.hmac("HmacSHA1", var1);
   }

   public ByteString hmacSha256(ByteString var1) {
      return this.hmac("HmacSHA256", var1);
   }

   public ByteString hmacSha512(ByteString var1) {
      return this.hmac("HmacSHA512", var1);
   }

   public final int indexOf(ByteString var1) {
      return this.indexOf((byte[])var1.internalArray(), 0);
   }

   public final int indexOf(ByteString var1, int var2) {
      return this.indexOf(var1.internalArray(), var2);
   }

   public final int indexOf(byte[] var1) {
      return this.indexOf((byte[])var1, 0);
   }

   public int indexOf(byte[] var1, int var2) {
      var2 = Math.max(var2, 0);
      int var3 = this.data.length;

      for(int var4 = var1.length; var2 <= var3 - var4; ++var2) {
         if (Util.arrayRangeEquals(this.data, var2, var1, 0, var1.length)) {
            return var2;
         }
      }

      return -1;
   }

   byte[] internalArray() {
      return this.data;
   }

   public final int lastIndexOf(ByteString var1) {
      return this.lastIndexOf(var1.internalArray(), this.size());
   }

   public final int lastIndexOf(ByteString var1, int var2) {
      return this.lastIndexOf(var1.internalArray(), var2);
   }

   public final int lastIndexOf(byte[] var1) {
      return this.lastIndexOf(var1, this.size());
   }

   public int lastIndexOf(byte[] var1, int var2) {
      for(var2 = Math.min(var2, this.data.length - var1.length); var2 >= 0; --var2) {
         if (Util.arrayRangeEquals(this.data, var2, var1, 0, var1.length)) {
            return var2;
         }
      }

      return -1;
   }

   public ByteString md5() {
      return this.digest("MD5");
   }

   public boolean rangeEquals(int var1, ByteString var2, int var3, int var4) {
      return var2.rangeEquals(var3, this.data, var1, var4);
   }

   public boolean rangeEquals(int var1, byte[] var2, int var3, int var4) {
      if (var1 >= 0) {
         byte[] var5 = this.data;
         if (var1 <= var5.length - var4 && var3 >= 0 && var3 <= var2.length - var4 && Util.arrayRangeEquals(var5, var1, var2, var3, var4)) {
            return true;
         }
      }

      return false;
   }

   public ByteString sha1() {
      return this.digest("SHA-1");
   }

   public ByteString sha256() {
      return this.digest("SHA-256");
   }

   public ByteString sha512() {
      return this.digest("SHA-512");
   }

   public int size() {
      return this.data.length;
   }

   public final boolean startsWith(ByteString var1) {
      return this.rangeEquals(0, (ByteString)var1, 0, var1.size());
   }

   public final boolean startsWith(byte[] var1) {
      return this.rangeEquals(0, (byte[])var1, 0, var1.length);
   }

   public String string(Charset var1) {
      if (var1 != null) {
         return new String(this.data, var1);
      } else {
         throw new IllegalArgumentException("charset == null");
      }
   }

   public ByteString substring(int var1) {
      return this.substring(var1, this.data.length);
   }

   public ByteString substring(int var1, int var2) {
      if (var1 >= 0) {
         byte[] var4 = this.data;
         if (var2 <= var4.length) {
            int var3 = var2 - var1;
            if (var3 >= 0) {
               if (var1 == 0 && var2 == var4.length) {
                  return this;
               } else {
                  var4 = new byte[var3];
                  System.arraycopy(this.data, var1, var4, 0, var3);
                  return new ByteString(var4);
               }
            } else {
               throw new IllegalArgumentException("endIndex < beginIndex");
            }
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("endIndex > length(");
            var5.append(this.data.length);
            var5.append(")");
            throw new IllegalArgumentException(var5.toString());
         }
      } else {
         throw new IllegalArgumentException("beginIndex < 0");
      }
   }

   public ByteString toAsciiLowercase() {
      int var1 = 0;

      while(true) {
         byte[] var4 = this.data;
         if (var1 >= var4.length) {
            return this;
         }

         byte var3 = var4[var1];
         if (var3 >= 65 && var3 <= 90) {
            var4 = (byte[])var4.clone();
            int var2 = var1 + 1;
            var4[var1] = (byte)(var3 + 32);

            for(var1 = var2; var1 < var4.length; ++var1) {
               byte var5 = var4[var1];
               if (var5 >= 65 && var5 <= 90) {
                  var4[var1] = (byte)(var5 + 32);
               }
            }

            return new ByteString(var4);
         }

         ++var1;
      }
   }

   public ByteString toAsciiUppercase() {
      int var1 = 0;

      while(true) {
         byte[] var4 = this.data;
         if (var1 >= var4.length) {
            return this;
         }

         byte var3 = var4[var1];
         if (var3 >= 97 && var3 <= 122) {
            var4 = (byte[])var4.clone();
            int var2 = var1 + 1;
            var4[var1] = (byte)(var3 - 32);

            for(var1 = var2; var1 < var4.length; ++var1) {
               byte var5 = var4[var1];
               if (var5 >= 97 && var5 <= 122) {
                  var4[var1] = (byte)(var5 - 32);
               }
            }

            return new ByteString(var4);
         }

         ++var1;
      }
   }

   public byte[] toByteArray() {
      return (byte[])this.data.clone();
   }

   public String toString() {
      if (this.data.length == 0) {
         return "[size=0]";
      } else {
         String var3 = this.utf8();
         int var1 = codePointIndexToCharIndex(var3, 64);
         if (var1 == -1) {
            StringBuilder var4;
            if (this.data.length <= 64) {
               var4 = new StringBuilder();
               var4.append("[hex=");
               var4.append(this.hex());
               var4.append("]");
               return var4.toString();
            } else {
               var4 = new StringBuilder();
               var4.append("[size=");
               var4.append(this.data.length);
               var4.append(" hex=");
               var4.append(this.substring(0, 64).hex());
               var4.append("…]");
               return var4.toString();
            }
         } else {
            String var2 = var3.substring(0, var1).replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
            StringBuilder var5;
            if (var1 < var3.length()) {
               var5 = new StringBuilder();
               var5.append("[size=");
               var5.append(this.data.length);
               var5.append(" text=");
               var5.append(var2);
               var5.append("…]");
               return var5.toString();
            } else {
               var5 = new StringBuilder();
               var5.append("[text=");
               var5.append(var2);
               var5.append("]");
               return var5.toString();
            }
         }
      }
   }

   public String utf8() {
      String var1 = this.utf8;
      if (var1 != null) {
         return var1;
      } else {
         var1 = new String(this.data, Util.UTF_8);
         this.utf8 = var1;
         return var1;
      }
   }

   public void write(OutputStream var1) throws IOException {
      if (var1 != null) {
         var1.write(this.data);
      } else {
         throw new IllegalArgumentException("out == null");
      }
   }

   void write(Buffer var1) {
      byte[] var2 = this.data;
      var1.write(var2, 0, var2.length);
   }
}
