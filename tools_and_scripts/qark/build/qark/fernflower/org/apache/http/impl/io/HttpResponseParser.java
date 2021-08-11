package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponseFactory;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public class HttpResponseParser extends AbstractMessageParser {
   private final CharArrayBuffer lineBuf;
   private final HttpResponseFactory responseFactory;

   public HttpResponseParser(SessionInputBuffer var1, LineParser var2, HttpResponseFactory var3, HttpParams var4) {
      super(var1, var2, var4);
      this.responseFactory = (HttpResponseFactory)Args.notNull(var3, "Response factory");
      this.lineBuf = new CharArrayBuffer(128);
   }

   protected HttpMessage parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException {
      this.lineBuf.clear();
      if (var1.readLine(this.lineBuf) != -1) {
         ParserCursor var2 = new ParserCursor(0, this.lineBuf.length());
         StatusLine var3 = this.lineParser.parseStatusLine(this.lineBuf, var2);
         return this.responseFactory.newHttpResponse(var3, (HttpContext)null);
      } else {
         throw new NoHttpResponseException("The target server failed to respond");
      }
   }
}
