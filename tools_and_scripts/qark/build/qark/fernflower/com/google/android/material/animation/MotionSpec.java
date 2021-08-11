package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Property;
import androidx.collection.SimpleArrayMap;
import java.util.List;

public class MotionSpec {
   private static final String TAG = "MotionSpec";
   private final SimpleArrayMap propertyValues = new SimpleArrayMap();
   private final SimpleArrayMap timings = new SimpleArrayMap();

   private static void addInfoFromAnimator(MotionSpec var0, Animator var1) {
      if (var1 instanceof ObjectAnimator) {
         ObjectAnimator var3 = (ObjectAnimator)var1;
         var0.setPropertyValues(var3.getPropertyName(), var3.getValues());
         var0.setTiming(var3.getPropertyName(), MotionTiming.createFromAnimator(var3));
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Animator must be an ObjectAnimator: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   private PropertyValuesHolder[] clonePropertyValuesHolder(PropertyValuesHolder[] var1) {
      PropertyValuesHolder[] var3 = new PropertyValuesHolder[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var3[var2] = var1[var2].clone();
      }

      return var3;
   }

   public static MotionSpec createFromAttribute(Context var0, TypedArray var1, int var2) {
      if (var1.hasValue(var2)) {
         var2 = var1.getResourceId(var2, 0);
         if (var2 != 0) {
            return createFromResource(var0, var2);
         }
      }

      return null;
   }

   public static MotionSpec createFromResource(Context param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   private static MotionSpec createSpecFromAnimators(List var0) {
      MotionSpec var3 = new MotionSpec();
      int var1 = 0;

      for(int var2 = var0.size(); var1 < var2; ++var1) {
         addInfoFromAnimator(var3, (Animator)var0.get(var1));
      }

      return var3;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof MotionSpec)) {
         return false;
      } else {
         MotionSpec var2 = (MotionSpec)var1;
         return this.timings.equals(var2.timings);
      }
   }

   public ObjectAnimator getAnimator(String var1, Object var2, Property var3) {
      ObjectAnimator var4 = ObjectAnimator.ofPropertyValuesHolder(var2, this.getPropertyValues(var1));
      var4.setProperty(var3);
      this.getTiming(var1).apply(var4);
      return var4;
   }

   public PropertyValuesHolder[] getPropertyValues(String var1) {
      if (this.hasPropertyValues(var1)) {
         return this.clonePropertyValuesHolder((PropertyValuesHolder[])this.propertyValues.get(var1));
      } else {
         throw new IllegalArgumentException();
      }
   }

   public MotionTiming getTiming(String var1) {
      if (this.hasTiming(var1)) {
         return (MotionTiming)this.timings.get(var1);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public long getTotalDuration() {
      long var3 = 0L;
      int var1 = 0;

      for(int var2 = this.timings.size(); var1 < var2; ++var1) {
         MotionTiming var5 = (MotionTiming)this.timings.valueAt(var1);
         var3 = Math.max(var3, var5.getDelay() + var5.getDuration());
      }

      return var3;
   }

   public boolean hasPropertyValues(String var1) {
      return this.propertyValues.get(var1) != null;
   }

   public boolean hasTiming(String var1) {
      return this.timings.get(var1) != null;
   }

   public int hashCode() {
      return this.timings.hashCode();
   }

   public void setPropertyValues(String var1, PropertyValuesHolder[] var2) {
      this.propertyValues.put(var1, var2);
   }

   public void setTiming(String var1, MotionTiming var2) {
      this.timings.put(var1, var2);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('\n');
      var1.append(this.getClass().getName());
      var1.append('{');
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" timings: ");
      var1.append(this.timings);
      var1.append("}\n");
      return var1.toString();
   }
}
