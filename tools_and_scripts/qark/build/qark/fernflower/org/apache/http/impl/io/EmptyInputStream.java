package org.apache.http.impl.io;

import java.io.InputStream;

public final class EmptyInputStream extends InputStream {
   public static final EmptyInputStream INSTANCE = new EmptyInputStream();

   private EmptyInputStream() {
   }

   public int available() {
      return 0;
   }

   public void close() {
   }

   public void mark(int var1) {
   }

   public boolean markSupported() {
      return true;
   }

   public int read() {
      return -1;
   }

   public int read(byte[] var1) {
      return -1;
   }

   public int read(byte[] var1, int var2, int var3) {
      return -1;
   }

   public void reset() {
   }

   public long skip(long var1) {
      return 0L;
   }
}
