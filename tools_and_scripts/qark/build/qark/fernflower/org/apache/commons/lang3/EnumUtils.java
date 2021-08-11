package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumUtils {
   private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
   private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";
   private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
   private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";

   private static Class asEnum(Class var0) {
      Validate.notNull(var0, "EnumClass must be defined.");
      Validate.isTrue(var0.isEnum(), "%s does not seem to be an Enum type", var0);
      return var0;
   }

   private static Class checkBitVectorable(Class var0) {
      Enum[] var2 = (Enum[])asEnum(var0).getEnumConstants();
      boolean var1;
      if (var2.length <= 64) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "Cannot store %s %s values in %s bits", var2.length, var0.getSimpleName(), 64);
      return var0;
   }

   public static long generateBitVector(Class var0, Iterable var1) {
      checkBitVectorable(var0);
      Validate.notNull(var1);
      long var2 = 0L;

      Enum var6;
      for(Iterator var5 = var1.iterator(); var5.hasNext(); var2 |= 1L << var6.ordinal()) {
         var6 = (Enum)var5.next();
         boolean var4;
         if (var6 != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         Validate.isTrue(var4, "null elements not permitted");
      }

      return var2;
   }

   @SafeVarargs
   public static long generateBitVector(Class var0, Enum... var1) {
      Validate.noNullElements((Object[])var1);
      return generateBitVector(var0, (Iterable)Arrays.asList(var1));
   }

   public static long[] generateBitVectors(Class var0, Iterable var1) {
      asEnum(var0);
      Validate.notNull(var1);
      EnumSet var5 = EnumSet.noneOf(var0);
      Iterator var8 = var1.iterator();

      while(true) {
         boolean var4 = var8.hasNext();
         boolean var3 = true;
         if (!var4) {
            long[] var7 = new long[(((Enum[])var0.getEnumConstants()).length - 1) / 64 + 1];

            int var2;
            Enum var9;
            for(var8 = var5.iterator(); var8.hasNext(); var7[var2] |= 1L << var9.ordinal() % 64) {
               var9 = (Enum)var8.next();
               var2 = var9.ordinal() / 64;
            }

            ArrayUtils.reverse(var7);
            return var7;
         }

         Enum var6 = (Enum)var8.next();
         if (var6 == null) {
            var3 = false;
         }

         Validate.isTrue(var3, "null elements not permitted");
         var5.add(var6);
      }
   }

   @SafeVarargs
   public static long[] generateBitVectors(Class var0, Enum... var1) {
      asEnum(var0);
      Validate.noNullElements((Object[])var1);
      EnumSet var3 = EnumSet.noneOf(var0);
      Collections.addAll(var3, var1);
      long[] var4 = new long[(((Enum[])var0.getEnumConstants()).length - 1) / 64 + 1];

      int var2;
      Enum var6;
      for(Iterator var5 = var3.iterator(); var5.hasNext(); var4[var2] |= 1L << var6.ordinal() % 64) {
         var6 = (Enum)var5.next();
         var2 = var6.ordinal() / 64;
      }

      ArrayUtils.reverse(var4);
      return var4;
   }

   public static Enum getEnum(Class var0, String var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            Enum var3 = Enum.valueOf(var0, var1);
            return var3;
         } catch (IllegalArgumentException var2) {
            return null;
         }
      }
   }

   public static Enum getEnumIgnoreCase(Class var0, String var1) {
      if (var1 != null) {
         if (!var0.isEnum()) {
            return null;
         } else {
            Enum[] var5 = (Enum[])var0.getEnumConstants();
            int var3 = var5.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               Enum var4 = var5[var2];
               if (var4.name().equalsIgnoreCase(var1)) {
                  return var4;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public static List getEnumList(Class var0) {
      return new ArrayList(Arrays.asList((Enum[])var0.getEnumConstants()));
   }

   public static Map getEnumMap(Class var0) {
      LinkedHashMap var3 = new LinkedHashMap();
      Enum[] var5 = (Enum[])var0.getEnumConstants();
      int var2 = var5.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         Enum var4 = var5[var1];
         var3.put(var4.name(), var4);
      }

      return var3;
   }

   public static boolean isValidEnum(Class var0, String var1) {
      return getEnum(var0, var1) != null;
   }

   public static boolean isValidEnumIgnoreCase(Class var0, String var1) {
      return getEnumIgnoreCase(var0, var1) != null;
   }

   public static EnumSet processBitVector(Class var0, long var1) {
      checkBitVectorable(var0).getEnumConstants();
      return processBitVectors(var0, var1);
   }

   public static EnumSet processBitVectors(Class var0, long... var1) {
      EnumSet var5 = EnumSet.noneOf(asEnum(var0));
      var1 = ArrayUtils.clone((long[])Validate.notNull(var1));
      ArrayUtils.reverse(var1);
      Enum[] var7 = (Enum[])var0.getEnumConstants();
      int var3 = var7.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Enum var6 = var7[var2];
         int var4 = var6.ordinal() / 64;
         if (var4 < var1.length && (var1[var4] & 1L << var6.ordinal() % 64) != 0L) {
            var5.add(var6);
         }
      }

      return var5;
   }
}
