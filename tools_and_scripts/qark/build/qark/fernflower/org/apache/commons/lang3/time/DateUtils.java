package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;

public class DateUtils {
   public static final long MILLIS_PER_DAY = 86400000L;
   public static final long MILLIS_PER_HOUR = 3600000L;
   public static final long MILLIS_PER_MINUTE = 60000L;
   public static final long MILLIS_PER_SECOND = 1000L;
   public static final int RANGE_MONTH_MONDAY = 6;
   public static final int RANGE_MONTH_SUNDAY = 5;
   public static final int RANGE_WEEK_CENTER = 4;
   public static final int RANGE_WEEK_MONDAY = 2;
   public static final int RANGE_WEEK_RELATIVE = 3;
   public static final int RANGE_WEEK_SUNDAY = 1;
   public static final int SEMI_MONTH = 1001;
   private static final int[][] fields;

   static {
      int[] var0 = new int[]{14};
      int[] var1 = new int[]{12};
      int[] var2 = new int[]{11, 10};
      int[] var3 = new int[]{2, 1001};
      fields = new int[][]{var0, {13}, var1, var2, {5, 5, 9}, var3, {1}, {0}};
   }

   private static Date add(Date var0, int var1, int var2) {
      validateDateNotNull(var0);
      Calendar var3 = Calendar.getInstance();
      var3.setTime(var0);
      var3.add(var1, var2);
      return var3.getTime();
   }

   public static Date addDays(Date var0, int var1) {
      return add(var0, 5, var1);
   }

   public static Date addHours(Date var0, int var1) {
      return add(var0, 11, var1);
   }

   public static Date addMilliseconds(Date var0, int var1) {
      return add(var0, 14, var1);
   }

   public static Date addMinutes(Date var0, int var1) {
      return add(var0, 12, var1);
   }

   public static Date addMonths(Date var0, int var1) {
      return add(var0, 2, var1);
   }

   public static Date addSeconds(Date var0, int var1) {
      return add(var0, 13, var1);
   }

   public static Date addWeeks(Date var0, int var1) {
      return add(var0, 3, var1);
   }

   public static Date addYears(Date var0, int var1) {
      return add(var0, 1, var1);
   }

   public static Calendar ceiling(Calendar var0, int var1) {
      if (var0 != null) {
         var0 = (Calendar)var0.clone();
         modify(var0, var1, DateUtils.ModifyType.CEILING);
         return var0;
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Date ceiling(Object var0, int var1) {
      if (var0 != null) {
         if (var0 instanceof Date) {
            return ceiling((Date)var0, var1);
         } else if (var0 instanceof Calendar) {
            return ceiling((Calendar)var0, var1).getTime();
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Could not find ceiling of for type: ");
            var2.append(var0.getClass());
            throw new ClassCastException(var2.toString());
         }
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Date ceiling(Date var0, int var1) {
      validateDateNotNull(var0);
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var0);
      modify(var2, var1, DateUtils.ModifyType.CEILING);
      return var2.getTime();
   }

   private static long getFragment(Calendar var0, int var1, TimeUnit var2) {
      if (var0 != null) {
         long var4 = 0L;
         byte var3;
         if (var2 == TimeUnit.DAYS) {
            var3 = 0;
         } else {
            var3 = 1;
         }

         if (var1 != 1) {
            if (var1 == 2) {
               var4 = 0L + var2.convert((long)(var0.get(5) - var3), TimeUnit.DAYS);
            }
         } else {
            var4 = 0L + var2.convert((long)(var0.get(6) - var3), TimeUnit.DAYS);
         }

         long var8;
         long var10;
         label60: {
            long var6;
            if (var1 != 1 && var1 != 2 && var1 != 5 && var1 != 6) {
               var6 = var4;
               var8 = var4;
               var10 = var4;
               switch(var1) {
               case 11:
                  break;
               case 12:
                  break label60;
               case 13:
                  return var10 + var2.convert((long)var0.get(14), TimeUnit.MILLISECONDS);
               case 14:
                  return var4;
               default:
                  StringBuilder var12 = new StringBuilder();
                  var12.append("The fragment ");
                  var12.append(var1);
                  var12.append(" is not supported");
                  throw new IllegalArgumentException(var12.toString());
               }
            } else {
               var6 = var4 + var2.convert((long)var0.get(11), TimeUnit.HOURS);
            }

            var8 = var6 + var2.convert((long)var0.get(12), TimeUnit.MINUTES);
         }

         var10 = var8 + var2.convert((long)var0.get(13), TimeUnit.SECONDS);
         return var10 + var2.convert((long)var0.get(14), TimeUnit.MILLISECONDS);
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   private static long getFragment(Date var0, int var1, TimeUnit var2) {
      validateDateNotNull(var0);
      Calendar var3 = Calendar.getInstance();
      var3.setTime(var0);
      return getFragment(var3, var1, var2);
   }

   public static long getFragmentInDays(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.DAYS);
   }

   public static long getFragmentInDays(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.DAYS);
   }

   public static long getFragmentInHours(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.HOURS);
   }

   public static long getFragmentInHours(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.HOURS);
   }

   public static long getFragmentInMilliseconds(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.MILLISECONDS);
   }

