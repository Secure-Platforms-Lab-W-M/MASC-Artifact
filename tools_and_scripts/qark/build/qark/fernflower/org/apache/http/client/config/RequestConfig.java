package org.apache.http.client.config;

import java.net.InetAddress;
import java.util.Collection;
import org.apache.http.HttpHost;

public class RequestConfig implements Cloneable {
   public static final RequestConfig DEFAULT = (new RequestConfig.Builder()).build();
   private final boolean authenticationEnabled;
   private final boolean circularRedirectsAllowed;
   private final int connectTimeout;
   private final int connectionRequestTimeout;
   private final boolean contentCompressionEnabled;
   private final String cookieSpec;
   private final boolean expectContinueEnabled;
   private final InetAddress localAddress;
   private final int maxRedirects;
   private final boolean normalizeUri;
   private final HttpHost proxy;
   private final Collection proxyPreferredAuthSchemes;
   private final boolean redirectsEnabled;
   private final boolean relativeRedirectsAllowed;
   private final int socketTimeout;
   private final boolean staleConnectionCheckEnabled;
   private final Collection targetPreferredAuthSchemes;

   protected RequestConfig() {
      this(false, (HttpHost)null, (InetAddress)null, false, (String)null, false, false, false, 0, false, (Collection)null, (Collection)null, 0, 0, 0, true, true);
   }

   RequestConfig(boolean var1, HttpHost var2, InetAddress var3, boolean var4, String var5, boolean var6, boolean var7, boolean var8, int var9, boolean var10, Collection var11, Collection var12, int var13, int var14, int var15, boolean var16, boolean var17) {
      this.expectContinueEnabled = var1;
      this.proxy = var2;
      this.localAddress = var3;
      this.staleConnectionCheckEnabled = var4;
      this.cookieSpec = var5;
      this.redirectsEnabled = var6;
      this.relativeRedirectsAllowed = var7;
      this.circularRedirectsAllowed = var8;
      this.maxRedirects = var9;
      this.authenticationEnabled = var10;
      this.targetPreferredAuthSchemes = var11;
      this.proxyPreferredAuthSchemes = var12;
      this.connectionRequestTimeout = var13;
      this.connectTimeout = var14;
      this.socketTimeout = var15;
      this.contentCompressionEnabled = var16;
      this.normalizeUri = var17;
   }

   public static RequestConfig.Builder copy(RequestConfig var0) {
      return (new RequestConfig.Builder()).setExpectContinueEnabled(var0.isExpectContinueEnabled()).setProxy(var0.getProxy()).setLocalAddress(var0.getLocalAddress()).setStaleConnectionCheckEnabled(var0.isStaleConnectionCheckEnabled()).setCookieSpec(var0.getCookieSpec()).setRedirectsEnabled(var0.isRedirectsEnabled()).setRelativeRedirectsAllowed(var0.isRelativeRedirectsAllowed()).setCircularRedirectsAllowed(var0.isCircularRedirectsAllowed()).setMaxRedirects(var0.getMaxRedirects()).setAuthenticationEnabled(var0.isAuthenticationEnabled()).setTargetPreferredAuthSchemes(var0.getTargetPreferredAuthSchemes()).setProxyPreferredAuthSchemes(var0.getProxyPreferredAuthSchemes()).setConnectionRequestTimeout(var0.getConnectionRequestTimeout()).setConnectTimeout(var0.getConnectTimeout()).setSocketTimeout(var0.getSocketTimeout()).setDecompressionEnabled(var0.isDecompressionEnabled()).setContentCompressionEnabled(var0.isContentCompressionEnabled()).setNormalizeUri(var0.isNormalizeUri());
   }

   public static RequestConfig.Builder custom() {
      return new RequestConfig.Builder();
   }

   protected RequestConfig clone() throws CloneNotSupportedException {
      return (RequestConfig)super.clone();
   }

   public int getConnectTimeout() {
      return this.connectTimeout;
   }

   public int getConnectionRequestTimeout() {
      return this.connectionRequestTimeout;
   }

   public String getCookieSpec() {
      return this.cookieSpec;
   }

   public InetAddress getLocalAddress() {
      return this.localAddress;
   }

   public int getMaxRedirects() {
      return this.maxRedirects;
   }

   public HttpHost getProxy() {
      return this.proxy;
   }

   public Collection getProxyPreferredAuthSchemes() {
      return this.proxyPreferredAuthSchemes;
   }

   public int getSocketTimeout() {
      return this.socketTimeout;
   }

   public Collection getTargetPreferredAuthSchemes() {
      return this.targetPreferredAuthSchemes;
   }

   public boolean isAuthenticationEnabled() {
      return this.authenticationEnabled;
   }

   public boolean isCircularRedirectsAllowed() {
      return this.circularRedirectsAllowed;
   }

   public boolean isContentCompressionEnabled() {
      return this.contentCompressionEnabled;
   }

   @Deprecated
   public boolean isDecompressionEnabled() {
      return this.contentCompressionEnabled;
   }

   public boolean isExpectContinueEnabled() {
      return this.expectContinueEnabled;
   }

   public boolean isNormalizeUri() {
      return this.normalizeUri;
   }

   public boolean isRedirectsEnabled() {
      return this.redirectsEnabled;
   }

