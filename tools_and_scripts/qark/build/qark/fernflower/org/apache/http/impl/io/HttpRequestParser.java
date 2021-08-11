package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequestFactory;
import org.apache.http.ParseException;
import org.apache.http.RequestLine;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public class HttpRequestParser extends AbstractMessageParser {
   private final CharArrayBuffer lineBuf;
   private final HttpRequestFactory requestFactory;

   public HttpRequestParser(SessionInputBuffer var1, LineParser var2, HttpRequestFactory var3, HttpParams var4) {
      super(var1, var2, var4);
      this.requestFactory = (HttpRequestFactory)Args.notNull(var3, "Request factory");
      this.lineBuf = new CharArrayBuffer(128);
   }

   protected HttpMessage parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException {
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
