package util.conpool;

import java.io.IOException;
import java.io.OutputStream;

public class PooledConnectionOutputStream extends OutputStream {
   private OutputStream out;
   private long traffic = 0L;
   private boolean valid = true;

   public PooledConnectionOutputStream(OutputStream var1) {
      this.out = var1;
   }

   public void close() throws IOException {
   }

   public void flush() throws IOException {
      this.out.flush();
      if (!this.valid) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Invalid:");
         var1.append(this);
         throw new IllegalStateException(var1.toString());
      }
   }

   public long getTraffic() {
      return this.traffic;
   }

   public void invalidate() {
      this.valid = false;
   }

   public void write(int var1) throws IOException {
      this.out.write(var1);
      ++this.traffic;
      if (!this.valid) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid:");
         var2.append(this);
         throw new IllegalStateException(var2.toString());
      }
   }

   public void write(byte[] var1) throws IOException {
      this.out.write(var1);
      this.traffic += (long)var1.length;
      if (!this.valid) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid:");
         var2.append(this);
         throw new IllegalStateException(var2.toString());
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
      this.traffic += (long)var3;
      if (!this.valid) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid:");
         var4.append(this);
         throw new IllegalStateException(var4.toString());
      }
   }
}
