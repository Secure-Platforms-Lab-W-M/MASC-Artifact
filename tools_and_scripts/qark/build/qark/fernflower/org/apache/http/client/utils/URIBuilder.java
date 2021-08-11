package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;

public class URIBuilder {
   private Charset charset;
   private String encodedAuthority;
   private String encodedFragment;
   private String encodedPath;
   private String encodedQuery;
   private String encodedSchemeSpecificPart;
   private String encodedUserInfo;
   private String fragment;
   private String host;
   private List pathSegments;
   private int port;
   private String query;
   private List queryParams;
   private String scheme;
   private String userInfo;

   public URIBuilder() {
      this.port = -1;
   }

   public URIBuilder(String var1) throws URISyntaxException {
      this.digestURI(new URI(var1));
   }

   public URIBuilder(URI var1) {
      this.digestURI(var1);
   }

   private String buildString() {
      StringBuilder var2 = new StringBuilder();
      String var3 = this.scheme;
      if (var3 != null) {
         var2.append(var3);
         var2.append(':');
      }

      var3 = this.encodedSchemeSpecificPart;
      if (var3 != null) {
         var2.append(var3);
      } else {
         if (this.encodedAuthority != null) {
            var2.append("//");
            var2.append(this.encodedAuthority);
         } else if (this.host != null) {
            var2.append("//");
            var3 = this.encodedUserInfo;
            if (var3 != null) {
               var2.append(var3);
               var2.append("@");
            } else {
               var3 = this.userInfo;
               if (var3 != null) {
                  var2.append(this.encodeUserInfo(var3));
                  var2.append("@");
               }
            }

            if (InetAddressUtils.isIPv6Address(this.host)) {
               var2.append("[");
               var2.append(this.host);
               var2.append("]");
            } else {
               var2.append(this.host);
            }

            if (this.port >= 0) {
               var2.append(":");
               var2.append(this.port);
            }
         }

         var3 = this.encodedPath;
         List var4;
         if (var3 != null) {
            boolean var1;
            if (var2.length() == 0) {
               var1 = true;
            } else {
               var1 = false;
            }

            var2.append(normalizePath(var3, var1));
         } else {
            var4 = this.pathSegments;
            if (var4 != null) {
               var2.append(this.encodePath(var4));
            }
         }

         if (this.encodedQuery != null) {
            var2.append("?");
            var2.append(this.encodedQuery);
         } else {
            var4 = this.queryParams;
            if (var4 != null && !var4.isEmpty()) {
               var2.append("?");
               var2.append(this.encodeUrlForm(this.queryParams));
            } else if (this.query != null) {
               var2.append("?");
               var2.append(this.encodeUric(this.query));
            }
         }
      }

      if (this.encodedFragment != null) {
         var2.append("#");
         var2.append(this.encodedFragment);
      } else if (this.fragment != null) {
         var2.append("#");
         var2.append(this.encodeUric(this.fragment));
      }

      return var2.toString();
   }

   private void digestURI(URI var1) {
      this.scheme = var1.getScheme();
      this.encodedSchemeSpecificPart = var1.getRawSchemeSpecificPart();
      this.encodedAuthority = var1.getRawAuthority();
      this.host = var1.getHost();
      this.port = var1.getPort();
      this.encodedUserInfo = var1.getRawUserInfo();
      this.userInfo = var1.getUserInfo();
      this.encodedPath = var1.getRawPath();
      String var3 = var1.getRawPath();
      Charset var2 = this.charset;
      if (var2 == null) {
         var2 = Consts.UTF_8;
      }

      this.pathSegments = this.parsePath(var3, var2);
      this.encodedQuery = var1.getRawQuery();
      var3 = var1.getRawQuery();
      var2 = this.charset;
      if (var2 == null) {
         var2 = Consts.UTF_8;
      }

      this.queryParams = this.parseQuery(var3, var2);
      this.encodedFragment = var1.getRawFragment();
      this.fragment = var1.getFragment();
   }

   private String encodePath(List var1) {
      Charset var2 = this.charset;
      if (var2 == null) {
         var2 = Consts.UTF_8;
      }

      return URLEncodedUtils.formatSegments(var1, var2);
   }

   private String encodeUric(String var1) {
      Charset var2 = this.charset;
      if (var2 == null) {
         var2 = Consts.UTF_8;
      }

      return URLEncodedUtils.encUric(var1, var2);
   }

   private String encodeUrlForm(List var1) {
      Charset var2 = this.charset;
      if (var2 == null) {
         var2 = Consts.UTF_8;
      }

      return URLEncodedUtils.format((Iterable)var1, (Charset)var2);
   }

   private String encodeUserInfo(String var1) {
      Charset var2 = this.charset;
      if (var2 == null) {
         var2 = Consts.UTF_8;
      }

      return URLEncodedUtils.encUserInfo(var1, var2);
   }

   private static String normalizePath(String var0, boolean var1) {
      if (TextUtils.isBlank(var0)) {
         return "";
      } else {
         String var2 = var0;
         if (!var1) {
            var2 = var0;
            if (!var0.startsWith("/")) {
               StringBuilder var3 = new StringBuilder();
               var3.append("/");
               var3.append(var0);
               var2 = var3.toString();
            }
         }

         return var2;
      }
   }

   private List parsePath(String var1, Charset var2) {
      return var1 != null && !var1.isEmpty() ? URLEncodedUtils.parsePathSegments(var1, var2) : null;
   }

   private List parseQuery(String var1, Charset var2) {
      return var1 != null && !var1.isEmpty() ? URLEncodedUtils.parse(var1, var2) : null;
   }

