package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;

public class StandardGifDecoder implements GifDecoder {
   private static final int BYTES_PER_INTEGER = 4;
   private static final int COLOR_TRANSPARENT_BLACK = 0;
   private static final int INITIAL_FRAME_POINTER = -1;
   private static final int MASK_INT_LOWEST_BYTE = 255;
   private static final int MAX_STACK_SIZE = 4096;
   private static final int NULL_CODE = -1;
   private static final String TAG = StandardGifDecoder.class.getSimpleName();
   private int[] act;
   private Config bitmapConfig;
   private final GifDecoder.BitmapProvider bitmapProvider;
   private byte[] block;
   private int downsampledHeight;
   private int downsampledWidth;
   private int framePointer;
   private GifHeader header;
   private Boolean isFirstFrameTransparent;
   private byte[] mainPixels;
   private int[] mainScratch;
   private GifHeaderParser parser;
   private final int[] pct;
   private byte[] pixelStack;
   private short[] prefix;
   private Bitmap previousImage;
   private ByteBuffer rawData;
   private int sampleSize;
   private boolean savePrevious;
   private int status;
   private byte[] suffix;

   public StandardGifDecoder(GifDecoder.BitmapProvider var1) {
      this.pct = new int[256];
      this.bitmapConfig = Config.ARGB_8888;
      this.bitmapProvider = var1;
      this.header = new GifHeader();
   }

   public StandardGifDecoder(GifDecoder.BitmapProvider var1, GifHeader var2, ByteBuffer var3) {
      this(var1, var2, var3, 1);
   }

   public StandardGifDecoder(GifDecoder.BitmapProvider var1, GifHeader var2, ByteBuffer var3, int var4) {
      this(var1);
      this.setData(var2, var3, var4);
   }

   private int averageColorsNear(int var1, int var2, int var3) {
      int var8 = 0;
      int var7 = 0;
      int var6 = 0;
      int var5 = 0;
      int var4 = 0;

      int var9;
      int var11;
      int var12;
      int var13;
      int var14;
      int var15;
      byte[] var16;
      int var18;
      for(var9 = var1; var9 < this.sampleSize + var1; var4 = var18) {
         var16 = this.mainPixels;
         if (var9 >= var16.length || var9 >= var2) {
            break;
         }

         byte var10 = var16[var9];
         var15 = this.act[var10 & 255];
         var14 = var8;
         var13 = var7;
         var12 = var6;
         var11 = var5;
         var18 = var4;
         if (var15 != 0) {
            var14 = var8 + (var15 >> 24 & 255);
            var13 = var7 + (var15 >> 16 & 255);
            var12 = var6 + (var15 >> 8 & 255);
            var11 = var5 + (var15 & 255);
            var18 = var4 + 1;
         }

         ++var9;
         var8 = var14;
         var7 = var13;
         var6 = var12;
         var5 = var11;
      }

      var9 = var1 + var3;
      var18 = var7;

      for(var12 = var8; var9 < var1 + var3 + this.sampleSize; var4 = var7) {
         var16 = this.mainPixels;
         if (var9 >= var16.length || var9 >= var2) {
            break;
         }

         byte var17 = var16[var9];
         var15 = this.act[var17 & 255];
         var14 = var12;
         var13 = var18;
         var11 = var6;
         var8 = var5;
         var7 = var4;
         if (var15 != 0) {
            var14 = var12 + (var15 >> 24 & 255);
            var13 = var18 + (var15 >> 16 & 255);
            var11 = var6 + (var15 >> 8 & 255);
            var8 = var5 + (var15 & 255);
            var7 = var4 + 1;
         }

         ++var9;
         var12 = var14;
         var18 = var13;
         var6 = var11;
         var5 = var8;
      }

      return var4 == 0 ? 0 : var12 / var4 << 24 | var18 / var4 << 16 | var6 / var4 << 8 | var5 / var4;
   }

   private void copyCopyIntoScratchRobust(GifFrame var1) {
      int[] var24 = this.mainScratch;
      int var10 = var1.field_140 / this.sampleSize;
      int var9 = var1.field_143 / this.sampleSize;
      int var8 = var1.field_141 / this.sampleSize;
      int var3 = var1.field_142 / this.sampleSize;
      int var4 = 0;
      boolean var7;
      if (this.framePointer == 0) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var17 = this.sampleSize;
      int var18 = this.downsampledWidth;
      int var19 = this.downsampledHeight;
      byte[] var25 = this.mainPixels;
      int[] var26 = this.act;
      int var2 = 1;
      Boolean var22 = this.isFirstFrameTransparent;
      byte var5 = 8;

      int var12;
      for(int var6 = 0; var6 < var10; var2 = var12) {
         int var13 = var6;
         int var11;
         if (var1.interlace) {
            if (var4 >= var10) {
               ++var2;
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 == 4) {
                        var4 = 1;
                        var5 = 2;
                     }
                  } else {
                     var4 = 2;
                     var5 = 4;
                  }
               } else {
                  var4 = 4;
               }
            }

