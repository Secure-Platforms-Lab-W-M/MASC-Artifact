package net.sf.fmj.media;

import java.io.IOException;
import java.io.InputStream;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.Seekable;

public class PullSourceStreamInputStream extends InputStream {
   private long markPosition = -1L;
   private final PullSourceStream pss;
   private final Seekable seekable;

   public PullSourceStreamInputStream(PullSourceStream var1) {
      this.pss = var1;
      if (var1 instanceof Seekable) {
         this.seekable = (Seekable)var1;
      } else {
         this.seekable = null;
      }
   }

   public void mark(int var1) {
      synchronized(this){}

      try {
         if (!this.markSupported()) {
            super.mark(var1);
         }

         this.markPosition = this.seekable.tell();
      } finally {
         ;
      }

   }

   public boolean markSupported() {
      return this.seekable != null;
   }

   public int read() throws IOException {
      byte[] var1 = new byte[1];
      return this.pss.read(var1, 0, 1) <= 0 ? -1 : var1[0] & 255;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.pss.read(var1, var2, var3);
   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         if (!this.markSupported()) {
            super.reset();
         }

         if (this.markPosition < 0L) {
            throw new IOException("mark must be called before reset");
         }

         this.seekable.seek(this.markPosition);
      } finally {
         ;
      }

   }

   public long skip(long var1) throws IOException {
      Seekable var5 = this.seekable;
      if (var5 == null) {
         return super.skip(var1);
      } else if (var1 <= 0L) {
         return 0L;
      } else {
         long var3 = var5.tell();
         return this.seekable.seek(var3 + var1) - var3;
      }
   }
}
