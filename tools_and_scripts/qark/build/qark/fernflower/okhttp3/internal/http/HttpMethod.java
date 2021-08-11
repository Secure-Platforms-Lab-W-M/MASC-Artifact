package okhttp3.internal.http;

public final class HttpMethod {
   private HttpMethod() {
   }

   public static boolean invalidatesCache(String var0) {
      return var0.equals("POST") || var0.equals("PATCH") || var0.equals("PUT") || var0.equals("DELETE") || var0.equals("MOVE");
   }

   public static boolean permitsRequestBody(String var0) {
      return requiresRequestBody(var0) || var0.equals("OPTIONS") || var0.equals("DELETE") || var0.equals("PROPFIND") || var0.equals("MKCOL") || var0.equals("LOCK");
   }

   public static boolean redirectsToGet(String var0) {
      return var0.equals("PROPFIND") ^ true;
   }

   public static boolean redirectsWithBody(String var0) {
      return var0.equals("PROPFIND");
   }

   public static boolean requiresRequestBody(String var0) {
      return var0.equals("POST") || var0.equals("PUT") || var0.equals("PATCH") || var0.equals("PROPPATCH") || var0.equals("REPORT");
   }
}
