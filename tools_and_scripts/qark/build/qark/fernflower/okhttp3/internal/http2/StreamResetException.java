package okhttp3.internal.http2;

import java.io.IOException;

public final class StreamResetException extends IOException {
   public final ErrorCode errorCode;

   public StreamResetException(ErrorCode var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("stream was reset: ");
      var2.append(var1);
      super(var2.toString());
      this.errorCode = var1;
   }
}
