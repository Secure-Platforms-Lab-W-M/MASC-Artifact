package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

public class FutureRequestExecutionService implements Closeable {
   private final AtomicBoolean closed = new AtomicBoolean(false);
   private final ExecutorService executorService;
   private final HttpClient httpclient;
   private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();

   public FutureRequestExecutionService(HttpClient var1, ExecutorService var2) {
      this.httpclient = var1;
      this.executorService = var2;
   }

   public void close() throws IOException {
      this.closed.set(true);
      this.executorService.shutdownNow();
      HttpClient var1 = this.httpclient;
      if (var1 instanceof Closeable) {
         ((Closeable)var1).close();
      }

   }

   public HttpRequestFutureTask execute(HttpUriRequest var1, HttpContext var2, ResponseHandler var3) {
      return this.execute(var1, var2, var3, (FutureCallback)null);
   }

   public HttpRequestFutureTask execute(HttpUriRequest var1, HttpContext var2, ResponseHandler var3, FutureCallback var4) {
      if (!this.closed.get()) {
         this.metrics.getScheduledConnections().incrementAndGet();
         HttpRequestFutureTask var5 = new HttpRequestFutureTask(var1, new HttpRequestTaskCallable(this.httpclient, var1, var2, var3, var4, this.metrics));
         this.executorService.execute(var5);
         return var5;
      } else {
         throw new IllegalStateException("Close has been called on this httpclient instance.");
      }
   }

   public FutureRequestExecutionMetrics metrics() {
      return this.metrics;
   }
}