   public URIBuilder addParameter(String var1, String var2) {
      if (this.queryParams == null) {
         this.queryParams = new ArrayList();
      }

      this.queryParams.add(new BasicNameValuePair(var1, var2));
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder addParameters(List var1) {
      if (this.queryParams == null) {
         this.queryParams = new ArrayList();
      }

      this.queryParams.addAll(var1);
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URI build() throws URISyntaxException {
      return new URI(this.buildString());
   }

   public URIBuilder clearParameters() {
      this.queryParams = null;
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      return this;
   }

   public Charset getCharset() {
      return this.charset;
   }

   public String getFragment() {
      return this.fragment;
   }

   public String getHost() {
      return this.host;
   }

   public String getPath() {
      if (this.pathSegments == null) {
         return null;
      } else {
         StringBuilder var1 = new StringBuilder();
         Iterator var2 = this.pathSegments.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append('/');
            var1.append(var3);
         }

         return var1.toString();
      }
   }

   public List getPathSegments() {
      return (List)(this.pathSegments != null ? new ArrayList(this.pathSegments) : Collections.emptyList());
   }

   public int getPort() {
      return this.port;
   }

   public List getQueryParams() {
      return (List)(this.queryParams != null ? new ArrayList(this.queryParams) : Collections.emptyList());
   }

   public String getScheme() {
      return this.scheme;
   }

   public String getUserInfo() {
      return this.userInfo;
   }

   public boolean isAbsolute() {
      return this.scheme != null;
   }

   public boolean isOpaque() {
      return this.isPathEmpty();
   }

   public boolean isPathEmpty() {
      List var1 = this.pathSegments;
      if (var1 == null || var1.isEmpty()) {
         String var2 = this.encodedPath;
         if (var2 == null || var2.isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public boolean isQueryEmpty() {
      List var1 = this.queryParams;
      return (var1 == null || var1.isEmpty()) && this.encodedQuery == null;
   }

   public URIBuilder removeQuery() {
      this.queryParams = null;
      this.query = null;
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      return this;
   }

   public URIBuilder setCharset(Charset var1) {
      this.charset = var1;
      return this;
   }

   public URIBuilder setCustomQuery(String var1) {
      this.query = var1;
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.queryParams = null;
      return this;
   }

   public URIBuilder setFragment(String var1) {
      this.fragment = var1;
      this.encodedFragment = null;
      return this;
   }

   public URIBuilder setHost(String var1) {
      this.host = var1;
      this.encodedSchemeSpecificPart = null;
      this.encodedAuthority = null;
      return this;
   }

   public URIBuilder setParameter(String var1, String var2) {
      if (this.queryParams == null) {
         this.queryParams = new ArrayList();
      }

      if (!this.queryParams.isEmpty()) {
         Iterator var3 = this.queryParams.iterator();

         while(var3.hasNext()) {
            if (((NameValuePair)var3.next()).getName().equals(var1)) {
               var3.remove();
            }
         }
      }

      this.queryParams.add(new BasicNameValuePair(var1, var2));
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder setParameters(List var1) {
      List var2 = this.queryParams;
      if (var2 == null) {
         this.queryParams = new ArrayList();
      } else {
         var2.clear();
      }

      this.queryParams.addAll(var1);
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder setParameters(NameValuePair... var1) {
      List var4 = this.queryParams;
      if (var4 == null) {
         this.queryParams = new ArrayList();
      } else {
         var4.clear();
      }

      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         NameValuePair var5 = var1[var2];
         this.queryParams.add(var5);
      }

      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      this.query = null;
      return this;
   }

   public URIBuilder setPath(String var1) {
      List var2;
      if (var1 != null) {
         var2 = URLEncodedUtils.splitPathSegments(var1);
      } else {
         var2 = null;
      }

      return this.setPathSegments(var2);
   }

   public URIBuilder setPathSegments(List var1) {
      ArrayList var2;
      if (var1 != null && var1.size() > 0) {
         var2 = new ArrayList(var1);
      } else {
         var2 = null;
      }

      this.pathSegments = var2;
      this.encodedSchemeSpecificPart = null;
      this.encodedPath = null;
      return this;
   }

   public URIBuilder setPathSegments(String... var1) {
      List var2;
      if (var1.length > 0) {
         var2 = Arrays.asList(var1);
      } else {
         var2 = null;
      }

      this.pathSegments = var2;
      this.encodedSchemeSpecificPart = null;
      this.encodedPath = null;
      return this;
   }

   public URIBuilder setPort(int var1) {
      if (var1 < 0) {
         var1 = -1;
      }

      this.port = var1;
      this.encodedSchemeSpecificPart = null;
      this.encodedAuthority = null;
      return this;
   }

   @Deprecated
   public URIBuilder setQuery(String var1) {
      Charset var2 = this.charset;
      if (var2 == null) {
         var2 = Consts.UTF_8;
      }

      this.queryParams = this.parseQuery(var1, var2);
      this.query = null;
      this.encodedQuery = null;
      this.encodedSchemeSpecificPart = null;
      return this;
   }

   public URIBuilder setScheme(String var1) {
      this.scheme = var1;
      return this;
   }

   public URIBuilder setUserInfo(String var1) {
      this.userInfo = var1;
      this.encodedSchemeSpecificPart = null;
      this.encodedAuthority = null;
      this.encodedUserInfo = null;
      return this;
   }

   public URIBuilder setUserInfo(String var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var1);
      var3.append(':');
      var3.append(var2);
      return this.setUserInfo(var3.toString());
   }

   public String toString() {
      return this.buildString();
   }
}
