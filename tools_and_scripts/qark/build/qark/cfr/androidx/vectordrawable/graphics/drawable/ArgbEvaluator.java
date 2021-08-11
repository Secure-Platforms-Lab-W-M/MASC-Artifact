/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.TypeEvaluator
 */
package androidx.vectordrawable.graphics.drawable;

import android.animation.TypeEvaluator;

public class ArgbEvaluator
implements TypeEvaluator {
    private static final ArgbEvaluator sInstance = new ArgbEvaluator();

    public static ArgbEvaluator getInstance() {
        return sInstance;
    }

    public Object evaluate(float f, Object object, Object object2) {
        int n = (Integer)object;
        float f2 = (float)(n >> 24 & 255) / 255.0f;
        float f3 = (float)(n >> 16 & 255) / 255.0f;
        float f4 = (float)(n >> 8 & 255) / 255.0f;
        float f5 = (float)(n & 255) / 255.0f;
        n = (Integer)object2;
        float f6 = (float)(n >> 24 & 255) / 255.0f;
        float f7 = (float)(n >> 16 & 255) / 255.0f;
        float f8 = (float)(n >> 8 & 255) / 255.0f;
        float f9 = (float)(n & 255) / 255.0f;
        f3 = (float)Math.pow(f3, 2.2);
        f4 = (float)Math.pow(f4, 2.2);
        f5 = (float)Math.pow(f5, 2.2);
        f7 = (float)Math.pow(f7, 2.2);
        f8 = (float)Math.pow(f8, 2.2);
        f9 = (float)Math.pow(f9, 2.2);
        f3 = (float)Math.pow((f7 - f3) * f + f3, 0.45454545454545453);
        f4 = (float)Math.pow((f8 - f4) * f + f4, 0.45454545454545453);
        f9 = (float)Math.pow((f9 - f5) * f + f5, 0.45454545454545453);
        return Math.round(((f6 - f2) * f + f2) * 255.0f) << 24 | Math.round(f3 * 255.0f) << 16 | Math.round(f4 * 255.0f) << 8 | Math.round(f9 * 255.0f);
    }
}

