package com.codetroopers.betterpickers.recurrencepicker;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import java.util.HashMap;

public class EventRecurrence {
   private static final boolean ALLOW_LOWER_CASE = true;
   public static final int DAILY = 4;
   // $FF: renamed from: FR int
   public static final int field_79 = 2097152;
   public static final int HOURLY = 3;
   public static final int MINUTELY = 2;
   // $FF: renamed from: MO int
   public static final int field_80 = 131072;
   public static final int MONTHLY = 6;
   private static final boolean ONLY_ONE_UNTIL_COUNT = false;
   private static final int PARSED_BYDAY = 128;
   private static final int PARSED_BYHOUR = 64;
   private static final int PARSED_BYMINUTE = 32;
   private static final int PARSED_BYMONTH = 2048;
   private static final int PARSED_BYMONTHDAY = 256;
   private static final int PARSED_BYSECOND = 16;
   private static final int PARSED_BYSETPOS = 4096;
   private static final int PARSED_BYWEEKNO = 1024;
   private static final int PARSED_BYYEARDAY = 512;
   private static final int PARSED_COUNT = 4;
   private static final int PARSED_FREQ = 1;
   private static final int PARSED_INTERVAL = 8;
   private static final int PARSED_UNTIL = 2;
   private static final int PARSED_WKST = 8192;
   // $FF: renamed from: SA int
   public static final int field_81 = 4194304;
   public static final int SECONDLY = 1;
   // $FF: renamed from: SU int
   public static final int field_82 = 65536;
   private static String TAG = "EventRecur";
   // $FF: renamed from: TH int
   public static final int field_83 = 1048576;
   // $FF: renamed from: TU int
   public static final int field_84 = 262144;
   private static final boolean VALIDATE_UNTIL = false;
   // $FF: renamed from: WE int
   public static final int field_85 = 524288;
   public static final int WEEKLY = 5;
   public static final int YEARLY = 7;
   private static final HashMap sParseFreqMap;
   private static HashMap sParsePartMap;
   private static final HashMap sParseWeekdayMap;
   public int[] byday;
   public int bydayCount;
   public int[] bydayNum;
   public int[] byhour;
   public int byhourCount;
   public int[] byminute;
   public int byminuteCount;
   public int[] bymonth;
   public int bymonthCount;
   public int[] bymonthday;
   public int bymonthdayCount;
   public int[] bysecond;
   public int bysecondCount;
   public int[] bysetpos;
   public int bysetposCount;
   public int[] byweekno;
   public int byweeknoCount;
   public int[] byyearday;
   public int byyeardayCount;
   public int count;
   public int freq;
   public int interval;
   public Time startDate;
   public String until;
   public int wkst;

   static {
      HashMap var0 = new HashMap();
      sParsePartMap = var0;
      var0.put("FREQ", new EventRecurrence.ParseFreq());
      sParsePartMap.put("UNTIL", new EventRecurrence.ParseUntil());
      sParsePartMap.put("COUNT", new EventRecurrence.ParseCount());
      sParsePartMap.put("INTERVAL", new EventRecurrence.ParseInterval());
      sParsePartMap.put("BYSECOND", new EventRecurrence.ParseBySecond());
      sParsePartMap.put("BYMINUTE", new EventRecurrence.ParseByMinute());
      sParsePartMap.put("BYHOUR", new EventRecurrence.ParseByHour());
      sParsePartMap.put("BYDAY", new EventRecurrence.ParseByDay());
      sParsePartMap.put("BYMONTHDAY", new EventRecurrence.ParseByMonthDay());
      sParsePartMap.put("BYYEARDAY", new EventRecurrence.ParseByYearDay());
      sParsePartMap.put("BYWEEKNO", new EventRecurrence.ParseByWeekNo());
      sParsePartMap.put("BYMONTH", new EventRecurrence.ParseByMonth());
      sParsePartMap.put("BYSETPOS", new EventRecurrence.ParseBySetPos());
      sParsePartMap.put("WKST", new EventRecurrence.ParseWkst());
      var0 = new HashMap();
      sParseFreqMap = var0;
      var0.put("SECONDLY", 1);
      sParseFreqMap.put("MINUTELY", 2);
      sParseFreqMap.put("HOURLY", 3);
      sParseFreqMap.put("DAILY", 4);
      sParseFreqMap.put("WEEKLY", 5);
      sParseFreqMap.put("MONTHLY", 6);
      sParseFreqMap.put("YEARLY", 7);
      var0 = new HashMap();
      sParseWeekdayMap = var0;
      var0.put("SU", 65536);
      sParseWeekdayMap.put("MO", 131072);
      sParseWeekdayMap.put("TU", 262144);
      sParseWeekdayMap.put("WE", 524288);
      sParseWeekdayMap.put("TH", 1048576);
      sParseWeekdayMap.put("FR", 2097152);
      sParseWeekdayMap.put("SA", 4194304);
   }

