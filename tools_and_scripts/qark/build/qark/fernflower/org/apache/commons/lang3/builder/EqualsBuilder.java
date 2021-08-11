package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.Pair;

public class EqualsBuilder implements Builder {
   private static final ThreadLocal REGISTRY = new ThreadLocal();
   private List bypassReflectionClasses;
   private String[] excludeFields = null;
   private boolean isEquals = true;
   private Class reflectUpToClass = null;
   private boolean testRecursive = false;
   private boolean testTransients = false;

   public EqualsBuilder() {
      ArrayList var1 = new ArrayList();
      this.bypassReflectionClasses = var1;
      var1.add(String.class);
   }

   private void appendArray(Object var1, Object var2) {
      if (var1.getClass() != var2.getClass()) {
         this.setEquals(false);
      } else if (var1 instanceof long[]) {
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
         this.append((Object[])var1, (Object[])var2);
      }
   }

   static Pair getRegisterPair(Object var0, Object var1) {
      return Pair.method_14(new IDKey(var0), new IDKey(var1));
   }

   static Set getRegistry() {
      return (Set)REGISTRY.get();
   }

   static boolean isRegistered(Object var0, Object var1) {
      Set var2 = getRegistry();
      Pair var3 = getRegisterPair(var0, var1);
      Pair var4 = Pair.method_14((IDKey)var3.getRight(), (IDKey)var3.getLeft());
      return var2 != null && (var2.contains(var3) || var2.contains(var4));
   }

   private void reflectionAppend(Object param1, Object param2, Class param3) {
      // $FF: Couldn't be decompiled
   }

   public static boolean reflectionEquals(Object var0, Object var1, Collection var2) {
      return reflectionEquals(var0, var1, ReflectionToStringBuilder.toNoNullStringArray(var2));
   }

   public static boolean reflectionEquals(Object var0, Object var1, boolean var2) {
      return reflectionEquals(var0, var1, var2, (Class)null);
   }

   public static boolean reflectionEquals(Object var0, Object var1, boolean var2, Class var3, boolean var4, String... var5) {
      if (var0 == var1) {
         return true;
      } else {
         return var0 != null && var1 != null ? (new EqualsBuilder()).setExcludeFields(var5).setReflectUpToClass(var3).setTestTransients(var2).setTestRecursive(var4).reflectionAppend(var0, var1).isEquals() : false;
      }
   }

   public static boolean reflectionEquals(Object var0, Object var1, boolean var2, Class var3, String... var4) {
      return reflectionEquals(var0, var1, var2, var3, false, var4);
   }

   public static boolean reflectionEquals(Object var0, Object var1, String... var2) {
      return reflectionEquals(var0, var1, false, (Class)null, var2);
   }

   private static void register(Object var0, Object var1) {
      Set var3 = getRegistry();
      Object var2 = var3;
      if (var3 == null) {
         var2 = new HashSet();
         REGISTRY.set(var2);
      }

      ((Set)var2).add(getRegisterPair(var0, var1));
   }

   private static void unregister(Object var0, Object var1) {
      Set var2 = getRegistry();
      if (var2 != null) {
         var2.remove(getRegisterPair(var0, var1));
         if (var2.isEmpty()) {
            REGISTRY.remove();
         }
      }

   }

