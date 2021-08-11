package com.jcraft.jzlib;

import java.io.ByteArrayOutputStream;

final class Inflate {
   private static final int BAD = 13;
   private static final int BLOCKS = 7;
   private static final int CHECK1 = 11;
   private static final int CHECK2 = 10;
   private static final int CHECK3 = 9;
   private static final int CHECK4 = 8;
   private static final int COMMENT = 21;
   private static final int DICT0 = 6;
   private static final int DICT1 = 5;
   private static final int DICT2 = 4;
   private static final int DICT3 = 3;
   private static final int DICT4 = 2;
   private static final int DONE = 12;
   private static final int EXLEN = 18;
   private static final int EXTRA = 19;
   private static final int FLAG = 1;
   private static final int FLAGS = 23;
   private static final int HCRC = 22;
   private static final int HEAD = 14;
   static final int INFLATE_ANY = 1073741824;
   private static final int LENGTH = 15;
   private static final int MAX_WBITS = 15;
   private static final int METHOD = 0;
   private static final int NAME = 20;
   // $FF: renamed from: OS int
   private static final int field_89 = 17;
   private static final int PRESET_DICT = 32;
   private static final int TIME = 16;
   private static final int Z_BUF_ERROR = -5;
   private static final int Z_DATA_ERROR = -3;
   private static final int Z_DEFLATED = 8;
   private static final int Z_ERRNO = -1;
   static final int Z_FINISH = 4;
   static final int Z_FULL_FLUSH = 3;
   private static final int Z_MEM_ERROR = -4;
   private static final int Z_NEED_DICT = 2;
   static final int Z_NO_FLUSH = 0;
   private static final int Z_OK = 0;
   static final int Z_PARTIAL_FLUSH = 1;
   private static final int Z_STREAM_END = 1;
   private static final int Z_STREAM_ERROR = -2;
   static final int Z_SYNC_FLUSH = 2;
   private static final int Z_VERSION_ERROR = -6;
   private static byte[] mark = new byte[]{0, 0, -1, -1};
   InfBlocks blocks;
   private byte[] crcbuf = new byte[4];
   private int flags;
   GZIPHeader gheader = null;
   int marker;
   int method;
   int mode;
   long need;
   private int need_bytes = -1;
   private ByteArrayOutputStream tmp_string = null;
   long was = -1L;
   int wbits;
   int wrap;
   // $FF: renamed from: z com.jcraft.jzlib.ZStream
   private final ZStream field_90;

   Inflate(ZStream var1) {
      this.field_90 = var1;
   }

   private void checksum(int var1, long var2) {
      for(int var4 = 0; var4 < var1; ++var4) {
         this.crcbuf[var4] = (byte)((int)(255L & var2));
         var2 >>= 8;
      }

      this.field_90.adler.update(this.crcbuf, 0, var1);
   }

   private int readBytes(int var1, int var2) throws Inflate.Return {
      if (this.tmp_string == null) {
         this.tmp_string = new ByteArrayOutputStream();
      }

      while(this.need > 0L) {
         if (this.field_90.avail_in == 0) {
            throw new Inflate.Return(var1);
         }

         var1 = var2;
         ZStream var4 = this.field_90;
         --var4.avail_in;
         var4 = this.field_90;
         ++var4.total_in;
         byte var10000 = this.field_90.next_in[this.field_90.next_in_index];
         this.tmp_string.write(this.field_90.next_in, this.field_90.next_in_index, 1);
         this.field_90.adler.update(this.field_90.next_in, this.field_90.next_in_index, 1);
         var4 = this.field_90;
         ++var4.next_in_index;
         --this.need;
      }

      return var1;
   }

   private int readBytes(int var1, int var2, int var3) throws Inflate.Return {
      int var4 = var2;
      if (this.need_bytes == -1) {
         this.need_bytes = var1;
         this.need = 0L;
         var4 = var2;
      }

      while(this.need_bytes > 0) {
         if (this.field_90.avail_in == 0) {
            throw new Inflate.Return(var4);
         }

         ZStream var8 = this.field_90;
         --var8.avail_in;
         var8 = this.field_90;
         ++var8.total_in;
         long var6 = this.need;
         byte[] var11 = this.field_90.next_in;
         ZStream var9 = this.field_90;
         var4 = var9.next_in_index++;
         byte var10 = var11[var4];
         int var5 = this.need_bytes;
         this.need = var6 | (long)((var10 & 255) << (var1 - var5) * 8);
         this.need_bytes = var5 - 1;
         var4 = var3;
      }

      if (var1 == 2) {
         this.need &= 65535L;
      } else if (var1 == 4) {
         this.need &= 4294967295L;
      }

      this.need_bytes = -1;
      return var4;
   }

