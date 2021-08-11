package org.apache.http.conn.util;

import java.util.regex.Pattern;

public class InetAddressUtils {
   private static final char COLON_CHAR = ':';
   private static final String IPV4_BASIC_PATTERN_STRING = "(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
   private static final Pattern IPV4_MAPPED_IPV6_PATTERN = Pattern.compile("^::[fF]{4}:(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
   private static final Pattern IPV4_PATTERN = Pattern.compile("^(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
   private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)::(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$");
   private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");
   private static final int MAX_COLON_COUNT = 7;

   private InetAddressUtils() {
   }

   public static boolean isIPv4Address(String var0) {
      return IPV4_PATTERN.matcher(var0).matches();
   }

   public static boolean isIPv4MappedIPv64Address(String var0) {
      return IPV4_MAPPED_IPV6_PATTERN.matcher(var0).matches();
   }

   public static boolean isIPv6Address(String var0) {
      return isIPv6StdAddress(var0) || isIPv6HexCompressedAddress(var0);
   }

   public static boolean isIPv6HexCompressedAddress(String var0) {
      int var2 = 0;

      int var3;
      for(int var1 = 0; var1 < var0.length(); var2 = var3) {
         var3 = var2;
         if (var0.charAt(var1) == ':') {
            var3 = var2 + 1;
         }

         ++var1;
      }

      if (var2 <= 7 && IPV6_HEX_COMPRESSED_PATTERN.matcher(var0).matches()) {
         return true;
      } else {
         return false;
      }
   }

   public static boolean isIPv6StdAddress(String var0) {
      return IPV6_STD_PATTERN.matcher(var0).matches();
   }
}
