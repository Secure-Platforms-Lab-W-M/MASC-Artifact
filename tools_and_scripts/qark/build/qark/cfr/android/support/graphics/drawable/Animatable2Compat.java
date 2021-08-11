/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.Animatable2
 *  android.graphics.drawable.Animatable2$AnimationCallback
 *  android.graphics.drawable.Drawable
 */
package android.support.graphics.drawable;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

public interface Animatable2Compat
extends Animatable {
    public void clearAnimationCallbacks();

    public void registerAnimationCallback(@NonNull AnimationCallback var1);

    public boolean unregisterAnimationCallback(@NonNull AnimationCallback var1);

    public static abstract class AnimationCallback {
        Animatable2.AnimationCallback mPlatformCallback;

        @RequiresApi(value=23)
        Animatable2.AnimationCallback getPlatformCallback() {
            if (this.mPlatformCallback == null) {
                this.mPlatformCallback = new Animatable2.AnimationCallback(){

                    public void onAnimationEnd(Drawable drawable2) {
                        AnimationCallback.this.onAnimationEnd(drawable2);
                    }

                    public void onAnimationStart(Drawable drawable2) {
                        AnimationCallback.this.onAnimationStart(drawable2);
                    }
                };
            }
            return this.mPlatformCallback;
        }

        public void onAnimationEnd(Drawable drawable2) {
        }

        public void onAnimationStart(Drawable drawable2) {
        }

    }

}

