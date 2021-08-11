package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.entity.DeflateInputStreamFactory;
import org.apache.http.client.entity.GZIPInputStreamFactory;
import org.apache.http.client.entity.InputStreamFactory;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.protocol.HttpContext;

public class ResponseContentEncoding implements HttpResponseInterceptor {
   public static final String UNCOMPRESSED = "http.client.response.uncompressed";
   private final Lookup decoderRegistry;
   private final boolean ignoreUnknown;

   public ResponseContentEncoding() {
      this((Lookup)null);
   }

   public ResponseContentEncoding(Lookup var1) {
      this(var1, true);
   }

   public ResponseContentEncoding(Lookup var1, boolean var2) {
      if (var1 == null) {
         var1 = RegistryBuilder.create().register("gzip", GZIPInputStreamFactory.getInstance()).register("x-gzip", GZIPInputStreamFactory.getInstance()).register("deflate", DeflateInputStreamFactory.getInstance()).build();
      }

      this.decoderRegistry = (Lookup)var1;
      this.ignoreUnknown = var2;
   }

   public ResponseContentEncoding(boolean var1) {
      this((Lookup)null, var1);
   }

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      HttpEntity var5 = var1.getEntity();
      if (HttpClientContext.adapt(var2).getRequestConfig().isContentCompressionEnabled() && var5 != null && var5.getContentLength() != 0L) {
         Header var9 = var5.getContentEncoding();
         if (var9 != null) {
            HeaderElement[] var11 = var9.getElements();
            int var4 = var11.length;

            for(int var3 = 0; var3 < var4; ++var3) {
               HeaderElement var10 = var11[var3];
               String var6 = var10.getName().toLowerCase(Locale.ROOT);
               InputStreamFactory var7 = (InputStreamFactory)this.decoderRegistry.lookup(var6);
               if (var7 != null) {
                  var1.setEntity(new DecompressingEntity(var1.getEntity(), var7));
                  var1.removeHeaders("Content-Length");
                  var1.removeHeaders("Content-Encoding");
                  var1.removeHeaders("Content-MD5");
               } else if (!"identity".equals(var6) && !this.ignoreUnknown) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("Unsupported Content-Encoding: ");
                  var8.append(var10.getName());
                  throw new HttpException(var8.toString());
               }
            }
         }
      }

   }
}
