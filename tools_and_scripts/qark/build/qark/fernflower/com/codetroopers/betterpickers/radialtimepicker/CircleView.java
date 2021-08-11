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

public class CircleView extends View {
   private static final String TAG = "CircleView";
   private float mAmPmCircleRadiusMultiplier;
   private int mCentralDotColor;
   private int mCircleColor;
   private int mCircleRadius;
   private float mCircleRadiusMultiplier;
   private boolean mDrawValuesReady;
   private boolean mIs24HourMode;
   private boolean mIsInitialized;
   private final Paint mPaint = new Paint();
   private int mXCenter;
   private int mYCenter;

   public CircleView(Context var1) {
      super(var1);
      Resources var2 = var1.getResources();
      this.mCircleColor = var2.getColor(color.bpWhite);
      this.mCentralDotColor = var2.getColor(color.numbers_text_color);
      this.mPaint.setAntiAlias(true);
      this.mIsInitialized = false;
   }

   public void initialize(Context var1, boolean var2) {
      if (this.mIsInitialized) {
         Log.e("CircleView", "CircleView may only be initialized once.");
      } else {
         Resources var3 = var1.getResources();
         this.mIs24HourMode = var2;
         if (var2) {
            this.mCircleRadiusMultiplier = Float.parseFloat(var3.getString(string.circle_radius_multiplier_24HourMode));
         } else {
            this.mCircleRadiusMultiplier = Float.parseFloat(var3.getString(string.circle_radius_multiplier));
            this.mAmPmCircleRadiusMultiplier = Float.parseFloat(var3.getString(string.ampm_circle_radius_multiplier));
         }

         this.mIsInitialized = true;
      }
   }

   public void onDraw(Canvas var1) {
      if (this.getWidth() != 0) {
         if (this.mIsInitialized) {
            if (!this.mDrawValuesReady) {
               this.mXCenter = this.getWidth() / 2;
               int var2 = this.getHeight() / 2;
               this.mYCenter = var2;
               var2 = (int)((float)Math.min(this.mXCenter, var2) * this.mCircleRadiusMultiplier);
               this.mCircleRadius = var2;
               if (!this.mIs24HourMode) {
                  var2 = (int)((float)var2 * this.mAmPmCircleRadiusMultiplier);
                  this.mYCenter -= var2 / 2;
               }

               this.mDrawValuesReady = true;
            }

            this.mPaint.setColor(this.mCircleColor);
            var1.drawCircle((float)this.mXCenter, (float)this.mYCenter, (float)this.mCircleRadius, this.mPaint);
            this.mPaint.setColor(this.mCentralDotColor);
            var1.drawCircle((float)this.mXCenter, (float)this.mYCenter, 2.0F, this.mPaint);
         }
      }
   }

   void setTheme(TypedArray var1) {
      this.mCircleColor = var1.getColor(styleable.BetterPickersDialogs_bpRadialBackgroundColor, ContextCompat.getColor(this.getContext(), color.radial_gray_light));
      this.mCentralDotColor = var1.getColor(styleable.BetterPickersDialogs_bpRadialTextColor, ContextCompat.getColor(this.getContext(), color.bpBlue));
   }
}
