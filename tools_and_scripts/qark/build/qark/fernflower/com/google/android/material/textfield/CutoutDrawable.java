package com.google.android.material.textfield;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build.VERSION;
import android.view.View;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

class CutoutDrawable extends MaterialShapeDrawable {
   private final RectF cutoutBounds;
   private final Paint cutoutPaint;
   private int savedLayer;

   CutoutDrawable() {
      this((ShapeAppearanceModel)null);
   }

   CutoutDrawable(ShapeAppearanceModel var1) {
      if (var1 == null) {
         var1 = new ShapeAppearanceModel();
      }

      super(var1);
      this.cutoutPaint = new Paint(1);
      this.setPaintStyles();
      this.cutoutBounds = new RectF();
   }

   private void postDraw(Canvas var1) {
      if (!this.useHardwareLayer(this.getCallback())) {
         var1.restoreToCount(this.savedLayer);
      }

   }

   private void preDraw(Canvas var1) {
      Callback var2 = this.getCallback();
      if (this.useHardwareLayer(var2)) {
         View var3 = (View)var2;
         if (var3.getLayerType() != 2) {
            var3.setLayerType(2, (Paint)null);
         }

      } else {
         this.saveCanvasLayer(var1);
      }
   }

   private void saveCanvasLayer(Canvas var1) {
      if (VERSION.SDK_INT >= 21) {
         this.savedLayer = var1.saveLayer(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight(), (Paint)null);
      } else {
         this.savedLayer = var1.saveLayer(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight(), (Paint)null, 31);
      }
   }

   private void setPaintStyles() {
      this.cutoutPaint.setStyle(Style.FILL_AND_STROKE);
      this.cutoutPaint.setColor(-1);
      this.cutoutPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
   }

   private boolean useHardwareLayer(Callback var1) {
      return var1 instanceof View;
   }

   public void draw(Canvas var1) {
      this.preDraw(var1);
      super.draw(var1);
      var1.drawRect(this.cutoutBounds, this.cutoutPaint);
      this.postDraw(var1);
   }

   boolean hasCutout() {
      return this.cutoutBounds.isEmpty() ^ true;
   }

   void removeCutout() {
      this.setCutout(0.0F, 0.0F, 0.0F, 0.0F);
   }

   void setCutout(float var1, float var2, float var3, float var4) {
      if (var1 != this.cutoutBounds.left || var2 != this.cutoutBounds.top || var3 != this.cutoutBounds.right || var4 != this.cutoutBounds.bottom) {
         this.cutoutBounds.set(var1, var2, var3, var4);
         this.invalidateSelf();
      }

   }

   void setCutout(RectF var1) {
      this.setCutout(var1.left, var1.top, var1.right, var1.bottom);
   }
}
