package com.bumptech.glide.gifdecoder;

import android.util.Log;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class GifHeaderParser {
   static final int DEFAULT_FRAME_DELAY = 10;
   private static final int DESCRIPTOR_MASK_INTERLACE_FLAG = 64;
   private static final int DESCRIPTOR_MASK_LCT_FLAG = 128;
   private static final int DESCRIPTOR_MASK_LCT_SIZE = 7;
   private static final int EXTENSION_INTRODUCER = 33;
   private static final int GCE_DISPOSAL_METHOD_SHIFT = 2;
   private static final int GCE_MASK_DISPOSAL_METHOD = 28;
   private static final int GCE_MASK_TRANSPARENT_COLOR_FLAG = 1;
   private static final int IMAGE_SEPARATOR = 44;
   private static final int LABEL_APPLICATION_EXTENSION = 255;
   private static final int LABEL_COMMENT_EXTENSION = 254;
   private static final int LABEL_GRAPHIC_CONTROL_EXTENSION = 249;
   private static final int LABEL_PLAIN_TEXT_EXTENSION = 1;
   private static final int LSD_MASK_GCT_FLAG = 128;
   private static final int LSD_MASK_GCT_SIZE = 7;
   private static final int MASK_INT_LOWEST_BYTE = 255;
   private static final int MAX_BLOCK_SIZE = 256;
   static final int MIN_FRAME_DELAY = 2;
   private static final String TAG = "GifHeaderParser";
   private static final int TRAILER = 59;
   private final byte[] block = new byte[256];
   private int blockSize = 0;
   private GifHeader header;
   private ByteBuffer rawData;

   private boolean err() {
      return this.header.status != 0;
   }

   private int read() {
      byte var1;
      try {
         var1 = this.rawData.get();
      } catch (Exception var3) {
         this.header.status = 1;
         return 0;
      }

      return var1 & 255;
   }

   private void readBitmap() {
      this.header.currentFrame.field_142 = this.readShort();
      this.header.currentFrame.field_143 = this.readShort();
      this.header.currentFrame.field_141 = this.readShort();
      this.header.currentFrame.field_140 = this.readShort();
      int var2 = this.read();
      boolean var4 = false;
      boolean var1;
      if ((var2 & 128) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      int var3 = (int)Math.pow(2.0D, (double)((var2 & 7) + 1));
      GifFrame var5 = this.header.currentFrame;
      if ((var2 & 64) != 0) {
         var4 = true;
      }

      var5.interlace = var4;
      if (var1) {
         this.header.currentFrame.lct = this.readColorTable(var3);
      } else {
         this.header.currentFrame.lct = null;
      }

      this.header.currentFrame.bufferFrameStart = this.rawData.position();
      this.skipImageData();
      if (!this.err()) {
         GifHeader var6 = this.header;
         ++var6.frameCount;
         this.header.frames.add(this.header.currentFrame);
      }
   }

   private void readBlock() {
      int var1 = this.read();
      this.blockSize = var1;
      int var2 = 0;
      if (var1 > 0) {
         var1 = 0;

         Exception var10000;
         int var3;
         while(true) {
            var3 = var1;

            boolean var10001;
            try {
               if (var2 >= this.blockSize) {
                  return;
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }

            var3 = var1;

            try {
               var1 = this.blockSize - var2;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break;
            }

            var3 = var1;

            try {
               this.rawData.get(this.block, var2, var1);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }

            var2 += var1;
         }

         Exception var4 = var10000;
         if (Log.isLoggable("GifHeaderParser", 3)) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Error Reading Block n: ");
            var5.append(var2);
            var5.append(" count: ");
            var5.append(var3);
            var5.append(" blockSize: ");
            var5.append(this.blockSize);
            Log.d("GifHeaderParser", var5.toString(), var4);
         }

         this.header.status = 1;
      }

   }

   private int[] readColorTable(int var1) {
      byte[] var6 = new byte[var1 * 3];

      int[] var7;
      try {
         this.rawData.get(var6);
         var7 = new int[256];
      } catch (BufferUnderflowException var8) {
         if (Log.isLoggable("GifHeaderParser", 3)) {
            Log.d("GifHeaderParser", "Format Error Reading Color Table", var8);
         }

         this.header.status = 1;
         return null;
      }

      int var2 = 0;

      for(int var3 = 0; var2 < var1; ++var2) {
         int var4 = var3 + 1;
         byte var9 = var6[var3];
         int var5 = var4 + 1;
         var7[var2] = -16777216 | (var9 & 255) << 16 | (var6[var4] & 255) << 8 | var6[var5] & 255;
         var3 = var5 + 1;
      }

      return var7;
   }

   private void readContents() {
      this.readContents(Integer.MAX_VALUE);
   }

   private void readContents(int var1) {
      boolean var2 = false;

      while(!var2 && !this.err() && this.header.frameCount <= var1) {
         int var3 = this.read();
         if (var3 != 33) {
            if (var3 != 44) {
               if (var3 != 59) {
                  this.header.status = 1;
               } else {
                  var2 = true;
               }
            } else {
               if (this.header.currentFrame == null) {
                  this.header.currentFrame = new GifFrame();
               }

               this.readBitmap();
            }
         } else {
            var3 = this.read();
            if (var3 == 1) {
               this.skip();
            } else if (var3 == 249) {
               this.header.currentFrame = new GifFrame();
               this.readGraphicControlExt();
            } else if (var3 == 254) {
               this.skip();
            } else if (var3 != 255) {
               this.skip();
            } else {
               this.readBlock();
               StringBuilder var4 = new StringBuilder();

               for(var3 = 0; var3 < 11; ++var3) {
                  var4.append((char)this.block[var3]);
               }

               if (var4.toString().equals("NETSCAPE2.0")) {
                  this.readNetscapeExt();
               } else {
                  this.skip();
               }
            }
         }
      }

   }

   private void readGraphicControlExt() {
      this.read();
      int var1 = this.read();
      this.header.currentFrame.dispose = (var1 & 28) >> 2;
      int var2 = this.header.currentFrame.dispose;
      boolean var3 = true;
      if (var2 == 0) {
         this.header.currentFrame.dispose = 1;
      }

      GifFrame var4 = this.header.currentFrame;
      if ((var1 & 1) == 0) {
         var3 = false;
      }

      var4.transparency = var3;
      var2 = this.readShort();
      var1 = var2;
      if (var2 < 2) {
         var1 = 10;
      }

      this.header.currentFrame.delay = var1 * 10;
      this.header.currentFrame.transIndex = this.read();
      this.read();
   }

   private void readHeader() {
      StringBuilder var2 = new StringBuilder();

      for(int var1 = 0; var1 < 6; ++var1) {
         var2.append((char)this.read());
      }

      if (!var2.toString().startsWith("GIF")) {
         this.header.status = 1;
      } else {
         this.readLSD();
         if (this.header.gctFlag && !this.err()) {
            GifHeader var3 = this.header;
            var3.gct = this.readColorTable(var3.gctSize);
            var3 = this.header;
            var3.bgColor = var3.gct[this.header.bgIndex];
         }

      }
   }

   private void readLSD() {
      this.header.width = this.readShort();
      this.header.height = this.readShort();
      int var1 = this.read();
      GifHeader var3 = this.header;
      boolean var2;
      if ((var1 & 128) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      var3.gctFlag = var2;
      this.header.gctSize = (int)Math.pow(2.0D, (double)((var1 & 7) + 1));
      this.header.bgIndex = this.read();
      this.header.pixelAspect = this.read();
   }

   private void readNetscapeExt() {
      do {
         this.readBlock();
         byte[] var3 = this.block;
         if (var3[0] == 1) {
            byte var1 = var3[1];
            byte var2 = var3[2];
            this.header.loopCount = (var2 & 255) << 8 | var1 & 255;
         }
      } while(this.blockSize > 0 && !this.err());

   }

   private int readShort() {
      return this.rawData.getShort();
   }

   private void reset() {
      this.rawData = null;
      Arrays.fill(this.block, (byte)0);
      this.header = new GifHeader();
      this.blockSize = 0;
   }

   private void skip() {
      int var1;
      do {
         var1 = this.read();
         int var2 = Math.min(this.rawData.position() + var1, this.rawData.limit());
         this.rawData.position(var2);
      } while(var1 > 0);

   }

   private void skipImageData() {
      this.read();
      this.skip();
   }

   public void clear() {
      this.rawData = null;
      this.header = null;
   }

   public boolean isAnimated() {
      this.readHeader();
      if (!this.err()) {
         this.readContents(2);
      }

      return this.header.frameCount > 1;
   }

   public GifHeader parseHeader() {
      if (this.rawData != null) {
         if (this.err()) {
            return this.header;
         } else {
            this.readHeader();
            if (!this.err()) {
               this.readContents();
               if (this.header.frameCount < 0) {
                  this.header.status = 1;
               }
            }

            return this.header;
         }
      } else {
         throw new IllegalStateException("You must call setData() before parseHeader()");
      }
   }

   public GifHeaderParser setData(ByteBuffer var1) {
      this.reset();
      var1 = var1.asReadOnlyBuffer();
      this.rawData = var1;
      var1.position(0);
      this.rawData.order(ByteOrder.LITTLE_ENDIAN);
      return this;
   }

   public GifHeaderParser setData(byte[] var1) {
      if (var1 != null) {
         this.setData(ByteBuffer.wrap(var1));
         return this;
      } else {
         this.rawData = null;
         this.header.status = 2;
         return this;
      }
   }
}
