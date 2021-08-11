package org.apache.commons.lang3.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class DurationFormatUtils {
   // $FF: renamed from: H java.lang.Object
   static final Object field_159 = "H";
   public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'";
   // $FF: renamed from: M java.lang.Object
   static final Object field_160 = "M";
   // $FF: renamed from: S java.lang.Object
   static final Object field_161 = "S";
   // $FF: renamed from: d java.lang.Object
   static final Object field_162 = "d";
   // $FF: renamed from: m java.lang.Object
   static final Object field_163 = "m";
   // $FF: renamed from: s java.lang.Object
   static final Object field_164 = "s";
   // $FF: renamed from: y java.lang.Object
   static final Object field_165 = "y";

   static String format(DurationFormatUtils.Token[] var0, long var1, long var3, long var5, long var7, long var9, long var11, long var13, boolean var15) {
      StringBuilder var21 = new StringBuilder();
      boolean var18 = false;
      int var19 = var0.length;

      boolean var16;
      for(int var17 = 0; var17 < var19; var18 = var16) {
         DurationFormatUtils.Token var22 = var0[var17];
         Object var23 = var22.getValue();
         int var20 = var22.getCount();
         if (var23 instanceof StringBuilder) {
            var21.append(var23.toString());
            var16 = var18;
         } else if (var23.equals(field_165)) {
            var21.append(paddedValue(var1, var15, var20));
            var16 = false;
         } else if (var23.equals(field_160)) {
            var21.append(paddedValue(var3, var15, var20));
            var16 = false;
         } else if (var23.equals(field_162)) {
            var21.append(paddedValue(var5, var15, var20));
            var16 = false;
         } else if (var23.equals(field_159)) {
            var21.append(paddedValue(var7, var15, var20));
            var16 = false;
         } else if (var23.equals(field_163)) {
            var21.append(paddedValue(var9, var15, var20));
            var16 = false;
         } else if (var23.equals(field_164)) {
            var21.append(paddedValue(var11, var15, var20));
            var16 = true;
         } else {
            var16 = var18;
            if (var23.equals(field_161)) {
               if (var18) {
                  int var24 = 3;
                  if (var15) {
                     var24 = Math.max(3, var20);
                  }

                  var21.append(paddedValue(var13, true, var24));
               } else {
                  var21.append(paddedValue(var13, var15, var20));
               }

               var16 = false;
            }
         }

         ++var17;
      }

      return var21.toString();
   }

   public static String formatDuration(long var0, String var2) {
      return formatDuration(var0, var2, true);
   }

   public static String formatDuration(long var0, String var2, boolean var3) {
      Validate.inclusiveBetween(0L, Long.MAX_VALUE, var0, "durationMillis must not be negative");
      DurationFormatUtils.Token[] var14 = lexx(var2);
      long var6 = 0L;
      long var8 = 0L;
      long var10 = 0L;
      long var4 = var0;
      var0 = var0;
      if (DurationFormatUtils.Token.containsTokenWithValue(var14, field_162)) {
         var6 = var4 / 86400000L;
         var0 = var4 - 86400000L * var6;
      }

      var4 = var0;
      if (DurationFormatUtils.Token.containsTokenWithValue(var14, field_159)) {
         var8 = var0 / 3600000L;
         var4 = var0 - 3600000L * var8;
      }

      var0 = var4;
      if (DurationFormatUtils.Token.containsTokenWithValue(var14, field_163)) {
         var10 = var4 / 60000L;
         var0 = var4 - 60000L * var10;
      }

      if (DurationFormatUtils.Token.containsTokenWithValue(var14, field_164)) {
         long var12 = var0 / 1000L;
         var4 = var12;
         var0 -= 1000L * var12;
      } else {
         var4 = 0L;
      }

      return format(var14, 0L, 0L, var6, var8, var10, var4, var0, var3);
   }

   public static String formatDurationHMS(long var0) {
      return formatDuration(var0, "HH:mm:ss.SSS");
   }

   public static String formatDurationISO(long var0) {
      return formatDuration(var0, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'", false);
   }

   public static String formatDurationWords(long var0, boolean var2, boolean var3) {
      String var4 = formatDuration(var0, "d' days 'H' hours 'm' minutes 's' seconds'");
      String var5 = var4;
      String var6;
      StringBuilder var7;
      if (var2) {
         var7 = new StringBuilder();
         var7.append(" ");
         var7.append(var4);
         var5 = var7.toString();
         var6 = StringUtils.replaceOnce(var5, " 0 days", "");
         var4 = var5;
         if (var6.length() != var5.length()) {
            var5 = var6;
            var6 = StringUtils.replaceOnce(var6, " 0 hours", "");
            var4 = var5;
            if (var6.length() != var5.length()) {
               var6 = StringUtils.replaceOnce(var6, " 0 minutes", "");
               var4 = var6;
               if (var6.length() != var6.length()) {
                  var4 = StringUtils.replaceOnce(var6, " 0 seconds", "");
               }
            }
         }

         var5 = var4;
         if (!var4.isEmpty()) {
            var5 = var4.substring(1);
         }
      }

      var4 = var5;
      if (var3) {
         var6 = StringUtils.replaceOnce(var5, " 0 seconds", "");
         var4 = var5;
         if (var6.length() != var5.length()) {
            var5 = var6;
            var6 = StringUtils.replaceOnce(var6, " 0 minutes", "");
            var4 = var5;
            if (var6.length() != var5.length()) {
               var5 = var6;
               var6 = StringUtils.replaceOnce(var6, " 0 hours", "");
               var4 = var5;
               if (var6.length() != var5.length()) {
                  var4 = StringUtils.replaceOnce(var6, " 0 days", "");
               }
            }
         }
      }

      var7 = new StringBuilder();
      var7.append(" ");
      var7.append(var4);
      return StringUtils.replaceOnce(StringUtils.replaceOnce(StringUtils.replaceOnce(StringUtils.replaceOnce(var7.toString(), " 1 seconds", " 1 second"), " 1 minutes", " 1 minute"), " 1 hours", " 1 hour"), " 1 days", " 1 day").trim();
   }

   public static String formatPeriod(long var0, long var2, String var4) {
      return formatPeriod(var0, var2, var4, true, TimeZone.getDefault());
   }

   public static String formatPeriod(long var0, long var2, String var4, boolean var5, TimeZone var6) {
      boolean var16;
      if (var0 <= var2) {
         var16 = true;
      } else {
         var16 = false;
      }

      Validate.isTrue(var16, "startMillis must not be greater than endMillis");
      DurationFormatUtils.Token[] var18 = lexx(var4);
      Calendar var17 = Calendar.getInstance(var6);
      var17.setTime(new Date(var0));
      Calendar var19 = Calendar.getInstance(var6);
      var19.setTime(new Date(var2));
      int var9 = var19.get(14) - var17.get(14);
      int var8 = var19.get(13) - var17.get(13);
      int var11 = var19.get(12) - var17.get(12);
      int var12 = var19.get(11) - var17.get(11);
      int var13 = var19.get(5) - var17.get(5);
      int var14 = var19.get(2) - var17.get(2);
      int var15 = var19.get(1) - var17.get(1);

      while(true) {
         int var10 = var8;
         int var7 = var11;
         if (var9 >= 0) {
            while(true) {
               var11 = var7;
               var8 = var12;
               if (var10 >= 0) {
                  while(true) {
                     var12 = var8;
                     var7 = var13;
                     if (var11 >= 0) {
                        while(var12 < 0) {
                           var12 += 24;
                           --var7;
                        }

                        if (DurationFormatUtils.Token.containsTokenWithValue(var18, field_160)) {
                           var13 = var7;

                           while(true) {
                              var7 = var14;
                              var8 = var15;
                              if (var13 >= 0) {
                                 while(var7 < 0) {
                                    var7 += 12;
                                    --var8;
                                 }

                                 if (!DurationFormatUtils.Token.containsTokenWithValue(var18, field_165) && var8 != 0) {
                                    while(var8 != 0) {
                                       var7 += var8 * 12;
                                       var8 = 0;
                                    }
                                 }
                                 break;
                              }

                              var13 += var17.getActualMaximum(5);
                              --var14;
                              var17.add(2, 1);
                           }
                        } else {
                           var8 = var7;
                           var13 = var15;
                           if (!DurationFormatUtils.Token.containsTokenWithValue(var18, field_165)) {
                              var8 = var19.get(1);
                              if (var14 < 0) {
                                 --var8;
                              }

                              while(var17.get(1) != var8) {
                                 var13 = var7 + (var17.getActualMaximum(6) - var17.get(6));
                                 var7 = var13;
                                 if (var17 instanceof GregorianCalendar) {
                                    var7 = var13;
                                    if (var17.get(2) == 1) {
                                       var7 = var13;
                                       if (var17.get(5) == 29) {
                                          var7 = var13 + 1;
                                       }
                                    }
                                 }

                                 var17.add(1, 1);
                                 var7 += var17.get(6);
                              }

                              var13 = 0;
                              var8 = var7;
                           }

                           while(var17.get(2) != var19.get(2)) {
                              var8 += var17.getActualMaximum(5);
                              var17.add(2, 1);
                           }

                           var14 = 0;
                           var7 = var8;

                           while(var7 < 0) {
                              var7 += var17.getActualMaximum(5);
                              --var14;
                              var17.add(2, 1);
                           }

                           var8 = var13;
                           var13 = var7;
                           var7 = var14;
                        }

                        var14 = var12;
                        var15 = var13;
                        if (!DurationFormatUtils.Token.containsTokenWithValue(var18, field_162)) {
                           var14 = var12 + var13 * 24;
                           var15 = 0;
                        }

                        if (!DurationFormatUtils.Token.containsTokenWithValue(var18, field_159)) {
                           var11 += var14 * 60;
                           var13 = 0;
                        } else {
                           var13 = var14;
                        }

                        var12 = var10;
                        var14 = var11;
                        if (!DurationFormatUtils.Token.containsTokenWithValue(var18, field_163)) {
                           var12 = var10 + var11 * 60;
                           var14 = 0;
                        }

                        if (!DurationFormatUtils.Token.containsTokenWithValue(var18, field_164)) {
                           var9 += var12 * 1000;
                           var12 = 0;
                        }

                        return format(var18, (long)var8, (long)var7, (long)var15, (long)var13, (long)var14, (long)var12, (long)var9, var5);
                     }

                     var11 += 60;
                     --var8;
                  }
               }

               var10 += 60;
               --var7;
            }
         }

         var9 += 1000;
         --var8;
      }
   }

   public static String formatPeriodISO(long var0, long var2) {
      return formatPeriod(var0, var2, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'", false, TimeZone.getDefault());
   }

   static DurationFormatUtils.Token[] lexx(String var0) {
      ArrayList var9 = new ArrayList(var0.length());
      boolean var2 = false;
      StringBuilder var6 = null;
      DurationFormatUtils.Token var7 = null;

      StringBuilder var5;
      DurationFormatUtils.Token var8;
      for(int var3 = 0; var3 < var0.length(); var7 = var8) {
         char var1 = var0.charAt(var3);
         boolean var4;
         if (var2 && var1 != '\'') {
            var6.append(var1);
            var4 = var2;
            var8 = var7;
         } else {
            var8 = null;
            Object var10;
            if (var1 != '\'') {
               if (var1 != 'H') {
                  if (var1 != 'M') {
                     if (var1 != 'S') {
                        if (var1 != 'd') {
                           if (var1 != 'm') {
                              if (var1 != 's') {
                                 if (var1 != 'y') {
                                    var5 = var6;
                                    if (var6 == null) {
                                       var5 = new StringBuilder();
                                       var9.add(new DurationFormatUtils.Token(var5));
                                    }

                                    var5.append(var1);
                                    var6 = var5;
                                    var10 = var8;
                                 } else {
                                    var10 = field_165;
                                 }
                              } else {
                                 var10 = field_164;
                              }
                           } else {
                              var10 = field_163;
                           }
                        } else {
                           var10 = field_162;
                        }
                     } else {
                        var10 = field_161;
                     }
                  } else {
                     var10 = field_160;
                  }
               } else {
                  var10 = field_159;
               }
            } else if (var2) {
               var6 = null;
               var2 = false;
               var10 = var8;
            } else {
               var6 = new StringBuilder();
               var9.add(new DurationFormatUtils.Token(var6));
               var2 = true;
               var10 = var8;
            }

            var4 = var2;
            var8 = var7;
            if (var10 != null) {
               if (var7 != null && var7.getValue().equals(var10)) {
                  var7.increment();
               } else {
                  var7 = new DurationFormatUtils.Token(var10);
                  var9.add(var7);
               }

               var6 = null;
               var8 = var7;
               var4 = var2;
            }
         }

         ++var3;
         var2 = var4;
      }

      if (!var2) {
         return (DurationFormatUtils.Token[])var9.toArray(new DurationFormatUtils.Token[var9.size()]);
      } else {
         var5 = new StringBuilder();
         var5.append("Unmatched quote in format: ");
         var5.append(var0);
         throw new IllegalArgumentException(var5.toString());
      }
   }

   private static String paddedValue(long var0, boolean var2, int var3) {
      String var4 = Long.toString(var0);
      return var2 ? StringUtils.leftPad(var4, var3, '0') : var4;
   }

   static class Token {
      private int count;
      private final Object value;

      Token(Object var1) {
         this.value = var1;
         this.count = 1;
      }

      Token(Object var1, int var2) {
         this.value = var1;
         this.count = var2;
      }

      static boolean containsTokenWithValue(DurationFormatUtils.Token[] var0, Object var1) {
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if (var0[var2].getValue() == var1) {
               return true;
            }
         }

         return false;
      }

      public boolean equals(Object var1) {
         boolean var3 = var1 instanceof DurationFormatUtils.Token;
         boolean var2 = false;
         if (var3) {
            DurationFormatUtils.Token var5 = (DurationFormatUtils.Token)var1;
            if (this.value.getClass() != var5.value.getClass()) {
               return false;
            } else if (this.count != var5.count) {
               return false;
            } else {
               Object var4 = this.value;
               if (var4 instanceof StringBuilder) {
                  return var4.toString().equals(var5.value.toString());
               } else if (var4 instanceof Number) {
                  return var4.equals(var5.value);
               } else {
                  if (var4 == var5.value) {
                     var2 = true;
                  }

                  return var2;
               }
            }
         } else {
            return false;
         }
      }

      int getCount() {
         return this.count;
      }

      Object getValue() {
         return this.value;
      }

      public int hashCode() {
         return this.value.hashCode();
      }

      void increment() {
         ++this.count;
      }

      public String toString() {
         return StringUtils.repeat(this.value.toString(), this.count);
      }
   }
}
