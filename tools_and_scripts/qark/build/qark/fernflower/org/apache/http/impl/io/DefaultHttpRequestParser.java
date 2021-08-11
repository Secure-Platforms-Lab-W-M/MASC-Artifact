package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.ParseException;
import org.apache.http.RequestLine;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class DefaultHttpRequestParser extends AbstractMessageParser {
   private final CharArrayBuffer lineBuf;
   private final HttpRequestFactory requestFactory;

   public DefaultHttpRequestParser(SessionInputBuffer var1) {
      this(var1, (LineParser)null, (HttpRequestFactory)null, (MessageConstraints)MessageConstraints.DEFAULT);
   }

   public DefaultHttpRequestParser(SessionInputBuffer var1, MessageConstraints var2) {
      this(var1, (LineParser)null, (HttpRequestFactory)null, (MessageConstraints)var2);
   }

   public DefaultHttpRequestParser(SessionInputBuffer var1, LineParser var2, HttpRequestFactory var3, MessageConstraints var4) {
      super(var1, var2, var4);
      if (var3 == null) {
         var3 = DefaultHttpRequestFactory.INSTANCE;
      }

      this.requestFactory = (HttpRequestFactory)var3;
      this.lineBuf = new CharArrayBuffer(128);
   }

   @Deprecated
   public DefaultHttpRequestParser(SessionInputBuffer var1, LineParser var2, HttpRequestFactory var3, HttpParams var4) {
      super(var1, var2, var4);
      this.requestFactory = (HttpRequestFactory)Args.notNull(var3, "Request factory");
      this.lineBuf = new CharArrayBuffer(128);
   }

   protected HttpRequest parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException {
      this.lineBuf.clear();
      if (var1.readLine(this.lineBuf) != -1) {
         ParserCursor var2 = new ParserCursor(0, this.lineBuf.length());
         RequestLine var3 = this.lineParser.parseRequestLine(this.lineBuf, var2);
         return this.requestFactory.newHttpRequest(var3);
      } else {
         throw new ConnectionClosedException("Client closed connection");
      }
   }
}
