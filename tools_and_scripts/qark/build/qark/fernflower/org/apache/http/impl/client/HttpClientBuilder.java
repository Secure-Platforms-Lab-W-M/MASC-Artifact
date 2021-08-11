package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.impl.execchain.BackoffStrategyExec;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.impl.execchain.MainClientExec;
import org.apache.http.impl.execchain.ProtocolExec;
import org.apache.http.impl.execchain.RedirectExec;
import org.apache.http.impl.execchain.RetryExec;
import org.apache.http.impl.execchain.ServiceUnavailableRetryExec;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.TextUtils;
import org.apache.http.util.VersionInfo;

public class HttpClientBuilder {
   private boolean authCachingDisabled;
   private Lookup authSchemeRegistry;
   private boolean automaticRetriesDisabled;
   private BackoffManager backoffManager;
   private List closeables;
   private HttpClientConnectionManager connManager;
   private boolean connManagerShared;
   private long connTimeToLive = -1L;
   private TimeUnit connTimeToLiveTimeUnit;
   private ConnectionBackoffStrategy connectionBackoffStrategy;
   private boolean connectionStateDisabled;
   private boolean contentCompressionDisabled;
   private Map contentDecoderMap;
   private boolean cookieManagementDisabled;
   private Lookup cookieSpecRegistry;
   private CookieStore cookieStore;
   private CredentialsProvider credentialsProvider;
   private ConnectionConfig defaultConnectionConfig;
   private Collection defaultHeaders;
   private RequestConfig defaultRequestConfig;
   private SocketConfig defaultSocketConfig;
   private boolean defaultUserAgentDisabled;
   private DnsResolver dnsResolver;
   private boolean evictExpiredConnections;
   private boolean evictIdleConnections;
   private HostnameVerifier hostnameVerifier;
   private HttpProcessor httpprocessor;
   private ConnectionKeepAliveStrategy keepAliveStrategy;
   private int maxConnPerRoute = 0;
   private int maxConnTotal = 0;
   private long maxIdleTime;
   private TimeUnit maxIdleTimeUnit;
   private HttpHost proxy;
   private AuthenticationStrategy proxyAuthStrategy;
   private PublicSuffixMatcher publicSuffixMatcher;
   private boolean redirectHandlingDisabled;
   private RedirectStrategy redirectStrategy;
   private HttpRequestExecutor requestExec;
   private LinkedList requestFirst;
   private LinkedList requestLast;
   private LinkedList responseFirst;
   private LinkedList responseLast;
   private HttpRequestRetryHandler retryHandler;
   private ConnectionReuseStrategy reuseStrategy;
   private HttpRoutePlanner routePlanner;
   private SchemePortResolver schemePortResolver;
   private ServiceUnavailableRetryStrategy serviceUnavailStrategy;
   private SSLContext sslContext;
   private LayeredConnectionSocketFactory sslSocketFactory;
   private boolean systemProperties;
   private AuthenticationStrategy targetAuthStrategy;
   private String userAgent;
   private UserTokenHandler userTokenHandler;

   protected HttpClientBuilder() {
      this.connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;
   }

   public static HttpClientBuilder create() {
      return new HttpClientBuilder();
   }

   private static String[] split(String var0) {
      return TextUtils.isBlank(var0) ? null : var0.split(" *, *");
   }

   protected void addCloseable(Closeable var1) {
      if (var1 != null) {
         if (this.closeables == null) {
            this.closeables = new ArrayList();
         }

         this.closeables.add(var1);
      }
   }

