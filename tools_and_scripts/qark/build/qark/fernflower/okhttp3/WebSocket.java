package okhttp3;

import javax.annotation.Nullable;
import okio.ByteString;

public interface WebSocket {
   void cancel();

   boolean close(int var1, @Nullable String var2);

   long queueSize();

   Request request();

   boolean send(String var1);

   boolean send(ByteString var1);

   public interface Factory {
      WebSocket newWebSocket(Request var1, WebSocketListener var2);
   }
}
