package org.apache.http.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.util.Args;

public class DefaultBHttpClientConnection extends BHttpConnectionBase implements HttpClientConnection {
   private final HttpMessageWriter requestWriter;
   private final HttpMessageParser responseParser;

   public DefaultBHttpClientConnection(int var1) {
      this(var1, var1, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public DefaultBHttpClientConnection(int var1, int var2, CharsetDecoder var3, CharsetEncoder var4, MessageConstraints var5, ContentLengthStrategy var6, ContentLengthStrategy var7, HttpMessageWriterFactory var8, HttpMessageParserFactory var9) {
      super(var1, var2, var3, var4, var5, var6, var7);
      if (var8 == null) {
         var8 = DefaultHttpRequestWriterFactory.INSTANCE;
      }

      this.requestWriter = ((HttpMessageWriterFactory)var8).create(this.getSessionOutputBuffer());
      if (var9 == null) {
         var9 = DefaultHttpResponseParserFactory.INSTANCE;
      }

      this.responseParser = ((HttpMessageParserFactory)var9).create(this.getSessionInputBuffer(), var5);
   }

   public DefaultBHttpClientConnection(int var1, CharsetDecoder var2, CharsetEncoder var3, MessageConstraints var4) {
      this(var1, var1, var2, var3, var4, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public void bind(Socket var1) throws IOException {
      super.bind(var1);
   }

   public void flush() throws IOException {
      this.ensureOpen();
      this.doFlush();
   }

   public boolean isResponseAvailable(int var1) throws IOException {
      this.ensureOpen();

      try {
         boolean var2 = this.awaitInput(var1);
         return var2;
      } catch (SocketTimeoutException var4) {
         return false;
      }
   }

   protected void onRequestSubmitted(HttpRequest var1) {
   }

   protected void onResponseReceived(HttpResponse var1) {
   }

   public void receiveResponseEntity(HttpResponse var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP response");
      this.ensureOpen();
      var1.setEntity(this.prepareInput(var1));
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      this.ensureOpen();
      HttpResponse var1 = (HttpResponse)this.responseParser.parse();
      this.onResponseReceived(var1);
      if (var1.getStatusLine().getStatusCode() >= 200) {
         this.incrementResponseCount();
      }

      return var1;
   }

   public void sendRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      this.ensureOpen();
      HttpEntity var2 = var1.getEntity();
      if (var2 != null) {
         OutputStream var3 = this.prepareOutput(var1);
         var2.writeTo(var3);
         var3.close();
      }
   }

   public void sendRequestHeader(HttpRequest var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      this.ensureOpen();
      this.requestWriter.write(var1);
      this.onRequestSubmitted(var1);
      this.incrementRequestCount();
   }
}
