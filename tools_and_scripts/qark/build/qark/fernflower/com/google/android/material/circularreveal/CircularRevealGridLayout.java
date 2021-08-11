package com.google.android.material.circularreveal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.GridLayout;

public class CircularRevealGridLayout extends GridLayout implements CircularRevealWidget {
   private final CircularRevealHelper helper;

   public CircularRevealGridLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CircularRevealGridLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.helper = new CircularRevealHelper(this);
   }

   public void actualDraw(Canvas var1) {
      super.draw(var1);
   }

   public boolean actualIsOpaque() {
      return super.isOpaque();
   }

   public void buildCircularRevealCache() {
      this.helper.buildCircularRevealCache();
   }

   public void destroyCircularRevealCache() {
      this.helper.destroyCircularRevealCache();
   }

   public void draw(Canvas var1) {
      CircularRevealHelper var2 = this.helper;
      if (var2 != null) {
         var2.draw(var1);
      } else {
         super.draw(var1);
      }
   }

   public Drawable getCircularRevealOverlayDrawable() {
      return this.helper.getCircularRevealOverlayDrawable();
   }

   public int getCircularRevealScrimColor() {
      return this.helper.getCircularRevealScrimColor();
   }

   public CircularRevealWidget.RevealInfo getRevealInfo() {
      return this.helper.getRevealInfo();
   }

   public boolean isOpaque() {
      CircularRevealHelper var1 = this.helper;
      return var1 != null ? var1.isOpaque() : super.isOpaque();
   }

   public void setCircularRevealOverlayDrawable(Drawable var1) {
      this.helper.setCircularRevealOverlayDrawable(var1);
   }

   public void setCircularRevealScrimColor(int var1) {
      this.helper.setCircularRevealScrimColor(var1);
   }

   public void setRevealInfo(CircularRevealWidget.RevealInfo var1) {
      this.helper.setRevealInfo(var1);
   }
}
