package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;

final class PreHoneycombCompat {
   static Property ALPHA = new FloatProperty("alpha") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getAlpha();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setAlpha(var2);
      }
   };
   static Property PIVOT_X = new FloatProperty("pivotX") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getPivotX();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setPivotX(var2);
      }
   };
   static Property PIVOT_Y = new FloatProperty("pivotY") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getPivotY();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setPivotY(var2);
      }
   };
   static Property ROTATION = new FloatProperty("rotation") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getRotation();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setRotation(var2);
      }
   };
   static Property ROTATION_X = new FloatProperty("rotationX") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getRotationX();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setRotationX(var2);
      }
   };
   static Property ROTATION_Y = new FloatProperty("rotationY") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getRotationY();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setRotationY(var2);
      }
   };
   static Property SCALE_X = new FloatProperty("scaleX") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getScaleX();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setScaleX(var2);
      }
   };
   static Property SCALE_Y = new FloatProperty("scaleY") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getScaleY();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setScaleY(var2);
      }
   };
   static Property SCROLL_X = new IntProperty("scrollX") {
      public Integer get(View var1) {
         return AnimatorProxy.wrap(var1).getScrollX();
      }

      public void setValue(View var1, int var2) {
         AnimatorProxy.wrap(var1).setScrollX(var2);
      }
   };
   static Property SCROLL_Y = new IntProperty("scrollY") {
      public Integer get(View var1) {
         return AnimatorProxy.wrap(var1).getScrollY();
      }

      public void setValue(View var1, int var2) {
         AnimatorProxy.wrap(var1).setScrollY(var2);
      }
   };
   static Property TRANSLATION_X = new FloatProperty("translationX") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getTranslationX();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setTranslationX(var2);
      }
   };
   static Property TRANSLATION_Y = new FloatProperty("translationY") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getTranslationY();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setTranslationY(var2);
      }
   };
   // $FF: renamed from: X com.nineoldandroids.util.Property
   static Property field_193 = new FloatProperty("x") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getX();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setX(var2);
      }
   };
   // $FF: renamed from: Y com.nineoldandroids.util.Property
   static Property field_194 = new FloatProperty("y") {
      public Float get(View var1) {
         return AnimatorProxy.wrap(var1).getY();
      }

      public void setValue(View var1, float var2) {
         AnimatorProxy.wrap(var1).setY(var2);
      }
   };

   private PreHoneycombCompat() {
   }
}
