package org.apache.http.client.protocol;

import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

@Deprecated
abstract class RequestAuthenticationBase implements HttpRequestInterceptor {
   final Log log = LogFactory.getLog(this.getClass());

   public RequestAuthenticationBase() {
   }

   private Header authenticate(AuthScheme var1, Credentials var2, HttpRequest var3, HttpContext var4) throws AuthenticationException {
      Asserts.notNull(var1, "Auth scheme");
      return var1 instanceof ContextAwareAuthScheme ? ((ContextAwareAuthScheme)var1).authenticate(var2, var3, var4) : var1.authenticate(var2, var3);
   }

   private void ensureAuthScheme(AuthScheme var1) {
      Asserts.notNull(var1, "Auth scheme");
   }

   void process(AuthState var1, HttpRequest var2, HttpContext var3) {
      AuthScheme var6 = var1.getAuthScheme();
      Credentials var7 = var1.getCredentials();
      int var4 = null.$SwitchMap$org$apache$http$auth$AuthProtocolState[var1.getState().ordinal()];
      if (var4 != 1) {
         if (var4 != 2) {
            if (var4 == 3) {
               Queue var5 = var1.getAuthOptions();
               if (var5 != null) {
                  while(!var5.isEmpty()) {
                     AuthOption var14 = (AuthOption)var5.remove();
                     var6 = var14.getAuthScheme();
                     var7 = var14.getCredentials();
                     var1.update(var6, var7);
                     Log var8;
                     StringBuilder var9;
                     if (this.log.isDebugEnabled()) {
                        var8 = this.log;
                        var9 = new StringBuilder();
                        var9.append("Generating response to an authentication challenge using ");
                        var9.append(var6.getSchemeName());
                        var9.append(" scheme");
                        var8.debug(var9.toString());
                     }

                     try {
                        var2.addHeader(this.authenticate(var6, var7, var2, var3));
                        return;
                     } catch (AuthenticationException var10) {
                        if (this.log.isWarnEnabled()) {
                           var8 = this.log;
                           var9 = new StringBuilder();
                           var9.append(var6);
                           var9.append(" authentication error: ");
                           var9.append(var10.getMessage());
                           var8.warn(var9.toString());
                        }
                     }
                  }

                  return;
               }

               this.ensureAuthScheme(var6);
            }
         } else {
            this.ensureAuthScheme(var6);
            if (var6.isConnectionBased()) {
               return;
            }
         }

         if (var6 != null) {
            try {
               var2.addHeader(this.authenticate(var6, var7, var2, var3));
               return;
            } catch (AuthenticationException var11) {
               if (this.log.isErrorEnabled()) {
                  Log var12 = this.log;
                  StringBuilder var13 = new StringBuilder();
                  var13.append(var6);
                  var13.append(" authentication error: ");
                  var13.append(var11.getMessage());
                  var12.error(var13.toString());
               }
            }
         }

      }
   }
}
