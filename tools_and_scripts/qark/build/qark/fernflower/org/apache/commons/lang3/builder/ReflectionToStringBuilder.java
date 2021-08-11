package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

public class ReflectionToStringBuilder extends ToStringBuilder {
   private boolean appendStatics = false;
   private boolean appendTransients = false;
   protected String[] excludeFieldNames;
   private boolean excludeNullValues;
   private Class upToClass = null;

   public ReflectionToStringBuilder(Object var1) {
      super(checkNotNull(var1));
   }

   public ReflectionToStringBuilder(Object var1, ToStringStyle var2) {
      super(checkNotNull(var1), var2);
   }

   public ReflectionToStringBuilder(Object var1, ToStringStyle var2, StringBuffer var3) {
      super(checkNotNull(var1), var2, var3);
   }

   public ReflectionToStringBuilder(Object var1, ToStringStyle var2, StringBuffer var3, Class var4, boolean var5, boolean var6) {
      super(checkNotNull(var1), var2, var3);
      this.setUpToClass(var4);
      this.setAppendTransients(var5);
      this.setAppendStatics(var6);
   }

   public ReflectionToStringBuilder(Object var1, ToStringStyle var2, StringBuffer var3, Class var4, boolean var5, boolean var6, boolean var7) {
      super(checkNotNull(var1), var2, var3);
      this.setUpToClass(var4);
      this.setAppendTransients(var5);
      this.setAppendStatics(var6);
      this.setExcludeNullValues(var7);
   }

   private static Object checkNotNull(Object var0) {
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "The Object passed in should not be null.");
      return var0;
   }

   static String[] toNoNullStringArray(Collection var0) {
      return var0 == null ? ArrayUtils.EMPTY_STRING_ARRAY : toNoNullStringArray(var0.toArray());
   }

   static String[] toNoNullStringArray(Object[] var0) {
      ArrayList var3 = new ArrayList(var0.length);
      int var2 = var0.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         Object var4 = var0[var1];
         if (var4 != null) {
            var3.add(var4.toString());
         }
      }

      return (String[])var3.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
   }

   public static String toString(Object var0) {
      return toString(var0, (ToStringStyle)null, false, false, (Class)null);
   }

   public static String toString(Object var0, ToStringStyle var1) {
      return toString(var0, var1, false, false, (Class)null);
   }

   public static String toString(Object var0, ToStringStyle var1, boolean var2) {
      return toString(var0, var1, var2, false, (Class)null);
   }

   public static String toString(Object var0, ToStringStyle var1, boolean var2, boolean var3) {
      return toString(var0, var1, var2, var3, (Class)null);
   }

   public static String toString(Object var0, ToStringStyle var1, boolean var2, boolean var3, Class var4) {
      return (new ReflectionToStringBuilder(var0, var1, (StringBuffer)null, var4, var2, var3)).toString();
   }

   public static String toString(Object var0, ToStringStyle var1, boolean var2, boolean var3, boolean var4, Class var5) {
      return (new ReflectionToStringBuilder(var0, var1, (StringBuffer)null, var5, var2, var3, var4)).toString();
   }

   public static String toStringExclude(Object var0, Collection var1) {
      return toStringExclude(var0, toNoNullStringArray(var1));
   }

   public static String toStringExclude(Object var0, String... var1) {
      return (new ReflectionToStringBuilder(var0)).setExcludeFieldNames(var1).toString();
   }

   protected boolean accept(Field var1) {
      if (var1.getName().indexOf(36) != -1) {
         return false;
      } else if (Modifier.isTransient(var1.getModifiers()) && !this.isAppendTransients()) {
         return false;
      } else if (Modifier.isStatic(var1.getModifiers()) && !this.isAppendStatics()) {
         return false;
      } else {
         String[] var2 = this.excludeFieldNames;
         return var2 != null && Arrays.binarySearch(var2, var1.getName()) >= 0 ? false : var1.isAnnotationPresent(ToStringExclude.class) ^ true;
      }
   }

   protected void appendFieldsIn(Class var1) {
      if (var1.isArray()) {
         this.reflectionAppendArray(this.getObject());
      } else {
         Field[] var11 = var1.getDeclaredFields();
         AccessibleObject.setAccessible(var11, true);
         int var3 = var11.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            Field var5 = var11[var2];
            String var6 = var5.getName();
            if (this.accept(var5)) {
               IllegalAccessException var10000;
               label57: {
                  Object var7;
                  boolean var10001;
                  label48: {
                     try {
                        var7 = this.getValue(var5);
                        if (!this.excludeNullValues) {
                           break label48;
                        }
                     } catch (IllegalAccessException var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label57;
                     }

                     if (var7 == null) {
                        continue;
                     }
                  }

                  boolean var4;
                  label42: {
                     label41: {
                        try {
                           if (!var5.isAnnotationPresent(ToStringSummary.class)) {
                              break label41;
                           }
                        } catch (IllegalAccessException var9) {
                           var10000 = var9;
                           var10001 = false;
                           break label57;
                        }

                        var4 = false;
                        break label42;
                     }

                     var4 = true;
                  }

                  try {
                     this.append(var6, var7, var4);
                     continue;
                  } catch (IllegalAccessException var8) {
                     var10000 = var8;
                     var10001 = false;
                  }
               }

               IllegalAccessException var12 = var10000;
               StringBuilder var13 = new StringBuilder();
               var13.append("Unexpected IllegalAccessException: ");
               var13.append(var12.getMessage());
               throw new InternalError(var13.toString());
            }
         }

      }
   }

   public String[] getExcludeFieldNames() {
      return (String[])this.excludeFieldNames.clone();
   }

   public Class getUpToClass() {
      return this.upToClass;
   }

   protected Object getValue(Field var1) throws IllegalAccessException {
      return var1.get(this.getObject());
   }

   public boolean isAppendStatics() {
      return this.appendStatics;
   }

   public boolean isAppendTransients() {
      return this.appendTransients;
   }

   public boolean isExcludeNullValues() {
      return this.excludeNullValues;
   }

   public ReflectionToStringBuilder reflectionAppendArray(Object var1) {
      this.getStyle().reflectionAppendArrayDetail(this.getStringBuffer(), (String)null, var1);
      return this;
   }

   public void setAppendStatics(boolean var1) {
      this.appendStatics = var1;
   }

   public void setAppendTransients(boolean var1) {
      this.appendTransients = var1;
   }

   public ReflectionToStringBuilder setExcludeFieldNames(String... var1) {
      if (var1 == null) {
         this.excludeFieldNames = null;
         return this;
      } else {
         var1 = toNoNullStringArray((Object[])var1);
         this.excludeFieldNames = var1;
         Arrays.sort(var1);
         return this;
      }
   }

   public void setExcludeNullValues(boolean var1) {
      this.excludeNullValues = var1;
   }

   public void setUpToClass(Class var1) {
      if (var1 != null) {
         Object var2 = this.getObject();
         if (var2 != null && !var1.isInstance(var2)) {
            throw new IllegalArgumentException("Specified class is not a superclass of the object");
         }
      }

      this.upToClass = var1;
   }

   public String toString() {
      if (this.getObject() == null) {
         return this.getStyle().getNullText();
      } else {
         Class var1 = this.getObject().getClass();
         this.appendFieldsIn(var1);

         while(var1.getSuperclass() != null && var1 != this.getUpToClass()) {
            var1 = var1.getSuperclass();
            this.appendFieldsIn(var1);
         }

         return super.toString();
      }
   }
}
