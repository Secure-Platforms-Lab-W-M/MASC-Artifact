package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastDateParser implements DateParser, Serializable {
   private static final FastDateParser.Strategy ABBREVIATED_YEAR_STRATEGY = new FastDateParser.NumberStrategy(1) {
      int modify(FastDateParser var1, int var2) {
         return var2 < 100 ? var1.adjustYear(var2) : var2;
      }
   };
   private static final FastDateParser.Strategy DAY_OF_MONTH_STRATEGY = new FastDateParser.NumberStrategy(5);
   private static final FastDateParser.Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new FastDateParser.NumberStrategy(8);
   private static final FastDateParser.Strategy DAY_OF_WEEK_STRATEGY = new FastDateParser.NumberStrategy(7) {
      int modify(FastDateParser var1, int var2) {
         return var2 == 7 ? 1 : var2 + 1;
      }
   };
   private static final FastDateParser.Strategy DAY_OF_YEAR_STRATEGY = new FastDateParser.NumberStrategy(6);
   private static final FastDateParser.Strategy HOUR12_STRATEGY = new FastDateParser.NumberStrategy(10) {
      int modify(FastDateParser var1, int var2) {
         return var2 == 12 ? 0 : var2;
      }
   };
   private static final FastDateParser.Strategy HOUR24_OF_DAY_STRATEGY = new FastDateParser.NumberStrategy(11) {
      int modify(FastDateParser var1, int var2) {
         return var2 == 24 ? 0 : var2;
      }
   };
   private static final FastDateParser.Strategy HOUR_OF_DAY_STRATEGY = new FastDateParser.NumberStrategy(11);
   private static final FastDateParser.Strategy HOUR_STRATEGY = new FastDateParser.NumberStrategy(10);
   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
   private static final FastDateParser.Strategy LITERAL_YEAR_STRATEGY = new FastDateParser.NumberStrategy(1);
   private static final Comparator LONGER_FIRST_LOWERCASE = new Comparator() {
      public int compare(String var1, String var2) {
         return var2.compareTo(var1);
      }
   };
   private static final FastDateParser.Strategy MILLISECOND_STRATEGY = new FastDateParser.NumberStrategy(14);
   private static final FastDateParser.Strategy MINUTE_STRATEGY = new FastDateParser.NumberStrategy(12);
   private static final FastDateParser.Strategy NUMBER_MONTH_STRATEGY = new FastDateParser.NumberStrategy(2) {
      int modify(FastDateParser var1, int var2) {
         return var2 - 1;
      }
   };
   private static final FastDateParser.Strategy SECOND_STRATEGY = new FastDateParser.NumberStrategy(13);
   private static final FastDateParser.Strategy WEEK_OF_MONTH_STRATEGY = new FastDateParser.NumberStrategy(4);
   private static final FastDateParser.Strategy WEEK_OF_YEAR_STRATEGY = new FastDateParser.NumberStrategy(3);
   private static final ConcurrentMap[] caches = new ConcurrentMap[17];
   private static final long serialVersionUID = 3L;
   private final int century;
   private final Locale locale;
   private final String pattern;
   private transient List patterns;
   private final int startYear;
   private final TimeZone timeZone;

   protected FastDateParser(String var1, TimeZone var2, Locale var3) {
      this(var1, var2, var3, (Date)null);
   }

   protected FastDateParser(String var1, TimeZone var2, Locale var3, Date var4) {
      this.pattern = var1;
      this.timeZone = var2;
      this.locale = var3;
      Calendar var7 = Calendar.getInstance(var2, var3);
      int var5;
      if (var4 != null) {
         var7.setTime(var4);
         var5 = var7.get(1);
      } else if (var3.equals(JAPANESE_IMPERIAL)) {
         var5 = 0;
      } else {
         var7.setTime(new Date());
         var5 = var7.get(1) - 80;
      }

      int var6 = var5 / 100 * 100;
      this.century = var6;
      this.startYear = var5 - var6;
      this.init(var7);
   }

   private int adjustYear(int var1) {
      int var2 = this.century + var1;
      return var1 >= this.startYear ? var2 : var2 + 100;
   }

   private static Map appendDisplayNames(Calendar var0, Locale var1, int var2, StringBuilder var3) {
      HashMap var4 = new HashMap();
      Map var5 = var0.getDisplayNames(var2, 0, var1);
      TreeSet var8 = new TreeSet(LONGER_FIRST_LOWERCASE);
      Iterator var10 = var5.entrySet().iterator();

      while(var10.hasNext()) {
         Entry var6 = (Entry)var10.next();
         String var7 = ((String)var6.getKey()).toLowerCase(var1);
         if (var8.add(var7)) {
            var4.put(var7, (Integer)var6.getValue());
         }
      }

      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         simpleQuote(var3, (String)var9.next()).append('|');
      }

      return var4;
   }

   private static ConcurrentMap getCache(int var0) {
      ConcurrentMap[] var1 = caches;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (caches[var0] == null) {
               caches[var0] = new ConcurrentHashMap(3);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            ConcurrentMap var15 = caches[var0];
            return var15;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   private FastDateParser.Strategy getLocaleSpecificStrategy(int var1, Calendar var2) {
      ConcurrentMap var5 = getCache(var1);
      FastDateParser.Strategy var4 = (FastDateParser.Strategy)var5.get(this.locale);
      Object var3 = var4;
      if (var4 == null) {
         Object var6;
         if (var1 == 15) {
            var6 = new FastDateParser.TimeZoneStrategy(this.locale);
         } else {
            var6 = new FastDateParser.CaseInsensitiveTextStrategy(var1, var2, this.locale);
         }

         var4 = (FastDateParser.Strategy)var5.putIfAbsent(this.locale, var6);
         var3 = var6;
         if (var4 != null) {
            return var4;
         }
      }

      return (FastDateParser.Strategy)var3;
   }

   private FastDateParser.Strategy getStrategy(char var1, int var2, Calendar var3) {
      if (var1 != 'y') {
         label61: {
            if (var1 != 'z') {
               switch(var1) {
               case 'D':
                  return DAY_OF_YEAR_STRATEGY;
               case 'E':
                  return this.getLocaleSpecificStrategy(7, var3);
               case 'F':
                  return DAY_OF_WEEK_IN_MONTH_STRATEGY;
               case 'G':
                  return this.getLocaleSpecificStrategy(0, var3);
               case 'H':
                  return HOUR_OF_DAY_STRATEGY;
               default:
                  switch(var1) {
                  case 'K':
                     return HOUR_STRATEGY;
                  case 'M':
                     if (var2 >= 3) {
                        return this.getLocaleSpecificStrategy(2, var3);
                     }

                     return NUMBER_MONTH_STRATEGY;
                  case 'S':
                     return MILLISECOND_STRATEGY;
                  case 'a':
                     return this.getLocaleSpecificStrategy(9, var3);
                  case 'd':
                     return DAY_OF_MONTH_STRATEGY;
                  case 'h':
                     return HOUR12_STRATEGY;
                  case 'k':
                     return HOUR24_OF_DAY_STRATEGY;
                  case 'm':
                     return MINUTE_STRATEGY;
                  case 's':
                     return SECOND_STRATEGY;
                  case 'u':
                     return DAY_OF_WEEK_STRATEGY;
                  case 'w':
                     return WEEK_OF_YEAR_STRATEGY;
                  default:
                     switch(var1) {
                     case 'W':
                        return WEEK_OF_MONTH_STRATEGY;
                     case 'X':
                        return FastDateParser.ISO8601TimeZoneStrategy.getStrategy(var2);
                     case 'Y':
                        break label61;
                     case 'Z':
                        if (var2 == 2) {
                           return FastDateParser.ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;
                        }
                        break;
                     default:
                        StringBuilder var4 = new StringBuilder();
                        var4.append("Format '");
                        var4.append(var1);
                        var4.append("' not supported");
                        throw new IllegalArgumentException(var4.toString());
                     }
                  }
               }
            }

            return this.getLocaleSpecificStrategy(15, var3);
         }
      }

      if (var2 > 2) {
         return LITERAL_YEAR_STRATEGY;
      } else {
         return ABBREVIATED_YEAR_STRATEGY;
      }
   }

   private void init(Calendar var1) {
      this.patterns = new ArrayList();
      FastDateParser.StrategyParser var3 = new FastDateParser.StrategyParser(var1);

      while(true) {
         FastDateParser.StrategyAndWidth var2 = var3.getNextStrategy();
         if (var2 == null) {
            return;
         }

         this.patterns.add(var2);
      }
   }

   private static boolean isFormatLetter(char var0) {
      return var0 >= 'A' && var0 <= 'Z' || var0 >= 'a' && var0 <= 'z';
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.init(Calendar.getInstance(this.timeZone, this.locale));
   }

   private static StringBuilder simpleQuote(StringBuilder var0, String var1) {
      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var2;
         label38: {
            var2 = var1.charAt(var3);
            if (var2 != '$' && var2 != '.' && var2 != '?' && var2 != '^' && var2 != '[' && var2 != '\\' && var2 != '{' && var2 != '|') {
               switch(var2) {
               case '(':
               case ')':
               case '*':
               case '+':
                  break;
               default:
                  break label38;
               }
            }

            var0.append('\\');
         }

         var0.append(var2);
      }

      if (var0.charAt(var0.length() - 1) == '.') {
         var0.append('?');
      }

      return var0;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof FastDateParser)) {
         return false;
      } else {
         FastDateParser var2 = (FastDateParser)var1;
         return this.pattern.equals(var2.pattern) && this.timeZone.equals(var2.timeZone) && this.locale.equals(var2.locale);
      }
   }

   public Locale getLocale() {
      return this.locale;
   }

   public String getPattern() {
      return this.pattern;
   }

   public TimeZone getTimeZone() {
      return this.timeZone;
   }

   public int hashCode() {
      return this.pattern.hashCode() + (this.timeZone.hashCode() + this.locale.hashCode() * 13) * 13;
   }

   public Date parse(String var1) throws ParseException {
      ParsePosition var2 = new ParsePosition(0);
      Date var3 = this.parse(var1, var2);
      if (var3 == null) {
         StringBuilder var4;
         if (this.locale.equals(JAPANESE_IMPERIAL)) {
            var4 = new StringBuilder();
            var4.append("(The ");
            var4.append(this.locale);
            var4.append(" locale does not support dates before 1868 AD)\nUnparseable date: \"");
            var4.append(var1);
            throw new ParseException(var4.toString(), var2.getErrorIndex());
         } else {
            var4 = new StringBuilder();
            var4.append("Unparseable date: ");
            var4.append(var1);
            throw new ParseException(var4.toString(), var2.getErrorIndex());
         }
      } else {
         return var3;
      }
   }

   public Date parse(String var1, ParsePosition var2) {
      Calendar var3 = Calendar.getInstance(this.timeZone, this.locale);
      var3.clear();
      return this.parse(var1, var2, var3) ? var3.getTime() : null;
   }

   public boolean parse(String var1, ParsePosition var2, Calendar var3) {
      ListIterator var5 = this.patterns.listIterator();

      int var4;
      FastDateParser.StrategyAndWidth var6;
      do {
         if (!var5.hasNext()) {
            return true;
         }

         var6 = (FastDateParser.StrategyAndWidth)var5.next();
         var4 = var6.getMaxWidth(var5);
      } while(var6.strategy.parse(this, var3, var1, var2, var4));

      return false;
   }

   public Object parseObject(String var1) throws ParseException {
      return this.parse(var1);
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.parse(var1, var2);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("FastDateParser[");
      var1.append(this.pattern);
      var1.append(",");
      var1.append(this.locale);
      var1.append(",");
      var1.append(this.timeZone.getID());
      var1.append("]");
      return var1.toString();
   }

   private static class CaseInsensitiveTextStrategy extends FastDateParser.PatternStrategy {
      private final int field;
      private final Map lKeyValues;
      final Locale locale;

      CaseInsensitiveTextStrategy(int var1, Calendar var2, Locale var3) {
         super(null);
         this.field = var1;
         this.locale = var3;
         StringBuilder var4 = new StringBuilder();
         var4.append("((?iu)");
         this.lKeyValues = FastDateParser.appendDisplayNames(var2, var3, var1, var4);
         var4.setLength(var4.length() - 1);
         var4.append(")");
         this.createPattern(var4);
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         String var4 = var3.toLowerCase(this.locale);
         Integer var7 = (Integer)this.lKeyValues.get(var4);
         Integer var5 = var7;
         if (var7 == null) {
            Map var6 = this.lKeyValues;
            StringBuilder var8 = new StringBuilder();
            var8.append(var4);
            var8.append('.');
            var5 = (Integer)var6.get(var8.toString());
         }

         var2.set(this.field, var5);
      }
   }

   private static class CopyQuotedStrategy extends FastDateParser.Strategy {
      private final String formatField;

      CopyQuotedStrategy(String var1) {
         super(null);
         this.formatField = var1;
      }

      boolean isNumber() {
         return false;
      }

      boolean parse(FastDateParser var1, Calendar var2, String var3, ParsePosition var4, int var5) {
         for(var5 = 0; var5 < this.formatField.length(); ++var5) {
            int var6 = var4.getIndex() + var5;
            if (var6 == var3.length()) {
               var4.setErrorIndex(var6);
               return false;
            }

            if (this.formatField.charAt(var5) != var3.charAt(var6)) {
               var4.setErrorIndex(var6);
               return false;
            }
         }

         var4.setIndex(this.formatField.length() + var4.getIndex());
         return true;
      }
   }

   private static class ISO8601TimeZoneStrategy extends FastDateParser.PatternStrategy {
      private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new FastDateParser.ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
      private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new FastDateParser.ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
      private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new FastDateParser.ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");

      ISO8601TimeZoneStrategy(String var1) {
         super(null);
         this.createPattern(var1);
      }

      static FastDateParser.Strategy getStrategy(int var0) {
         if (var0 != 1) {
            if (var0 != 2) {
               if (var0 == 3) {
                  return ISO_8601_3_STRATEGY;
               } else {
                  throw new IllegalArgumentException("invalid number of X");
               }
            } else {
               return ISO_8601_2_STRATEGY;
            }
         } else {
            return ISO_8601_1_STRATEGY;
         }
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         var2.setTimeZone(FastTimeZone.getGmtTimeZone(var3));
      }
   }

   private static class NumberStrategy extends FastDateParser.Strategy {
      private final int field;

      NumberStrategy(int var1) {
         super(null);
         this.field = var1;
      }

      boolean isNumber() {
         return true;
      }

      int modify(FastDateParser var1, int var2) {
         return var2;
      }

      boolean parse(FastDateParser var1, Calendar var2, String var3, ParsePosition var4, int var5) {
         int var6 = var4.getIndex();
         int var8 = var3.length();
         int var7;
         if (var5 == 0) {
            for(var5 = var6; var5 < var8 && Character.isWhitespace(var3.charAt(var5)); ++var5) {
            }

            var4.setIndex(var5);
            var7 = var8;
         } else {
            int var9 = var6 + var5;
            var5 = var6;
            var7 = var8;
            if (var8 > var9) {
               var7 = var9;
               var5 = var6;
            }
         }

         while(var5 < var7 && Character.isDigit(var3.charAt(var5))) {
            ++var5;
         }

         if (var4.getIndex() == var5) {
            var4.setErrorIndex(var5);
            return false;
         } else {
            var6 = Integer.parseInt(var3.substring(var4.getIndex(), var5));
            var4.setIndex(var5);
            var2.set(this.field, this.modify(var1, var6));
            return true;
         }
      }
   }

   private abstract static class PatternStrategy extends FastDateParser.Strategy {
      private Pattern pattern;

      private PatternStrategy() {
         super(null);
      }

      // $FF: synthetic method
      PatternStrategy(Object var1) {
         this();
      }

      void createPattern(String var1) {
         this.pattern = Pattern.compile(var1);
      }

      void createPattern(StringBuilder var1) {
         this.createPattern(var1.toString());
      }

      boolean isNumber() {
         return false;
      }

      boolean parse(FastDateParser var1, Calendar var2, String var3, ParsePosition var4, int var5) {
         Matcher var6 = this.pattern.matcher(var3.substring(var4.getIndex()));
         if (!var6.lookingAt()) {
            var4.setErrorIndex(var4.getIndex());
            return false;
         } else {
            var4.setIndex(var4.getIndex() + var6.end(1));
            this.setCalendar(var1, var2, var6.group(1));
            return true;
         }
      }

      abstract void setCalendar(FastDateParser var1, Calendar var2, String var3);
   }

   private abstract static class Strategy {
      private Strategy() {
      }

      // $FF: synthetic method
      Strategy(Object var1) {
         this();
      }

      boolean isNumber() {
         return false;
      }

      abstract boolean parse(FastDateParser var1, Calendar var2, String var3, ParsePosition var4, int var5);
   }

   private static class StrategyAndWidth {
      final FastDateParser.Strategy strategy;
      final int width;

      StrategyAndWidth(FastDateParser.Strategy var1, int var2) {
         this.strategy = var1;
         this.width = var2;
      }

      int getMaxWidth(ListIterator var1) {
         boolean var3 = this.strategy.isNumber();
         int var2 = 0;
         if (var3) {
            if (!var1.hasNext()) {
               return 0;
            } else {
               FastDateParser.Strategy var4 = ((FastDateParser.StrategyAndWidth)var1.next()).strategy;
               var1.previous();
               if (var4.isNumber()) {
                  var2 = this.width;
               }

               return var2;
            }
         } else {
            return 0;
         }
      }
   }

   private class StrategyParser {
      private int currentIdx;
      private final Calendar definingCalendar;

      StrategyParser(Calendar var2) {
         this.definingCalendar = var2;
      }

      private FastDateParser.StrategyAndWidth letterPattern(char var1) {
         int var2 = this.currentIdx;

         int var3;
         do {
            var3 = this.currentIdx + 1;
            this.currentIdx = var3;
         } while(var3 < FastDateParser.this.pattern.length() && FastDateParser.this.pattern.charAt(this.currentIdx) == var1);

         var2 = this.currentIdx - var2;
         return new FastDateParser.StrategyAndWidth(FastDateParser.this.getStrategy(var1, var2, this.definingCalendar), var2);
      }

      private FastDateParser.StrategyAndWidth literal() {
         boolean var2 = false;
         StringBuilder var5 = new StringBuilder();

         while(this.currentIdx < FastDateParser.this.pattern.length()) {
            char var1 = FastDateParser.this.pattern.charAt(this.currentIdx);
            if (!var2 && FastDateParser.isFormatLetter(var1)) {
               break;
            }

            boolean var3 = true;
            if (var1 == '\'') {
               int var4 = this.currentIdx + 1;
               this.currentIdx = var4;
               if (var4 == FastDateParser.this.pattern.length() || FastDateParser.this.pattern.charAt(this.currentIdx) != '\'') {
                  if (!var2) {
                     var2 = var3;
                  } else {
                     var2 = false;
                  }
                  continue;
               }
            }

            ++this.currentIdx;
            var5.append(var1);
         }

         if (!var2) {
            String var6 = var5.toString();
            return new FastDateParser.StrategyAndWidth(new FastDateParser.CopyQuotedStrategy(var6), var6.length());
         } else {
            throw new IllegalArgumentException("Unterminated quote");
         }
      }

      FastDateParser.StrategyAndWidth getNextStrategy() {
         if (this.currentIdx >= FastDateParser.this.pattern.length()) {
            return null;
         } else {
            char var1 = FastDateParser.this.pattern.charAt(this.currentIdx);
            return FastDateParser.isFormatLetter(var1) ? this.letterPattern(var1) : this.literal();
         }
      }
   }

   static class TimeZoneStrategy extends FastDateParser.PatternStrategy {
      private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
      // $FF: renamed from: ID int
      private static final int field_30 = 0;
      private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
      private final Locale locale;
      private final Map tzNames = new HashMap();

      TimeZoneStrategy(Locale var1) {
         super(null);
         this.locale = var1;
         StringBuilder var7 = new StringBuilder();
         var7.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
         TreeSet var8 = new TreeSet(FastDateParser.LONGER_FIRST_LOWERCASE);
         String[][] var9 = DateFormatSymbols.getInstance(var1).getZoneStrings();
         int var4 = var9.length;

         String var5;
         for(int var2 = 0; var2 < var4; ++var2) {
            String[] var10 = var9[var2];
            var5 = var10[0];
            if (!var5.equalsIgnoreCase("GMT")) {
               TimeZone var11 = TimeZone.getTimeZone(var5);
               FastDateParser.TimeZoneStrategy.TzInfo var6 = new FastDateParser.TimeZoneStrategy.TzInfo(var11, false);
               FastDateParser.TimeZoneStrategy.TzInfo var14 = var6;

               for(int var3 = 1; var3 < var10.length; ++var3) {
                  if (var3 != 3) {
                     if (var3 == 5) {
                        var14 = var6;
                     }
                  } else {
                     var14 = new FastDateParser.TimeZoneStrategy.TzInfo(var11, true);
                  }

                  if (var10[var3] != null) {
                     String var12 = var10[var3].toLowerCase(var1);
                     if (var8.add(var12)) {
                        this.tzNames.put(var12, var14);
                     }
                  }
               }
            }
         }

         Iterator var13 = var8.iterator();

         while(var13.hasNext()) {
            var5 = (String)var13.next();
            var7.append('|');
            FastDateParser.simpleQuote(var7, var5);
         }

         var7.append(")");
         this.createPattern(var7);
      }

      void setCalendar(FastDateParser var1, Calendar var2, String var3) {
         TimeZone var5 = FastTimeZone.getGmtTimeZone(var3);
         if (var5 != null) {
            var2.setTimeZone(var5);
         } else {
            String var4 = var3.toLowerCase(this.locale);
            FastDateParser.TimeZoneStrategy.TzInfo var8 = (FastDateParser.TimeZoneStrategy.TzInfo)this.tzNames.get(var4);
            FastDateParser.TimeZoneStrategy.TzInfo var6 = var8;
            if (var8 == null) {
               Map var7 = this.tzNames;
               StringBuilder var9 = new StringBuilder();
               var9.append(var4);
               var9.append('.');
               var6 = (FastDateParser.TimeZoneStrategy.TzInfo)var7.get(var9.toString());
            }

            var2.set(16, var6.dstOffset);
            var2.set(15, var6.zone.getRawOffset());
         }
      }

      private static class TzInfo {
         int dstOffset;
         TimeZone zone;

         TzInfo(TimeZone var1, boolean var2) {
            this.zone = var1;
            int var3;
            if (var2) {
               var3 = var1.getDSTSavings();
            } else {
               var3 = 0;
            }

            this.dstOffset = var3;
         }
      }
   }
}
