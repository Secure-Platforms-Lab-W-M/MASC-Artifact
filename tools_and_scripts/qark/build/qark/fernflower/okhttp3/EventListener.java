package okhttp3;

import java.net.InetAddress;
import java.util.List;

abstract class EventListener {
   public static final EventListener NONE = new EventListener() {
   };

   static EventListener.Factory factory(final EventListener var0) {
      return new EventListener.Factory() {
         public EventListener create(Call var1) {
            return var0;
         }
      };
   }

   public void connectEnd(Call var1, InetAddress var2, int var3, String var4, Throwable var5) {
   }

   public void connectStart(Call var1, InetAddress var2, int var3) {
   }

   public void dnsEnd(Call var1, String var2, List var3, Throwable var4) {
   }

   public void dnsStart(Call var1, String var2) {
   }

   public void fetchEnd(Call var1, Throwable var2) {
   }

   public void fetchStart(Call var1) {
   }

   public void requestBodyEnd(Call var1, Throwable var2) {
   }

   public void requestBodyStart(Call var1) {
   }

   public void requestHeadersEnd(Call var1, Throwable var2) {
   }

   public void requestHeadersStart(Call var1) {
   }

   public void responseBodyEnd(Call var1, Throwable var2) {
   }

   public void responseBodyStart(Call var1) {
   }

   public void responseHeadersEnd(Call var1, Throwable var2) {
   }

   public void responseHeadersStart(Call var1) {
   }

   public void secureConnectEnd(Call var1, Handshake var2, Throwable var3) {
   }

   public void secureConnectStart(Call var1) {
   }

   public interface Factory {
      EventListener create(Call var1);
   }
}
