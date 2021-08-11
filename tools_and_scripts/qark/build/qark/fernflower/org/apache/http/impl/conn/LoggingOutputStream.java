package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.OutputStream;

class LoggingOutputStream extends OutputStream {
   private final OutputStream out;
   private final Wire wire;

   public LoggingOutputStream(OutputStream var1, Wire var2) {
      this.out = var1;
      this.wire = var2;
   }

   public void close() throws IOException {
      try {
         this.out.close();
      } catch (IOException var4) {
         Wire var2 = this.wire;
         StringBuilder var3 = new StringBuilder();
         var3.append("[close] I/O error: ");
         var3.append(var4.getMessage());
         var2.output(var3.toString());
         throw var4;
      }
   }

   public void flush() throws IOException {
      try {
         this.out.flush();
      } catch (IOException var4) {
         Wire var2 = this.wire;
         StringBuilder var3 = new StringBuilder();
         var3.append("[flush] I/O error: ");
         var3.append(var4.getMessage());
         var2.output(var3.toString());
         throw var4;
      }
   }

   public void write(int var1) throws IOException {
      try {
         this.wire.output(var1);
      } catch (IOException var5) {
         Wire var3 = this.wire;
         StringBuilder var4 = new StringBuilder();
         var4.append("[write] I/O error: ");
         var4.append(var5.getMessage());
         var3.output(var4.toString());
         throw var5;
      }
   }

   public void write(byte[] var1) throws IOException {
      try {
         this.wire.output(var1);
         this.out.write(var1);
      } catch (IOException var4) {
         Wire var2 = this.wire;
         StringBuilder var3 = new StringBuilder();
         var3.append("[write] I/O error: ");
         var3.append(var4.getMessage());
         var2.output(var3.toString());
         throw var4;
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      try {
         this.wire.output(var1, var2, var3);
         this.out.write(var1, var2, var3);
      } catch (IOException var6) {
         Wire var4 = this.wire;
         StringBuilder var5 = new StringBuilder();
         var5.append("[write] I/O error: ");
         var5.append(var6.getMessage());
         var4.output(var5.toString());
         throw var6;
      }
   }
}
