package org.apache.http.impl.cookie;

import java.util.BitSet;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;

public class LaxExpiresHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
   private static final Pattern DAY_OF_MONTH_PATTERN;
   private static final BitSet DELIMS;
   private static final Map MONTHS;
   private static final Pattern MONTH_PATTERN;
   private static final Pattern TIME_PATTERN;
   static final TimeZone UTC = TimeZone.getTimeZone("UTC");
   private static final Pattern YEAR_PATTERN;

   static {
      BitSet var1 = new BitSet();
      var1.set(9);

      int var0;
      for(var0 = 32; var0 <= 47; ++var0) {
         var1.set(var0);
      }

      for(var0 = 59; var0 <= 64; ++var0) {
         var1.set(var0);
      }

      for(var0 = 91; var0 <= 96; ++var0) {
         var1.set(var0);
      }

      for(var0 = 123; var0 <= 126; ++var0) {
         var1.set(var0);
      }

      DELIMS = var1;
      ConcurrentHashMap var2 = new ConcurrentHashMap(12);
      var2.put("jan", 0);
      var2.put("feb", 1);
      var2.put("mar", 2);
      var2.put("apr", 3);
      var2.put("may", 4);
      var2.put("jun", 5);
      var2.put("jul", 6);
      var2.put("aug", 7);
      var2.put("sep", 8);
      var2.put("oct", 9);
      var2.put("nov", 10);
      var2.put("dec", 11);
      MONTHS = var2;
      TIME_PATTERN = Pattern.compile("^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})([^0-9].*)?$");
      DAY_OF_MONTH_PATTERN = Pattern.compile("^([0-9]{1,2})([^0-9].*)?$");
      MONTH_PATTERN = Pattern.compile("^(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)(.*)?$", 2);
      YEAR_PATTERN = Pattern.compile("^([0-9]{2,4})([^0-9].*)?$");
   }

   private void copyContent(CharSequence var1, ParserCursor var2, StringBuilder var3) {
      int var6 = var2.getPos();
      int var5 = var2.getPos();

      for(int var7 = var2.getUpperBound(); var5 < var7; ++var5) {
         char var4 = var1.charAt(var5);
         if (DELIMS.get(var4)) {
            break;
         }

         ++var6;
         var3.append(var4);
      }

      var2.updatePos(var6);
   }

   private void skipDelims(CharSequence var1, ParserCursor var2) {
      int var4 = var2.getPos();
      int var3 = var2.getPos();

      for(int var5 = var2.getUpperBound(); var3 < var5; ++var3) {
         char var6 = var1.charAt(var3);
         if (!DELIMS.get(var6)) {
            break;
         }

         ++var4;
      }

      var2.updatePos(var4);
   }

   public String getAttributeName() {
      return "expires";
   }

   public void parse(SetCookie var1, String var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      ParserCursor var15 = new ParserCursor(0, var2.length());
      StringBuilder var16 = new StringBuilder();
      boolean var7 = false;
      boolean var4 = false;
      boolean var8 = false;
      boolean var12 = false;
      int var3 = 0;
      int var5 = 0;
      int var6 = 0;
      int var11 = 0;
      int var10 = 0;
      int var9 = 0;

      StringBuilder var27;
      while(true) {
         LaxExpiresHandler var14 = this;

         label163: {
            boolean var13;
            try {
               var13 = var15.atEnd();
            } catch (NumberFormatException var26) {
               break label163;
            }

            if (var13) {
               break;
            }

            boolean var10001;
            try {
               var14.skipDelims(var2, var15);
               var16.setLength(0);
               var14.copyContent(var2, var15, var16);
               if (var16.length() == 0) {
                  break;
               }
            } catch (NumberFormatException var25) {
               var10001 = false;
               break label163;
            }

            Matcher var30;
            if (!var12) {
               label159: {
                  try {
                     var30 = TIME_PATTERN.matcher(var16);
                     if (!var30.matches()) {
                        break label159;
                     }
                  } catch (NumberFormatException var24) {
                     var10001 = false;
                     break label163;
                  }

                  var12 = true;

                  try {
                     var11 = Integer.parseInt(var30.group(1));
                     var10 = Integer.parseInt(var30.group(2));
                     var9 = Integer.parseInt(var30.group(3));
                     continue;
                  } catch (NumberFormatException var17) {
                     var10001 = false;
                     break label163;
                  }
               }
            }

            label162: {
               if (!var8) {
                  try {
                     var30 = DAY_OF_MONTH_PATTERN.matcher(var16);
                     if (var30.matches()) {
                        break label162;
                     }
                  } catch (NumberFormatException var23) {
                     var10001 = false;
                     break label163;
                  }
               }

               if (!var4) {
                  label160: {
                     try {
                        var30 = MONTH_PATTERN.matcher(var16);
                        if (!var30.matches()) {
                           break label160;
                        }
                     } catch (NumberFormatException var22) {
                        var10001 = false;
                        break label163;
                     }

                     var4 = true;

                     try {
                        var5 = (Integer)MONTHS.get(var30.group(1).toLowerCase(Locale.ROOT));
                        continue;
                     } catch (NumberFormatException var19) {
                        var10001 = false;
                        break label163;
                     }
                  }
               }

               if (var7) {
                  continue;
               }

               try {
                  var30 = YEAR_PATTERN.matcher(var16);
                  if (!var30.matches()) {
                     continue;
                  }
               } catch (NumberFormatException var21) {
                  var10001 = false;
                  break label163;
               }

               var7 = true;

               try {
                  var3 = Integer.parseInt(var30.group(1));
                  continue;
               } catch (NumberFormatException var20) {
                  var10001 = false;
                  break label163;
               }
            }

            var8 = true;

            try {
               var6 = Integer.parseInt(var30.group(1));
               continue;
            } catch (NumberFormatException var18) {
               var10001 = false;
            }
         }

         var27 = new StringBuilder();
         var27.append("Invalid 'expires' attribute: ");
         var27.append(var2);
         throw new MalformedCookieException(var27.toString());
      }

      if (var12 && var8 && var4 && var7) {
         int var29 = var3;
         if (var3 >= 70) {
            var29 = var3;
            if (var3 <= 99) {
               var29 = var3 + 1900;
            }
         }

         var3 = var29;
         if (var29 >= 0) {
            var3 = var29;
            if (var29 <= 69) {
               var3 = var29 + 2000;
            }
         }

         if (var6 >= 1 && var6 <= 31 && var3 >= 1601 && var11 <= 23 && var10 <= 59 && var9 <= 59) {
            Calendar var28 = Calendar.getInstance();
            var28.setTimeZone(UTC);
            var28.setTimeInMillis(0L);
            var28.set(13, var9);
            var28.set(12, var10);
            var28.set(11, var11);
            var28.set(5, var6);
            var28.set(2, var5);
            var28.set(1, var3);
            var1.setExpiryDate(var28.getTime());
         } else {
            var27 = new StringBuilder();
            var27.append("Invalid 'expires' attribute: ");
            var27.append(var2);
            throw new MalformedCookieException(var27.toString());
         }
      } else {
         var27 = new StringBuilder();
         var27.append("Invalid 'expires' attribute: ");
         var27.append(var2);
         throw new MalformedCookieException(var27.toString());
      }
   }
}
