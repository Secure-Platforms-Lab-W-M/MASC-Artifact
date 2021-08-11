package com.tokenautocomplete;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;

public class ViewSpan extends ReplacementSpan {
   private int maxWidth;
   protected View view;

   public ViewSpan(View var1, int var2) {
      this.maxWidth = var2;
      this.view = var1;
      var1.setLayoutParams(new LayoutParams(-2, -2));
   }

   private void prepView() {
      int var1 = MeasureSpec.makeMeasureSpec(this.maxWidth, Integer.MIN_VALUE);
      int var2 = MeasureSpec.makeMeasureSpec(0, 0);
      this.view.measure(var1, var2);
      View var3 = this.view;
      var3.layout(0, 0, var3.getMeasuredWidth(), this.view.getMeasuredHeight());
   }

   public void draw(Canvas var1, CharSequence var2, int var3, int var4, float var5, int var6, int var7, int var8, Paint var9) {
      this.prepView();
      var1.save();
      var3 = (var8 - var6 - this.view.getBottom()) / 2;
      var1.translate(var5, (float)(var8 - this.view.getBottom() - var3));
      this.view.draw(var1);
      var1.restore();
   }

   public int getSize(Paint var1, CharSequence var2, int var3, int var4, FontMetricsInt var5) {
      this.prepView();
      if (var5 != null) {
         var3 = this.view.getMeasuredHeight() - (var5.descent - var5.ascent);
         if (var3 > 0) {
            var4 = var3 / 2;
            var5.descent += var3 - var4;
            var5.ascent -= var4;
            var5.bottom += var3 - var4;
            var5.top -= var3 / 2;
         }
      }

      return this.view.getRight();
   }
}
