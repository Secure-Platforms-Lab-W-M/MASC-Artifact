package okhttp3;

import java.util.Collections;
import java.util.List;

public interface CookieJar {
   CookieJar NO_COOKIES = new CookieJar() {
      public List loadForRequest(HttpUrl var1) {
         return Collections.emptyList();
      }

      public void saveFromResponse(HttpUrl var1, List var2) {
      }
   };

   List loadForRequest(HttpUrl var1);

   void saveFromResponse(HttpUrl var1, List var2);
}
