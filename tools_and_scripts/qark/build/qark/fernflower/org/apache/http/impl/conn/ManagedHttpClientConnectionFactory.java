package org.apache.http.impl.conn;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

public class ManagedHttpClientConnectionFactory implements HttpConnectionFactory {
   private static final AtomicLong COUNTER = new AtomicLong();
   public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
   private final Log headerLog;
   private final ContentLengthStrategy incomingContentStrategy;
   private final Log log;
   private final ContentLengthStrategy outgoingContentStrategy;
   private final HttpMessageWriterFactory requestWriterFactory;
   private final HttpMessageParserFactory responseParserFactory;
   private final Log wireLog;

   public ManagedHttpClientConnectionFactory() {
      this((HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public ManagedHttpClientConnectionFactory(HttpMessageParserFactory var1) {
      this((HttpMessageWriterFactory)null, var1);
   }

   public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory var1, HttpMessageParserFactory var2) {
      this(var1, var2, (ContentLengthStrategy)null, (ContentLengthStrategy)null);
   }

   public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory var1, HttpMessageParserFactory var2, ContentLengthStrategy var3, ContentLengthStrategy var4) {
      this.log = LogFactory.getLog(DefaultManagedHttpClientConnection.class);
      this.headerLog = LogFactory.getLog("org.apache.http.headers");
      this.wireLog = LogFactory.getLog("org.apache.http.wire");
      if (var1 == null) {
         var1 = DefaultHttpRequestWriterFactory.INSTANCE;
      }

      this.requestWriterFactory = (HttpMessageWriterFactory)var1;
      if (var2 == null) {
         var2 = DefaultHttpResponseParserFactory.INSTANCE;
      }

      this.responseParserFactory = (HttpMessageParserFactory)var2;
      if (var3 == null) {
         var3 = LaxContentLengthStrategy.INSTANCE;
      }

      this.incomingContentStrategy = (ContentLengthStrategy)var3;
      if (var4 == null) {
         var4 = StrictContentLengthStrategy.INSTANCE;
      }

      this.outgoingContentStrategy = (ContentLengthStrategy)var4;
   }

   public ManagedHttpClientConnection create(HttpRoute var1, ConnectionConfig var2) {
      ConnectionConfig var7;
      if (var2 != null) {
         var7 = var2;
      } else {
         var7 = ConnectionConfig.DEFAULT;
      }

      CharsetDecoder var4 = null;
      CharsetEncoder var5 = null;
      Charset var6 = var7.getCharset();
      CodingErrorAction var8;
      if (var7.getMalformedInputAction() != null) {
         var8 = var7.getMalformedInputAction();
      } else {
         var8 = CodingErrorAction.REPORT;
      }

      CodingErrorAction var3;
      if (var7.getUnmappableInputAction() != null) {
         var3 = var7.getUnmappableInputAction();
      } else {
         var3 = CodingErrorAction.REPORT;
      }

      if (var6 != null) {
         var4 = var6.newDecoder();
         var4.onMalformedInput(var8);
         var4.onUnmappableCharacter(var3);
         var5 = var6.newEncoder();
         var5.onMalformedInput(var8);
         var5.onUnmappableCharacter(var3);
      }

      StringBuilder var9 = new StringBuilder();
      var9.append("http-outgoing-");
      var9.append(Long.toString(COUNTER.getAndIncrement()));
      return new LoggingManagedHttpClientConnection(var9.toString(), this.log, this.headerLog, this.wireLog, var7.getBufferSize(), var7.getFragmentSizeHint(), var4, var5, var7.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
   }
}