            var13 = var4;
            var11 = var4 + var5;
            var12 = var2;
         } else {
            var12 = var2;
            var11 = var4;
         }

         var2 = var13 + var9;
         boolean var27;
         if (var17 == 1) {
            var27 = true;
         } else {
            var27 = false;
         }

         if (var2 >= var19) {
            var2 = var3;
         } else {
            int var15 = var2 * var18;
            var13 = var15 + var3;
            int var14 = var13 + var8;
            var2 = var14;
            if (var15 + var18 < var14) {
               var2 = var15 + var18;
            }

            var14 = var6 * var17 * var1.field_141;
            if (var27) {
               Boolean var23;
               for(var4 = var13; var4 < var2; var22 = var23) {
                  var13 = var26[var25[var14] & 255];
                  if (var13 != 0) {
                     var24[var4] = var13;
                     var23 = var22;
                  } else {
                     var23 = var22;
                     if (var7) {
                        var23 = var22;
                        if (var22 == null) {
                           var23 = true;
                        }
                     }
                  }

                  var14 += var17;
                  ++var4;
               }

               var2 = var3;
            } else {
               var15 = var3;
               int var16 = var13;
               var4 = var14;

               for(var3 = var2; var16 < var3; ++var16) {
                  int var20 = this.averageColorsNear(var4, (var2 - var13) * var17 + var14, var1.field_141);
                  if (var20 != 0) {
                     var24[var16] = var20;
                  } else if (var7 && var22 == null) {
                     var22 = true;
                  }

                  var4 += var17;
               }

               var2 = var15;
            }
         }

         ++var6;
         var3 = var2;
         var4 = var11;
      }

      if (this.isFirstFrameTransparent == null) {
         boolean var21;
         if (var22 == null) {
            var21 = false;
         } else {
            var21 = var22;
         }

         this.isFirstFrameTransparent = var21;
      }

   }

   private void copyIntoScratchFast(GifFrame var1) {
      int[] var18 = this.mainScratch;
      int var6 = var1.field_140;
      int var5 = var1.field_143;
      int var12 = var1.field_141;
      int var13 = var1.field_142;
      boolean var2;
      if (this.framePointer == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var14 = this.downsampledWidth;
      byte[] var17 = this.mainPixels;
      int[] var19 = this.act;
      byte var7 = -1;

      for(int var3 = 0; var3 < var6; ++var3) {
         int var10 = (var3 + var5) * var14;
         int var8 = var10 + var13;
         int var9 = var8 + var12;
         int var4 = var9;
         if (var10 + var14 < var9) {
            var4 = var10 + var14;
         }

         byte var21;
         for(var9 = var1.field_141 * var3; var8 < var4; var7 = var21) {
            byte var11 = var17[var9];
            int var15 = var11 & 255;
            var21 = var7;
            if (var15 != var7) {
               var10 = var19[var15];
               if (var10 != 0) {
                  var18[var8] = var10;
                  var21 = var7;
               } else {
                  var21 = var11;
               }
            }

            ++var9;
            ++var8;
         }
      }

      Boolean var20 = this.isFirstFrameTransparent;
      boolean var16;
      if ((var20 == null || !var20) && (this.isFirstFrameTransparent != null || !var2 || var7 == -1)) {
         var16 = false;
      } else {
         var16 = true;
      }

      this.isFirstFrameTransparent = var16;
   }

   private void decodeBitmapData(GifFrame var1) {
      if (var1 != null) {
         this.rawData.position(var1.bufferFrameStart);
      }

      int var2;
      int var3;
      if (var1 == null) {
         var2 = this.header.width;
         var3 = this.header.height;
      } else {
         var2 = var1.field_141;
         var3 = var1.field_140;
      }

      int var18 = var2 * var3;
      byte[] var26 = this.mainPixels;
      if (var26 == null || var26.length < var18) {
         this.mainPixels = this.bitmapProvider.obtainByteArray(var18);
      }

      byte[] var22 = this.mainPixels;
      if (this.prefix == null) {
         this.prefix = new short[4096];
      }

      short[] var23 = this.prefix;
      if (this.suffix == null) {
         this.suffix = new byte[4096];
      }

      byte[] var24 = this.suffix;
      if (this.pixelStack == null) {
         this.pixelStack = new byte[4097];
      }

      byte[] var25 = this.pixelStack;
      int var19 = this.readByte();
      int var20 = 1 << var19;
      int var8 = var20 + 2;
      int var12 = -1;
      int var9 = var19 + 1;
      int var13 = (1 << var9) - 1;
      var2 = 0;

      while(true) {
         int var10 = 0;
         if (var2 >= var20) {
            var26 = this.block;
            int var5 = 0;
            int var7 = 0;
            var2 = 0;
            int var6 = 0;
            int var4 = 0;
            int var11 = 0;
            var3 = 0;

            label95:
            while(var3 < var18) {
               if (var6 == 0) {
                  var6 = this.readBlock();
                  if (var6 <= 0) {
                     this.status = 3;
                     break;
                  }

                  var5 = 0;
               }

               var11 += (var26[var5] & 255) << var4;
               int var15 = var5 + 1;
               int var16 = var6 - 1;
               var5 = var4 + 8;
               var4 = var2;
               var6 = var7;
               var7 = var13;
               var2 = var12;
               var12 = var5;

               while(true) {
                  int var14;
                  while(var12 >= var9) {
                     var5 = var11 & var7;
                     var11 >>= var9;
                     var12 -= var9;
                     if (var5 == var20) {
                        var9 = var19 + 1;
                        var7 = (1 << var9) - 1;
                        var8 = var20 + 2;
                        var2 = -1;
                     } else {
                        if (var5 == var20 + 1) {
                           var14 = var12;
                           var12 = var2;
                           var13 = var7;
                           var5 = var15;
                           var7 = var6;
                           var2 = var4;
                           var6 = var16;
                           var4 = var14;
                           continue label95;
                        }

                        if (var2 == -1) {
                           var22[var10] = var24[var5];
                           ++var10;
                           ++var3;
                           var2 = var5;
                           var4 = var5;
                        } else {
                           if (var5 >= var8) {
                              var25[var6] = (byte)var4;
                              var4 = var6 + 1;
                              var6 = var2;
                           } else {
                              var4 = var6;
                              var6 = var5;
                           }

                           while(var6 >= var20) {
                              var25[var4] = var24[var6];
                              ++var4;
                              var6 = var23[var6];
                           }

                           int var17 = var24[var6] & 255;
                           var22[var10] = (byte)var17;
                           ++var10;
                           ++var3;

                           while(var4 > 0) {
                              --var4;
                              var22[var10] = var25[var4];
                              ++var10;
                              ++var3;
                           }

                           var6 = var8;
                           var13 = var9;
                           var14 = var7;
                           if (var8 < 4096) {
                              var23[var8] = (short)var2;
                              var24[var8] = (byte)var17;
                              var2 = var8 + 1;
                              if ((var2 & var7) == 0) {
                                 var6 = var2;
                                 var13 = var9;
                                 var14 = var7;
                                 if (var2 < 4096) {
                                    var13 = var9 + 1;
                                    var14 = var7 + var2;
                                    var6 = var2;
                                 }
                              } else {
                                 var14 = var7;
                                 var13 = var9;
                                 var6 = var2;
                              }
                           }

                           var2 = var5;
                           var8 = var6;
                           var9 = var13;
                           var7 = var14;
                           var6 = var4;
                           var4 = var17;
                        }
                     }
                  }

                  var14 = var12;
                  var12 = var2;
                  var13 = var7;
                  var5 = var15;
                  var7 = var6;
                  var2 = var4;
                  var6 = var16;
                  var4 = var14;
                  break;
               }
            }

            Arrays.fill(var22, var10, var18, (byte)0);
            return;
         }

         var23[var2] = 0;
         var24[var2] = (byte)var2;
         ++var2;
      }
   }

   private GifHeaderParser getHeaderParser() {
      if (this.parser == null) {
         this.parser = new GifHeaderParser();
      }

      return this.parser;
   }

   private Bitmap getNextBitmap() {
      Boolean var1 = this.isFirstFrameTransparent;
      Config var2;
      if (var1 != null && !var1) {
         var2 = this.bitmapConfig;
      } else {
         var2 = Config.ARGB_8888;
      }

      Bitmap var3 = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, var2);
      var3.setHasAlpha(true);
      return var3;
   }

   private int readBlock() {
      int var1 = this.readByte();
      if (var1 <= 0) {
         return var1;
      } else {
         ByteBuffer var2 = this.rawData;
         var2.get(this.block, 0, Math.min(var1, var2.remaining()));
         return var1;
      }
   }

   private int readByte() {
      return this.rawData.get() & 255;
   }

   private Bitmap setPixels(GifFrame var1, GifFrame var2) {
      int[] var10 = this.mainScratch;
      if (var2 == null) {
         Bitmap var11 = this.previousImage;
         if (var11 != null) {
            this.bitmapProvider.release(var11);
         }

         this.previousImage = null;
         Arrays.fill(var10, 0);
      }

      if (var2 != null && var2.dispose == 3 && this.previousImage == null) {
         Arrays.fill(var10, 0);
      }

      int var3;
      if (var2 != null && var2.dispose > 0) {
         if (var2.dispose == 2) {
            var3 = 0;
            int var4;
            if (!var1.transparency) {
               var4 = this.header.bgColor;
               var3 = var4;
               if (var1.lct != null) {
                  var3 = var4;
                  if (this.header.bgIndex == var1.transIndex) {
                     var3 = 0;
                  }
               }
            }

            int var7 = var2.field_140 / this.sampleSize;
            var4 = var2.field_143 / this.sampleSize;
            int var8 = var2.field_141 / this.sampleSize;
            int var5 = var2.field_142 / this.sampleSize;
            int var9 = this.downsampledWidth;
            int var6 = var4 * var9 + var5;

            for(var4 = var6; var4 < var9 * var7 + var6; var4 += this.downsampledWidth) {
               for(var5 = var4; var5 < var4 + var8; ++var5) {
                  var10[var5] = var3;
               }
            }
         } else if (var2.dispose == 3) {
            Bitmap var13 = this.previousImage;
            if (var13 != null) {
               var3 = this.downsampledWidth;
               var13.getPixels(var10, 0, var3, 0, 0, var3, this.downsampledHeight);
            }
         }
      }

      this.decodeBitmapData(var1);
      if (!var1.interlace && this.sampleSize == 1) {
         this.copyIntoScratchFast(var1);
      } else {
         this.copyCopyIntoScratchRobust(var1);
      }

      Bitmap var12;
      if (this.savePrevious && (var1.dispose == 0 || var1.dispose == 1)) {
         if (this.previousImage == null) {
            this.previousImage = this.getNextBitmap();
         }

         var12 = this.previousImage;
         var3 = this.downsampledWidth;
         var12.setPixels(var10, 0, var3, 0, 0, var3, this.downsampledHeight);
      }

      var12 = this.getNextBitmap();
      var3 = this.downsampledWidth;
      var12.setPixels(var10, 0, var3, 0, 0, var3, this.downsampledHeight);
      return var12;
   }

   public void advance() {
      this.framePointer = (this.framePointer + 1) % this.header.frameCount;
   }

   public void clear() {
      this.header = null;
      byte[] var1 = this.mainPixels;
      if (var1 != null) {
         this.bitmapProvider.release(var1);
      }

      int[] var2 = this.mainScratch;
      if (var2 != null) {
         this.bitmapProvider.release(var2);
      }

      Bitmap var3 = this.previousImage;
      if (var3 != null) {
         this.bitmapProvider.release(var3);
      }

      this.previousImage = null;
      this.rawData = null;
      this.isFirstFrameTransparent = null;
      var1 = this.block;
      if (var1 != null) {
         this.bitmapProvider.release(var1);
      }

   }

   public int getByteSize() {
      return this.rawData.limit() + this.mainPixels.length + this.mainScratch.length * 4;
   }

   public int getCurrentFrameIndex() {
      return this.framePointer;
   }

   public ByteBuffer getData() {
      return this.rawData;
   }

   public int getDelay(int var1) {
      byte var3 = -1;
      int var2 = var3;
      if (var1 >= 0) {
         var2 = var3;
         if (var1 < this.header.frameCount) {
            var2 = ((GifFrame)this.header.frames.get(var1)).delay;
         }
      }

      return var2;
   }

   public int getFrameCount() {
      return this.header.frameCount;
   }

   public int getHeight() {
      return this.header.height;
   }

   @Deprecated
   public int getLoopCount() {
      return this.header.loopCount == -1 ? 1 : this.header.loopCount;
   }

   public int getNetscapeLoopCount() {
      return this.header.loopCount;
   }

   public int getNextDelay() {
      if (this.header.frameCount > 0) {
         int var1 = this.framePointer;
         if (var1 >= 0) {
            return this.getDelay(var1);
         }
      }

      return 0;
   }

   public Bitmap getNextFrame() {
      synchronized(this){}

      Throwable var10000;
      label2494: {
         String var2;
         StringBuilder var3;
         boolean var10001;
         label2490: {
            try {
               if (this.header.frameCount > 0 && this.framePointer >= 0) {
                  break label2490;
               }
            } catch (Throwable var275) {
               var10000 = var275;
               var10001 = false;
               break label2494;
            }

            try {
               if (Log.isLoggable(TAG, 3)) {
                  var2 = TAG;
                  var3 = new StringBuilder();
                  var3.append("Unable to decode frame, frameCount=");
                  var3.append(this.header.frameCount);
                  var3.append(", framePointer=");
                  var3.append(this.framePointer);
                  Log.d(var2, var3.toString());
               }
            } catch (Throwable var276) {
               var10000 = var276;
               var10001 = false;
               break label2494;
            }

            try {
               this.status = 1;
            } catch (Throwable var274) {
               var10000 = var274;
               var10001 = false;
               break label2494;
            }
         }

         label2495: {
            try {
               if (this.status != 1 && this.status != 2) {
                  break label2495;
               }
            } catch (Throwable var273) {
               var10000 = var273;
               var10001 = false;
               break label2494;
            }

            try {
               if (Log.isLoggable(TAG, 3)) {
                  var2 = TAG;
                  var3 = new StringBuilder();
                  var3.append("Unable to decode frame, status=");
                  var3.append(this.status);
                  Log.d(var2, var3.toString());
               }
            } catch (Throwable var272) {
               var10000 = var272;
               var10001 = false;
               break label2494;
            }

            return null;
         }

         try {
            this.status = 0;
            if (this.block == null) {
               this.block = this.bitmapProvider.obtainByteArray(255);
            }
         } catch (Throwable var271) {
            var10000 = var271;
            var10001 = false;
            break label2494;
         }

         GifFrame var4;
         try {
            var4 = (GifFrame)this.header.frames.get(this.framePointer);
         } catch (Throwable var270) {
            var10000 = var270;
            var10001 = false;
            break label2494;
         }

         GifFrame var277 = null;

         int var1;
         try {
            var1 = this.framePointer - 1;
         } catch (Throwable var269) {
            var10000 = var269;
            var10001 = false;
            break label2494;
         }

         if (var1 >= 0) {
            try {
               var277 = (GifFrame)this.header.frames.get(var1);
            } catch (Throwable var268) {
               var10000 = var268;
               var10001 = false;
               break label2494;
            }
         }

         int[] var279;
         label2442: {
            try {
               if (var4.lct != null) {
                  var279 = var4.lct;
                  break label2442;
               }
            } catch (Throwable var267) {
               var10000 = var267;
               var10001 = false;
               break label2494;
            }

            try {
               var279 = this.header.gct;
            } catch (Throwable var266) {
               var10000 = var266;
               var10001 = false;
               break label2494;
            }
         }

         try {
            this.act = var279;
         } catch (Throwable var265) {
            var10000 = var265;
            var10001 = false;
            break label2494;
         }

         if (var279 == null) {
            label2492: {
               try {
                  if (Log.isLoggable(TAG, 3)) {
                     var2 = TAG;
                     var3 = new StringBuilder();
                     var3.append("No valid color table found for frame #");
                     var3.append(this.framePointer);
                     Log.d(var2, var3.toString());
                  }
               } catch (Throwable var262) {
                  var10000 = var262;
                  var10001 = false;
                  break label2492;
               }

               try {
                  this.status = 1;
               } catch (Throwable var261) {
                  var10000 = var261;
                  var10001 = false;
                  break label2492;
               }

               return null;
            }
         } else {
            label2493: {
               try {
                  if (var4.transparency) {
                     System.arraycopy(this.act, 0, this.pct, 0, this.act.length);
                     var279 = this.pct;
                     this.act = var279;
                     var279[var4.transIndex] = 0;
                     if (var4.dispose == 2 && this.framePointer == 0) {
                        this.isFirstFrameTransparent = true;
                     }
                  }
               } catch (Throwable var264) {
                  var10000 = var264;
                  var10001 = false;
                  break label2493;
               }

               Bitmap var280;
               try {
                  var280 = this.setPixels(var4, var277);
               } catch (Throwable var263) {
                  var10000 = var263;
                  var10001 = false;
                  break label2493;
               }

               return var280;
            }
         }
      }

      Throwable var278 = var10000;
      throw var278;
   }

   public int getStatus() {
      return this.status;
   }

   public int getTotalIterationCount() {
      if (this.header.loopCount == -1) {
         return 1;
      } else {
         return this.header.loopCount == 0 ? 0 : this.header.loopCount + 1;
      }
   }

   public int getWidth() {
      return this.header.width;
   }

   public int read(InputStream var1, int var2) {
      if (var1 != null) {
         label54: {
            if (var2 > 0) {
               var2 += 4096;
            } else {
               var2 = 16384;
            }

            IOException var10000;
            label48: {
               ByteArrayOutputStream var3;
               byte[] var4;
               boolean var10001;
               try {
                  var3 = new ByteArrayOutputStream(var2);
                  var4 = new byte[16384];
               } catch (IOException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label48;
               }

               while(true) {
                  try {
                     var2 = var1.read(var4, 0, var4.length);
                  } catch (IOException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }

                  if (var2 == -1) {
                     try {
                        var3.flush();
                        this.read(var3.toByteArray());
                        break label54;
                     } catch (IOException var6) {
                        var10000 = var6;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     var3.write(var4, 0, var2);
                  } catch (IOException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }
               }
            }

            IOException var10 = var10000;
            Log.w(TAG, "Error reading data from stream", var10);
         }
      } else {
         this.status = 2;
      }

      if (var1 != null) {
         try {
            var1.close();
         } catch (IOException var5) {
            Log.w(TAG, "Error closing stream", var5);
         }
      }

      return this.status;
   }

   public int read(byte[] var1) {
      synchronized(this){}

      Throwable var10000;
      label116: {
         boolean var10001;
         GifHeader var3;
         try {
            var3 = this.getHeaderParser().setData(var1).parseHeader();
            this.header = var3;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label116;
         }

         if (var1 != null) {
            try {
               this.setData(var3, var1);
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label116;
            }
         }

         label104:
         try {
            int var2 = this.status;
            return var2;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label104;
         }
      }

      Throwable var16 = var10000;
      throw var16;
   }

   public void resetFrameIndex() {
      this.framePointer = -1;
   }

   public void setData(GifHeader var1, ByteBuffer var2) {
      synchronized(this){}

      try {
         this.setData(var1, var2, 1);
      } finally {
         ;
      }

   }

   public void setData(GifHeader var1, ByteBuffer var2, int var3) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var3 > 0) {
         label217: {
            Iterator var26;
            try {
               var3 = Integer.highestOneBit(var3);
               this.status = 0;
               this.header = var1;
               this.framePointer = -1;
               var2 = var2.asReadOnlyBuffer();
               this.rawData = var2;
               var2.position(0);
               this.rawData.order(ByteOrder.LITTLE_ENDIAN);
               this.savePrevious = false;
               var26 = var1.frames.iterator();
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label217;
            }

            while(true) {
               try {
                  if (!var26.hasNext()) {
                     break;
                  }

                  if (((GifFrame)var26.next()).dispose == 3) {
                     this.savePrevious = true;
                     break;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label217;
               }
            }

            try {
               this.sampleSize = var3;
               this.downsampledWidth = var1.width / var3;
               this.downsampledHeight = var1.height / var3;
               this.mainPixels = this.bitmapProvider.obtainByteArray(var1.width * var1.height);
               this.mainScratch = this.bitmapProvider.obtainIntArray(this.downsampledWidth * this.downsampledHeight);
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label217;
            }

            return;
         }
      } else {
         label212:
         try {
            StringBuilder var25 = new StringBuilder();
            var25.append("Sample size must be >=0, not: ");
            var25.append(var3);
            throw new IllegalArgumentException(var25.toString());
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label212;
         }
      }

      Throwable var24 = var10000;
      throw var24;
   }

   public void setData(GifHeader var1, byte[] var2) {
      synchronized(this){}

      try {
         this.setData(var1, ByteBuffer.wrap(var2));
      } finally {
         ;
      }

   }

   public void setDefaultBitmapConfig(Config var1) {
      if (var1 != Config.ARGB_8888 && var1 != Config.RGB_565) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unsupported format: ");
         var2.append(var1);
         var2.append(", must be one of ");
         var2.append(Config.ARGB_8888);
         var2.append(" or ");
         var2.append(Config.RGB_565);
         throw new IllegalArgumentException(var2.toString());
      } else {
         this.bitmapConfig = var1;
      }
   }
}