   private void appendByDay(StringBuilder var1, int var2) {
      int var3 = this.bydayNum[var2];
      if (var3 != 0) {
         var1.append(var3);
      }

      var1.append(day2String(this.byday[var2]));
   }

   private static void appendNumbers(StringBuilder var0, String var1, int var2, int[] var3) {
      if (var2 > 0) {
         var0.append(var1);
         int var4 = var2 - 1;

         for(var2 = 0; var2 < var4; ++var2) {
            var0.append(var3[var2]);
            var0.append(",");
         }

         var0.append(var3[var4]);
      }

   }

   private static boolean arraysEqual(int[] var0, int var1, int[] var2, int var3) {
      if (var1 != var3) {
         return false;
      } else {
         for(var3 = 0; var3 < var1; ++var3) {
            if (var0[var3] != var2[var3]) {
               return false;
            }
         }

         return true;
      }
   }

   public static int calendarDay2Day(int var0) {
      switch(var0) {
      case 1:
         return 65536;
      case 2:
         return 131072;
      case 3:
         return 262144;
      case 4:
         return 524288;
      case 5:
         return 1048576;
      case 6:
         return 2097152;
      case 7:
         return 4194304;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("bad day of week: ");
         var1.append(var0);
         throw new RuntimeException(var1.toString());
      }
   }

   public static int day2CalendarDay(int var0) {
      if (var0 != 65536) {
         if (var0 != 131072) {
            if (var0 != 262144) {
               if (var0 != 524288) {
                  if (var0 != 1048576) {
                     if (var0 != 2097152) {
                        if (var0 == 4194304) {
                           return 7;
                        } else {
                           StringBuilder var1 = new StringBuilder();
                           var1.append("bad day of week: ");
                           var1.append(var0);
                           throw new RuntimeException(var1.toString());
                        }
                     } else {
                        return 6;
                     }
                  } else {
                     return 5;
                  }
               } else {
                  return 4;
               }
            } else {
               return 3;
            }
         } else {
            return 2;
         }
      } else {
         return 1;
      }
   }

   private static String day2String(int var0) {
      if (var0 != 65536) {
         if (var0 != 131072) {
            if (var0 != 262144) {
               if (var0 != 524288) {
                  if (var0 != 1048576) {
                     if (var0 != 2097152) {
                        if (var0 == 4194304) {
                           return "SA";
                        } else {
                           StringBuilder var1 = new StringBuilder();
                           var1.append("bad day argument: ");
                           var1.append(var0);
                           throw new IllegalArgumentException(var1.toString());
                        }
                     } else {
                        return "FR";
                     }
                  } else {
                     return "TH";
                  }
               } else {
                  return "WE";
               }
            } else {
               return "TU";
            }
         } else {
            return "MO";
         }
      } else {
         return "SU";
      }
   }

