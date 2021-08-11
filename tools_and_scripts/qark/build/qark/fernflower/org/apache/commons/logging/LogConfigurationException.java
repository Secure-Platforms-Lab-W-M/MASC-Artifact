package org.apache.commons.logging;

public class LogConfigurationException extends RuntimeException {
   private static final long serialVersionUID = 8486587136871052495L;
   protected Throwable cause;

   public LogConfigurationException() {
      this.cause = null;
   }

   public LogConfigurationException(String var1) {
      super(var1);
      this.cause = null;
   }

   public LogConfigurationException(String var1, Throwable var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append(var1);
      var3.append(" (Caused by ");
      var3.append(var2);
      var3.append(")");
      super(var3.toString());
      this.cause = null;
      this.cause = var2;
   }

   public LogConfigurationException(Throwable var1) {
      String var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = var1.toString();
      }

      this(var2, var1);
   }

   public Throwable getCause() {
      return this.cause;
   }
}
