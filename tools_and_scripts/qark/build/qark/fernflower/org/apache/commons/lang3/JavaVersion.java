package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;

public enum JavaVersion {
   JAVA_0_9(1.5F, "0.9"),
   JAVA_10(10.0F, "10"),
   JAVA_11(11.0F, "11"),
   JAVA_12(12.0F, "12"),
   JAVA_13(13.0F, "13"),
   JAVA_1_1(1.1F, "1.1"),
   JAVA_1_2(1.2F, "1.2"),
   JAVA_1_3(1.3F, "1.3"),
   JAVA_1_4(1.4F, "1.4"),
   JAVA_1_5(1.5F, "1.5"),
   JAVA_1_6(1.6F, "1.6"),
   JAVA_1_7(1.7F, "1.7"),
   JAVA_1_8(1.8F, "1.8"),
   @Deprecated
   JAVA_1_9(9.0F, "9"),
   JAVA_9(9.0F, "9"),
   JAVA_RECENT;

   private final String name;
   private final float value;

   static {
      JavaVersion var0 = new JavaVersion("JAVA_RECENT", 15, maxVersion(), Float.toString(maxVersion()));
      JAVA_RECENT = var0;
   }

   private JavaVersion(float var3, String var4) {
      this.value = var3;
      this.name = var4;
   }

   static JavaVersion get(String var0) {
      if ("0.9".equals(var0)) {
         return JAVA_0_9;
      } else if ("1.1".equals(var0)) {
         return JAVA_1_1;
      } else if ("1.2".equals(var0)) {
         return JAVA_1_2;
      } else if ("1.3".equals(var0)) {
         return JAVA_1_3;
      } else if ("1.4".equals(var0)) {
         return JAVA_1_4;
      } else if ("1.5".equals(var0)) {
         return JAVA_1_5;
      } else if ("1.6".equals(var0)) {
         return JAVA_1_6;
      } else if ("1.7".equals(var0)) {
         return JAVA_1_7;
      } else if ("1.8".equals(var0)) {
         return JAVA_1_8;
      } else if ("9".equals(var0)) {
         return JAVA_9;
      } else if ("10".equals(var0)) {
         return JAVA_10;
      } else if ("11".equals(var0)) {
         return JAVA_11;
      } else if ("12".equals(var0)) {
         return JAVA_12;
      } else if ("13".equals(var0)) {
         return JAVA_13;
      } else if (var0 == null) {
         return null;
      } else {
         float var1 = toFloatVersion(var0);
         if ((double)var1 - 1.0D < 1.0D) {
            int var2 = Math.max(var0.indexOf(46), var0.indexOf(44));
            return Float.parseFloat(var0.substring(var2 + 1, Math.max(var0.length(), var0.indexOf(44, var2)))) > 0.9F ? JAVA_RECENT : null;
         } else {
            return var1 > 10.0F ? JAVA_RECENT : null;
         }
      }
   }

   static JavaVersion getJavaVersion(String var0) {
      return get(var0);
   }

   private static float maxVersion() {
      float var0 = toFloatVersion(System.getProperty("java.specification.version", "99.0"));
      return var0 > 0.0F ? var0 : 99.0F;
   }

   private static float toFloatVersion(String var0) {
      if (var0.contains(".")) {
         String[] var2 = var0.split("\\.");
         if (var2.length >= 2) {
            StringBuilder var1 = new StringBuilder();
            var1.append(var2[0]);
            var1.append('.');
            var1.append(var2[1]);
            return NumberUtils.toFloat(var1.toString(), -1.0F);
         } else {
            return -1.0F;
         }
      } else {
         return NumberUtils.toFloat(var0, -1.0F);
      }
   }

   public boolean atLeast(JavaVersion var1) {
      return this.value >= var1.value;
   }

   public boolean atMost(JavaVersion var1) {
      return this.value <= var1.value;
   }

   public String toString() {
      return this.name;
   }
}
