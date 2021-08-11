package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;
import java.util.HashMap;
import java.util.Map;

public final class ObjectAnimator extends ValueAnimator {
   private static final boolean DBG = false;
   private static final Map PROXY_PROPERTIES;
   private Property mProperty;
   private String mPropertyName;
   private Object mTarget;

   static {
      HashMap var0 = new HashMap();
      PROXY_PROPERTIES = var0;
      var0.put("alpha", PreHoneycombCompat.ALPHA);
      PROXY_PROPERTIES.put("pivotX", PreHoneycombCompat.PIVOT_X);
      PROXY_PROPERTIES.put("pivotY", PreHoneycombCompat.PIVOT_Y);
      PROXY_PROPERTIES.put("translationX", PreHoneycombCompat.TRANSLATION_X);
      PROXY_PROPERTIES.put("translationY", PreHoneycombCompat.TRANSLATION_Y);
      PROXY_PROPERTIES.put("rotation", PreHoneycombCompat.ROTATION);
      PROXY_PROPERTIES.put("rotationX", PreHoneycombCompat.ROTATION_X);
      PROXY_PROPERTIES.put("rotationY", PreHoneycombCompat.ROTATION_Y);
      PROXY_PROPERTIES.put("scaleX", PreHoneycombCompat.SCALE_X);
      PROXY_PROPERTIES.put("scaleY", PreHoneycombCompat.SCALE_Y);
      PROXY_PROPERTIES.put("scrollX", PreHoneycombCompat.SCROLL_X);
      PROXY_PROPERTIES.put("scrollY", PreHoneycombCompat.SCROLL_Y);
      PROXY_PROPERTIES.put("x", PreHoneycombCompat.field_193);
      PROXY_PROPERTIES.put("y", PreHoneycombCompat.field_194);
   }

   public ObjectAnimator() {
   }

   private ObjectAnimator(Object var1, Property var2) {
      this.mTarget = var1;
      this.setProperty(var2);
   }

   private ObjectAnimator(Object var1, String var2) {
      this.mTarget = var1;
      this.setPropertyName(var2);
   }

   public static ObjectAnimator ofFloat(Object var0, Property var1, float... var2) {
      ObjectAnimator var3 = new ObjectAnimator(var0, var1);
      var3.setFloatValues(var2);
      return var3;
   }

   public static ObjectAnimator ofFloat(Object var0, String var1, float... var2) {
      ObjectAnimator var3 = new ObjectAnimator(var0, var1);
      var3.setFloatValues(var2);
      return var3;
   }

   public static ObjectAnimator ofInt(Object var0, Property var1, int... var2) {
      ObjectAnimator var3 = new ObjectAnimator(var0, var1);
      var3.setIntValues(var2);
      return var3;
   }

   public static ObjectAnimator ofInt(Object var0, String var1, int... var2) {
      ObjectAnimator var3 = new ObjectAnimator(var0, var1);
      var3.setIntValues(var2);
      return var3;
   }

   public static ObjectAnimator ofObject(Object var0, Property var1, TypeEvaluator var2, Object... var3) {
      ObjectAnimator var4 = new ObjectAnimator(var0, var1);
      var4.setObjectValues(var3);
      var4.setEvaluator(var2);
      return var4;
   }

   public static ObjectAnimator ofObject(Object var0, String var1, TypeEvaluator var2, Object... var3) {
      ObjectAnimator var4 = new ObjectAnimator(var0, var1);
      var4.setObjectValues(var3);
      var4.setEvaluator(var2);
      return var4;
   }

   public static ObjectAnimator ofPropertyValuesHolder(Object var0, PropertyValuesHolder... var1) {
      ObjectAnimator var2 = new ObjectAnimator();
      var2.mTarget = var0;
      var2.setValues(var1);
      return var2;
   }

