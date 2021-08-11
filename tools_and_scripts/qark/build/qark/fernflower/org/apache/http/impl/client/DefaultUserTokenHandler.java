package org.apache.http.impl.client;

import java.security.Principal;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpConnection;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;

public class DefaultUserTokenHandler implements UserTokenHandler {
   public static final DefaultUserTokenHandler INSTANCE = new DefaultUserTokenHandler();

   private static Principal getAuthPrincipal(AuthState var0) {
      AuthScheme var1 = var0.getAuthScheme();
      if (var1 != null && var1.isComplete() && var1.isConnectionBased()) {
         Credentials var2 = var0.getCredentials();
         if (var2 != null) {
            return var2.getUserPrincipal();
         }
      }

      return null;
   }

   public Object getUserToken(HttpContext var1) {
      HttpClientContext var3 = HttpClientContext.adapt(var1);
      Principal var4 = null;
      AuthState var2 = var3.getTargetAuthState();
      Principal var5;
      if (var2 != null) {
         var5 = getAuthPrincipal(var2);
         var4 = var5;
         if (var5 == null) {
            var4 = getAuthPrincipal(var3.getProxyAuthState());
         }
      }

      var5 = var4;
      if (var4 == null) {
         HttpConnection var6 = var3.getConnection();
         var5 = var4;
         if (var6.isOpen()) {
            var5 = var4;
            if (var6 instanceof ManagedHttpClientConnection) {
               SSLSession var7 = ((ManagedHttpClientConnection)var6).getSSLSession();
               var5 = var4;
               if (var7 != null) {
                  var5 = var7.getLocalPrincipal();
               }
            }
         }
      }

      return var5;
   }
}
