package org.apache.http.impl.io;

import org.apache.http.io.HttpTransportMetrics;

public class HttpTransportMetricsImpl implements HttpTransportMetrics {
   private long bytesTransferred = 0L;

   public long getBytesTransferred() {
      return this.bytesTransferred;
   }

   public void incrementBytesTransferred(long var1) {
      this.bytesTransferred += var1;
   }

   public void reset() {
      this.bytesTransferred = 0L;
   }

   public void setBytesTransferred(long var1) {
      this.bytesTransferred = var1;
   }
}
