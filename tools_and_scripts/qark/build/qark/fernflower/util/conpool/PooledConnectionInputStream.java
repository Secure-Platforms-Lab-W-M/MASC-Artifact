package util.conpool;

import java.io.IOException;
import java.io.InputStream;

public class PooledConnectionInputStream extends InputStream {
   // $FF: renamed from: in java.io.InputStream
   private InputStream field_9 = null;
   private long traffic = 0L;
   private boolean valid = true;

   public PooledConnectionInputStream(InputStream var1) {
      this.field_9 = var1;
   }

   public void close() throws IOException {
   }

   public long getTraffic() {
      return this.traffic;
   }

   public void invalidate() {
      this.valid = false;
   }

   public int read() throws IOException {
      int var1 = this.field_9.read();
      if (!this.valid) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid:");
         var2.append(this);
         throw new IllegalStateException(var2.toString());
      } else {
         if (var1 != -1) {
            ++this.traffic;
         }

         return var1;
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      var2 = this.field_9.read(var1, var2, var3);
      if (!this.valid) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid:");
         var4.append(this);
         throw new IllegalStateException(var4.toString());
      } else {
         this.traffic += (long)var2;
         return var2;
      }
   }
}
