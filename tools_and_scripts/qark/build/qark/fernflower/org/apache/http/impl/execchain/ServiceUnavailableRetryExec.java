package org.apache.http.impl.execchain;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

public class ServiceUnavailableRetryExec implements ClientExecChain {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain requestExecutor;
   private final ServiceUnavailableRetryStrategy retryStrategy;

   public ServiceUnavailableRetryExec(ClientExecChain var1, ServiceUnavailableRetryStrategy var2) {
      Args.notNull(var1, "HTTP request executor");
      Args.notNull(var2, "Retry strategy");
      this.requestExecutor = var1;
      this.retryStrategy = var2;
   }

   public CloseableHttpResponse execute(HttpRoute param1, HttpRequestWrapper param2, HttpClientContext param3, HttpExecutionAware param4) throws IOException, HttpException {
      // $FF: Couldn't be decompiled
   }
}
