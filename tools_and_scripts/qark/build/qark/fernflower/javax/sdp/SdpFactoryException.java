package javax.sdp;

public class SdpFactoryException extends SdpException {
   protected Exception mEx;

   public SdpFactoryException() {
   }

   public SdpFactoryException(Exception var1) {
      super(var1.getMessage());
      this.mEx = var1;
   }

   public SdpFactoryException(String var1) {
      super(var1);
   }

   public SdpFactoryException(String var1, Exception var2) {
      super(var1);
      this.mEx = var2;
   }

   public Exception getException() {
      return this.mEx;
   }

   public String getMessage() {
      if (super.getMessage() != null) {
         return super.getMessage();
      } else {
         Exception var1 = this.mEx;
         return var1 != null ? var1.getMessage() : null;
      }
   }
}
