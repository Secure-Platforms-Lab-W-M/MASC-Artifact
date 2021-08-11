package org.apache.http.impl.auth;

import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;

public class BasicScheme extends RFC2617Scheme {
   private static final long serialVersionUID = -1931571557597830536L;
   private boolean complete;

   public BasicScheme() {
      this(Consts.ASCII);
   }

   public BasicScheme(Charset var1) {
      super(var1);
      this.complete = false;
   }

   @Deprecated
   public BasicScheme(ChallengeState var1) {
      super(var1);
   }

   @Deprecated
   public static Header authenticate(Credentials var0, String var1, boolean var2) {
      Args.notNull(var0, "Credentials");
      Args.notNull(var1, "charset");
      StringBuilder var3 = new StringBuilder();
      var3.append(var0.getUserPrincipal().getName());
      var3.append(":");
      String var4;
      if (var0.getPassword() == null) {
         var4 = "null";
      } else {
         var4 = var0.getPassword();
      }

      var3.append(var4);
      byte[] var5 = Base64.encodeBase64(EncodingUtils.getBytes(var3.toString(), var1), false);
      CharArrayBuffer var6 = new CharArrayBuffer(32);
      if (var2) {
         var6.append("Proxy-Authorization");
      } else {
         var6.append("Authorization");
      }

      var6.append(": Basic ");
      var6.append(var5, 0, var5.length);
      return new BufferedHeader(var6);
   }

   @Deprecated
   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      return this.authenticate(var1, var2, new BasicHttpContext());
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      Args.notNull(var1, "Credentials");
      Args.notNull(var2, "HTTP request");
      StringBuilder var7 = new StringBuilder();
      var7.append(var1.getUserPrincipal().getName());
      var7.append(":");
      String var4;
      if (var1.getPassword() == null) {
         var4 = "null";
      } else {
         var4 = var1.getPassword();
      }

      var7.append(var4);
      byte[] var5 = (new Base64(0)).encode(EncodingUtils.getBytes(var7.toString(), this.getCredentialsCharset(var2)));
      CharArrayBuffer var6 = new CharArrayBuffer(32);
      if (this.isProxy()) {
         var6.append("Proxy-Authorization");
      } else {
         var6.append("Authorization");
      }

      var6.append(": Basic ");
      var6.append(var5, 0, var5.length);
      return new BufferedHeader(var6);
   }

   public String getSchemeName() {
      return "basic";
   }

   public boolean isComplete() {
      return this.complete;
   }

   public boolean isConnectionBased() {
      return false;
   }

   public void processChallenge(Header var1) throws MalformedChallengeException {
      super.processChallenge(var1);
      this.complete = true;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("BASIC [complete=");
      var1.append(this.complete);
      var1.append("]");
      return var1.toString();
   }
}
