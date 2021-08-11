package org.apache.commons.lang3.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;

public class FieldUtils {
   public static Field[] getAllFields(Class var0) {
      List var1 = getAllFieldsList(var0);
      return (Field[])var1.toArray(new Field[var1.size()]);
   }

   public static List getAllFieldsList(Class var0) {
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "The class must not be null");

      ArrayList var2;
      for(var2 = new ArrayList(); var0 != null; var0 = var0.getSuperclass()) {
         Collections.addAll(var2, var0.getDeclaredFields());
      }

      return var2;
   }

   public static Field getDeclaredField(Class var0, String var1) {
      return getDeclaredField(var0, var1, false);
   }

   public static Field getDeclaredField(Class var0, String var1, boolean var2) {
      boolean var3;
      if (var0 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The class must not be null");
      Validate.isTrue(StringUtils.isNotBlank(var1), "The field name must not be blank/empty");

      boolean var10001;
      Field var6;
      try {
         var6 = var0.getDeclaredField(var1);
         if (MemberUtils.isAccessible(var6)) {
            return var6;
         }
      } catch (NoSuchFieldException var5) {
         var10001 = false;
         return null;
      }

      if (!var2) {
         return null;
      } else {
         try {
            var6.setAccessible(true);
            return var6;
         } catch (NoSuchFieldException var4) {
            var10001 = false;
            return null;
         }
      }
   }

   public static Field getField(Class var0, String var1) {
      Field var2 = getField(var0, var1, false);
      MemberUtils.setAccessibleWorkaround(var2);
      return var2;
   }

   public static Field getField(Class var0, String var1, boolean var2) {
      boolean var3;
      if (var0 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The class must not be null");
      Validate.isTrue(StringUtils.isNotBlank(var1), "The field name must not be blank/empty");

      boolean var10001;
      Field var5;
      for(Class var4 = var0; var4 != null; var4 = var4.getSuperclass()) {
         try {
            var5 = var4.getDeclaredField(var1);
            if (Modifier.isPublic(var5.getModifiers())) {
               return var5;
            }
         } catch (NoSuchFieldException var10) {
            var10001 = false;
            continue;
         }

         if (var2) {
            try {
               var5.setAccessible(true);
               return var5;
            } catch (NoSuchFieldException var9) {
               var10001 = false;
            }
         }
      }

      Field var11 = null;
      Iterator var6 = ClassUtils.getAllInterfaces(var0).iterator();

      while(var6.hasNext()) {
         Class var12 = (Class)var6.next();

         try {
            var5 = var12.getField(var1);
         } catch (NoSuchFieldException var8) {
            var10001 = false;
            continue;
         }

         if (var11 == null) {
            var2 = true;
         } else {
            var2 = false;
         }

         try {
            Validate.isTrue(var2, "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.", var1, var0);
         } catch (NoSuchFieldException var7) {
            var10001 = false;
            continue;
         }

         var11 = var5;
      }

      return var11;
   }

   public static List getFieldsListWithAnnotation(Class var0, Class var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "The annotation class must not be null");
      List var3 = getAllFieldsList(var0);
      ArrayList var5 = new ArrayList();
      Iterator var6 = var3.iterator();

      while(var6.hasNext()) {
         Field var4 = (Field)var6.next();
         if (var4.getAnnotation(var1) != null) {
            var5.add(var4);
         }
      }

      return var5;
   }

   public static Field[] getFieldsWithAnnotation(Class var0, Class var1) {
      List var2 = getFieldsListWithAnnotation(var0, var1);
      return (Field[])var2.toArray(new Field[var2.size()]);
   }

   public static Object readDeclaredField(Object var0, String var1) throws IllegalAccessException {
      return readDeclaredField(var0, var1, false);
   }

   public static Object readDeclaredField(Object var0, String var1, boolean var2) throws IllegalAccessException {
      boolean var3;
      if (var0 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "target object must not be null");
      Class var4 = var0.getClass();
      Field var5 = getDeclaredField(var4, var1, var2);
      if (var5 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Cannot locate declared field %s.%s", var4, var1);
      return readField(var5, var0, false);
   }

   public static Object readDeclaredStaticField(Class var0, String var1) throws IllegalAccessException {
      return readDeclaredStaticField(var0, var1, false);
   }

   public static Object readDeclaredStaticField(Class var0, String var1, boolean var2) throws IllegalAccessException {
      Field var3 = getDeclaredField(var0, var1, var2);
      if (var3 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Cannot locate declared field %s.%s", var0.getName(), var1);
      return readStaticField(var3, false);
   }

   public static Object readField(Object var0, String var1) throws IllegalAccessException {
      return readField(var0, var1, false);
   }

   public static Object readField(Object var0, String var1, boolean var2) throws IllegalAccessException {
      boolean var3;
      if (var0 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "target object must not be null");
      Class var4 = var0.getClass();
      Field var5 = getField(var4, var1, var2);
      if (var5 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Cannot locate field %s on %s", var1, var4);
      return readField(var5, var0, false);
   }

   public static Object readField(Field var0, Object var1) throws IllegalAccessException {
      return readField(var0, var1, false);
   }

   public static Object readField(Field var0, Object var1, boolean var2) throws IllegalAccessException {
      boolean var3;
      if (var0 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The field must not be null");
      if (var2 && !var0.isAccessible()) {
         var0.setAccessible(true);
      } else {
         MemberUtils.setAccessibleWorkaround(var0);
      }

      return var0.get(var1);
   }

   public static Object readStaticField(Class var0, String var1) throws IllegalAccessException {
      return readStaticField(var0, var1, false);
   }

   public static Object readStaticField(Class var0, String var1, boolean var2) throws IllegalAccessException {
      Field var3 = getField(var0, var1, var2);
      if (var3 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Cannot locate field '%s' on %s", var1, var0);
      return readStaticField(var3, false);
   }

   public static Object readStaticField(Field var0) throws IllegalAccessException {
      return readStaticField(var0, false);
   }

   public static Object readStaticField(Field var0, boolean var1) throws IllegalAccessException {
      boolean var2;
      if (var0 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "The field must not be null");
      Validate.isTrue(Modifier.isStatic(var0.getModifiers()), "The field '%s' is not static", var0.getName());
      return readField((Field)var0, (Object)null, var1);
   }

   public static void removeFinalModifier(Field var0) {
      removeFinalModifier(var0, true);
   }

   @Deprecated
   public static void removeFinalModifier(Field var0, boolean var1) {
      boolean var3;
      if (var0 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The field must not be null");

      Object var29;
      label172: {
         NoSuchFieldException var30;
         label171: {
            IllegalAccessException var10000;
            label178: {
               Field var4;
               boolean var10001;
               try {
                  if (!Modifier.isFinal(var0.getModifiers())) {
                     return;
                  }

                  var4 = Field.class.getDeclaredField("modifiers");
               } catch (NoSuchFieldException var27) {
                  var30 = var27;
                  var10001 = false;
                  break label171;
               } catch (IllegalAccessException var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label178;
               }

               boolean var2;
               label166: {
                  label165: {
                     if (var1) {
                        try {
                           if (!var4.isAccessible()) {
                              break label165;
                           }
                        } catch (NoSuchFieldException var25) {
                           var30 = var25;
                           var10001 = false;
                           break label171;
                        } catch (IllegalAccessException var26) {
                           var10000 = var26;
                           var10001 = false;
                           break label178;
                        }
                     }

                     var2 = false;
                     break label166;
                  }

                  var2 = true;
               }

               if (var2) {
                  try {
                     var4.setAccessible(true);
                  } catch (NoSuchFieldException var23) {
                     var30 = var23;
                     var10001 = false;
                     break label171;
                  } catch (IllegalAccessException var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label178;
                  }
               }

               try {
                  var4.setInt(var0, var0.getModifiers() & -17);
                  return;
               } finally {
                  label152: {
                     if (var2) {
                        try {
                           var4.setAccessible(false);
                        } catch (NoSuchFieldException var20) {
                           var30 = var20;
                           var10001 = false;
                           break label171;
                        } catch (IllegalAccessException var21) {
                           var10000 = var21;
                           var10001 = false;
                           break label152;
                        }
                     }

                     label148:
                     try {
                        ;
                     } catch (NoSuchFieldException var18) {
                        var30 = var18;
                        var10001 = false;
                        break label171;
                     } catch (IllegalAccessException var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label148;
                     }
                  }
               }
            }

            var29 = var10000;
            break label172;
         }

         var29 = var30;
      }

      if (SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_12)) {
         throw new UnsupportedOperationException("In java 12+ final cannot be removed.", (Throwable)var29);
      }
   }

   public static void writeDeclaredField(Object var0, String var1, Object var2) throws IllegalAccessException {
      writeDeclaredField(var0, var1, var2, false);
   }

   public static void writeDeclaredField(Object var0, String var1, Object var2, boolean var3) throws IllegalAccessException {
      boolean var4;
      if (var0 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "target object must not be null");
      Class var5 = var0.getClass();
      Field var6 = getDeclaredField(var5, var1, var3);
      if (var6 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Cannot locate declared field %s.%s", var5.getName(), var1);
      writeField(var6, var0, var2, false);
   }

   public static void writeDeclaredStaticField(Class var0, String var1, Object var2) throws IllegalAccessException {
      writeDeclaredStaticField(var0, var1, var2, false);
   }

   public static void writeDeclaredStaticField(Class var0, String var1, Object var2, boolean var3) throws IllegalAccessException {
      Field var4 = getDeclaredField(var0, var1, var3);
      if (var4 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Cannot locate declared field %s.%s", var0.getName(), var1);
      writeField((Field)var4, (Object)null, var2, false);
   }

   public static void writeField(Object var0, String var1, Object var2) throws IllegalAccessException {
      writeField(var0, var1, var2, false);
   }

   public static void writeField(Object var0, String var1, Object var2, boolean var3) throws IllegalAccessException {
      boolean var4;
      if (var0 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "target object must not be null");
      Class var5 = var0.getClass();
      Field var6 = getField(var5, var1, var3);
      if (var6 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Cannot locate declared field %s.%s", var5.getName(), var1);
      writeField(var6, var0, var2, false);
   }

   public static void writeField(Field var0, Object var1, Object var2) throws IllegalAccessException {
      writeField(var0, var1, var2, false);
   }

   public static void writeField(Field var0, Object var1, Object var2, boolean var3) throws IllegalAccessException {
      boolean var4;
      if (var0 != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      Validate.isTrue(var4, "The field must not be null");
      if (var3 && !var0.isAccessible()) {
         var0.setAccessible(true);
      } else {
         MemberUtils.setAccessibleWorkaround(var0);
      }

      var0.set(var1, var2);
   }

   public static void writeStaticField(Class var0, String var1, Object var2) throws IllegalAccessException {
      writeStaticField(var0, var1, var2, false);
   }

   public static void writeStaticField(Class var0, String var1, Object var2, boolean var3) throws IllegalAccessException {
      Field var4 = getField(var0, var1, var3);
      if (var4 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Cannot locate field %s on %s", var1, var0);
      writeStaticField(var4, var2, false);
   }

   public static void writeStaticField(Field var0, Object var1) throws IllegalAccessException {
      writeStaticField(var0, var1, false);
   }

   public static void writeStaticField(Field var0, Object var1, boolean var2) throws IllegalAccessException {
      boolean var3;
      if (var0 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The field must not be null");
      Validate.isTrue(Modifier.isStatic(var0.getModifiers()), "The field %s.%s is not static", var0.getDeclaringClass().getName(), var0.getName());
      writeField((Field)var0, (Object)null, var1, var2);
   }
}
