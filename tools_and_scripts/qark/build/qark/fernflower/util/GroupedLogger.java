package util;

import java.util.Vector;

public class GroupedLogger implements LoggerInterface {
   LoggerInterface[] nestedLoggers;

   public GroupedLogger(LoggerInterface[] var1) {
      this.nestedLoggers = var1;
   }

   public void attachLogger(LoggerInterface var1) {
      LoggerInterface[] var3 = this.nestedLoggers;
      int var2 = 0;

      for(var3 = new LoggerInterface[var3.length + 1]; var2 < this.nestedLoggers.length; ++var2) {
         var3[var2] = this.nestedLoggers[var2];
      }

      var3[this.nestedLoggers.length] = var1;
      this.nestedLoggers = var3;
   }

   public void closeLogger() {
      for(int var1 = 0; var1 < this.nestedLoggers.length; ++var1) {
         this.nestedLoggers[var1].closeLogger();
      }

   }

   public void detachLogger(LoggerInterface var1) {
      LoggerInterface[] var3 = this.nestedLoggers;
      int var2 = 0;

      Vector var4;
      for(var4 = new Vector(var3.length); var2 < this.nestedLoggers.length; ++var2) {
         if (this.nestedLoggers[var2] != var1) {
            var4.add(this.nestedLoggers[var2]);
         }
      }

      this.nestedLoggers = (LoggerInterface[])var4.toArray(new LoggerInterface[var4.size()]);
   }

   public void log(String var1) {
      for(int var2 = 0; var2 < this.nestedLoggers.length; ++var2) {
         this.nestedLoggers[var2].log(var1);
      }

   }

   public void logException(Exception var1) {
      for(int var2 = 0; var2 < this.nestedLoggers.length; ++var2) {
         this.nestedLoggers[var2].logException(var1);
      }

   }

   public void logLine(String var1) {
      for(int var2 = 0; var2 < this.nestedLoggers.length; ++var2) {
         this.nestedLoggers[var2].logLine(var1);
      }

   }

   public void message(String var1) {
      for(int var2 = 0; var2 < this.nestedLoggers.length; ++var2) {
         this.nestedLoggers[var2].message(var1);
      }

   }
}
