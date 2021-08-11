package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthCache;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
public class ResponseAuthCache implements HttpResponseInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   private void cache(AuthCache var1, HttpHost var2, AuthScheme var3) {
      if (this.log.isDebugEnabled()) {
         Log var4 = this.log;
         StringBuilder var5 = new StringBuilder();
         var5.append("Caching '");
         var5.append(var3.getSchemeName());
         var5.append("' auth scheme for ");
         var5.append(var2);
         var4.debug(var5.toString());
      }

      var1.put(var2, var3);
   }

   private boolean isCachable(AuthState var1) {
      AuthScheme var3 = var1.getAuthScheme();
      boolean var2 = false;
      if (var3 != null) {
         if (!var3.isComplete()) {
            return false;
         } else {
            String var4 = var3.getSchemeName();
            if (var4.equalsIgnoreCase("Basic") || var4.equalsIgnoreCase("Digest")) {
               var2 = true;
            }

            return var2;
         }
      } else {
         return false;
      }
   }

   private void uncache(AuthCache var1, HttpHost var2, AuthScheme var3) {
      if (this.log.isDebugEnabled()) {
         Log var4 = this.log;
         StringBuilder var5 = new StringBuilder();
         var5.append("Removing from cache '");
         var5.append(var3.getSchemeName());
         var5.append("' auth scheme for ");
         var5.append(var2);
         var4.debug(var5.toString());
      }

      var1.remove(var2);
   }

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      AuthCache var4 = (AuthCache)var2.getAttribute("http.auth.auth-cache");
      HttpHost var6 = (HttpHost)var2.getAttribute("http.target_host");
      AuthState var7 = (AuthState)var2.getAttribute("http.auth.target-scope");
      Object var8 = var4;
      int var3;
      HttpHost var14;
      if (var6 != null) {
         var8 = var4;
         if (var7 != null) {
            if (this.log.isDebugEnabled()) {
               Log var9 = this.log;
               StringBuilder var5 = new StringBuilder();
               var5.append("Target auth state: ");
               var5.append(var7.getState());
               var9.debug(var5.toString());
            }

            var8 = var4;
            if (this.isCachable(var7)) {
               SchemeRegistry var10 = (SchemeRegistry)var2.getAttribute("http.scheme-registry");
               var14 = var6;
               if (var6.getPort() < 0) {
                  Scheme var11 = var10.getScheme(var6);
                  var14 = new HttpHost(var6.getHostName(), var11.resolvePort(var6.getPort()), var6.getSchemeName());
               }

               var8 = var4;
               if (var4 == null) {
                  var8 = new BasicAuthCache();
                  var2.setAttribute("http.auth.auth-cache", var8);
               }

               var3 = null.$SwitchMap$org$apache$http$auth$AuthProtocolState[var7.getState().ordinal()];
               if (var3 != 1) {
                  if (var3 == 2) {
                     this.uncache((AuthCache)var8, var14, var7.getAuthScheme());
                  }
               } else {
                  this.cache((AuthCache)var8, var14, var7.getAuthScheme());
               }
            }
         }
      }

      var14 = (HttpHost)var2.getAttribute("http.proxy_host");
      AuthState var15 = (AuthState)var2.getAttribute("http.auth.proxy-scope");
      if (var14 != null && var15 != null) {
         if (this.log.isDebugEnabled()) {
            Log var12 = this.log;
            StringBuilder var16 = new StringBuilder();
            var16.append("Proxy auth state: ");
            var16.append(var15.getState());
            var12.debug(var16.toString());
         }

         if (this.isCachable(var15)) {
            Object var13 = var8;
            if (var8 == null) {
               var13 = new BasicAuthCache();
               var2.setAttribute("http.auth.auth-cache", var13);
            }

            var3 = null.$SwitchMap$org$apache$http$auth$AuthProtocolState[var15.getState().ordinal()];
            if (var3 != 1) {
               if (var3 != 2) {
                  return;
               }

               this.uncache((AuthCache)var13, var14, var15.getAuthScheme());
               return;
            }

            this.cache((AuthCache)var13, var14, var15.getAuthScheme());
         }
      }

   }
}
