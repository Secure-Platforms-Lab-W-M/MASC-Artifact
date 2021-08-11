package org.jsoup.helper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.Proxy.Type;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TokenQueue;

public class HttpConnection implements Connection {
   public static final String CONTENT_ENCODING = "Content-Encoding";
   private static final String CONTENT_TYPE = "Content-Type";
   private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
   private static final int HTTP_TEMP_REDIR = 307;
   private static final String MULTIPART_FORM_DATA = "multipart/form-data";
   private Connection.Request req = new HttpConnection.Request();
   private Connection.Response res = new HttpConnection.Response();

   private HttpConnection() {
   }

   public static Connection connect(String var0) {
      HttpConnection var1 = new HttpConnection();
      var1.url(var0);
      return var1;
   }

   public static Connection connect(URL var0) {
      HttpConnection var1 = new HttpConnection();
      var1.url(var0);
      return var1;
   }

   private static String encodeMimeName(String var0) {
      return var0 == null ? null : var0.replaceAll("\"", "%22");
   }

   private static String encodeUrl(String var0) {
      return var0 == null ? null : var0.replaceAll(" ", "%20");
   }

   private static boolean needsMultipart(Connection.Request var0) {
      boolean var2 = false;
      Iterator var3 = var0.data().iterator();

      boolean var1;
      while(true) {
         var1 = var2;
         if (!var3.hasNext()) {
            break;
         }

         if (((Connection.KeyVal)var3.next()).hasInputStream()) {
            var1 = true;
            break;
         }
      }

      return var1;
   }

   public Connection cookie(String var1, String var2) {
      this.req.cookie(var1, var2);
      return this;
   }

   public Connection cookies(Map var1) {
      Validate.notNull(var1, "Cookie map must not be null");
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         this.req.cookie((String)var2.getKey(), (String)var2.getValue());
      }

