package org.apache.http.impl.auth;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.KerberosCredentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

public abstract class GGSSchemeBase extends AuthSchemeBase {
   private final Base64 base64codec;
   private final Log log;
   private GGSSchemeBase.State state;
   private final boolean stripPort;
   private byte[] token;
   private final boolean useCanonicalHostname;

   GGSSchemeBase() {
      this(true, true);
   }

   GGSSchemeBase(boolean var1) {
      this(var1, true);
   }

   GGSSchemeBase(boolean var1, boolean var2) {
      this.log = LogFactory.getLog(this.getClass());
      this.base64codec = new Base64(0);
      this.stripPort = var1;
      this.useCanonicalHostname = var2;
      this.state = GGSSchemeBase.State.UNINITIATED;
   }

   private String resolveCanonicalHostname(String var1) throws UnknownHostException {
      InetAddress var2 = InetAddress.getByName(var1);
      String var3 = var2.getCanonicalHostName();
      return var2.getHostAddress().contentEquals(var3) ? var1 : var3;
   }

   @Deprecated
   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      return this.authenticate(var1, var2, (HttpContext)null);
   }

   public Header authenticate(Credentials param1, HttpRequest param2, HttpContext param3) throws AuthenticationException {
      // $FF: Couldn't be decompiled
   }

   GSSContext createGSSContext(GSSManager var1, Oid var2, GSSName var3, GSSCredential var4) throws GSSException {
      GSSContext var5 = var1.createContext(var3.canonicalize(var2), var2, var4, 0);
      var5.requestMutualAuth(true);
      return var5;
   }

   protected byte[] generateGSSToken(byte[] var1, Oid var2, String var3) throws GSSException {
      return this.generateGSSToken(var1, var2, var3, (Credentials)null);
   }

   protected byte[] generateGSSToken(byte[] var1, Oid var2, String var3, Credentials var4) throws GSSException {
      GSSManager var5 = this.getManager();
      StringBuilder var6 = new StringBuilder();
      var6.append("HTTP@");
      var6.append(var3);
      GSSName var9 = var5.createName(var6.toString(), GSSName.NT_HOSTBASED_SERVICE);
      GSSCredential var8;
      if (var4 instanceof KerberosCredentials) {
         var8 = ((KerberosCredentials)var4).getGSSCredential();
      } else {
         var8 = null;
      }

      GSSContext var7 = this.createGSSContext(var5, var2, var9, var8);
      return var1 != null ? var7.initSecContext(var1, 0, var1.length) : var7.initSecContext(new byte[0], 0, 0);
   }

   @Deprecated
   protected byte[] generateToken(byte[] var1, String var2) throws GSSException {
      return null;
   }

   protected byte[] generateToken(byte[] var1, String var2, Credentials var3) throws GSSException {
      return this.generateToken(var1, var2);
   }

   protected GSSManager getManager() {
      return GSSManager.getInstance();
   }

   public boolean isComplete() {
      return this.state == GGSSchemeBase.State.TOKEN_GENERATED || this.state == GGSSchemeBase.State.FAILED;
   }

   protected void parseChallenge(CharArrayBuffer var1, int var2, int var3) throws MalformedChallengeException {
      String var6 = var1.substringTrimmed(var2, var3);
      if (this.log.isDebugEnabled()) {
         Log var4 = this.log;
         StringBuilder var5 = new StringBuilder();
         var5.append("Received challenge '");
         var5.append(var6);
         var5.append("' from the auth server");
         var4.debug(var5.toString());
      }

      if (this.state == GGSSchemeBase.State.UNINITIATED) {
         this.token = Base64.decodeBase64(var6.getBytes());
         this.state = GGSSchemeBase.State.CHALLENGE_RECEIVED;
      } else {
         this.log.debug("Authentication already attempted");
         this.state = GGSSchemeBase.State.FAILED;
      }
   }

   static enum State {
      CHALLENGE_RECEIVED,
      FAILED,
      TOKEN_GENERATED,
      UNINITIATED;

      static {
         GGSSchemeBase.State var0 = new GGSSchemeBase.State("FAILED", 3);
         FAILED = var0;
      }
   }
}
