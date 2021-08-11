package com.codetroopers.betterpickers.radialtimepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
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

public class RadialSelectorView extends View {
   private static final int FULL_ALPHA = 255;
   private static final String TAG = "RadialSelectorView";
   private float mAmPmCircleRadiusMultiplier;
   private float mAnimationRadiusMultiplier;
   private int mCircleRadius;
   private float mCircleRadiusMultiplier;
   private boolean mDrawValuesReady;
   private boolean mForceDrawDot;
   private boolean mHasInnerCircle;
   private float mInnerNumbersRadiusMultiplier;
   private RadialSelectorView.InvalidateUpdateListener mInvalidateUpdateListener;
   private boolean mIs24HourMode;
   private boolean mIsInitialized = false;
   private int mLineLength;
   private float mNumbersRadiusMultiplier;
   private float mOuterNumbersRadiusMultiplier;
   private final Paint mPaint = new Paint();
   private int mSelectionAlpha;
   private int mSelectionDegrees;
   private double mSelectionRadians;
   private int mSelectionRadius;
   private float mSelectionRadiusMultiplier;
   private float mTransitionEndRadiusMultiplier;
   private float mTransitionMidRadiusMultiplier;
   private int mXCenter;
   private int mYCenter;

   public RadialSelectorView(Context var1) {
      super(var1);
   }

   public int getDegreesFromCoords(float var1, float var2, boolean var3, Boolean[] var4) {
      if (!this.mDrawValuesReady) {
         return -1;
      } else {
         int var10 = this.mYCenter;
         float var7 = (float)var10;
         float var8 = (float)var10;
         var10 = this.mXCenter;
         double var5 = Math.sqrt((double)((var2 - var7) * (var2 - var8) + (var1 - (float)var10) * (var1 - (float)var10)));
         boolean var15 = this.mHasInnerCircle;
         boolean var11 = true;
         int var12;
         int var13;
         if (var15) {
            if (var3) {
               if ((int)Math.abs(var5 - (double)((int)((float)this.mCircleRadius * this.mInnerNumbersRadiusMultiplier))) <= (int)Math.abs(var5 - (double)((int)((float)this.mCircleRadius * this.mOuterNumbersRadiusMultiplier)))) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var4[0] = var3;
            } else {
               int var14 = this.mCircleRadius;
               var8 = (float)var14;
               var7 = this.mInnerNumbersRadiusMultiplier;
               var10 = (int)(var8 * var7);
               var12 = this.mSelectionRadius;
               var8 = (float)var14;
               float var9 = this.mOuterNumbersRadiusMultiplier;
               var13 = (int)(var8 * var9);
               var14 = (int)((float)var14 * ((var9 + var7) / 2.0F));
               if (var5 >= (double)(var10 - var12) && var5 <= (double)var14) {
                  var4[0] = true;
               } else {
                  if (var5 > (double)(var13 + var12) || var5 < (double)var14) {
                     return -1;
                  }

                  var4[0] = false;
               }
            }
         } else if (!var3 && (int)Math.abs(var5 - (double)this.mLineLength) > (int)((float)this.mCircleRadius * (1.0F - this.mNumbersRadiusMultiplier))) {
            return -1;
         }

         var13 = (int)(180.0D * Math.asin((double)Math.abs(var2 - (float)this.mYCenter) / var5) / 3.141592653589793D);
         boolean var16;
         if (var1 > (float)this.mXCenter) {
            var16 = true;
         } else {
            var16 = false;
         }

         if (var2 >= (float)this.mYCenter) {
            var11 = false;
         }

         if (var16 && var11) {
            return 90 - var13;
         } else if (var16 && !var11) {
            return var13 + 90;
         } else if (!var16 && !var11) {
            return 270 - var13;
         } else {
            var12 = var13;
            if (!var16) {
               var12 = var13;
               if (var11) {
                  var12 = var13 + 270;
               }
            }

            return var12;
         }
      }
   }

