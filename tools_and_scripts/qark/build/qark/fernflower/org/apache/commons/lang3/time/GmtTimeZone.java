package org.apache.commons.lang3.time;

import java.util.Date;
import java.util.TimeZone;

class GmtTimeZone extends TimeZone {
   private static final int HOURS_PER_DAY = 24;
   private static final int MILLISECONDS_PER_MINUTE = 60000;
   private static final int MINUTES_PER_HOUR = 60;
   static final long serialVersionUID = 1L;
   private final int offset;
   private final String zoneId;

   GmtTimeZone(boolean var1, int var2, int var3) {
      StringBuilder var6;
      if (var2 < 24) {
         if (var3 < 60) {
            int var5 = (var2 * 60 + var3) * '\uea60';
            if (var1) {
               var5 = -var5;
            }

            this.offset = var5;
            var6 = new StringBuilder(9);
            var6.append("GMT");
            char var4;
            if (var1) {
               var4 = '-';
            } else {
               var4 = '+';
            }

            var6.append(var4);
            var6 = twoDigits(var6, var2);
            var6.append(':');
            this.zoneId = twoDigits(var6, var3).toString();
         } else {
            var6 = new StringBuilder();
            var6.append(var3);
            var6.append(" minutes out of range");
            throw new IllegalArgumentException(var6.toString());
         }
      } else {
         var6 = new StringBuilder();
         var6.append(var2);
         var6.append(" hours out of range");
         throw new IllegalArgumentException(var6.toString());
      }
   }

   private static StringBuilder twoDigits(StringBuilder var0, int var1) {
      var0.append((char)(var1 / 10 + 48));
      var0.append((char)(var1 % 10 + 48));
      return var0;
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof GmtTimeZone;
      boolean var2 = false;
      if (!var3) {
         return false;
      } else {
         if (this.zoneId == ((GmtTimeZone)var1).zoneId) {
            var2 = true;
         }

         return var2;
      }
   }

   public String getID() {
      return this.zoneId;
   }

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6) {
      return this.offset;
   }

   public int getRawOffset() {
      return this.offset;
   }

   public int hashCode() {
      return this.offset;
   }

   public boolean inDaylightTime(Date var1) {
      return false;
   }

   public void setRawOffset(int var1) {
      throw new UnsupportedOperationException();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[GmtTimeZone id=\"");
      var1.append(this.zoneId);
      var1.append("\",offset=");
      var1.append(this.offset);
      var1.append(']');
      return var1.toString();
   }

   public boolean useDaylightTime() {
      return false;
   }
}
