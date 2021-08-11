package org.apache.http.impl.conn;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultProxyRoutePlanner extends DefaultRoutePlanner {
   private final HttpHost proxy;

   public DefaultProxyRoutePlanner(HttpHost var1) {
      this(var1, (SchemePortResolver)null);
   }

   public DefaultProxyRoutePlanner(HttpHost var1, SchemePortResolver var2) {
      super(var2);
      this.proxy = (HttpHost)Args.notNull(var1, "Proxy host");
   }

   protected HttpHost determineProxy(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      return this.proxy;
   }
}
