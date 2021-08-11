package com.jcraft.jzlib;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InflaterInputStream extends FilterInputStream {
   protected static final int DEFAULT_BUFSIZE = 512;
   // $FF: renamed from: b byte[]
   private byte[] field_158;
   protected byte[] buf;
   private byte[] byte1;
   private boolean close_in;
   private boolean closed;
   private boolean eof;
   protected final Inflater inflater;
   protected boolean myinflater;

   public InflaterInputStream(InputStream var1) throws IOException {
      this(var1, false);
   }

   public InflaterInputStream(InputStream var1, Inflater var2) throws IOException {
      this(var1, var2, 512);
   }

   public InflaterInputStream(InputStream var1, Inflater var2, int var3) throws IOException {
      this(var1, var2, var3, true);
   }

   public InflaterInputStream(InputStream var1, Inflater var2, int var3, boolean var4) throws IOException {
      super(var1);
      this.closed = false;
      this.eof = false;
      this.close_in = true;
      this.myinflater = false;
      this.byte1 = new byte[1];
      this.field_158 = new byte[512];
      if (var1 != null && var2 != null) {
         if (var3 > 0) {
            this.inflater = var2;
            this.buf = new byte[var3];
            this.close_in = var4;
         } else {
            throw new IllegalArgumentException("buffer size must be greater than 0");
         }
      } else {
         throw null;
      }
   }

   public InflaterInputStream(InputStream var1, boolean var2) throws IOException {
      this(var1, new Inflater(var2));
      this.myinflater = true;
   }

   public int available() throws IOException {
      if (!this.closed) {
         return this.eof ? 0 : 1;
      } else {
         throw new IOException("Stream closed");
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         if (this.myinflater) {
            this.inflater.end();
         }

         if (this.close_in) {
            this.in.close();
         }

         this.closed = true;
      }

   }

   protected void fill() throws IOException {
      if (this.closed) {
         throw new IOException("Stream closed");
      } else {
         InputStream var3 = this.in;
         byte[] var4 = this.buf;
         int var2 = var3.read(var4, 0, var4.length);
         int var1 = var2;
         if (var2 == -1) {
            if (this.inflater.istate.wrap != 0 || this.inflater.finished()) {
               if (this.inflater.istate.was != -1L) {
                  throw new IOException("footer is not found");
               }

               throw new EOFException("Unexpected end of ZLIB input stream");
            }

            this.buf[0] = 0;
            var1 = 1;
         }

         this.inflater.setInput(this.buf, 0, var1, true);
      }
   }

   public byte[] getAvailIn() {
      if (this.inflater.avail_in <= 0) {
         return null;
      } else {
         byte[] var1 = new byte[this.inflater.avail_in];
         System.arraycopy(this.inflater.next_in, this.inflater.next_in_index, var1, 0, this.inflater.avail_in);
         return var1;
      }
   }

   public Inflater getInflater() {
      return this.inflater;
   }

   public long getTotalIn() {
      return this.inflater.getTotalIn();
   }

   public long getTotalOut() {
      return this.inflater.getTotalOut();
   }

   public void mark(int var1) {
      synchronized(this){}
   }

   public boolean markSupported() {
      return false;
   }

   public int read() throws IOException {
      if (!this.closed) {
         return this.read(this.byte1, 0, 1) == -1 ? -1 : this.byte1[0] & 255;
      } else {
         throw new IOException("Stream closed");
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (!this.closed) {
         if (var1 == null) {
            throw null;
         } else if (var2 >= 0 && var3 >= 0 && var3 <= var1.length - var2) {
            if (var3 == 0) {
               return 0;
            } else if (this.eof) {
               return -1;
            } else {
               byte var4 = 0;
               this.inflater.setOutput(var1, var2, var3);
               var3 = var2;
               var2 = var4;

               do {
                  if (this.eof) {
                     return var2;
                  }

                  if (this.inflater.avail_in == 0) {
                     this.fill();
                  }

                  int var5 = this.inflater.inflate(0);
                  var2 += this.inflater.next_out_index - var3;
                  var3 = this.inflater.next_out_index;
                  if (var5 == -3) {
                     throw new IOException(this.inflater.msg);
                  }

                  if (var5 == 1 || var5 == 2) {
                     this.eof = true;
                     if (var5 == 2) {
                        return -1;
                     }
                  }
               } while(this.inflater.avail_out != 0);

               return var2;
            }
         } else {
            throw new IndexOutOfBoundsException();
         }
      } else {
         throw new IOException("Stream closed");
      }
   }

   public void readHeader() throws IOException {
      byte[] var1 = "".getBytes();
      this.inflater.setInput(var1, 0, 0, false);
      this.inflater.setOutput(var1, 0, 0);
      this.inflater.inflate(0);
      if (this.inflater.istate.inParsingHeader()) {
         var1 = new byte[1];

         do {
            if (this.in.read(var1) <= 0) {
               throw new IOException("no input");
            }

            this.inflater.setInput(var1);
            if (this.inflater.inflate(0) != 0) {
               throw new IOException(this.inflater.msg);
            }
         } while(this.inflater.istate.inParsingHeader());

      }
   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         throw new IOException("mark/reset not supported");
      } finally {
         ;
      }
   }

   public long skip(long var1) throws IOException {
      if (var1 < 0L) {
         throw new IllegalArgumentException("negative skip length");
      } else if (this.closed) {
         throw new IOException("Stream closed");
      } else {
         int var6 = (int)Math.min(var1, 2147483647L);

         int var3;
         int var4;
         for(var3 = 0; var3 < var6; var3 += var4) {
            int var5 = var6 - var3;
            byte[] var7 = this.field_158;
            var4 = var5;
            if (var5 > var7.length) {
               var4 = var7.length;
            }

            var4 = this.read(this.field_158, 0, var4);
            if (var4 == -1) {
               this.eof = true;
               break;
            }
         }

         return (long)var3;
      }
   }
}
