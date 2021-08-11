package org.apache.http.impl.io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {
   public SocketOutputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Socket");
      int var4 = var2;
      var2 = var2;
      if (var4 < 0) {
         var2 = var1.getSendBufferSize();
      }

      var4 = var2;
      if (var2 < 1024) {
         var4 = 1024;
      }

      this.init(var1.getOutputStream(), var4, var3);
   }
}
