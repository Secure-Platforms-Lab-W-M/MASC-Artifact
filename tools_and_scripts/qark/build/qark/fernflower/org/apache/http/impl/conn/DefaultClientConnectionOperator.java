package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class DefaultClientConnectionOperator implements ClientConnectionOperator {
   protected final DnsResolver dnsResolver;
   private final Log log = LogFactory.getLog(this.getClass());
   protected final SchemeRegistry schemeRegistry;

   public DefaultClientConnectionOperator(SchemeRegistry var1) {
      Args.notNull(var1, "Scheme registry");
      this.schemeRegistry = var1;
      this.dnsResolver = new SystemDefaultDnsResolver();
   }

   public DefaultClientConnectionOperator(SchemeRegistry var1, DnsResolver var2) {
      Args.notNull(var1, "Scheme registry");
      Args.notNull(var2, "DNS resolver");
      this.schemeRegistry = var1;
      this.dnsResolver = var2;
   }

   private SchemeRegistry getSchemeRegistry(HttpContext var1) {
      SchemeRegistry var2 = (SchemeRegistry)var1.getAttribute("http.scheme-registry");
      SchemeRegistry var3 = var2;
      if (var2 == null) {
         var3 = this.schemeRegistry;
      }

      return var3;
   }

   public OperatedClientConnection createConnection() {
      return new DefaultClientConnection();
   }

   public void openConnection(OperatedClientConnection var1, HttpHost var2, InetAddress var3, HttpContext var4, HttpParams var5) throws IOException {
      Args.notNull(var1, "Connection");
      Args.notNull(var2, "Target host");
      Args.notNull(var5, "HTTP parameters");
      Asserts.check(var1.isOpen() ^ true, "Connection must not be open");
      SchemeRegistry var9 = this.getSchemeRegistry(var4);
      Scheme var11 = var9.getScheme(var2.getSchemeName());
      SchemeSocketFactory var15 = var11.getSchemeSocketFactory();
      InetAddress[] var16 = this.resolveHostname(var2.getHostName());
      int var8 = var11.resolvePort(var2.getPort());
      int var6 = 0;

      while(true) {
         DefaultClientConnectionOperator var25 = this;
         if (var6 >= var16.length) {
            return;
         }

         InetAddress var13 = var16[var6];
         boolean var7;
         if (var6 == var16.length - 1) {
            var7 = true;
         } else {
            var7 = false;
         }

         Socket var12 = var15.createSocket(var5);
         var1.opening(var12, var2);
         HttpInetSocketAddress var30 = new HttpInetSocketAddress(var2, var13, var8);
         InetSocketAddress var10;
         if (var3 != null) {
            var10 = new InetSocketAddress(var3, 0);
         } else {
            var10 = null;
         }

         if (this.log.isDebugEnabled()) {
            Log var14 = this.log;
            StringBuilder var17 = new StringBuilder();
            var17.append("Connecting to ");
            var17.append(var30);
            var14.debug(var17.toString());
         }

         label70: {
            ConnectTimeoutException var10000;
            label69: {
               ConnectException var32;
               label76: {
                  boolean var10001;
                  Socket var31;
                  try {
                     var31 = var15.connectSocket(var12, var30, var10, var5);
                  } catch (ConnectException var22) {
                     var32 = var22;
                     var10001 = false;
                     break label76;
                  } catch (ConnectTimeoutException var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label69;
                  }

                  Socket var24 = var12;
                  if (var12 != var31) {
                     var24 = var31;

                     try {
                        var1.opening(var24, var2);
                     } catch (ConnectException var20) {
                        var32 = var20;
                        var10001 = false;
                        break label76;
                     } catch (ConnectTimeoutException var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label69;
                     }
                  }

                  try {
                     var25.prepareSocket(var24, var4, var5);
                     var1.openCompleted(var15.isSecure(var24), var5);
                     return;
                  } catch (ConnectException var18) {
                     var32 = var18;
                     var10001 = false;
                  } catch (ConnectTimeoutException var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label69;
                  }
               }

               ConnectException var28 = var32;
               if (var7) {
                  throw var28;
               }
               break label70;
            }

            ConnectTimeoutException var26 = var10000;
            if (var7) {
               throw var26;
            }
         }

         if (this.log.isDebugEnabled()) {
            Log var29 = this.log;
            StringBuilder var27 = new StringBuilder();
            var27.append("Connect to ");
            var27.append(var30);
            var27.append(" timed out. ");
            var27.append("Connection will be retried using another IP address");
            var29.debug(var27.toString());
         }

         ++var6;
      }
   }

   protected void prepareSocket(Socket var1, HttpContext var2, HttpParams var3) throws IOException {
      var1.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(var3));
      var1.setSoTimeout(HttpConnectionParams.getSoTimeout(var3));
      int var4 = HttpConnectionParams.getLinger(var3);
      if (var4 >= 0) {
         boolean var5;
         if (var4 > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         var1.setSoLinger(var5, var4);
      }

   }

   protected InetAddress[] resolveHostname(String var1) throws UnknownHostException {
      return this.dnsResolver.resolve(var1);
   }

   public void updateSecureConnection(OperatedClientConnection var1, HttpHost var2, HttpContext var3, HttpParams var4) throws IOException {
      Args.notNull(var1, "Connection");
      Args.notNull(var2, "Target host");
      Args.notNull(var4, "Parameters");
      Asserts.check(var1.isOpen(), "Connection must be open");
      Scheme var6 = this.getSchemeRegistry(var3).getScheme(var2.getSchemeName());
      Asserts.check(var6.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory, "Socket factory must implement SchemeLayeredSocketFactory");
      SchemeLayeredSocketFactory var5 = (SchemeLayeredSocketFactory)var6.getSchemeSocketFactory();
      Socket var7 = var5.createLayeredSocket(var1.getSocket(), var2.getHostName(), var6.resolvePort(var2.getPort()), var4);
      this.prepareSocket(var7, var3, var4);
      var1.update(var7, var2, var5.isSecure(var7), var4);
   }
}
