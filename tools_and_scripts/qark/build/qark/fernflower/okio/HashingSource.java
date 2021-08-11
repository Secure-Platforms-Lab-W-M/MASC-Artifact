package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HashingSource extends ForwardingSource {
   private final Mac mac;
   private final MessageDigest messageDigest;

   private HashingSource(Source var1, String var2) {
      super(var1);

      try {
         this.messageDigest = MessageDigest.getInstance(var2);
         this.mac = null;
      } catch (NoSuchAlgorithmException var3) {
         throw new AssertionError();
      }
   }

   private HashingSource(Source var1, ByteString var2, String var3) {
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

   public static HashingSource hmacSha1(Source var0, ByteString var1) {
      return new HashingSource(var0, var1, "HmacSHA1");
   }

   public static HashingSource hmacSha256(Source var0, ByteString var1) {
      return new HashingSource(var0, var1, "HmacSHA256");
   }

   public static HashingSource md5(Source var0) {
      return new HashingSource(var0, "MD5");
   }

   public static HashingSource sha1(Source var0) {
      return new HashingSource(var0, "SHA-1");
   }

   public static HashingSource sha256(Source var0) {
      return new HashingSource(var0, "SHA-256");
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

   public long read(Buffer var1, long var2) throws IOException {
      long var11 = super.read(var1, var2);
      if (var11 != -1L) {
         long var9 = var1.size - var11;
         var2 = var1.size;
         Segment var13 = var1.head;

         while(true) {
            long var5 = var9;
            long var7 = var2;
            Segment var14 = var13;
            if (var2 <= var9) {
               while(var7 < var1.size) {
                  int var4 = (int)((long)var14.pos + var5 - var7);
                  MessageDigest var15 = this.messageDigest;
                  if (var15 != null) {
                     var15.update(var14.data, var4, var14.limit - var4);
                  } else {
                     this.mac.update(var14.data, var4, var14.limit - var4);
                  }

                  var7 += (long)(var14.limit - var14.pos);
                  var5 = var7;
                  var14 = var14.next;
               }
               break;
            }

            var13 = var13.prev;
            var2 -= (long)(var13.limit - var13.pos);
         }
      }

      return var11;
   }
}
