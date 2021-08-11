/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager
 *  android.hardware.fingerprint.FingerprintManager
 *  android.hardware.fingerprint.FingerprintManager$AuthenticationCallback
 *  android.hardware.fingerprint.FingerprintManager$AuthenticationResult
 *  android.hardware.fingerprint.FingerprintManager$CryptoObject
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 *  android.os.Handler
 */
package androidx.core.hardware.fingerprint;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Handler;
import androidx.core.os.CancellationSignal;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

@Deprecated
public final class FingerprintManagerCompat {
    private final Context mContext;

    private FingerprintManagerCompat(Context context) {
        this.mContext = context;
    }

    public static FingerprintManagerCompat from(Context context) {
        return new FingerprintManagerCompat(context);
    }

    private static FingerprintManager getFingerprintManagerOrNull(Context context) {
        if (Build.VERSION.SDK_INT == 23) {
            return (FingerprintManager)context.getSystemService(FingerprintManager.class);
        }
        if (Build.VERSION.SDK_INT > 23 && context.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            return (FingerprintManager)context.getSystemService(FingerprintManager.class);
        }
        return null;
    }

    static CryptoObject unwrapCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
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
                authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(FingerprintManagerCompat.unwrapCryptoObject(authenticationResult.getCryptoObject())));
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

    public void authenticate(CryptoObject cryptoObject, int n, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler) {
        FingerprintManager fingerprintManager;
        if (Build.VERSION.SDK_INT >= 23 && (fingerprintManager = FingerprintManagerCompat.getFingerprintManagerOrNull(this.mContext)) != null) {
            cancellationSignal = cancellationSignal != null ? (android.os.CancellationSignal)cancellationSignal.getCancellationSignalObject() : null;
            fingerprintManager.authenticate(FingerprintManagerCompat.wrapCryptoObject(cryptoObject), (android.os.CancellationSignal)cancellationSignal, n, FingerprintManagerCompat.wrapCallback(authenticationCallback), handler);
        }
    }

    public boolean hasEnrolledFingerprints() {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        if (n >= 23) {
            FingerprintManager fingerprintManager = FingerprintManagerCompat.getFingerprintManagerOrNull(this.mContext);
            boolean bl2 = bl;
            if (fingerprintManager != null) {
                bl2 = bl;
                if (fingerprintManager.hasEnrolledFingerprints()) {
                    bl2 = true;
                }
            }
            return bl2;
        }
        return false;
    }

    public boolean isHardwareDetected() {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        if (n >= 23) {
            FingerprintManager fingerprintManager = FingerprintManagerCompat.getFingerprintManagerOrNull(this.mContext);
            boolean bl2 = bl;
            if (fingerprintManager != null) {
                bl2 = bl;
                if (fingerprintManager.isHardwareDetected()) {
                    bl2 = true;
                }
            }
            return bl2;
        }
        return false;
    }

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int n, CharSequence charSequence) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int n, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
        }
    }

    public static final class AuthenticationResult {
        private final CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject cryptoObject) {
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

