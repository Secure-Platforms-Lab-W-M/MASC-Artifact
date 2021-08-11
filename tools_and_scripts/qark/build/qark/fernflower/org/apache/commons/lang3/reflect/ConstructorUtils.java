package org.apache.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

public class ConstructorUtils {
   public static Constructor getAccessibleConstructor(Class var0, Class... var1) {
      Validate.notNull(var0, "class cannot be null");

      try {
         Constructor var3 = getAccessibleConstructor(var0.getConstructor(var1));
         return var3;
      } catch (NoSuchMethodException var2) {
         return null;
      }
   }

   public static Constructor getAccessibleConstructor(Constructor var0) {
      Validate.notNull(var0, "constructor cannot be null");
      return MemberUtils.isAccessible(var0) && isAccessible(var0.getDeclaringClass()) ? var0 : null;
   }

   public static Constructor getMatchingAccessibleConstructor(Class var0, Class... var1) {
      int var2 = 0;
      Validate.notNull(var0, "class cannot be null");

      Constructor var4;
      try {
         var4 = var0.getConstructor(var1);
         MemberUtils.setAccessibleWorkaround(var4);
         return var4;
      } catch (NoSuchMethodException var7) {
         var4 = null;
         Constructor[] var6 = var0.getConstructors();
         int var3 = var6.length;

         Constructor var8;
         for(var8 = var4; var2 < var3; var8 = var4) {
            Constructor var5 = var6[var2];
            var4 = var8;
            if (MemberUtils.isMatchingConstructor(var5, var1)) {
               var5 = getAccessibleConstructor(var5);
               var4 = var8;
               if (var5 != null) {
                  label36: {
                     MemberUtils.setAccessibleWorkaround(var5);
                     if (var8 != null) {
                        var4 = var8;
                        if (MemberUtils.compareConstructorFit(var5, var8, var1) >= 0) {
                           break label36;
                        }
                     }

                     var4 = var5;
                  }
               }
            }

            ++var2;
         }

         return var8;
      }
   }

   public static Object invokeConstructor(Class var0, Object... var1) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      var1 = ArrayUtils.nullToEmpty(var1);
      return invokeConstructor(var0, var1, ClassUtils.toClass(var1));
   }

   public static Object invokeConstructor(Class var0, Object[] var1, Class[] var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      var1 = ArrayUtils.nullToEmpty(var1);
      Constructor var5 = getMatchingAccessibleConstructor(var0, ArrayUtils.nullToEmpty(var2));
      if (var5 != null) {
         Object[] var3 = var1;
         if (var5.isVarArgs()) {
            var3 = MethodUtils.getVarArgs(var1, var5.getParameterTypes());
         }

         return var5.newInstance(var3);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("No such accessible constructor on object: ");
         var4.append(var0.getName());
         throw new NoSuchMethodException(var4.toString());
      }
   }

   public static Object invokeExactConstructor(Class var0, Object... var1) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      var1 = ArrayUtils.nullToEmpty(var1);
      return invokeExactConstructor(var0, var1, ClassUtils.toClass(var1));
   }

   public static Object invokeExactConstructor(Class var0, Object[] var1, Class[] var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      var1 = ArrayUtils.nullToEmpty(var1);
      Constructor var4 = getAccessibleConstructor(var0, ArrayUtils.nullToEmpty(var2));
      if (var4 != null) {
         return var4.newInstance(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("No such accessible constructor on object: ");
         var3.append(var0.getName());
         throw new NoSuchMethodException(var3.toString());
      }
   }

   private static boolean isAccessible(Class var0) {
      while(var0 != null) {
         if (!Modifier.isPublic(var0.getModifiers())) {
            return false;
         }

         var0 = var0.getEnclosingClass();
      }

      return true;
   }
}
