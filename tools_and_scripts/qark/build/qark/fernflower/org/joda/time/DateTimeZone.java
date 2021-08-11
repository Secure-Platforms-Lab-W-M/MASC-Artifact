package org.joda.time;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import org.joda.convert.FromString;
import org.joda.convert.ToString;
import org.joda.time.DateTimeZone.LazyInit;
import org.joda.time.DateTimeZone.Stub;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.FormatUtils;
import org.joda.time.tz.DefaultNameProvider;
import org.joda.time.tz.FixedDateTimeZone;
import org.joda.time.tz.NameProvider;
import org.joda.time.tz.Provider;

public abstract class DateTimeZone implements Serializable {
   private static final int MAX_MILLIS = 86399999;
   public static final DateTimeZone UTC;
   private static final AtomicReference cDefault;
   private static final AtomicReference cNameProvider;
   private static final AtomicReference cProvider;
   private static final long serialVersionUID = 5546345482340108586L;
   private final String iID;

   static {
      UTC = UTCDateTimeZone.INSTANCE;
      cProvider = new AtomicReference();
      cNameProvider = new AtomicReference();
      cDefault = new AtomicReference();
   }

   protected DateTimeZone(String var1) {
      if (var1 != null) {
         this.iID = var1;
      } else {
         throw new IllegalArgumentException("Id must not be null");
      }
   }

   private static String convertToAsciiNumber(String var0) {
      StringBuilder var3 = new StringBuilder(var0);

      for(int var1 = 0; var1 < var3.length(); ++var1) {
         int var2 = Character.digit(var3.charAt(var1), 10);
         if (var2 >= 0) {
            var3.setCharAt(var1, (char)(var2 + 48));
         }
      }

      return var3.toString();
   }

   private static DateTimeZone fixedOffsetZone(String var0, int var1) {
      return (DateTimeZone)(var1 == 0 ? UTC : new FixedDateTimeZone(var0, (String)null, var1, var1));
   }

   @FromString
   public static DateTimeZone forID(String var0) {
      if (var0 == null) {
         return getDefault();
      } else if (var0.equals("UTC")) {
         return UTC;
      } else {
         DateTimeZone var2 = getProvider().getZone(var0);
         if (var2 != null) {
            return var2;
         } else if (!var0.startsWith("+") && !var0.startsWith("-")) {
            StringBuilder var3 = new StringBuilder();
            var3.append("The datetime zone id '");
            var3.append(var0);
            var3.append("' is not recognised");
            throw new IllegalArgumentException(var3.toString());
         } else {
            int var1 = parseOffset(var0);
            return (long)var1 == 0L ? UTC : fixedOffsetZone(printOffset(var1), var1);
         }
      }
   }

   public static DateTimeZone forOffsetHours(int var0) throws IllegalArgumentException {
      return forOffsetHoursMinutes(var0, 0);
   }

   public static DateTimeZone forOffsetHoursMinutes(int var0, int var1) throws IllegalArgumentException {
      if (var0 == 0 && var1 == 0) {
         return UTC;
      } else {
         StringBuilder var2;
         if (var0 >= -23 && var0 <= 23) {
            if (var1 >= -59 && var1 <= 59) {
               if (var0 > 0 && var1 < 0) {
                  var2 = new StringBuilder();
                  var2.append("Positive hours must not have negative minutes: ");
                  var2.append(var1);
                  throw new IllegalArgumentException(var2.toString());
               } else {
                  var0 *= 60;
                  boolean var10001;
                  if (var0 < 0) {
                     try {
                        var0 -= Math.abs(var1);
                     } catch (ArithmeticException var4) {
                        var10001 = false;
                        throw new IllegalArgumentException("Offset is too large");
                     }
                  } else {
                     var0 += var1;
                  }

                  try {
                     var0 = FieldUtils.safeMultiply(var0, 60000);
                     return forOffsetMillis(var0);
                  } catch (ArithmeticException var3) {
                     var10001 = false;
                     throw new IllegalArgumentException("Offset is too large");
                  }
               }
            } else {
               var2 = new StringBuilder();
               var2.append("Minutes out of range: ");
               var2.append(var1);
               throw new IllegalArgumentException(var2.toString());
            }
         } else {
            var2 = new StringBuilder();
            var2.append("Hours out of range: ");
            var2.append(var0);
            throw new IllegalArgumentException(var2.toString());
         }
      }
   }

