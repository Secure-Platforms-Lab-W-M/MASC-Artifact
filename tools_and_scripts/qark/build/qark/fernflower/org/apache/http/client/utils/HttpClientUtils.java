package org.apache.http.client.utils;

import java.io.Closeable;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
   private HttpClientUtils() {
   }

   public static void closeQuietly(HttpResponse var0) {
      if (var0 != null) {
         HttpEntity var2 = var0.getEntity();
         if (var2 != null) {
            try {
               EntityUtils.consume(var2);
               return;
            } catch (IOException var1) {
            }
         }
      }

   }

   public static void closeQuietly(HttpClient var0) {
      if (var0 != null && var0 instanceof Closeable) {
         try {
            ((Closeable)var0).close();
            return;
         } catch (IOException var1) {
         }
      }

   }

   public static void closeQuietly(CloseableHttpResponse var0) {
      if (var0 != null) {
         try {
            EntityUtils.consume(var0.getEntity());
         } finally {
            try {
               var0.close();
            } catch (IOException var5) {
               boolean var10001 = false;
               return;
            }
         }

      }
   }
}
