package net.sf.fmj.utility;

import java.io.IOException;
import java.io.OutputStream;

public class ByteBufferOutputStream extends OutputStream {
   private final int beginIndex;
   private final byte[] buf;
   private final int endIndex;
   private int index;

   public ByteBufferOutputStream(byte[] var1) {
      this(var1, 0, var1.length);
   }

   public ByteBufferOutputStream(byte[] var1, int var2, int var3) {
      if (var1 != null) {
         if (var2 >= 0) {
            if (var3 <= var1.length) {
               this.buf = var1;
               this.beginIndex = var2;
               this.index = var2;
               this.endIndex = var2 + var3;
            } else {
               throw new IndexOutOfBoundsException("len");
            }
         } else {
            throw new IndexOutOfBoundsException("off");
         }
      } else {
         throw new NullPointerException("buf");
      }
   }

   public int size() {
      return this.index - this.beginIndex;
   }

   public void write(int var1) throws IOException {
      int var2 = this.index;
      if (var2 < this.endIndex) {
         byte[] var4 = this.buf;
         this.index = var2 + 1;
         var4[var2] = (byte)var1;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("This ");
         var3.append(ByteBufferOutputStream.class.getName());
         var3.append(" is fully written.");
         throw new IOException(var3.toString());
      }
   }
}
