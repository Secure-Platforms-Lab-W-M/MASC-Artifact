package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HashingSink extends ForwardingSink {
   @Nullable
   private final Mac mac;
   @Nullable
   private final MessageDigest messageDigest;

   private HashingSink(Sink var1, String var2) {
      super(var1);

      try {
         this.messageDigest = MessageDigest.getInstance(var2);
         this.mac = null;
      } catch (NoSuchAlgorithmException var3) {
         throw new AssertionError();
      }
   }

   private HashingSink(Sink var1, ByteString var2, String var3) {
      super(var1);

      try {
         Mac var6 = Mac.getInstance(var3);
         this.mac = var6;
         var6.init(new SecretKeySpec(var2.toByteArray(), var3));
         this.messageDigest = null;
      } catch (NoSuchAlgorithmException var4) {
         throw new AssertionError();
      } catch (InvalidKeyException var5) {
         throw new IllegalArgumentException(var5);
      }
   }

   public static HashingSink hmacSha1(Sink var0, ByteString var1) {
      return new HashingSink(var0, var1, "HmacSHA1");
   }

   public static HashingSink hmacSha256(Sink var0, ByteString var1) {
      return new HashingSink(var0, var1, "HmacSHA256");
   }

   public static HashingSink hmacSha512(Sink var0, ByteString var1) {
      return new HashingSink(var0, var1, "HmacSHA512");
   }

   public static HashingSink md5(Sink var0) {
      return new HashingSink(var0, "MD5");
   }

   public static HashingSink sha1(Sink var0) {
      return new HashingSink(var0, "SHA-1");
   }

   public static HashingSink sha256(Sink var0) {
      return new HashingSink(var0, "SHA-256");
   }

   public static HashingSink sha512(Sink var0) {
      return new HashingSink(var0, "SHA-512");
   }

   public ByteString hash() {
      MessageDigest var1 = this.messageDigest;
      byte[] var2;
      if (var1 != null) {
         var2 = var1.digest();
      } else {
         var2 = this.mac.doFinal();
      }

      return ByteString.method_6(var2);
   }

   public void write(Buffer var1, long var2) throws IOException {
      Util.checkOffsetAndCount(var1.size, 0L, var2);
      long var5 = 0L;

      for(Segment var7 = var1.head; var5 < var2; var7 = var7.next) {
         int var4 = (int)Math.min(var2 - var5, (long)(var7.limit - var7.pos));
         MessageDigest var8 = this.messageDigest;
         if (var8 != null) {
            var8.update(var7.data, var7.pos, var4);
         } else {
            this.mac.update(var7.data, var7.pos, var4);
         }

         var5 += (long)var4;
      }

      super.write(var1, var2);
   }
}
