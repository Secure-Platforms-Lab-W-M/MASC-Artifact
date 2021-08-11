package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionOperator;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultHttpClientConnectionOperator implements HttpClientConnectionOperator {
   static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";
   private final DnsResolver dnsResolver;
   private final Log log = LogFactory.getLog(this.getClass());
   private final SchemePortResolver schemePortResolver;
   private final Lookup socketFactoryRegistry;

   public DefaultHttpClientConnectionOperator(Lookup var1, SchemePortResolver var2, DnsResolver var3) {
      Args.notNull(var1, "Socket factory registry");
      this.socketFactoryRegistry = var1;
      if (var2 == null) {
         var2 = DefaultSchemePortResolver.INSTANCE;
      }

      this.schemePortResolver = (SchemePortResolver)var2;
      if (var3 == null) {
         var3 = SystemDefaultDnsResolver.INSTANCE;
      }

      this.dnsResolver = (DnsResolver)var3;
   }

   private Lookup getSocketFactoryRegistry(HttpContext var1) {
      Lookup var2 = (Lookup)var1.getAttribute("http.socket-factory-registry");
      Lookup var3 = var2;
      if (var2 == null) {
         var3 = this.socketFactoryRegistry;
      }

      return var3;
   }

   public void connect(ManagedHttpClientConnection var1, HttpHost var2, InetSocketAddress var3, int var4, SocketConfig var5, HttpContext var6) throws IOException {
      ConnectionSocketFactory var13 = (ConnectionSocketFactory)this.getSocketFactoryRegistry(var6).lookup(var2.getSchemeName());
      if (var13 == null) {
         StringBuilder var24 = new StringBuilder();
         var24.append(var2.getSchemeName());
         var24.append(" protocol is not supported");
         throw new UnsupportedSchemeException(var24.toString());
      } else {
         InetAddress[] var11;
         if (var2.getAddress() != null) {
            var11 = new InetAddress[]{var2.getAddress()};
         } else {
            var11 = this.dnsResolver.resolve(var2.getHostName());
         }

         int var7 = this.schemePortResolver.resolve(var2);

         for(int var8 = 0; var8 < var11.length; ++var8) {
            InetAddress var14 = var11[var8];
            boolean var9;
            if (var8 == var11.length - 1) {
               var9 = true;
            } else {
               var9 = false;
            }

            Socket var12 = var13.createSocket(var6);
            var12.setSoTimeout(var5.getSoTimeout());
            var12.setReuseAddress(var5.isSoReuseAddress());
            var12.setTcpNoDelay(var5.isTcpNoDelay());
            var12.setKeepAlive(var5.isSoKeepAlive());
            if (var5.getRcvBufSize() > 0) {
               var12.setReceiveBufferSize(var5.getRcvBufSize());
            }

            if (var5.getSndBufSize() > 0) {
               var12.setSendBufferSize(var5.getSndBufSize());
            }

            int var10 = var5.getSoLinger();
            if (var10 >= 0) {
               var12.setSoLinger(true, var10);
            }

            var1.bind(var12);
            InetSocketAddress var28 = new InetSocketAddress(var14, var7);
            if (this.log.isDebugEnabled()) {
               Log var15 = this.log;
               StringBuilder var16 = new StringBuilder();
               var16.append("Connecting to ");
               var16.append(var28);
               var15.debug(var16.toString());
            }

            Log var29;
            StringBuilder var30;
            label94: {
               SocketTimeoutException var27;
               label93: {
                  ConnectException var26;
                  label92: {
                     NoRouteToHostException var25;
                     label91: {
                        try {
                           var12 = var13.connectSocket(var4, var12, var2, var28, var3, var6);
                        } catch (SocketTimeoutException var20) {
                           var27 = var20;
                           break label93;
                        } catch (ConnectException var21) {
                           var26 = var21;
                           break label92;
                        } catch (NoRouteToHostException var22) {
                           var25 = var22;
                           break label91;
                        }

                        try {
                           var1.bind(var12);
                           if (this.log.isDebugEnabled()) {
                              var29 = this.log;
                              var30 = new StringBuilder();
                              var30.append("Connection established ");
                              var30.append(var1);
                              var29.debug(var30.toString());
                           }

                           return;
                        } catch (SocketTimeoutException var17) {
                           var27 = var17;
                           break label93;
                        } catch (ConnectException var18) {
                           var26 = var18;
                           break label92;
                        } catch (NoRouteToHostException var19) {
                           var25 = var19;
                        }
                     }

                     if (var9) {
                        throw var25;
                     }
                     break label94;
                  }

                  if (var9) {
                     Object var23;
                     if ("Connection timed out".equals(var26.getMessage())) {
                        var23 = new ConnectTimeoutException(var26, var2, var11);
                     } else {
                        var23 = new HttpHostConnectException(var26, var2, var11);
                     }

                     throw var23;
                  }
                  break label94;
               }

               if (var9) {
                  throw new ConnectTimeoutException(var27, var2, var11);
               }
            }

            if (this.log.isDebugEnabled()) {
               var29 = this.log;
               var30 = new StringBuilder();
               var30.append("Connect to ");
               var30.append(var28);
               var30.append(" timed out. ");
               var30.append("Connection will be retried using another IP address");
               var29.debug(var30.toString());
            }
         }

      }
   }

   public void upgrade(ManagedHttpClientConnection var1, HttpHost var2, HttpContext var3) throws IOException {
      ConnectionSocketFactory var5 = (ConnectionSocketFactory)this.getSocketFactoryRegistry(HttpClientContext.adapt(var3)).lookup(var2.getSchemeName());
      StringBuilder var8;
      if (var5 != null) {
         if (var5 instanceof LayeredConnectionSocketFactory) {
            LayeredConnectionSocketFactory var7 = (LayeredConnectionSocketFactory)var5;
            Socket var6 = var1.getSocket();
            int var4 = this.schemePortResolver.resolve(var2);
            var1.bind(var7.createLayeredSocket(var6, var2.getHostName(), var4, var3));
         } else {
            var8 = new StringBuilder();
            var8.append(var2.getSchemeName());
            var8.append(" protocol does not support connection upgrade");
            throw new UnsupportedSchemeException(var8.toString());
         }
      } else {
         var8 = new StringBuilder();
         var8.append(var2.getSchemeName());
         var8.append(" protocol is not supported");
         throw new UnsupportedSchemeException(var8.toString());
      }
   }
}
