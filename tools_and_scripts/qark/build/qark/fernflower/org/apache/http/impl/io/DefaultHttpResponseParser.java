package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class DefaultHttpResponseParser extends AbstractMessageParser {
   private final CharArrayBuffer lineBuf;
   private final HttpResponseFactory responseFactory;

   public DefaultHttpResponseParser(SessionInputBuffer var1) {
      this(var1, (LineParser)null, (HttpResponseFactory)null, (MessageConstraints)MessageConstraints.DEFAULT);
   }

   public DefaultHttpResponseParser(SessionInputBuffer var1, MessageConstraints var2) {
      this(var1, (LineParser)null, (HttpResponseFactory)null, (MessageConstraints)var2);
   }

   public DefaultHttpResponseParser(SessionInputBuffer var1, LineParser var2, HttpResponseFactory var3, MessageConstraints var4) {
      super(var1, var2, var4);
      if (var3 == null) {
         var3 = DefaultHttpResponseFactory.INSTANCE;
      }

      this.responseFactory = (HttpResponseFactory)var3;
      this.lineBuf = new CharArrayBuffer(128);
   }

   @Deprecated
   public DefaultHttpResponseParser(SessionInputBuffer var1, LineParser var2, HttpResponseFactory var3, HttpParams var4) {
      super(var1, var2, var4);
      this.responseFactory = (HttpResponseFactory)Args.notNull(var3, "Response factory");
      this.lineBuf = new CharArrayBuffer(128);
   }

   protected HttpResponse parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException {
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