   public final HttpClientBuilder addInterceptorFirst(HttpRequestInterceptor var1) {
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

   public final HttpClientBuilder addInterceptorFirst(HttpResponseInterceptor var1) {
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

   public final HttpClientBuilder addInterceptorLast(HttpRequestInterceptor var1) {
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

   public final HttpClientBuilder addInterceptorLast(HttpResponseInterceptor var1) {
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

   public CloseableHttpClient build() {
      PublicSuffixMatcher var7 = this.publicSuffixMatcher;
      if (var7 == null) {
         var7 = PublicSuffixMatcherLoader.getDefault();
      }

      HttpRequestExecutor var9 = this.requestExec;
      if (var9 == null) {
         var9 = new HttpRequestExecutor();
      }

      final Object var8 = this.connManager;
      long var2;
      Object var4;
      Object var6;
      if (var8 == null) {
         LayeredConnectionSocketFactory var5 = this.sslSocketFactory;
         var4 = var5;
         if (var5 == null) {
            String[] var13;
            if (this.systemProperties) {
               var13 = split(System.getProperty("https.protocols"));
            } else {
               var13 = null;
            }

            String[] var14;
            if (this.systemProperties) {
               var14 = split(System.getProperty("https.cipherSuites"));
            } else {
               var14 = null;
            }

            HostnameVerifier var21 = this.hostnameVerifier;
            var6 = var21;
            if (var21 == null) {
               var6 = new DefaultHostnameVerifier(var7);
            }

            if (this.sslContext != null) {
               var4 = new SSLConnectionSocketFactory(this.sslContext, var13, var14, (HostnameVerifier)var6);
            } else if (this.systemProperties) {
               var4 = new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), var13, var14, (HostnameVerifier)var6);
            } else {
               var4 = new SSLConnectionSocketFactory(SSLContexts.createDefault(), (HostnameVerifier)var6);
            }
         }

         Registry var15 = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", var4).build();
         DnsResolver var18 = this.dnsResolver;
         var2 = this.connTimeToLive;
         TimeUnit var16 = this.connTimeToLiveTimeUnit;
         if (var16 == null) {
            var16 = TimeUnit.MILLISECONDS;
         }

         var8 = new PoolingHttpClientConnectionManager(var15, (HttpConnectionFactory)null, (SchemePortResolver)null, var18, var2, var16);
         SocketConfig var19 = this.defaultSocketConfig;
         if (var19 != null) {
            ((PoolingHttpClientConnectionManager)var8).setDefaultSocketConfig(var19);
         }

         ConnectionConfig var20 = this.defaultConnectionConfig;
         if (var20 != null) {
            ((PoolingHttpClientConnectionManager)var8).setDefaultConnectionConfig(var20);
         }

         int var1;
         if (this.systemProperties && "true".equalsIgnoreCase(System.getProperty("http.keepAlive", "true"))) {
            var1 = Integer.parseInt(System.getProperty("http.maxConnections", "5"));
            ((PoolingHttpClientConnectionManager)var8).setDefaultMaxPerRoute(var1);
            ((PoolingHttpClientConnectionManager)var8).setMaxTotal(var1 * 2);
         }

         var1 = this.maxConnTotal;
         if (var1 > 0) {
            ((PoolingHttpClientConnectionManager)var8).setMaxTotal(var1);
         }

         var1 = this.maxConnPerRoute;
         if (var1 > 0) {
            ((PoolingHttpClientConnectionManager)var8).setDefaultMaxPerRoute(var1);
         }
      }

      var4 = this.reuseStrategy;
      if (var4 == null) {
         if (this.systemProperties) {
            if ("true".equalsIgnoreCase(System.getProperty("http.keepAlive", "true"))) {
               var4 = DefaultClientConnectionReuseStrategy.INSTANCE;
            } else {
               var4 = NoConnectionReuseStrategy.INSTANCE;
            }
         } else {
            var4 = DefaultClientConnectionReuseStrategy.INSTANCE;
         }
      }

      Object var10 = this.keepAliveStrategy;
      if (var10 == null) {
         var10 = DefaultConnectionKeepAliveStrategy.INSTANCE;
      }

      Object var11 = this.targetAuthStrategy;
      if (var11 == null) {
         var11 = TargetAuthenticationStrategy.INSTANCE;
      }

      Object var12 = this.proxyAuthStrategy;
      if (var12 == null) {
         var12 = ProxyAuthenticationStrategy.INSTANCE;
      }

      var6 = this.userTokenHandler;
      if (var6 == null) {
         if (!this.connectionStateDisabled) {
            var6 = DefaultUserTokenHandler.INSTANCE;
         } else {
            var6 = NoopUserTokenHandler.INSTANCE;
         }
      }

      String var17 = this.userAgent;
      if (var17 == null) {
         if (this.systemProperties) {
            var17 = System.getProperty("http.agent");
         }

         if (var17 == null && !this.defaultUserAgentDisabled) {
            var17 = VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", this.getClass());
         }
      }

      ClientExecChain var30 = this.decorateMainExec(this.createMainExec(var9, (HttpClientConnectionManager)var8, (ConnectionReuseStrategy)var4, (ConnectionKeepAliveStrategy)var10, new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestTargetHost(), new RequestUserAgent(var17)}), (AuthenticationStrategy)var11, (AuthenticationStrategy)var12, (UserTokenHandler)var6));
      HttpProcessor var25 = this.httpprocessor;
      HttpProcessor var32 = var25;
      if (var25 == null) {
         HttpProcessorBuilder var35 = HttpProcessorBuilder.create();
         LinkedList var28 = this.requestFirst;
         Iterator var31;
         if (var28 != null) {
            var31 = var28.iterator();

            while(var31.hasNext()) {
               var35.addFirst((HttpRequestInterceptor)var31.next());
            }
         }

         var28 = this.responseFirst;
         if (var28 != null) {
            var31 = var28.iterator();

            while(var31.hasNext()) {
               var35.addFirst((HttpResponseInterceptor)var31.next());
            }
         }

         var35.addAll(new HttpRequestInterceptor[]{new RequestDefaultHeaders(this.defaultHeaders), new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(var17), new RequestExpectContinue()});
         if (!this.cookieManagementDisabled) {
            var35.add(new RequestAddCookies());
         }

         if (!this.contentCompressionDisabled) {
            if (this.contentDecoderMap != null) {
               ArrayList var23 = new ArrayList(this.contentDecoderMap.keySet());
               Collections.sort(var23);
               var35.add(new RequestAcceptEncoding(var23));
            } else {
               var35.add(new RequestAcceptEncoding());
            }
         }

         if (!this.authCachingDisabled) {
            var35.add(new RequestAuthCache());
         }

         if (!this.cookieManagementDisabled) {
            var35.add(new ResponseProcessCookies());
         }

         if (!this.contentCompressionDisabled) {
            if (this.contentDecoderMap == null) {
               var35.add(new ResponseContentEncoding());
            } else {
               RegistryBuilder var24 = RegistryBuilder.create();
               var31 = this.contentDecoderMap.entrySet().iterator();

               while(var31.hasNext()) {
                  Entry var40 = (Entry)var31.next();
                  var24.register((String)var40.getKey(), var40.getValue());
               }

               var35.add(new ResponseContentEncoding(var24.build()));
            }
         }

         LinkedList var26 = this.requestLast;
         Iterator var29;
         if (var26 != null) {
            var29 = var26.iterator();

            while(var29.hasNext()) {
               var35.addLast((HttpRequestInterceptor)var29.next());
            }
         }

         var26 = this.responseLast;
         if (var26 != null) {
            var29 = var26.iterator();

            while(var29.hasNext()) {
               var35.addLast((HttpResponseInterceptor)var29.next());
            }
         }

         var32 = var35.build();
      }

      ClientExecChain var42 = this.decorateProtocolExec(new ProtocolExec(var30, var32));
      Object var36 = var42;
      if (!this.automaticRetriesDisabled) {
         HttpRequestRetryHandler var38 = this.retryHandler;
         var4 = var38;
         if (var38 == null) {
            var4 = DefaultHttpRequestRetryHandler.INSTANCE;
         }

         var36 = new RetryExec(var42, (HttpRequestRetryHandler)var4);
      }

      HttpRoutePlanner var44 = this.routePlanner;
      var4 = var44;
      if (var44 == null) {
         SchemePortResolver var45 = this.schemePortResolver;
         var4 = var45;
         if (var45 == null) {
            var4 = DefaultSchemePortResolver.INSTANCE;
         }

         HttpHost var47 = this.proxy;
         if (var47 != null) {
            var4 = new DefaultProxyRoutePlanner(var47, (SchemePortResolver)var4);
         } else if (this.systemProperties) {
            var4 = new SystemDefaultRoutePlanner((SchemePortResolver)var4, ProxySelector.getDefault());
         } else {
            var4 = new DefaultRoutePlanner((SchemePortResolver)var4);
         }
      }

      ServiceUnavailableRetryStrategy var33 = this.serviceUnavailStrategy;
      var6 = var36;
      if (var33 != null) {
         var6 = new ServiceUnavailableRetryExec((ClientExecChain)var36, var33);
      }

      var36 = var6;
      if (!this.redirectHandlingDisabled) {
         RedirectStrategy var37 = this.redirectStrategy;
         var36 = var37;
         if (var37 == null) {
            var36 = DefaultRedirectStrategy.INSTANCE;
         }

         var36 = new RedirectExec((ClientExecChain)var6, (HttpRoutePlanner)var4, (RedirectStrategy)var36);
      }

      BackoffManager var48 = this.backoffManager;
      Object var39 = var36;
      if (var48 != null) {
         ConnectionBackoffStrategy var43 = this.connectionBackoffStrategy;
         var39 = var36;
         if (var43 != null) {
            var39 = new BackoffStrategyExec((ClientExecChain)var36, var43, var48);
         }
      }

      Lookup var49 = this.authSchemeRegistry;
      var10 = var49;
      if (var49 == null) {
         var10 = RegistryBuilder.create().register("Basic", new BasicSchemeFactory()).register("Digest", new DigestSchemeFactory()).register("NTLM", new NTLMSchemeFactory()).register("Negotiate", new SPNegoSchemeFactory()).register("Kerberos", new KerberosSchemeFactory()).build();
      }

      var49 = this.cookieSpecRegistry;
      Lookup var46 = var49;
      if (var49 == null) {
         var46 = CookieSpecRegistries.createDefault(var7);
      }

      CookieStore var52 = this.cookieStore;
      var12 = var52;
      if (var52 == null) {
         var12 = new BasicCookieStore();
      }

      CredentialsProvider var50 = this.credentialsProvider;
      var36 = var50;
      if (var50 == null) {
         if (this.systemProperties) {
            var36 = new SystemDefaultCredentialsProvider();
         } else {
            var36 = new BasicCredentialsProvider();
         }
      }

      ArrayList var22;
      if (this.closeables != null) {
         var22 = new ArrayList(this.closeables);
      } else {
         var22 = null;
      }

      ArrayList var51;
      if (!this.connManagerShared) {
         var51 = var22;
         if (var22 == null) {
            var51 = new ArrayList(1);
         }

         if (this.evictExpiredConnections || this.evictIdleConnections) {
            var2 = this.maxIdleTime;
            if (var2 <= 0L) {
               var2 = 10L;
            }

            TimeUnit var27 = this.maxIdleTimeUnit;
            if (var27 == null) {
               var27 = TimeUnit.SECONDS;
            }

            final IdleConnectionEvictor var34 = new IdleConnectionEvictor((HttpClientConnectionManager)var8, var2, var27, this.maxIdleTime, this.maxIdleTimeUnit);
            var51.add(new Closeable() {
               public void close() throws IOException {
                  var34.shutdown();

                  try {
                     var34.awaitTermination(1L, TimeUnit.SECONDS);
                  } catch (InterruptedException var2) {
                     Thread.currentThread().interrupt();
                  }
               }
            });
            var34.start();
         }

         var51.add(new Closeable() {
            public void close() throws IOException {
               ((HttpClientConnectionManager)var8).shutdown();
            }
         });
      } else {
         var51 = var22;
      }

      RequestConfig var41 = this.defaultRequestConfig;
      if (var41 == null) {
         var41 = RequestConfig.DEFAULT;
      }

      return new InternalHttpClient((ClientExecChain)var39, (HttpClientConnectionManager)var8, (HttpRoutePlanner)var4, var46, (Lookup)var10, (CookieStore)var12, (CredentialsProvider)var36, var41, var51);
   }

