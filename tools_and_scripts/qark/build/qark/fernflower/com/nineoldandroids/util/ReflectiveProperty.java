package com.nineoldandroids.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectiveProperty extends Property {
   private static final String PREFIX_GET = "get";
   private static final String PREFIX_IS = "is";
   private static final String PREFIX_SET = "set";
   private Field mField;
   private Method mGetter;
   private Method mSetter;

   public ReflectiveProperty(Class var1, Class var2, String var3) {
      super(var2, var3);
      char var4 = Character.toUpperCase(var3.charAt(0));
      String var5 = var3.substring(1);
      StringBuilder var6 = new StringBuilder();
      var6.append(var4);
      var6.append(var5);
      var5 = var6.toString();
      var6 = new StringBuilder();
      var6.append("get");
      var6.append(var5);
      String var21 = var6.toString();

      StringBuilder var14;
      try {
         this.mGetter = var1.getMethod(var21, (Class[])null);
      } catch (NoSuchMethodException var13) {
         Method var22;
         try {
            var22 = var1.getDeclaredMethod(var21, (Class[])null);
            this.mGetter = var22;
            var22.setAccessible(true);
         } catch (NoSuchMethodException var12) {
            var6 = new StringBuilder();
            var6.append("is");
            var6.append(var5);
            var21 = var6.toString();

            try {
               this.mGetter = var1.getMethod(var21, (Class[])null);
            } catch (NoSuchMethodException var11) {
               try {
                  var22 = var1.getDeclaredMethod(var21, (Class[])null);
                  this.mGetter = var22;
                  var22.setAccessible(true);
               } catch (NoSuchMethodException var10) {
                  try {
                     Field var15 = var1.getField(var3);
                     this.mField = var15;
                     var1 = var15.getType();
                     if (this.typesMatch(var2, var1)) {
                        return;
                     }

                     StringBuilder var20 = new StringBuilder();
                     var20.append("Underlying type (");
                     var20.append(var1);
                     var20.append(") ");
                     var20.append("does not match Property type (");
                     var20.append(var2);
                     var20.append(")");
                     throw new NoSuchPropertyException(var20.toString());
                  } catch (NoSuchFieldException var8) {
                     var14 = new StringBuilder();
                     var14.append("No accessor method or field found for property with name ");
                     var14.append(var3);
                     throw new NoSuchPropertyException(var14.toString());
                  }
               }
            }
         }
      }

      Class var19 = this.mGetter.getReturnType();
      if (this.typesMatch(var2, var19)) {
         StringBuilder var16 = new StringBuilder();
         var16.append("set");
         var16.append(var5);
         String var17 = var16.toString();

         try {
            Method var18 = var1.getDeclaredMethod(var17, var19);
            this.mSetter = var18;
            var18.setAccessible(true);
         } catch (NoSuchMethodException var9) {
         }
      } else {
         var14 = new StringBuilder();
         var14.append("Underlying type (");
         var14.append(var19);
         var14.append(") ");
         var14.append("does not match Property type (");
         var14.append(var2);
         var14.append(")");
         throw new NoSuchPropertyException(var14.toString());
      }
   }

   private boolean typesMatch(Class var1, Class var2) {
      boolean var4 = true;
      if (var2 != var1) {
         if (!var2.isPrimitive()) {
            return false;
         } else {
            boolean var3;
            if (var2 == Float.TYPE) {
               var3 = var4;
               if (var1 == Float.class) {
                  return var3;
               }
            }

            if (var2 == Integer.TYPE) {
               var3 = var4;
               if (var1 == Integer.class) {
                  return var3;
               }
            }

            if (var2 == Boolean.TYPE) {
               var3 = var4;
               if (var1 == Boolean.class) {
                  return var3;
               }
            }

            if (var2 == Long.TYPE) {
               var3 = var4;
               if (var1 == Long.class) {
                  return var3;
               }
            }

            if (var2 == Double.TYPE) {
               var3 = var4;
               if (var1 == Double.class) {
                  return var3;
               }
            }

            if (var2 == Short.TYPE) {
               var3 = var4;
               if (var1 == Short.class) {
                  return var3;
               }
            }

            if (var2 == Byte.TYPE) {
               var3 = var4;
               if (var1 == Byte.class) {
                  return var3;
               }
            }

            if (var2 == Character.TYPE && var1 == Character.class) {
               return true;
            } else {
               var3 = false;
               return var3;
            }
         }
      } else {
         return true;
      }
   }

   public Object get(Object var1) {
      Method var2 = this.mGetter;
      if (var2 != null) {
         try {
            var1 = var2.invoke(var1, (Object[])null);
            return var1;
         } catch (IllegalAccessException var3) {
            throw new AssertionError();
         } catch (InvocationTargetException var4) {
            throw new RuntimeException(var4.getCause());
         }
      } else {
         Field var6 = this.mField;
         if (var6 != null) {
            try {
               var1 = var6.get(var1);
               return var1;
            } catch (IllegalAccessException var5) {
               throw new AssertionError();
            }
         } else {
            throw new AssertionError();
         }
      }
   }

   public boolean isReadOnly() {
      return this.mSetter == null && this.mField == null;
   }

   public void set(Object var1, Object var2) {
      Method var3 = this.mSetter;
      if (var3 != null) {
         try {
            var3.invoke(var1, var2);
         } catch (IllegalAccessException var4) {
            throw new AssertionError();
         } catch (InvocationTargetException var5) {
            throw new RuntimeException(var5.getCause());
         }
      } else {
         Field var8 = this.mField;
         if (var8 != null) {
            try {
               var8.set(var1, var2);
            } catch (IllegalAccessException var6) {
               throw new AssertionError();
            }
         } else {
            StringBuilder var7 = new StringBuilder();
            var7.append("Property ");
            var7.append(this.getName());
            var7.append(" is read-only");
            throw new UnsupportedOperationException(var7.toString());
         }
      }
   }
}
