package com.google.android.material.shadow;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Region.Op;
import android.graphics.Shader.TileMode;
import androidx.core.graphics.ColorUtils;

public class ShadowRenderer {
   private static final int COLOR_ALPHA_END = 0;
   private static final int COLOR_ALPHA_MIDDLE = 20;
   private static final int COLOR_ALPHA_START = 68;
   private static final int[] cornerColors = new int[4];
   private static final float[] cornerPositions = new float[]{0.0F, 0.0F, 0.5F, 1.0F};
   private static final int[] edgeColors = new int[3];
   private static final float[] edgePositions = new float[]{0.0F, 0.5F, 1.0F};
   private final Paint cornerShadowPaint;
   private final Paint edgeShadowPaint;
   private final Path scratch;
   private int shadowEndColor;
   private int shadowMiddleColor;
   private final Paint shadowPaint;
   private int shadowStartColor;

   public ShadowRenderer() {
      this(-16777216);
   }

   public ShadowRenderer(int var1) {
      this.scratch = new Path();
      this.setShadowColor(var1);
      Paint var2 = new Paint(4);
      this.cornerShadowPaint = var2;
      var2.setStyle(Style.FILL);
      var2 = new Paint();
      this.shadowPaint = var2;
      var2.setColor(this.shadowStartColor);
      this.edgeShadowPaint = new Paint(this.cornerShadowPaint);
   }

   public void drawCornerShadow(Canvas var1, Matrix var2, RectF var3, int var4, float var5, float var6) {
      boolean var9;
      if (var6 < 0.0F) {
         var9 = true;
      } else {
         var9 = false;
      }

      Path var10 = this.scratch;
      int[] var11;
      if (var9) {
         var11 = cornerColors;
         var11[0] = 0;
         var11[1] = this.shadowEndColor;
         var11[2] = this.shadowMiddleColor;
         var11[3] = this.shadowStartColor;
      } else {
         var10.rewind();
         var10.moveTo(var3.centerX(), var3.centerY());
         var10.arcTo(var3, var5, var6);
         var10.close();
         var3.inset((float)(-var4), (float)(-var4));
         var11 = cornerColors;
         var11[0] = 0;
         var11[1] = this.shadowStartColor;
         var11[2] = this.shadowMiddleColor;
         var11[3] = this.shadowEndColor;
      }

      float var7 = 1.0F - (float)var4 / (var3.width() / 2.0F);
      float var8 = (1.0F - var7) / 2.0F;
      float[] var12 = cornerPositions;
      var12[1] = var7;
      var12[2] = var7 + var8;
      this.cornerShadowPaint.setShader(new RadialGradient(var3.centerX(), var3.centerY(), var3.width() / 2.0F, cornerColors, cornerPositions, TileMode.CLAMP));
      var1.save();
      var1.concat(var2);
      if (!var9) {
         var1.clipPath(var10, Op.DIFFERENCE);
      }

      var1.drawArc(var3, var5, var6, true, this.cornerShadowPaint);
      var1.restore();
   }

   public void drawEdgeShadow(Canvas var1, Matrix var2, RectF var3, int var4) {
      var3.bottom += (float)var4;
      var3.offset(0.0F, (float)(-var4));
      int[] var5 = edgeColors;
      var5[0] = this.shadowEndColor;
      var5[1] = this.shadowMiddleColor;
      var5[2] = this.shadowStartColor;
      this.edgeShadowPaint.setShader(new LinearGradient(var3.left, var3.top, var3.left, var3.bottom, edgeColors, edgePositions, TileMode.CLAMP));
      var1.save();
      var1.concat(var2);
      var1.drawRect(var3, this.edgeShadowPaint);
      var1.restore();
   }

   public Paint getShadowPaint() {
      return this.shadowPaint;
   }

   public void setShadowColor(int var1) {
      this.shadowStartColor = ColorUtils.setAlphaComponent(var1, 68);
      this.shadowMiddleColor = ColorUtils.setAlphaComponent(var1, 20);
      this.shadowEndColor = ColorUtils.setAlphaComponent(var1, 0);
   }
}
