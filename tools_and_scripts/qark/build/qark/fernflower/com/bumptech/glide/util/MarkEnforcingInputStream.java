package com.bumptech.glide.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarkEnforcingInputStream extends FilterInputStream {
   private static final int END_OF_STREAM = -1;
   private static final int UNSET = Integer.MIN_VALUE;
   private int availableBytes = Integer.MIN_VALUE;

   public MarkEnforcingInputStream(InputStream var1) {
      super(var1);
   }

   private long getBytesToRead(long var1) {
      int var3 = this.availableBytes;
      if (var3 == 0) {
         return -1L;
      } else {
         return var3 != Integer.MIN_VALUE && var1 > (long)var3 ? (long)var3 : var1;
      }
   }

   private void updateAvailableBytesAfterRead(long var1) {
      int var3 = this.availableBytes;
      if (var3 != Integer.MIN_VALUE && var1 != -1L) {
         this.availableBytes = (int)((long)var3 - var1);
      }

   }

   public int available() throws IOException {
      int var1 = this.availableBytes;
      return var1 == Integer.MIN_VALUE ? super.available() : Math.min(var1, super.available());
   }

   public void mark(int var1) {
      synchronized(this){}

      try {
         super.mark(var1);
         this.availableBytes = var1;
      } finally {
         ;
      }

   }

   public int read() throws IOException {
      if (this.getBytesToRead(1L) == -1L) {
         return -1;
      } else {
         int var1 = super.read();
         this.updateAvailableBytesAfterRead(1L);
         return var1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      var3 = (int)this.getBytesToRead((long)var3);
      if (var3 == -1) {
         return -1;
      } else {
         var2 = super.read(var1, var2, var3);
         this.updateAvailableBytesAfterRead((long)var2);
         return var2;
      }
   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         super.reset();
         this.availableBytes = Integer.MIN_VALUE;
      } finally {
         ;
      }

   }

   public long skip(long var1) throws IOException {
      var1 = this.getBytesToRead(var1);
      if (var1 == -1L) {
         return 0L;
      } else {
         var1 = super.skip(var1);
         this.updateAvailableBytesAfterRead(var1);
         return var1;
      }
   }
}
