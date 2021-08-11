package dnsfilter;

import util.LoggerInterface;

class DNSFilterProxy$1StandaloneLogger implements LoggerInterface {
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
      this.logLine(var1);
   }
}
