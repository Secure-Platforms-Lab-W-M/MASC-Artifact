package org.apache.http.impl.cookie;

import java.util.Date;
import java.util.TimeZone;

@Deprecated
public final class DateUtils {
   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
   public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
   public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
   public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

   private DateUtils() {
   }

   public static String formatDate(Date var0) {
      return org.apache.http.client.utils.DateUtils.formatDate(var0);
   }

   public static String formatDate(Date var0, String var1) {
      return org.apache.http.client.utils.DateUtils.formatDate(var0, var1);
   }

   public static Date parseDate(String var0) throws DateParseException {
      return parseDate(var0, (String[])null, (Date)null);
   }

   public static Date parseDate(String var0, String[] var1) throws DateParseException {
      return parseDate(var0, var1, (Date)null);
   }

   public static Date parseDate(String var0, String[] var1, Date var2) throws DateParseException {
      Date var3 = org.apache.http.client.utils.DateUtils.parseDate(var0, var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unable to parse the date ");
         var4.append(var0);
         throw new DateParseException(var4.toString());
      }
   }
}
