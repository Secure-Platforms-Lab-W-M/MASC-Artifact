package util;

public interface LoggerInterface {
   void closeLogger();

   void log(String var1);

   void logException(Exception var1);

   void logLine(String var1);

   void message(String var1);
}
