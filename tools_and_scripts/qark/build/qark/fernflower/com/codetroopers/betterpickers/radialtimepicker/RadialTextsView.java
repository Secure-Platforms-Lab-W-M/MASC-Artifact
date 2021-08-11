package com.codetroopers.betterpickers.radialtimepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.string;
import com.codetroopers.betterpickers.R.styleable;
import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.animation.AnimatorProxy;

public class RadialTextsView extends View {
   private static final String TAG = "RadialTextsView";
   private float mAmPmCircleRadiusMultiplier;
   private float mAnimationRadiusMultiplier;
   private float mCircleRadius;
   private float mCircleRadiusMultiplier;
   ObjectAnimator mDisappearAnimator;
   private boolean mDrawValuesReady;
   private boolean mHasInnerCircle;
   private float mInnerNumbersRadiusMultiplier;
   private float[] mInnerTextGridHeights;
   private float[] mInnerTextGridWidths;
   private float mInnerTextSize;
   private float mInnerTextSizeMultiplier;
   private String[] mInnerTexts;
   private RadialTextsView.InvalidateUpdateListener mInvalidateUpdateListener;
   private boolean mIs24HourMode;
   private boolean mIsInitialized = false;
   private float mNumbersRadiusMultiplier;
   private final Paint mPaint = new Paint();
   ObjectAnimator mReappearAnimator;
   private float[] mTextGridHeights;
   private boolean mTextGridValuesDirty;
   private float[] mTextGridWidths;
   private float mTextSize;
   private float mTextSizeMultiplier;
   private String[] mTexts;
   private float mTransitionEndRadiusMultiplier;
   private float mTransitionMidRadiusMultiplier;
   private Typeface mTypefaceLight;
   private Typeface mTypefaceRegular;
   private int mXCenter;
   private int mYCenter;

   public RadialTextsView(Context var1) {
      super(var1);
   }

   private void calculateGridSizes(float var1, float var2, float var3, float var4, float[] var5, float[] var6) {
      float var7 = (float)Math.sqrt(3.0D) * var1 / 2.0F;
      float var8 = var1 / 2.0F;
      this.mPaint.setTextSize(var4);
      var3 -= (this.mPaint.descent() + this.mPaint.ascent()) / 2.0F;
      var5[0] = var3 - var1;
      var6[0] = var2 - var1;
      var5[1] = var3 - var7;
      var6[1] = var2 - var7;
      var5[2] = var3 - var8;
      var6[2] = var2 - var8;
      var5[3] = var3;
      var6[3] = var2;
      var5[4] = var3 + var8;
      var6[4] = var2 + var8;
      var5[5] = var3 + var7;
      var6[5] = var2 + var7;
      var5[6] = var3 + var1;
      var6[6] = var2 + var1;
   }

   private void drawTexts(Canvas var1, float var2, Typeface var3, String[] var4, float[] var5, float[] var6) {
      this.mPaint.setTextSize(var2);
      this.mPaint.setTypeface(var3);
      var1.drawText(var4[0], var5[3], var6[0], this.mPaint);
      var1.drawText(var4[1], var5[4], var6[1], this.mPaint);
      var1.drawText(var4[2], var5[5], var6[2], this.mPaint);
      var1.drawText(var4[3], var5[6], var6[3], this.mPaint);
      var1.drawText(var4[4], var5[5], var6[4], this.mPaint);
      var1.drawText(var4[5], var5[4], var6[5], this.mPaint);
      var1.drawText(var4[6], var5[3], var6[6], this.mPaint);
      var1.drawText(var4[7], var5[2], var6[5], this.mPaint);
      var1.drawText(var4[8], var5[1], var6[4], this.mPaint);
      var1.drawText(var4[9], var5[0], var6[3], this.mPaint);
      var1.drawText(var4[10], var5[1], var6[2], this.mPaint);
      var1.drawText(var4[11], var5[2], var6[1], this.mPaint);
   }