   void animateValue(float var1) {
      super.animateValue(var1);
      int var3 = this.mValues.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         this.mValues[var2].setAnimatedValue(this.mTarget);
      }

   }

   public ObjectAnimator clone() {
      return (ObjectAnimator)super.clone();
   }

   public String getPropertyName() {
      return this.mPropertyName;
   }

   public Object getTarget() {
      return this.mTarget;
   }

   void initAnimation() {
      if (!this.mInitialized) {
         if (this.mProperty == null && AnimatorProxy.NEEDS_PROXY && this.mTarget instanceof View && PROXY_PROPERTIES.containsKey(this.mPropertyName)) {
            this.setProperty((Property)PROXY_PROPERTIES.get(this.mPropertyName));
         }

         int var2 = this.mValues.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            this.mValues[var1].setupSetterAndGetter(this.mTarget);
         }

         super.initAnimation();
      }

   }

   public ObjectAnimator setDuration(long var1) {
      super.setDuration(var1);
      return this;
   }

   public void setFloatValues(float... var1) {
      if (this.mValues != null && this.mValues.length != 0) {
         super.setFloatValues(var1);
      } else {
         Property var2 = this.mProperty;
         if (var2 != null) {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(var2, var1)});
         } else {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(this.mPropertyName, var1)});
         }
      }
   }

   public void setIntValues(int... var1) {
      if (this.mValues != null && this.mValues.length != 0) {
         super.setIntValues(var1);
      } else {
         Property var2 = this.mProperty;
         if (var2 != null) {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofInt(var2, var1)});
         } else {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofInt(this.mPropertyName, var1)});
         }
      }
   }

   public void setObjectValues(Object... var1) {
      if (this.mValues != null && this.mValues.length != 0) {
         super.setObjectValues(var1);
      } else {
         Property var2 = this.mProperty;
         if (var2 != null) {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofObject(var2, (TypeEvaluator)null, var1)});
         } else {
            this.setValues(new PropertyValuesHolder[]{PropertyValuesHolder.ofObject(this.mPropertyName, (TypeEvaluator)null, var1)});
         }
      }
   }

   public void setProperty(Property var1) {
      if (this.mValues != null) {
         PropertyValuesHolder var2 = this.mValues[0];
         String var3 = var2.getPropertyName();
         var2.setProperty(var1);
         this.mValuesMap.remove(var3);
         this.mValuesMap.put(this.mPropertyName, var2);
      }

      if (this.mProperty != null) {
         this.mPropertyName = var1.getName();
      }

      this.mProperty = var1;
      this.mInitialized = false;
   }

   public void setPropertyName(String var1) {
      if (this.mValues != null) {
         PropertyValuesHolder var2 = this.mValues[0];
         String var3 = var2.getPropertyName();
         var2.setPropertyName(var1);
         this.mValuesMap.remove(var3);
         this.mValuesMap.put(var1, var2);
      }

      this.mPropertyName = var1;
      this.mInitialized = false;
   }

   public void setTarget(Object var1) {
      if (this.mTarget != var1) {
         Object var2 = this.mTarget;
         this.mTarget = var1;
         if (var2 != null && var1 != null && var2.getClass() == var1.getClass()) {
            return;
         }

         this.mInitialized = false;
      }

   }

   public void setupEndValues() {
      this.initAnimation();
      int var2 = this.mValues.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         this.mValues[var1].setupEndValue(this.mTarget);
      }

   }

   public void setupStartValues() {
      this.initAnimation();
      int var2 = this.mValues.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         this.mValues[var1].setupStartValue(this.mTarget);
      }

   }

   public void start() {
      super.start();
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      var2.append("ObjectAnimator@");
      var2.append(Integer.toHexString(this.hashCode()));
      var2.append(", target ");
      var2.append(this.mTarget);
      String var4 = var2.toString();
      String var3 = var4;
      if (this.mValues != null) {
         int var1 = 0;

         while(true) {
            var3 = var4;
            if (var1 >= this.mValues.length) {
               break;
            }

            StringBuilder var5 = new StringBuilder();
            var5.append(var4);
            var5.append("\n    ");
            var5.append(this.mValues[var1].toString());
            var4 = var5.toString();
            ++var1;
         }
      }

      return var3;
   }
}
