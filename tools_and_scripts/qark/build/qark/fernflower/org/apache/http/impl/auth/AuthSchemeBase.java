package org.apache.http.impl.auth;

import java.util.Locale;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public abstract class AuthSchemeBase implements ContextAwareAuthScheme {
   protected ChallengeState challengeState;

   public AuthSchemeBase() {
   }

   @Deprecated
   public AuthSchemeBase(ChallengeState var1) {
      this.challengeState = var1;
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      return this.authenticate(var1, var2);
   }

   public ChallengeState getChallengeState() {
      return this.challengeState;
   }

   public boolean isProxy() {
      ChallengeState var1 = this.challengeState;
      return var1 != null && var1 == ChallengeState.PROXY;
   }

   protected abstract void parseChallenge(CharArrayBuffer var1, int var2, int var3) throws MalformedChallengeException;

   public void processChallenge(Header var1) throws MalformedChallengeException {
      Args.notNull(var1, "Header");
      String var4 = var1.getName();
      StringBuilder var6;
      if (var4.equalsIgnoreCase("WWW-Authenticate")) {
         this.challengeState = ChallengeState.TARGET;
      } else {
         if (!var4.equalsIgnoreCase("Proxy-Authenticate")) {
            var6 = new StringBuilder();
            var6.append("Unexpected header name: ");
            var6.append(var4);
            throw new MalformedChallengeException(var6.toString());
         }

         this.challengeState = ChallengeState.PROXY;
      }

      int var2;
      CharArrayBuffer var5;
      if (var1 instanceof FormattedHeader) {
         CharArrayBuffer var7 = ((FormattedHeader)var1).getBuffer();
         var2 = ((FormattedHeader)var1).getValuePos();
         var5 = var7;
      } else {
         var4 = var1.getValue();
         if (var4 == null) {
            throw new MalformedChallengeException("Header value is null");
         }

         var5 = new CharArrayBuffer(var4.length());
         var5.append(var4);
         var2 = 0;
      }

      while(var2 < var5.length() && HTTP.isWhitespace(var5.charAt(var2))) {
         ++var2;
      }

      int var3;
      for(var3 = var2; var3 < var5.length() && !HTTP.isWhitespace(var5.charAt(var3)); ++var3) {
      }

      var4 = var5.substring(var2, var3);
      if (var4.equalsIgnoreCase(this.getSchemeName())) {
         this.parseChallenge(var5, var3, var5.length());
      } else {
         var6 = new StringBuilder();
         var6.append("Invalid scheme identifier: ");
         var6.append(var4);
         throw new MalformedChallengeException(var6.toString());
      }
   }

   public String toString() {
      String var1 = this.getSchemeName();
      return var1 != null ? var1.toUpperCase(Locale.ROOT) : super.toString();
   }
}
