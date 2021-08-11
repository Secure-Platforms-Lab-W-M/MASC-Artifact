package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;

@TargetApi(14)
@RequiresApi(14)
class FloatingActionButtonIcs extends FloatingActionButtonGingerbread {
   private float mRotation;

   FloatingActionButtonIcs(VisibilityAwareImageButton var1, ShadowViewDelegate var2, ValueAnimatorCompat.Creator var3) {
      super(var1, var2, var3);
      this.mRotation = this.mView.getRotation();
   }

   private boolean shouldAnimateVisibilityChange() {
      return ViewCompat.isLaidOut(this.mView) && !this.mView.isInEditMode();
   }

   private void updateFromViewRotation() {
      if (VERSION.SDK_INT == 19) {
         if (this.mRotation % 90.0F != 0.0F) {
            if (this.mView.getLayerType() != 1) {
               this.mView.setLayerType(1, (Paint)null);
            }
         } else if (this.mView.getLayerType() != 0) {
            this.mView.setLayerType(0, (Paint)null);
         }
      }

      if (this.mShadowDrawable != null) {
         this.mShadowDrawable.setRotation(-this.mRotation);
      }

      if (this.mBorderDrawable != null) {
         this.mBorderDrawable.setRotation(-this.mRotation);
      }

   }

   void hide(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeHidden()) {
         this.mView.animate().cancel();
         if (this.shouldAnimateVisibilityChange()) {
            this.mAnimState = 1;
            this.mView.animate().scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setDuration(200L).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
               private boolean mCancelled;

               public void onAnimationCancel(Animator var1x) {
                  this.mCancelled = true;
               }

               public void onAnimationEnd(Animator var1x) {
                  FloatingActionButtonIcs.this.mAnimState = 0;
                  if (!this.mCancelled) {
                     VisibilityAwareImageButton var3 = FloatingActionButtonIcs.this.mView;
                     byte var2x;
                     if (var2) {
                        var2x = 8;
                     } else {
                        var2x = 4;
                     }

                     var3.internalSetVisibility(var2x, var2);
                     if (var1 != null) {
                        var1.onHidden();
                     }
                  }

               }

               public void onAnimationStart(Animator var1x) {
                  FloatingActionButtonIcs.this.mView.internalSetVisibility(0, var2);
                  this.mCancelled = false;
               }
            });
            return;
         }

         VisibilityAwareImageButton var4 = this.mView;
         byte var3;
         if (var2) {
            var3 = 8;
         } else {
            var3 = 4;
         }

         var4.internalSetVisibility(var3, var2);
         if (var1 != null) {
            var1.onHidden();
            return;
         }
      }

   }

   void onPreDraw() {
      float var1 = this.mView.getRotation();
      if (this.mRotation != var1) {
         this.mRotation = var1;
         this.updateFromViewRotation();
      }

   }

   boolean requirePreDrawListener() {
      return true;
   }

   void show(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener var1, final boolean var2) {
      if (!this.isOrWillBeShown()) {
         this.mView.animate().cancel();
         if (this.shouldAnimateVisibilityChange()) {
            this.mAnimState = 2;
            if (this.mView.getVisibility() != 0) {
               this.mView.setAlpha(0.0F);
               this.mView.setScaleY(0.0F);
               this.mView.setScaleX(0.0F);
            }

            this.mView.animate().scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setDuration(200L).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  FloatingActionButtonIcs.this.mAnimState = 0;
                  if (var1 != null) {
                     var1.onShown();
                  }

               }

               public void onAnimationStart(Animator var1x) {
                  FloatingActionButtonIcs.this.mView.internalSetVisibility(0, var2);
               }
            });
            return;
         }

         this.mView.internalSetVisibility(0, var2);
         this.mView.setAlpha(1.0F);
         this.mView.setScaleY(1.0F);
         this.mView.setScaleX(1.0F);
         if (var1 != null) {
            var1.onShown();
            return;
         }
      }

   }
}
