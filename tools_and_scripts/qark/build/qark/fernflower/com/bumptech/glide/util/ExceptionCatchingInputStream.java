package com.bumptech.glide.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class ExceptionCatchingInputStream extends InputStream {
   private static final Queue QUEUE = Util.createQueue(0);
   private IOException exception;
   private InputStream wrapped;

   ExceptionCatchingInputStream() {
   }

   static void clearQueue() {
      while(!QUEUE.isEmpty()) {
         QUEUE.remove();
      }

   }

   public static ExceptionCatchingInputStream obtain(InputStream param0) {
      // $FF: Couldn't be decompiled
   }

   public int available() throws IOException {
      return this.wrapped.available();
   }

   public void close() throws IOException {
      this.wrapped.close();
   }

   public IOException getException() {
      return this.exception;
   }

   public void mark(int var1) {
      this.wrapped.mark(var1);
   }

   public boolean markSupported() {
      return this.wrapped.markSupported();
   }

   public int read() {
      try {
         int var1 = this.wrapped.read();
         return var1;
      } catch (IOException var3) {
         this.exception = var3;
         return -1;
      }
   }

   public int read(byte[] var1) {
      try {
         int var2 = this.wrapped.read(var1);
         return var2;
      } catch (IOException var3) {
         this.exception = var3;
         return -1;
      }
   }

   public int read(byte[] var1, int var2, int var3) {
      try {
         var2 = this.wrapped.read(var1, var2, var3);
         return var2;
      } catch (IOException var4) {
         this.exception = var4;
         return -1;
      }
   }

   public void release() {
      // $FF: Couldn't be decompiled
   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         this.wrapped.reset();
      } finally {
         ;
      }

   }

   void setInputStream(InputStream var1) {
      this.wrapped = var1;
   }

   public long skip(long var1) {
      try {
         var1 = this.wrapped.skip(var1);
         return var1;
      } catch (IOException var4) {
         this.exception = var4;
         return 0L;
      }
   }
}