   public static DateTimeZone forOffsetMillis(int var0) {
      if (var0 >= -86399999 && var0 <= 86399999) {
         return fixedOffsetZone(printOffset(var0), var0);
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Millis out of range: ");
         var1.append(var0);
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static DateTimeZone forTimeZone(TimeZone var0) {
      if (var0 == null) {
         return getDefault();
      } else {
         String var3 = var0.getID();
         if (var3 == null) {
            throw new IllegalArgumentException("The TimeZone id must not be null");
         } else if (var3.equals("UTC")) {
            return UTC;
         } else {
            DateTimeZone var6 = null;
            String var4 = getConvertedId(var3);
            Provider var5 = getProvider();
            if (var4 != null) {
               var6 = var5.getZone(var4);
            }

            if (var6 == null) {
               var6 = var5.getZone(var3);
            }

            if (var6 != null) {
               return var6;
            } else if (var4 != null || !var3.startsWith("GMT+") && !var3.startsWith("GMT-")) {
               StringBuilder var8 = new StringBuilder();
               var8.append("The datetime zone id '");
               var8.append(var3);
               var8.append("' is not recognised");
               throw new IllegalArgumentException(var8.toString());
            } else {
               String var7 = var3.substring(3);
               if (var7.length() > 2) {
                  char var1 = var7.charAt(1);
                  if (var1 > '9' && Character.isDigit(var1)) {
                     var7 = convertToAsciiNumber(var7);
                  }
               }

               int var2 = parseOffset(var7);
               return (long)var2 == 0L ? UTC : fixedOffsetZone(printOffset(var2), var2);
            }
         }
      }
   }

   public static Set getAvailableIDs() {
      return getProvider().getAvailableIDs();
   }

   private static String getConvertedId(String var0) {
      return (String)LazyInit.CONVERSION_MAP.get(var0);
   }

   public static DateTimeZone getDefault() {
      // $FF: Couldn't be decompiled
   }

   private static NameProvider getDefaultNameProvider() {
      // $FF: Couldn't be decompiled
   }

   private static Provider getDefaultProvider() {
      // $FF: Couldn't be decompiled
   }

   public static NameProvider getNameProvider() {
      NameProvider var0 = (NameProvider)cNameProvider.get();
      if (var0 == null) {
         var0 = getDefaultNameProvider();
         return !cNameProvider.compareAndSet((Object)null, var0) ? (NameProvider)cNameProvider.get() : var0;
      } else {
         return var0;
      }
   }

   public static Provider getProvider() {
      Provider var0 = (Provider)cProvider.get();
      if (var0 == null) {
         var0 = getDefaultProvider();
         return !cProvider.compareAndSet((Object)null, var0) ? (Provider)cProvider.get() : var0;
      } else {
         return var0;
      }
   }

   private static int parseOffset(String var0) {
      return -((int)LazyInit.OFFSET_FORMATTER.parseMillis(var0));
   }

   private static String printOffset(int var0) {
      StringBuffer var2 = new StringBuffer();
      if (var0 >= 0) {
         var2.append('+');
      } else {
         var2.append('-');
         var0 = -var0;
      }

      int var1 = var0 / 3600000;
      FormatUtils.appendPaddedInteger(var2, var1, 2);
      var0 -= var1 * 3600000;
      var1 = var0 / '\uea60';
      var2.append(':');
      FormatUtils.appendPaddedInteger(var2, var1, 2);
      var0 -= var1 * '\uea60';
      if (var0 == 0) {
         return var2.toString();
      } else {
         var1 = var0 / 1000;
         var2.append(':');
         FormatUtils.appendPaddedInteger(var2, var1, 2);
         var0 -= var1 * 1000;
         if (var0 == 0) {
            return var2.toString();
         } else {
            var2.append('.');
            FormatUtils.appendPaddedInteger(var2, var0, 3);
            return var2.toString();
         }
      }
   }

   public static void setDefault(DateTimeZone var0) throws SecurityException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new JodaTimePermission("DateTimeZone.setDefault"));
      }

