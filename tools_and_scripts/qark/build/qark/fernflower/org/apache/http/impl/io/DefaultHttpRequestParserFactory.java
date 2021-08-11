package org.apache.http.impl.io;

import org.apache.http.HttpRequestFactory;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;

public class DefaultHttpRequestParserFactory implements HttpMessageParserFactory {
   public static final DefaultHttpRequestParserFactory INSTANCE = new DefaultHttpRequestParserFactory();
   private final LineParser lineParser;
   private final HttpRequestFactory requestFactory;

   public DefaultHttpRequestParserFactory() {
      this((LineParser)null, (HttpRequestFactory)null);
   }

   public DefaultHttpRequestParserFactory(LineParser var1, HttpRequestFactory var2) {
      if (var1 == null) {
         var1 = BasicLineParser.INSTANCE;
      }

      this.lineParser = (LineParser)var1;
      if (var2 == null) {
         var2 = DefaultHttpRequestFactory.INSTANCE;
      }

      this.requestFactory = (HttpRequestFactory)var2;
   }

   public HttpMessageParser create(SessionInputBuffer var1, MessageConstraints var2) {
      return new DefaultHttpRequestParser(var1, this.lineParser, this.requestFactory, var2);
   }
}
