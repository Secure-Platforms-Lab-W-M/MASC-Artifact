package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class RequestAuthCache implements HttpRequestInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   private void doPreemptiveAuth(HttpHost var1, AuthScheme var2, AuthState var3, CredentialsProvider var4) {
      String var5 = var2.getSchemeName();
      if (this.log.isDebugEnabled()) {
         Log var6 = this.log;
         StringBuilder var7 = new StringBuilder();
         var7.append("Re-using cached '");
         var7.append(var5);
         var7.append("' auth scheme for ");
         var7.append(var1);
         var6.debug(var7.toString());
      }

      Credentials var8 = var4.getCredentials(new AuthScope(var1, AuthScope.ANY_REALM, var5));
      if (var8 != null) {
         var3.update(var2, var8);
      } else {
         this.log.debug("No credentials for preemptive authentication");
      }
   }

   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      HttpClientContext var5 = HttpClientContext.adapt(var2);
      AuthCache var4 = var5.getAuthCache();
      if (var4 == null) {
         this.log.debug("Auth cache not set in the context");
      } else {
         CredentialsProvider var3 = var5.getCredentialsProvider();
         if (var3 == null) {
            this.log.debug("Credentials provider not set in the context");
         } else {
            RouteInfo var6 = var5.getHttpRoute();
            if (var6 == null) {
               this.log.debug("Route info not set in the context");
            } else {
               HttpHost var9 = var5.getTargetHost();
               if (var9 == null) {
                  this.log.debug("Target host not set in the context");
               } else {
                  HttpHost var8 = var9;
                  if (var9.getPort() < 0) {
                     var8 = new HttpHost(var9.getHostName(), var6.getTargetHost().getPort(), var9.getSchemeName());
                  }

                  AuthState var10 = var5.getTargetAuthState();
                  if (var10 != null && var10.getState() == AuthProtocolState.UNCHALLENGED) {
                     AuthScheme var7 = var4.get(var8);
                     if (var7 != null) {
                        this.doPreemptiveAuth(var8, var7, var10, var3);
                     }
                  }

                  var8 = var6.getProxyHost();
                  var10 = var5.getProxyAuthState();
                  if (var8 != null && var10 != null && var10.getState() == AuthProtocolState.UNCHALLENGED) {
                     AuthScheme var11 = var4.get(var8);
                     if (var11 != null) {
                        this.doPreemptiveAuth(var8, var11, var10, var3);
                     }
                  }

               }
            }
         }
      }
   }
}
