package org.apache.http.impl.execchain;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

public class RetryExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;
   private final HttpRequestRetryHandler retryHandler;

   public RetryExec(ClientExecChain var1, HttpRequestRetryHandler var2) {
      Args.notNull(var1, "HTTP request executor");
      Args.notNull(var2, "HTTP request retry handler");
      this.requestExecutor = var1;
      this.retryHandler = var2;
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Args.notNull(var1, "HTTP route");
      Args.notNull(var2, "HTTP request");
      Args.notNull(var3, "HTTP context");
      Header[] var6 = var2.getAllHeaders();
      int var5 = 1;

      while(true) {
         try {
            CloseableHttpResponse var13 = this.requestExecutor.execute(var1, var2, var3, var4);
            return var13;
         } catch (IOException var10) {
            if (var4 != null && var4.isAborted()) {
               this.log.debug("Request has been aborted");
               throw var10;
            }

            if (!this.retryHandler.retryRequest(var10, var5, var3)) {
               if (var10 instanceof NoHttpResponseException) {
                  StringBuilder var12 = new StringBuilder();
                  var12.append(var1.getTargetHost().toHostString());
                  var12.append(" failed to respond");
                  NoHttpResponseException var11 = new NoHttpResponseException(var12.toString());
                  var11.setStackTrace(var10.getStackTrace());
                  throw var11;
               }

               throw var10;
            }

            if (this.log.isInfoEnabled()) {
               Log var8 = this.log;
               StringBuilder var9 = new StringBuilder();
               var9.append("I/O exception (");
               var9.append(var10.getClass().getName());
               var9.append(") caught when processing request to ");
               var9.append(var1);
               var9.append(": ");
               var9.append(var10.getMessage());
               var8.info(var9.toString());
            }

            if (this.log.isDebugEnabled()) {
               this.log.debug(var10.getMessage(), var10);
            }

            if (!RequestEntityProxy.isRepeatable(var2)) {
               this.log.debug("Cannot retry non-repeatable request");
               throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity", var10);
            }

            var2.setHeaders(var6);
            if (this.log.isInfoEnabled()) {
               Log var7 = this.log;
               StringBuilder var14 = new StringBuilder();
               var14.append("Retrying request to ");
               var14.append(var1);
               var7.info(var14.toString());
            }

            ++var5;
         }
      }
   }
}
