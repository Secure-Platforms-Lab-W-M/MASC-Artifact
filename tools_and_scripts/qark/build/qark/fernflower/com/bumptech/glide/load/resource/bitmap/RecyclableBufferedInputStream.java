package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecyclableBufferedInputStream extends FilterInputStream {
   private volatile byte[] buf;
   private final ArrayPool byteArrayPool;
   private int count;
   private int marklimit;
   private int markpos;
   private int pos;

   public RecyclableBufferedInputStream(InputStream var1, ArrayPool var2) {
      this(var1, var2, 65536);
   }

   RecyclableBufferedInputStream(InputStream var1, ArrayPool var2, int var3) {
      super(var1);
      this.markpos = -1;
      this.byteArrayPool = var2;
      this.buf = (byte[])var2.get(var3, byte[].class);
   }

   private int fillbuf(InputStream var1, byte[] var2) throws IOException {
      int var3 = this.markpos;
      if (var3 != -1) {
         int var4 = this.pos;
         int var5 = this.marklimit;
         if (var4 - var3 < var5) {
            label43: {
               byte[] var6;
               if (var3 == 0 && var5 > var2.length && this.count == var2.length) {
                  var4 = var2.length * 2;
                  var3 = var4;
                  if (var4 > var5) {
                     var3 = this.marklimit;
                  }

                  var6 = (byte[])this.byteArrayPool.get(var3, byte[].class);
                  System.arraycopy(var2, 0, var6, 0, var2.length);
                  this.buf = var6;
                  this.byteArrayPool.put(var2);
               } else {
                  var3 = this.markpos;
                  var6 = var2;
                  if (var3 > 0) {
                     System.arraycopy(var2, var3, var2, 0, var2.length - var3);
                     break label43;
                  }
               }

               var2 = var6;
            }

            var3 = this.pos - this.markpos;
            this.pos = var3;
            this.markpos = 0;
            this.count = 0;
            var4 = var1.read(var2, var3, var2.length - var3);
            var3 = this.pos;
            if (var4 > 0) {
               var3 += var4;
            }

            this.count = var3;
            return var4;
         }
      }

      var3 = var1.read(var2);
      if (var3 > 0) {
         this.markpos = -1;
         this.pos = 0;
         this.count = var3;
      }

      return var3;
   }

   private static IOException streamClosed() throws IOException {
      throw new IOException("BufferedInputStream is closed");
   }

   public int available() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label117: {
         boolean var10001;
         label116: {
            InputStream var4;
            try {
               var4 = this.in;
               if (this.buf == null) {
                  break label116;
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label117;
            }

            if (var4 != null) {
               int var1;
               int var2;
               int var3;
               try {
                  var1 = this.count;
                  var2 = this.pos;
                  var3 = var4.available();
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label117;
               }

               return var1 - var2 + var3;
            }
         }

         label110:
         try {
            throw streamClosed();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label110;
         }
      }

      Throwable var17 = var10000;
      throw var17;
   }

   public void close() throws IOException {
      if (this.buf != null) {
         this.byteArrayPool.put(this.buf);
         this.buf = null;
      }

      InputStream var1 = this.in;
      this.in = null;
      if (var1 != null) {
         var1.close();
      }

   }

   public void fixMarkLimit() {
      synchronized(this){}

      try {
         this.marklimit = this.buf.length;
      } finally {
         ;
      }

   }

   public void mark(int var1) {
      synchronized(this){}

      try {
         this.marklimit = Math.max(this.marklimit, var1);
         this.markpos = this.pos;
      } finally {
         ;
      }

   }

   public boolean markSupported() {
      return true;
   }

   public int read() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label497: {
         InputStream var2;
         byte[] var3;
         boolean var10001;
         try {
            var3 = this.buf;
            var2 = this.in;
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label497;
         }

         if (var3 != null && var2 != null) {
            label498: {
               int var1;
               label483: {
                  try {
                     if (this.pos < this.count) {
                        break label483;
                     }

                     var1 = this.fillbuf(var2, var3);
                  } catch (Throwable var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label498;
                  }

                  if (var1 == -1) {
                     return -1;
                  }
               }

               byte[] var47 = var3;

               label475: {
                  try {
                     if (var3 == this.buf) {
                        break label475;
                     }

                     var47 = this.buf;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label498;
                  }

                  if (var47 == null) {
                     try {
                        throw streamClosed();
                     } catch (Throwable var40) {
                        var10000 = var40;
                        var10001 = false;
                        break label498;
                     }
                  }
               }

               label467: {
                  try {
                     if (this.count - this.pos > 0) {
                        var1 = this.pos++;
                        break label467;
                     }
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label498;
                  }

                  return -1;
               }

               byte var46 = var47[var1];
               return var46 & 255;
            }
         } else {
            label486:
            try {
               throw streamClosed();
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label486;
            }
         }
      }

      Throwable var48 = var10000;
      throw var48;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      synchronized(this){}

      Throwable var10000;
      label3087: {
         byte[] var8;
         boolean var10001;
         try {
            var8 = this.buf;
         } catch (Throwable var312) {
            var10000 = var312;
            var10001 = false;
            break label3087;
         }

         if (var8 != null) {
            label3088: {
               if (var3 == 0) {
                  return 0;
               }

               InputStream var10;
               try {
                  var10 = this.in;
               } catch (Throwable var311) {
                  var10000 = var311;
                  var10001 = false;
                  break label3088;
               }

               if (var10 == null) {
                  label3004:
                  try {
                     throw streamClosed();
                  } catch (Throwable var301) {
                     var10000 = var301;
                     var10001 = false;
                     break label3004;
                  }
               } else {
                  label3092: {
                     int var4;
                     int var5;
                     label3074: {
                        label3089: {
                           label3072: {
                              label3071: {
                                 try {
                                    if (this.pos >= this.count) {
                                       break label3089;
                                    }

                                    if (this.count - this.pos < var3) {
                                       break label3071;
                                    }
                                 } catch (Throwable var316) {
                                    var10000 = var316;
                                    var10001 = false;
                                    break label3092;
                                 }

                                 var4 = var3;
                                 break label3072;
                              }

                              try {
                                 var4 = this.count - this.pos;
                              } catch (Throwable var309) {
                                 var10000 = var309;
                                 var10001 = false;
                                 break label3092;
                              }
                           }

                           try {
                              System.arraycopy(var8, this.pos, var1, var2, var4);
                              this.pos += var4;
                           } catch (Throwable var308) {
                              var10000 = var308;
                              var10001 = false;
                              break label3092;
                           }

                           if (var4 == var3) {
                              return var4;
                           }

                           try {
                              var5 = var10.available();
                           } catch (Throwable var307) {
                              var10000 = var307;
                              var10001 = false;
                              break label3092;
                           }

                           if (var5 == 0) {
                              return var4;
                           }

                           var5 = var2 + var4;
                           var2 = var3 - var4;
                           var4 = var5;
                           break label3074;
                        }

                        var4 = var2;
                        var2 = var3;
                     }

                     while(true) {
                        int var6;
                        try {
                           var6 = this.markpos;
                        } catch (Throwable var306) {
                           var10000 = var306;
                           var10001 = false;
                           break;
                        }

                        label3059: {
                           int var7;
                           label3091: {
                              var5 = -1;
                              if (var6 == -1) {
                                 try {
                                    if (var2 >= var8.length) {
                                       var7 = var10.read(var1, var4, var2);
                                       break label3091;
                                    }
                                 } catch (Throwable var315) {
                                    var10000 = var315;
                                    var10001 = false;
                                    break;
                                 }
                              }

                              try {
                                 var6 = this.fillbuf(var10, var8);
                              } catch (Throwable var305) {
                                 var10000 = var305;
                                 var10001 = false;
                                 break;
                              }

                              if (var6 == -1) {
                                 if (var2 != var3) {
                                    var5 = var3 - var2;
                                 }

                                 return var5;
                              }

                              byte[] var9 = var8;

                              label3051: {
                                 try {
                                    if (var8 == this.buf) {
                                       break label3051;
                                    }

                                    var9 = this.buf;
                                 } catch (Throwable var314) {
                                    var10000 = var314;
                                    var10001 = false;
                                    break;
                                 }

                                 if (var9 == null) {
                                    try {
                                       throw streamClosed();
                                    } catch (Throwable var300) {
                                       var10000 = var300;
                                       var10001 = false;
                                       break;
                                    }
                                 }
                              }

                              label3045: {
                                 label3044: {
                                    try {
                                       if (this.count - this.pos >= var2) {
                                          break label3044;
                                       }
                                    } catch (Throwable var313) {
                                       var10000 = var313;
                                       var10001 = false;
                                       break;
                                    }

                                    try {
                                       var5 = this.count - this.pos;
                                       break label3045;
                                    } catch (Throwable var304) {
                                       var10000 = var304;
                                       var10001 = false;
                                       break;
                                    }
                                 }

                                 var5 = var2;
                              }

                              try {
                                 System.arraycopy(var9, this.pos, var1, var4, var5);
                                 this.pos += var5;
                              } catch (Throwable var303) {
                                 var10000 = var303;
                                 var10001 = false;
                                 break;
                              }

                              var6 = var5;
                              var8 = var9;
                              break label3059;
                           }

                           var6 = var7;
                           if (var7 == -1) {
                              if (var2 != var3) {
                                 var5 = var3 - var2;
                              }

                              return var5;
                           }
                        }

                        var2 -= var6;
                        if (var2 == 0) {
                           return var3;
                        }

                        try {
                           var5 = var10.available();
                        } catch (Throwable var310) {
                           var10000 = var310;
                           var10001 = false;
                           break;
                        }

                        if (var5 == 0) {
                           return var3 - var2;
                        }

                        var4 += var6;
                     }
                  }
               }
            }
         } else {
            label3006:
            try {
               throw streamClosed();
            } catch (Throwable var302) {
               var10000 = var302;
               var10001 = false;
               break label3006;
            }
         }
      }

      Throwable var317 = var10000;
      throw var317;
   }

   public void release() {
      synchronized(this){}

      try {
         if (this.buf != null) {
            this.byteArrayPool.put(this.buf);
            this.buf = null;
         }
      } finally {
         ;
      }

   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         if (this.buf == null) {
            throw new IOException("Stream is closed");
         }

         if (-1 == this.markpos) {
            StringBuilder var1 = new StringBuilder();
            var1.append("Mark has been invalidated, pos: ");
            var1.append(this.pos);
            var1.append(" markLimit: ");
            var1.append(this.marklimit);
            throw new RecyclableBufferedInputStream.InvalidMarkException(var1.toString());
         }

         this.pos = this.markpos;
      } finally {
         ;
      }

   }

   public long skip(long var1) throws IOException {
      synchronized(this){}
      if (var1 < 1L) {
         return 0L;
      } else {
         Throwable var10000;
         label913: {
            byte[] var8;
            boolean var10001;
            try {
               var8 = this.buf;
            } catch (Throwable var96) {
               var10000 = var96;
               var10001 = false;
               break label913;
            }

            if (var8 != null) {
               label914: {
                  InputStream var9;
                  try {
                     var9 = this.in;
                  } catch (Throwable var95) {
                     var10000 = var95;
                     var10001 = false;
                     break label914;
                  }

                  if (var9 == null) {
                     label872:
                     try {
                        throw streamClosed();
                     } catch (Throwable var93) {
                        var10000 = var93;
                        var10001 = false;
                        break label872;
                     }
                  } else {
                     label918: {
                        label902: {
                           try {
                              if ((long)(this.count - this.pos) < var1) {
                                 break label902;
                              }

                              this.pos = (int)((long)this.pos + var1);
                           } catch (Throwable var99) {
                              var10000 = var99;
                              var10001 = false;
                              break label918;
                           }

                           return var1;
                        }

                        long var4;
                        label915: {
                           int var3;
                           try {
                              var4 = (long)this.count - (long)this.pos;
                              this.pos = this.count;
                              if (this.markpos == -1 || var1 > (long)this.marklimit) {
                                 break label915;
                              }

                              var3 = this.fillbuf(var9, var8);
                           } catch (Throwable var98) {
                              var10000 = var98;
                              var10001 = false;
                              break label918;
                           }

                           if (var3 == -1) {
                              return var4;
                           }

                           try {
                              if ((long)(this.count - this.pos) >= var1 - var4) {
                                 this.pos = (int)((long)this.pos + var1 - var4);
                                 return var1;
                              }
                           } catch (Throwable var97) {
                              var10000 = var97;
                              var10001 = false;
                              break label918;
                           }

                           long var6;
                           try {
                              var1 = (long)this.count;
                              var6 = (long)this.pos;
                              this.pos = this.count;
                           } catch (Throwable var91) {
                              var10000 = var91;
                              var10001 = false;
                              break label918;
                           }

                           return var1 + var4 - var6;
                        }

                        try {
                           var1 = var9.skip(var1 - var4);
                        } catch (Throwable var92) {
                           var10000 = var92;
                           var10001 = false;
                           break label918;
                        }

                        return var1 + var4;
                     }
                  }
               }
            } else {
               label874:
               try {
                  throw streamClosed();
               } catch (Throwable var94) {
                  var10000 = var94;
                  var10001 = false;
                  break label874;
               }
            }
         }

         Throwable var100 = var10000;
         throw var100;
      }
   }

   static class InvalidMarkException extends IOException {
      private static final long serialVersionUID = -4338378848813561757L;

      InvalidMarkException(String var1) {
         super(var1);
      }
   }
}
