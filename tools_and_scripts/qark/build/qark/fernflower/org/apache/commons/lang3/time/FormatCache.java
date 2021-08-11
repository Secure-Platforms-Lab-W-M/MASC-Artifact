package org.apache.commons.lang3.time;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.Validate;

abstract class FormatCache {
   static final int NONE = -1;
   private static final ConcurrentMap cDateTimeInstanceCache = new ConcurrentHashMap(7);
   private final ConcurrentMap cInstanceCache = new ConcurrentHashMap(7);

   private Format getDateTimeInstance(Integer var1, Integer var2, TimeZone var3, Locale var4) {
      Locale var5 = var4;
      if (var4 == null) {
         var5 = Locale.getDefault();
      }

      return this.getInstance(getPatternForStyle(var1, var2, var5), var3, var5);
   }

   static String getPatternForStyle(Integer var0, Integer var1, Locale var2) {
      FormatCache.MultipartKey var3 = new FormatCache.MultipartKey(new Object[]{var0, var1, var2});
      String var4 = (String)cDateTimeInstanceCache.get(var3);
      if (var4 != null) {
         return var4;
      } else {
         String var11;
         String var12;
         label43: {
            label42: {
               boolean var10001;
               DateFormat var9;
               if (var0 == null) {
                  try {
                     var9 = DateFormat.getTimeInstance(var1, var2);
                  } catch (ClassCastException var8) {
                     var10001 = false;
                     break label42;
                  }
               } else if (var1 == null) {
                  try {
                     var9 = DateFormat.getDateInstance(var0, var2);
                  } catch (ClassCastException var7) {
                     var10001 = false;
                     break label42;
                  }
               } else {
                  try {
                     var9 = DateFormat.getDateTimeInstance(var0, var1, var2);
                  } catch (ClassCastException var6) {
                     var10001 = false;
                     break label42;
                  }
               }

               try {
                  var12 = ((SimpleDateFormat)var9).toPattern();
                  var11 = (String)cDateTimeInstanceCache.putIfAbsent(var3, var12);
                  break label43;
               } catch (ClassCastException var5) {
                  var10001 = false;
               }
            }

            StringBuilder var10 = new StringBuilder();
            var10.append("No date time pattern for locale: ");
            var10.append(var2);
            throw new IllegalArgumentException(var10.toString());
         }

         if (var11 != null) {
            var12 = var11;
         }

         return var12;
      }
   }

   protected abstract Format createInstance(String var1, TimeZone var2, Locale var3);

   Format getDateInstance(int var1, TimeZone var2, Locale var3) {
      return this.getDateTimeInstance(var1, (Integer)null, var2, var3);
   }

   Format getDateTimeInstance(int var1, int var2, TimeZone var3, Locale var4) {
      return this.getDateTimeInstance(var1, var2, var3, var4);
   }

   public Format getInstance() {
      return this.getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
   }

   public Format getInstance(String var1, TimeZone var2, Locale var3) {
      Validate.notNull(var1, "pattern must not be null");
      TimeZone var4 = var2;
      if (var2 == null) {
         var4 = TimeZone.getDefault();
      }

      Locale var5 = var3;
      if (var3 == null) {
         var5 = Locale.getDefault();
      }

      FormatCache.MultipartKey var6 = new FormatCache.MultipartKey(new Object[]{var1, var4, var5});
      Format var9 = (Format)this.cInstanceCache.get(var6);
      Format var8 = var9;
      if (var9 == null) {
         var8 = this.createInstance(var1, var4, var5);
         Format var7 = (Format)this.cInstanceCache.putIfAbsent(var6, var8);
         if (var7 != null) {
            var8 = var7;
         }
      }

      return var8;
   }

   Format getTimeInstance(int var1, TimeZone var2, Locale var3) {
      return this.getDateTimeInstance((Integer)null, var1, var2, var3);
   }

   private static class MultipartKey {
      private int hashCode;
      private final Object[] keys;

      MultipartKey(Object... var1) {
         this.keys = var1;
      }

      public boolean equals(Object var1) {
         return Arrays.equals(this.keys, ((FormatCache.MultipartKey)var1).keys);
      }

      public int hashCode() {
         if (this.hashCode == 0) {
            int var2 = 0;
            Object[] var5 = this.keys;
            int var4 = var5.length;

            int var3;
            for(int var1 = 0; var1 < var4; var2 = var3) {
               Object var6 = var5[var1];
               var3 = var2;
               if (var6 != null) {
                  var3 = var2 * 7 + var6.hashCode();
               }

               ++var1;
            }

            this.hashCode = var2;
         }

         return this.hashCode;
      }
   }
}
