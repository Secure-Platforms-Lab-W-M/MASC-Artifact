package org.apache.http.impl.execchain;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

public class BackoffStrategyExec implements ClientExecChain {
   private final BackoffManager backoffManager;
   private final ConnectionBackoffStrategy connectionBackoffStrategy;
   private final ClientExecChain requestExecutor;

   public BackoffStrategyExec(ClientExecChain var1, ConnectionBackoffStrategy var2, BackoffManager var3) {
      Args.notNull(var1, "HTTP client request executor");
      Args.notNull(var2, "Connection backoff strategy");
      Args.notNull(var3, "Backoff manager");
      this.requestExecutor = var1;
      this.connectionBackoffStrategy = var2;
      this.backoffManager = var3;
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Args.notNull(var1, "HTTP route");
      Args.notNull(var2, "HTTP request");
      Args.notNull(var3, "HTTP context");

      CloseableHttpResponse var6;
      try {
         var6 = this.requestExecutor.execute(var1, var2, var3, var4);
      } catch (Exception var5) {
         if (false) {
            throw new NullPointerException();
         }

         if (this.connectionBackoffStrategy.shouldBackoff((Throwable)var5)) {
            this.backoffManager.backOff(var1);
         }

         if (!(var5 instanceof RuntimeException)) {
            if (!(var5 instanceof HttpException)) {
               if (var5 instanceof IOException) {
                  throw (IOException)var5;
               }

               throw new UndeclaredThrowableException(var5);
            }

            throw (HttpException)var5;
         }

         throw (RuntimeException)var5;
      }

      if (this.connectionBackoffStrategy.shouldBackoff((HttpResponse)var6)) {
         this.backoffManager.backOff(var1);
         return var6;
      } else {
         this.backoffManager.probe(var1);
         return var6;
      }
   }
}
