package org.apache.commons.lang3.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

public class MethodUtils {
   private static int distance(Class[] var0, Class[] var1) {
      int var2 = 0;
      if (!ClassUtils.isAssignable(var0, var1, true)) {
         return -1;
      } else {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            if (!var0[var3].equals(var1[var3])) {
               if (ClassUtils.isAssignable(var0[var3], var1[var3], true) && !ClassUtils.isAssignable(var0[var3], var1[var3], false)) {
                  ++var2;
               } else {
                  var2 += 2;
               }
            }
         }

         return var2;
      }
   }

   public static Method getAccessibleMethod(Class var0, String var1, Class... var2) {
      try {
         Method var4 = getAccessibleMethod(var0.getMethod(var1, var2));
         return var4;
      } catch (NoSuchMethodException var3) {
         return null;
      }
   }

   public static Method getAccessibleMethod(Method var0) {
      if (!MemberUtils.isAccessible(var0)) {
         return null;
      } else {
         Class var2 = var0.getDeclaringClass();
         if (Modifier.isPublic(var2.getModifiers())) {
            return var0;
         } else {
            String var3 = var0.getName();
            Class[] var4 = var0.getParameterTypes();
            Method var1 = getAccessibleMethodFromInterfaceNest(var2, var3, var4);
            var0 = var1;
            if (var1 == null) {
               var0 = getAccessibleMethodFromSuperclass(var2, var3, var4);
            }

            return var0;
         }
      }
   }

   private static Method getAccessibleMethodFromInterfaceNest(Class var0, String var1, Class... var2) {
      while(var0 != null) {
         Class[] var5 = var0.getInterfaces();
         int var4 = var5.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            Class var6 = var5[var3];
            if (Modifier.isPublic(var6.getModifiers())) {
               try {
                  Method var7 = var6.getDeclaredMethod(var1, var2);
                  return var7;
               } catch (NoSuchMethodException var8) {
                  Method var9 = getAccessibleMethodFromInterfaceNest(var6, var1, var2);
                  if (var9 != null) {
                     return var9;
                  }
               }
            }
         }

         var0 = var0.getSuperclass();
      }

      return null;
   }

   private static Method getAccessibleMethodFromSuperclass(Class var0, String var1, Class... var2) {
      for(var0 = var0.getSuperclass(); var0 != null; var0 = var0.getSuperclass()) {
         if (Modifier.isPublic(var0.getModifiers())) {
            try {
               Method var4 = var0.getMethod(var1, var2);
               return var4;
            } catch (NoSuchMethodException var3) {
               return null;
            }
         }
      }

      return null;
   }

   private static List getAllSuperclassesAndInterfaces(Class var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var4 = new ArrayList();
         List var5 = ClassUtils.getAllSuperclasses(var0);
         int var1 = 0;
         List var6 = ClassUtils.getAllInterfaces(var0);

         for(int var2 = 0; var2 < var6.size() || var1 < var5.size(); var4.add(var0)) {
            int var3;
            if (var2 >= var6.size()) {
               var3 = var1 + 1;
               var0 = (Class)var5.get(var1);
               var1 = var3;
            } else if (var1 >= var5.size()) {
               var0 = (Class)var6.get(var2);
               ++var2;
            } else if (var2 < var1) {
               var0 = (Class)var6.get(var2);
               ++var2;
            } else if (var1 < var2) {
               var3 = var1 + 1;
               var0 = (Class)var5.get(var1);
               var1 = var3;
            } else {
               var0 = (Class)var6.get(var2);
               ++var2;
            }
         }

         return var4;
      }
   }

   public static Annotation getAnnotation(Method var0, Class var1, boolean var2, boolean var3) {
      boolean var5 = true;
      boolean var4;
      if (var0 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "The method must not be null");
      if (var1 != null) {
         var4 = var5;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "The annotation class must not be null");
      if (!var3 && !MemberUtils.isAccessible(var0)) {
         return null;
      } else {
         Annotation var6 = var0.getAnnotation(var1);
         Annotation var7 = var6;
         if (var6 == null) {
            var7 = var6;
            if (var2) {
               Iterator var8 = getAllSuperclassesAndInterfaces(var0.getDeclaringClass()).iterator();

               do {
                  Method var11;
                  while(true) {
                     var7 = var6;
                     if (!var8.hasNext()) {
                        return var7;
                     }

                     Class var12 = (Class)var8.next();
                     Method var13;
                     boolean var10001;
                     if (var3) {
                        try {
                           var13 = var12.getDeclaredMethod(var0.getName(), var0.getParameterTypes());
                        } catch (NoSuchMethodException var9) {
                           var10001 = false;
                           continue;
                        }

                        var11 = var13;
                     } else {
                        try {
                           var13 = var12.getMethod(var0.getName(), var0.getParameterTypes());
                        } catch (NoSuchMethodException var10) {
                           var10001 = false;
                           continue;
                        }

                        var11 = var13;
                     }
                     break;
                  }

                  var6 = var11.getAnnotation(var1);
               } while(var6 == null);

               return var6;
            }
         }

         return var7;
      }
   }

   public static Method getMatchingAccessibleMethod(Class var0, String var1, Class... var2) {
      Method var5;
      try {
         var5 = var0.getMethod(var1, var2);
         MemberUtils.setAccessibleWorkaround(var5);
         return var5;
      } catch (NoSuchMethodException var8) {
         var5 = null;
         Method[] var7 = var0.getMethods();
         int var4 = var7.length;
         int var3 = 0;

         Method var9;
         for(var9 = var5; var3 < var4; var9 = var5) {
            Method var6 = var7[var3];
            var5 = var9;
            if (var6.getName().equals(var1)) {
               var5 = var9;
               if (MemberUtils.isMatchingMethod(var6, var2)) {
                  var6 = getAccessibleMethod(var6);
                  var5 = var9;
                  if (var6 != null) {
                     label57: {
                        if (var9 != null) {
                           var5 = var9;
                           if (MemberUtils.compareMethodFit(var6, var9, var2) >= 0) {
                              break label57;
                           }
                        }

                        var5 = var6;
                     }
                  }
               }
            }

            ++var3;
         }

         if (var9 != null) {
            MemberUtils.setAccessibleWorkaround(var9);
         }

         if (var9 != null && var9.isVarArgs() && var9.getParameterTypes().length > 0 && var2.length > 0) {
            Class[] var10 = var9.getParameterTypes();
            var1 = ClassUtils.primitiveToWrapper(var10[var10.length - 1].getComponentType()).getName();
            String var12 = var2[var2.length - 1].getName();
            String var11 = var2[var2.length - 1].getSuperclass().getName();
            if (!var1.equals(var12) && !var1.equals(var11)) {
               return null;
            }
         }

         return var9;
      }
   }

   public static Method getMatchingMethod(Class var0, String var1, Class... var2) {
      int var3 = 0;
      Validate.notNull(var0, "Null class not allowed.");
      Validate.notEmpty((CharSequence)var1, "Null or blank methodName not allowed.");
      Method[] var5 = var0.getDeclaredMethods();

      for(Iterator var8 = ClassUtils.getAllSuperclasses(var0).iterator(); var8.hasNext(); var5 = (Method[])ArrayUtils.addAll((Object[])var5, (Object[])((Class)var8.next()).getDeclaredMethods())) {
      }

      Method var6 = null;

      Method var9;
      for(int var4 = var5.length; var3 < var4; var6 = var9) {
         Method var7 = var5[var3];
         if (var1.equals(var7.getName()) && Objects.deepEquals(var2, var7.getParameterTypes())) {
            return var7;
         }

         var9 = var6;
         if (var1.equals(var7.getName())) {
            var9 = var6;
            if (ClassUtils.isAssignable(var2, var7.getParameterTypes(), true)) {
               if (var6 == null) {
                  var9 = var7;
               } else {
                  var9 = var6;
                  if (distance(var2, var7.getParameterTypes()) < distance(var2, var6.getParameterTypes())) {
                     var9 = var7;
                  }
               }
            }
         }

         ++var3;
      }

      return var6;
   }

   public static List getMethodsListWithAnnotation(Class var0, Class var1) {
      return getMethodsListWithAnnotation(var0, var1, false, false);
   }

   public static List getMethodsListWithAnnotation(Class var0, Class var1, boolean var2, boolean var3) {
      boolean var7 = true;
      boolean var6;
      if (var0 != null) {
         var6 = true;
      } else {
         var6 = false;
      }

      Validate.isTrue(var6, "The class must not be null");
      if (var1 != null) {
         var6 = var7;
      } else {
         var6 = false;
      }

      Validate.isTrue(var6, "The annotation class must not be null");
      Object var8;
      if (var2) {
         var8 = getAllSuperclassesAndInterfaces(var0);
      } else {
         var8 = new ArrayList();
      }

      ((List)var8).add(0, var0);
      ArrayList var9 = new ArrayList();
      Iterator var12 = ((List)var8).iterator();

      while(var12.hasNext()) {
         var0 = (Class)var12.next();
         Method[] var11;
         if (var3) {
            var11 = var0.getDeclaredMethods();
         } else {
            var11 = var0.getMethods();
         }

         int var5 = var11.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            Method var10 = var11[var4];
            if (var10.getAnnotation(var1) != null) {
               var9.add(var10);
            }
         }
      }

      return var9;
   }

   public static Method[] getMethodsWithAnnotation(Class var0, Class var1) {
      return getMethodsWithAnnotation(var0, var1, false, false);
   }

   public static Method[] getMethodsWithAnnotation(Class var0, Class var1, boolean var2, boolean var3) {
      List var4 = getMethodsListWithAnnotation(var0, var1, var2, var3);
      return (Method[])var4.toArray(new Method[var4.size()]);
   }

   public static Set getOverrideHierarchy(Method var0, ClassUtils.Interfaces var1) {
      Validate.notNull(var0);
      LinkedHashSet var3 = new LinkedHashSet();
      var3.add(var0);
      Class[] var4 = var0.getParameterTypes();
      Class var5 = var0.getDeclaringClass();
      Iterator var8 = ClassUtils.hierarchy(var5, var1).iterator();
      var8.next();

      while(true) {
         label32:
         while(true) {
            Method var6;
            do {
               if (!var8.hasNext()) {
                  return var3;
               }

               var6 = getMatchingAccessibleMethod((Class)var8.next(), var0.getName(), var4);
            } while(var6 == null);

            if (Arrays.equals(var6.getParameterTypes(), var4)) {
               var3.add(var6);
            } else {
               Map var7 = TypeUtils.getTypeArguments(var5, var6.getDeclaringClass());

               for(int var2 = 0; var2 < var4.length; ++var2) {
                  if (!TypeUtils.equals(TypeUtils.unrollVariables(var7, var0.getGenericParameterTypes()[var2]), TypeUtils.unrollVariables(var7, var6.getGenericParameterTypes()[var2]))) {
                     continue label32;
                  }
               }

               var3.add(var6);
            }
         }
      }
   }

   static Object[] getVarArgs(Object[] var0, Class[] var1) {
      if (var0.length == var1.length && var0[var0.length - 1].getClass().equals(var1[var1.length - 1])) {
         return var0;
      } else {
         Object[] var4 = new Object[var1.length];
         System.arraycopy(var0, 0, var4, 0, var1.length - 1);
         Class var5 = var1[var1.length - 1].getComponentType();
         int var2 = var0.length - var1.length + 1;
         Object var3 = Array.newInstance(ClassUtils.primitiveToWrapper(var5), var2);
         System.arraycopy(var0, var1.length - 1, var3, 0, var2);
         Object var6 = var3;
         if (var5.isPrimitive()) {
            var6 = ArrayUtils.toPrimitive(var3);
         }

         var4[var1.length - 1] = var6;
         return var4;
      }
   }

   public static Object invokeExactMethod(Object var0, String var1) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      return invokeExactMethod(var0, var1, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class[])null);
   }

   public static Object invokeExactMethod(Object var0, String var1, Object... var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      return invokeExactMethod(var0, var1, var2, ClassUtils.toClass(var2));
   }

   public static Object invokeExactMethod(Object var0, String var1, Object[] var2, Class[] var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      var3 = ArrayUtils.nullToEmpty(var3);
      Method var5 = getAccessibleMethod(var0.getClass(), var1, var3);
      if (var5 != null) {
         return var5.invoke(var0, var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("No such accessible method: ");
         var4.append(var1);
         var4.append("() on object: ");
         var4.append(var0.getClass().getName());
         throw new NoSuchMethodException(var4.toString());
      }
   }

   public static Object invokeExactStaticMethod(Class var0, String var1, Object... var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      return invokeExactStaticMethod(var0, var1, var2, ClassUtils.toClass(var2));
   }

   public static Object invokeExactStaticMethod(Class var0, String var1, Object[] var2, Class[] var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      Method var5 = getAccessibleMethod(var0, var1, ArrayUtils.nullToEmpty(var3));
      if (var5 != null) {
         return var5.invoke((Object)null, var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("No such accessible method: ");
         var4.append(var1);
         var4.append("() on class: ");
         var4.append(var0.getName());
         throw new NoSuchMethodException(var4.toString());
      }
   }

   public static Object invokeMethod(Object var0, String var1) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      return invokeMethod(var0, var1, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class[])null);
   }

   public static Object invokeMethod(Object var0, String var1, Object... var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      return invokeMethod(var0, var1, var2, ClassUtils.toClass(var2));
   }

   public static Object invokeMethod(Object var0, String var1, Object[] var2, Class[] var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      return invokeMethod(var0, false, var1, var2, var3);
   }

   public static Object invokeMethod(Object var0, boolean var1, String var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      return invokeMethod(var0, var1, var2, ArrayUtils.EMPTY_OBJECT_ARRAY, (Class[])null);
   }

   public static Object invokeMethod(Object var0, boolean var1, String var2, Object... var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var3 = ArrayUtils.nullToEmpty(var3);
      return invokeMethod(var0, var1, var2, var3, ClassUtils.toClass(var3));
   }

   public static Object invokeMethod(Object var0, boolean var1, String var2, Object[] var3, Class[] var4) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var4 = ArrayUtils.nullToEmpty(var4);
      Object[] var7 = ArrayUtils.nullToEmpty(var3);
      String var8;
      Method var9;
      if (var1) {
         String var5 = "No such method: ";
         Method var6 = getMatchingMethod(var0.getClass(), var2, var4);
         var9 = var6;
         var8 = var5;
         if (var6 != null) {
            var9 = var6;
            var8 = var5;
            if (!var6.isAccessible()) {
               var6.setAccessible(true);
               var9 = var6;
               var8 = var5;
            }
         }
      } else {
         var8 = "No such accessible method: ";
         var9 = getMatchingAccessibleMethod(var0.getClass(), var2, var4);
      }

      if (var9 != null) {
         return var9.invoke(var0, toVarArgs(var9, var7));
      } else {
         StringBuilder var10 = new StringBuilder();
         var10.append(var8);
         var10.append(var2);
         var10.append("() on object: ");
         var10.append(var0.getClass().getName());
         throw new NoSuchMethodException(var10.toString());
      }
   }

   public static Object invokeStaticMethod(Class var0, String var1, Object... var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      return invokeStaticMethod(var0, var1, var2, ClassUtils.toClass(var2));
   }

   public static Object invokeStaticMethod(Class var0, String var1, Object[] var2, Class[] var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      Method var5 = getMatchingAccessibleMethod(var0, var1, ArrayUtils.nullToEmpty(var3));
      if (var5 != null) {
         return var5.invoke((Object)null, toVarArgs(var5, var2));
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("No such accessible method: ");
         var4.append(var1);
         var4.append("() on class: ");
         var4.append(var0.getName());
         throw new NoSuchMethodException(var4.toString());
      }
   }

   private static Object[] toVarArgs(Method var0, Object[] var1) {
      Object[] var2 = var1;
      if (var0.isVarArgs()) {
         var2 = getVarArgs(var1, var0.getParameterTypes());
      }

      return var2;
   }
}
