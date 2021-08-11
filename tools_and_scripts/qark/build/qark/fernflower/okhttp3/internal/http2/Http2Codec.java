package okhttp3.internal.http2;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Http2Codec implements HttpCodec {
   private static final ByteString CONNECTION = ByteString.encodeUtf8("connection");
   private static final ByteString ENCODING = ByteString.encodeUtf8("encoding");
   private static final ByteString HOST = ByteString.encodeUtf8("host");
   private static final List HTTP_2_SKIPPED_REQUEST_HEADERS;
   private static final List HTTP_2_SKIPPED_RESPONSE_HEADERS;
   private static final ByteString KEEP_ALIVE = ByteString.encodeUtf8("keep-alive");
   private static final ByteString PROXY_CONNECTION = ByteString.encodeUtf8("proxy-connection");
   // $FF: renamed from: TE okio.ByteString
   private static final ByteString field_202 = ByteString.encodeUtf8("te");
   private static final ByteString TRANSFER_ENCODING = ByteString.encodeUtf8("transfer-encoding");
   private static final ByteString UPGRADE;
   private final OkHttpClient client;
   private final Http2Connection connection;
   private Http2Stream stream;
   final StreamAllocation streamAllocation;

   static {
      ByteString var0 = ByteString.encodeUtf8("upgrade");
      UPGRADE = var0;
      HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList((Object[])(CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, field_202, TRANSFER_ENCODING, ENCODING, var0, Header.TARGET_METHOD, Header.TARGET_PATH, Header.TARGET_SCHEME, Header.TARGET_AUTHORITY));
      HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList((Object[])(CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, field_202, TRANSFER_ENCODING, ENCODING, UPGRADE));
   }

   public Http2Codec(OkHttpClient var1, StreamAllocation var2, Http2Connection var3) {
      this.client = var1;
      this.streamAllocation = var2;
      this.connection = var3;
   }

   public static List http2HeadersList(Request var0) {
      Headers var3 = var0.headers();
      ArrayList var4 = new ArrayList(var3.size() + 4);
      var4.add(new Header(Header.TARGET_METHOD, var0.method()));
      var4.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(var0.url())));
      String var5 = var0.header("Host");
      if (var5 != null) {
         var4.add(new Header(Header.TARGET_AUTHORITY, var5));
      }

      var4.add(new Header(Header.TARGET_SCHEME, var0.url().scheme()));
      int var1 = 0;

      for(int var2 = var3.size(); var1 < var2; ++var1) {
         ByteString var6 = ByteString.encodeUtf8(var3.name(var1).toLowerCase(Locale.US));
         if (!HTTP_2_SKIPPED_REQUEST_HEADERS.contains(var6)) {
            var4.add(new Header(var6, var3.value(var1)));
         }
      }

      return var4;
   }

   public static Response.Builder readHttp2HeadersList(List var0) throws IOException {
      StatusLine var5 = null;
      Headers.Builder var4 = new Headers.Builder();
      int var1 = 0;

      Headers.Builder var6;
      for(int var2 = var0.size(); var1 < var2; var4 = var6) {
         Header var3 = (Header)var0.get(var1);
         StatusLine var9;
         if (var3 == null) {
            var9 = var5;
            var6 = var4;
            if (var5 != null) {
               var9 = var5;
               var6 = var4;
               if (var5.code == 100) {
                  var9 = null;
                  var6 = new Headers.Builder();
               }
            }
         } else {
            ByteString var7 = var3.name;
            String var8 = var3.value.utf8();
            if (var7.equals(Header.RESPONSE_STATUS)) {
               StringBuilder var10 = new StringBuilder();
               var10.append("HTTP/1.1 ");
               var10.append(var8);
               var9 = StatusLine.parse(var10.toString());
               var6 = var4;
            } else {
               var9 = var5;
               var6 = var4;
               if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(var7)) {
                  Internal.instance.addLenient(var4, var7.utf8(), var8);
                  var6 = var4;
                  var9 = var5;
               }
            }
         }

         ++var1;
         var5 = var9;
      }

      if (var5 != null) {
         return (new Response.Builder()).protocol(Protocol.HTTP_2).code(var5.code).message(var5.message).headers(var4.build());
      } else {
         throw new ProtocolException("Expected ':status' header not present");
      }
   }

   public void cancel() {
      Http2Stream var1 = this.stream;
      if (var1 != null) {
         var1.closeLater(ErrorCode.CANCEL);
      }

   }

   public Sink createRequestBody(Request var1, long var2) {
      return this.stream.getSink();
   }

   public void finishRequest() throws IOException {
      this.stream.getSink().close();
   }

   public void flushRequest() throws IOException {
      this.connection.flush();
   }

   public ResponseBody openResponseBody(Response var1) throws IOException {
      Http2Codec.StreamFinishingSource var2 = new Http2Codec.StreamFinishingSource(this.stream.getSource());
      return new RealResponseBody(var1.headers(), Okio.buffer((Source)var2));
   }

   public Response.Builder readResponseHeaders(boolean var1) throws IOException {
      Response.Builder var2 = readHttp2HeadersList(this.stream.takeResponseHeaders());
      return var1 && Internal.instance.code(var2) == 100 ? null : var2;
   }

   public void writeRequestHeaders(Request var1) throws IOException {
      if (this.stream == null) {
         boolean var2;
         if (var1.body() != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         List var3 = http2HeadersList(var1);
         Http2Stream var4 = this.connection.newStream(var3, var2);
         this.stream = var4;
         var4.readTimeout().timeout((long)this.client.readTimeoutMillis(), TimeUnit.MILLISECONDS);
         this.stream.writeTimeout().timeout((long)this.client.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
      }
   }

   class StreamFinishingSource extends ForwardingSource {
      StreamFinishingSource(Source var2) {
         super(var2);
      }

      public void close() throws IOException {
         Http2Codec.this.streamAllocation.streamFinished(false, Http2Codec.this);
         super.close();
      }
   }
}
