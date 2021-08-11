package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Protocol;
import okhttp3.Response;

public final class StatusLine {
   public static final int HTTP_CONTINUE = 100;
   public static final int HTTP_PERM_REDIRECT = 308;
   public static final int HTTP_TEMP_REDIRECT = 307;
   public final int code;
   public final String message;
   public final Protocol protocol;

   public StatusLine(Protocol var1, int var2, String var3) {
      this.protocol = var1;
      this.code = var2;
      this.message = var3;
   }

   public static StatusLine get(Response var0) {
      return new StatusLine(var0.protocol(), var0.code(), var0.message());
   }

   public static StatusLine parse(String var0) throws IOException {
      byte var1;
      int var2;
      Protocol var3;
      StringBuilder var6;
      if (var0.startsWith("HTTP/1.")) {
         if (var0.length() < 9 || var0.charAt(8) != ' ') {
            var6 = new StringBuilder();
            var6.append("Unexpected status line: ");
            var6.append(var0);
            throw new ProtocolException(var6.toString());
         }

         var2 = var0.charAt(7) - 48;
         var1 = 9;
         if (var2 == 0) {
            var3 = Protocol.HTTP_1_0;
         } else {
            if (var2 != 1) {
               var6 = new StringBuilder();
               var6.append("Unexpected status line: ");
               var6.append(var0);
               throw new ProtocolException(var6.toString());
            }

            var3 = Protocol.HTTP_1_1;
         }
      } else {
         if (!var0.startsWith("ICY ")) {
            var6 = new StringBuilder();
            var6.append("Unexpected status line: ");
            var6.append(var0);
            throw new ProtocolException(var6.toString());
         }

         var3 = Protocol.HTTP_1_0;
         var1 = 4;
      }

      if (var0.length() >= var1 + 3) {
         try {
            var2 = Integer.parseInt(var0.substring(var1, var1 + 3));
         } catch (NumberFormatException var5) {
            var6 = new StringBuilder();
            var6.append("Unexpected status line: ");
            var6.append(var0);
            throw new ProtocolException(var6.toString());
         }

         String var4 = "";
         if (var0.length() > var1 + 3) {
            if (var0.charAt(var1 + 3) != ' ') {
               var6 = new StringBuilder();
               var6.append("Unexpected status line: ");
               var6.append(var0);
               throw new ProtocolException(var6.toString());
            }

            var4 = var0.substring(var1 + 4);
         }

         return new StatusLine(var3, var2, var4);
      } else {
         var6 = new StringBuilder();
         var6.append("Unexpected status line: ");
         var6.append(var0);
         throw new ProtocolException(var6.toString());
      }
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      String var1;
      if (this.protocol == Protocol.HTTP_1_0) {
         var1 = "HTTP/1.0";
      } else {
         var1 = "HTTP/1.1";
      }

      var2.append(var1);
      var2.append(' ');
      var2.append(this.code);
      if (this.message != null) {
         var2.append(' ');
         var2.append(this.message);
      }

      return var2.toString();
   }
}
