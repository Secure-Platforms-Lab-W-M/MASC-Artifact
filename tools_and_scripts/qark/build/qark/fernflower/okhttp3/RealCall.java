package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;

final class RealCall implements Call {
   final OkHttpClient client;
   final EventListener eventListener;
   private boolean executed;
   final boolean forWebSocket;
   final Request originalRequest;
   final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;

   RealCall(OkHttpClient var1, Request var2, boolean var3) {
      EventListener.Factory var4 = var1.eventListenerFactory();
      this.client = var1;
      this.originalRequest = var2;
      this.forWebSocket = var3;
      this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(var1, var3);
      this.eventListener = var4.create(this);
   }

   private void captureCallStackTrace() {
      Object var1 = Platform.get().getStackTraceForCloseable("response.body().close()");
      this.retryAndFollowUpInterceptor.setCallStackTrace(var1);
   }

   public void cancel() {
      this.retryAndFollowUpInterceptor.cancel();
   }

   public RealCall clone() {
      return new RealCall(this.client, this.originalRequest, this.forWebSocket);
   }

   public void enqueue(Callback var1) {
      synchronized(this){}

      label140: {
         Throwable var10000;
         boolean var10001;
         label135: {
            try {
               if (!this.executed) {
                  this.executed = true;
                  break label140;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label135;
            }

            label128:
            try {
               throw new IllegalStateException("Already Executed");
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label128;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }

      this.captureCallStackTrace();
      this.client.dispatcher().enqueue(new RealCall.AsyncCall(var1));
   }

   public Response execute() throws IOException {
      synchronized(this){}

      Throwable var10000;
      Throwable var32;
      boolean var10001;
      label311: {
         label315: {
            try {
               if (this.executed) {
                  break label315;
               }

               this.executed = true;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label311;
            }

            this.captureCallStackTrace();

            label316: {
               Response var1;
               try {
                  this.client.dispatcher().executed(this);
                  var1 = this.getResponseWithInterceptorChain();
               } catch (Throwable var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label316;
               }

               if (var1 != null) {
                  this.client.dispatcher().finished(this);
                  return var1;
               }

               label294:
               try {
                  throw new IOException("Canceled");
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label294;
               }
            }

            var32 = var10000;
            this.client.dispatcher().finished(this);
            throw var32;
         }

         label304:
         try {
            throw new IllegalStateException("Already Executed");
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label304;
         }
      }

      while(true) {
         var32 = var10000;

         try {
            throw var32;
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            continue;
         }
      }
   }

   Response getResponseWithInterceptorChain() throws IOException {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.client.interceptors());
      var1.add(this.retryAndFollowUpInterceptor);
      var1.add(new BridgeInterceptor(this.client.cookieJar()));
      var1.add(new CacheInterceptor(this.client.internalCache()));
      var1.add(new ConnectInterceptor(this.client));
      if (!this.forWebSocket) {
         var1.addAll(this.client.networkInterceptors());
      }

      var1.add(new CallServerInterceptor(this.forWebSocket));
      return (new RealInterceptorChain(var1, (StreamAllocation)null, (HttpCodec)null, (RealConnection)null, 0, this.originalRequest)).proceed(this.originalRequest);
   }

   public boolean isCanceled() {
      return this.retryAndFollowUpInterceptor.isCanceled();
   }

   public boolean isExecuted() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.executed;
      } finally {
         ;
      }

      return var1;
   }

   String redactedUrl() {
      return this.originalRequest.url().redact();
   }

   public Request request() {
      return this.originalRequest;
   }

   StreamAllocation streamAllocation() {
      return this.retryAndFollowUpInterceptor.streamAllocation();
   }

   String toLoggableString() {
      StringBuilder var2 = new StringBuilder();
      String var1;
      if (this.isCanceled()) {
         var1 = "canceled ";
      } else {
         var1 = "";
      }

      var2.append(var1);
      if (this.forWebSocket) {
         var1 = "web socket";
      } else {
         var1 = "call";
      }

      var2.append(var1);
      var2.append(" to ");
      var2.append(this.redactedUrl());
      return var2.toString();
   }

   final class AsyncCall extends NamedRunnable {
      private final Callback responseCallback;

      AsyncCall(Callback var2) {
         super("OkHttp %s", RealCall.this.redactedUrl());
         this.responseCallback = var2;
      }

      protected void execute() {
         // $FF: Couldn't be decompiled
      }

      RealCall get() {
         return RealCall.this;
      }

      String host() {
         return RealCall.this.originalRequest.url().host();
      }

      Request request() {
         return RealCall.this.originalRequest;
      }
   }
}