      return this;
   }

   public Connection.KeyVal data(String var1) {
      Validate.notEmpty(var1, "Data key must not be empty");
      Iterator var2 = this.request().data().iterator();

      Connection.KeyVal var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Connection.KeyVal)var2.next();
      } while(!var3.key().equals(var1));

      return var3;
   }

   public Connection data(String var1, String var2) {
      this.req.data(HttpConnection.KeyVal.create(var1, var2));
      return this;
   }

   public Connection data(String var1, String var2, InputStream var3) {
      this.req.data(HttpConnection.KeyVal.create(var1, var2, var3));
      return this;
   }

   public Connection data(Collection var1) {
      Validate.notNull(var1, "Data collection must not be null");
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Connection.KeyVal var2 = (Connection.KeyVal)var3.next();
         this.req.data(var2);
      }

      return this;
   }

   public Connection data(Map var1) {
      Validate.notNull(var1, "Data map must not be null");
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         this.req.data(HttpConnection.KeyVal.create((String)var2.getKey(), (String)var2.getValue()));
      }

      return this;
   }

   public Connection data(String... var1) {
      Validate.notNull(var1, "Data key value pairs must not be null");
      boolean var3;
      if (var1.length % 2 == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Must supply an even number of key value pairs");

      for(int var2 = 0; var2 < var1.length; var2 += 2) {
         String var4 = var1[var2];
         String var5 = var1[var2 + 1];
         Validate.notEmpty(var4, "Data key must not be empty");
         Validate.notNull(var5, "Data value must not be null");
         this.req.data(HttpConnection.KeyVal.create(var4, var5));
      }

      return this;
   }

   public Connection.Response execute() throws IOException {
      this.res = HttpConnection.Response.execute(this.req);
      return this.res;
   }

   public Connection followRedirects(boolean var1) {
      this.req.followRedirects(var1);
      return this;
   }

   public Document get() throws IOException {
      this.req.method(Connection.Method.GET);
      this.execute();
      return this.res.parse();
   }

   public Connection header(String var1, String var2) {
      this.req.header(var1, var2);
      return this;
   }

   public Connection ignoreContentType(boolean var1) {
      this.req.ignoreContentType(var1);
      return this;
   }

   public Connection ignoreHttpErrors(boolean var1) {
      this.req.ignoreHttpErrors(var1);
      return this;
   }

   public Connection maxBodySize(int var1) {
      this.req.maxBodySize(var1);
      return this;
   }

   public Connection method(Connection.Method var1) {
      this.req.method(var1);
      return this;
   }

   public Connection parser(Parser var1) {
      this.req.parser(var1);
      return this;
   }

   public Document post() throws IOException {
      this.req.method(Connection.Method.POST);
      this.execute();
      return this.res.parse();
   }

   public Connection postDataCharset(String var1) {
      this.req.postDataCharset(var1);
      return this;
   }

   public Connection proxy(String var1, int var2) {
      this.req.proxy(var1, var2);
      return this;
   }

   public Connection proxy(Proxy var1) {
      this.req.proxy(var1);
      return this;
   }

   public Connection referrer(String var1) {
      Validate.notNull(var1, "Referrer must not be null");
      this.req.header("Referer", var1);
      return this;
   }

   public Connection.Request request() {
      return this.req;
   }

   public Connection request(Connection.Request var1) {
      this.req = var1;
      return this;
   }

   public Connection requestBody(String var1) {
      this.req.requestBody(var1);
      return this;
   }

   public Connection.Response response() {
      return this.res;
   }

   public Connection response(Connection.Response var1) {
      this.res = var1;
      return this;
   }

   public Connection timeout(int var1) {
      this.req.timeout(var1);
      return this;
   }

   public Connection url(String var1) {
      Validate.notEmpty(var1, "Must supply a valid URL");

      try {
         this.req.url(new URL(encodeUrl(var1)));
         return this;
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException("Malformed URL: " + var1, var3);
      }
   }

   public Connection url(URL var1) {
      this.req.url(var1);
      return this;
   }

   public Connection userAgent(String var1) {
      Validate.notNull(var1, "User agent must not be null");
      this.req.header("User-Agent", var1);
      return this;
   }

   public Connection validateTLSCertificates(boolean var1) {
      this.req.validateTLSCertificates(var1);
      return this;
   }

   private abstract static class Base implements Connection.Base {
      Map cookies;
      Map headers;
      Connection.Method method;
      URL url;

      private Base() {
         this.headers = new LinkedHashMap();
         this.cookies = new LinkedHashMap();
      }

      // $FF: synthetic method
      Base(Object var1) {
         this();
      }

      private String getHeaderCaseInsensitive(String var1) {
         Validate.notNull(var1, "Header name must not be null");
         String var3 = (String)this.headers.get(var1);
         String var2 = var3;
         if (var3 == null) {
            var2 = (String)this.headers.get(var1.toLowerCase());
         }

         var3 = var2;
         if (var2 == null) {
            Entry var4 = this.scanHeaders(var1);
            var3 = var2;
            if (var4 != null) {
               var3 = (String)var4.getValue();
            }
         }

         return var3;
      }

      private Entry scanHeaders(String var1) {
         var1 = var1.toLowerCase();
         Iterator var2 = this.headers.entrySet().iterator();

         Entry var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (Entry)var2.next();
         } while(!((String)var3.getKey()).toLowerCase().equals(var1));

         return var3;
      }

      public String cookie(String var1) {
         Validate.notEmpty(var1, "Cookie name must not be empty");
         return (String)this.cookies.get(var1);
      }

      public Connection.Base cookie(String var1, String var2) {
         Validate.notEmpty(var1, "Cookie name must not be empty");
         Validate.notNull(var2, "Cookie value must not be null");
         this.cookies.put(var1, var2);
         return this;
      }

      public Map cookies() {
         return this.cookies;
      }

      public boolean hasCookie(String var1) {
         Validate.notEmpty(var1, "Cookie name must not be empty");
         return this.cookies.containsKey(var1);
      }

      public boolean hasHeader(String var1) {
         Validate.notEmpty(var1, "Header name must not be empty");
         return this.getHeaderCaseInsensitive(var1) != null;
      }

      public boolean hasHeaderWithValue(String var1, String var2) {
         return this.hasHeader(var1) && this.header(var1).equalsIgnoreCase(var2);
      }

      public String header(String var1) {
         Validate.notNull(var1, "Header name must not be null");
         return this.getHeaderCaseInsensitive(var1);
      }

      public Connection.Base header(String var1, String var2) {
         Validate.notEmpty(var1, "Header name must not be empty");
         Validate.notNull(var2, "Header value must not be null");
         this.removeHeader(var1);
         this.headers.put(var1, var2);
         return this;
      }

      public Map headers() {
         return this.headers;
      }

      public Connection.Base method(Connection.Method var1) {
         Validate.notNull(var1, "Method must not be null");
         this.method = var1;
         return this;
      }

      public Connection.Method method() {
         return this.method;
      }

      public Connection.Base removeCookie(String var1) {
         Validate.notEmpty(var1, "Cookie name must not be empty");
         this.cookies.remove(var1);
         return this;
      }

      public Connection.Base removeHeader(String var1) {
         Validate.notEmpty(var1, "Header name must not be empty");
         Entry var2 = this.scanHeaders(var1);
         if (var2 != null) {
            this.headers.remove(var2.getKey());
         }

         return this;
      }

      public URL url() {
         return this.url;
      }

      public Connection.Base url(URL var1) {
         Validate.notNull(var1, "URL must not be null");
         this.url = var1;
         return this;
      }
   }

   public static class KeyVal implements Connection.KeyVal {
      private String key;
      private InputStream stream;
      private String value;

      private KeyVal() {
      }

      public static HttpConnection.KeyVal create(String var0, String var1) {
         return (new HttpConnection.KeyVal()).key(var0).value(var1);
      }

      public static HttpConnection.KeyVal create(String var0, String var1, InputStream var2) {
         return (new HttpConnection.KeyVal()).key(var0).value(var1).inputStream(var2);
      }

      public boolean hasInputStream() {
         return this.stream != null;
      }

      public InputStream inputStream() {
         return this.stream;
      }

      public HttpConnection.KeyVal inputStream(InputStream var1) {
         Validate.notNull(this.value, "Data input stream must not be null");
         this.stream = var1;
         return this;
      }

      public String key() {
         return this.key;
      }

      public HttpConnection.KeyVal key(String var1) {
         Validate.notEmpty(var1, "Data key must not be empty");
         this.key = var1;
         return this;
      }

      public String toString() {
         return this.key + "=" + this.value;
      }

      public String value() {
         return this.value;
      }

      public HttpConnection.KeyVal value(String var1) {
         Validate.notNull(var1, "Data value must not be null");
         this.value = var1;
         return this;
      }
   }

   public static class Request extends HttpConnection.Base implements Connection.Request {
      private String body;
      private Collection data;
      private boolean followRedirects;
      private boolean ignoreContentType;
      private boolean ignoreHttpErrors;
      private int maxBodySizeBytes;
      private Parser parser;
      private boolean parserDefined;
      private String postDataCharset;
      private Proxy proxy;
      private int timeoutMilliseconds;
      private boolean validateTSLCertificates;

      private Request() {
         super(null);
         this.body = null;
         this.ignoreHttpErrors = false;
         this.ignoreContentType = false;
         this.parserDefined = false;
         this.validateTSLCertificates = true;
         this.postDataCharset = "UTF-8";
         this.timeoutMilliseconds = 3000;
         this.maxBodySizeBytes = 1048576;
         this.followRedirects = true;
         this.data = new ArrayList();
         this.method = Connection.Method.GET;
         this.headers.put("Accept-Encoding", "gzip");
         this.parser = Parser.htmlParser();
      }

      // $FF: synthetic method
      Request(Object var1) {
         this();
      }

      public Collection data() {
         return this.data;
      }

      public HttpConnection.Request data(Connection.KeyVal var1) {
         Validate.notNull(var1, "Key val must not be null");
         this.data.add(var1);
         return this;
      }

      public Connection.Request followRedirects(boolean var1) {
         this.followRedirects = var1;
         return this;
      }

      public boolean followRedirects() {
         return this.followRedirects;
      }

      public Connection.Request ignoreContentType(boolean var1) {
         this.ignoreContentType = var1;
         return this;
      }

      public boolean ignoreContentType() {
         return this.ignoreContentType;
      }

      public Connection.Request ignoreHttpErrors(boolean var1) {
         this.ignoreHttpErrors = var1;
         return this;
      }

      public boolean ignoreHttpErrors() {
         return this.ignoreHttpErrors;
      }

      public int maxBodySize() {
         return this.maxBodySizeBytes;
      }

      public Connection.Request maxBodySize(int var1) {
         boolean var2;
         if (var1 >= 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         Validate.isTrue(var2, "maxSize must be 0 (unlimited) or larger");
         this.maxBodySizeBytes = var1;
         return this;
      }

      public HttpConnection.Request parser(Parser var1) {
         this.parser = var1;
         this.parserDefined = true;
         return this;
      }

      public Parser parser() {
         return this.parser;
      }

      public String postDataCharset() {
         return this.postDataCharset;
      }

      public Connection.Request postDataCharset(String var1) {
         Validate.notNull(var1, "Charset must not be null");
         if (!Charset.isSupported(var1)) {
            throw new IllegalCharsetNameException(var1);
         } else {
            this.postDataCharset = var1;
            return this;
         }
      }

      public Proxy proxy() {
         return this.proxy;
      }

      public HttpConnection.Request proxy(String var1, int var2) {
         this.proxy = new Proxy(Type.HTTP, InetSocketAddress.createUnresolved(var1, var2));
         return this;
      }

      public HttpConnection.Request proxy(Proxy var1) {
         this.proxy = var1;
         return this;
      }

      public String requestBody() {
         return this.body;
      }

      public Connection.Request requestBody(String var1) {
         this.body = var1;
         return this;
      }

      public int timeout() {
         return this.timeoutMilliseconds;
      }

      public HttpConnection.Request timeout(int var1) {
         boolean var2;
         if (var1 >= 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         Validate.isTrue(var2, "Timeout milliseconds must be 0 (infinite) or greater");
         this.timeoutMilliseconds = var1;
         return this;
      }

      public void validateTLSCertificates(boolean var1) {
         this.validateTSLCertificates = var1;
      }

      public boolean validateTLSCertificates() {
         return this.validateTSLCertificates;
      }
   }

   public static class Response extends HttpConnection.Base implements Connection.Response {
      private static final String LOCATION = "Location";
      private static final int MAX_REDIRECTS = 20;
      private static SSLSocketFactory sslSocketFactory;
      private static final Pattern xmlContentTypeRxp = Pattern.compile("(application|text)/\\w*\\+?xml.*");
      private ByteBuffer byteData;
      private String charset;
      private String contentType;
      private boolean executed = false;
      private int numRedirects = 0;
      private Connection.Request req;
      private int statusCode;
      private String statusMessage;

      Response() {
         super(null);
      }

      private Response(HttpConnection.Response var1) throws IOException {
         super(null);
         if (var1 != null) {
            this.numRedirects = var1.numRedirects + 1;
            if (this.numRedirects >= 20) {
               throw new IOException(String.format("Too many redirects occurred trying to load URL %s", var1.url()));
            }
         }

      }

      private static HttpURLConnection createConnection(Connection.Request var0) throws IOException {
         URLConnection var1;
         if (var0.proxy() == null) {
            var1 = var0.url().openConnection();
         } else {
            var1 = var0.url().openConnection(var0.proxy());
         }

         HttpURLConnection var4 = (HttpURLConnection)((HttpURLConnection)var1);
         var4.setRequestMethod(var0.method().name());
         var4.setInstanceFollowRedirects(false);
         var4.setConnectTimeout(var0.timeout());
         var4.setReadTimeout(var0.timeout());
         if (var4 instanceof HttpsURLConnection && !var0.validateTLSCertificates()) {
            initUnSecureTSL();
            ((HttpsURLConnection)var4).setSSLSocketFactory(sslSocketFactory);
            ((HttpsURLConnection)var4).setHostnameVerifier(getInsecureVerifier());
         }

         if (var0.method().hasBody()) {
            var4.setDoOutput(true);
         }

         if (var0.cookies().size() > 0) {
            var4.addRequestProperty("Cookie", getRequestCookieString(var0));
         }

         Iterator var3 = var0.headers().entrySet().iterator();

         while(var3.hasNext()) {
            Entry var2 = (Entry)var3.next();
            var4.addRequestProperty((String)var2.getKey(), (String)var2.getValue());
         }

         return var4;
      }

      private static LinkedHashMap createHeaderMap(HttpURLConnection var0) {
         LinkedHashMap var3 = new LinkedHashMap();
         int var1 = 0;

         while(true) {
            String var4 = var0.getHeaderFieldKey(var1);
            String var5 = var0.getHeaderField(var1);
            if (var4 == null && var5 == null) {
               return var3;
            }

            int var2 = var1 + 1;
            var1 = var2;
            if (var4 != null) {
               var1 = var2;
               if (var5 != null) {
                  if (var3.containsKey(var4)) {
                     ((List)var3.get(var4)).add(var5);
                     var1 = var2;
                  } else {
                     ArrayList var6 = new ArrayList();
                     var6.add(var5);
                     var3.put(var4, var6);
                     var1 = var2;
                  }
               }
            }
         }
      }

      static HttpConnection.Response execute(Connection.Request var0) throws IOException {
         return execute(var0, (HttpConnection.Response)null);
      }

      static HttpConnection.Response execute(Connection.Request var0, HttpConnection.Response var1) throws IOException {
         Validate.notNull(var0, "Request must not be null");
         String var5 = var0.url().getProtocol();
         if (!var5.equals("http") && !var5.equals("https")) {
            throw new MalformedURLException("Only http & https protocols supported");
         } else {
            boolean var4 = var0.method().hasBody();
            boolean var3;
            if (var0.requestBody() != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            if (!var4) {
               Validate.isFalse(var3, "Cannot set a request body for HTTP method " + var0.method());
            }

            var5 = null;
            if (var0.data().size() > 0 && (!var4 || var3)) {
               serialiseRequestUrl(var0);
            } else if (var4) {
               var5 = setOutputContentType(var0);
            }

            HttpURLConnection var7 = createConnection(var0);

            Throwable var10000;
            Throwable var609;
            label6171: {
               boolean var10001;
               try {
                  var7.connect();
                  if (var7.getDoOutput()) {
                     writePost(var0, var7.getOutputStream(), var5);
                  }
               } catch (Throwable var608) {
                  var10000 = var608;
                  var10001 = false;
                  break label6171;
               }

               int var2;
               HttpConnection.Response var8;
               String var611;
               label6183: {
                  try {
                     var2 = var7.getResponseCode();
                     var8 = new HttpConnection.Response(var1);
                     var8.setupFromConnection(var7, var1);
                     var8.req = var0;
                     if (var8.hasHeader("Location") && var0.followRedirects()) {
                        break label6183;
                     }
                  } catch (Throwable var607) {
                     var10000 = var607;
                     var10001 = false;
                     break label6171;
                  }

                  if (var2 < 200 || var2 >= 400) {
                     try {
                        if (!var0.ignoreHttpErrors()) {
                           throw new HttpStatusException("HTTP error fetching URL", var2, var0.url().toString());
                        }
                     } catch (Throwable var606) {
                        var10000 = var606;
                        var10001 = false;
                        break label6171;
                     }
                  }

                  try {
                     var611 = var8.contentType();
                  } catch (Throwable var605) {
                     var10000 = var605;
                     var10001 = false;
                     break label6171;
                  }

                  if (var611 != null) {
                     try {
                        if (!var0.ignoreContentType() && !var611.startsWith("text/") && !xmlContentTypeRxp.matcher(var611).matches()) {
                           throw new UnsupportedMimeTypeException("Unhandled content type. Must be text/*, application/xml, or application/xhtml+xml", var611, var0.url().toString());
                        }
                     } catch (Throwable var604) {
                        var10000 = var604;
                        var10001 = false;
                        break label6171;
                     }
                  }

                  if (var611 != null) {
                     try {
                        if (xmlContentTypeRxp.matcher(var611).matches() && var0 instanceof HttpConnection.Request && !((HttpConnection.Request)var0).parserDefined) {
                           var0.parser(Parser.xmlParser());
                        }
                     } catch (Throwable var603) {
                        var10000 = var603;
                        var10001 = false;
                        break label6171;
                     }
                  }

                  label6123: {
                     label6122: {
                        Connection.Method var612;
                        Connection.Method var615;
                        try {
                           var8.charset = DataUtil.getCharsetFromContentType(var8.contentType);
                           if (var7.getContentLength() == 0) {
                              break label6122;
                           }

                           var612 = var0.method();
                           var615 = Connection.Method.HEAD;
                        } catch (Throwable var602) {
                           var10000 = var602;
                           var10001 = false;
                           break label6171;
                        }

                        if (var612 != var615) {
                           var1 = null;
                           Object var616 = var1;

                           Object var6;
                           label6174: {
                              label6175: {
                                 InputStream var613;
                                 label6176: {
                                    label6177: {
                                       try {
                                          if (var7.getErrorStream() == null) {
                                             break label6177;
                                          }
                                       } catch (Throwable var601) {
                                          var10000 = var601;
                                          var10001 = false;
                                          break label6175;
                                       }

                                       var616 = var1;

                                       try {
                                          var613 = var7.getErrorStream();
                                          break label6176;
                                       } catch (Throwable var600) {
                                          var10000 = var600;
                                          var10001 = false;
                                          break label6175;
                                       }
                                    }

                                    var616 = var1;

                                    try {
                                       var613 = var7.getInputStream();
                                    } catch (Throwable var599) {
                                       var10000 = var599;
                                       var10001 = false;
                                       break label6175;
                                    }
                                 }

                                 var6 = var613;
                                 var616 = var613;

                                 label6178: {
                                    try {
                                       if (!var8.hasHeaderWithValue("Content-Encoding", "gzip")) {
                                          break label6178;
                                       }
                                    } catch (Throwable var598) {
                                       var10000 = var598;
                                       var10001 = false;
                                       break label6175;
                                    }

                                    var616 = var613;

                                    try {
                                       var6 = new GZIPInputStream(var613);
                                    } catch (Throwable var597) {
                                       var10000 = var597;
                                       var10001 = false;
                                       break label6175;
                                    }
                                 }

                                 var616 = var6;

                                 label6091:
                                 try {
                                    var8.byteData = DataUtil.readToByteBuffer((InputStream)var6, var0.maxBodySize());
                                    break label6174;
                                 } catch (Throwable var596) {
                                    var10000 = var596;
                                    var10001 = false;
                                    break label6091;
                                 }
                              }

                              var609 = var10000;
                              if (var616 != null) {
                                 try {
                                    ((InputStream)var616).close();
                                 } catch (Throwable var586) {
                                    var10000 = var586;
                                    var10001 = false;
                                    break label6171;
                                 }
                              }

                              try {
                                 throw var609;
                              } catch (Throwable var585) {
                                 var10000 = var585;
                                 var10001 = false;
                                 break label6171;
                              }
                           }

                           if (var6 != null) {
                              try {
                                 ((InputStream)var6).close();
                              } catch (Throwable var595) {
                                 var10000 = var595;
                                 var10001 = false;
                                 break label6171;
                              }
                           }
                           break label6123;
                        }
                     }

                     try {
                        var8.byteData = DataUtil.emptyByteBuffer();
                     } catch (Throwable var594) {
                        var10000 = var594;
                        var10001 = false;
                        break label6171;
                     }
                  }

                  var7.disconnect();
                  var8.executed = true;
                  return var8;
               }

               if (var2 != 307) {
                  try {
                     var0.method(Connection.Method.GET);
                     var0.data().clear();
                  } catch (Throwable var593) {
                     var10000 = var593;
                     var10001 = false;
                     break label6171;
                  }
               }

               try {
                  var5 = var8.header("Location");
               } catch (Throwable var592) {
                  var10000 = var592;
                  var10001 = false;
                  break label6171;
               }

               var611 = var5;
               if (var5 != null) {
                  label6179: {
                     var611 = var5;

                     try {
                        if (!var5.startsWith("http:/")) {
                           break label6179;
                        }
                     } catch (Throwable var591) {
                        var10000 = var591;
                        var10001 = false;
                        break label6171;
                     }

                     var611 = var5;

                     try {
                        if (var5.charAt(6) != '/') {
                           var611 = var5.substring(6);
                        }
                     } catch (Throwable var590) {
                        var10000 = var590;
                        var10001 = false;
                        break label6171;
                     }
                  }
               }

               Iterator var614;
               try {
                  var0.url(StringUtil.resolve(var0.url(), HttpConnection.encodeUrl(var611)));
                  var614 = var8.cookies.entrySet().iterator();
               } catch (Throwable var588) {
                  var10000 = var588;
                  var10001 = false;
                  break label6171;
               }

               while(true) {
                  try {
                     if (!var614.hasNext()) {
                        break;
                     }

                     Entry var617 = (Entry)var614.next();
                     var0.cookie((String)var617.getKey(), (String)var617.getValue());
                  } catch (Throwable var589) {
                     var10000 = var589;
                     var10001 = false;
                     break label6171;
                  }
               }

               HttpConnection.Response var610;
               try {
                  var610 = execute(var0, var8);
               } catch (Throwable var587) {
                  var10000 = var587;
                  var10001 = false;
                  break label6171;
               }

               var7.disconnect();
               return var610;
            }

            var609 = var10000;
            var7.disconnect();
            throw var609;
         }
      }

      private static HostnameVerifier getInsecureVerifier() {
         return new HostnameVerifier() {
            public boolean verify(String var1, SSLSession var2) {
               return true;
            }
         };
      }

      private static String getRequestCookieString(Connection.Request var0) {
         StringBuilder var2 = new StringBuilder();
         boolean var1 = true;

         Entry var3;
         for(Iterator var4 = var0.cookies().entrySet().iterator(); var4.hasNext(); var2.append((String)var3.getKey()).append('=').append((String)var3.getValue())) {
            var3 = (Entry)var4.next();
            if (!var1) {
               var2.append("; ");
            } else {
               var1 = false;
            }
         }

         return var2.toString();
      }

      private static void initUnSecureTSL() throws IOException {
         // $FF: Couldn't be decompiled
      }

      private static void serialiseRequestUrl(Connection.Request var0) throws IOException {
         URL var3 = var0.url();
         StringBuilder var2 = new StringBuilder();
         boolean var1 = true;
         var2.append(var3.getProtocol()).append("://").append(var3.getAuthority()).append(var3.getPath()).append("?");
         if (var3.getQuery() != null) {
            var2.append(var3.getQuery());
            var1 = false;
         }

         Connection.KeyVal var4;
         for(Iterator var5 = var0.data().iterator(); var5.hasNext(); var2.append(URLEncoder.encode(var4.key(), "UTF-8")).append('=').append(URLEncoder.encode(var4.value(), "UTF-8"))) {
            var4 = (Connection.KeyVal)var5.next();
            Validate.isFalse(var4.hasInputStream(), "InputStream data not supported in URL query string.");
            if (!var1) {
               var2.append('&');
            } else {
               var1 = false;
            }
         }

         var0.url(new URL(var2.toString()));
         var0.data().clear();
      }

      private static String setOutputContentType(Connection.Request var0) {
         if (HttpConnection.needsMultipart(var0)) {
            String var1 = DataUtil.mimeBoundary();
            var0.header("Content-Type", "multipart/form-data; boundary=" + var1);
            return var1;
         } else {
            var0.header("Content-Type", "application/x-www-form-urlencoded; charset=" + var0.postDataCharset());
            return null;
         }
      }

      private void setupFromConnection(HttpURLConnection var1, Connection.Response var2) throws IOException {
         this.method = Connection.Method.valueOf(var1.getRequestMethod());
         this.url = var1.getURL();
         this.statusCode = var1.getResponseCode();
         this.statusMessage = var1.getResponseMessage();
         this.contentType = var1.getContentType();
         this.processResponseHeaders(createHeaderMap(var1));
         if (var2 != null) {
            Iterator var3 = var2.cookies().entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               if (!this.hasCookie((String)var4.getKey())) {
                  this.cookie((String)var4.getKey(), (String)var4.getValue());
               }
            }
         }

      }

      private static void writePost(Connection.Request var0, OutputStream var1, String var2) throws IOException {
         Collection var5 = var0.data();
         BufferedWriter var4 = new BufferedWriter(new OutputStreamWriter(var1, var0.postDataCharset()));
         if (var2 != null) {
            for(Iterator var6 = var5.iterator(); var6.hasNext(); var4.write("\r\n")) {
               Connection.KeyVal var9 = (Connection.KeyVal)var6.next();
               var4.write("--");
               var4.write(var2);
               var4.write("\r\n");
               var4.write("Content-Disposition: form-data; name=\"");
               var4.write(HttpConnection.encodeMimeName(var9.key()));
               var4.write("\"");
               if (var9.hasInputStream()) {
                  var4.write("; filename=\"");
                  var4.write(HttpConnection.encodeMimeName(var9.value()));
                  var4.write("\"\r\nContent-Type: application/octet-stream\r\n\r\n");
                  var4.flush();
                  DataUtil.crossStreams(var9.inputStream(), var1);
                  var1.flush();
               } else {
                  var4.write("\r\n\r\n");
                  var4.write(var9.value());
               }
            }

            var4.write("--");
            var4.write(var2);
            var4.write("--");
         } else if (var0.requestBody() != null) {
            var4.write(var0.requestBody());
         } else {
            boolean var3 = true;
            Iterator var7 = var5.iterator();

            while(var7.hasNext()) {
               Connection.KeyVal var8 = (Connection.KeyVal)var7.next();
               if (!var3) {
                  var4.append('&');
               } else {
                  var3 = false;
               }

               var4.write(URLEncoder.encode(var8.key(), var0.postDataCharset()));
               var4.write(61);
               var4.write(URLEncoder.encode(var8.value(), var0.postDataCharset()));
            }
         }

         var4.close();
      }

      public String body() {
         Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
         String var1;
         if (this.charset == null) {
            var1 = Charset.forName("UTF-8").decode(this.byteData).toString();
         } else {
            var1 = Charset.forName(this.charset).decode(this.byteData).toString();
         }

         this.byteData.rewind();
         return var1;
      }

      public byte[] bodyAsBytes() {
         Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
         return this.byteData.array();
      }

      public String charset() {
         return this.charset;
      }

      public String contentType() {
         return this.contentType;
      }

      public Document parse() throws IOException {
         Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before parsing response");
         Document var1 = DataUtil.parseByteData(this.byteData, this.charset, this.url.toExternalForm(), this.req.parser());
         this.byteData.rewind();
         this.charset = var1.outputSettings().charset().name();
         return var1;
      }

      void processResponseHeaders(Map var1) {
         Iterator var7 = var1.entrySet().iterator();

         while(true) {
            while(true) {
               String var3;
               Entry var4;
               do {
                  if (!var7.hasNext()) {
                     return;
                  }

                  var4 = (Entry)var7.next();
                  var3 = (String)var4.getKey();
               } while(var3 == null);

               List var9 = (List)var4.getValue();
               if (var3.equalsIgnoreCase("Set-Cookie")) {
                  Iterator var8 = var9.iterator();

                  while(var8.hasNext()) {
                     String var10 = (String)var8.next();
                     if (var10 != null) {
                        TokenQueue var11 = new TokenQueue(var10);
                        var10 = var11.chompTo("=").trim();
                        String var12 = var11.consumeTo(";").trim();
                        if (var10.length() > 0) {
                           this.cookie(var10, var12);
                        }
                     }
                  }
               } else if (var9.size() == 1) {
                  this.header(var3, (String)var9.get(0));
               } else if (var9.size() > 1) {
                  StringBuilder var5 = new StringBuilder();

                  for(int var2 = 0; var2 < var9.size(); ++var2) {
                     String var6 = (String)var9.get(var2);
                     if (var2 != 0) {
                        var5.append(", ");
                     }

                     var5.append(var6);
                  }

                  this.header(var3, var5.toString());
               }
            }
         }
      }

      public int statusCode() {
         return this.statusCode;
      }

      public String statusMessage() {
         return this.statusMessage;
      }
   }
}
