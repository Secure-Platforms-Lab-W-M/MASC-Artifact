package org.apache.http.impl.auth;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

public class HttpAuthenticator {
   private final Log log;

   public HttpAuthenticator() {
      this((Log)null);
   }

   public HttpAuthenticator(Log var1) {
      if (var1 == null) {
         var1 = LogFactory.getLog(this.getClass());
      }

      this.log = var1;
   }

   private Header doAuth(AuthScheme var1, Credentials var2, HttpRequest var3, HttpContext var4) throws AuthenticationException {
      return var1 instanceof ContextAwareAuthScheme ? ((ContextAwareAuthScheme)var1).authenticate(var2, var3, var4) : var1.authenticate(var2, var3);
   }

   private void ensureAuthScheme(AuthScheme var1) {
      Asserts.notNull(var1, "Auth scheme");
   }

   public void generateAuthResponse(HttpRequest var1, AuthState var2, HttpContext var3) throws HttpException, IOException {
      AuthScheme var6 = var2.getAuthScheme();
      Credentials var7 = var2.getCredentials();
      int var4 = null.$SwitchMap$org$apache$http$auth$AuthProtocolState[var2.getState().ordinal()];
      if (var4 != 1) {
         if (var4 != 3) {
            if (var4 == 4) {
               return;
            }
         } else {
            this.ensureAuthScheme(var6);
            if (var6.isConnectionBased()) {
               return;
            }
         }
      } else {
         Queue var5 = var2.getAuthOptions();
         if (var5 != null) {
            while(!var5.isEmpty()) {
               AuthOption var14 = (AuthOption)var5.remove();
               var6 = var14.getAuthScheme();
               var7 = var14.getCredentials();
               var2.update(var6, var7);
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
                  var1.addHeader(this.doAuth(var6, var7, var1, var3));
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

      if (var6 != null) {
         try {
            var1.addHeader(this.doAuth(var6, var7, var1, var3));
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

   public boolean handleAuthChallenge(HttpHost var1, HttpResponse var2, AuthenticationStrategy var3, AuthState var4, HttpContext var5) {
      MalformedChallengeException var10000;
      Log var24;
      StringBuilder var25;
      label131: {
         boolean var10001;
         try {
            if (this.log.isDebugEnabled()) {
               Log var7 = this.log;
               StringBuilder var8 = new StringBuilder();
               var8.append(var1.toHostString());
               var8.append(" requested authentication");
               var7.debug(var8.toString());
            }
         } catch (MalformedChallengeException var19) {
            var10000 = var19;
            var10001 = false;
            break label131;
         }

         Map var26;
         try {
            var26 = var3.getChallenges(var1, var2, var5);
            if (var26.isEmpty()) {
               this.log.debug("Response contains no authentication challenges");
               return false;
            }
         } catch (MalformedChallengeException var21) {
            var10000 = var21;
            var10001 = false;
            break label131;
         }

         int var6;
         AuthScheme var27;
         try {
            var27 = var4.getAuthScheme();
            var6 = null.$SwitchMap$org$apache$http$auth$AuthProtocolState[var4.getState().ordinal()];
         } catch (MalformedChallengeException var18) {
            var10000 = var18;
            var10001 = false;
            break label131;
         }

         label135: {
            if (var6 != 1 && var6 != 2) {
               if (var6 == 3) {
                  try {
                     var4.reset();
                     break label135;
                  } catch (MalformedChallengeException var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label131;
                  }
               }

               if (var6 == 4) {
                  return false;
               }

               if (var6 != 5) {
                  break label135;
               }
            } else if (var27 == null) {
               try {
                  this.log.debug("Auth scheme is null");
                  var3.authFailed(var1, (AuthScheme)null, var5);
                  var4.reset();
                  var4.setState(AuthProtocolState.FAILURE);
                  return false;
               } catch (MalformedChallengeException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label131;
               }
            }

            if (var27 != null) {
               Header var9;
               try {
                  var9 = (Header)var26.get(var27.getSchemeName().toLowerCase(Locale.ROOT));
               } catch (MalformedChallengeException var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label131;
               }

               if (var9 != null) {
                  try {
                     this.log.debug("Authorization challenge processed");
                     var27.processChallenge(var9);
                     if (var27.isComplete()) {
                        this.log.debug("Authentication failed");
                        var3.authFailed(var1, var4.getAuthScheme(), var5);
                        var4.reset();
                        var4.setState(AuthProtocolState.FAILURE);
                        return false;
                     }
                  } catch (MalformedChallengeException var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label131;
                  }

                  try {
                     var4.setState(AuthProtocolState.HANDSHAKE);
                     return true;
                  } catch (MalformedChallengeException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label131;
                  }
               }

               try {
                  var4.reset();
               } catch (MalformedChallengeException var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label131;
               }
            }
         }

         Queue var22;
         try {
            var22 = var3.select(var26, var1, var2, var5);
         } catch (MalformedChallengeException var14) {
            var10000 = var14;
            var10001 = false;
            break label131;
         }

         if (var22 == null) {
            return false;
         }

         try {
            if (var22.isEmpty()) {
               return false;
            }

            if (this.log.isDebugEnabled()) {
               var24 = this.log;
               var25 = new StringBuilder();
               var25.append("Selected authentication options: ");
               var25.append(var22);
               var24.debug(var25.toString());
            }
         } catch (MalformedChallengeException var13) {
            var10000 = var13;
            var10001 = false;
            break label131;
         }

         try {
            var4.setState(AuthProtocolState.CHALLENGED);
            var4.update(var22);
            return true;
         } catch (MalformedChallengeException var12) {
            var10000 = var12;
            var10001 = false;
         }
      }

      MalformedChallengeException var23 = var10000;
      if (this.log.isWarnEnabled()) {
         var24 = this.log;
         var25 = new StringBuilder();
         var25.append("Malformed challenge: ");
         var25.append(var23.getMessage());
         var24.warn(var25.toString());
      }

      var4.reset();
      return false;
   }

   public boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, AuthenticationStrategy var3, AuthState var4, HttpContext var5) {
      if (var3.isAuthenticationRequested(var1, var2, var5)) {
         this.log.debug("Authentication required");
         if (var4.getState() == AuthProtocolState.SUCCESS) {
            var3.authFailed(var1, var4.getAuthScheme(), var5);
         }

         return true;
      } else {
         int var6 = null.$SwitchMap$org$apache$http$auth$AuthProtocolState[var4.getState().ordinal()];
         if (var6 != 1 && var6 != 2) {
            if (var6 != 3) {
               var4.setState(AuthProtocolState.UNCHALLENGED);
            }
         } else {
            this.log.debug("Authentication succeeded");
            var4.setState(AuthProtocolState.SUCCESS);
            var3.authSucceeded(var1, var4.getAuthScheme(), var5);
         }

         return false;
      }
   }
}
