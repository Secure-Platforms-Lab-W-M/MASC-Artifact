package org.jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public interface Connection {
   Connection cookie(String var1, String var2);

   Connection cookies(Map var1);

   Connection.KeyVal data(String var1);

   Connection data(String var1, String var2);

   Connection data(String var1, String var2, InputStream var3);

   Connection data(Collection var1);

   Connection data(Map var1);

   Connection data(String... var1);

   Connection.Response execute() throws IOException;

   Connection followRedirects(boolean var1);

   Document get() throws IOException;

   Connection header(String var1, String var2);

   Connection ignoreContentType(boolean var1);

   Connection ignoreHttpErrors(boolean var1);

   Connection maxBodySize(int var1);

   Connection method(Connection.Method var1);

   Connection parser(Parser var1);

   Document post() throws IOException;

   Connection postDataCharset(String var1);

   Connection proxy(String var1, int var2);

   Connection proxy(Proxy var1);

   Connection referrer(String var1);

   Connection.Request request();

   Connection request(Connection.Request var1);

   Connection requestBody(String var1);

   Connection.Response response();

   Connection response(Connection.Response var1);

   Connection timeout(int var1);

   Connection url(String var1);

   Connection url(URL var1);

   Connection userAgent(String var1);

   Connection validateTLSCertificates(boolean var1);

   public interface Base {
      String cookie(String var1);

      Connection.Base cookie(String var1, String var2);

      Map cookies();

      boolean hasCookie(String var1);

      boolean hasHeader(String var1);

      boolean hasHeaderWithValue(String var1, String var2);

      String header(String var1);

      Connection.Base header(String var1, String var2);

      Map headers();

      Connection.Base method(Connection.Method var1);

      Connection.Method method();

      Connection.Base removeCookie(String var1);

      Connection.Base removeHeader(String var1);

      URL url();

      Connection.Base url(URL var1);
   }

   public interface KeyVal {
      boolean hasInputStream();

      InputStream inputStream();

      Connection.KeyVal inputStream(InputStream var1);

      String key();

      Connection.KeyVal key(String var1);

      String value();

      Connection.KeyVal value(String var1);
   }

   public static enum Method {
      DELETE(false),
      GET(false),
      HEAD(false),
      OPTIONS(false),
      PATCH(true),
      POST(true),
      PUT(true),
      TRACE(false);

      private final boolean hasBody;

      private Method(boolean var3) {
         this.hasBody = var3;
      }

      public final boolean hasBody() {
         return this.hasBody;
      }
   }

   public interface Request extends Connection.Base {
      Collection data();

      Connection.Request data(Connection.KeyVal var1);

      Connection.Request followRedirects(boolean var1);

      boolean followRedirects();

      Connection.Request ignoreContentType(boolean var1);

      boolean ignoreContentType();

      Connection.Request ignoreHttpErrors(boolean var1);

      boolean ignoreHttpErrors();

      int maxBodySize();

      Connection.Request maxBodySize(int var1);

      Connection.Request parser(Parser var1);

      Parser parser();

      String postDataCharset();

      Connection.Request postDataCharset(String var1);

      Proxy proxy();

      Connection.Request proxy(String var1, int var2);

      Connection.Request proxy(Proxy var1);

      String requestBody();

      Connection.Request requestBody(String var1);

      int timeout();

      Connection.Request timeout(int var1);

      void validateTLSCertificates(boolean var1);

      boolean validateTLSCertificates();
   }

   public interface Response extends Connection.Base {
      String body();

      byte[] bodyAsBytes();

      String charset();

      String contentType();

      Document parse() throws IOException;

      int statusCode();

      String statusMessage();
   }
}
