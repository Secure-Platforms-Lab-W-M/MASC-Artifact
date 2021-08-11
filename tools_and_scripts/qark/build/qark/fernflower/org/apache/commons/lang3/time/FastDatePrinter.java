package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FastDatePrinter implements DatePrinter, Serializable {
   public static final int FULL = 0;
   public static final int LONG = 1;
   private static final int MAX_DIGITS = 10;
   public static final int MEDIUM = 2;
   public static final int SHORT = 3;
   private static final ConcurrentMap cTimeZoneDisplayCache = new ConcurrentHashMap(7);
   private static final long serialVersionUID = 1L;
   private final Locale mLocale;
   private transient int mMaxLengthEstimate;
   private final String mPattern;
   private transient FastDatePrinter.Rule[] mRules;
   private final TimeZone mTimeZone;

   protected FastDatePrinter(String var1, TimeZone var2, Locale var3) {
      this.mPattern = var1;
      this.mTimeZone = var2;
      this.mLocale = var3;
      this.init();
   }

   private static void appendDigits(Appendable var0, int var1) throws IOException {
      var0.append((char)(var1 / 10 + 48));
      var0.append((char)(var1 % 10 + 48));
   }

   private static void appendFullDigits(Appendable var0, int var1, int var2) throws IOException {
      int var3;
      int var6;
      if (var1 < 10000) {
         var3 = 4;
         if (var1 < 1000) {
            var6 = 4 - 1;
            var3 = var6;
            if (var1 < 100) {
               --var6;
               var3 = var6;
               if (var1 < 10) {
                  var3 = var6 - 1;
               }
            }
         }

         for(var2 -= var3; var2 > 0; --var2) {
            var0.append('0');
         }

         var2 = var1;
         if (var3 != 1) {
            var2 = var1;
            if (var3 != 2) {
               var2 = var1;
               if (var3 != 3) {
                  if (var3 != 4) {
                     return;
                  }

                  var0.append((char)(var1 / 1000 + 48));
                  var2 = var1 % 1000;
               }

               if (var2 >= 100) {
                  var0.append((char)(var2 / 100 + 48));
                  var2 %= 100;
               } else {
                  var0.append('0');
               }
            }

            if (var2 >= 10) {
               var0.append((char)(var2 / 10 + 48));
               var2 %= 10;
            } else {
               var0.append('0');
            }
         }

         var0.append((char)(var2 + 48));
      } else {
         char[] var5 = new char[10];
         byte var4 = 0;
         var3 = var1;
         var1 = var4;

         while(true) {
            var6 = var2;
            if (var3 == 0) {
               while(true) {
                  var2 = var1;
                  if (var1 >= var6) {
                     while(true) {
                        --var2;
                        if (var2 < 0) {
                           return;
                        }

                        var0.append(var5[var2]);
                     }
                  }

                  var0.append('0');
                  --var6;
               }
            }

            var5[var1] = (char)(var3 % 10 + 48);
            var3 /= 10;
            ++var1;
         }
      }
   }

   private Appendable applyRules(Calendar param1, Appendable param2) {
      // $FF: Couldn't be decompiled
   }

   private String applyRulesToString(Calendar var1) {
      return ((StringBuilder)this.applyRules(var1, (Appendable)(new StringBuilder(this.mMaxLengthEstimate)))).toString();
   }

   static String getTimeZoneDisplay(TimeZone var0, boolean var1, int var2, Locale var3) {
      FastDatePrinter.TimeZoneDisplayKey var6 = new FastDatePrinter.TimeZoneDisplayKey(var0, var1, var2, var3);
      String var5 = (String)cTimeZoneDisplayCache.get(var6);
      String var4 = var5;
      if (var5 == null) {
         var4 = var0.getDisplayName(var1, var2, var3);
         String var7 = (String)cTimeZoneDisplayCache.putIfAbsent(var6, var4);
         if (var7 != null) {
            var4 = var7;
         }
      }

      return var4;
   }

   private void init() {
      List var3 = this.parsePattern();
      FastDatePrinter.Rule[] var4 = (FastDatePrinter.Rule[])var3.toArray(new FastDatePrinter.Rule[var3.size()]);
      this.mRules = var4;
      int var1 = 0;
      int var2 = var4.length;

      while(true) {
         --var2;
         if (var2 < 0) {
            this.mMaxLengthEstimate = var1;
            return;
         }

         var1 += this.mRules[var2].estimateLength();
      }
   }

   private Calendar newCalendar() {
      return Calendar.getInstance(this.mTimeZone, this.mLocale);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.init();
   }

   @Deprecated
   protected StringBuffer applyRules(Calendar var1, StringBuffer var2) {
      return (StringBuffer)this.applyRules(var1, (Appendable)var2);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof FastDatePrinter)) {
         return false;
      } else {
         FastDatePrinter var2 = (FastDatePrinter)var1;
         return this.mPattern.equals(var2.mPattern) && this.mTimeZone.equals(var2.mTimeZone) && this.mLocale.equals(var2.mLocale);
      }
   }

   public Appendable format(long var1, Appendable var3) {
      Calendar var4 = this.newCalendar();
      var4.setTimeInMillis(var1);
      return this.applyRules(var4, var3);
   }

   public Appendable format(Calendar var1, Appendable var2) {
      Calendar var3 = var1;
      if (!var1.getTimeZone().equals(this.mTimeZone)) {
         var3 = (Calendar)var1.clone();
         var3.setTimeZone(this.mTimeZone);
      }

      return this.applyRules(var3, var2);
   }

   public Appendable format(Date var1, Appendable var2) {
      Calendar var3 = this.newCalendar();
      var3.setTime(var1);
      return this.applyRules(var3, var2);
   }

   public String format(long var1) {
      Calendar var3 = this.newCalendar();
      var3.setTimeInMillis(var1);
      return this.applyRulesToString(var3);
   }

   String format(Object var1) {
      if (var1 instanceof Date) {
         return this.format((Date)var1);
      } else if (var1 instanceof Calendar) {
         return this.format((Calendar)var1);
      } else if (var1 instanceof Long) {
         return this.format((Long)var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unknown class: ");
         String var3;
         if (var1 == null) {
            var3 = "<null>";
         } else {
            var3 = var1.getClass().getName();
         }

         var2.append(var3);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public String format(Calendar var1) {
      return ((StringBuilder)this.format((Calendar)var1, (Appendable)(new StringBuilder(this.mMaxLengthEstimate)))).toString();
   }

   public String format(Date var1) {
      Calendar var2 = this.newCalendar();
      var2.setTime(var1);
      return this.applyRulesToString(var2);
   }

   public StringBuffer format(long var1, StringBuffer var3) {
      Calendar var4 = this.newCalendar();
      var4.setTimeInMillis(var1);
      return (StringBuffer)this.applyRules(var4, (Appendable)var3);
   }

   @Deprecated
   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      if (var1 instanceof Date) {
         return this.format((Date)var1, var2);
      } else if (var1 instanceof Calendar) {
         return this.format((Calendar)var1, var2);
      } else if (var1 instanceof Long) {
         return this.format((Long)var1, var2);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Unknown class: ");
         String var4;
         if (var1 == null) {
            var4 = "<null>";
         } else {
            var4 = var1.getClass().getName();
         }

         var5.append(var4);
         throw new IllegalArgumentException(var5.toString());
      }
   }

   public StringBuffer format(Calendar var1, StringBuffer var2) {
      return this.format(var1.getTime(), var2);
   }

   public StringBuffer format(Date var1, StringBuffer var2) {
      Calendar var3 = this.newCalendar();
      var3.setTime(var1);
      return (StringBuffer)this.applyRules(var3, (Appendable)var2);
   }

   public Locale getLocale() {
      return this.mLocale;
   }

   public int getMaxLengthEstimate() {
      return this.mMaxLengthEstimate;
   }

   public String getPattern() {
      return this.mPattern;
   }

   public TimeZone getTimeZone() {
      return this.mTimeZone;
   }

   public int hashCode() {
      return this.mPattern.hashCode() + (this.mTimeZone.hashCode() + this.mLocale.hashCode() * 13) * 13;
   }

   protected List parsePattern() {
      DateFormatSymbols var10 = new DateFormatSymbols(this.mLocale);
      ArrayList var12 = new ArrayList();
      String[] var9 = var10.getEras();
      String[] var13 = var10.getMonths();
      String[] var14 = var10.getShortMonths();
      String[] var7 = var10.getWeekdays();
      String[] var11 = var10.getShortWeekdays();
      String[] var15 = var10.getAmPmStrings();
      int var3 = this.mPattern.length();
      int[] var16 = new int[1];

      int var4;
      for(int var1 = 0; var1 < var3; var1 = var4 + 1) {
         var16[0] = var1;
         String var6 = this.parseToken(this.mPattern, var16);
         var4 = var16[0];
         var1 = var6.length();
         if (var1 == 0) {
            return var12;
         }

         Object var17;
         label99: {
            char var5 = var6.charAt(0);
            byte var2 = 4;
            if (var5 != 'y') {
               if (var5 == 'z') {
                  if (var1 >= 4) {
                     var17 = new FastDatePrinter.TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
                  } else {
                     var17 = new FastDatePrinter.TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
                  }
                  break label99;
               }

               switch(var5) {
               case '\'':
                  var6 = var6.substring(1);
                  if (var6.length() == 1) {
                     var17 = new FastDatePrinter.CharacterLiteral(var6.charAt(0));
                  } else {
                     var17 = new FastDatePrinter.StringLiteral(var6);
                  }
                  break label99;
               case 'K':
                  var17 = this.selectNumberRule(10, var1);
                  break label99;
               case 'M':
                  if (var1 >= 4) {
                     var17 = new FastDatePrinter.TextField(2, var13);
                  } else if (var1 == 3) {
                     var17 = new FastDatePrinter.TextField(2, var14);
                  } else if (var1 == 2) {
                     var17 = FastDatePrinter.TwoDigitMonthField.INSTANCE;
                  } else {
                     var17 = FastDatePrinter.UnpaddedMonthField.INSTANCE;
                  }
                  break label99;
               case 'S':
                  var17 = this.selectNumberRule(14, var1);
                  break label99;
               case 'a':
                  var17 = new FastDatePrinter.TextField(9, var15);
                  break label99;
               case 'd':
                  var17 = this.selectNumberRule(5, var1);
                  break label99;
               case 'h':
                  var17 = new FastDatePrinter.TwelveHourField(this.selectNumberRule(10, var1));
                  break label99;
               case 'k':
                  var17 = new FastDatePrinter.TwentyFourHourField(this.selectNumberRule(11, var1));
                  break label99;
               case 'm':
                  var17 = this.selectNumberRule(12, var1);
                  break label99;
               case 's':
                  var17 = this.selectNumberRule(13, var1);
                  break label99;
               case 'u':
                  var17 = new FastDatePrinter.DayInWeekField(this.selectNumberRule(7, var1));
                  break label99;
               case 'w':
                  var17 = this.selectNumberRule(3, var1);
                  break label99;
               default:
                  switch(var5) {
                  case 'D':
                     var17 = this.selectNumberRule(6, var1);
                     break label99;
                  case 'E':
                     String[] var19;
                     if (var1 < 4) {
                        var19 = var11;
                     } else {
                        var19 = var7;
                     }

                     var17 = new FastDatePrinter.TextField(7, var19);
                     break label99;
                  case 'F':
                     var17 = this.selectNumberRule(8, var1);
                     break label99;
                  case 'G':
                     var17 = new FastDatePrinter.TextField(0, var9);
                     break label99;
                  case 'H':
                     var17 = this.selectNumberRule(11, var1);
                     break label99;
                  default:
                     switch(var5) {
                     case 'W':
                        var17 = this.selectNumberRule(4, var1);
                        break label99;
                     case 'X':
                        var17 = FastDatePrinter.Iso8601_Rule.getRule(var1);
                        break label99;
                     case 'Y':
                        break;
                     case 'Z':
                        if (var1 == 1) {
                           var17 = FastDatePrinter.TimeZoneNumberRule.INSTANCE_NO_COLON;
                        } else if (var1 == 2) {
                           var17 = FastDatePrinter.Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES;
                        } else {
                           var17 = FastDatePrinter.TimeZoneNumberRule.INSTANCE_COLON;
                        }
                        break label99;
                     default:
                        StringBuilder var18 = new StringBuilder();
                        var18.append("Illegal pattern component: ");
                        var18.append(var6);
                        throw new IllegalArgumentException(var18.toString());
                     }
                  }
               }
            }

            Object var8;
            if (var1 == 2) {
               var8 = FastDatePrinter.TwoDigitYearField.INSTANCE;
            } else {
               if (var1 < 4) {
                  var1 = var2;
               }

               var8 = this.selectNumberRule(1, var1);
            }

            var17 = var8;
            if (var5 == 'Y') {
               var17 = new FastDatePrinter.WeekYear((FastDatePrinter.NumberRule)var8);
            }
         }

         var12.add(var17);
      }

      return var12;
   }

   protected String parseToken(String var1, int[] var2) {
      StringBuilder var8 = new StringBuilder();
      int var4 = var2[0];
      int var7 = var1.length();
      char var3 = var1.charAt(var4);
      int var5;
      if (var3 >= 'A' && var3 <= 'Z' || var3 >= 'a' && var3 <= 'z') {
         var8.append(var3);

         while(true) {
            var5 = var4;
            if (var4 + 1 >= var7) {
               break;
            }

            var5 = var4;
            if (var1.charAt(var4 + 1) != var3) {
               break;
            }

            var8.append(var3);
            ++var4;
         }
      } else {
         var8.append('\'');
         boolean var6 = false;

         while(true) {
            var5 = var4;
            if (var4 >= var7) {
               break;
            }

            var3 = var1.charAt(var4);
            if (var3 == '\'') {
               if (var4 + 1 < var7 && var1.charAt(var4 + 1) == '\'') {
                  ++var4;
                  var8.append(var3);
               } else {
                  boolean var9;
                  if (!var6) {
                     var9 = true;
                  } else {
                     var9 = false;
                  }

                  var6 = var9;
               }
            } else {
               if (!var6 && (var3 >= 'A' && var3 <= 'Z' || var3 >= 'a' && var3 <= 'z')) {
                  var5 = var4 - 1;
                  break;
               }

               var8.append(var3);
            }

            ++var4;
         }
      }

      var2[0] = var5;
      return var8.toString();
   }

   protected FastDatePrinter.NumberRule selectNumberRule(int var1, int var2) {
      if (var2 != 1) {
         return (FastDatePrinter.NumberRule)(var2 != 2 ? new FastDatePrinter.PaddedNumberField(var1, var2) : new FastDatePrinter.TwoDigitNumberField(var1));
      } else {
         return new FastDatePrinter.UnpaddedNumberField(var1);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("FastDatePrinter[");
      var1.append(this.mPattern);
      var1.append(",");
      var1.append(this.mLocale);
      var1.append(",");
      var1.append(this.mTimeZone.getID());
      var1.append("]");
      return var1.toString();
   }

   private static class CharacterLiteral implements FastDatePrinter.Rule {
      private final char mValue;

      CharacterLiteral(char var1) {
         this.mValue = var1;
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         var1.append(this.mValue);
      }

      public int estimateLength() {
         return 1;
      }
   }

   private static class DayInWeekField implements FastDatePrinter.NumberRule {
      private final FastDatePrinter.NumberRule mRule;

      DayInWeekField(FastDatePrinter.NumberRule var1) {
         this.mRule = var1;
      }

      public void appendTo(Appendable var1, int var2) throws IOException {
         this.mRule.appendTo(var1, var2);
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         int var3 = 7;
         int var4 = var2.get(7);
         FastDatePrinter.NumberRule var5 = this.mRule;
         if (var4 != 1) {
            var3 = var4 - 1;
         }

         var5.appendTo(var1, var3);
      }

      public int estimateLength() {
         return this.mRule.estimateLength();
      }
   }

   private static class Iso8601_Rule implements FastDatePrinter.Rule {
      static final FastDatePrinter.Iso8601_Rule ISO8601_HOURS = new FastDatePrinter.Iso8601_Rule(3);
      static final FastDatePrinter.Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new FastDatePrinter.Iso8601_Rule(6);
      static final FastDatePrinter.Iso8601_Rule ISO8601_HOURS_MINUTES = new FastDatePrinter.Iso8601_Rule(5);
      final int length;

      Iso8601_Rule(int var1) {
         this.length = var1;
      }

      static FastDatePrinter.Iso8601_Rule getRule(int var0) {
         if (var0 != 1) {
            if (var0 != 2) {
               if (var0 == 3) {
                  return ISO8601_HOURS_COLON_MINUTES;
               } else {
                  throw new IllegalArgumentException("invalid number of X");
               }
            } else {
               return ISO8601_HOURS_MINUTES;
            }
         } else {
            return ISO8601_HOURS;
         }
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         int var3 = var2.get(15) + var2.get(16);
         if (var3 == 0) {
            var1.append("Z");
         } else {
            if (var3 < 0) {
               var1.append('-');
               var3 = -var3;
            } else {
               var1.append('+');
            }

            int var4 = var3 / 3600000;
            FastDatePrinter.appendDigits(var1, var4);
            int var5 = this.length;
            if (var5 >= 5) {
               if (var5 == 6) {
                  var1.append(':');
               }

               FastDatePrinter.appendDigits(var1, var3 / '\uea60' - var4 * 60);
            }
         }
      }

      public int estimateLength() {
         return this.length;
      }
   }

   private interface NumberRule extends FastDatePrinter.Rule {
      void appendTo(Appendable var1, int var2) throws IOException;
   }

   private static class PaddedNumberField implements FastDatePrinter.NumberRule {
      private final int mField;
      private final int mSize;

      PaddedNumberField(int var1, int var2) {
         if (var2 >= 3) {
            this.mField = var1;
            this.mSize = var2;
         } else {
            throw new IllegalArgumentException();
         }
      }

      public final void appendTo(Appendable var1, int var2) throws IOException {
         FastDatePrinter.appendFullDigits(var1, var2, this.mSize);
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         this.appendTo(var1, var2.get(this.mField));
      }

      public int estimateLength() {
         return this.mSize;
      }
   }

   private interface Rule {
      void appendTo(Appendable var1, Calendar var2) throws IOException;

      int estimateLength();
   }

   private static class StringLiteral implements FastDatePrinter.Rule {
      private final String mValue;

      StringLiteral(String var1) {
         this.mValue = var1;
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         var1.append(this.mValue);
      }

      public int estimateLength() {
         return this.mValue.length();
      }
   }

   private static class TextField implements FastDatePrinter.Rule {
      private final int mField;
      private final String[] mValues;

      TextField(int var1, String[] var2) {
         this.mField = var1;
         this.mValues = var2;
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         var1.append(this.mValues[var2.get(this.mField)]);
      }

      public int estimateLength() {
         int var1 = 0;
         int var2 = this.mValues.length;

         while(true) {
            int var3 = var2 - 1;
            if (var3 < 0) {
               return var1;
            }

            int var4 = this.mValues[var3].length();
            var2 = var1;
            if (var4 > var1) {
               var2 = var4;
            }

            var1 = var2;
            var2 = var3;
         }
      }
   }

   private static class TimeZoneDisplayKey {
      private final Locale mLocale;
      private final int mStyle;
      private final TimeZone mTimeZone;

      TimeZoneDisplayKey(TimeZone var1, boolean var2, int var3, Locale var4) {
         this.mTimeZone = var1;
         if (var2) {
            this.mStyle = Integer.MIN_VALUE | var3;
         } else {
            this.mStyle = var3;
         }

         this.mLocale = var4;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 instanceof FastDatePrinter.TimeZoneDisplayKey) {
            FastDatePrinter.TimeZoneDisplayKey var2 = (FastDatePrinter.TimeZoneDisplayKey)var1;
            return this.mTimeZone.equals(var2.mTimeZone) && this.mStyle == var2.mStyle && this.mLocale.equals(var2.mLocale);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
      }
   }

   private static class TimeZoneNameRule implements FastDatePrinter.Rule {
      private final String mDaylight;
      private final Locale mLocale;
      private final String mStandard;
      private final int mStyle;

      TimeZoneNameRule(TimeZone var1, Locale var2, int var3) {
         this.mLocale = var2;
         this.mStyle = var3;
         this.mStandard = FastDatePrinter.getTimeZoneDisplay(var1, false, var3, var2);
         this.mDaylight = FastDatePrinter.getTimeZoneDisplay(var1, true, var3, var2);
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         TimeZone var3 = var2.getTimeZone();
         if (var2.get(16) == 0) {
            var1.append(FastDatePrinter.getTimeZoneDisplay(var3, false, this.mStyle, this.mLocale));
         } else {
            var1.append(FastDatePrinter.getTimeZoneDisplay(var3, true, this.mStyle, this.mLocale));
         }
      }

      public int estimateLength() {
         return Math.max(this.mStandard.length(), this.mDaylight.length());
      }
   }

   private static class TimeZoneNumberRule implements FastDatePrinter.Rule {
      static final FastDatePrinter.TimeZoneNumberRule INSTANCE_COLON = new FastDatePrinter.TimeZoneNumberRule(true);
      static final FastDatePrinter.TimeZoneNumberRule INSTANCE_NO_COLON = new FastDatePrinter.TimeZoneNumberRule(false);
      final boolean mColon;

      TimeZoneNumberRule(boolean var1) {
         this.mColon = var1;
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         int var3 = var2.get(15) + var2.get(16);
         if (var3 < 0) {
            var1.append('-');
            var3 = -var3;
         } else {
            var1.append('+');
         }

         int var4 = var3 / 3600000;
         FastDatePrinter.appendDigits(var1, var4);
         if (this.mColon) {
            var1.append(':');
         }

         FastDatePrinter.appendDigits(var1, var3 / '\uea60' - var4 * 60);
      }

      public int estimateLength() {
         return 5;
      }
   }

   private static class TwelveHourField implements FastDatePrinter.NumberRule {
      private final FastDatePrinter.NumberRule mRule;

      TwelveHourField(FastDatePrinter.NumberRule var1) {
         this.mRule = var1;
      }

      public void appendTo(Appendable var1, int var2) throws IOException {
         this.mRule.appendTo(var1, var2);
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         int var4 = var2.get(10);
         int var3 = var4;
         if (var4 == 0) {
            var3 = var2.getLeastMaximum(10) + 1;
         }

         this.mRule.appendTo(var1, var3);
      }

      public int estimateLength() {
         return this.mRule.estimateLength();
      }
   }

   private static class TwentyFourHourField implements FastDatePrinter.NumberRule {
      private final FastDatePrinter.NumberRule mRule;

      TwentyFourHourField(FastDatePrinter.NumberRule var1) {
         this.mRule = var1;
      }

      public void appendTo(Appendable var1, int var2) throws IOException {
         this.mRule.appendTo(var1, var2);
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         int var4 = var2.get(11);
         int var3 = var4;
         if (var4 == 0) {
            var3 = var2.getMaximum(11) + 1;
         }

         this.mRule.appendTo(var1, var3);
      }

      public int estimateLength() {
         return this.mRule.estimateLength();
      }
   }

   private static class TwoDigitMonthField implements FastDatePrinter.NumberRule {
      static final FastDatePrinter.TwoDigitMonthField INSTANCE = new FastDatePrinter.TwoDigitMonthField();

      TwoDigitMonthField() {
      }

      public final void appendTo(Appendable var1, int var2) throws IOException {
         FastDatePrinter.appendDigits(var1, var2);
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         this.appendTo(var1, var2.get(2) + 1);
      }

      public int estimateLength() {
         return 2;
      }
   }

   private static class TwoDigitNumberField implements FastDatePrinter.NumberRule {
      private final int mField;

      TwoDigitNumberField(int var1) {
         this.mField = var1;
      }

      public final void appendTo(Appendable var1, int var2) throws IOException {
         if (var2 < 100) {
            FastDatePrinter.appendDigits(var1, var2);
         } else {
            FastDatePrinter.appendFullDigits(var1, var2, 2);
         }
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         this.appendTo(var1, var2.get(this.mField));
      }

      public int estimateLength() {
         return 2;
      }
   }

   private static class TwoDigitYearField implements FastDatePrinter.NumberRule {
      static final FastDatePrinter.TwoDigitYearField INSTANCE = new FastDatePrinter.TwoDigitYearField();

      TwoDigitYearField() {
      }

      public final void appendTo(Appendable var1, int var2) throws IOException {
         FastDatePrinter.appendDigits(var1, var2);
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         this.appendTo(var1, var2.get(1) % 100);
      }

      public int estimateLength() {
         return 2;
      }
   }

   private static class UnpaddedMonthField implements FastDatePrinter.NumberRule {
      static final FastDatePrinter.UnpaddedMonthField INSTANCE = new FastDatePrinter.UnpaddedMonthField();

      UnpaddedMonthField() {
      }

      public final void appendTo(Appendable var1, int var2) throws IOException {
         if (var2 < 10) {
            var1.append((char)(var2 + 48));
         } else {
            FastDatePrinter.appendDigits(var1, var2);
         }
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         this.appendTo(var1, var2.get(2) + 1);
      }

      public int estimateLength() {
         return 2;
      }
   }

   private static class UnpaddedNumberField implements FastDatePrinter.NumberRule {
      private final int mField;

      UnpaddedNumberField(int var1) {
         this.mField = var1;
      }

      public final void appendTo(Appendable var1, int var2) throws IOException {
         if (var2 < 10) {
            var1.append((char)(var2 + 48));
         } else if (var2 < 100) {
            FastDatePrinter.appendDigits(var1, var2);
         } else {
            FastDatePrinter.appendFullDigits(var1, var2, 1);
         }
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         this.appendTo(var1, var2.get(this.mField));
      }

      public int estimateLength() {
         return 4;
      }
   }

   private static class WeekYear implements FastDatePrinter.NumberRule {
      private final FastDatePrinter.NumberRule mRule;

      WeekYear(FastDatePrinter.NumberRule var1) {
         this.mRule = var1;
      }

      public void appendTo(Appendable var1, int var2) throws IOException {
         this.mRule.appendTo(var1, var2);
      }

      public void appendTo(Appendable var1, Calendar var2) throws IOException {
         this.mRule.appendTo(var1, var2.getWeekYear());
      }

      public int estimateLength() {
         return this.mRule.estimateLength();
      }
   }
}
