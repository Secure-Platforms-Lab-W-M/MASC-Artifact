package com.google.android.material.circularreveal;

import android.animation.TypeEvaluator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Property;
import com.google.android.material.math.MathUtils;

public interface CircularRevealWidget extends CircularRevealHelper.Delegate {
   void buildCircularRevealCache();

   void destroyCircularRevealCache();

   void draw(Canvas var1);

   Drawable getCircularRevealOverlayDrawable();

   int getCircularRevealScrimColor();

   CircularRevealWidget.RevealInfo getRevealInfo();

   boolean isOpaque();

   void setCircularRevealOverlayDrawable(Drawable var1);

   void setCircularRevealScrimColor(int var1);

   void setRevealInfo(CircularRevealWidget.RevealInfo var1);

   public static class CircularRevealEvaluator implements TypeEvaluator {
      public static final TypeEvaluator CIRCULAR_REVEAL = new CircularRevealWidget.CircularRevealEvaluator();
      private final CircularRevealWidget.RevealInfo revealInfo = new CircularRevealWidget.RevealInfo();

      public CircularRevealWidget.RevealInfo evaluate(float var1, CircularRevealWidget.RevealInfo var2, CircularRevealWidget.RevealInfo var3) {
         this.revealInfo.set(MathUtils.lerp(var2.centerX, var3.centerX, var1), MathUtils.lerp(var2.centerY, var3.centerY, var1), MathUtils.lerp(var2.radius, var3.radius, var1));
         return this.revealInfo;
      }
   }

   public static class CircularRevealProperty extends Property {
      public static final Property CIRCULAR_REVEAL = new CircularRevealWidget.CircularRevealProperty("circularReveal");

      private CircularRevealProperty(String var1) {
         super(CircularRevealWidget.RevealInfo.class, var1);
      }

      public CircularRevealWidget.RevealInfo get(CircularRevealWidget var1) {
         return var1.getRevealInfo();
      }

      public void set(CircularRevealWidget var1, CircularRevealWidget.RevealInfo var2) {
         var1.setRevealInfo(var2);
      }
   }

   public static class CircularRevealScrimColorProperty extends Property {
      public static final Property CIRCULAR_REVEAL_SCRIM_COLOR = new CircularRevealWidget.CircularRevealScrimColorProperty("circularRevealScrimColor");

      private CircularRevealScrimColorProperty(String var1) {
         super(Integer.class, var1);
      }

      public Integer get(CircularRevealWidget var1) {
         return var1.getCircularRevealScrimColor();
      }

      public void set(CircularRevealWidget var1, Integer var2) {
         var1.setCircularRevealScrimColor(var2);
      }
   }

   public static class RevealInfo {
      public static final float INVALID_RADIUS = Float.MAX_VALUE;
      public float centerX;
      public float centerY;
      public float radius;

      private RevealInfo() {
      }

      public RevealInfo(float var1, float var2, float var3) {
         this.centerX = var1;
         this.centerY = var2;
         this.radius = var3;
      }

      // $FF: synthetic method
      RevealInfo(Object var1) {
         this();
      }

      public RevealInfo(CircularRevealWidget.RevealInfo var1) {
         this(var1.centerX, var1.centerY, var1.radius);
      }

      public boolean isInvalid() {
         return this.radius == Float.MAX_VALUE;
      }

      public void set(float var1, float var2, float var3) {
         this.centerX = var1;
         this.centerY = var2;
         this.radius = var3;
      }

      public void set(CircularRevealWidget.RevealInfo var1) {
         this.set(var1.centerX, var1.centerY, var1.radius);
      }
   }
}
