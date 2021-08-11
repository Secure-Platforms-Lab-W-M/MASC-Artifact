package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.dimen;
import com.codetroopers.betterpickers.R.string;

public class TextViewWithCircularIndicator extends TextView {
   private static final int SELECTED_CIRCLE_ALPHA = 60;
   private int mCircleColor;
   Paint mCirclePaint = new Paint();
   private boolean mDrawCircle;
   private final String mItemIsSelectedText;
   private final int mRadius;

   public TextViewWithCircularIndicator(Context var1, AttributeSet var2) {
      super(var1, var2);
      Resources var3 = var1.getResources();
      this.mCircleColor = var3.getColor(color.bpBlue);
      this.mRadius = var3.getDimensionPixelOffset(dimen.month_select_circle_radius);
      this.mItemIsSelectedText = var1.getResources().getString(string.item_is_selected);
      this.init();
   }

   private void init() {
      this.mCirclePaint.setFakeBoldText(true);
      this.mCirclePaint.setAntiAlias(true);
      this.mCirclePaint.setColor(this.mCircleColor);
      this.mCirclePaint.setTextAlign(Align.CENTER);
      this.mCirclePaint.setStyle(Style.FILL);
      this.mCirclePaint.setAlpha(60);
   }

   public void drawIndicator(boolean var1) {
      this.mDrawCircle = var1;
   }

   public CharSequence getContentDescription() {
      CharSequence var1 = this.getText();
      return (CharSequence)(this.mDrawCircle ? String.format(this.mItemIsSelectedText, var1) : var1);
   }

   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mDrawCircle) {
         int var2 = this.getWidth();
         int var3 = this.getHeight();
         int var4 = Math.min(var2, var3) / 2;
         var1.drawCircle((float)(var2 / 2), (float)(var3 / 2), (float)var4, this.mCirclePaint);
      }

   }

   public void setCircleColor(int var1) {
      this.mCircleColor = var1;
      this.init();
   }
}
