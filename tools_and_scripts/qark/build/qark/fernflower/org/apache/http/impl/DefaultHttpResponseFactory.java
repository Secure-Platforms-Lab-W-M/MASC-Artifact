package org.apache.http.impl;

import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.ProtocolVersion;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultHttpResponseFactory implements HttpResponseFactory {
   public static final DefaultHttpResponseFactory INSTANCE = new DefaultHttpResponseFactory();
   protected final ReasonPhraseCatalog reasonCatalog;

   public DefaultHttpResponseFactory() {
      this(EnglishReasonPhraseCatalog.INSTANCE);
   }

   public DefaultHttpResponseFactory(ReasonPhraseCatalog var1) {
      this.reasonCatalog = (ReasonPhraseCatalog)Args.notNull(var1, "Reason phrase catalog");
   }

   protected Locale determineLocale(HttpContext var1) {
      return Locale.getDefault();
   }

   public HttpResponse newHttpResponse(ProtocolVersion var1, int var2, HttpContext var3) {
      Args.notNull(var1, "HTTP version");
      Locale var4 = this.determineLocale(var3);
      return new BasicHttpResponse(new BasicStatusLine(var1, var2, this.reasonCatalog.getReason(var2, var4)), this.reasonCatalog, var4);
   }

   public HttpResponse newHttpResponse(StatusLine var1, HttpContext var2) {
      Args.notNull(var1, "Status line");
      return new BasicHttpResponse(var1, this.reasonCatalog, this.determineLocale(var2));
   }
}