   public boolean isRelativeRedirectsAllowed() {
      return this.relativeRedirectsAllowed;
   }

   @Deprecated
   public boolean isStaleConnectionCheckEnabled() {
      return this.staleConnectionCheckEnabled;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      var1.append("expectContinueEnabled=");
      var1.append(this.expectContinueEnabled);
      var1.append(", proxy=");
      var1.append(this.proxy);
      var1.append(", localAddress=");
      var1.append(this.localAddress);
      var1.append(", cookieSpec=");
      var1.append(this.cookieSpec);
      var1.append(", redirectsEnabled=");
      var1.append(this.redirectsEnabled);
      var1.append(", relativeRedirectsAllowed=");
      var1.append(this.relativeRedirectsAllowed);
      var1.append(", maxRedirects=");
      var1.append(this.maxRedirects);
      var1.append(", circularRedirectsAllowed=");
      var1.append(this.circularRedirectsAllowed);
      var1.append(", authenticationEnabled=");
      var1.append(this.authenticationEnabled);
      var1.append(", targetPreferredAuthSchemes=");
      var1.append(this.targetPreferredAuthSchemes);
      var1.append(", proxyPreferredAuthSchemes=");
      var1.append(this.proxyPreferredAuthSchemes);
      var1.append(", connectionRequestTimeout=");
      var1.append(this.connectionRequestTimeout);
      var1.append(", connectTimeout=");
      var1.append(this.connectTimeout);
      var1.append(", socketTimeout=");
      var1.append(this.socketTimeout);
      var1.append(", contentCompressionEnabled=");
      var1.append(this.contentCompressionEnabled);
      var1.append(", normalizeUri=");
      var1.append(this.normalizeUri);
      var1.append("]");
      return var1.toString();
   }

   public static class Builder {
      private boolean authenticationEnabled = true;
      private boolean circularRedirectsAllowed;
      private int connectTimeout = -1;
      private int connectionRequestTimeout = -1;
      private boolean contentCompressionEnabled = true;
      private String cookieSpec;
      private boolean expectContinueEnabled;
      private InetAddress localAddress;
      private int maxRedirects = 50;
      private boolean normalizeUri = true;
      private HttpHost proxy;
      private Collection proxyPreferredAuthSchemes;
      private boolean redirectsEnabled = true;
      private boolean relativeRedirectsAllowed = true;
      private int socketTimeout = -1;
      private boolean staleConnectionCheckEnabled = false;
      private Collection targetPreferredAuthSchemes;

      Builder() {
      }

      public RequestConfig build() {
         return new RequestConfig(this.expectContinueEnabled, this.proxy, this.localAddress, this.staleConnectionCheckEnabled, this.cookieSpec, this.redirectsEnabled, this.relativeRedirectsAllowed, this.circularRedirectsAllowed, this.maxRedirects, this.authenticationEnabled, this.targetPreferredAuthSchemes, this.proxyPreferredAuthSchemes, this.connectionRequestTimeout, this.connectTimeout, this.socketTimeout, this.contentCompressionEnabled, this.normalizeUri);
      }

      public RequestConfig.Builder setAuthenticationEnabled(boolean var1) {
         this.authenticationEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setCircularRedirectsAllowed(boolean var1) {
         this.circularRedirectsAllowed = var1;
         return this;
      }

      public RequestConfig.Builder setConnectTimeout(int var1) {
         this.connectTimeout = var1;
         return this;
      }

      public RequestConfig.Builder setConnectionRequestTimeout(int var1) {
         this.connectionRequestTimeout = var1;
         return this;
      }

      public RequestConfig.Builder setContentCompressionEnabled(boolean var1) {
         this.contentCompressionEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setCookieSpec(String var1) {
         this.cookieSpec = var1;
         return this;
      }

      @Deprecated
      public RequestConfig.Builder setDecompressionEnabled(boolean var1) {
         this.contentCompressionEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setExpectContinueEnabled(boolean var1) {
         this.expectContinueEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setLocalAddress(InetAddress var1) {
         this.localAddress = var1;
         return this;
      }

      public RequestConfig.Builder setMaxRedirects(int var1) {
         this.maxRedirects = var1;
         return this;
      }

      public RequestConfig.Builder setNormalizeUri(boolean var1) {
         this.normalizeUri = var1;
         return this;
      }

      public RequestConfig.Builder setProxy(HttpHost var1) {
         this.proxy = var1;
         return this;
      }

      public RequestConfig.Builder setProxyPreferredAuthSchemes(Collection var1) {
         this.proxyPreferredAuthSchemes = var1;
         return this;
      }

      public RequestConfig.Builder setRedirectsEnabled(boolean var1) {
         this.redirectsEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setRelativeRedirectsAllowed(boolean var1) {
         this.relativeRedirectsAllowed = var1;
         return this;
      }

      public RequestConfig.Builder setSocketTimeout(int var1) {
         this.socketTimeout = var1;
         return this;
      }

      @Deprecated
      public RequestConfig.Builder setStaleConnectionCheckEnabled(boolean var1) {
         this.staleConnectionCheckEnabled = var1;
         return this;
      }

      public RequestConfig.Builder setTargetPreferredAuthSchemes(Collection var1) {
         this.targetPreferredAuthSchemes = var1;
         return this;
      }
   }
}
