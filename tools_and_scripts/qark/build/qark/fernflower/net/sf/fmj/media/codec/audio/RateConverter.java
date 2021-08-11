package net.sf.fmj.media.codec.audio;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.AudioFormat;

public class RateConverter extends AudioCodec {
   public RateConverter() {
      this.inputFormats = new Format[]{new AudioFormat("LINEAR")};
   }

   private int doByteCvrt(Buffer var1, int var2, int var3, Buffer var4, int var5, int var6, double var7) {
      byte[] var18 = (byte[])((byte[])var1.getData());
      byte[] var17 = this.validateByteArraySize(var4, var5);
      var4.setData(var17);
      var4.setFormat(this.outputFormat);
      var4.setOffset(0);
      var4.setLength(var5);
      double var9 = 0.0D;
      int var13 = var3;
      byte var15 = 0;
      byte var14 = 0;
      int var16 = var3 + var2;
      if (var7 == 1.0D) {
         System.arraycopy(var18, var3, var17, 0, var2);
         return 0;
      } else {
         if (var7 > 1.0D) {
            var2 = var14;

            while(var13 <= var16 - var6 && var2 <= var5 - var6) {
               for(var3 = 0; var3 < var6; ++var2) {
                  var17[var2] = var18[var13 + var3];
                  ++var3;
               }

               for(var9 += var7; var9 > 0.0D; --var9) {
                  var13 += var6;
               }
            }
         } else {
            byte[] var19 = new byte[var6];

            label55:
            for(var2 = var15; var13 <= var16 - var6; var13 += var6) {
               int var20 = 0;
               var3 = var2;

               while(true) {
                  double var11 = var9;
                  var2 = var3;
                  if (var20 >= var6) {
                     while(true) {
                        do {
                           var11 += var7;
                           var9 = var11;
                           if (var11 >= 1.0D) {
                              var9 = var11 - 1.0D;
                              continue label55;
                           }

                           var11 = var11;
                        } while(var2 > var5 - var6);

                        for(var3 = 0; var3 < var6; ++var2) {
                           var17[var2] = var19[var3];
                           ++var3;
                        }

                        var11 = var9;
                     }
                  }

                  var17[var3] = var18[var13 + var20];
                  var19[var20] = var18[var13 + var20];
                  ++var20;
                  ++var3;
               }
            }
         }

         return 0;
      }
   }

   private int doIntCvrt(Buffer var1, int var2, int var3, Buffer var4, int var5, int var6, double var7) {
      int[] var18 = (int[])((int[])var1.getData());
      int[] var17 = this.validateIntArraySize(var4, var5);
      var4.setData(var17);
      var4.setFormat(this.outputFormat);
      var4.setOffset(0);
      var4.setLength(var5);
      double var9 = 0.0D;
      int var13 = var3;
      byte var15 = 0;
      byte var14 = 0;
      int var16 = var3 + var2;
      if (var7 == 1.0D) {
         System.arraycopy(var18, var3, var17, 0, var2);
         return 0;
      } else {
         if (var7 > 1.0D) {
            var2 = var14;

            while(var13 <= var16 - var6 && var2 <= var5 - var6) {
               for(var3 = 0; var3 < var6; ++var2) {
                  var17[var2] = var18[var13 + var3];
                  ++var3;
               }

               for(var9 += var7; var9 > 0.0D; --var9) {
                  var13 += var6;
               }
            }
         } else {
            int[] var19 = new int[var6];

            label55:
            for(var2 = var15; var13 <= var16 - var6; var13 += var6) {
               int var20 = 0;
               var3 = var2;

               while(true) {
                  double var11 = var9;
                  var2 = var3;
                  if (var20 >= var6) {
                     while(true) {
                        do {
                           var11 += var7;
                           var9 = var11;
                           if (var11 >= 1.0D) {
                              var9 = var11 - 1.0D;
                              continue label55;
                           }

                           var11 = var11;
                        } while(var2 > var5 - var6);

                        for(var3 = 0; var3 < var6; ++var2) {
                           var17[var2] = var19[var3];
                           ++var3;
                        }

                        var11 = var9;
                     }
                  }

                  var17[var3] = var18[var13 + var20];
                  var19[var20] = var18[var13 + var20];
                  ++var20;
                  ++var3;
               }
            }
         }

         return 0;
      }
   }