   public static long getFragmentInMilliseconds(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.MILLISECONDS);
   }

   public static long getFragmentInMinutes(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.MINUTES);
   }

   public static long getFragmentInMinutes(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.MINUTES);
   }

   public static long getFragmentInSeconds(Calendar var0, int var1) {
      return getFragment(var0, var1, TimeUnit.SECONDS);
   }

   public static long getFragmentInSeconds(Date var0, int var1) {
      return getFragment(var0, var1, TimeUnit.SECONDS);
   }

   public static boolean isSameDay(Calendar var0, Calendar var1) {
      if (var0 != null && var1 != null) {
         return var0.get(0) == var1.get(0) && var0.get(1) == var1.get(1) && var0.get(6) == var1.get(6);
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static boolean isSameDay(Date var0, Date var1) {
      if (var0 != null && var1 != null) {
         Calendar var2 = Calendar.getInstance();
         var2.setTime(var0);
         Calendar var3 = Calendar.getInstance();
         var3.setTime(var1);
         return isSameDay(var2, var3);
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static boolean isSameInstant(Calendar var0, Calendar var1) {
      if (var0 != null && var1 != null) {
         return var0.getTime().getTime() == var1.getTime().getTime();
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static boolean isSameInstant(Date var0, Date var1) {
      if (var0 != null && var1 != null) {
         return var0.getTime() == var1.getTime();
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static boolean isSameLocalTime(Calendar var0, Calendar var1) {
      if (var0 != null && var1 != null) {
         return var0.get(14) == var1.get(14) && var0.get(13) == var1.get(13) && var0.get(12) == var1.get(12) && var0.get(11) == var1.get(11) && var0.get(6) == var1.get(6) && var0.get(1) == var1.get(1) && var0.get(0) == var1.get(0) && var0.getClass() == var1.getClass();
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Iterator iterator(Object var0, int var1) {
      if (var0 != null) {
         if (var0 instanceof Date) {
            return iterator((Date)var0, var1);
         } else if (var0 instanceof Calendar) {
            return iterator((Calendar)var0, var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Could not iterate based on ");
            var2.append(var0);
            throw new ClassCastException(var2.toString());
         }
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Iterator iterator(Calendar var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("The date must not be null");
      } else {
         int var3 = 1;
         int var2 = 7;
         Calendar var4;
         Calendar var5;
         switch(var1) {
         case 1:
         case 2:
         case 3:
         case 4:
            var5 = truncate((Calendar)var0, 5);
            var4 = truncate((Calendar)var0, 5);
            if (var1 != 2) {
               if (var1 != 3) {
                  if (var1 != 4) {
                     var0 = var5;
                  } else {
                     var3 = var0.get(7) - 3;
                     var2 = var0.get(7) + 3;
                     var0 = var5;
                  }
               } else {
                  var3 = var0.get(7);
                  var2 = var3 - 1;
                  var0 = var5;
               }
            } else {
               var3 = 2;
               var2 = 1;
               var0 = var5;
            }
            break;
         case 5:
         case 6:
            var5 = truncate((Calendar)var0, 2);
            Calendar var6 = (Calendar)var5.clone();
            var6.add(2, 1);
            var6.add(5, -1);
            var0 = var5;
            var4 = var6;
            if (var1 == 6) {
               var3 = 2;
               var2 = 1;
               var0 = var5;
               var4 = var6;
            }
            break;
         default:
            StringBuilder var7 = new StringBuilder();
            var7.append("The range style ");
            var7.append(var1);
            var7.append(" is not valid.");
            throw new IllegalArgumentException(var7.toString());
         }

         var1 = var3;
         if (var3 < 1) {
            var1 = var3 + 7;
         }

         var3 = var1;
         if (var1 > 7) {
            var3 = var1 - 7;
         }

         var1 = var2;
         if (var2 < 1) {
            var1 = var2 + 7;
         }

         var2 = var1;
         if (var1 > 7) {
            var2 = var1 - 7;
         }

         while(var0.get(7) != var3) {
            var0.add(5, -1);
         }

         while(var4.get(7) != var2) {
            var4.add(5, 1);
         }

         return new DateUtils.DateIterator(var0, var4);
      }
   }

   public static Iterator iterator(Date var0, int var1) {
      validateDateNotNull(var0);
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var0);
      return iterator(var2, var1);
   }

   private static void modify(Calendar var0, int var1, DateUtils.ModifyType var2) {
      if (var0.get(1) <= 280000000) {
         if (var1 != 14) {
            boolean var3;
            long var9;
            long var11;
            Date var13;
            label140: {
               var13 = var0.getTime();
               var11 = var13.getTime();
               var3 = false;
               int var7 = var0.get(14);
               if (DateUtils.ModifyType.TRUNCATE != var2) {
                  var9 = var11;
                  if (var7 >= 500) {
                     break label140;
                  }
               }

               var9 = var11 - (long)var7;
            }

            if (var1 == 13) {
               var3 = true;
            }

            int var4 = var0.get(13);
            var11 = var9;
            if (!var3) {
               label133: {
                  if (DateUtils.ModifyType.TRUNCATE != var2) {
                     var11 = var9;
                     if (var4 >= 30) {
                        break label133;
                     }
                  }

                  var11 = var9 - (long)var4 * 1000L;
               }
            }

            if (var1 == 12) {
               var3 = true;
            }

            var4 = var0.get(12);
            var9 = var11;
            if (!var3) {
               label126: {
                  if (DateUtils.ModifyType.TRUNCATE != var2) {
                     var9 = var11;
                     if (var4 >= 30) {
                        break label126;
                     }
                  }

                  var9 = var11 - (long)var4 * 60000L;
               }
            }

            if (var13.getTime() != var9) {
               var13.setTime(var9);
               var0.setTime(var13);
            }

            boolean var18 = false;
            int[][] var15 = fields;
            int var8 = var15.length;

            for(int var6 = 0; var6 < var8; ++var6) {
               int[] var16 = var15[var6];
               int var5 = var16.length;

               int var19;
               for(var19 = 0; var19 < var5; ++var19) {
                  if (var16[var19] == var1) {
                     if (var2 == DateUtils.ModifyType.CEILING || var2 == DateUtils.ModifyType.ROUND && var18) {
                        if (var1 == 1001) {
                           if (var0.get(5) == 1) {
                              var0.add(5, 15);
                              return;
                           }

                           var0.add(5, -15);
                           var0.add(2, 1);
                           return;
                        }

                        if (var1 == 9) {
                           if (var0.get(11) == 0) {
                              var0.add(11, 12);
                              return;
                           }

                           var0.add(11, -12);
                           var0.add(5, 1);
                           return;
                        }

                        var0.add(var16[0], 1);
                     }

                     return;
                  }
               }

               var19 = 0;
               boolean var20 = false;
               if (var1 != 9) {
                  if (var1 == 1001 && var16[0] == 5) {
                     var19 = var0.get(5) - 1;
                     if (var19 >= 15) {
                        var19 -= 15;
                     }

                     if (var19 > 7) {
                        var18 = true;
                     } else {
                        var18 = false;
                     }

                     var20 = true;
                  }
               } else if (var16[0] == 11) {
                  var19 = var0.get(11);
                  if (var19 >= 12) {
                     var19 -= 12;
                  }

                  if (var19 >= 6) {
                     var18 = true;
                  } else {
                     var18 = false;
                  }

                  var20 = true;
               }

               if (!var20) {
                  var19 = var0.getActualMinimum(var16[0]);
                  var5 = var0.getActualMaximum(var16[0]);
                  var4 = var0.get(var16[0]) - var19;
                  if (var4 > (var5 - var19) / 2) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  var5 = var4;
                  var18 = var3;
               } else {
                  var5 = var19;
               }

               if (var5 != 0) {
                  var0.set(var16[0], var0.get(var16[0]) - var5);
               }
            }

            StringBuilder var17 = new StringBuilder();
            var17.append("The field ");
            var17.append(var1);
            var17.append(" is not supported");
            throw new IllegalArgumentException(var17.toString());
         }
      } else {
         throw new ArithmeticException("Calendar value too large for accurate calculations");
      }
   }

   public static Date parseDate(String var0, Locale var1, String... var2) throws ParseException {
      return parseDateWithLeniency(var0, var1, var2, true);
   }

   public static Date parseDate(String var0, String... var1) throws ParseException {
      return parseDate(var0, (Locale)null, var1);
   }

   public static Date parseDateStrictly(String var0, Locale var1, String... var2) throws ParseException {
      return parseDateWithLeniency(var0, var1, var2, false);
   }

   public static Date parseDateStrictly(String var0, String... var1) throws ParseException {
      return parseDateStrictly(var0, (Locale)null, var1);
   }

   private static Date parseDateWithLeniency(String var0, Locale var1, String[] var2, boolean var3) throws ParseException {
      if (var0 != null && var2 != null) {
         TimeZone var6 = TimeZone.getDefault();
         if (var1 == null) {
            var1 = Locale.getDefault();
         }

         ParsePosition var7 = new ParsePosition(0);
         Calendar var8 = Calendar.getInstance(var6, var1);
         var8.setLenient(var3);
         int var5 = var2.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            FastDateParser var9 = new FastDateParser(var2[var4], var6, var1);
            var8.clear();

            try {
               if (var9.parse(var0, var7, var8) && var7.getIndex() == var0.length()) {
                  Date var12 = var8.getTime();
                  return var12;
               }
            } catch (IllegalArgumentException var10) {
            }

            var7.setIndex(0);
         }

         StringBuilder var11 = new StringBuilder();
         var11.append("Unable to parse the date: ");
         var11.append(var0);
         throw new ParseException(var11.toString(), -1);
      } else {
         throw new IllegalArgumentException("Date and Patterns must not be null");
      }
   }

   public static Calendar round(Calendar var0, int var1) {
      if (var0 != null) {
         var0 = (Calendar)var0.clone();
         modify(var0, var1, DateUtils.ModifyType.ROUND);
         return var0;
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Date round(Object var0, int var1) {
      if (var0 != null) {
         if (var0 instanceof Date) {
            return round((Date)var0, var1);
         } else if (var0 instanceof Calendar) {
            return round((Calendar)var0, var1).getTime();
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Could not round ");
            var2.append(var0);
            throw new ClassCastException(var2.toString());
         }
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Date round(Date var0, int var1) {
      validateDateNotNull(var0);
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var0);
      modify(var2, var1, DateUtils.ModifyType.ROUND);
      return var2.getTime();
   }

   private static Date set(Date var0, int var1, int var2) {
      validateDateNotNull(var0);
      Calendar var3 = Calendar.getInstance();
      var3.setLenient(false);
      var3.setTime(var0);
      var3.set(var1, var2);
      return var3.getTime();
   }

   public static Date setDays(Date var0, int var1) {
      return set(var0, 5, var1);
   }

   public static Date setHours(Date var0, int var1) {
      return set(var0, 11, var1);
   }

   public static Date setMilliseconds(Date var0, int var1) {
      return set(var0, 14, var1);
   }

   public static Date setMinutes(Date var0, int var1) {
      return set(var0, 12, var1);
   }

   public static Date setMonths(Date var0, int var1) {
      return set(var0, 2, var1);
   }

   public static Date setSeconds(Date var0, int var1) {
      return set(var0, 13, var1);
   }

   public static Date setYears(Date var0, int var1) {
      return set(var0, 1, var1);
   }

   public static Calendar toCalendar(Date var0) {
      Calendar var1 = Calendar.getInstance();
      var1.setTime(var0);
      return var1;
   }

   public static Calendar toCalendar(Date var0, TimeZone var1) {
      Calendar var2 = Calendar.getInstance(var1);
      var2.setTime(var0);
      return var2;
   }

   public static Calendar truncate(Calendar var0, int var1) {
      if (var0 != null) {
         var0 = (Calendar)var0.clone();
         modify(var0, var1, DateUtils.ModifyType.TRUNCATE);
         return var0;
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Date truncate(Object var0, int var1) {
      if (var0 != null) {
         if (var0 instanceof Date) {
            return truncate((Date)var0, var1);
         } else if (var0 instanceof Calendar) {
            return truncate((Calendar)var0, var1).getTime();
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Could not truncate ");
            var2.append(var0);
            throw new ClassCastException(var2.toString());
         }
      } else {
         throw new IllegalArgumentException("The date must not be null");
      }
   }

   public static Date truncate(Date var0, int var1) {
      validateDateNotNull(var0);
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var0);
      modify(var2, var1, DateUtils.ModifyType.TRUNCATE);
      return var2.getTime();
   }

   public static int truncatedCompareTo(Calendar var0, Calendar var1, int var2) {
      return truncate(var0, var2).compareTo(truncate(var1, var2));
   }

   public static int truncatedCompareTo(Date var0, Date var1, int var2) {
      return truncate(var0, var2).compareTo(truncate(var1, var2));
   }

   public static boolean truncatedEquals(Calendar var0, Calendar var1, int var2) {
      return truncatedCompareTo(var0, var1, var2) == 0;
   }

   public static boolean truncatedEquals(Date var0, Date var1, int var2) {
      return truncatedCompareTo(var0, var1, var2) == 0;
   }

   private static void validateDateNotNull(Date var0) {
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "The date must not be null");
   }

   static class DateIterator implements Iterator {
      private final Calendar endFinal;
      private final Calendar spot;

      DateIterator(Calendar var1, Calendar var2) {
         this.endFinal = var2;
         this.spot = var1;
         var1.add(5, -1);
      }

      public boolean hasNext() {
         return this.spot.before(this.endFinal);
      }

      public Calendar next() {
         if (!this.spot.equals(this.endFinal)) {
            this.spot.add(5, 1);
            return (Calendar)this.spot.clone();
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   private static enum ModifyType {
      CEILING,
      ROUND,
      TRUNCATE;

      static {
         DateUtils.ModifyType var0 = new DateUtils.ModifyType("CEILING", 2);
         CEILING = var0;
      }
   }
}
