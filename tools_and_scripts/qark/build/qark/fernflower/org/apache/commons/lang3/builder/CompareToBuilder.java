package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;
import org.apache.commons.lang3.ArrayUtils;

public class CompareToBuilder implements Builder {
   private int comparison = 0;

   private void appendArray(Object var1, Object var2, Comparator var3) {
      if (var1 instanceof long[]) {
         this.append((long[])var1, (long[])var2);
      } else if (var1 instanceof int[]) {
         this.append((int[])var1, (int[])var2);
      } else if (var1 instanceof short[]) {
         this.append((short[])var1, (short[])var2);
      } else if (var1 instanceof char[]) {
         this.append((char[])var1, (char[])var2);
      } else if (var1 instanceof byte[]) {
         this.append((byte[])var1, (byte[])var2);
      } else if (var1 instanceof double[]) {
         this.append((double[])var1, (double[])var2);
      } else if (var1 instanceof float[]) {
         this.append((float[])var1, (float[])var2);
      } else if (var1 instanceof boolean[]) {
         this.append((boolean[])var1, (boolean[])var2);
      } else {
         this.append((Object[])var1, (Object[])var2, var3);
      }
   }

   private static void reflectionAppend(Object var0, Object var1, Class var2, CompareToBuilder var3, boolean var4, String[] var5) {
      Field[] var9 = var2.getDeclaredFields();
      AccessibleObject.setAccessible(var9, true);

      for(int var6 = 0; var6 < var9.length && var3.comparison == 0; ++var6) {
         Field var7 = var9[var6];
         if (!ArrayUtils.contains(var5, var7.getName()) && !var7.getName().contains("$") && (var4 || !Modifier.isTransient(var7.getModifiers())) && !Modifier.isStatic(var7.getModifiers())) {
            try {
               var3.append(var7.get(var0), var7.get(var1));
            } catch (IllegalAccessException var8) {
               throw new InternalError("Unexpected IllegalAccessException");
            }
         }
      }

   }

   public static int reflectionCompare(Object var0, Object var1) {
      return reflectionCompare(var0, var1, false, (Class)null);
   }

   public static int reflectionCompare(Object var0, Object var1, Collection var2) {
      return reflectionCompare(var0, var1, ReflectionToStringBuilder.toNoNullStringArray(var2));
   }

   public static int reflectionCompare(Object var0, Object var1, boolean var2) {
      return reflectionCompare(var0, var1, var2, (Class)null);
   }

   public static int reflectionCompare(Object var0, Object var1, boolean var2, Class var3, String... var4) {
      if (var0 == var1) {
         return 0;
      } else if (var0 != null && var1 != null) {
         Class var5 = var0.getClass();
         if (!var5.isInstance(var1)) {
            throw new ClassCastException();
         } else {
            CompareToBuilder var6 = new CompareToBuilder();
            reflectionAppend(var0, var1, var5, var6, var2, var4);

            while(var5.getSuperclass() != null && var5 != var3) {
               var5 = var5.getSuperclass();
               reflectionAppend(var0, var1, var5, var6, var2, var4);
            }

            return var6.toComparison();
         }
      } else {
         throw null;
      }
   }

   public static int reflectionCompare(Object var0, Object var1, String... var2) {
      return reflectionCompare(var0, var1, false, (Class)null, var2);
   }