   private int doShortCvrt(Buffer var1, int var2, int var3, Buffer var4, int var5, int var6, double var7) {
      short[] var18 = (short[])((short[])var1.getData());
      short[] var17 = this.validateShortArraySize(var4, var5);
      var4.setData(var17);
      var4.setFormat(this.outputFormat);
      var4.setOffset(0);
      var4.setLength(var5);
      double var9 = 0.0D;
      int var13 = var3;
      byte var15 = 0;
      byte var14 = 0;
      int var16 = var3 + var2;
      if (var7 == 1.0D) {
         System.arraycopy(var18, var3, var17, 0, var2);
         return 0;
      } else {
         if (var7 > 1.0D) {
            var2 = var14;

            while(var13 <= var16 - var6 && var2 <= var5 - var6) {
               for(var3 = 0; var3 < var6; ++var2) {
                  var17[var2] = var18[var13 + var3];
                  ++var3;
               }

               for(var9 += var7; var9 > 0.0D; --var9) {
                  var13 += var6;
               }
            }
         } else {
            short[] var19 = new short[var6];

            label55:
            for(var2 = var15; var13 <= var16 - var6; var13 += var6) {
               int var20 = 0;
               var3 = var2;

               while(true) {
                  double var11 = var9;
                  var2 = var3;
                  if (var20 >= var6) {
                     while(true) {
                        do {
                           var11 += var7;
                           var9 = var11;
                           if (var11 >= 1.0D) {
                              var9 = var11 - 1.0D;
                              continue label55;
                           }

                           var11 = var11;
                        } while(var2 > var5 - var6);

                        for(var3 = 0; var3 < var6; ++var2) {
                           var17[var2] = var19[var3];
                           ++var3;
                        }

                        var11 = var9;
                     }
                  }

                  var17[var3] = var18[var13 + var20];
                  var19[var20] = var18[var13 + var20];
                  ++var20;
                  ++var3;
               }
            }
         }

         return 0;
      }
   }

   public String getName() {
      return "Rate Conversion";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return new Format[]{new AudioFormat("LINEAR")};
      } else {
         if (var1 instanceof AudioFormat) {
            AudioFormat var6 = (AudioFormat)var1;
            int var2 = var6.getSampleSizeInBits();
            int var3 = var6.getChannels();
            int var4 = var6.getEndian();
            int var5 = var6.getSigned();
            this.outputFormats = new Format[]{new AudioFormat("LINEAR", 8000.0D, var2, var3, var4, var5), new AudioFormat("LINEAR", 11025.0D, var2, var3, var4, var5), new AudioFormat("LINEAR", 16000.0D, var2, var3, var4, var5), new AudioFormat("LINEAR", 22050.0D, var2, var3, var4, var5), new AudioFormat("LINEAR", 32000.0D, var2, var3, var4, var5), new AudioFormat("LINEAR", 44100.0D, var2, var3, var4, var5), new AudioFormat("LINEAR", 48000.0D, var2, var3, var4, var5)};
         } else {
            this.outputFormats = new Format[0];
         }

         return this.outputFormats;
      }
   }

   public int process(Buffer var1, Buffer var2) {
      synchronized(this){}

      Throwable var10000;
      label997: {
         boolean var13;
         boolean var10001;
         try {
            var13 = this.checkInputBuffer(var1);
         } catch (Throwable var82) {
            var10000 = var82;
            var10001 = false;
            break label997;
         }

         if (!var13) {
            return 1;
         }

         try {
            if (this.isEOM(var1)) {
               this.propagateEOM(var2);
               return 0;
            }
         } catch (Throwable var85) {
            var10000 = var85;
            var10001 = false;
            break label997;
         }

         double var3;
         double var5;
         int var9;
         int var10;
         int var11;
         int var12;
         try {
            var11 = var1.getOffset();
            var12 = var1.getLength();
            var3 = ((AudioFormat)this.inputFormat).getSampleRate();
            var5 = ((AudioFormat)this.outputFormat).getSampleRate();
            var9 = ((AudioFormat)this.inputFormat).getChannels();
            var10 = ((AudioFormat)this.inputFormat).getSampleSizeInBits() / 8;
         } catch (Throwable var81) {
            var10000 = var81;
            var10001 = false;
            break label997;
         }

         byte var87;
         if (var9 == 2) {
            if (var10 == 2) {
               var87 = 4;
            } else {
               var87 = 2;
            }
         } else if (var10 == 2) {
            var87 = 2;
         } else {
            var87 = 1;
         }

         if (var5 != 0.0D && var3 != 0.0D) {
            double var7;
            try {
               var7 = var3 / var5;
               var10 = (int)((double)(var12 - var11) * var5 / var3 + 0.5D);
            } catch (Throwable var80) {
               var10000 = var80;
               var10001 = false;
               break label997;
            }

            if (var87 != 2) {
               if (var87 == 4 && var10 % 4 != 0) {
                  try {
                     var10 = var10 / 4 + 1 << 2;
                  } catch (Throwable var79) {
                     var10000 = var79;
                     var10001 = false;
                     break label997;
                  }
               }
            } else if (var10 % 2 == 1) {
               ++var10;
            }

            try {
               if (this.inputFormat.getDataType() == Format.byteArray) {
                  var9 = this.doByteCvrt(var1, var12, var11, var2, var10, var87, var7);
                  return var9;
               }
            } catch (Throwable var84) {
               var10000 = var84;
               var10001 = false;
               break label997;
            }

            label971: {
               try {
                  if (this.inputFormat.getDataType() != Format.shortArray) {
                     break label971;
                  }

                  var9 = this.doShortCvrt(var1, var12, var11, var2, var10, var87, var7);
               } catch (Throwable var83) {
                  var10000 = var83;
                  var10001 = false;
                  break label997;
               }

               return var9;
            }

            try {
               if (this.inputFormat.getDataType() == Format.intArray) {
                  var9 = this.doIntCvrt(var1, var12, var11, var2, var10, var87, var7);
                  return var9;
               }
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break label997;
            }

            return 1;
         }

         return 1;
      }

      Throwable var86 = var10000;
      throw var86;
   }
}
