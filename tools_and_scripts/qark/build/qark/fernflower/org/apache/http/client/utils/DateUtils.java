package org.apache.http.client.utils;

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.http.util.Args;

public final class DateUtils {
   private static final String[] DEFAULT_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy"};
   private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
   public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
   public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
   public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

   static {
      Calendar var0 = Calendar.getInstance();
      var0.setTimeZone(GMT);
      var0.set(2000, 0, 1, 0, 0, 0);
      var0.set(14, 0);
      DEFAULT_TWO_DIGIT_YEAR_START = var0.getTime();
   }

   private DateUtils() {
   }

   public static void clearThreadLocal() {
      DateUtils.DateFormatHolder.clearThreadLocal();
   }

   public static String formatDate(Date var0) {
      return formatDate(var0, "EEE, dd MMM yyyy HH:mm:ss zzz");
   }

   public static String formatDate(Date var0, String var1) {
      Args.notNull(var0, "Date");
      Args.notNull(var1, "Pattern");
      return DateUtils.DateFormatHolder.formatFor(var1).format(var0);
   }

   public static Date parseDate(String var0) {
      return parseDate(var0, (String[])null, (Date)null);
   }

   public static Date parseDate(String var0, String[] var1) {
      return parseDate(var0, var1, (Date)null);
   }

   public static Date parseDate(String var0, String[] var1, Date var2) {
      Args.notNull(var0, "Date value");
      if (var1 == null) {
         var1 = DEFAULT_PATTERNS;
      }

      if (var2 == null) {
         var2 = DEFAULT_TWO_DIGIT_YEAR_START;
      }

      String var5 = var0;
      var0 = var0;
      if (var5.length() > 1) {
         var0 = var5;
         if (var5.startsWith("'")) {
            var0 = var5;
            if (var5.endsWith("'")) {
               var0 = var5.substring(1, var5.length() - 1);
            }
         }
      }

      int var4 = var1.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         SimpleDateFormat var6 = DateUtils.DateFormatHolder.formatFor(var1[var3]);
         var6.set2DigitYearStart(var2);
         ParsePosition var7 = new ParsePosition(0);
         Date var8 = var6.parse(var0, var7);
         if (var7.getIndex() != 0) {
            return var8;
         }
      }

      return null;
   }

   static final class DateFormatHolder {
      private static final ThreadLocal THREADLOCAL_FORMATS = new ThreadLocal();

      public static void clearThreadLocal() {
         THREADLOCAL_FORMATS.remove();
      }

      public static SimpleDateFormat formatFor(String var0) {
         SoftReference var1 = (SoftReference)THREADLOCAL_FORMATS.get();
         Map var4;
         if (var1 == null) {
            var4 = null;
         } else {
            var4 = (Map)var1.get();
         }

         Object var2 = var4;
         if (var4 == null) {
            var2 = new HashMap();
            THREADLOCAL_FORMATS.set(new SoftReference(var2));
         }

         SimpleDateFormat var3 = (SimpleDateFormat)((Map)var2).get(var0);
         SimpleDateFormat var5 = var3;
         if (var3 == null) {
            var5 = new SimpleDateFormat(var0, Locale.US);
            var5.setTimeZone(TimeZone.getTimeZone("GMT"));
            ((Map)var2).put(var0, var5);
         }

         return var5;
      }
   }
}
