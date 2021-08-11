package okhttp3.internal.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Challenge;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

public final class HttpHeaders {
   private static final Pattern PARAMETER = Pattern.compile(" +([^ \"=]*)=(:?\"([^\"]*)\"|([^ \"=]*)) *(:?,|$)");
   private static final String QUOTED_STRING = "\"([^\"]*)\"";
   private static final String TOKEN = "([^ \"=]*)";

   private HttpHeaders() {
   }

   public static long contentLength(Headers var0) {
      return stringToLong(var0.get("Content-Length"));
   }

   public static long contentLength(Response var0) {
      return contentLength(var0.headers());
   }

   public static boolean hasBody(Response var0) {
      if (var0.request().method().equals("HEAD")) {
         return false;
      } else {
         int var1 = var0.code();
         if ((var1 < 100 || var1 >= 200) && var1 != 204 && var1 != 304) {
            return true;
         } else if (contentLength(var0) == -1L) {
            return "chunked".equalsIgnoreCase(var0.header("Transfer-Encoding"));
         } else {
            return true;
         }
      }
   }

   public static boolean hasVaryAll(Headers var0) {
      return varyFields(var0).contains("*");
   }

   public static boolean hasVaryAll(Response var0) {
      return hasVaryAll(var0.headers());
   }

   public static List parseChallenges(Headers var0, String var1) {
      ArrayList var4 = new ArrayList();
      Iterator var8 = var0.values(var1).iterator();

      while(true) {
         while(true) {
            int var3;
            do {
               if (!var8.hasNext()) {
                  return var4;
               }

               var1 = (String)var8.next();
               var3 = var1.indexOf(32);
            } while(var3 == -1);

            Matcher var5 = PARAMETER.matcher(var1);

            for(int var2 = var3; var5.find(var2); var2 = var5.end()) {
               if (var1.regionMatches(true, var5.start(1), "realm", 0, 5)) {
                  String var6 = var1.substring(0, var3);
                  String var7 = var5.group(3);
                  if (var7 != null) {
                     var4.add(new Challenge(var6, var7));
                     break;
                  }
               }
            }
         }
      }
   }

   public static int parseSeconds(String var0, int var1) {
      long var2;
      try {
         var2 = Long.parseLong(var0);
      } catch (NumberFormatException var4) {
         return var1;
      }

      if (var2 > 2147483647L) {
         return Integer.MAX_VALUE;
      } else {
         return var2 < 0L ? 0 : (int)var2;
      }
   }

   public static void receiveHeaders(CookieJar var0, HttpUrl var1, Headers var2) {
      if (var0 != CookieJar.NO_COOKIES) {
         List var3 = Cookie.parseAll(var1, var2);
         if (!var3.isEmpty()) {
            var0.saveFromResponse(var1, var3);
         }
      }
   }

   public static int skipUntil(String var0, int var1, String var2) {
      while(var1 < var0.length()) {
         if (var2.indexOf(var0.charAt(var1)) != -1) {
            return var1;
         }

         ++var1;
      }

      return var1;
   }

   public static int skipWhitespace(String var0, int var1) {
      while(var1 < var0.length()) {
         char var2 = var0.charAt(var1);
         if (var2 != ' ' && var2 != '\t') {
            return var1;
         }

         ++var1;
      }

      return var1;
   }

   private static long stringToLong(String var0) {
      if (var0 == null) {
         return -1L;
      } else {
         try {
            long var1 = Long.parseLong(var0);
            return var1;
         } catch (NumberFormatException var3) {
            return -1L;
         }
      }
   }

   public static Set varyFields(Headers var0) {
      Object var6 = Collections.emptySet();
      int var1 = 0;

      for(int var3 = var0.size(); var1 < var3; ++var1) {
         if ("Vary".equalsIgnoreCase(var0.name(var1))) {
            String var7 = var0.value(var1);
            Object var5 = var6;
            if (((Set)var6).isEmpty()) {
               var5 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
            }

            String[] var8 = var7.split(",");
            int var4 = var8.length;
            int var2 = 0;

            while(true) {
               var6 = var5;
               if (var2 >= var4) {
                  break;
               }

               ((Set)var5).add(var8[var2].trim());
               ++var2;
            }
         }
      }

      return (Set)var6;
   }

   private static Set varyFields(Response var0) {
      return varyFields(var0.headers());
   }

   public static Headers varyHeaders(Headers var0, Headers var1) {
      Set var6 = varyFields(var1);
      if (var6.isEmpty()) {
         return (new Headers.Builder()).build();
      } else {
         Headers.Builder var4 = new Headers.Builder();
         int var2 = 0;

         for(int var3 = var0.size(); var2 < var3; ++var2) {
            String var5 = var0.name(var2);
            if (var6.contains(var5)) {
               var4.add(var5, var0.value(var2));
            }
         }

         return var4.build();
      }
   }

   public static Headers varyHeaders(Response var0) {
      return varyHeaders(var0.networkResponse().request().headers(), var0.headers());
   }

   public static boolean varyMatches(Response var0, Headers var1, Request var2) {
      Iterator var4 = varyFields(var0).iterator();

      String var3;
      do {
         if (!var4.hasNext()) {
            return true;
         }

         var3 = (String)var4.next();
      } while(Util.equal(var1.values(var3), var2.headers(var3)));

      return false;
   }
}
