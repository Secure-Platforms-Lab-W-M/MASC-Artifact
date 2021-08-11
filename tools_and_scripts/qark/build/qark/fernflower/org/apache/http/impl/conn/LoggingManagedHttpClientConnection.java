package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.logging.Log;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

class LoggingManagedHttpClientConnection extends DefaultManagedHttpClientConnection {
   private final Log headerLog;
   private final Log log;
   private final Wire wire;

   public LoggingManagedHttpClientConnection(String var1, Log var2, Log var3, Log var4, int var5, int var6, CharsetDecoder var7, CharsetEncoder var8, MessageConstraints var9, ContentLengthStrategy var10, ContentLengthStrategy var11, HttpMessageWriterFactory var12, HttpMessageParserFactory var13) {
      super(var1, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      this.log = var2;
      this.headerLog = var3;
      this.wire = new Wire(var4, var1);
   }

   public void close() throws IOException {
      if (super.isOpen()) {
         if (this.log.isDebugEnabled()) {
            Log var1 = this.log;
            StringBuilder var2 = new StringBuilder();
            var2.append(this.getId());
            var2.append(": Close connection");
            var1.debug(var2.toString());
         }

         super.close();
      }

   }

   protected InputStream getSocketInputStream(Socket var1) throws IOException {
      InputStream var2 = super.getSocketInputStream(var1);
      Object var3 = var2;
      if (this.wire.enabled()) {
         var3 = new LoggingInputStream(var2, this.wire);
      }

      return (InputStream)var3;
   }

   protected OutputStream getSocketOutputStream(Socket var1) throws IOException {
      OutputStream var2 = super.getSocketOutputStream(var1);
      Object var3 = var2;
      if (this.wire.enabled()) {
         var3 = new LoggingOutputStream(var2, this.wire);
      }

      return (OutputStream)var3;
   }

   protected void onRequestSubmitted(HttpRequest var1) {
      if (var1 != null && this.headerLog.isDebugEnabled()) {
         Log var4 = this.headerLog;
         StringBuilder var5 = new StringBuilder();
         var5.append(this.getId());
         var5.append(" >> ");
         var5.append(var1.getRequestLine().toString());
         var4.debug(var5.toString());
         Header[] var7 = var1.getAllHeaders();
         int var3 = var7.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            Header var8 = var7[var2];
            Log var9 = this.headerLog;
            StringBuilder var6 = new StringBuilder();
            var6.append(this.getId());
            var6.append(" >> ");
            var6.append(var8.toString());
            var9.debug(var6.toString());
         }
      }

   }

   protected void onResponseReceived(HttpResponse var1) {
      if (var1 != null && this.headerLog.isDebugEnabled()) {
         Log var4 = this.headerLog;
         StringBuilder var5 = new StringBuilder();
         var5.append(this.getId());
         var5.append(" << ");
         var5.append(var1.getStatusLine().toString());
         var4.debug(var5.toString());
         Header[] var7 = var1.getAllHeaders();
         int var3 = var7.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            Header var8 = var7[var2];
            Log var9 = this.headerLog;
            StringBuilder var6 = new StringBuilder();
            var6.append(this.getId());
            var6.append(" << ");
            var6.append(var8.toString());
            var9.debug(var6.toString());
         }
      }

   }

   public void setSocketTimeout(int var1) {
      if (this.log.isDebugEnabled()) {
         Log var2 = this.log;
         StringBuilder var3 = new StringBuilder();
         var3.append(this.getId());
         var3.append(": set socket timeout to ");
         var3.append(var1);
         var2.debug(var3.toString());
      }

      super.setSocketTimeout(var1);
   }

   public void shutdown() throws IOException {
      if (this.log.isDebugEnabled()) {
         Log var1 = this.log;
         StringBuilder var2 = new StringBuilder();
         var2.append(this.getId());
         var2.append(": Shutdown connection");
         var1.debug(var2.toString());
      }

      super.shutdown();
   }
}
