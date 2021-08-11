package org.apache.http.impl.client;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;

public class StandardHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {
   private final Map idempotentMethods;

   public StandardHttpRequestRetryHandler() {
      this(3, false);
   }

   public StandardHttpRequestRetryHandler(int var1, boolean var2) {
      super(var1, var2);
      ConcurrentHashMap var3 = new ConcurrentHashMap();
      this.idempotentMethods = var3;
      var3.put("GET", Boolean.TRUE);
      this.idempotentMethods.put("HEAD", Boolean.TRUE);
      this.idempotentMethods.put("PUT", Boolean.TRUE);
      this.idempotentMethods.put("DELETE", Boolean.TRUE);
      this.idempotentMethods.put("OPTIONS", Boolean.TRUE);
      this.idempotentMethods.put("TRACE", Boolean.TRUE);
   }

   protected boolean handleAsIdempotent(HttpRequest var1) {
      String var2 = var1.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
      Boolean var3 = (Boolean)this.idempotentMethods.get(var2);
      return var3 != null && var3;
   }
}
