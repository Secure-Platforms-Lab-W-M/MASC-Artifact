package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.util.AttributeSet;
import android.widget.SeekBar;
import androidx.appcompat.R.styleable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

class AppCompatSeekBarHelper extends AppCompatProgressBarHelper {
   private boolean mHasTickMarkTint = false;
   private boolean mHasTickMarkTintMode = false;
   private Drawable mTickMark;
   private ColorStateList mTickMarkTintList = null;
   private Mode mTickMarkTintMode = null;
   private final SeekBar mView;

   AppCompatSeekBarHelper(SeekBar var1) {
      super(var1);
      this.mView = var1;
   }

   private void applyTickMarkTint() {
      if (this.mTickMark != null && (this.mHasTickMarkTint || this.mHasTickMarkTintMode)) {
         Drawable var1 = DrawableCompat.wrap(this.mTickMark.mutate());
         this.mTickMark = var1;
         if (this.mHasTickMarkTint) {
            DrawableCompat.setTintList(var1, this.mTickMarkTintList);
         }

         if (this.mHasTickMarkTintMode) {
            DrawableCompat.setTintMode(this.mTickMark, this.mTickMarkTintMode);
         }

         if (this.mTickMark.isStateful()) {
            this.mTickMark.setState(this.mView.getDrawableState());
         }
      }

   }

   void drawTickMarks(Canvas var1) {
      if (this.mTickMark != null) {
         int var5 = this.mView.getMax();
         int var4 = 1;
         if (var5 > 1) {
            int var3 = this.mTickMark.getIntrinsicWidth();
            int var6 = this.mTickMark.getIntrinsicHeight();
            if (var3 >= 0) {
               var3 /= 2;
            } else {
               var3 = 1;
            }

            if (var6 >= 0) {
               var4 = var6 / 2;
            }

            this.mTickMark.setBounds(-var3, -var4, var3, var4);
            float var2 = (float)(this.mView.getWidth() - this.mView.getPaddingLeft() - this.mView.getPaddingRight()) / (float)var5;
            var4 = var1.save();
            var1.translate((float)this.mView.getPaddingLeft(), (float)(this.mView.getHeight() / 2));

            for(var3 = 0; var3 <= var5; ++var3) {
               this.mTickMark.draw(var1);
               var1.translate(var2, 0.0F);
            }

            var1.restoreToCount(var4);
         }
      }

   }

   void drawableStateChanged() {
      Drawable var1 = this.mTickMark;
      if (var1 != null && var1.isStateful() && var1.setState(this.mView.getDrawableState())) {
         this.mView.invalidateDrawable(var1);
      }

   }

   Drawable getTickMark() {
      return this.mTickMark;
   }

   ColorStateList getTickMarkTintList() {
      return this.mTickMarkTintList;
   }

   Mode getTickMarkTintMode() {
      return this.mTickMarkTintMode;
   }

   void jumpDrawablesToCurrentState() {
      Drawable var1 = this.mTickMark;
      if (var1 != null) {
         var1.jumpToCurrentState();
      }

   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      super.loadFromAttributes(var1, var2);
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, styleable.AppCompatSeekBar, var2, 0);
      Drawable var3 = var4.getDrawableIfKnown(styleable.AppCompatSeekBar_android_thumb);
      if (var3 != null) {
         this.mView.setThumb(var3);
      }

      this.setTickMark(var4.getDrawable(styleable.AppCompatSeekBar_tickMark));
      if (var4.hasValue(styleable.AppCompatSeekBar_tickMarkTintMode)) {
         this.mTickMarkTintMode = DrawableUtils.parseTintMode(var4.getInt(styleable.AppCompatSeekBar_tickMarkTintMode, -1), this.mTickMarkTintMode);
         this.mHasTickMarkTintMode = true;
      }

      if (var4.hasValue(styleable.AppCompatSeekBar_tickMarkTint)) {
         this.mTickMarkTintList = var4.getColorStateList(styleable.AppCompatSeekBar_tickMarkTint);
         this.mHasTickMarkTint = true;
      }

      var4.recycle();
      this.applyTickMarkTint();
   }

   void setTickMark(Drawable var1) {
      Drawable var2 = this.mTickMark;
      if (var2 != null) {
         var2.setCallback((Callback)null);
      }

      this.mTickMark = var1;
      if (var1 != null) {
         var1.setCallback(this.mView);
         DrawableCompat.setLayoutDirection(var1, ViewCompat.getLayoutDirection(this.mView));
         if (var1.isStateful()) {
            var1.setState(this.mView.getDrawableState());
         }

         this.applyTickMarkTint();
      }

      this.mView.invalidate();
   }

   void setTickMarkTintList(ColorStateList var1) {
      this.mTickMarkTintList = var1;
      this.mHasTickMarkTint = true;
      this.applyTickMarkTint();
   }

   void setTickMarkTintMode(Mode var1) {
      this.mTickMarkTintMode = var1;
      this.mHasTickMarkTintMode = true;
      this.applyTickMarkTint();
   }
}