   protected ClientExecChain createMainExec(HttpRequestExecutor var1, HttpClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpProcessor var5, AuthenticationStrategy var6, AuthenticationStrategy var7, UserTokenHandler var8) {
      return new MainClientExec(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   protected ClientExecChain decorateMainExec(ClientExecChain var1) {
      return var1;
   }

   protected ClientExecChain decorateProtocolExec(ClientExecChain var1) {
      return var1;
   }

   public final HttpClientBuilder disableAuthCaching() {
      this.authCachingDisabled = true;
      return this;
   }

   public final HttpClientBuilder disableAutomaticRetries() {
      this.automaticRetriesDisabled = true;
      return this;
   }

   public final HttpClientBuilder disableConnectionState() {
      this.connectionStateDisabled = true;
      return this;
   }

   public final HttpClientBuilder disableContentCompression() {
      this.contentCompressionDisabled = true;
      return this;
   }

   public final HttpClientBuilder disableCookieManagement() {
      this.cookieManagementDisabled = true;
      return this;
   }

   public final HttpClientBuilder disableDefaultUserAgent() {
      this.defaultUserAgentDisabled = true;
      return this;
   }

   public final HttpClientBuilder disableRedirectHandling() {
      this.redirectHandlingDisabled = true;
      return this;
   }

   public final HttpClientBuilder evictExpiredConnections() {
      this.evictExpiredConnections = true;
      return this;
   }

   public final HttpClientBuilder evictIdleConnections(long var1, TimeUnit var3) {
      this.evictIdleConnections = true;
      this.maxIdleTime = var1;
      this.maxIdleTimeUnit = var3;
      return this;
   }

   @Deprecated
   public final HttpClientBuilder evictIdleConnections(Long var1, TimeUnit var2) {
      return this.evictIdleConnections(var1, var2);
   }

   public final HttpClientBuilder setBackoffManager(BackoffManager var1) {
      this.backoffManager = var1;
      return this;
   }

   public final HttpClientBuilder setConnectionBackoffStrategy(ConnectionBackoffStrategy var1) {
      this.connectionBackoffStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setConnectionManager(HttpClientConnectionManager var1) {
      this.connManager = var1;
      return this;
   }

   public final HttpClientBuilder setConnectionManagerShared(boolean var1) {
      this.connManagerShared = var1;
      return this;
   }

   public final HttpClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy var1) {
      this.reuseStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setConnectionTimeToLive(long var1, TimeUnit var3) {
      this.connTimeToLive = var1;
      this.connTimeToLiveTimeUnit = var3;
      return this;
   }

   public final HttpClientBuilder setContentDecoderRegistry(Map var1) {
      this.contentDecoderMap = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultAuthSchemeRegistry(Lookup var1) {
      this.authSchemeRegistry = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultConnectionConfig(ConnectionConfig var1) {
      this.defaultConnectionConfig = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultCookieSpecRegistry(Lookup var1) {
      this.cookieSpecRegistry = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultCookieStore(CookieStore var1) {
      this.cookieStore = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultCredentialsProvider(CredentialsProvider var1) {
      this.credentialsProvider = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultHeaders(Collection var1) {
      this.defaultHeaders = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultRequestConfig(RequestConfig var1) {
      this.defaultRequestConfig = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultSocketConfig(SocketConfig var1) {
      this.defaultSocketConfig = var1;
      return this;
   }

   public final HttpClientBuilder setDnsResolver(DnsResolver var1) {
      this.dnsResolver = var1;
      return this;
   }

   @Deprecated
   public final HttpClientBuilder setHostnameVerifier(X509HostnameVerifier var1) {
      this.hostnameVerifier = var1;
      return this;
   }

   public final HttpClientBuilder setHttpProcessor(HttpProcessor var1) {
      this.httpprocessor = var1;
      return this;
   }

   public final HttpClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy var1) {
      this.keepAliveStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setMaxConnPerRoute(int var1) {
      this.maxConnPerRoute = var1;
      return this;
   }

   public final HttpClientBuilder setMaxConnTotal(int var1) {
      this.maxConnTotal = var1;
      return this;
   }

   public final HttpClientBuilder setProxy(HttpHost var1) {
      this.proxy = var1;
      return this;
   }

   public final HttpClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy var1) {
      this.proxyAuthStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setPublicSuffixMatcher(PublicSuffixMatcher var1) {
      this.publicSuffixMatcher = var1;
      return this;
   }

   public final HttpClientBuilder setRedirectStrategy(RedirectStrategy var1) {
      this.redirectStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setRequestExecutor(HttpRequestExecutor var1) {
      this.requestExec = var1;
      return this;
   }

   public final HttpClientBuilder setRetryHandler(HttpRequestRetryHandler var1) {
      this.retryHandler = var1;
      return this;
   }

   public final HttpClientBuilder setRoutePlanner(HttpRoutePlanner var1) {
      this.routePlanner = var1;
      return this;
   }

   public final HttpClientBuilder setSSLContext(SSLContext var1) {
      this.sslContext = var1;
      return this;
   }

   public final HttpClientBuilder setSSLHostnameVerifier(HostnameVerifier var1) {
      this.hostnameVerifier = var1;
      return this;
   }

   public final HttpClientBuilder setSSLSocketFactory(LayeredConnectionSocketFactory var1) {
      this.sslSocketFactory = var1;
      return this;
   }

   public final HttpClientBuilder setSchemePortResolver(SchemePortResolver var1) {
      this.schemePortResolver = var1;
      return this;
   }

   public final HttpClientBuilder setServiceUnavailableRetryStrategy(ServiceUnavailableRetryStrategy var1) {
      this.serviceUnavailStrategy = var1;
      return this;
   }

   @Deprecated
   public final HttpClientBuilder setSslcontext(SSLContext var1) {
      return this.setSSLContext(var1);
   }

   public final HttpClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy var1) {
      this.targetAuthStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setUserAgent(String var1) {
      this.userAgent = var1;
      return this;
   }

   public final HttpClientBuilder setUserTokenHandler(UserTokenHandler var1) {
      this.userTokenHandler = var1;
      return this;
   }

   public final HttpClientBuilder useSystemProperties() {
      this.systemProperties = true;
      return this;
   }
}
