package org.apache.http.impl.bootstrap;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerMapper;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;

public class ServerBootstrap {
   private ConnectionReuseStrategy connStrategy;
   private ConnectionConfig connectionConfig;
   private HttpConnectionFactory connectionFactory;
   private ExceptionLogger exceptionLogger;
   private HttpExpectationVerifier expectationVerifier;
   private Map handlerMap;
   private HttpRequestHandlerMapper handlerMapper;
   private HttpProcessor httpProcessor;
   private int listenerPort;
   private InetAddress localAddress;
   private LinkedList requestFirst;
   private LinkedList requestLast;
   private HttpResponseFactory responseFactory;
   private LinkedList responseFirst;
   private LinkedList responseLast;
   private String serverInfo;
   private ServerSocketFactory serverSocketFactory;
   private SocketConfig socketConfig;
   private SSLContext sslContext;
   private SSLServerSetupHandler sslSetupHandler;

   private ServerBootstrap() {
   }

   public static ServerBootstrap bootstrap() {
      return new ServerBootstrap();
   }

   public final ServerBootstrap addInterceptorFirst(HttpRequestInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.requestFirst == null) {
            this.requestFirst = new LinkedList();
         }

         this.requestFirst.addFirst(var1);
         return this;
      }
   }

   public final ServerBootstrap addInterceptorFirst(HttpResponseInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.responseFirst == null) {
            this.responseFirst = new LinkedList();
         }

         this.responseFirst.addFirst(var1);
         return this;
      }
   }

   public final ServerBootstrap addInterceptorLast(HttpRequestInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.requestLast == null) {
            this.requestLast = new LinkedList();
         }

         this.requestLast.addLast(var1);
         return this;
      }
   }

   public final ServerBootstrap addInterceptorLast(HttpResponseInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.responseLast == null) {
            this.responseLast = new LinkedList();
         }

         this.responseLast.addLast(var1);
         return this;
      }
   }

   public HttpServer create() {
      HttpProcessor var3 = this.httpProcessor;
      HttpProcessor var2 = var3;
      if (var3 == null) {
         HttpProcessorBuilder var4 = HttpProcessorBuilder.create();
         LinkedList var8 = this.requestFirst;
         Iterator var9;
         if (var8 != null) {
            var9 = var8.iterator();

            while(var9.hasNext()) {
               var4.addFirst((HttpRequestInterceptor)var9.next());
            }
         }

         var8 = this.responseFirst;
         if (var8 != null) {
            var9 = var8.iterator();

            while(var9.hasNext()) {
               var4.addFirst((HttpResponseInterceptor)var9.next());
            }
         }

         String var10 = this.serverInfo;
         String var12 = var10;
         if (var10 == null) {
            var12 = "Apache-HttpCore/1.1";
         }

         var4.addAll(new HttpResponseInterceptor[]{new ResponseDate(), new ResponseServer(var12), new ResponseContent(), new ResponseConnControl()});
         var8 = this.requestLast;
         if (var8 != null) {
            var9 = var8.iterator();

            while(var9.hasNext()) {
               var4.addLast((HttpRequestInterceptor)var9.next());
            }
         }

         var8 = this.responseLast;
         if (var8 != null) {
            var9 = var8.iterator();

            while(var9.hasNext()) {
               var4.addLast((HttpResponseInterceptor)var9.next());
            }
         }

         var2 = var4.build();
      }

      HttpRequestHandlerMapper var13 = this.handlerMapper;
      Object var11 = var13;
      if (var13 == null) {
         var11 = new UriHttpRequestHandlerMapper();
         Map var14 = this.handlerMap;
         if (var14 != null) {
            Iterator var16 = var14.entrySet().iterator();

            while(var16.hasNext()) {
               Entry var5 = (Entry)var16.next();
               ((UriHttpRequestHandlerMapper)var11).register((String)var5.getKey(), (HttpRequestHandler)var5.getValue());
            }
         }
      }

      ConnectionReuseStrategy var17 = this.connStrategy;
      Object var18 = var17;
      if (var17 == null) {
         var18 = DefaultConnectionReuseStrategy.INSTANCE;
      }

      Object var20 = this.responseFactory;
      if (var20 == null) {
         var20 = DefaultHttpResponseFactory.INSTANCE;
      }

      HttpService var6 = new HttpService(var2, (ConnectionReuseStrategy)var18, (HttpResponseFactory)var20, (HttpRequestHandlerMapper)var11, this.expectationVerifier);
      ServerSocketFactory var15 = this.serverSocketFactory;
      Object var22 = var15;
      if (var15 == null) {
         SSLContext var25 = this.sslContext;
         if (var25 != null) {
            var22 = var25.getServerSocketFactory();
         } else {
            var22 = ServerSocketFactory.getDefault();
         }
      }

      HttpConnectionFactory var21 = this.connectionFactory;
      var11 = var21;
      if (var21 == null) {
         ConnectionConfig var19 = this.connectionConfig;
         if (var19 != null) {
            var11 = new DefaultBHttpServerConnectionFactory(var19);
         } else {
            var11 = DefaultBHttpServerConnectionFactory.INSTANCE;
         }
      }

      ExceptionLogger var23 = this.exceptionLogger;
      ExceptionLogger var24 = var23;
      if (var23 == null) {
         var24 = ExceptionLogger.NO_OP;
      }

      int var1 = this.listenerPort;
      if (var1 <= 0) {
         var1 = 0;
      }

      InetAddress var7 = this.localAddress;
      SocketConfig var26 = this.socketConfig;
      if (var26 == null) {
         var26 = SocketConfig.DEFAULT;
      }

      return new HttpServer(var1, var7, var26, (ServerSocketFactory)var22, var6, (HttpConnectionFactory)var11, this.sslSetupHandler, var24);
   }

   public final ServerBootstrap registerHandler(String var1, HttpRequestHandler var2) {
      if (var1 != null) {
         if (var2 == null) {
            return this;
         } else {
            if (this.handlerMap == null) {
               this.handlerMap = new HashMap();
            }

            this.handlerMap.put(var1, var2);
            return this;
         }
      } else {
         return this;
      }
   }

   public final ServerBootstrap setConnectionConfig(ConnectionConfig var1) {
      this.connectionConfig = var1;
      return this;
   }

   public final ServerBootstrap setConnectionFactory(HttpConnectionFactory var1) {
      this.connectionFactory = var1;
      return this;
   }

   public final ServerBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy var1) {
      this.connStrategy = var1;
      return this;
   }

   public final ServerBootstrap setExceptionLogger(ExceptionLogger var1) {
      this.exceptionLogger = var1;
      return this;
   }

   public final ServerBootstrap setExpectationVerifier(HttpExpectationVerifier var1) {
      this.expectationVerifier = var1;
      return this;
   }

   public final ServerBootstrap setHandlerMapper(HttpRequestHandlerMapper var1) {
      this.handlerMapper = var1;
      return this;
   }

   public final ServerBootstrap setHttpProcessor(HttpProcessor var1) {
      this.httpProcessor = var1;
      return this;
   }

   public final ServerBootstrap setListenerPort(int var1) {
      this.listenerPort = var1;
      return this;
   }

   public final ServerBootstrap setLocalAddress(InetAddress var1) {
      this.localAddress = var1;
      return this;
   }

   public final ServerBootstrap setResponseFactory(HttpResponseFactory var1) {
      this.responseFactory = var1;
      return this;
   }

   public final ServerBootstrap setServerInfo(String var1) {
      this.serverInfo = var1;
      return this;
   }

   public final ServerBootstrap setServerSocketFactory(ServerSocketFactory var1) {
      this.serverSocketFactory = var1;
      return this;
   }

   public final ServerBootstrap setSocketConfig(SocketConfig var1) {
      this.socketConfig = var1;
      return this;
   }

   public final ServerBootstrap setSslContext(SSLContext var1) {
      this.sslContext = var1;
      return this;
   }

   public final ServerBootstrap setSslSetupHandler(SSLServerSetupHandler var1) {
      this.sslSetupHandler = var1;
      return this;
   }
}
