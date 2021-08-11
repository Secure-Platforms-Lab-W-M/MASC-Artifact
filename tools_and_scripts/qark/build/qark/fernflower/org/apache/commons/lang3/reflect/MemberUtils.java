package org.apache.commons.lang3.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ClassUtils;

abstract class MemberUtils {
   private static final int ACCESS_TEST = 7;
   private static final Class[] ORDERED_PRIMITIVE_TYPES;

   static {
      ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
   }

   static int compareConstructorFit(Constructor var0, Constructor var1, Class[] var2) {
      return compareParameterTypes(MemberUtils.Executable.method_35(var0), MemberUtils.Executable.method_35(var1), var2);
   }

   static int compareMethodFit(Method var0, Method var1, Class[] var2) {
      return compareParameterTypes(MemberUtils.Executable.method_36(var0), MemberUtils.Executable.method_36(var1), var2);
   }

   private static int compareParameterTypes(MemberUtils.Executable var0, MemberUtils.Executable var1, Class[] var2) {
      return Float.compare(getTotalTransformationCost(var2, var0), getTotalTransformationCost(var2, var1));
   }

   private static float getObjectTransformationCost(Class var0, Class var1) {
      if (var1.isPrimitive()) {
         return getPrimitivePromotionCost(var0, var1);
      } else {
         float var3 = 0.0F;

         float var2;
         while(true) {
            var2 = var3;
            if (var0 == null) {
               break;
            }

            var2 = var3;
            if (var1.equals(var0)) {
               break;
            }

            if (var1.isInterface() && ClassUtils.isAssignable(var0, var1)) {
               var2 = var3 + 0.25F;
               break;
            }

            ++var3;
            var0 = var0.getSuperclass();
         }

         var3 = var2;
         if (var0 == null) {
            var3 = var2 + 1.5F;
         }

         return var3;
      }
   }

   private static float getPrimitivePromotionCost(Class var0, Class var1) {
      float var2 = 0.0F;
      Class var5 = var0;
      var0 = var0;
      if (!var5.isPrimitive()) {
         var2 = 0.0F + 0.1F;
         var0 = ClassUtils.wrapperToPrimitive(var5);
      }

      int var4 = 0;

      float var3;
      for(var3 = var2; var0 != var1; var0 = var5) {
         Class[] var6 = ORDERED_PRIMITIVE_TYPES;
         if (var4 >= var6.length) {
            break;
         }

         var2 = var3;
         var5 = var0;
         if (var0 == var6[var4]) {
            var3 += 0.1F;
            var2 = var3;
            var5 = var0;
            if (var4 < var6.length - 1) {
               var5 = var6[var4 + 1];
               var2 = var3;
            }
         }

         ++var4;
         var3 = var2;
      }

      return var3;
   }

   private static float getTotalTransformationCost(Class[] var0, MemberUtils.Executable var1) {
      Class[] var10 = var1.getParameterTypes();
      boolean var7 = var1.isVarArgs();
      float var2 = 0.0F;
      int var5 = var10.length;
      int var4 = var5;
      if (var7) {
         var4 = var5 - 1;
      }

      long var8 = (long)var4;
      if ((long)var0.length < var8) {
         return Float.MAX_VALUE;
      } else {
         for(var4 = 0; (long)var4 < var8; ++var4) {
            var2 += getObjectTransformationCost(var0[var4], var10[var4]);
         }

         float var3 = var2;
         if (var7) {
            var4 = var0.length;
            var5 = var10.length;
            boolean var6 = false;
            boolean var13;
            if (var4 < var5) {
               var13 = true;
            } else {
               var13 = false;
            }

            boolean var12 = var6;
            if (var0.length == var10.length) {
               var12 = var6;
               if (var0[var0.length - 1].isArray()) {
                  var12 = true;
               }
            }

            Class var11 = var10[var10.length - 1].getComponentType();
            if (var13) {
               return var2 + getObjectTransformationCost(var11, Object.class) + 0.001F;
            }

            if (var12) {
               return var2 + getObjectTransformationCost(var0[var0.length - 1].getComponentType(), var11) + 0.001F;
            }

            var4 = var10.length - 1;

            while(true) {
               var3 = var2;
               if (var4 >= var0.length) {
                  break;
               }

               var2 += getObjectTransformationCost(var0[var4], var11) + 0.001F;
               ++var4;
            }
         }

         return var3;
      }
   }

   static boolean isAccessible(Member var0) {
      return var0 != null && Modifier.isPublic(var0.getModifiers()) && !var0.isSynthetic();
   }

   static boolean isMatchingConstructor(Constructor var0, Class[] var1) {
      return isMatchingExecutable(MemberUtils.Executable.method_35(var0), var1);
   }

   private static boolean isMatchingExecutable(MemberUtils.Executable var0, Class[] var1) {
      Class[] var3 = var0.getParameterTypes();
      if (ClassUtils.isAssignable(var1, var3, true)) {
         return true;
      } else if (!var0.isVarArgs()) {
         return false;
      } else {
         int var2;
         for(var2 = 0; var2 < var3.length - 1 && var2 < var1.length; ++var2) {
            if (!ClassUtils.isAssignable(var1[var2], var3[var2], true)) {
               return false;
            }
         }

         for(Class var4 = var3[var3.length - 1].getComponentType(); var2 < var1.length; ++var2) {
            if (!ClassUtils.isAssignable(var1[var2], var4, true)) {
               return false;
            }
         }

         return true;
      }
   }

   static boolean isMatchingMethod(Method var0, Class[] var1) {
      return isMatchingExecutable(MemberUtils.Executable.method_36(var0), var1);
   }

   static boolean isPackageAccess(int var0) {
      return (var0 & 7) == 0;
   }

   static boolean setAccessibleWorkaround(AccessibleObject var0) {
      if (var0 != null) {
         if (var0.isAccessible()) {
            return false;
         } else {
            Member var1 = (Member)var0;
            if (!var0.isAccessible() && Modifier.isPublic(var1.getModifiers()) && isPackageAccess(var1.getDeclaringClass().getModifiers())) {
               try {
                  var0.setAccessible(true);
                  return true;
               } catch (SecurityException var2) {
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private static final class Executable {
      private final boolean isVarArgs;
      private final Class[] parameterTypes;

      private Executable(Constructor var1) {
         this.parameterTypes = var1.getParameterTypes();
         this.isVarArgs = var1.isVarArgs();
      }

      private Executable(Method var1) {
         this.parameterTypes = var1.getParameterTypes();
         this.isVarArgs = var1.isVarArgs();
      }

      // $FF: renamed from: of (java.lang.reflect.Constructor) org.apache.commons.lang3.reflect.MemberUtils$Executable
      private static MemberUtils.Executable method_35(Constructor var0) {
         return new MemberUtils.Executable(var0);
      }

      // $FF: renamed from: of (java.lang.reflect.Method) org.apache.commons.lang3.reflect.MemberUtils$Executable
      private static MemberUtils.Executable method_36(Method var0) {
         return new MemberUtils.Executable(var0);
      }

      public Class[] getParameterTypes() {
         return this.parameterTypes;
      }

      public boolean isVarArgs() {
         return this.isVarArgs;
      }
   }
}
