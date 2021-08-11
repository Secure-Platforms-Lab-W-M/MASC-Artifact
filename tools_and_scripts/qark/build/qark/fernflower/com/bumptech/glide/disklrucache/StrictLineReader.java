package com.bumptech.glide.disklrucache;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

class StrictLineReader implements Closeable {
   // $FF: renamed from: CR byte
   private static final byte field_144 = 13;
   // $FF: renamed from: LF byte
   private static final byte field_145 = 10;
   private byte[] buf;
   private final Charset charset;
   private int end;
   // $FF: renamed from: in java.io.InputStream
   private final InputStream field_146;
   private int pos;

   public StrictLineReader(InputStream var1, int var2, Charset var3) {
      if (var1 != null && var3 != null) {
         if (var2 >= 0) {
            if (var3.equals(Util.US_ASCII)) {
               this.field_146 = var1;
               this.charset = var3;
               this.buf = new byte[var2];
            } else {
               throw new IllegalArgumentException("Unsupported encoding");
            }
         } else {
            throw new IllegalArgumentException("capacity <= 0");
         }
      } else {
         throw null;
      }
   }

   public StrictLineReader(InputStream var1, Charset var2) {
      this(var1, 8192, var2);
   }

   private void fillBuf() throws IOException {
      InputStream var2 = this.field_146;
      byte[] var3 = this.buf;
      int var1 = var2.read(var3, 0, var3.length);
      if (var1 != -1) {
         this.pos = 0;
         this.end = var1;
      } else {
         throw new EOFException();
      }
   }

   public void close() throws IOException {
      InputStream var1 = this.field_146;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.buf != null) {
               this.buf = null;
               this.field_146.close();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean hasUnterminatedLine() {
      return this.end == -1;
   }

   public String readLine() throws IOException {
      InputStream var3 = this.field_146;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label1262: {
         label1257: {
            try {
               if (this.buf != null) {
                  if (this.pos >= this.end) {
                     this.fillBuf();
                  }
                  break label1257;
               }
            } catch (Throwable var114) {
               var10000 = var114;
               var10001 = false;
               break label1262;
            }

            try {
               throw new IOException("LineReader is closed");
            } catch (Throwable var113) {
               var10000 = var113;
               var10001 = false;
               break label1262;
            }
         }

         int var1;
         try {
            var1 = this.pos;
         } catch (Throwable var111) {
            var10000 = var111;
            var10001 = false;
            break label1262;
         }

         int var2;
         String var4;
         while(true) {
            label1264: {
               label1243: {
                  label1242: {
                     try {
                        if (var1 == this.end) {
                           break label1243;
                        }

                        if (this.buf[var1] != 10) {
                           break label1264;
                        }

                        if (var1 == this.pos || this.buf[var1 - 1] != 13) {
                           break label1242;
                        }
                     } catch (Throwable var112) {
                        var10000 = var112;
                        var10001 = false;
                        break label1262;
                     }

                     var2 = var1 - 1;
                     break;
                  }

                  var2 = var1;
                  break;
               }

               ByteArrayOutputStream var115;
               try {
                  var115 = new ByteArrayOutputStream(this.end - this.pos + 80) {
                     public String toString() {
                        int var1;
                        if (this.count > 0 && this.buf[this.count - 1] == 13) {
                           var1 = this.count - 1;
                        } else {
                           var1 = this.count;
                        }

                        try {
                           String var2 = new String(this.buf, 0, var1, StrictLineReader.this.charset.name());
                           return var2;
                        } catch (UnsupportedEncodingException var3) {
                           throw new AssertionError(var3);
                        }
                     }
                  };
               } catch (Throwable var109) {
                  var10000 = var109;
                  var10001 = false;
                  break label1262;
               }

               label1224:
               while(true) {
                  try {
                     var115.write(this.buf, this.pos, this.end - this.pos);
                     this.end = -1;
                     this.fillBuf();
                     var1 = this.pos;
                  } catch (Throwable var108) {
                     var10000 = var108;
                     var10001 = false;
                     break label1262;
                  }

                  while(true) {
                     label1219:
                     try {
                        if (var1 != this.end) {
                           if (this.buf[var1] != 10) {
                              break label1219;
                           }

                           if (var1 != this.pos) {
                              var115.write(this.buf, this.pos, var1 - this.pos);
                           }
                           break label1224;
                        }
                        break;
                     } catch (Throwable var110) {
                        var10000 = var110;
                        var10001 = false;
                        break label1262;
                     }

                     ++var1;
                  }
               }

               try {
                  this.pos = var1 + 1;
                  var4 = var115.toString();
                  return var4;
               } catch (Throwable var107) {
                  var10000 = var107;
                  var10001 = false;
                  break label1262;
               }
            }

            ++var1;
         }

         label1205:
         try {
            var4 = new String(this.buf, this.pos, var2 - this.pos, this.charset.name());
            this.pos = var1 + 1;
            return var4;
         } catch (Throwable var106) {
            var10000 = var106;
            var10001 = false;
            break label1205;
         }
      }

      while(true) {
         Throwable var116 = var10000;

         try {
            throw var116;
         } catch (Throwable var105) {
            var10000 = var105;
            var10001 = false;
            continue;
         }
      }
   }
}
