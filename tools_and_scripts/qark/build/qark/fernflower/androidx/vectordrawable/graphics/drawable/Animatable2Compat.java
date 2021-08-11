package androidx.vectordrawable.graphics.drawable;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public interface Animatable2Compat extends Animatable {
   void clearAnimationCallbacks();

   void registerAnimationCallback(Animatable2Compat.AnimationCallback var1);

   boolean unregisterAnimationCallback(Animatable2Compat.AnimationCallback var1);

   public abstract static class AnimationCallback {
      android.graphics.drawable.Animatable2.AnimationCallback mPlatformCallback;

      android.graphics.drawable.Animatable2.AnimationCallback getPlatformCallback() {
         if (this.mPlatformCallback == null) {
            this.mPlatformCallback = new android.graphics.drawable.Animatable2.AnimationCallback() {
               public void onAnimationEnd(Drawable var1) {
                  AnimationCallback.this.onAnimationEnd(var1);
               }

               public void onAnimationStart(Drawable var1) {
                  AnimationCallback.this.onAnimationStart(var1);
               }
            };
         }

         return this.mPlatformCallback;
      }

      public void onAnimationEnd(Drawable var1) {
      }

      public void onAnimationStart(Drawable var1) {
      }
   }
}
