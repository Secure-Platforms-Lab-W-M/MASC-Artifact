package android.support.v4.os;

public class OperationCanceledException extends RuntimeException {
   public OperationCanceledException() {
      this((String)null);
   }

   public OperationCanceledException(String var1) {
      if (var1 == null) {
         var1 = "The operation has been canceled.";
      }

      super(var1);
   }
}
