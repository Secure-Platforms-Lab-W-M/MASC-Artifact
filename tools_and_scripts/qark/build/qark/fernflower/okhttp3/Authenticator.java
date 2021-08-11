package okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;

public interface Authenticator {
   Authenticator NONE = new Authenticator() {
      public Request authenticate(Route var1, Response var2) {
         return null;
      }
   };

   @Nullable
   Request authenticate(Route var1, Response var2) throws IOException;
}
