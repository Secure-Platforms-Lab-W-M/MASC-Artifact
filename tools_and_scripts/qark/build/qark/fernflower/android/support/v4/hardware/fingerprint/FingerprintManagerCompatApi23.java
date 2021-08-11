package android.support.v4.hardware.fingerprint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

@RequiresApi(23)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class FingerprintManagerCompatApi23 {
   @SuppressLint({"MissingPermission"})
   static void authenticate(Context var0, FingerprintManagerCompatApi23.CryptoObject var1, int var2, Object var3, FingerprintManagerCompatApi23.AuthenticationCallback var4, Handler var5) {
      FingerprintManager var6 = getFingerprintManagerOrNull(var0);
      if (var6 != null) {
         var6.authenticate(wrapCryptoObject(var1), (CancellationSignal)var3, var2, wrapCallback(var4), var5);
      }

   }

   private static FingerprintManager getFingerprintManagerOrNull(Context var0) {
      return var0.getPackageManager().hasSystemFeature("android.hardware.fingerprint") ? (FingerprintManager)var0.getSystemService(FingerprintManager.class) : null;
   }

   @SuppressLint({"MissingPermission"})
   static boolean hasEnrolledFingerprints(Context var0) {
      FingerprintManager var1 = getFingerprintManagerOrNull(var0);
      return var1 != null && var1.hasEnrolledFingerprints();
   }

   @SuppressLint({"MissingPermission"})
   static boolean isHardwareDetected(Context var0) {
      FingerprintManager var1 = getFingerprintManagerOrNull(var0);
      return var1 != null && var1.isHardwareDetected();
   }

   private static FingerprintManagerCompatApi23.CryptoObject unwrapCryptoObject(android.hardware.fingerprint.FingerprintManager.CryptoObject var0) {
      if (var0 == null) {
         return null;
      } else if (var0.getCipher() != null) {
         return new FingerprintManagerCompatApi23.CryptoObject(var0.getCipher());
      } else if (var0.getSignature() != null) {
         return new FingerprintManagerCompatApi23.CryptoObject(var0.getSignature());
      } else {
         return var0.getMac() != null ? new FingerprintManagerCompatApi23.CryptoObject(var0.getMac()) : null;
      }
   }

   private static android.hardware.fingerprint.FingerprintManager.AuthenticationCallback wrapCallback(final FingerprintManagerCompatApi23.AuthenticationCallback var0) {
      return new android.hardware.fingerprint.FingerprintManager.AuthenticationCallback() {
         public void onAuthenticationError(int var1, CharSequence var2) {
            var0.onAuthenticationError(var1, var2);
         }

         public void onAuthenticationFailed() {
            var0.onAuthenticationFailed();
         }

         public void onAuthenticationHelp(int var1, CharSequence var2) {
            var0.onAuthenticationHelp(var1, var2);
         }

         public void onAuthenticationSucceeded(AuthenticationResult var1) {
            var0.onAuthenticationSucceeded(new FingerprintManagerCompatApi23.AuthenticationResultInternal(FingerprintManagerCompatApi23.unwrapCryptoObject(var1.getCryptoObject())));
         }
      };
   }

   private static android.hardware.fingerprint.FingerprintManager.CryptoObject wrapCryptoObject(FingerprintManagerCompatApi23.CryptoObject var0) {
      if (var0 == null) {
         return null;
      } else if (var0.getCipher() != null) {
         return new android.hardware.fingerprint.FingerprintManager.CryptoObject(var0.getCipher());
      } else if (var0.getSignature() != null) {
         return new android.hardware.fingerprint.FingerprintManager.CryptoObject(var0.getSignature());
      } else {
         return var0.getMac() != null ? new android.hardware.fingerprint.FingerprintManager.CryptoObject(var0.getMac()) : null;
      }
   }

   public abstract static class AuthenticationCallback {
      public void onAuthenticationError(int var1, CharSequence var2) {
      }

      public void onAuthenticationFailed() {
      }

      public void onAuthenticationHelp(int var1, CharSequence var2) {
      }

      public void onAuthenticationSucceeded(FingerprintManagerCompatApi23.AuthenticationResultInternal var1) {
      }
   }

   public static final class AuthenticationResultInternal {
      private FingerprintManagerCompatApi23.CryptoObject mCryptoObject;

      public AuthenticationResultInternal(FingerprintManagerCompatApi23.CryptoObject var1) {
         this.mCryptoObject = var1;
      }

      public FingerprintManagerCompatApi23.CryptoObject getCryptoObject() {
         return this.mCryptoObject;
      }
   }

   public static class CryptoObject {
      private final Cipher mCipher;
      private final Mac mMac;
      private final Signature mSignature;

      public CryptoObject(Signature var1) {
         this.mSignature = var1;
         this.mCipher = null;
         this.mMac = null;
      }

      public CryptoObject(Cipher var1) {
         this.mCipher = var1;
         this.mSignature = null;
         this.mMac = null;
      }

      public CryptoObject(Mac var1) {
         this.mMac = var1;
         this.mCipher = null;
         this.mSignature = null;
      }

      public Cipher getCipher() {
         return this.mCipher;
      }

      public Mac getMac() {
         return this.mMac;
      }

      public Signature getSignature() {
         return this.mSignature;
      }
   }
}
