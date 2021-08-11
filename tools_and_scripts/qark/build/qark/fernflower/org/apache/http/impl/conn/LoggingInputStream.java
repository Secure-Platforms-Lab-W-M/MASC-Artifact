package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;

class LoggingInputStream extends InputStream {
   // $FF: renamed from: in java.io.InputStream
   private final InputStream field_185;
   private final Wire wire;

   public LoggingInputStream(InputStream var1, Wire var2) {
      this.field_185 = var1;
      this.wire = var2;
   }

   public int available() throws IOException {
      try {
         int var1 = this.field_185.available();
         return var1;
      } catch (IOException var5) {
         Wire var3 = this.wire;
         StringBuilder var4 = new StringBuilder();
         var4.append("[available] I/O error : ");
         var4.append(var5.getMessage());
         var3.input(var4.toString());
         throw var5;
      }
   }

   public void close() throws IOException {
      try {
         this.field_185.close();
      } catch (IOException var4) {
         Wire var2 = this.wire;
         StringBuilder var3 = new StringBuilder();
         var3.append("[close] I/O error: ");
         var3.append(var4.getMessage());
         var2.input(var3.toString());
         throw var4;
      }
   }

   public void mark(int var1) {
      super.mark(var1);
   }

   public boolean markSupported() {
      return false;
   }

   public int read() throws IOException {
      IOException var10000;
      label25: {
         int var1;
         boolean var10001;
         try {
            var1 = this.field_185.read();
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label25;
         }

         if (var1 == -1) {
            try {
               this.wire.input("end of stream");
               return var1;
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
            }
         } else {
            try {
               this.wire.input(var1);
               return var1;
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
            }
         }
      }

      IOException var2 = var10000;
      Wire var3 = this.wire;
      StringBuilder var4 = new StringBuilder();
      var4.append("[read] I/O error: ");
      var4.append(var2.getMessage());
      var3.input(var4.toString());
      throw var2;
   }

   public int read(byte[] var1) throws IOException {
      IOException var10000;
      label28: {
         boolean var10001;
         int var2;
         try {
            var2 = this.field_185.read(var1);
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label28;
         }

         if (var2 == -1) {
            try {
               this.wire.input("end of stream");
               return var2;
            } catch (IOException var5) {
               var10000 = var5;
               var10001 = false;
            }
         } else {
            label24: {
               if (var2 > 0) {
                  try {
                     this.wire.input(var1, 0, var2);
                  } catch (IOException var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label24;
                  }
               }

               return var2;
            }
         }
      }

      IOException var8 = var10000;
      Wire var3 = this.wire;
      StringBuilder var4 = new StringBuilder();
      var4.append("[read] I/O error: ");
      var4.append(var8.getMessage());
      var3.input(var4.toString());
      throw var8;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      IOException var10000;
      label28: {
         boolean var10001;
         try {
            var3 = this.field_185.read(var1, var2, var3);
         } catch (IOException var8) {
            var10000 = var8;
            var10001 = false;
            break label28;
         }

         if (var3 == -1) {
            try {
               this.wire.input("end of stream");
               return var3;
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
            }
         } else {
            label24: {
               if (var3 > 0) {
                  try {
                     this.wire.input(var1, var2, var3);
                  } catch (IOException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label24;
                  }
               }

               return var3;
            }
         }
      }

      IOException var9 = var10000;
      Wire var4 = this.wire;
      StringBuilder var5 = new StringBuilder();
      var5.append("[read] I/O error: ");
      var5.append(var9.getMessage());
      var4.input(var5.toString());
      throw var9;
   }

   public void reset() throws IOException {
      super.reset();
   }

   public long skip(long var1) throws IOException {
      try {
         var1 = super.skip(var1);
         return var1;
      } catch (IOException var6) {
         Wire var4 = this.wire;
         StringBuilder var5 = new StringBuilder();
         var5.append("[skip] I/O error: ");
         var5.append(var6.getMessage());
         var4.input(var5.toString());
         throw var6;
      }
   }
}
