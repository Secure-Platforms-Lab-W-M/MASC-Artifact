package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthCache;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

abstract class AuthenticationStrategyImpl implements AuthenticationStrategy {
   private static final List DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList("Negotiate", "Kerberos", "NTLM", "CredSSP", "Digest", "Basic"));
   private final int challengeCode;
   private final String headerName;
   private final Log log = LogFactory.getLog(this.getClass());

   AuthenticationStrategyImpl(int var1, String var2) {
      this.challengeCode = var1;
      this.headerName = var2;
   }

   public void authFailed(HttpHost var1, AuthScheme var2, HttpContext var3) {
      Args.notNull(var1, "Host");
      Args.notNull(var3, "HTTP context");
      AuthCache var5 = HttpClientContext.adapt(var3).getAuthCache();
      if (var5 != null) {
         if (this.log.isDebugEnabled()) {
            Log var6 = this.log;
            StringBuilder var4 = new StringBuilder();
            var4.append("Clearing cached auth scheme for ");
            var4.append(var1);
            var6.debug(var4.toString());
         }

         var5.remove(var1);
      }

   }

   public void authSucceeded(HttpHost var1, AuthScheme var2, HttpContext var3) {
      Args.notNull(var1, "Host");
      Args.notNull(var2, "Auth scheme");
      Args.notNull(var3, "HTTP context");
      HttpClientContext var5 = HttpClientContext.adapt(var3);
      if (this.isCachable(var2)) {
         AuthCache var4 = var5.getAuthCache();
         Object var7 = var4;
         if (var4 == null) {
            var7 = new BasicAuthCache();
            var5.setAuthCache((AuthCache)var7);
         }

         if (this.log.isDebugEnabled()) {
            Log var8 = this.log;
            StringBuilder var6 = new StringBuilder();
            var6.append("Caching '");
            var6.append(var2.getSchemeName());
            var6.append("' auth scheme for ");
            var6.append(var1);
            var8.debug(var6.toString());
         }

         ((AuthCache)var7).put(var1, var2);
      }

   }

   public Map getChallenges(HttpHost var1, HttpResponse var2, HttpContext var3) throws MalformedChallengeException {
      Args.notNull(var2, "HTTP response");
      Header[] var11 = var2.getHeaders(this.headerName);
      HashMap var12 = new HashMap(var11.length);
      int var7 = var11.length;

      for(int var6 = 0; var6 < var7; ++var6) {
         Header var8 = var11[var6];
         int var4;
         CharArrayBuffer var10;
         if (var8 instanceof FormattedHeader) {
            var10 = ((FormattedHeader)var8).getBuffer();
            var4 = ((FormattedHeader)var8).getValuePos();
         } else {
            String var9 = var8.getValue();
            if (var9 == null) {
               throw new MalformedChallengeException("Header value is null");
            }

            var10 = new CharArrayBuffer(var9.length());
            var10.append(var9);
            var4 = 0;
         }

         while(var4 < var10.length() && HTTP.isWhitespace(var10.charAt(var4))) {
            ++var4;
         }

         int var5;
         for(var5 = var4; var5 < var10.length() && !HTTP.isWhitespace(var10.charAt(var5)); ++var5) {
         }

         var12.put(var10.substring(var4, var5).toLowerCase(Locale.ROOT), var8);
      }

      return var12;
   }

   abstract Collection getPreferredAuthSchemes(RequestConfig var1);

   public boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, HttpContext var3) {
      Args.notNull(var2, "HTTP response");
      return var2.getStatusLine().getStatusCode() == this.challengeCode;
   }

   protected boolean isCachable(AuthScheme var1) {
      return var1 != null && var1.isComplete() ? var1.getSchemeName().equalsIgnoreCase("Basic") : false;
   }

   public Queue select(Map var1, HttpHost var2, HttpResponse var3, HttpContext var4) throws MalformedChallengeException {
      Args.notNull(var1, "Map of auth challenges");
      Args.notNull(var2, "Host");
      Args.notNull(var3, "HTTP response");
      Args.notNull(var4, "HTTP context");
      HttpClientContext var5 = HttpClientContext.adapt(var4);
      LinkedList var7 = new LinkedList();
      Lookup var8 = var5.getAuthSchemeRegistry();
      if (var8 == null) {
         this.log.debug("Auth scheme registry not set in the context");
         return var7;
      } else {
         CredentialsProvider var9 = var5.getCredentialsProvider();
         if (var9 == null) {
            this.log.debug("Credentials provider not set in the context");
            return var7;
         } else {
            Collection var6 = this.getPreferredAuthSchemes(var5.getRequestConfig());
            Object var12 = var6;
            if (var6 == null) {
               var12 = DEFAULT_SCHEME_PRIORITY;
            }

            if (this.log.isDebugEnabled()) {
               Log var15 = this.log;
               StringBuilder var10 = new StringBuilder();
               var10.append("Authentication schemes in the order of preference: ");
               var10.append(var12);
               var15.debug(var10.toString());
            }

            Iterator var16 = ((Collection)var12).iterator();

            while(var16.hasNext()) {
               String var13 = (String)var16.next();
               Header var17 = (Header)var1.get(var13.toLowerCase(Locale.ROOT));
               Log var18;
               StringBuilder var20;
               if (var17 != null) {
                  AuthSchemeProvider var11 = (AuthSchemeProvider)var8.lookup(var13);
                  if (var11 == null) {
                     if (this.log.isWarnEnabled()) {
                        var18 = this.log;
                        var20 = new StringBuilder();
                        var20.append("Authentication scheme ");
                        var20.append(var13);
                        var20.append(" not supported");
                        var18.warn(var20.toString());
                     }
                  } else {
                     AuthScheme var14 = var11.create(var4);
                     var14.processChallenge(var17);
                     Credentials var19 = var9.getCredentials(new AuthScope(var2, var14.getRealm(), var14.getSchemeName()));
                     if (var19 != null) {
                        var7.add(new AuthOption(var14, var19));
                     }
                  }
               } else if (this.log.isDebugEnabled()) {
                  var18 = this.log;
                  var20 = new StringBuilder();
                  var20.append("Challenge for ");
                  var20.append(var13);
                  var20.append(" authentication scheme not available");
                  var18.debug(var20.toString());
               }
            }

            return var7;
         }
      }
   }
}
