package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpParams;

@Deprecated
public class HttpResponseWriter extends AbstractMessageWriter {
   public HttpResponseWriter(SessionOutputBuffer var1, LineFormatter var2, HttpParams var3) {
      super(var1, var2, var3);
   }

   protected void writeHeadLine(HttpResponse var1) throws IOException {
      this.lineFormatter.formatStatusLine(this.lineBuf, var1.getStatusLine());
      this.sessionBuffer.writeLine(this.lineBuf);
   }
}
