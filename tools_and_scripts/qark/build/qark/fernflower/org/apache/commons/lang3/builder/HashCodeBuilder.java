package org.apache.commons.lang3.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Validate;

public class HashCodeBuilder implements Builder {
   private static final int DEFAULT_INITIAL_VALUE = 17;
   private static final int DEFAULT_MULTIPLIER_VALUE = 37;
   private static final ThreadLocal REGISTRY = new ThreadLocal();
   private final int iConstant;
   private int iTotal = 0;

   public HashCodeBuilder() {
      this.iConstant = 37;
      this.iTotal = 17;
   }

   public HashCodeBuilder(int var1, int var2) {
      boolean var4 = true;
      boolean var3;
      if (var1 % 2 != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "HashCodeBuilder requires an odd initial value");
      if (var2 % 2 != 0) {
         var3 = var4;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "HashCodeBuilder requires an odd multiplier");
      this.iConstant = var2;
      this.iTotal = var1;
   }

   private void appendArray(Object var1) {
      if (var1 instanceof long[]) {
         this.append((long[])var1);
      } else if (var1 instanceof int[]) {
         this.append((int[])var1);
      } else if (var1 instanceof short[]) {
         this.append((short[])var1);
      } else if (var1 instanceof char[]) {
         this.append((char[])var1);
      } else if (var1 instanceof byte[]) {
         this.append((byte[])var1);
      } else if (var1 instanceof double[]) {
         this.append((double[])var1);
      } else if (var1 instanceof float[]) {
         this.append((float[])var1);
      } else if (var1 instanceof boolean[]) {
         this.append((boolean[])var1);
      } else {
         this.append((Object[])var1);
      }
   }

   static Set getRegistry() {
      return (Set)REGISTRY.get();
   }

   static boolean isRegistered(Object var0) {
      Set var1 = getRegistry();
      return var1 != null && var1.contains(new IDKey(var0));
   }

   private static void reflectionAppend(Object param0, Class param1, HashCodeBuilder param2, boolean param3, String[] param4) {
      // $FF: Couldn't be decompiled
   }

   public static int reflectionHashCode(int var0, int var1, Object var2) {
      return reflectionHashCode(var0, var1, var2, false, (Class)null);
   }

   public static int reflectionHashCode(int var0, int var1, Object var2, boolean var3) {
      return reflectionHashCode(var0, var1, var2, var3, (Class)null);
   }

   public static int reflectionHashCode(int var0, int var1, Object var2, boolean var3, Class var4, String... var5) {
      boolean var6;
      if (var2 != null) {
         var6 = true;
      } else {
         var6 = false;
      }

      Validate.isTrue(var6, "The object to build a hash code for must not be null");
      HashCodeBuilder var8 = new HashCodeBuilder(var0, var1);
      Class var7 = var2.getClass();
      reflectionAppend(var2, var7, var8, var3, var5);

      while(var7.getSuperclass() != null && var7 != var4) {
         var7 = var7.getSuperclass();
         reflectionAppend(var2, var7, var8, var3, var5);
      }

      return var8.toHashCode();
   }

   public static int reflectionHashCode(Object var0, Collection var1) {
      return reflectionHashCode(var0, ReflectionToStringBuilder.toNoNullStringArray(var1));
   }

   public static int reflectionHashCode(Object var0, boolean var1) {
      return reflectionHashCode(17, 37, var0, var1, (Class)null);
   }

   public static int reflectionHashCode(Object var0, String... var1) {
      return reflectionHashCode(17, 37, var0, false, (Class)null, var1);
   }

   private static void register(Object var0) {
      Set var2 = getRegistry();
      Object var1 = var2;
      if (var2 == null) {
         var1 = new HashSet();
         REGISTRY.set(var1);
      }

      ((Set)var1).add(new IDKey(var0));
   }

   private static void unregister(Object var0) {
      Set var1 = getRegistry();
      if (var1 != null) {
         var1.remove(new IDKey(var0));
         if (var1.isEmpty()) {
            REGISTRY.remove();
         }
      }

   }

   public HashCodeBuilder append(byte var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public HashCodeBuilder append(char var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public HashCodeBuilder append(double var1) {
      return this.append(Double.doubleToLongBits(var1));
   }

   public HashCodeBuilder append(float var1) {
      this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(var1);
      return this;
   }

   public HashCodeBuilder append(int var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public HashCodeBuilder append(long var1) {
      this.iTotal = this.iTotal * this.iConstant + (int)(var1 >> 32 ^ var1);
      return this;
   }

   public HashCodeBuilder append(Object var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else if (var1.getClass().isArray()) {
         this.appendArray(var1);
         return this;
      } else {
         this.iTotal = this.iTotal * this.iConstant + var1.hashCode();
         return this;
      }
   }

   public HashCodeBuilder append(short var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public HashCodeBuilder append(boolean var1) {
      this.iTotal = this.iTotal * this.iConstant + (var1 ^ 1);
      return this;
   }

   public HashCodeBuilder append(byte[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder append(char[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder append(double[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder append(float[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder append(int[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder append(long[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder append(Object[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder append(short[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder append(boolean[] var1) {
      if (var1 == null) {
         this.iTotal *= this.iConstant;
         return this;
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.append(var1[var2]);
         }

         return this;
      }
   }

   public HashCodeBuilder appendSuper(int var1) {
      this.iTotal = this.iTotal * this.iConstant + var1;
      return this;
   }

   public Integer build() {
      return this.toHashCode();
   }

   public int hashCode() {
      return this.toHashCode();
   }

   public int toHashCode() {
      return this.iTotal;
   }
}
