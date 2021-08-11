package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

public abstract class Keyframe implements Cloneable {
   float mFraction;
   boolean mHasValue = false;
   private Interpolator mInterpolator = null;
   Class mValueType;

   public static Keyframe ofFloat(float var0) {
      return new Keyframe.FloatKeyframe(var0);
   }

   public static Keyframe ofFloat(float var0, float var1) {
      return new Keyframe.FloatKeyframe(var0, var1);
   }

   public static Keyframe ofInt(float var0) {
      return new Keyframe.IntKeyframe(var0);
   }

   public static Keyframe ofInt(float var0, int var1) {
      return new Keyframe.IntKeyframe(var0, var1);
   }

   public static Keyframe ofObject(float var0) {
      return new Keyframe.ObjectKeyframe(var0, (Object)null);
   }

   public static Keyframe ofObject(float var0, Object var1) {
      return new Keyframe.ObjectKeyframe(var0, var1);
   }

   public abstract Keyframe clone();

   public float getFraction() {
      return this.mFraction;
   }

   public Interpolator getInterpolator() {
      return this.mInterpolator;
   }

   public Class getType() {
      return this.mValueType;
   }

   public abstract Object getValue();

   public boolean hasValue() {
      return this.mHasValue;
   }

   public void setFraction(float var1) {
      this.mFraction = var1;
   }

   public void setInterpolator(Interpolator var1) {
      this.mInterpolator = var1;
   }

   public abstract void setValue(Object var1);

   static class FloatKeyframe extends Keyframe {
      float mValue;

      FloatKeyframe(float var1) {
         this.mFraction = var1;
         this.mValueType = Float.TYPE;
      }

      FloatKeyframe(float var1, float var2) {
         this.mFraction = var1;
         this.mValue = var2;
         this.mValueType = Float.TYPE;
         this.mHasValue = true;
      }

      public Keyframe.FloatKeyframe clone() {
         Keyframe.FloatKeyframe var1 = new Keyframe.FloatKeyframe(this.getFraction(), this.mValue);
         var1.setInterpolator(this.getInterpolator());
         return var1;
      }

      public float getFloatValue() {
         return this.mValue;
      }

      public Object getValue() {
         return this.mValue;
      }

      public void setValue(Object var1) {
         if (var1 != null && var1.getClass() == Float.class) {
            this.mValue = (Float)var1;
            this.mHasValue = true;
         }

      }
   }

   static class IntKeyframe extends Keyframe {
      int mValue;

      IntKeyframe(float var1) {
         this.mFraction = var1;
         this.mValueType = Integer.TYPE;
      }

      IntKeyframe(float var1, int var2) {
         this.mFraction = var1;
         this.mValue = var2;
         this.mValueType = Integer.TYPE;
         this.mHasValue = true;
      }

      public Keyframe.IntKeyframe clone() {
         Keyframe.IntKeyframe var1 = new Keyframe.IntKeyframe(this.getFraction(), this.mValue);
         var1.setInterpolator(this.getInterpolator());
         return var1;
      }

      public int getIntValue() {
         return this.mValue;
      }

      public Object getValue() {
         return this.mValue;
      }

      public void setValue(Object var1) {
         if (var1 != null && var1.getClass() == Integer.class) {
            this.mValue = (Integer)var1;
            this.mHasValue = true;
         }

      }
   }

   static class ObjectKeyframe extends Keyframe {
      Object mValue;

      ObjectKeyframe(float var1, Object var2) {
         this.mFraction = var1;
         this.mValue = var2;
         boolean var3;
         if (var2 != null) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mHasValue = var3;
         Class var4;
         if (this.mHasValue) {
            var4 = var2.getClass();
         } else {
            var4 = Object.class;
         }

         this.mValueType = var4;
      }

      public Keyframe.ObjectKeyframe clone() {
         Keyframe.ObjectKeyframe var1 = new Keyframe.ObjectKeyframe(this.getFraction(), this.mValue);
         var1.setInterpolator(this.getInterpolator());
         return var1;
      }

      public Object getValue() {
         return this.mValue;
      }

      public void setValue(Object var1) {
         this.mValue = var1;
         boolean var2;
         if (var1 != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.mHasValue = var2;
      }
   }
}
