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
import java.text.DateFormatSymbols;

public class AmPmCirclesView extends View {
   // $FF: renamed from: AM int
   private static final int field_195 = 0;
   // $FF: renamed from: PM int
   private static final int field_196 = 1;
   private static final String TAG = "AmPmCirclesView";
   private int mAmOrPm;
   private int mAmOrPmPressed;
   private int mAmPmCircleRadius;
   private float mAmPmCircleRadiusMultiplier;
   private int mAmPmTextColor;
   private int mAmPmYCenter;
   private String mAmText;
   private int mAmXCenter;
   private float mCircleRadiusMultiplier;
   private boolean mDrawValuesReady;
   private boolean mIsInitialized = false;
   private final Paint mPaint = new Paint();
   private String mPmText;
   private int mPmXCenter;
   private int mSelectedAlpha;
   private int mSelectedColor;
   private int mUnselectedAlpha;
   private int mUnselectedColor;

   public AmPmCirclesView(Context var1) {
      super(var1);
   }

   public int getIsTouchingAmOrPm(float var1, float var2) {
      if (!this.mDrawValuesReady) {
         return -1;
      } else {
         int var3 = this.mAmPmYCenter;
         var3 = (int)((var2 - (float)var3) * (var2 - (float)var3));
         int var4 = this.mAmXCenter;
         if ((int)Math.sqrt((double)((var1 - (float)var4) * (var1 - (float)var4) + (float)var3)) <= this.mAmPmCircleRadius) {
            return 0;
         } else {
            var4 = this.mPmXCenter;
            return (int)Math.sqrt((double)((var1 - (float)var4) * (var1 - (float)var4) + (float)var3)) <= this.mAmPmCircleRadius ? 1 : -1;
         }
      }
   }

   public void initialize(Context var1, int var2) {
      if (this.mIsInitialized) {
         Log.e("AmPmCirclesView", "AmPmCirclesView may only be initialized once.");
      } else {
         Resources var4 = var1.getResources();
         this.mUnselectedColor = var4.getColor(color.bpWhite);
         this.mSelectedColor = var4.getColor(color.bpBlue);
         this.mAmPmTextColor = var4.getColor(color.ampm_text_color);
         Typeface var3 = Typeface.create(var4.getString(string.sans_serif), 0);
         this.mPaint.setTypeface(var3);
         this.mPaint.setAntiAlias(true);
         this.mPaint.setTextAlign(Align.CENTER);
         this.mCircleRadiusMultiplier = Float.parseFloat(var4.getString(string.circle_radius_multiplier));
         this.mAmPmCircleRadiusMultiplier = Float.parseFloat(var4.getString(string.ampm_circle_radius_multiplier));
         String[] var5 = (new DateFormatSymbols()).getAmPmStrings();
         this.mAmText = var5[0];
         this.mPmText = var5[1];
         this.setAmOrPm(var2);
         this.mAmOrPmPressed = -1;
         this.mIsInitialized = true;
      }
   }

   public void onDraw(Canvas var1) {
      if (this.getWidth() != 0) {
         if (this.mIsInitialized) {
            int var2;
            int var3;
            int var4;
            int var5;
            if (!this.mDrawValuesReady) {
               var2 = this.getWidth() / 2;
               var3 = this.getHeight() / 2;
               var4 = (int)((float)Math.min(var2, var3) * this.mCircleRadiusMultiplier);
               var5 = (int)((float)var4 * this.mAmPmCircleRadiusMultiplier);
               this.mAmPmCircleRadius = var5;
               var5 = var5 * 3 / 4;
               this.mPaint.setTextSize((float)var5);
               var5 = this.mAmPmCircleRadius;
               this.mAmPmYCenter = var3 - var5 / 2 + var4;
               this.mAmXCenter = var2 - var4 + var5;
               this.mPmXCenter = var2 + var4 - var5;
               this.mDrawValuesReady = true;
            }

            int var6 = this.mUnselectedColor;
            int var7 = this.mUnselectedAlpha;
            var5 = this.mUnselectedColor;
            var4 = this.mUnselectedAlpha;
            int var8 = this.mAmOrPm;
            if (var8 == 0) {
               var2 = this.mSelectedColor;
               var3 = this.mSelectedAlpha;
            } else {
               var2 = var6;
               var3 = var7;
               if (var8 == 1) {
                  var5 = this.mSelectedColor;
                  var4 = this.mSelectedAlpha;
                  var3 = var7;
                  var2 = var6;
               }
            }

            var8 = this.mAmOrPmPressed;
            if (var8 == 0) {
               var6 = this.mSelectedColor;
               var7 = this.mSelectedAlpha;
            } else {
               var6 = var2;
               var7 = var3;
               if (var8 == 1) {
                  var5 = this.mSelectedColor;
                  var4 = this.mSelectedAlpha;
                  var7 = var3;
                  var6 = var2;
               }
            }

            this.mPaint.setColor(var6);
            this.mPaint.setAlpha(var7);
            var1.drawCircle((float)this.mAmXCenter, (float)this.mAmPmYCenter, (float)this.mAmPmCircleRadius, this.mPaint);
            this.mPaint.setColor(var5);
            this.mPaint.setAlpha(var4);
            var1.drawCircle((float)this.mPmXCenter, (float)this.mAmPmYCenter, (float)this.mAmPmCircleRadius, this.mPaint);
            this.mPaint.setColor(this.mAmPmTextColor);
            var2 = this.mAmPmYCenter - (int)(this.mPaint.descent() + this.mPaint.ascent()) / 2;
            var1.drawText(this.mAmText, (float)this.mAmXCenter, (float)var2, this.mPaint);
            var1.drawText(this.mPmText, (float)this.mPmXCenter, (float)var2, this.mPaint);
         }
      }
   }

   public void setAmOrPm(int var1) {
      this.mAmOrPm = var1;
   }

   public void setAmOrPmPressed(int var1) {
      this.mAmOrPmPressed = var1;
   }

   void setTheme(TypedArray var1) {
      this.mUnselectedColor = var1.getColor(styleable.BetterPickersDialogs_bpAmPmCircleColor, ContextCompat.getColor(this.getContext(), color.bpBlue));
      this.mSelectedColor = var1.getColor(styleable.BetterPickersDialogs_bpAmPmCircleColor, ContextCompat.getColor(this.getContext(), color.bpBlue));
      this.mAmPmTextColor = var1.getColor(styleable.BetterPickersDialogs_bpAmPmTextColor, ContextCompat.getColor(this.getContext(), color.bpWhite));
      this.mSelectedAlpha = 200;
      this.mUnselectedAlpha = 50;
   }
}
