package org.apache.http.impl.client;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthCache;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
class AuthenticationStrategyAdaptor implements AuthenticationStrategy {
   private final AuthenticationHandler handler;
   private final Log log = LogFactory.getLog(this.getClass());

   public AuthenticationStrategyAdaptor(AuthenticationHandler var1) {
      this.handler = var1;
   }

   private boolean isCachable(AuthScheme var1) {
      return var1 != null && var1.isComplete() ? var1.getSchemeName().equalsIgnoreCase("Basic") : false;
   }

   public void authFailed(HttpHost var1, AuthScheme var2, HttpContext var3) {
      AuthCache var6 = (AuthCache)var3.getAttribute("http.auth.auth-cache");
      if (var6 != null) {
         if (this.log.isDebugEnabled()) {
            Log var4 = this.log;
            StringBuilder var5 = new StringBuilder();
            var5.append("Removing from cache '");
            var5.append(var2.getSchemeName());
            var5.append("' auth scheme for ");
            var5.append(var1);
            var4.debug(var5.toString());
         }

         var6.remove(var1);
      }
   }

   public void authSucceeded(HttpHost var1, AuthScheme var2, HttpContext var3) {
      AuthCache var5 = (AuthCache)var3.getAttribute("http.auth.auth-cache");
      if (this.isCachable(var2)) {
         Object var4 = var5;
         if (var5 == null) {
            var4 = new BasicAuthCache();
            var3.setAttribute("http.auth.auth-cache", var4);
         }

         if (this.log.isDebugEnabled()) {
            Log var7 = this.log;
            StringBuilder var6 = new StringBuilder();
            var6.append("Caching '");
            var6.append(var2.getSchemeName());
            var6.append("' auth scheme for ");
            var6.append(var1);
            var7.debug(var6.toString());
         }

         ((AuthCache)var4).put(var1, var2);
      }

   }

   public Map getChallenges(HttpHost var1, HttpResponse var2, HttpContext var3) throws MalformedChallengeException {
      return this.handler.getChallenges(var2, var3);
   }

   public AuthenticationHandler getHandler() {
      return this.handler;
   }

   public boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, HttpContext var3) {
      return this.handler.isAuthenticationRequested(var2, var3);
   }

   public Queue select(Map var1, HttpHost var2, HttpResponse var3, HttpContext var4) throws MalformedChallengeException {
      Args.notNull(var1, "Map of auth challenges");
      Args.notNull(var2, "Host");
      Args.notNull(var3, "HTTP response");
      Args.notNull(var4, "HTTP context");
      LinkedList var5 = new LinkedList();
      CredentialsProvider var6 = (CredentialsProvider)var4.getAttribute("http.auth.credentials-provider");
      if (var6 == null) {
         this.log.debug("Credentials provider not set in the context");
         return var5;
      } else {
         AuthScheme var9;
         try {
            var9 = this.handler.selectScheme(var1, var3, var4);
         } catch (AuthenticationException var7) {
            if (this.log.isWarnEnabled()) {
               this.log.warn(var7.getMessage(), var7);
            }

            return var5;
         }

         var9.processChallenge((Header)var1.get(var9.getSchemeName().toLowerCase(Locale.ROOT)));
         Credentials var8 = var6.getCredentials(new AuthScope(var2.getHostName(), var2.getPort(), var9.getRealm(), var9.getSchemeName()));
         if (var8 != null) {
            var5.add(new AuthOption(var9, var8));
         }

         return var5;
      }
   }
}
