package javax.sdp;

public class SdpException extends Exception {
   private static final long serialVersionUID = 1L;

   public SdpException() {
   }

   public SdpException(String var1) {
      super(var1);
   }

   public SdpException(String var1, Throwable var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var2.getMessage());
      var3.append(';');
      var3.append(var1);
      super(var3.toString());
   }

   public SdpException(Throwable var1) {
      super(var1.getLocalizedMessage());
   }

   public Throwable getRootCause() {
      return this.fillInStackTrace();
   }
}
