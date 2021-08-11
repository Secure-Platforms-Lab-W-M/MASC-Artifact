package okhttp3;

import java.nio.charset.Charset;
import okio.ByteString;

public final class Credentials {
   private Credentials() {
   }

   public static String basic(String var0, String var1) {
      return basic(var0, var1, Charset.forName("ISO-8859-1"));
   }

   public static String basic(String var0, String var1, Charset var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var0);
      var3.append(":");
      var3.append(var1);
      var0 = ByteString.method_6(var3.toString().getBytes(var2)).base64();
      StringBuilder var4 = new StringBuilder();
      var4.append("Basic ");
      var4.append(var0);
      return var4.toString();
   }
}
