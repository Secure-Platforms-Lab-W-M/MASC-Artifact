package org.apache.http.client.methods;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderGroup;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

public class RequestBuilder {
   private Charset charset;
   private RequestConfig config;
   private HttpEntity entity;
   private HeaderGroup headerGroup;
   private String method;
   private List parameters;
   private URI uri;
   private ProtocolVersion version;

   RequestBuilder() {
      this((String)null);
   }

   RequestBuilder(String var1) {
      this.charset = Consts.UTF_8;
      this.method = var1;
   }

   RequestBuilder(String var1, String var2) {
      this.method = var1;
      URI var3;
      if (var2 != null) {
         var3 = URI.create(var2);
      } else {
         var3 = null;
      }

      this.uri = var3;
   }

   RequestBuilder(String var1, URI var2) {
      this.method = var1;
      this.uri = var2;
   }

   public static RequestBuilder copy(HttpRequest var0) {
      Args.notNull(var0, "HTTP request");
      return (new RequestBuilder()).doCopy(var0);
   }

   public static RequestBuilder create(String var0) {
      Args.notBlank(var0, "HTTP method");
      return new RequestBuilder(var0);
   }

   public static RequestBuilder delete() {
      return new RequestBuilder("DELETE");
   }

   public static RequestBuilder delete(String var0) {
      return new RequestBuilder("DELETE", var0);
   }

   public static RequestBuilder delete(URI var0) {
      return new RequestBuilder("DELETE", var0);
   }

   private RequestBuilder doCopy(HttpRequest var1) {
      if (var1 == null) {
         return this;
      } else {
         this.method = var1.getRequestLine().getMethod();
         this.version = var1.getRequestLine().getProtocolVersion();
         if (this.headerGroup == null) {
            this.headerGroup = new HeaderGroup();
         }

         this.headerGroup.clear();
         this.headerGroup.setHeaders(var1.getAllHeaders());
         this.parameters = null;
         this.entity = null;
         if (var1 instanceof HttpEntityEnclosingRequest) {
            HttpEntity var2 = ((HttpEntityEnclosingRequest)var1).getEntity();
            ContentType var3 = ContentType.get(var2);
            if (var3 != null && var3.getMimeType().equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
               try {
                  List var5 = URLEncodedUtils.parse(var2);
                  if (!var5.isEmpty()) {
                     this.parameters = var5;
                  }
               } catch (IOException var4) {
               }
            } else {
               this.entity = var2;
            }
         }

         if (var1 instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest)var1).getURI();
         } else {
            this.uri = URI.create(var1.getRequestLine().getUri());
         }

