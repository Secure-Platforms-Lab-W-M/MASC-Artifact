package okhttp3.internal.http2;

import java.io.IOException;
import java.util.List;
import okio.BufferedSource;

public interface PushObserver {
   PushObserver CANCEL = new PushObserver() {
      public boolean onData(int var1, BufferedSource var2, int var3, boolean var4) throws IOException {
         var2.skip((long)var3);
         return true;
      }

      public boolean onHeaders(int var1, List var2, boolean var3) {
         return true;
      }

      public boolean onRequest(int var1, List var2) {
         return true;
      }

      public void onReset(int var1, ErrorCode var2) {
      }
   };

   boolean onData(int var1, BufferedSource var2, int var3, boolean var4) throws IOException;

   boolean onHeaders(int var1, List var2, boolean var3);

   boolean onRequest(int var1, List var2);

   void onReset(int var1, ErrorCode var2);
}