   private void renderAnimations() {
      PropertyValuesHolder var4 = PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", Keyframe.ofFloat(0.0F, 1.0F), Keyframe.ofFloat(0.2F, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0F, this.mTransitionEndRadiusMultiplier));
      PropertyValuesHolder var5 = PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0F, 1.0F), Keyframe.ofFloat(1.0F, 0.0F));
      Object var3;
      if (AnimatorProxy.NEEDS_PROXY) {
         var3 = AnimatorProxy.wrap(this);
      } else {
         var3 = this;
      }

      ObjectAnimator var6 = ObjectAnimator.ofPropertyValuesHolder(var3, var4, var5).setDuration((long)500);
      this.mDisappearAnimator = var6;
      var6.addUpdateListener(this.mInvalidateUpdateListener);
      int var2 = (int)((float)500 * (1.0F + 0.25F));
      float var1 = (float)500 * 0.25F / (float)var2;
      var4 = PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", Keyframe.ofFloat(0.0F, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(var1, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(1.0F - (1.0F - var1) * 0.2F, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0F, 1.0F));
      var5 = PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0F, 0.0F), Keyframe.ofFloat(var1, 0.0F), Keyframe.ofFloat(1.0F, 1.0F));
      if (AnimatorProxy.NEEDS_PROXY) {
         var3 = AnimatorProxy.wrap(this);
      } else {
         var3 = this;
      }

      var6 = ObjectAnimator.ofPropertyValuesHolder(var3, var4, var5).setDuration((long)var2);
      this.mReappearAnimator = var6;
      var6.addUpdateListener(this.mInvalidateUpdateListener);
   }

   public ObjectAnimator getDisappearAnimator() {
      if (this.mIsInitialized && this.mDrawValuesReady) {
         ObjectAnimator var1 = this.mDisappearAnimator;
         if (var1 != null) {
            return var1;
         }
      }

      Log.e("RadialTextsView", "RadialTextView was not ready for animation.");
      return null;
   }

   public ObjectAnimator getReappearAnimator() {
      if (this.mIsInitialized && this.mDrawValuesReady) {
         ObjectAnimator var1 = this.mReappearAnimator;
         if (var1 != null) {
            return var1;
         }
      }

      Log.e("RadialTextsView", "RadialTextView was not ready for animation.");
      return null;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public void initialize(Resources var1, String[] var2, String[] var3, boolean var4, boolean var5) {
      if (this.mIsInitialized) {
         Log.e("RadialTextsView", "This RadialTextsView may only be initialized once.");
      } else {
         this.mPaint.setColor(var1.getColor(color.numbers_text_color));
         String var9 = var1.getString(string.radial_numbers_typeface);
         boolean var8 = false;
         this.mTypefaceLight = Typeface.create(var9, 0);
         this.mTypefaceRegular = Typeface.create(var1.getString(string.sans_serif), 0);
         this.mPaint.setAntiAlias(true);
         this.mPaint.setTextAlign(Align.CENTER);
         this.mTexts = var2;
         this.mInnerTexts = var3;
         this.mIs24HourMode = var4;
         if (var3 != null) {
            var8 = true;
         }

         this.mHasInnerCircle = var8;
         if (var4) {
            this.mCircleRadiusMultiplier = Float.parseFloat(var1.getString(string.circle_radius_multiplier_24HourMode));
         } else {
            this.mCircleRadiusMultiplier = Float.parseFloat(var1.getString(string.circle_radius_multiplier));
            this.mAmPmCircleRadiusMultiplier = Float.parseFloat(var1.getString(string.ampm_circle_radius_multiplier));
         }

         this.mTextGridHeights = new float[7];
         this.mTextGridWidths = new float[7];
         if (this.mHasInnerCircle) {
            this.mNumbersRadiusMultiplier = Float.parseFloat(var1.getString(string.numbers_radius_multiplier_outer));
            this.mTextSizeMultiplier = Float.parseFloat(var1.getString(string.text_size_multiplier_outer));
            this.mInnerNumbersRadiusMultiplier = Float.parseFloat(var1.getString(string.numbers_radius_multiplier_inner));
            this.mInnerTextSizeMultiplier = Float.parseFloat(var1.getString(string.text_size_multiplier_inner));
            this.mInnerTextGridHeights = new float[7];
            this.mInnerTextGridWidths = new float[7];
         } else {
            this.mNumbersRadiusMultiplier = Float.parseFloat(var1.getString(string.numbers_radius_multiplier_normal));
            this.mTextSizeMultiplier = Float.parseFloat(var1.getString(string.text_size_multiplier_normal));
         }

         this.mAnimationRadiusMultiplier = 1.0F;
         byte var7 = -1;
         byte var6;
         if (var5) {
            var6 = -1;
         } else {
            var6 = 1;
         }

         this.mTransitionMidRadiusMultiplier = (float)var6 * 0.05F + 1.0F;
         var6 = var7;
         if (var5) {
            var6 = 1;
         }

         this.mTransitionEndRadiusMultiplier = (float)var6 * 0.3F + 1.0F;
         this.mInvalidateUpdateListener = new RadialTextsView.InvalidateUpdateListener();
         this.mTextGridValuesDirty = true;
         this.mIsInitialized = true;
      }
   }

   public void onDraw(Canvas var1) {
      if (this.getWidth() != 0) {
         if (this.mIsInitialized) {
            if (!this.mDrawValuesReady) {
               this.mXCenter = this.getWidth() / 2;
               int var4 = this.getHeight() / 2;
               this.mYCenter = var4;
               float var2 = (float)Math.min(this.mXCenter, var4) * this.mCircleRadiusMultiplier;
               this.mCircleRadius = var2;
               if (!this.mIs24HourMode) {
                  float var3 = this.mAmPmCircleRadiusMultiplier;
                  this.mYCenter = (int)((float)this.mYCenter - var2 * var3 / 2.0F);
               }

               var2 = this.mCircleRadius;
               this.mTextSize = this.mTextSizeMultiplier * var2;
               if (this.mHasInnerCircle) {
                  this.mInnerTextSize = var2 * this.mInnerTextSizeMultiplier;
               }

               this.renderAnimations();
               this.mTextGridValuesDirty = true;
               this.mDrawValuesReady = true;
            }

            if (this.mTextGridValuesDirty) {
               this.calculateGridSizes(this.mCircleRadius * this.mNumbersRadiusMultiplier * this.mAnimationRadiusMultiplier, (float)this.mXCenter, (float)this.mYCenter, this.mTextSize, this.mTextGridHeights, this.mTextGridWidths);
               if (this.mHasInnerCircle) {
                  this.calculateGridSizes(this.mCircleRadius * this.mInnerNumbersRadiusMultiplier * this.mAnimationRadiusMultiplier, (float)this.mXCenter, (float)this.mYCenter, this.mInnerTextSize, this.mInnerTextGridHeights, this.mInnerTextGridWidths);
               }

               this.mTextGridValuesDirty = false;
            }

            this.drawTexts(var1, this.mTextSize, this.mTypefaceLight, this.mTexts, this.mTextGridWidths, this.mTextGridHeights);
            if (this.mHasInnerCircle) {
               this.drawTexts(var1, this.mInnerTextSize, this.mTypefaceRegular, this.mInnerTexts, this.mInnerTextGridWidths, this.mInnerTextGridHeights);
            }

         }
      }
   }

   public void setAnimationRadiusMultiplier(float var1) {
      this.mAnimationRadiusMultiplier = var1;
      this.mTextGridValuesDirty = true;
   }

   void setTheme(TypedArray var1) {
      this.mPaint.setColor(var1.getColor(styleable.BetterPickersDialogs_bpRadialTextColor, ContextCompat.getColor(this.getContext(), color.bpBlue)));
   }

   private class InvalidateUpdateListener implements ValueAnimator.AnimatorUpdateListener {
      private InvalidateUpdateListener() {
      }

      // $FF: synthetic method
      InvalidateUpdateListener(Object var2) {
         this();
      }

      public void onAnimationUpdate(ValueAnimator var1) {
         RadialTextsView.this.invalidate();
      }
   }
}
