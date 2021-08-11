package com.nineoldandroids.animation;

import android.util.Log;
import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PropertyValuesHolder implements Cloneable {
   private static Class[] DOUBLE_VARIANTS;
   private static Class[] FLOAT_VARIANTS;
   private static Class[] INTEGER_VARIANTS;
   private static final TypeEvaluator sFloatEvaluator = new FloatEvaluator();
   private static final HashMap sGetterPropertyMap;
   private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
   private static final HashMap sSetterPropertyMap;
   private Object mAnimatedValue;
   private TypeEvaluator mEvaluator;
   private Method mGetter;
   KeyframeSet mKeyframeSet;
   protected Property mProperty;
   final ReentrantReadWriteLock mPropertyMapLock;
   String mPropertyName;
   Method mSetter;
   final Object[] mTmpValueArray;
   Class mValueType;

   static {
      FLOAT_VARIANTS = new Class[]{Float.TYPE, Float.class, Double.TYPE, Integer.TYPE, Double.class, Integer.class};
      INTEGER_VARIANTS = new Class[]{Integer.TYPE, Integer.class, Float.TYPE, Double.TYPE, Float.class, Double.class};
      DOUBLE_VARIANTS = new Class[]{Double.TYPE, Double.class, Float.TYPE, Integer.TYPE, Float.class, Integer.class};
      sSetterPropertyMap = new HashMap();
      sGetterPropertyMap = new HashMap();
   }

   private PropertyValuesHolder(Property var1) {
      this.mSetter = null;
      this.mGetter = null;
      this.mKeyframeSet = null;
      this.mPropertyMapLock = new ReentrantReadWriteLock();
      this.mTmpValueArray = new Object[1];
      this.mProperty = var1;
      if (var1 != null) {
         this.mPropertyName = var1.getName();
      }

   }

   // $FF: synthetic method
   PropertyValuesHolder(Property var1, Object var2) {
      this(var1);
   }

   private PropertyValuesHolder(String var1) {
      this.mSetter = null;
      this.mGetter = null;
      this.mKeyframeSet = null;
      this.mPropertyMapLock = new ReentrantReadWriteLock();
      this.mTmpValueArray = new Object[1];
      this.mPropertyName = var1;
   }

   // $FF: synthetic method
   PropertyValuesHolder(String var1, Object var2) {
      this(var1);
   }

   static String getMethodName(String var0, String var1) {
      if (var1 != null) {
         if (var1.length() == 0) {
            return var0;
         } else {
            char var2 = Character.toUpperCase(var1.charAt(0));
            var1 = var1.substring(1);
            StringBuilder var3 = new StringBuilder();
            var3.append(var0);
            var3.append(var2);
            var3.append(var1);
            return var3.toString();
         }
      } else {
         return var0;
      }
   }

   private Method getPropertyFunction(Class var1, String var2, Class var3) {
      Method var6 = null;
      String var7 = getMethodName(var2, this.mPropertyName);
      boolean var10001;
      StringBuilder var18;
      if (var3 == null) {
         Method var22;
         try {
            var22 = var1.getMethod(var7, (Class[])null);
            return var22;
         } catch (NoSuchMethodException var12) {
            var22 = var6;

            label97: {
               Method var19;
               try {
                  var19 = var1.getDeclaredMethod(var7, (Class[])null);
               } catch (NoSuchMethodException var11) {
                  var10001 = false;
                  break label97;
               }

               var22 = var19;

               try {
                  var19.setAccessible(true);
                  return var19;
               } catch (NoSuchMethodException var10) {
                  var10001 = false;
               }
            }

            var18 = new StringBuilder();
            var18.append("Couldn't find no-arg method for property ");
            var18.append(this.mPropertyName);
            var18.append(": ");
            var18.append(var12);
            Log.e("PropertyValuesHolder", var18.toString());
            return var22;
         }
      } else {
         Class[] var8 = new Class[1];
         Class[] var20;
         if (this.mValueType.equals(Float.class)) {
            var20 = FLOAT_VARIANTS;
         } else if (this.mValueType.equals(Integer.class)) {
            var20 = INTEGER_VARIANTS;
         } else if (this.mValueType.equals(Double.class)) {
            var20 = DOUBLE_VARIANTS;
         } else {
            var20 = new Class[]{this.mValueType};
         }

         int var5 = var20.length;
         Method var21 = null;

         for(int var4 = 0; var4 < var5; ++var4) {
            Class var9 = var20[var4];
            var8[0] = var9;

            label99: {
               try {
                  var6 = var1.getMethod(var7, var8);
               } catch (NoSuchMethodException var17) {
                  var10001 = false;
                  break label99;
               }

               var21 = var6;

               try {
                  this.mValueType = var9;
                  return var6;
               } catch (NoSuchMethodException var16) {
                  var10001 = false;
               }
            }

            try {
               var6 = var1.getDeclaredMethod(var7, var8);
            } catch (NoSuchMethodException var15) {
               var10001 = false;
               continue;
            }

            var21 = var6;

            try {
               var6.setAccessible(true);
            } catch (NoSuchMethodException var14) {
               var10001 = false;
               continue;
            }

            var21 = var6;

            try {
               this.mValueType = var9;
               return var6;
            } catch (NoSuchMethodException var13) {
               var10001 = false;
            }
         }

         var18 = new StringBuilder();
         var18.append("Couldn't find setter/getter for property ");
         var18.append(this.mPropertyName);
         var18.append(" with value type ");
         var18.append(this.mValueType);
         Log.e("PropertyValuesHolder", var18.toString());
         return var21;
      }
   }

   public static PropertyValuesHolder ofFloat(Property var0, float... var1) {
      return new PropertyValuesHolder.FloatPropertyValuesHolder(var0, var1);
   }

   public static PropertyValuesHolder ofFloat(String var0, float... var1) {
      return new PropertyValuesHolder.FloatPropertyValuesHolder(var0, var1);
   }

   public static PropertyValuesHolder ofInt(Property var0, int... var1) {
      return new PropertyValuesHolder.IntPropertyValuesHolder(var0, var1);
   }

   public static PropertyValuesHolder ofInt(String var0, int... var1) {
      return new PropertyValuesHolder.IntPropertyValuesHolder(var0, var1);
   }

   public static PropertyValuesHolder ofKeyframe(Property var0, Keyframe... var1) {
      KeyframeSet var2 = KeyframeSet.ofKeyframe(var1);
      if (var2 instanceof IntKeyframeSet) {
         return new PropertyValuesHolder.IntPropertyValuesHolder(var0, (IntKeyframeSet)var2);
      } else if (var2 instanceof FloatKeyframeSet) {
         return new PropertyValuesHolder.FloatPropertyValuesHolder(var0, (FloatKeyframeSet)var2);
      } else {
         PropertyValuesHolder var3 = new PropertyValuesHolder(var0);
         var3.mKeyframeSet = var2;
         var3.mValueType = var1[0].getType();
         return var3;
      }
   }

   public static PropertyValuesHolder ofKeyframe(String var0, Keyframe... var1) {
      KeyframeSet var2 = KeyframeSet.ofKeyframe(var1);
      if (var2 instanceof IntKeyframeSet) {
         return new PropertyValuesHolder.IntPropertyValuesHolder(var0, (IntKeyframeSet)var2);
      } else if (var2 instanceof FloatKeyframeSet) {
         return new PropertyValuesHolder.FloatPropertyValuesHolder(var0, (FloatKeyframeSet)var2);
      } else {
         PropertyValuesHolder var3 = new PropertyValuesHolder(var0);
         var3.mKeyframeSet = var2;
         var3.mValueType = var1[0].getType();
         return var3;
      }
   }

   public static PropertyValuesHolder ofObject(Property var0, TypeEvaluator var1, Object... var2) {
      PropertyValuesHolder var3 = new PropertyValuesHolder(var0);
      var3.setObjectValues(var2);
      var3.setEvaluator(var1);
      return var3;
   }

   public static PropertyValuesHolder ofObject(String var0, TypeEvaluator var1, Object... var2) {
      PropertyValuesHolder var3 = new PropertyValuesHolder(var0);
      var3.setObjectValues(var2);
      var3.setEvaluator(var1);
      return var3;
   }

   private void setupGetter(Class var1) {
      this.mGetter = this.setupSetterOrGetter(var1, sGetterPropertyMap, "get", (Class)null);
   }

   private Method setupSetterOrGetter(Class var1, HashMap var2, String var3, Class var4) {
      Method var5 = null;

      Method var6;
      label260: {
         Throwable var10000;
         label264: {
            HashMap var7;
            boolean var10001;
            try {
               this.mPropertyMapLock.writeLock().lock();
               var7 = (HashMap)var2.get(var1);
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label264;
            }

            if (var7 != null) {
               try {
                  var5 = (Method)var7.get(this.mPropertyName);
               } catch (Throwable var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label264;
               }
            }

            var6 = var5;
            if (var5 != null) {
               break label260;
            }

            try {
               var6 = this.getPropertyFunction(var1, var3, var4);
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label264;
            }

            HashMap var39 = var7;
            if (var7 == null) {
               try {
                  var39 = new HashMap();
                  var2.put(var1, var39);
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label264;
               }
            }

            label244:
            try {
               var39.put(this.mPropertyName, var6);
               break label260;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label244;
            }
         }

         Throwable var38 = var10000;
         this.mPropertyMapLock.writeLock().unlock();
         throw var38;
      }

      this.mPropertyMapLock.writeLock().unlock();
      return var6;
   }

   private void setupValue(Object var1, Keyframe var2) {
      Property var3 = this.mProperty;
      if (var3 != null) {
         var2.setValue(var3.get(var1));
      }

      try {
         if (this.mGetter == null) {
            this.setupGetter(var1.getClass());
         }

         var2.setValue(this.mGetter.invoke(var1));
      } catch (InvocationTargetException var4) {
         Log.e("PropertyValuesHolder", var4.toString());
      } catch (IllegalAccessException var5) {
         Log.e("PropertyValuesHolder", var5.toString());
         return;
      }

   }

   void calculateValue(float var1) {
      this.mAnimatedValue = this.mKeyframeSet.getValue(var1);
   }

   public PropertyValuesHolder clone() {
      try {
         PropertyValuesHolder var1 = (PropertyValuesHolder)super.clone();
         var1.mPropertyName = this.mPropertyName;
         var1.mProperty = this.mProperty;
         var1.mKeyframeSet = this.mKeyframeSet.clone();
         var1.mEvaluator = this.mEvaluator;
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   Object getAnimatedValue() {
      return this.mAnimatedValue;
   }

   public String getPropertyName() {
      return this.mPropertyName;
   }

   void init() {
      TypeEvaluator var2;
      if (this.mEvaluator == null) {
         Class var1 = this.mValueType;
         if (var1 == Integer.class) {
            var2 = sIntEvaluator;
         } else if (var1 == Float.class) {
            var2 = sFloatEvaluator;
         } else {
            var2 = null;
         }

         this.mEvaluator = var2;
      }

      var2 = this.mEvaluator;
      if (var2 != null) {
         this.mKeyframeSet.setEvaluator(var2);
      }

   }

   void setAnimatedValue(Object var1) {
      Property var2 = this.mProperty;
      if (var2 != null) {
         var2.set(var1, this.getAnimatedValue());
      }

      if (this.mSetter != null) {
         try {
            this.mTmpValueArray[0] = this.getAnimatedValue();
            this.mSetter.invoke(var1, this.mTmpValueArray);
            return;
         } catch (InvocationTargetException var3) {
            Log.e("PropertyValuesHolder", var3.toString());
         } catch (IllegalAccessException var4) {
            Log.e("PropertyValuesHolder", var4.toString());
            return;
         }
      }

   }

   public void setEvaluator(TypeEvaluator var1) {
      this.mEvaluator = var1;
      this.mKeyframeSet.setEvaluator(var1);
   }

   public void setFloatValues(float... var1) {
      this.mValueType = Float.TYPE;
      this.mKeyframeSet = KeyframeSet.ofFloat(var1);
   }

   public void setIntValues(int... var1) {
      this.mValueType = Integer.TYPE;
      this.mKeyframeSet = KeyframeSet.ofInt(var1);
   }

   public void setKeyframes(Keyframe... var1) {
      int var3 = var1.length;
      Keyframe[] var4 = new Keyframe[Math.max(var3, 2)];
      this.mValueType = var1[0].getType();

      for(int var2 = 0; var2 < var3; ++var2) {
         var4[var2] = var1[var2];
      }

      this.mKeyframeSet = new KeyframeSet(var4);
   }

   public void setObjectValues(Object... var1) {
      this.mValueType = var1[0].getClass();
      this.mKeyframeSet = KeyframeSet.ofObject(var1);
   }

   public void setProperty(Property var1) {
      this.mProperty = var1;
   }

   public void setPropertyName(String var1) {
      this.mPropertyName = var1;
   }

   void setupEndValue(Object var1) {
      this.setupValue(var1, (Keyframe)this.mKeyframeSet.mKeyframes.get(this.mKeyframeSet.mKeyframes.size() - 1));
   }

   void setupSetter(Class var1) {
      this.mSetter = this.setupSetterOrGetter(var1, sSetterPropertyMap, "set", this.mValueType);
   }

   void setupSetterAndGetter(Object var1) {
      Property var2 = this.mProperty;
      if (var2 != null) {
         label53: {
            boolean var10001;
            Iterator var9;
            try {
               var2.get(var1);
               var9 = this.mKeyframeSet.mKeyframes.iterator();
            } catch (ClassCastException var8) {
               var10001 = false;
               break label53;
            }

            while(true) {
               try {
                  if (!var9.hasNext()) {
                     return;
                  }

                  Keyframe var12 = (Keyframe)var9.next();
                  if (!var12.hasValue()) {
                     var12.setValue(this.mProperty.get(var1));
                  }
               } catch (ClassCastException var7) {
                  var10001 = false;
                  break;
               }
            }
         }

         StringBuilder var10 = new StringBuilder();
         var10.append("No such property (");
         var10.append(this.mProperty.getName());
         var10.append(") on target object ");
         var10.append(var1);
         var10.append(". Trying reflection instead");
         Log.e("PropertyValuesHolder", var10.toString());
         this.mProperty = null;
      }

      Class var11 = var1.getClass();
      if (this.mSetter == null) {
         this.setupSetter(var11);
      }

      Iterator var3 = this.mKeyframeSet.mKeyframes.iterator();

      while(var3.hasNext()) {
         Keyframe var4 = (Keyframe)var3.next();
         if (!var4.hasValue()) {
            if (this.mGetter == null) {
               this.setupGetter(var11);
            }

            try {
               var4.setValue(this.mGetter.invoke(var1));
            } catch (InvocationTargetException var5) {
               Log.e("PropertyValuesHolder", var5.toString());
            } catch (IllegalAccessException var6) {
               Log.e("PropertyValuesHolder", var6.toString());
            }
         }
      }

   }

   void setupStartValue(Object var1) {
      this.setupValue(var1, (Keyframe)this.mKeyframeSet.mKeyframes.get(0));
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.mPropertyName);
      var1.append(": ");
      var1.append(this.mKeyframeSet.toString());
      return var1.toString();
   }

   static class FloatPropertyValuesHolder extends PropertyValuesHolder {
      float mFloatAnimatedValue;
      FloatKeyframeSet mFloatKeyframeSet;
      private FloatProperty mFloatProperty;

      public FloatPropertyValuesHolder(Property var1, FloatKeyframeSet var2) {
         super((Property)var1, null);
         this.mValueType = Float.TYPE;
         this.mKeyframeSet = var2;
         this.mFloatKeyframeSet = (FloatKeyframeSet)this.mKeyframeSet;
         if (var1 instanceof FloatProperty) {
            this.mFloatProperty = (FloatProperty)this.mProperty;
         }

      }

      public FloatPropertyValuesHolder(Property var1, float... var2) {
         super((Property)var1, null);
         this.setFloatValues(var2);
         if (var1 instanceof FloatProperty) {
            this.mFloatProperty = (FloatProperty)this.mProperty;
         }

      }

      public FloatPropertyValuesHolder(String var1, FloatKeyframeSet var2) {
         super((String)var1, null);
         this.mValueType = Float.TYPE;
         this.mKeyframeSet = var2;
         this.mFloatKeyframeSet = (FloatKeyframeSet)this.mKeyframeSet;
      }

      public FloatPropertyValuesHolder(String var1, float... var2) {
         super((String)var1, null);
         this.setFloatValues(var2);
      }

      void calculateValue(float var1) {
         this.mFloatAnimatedValue = this.mFloatKeyframeSet.getFloatValue(var1);
      }

      public PropertyValuesHolder.FloatPropertyValuesHolder clone() {
         PropertyValuesHolder.FloatPropertyValuesHolder var1 = (PropertyValuesHolder.FloatPropertyValuesHolder)super.clone();
         var1.mFloatKeyframeSet = (FloatKeyframeSet)var1.mKeyframeSet;
         return var1;
      }

      Object getAnimatedValue() {
         return this.mFloatAnimatedValue;
      }

      void setAnimatedValue(Object var1) {
         FloatProperty var2 = this.mFloatProperty;
         if (var2 != null) {
            var2.setValue(var1, this.mFloatAnimatedValue);
         } else if (this.mProperty != null) {
            this.mProperty.set(var1, this.mFloatAnimatedValue);
         } else {
            if (this.mSetter != null) {
               try {
                  this.mTmpValueArray[0] = this.mFloatAnimatedValue;
                  this.mSetter.invoke(var1, this.mTmpValueArray);
                  return;
               } catch (InvocationTargetException var3) {
                  Log.e("PropertyValuesHolder", var3.toString());
               } catch (IllegalAccessException var4) {
                  Log.e("PropertyValuesHolder", var4.toString());
                  return;
               }
            }

         }
      }

      public void setFloatValues(float... var1) {
         super.setFloatValues(var1);
         this.mFloatKeyframeSet = (FloatKeyframeSet)this.mKeyframeSet;
      }

      void setupSetter(Class var1) {
         if (this.mProperty == null) {
            super.setupSetter(var1);
         }
      }
   }

   static class IntPropertyValuesHolder extends PropertyValuesHolder {
      int mIntAnimatedValue;
      IntKeyframeSet mIntKeyframeSet;
      private IntProperty mIntProperty;

      public IntPropertyValuesHolder(Property var1, IntKeyframeSet var2) {
         super((Property)var1, null);
         this.mValueType = Integer.TYPE;
         this.mKeyframeSet = var2;
         this.mIntKeyframeSet = (IntKeyframeSet)this.mKeyframeSet;
         if (var1 instanceof IntProperty) {
            this.mIntProperty = (IntProperty)this.mProperty;
         }

      }

      public IntPropertyValuesHolder(Property var1, int... var2) {
         super((Property)var1, null);
         this.setIntValues(var2);
         if (var1 instanceof IntProperty) {
            this.mIntProperty = (IntProperty)this.mProperty;
         }

      }

      public IntPropertyValuesHolder(String var1, IntKeyframeSet var2) {
         super((String)var1, null);
         this.mValueType = Integer.TYPE;
         this.mKeyframeSet = var2;
         this.mIntKeyframeSet = (IntKeyframeSet)this.mKeyframeSet;
      }

      public IntPropertyValuesHolder(String var1, int... var2) {
         super((String)var1, null);
         this.setIntValues(var2);
      }

      void calculateValue(float var1) {
         this.mIntAnimatedValue = this.mIntKeyframeSet.getIntValue(var1);
      }

      public PropertyValuesHolder.IntPropertyValuesHolder clone() {
         PropertyValuesHolder.IntPropertyValuesHolder var1 = (PropertyValuesHolder.IntPropertyValuesHolder)super.clone();
         var1.mIntKeyframeSet = (IntKeyframeSet)var1.mKeyframeSet;
         return var1;
      }

      Object getAnimatedValue() {
         return this.mIntAnimatedValue;
      }

      void setAnimatedValue(Object var1) {
         IntProperty var2 = this.mIntProperty;
         if (var2 != null) {
            var2.setValue(var1, this.mIntAnimatedValue);
         } else if (this.mProperty != null) {
            this.mProperty.set(var1, this.mIntAnimatedValue);
         } else {
            if (this.mSetter != null) {
               try {
                  this.mTmpValueArray[0] = this.mIntAnimatedValue;
                  this.mSetter.invoke(var1, this.mTmpValueArray);
                  return;
               } catch (InvocationTargetException var3) {
                  Log.e("PropertyValuesHolder", var3.toString());
               } catch (IllegalAccessException var4) {
                  Log.e("PropertyValuesHolder", var4.toString());
                  return;
               }
            }

         }
      }

      public void setIntValues(int... var1) {
         super.setIntValues(var1);
         this.mIntKeyframeSet = (IntKeyframeSet)this.mKeyframeSet;
      }

      void setupSetter(Class var1) {
         if (this.mProperty == null) {
            super.setupSetter(var1);
         }
      }
   }
}
