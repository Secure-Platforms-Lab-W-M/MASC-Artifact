package org.apache.http.impl.client;

import org.apache.http.HttpVersion;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.RequestProxyAuthentication;
import org.apache.http.client.protocol.RequestTargetAuthentication;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.VersionInfo;

@Deprecated
public class DefaultHttpClient extends AbstractHttpClient {
   public DefaultHttpClient() {
      super((ClientConnectionManager)null, (HttpParams)null);
   }

   public DefaultHttpClient(ClientConnectionManager var1) {
      super(var1, (HttpParams)null);
   }

   public DefaultHttpClient(ClientConnectionManager var1, HttpParams var2) {
      super(var1, var2);
   }

   public DefaultHttpClient(HttpParams var1) {
      super((ClientConnectionManager)null, var1);
   }

   public static void setDefaultHttpParams(HttpParams var0) {
      HttpProtocolParams.setVersion(var0, HttpVersion.HTTP_1_1);
      HttpProtocolParams.setContentCharset(var0, HTTP.DEF_CONTENT_CHARSET.name());
      HttpConnectionParams.setTcpNoDelay(var0, true);
      HttpConnectionParams.setSocketBufferSize(var0, 8192);
      HttpProtocolParams.setUserAgent(var0, VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", DefaultHttpClient.class));
   }

   protected HttpParams createHttpParams() {
      SyncBasicHttpParams var1 = new SyncBasicHttpParams();
      setDefaultHttpParams(var1);
      return var1;
   }

   protected BasicHttpProcessor createHttpProcessor() {
      BasicHttpProcessor var1 = new BasicHttpProcessor();
      var1.addInterceptor(new RequestDefaultHeaders());
      var1.addInterceptor(new RequestContent());
      var1.addInterceptor(new RequestTargetHost());
      var1.addInterceptor(new RequestClientConnControl());
      var1.addInterceptor(new RequestUserAgent());
      var1.addInterceptor(new RequestExpectContinue());
      var1.addInterceptor(new RequestAddCookies());
      var1.addInterceptor(new ResponseProcessCookies());
      var1.addInterceptor(new RequestAuthCache());
      var1.addInterceptor(new RequestTargetAuthentication());
      var1.addInterceptor(new RequestProxyAuthentication());
      return var1;
   }
}