   public CompareToBuilder append(byte var1, byte var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Byte.compare(var1, var2);
         return this;
      }
   }

   public CompareToBuilder append(char var1, char var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Character.compare(var1, var2);
         return this;
      }
   }

   public CompareToBuilder append(double var1, double var3) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Double.compare(var1, var3);
         return this;
      }
   }

   public CompareToBuilder append(float var1, float var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Float.compare(var1, var2);
         return this;
      }
   }

   public CompareToBuilder append(int var1, int var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Integer.compare(var1, var2);
         return this;
      }
   }

   public CompareToBuilder append(long var1, long var3) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Long.compare(var1, var3);
         return this;
      }
   }

   public CompareToBuilder append(Object var1, Object var2) {
      return this.append((Object)var1, (Object)var2, (Comparator)null);
   }

   public CompareToBuilder append(Object var1, Object var2, Comparator var3) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 == null) {
         this.comparison = -1;
         return this;
      } else if (var2 == null) {
         this.comparison = 1;
         return this;
      } else if (var1.getClass().isArray()) {
         this.appendArray(var1, var2, var3);
         return this;
      } else if (var3 == null) {
         this.comparison = ((Comparable)var1).compareTo(var2);
         return this;
      } else {
         this.comparison = var3.compare(var1, var2);
         return this;
      }
   }

   public CompareToBuilder append(short var1, short var2) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = Short.compare(var1, var2);
         return this;
      }
   }

   public CompareToBuilder append(boolean var1, boolean var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1) {
         this.comparison = 1;
         return this;
      } else {
         this.comparison = -1;
         return this;
      }
   }

   public CompareToBuilder append(byte[] var1, byte[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var3 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var3 = 1;
            }

            this.comparison = var3;
            return this;
         } else {
            for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
               this.append(var1[var4], var2[var4]);
            }

            return this;
         }
      }
   }

   public CompareToBuilder append(char[] var1, char[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var3 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var3 = 1;
            }

            this.comparison = var3;
            return this;
         } else {
            for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
               this.append(var1[var4], var2[var4]);
            }

            return this;
         }
      }
   }

   public CompareToBuilder append(double[] var1, double[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var3 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var3 = 1;
            }

            this.comparison = var3;
            return this;
         } else {
            for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
               this.append(var1[var4], var2[var4]);
            }

            return this;
         }
      }
   }

   public CompareToBuilder append(float[] var1, float[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var3 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var3 = 1;
            }

            this.comparison = var3;
            return this;
         } else {
            for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
               this.append(var1[var4], var2[var4]);
            }

            return this;
         }
      }
   }

   public CompareToBuilder append(int[] var1, int[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var3 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var3 = 1;
            }

            this.comparison = var3;
            return this;
         } else {
            for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
               this.append(var1[var4], var2[var4]);
            }

            return this;
         }
      }
   }

   public CompareToBuilder append(long[] var1, long[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var3 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var3 = 1;
            }

            this.comparison = var3;
            return this;
         } else {
            for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
               this.append(var1[var4], var2[var4]);
            }

            return this;
         }
      }
   }

   public CompareToBuilder append(Object[] var1, Object[] var2) {
      return this.append((Object[])var1, (Object[])var2, (Comparator)null);
   }

   public CompareToBuilder append(Object[] var1, Object[] var2, Comparator var3) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var4 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var4 = 1;
            }

            this.comparison = var4;
            return this;
         } else {
            for(int var5 = 0; var5 < var1.length && this.comparison == 0; ++var5) {
               this.append(var1[var5], var2[var5], var3);
            }

            return this;
         }
      }
   }

   public CompareToBuilder append(short[] var1, short[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var3 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var3 = 1;
            }

            this.comparison = var3;
            return this;
         } else {
            for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
               this.append(var1[var4], var2[var4]);
            }

            return this;
         }
      }
   }

   public CompareToBuilder append(boolean[] var1, boolean[] var2) {
      if (this.comparison != 0) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else {
         byte var3 = -1;
         if (var1 == null) {
            this.comparison = -1;
            return this;
         } else if (var2 == null) {
            this.comparison = 1;
            return this;
         } else if (var1.length != var2.length) {
            if (var1.length >= var2.length) {
               var3 = 1;
            }

            this.comparison = var3;
            return this;
         } else {
            for(int var4 = 0; var4 < var1.length && this.comparison == 0; ++var4) {
               this.append(var1[var4], var2[var4]);
            }

            return this;
         }
      }
   }

   public CompareToBuilder appendSuper(int var1) {
      if (this.comparison != 0) {
         return this;
      } else {
         this.comparison = var1;
         return this;
      }
   }

   public Integer build() {
      return this.toComparison();
   }

   public int toComparison() {
      return this.comparison;
   }
}
