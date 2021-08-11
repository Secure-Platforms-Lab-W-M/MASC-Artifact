package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public abstract class AbstractMessageWriter implements HttpMessageWriter {
   protected final CharArrayBuffer lineBuf;
   protected final LineFormatter lineFormatter;
   protected final SessionOutputBuffer sessionBuffer;

   public AbstractMessageWriter(SessionOutputBuffer var1, LineFormatter var2) {
      this.sessionBuffer = (SessionOutputBuffer)Args.notNull(var1, "Session input buffer");
      if (var2 == null) {
         var2 = BasicLineFormatter.INSTANCE;
      }

      this.lineFormatter = (LineFormatter)var2;
      this.lineBuf = new CharArrayBuffer(128);
   }

   @Deprecated
   public AbstractMessageWriter(SessionOutputBuffer var1, LineFormatter var2, HttpParams var3) {
      Args.notNull(var1, "Session input buffer");
      this.sessionBuffer = var1;
      this.lineBuf = new CharArrayBuffer(128);
      if (var2 == null) {
         var2 = BasicLineFormatter.INSTANCE;
      }

      this.lineFormatter = (LineFormatter)var2;
   }

   public void write(HttpMessage var1) throws IOException, HttpException {
      Args.notNull(var1, "HTTP message");
      this.writeHeadLine(var1);
      HeaderIterator var3 = var1.headerIterator();

      while(var3.hasNext()) {
         Header var2 = var3.nextHeader();
         this.sessionBuffer.writeLine(this.lineFormatter.formatHeader(this.lineBuf, var2));
      }

      this.lineBuf.clear();
      this.sessionBuffer.writeLine(this.lineBuf);
   }

   protected abstract void writeHeadLine(HttpMessage var1) throws IOException;
}
