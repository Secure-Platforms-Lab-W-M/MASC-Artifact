package android.support.v4.hardware.fingerprint;

import android.content.Context;
import android.os.Handler;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.os.CancellationSignal;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public final class FingerprintManagerCompat {
   static final FingerprintManagerCompat.FingerprintManagerCompatImpl IMPL;
   private Context mContext;

   static {
      if (VERSION.SDK_INT >= 23) {
         IMPL = new FingerprintManagerCompat.Api23FingerprintManagerCompatImpl();
      } else {
         IMPL = new FingerprintManagerCompat.LegacyFingerprintManagerCompatImpl();
      }
   }

   private FingerprintManagerCompat(Context var1) {
      this.mContext = var1;
   }

   public static FingerprintManagerCompat from(Context var0) {
      return new FingerprintManagerCompat(var0);
   }

   public void authenticate(@Nullable FingerprintManagerCompat.CryptoObject var1, int var2, @Nullable CancellationSignal var3, @NonNull FingerprintManagerCompat.AuthenticationCallback var4, @Nullable Handler var5) {
      IMPL.authenticate(this.mContext, var1, var2, var3, var4, var5);
   }

   public boolean hasEnrolledFingerprints() {
      return IMPL.hasEnrolledFingerprints(this.mContext);
   }

   public boolean isHardwareDetected() {
      return IMPL.isHardwareDetected(this.mContext);
   }

   @RequiresApi(23)
   private static class Api23FingerprintManagerCompatImpl implements FingerprintManagerCompat.FingerprintManagerCompatImpl {
      public Api23FingerprintManagerCompatImpl() {
      }

      static FingerprintManagerCompat.CryptoObject unwrapCryptoObject(FingerprintManagerCompatApi23.CryptoObject var0) {
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

      private static FingerprintManagerCompatApi23.AuthenticationCallback wrapCallback(final FingerprintManagerCompat.AuthenticationCallback var0) {
         return new FingerprintManagerCompatApi23.AuthenticationCallback() {
            public void onAuthenticationError(int var1, CharSequence var2) {
               var0.onAuthenticationError(var1, var2);
            }

            public void onAuthenticationFailed() {
               var0.onAuthenticationFailed();
            }

            public void onAuthenticationHelp(int var1, CharSequence var2) {
               var0.onAuthenticationHelp(var1, var2);
            }

            public void onAuthenticationSucceeded(FingerprintManagerCompatApi23.AuthenticationResultInternal var1) {
               var0.onAuthenticationSucceeded(new FingerprintManagerCompat.AuthenticationResult(FingerprintManagerCompat.Api23FingerprintManagerCompatImpl.unwrapCryptoObject(var1.getCryptoObject())));
            }
         };
      }

      private static FingerprintManagerCompatApi23.CryptoObject wrapCryptoObject(FingerprintManagerCompat.CryptoObject var0) {
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

      public void authenticate(Context var1, FingerprintManagerCompat.CryptoObject var2, int var3, CancellationSignal var4, FingerprintManagerCompat.AuthenticationCallback var5, Handler var6) {
         FingerprintManagerCompatApi23.CryptoObject var7 = wrapCryptoObject(var2);
         Object var8;
         if (var4 != null) {
            var8 = var4.getCancellationSignalObject();
         } else {
            var8 = null;
         }

         FingerprintManagerCompatApi23.authenticate(var1, var7, var3, var8, wrapCallback(var5), var6);
      }

      public boolean hasEnrolledFingerprints(Context var1) {
         return FingerprintManagerCompatApi23.hasEnrolledFingerprints(var1);
      }

      public boolean isHardwareDetected(Context var1) {
         return FingerprintManagerCompatApi23.isHardwareDetected(var1);
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
      private FingerprintManagerCompat.CryptoObject mCryptoObject;

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

   private interface FingerprintManagerCompatImpl {
      void authenticate(Context var1, FingerprintManagerCompat.CryptoObject var2, int var3, CancellationSignal var4, FingerprintManagerCompat.AuthenticationCallback var5, Handler var6);

      boolean hasEnrolledFingerprints(Context var1);

      boolean isHardwareDetected(Context var1);
   }

   private static class LegacyFingerprintManagerCompatImpl implements FingerprintManagerCompat.FingerprintManagerCompatImpl {
      public LegacyFingerprintManagerCompatImpl() {
      }

      public void authenticate(Context var1, FingerprintManagerCompat.CryptoObject var2, int var3, CancellationSignal var4, FingerprintManagerCompat.AuthenticationCallback var5, Handler var6) {
      }

      public boolean hasEnrolledFingerprints(Context var1) {
         return false;
      }

      public boolean isHardwareDetected(Context var1) {
         return false;
      }
   }
}
