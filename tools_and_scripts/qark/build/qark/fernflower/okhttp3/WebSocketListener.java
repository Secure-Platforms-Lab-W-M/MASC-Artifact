package okhttp3;

import okio.ByteString;

public abstract class WebSocketListener {
   public void onClosed(WebSocket var1, int var2, String var3) {
   }

   public void onClosing(WebSocket var1, int var2, String var3) {
   }

   public void onFailure(WebSocket var1, Throwable var2, Response var3) {
   }

   public void onMessage(WebSocket var1, String var2) {
   }

   public void onMessage(WebSocket var1, ByteString var2) {
   }

   public void onOpen(WebSocket var1, Response var2) {
   }
}