   private int readString(int var1, int var2) throws Inflate.Return {
      if (this.tmp_string == null) {
         this.tmp_string = new ByteArrayOutputStream();
      }

      byte var4;
      do {
         if (this.field_90.avail_in == 0) {
            throw new Inflate.Return(var1);
         }

         ZStream var5 = this.field_90;
         --var5.avail_in;
         var5 = this.field_90;
         ++var5.total_in;
         var4 = this.field_90.next_in[this.field_90.next_in_index];
         if (var4 != 0) {
            this.tmp_string.write(this.field_90.next_in, this.field_90.next_in_index, 1);
         }

         this.field_90.adler.update(this.field_90.next_in, this.field_90.next_in_index, 1);
         var5 = this.field_90;
         ++var5.next_in_index;
         var1 = var2;
      } while(var4 != 0);

      return var2;
   }

   public GZIPHeader getGZIPHeader() {
      return this.gheader;
   }

   boolean inParsingHeader() {
      int var1 = this.mode;
      if (var1 != 2 && var1 != 3 && var1 != 4 && var1 != 5 && var1 != 14) {
         switch(var1) {
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
            break;
         default:
            return false;
         }
      }

      return true;
   }

   int inflate(int var1) {
      ZStream var8 = this.field_90;
      if (var8 != null && var8.next_in != null) {
         byte var2;
         if (var1 == 4) {
            var2 = -5;
         } else {
            var2 = 0;
         }

         var1 = -5;
         boolean var3 = false;

         long var6;
         ZStream var9;
         byte[] var22;
         label398:
         while(true) {
            while(true) {
               int var4;
               GZIPHeader var21;
               label411: {
                  label393:
                  while(true) {
                     label413: {
                        label414: {
                           int var20;
                           label415: {
                              label416: {
                                 label386:
                                 while(true) {
                                    switch(this.mode) {
                                    case 3:
                                       break label415;
                                    case 4:
                                       break label393;
                                    case 5:
                                       break label398;
                                    case 6:
                                       this.mode = 13;
                                       this.field_90.msg = "need dictionary";
                                       this.marker = 0;
                                       return -2;
                                    case 7:
                                       var1 = this.blocks.proc(var1);
                                       if (var1 == -3) {
                                          this.mode = 13;
                                          this.marker = 0;
                                          break;
                                       } else {
                                          var4 = var1;
                                          if (var1 == 0) {
                                             var4 = var2;
                                          }

                                          if (var4 != 1) {
                                             return var4;
                                          }

                                          var1 = var2;
                                          this.was = this.field_90.adler.getValue();
                                          this.blocks.reset();
                                          if (this.wrap == 0) {
                                             this.mode = 12;
                                             break;
                                          } else {
                                             this.mode = 8;
                                          }
                                       }
                                    case 8:
                                       if (this.field_90.avail_in == 0) {
                                          return var1;
                                       }

                                       var1 = var2;
                                       var8 = this.field_90;
                                       --var8.avail_in;
                                       var8 = this.field_90;
                                       ++var8.total_in;
                                       var22 = this.field_90.next_in;
                                       var9 = this.field_90;
                                       var4 = var9.next_in_index++;
                                       this.need = (long)((var22[var4] & 255) << 24) & 4278190080L;
                                       this.mode = 9;
                                    case 9:
                                       if (this.field_90.avail_in == 0) {
                                          return var1;
                                       }

                                       var1 = var2;
                                       var8 = this.field_90;
                                       --var8.avail_in;
                                       var8 = this.field_90;
                                       ++var8.total_in;
                                       var6 = this.need;
                                       var22 = this.field_90.next_in;
                                       var9 = this.field_90;
                                       var4 = var9.next_in_index++;
                                       this.need = var6 + ((long)((var22[var4] & 255) << 16) & 16711680L);
                                       this.mode = 10;
                                    case 10:
                                       if (this.field_90.avail_in == 0) {
                                          return var1;
                                       }

                                       var1 = var2;
                                       var8 = this.field_90;
                                       --var8.avail_in;
                                       var8 = this.field_90;
                                       ++var8.total_in;
                                       var6 = this.need;
                                       var22 = this.field_90.next_in;
                                       var9 = this.field_90;
                                       var4 = var9.next_in_index++;
                                       this.need = var6 + ((long)((var22[var4] & 255) << 8) & 65280L);
                                       this.mode = 11;
                                    case 11:
                                       break label386;
                                    case 12:
                                       return 1;
                                    case 13:
                                       return -3;
                                    case 14:
                                       if (this.wrap == 0) {
                                          this.mode = 7;
                                          break;
                                       } else {
                                          try {
                                             var1 = this.readBytes(2, var1, var2);
                                          } catch (Inflate.Return var11) {
                                             return var11.field_238;
                                          }

                                          var4 = this.wrap;
                                          if ((var4 == 4 || (var4 & 2) != 0) && this.need == 35615L) {
                                             if (this.wrap == 4) {
                                                this.wrap = 2;
                                             }

                                             this.field_90.adler = new CRC32();
                                             this.checksum(2, this.need);
                                             if (this.gheader == null) {
                                                this.gheader = new GZIPHeader();
                                             }

                                             this.mode = 23;
                                             break;
                                          } else {
                                             var4 = this.wrap;
                                             if ((var4 & 2) != 0) {
                                                this.mode = 13;
                                                this.field_90.msg = "incorrect header check";
                                                break;
                                             } else {
                                                this.flags = 0;
                                                var6 = this.need;
                                                int var5 = (int)var6 & 255;
                                                this.method = var5;
                                                var20 = (int)(var6 >> 8) & 255;
                                                if (((var4 & 1) == 0 || ((var5 << 8) + var20) % 31 != 0) && (this.method & 15) != 8) {
                                                   if (this.wrap == 4) {
                                                      var8 = this.field_90;
                                                      var8.next_in_index -= 2;
                                                      var8 = this.field_90;
                                                      var8.avail_in += 2;
                                                      var8 = this.field_90;
                                                      var8.total_in -= 2L;
                                                      this.wrap = 0;
                                                      this.mode = 7;
                                                   } else {
                                                      this.mode = 13;
                                                      this.field_90.msg = "incorrect header check";
                                                   }
                                                   break;
                                                } else if ((this.method & 15) != 8) {
                                                   this.mode = 13;
                                                   this.field_90.msg = "unknown compression method";
                                                   break;
                                                } else {
                                                   if (this.wrap == 4) {
                                                      this.wrap = 1;
                                                   }

                                                   if ((this.method >> 4) + 8 > this.wbits) {
                                                      this.mode = 13;
                                                      this.field_90.msg = "invalid window size";
                                                      break;
                                                   } else {
                                                      this.field_90.adler = new Adler32();
                                                      if ((var20 & 32) == 0) {
                                                         this.mode = 7;
                                                         break;
                                                      } else {
                                                         this.mode = 2;
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    case 2:
                                       if (this.field_90.avail_in == 0) {
                                          return var1;
                                       }

                                       var1 = var2;
                                       var8 = this.field_90;
                                       --var8.avail_in;
                                       var8 = this.field_90;
                                       ++var8.total_in;
                                       var22 = this.field_90.next_in;
                                       var9 = this.field_90;
                                       var20 = var9.next_in_index++;
                                       this.need = (long)((var22[var20] & 255) << 24) & 4278190080L;
                                       this.mode = 3;
                                       break label415;
                                    case 15:
                                       break label413;
                                    case 20:
                                       break label416;
                                    case 21:
                                       break label414;
                                    case 22:
                                       var4 = var1;
                                       break label411;
                                    case 23:
                                       try {
                                          var1 = this.readBytes(2, var1, var2);
                                       } catch (Inflate.Return var18) {
                                          return var18.field_238;
                                       }

                                       var6 = this.need;
                                       var4 = (int)var6 & '\uffff';
                                       this.flags = var4;
                                       if ((var4 & 255) != 8) {
                                          this.field_90.msg = "unknown compression method";
                                          this.mode = 13;
                                          break;
                                       } else if (('\ue000' & var4) != 0) {
                                          this.field_90.msg = "unknown header flags set";
                                          this.mode = 13;
                                          break;
                                       } else {
                                          if ((var4 & 512) != 0) {
                                             this.checksum(2, var6);
                                          }

                                          this.mode = 16;
                                       }
                                    case 16:
                                       try {
                                          var1 = this.readBytes(4, var1, var2);
                                       } catch (Inflate.Return var17) {
                                          return var17.field_238;
                                       }

                                       var21 = this.gheader;
                                       if (var21 != null) {
                                          var21.time = this.need;
                                       }

                                       if ((this.flags & 512) != 0) {
                                          this.checksum(4, this.need);
                                       }

                                       this.mode = 17;
                                    case 17:
                                       try {
                                          var1 = this.readBytes(2, var1, var2);
                                       } catch (Inflate.Return var16) {
                                          return var16.field_238;
                                       }

                                       var21 = this.gheader;
                                       if (var21 != null) {
                                          var21.xflags = (int)this.need & 255;
                                          this.gheader.field_148 = (int)this.need >> 8 & 255;
                                       }

                                       if ((this.flags & 512) != 0) {
                                          this.checksum(2, this.need);
                                       }

                                       this.mode = 18;
                                    case 18:
                                       if ((this.flags & 1024) != 0) {
                                          try {
                                             var4 = this.readBytes(2, var1, var2);
                                          } catch (Inflate.Return var15) {
                                             return var15.field_238;
                                          }

                                          var21 = this.gheader;
                                          if (var21 != null) {
                                             var21.extra = new byte[(int)this.need & '\uffff'];
                                          }

                                          if ((this.flags & 512) != 0) {
                                             this.checksum(2, this.need);
                                          }
                                       } else {
                                          var21 = this.gheader;
                                          var4 = var1;
                                          if (var21 != null) {
                                             var21.extra = null;
                                             var4 = var1;
                                          }
                                       }

                                       this.mode = 19;
                                       var1 = var4;
                                    case 19:
                                       if ((this.flags & 1024) == 0) {
                                          var21 = this.gheader;
                                          var4 = var1;
                                          if (var21 != null) {
                                             var21.extra = null;
                                             var4 = var1;
                                          }
                                       } else {
                                          try {
                                             var1 = this.readBytes(var1, var2);
                                             if (this.gheader != null) {
                                                var22 = this.tmp_string.toByteArray();
                                                this.tmp_string = null;
                                                if (var22.length != this.gheader.extra.length) {
                                                   this.field_90.msg = "bad extra field length";
                                                   this.mode = 13;
                                                   break;
                                                }

                                                System.arraycopy(var22, 0, this.gheader.extra, 0, var22.length);
                                             }
                                          } catch (Inflate.Return var19) {
                                             return var19.field_238;
                                          }

                                          var4 = var1;
                                       }

                                       this.mode = 20;
                                       var1 = var4;
                                       break label416;
                                    default:
                                       return -2;
                                    }
                                 }

                                 if (this.field_90.avail_in == 0) {
                                    return var1;
                                 }

                                 var8 = this.field_90;
                                 --var8.avail_in;
                                 var8 = this.field_90;
                                 ++var8.total_in;
                                 var6 = this.need;
                                 var22 = this.field_90.next_in;
                                 var9 = this.field_90;
                                 var1 = var9.next_in_index++;
                                 var6 += (long)var22[var1] & 255L;
                                 this.need = var6;
                                 if (this.flags != 0) {
                                    this.need = ((var6 & 65535L) << 24 | (65280L & var6) << 8 | (-16777216L & var6) >> 24 | (var6 & 16711680L) >> 8) & 4294967295L;
                                 }

                                 var1 = (int)this.was;
                                 var6 = this.need;
                                 if (var1 != (int)var6) {
                                    this.field_90.msg = "incorrect data check";
                                 } else if (this.flags != 0) {
                                    var21 = this.gheader;
                                    if (var21 != null) {
                                       var21.crc = var6;
                                    }
                                 }

                                 this.mode = 15;
                                 var1 = var2;
                                 break label413;
                              }

                              if ((this.flags & 2048) != 0) {
                                 try {
                                    var4 = this.readString(var1, var2);
                                    if (this.gheader != null) {
                                       this.gheader.name = this.tmp_string.toByteArray();
                                    }

                                    this.tmp_string = null;
                                 } catch (Inflate.Return var14) {
                                    return var14.field_238;
                                 }
                              } else {
                                 var21 = this.gheader;
                                 var4 = var1;
                                 if (var21 != null) {
                                    var21.name = null;
                                    var4 = var1;
                                 }
                              }

                              this.mode = 21;
                              var1 = var4;
                              break label414;
                           }

                           if (this.field_90.avail_in == 0) {
                              return var1;
                           }

                           var1 = var2;
                           var8 = this.field_90;
                           --var8.avail_in;
                           var8 = this.field_90;
                           ++var8.total_in;
                           var6 = this.need;
                           var22 = this.field_90.next_in;
                           var9 = this.field_90;
                           var20 = var9.next_in_index++;
                           this.need = var6 + ((long)((var22[var20] & 255) << 16) & 16711680L);
                           this.mode = 4;
                           break;
                        }

                        if ((this.flags & 4096) != 0) {
                           try {
                              var4 = this.readString(var1, var2);
                              if (this.gheader != null) {
                                 this.gheader.comment = this.tmp_string.toByteArray();
                              }

                              this.tmp_string = null;
                           } catch (Inflate.Return var13) {
                              return var13.field_238;
                           }
                        } else {
                           var21 = this.gheader;
                           var4 = var1;
                           if (var21 != null) {
                              var21.comment = null;
                              var4 = var1;
                           }
                        }

                        this.mode = 22;
                        break label411;
                     }

                     if (this.wrap != 0 && this.flags != 0) {
                        try {
                           var1 = this.readBytes(4, var1, var2);
                        } catch (Inflate.Return var10) {
                           return var10.field_238;
                        }

                        if (this.field_90.msg != null && this.field_90.msg.equals("incorrect data check")) {
                           this.mode = 13;
                           this.marker = 5;
                           continue;
                        }

                        if (this.need != (this.field_90.total_out & 4294967295L)) {
                           this.field_90.msg = "incorrect length check";
                           this.mode = 13;
                           continue;
                        }

                        this.field_90.msg = null;
                     } else if (this.field_90.msg != null && this.field_90.msg.equals("incorrect data check")) {
                        this.mode = 13;
                        this.marker = 5;
                        continue;
                     }

                     this.mode = 12;
                     return 1;
                  }

                  if (this.field_90.avail_in == 0) {
                     return var1;
                  }

                  var8 = this.field_90;
                  --var8.avail_in;
                  var8 = this.field_90;
                  ++var8.total_in;
                  var6 = this.need;
                  var22 = this.field_90.next_in;
                  var9 = this.field_90;
                  var1 = var9.next_in_index++;
                  this.need = var6 + ((long)((var22[var1] & 255) << 8) & 65280L);
                  this.mode = 5;
                  var1 = var2;
                  break label398;
               }

               var1 = var4;
               if ((this.flags & 512) != 0) {
                  try {
                     var4 = this.readBytes(2, var4, var2);
                  } catch (Inflate.Return var12) {
                     return var12.field_238;
                  }

                  var21 = this.gheader;
                  if (var21 != null) {
                     var21.hcrc = (int)(this.need & 65535L);
                  }

                  var1 = var4;
                  if (this.need != (65535L & this.field_90.adler.getValue())) {
                     this.mode = 13;
                     this.field_90.msg = "header crc mismatch";
                     this.marker = 5;
                     var1 = var4;
                     continue;
                  }
               }

               this.field_90.adler = new CRC32();
               this.mode = 7;
            }
         }

         if (this.field_90.avail_in == 0) {
            return var1;
         } else {
            var8 = this.field_90;
            --var8.avail_in;
            var8 = this.field_90;
            ++var8.total_in;
            var6 = this.need;
            var22 = this.field_90.next_in;
            var9 = this.field_90;
            var1 = var9.next_in_index++;
            this.need = var6 + ((long)var22[var1] & 255L);
            this.field_90.adler.reset(this.need);
            this.mode = 6;
            return 2;
         }
      } else {
         return var1 == 4 && this.mode == 14 ? 0 : -2;
      }
   }

   int inflateEnd() {
      InfBlocks var1 = this.blocks;
      if (var1 != null) {
         var1.free();
      }

      return 0;
   }

   int inflateInit(int var1) {
      this.field_90.msg = null;
      this.blocks = null;
      this.wrap = 0;
      int var2;
      if (var1 < 0) {
         var2 = -var1;
      } else if ((1073741824 & var1) != 0) {
         this.wrap = 4;
         var1 &= -1073741825;
         var2 = var1;
         if (var1 < 48) {
            var2 = var1 & 15;
         }
      } else if ((var1 & -32) != 0) {
         this.wrap = 4;
         var2 = var1 & 15;
      } else {
         this.wrap = (var1 >> 4) + 1;
         var2 = var1;
         if (var1 < 48) {
            var2 = var1 & 15;
         }
      }

      if (var2 >= 8 && var2 <= 15) {
         InfBlocks var3 = this.blocks;
         if (var3 != null && this.wbits != var2) {
            var3.free();
            this.blocks = null;
         }

         this.wbits = var2;
         this.blocks = new InfBlocks(this.field_90, 1 << var2);
         this.inflateReset();
         return 0;
      } else {
         this.inflateEnd();
         return -2;
      }
   }

   int inflateReset() {
      ZStream var1 = this.field_90;
      if (var1 == null) {
         return -2;
      } else {
         var1.total_out = 0L;
         var1.total_in = 0L;
         this.field_90.msg = null;
         this.mode = 14;
         this.need_bytes = -1;
         this.blocks.reset();
         return 0;
      }
   }

   int inflateSetDictionary(byte[] var1, int var2) {
      if (this.field_90 == null || this.mode != 6 && this.wrap != 0) {
         return -2;
      } else {
         int var5 = 0;
         if (this.mode == 6) {
            long var7 = this.field_90.adler.getValue();
            this.field_90.adler.reset();
            this.field_90.adler.update(var1, 0, var2);
            if (this.field_90.adler.getValue() != var7) {
               return -3;
            }
         }

         this.field_90.adler.reset();
         int var6 = this.wbits;
         int var4 = var2;
         if (var2 >= 1 << var6) {
            var4 = (1 << var6) - 1;
            var5 = var2 - var4;
         }

         this.blocks.set_dictionary(var1, var5, var4);
         this.mode = 7;
         return 0;
      }
   }

   int inflateSync() {
      if (this.field_90 == null) {
         return -2;
      } else {
         if (this.mode != 13) {
            this.mode = 13;
            this.marker = 0;
         }

         int var1 = this.field_90.avail_in;
         int var2 = var1;
         if (var1 == 0) {
            return -5;
         } else {
            int var3 = this.field_90.next_in_index;

            for(var1 = this.marker; var2 != 0 && var1 < 4; --var2) {
               if (this.field_90.next_in[var3] == mark[var1]) {
                  ++var1;
               } else if (this.field_90.next_in[var3] != 0) {
                  var1 = 0;
               } else {
                  var1 = 4 - var1;
               }

               ++var3;
            }

            ZStream var8 = this.field_90;
            var8.total_in += (long)(var3 - this.field_90.next_in_index);
            this.field_90.next_in_index = var3;
            this.field_90.avail_in = var2;
            this.marker = var1;
            if (var1 != 4) {
               return -3;
            } else {
               long var4 = this.field_90.total_in;
               long var6 = this.field_90.total_out;
               this.inflateReset();
               this.field_90.total_in = var4;
               this.field_90.total_out = var6;
               this.mode = 7;
               return 0;
            }
         }
      }
   }

   int inflateSyncPoint() {
      if (this.field_90 != null) {
         InfBlocks var1 = this.blocks;
         if (var1 != null) {
            return var1.sync_point();
         }
      }

      return -2;
   }

   class Return extends Exception {
      // $FF: renamed from: r int
      int field_238;

      Return(int var2) {
         this.field_238 = var2;
      }
   }
}
