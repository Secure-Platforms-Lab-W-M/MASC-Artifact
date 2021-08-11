package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface DateParser {
   Locale getLocale();

   String getPattern();

   TimeZone getTimeZone();

   Date parse(String var1) throws ParseException;

   Date parse(String var1, ParsePosition var2);

   boolean parse(String var1, ParsePosition var2, Calendar var3);

   Object parseObject(String var1) throws ParseException;

   Object parseObject(String var1, ParsePosition var2);
}
