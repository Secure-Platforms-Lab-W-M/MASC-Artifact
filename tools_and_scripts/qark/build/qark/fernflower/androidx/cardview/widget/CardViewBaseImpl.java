package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

class CardViewBaseImpl implements CardViewImpl {
   final RectF mCornerRect = new RectF();

   private RoundRectDrawableWithShadow createBackground(Context var1, ColorStateList var2, float var3, float var4, float var5) {
      return new RoundRectDrawableWithShadow(var1.getResources(), var2, var3, var4, var5);
   }

   private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate var1) {
      return (RoundRectDrawableWithShadow)var1.getCardBackground();
   }

   public ColorStateList getBackgroundColor(CardViewDelegate var1) {
      return this.getShadowBackground(var1).getColor();
   }

   public float getElevation(CardViewDelegate var1) {
      return this.getShadowBackground(var1).getShadowSize();
   }

   public float getMaxElevation(CardViewDelegate var1) {
      return this.getShadowBackground(var1).getMaxShadowSize();
   }

   public float getMinHeight(CardViewDelegate var1) {
      return this.getShadowBackground(var1).getMinHeight();
   }

   public float getMinWidth(CardViewDelegate var1) {
      return this.getShadowBackground(var1).getMinWidth();
   }

   public float getRadius(CardViewDelegate var1) {
      return this.getShadowBackground(var1).getCornerRadius();
   }

   public void initStatic() {
      RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper() {
         public void drawRoundRect(Canvas var1, RectF var2, float var3, Paint var4) {
            float var5 = var3 * 2.0F;
            float var6 = var2.width() - var5 - 1.0F;
            float var7 = var2.height();
            if (var3 >= 1.0F) {
               float var8 = var3 + 0.5F;
               CardViewBaseImpl.this.mCornerRect.set(-var8, -var8, var8, var8);
               int var9 = var1.save();
               var1.translate(var2.left + var8, var2.top + var8);
               var1.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0F, 90.0F, true, var4);
               var1.translate(var6, 0.0F);
               var1.rotate(90.0F);
               var1.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0F, 90.0F, true, var4);
               var1.translate(var7 - var5 - 1.0F, 0.0F);
               var1.rotate(90.0F);
               var1.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0F, 90.0F, true, var4);
               var1.translate(var6, 0.0F);
               var1.rotate(90.0F);
               var1.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0F, 90.0F, true, var4);
               var1.restoreToCount(var9);
               var1.drawRect(var2.left + var8 - 1.0F, var2.top, var2.right - var8 + 1.0F, var2.top + var8, var4);
               var1.drawRect(var2.left + var8 - 1.0F, var2.bottom - var8, var2.right - var8 + 1.0F, var2.bottom, var4);
            }

            var1.drawRect(var2.left, var2.top + var3, var2.right, var2.bottom - var3, var4);
         }
      };
   }

   public void initialize(CardViewDelegate var1, Context var2, ColorStateList var3, float var4, float var5, float var6) {
      RoundRectDrawableWithShadow var7 = this.createBackground(var2, var3, var4, var5, var6);
      var7.setAddPaddingForCorners(var1.getPreventCornerOverlap());
      var1.setCardBackground(var7);
      this.updatePadding(var1);
   }

   public void onCompatPaddingChanged(CardViewDelegate var1) {
   }

   public void onPreventCornerOverlapChanged(CardViewDelegate var1) {
      this.getShadowBackground(var1).setAddPaddingForCorners(var1.getPreventCornerOverlap());
      this.updatePadding(var1);
   }

   public void setBackgroundColor(CardViewDelegate var1, ColorStateList var2) {
      this.getShadowBackground(var1).setColor(var2);
   }

   public void setElevation(CardViewDelegate var1, float var2) {
      this.getShadowBackground(var1).setShadowSize(var2);
   }

   public void setMaxElevation(CardViewDelegate var1, float var2) {
      this.getShadowBackground(var1).setMaxShadowSize(var2);
      this.updatePadding(var1);
   }

   public void setRadius(CardViewDelegate var1, float var2) {
      this.getShadowBackground(var1).setCornerRadius(var2);
      this.updatePadding(var1);
   }

   public void updatePadding(CardViewDelegate var1) {
      Rect var2 = new Rect();
      this.getShadowBackground(var1).getMaxShadowAndCornerPadding(var2);
      var1.setMinWidthHeightInternal((int)Math.ceil((double)this.getMinWidth(var1)), (int)Math.ceil((double)this.getMinHeight(var1)));
      var1.setShadowPadding(var2.left, var2.top, var2.right, var2.bottom);
   }
}
