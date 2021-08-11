package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {
   private static final List DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList("Negotiate", "NTLM", "Digest", "Basic"));
   private final Log log = LogFactory.getLog(this.getClass());

   protected List getAuthPreferences() {
      return DEFAULT_SCHEME_PRIORITY;
   }

   protected List getAuthPreferences(HttpResponse var1, HttpContext var2) {
      return this.getAuthPreferences();
   }

   protected Map parseChallenges(Header[] var1) throws MalformedChallengeException {
      HashMap var7 = new HashMap(var1.length);
      int var5 = var1.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Header var8 = var1[var4];
         int var2;
         CharArrayBuffer var6;
         if (var8 instanceof FormattedHeader) {
            var6 = ((FormattedHeader)var8).getBuffer();
            var2 = ((FormattedHeader)var8).getValuePos();
         } else {
            String var9 = var8.getValue();
            if (var9 == null) {
               throw new MalformedChallengeException("Header value is null");
            }

            var6 = new CharArrayBuffer(var9.length());
            var6.append(var9);
            var2 = 0;
         }

         while(var2 < var6.length() && HTTP.isWhitespace(var6.charAt(var2))) {
            ++var2;
         }

         int var3;
         for(var3 = var2; var3 < var6.length() && !HTTP.isWhitespace(var6.charAt(var3)); ++var3) {
         }

         var7.put(var6.substring(var2, var3).toLowerCase(Locale.ROOT), var8);
      }

      return var7;
   }

   public AuthScheme selectScheme(Map var1, HttpResponse var2, HttpContext var3) throws AuthenticationException {
      AuthSchemeRegistry var5 = (AuthSchemeRegistry)var3.getAttribute("http.authscheme-registry");
      Asserts.notNull(var5, "AuthScheme registry");
      List var4 = this.getAuthPreferences(var2, var3);
      List var11 = var4;
      if (var4 == null) {
         var11 = DEFAULT_SCHEME_PRIORITY;
      }

      if (this.log.isDebugEnabled()) {
         Log var12 = this.log;
         StringBuilder var6 = new StringBuilder();
         var6.append("Authentication schemes in the order of preference: ");
         var6.append(var11);
         var12.debug(var6.toString());
      }

      var4 = null;
      Iterator var15 = var11.iterator();

      AuthScheme var13;
      while(true) {
         var13 = var4;
         if (!var15.hasNext()) {
            break;
         }

         String var7 = (String)var15.next();
         StringBuilder var8;
         Log var14;
         if ((Header)var1.get(var7.toLowerCase(Locale.ENGLISH)) != null) {
            if (this.log.isDebugEnabled()) {
               var14 = this.log;
               var8 = new StringBuilder();
               var8.append(var7);
               var8.append(" authentication scheme selected");
               var14.debug(var8.toString());
            }

            try {
               var13 = var5.getAuthScheme(var7, var2.getParams());
               break;
            } catch (IllegalStateException var9) {
               if (this.log.isWarnEnabled()) {
                  var14 = this.log;
                  var8 = new StringBuilder();
                  var8.append("Authentication scheme ");
                  var8.append(var7);
                  var8.append(" not supported");
                  var14.warn(var8.toString());
               }
            }
         } else if (this.log.isDebugEnabled()) {
            var14 = this.log;
            var8 = new StringBuilder();
            var8.append("Challenge for ");
            var8.append(var7);
            var8.append(" authentication scheme not available");
            var14.debug(var8.toString());
         }
      }

      if (var13 != null) {
         return var13;
      } else {
         StringBuilder var10 = new StringBuilder();
         var10.append("Unable to respond to any of these challenges: ");
         var10.append(var1);
         throw new AuthenticationException(var10.toString());
      }
   }
}
