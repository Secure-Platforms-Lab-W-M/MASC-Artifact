package okhttp3;

import java.io.IOException;

public enum Protocol {
   HTTP_1_0("http/1.0"),
   HTTP_1_1("http/1.1"),
   HTTP_2,
   SPDY_3("spdy/3.1");

   private final String protocol;

   static {
      Protocol var0 = new Protocol("HTTP_2", 3, "h2");
      HTTP_2 = var0;
   }

   private Protocol(String var3) {
      this.protocol = var3;
   }

   public static Protocol get(String var0) throws IOException {
      if (var0.equals(HTTP_1_0.protocol)) {
         return HTTP_1_0;
      } else if (var0.equals(HTTP_1_1.protocol)) {
         return HTTP_1_1;
      } else if (var0.equals(HTTP_2.protocol)) {
         return HTTP_2;
      } else if (var0.equals(SPDY_3.protocol)) {
         return SPDY_3;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Unexpected protocol: ");
         var1.append(var0);
         throw new IOException(var1.toString());
      }
   }

   public String toString() {
      return this.protocol;
   }
}
