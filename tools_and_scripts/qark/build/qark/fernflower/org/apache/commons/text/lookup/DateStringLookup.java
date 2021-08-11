package org.apache.commons.text.lookup;

import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;

final class DateStringLookup extends AbstractStringLookup {
   static final DateStringLookup INSTANCE = new DateStringLookup();

   private DateStringLookup() {
   }

   private String formatDate(long var1, String var3) {
      FastDateFormat var4 = null;
      if (var3 != null) {
         try {
            var4 = FastDateFormat.getInstance(var3);
         } catch (Exception var5) {
            throw IllegalArgumentExceptions.format(var5, "Invalid date format: [%s]", var3);
         }
      }

      FastDateFormat var6 = var4;
      if (var4 == null) {
         var6 = FastDateFormat.getInstance();
      }

      return var6.format(new Date(var1));
   }

   public String lookup(String var1) {
      return this.formatDate(System.currentTimeMillis(), var1);
   }
}
