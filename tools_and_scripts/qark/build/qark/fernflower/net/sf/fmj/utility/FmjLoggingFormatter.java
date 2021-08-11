package net.sf.fmj.utility;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FmjLoggingFormatter extends Formatter {
   private static final String format = "{0,date} {0,time}";
   private final boolean NO_FIRST_LINE = true;
   private Object[] args = new Object[1];
   Date dat = new Date();
   private MessageFormat formatter;
   private String lineSeparator = System.getProperty("line.separator");

   public String format(LogRecord param1) {
      // $FF: Couldn't be decompiled
   }
}
