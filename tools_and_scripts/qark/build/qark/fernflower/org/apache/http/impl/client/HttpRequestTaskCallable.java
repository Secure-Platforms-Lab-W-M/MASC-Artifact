package org.apache.http.impl.client;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

class HttpRequestTaskCallable implements Callable {
   private final FutureCallback callback;
   private final AtomicBoolean cancelled = new AtomicBoolean(false);
   private final HttpContext context;
   private long ended = -1L;
   private final HttpClient httpclient;
   private final FutureRequestExecutionMetrics metrics;
   private final HttpUriRequest request;
   private final ResponseHandler responseHandler;
   private final long scheduled = System.currentTimeMillis();
   private long started = -1L;

   HttpRequestTaskCallable(HttpClient var1, HttpUriRequest var2, HttpContext var3, ResponseHandler var4, FutureCallback var5, FutureRequestExecutionMetrics var6) {
      this.httpclient = var1;
      this.responseHandler = var4;
      this.request = var2;
      this.context = var3;
      this.callback = var5;
      this.metrics = var6;
   }

   public Object call() throws Exception {
      if (!this.cancelled.get()) {
         Object var6;
         try {
            this.metrics.getActiveConnections().incrementAndGet();
            this.started = System.currentTimeMillis();

            try {
               this.metrics.getScheduledConnections().decrementAndGet();
               var6 = this.httpclient.execute(this.request, this.responseHandler, this.context);
               this.ended = System.currentTimeMillis();
               this.metrics.getSuccessfulConnections().increment(this.started);
               if (this.callback != null) {
                  this.callback.completed(var6);
               }
            } catch (Exception var4) {
               this.metrics.getFailedConnections().increment(this.started);
               this.ended = System.currentTimeMillis();
               if (this.callback != null) {
                  this.callback.failed(var4);
               }

               throw var4;
            }
         } finally {
            this.metrics.getRequests().increment(this.started);
            this.metrics.getTasks().increment(this.started);
            this.metrics.getActiveConnections().decrementAndGet();
         }

         return var6;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("call has been cancelled for request ");
         var1.append(this.request.getURI());
         throw new IllegalStateException(var1.toString());
      }
   }

   public void cancel() {
      this.cancelled.set(true);
      FutureCallback var1 = this.callback;
      if (var1 != null) {
         var1.cancelled();
      }

   }

   public long getEnded() {
      return this.ended;
   }

   public long getScheduled() {
      return this.scheduled;
   }

   public long getStarted() {
      return this.started;
   }
}