   public ObjectAnimator getDisappearAnimator() {
      if (this.mIsInitialized && this.mDrawValuesReady) {
         PropertyValuesHolder var2 = PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", Keyframe.ofFloat(0.0F, 1.0F), Keyframe.ofFloat(0.2F, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0F, this.mTransitionEndRadiusMultiplier));
         PropertyValuesHolder var3 = PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0F, 1.0F), Keyframe.ofFloat(1.0F, 0.0F));
         Object var1;
         if (AnimatorProxy.NEEDS_PROXY) {
            var1 = AnimatorProxy.wrap(this);
         } else {
            var1 = this;
         }

         ObjectAnimator var4 = ObjectAnimator.ofPropertyValuesHolder(var1, var2, var3).setDuration((long)500);
         var4.addUpdateListener(this.mInvalidateUpdateListener);
         return var4;
      } else {
         Log.e("RadialSelectorView", "RadialSelectorView was not ready for animation.");
         return null;
      }
   }

   public ObjectAnimator getReappearAnimator() {
      if (this.mIsInitialized && this.mDrawValuesReady) {
         int var2 = (int)((float)500 * (1.0F + 0.25F));
         float var1 = (float)500 * 0.25F / (float)var2;
         PropertyValuesHolder var4 = PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", Keyframe.ofFloat(0.0F, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(var1, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(1.0F - (1.0F - var1) * 0.2F, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0F, 1.0F));
         PropertyValuesHolder var5 = PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0F, 0.0F), Keyframe.ofFloat(var1, 0.0F), Keyframe.ofFloat(1.0F, 1.0F));
         Object var3;
         if (AnimatorProxy.NEEDS_PROXY) {
            var3 = AnimatorProxy.wrap(this);
         } else {
            var3 = this;
         }

         ObjectAnimator var6 = ObjectAnimator.ofPropertyValuesHolder(var3, var4, var5).setDuration((long)var2);
         var6.addUpdateListener(this.mInvalidateUpdateListener);
         return var6;
      } else {
         Log.e("RadialSelectorView", "RadialSelectorView was not ready for animation.");
         return null;
      }
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   public void initialize(Context var1, boolean var2, boolean var3, boolean var4, int var5, boolean var6) {
      if (this.mIsInitialized) {
         Log.e("RadialSelectorView", "This RadialSelectorView may only be initialized once.");
      } else {
         Resources var9 = var1.getResources();
         this.mPaint.setAntiAlias(true);
         this.mIs24HourMode = var2;
         if (var2) {
            this.mCircleRadiusMultiplier = Float.parseFloat(var9.getString(string.circle_radius_multiplier_24HourMode));
         } else {
            this.mCircleRadiusMultiplier = Float.parseFloat(var9.getString(string.circle_radius_multiplier));
            this.mAmPmCircleRadiusMultiplier = Float.parseFloat(var9.getString(string.ampm_circle_radius_multiplier));
         }

         this.mHasInnerCircle = var3;
         if (var3) {
            this.mInnerNumbersRadiusMultiplier = Float.parseFloat(var9.getString(string.numbers_radius_multiplier_inner));
            this.mOuterNumbersRadiusMultiplier = Float.parseFloat(var9.getString(string.numbers_radius_multiplier_outer));
         } else {
            this.mNumbersRadiusMultiplier = Float.parseFloat(var9.getString(string.numbers_radius_multiplier_normal));
         }

         this.mSelectionRadiusMultiplier = Float.parseFloat(var9.getString(string.selection_radius_multiplier));
         this.mAnimationRadiusMultiplier = 1.0F;
         byte var8 = -1;
         byte var7;
         if (var4) {
            var7 = -1;
         } else {
            var7 = 1;
         }

         this.mTransitionMidRadiusMultiplier = (float)var7 * 0.05F + 1.0F;
         var7 = var8;
         if (var4) {
            var7 = 1;
         }

         this.mTransitionEndRadiusMultiplier = (float)var7 * 0.3F + 1.0F;
         this.mInvalidateUpdateListener = new RadialSelectorView.InvalidateUpdateListener();
         this.setSelection(var5, var6, false);
         this.mIsInitialized = true;
      }
   }

   public void onDraw(Canvas var1) {
      if (this.getWidth() != 0) {
         if (this.mIsInitialized) {
            boolean var5 = this.mDrawValuesReady;
            boolean var2 = true;
            int var3;
            if (!var5) {
               this.mXCenter = this.getWidth() / 2;
               var3 = this.getHeight() / 2;
               this.mYCenter = var3;
               var3 = (int)((float)Math.min(this.mXCenter, var3) * this.mCircleRadiusMultiplier);
               this.mCircleRadius = var3;
               if (!this.mIs24HourMode) {
                  var3 = (int)((float)var3 * this.mAmPmCircleRadiusMultiplier);
                  this.mYCenter -= var3 / 2;
               }

               this.mSelectionRadius = (int)((float)this.mCircleRadius * this.mSelectionRadiusMultiplier);
               this.mDrawValuesReady = true;
            }

            var3 = (int)((float)this.mCircleRadius * this.mNumbersRadiusMultiplier * this.mAnimationRadiusMultiplier);
            this.mLineLength = var3;
            var3 = this.mXCenter + (int)((double)var3 * Math.sin(this.mSelectionRadians));
            int var4 = this.mYCenter - (int)((double)this.mLineLength * Math.cos(this.mSelectionRadians));
            this.mPaint.setAlpha(this.mSelectionAlpha);
            var1.drawCircle((float)var3, (float)var4, (float)this.mSelectionRadius, this.mPaint);
            var5 = this.mForceDrawDot;
            if (this.mSelectionDegrees % 30 == 0) {
               var2 = false;
            }

            int var6;
            if (var2 | var5) {
               this.mPaint.setAlpha(255);
               var1.drawCircle((float)var3, (float)var4, (float)(this.mSelectionRadius * 2 / 7), this.mPaint);
               var6 = var4;
            } else {
               var6 = this.mLineLength - this.mSelectionRadius;
               var3 = this.mXCenter + (int)((double)var6 * Math.sin(this.mSelectionRadians));
               var6 = this.mYCenter - (int)((double)var6 * Math.cos(this.mSelectionRadians));
            }

            this.mPaint.setAlpha(255);
            this.mPaint.setStrokeWidth(1.0F);
            var1.drawLine((float)this.mXCenter, (float)this.mYCenter, (float)var3, (float)var6, this.mPaint);
         }
      }
   }

   public void setAnimationRadiusMultiplier(float var1) {
      this.mAnimationRadiusMultiplier = var1;
   }

   public void setSelection(int var1, boolean var2, boolean var3) {
      this.mSelectionDegrees = var1;
      this.mSelectionRadians = (double)var1 * 3.141592653589793D / 180.0D;
      this.mForceDrawDot = var3;
      if (this.mHasInnerCircle) {
         if (var2) {
            this.mNumbersRadiusMultiplier = this.mInnerNumbersRadiusMultiplier;
            return;
         }

         this.mNumbersRadiusMultiplier = this.mOuterNumbersRadiusMultiplier;
      }

   }

   void setTheme(TypedArray var1) {
      this.mPaint.setColor(var1.getColor(styleable.BetterPickersDialogs_bpRadialPointerColor, ContextCompat.getColor(this.getContext(), color.bpBlue)));
      this.mSelectionAlpha = var1.getInt(styleable.BetterPickersDialogs_bpRadialPointerAlpha, 35);
   }

   private class InvalidateUpdateListener implements ValueAnimator.AnimatorUpdateListener {
      private InvalidateUpdateListener() {
      }

      // $FF: synthetic method
      InvalidateUpdateListener(Object var2) {
         this();
      }

      public void onAnimationUpdate(ValueAnimator var1) {
         RadialSelectorView.this.invalidate();
      }
   }
}
