package org.apache.http.impl.io;

import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;

public class DefaultHttpResponseWriterFactory implements HttpMessageWriterFactory {
   public static final DefaultHttpResponseWriterFactory INSTANCE = new DefaultHttpResponseWriterFactory();
   private final LineFormatter lineFormatter;

   public DefaultHttpResponseWriterFactory() {
      this((LineFormatter)null);
   }

   public DefaultHttpResponseWriterFactory(LineFormatter var1) {
      if (var1 == null) {
         var1 = BasicLineFormatter.INSTANCE;
      }

      this.lineFormatter = (LineFormatter)var1;
   }

   public HttpMessageWriter create(SessionOutputBuffer var1) {
      return new DefaultHttpResponseWriter(var1, this.lineFormatter);
   }
}
