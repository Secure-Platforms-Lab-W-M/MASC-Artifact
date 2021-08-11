package org.apache.http.impl.io;

import org.apache.http.HttpResponseFactory;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;

public class DefaultHttpResponseParserFactory implements HttpMessageParserFactory {
   public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();
   private final LineParser lineParser;
   private final HttpResponseFactory responseFactory;

   public DefaultHttpResponseParserFactory() {
      this((LineParser)null, (HttpResponseFactory)null);
   }

   public DefaultHttpResponseParserFactory(LineParser var1, HttpResponseFactory var2) {
      if (var1 == null) {
         var1 = BasicLineParser.INSTANCE;
      }

      this.lineParser = (LineParser)var1;
      if (var2 == null) {
         var2 = DefaultHttpResponseFactory.INSTANCE;
      }

      this.responseFactory = (HttpResponseFactory)var2;
   }

   public HttpMessageParser create(SessionInputBuffer var1, MessageConstraints var2) {
      return new DefaultHttpResponseParser(var1, this.lineParser, this.responseFactory, var2);
   }
}
