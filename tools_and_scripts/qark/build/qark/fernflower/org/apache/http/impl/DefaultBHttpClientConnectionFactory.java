package org.apache.http.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

public class DefaultBHttpClientConnectionFactory implements HttpConnectionFactory {
   public static final DefaultBHttpClientConnectionFactory INSTANCE = new DefaultBHttpClientConnectionFactory();
   private final ConnectionConfig cconfig;
   private final ContentLengthStrategy incomingContentStrategy;
   private final ContentLengthStrategy outgoingContentStrategy;
   private final HttpMessageWriterFactory requestWriterFactory;
   private final HttpMessageParserFactory responseParserFactory;

   public DefaultBHttpClientConnectionFactory() {
      this((ConnectionConfig)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public DefaultBHttpClientConnectionFactory(ConnectionConfig var1) {
      this(var1, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public DefaultBHttpClientConnectionFactory(ConnectionConfig var1, ContentLengthStrategy var2, ContentLengthStrategy var3, HttpMessageWriterFactory var4, HttpMessageParserFactory var5) {
      if (var1 == null) {
         var1 = ConnectionConfig.DEFAULT;
      }

      this.cconfig = var1;
      this.incomingContentStrategy = var2;
      this.outgoingContentStrategy = var3;
      this.requestWriterFactory = var4;
      this.responseParserFactory = var5;
   }

   public DefaultBHttpClientConnectionFactory(ConnectionConfig var1, HttpMessageWriterFactory var2, HttpMessageParserFactory var3) {
      this(var1, (ContentLengthStrategy)null, (ContentLengthStrategy)null, var2, var3);
   }

   public DefaultBHttpClientConnection createConnection(Socket var1) throws IOException {
      DefaultBHttpClientConnection var2 = new DefaultBHttpClientConnection(this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
      var2.bind(var1);
      return var2;
   }
}
