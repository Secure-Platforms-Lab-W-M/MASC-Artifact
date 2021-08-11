package okhttp3.internal.http2;

public enum ErrorCode {
   CANCEL,
   FLOW_CONTROL_ERROR(3),
   INTERNAL_ERROR(2),
   NO_ERROR(0),
   PROTOCOL_ERROR(1),
   REFUSED_STREAM(7);

   public final int httpCode;

   static {
      ErrorCode var0 = new ErrorCode("CANCEL", 5, 8);
      CANCEL = var0;
   }

   private ErrorCode(int var3) {
      this.httpCode = var3;
   }

   public static ErrorCode fromHttp2(int var0) {
      ErrorCode[] var3 = values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ErrorCode var4 = var3[var1];
         if (var4.httpCode == var0) {
            return var4;
         }
      }

      return null;
   }
}
