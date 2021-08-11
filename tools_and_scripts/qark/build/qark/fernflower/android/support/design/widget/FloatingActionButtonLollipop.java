package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import java.util.ArrayList;

@RequiresApi(21)
class FloatingActionButtonLollipop extends FloatingActionButtonImpl {
   private InsetDrawable mInsetDrawable;

   FloatingActionButtonLollipop(VisibilityAwareImageButton var1, ShadowViewDelegate var2) {
      super(var1, var2);
   }

   public float getElevation() {
      return this.mView.getElevation();
   }

   void getPadding(Rect var1) {
      if (this.mShadowViewDelegate.isCompatPaddingEnabled()) {
         float var2 = this.mShadowViewDelegate.getRadius();
         float var3 = this.getElevation() + this.mPressedTranslationZ;
         int var4 = (int)Math.ceil((double)ShadowDrawableWrapper.calculateHorizontalPadding(var3, var2, false));
         int var5 = (int)Math.ceil((double)ShadowDrawableWrapper.calculateVerticalPadding(var3, var2, false));
         var1.set(var4, var5, var4, var5);
      } else {
         var1.set(0, 0, 0, 0);
      }
   }

   void jumpDrawableToCurrentState() {
   }

   CircularBorderDrawable newCircularDrawable() {
      return new CircularBorderDrawableLollipop();
   }

   GradientDrawable newGradientDrawableForShape() {
      return new FloatingActionButtonLollipop.AlwaysStatefulGradientDrawable();
   }

   void onCompatShadowChanged() {
      this.updatePadding();
   }

   void onDrawableStateChanged(int[] var1) {
   }

   void onElevationsChanged(float var1, float var2) {
      if (VERSION.SDK_INT == 21) {
         if (this.mView.isEnabled()) {
            this.mView.setElevation(var1);
            if (!this.mView.isFocused() && !this.mView.isPressed()) {
               this.mView.setTranslationZ(0.0F);
            } else {
               this.mView.setTranslationZ(var2);
            }
         } else {
            this.mView.setElevation(0.0F);
            this.mView.setTranslationZ(0.0F);
         }
      } else {
         android.animation.StateListAnimator var3 = new android.animation.StateListAnimator();
         AnimatorSet var4 = new AnimatorSet();
         var4.play(ObjectAnimator.ofFloat(this.mView, "elevation", new float[]{var1}).setDuration(0L)).with(ObjectAnimator.ofFloat(this.mView, View.TRANSLATION_Z, new float[]{var2}).setDuration(100L));
         var4.setInterpolator(ANIM_INTERPOLATOR);
         var3.addState(PRESSED_ENABLED_STATE_SET, var4);
         var4 = new AnimatorSet();
         var4.play(ObjectAnimator.ofFloat(this.mView, "elevation", new float[]{var1}).setDuration(0L)).with(ObjectAnimator.ofFloat(this.mView, View.TRANSLATION_Z, new float[]{var2}).setDuration(100L));
         var4.setInterpolator(ANIM_INTERPOLATOR);
         var3.addState(FOCUSED_ENABLED_STATE_SET, var4);
         var4 = new AnimatorSet();
         ArrayList var5 = new ArrayList();
         var5.add(ObjectAnimator.ofFloat(this.mView, "elevation", new float[]{var1}).setDuration(0L));
         if (VERSION.SDK_INT >= 22 && VERSION.SDK_INT <= 24) {
            var5.add(ObjectAnimator.ofFloat(this.mView, View.TRANSLATION_Z, new float[]{this.mView.getTranslationZ()}).setDuration(100L));
         }

         var5.add(ObjectAnimator.ofFloat(this.mView, View.TRANSLATION_Z, new float[]{0.0F}).setDuration(100L));
         var4.playSequentially((Animator[])var5.toArray(new ObjectAnimator[0]));
         var4.setInterpolator(ANIM_INTERPOLATOR);
         var3.addState(ENABLED_STATE_SET, var4);
         var4 = new AnimatorSet();
         var4.play(ObjectAnimator.ofFloat(this.mView, "elevation", new float[]{0.0F}).setDuration(0L)).with(ObjectAnimator.ofFloat(this.mView, View.TRANSLATION_Z, new float[]{0.0F}).setDuration(0L));
         var4.setInterpolator(ANIM_INTERPOLATOR);
         var3.addState(EMPTY_STATE_SET, var4);
         this.mView.setStateListAnimator(var3);
      }

      if (this.mShadowViewDelegate.isCompatPaddingEnabled()) {
         this.updatePadding();
      }
   }

   void onPaddingUpdated(Rect var1) {
      if (this.mShadowViewDelegate.isCompatPaddingEnabled()) {
         this.mInsetDrawable = new InsetDrawable(this.mRippleDrawable, var1.left, var1.top, var1.right, var1.bottom);
         this.mShadowViewDelegate.setBackgroundDrawable(this.mInsetDrawable);
      } else {
         this.mShadowViewDelegate.setBackgroundDrawable(this.mRippleDrawable);
      }
   }

   boolean requirePreDrawListener() {
      return false;
   }

   void setBackgroundDrawable(ColorStateList var1, Mode var2, int var3, int var4) {
      this.mShapeDrawable = DrawableCompat.wrap(this.createShapeDrawable());
      DrawableCompat.setTintList(this.mShapeDrawable, var1);
      if (var2 != null) {
         DrawableCompat.setTintMode(this.mShapeDrawable, var2);
      }

      Object var5;
      if (var4 > 0) {
         this.mBorderDrawable = this.createBorderDrawable(var4, var1);
         var5 = new LayerDrawable(new Drawable[]{this.mBorderDrawable, this.mShapeDrawable});
      } else {
         this.mBorderDrawable = null;
         var5 = this.mShapeDrawable;
      }

      this.mRippleDrawable = new RippleDrawable(ColorStateList.valueOf(var3), (Drawable)var5, (Drawable)null);
      this.mContentBackground = this.mRippleDrawable;
      this.mShadowViewDelegate.setBackgroundDrawable(this.mRippleDrawable);
   }

   void setRippleColor(int var1) {
      if (this.mRippleDrawable instanceof RippleDrawable) {
         ((RippleDrawable)this.mRippleDrawable).setColor(ColorStateList.valueOf(var1));
      } else {
         super.setRippleColor(var1);
      }
   }

   static class AlwaysStatefulGradientDrawable extends GradientDrawable {
      public boolean isStateful() {
         return true;
      }
   }
}
