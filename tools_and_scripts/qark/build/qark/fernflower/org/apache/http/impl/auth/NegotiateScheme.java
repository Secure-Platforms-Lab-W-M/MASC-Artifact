package org.apache.http.impl.auth;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

@Deprecated
public class NegotiateScheme extends GGSSchemeBase {
   private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
   private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
   private final Log log;
   private final SpnegoTokenGenerator spengoGenerator;

   public NegotiateScheme() {
      this((SpnegoTokenGenerator)null, false);
   }

   public NegotiateScheme(SpnegoTokenGenerator var1) {
      this(var1, false);
   }

   public NegotiateScheme(SpnegoTokenGenerator var1, boolean var2) {
      super(var2);
      this.log = LogFactory.getLog(this.getClass());
      this.spengoGenerator = var1;
   }

   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      return this.authenticate(var1, var2, (HttpContext)null);
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      return super.authenticate(var1, var2, var3);
   }

   protected byte[] generateToken(byte[] var1, String var2) throws GSSException {
      return super.generateToken(var1, var2);
   }

   protected byte[] generateToken(byte[] var1, String var2, Credentials var3) throws GSSException {
      Oid var5 = new Oid("1.3.6.1.5.5.2");
      boolean var4 = false;

      byte[] var10;
      label32: {
         try {
            var10 = this.generateGSSToken(var1, var5, var2, var3);
         } catch (GSSException var7) {
            if (var7.getMajor() == 2) {
               this.log.debug("GSSException BAD_MECH, retry with Kerberos MECH");
               var4 = true;
               break label32;
            }

            throw var7;
         }

         var1 = var10;
      }

      var10 = var1;
      if (var4) {
         this.log.debug("Using Kerberos MECH 1.2.840.113554.1.2.2");
         var1 = this.generateGSSToken(var1, new Oid("1.2.840.113554.1.2.2"), var2, var3);
         var10 = var1;
         if (var1 != null) {
            SpnegoTokenGenerator var8 = this.spengoGenerator;
            var10 = var1;
            if (var8 != null) {
               try {
                  byte[] var9 = var8.generateSpnegoDERObject(var1);
                  return var9;
               } catch (IOException var6) {
                  this.log.error(var6.getMessage(), var6);
                  var10 = var1;
               }
            }
         }
      }

      return var10;
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