      if (var0 != null) {
         cDefault.set(var0);
      } else {
         throw new IllegalArgumentException("The datetime zone must not be null");
      }
   }

   public static void setNameProvider(NameProvider var0) throws SecurityException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new JodaTimePermission("DateTimeZone.setNameProvider"));
      }

      if (var0 == null) {
         var0 = getDefaultNameProvider();
      }

      cNameProvider.set(var0);
   }

   public static void setProvider(Provider var0) throws SecurityException {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new JodaTimePermission("DateTimeZone.setProvider"));
      }

      if (var0 == null) {
         var0 = getDefaultProvider();
      } else {
         validateProvider(var0);
      }

      cProvider.set(var0);
   }

   private static Provider validateProvider(Provider var0) {
      Set var1 = var0.getAvailableIDs();
      if (var1 != null && var1.size() != 0) {
         if (var1.contains("UTC")) {
            if (UTC.equals(var0.getZone("UTC"))) {
               return var0;
            } else {
               throw new IllegalArgumentException("Invalid UTC zone provided");
            }
         } else {
            throw new IllegalArgumentException("The provider doesn't support UTC");
         }
      } else {
         throw new IllegalArgumentException("The provider doesn't have any available ids");
      }
   }

   public long adjustOffset(long var1, boolean var3) {
      long var4 = var1 - 10800000L;
      long var6 = (long)this.getOffset(var4);
      long var8 = (long)this.getOffset(10800000L + var1);
      if (var6 <= var8) {
         return var1;
      } else {
         var6 -= var8;
         var4 = this.nextTransition(var4);
         var8 = var4 - var6;
         if (var1 >= var8) {
            if (var1 >= var4 + var6) {
               return var1;
            } else if (var1 - var8 >= var6) {
               return var3 ? var1 : var1 - var6;
            } else {
               var4 = var1;
               if (var3) {
                  var4 = var1 + var6;
               }

               return var4;
            }
         } else {
            return var1;
         }
      }
   }

   public long convertLocalToUTC(long var1, boolean var3) {
      int var4;
      long var6;
      long var8;
      label41: {
         var4 = this.getOffset(var1);
         long var10 = var1 - (long)var4;
         int var5 = this.getOffset(var10);
         if (var4 != var5 && (var3 || var4 < 0)) {
            var6 = this.nextTransition(var10);
            var8 = Long.MAX_VALUE;
            if (var6 == var10) {
               var6 = Long.MAX_VALUE;
            }

            long var12 = var1 - (long)var5;
            var10 = this.nextTransition(var12);
            if (var10 != var12) {
               var8 = var10;
            }

            if (var6 != var8) {
               if (var3) {
                  throw new IllegalInstantException(var1, this.getID());
               }
               break label41;
            }
         }

         var4 = var5;
      }

      var6 = (long)var4;
      var8 = var1 - var6;
      if ((var1 ^ var8) < 0L) {
         if ((var1 ^ var6) >= 0L) {
            return var8;
         } else {
            throw new ArithmeticException("Subtracting time zone offset caused overflow");
         }
      } else {
         return var8;
      }
   }

   public long convertLocalToUTC(long var1, boolean var3, long var4) {
      int var6 = this.getOffset(var4);
      var4 = var1 - (long)var6;
      return this.getOffset(var4) == var6 ? var4 : this.convertLocalToUTC(var1, var3);
   }

   public long convertUTCToLocal(long var1) {
      long var3 = (long)this.getOffset(var1);
      long var5 = var1 + var3;
      if ((var1 ^ var5) < 0L) {
         if ((var1 ^ var3) < 0L) {
            return var5;
         } else {
            throw new ArithmeticException("Adding time zone offset caused overflow");
         }
      } else {
         return var5;
      }
   }

   public abstract boolean equals(Object var1);

   @ToString
   public final String getID() {
      return this.iID;
   }

   public long getMillisKeepLocal(DateTimeZone var1, long var2) {
      if (var1 == null) {
         var1 = getDefault();
      }

      return var1 == this ? var2 : var1.convertLocalToUTC(this.convertUTCToLocal(var2), false, var2);
   }

   public final String getName(long var1) {
      return this.getName(var1, (Locale)null);
   }

   public String getName(long var1, Locale var3) {
      if (var3 == null) {
         var3 = Locale.getDefault();
      }

      String var4 = this.getNameKey(var1);
      if (var4 == null) {
         return this.iID;
      } else {
         NameProvider var5 = getNameProvider();
         String var6;
         if (var5 instanceof DefaultNameProvider) {
            var6 = ((DefaultNameProvider)var5).getName(var3, this.iID, var4, this.isStandardOffset(var1));
         } else {
            var6 = var5.getName(var3, this.iID, var4);
         }

         return var6 != null ? var6 : printOffset(this.getOffset(var1));
      }
   }

   public abstract String getNameKey(long var1);

   public abstract int getOffset(long var1);

   public final int getOffset(ReadableInstant var1) {
      return var1 == null ? this.getOffset(DateTimeUtils.currentTimeMillis()) : this.getOffset(var1.getMillis());
   }

   public int getOffsetFromLocal(long var1) {
      int var3 = this.getOffset(var1);
      long var8 = var1 - (long)var3;
      int var4 = this.getOffset(var8);
      if (var3 != var4) {
         if (var3 - var4 < 0) {
            long var6 = this.nextTransition(var8);
            if (var6 == var8) {
               var6 = Long.MAX_VALUE;
            }

            var8 = var1 - (long)var4;
            var1 = this.nextTransition(var8);
            if (var1 == var8) {
               var1 = Long.MAX_VALUE;
            }

            return var6 != var1 ? var3 : var4;
         } else {
            return var4;
         }
      } else if (var3 >= 0) {
         var1 = this.previousTransition(var8);
         if (var1 < var8) {
            int var5 = this.getOffset(var1);
            return var8 - var1 <= (long)(var5 - var3) ? var5 : var4;
         } else {
            return var4;
         }
      } else {
         return var4;
      }
   }

   public final String getShortName(long var1) {
      return this.getShortName(var1, (Locale)null);
   }

   public String getShortName(long var1, Locale var3) {
      if (var3 == null) {
         var3 = Locale.getDefault();
      }

      String var4 = this.getNameKey(var1);
      if (var4 == null) {
         return this.iID;
      } else {
         NameProvider var5 = getNameProvider();
         String var6;
         if (var5 instanceof DefaultNameProvider) {
            var6 = ((DefaultNameProvider)var5).getShortName(var3, this.iID, var4, this.isStandardOffset(var1));
         } else {
            var6 = var5.getShortName(var3, this.iID, var4);
         }

         return var6 != null ? var6 : printOffset(this.getOffset(var1));
      }
   }

   public abstract int getStandardOffset(long var1);

   public int hashCode() {
      return this.getID().hashCode() + 57;
   }

   public abstract boolean isFixed();

   public boolean isLocalDateTimeGap(LocalDateTime var1) {
      if (this.isFixed()) {
         return false;
      } else {
         try {
            var1.toDateTime(this);
            return false;
         } catch (IllegalInstantException var2) {
            return true;
         }
      }
   }

   public boolean isStandardOffset(long var1) {
      return this.getOffset(var1) == this.getStandardOffset(var1);
   }

   public abstract long nextTransition(long var1);

   public abstract long previousTransition(long var1);

   public String toString() {
      return this.getID();
   }

   public TimeZone toTimeZone() {
      return TimeZone.getTimeZone(this.iID);
   }

   protected Object writeReplace() throws ObjectStreamException {
      return new Stub(this.iID);
   }
}
