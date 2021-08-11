package org.apache.http.client;

import java.util.Date;
import java.util.List;
import org.apache.http.cookie.Cookie;

public interface CookieStore {
   void addCookie(Cookie var1);

   void clear();

   boolean clearExpired(Date var1);

   List getCookies();
}
