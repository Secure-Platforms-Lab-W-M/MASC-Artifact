package org.apache.commons.lang3;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AnnotationUtils {
   private static final ToStringStyle TO_STRING_STYLE = new ToStringStyle() {
      private static final long serialVersionUID = 1L;

      {
         this.setDefaultFullDetail(true);
         this.setArrayContentDetail(true);
         this.setUseClassName(true);
         this.setUseShortClassName(true);
         this.setUseIdentityHashCode(false);
         this.setContentStart("(");
         this.setContentEnd(")");
         this.setFieldSeparator(", ");
         this.setArrayStart("[");
         this.setArrayEnd("]");
      }

      protected void appendDetail(StringBuffer var1, String var2, Object var3) {
         Object var4 = var3;
         if (var3 instanceof Annotation) {
            var4 = AnnotationUtils.toString((Annotation)var3);
         }

         super.appendDetail(var1, var2, var4);
      }

      protected String getShortClassName(Class var1) {
         Object var2 = null;
         Iterator var3 = ClassUtils.getAllInterfaces(var1).iterator();

         do {
            var1 = (Class)var2;
            if (!var3.hasNext()) {
               break;
            }

            var1 = (Class)var3.next();
         } while(!Annotation.class.isAssignableFrom(var1));

         String var4;
         if (var1 == null) {
            var4 = "";
         } else {
            var4 = var1.getName();
         }

         return (new StringBuilder(var4)).insert(0, '@').toString();
      }
   };

   private static boolean annotationArrayMemberEquals(Annotation[] var0, Annotation[] var1) {
      if (var0.length != var1.length) {
         return false;
      } else {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (!equals(var0[var2], var1[var2])) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean arrayMemberEquals(Class var0, Object var1, Object var2) {
      if (var0.isAnnotation()) {
         return annotationArrayMemberEquals((Annotation[])var1, (Annotation[])var2);
      } else if (var0.equals(Byte.TYPE)) {
         return Arrays.equals((byte[])var1, (byte[])var2);
      } else if (var0.equals(Short.TYPE)) {
         return Arrays.equals((short[])var1, (short[])var2);
      } else if (var0.equals(Integer.TYPE)) {
         return Arrays.equals((int[])var1, (int[])var2);
      } else if (var0.equals(Character.TYPE)) {
         return Arrays.equals((char[])var1, (char[])var2);
      } else if (var0.equals(Long.TYPE)) {
         return Arrays.equals((long[])var1, (long[])var2);
      } else if (var0.equals(Float.TYPE)) {
         return Arrays.equals((float[])var1, (float[])var2);
      } else if (var0.equals(Double.TYPE)) {
         return Arrays.equals((double[])var1, (double[])var2);
      } else {
         return var0.equals(Boolean.TYPE) ? Arrays.equals((boolean[])var1, (boolean[])var2) : Arrays.equals((Object[])var1, (Object[])var2);
      }
   }

   private static int arrayMemberHash(Class var0, Object var1) {
      if (var0.equals(Byte.TYPE)) {
         return Arrays.hashCode((byte[])var1);
      } else if (var0.equals(Short.TYPE)) {
         return Arrays.hashCode((short[])var1);
      } else if (var0.equals(Integer.TYPE)) {
         return Arrays.hashCode((int[])var1);
      } else if (var0.equals(Character.TYPE)) {
         return Arrays.hashCode((char[])var1);
      } else if (var0.equals(Long.TYPE)) {
         return Arrays.hashCode((long[])var1);
      } else if (var0.equals(Float.TYPE)) {
         return Arrays.hashCode((float[])var1);
      } else if (var0.equals(Double.TYPE)) {
         return Arrays.hashCode((double[])var1);
      } else {
         return var0.equals(Boolean.TYPE) ? Arrays.hashCode((boolean[])var1) : Arrays.hashCode((Object[])var1);
      }
   }

   public static boolean equals(Annotation var0, Annotation var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null) {
         if (var1 == null) {
            return false;
         } else {
            Class var5 = var0.annotationType();
            Class var6 = var1.annotationType();
            Validate.notNull(var5, "Annotation %s with null annotationType()", var0);
            Validate.notNull(var6, "Annotation %s with null annotationType()", var1);
            if (!var5.equals(var6)) {
               return false;
            } else {
               boolean var10001;
               int var3;
               Method[] var13;
               try {
                  var13 = var5.getDeclaredMethods();
                  var3 = var13.length;
               } catch (IllegalAccessException var11) {
                  var10001 = false;
                  return false;
               } catch (InvocationTargetException var12) {
                  var10001 = false;
                  return false;
               }

               for(int var2 = 0; var2 < var3; ++var2) {
                  Method var14 = var13[var2];

                  boolean var4;
                  try {
                     if (var14.getParameterTypes().length != 0 || !isValidAnnotationMemberType(var14.getReturnType())) {
                        continue;
                     }

                     Object var7 = var14.invoke(var0);
                     Object var8 = var14.invoke(var1);
                     var4 = memberEquals(var14.getReturnType(), var7, var8);
                  } catch (IllegalAccessException var9) {
                     var10001 = false;
                     return false;
                  } catch (InvocationTargetException var10) {
                     var10001 = false;
                     return false;
                  }

                  if (!var4) {
                     return false;
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   public static int hashCode(Annotation var0) {
      int var2 = 0;
      Method[] var4 = var0.annotationType().getDeclaredMethods();
      int var3 = var4.length;
      int var1 = 0;

      while(true) {
         if (var1 < var3) {
            Method var5 = var4[var1];

            RuntimeException var15;
            label40: {
               Exception var10000;
               label39: {
                  Object var6;
                  boolean var10001;
                  try {
                     var6 = var5.invoke(var0);
                  } catch (RuntimeException var11) {
                     var15 = var11;
                     var10001 = false;
                     break label40;
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label39;
                  }

                  if (var6 != null) {
                     label33: {
                        try {
                           var2 += hashMember(var5.getName(), var6);
                        } catch (RuntimeException var7) {
                           var15 = var7;
                           var10001 = false;
                           break label40;
                        } catch (Exception var8) {
                           var10000 = var8;
                           var10001 = false;
                           break label33;
                        }

                        ++var1;
                        continue;
                     }
                  } else {
                     try {
                        throw new IllegalStateException(String.format("Annotation method %s returned null", var5));
                     } catch (RuntimeException var9) {
                        var15 = var9;
                        var10001 = false;
                        break label40;
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                     }
                  }
               }

               Exception var13 = var10000;
               throw new RuntimeException(var13);
            }

            RuntimeException var14 = var15;
            throw var14;
         }

         return var2;
      }
   }

   private static int hashMember(String var0, Object var1) {
      int var2 = var0.hashCode() * 127;
      if (var1.getClass().isArray()) {
         return arrayMemberHash(var1.getClass().getComponentType(), var1) ^ var2;
      } else {
         return var1 instanceof Annotation ? hashCode((Annotation)var1) ^ var2 : var1.hashCode() ^ var2;
      }
   }

   public static boolean isValidAnnotationMemberType(Class var0) {
      boolean var1 = false;
      if (var0 == null) {
         return false;
      } else {
         Class var2 = var0;
         if (var0.isArray()) {
            var2 = var0.getComponentType();
         }

         if (var2.isPrimitive() || var2.isEnum() || var2.isAnnotation() || String.class.equals(var2) || Class.class.equals(var2)) {
            var1 = true;
         }

         return var1;
      }
   }

   private static boolean memberEquals(Class var0, Object var1, Object var2) {
      if (var1 == var2) {
         return true;
      } else if (var1 != null && var2 != null) {
         if (var0.isArray()) {
            return arrayMemberEquals(var0.getComponentType(), var1, var2);
         } else {
            return var0.isAnnotation() ? equals((Annotation)var1, (Annotation)var2) : var1.equals(var2);
         }
      } else {
         return false;
      }
   }

   public static String toString(Annotation var0) {
      ToStringBuilder var3 = new ToStringBuilder(var0, TO_STRING_STYLE);
      Method[] var4 = var0.annotationType().getDeclaredMethods();
      int var2 = var4.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         Method var5 = var4[var1];
         if (var5.getParameterTypes().length <= 0) {
            try {
               var3.append(var5.getName(), var5.invoke(var0));
            } catch (RuntimeException var6) {
               throw var6;
            } catch (Exception var7) {
               throw new RuntimeException(var7);
            }
         }
      }

      return var3.build();
   }
}
