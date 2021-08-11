/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.hardware.fingerprint.FingerprintManager
 *  android.hardware.fingerprint.FingerprintManager$AuthenticationCallback
 *  android.hardware.fingerprint.FingerprintManager$AuthenticationResult
 *  android.hardware.fingerprint.FingerprintManager$CryptoObject
 *  android.os.CancellationSignal
 *  android.os.Handler
 */
package android.support.v4.hardware.fingerprint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

@RequiresApi(value=23)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public final class FingerprintManagerCompatApi23 {
    @SuppressLint(value={"MissingPermission"})
    static void authenticate(Context context, CryptoObject cryptoObject, int n, Object object, AuthenticationCallback authenticationCallback, Handler handler) {
        if ((context = FingerprintManagerCompatApi23.getFingerprintManagerOrNull(context)) != null) {
            context.authenticate(FingerprintManagerCompatApi23.wrapCryptoObject(cryptoObject), (CancellationSignal)object, n, FingerprintManagerCompatApi23.wrapCallback(authenticationCallback), handler);
            return;
        }
    }

    private static FingerprintManager getFingerprintManagerOrNull(Context context) {
        if (context.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            return (FingerprintManager)context.getSystemService(FingerprintManager.class);
        }
        return null;
    }

    @SuppressLint(value={"MissingPermission"})
    static boolean hasEnrolledFingerprints(Context context) {
        if ((context = FingerprintManagerCompatApi23.getFingerprintManagerOrNull(context)) != null && context.hasEnrolledFingerprints()) {
            return true;
        }
        return false;
    }

    @SuppressLint(value={"MissingPermission"})
    static boolean isHardwareDetected(Context context) {
        if ((context = FingerprintManagerCompatApi23.getFingerprintManagerOrNull(context)) != null && context.isHardwareDetected()) {
            return true;
        }
        return false;
    }

    private static CryptoObject unwrapCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new CryptoObject(cryptoObject.getMac());
        }
        return null;
    }

    private static FingerprintManager.AuthenticationCallback wrapCallback(final AuthenticationCallback authenticationCallback) {
        return new FingerprintManager.AuthenticationCallback(){

            public void onAuthenticationError(int n, CharSequence charSequence) {
                authenticationCallback.onAuthenticationError(n, charSequence);
            }

            public void onAuthenticationFailed() {
                authenticationCallback.onAuthenticationFailed();
            }

            public void onAuthenticationHelp(int n, CharSequence charSequence) {
                authenticationCallback.onAuthenticationHelp(n, charSequence);
            }

            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
                authenticationCallback.onAuthenticationSucceeded(new AuthenticationResultInternal(FingerprintManagerCompatApi23.unwrapCryptoObject(authenticationResult.getCryptoObject())));
            }
        };
    }

    private static FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new FingerprintManager.CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new FingerprintManager.CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new FingerprintManager.CryptoObject(cryptoObject.getMac());
        }
        return null;
    }

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int n, CharSequence charSequence) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int n, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResultInternal authenticationResultInternal) {
        }
    }

    public static final class AuthenticationResultInternal {
        private CryptoObject mCryptoObject;

        public AuthenticationResultInternal(CryptoObject cryptoObject) {
            this.mCryptoObject = cryptoObject;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }

    public static class CryptoObject {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;

        public CryptoObject(Signature signature) {
            this.mSignature = signature;
            this.mCipher = null;
            this.mMac = null;
        }

        public CryptoObject(Cipher cipher) {
            this.mCipher = cipher;
            this.mSignature = null;
            this.mMac = null;
        }

        public CryptoObject(Mac mac) {
            this.mMac = mac;
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

