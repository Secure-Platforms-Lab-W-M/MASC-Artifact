package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.net.ssl.SSLException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultHttpRequestRetryHandler implements HttpRequestRetryHandler {
   public static final DefaultHttpRequestRetryHandler INSTANCE = new DefaultHttpRequestRetryHandler();
   private final Set nonRetriableClasses;
   private final boolean requestSentRetryEnabled;
   private final int retryCount;

   public DefaultHttpRequestRetryHandler() {
      this(3, false);
   }

   public DefaultHttpRequestRetryHandler(int var1, boolean var2) {
      this(var1, var2, Arrays.asList(InterruptedIOException.class, UnknownHostException.class, ConnectException.class, SSLException.class));
   }

   protected DefaultHttpRequestRetryHandler(int var1, boolean var2, Collection var3) {
      this.retryCount = var1;
      this.requestSentRetryEnabled = var2;
      this.nonRetriableClasses = new HashSet();
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Class var4 = (Class)var5.next();
         this.nonRetriableClasses.add(var4);
      }

   }

   public int getRetryCount() {
      return this.retryCount;
   }

   protected boolean handleAsIdempotent(HttpRequest var1) {
      return var1 instanceof HttpEntityEnclosingRequest ^ true;
   }

   public boolean isRequestSentRetryEnabled() {
      return this.requestSentRetryEnabled;
   }

   @Deprecated
   protected boolean requestIsAborted(HttpRequest var1) {
      HttpRequest var2 = var1;
      if (var1 instanceof RequestWrapper) {
         var2 = ((RequestWrapper)var1).getOriginal();
      }

      return var2 instanceof HttpUriRequest && ((HttpUriRequest)var2).isAborted();
   }

   public boolean retryRequest(IOException var1, int var2, HttpContext var3) {
      Args.notNull(var1, "Exception parameter");
      Args.notNull(var3, "HTTP context");
      if (var2 > this.retryCount) {
         return false;
      } else if (this.nonRetriableClasses.contains(var1.getClass())) {
         return false;
      } else {
         Iterator var4 = this.nonRetriableClasses.iterator();

         do {
            if (!var4.hasNext()) {
               HttpClientContext var5 = HttpClientContext.adapt(var3);
               HttpRequest var6 = var5.getRequest();
               if (this.requestIsAborted(var6)) {
                  return false;
               }

               if (this.handleAsIdempotent(var6)) {
                  return true;
               }

               if (var5.isRequestSent()) {
                  if (this.requestSentRetryEnabled) {
                     return true;
                  }

                  return false;
               }

               return true;
            }
         } while(!((Class)var4.next()).isInstance(var1));

         return false;
      }
   }
}
