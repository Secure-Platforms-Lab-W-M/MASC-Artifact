package org.apache.commons.lang3.time;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatUtils {
   public static final FastDateFormat ISO_8601_EXTENDED_DATETIME_FORMAT;
   public static final FastDateFormat ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT;
   public static final FastDateFormat ISO_8601_EXTENDED_DATE_FORMAT;
   public static final FastDateFormat ISO_8601_EXTENDED_TIME_FORMAT;
   public static final FastDateFormat ISO_8601_EXTENDED_TIME_TIME_ZONE_FORMAT;
   @Deprecated
   public static final FastDateFormat ISO_DATETIME_FORMAT;
   @Deprecated
   public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT;
   @Deprecated
   public static final FastDateFormat ISO_DATE_FORMAT;
   @Deprecated
   public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT;
   @Deprecated
   public static final FastDateFormat ISO_TIME_FORMAT;
   @Deprecated
   public static final FastDateFormat ISO_TIME_NO_T_FORMAT;
   @Deprecated
   public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT;
   @Deprecated
   public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT;
   public static final FastDateFormat SMTP_DATETIME_FORMAT;
   private static final TimeZone UTC_TIME_ZONE = FastTimeZone.getGmtTimeZone();

   static {
      FastDateFormat var0 = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
      ISO_8601_EXTENDED_DATETIME_FORMAT = var0;
      ISO_DATETIME_FORMAT = var0;
      var0 = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
      ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT = var0;
      ISO_DATETIME_TIME_ZONE_FORMAT = var0;
      var0 = FastDateFormat.getInstance("yyyy-MM-dd");
      ISO_8601_EXTENDED_DATE_FORMAT = var0;
      ISO_DATE_FORMAT = var0;
      ISO_DATE_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-ddZZ");
      ISO_TIME_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ss");
      ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ssZZ");
      var0 = FastDateFormat.getInstance("HH:mm:ss");
      ISO_8601_EXTENDED_TIME_FORMAT = var0;
      ISO_TIME_NO_T_FORMAT = var0;
      var0 = FastDateFormat.getInstance("HH:mm:ssZZ");
      ISO_8601_EXTENDED_TIME_TIME_ZONE_FORMAT = var0;
      ISO_TIME_NO_T_TIME_ZONE_FORMAT = var0;
      SMTP_DATETIME_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
   }

   public static String format(long var0, String var2) {
      return format((Date)(new Date(var0)), var2, (TimeZone)null, (Locale)null);
   }

   public static String format(long var0, String var2, Locale var3) {
      return format((Date)(new Date(var0)), var2, (TimeZone)null, var3);
   }

   public static String format(long var0, String var2, TimeZone var3) {
      return format((Date)(new Date(var0)), var2, var3, (Locale)null);
   }

   public static String format(long var0, String var2, TimeZone var3, Locale var4) {
      return format(new Date(var0), var2, var3, var4);
   }

   public static String format(Calendar var0, String var1) {
      return format((Calendar)var0, var1, (TimeZone)null, (Locale)null);
   }

   public static String format(Calendar var0, String var1, Locale var2) {
      return format((Calendar)var0, var1, (TimeZone)null, var2);
   }

   public static String format(Calendar var0, String var1, TimeZone var2) {
      return format((Calendar)var0, var1, var2, (Locale)null);
   }

   public static String format(Calendar var0, String var1, TimeZone var2, Locale var3) {
      return FastDateFormat.getInstance(var1, var2, var3).format(var0);
   }

   public static String format(Date var0, String var1) {
      return format((Date)var0, var1, (TimeZone)null, (Locale)null);
   }

   public static String format(Date var0, String var1, Locale var2) {
      return format((Date)var0, var1, (TimeZone)null, var2);
   }

   public static String format(Date var0, String var1, TimeZone var2) {
      return format((Date)var0, var1, var2, (Locale)null);
   }

   public static String format(Date var0, String var1, TimeZone var2, Locale var3) {
      return FastDateFormat.getInstance(var1, var2, var3).format(var0);
   }

   public static String formatUTC(long var0, String var2) {
      return format((Date)(new Date(var0)), var2, UTC_TIME_ZONE, (Locale)null);
   }

   public static String formatUTC(long var0, String var2, Locale var3) {
      return format(new Date(var0), var2, UTC_TIME_ZONE, var3);
   }

   public static String formatUTC(Date var0, String var1) {
      return format((Date)var0, var1, UTC_TIME_ZONE, (Locale)null);
   }

   public static String formatUTC(Date var0, String var1, Locale var2) {
      return format(var0, var1, UTC_TIME_ZONE, var2);
   }
}
