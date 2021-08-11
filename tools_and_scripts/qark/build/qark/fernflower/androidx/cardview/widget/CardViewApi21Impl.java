package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;

class CardViewApi21Impl implements CardViewImpl {
   private RoundRectDrawable getCardBackground(CardViewDelegate var1) {
      return (RoundRectDrawable)var1.getCardBackground();
   }

   public ColorStateList getBackgroundColor(CardViewDelegate var1) {
      return this.getCardBackground(var1).getColor();
   }

   public float getElevation(CardViewDelegate var1) {
      return var1.getCardView().getElevation();
   }

   public float getMaxElevation(CardViewDelegate var1) {
      return this.getCardBackground(var1).getPadding();
   }

   public float getMinHeight(CardViewDelegate var1) {
      return this.getRadius(var1) * 2.0F;
   }

   public float getMinWidth(CardViewDelegate var1) {
      return this.getRadius(var1) * 2.0F;
   }

   public float getRadius(CardViewDelegate var1) {
      return this.getCardBackground(var1).getRadius();
   }

   public void initStatic() {
   }

   public void initialize(CardViewDelegate var1, Context var2, ColorStateList var3, float var4, float var5, float var6) {
      var1.setCardBackground(new RoundRectDrawable(var3, var4));
      View var7 = var1.getCardView();
      var7.setClipToOutline(true);
      var7.setElevation(var5);
      this.setMaxElevation(var1, var6);
   }

   public void onCompatPaddingChanged(CardViewDelegate var1) {
      this.setMaxElevation(var1, this.getMaxElevation(var1));
   }

   public void onPreventCornerOverlapChanged(CardViewDelegate var1) {
      this.setMaxElevation(var1, this.getMaxElevation(var1));
   }

   public void setBackgroundColor(CardViewDelegate var1, ColorStateList var2) {
      this.getCardBackground(var1).setColor(var2);
   }

   public void setElevation(CardViewDelegate var1, float var2) {
      var1.getCardView().setElevation(var2);
   }

   public void setMaxElevation(CardViewDelegate var1, float var2) {
      this.getCardBackground(var1).setPadding(var2, var1.getUseCompatPadding(), var1.getPreventCornerOverlap());
      this.updatePadding(var1);
   }

   public void setRadius(CardViewDelegate var1, float var2) {
      this.getCardBackground(var1).setRadius(var2);
   }

   public void updatePadding(CardViewDelegate var1) {
      if (!var1.getUseCompatPadding()) {
         var1.setShadowPadding(0, 0, 0, 0);
      } else {
         float var2 = this.getMaxElevation(var1);
         float var3 = this.getRadius(var1);
         int var4 = (int)Math.ceil((double)RoundRectDrawableWithShadow.calculateHorizontalPadding(var2, var3, var1.getPreventCornerOverlap()));
         int var5 = (int)Math.ceil((double)RoundRectDrawableWithShadow.calculateVerticalPadding(var2, var3, var1.getPreventCornerOverlap()));
         var1.setShadowPadding(var4, var5, var4, var5);
      }
   }
}
