package org.apache.http.impl.io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.io.EofSensor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public class SocketInputBuffer extends AbstractSessionInputBuffer implements EofSensor {
   private boolean eof;
   private final Socket socket;

   public SocketInputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Socket");
      this.socket = var1;
      this.eof = false;
      int var4 = var2;
      var2 = var2;
      if (var4 < 0) {
         var2 = var1.getReceiveBufferSize();
      }

      var4 = var2;
      if (var2 < 1024) {
         var4 = 1024;
      }

      this.init(var1.getInputStream(), var4, var3);
   }

   protected int fillBuffer() throws IOException {
      int var1 = super.fillBuffer();
      boolean var2;
      if (var1 == -1) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.eof = var2;
      return var1;
   }

   public boolean isDataAvailable(int var1) throws IOException {
      boolean var3 = this.hasBufferedData();
      if (!var3) {
         int var2 = this.socket.getSoTimeout();

         try {
            this.socket.setSoTimeout(var1);
            this.fillBuffer();
            var3 = this.hasBufferedData();
         } finally {
            this.socket.setSoTimeout(var2);
         }

         return var3;
      } else {
         return var3;
      }
   }

   public boolean isEof() {
      return this.eof;
   }
}
