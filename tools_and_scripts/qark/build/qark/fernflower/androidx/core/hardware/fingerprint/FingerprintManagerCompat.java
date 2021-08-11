package androidx.core.hardware.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.os.Build.VERSION;
import androidx.core.os.CancellationSignal;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

@Deprecated
public final class FingerprintManagerCompat {
   private final Context mContext;

   private FingerprintManagerCompat(Context var1) {
      this.mContext = var1;
   }

   public static FingerprintManagerCompat from(Context var0) {
      return new FingerprintManagerCompat(var0);
   }

   private static FingerprintManager getFingerprintManagerOrNull(Context var0) {
      if (VERSION.SDK_INT == 23) {
         return (FingerprintManager)var0.getSystemService(FingerprintManager.class);
      } else {
         return VERSION.SDK_INT > 23 && var0.getPackageManager().hasSystemFeature("android.hardware.fingerprint") ? (FingerprintManager)var0.getSystemService(FingerprintManager.class) : null;
      }
   }

   static FingerprintManagerCompat.CryptoObject unwrapCryptoObject(android.hardware.fingerprint.FingerprintManager.CryptoObject var0) {
      if (var0 == null) {
         return null;
      } else if (var0.getCipher() != null) {
         return new FingerprintManagerCompat.CryptoObject(var0.getCipher());
      } else if (var0.getSignature() != null) {
         return new FingerprintManagerCompat.CryptoObject(var0.getSignature());
      } else {
         return var0.getMac() != null ? new FingerprintManagerCompat.CryptoObject(var0.getMac()) : null;
      }
   }

   private static android.hardware.fingerprint.FingerprintManager.AuthenticationCallback wrapCallback(final FingerprintManagerCompat.AuthenticationCallback var0) {
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

         public void onAuthenticationSucceeded(android.hardware.fingerprint.FingerprintManager.AuthenticationResult var1) {
            var0.onAuthenticationSucceeded(new FingerprintManagerCompat.AuthenticationResult(FingerprintManagerCompat.unwrapCryptoObject(var1.getCryptoObject())));
         }
      };
   }

   private static android.hardware.fingerprint.FingerprintManager.CryptoObject wrapCryptoObject(FingerprintManagerCompat.CryptoObject var0) {
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

   public void authenticate(FingerprintManagerCompat.CryptoObject var1, int var2, CancellationSignal var3, FingerprintManagerCompat.AuthenticationCallback var4, Handler var5) {
      if (VERSION.SDK_INT >= 23) {
         FingerprintManager var6 = getFingerprintManagerOrNull(this.mContext);
         if (var6 != null) {
            android.os.CancellationSignal var7;
            if (var3 != null) {
               var7 = (android.os.CancellationSignal)var3.getCancellationSignalObject();
            } else {
               var7 = null;
            }

            var6.authenticate(wrapCryptoObject(var1), var7, var2, wrapCallback(var4), var5);
         }
      }

   }

   public boolean hasEnrolledFingerprints() {
      int var1 = VERSION.SDK_INT;
      boolean var3 = false;
      if (var1 >= 23) {
         FingerprintManager var4 = getFingerprintManagerOrNull(this.mContext);
         boolean var2 = var3;
         if (var4 != null) {
            var2 = var3;
            if (var4.hasEnrolledFingerprints()) {
               var2 = true;
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public boolean isHardwareDetected() {
      int var1 = VERSION.SDK_INT;
      boolean var3 = false;
      if (var1 >= 23) {
         FingerprintManager var4 = getFingerprintManagerOrNull(this.mContext);
         boolean var2 = var3;
         if (var4 != null) {
            var2 = var3;
            if (var4.isHardwareDetected()) {
               var2 = true;
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public abstract static class AuthenticationCallback {
      public void onAuthenticationError(int var1, CharSequence var2) {
      }

      public void onAuthenticationFailed() {
      }

      public void onAuthenticationHelp(int var1, CharSequence var2) {
      }

      public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult var1) {
      }
   }

   public static final class AuthenticationResult {
      private final FingerprintManagerCompat.CryptoObject mCryptoObject;

      public AuthenticationResult(FingerprintManagerCompat.CryptoObject var1) {
         this.mCryptoObject = var1;
      }

      public FingerprintManagerCompat.CryptoObject getCryptoObject() {
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
