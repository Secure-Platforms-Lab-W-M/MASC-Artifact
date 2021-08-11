package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

public class SPNegoScheme extends GGSSchemeBase {
   private static final String SPNEGO_OID = "1.3.6.1.5.5.2";

   public SPNegoScheme() {
   }

   public SPNegoScheme(boolean var1) {
      super(var1);
   }

   public SPNegoScheme(boolean var1, boolean var2) {
      super(var1, var2);
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      return super.authenticate(var1, var2, var3);
   }

   protected byte[] generateToken(byte[] var1, String var2) throws GSSException {
      return super.generateToken(var1, var2);
   }

   protected byte[] generateToken(byte[] var1, String var2, Credentials var3) throws GSSException {
      return this.generateGSSToken(var1, new Oid("1.3.6.1.5.5.2"), var2, var3);
   }

   public String getParameter(String var1) {
      Args.notNull(var1, "Parameter name");
      return null;
   }

   public String getRealm() {
      return null;
   }

   public String getSchemeName() {
      return "Negotiate";
   }

   public boolean isConnectionBased() {
      return true;
   }
}