   public EqualsBuilder append(byte var1, byte var2) {
      if (!this.isEquals) {
         return this;
      } else {
         boolean var3;
         if (var1 == var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isEquals = var3;
         return this;
      }
   }

   public EqualsBuilder append(char var1, char var2) {
      if (!this.isEquals) {
         return this;
      } else {
         boolean var3;
         if (var1 == var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isEquals = var3;
         return this;
      }
   }

   public EqualsBuilder append(double var1, double var3) {
      return !this.isEquals ? this : this.append(Double.doubleToLongBits(var1), Double.doubleToLongBits(var3));
   }

   public EqualsBuilder append(float var1, float var2) {
      return !this.isEquals ? this : this.append(Float.floatToIntBits(var1), Float.floatToIntBits(var2));
   }

   public EqualsBuilder append(int var1, int var2) {
      if (!this.isEquals) {
         return this;
      } else {
         boolean var3;
         if (var1 == var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isEquals = var3;
         return this;
      }
   }

   public EqualsBuilder append(long var1, long var3) {
      if (!this.isEquals) {
         return this;
      } else {
         boolean var5;
         if (var1 == var3) {
            var5 = true;
         } else {
            var5 = false;
         }

         this.isEquals = var5;
         return this;
      }
   }

   public EqualsBuilder append(Object var1, Object var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         Class var3 = var1.getClass();
         if (var3.isArray()) {
            this.appendArray(var1, var2);
            return this;
         } else if (this.testRecursive && !ClassUtils.isPrimitiveOrWrapper(var3)) {
            this.reflectionAppend(var1, var2);
            return this;
         } else {
            this.isEquals = var1.equals(var2);
            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(short var1, short var2) {
      if (!this.isEquals) {
         return this;
      } else {
         boolean var3;
         if (var1 == var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isEquals = var3;
         return this;
      }
   }

   public EqualsBuilder append(boolean var1, boolean var2) {
      if (!this.isEquals) {
         return this;
      } else {
         if (var1 == var2) {
            var1 = true;
         } else {
            var1 = false;
         }

         this.isEquals = var1;
         return this;
      }
   }

   public EqualsBuilder append(byte[] var1, byte[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(char[] var1, char[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(double[] var1, double[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(float[] var1, float[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(int[] var1, int[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(long[] var1, long[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(Object[] var1, Object[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(short[] var1, short[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder append(boolean[] var1, boolean[] var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         if (var1.length != var2.length) {
            this.setEquals(false);
            return this;
         } else {
            for(int var3 = 0; var3 < var1.length && this.isEquals; ++var3) {
               this.append(var1[var3], var2[var3]);
            }

            return this;
         }
      } else {
         this.setEquals(false);
         return this;
      }
   }

   public EqualsBuilder appendSuper(boolean var1) {
      if (!this.isEquals) {
         return this;
      } else {
         this.isEquals = var1;
         return this;
      }
   }

   public Boolean build() {
      return this.isEquals();
   }

   public boolean isEquals() {
      return this.isEquals;
   }

   public EqualsBuilder reflectionAppend(Object var1, Object var2) {
      if (!this.isEquals) {
         return this;
      } else if (var1 == var2) {
         return this;
      } else if (var1 != null && var2 != null) {
         Class var4 = var1.getClass();
         Class var5 = var2.getClass();
         Class var3;
         if (var4.isInstance(var2)) {
            var3 = var4;
            if (!var5.isInstance(var1)) {
               var3 = var5;
            }
         } else {
            if (!var5.isInstance(var1)) {
               this.isEquals = false;
               return this;
            }

            var3 = var5;
            if (!var4.isInstance(var2)) {
               var3 = var4;
            }
         }

         label91: {
            boolean var10001;
            try {
               if (var3.isArray()) {
                  this.append(var1, var2);
                  return this;
               }
            } catch (IllegalArgumentException var10) {
               var10001 = false;
               break label91;
            }

            label76: {
               try {
                  if (this.bypassReflectionClasses == null || !this.bypassReflectionClasses.contains(var4) && !this.bypassReflectionClasses.contains(var5)) {
                     break label76;
                  }
               } catch (IllegalArgumentException var9) {
                  var10001 = false;
                  break label91;
               }

               try {
                  this.isEquals = var1.equals(var2);
                  return this;
               } catch (IllegalArgumentException var6) {
                  var10001 = false;
                  break label91;
               }
            }

            try {
               this.reflectionAppend(var1, var2, var3);
            } catch (IllegalArgumentException var8) {
               var10001 = false;
               break label91;
            }

            while(true) {
               try {
                  if (var3.getSuperclass() == null || var3 == this.reflectUpToClass) {
                     return this;
                  }

                  var3 = var3.getSuperclass();
                  this.reflectionAppend(var1, var2, var3);
               } catch (IllegalArgumentException var7) {
                  var10001 = false;
                  break;
               }
            }
         }

         this.isEquals = false;
         return this;
      } else {
         this.isEquals = false;
         return this;
      }
   }

   public void reset() {
      this.isEquals = true;
   }

   public EqualsBuilder setBypassReflectionClasses(List var1) {
      this.bypassReflectionClasses = var1;
      return this;
   }

   protected void setEquals(boolean var1) {
      this.isEquals = var1;
   }

   public EqualsBuilder setExcludeFields(String... var1) {
      this.excludeFields = var1;
      return this;
   }

   public EqualsBuilder setReflectUpToClass(Class var1) {
      this.reflectUpToClass = var1;
      return this;
   }

   public EqualsBuilder setTestRecursive(boolean var1) {
      this.testRecursive = var1;
      return this;
   }

   public EqualsBuilder setTestTransients(boolean var1) {
      this.testTransients = var1;
      return this;
   }
}
