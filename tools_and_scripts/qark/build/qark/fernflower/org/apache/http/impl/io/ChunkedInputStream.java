package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.TruncatedChunkException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class ChunkedInputStream extends InputStream {
   private static final int BUFFER_SIZE = 2048;
   private static final int CHUNK_CRLF = 3;
   private static final int CHUNK_DATA = 2;
   private static final int CHUNK_INVALID = Integer.MAX_VALUE;
   private static final int CHUNK_LEN = 1;
   private final CharArrayBuffer buffer;
   private long chunkSize;
   private boolean closed;
   private final MessageConstraints constraints;
   private boolean eof;
   private Header[] footers;
   // $FF: renamed from: in org.apache.http.io.SessionInputBuffer
   private final SessionInputBuffer field_205;
   private long pos;
   private int state;

   public ChunkedInputStream(SessionInputBuffer var1) {
      this(var1, (MessageConstraints)null);
   }

   public ChunkedInputStream(SessionInputBuffer var1, MessageConstraints var2) {
      this.eof = false;
      this.closed = false;
      this.footers = new Header[0];
      this.field_205 = (SessionInputBuffer)Args.notNull(var1, "Session input buffer");
      this.pos = 0L;
      this.buffer = new CharArrayBuffer(16);
      if (var2 == null) {
         var2 = MessageConstraints.DEFAULT;
      }

      this.constraints = var2;
      this.state = 1;
   }

   private long getChunkSize() throws IOException {
      int var1 = this.state;
      if (var1 != 1) {
         if (var1 != 3) {
            throw new IllegalStateException("Inconsistent codec state");
         }

         this.buffer.clear();
         if (this.field_205.readLine(this.buffer) == -1) {
            throw new MalformedChunkCodingException("CRLF expected at end of chunk");
         }

         if (!this.buffer.isEmpty()) {
            throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
         }

         this.state = 1;
      }

      this.buffer.clear();
      if (this.field_205.readLine(this.buffer) != -1) {
         int var2 = this.buffer.indexOf(59);
         var1 = var2;
         if (var2 < 0) {
            var1 = this.buffer.length();
         }

         String var5 = this.buffer.substringTrimmed(0, var1);

         try {
            long var3 = Long.parseLong(var5, 16);
            return var3;
         } catch (NumberFormatException var7) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Bad chunk header: ");
            var6.append(var5);
            throw new MalformedChunkCodingException(var6.toString());
         }
      } else {
         throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
      }
   }

   private void nextChunk() throws IOException {
      if (this.state != Integer.MAX_VALUE) {
         MalformedChunkCodingException var10000;
         label37: {
            long var1;
            boolean var10001;
            try {
               var1 = this.getChunkSize();
               this.chunkSize = var1;
            } catch (MalformedChunkCodingException var7) {
               var10000 = var7;
               var10001 = false;
               break label37;
            }

            if (var1 >= 0L) {
               label41: {
                  try {
                     this.state = 2;
                     this.pos = 0L;
                  } catch (MalformedChunkCodingException var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label41;
                  }

                  if (var1 != 0L) {
                     return;
                  }

                  try {
                     this.eof = true;
                     this.parseTrailerHeaders();
                     return;
                  } catch (MalformedChunkCodingException var4) {
                     var10000 = var4;
                     var10001 = false;
                  }
               }
            } else {
               try {
                  throw new MalformedChunkCodingException("Negative chunk size");
               } catch (MalformedChunkCodingException var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }
         }

         MalformedChunkCodingException var3 = var10000;
         this.state = Integer.MAX_VALUE;
         throw var3;
      } else {
         throw new MalformedChunkCodingException("Corrupt data stream");
      }
   }

   private void parseTrailerHeaders() throws IOException {
      try {
         this.footers = AbstractMessageParser.parseHeaders(this.field_205, this.constraints.getMaxHeaderCount(), this.constraints.getMaxLineLength(), (LineParser)null);
      } catch (HttpException var3) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Invalid footer: ");
         var2.append(var3.getMessage());
         MalformedChunkCodingException var4 = new MalformedChunkCodingException(var2.toString());
         var4.initCause(var3);
         throw var4;
      }
   }

   public int available() throws IOException {
      SessionInputBuffer var1 = this.field_205;
      return var1 instanceof BufferInfo ? (int)Math.min((long)((BufferInfo)var1).length(), this.chunkSize - this.pos) : 0;
   }

   public void close() throws IOException {
      if (!this.closed) {
         label99: {
            Throwable var10000;
            label98: {
               boolean var10001;
               byte[] var2;
               try {
                  if (this.eof || this.state == Integer.MAX_VALUE) {
                     break label99;
                  }

                  var2 = new byte[2048];
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label98;
               }

               while(true) {
                  int var1;
                  try {
                     var1 = this.read(var2);
                  } catch (Throwable var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }

                  if (var1 < 0) {
                     break label99;
                  }
               }
            }

            Throwable var9 = var10000;
            this.eof = true;
            this.closed = true;
            throw var9;
         }

         this.eof = true;
         this.closed = true;
      }
   }

   public Header[] getFooters() {
      return (Header[])this.footers.clone();
   }

   public int read() throws IOException {
      if (!this.closed) {
         if (this.eof) {
            return -1;
         } else {
            if (this.state != 2) {
               this.nextChunk();
               if (this.eof) {
                  return -1;
               }
            }

            int var1 = this.field_205.read();
            if (var1 != -1) {
               long var2 = this.pos + 1L;
               this.pos = var2;
               if (var2 >= this.chunkSize) {
                  this.state = 3;
               }
            }

            return var1;
         }
      } else {
         throw new IOException("Attempted read from closed stream.");
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (!this.closed) {
         if (this.eof) {
            return -1;
         } else {
            if (this.state != 2) {
               this.nextChunk();
               if (this.eof) {
                  return -1;
               }
            }

            var2 = this.field_205.read(var1, var2, (int)Math.min((long)var3, this.chunkSize - this.pos));
            if (var2 != -1) {
               long var4 = this.pos + (long)var2;
               this.pos = var4;
               if (var4 >= this.chunkSize) {
                  this.state = 3;
               }

               return var2;
            } else {
               this.eof = true;
               throw new TruncatedChunkException("Truncated chunk (expected size: %,d; actual size: %,d)", new Object[]{this.chunkSize, this.pos});
            }
         }
      } else {
         throw new IOException("Attempted read from closed stream.");
      }
   }
}
