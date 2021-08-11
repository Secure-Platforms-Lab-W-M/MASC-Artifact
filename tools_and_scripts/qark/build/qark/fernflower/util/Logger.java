package util;

import java.util.Hashtable;

public class Logger implements LoggerInterface {
   private static Hashtable _loggers = new Hashtable();
   private static LoggerInterface m_default = new Logger();
   private static LoggerInterface m_logger;

   public static LoggerInterface getLogger() {
      return m_logger != null ? m_logger : m_default;
   }

   public static LoggerInterface getLogger(String var0) {
      LoggerInterface var1 = (LoggerInterface)_loggers.get(var0);
      return var1 != null ? var1 : getLogger();
   }

   public static void removeLogger(String var0) {
      _loggers.remove(var0);
   }

   public static void setLogger(LoggerInterface var0) {
      m_logger = var0;
   }

   public static void setLogger(LoggerInterface var0, String var1) {
      _loggers.put(var1, var0);
   }

   public void closeLogger() {
   }

   public void log(String var1) {
      System.out.print(var1);
   }

   public void logException(Exception var1) {
      var1.printStackTrace();
   }

   public void logLine(String var1) {
      System.out.println(var1);
   }

   public void message(String var1) {
      System.out.println(var1);
   }
}
