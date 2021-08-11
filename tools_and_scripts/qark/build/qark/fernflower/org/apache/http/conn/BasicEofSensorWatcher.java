package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.util.Args;

@Deprecated
public class BasicEofSensorWatcher implements EofSensorWatcher {
   protected final boolean attemptReuse;
   protected final ManagedClientConnection managedConn;

   public BasicEofSensorWatcher(ManagedClientConnection var1, boolean var2) {
      Args.notNull(var1, "Connection");
      this.managedConn = var1;
      this.attemptReuse = var2;
   }

   public boolean eofDetected(InputStream var1) throws IOException {
      try {
         if (this.attemptReuse) {
            var1.close();
            this.managedConn.markReusable();
         }
      } finally {
         this.managedConn.releaseConnection();
      }

      return false;
   }

   public boolean streamAbort(InputStream var1) throws IOException {
      this.managedConn.abortConnection();
      return false;
   }

   public boolean streamClosed(InputStream var1) throws IOException {
      try {
         if (this.attemptReuse) {
            var1.close();
            this.managedConn.markReusable();
         }
      } finally {
         this.managedConn.releaseConnection();
      }

      return false;
   }
}