   public static int day2TimeDay(int var0) {
      if (var0 != 65536) {
         if (var0 != 131072) {
            if (var0 != 262144) {
               if (var0 != 524288) {
                  if (var0 != 1048576) {
                     if (var0 != 2097152) {
                        if (var0 == 4194304) {
                           return 6;
                        } else {
                           StringBuilder var1 = new StringBuilder();
                           var1.append("bad day of week: ");
                           var1.append(var0);
                           throw new RuntimeException(var1.toString());
                        }
                     } else {
                        return 5;
                     }
                  } else {
                     return 4;
                  }
               } else {
                  return 3;
               }
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      } else {
         return 0;
      }
   }

   private void resetFields() {
      this.until = null;
      this.bysetposCount = 0;
      this.bymonthCount = 0;
      this.byweeknoCount = 0;
      this.byyeardayCount = 0;
      this.bymonthdayCount = 0;
      this.bydayCount = 0;
      this.byhourCount = 0;
      this.byminuteCount = 0;
      this.bysecondCount = 0;
      this.interval = 0;
      this.count = 0;
      this.freq = 0;
   }

   public static int timeDay2Day(int var0) {
      switch(var0) {
      case 0:
         return 65536;
      case 1:
         return 131072;
      case 2:
         return 262144;
      case 3:
         return 524288;
      case 4:
         return 1048576;
      case 5:
         return 2097152;
      case 6:
         return 4194304;
      default:
         StringBuilder var1 = new StringBuilder();
         var1.append("bad day of week: ");
         var1.append(var0);
         throw new RuntimeException(var1.toString());
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof EventRecurrence)) {
         return false;
      } else {
         EventRecurrence var3 = (EventRecurrence)var1;
         Time var2 = this.startDate;
         if (var2 == null) {
            if (var3.startDate != null) {
               return false;
            }
         } else if (Time.compare(var2, var3.startDate) != 0) {
            return false;
         }

         if (this.freq == var3.freq) {
            String var4 = this.until;
            if (var4 == null) {
               if (var3.until != null) {
                  return false;
               }
            } else if (!var4.equals(var3.until)) {
               return false;
            }

            if (this.count == var3.count && this.interval == var3.interval && this.wkst == var3.wkst && arraysEqual(this.bysecond, this.bysecondCount, var3.bysecond, var3.bysecondCount) && arraysEqual(this.byminute, this.byminuteCount, var3.byminute, var3.byminuteCount) && arraysEqual(this.byhour, this.byhourCount, var3.byhour, var3.byhourCount) && arraysEqual(this.byday, this.bydayCount, var3.byday, var3.bydayCount) && arraysEqual(this.bydayNum, this.bydayCount, var3.bydayNum, var3.bydayCount) && arraysEqual(this.bymonthday, this.bymonthdayCount, var3.bymonthday, var3.bymonthdayCount) && arraysEqual(this.byyearday, this.byyeardayCount, var3.byyearday, var3.byyeardayCount) && arraysEqual(this.byweekno, this.byweeknoCount, var3.byweekno, var3.byweeknoCount) && arraysEqual(this.bymonth, this.bymonthCount, var3.bymonth, var3.bymonthCount) && arraysEqual(this.bysetpos, this.bysetposCount, var3.bysetpos, var3.bysetposCount)) {
               return true;
            }
         }

         return false;
      }
   }

   public int hashCode() {
      throw new UnsupportedOperationException();
   }

   public void parse(String var1) {
      this.resetFields();
      int var3 = 0;
      String[] var8 = var1.toUpperCase().split(";");
      int var4 = var8.length;

      String var6;
      for(int var2 = 0; var2 < var4; ++var2) {
         String var7 = var8[var2];
         if (!TextUtils.isEmpty(var7)) {
            int var5 = var7.indexOf(61);
            StringBuilder var10;
            if (var5 <= 0) {
               var10 = new StringBuilder();
               var10.append("Missing LHS in ");
               var10.append(var7);
               throw new EventRecurrence.InvalidFormatException(var10.toString());
            }

            var6 = var7.substring(0, var5);
            String var9 = var7.substring(var5 + 1);
            if (var9.length() == 0) {
               var10 = new StringBuilder();
               var10.append("Missing RHS in ");
               var10.append(var7);
               throw new EventRecurrence.InvalidFormatException(var10.toString());
            }

            EventRecurrence.PartParser var11 = (EventRecurrence.PartParser)sParsePartMap.get(var6);
            if (var11 == null) {
               if (!var6.startsWith("X-")) {
                  var10 = new StringBuilder();
                  var10.append("Couldn't find parser for ");
                  var10.append(var6);
                  throw new EventRecurrence.InvalidFormatException(var10.toString());
               }
            } else {
               var5 = var11.parsePart(var9, this);
               if ((var3 & var5) != 0) {
                  var10 = new StringBuilder();
                  var10.append("Part ");
                  var10.append(var6);
                  var10.append(" was specified twice");
                  throw new EventRecurrence.InvalidFormatException(var10.toString());
               }

               var3 |= var5;
            }
         }
      }

      if ((var3 & 8192) == 0) {
         this.wkst = 131072;
      }

      if ((var3 & 1) != 0) {
         if ((var3 & 6) == 6) {
            var6 = TAG;
            StringBuilder var12 = new StringBuilder();
            var12.append("Warning: rrule has both UNTIL and COUNT: ");
            var12.append(var1);
            Log.w(var6, var12.toString());
         }

      } else {
         throw new EventRecurrence.InvalidFormatException("Must specify a FREQ value");
      }
   }

   public boolean repeatsMonthlyOnDayCount() {
      if (this.freq != 6) {
         return false;
      } else if (this.bydayCount == 1) {
         if (this.bymonthdayCount != 0) {
            return false;
         } else {
            return this.bydayNum[0] > 0;
         }
      } else {
         return false;
      }
   }

   public boolean repeatsOnEveryWeekDay() {
      if (this.freq != 5) {
         return false;
      } else {
         int var2 = this.bydayCount;
         if (var2 != 5) {
            return false;
         } else {
            for(int var1 = 0; var1 < var2; ++var1) {
               int var3 = this.byday[var1];
               if (var3 == 65536) {
                  return false;
               }

               if (var3 == 4194304) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public void setStartDate(Time var1) {
      this.startDate = var1;
   }

   public String toString() {
      StringBuilder var3 = new StringBuilder();
      var3.append("FREQ=");
      switch(this.freq) {
      case 1:
         var3.append("SECONDLY");
         break;
      case 2:
         var3.append("MINUTELY");
         break;
      case 3:
         var3.append("HOURLY");
         break;
      case 4:
         var3.append("DAILY");
         break;
      case 5:
         var3.append("WEEKLY");
         break;
      case 6:
         var3.append("MONTHLY");
         break;
      case 7:
         var3.append("YEARLY");
      }

      if (!TextUtils.isEmpty(this.until)) {
         var3.append(";UNTIL=");
         var3.append(this.until);
      }

      if (this.count != 0) {
         var3.append(";COUNT=");
         var3.append(this.count);
      }

      if (this.interval != 0) {
         var3.append(";INTERVAL=");
         var3.append(this.interval);
      }

      if (this.wkst != 0) {
         var3.append(";WKST=");
         var3.append(day2String(this.wkst));
      }

      appendNumbers(var3, ";BYSECOND=", this.bysecondCount, this.bysecond);
      appendNumbers(var3, ";BYMINUTE=", this.byminuteCount, this.byminute);
      appendNumbers(var3, ";BYHOUR=", this.byhourCount, this.byhour);
      int var1 = this.bydayCount;
      if (var1 > 0) {
         var3.append(";BYDAY=");
         int var2 = var1 - 1;

         for(var1 = 0; var1 < var2; ++var1) {
            this.appendByDay(var3, var1);
            var3.append(",");
         }

         this.appendByDay(var3, var2);
      }

      appendNumbers(var3, ";BYMONTHDAY=", this.bymonthdayCount, this.bymonthday);
      appendNumbers(var3, ";BYYEARDAY=", this.byyeardayCount, this.byyearday);
      appendNumbers(var3, ";BYWEEKNO=", this.byweeknoCount, this.byweekno);
      appendNumbers(var3, ";BYMONTH=", this.bymonthCount, this.bymonth);
      appendNumbers(var3, ";BYSETPOS=", this.bysetposCount, this.bysetpos);
      return var3.toString();
   }

   public static class InvalidFormatException extends RuntimeException {
      InvalidFormatException(String var1) {
         super(var1);
      }
   }

   private static class ParseByDay extends EventRecurrence.PartParser {
      private ParseByDay() {
      }

      // $FF: synthetic method
      ParseByDay(Object var1) {
         this();
      }

      private static void parseWday(String var0, int[] var1, int[] var2, int var3) {
         int var4 = var0.length() - 2;
         String var6;
         if (var4 > 0) {
            var2[var3] = parseIntRange(var0.substring(0, var4), -53, 53, false);
            var6 = var0.substring(var4);
         } else {
            var6 = var0;
         }

         Integer var7 = (Integer)EventRecurrence.sParseWeekdayMap.get(var6);
         if (var7 != null) {
            var1[var3] = var7;
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("Invalid BYDAY value: ");
            var5.append(var0);
            throw new EventRecurrence.InvalidFormatException(var5.toString());
         }
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int var3;
         int[] var5;
         int[] var7;
         if (var1.indexOf(",") < 0) {
            var3 = 1;
            int[] var6 = new int[1];
            var5 = new int[1];
            parseWday(var1, var6, var5, 0);
            var7 = var6;
         } else {
            String[] var8 = var1.split(",");
            int var4 = var8.length;
            var7 = new int[var4];
            var5 = new int[var4];

            for(var3 = 0; var3 < var4; ++var3) {
               parseWday(var8[var3], var7, var5, var3);
            }

            var3 = var4;
         }

         var2.byday = var7;
         var2.bydayNum = var5;
         var2.bydayCount = var3;
         return 128;
      }
   }

   private static class ParseByHour extends EventRecurrence.PartParser {
      private ParseByHour() {
      }

      // $FF: synthetic method
      ParseByHour(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int[] var3 = parseNumberList(var1, 0, 23, true);
         var2.byhour = var3;
         var2.byhourCount = var3.length;
         return 64;
      }
   }

   private static class ParseByMinute extends EventRecurrence.PartParser {
      private ParseByMinute() {
      }

      // $FF: synthetic method
      ParseByMinute(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int[] var3 = parseNumberList(var1, 0, 59, true);
         var2.byminute = var3;
         var2.byminuteCount = var3.length;
         return 32;
      }
   }

   private static class ParseByMonth extends EventRecurrence.PartParser {
      private ParseByMonth() {
      }

      // $FF: synthetic method
      ParseByMonth(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int[] var3 = parseNumberList(var1, 1, 12, false);
         var2.bymonth = var3;
         var2.bymonthCount = var3.length;
         return 2048;
      }
   }

   private static class ParseByMonthDay extends EventRecurrence.PartParser {
      private ParseByMonthDay() {
      }

      // $FF: synthetic method
      ParseByMonthDay(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int[] var3 = parseNumberList(var1, -31, 31, false);
         var2.bymonthday = var3;
         var2.bymonthdayCount = var3.length;
         return 256;
      }
   }

   private static class ParseBySecond extends EventRecurrence.PartParser {
      private ParseBySecond() {
      }

      // $FF: synthetic method
      ParseBySecond(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int[] var3 = parseNumberList(var1, 0, 59, true);
         var2.bysecond = var3;
         var2.bysecondCount = var3.length;
         return 16;
      }
   }

   private static class ParseBySetPos extends EventRecurrence.PartParser {
      private ParseBySetPos() {
      }

      // $FF: synthetic method
      ParseBySetPos(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int[] var3 = parseNumberList(var1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
         var2.bysetpos = var3;
         var2.bysetposCount = var3.length;
         return 4096;
      }
   }

   private static class ParseByWeekNo extends EventRecurrence.PartParser {
      private ParseByWeekNo() {
      }

      // $FF: synthetic method
      ParseByWeekNo(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int[] var3 = parseNumberList(var1, -53, 53, false);
         var2.byweekno = var3;
         var2.byweeknoCount = var3.length;
         return 1024;
      }
   }

   private static class ParseByYearDay extends EventRecurrence.PartParser {
      private ParseByYearDay() {
      }

      // $FF: synthetic method
      ParseByYearDay(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         int[] var3 = parseNumberList(var1, -366, 366, false);
         var2.byyearday = var3;
         var2.byyeardayCount = var3.length;
         return 512;
      }
   }

   private static class ParseCount extends EventRecurrence.PartParser {
      private ParseCount() {
      }

      // $FF: synthetic method
      ParseCount(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         var2.count = parseIntRange(var1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
         if (var2.count < 0) {
            String var3 = EventRecurrence.TAG;
            StringBuilder var4 = new StringBuilder();
            var4.append("Invalid Count. Forcing COUNT to 1 from ");
            var4.append(var1);
            Log.d(var3, var4.toString());
            var2.count = 1;
         }

         return 4;
      }
   }

   private static class ParseFreq extends EventRecurrence.PartParser {
      private ParseFreq() {
      }

      // $FF: synthetic method
      ParseFreq(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         Integer var3 = (Integer)EventRecurrence.sParseFreqMap.get(var1);
         if (var3 != null) {
            var2.freq = var3;
            return 1;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Invalid FREQ value: ");
            var4.append(var1);
            throw new EventRecurrence.InvalidFormatException(var4.toString());
         }
      }
   }

   private static class ParseInterval extends EventRecurrence.PartParser {
      private ParseInterval() {
      }

      // $FF: synthetic method
      ParseInterval(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         var2.interval = parseIntRange(var1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
         if (var2.interval < 1) {
            String var3 = EventRecurrence.TAG;
            StringBuilder var4 = new StringBuilder();
            var4.append("Invalid Interval. Forcing INTERVAL to 1 from ");
            var4.append(var1);
            Log.d(var3, var4.toString());
            var2.interval = 1;
         }

         return 8;
      }
   }

   private static class ParseUntil extends EventRecurrence.PartParser {
      private ParseUntil() {
      }

      // $FF: synthetic method
      ParseUntil(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         var2.until = var1;
         return 2;
      }
   }

   private static class ParseWkst extends EventRecurrence.PartParser {
      private ParseWkst() {
      }

      // $FF: synthetic method
      ParseWkst(Object var1) {
         this();
      }

      public int parsePart(String var1, EventRecurrence var2) {
         Integer var3 = (Integer)EventRecurrence.sParseWeekdayMap.get(var1);
         if (var3 != null) {
            var2.wkst = var3;
            return 8192;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Invalid WKST value: ");
            var4.append(var1);
            throw new EventRecurrence.InvalidFormatException(var4.toString());
         }
      }
   }

   abstract static class PartParser {
      public static int parseIntRange(String var0, int var1, int var2, boolean var3) {
         String var5 = var0;
         String var6 = var0;

         StringBuilder var14;
         label81: {
            boolean var10001;
            label80: {
               try {
                  if (var0.charAt(0) != '+') {
                     break label80;
                  }
               } catch (NumberFormatException var13) {
                  var10001 = false;
                  break label81;
               }

               var6 = var0;

               try {
                  var5 = var0.substring(1);
               } catch (NumberFormatException var12) {
                  var10001 = false;
                  break label81;
               }
            }

            var6 = var5;

            int var4;
            try {
               var4 = Integer.parseInt(var5);
            } catch (NumberFormatException var11) {
               var10001 = false;
               break label81;
            }

            if (var4 >= var1 && var4 <= var2) {
               if (var4 != 0) {
                  return var4;
               }

               if (var3) {
                  return var4;
               }
            }

            var6 = var5;

            try {
               var14 = new StringBuilder();
            } catch (NumberFormatException var10) {
               var10001 = false;
               break label81;
            }

            var6 = var5;

            try {
               var14.append("Integer value out of range: ");
            } catch (NumberFormatException var9) {
               var10001 = false;
               break label81;
            }

            var6 = var5;

            try {
               var14.append(var5);
            } catch (NumberFormatException var8) {
               var10001 = false;
               break label81;
            }

            var6 = var5;

            try {
               throw new EventRecurrence.InvalidFormatException(var14.toString());
            } catch (NumberFormatException var7) {
               var10001 = false;
            }
         }

         var14 = new StringBuilder();
         var14.append("Invalid integer value: ");
         var14.append(var6);
         throw new EventRecurrence.InvalidFormatException(var14.toString());
      }

      public static int[] parseNumberList(String var0, int var1, int var2, boolean var3) {
         if (var0.indexOf(",") < 0) {
            return new int[]{parseIntRange(var0, var1, var2, var3)};
         } else {
            String[] var7 = var0.split(",");
            int var5 = var7.length;
            int[] var6 = new int[var5];

            for(int var4 = 0; var4 < var5; ++var4) {
               var6[var4] = parseIntRange(var7[var4], var1, var2, var3);
            }

            return var6;
         }
      }

      public abstract int parsePart(String var1, EventRecurrence var2);
   }
}
