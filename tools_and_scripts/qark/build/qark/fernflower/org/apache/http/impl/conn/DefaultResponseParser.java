package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponseFactory;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.impl.io.AbstractMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public class DefaultResponseParser extends AbstractMessageParser {
   private final CharArrayBuffer lineBuf;
   private final Log log = LogFactory.getLog(this.getClass());
   private final int maxGarbageLines;
   private final HttpResponseFactory responseFactory;

   public DefaultResponseParser(SessionInputBuffer var1, LineParser var2, HttpResponseFactory var3, HttpParams var4) {
      super(var1, var2, var4);
      Args.notNull(var3, "Response factory");
      this.responseFactory = var3;
      this.lineBuf = new CharArrayBuffer(128);
      this.maxGarbageLines = this.getMaxGarbageLines(var4);
   }

   protected int getMaxGarbageLines(HttpParams var1) {
      return var1.getIntParameter("http.connection.max-status-line-garbage", Integer.MAX_VALUE);
   }

   protected HttpMessage parseHead(SessionInputBuffer var1) throws IOException, HttpException {
      int var2 = 0;

      while(true) {
         this.lineBuf.clear();
         int var3 = var1.readLine(this.lineBuf);
         if (var3 == -1 && var2 == 0) {
            throw new NoHttpResponseException("The target server failed to respond");
         }

         ParserCursor var4 = new ParserCursor(0, this.lineBuf.length());
         if (this.lineParser.hasProtocolVersion(this.lineBuf, var4)) {
            StatusLine var6 = this.lineParser.parseStatusLine(this.lineBuf, var4);
            return this.responseFactory.newHttpResponse(var6, (HttpContext)null);
         }

         if (var3 == -1 || var2 >= this.maxGarbageLines) {
            throw new ProtocolException("The server failed to respond with a valid HTTP response");
         }

         if (this.log.isDebugEnabled()) {
            Log var7 = this.log;
            StringBuilder var5 = new StringBuilder();
            var5.append("Garbage in response: ");
            var5.append(this.lineBuf.toString());
            var7.debug(var5.toString());
         }

         ++var2;
      }
   }
}