         if (var1 instanceof Configurable) {
            this.config = ((Configurable)var1).getConfig();
            return this;
         } else {
            this.config = null;
            return this;
         }
      }
   }

   public static RequestBuilder get() {
      return new RequestBuilder("GET");
   }

   public static RequestBuilder get(String var0) {
      return new RequestBuilder("GET", var0);
   }

   public static RequestBuilder get(URI var0) {
      return new RequestBuilder("GET", var0);
   }

   public static RequestBuilder head() {
      return new RequestBuilder("HEAD");
   }

   public static RequestBuilder head(String var0) {
      return new RequestBuilder("HEAD", var0);
   }

   public static RequestBuilder head(URI var0) {
      return new RequestBuilder("HEAD", var0);
   }

   public static RequestBuilder options() {
      return new RequestBuilder("OPTIONS");
   }

   public static RequestBuilder options(String var0) {
      return new RequestBuilder("OPTIONS", var0);
   }

   public static RequestBuilder options(URI var0) {
      return new RequestBuilder("OPTIONS", var0);
   }

   public static RequestBuilder patch() {
      return new RequestBuilder("PATCH");
   }

   public static RequestBuilder patch(String var0) {
      return new RequestBuilder("PATCH", var0);
   }

   public static RequestBuilder patch(URI var0) {
      return new RequestBuilder("PATCH", var0);
   }

   public static RequestBuilder post() {
      return new RequestBuilder("POST");
   }

   public static RequestBuilder post(String var0) {
      return new RequestBuilder("POST", var0);
   }

   public static RequestBuilder post(URI var0) {
      return new RequestBuilder("POST", var0);
   }

   public static RequestBuilder put() {
      return new RequestBuilder("PUT");
   }

   public static RequestBuilder put(String var0) {
      return new RequestBuilder("PUT", var0);
   }

   public static RequestBuilder put(URI var0) {
      return new RequestBuilder("PUT", var0);
   }

   public static RequestBuilder trace() {
      return new RequestBuilder("TRACE");
   }

   public static RequestBuilder trace(String var0) {
      return new RequestBuilder("TRACE", var0);
   }

   public static RequestBuilder trace(URI var0) {
      return new RequestBuilder("TRACE", var0);
   }

   public RequestBuilder addHeader(String var1, String var2) {
      if (this.headerGroup == null) {
         this.headerGroup = new HeaderGroup();
      }

      this.headerGroup.addHeader(new BasicHeader(var1, var2));
      return this;
   }

   public RequestBuilder addHeader(Header var1) {
      if (this.headerGroup == null) {
         this.headerGroup = new HeaderGroup();
      }

      this.headerGroup.addHeader(var1);
      return this;
   }

   public RequestBuilder addParameter(String var1, String var2) {
      return this.addParameter(new BasicNameValuePair(var1, var2));
   }

   public RequestBuilder addParameter(NameValuePair var1) {
      Args.notNull(var1, "Name value pair");
      if (this.parameters == null) {
         this.parameters = new LinkedList();
      }

      this.parameters.add(var1);
      return this;
   }

   public RequestBuilder addParameters(NameValuePair... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         this.addParameter(var1[var2]);
      }

      return this;
   }

   public HttpUriRequest build() {
      URI var1 = this.uri;
      if (var1 == null) {
         var1 = URI.create("/");
      }

      HttpEntity var4 = this.entity;
      List var5 = this.parameters;
      URI var2 = var1;
      Object var3 = var4;
      if (var5 != null) {
         var2 = var1;
         var3 = var4;
         if (!var5.isEmpty()) {
            if (var4 != null || !"POST".equalsIgnoreCase(this.method) && !"PUT".equalsIgnoreCase(this.method)) {
               label52: {
                  try {
                     var2 = (new URIBuilder(var1)).setCharset(this.charset).addParameters(this.parameters).build();
                  } catch (URISyntaxException var6) {
                     var3 = var4;
                     var2 = var1;
                     break label52;
                  }

                  var3 = var4;
               }
            } else {
               List var9 = this.parameters;
               Charset var8 = this.charset;
               if (var8 == null) {
                  var8 = HTTP.DEF_CONTENT_CHARSET;
               }

               var3 = new UrlEncodedFormEntity(var9, var8);
               var2 = var1;
            }
         }
      }

      Object var7;
      if (var3 == null) {
         var7 = new RequestBuilder.InternalRequest(this.method);
      } else {
         var7 = new RequestBuilder.InternalEntityEclosingRequest(this.method);
         ((RequestBuilder.InternalEntityEclosingRequest)var7).setEntity((HttpEntity)var3);
      }

      ((HttpRequestBase)var7).setProtocolVersion(this.version);
      ((HttpRequestBase)var7).setURI(var2);
      HeaderGroup var10 = this.headerGroup;
      if (var10 != null) {
         ((HttpRequestBase)var7).setHeaders(var10.getAllHeaders());
      }

      ((HttpRequestBase)var7).setConfig(this.config);
      return (HttpUriRequest)var7;
   }

   public Charset getCharset() {
      return this.charset;
   }

   public RequestConfig getConfig() {
      return this.config;
   }

   public HttpEntity getEntity() {
      return this.entity;
   }

   public Header getFirstHeader(String var1) {
      HeaderGroup var2 = this.headerGroup;
      return var2 != null ? var2.getFirstHeader(var1) : null;
   }

   public Header[] getHeaders(String var1) {
      HeaderGroup var2 = this.headerGroup;
      return var2 != null ? var2.getHeaders(var1) : null;
   }

   public Header getLastHeader(String var1) {
      HeaderGroup var2 = this.headerGroup;
      return var2 != null ? var2.getLastHeader(var1) : null;
   }

   public String getMethod() {
      return this.method;
   }

   public List getParameters() {
      return this.parameters != null ? new ArrayList(this.parameters) : new ArrayList();
   }

   public URI getUri() {
      return this.uri;
   }

   public ProtocolVersion getVersion() {
      return this.version;
   }

   public RequestBuilder removeHeader(Header var1) {
      if (this.headerGroup == null) {
         this.headerGroup = new HeaderGroup();
      }

      this.headerGroup.removeHeader(var1);
      return this;
   }

   public RequestBuilder removeHeaders(String var1) {
      if (var1 != null) {
         HeaderGroup var2 = this.headerGroup;
         if (var2 == null) {
            return this;
         } else {
            HeaderIterator var3 = var2.iterator();

            while(var3.hasNext()) {
               if (var1.equalsIgnoreCase(var3.nextHeader().getName())) {
                  var3.remove();
               }
            }

            return this;
         }
      } else {
         return this;
      }
   }

   public RequestBuilder setCharset(Charset var1) {
      this.charset = var1;
      return this;
   }

   public RequestBuilder setConfig(RequestConfig var1) {
      this.config = var1;
      return this;
   }

   public RequestBuilder setEntity(HttpEntity var1) {
      this.entity = var1;
      return this;
   }

   public RequestBuilder setHeader(String var1, String var2) {
      if (this.headerGroup == null) {
         this.headerGroup = new HeaderGroup();
      }

      this.headerGroup.updateHeader(new BasicHeader(var1, var2));
      return this;
   }

   public RequestBuilder setHeader(Header var1) {
      if (this.headerGroup == null) {
         this.headerGroup = new HeaderGroup();
      }

      this.headerGroup.updateHeader(var1);
      return this;
   }

   public RequestBuilder setUri(String var1) {
      URI var2;
      if (var1 != null) {
         var2 = URI.create(var1);
      } else {
         var2 = null;
      }

      this.uri = var2;
      return this;
   }

   public RequestBuilder setUri(URI var1) {
      this.uri = var1;
      return this;
   }

   public RequestBuilder setVersion(ProtocolVersion var1) {
      this.version = var1;
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("RequestBuilder [method=");
      var1.append(this.method);
      var1.append(", charset=");
      var1.append(this.charset);
      var1.append(", version=");
      var1.append(this.version);
      var1.append(", uri=");
      var1.append(this.uri);
      var1.append(", headerGroup=");
      var1.append(this.headerGroup);
      var1.append(", entity=");
      var1.append(this.entity);
      var1.append(", parameters=");
      var1.append(this.parameters);
      var1.append(", config=");
      var1.append(this.config);
      var1.append("]");
      return var1.toString();
   }

   static class InternalEntityEclosingRequest extends HttpEntityEnclosingRequestBase {
      private final String method;

      InternalEntityEclosingRequest(String var1) {
         this.method = var1;
      }

      public String getMethod() {
         return this.method;
      }
   }

   static class InternalRequest extends HttpRequestBase {
      private final String method;

      InternalRequest(String var1) {
         this.method = var1;
      }

      public String getMethod() {
         return this.method;
      }
   }
}
