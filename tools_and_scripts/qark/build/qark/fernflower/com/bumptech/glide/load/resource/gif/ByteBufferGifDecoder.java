package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Queue;

public class ByteBufferGifDecoder implements ResourceDecoder {
   private static final ByteBufferGifDecoder.GifDecoderFactory GIF_DECODER_FACTORY = new ByteBufferGifDecoder.GifDecoderFactory();
   private static final ByteBufferGifDecoder.GifHeaderParserPool PARSER_POOL = new ByteBufferGifDecoder.GifHeaderParserPool();
   private static final String TAG = "BufferGifDecoder";
   private final Context context;
   private final ByteBufferGifDecoder.GifDecoderFactory gifDecoderFactory;
   private final ByteBufferGifDecoder.GifHeaderParserPool parserPool;
   private final List parsers;
   private final GifBitmapProvider provider;

   public ByteBufferGifDecoder(Context var1) {
      this(var1, Glide.get(var1).getRegistry().getImageHeaderParsers(), Glide.get(var1).getBitmapPool(), Glide.get(var1).getArrayPool());
   }

   public ByteBufferGifDecoder(Context var1, List var2, BitmapPool var3, ArrayPool var4) {
      this(var1, var2, var3, var4, PARSER_POOL, GIF_DECODER_FACTORY);
   }

   ByteBufferGifDecoder(Context var1, List var2, BitmapPool var3, ArrayPool var4, ByteBufferGifDecoder.GifHeaderParserPool var5, ByteBufferGifDecoder.GifDecoderFactory var6) {
      this.context = var1.getApplicationContext();
      this.parsers = var2;
      this.gifDecoderFactory = var6;
      this.provider = new GifBitmapProvider(var3, var4);
      this.parserPool = var5;
   }

   private GifDrawableResource decode(ByteBuffer var1, int var2, int var3, GifHeaderParser var4, Options var5) {
      long var7 = LogTime.getLogTime();

      StringBuilder var47;
      Throwable var10000;
      label433: {
         StringBuilder var40;
         GifHeader var9;
         boolean var10001;
         label432: {
            try {
               var9 = var4.parseHeader();
               if (var9.getNumFrames() > 0 && var9.getStatus() == 0) {
                  break label432;
               }
            } catch (Throwable var39) {
               var10000 = var39;
               var10001 = false;
               break label433;
            }

            if (Log.isLoggable("BufferGifDecoder", 2)) {
               var40 = new StringBuilder();
               var40.append("Decoded GIF from stream in ");
               var40.append(LogTime.getElapsedMillis(var7));
               Log.v("BufferGifDecoder", var40.toString());
            }

            return null;
         }

         Config var44;
         label416: {
            try {
               if (var5.get(GifOptions.DECODE_FORMAT) == DecodeFormat.PREFER_RGB_565) {
                  var44 = Config.RGB_565;
                  break label416;
               }
            } catch (Throwable var38) {
               var10000 = var38;
               var10001 = false;
               break label433;
            }

            try {
               var44 = Config.ARGB_8888;
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label433;
            }
         }

         GifDecoder var41;
         Bitmap var45;
         try {
            int var6 = getSampleSize(var9, var2, var3);
            var41 = this.gifDecoderFactory.build(this.provider, var9, var1, var6);
            var41.setDefaultBitmapConfig(var44);
            var41.advance();
            var45 = var41.getNextFrame();
         } catch (Throwable var36) {
            var10000 = var36;
            var10001 = false;
            break label433;
         }

         if (var45 == null) {
            if (Log.isLoggable("BufferGifDecoder", 2)) {
               var40 = new StringBuilder();
               var40.append("Decoded GIF from stream in ");
               var40.append(LogTime.getElapsedMillis(var7));
               Log.v("BufferGifDecoder", var40.toString());
            }

            return null;
         }

         GifDrawableResource var43;
         try {
            UnitTransformation var46 = UnitTransformation.get();
            var43 = new GifDrawableResource(new GifDrawable(this.context, var41, var46, var2, var3, var45));
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label433;
         }

         if (Log.isLoggable("BufferGifDecoder", 2)) {
            var47 = new StringBuilder();
            var47.append("Decoded GIF from stream in ");
            var47.append(LogTime.getElapsedMillis(var7));
            Log.v("BufferGifDecoder", var47.toString());
         }

         return var43;
      }

      Throwable var42 = var10000;
      if (Log.isLoggable("BufferGifDecoder", 2)) {
         var47 = new StringBuilder();
         var47.append("Decoded GIF from stream in ");
         var47.append(LogTime.getElapsedMillis(var7));
         Log.v("BufferGifDecoder", var47.toString());
      }

      throw var42;
   }

   private static int getSampleSize(GifHeader var0, int var1, int var2) {
      int var3 = Math.min(var0.getHeight() / var2, var0.getWidth() / var1);
      if (var3 == 0) {
         var3 = 0;
      } else {
         var3 = Integer.highestOneBit(var3);
      }

      var3 = Math.max(1, var3);
      if (Log.isLoggable("BufferGifDecoder", 2) && var3 > 1) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Downsampling GIF, sampleSize: ");
         var4.append(var3);
         var4.append(", target dimens: [");
         var4.append(var1);
         var4.append("x");
         var4.append(var2);
         var4.append("], actual dimens: [");
         var4.append(var0.getWidth());
         var4.append("x");
         var4.append(var0.getHeight());
         var4.append("]");
         Log.v("BufferGifDecoder", var4.toString());
      }

      return var3;
   }

   public GifDrawableResource decode(ByteBuffer var1, int var2, int var3, Options var4) {
      GifHeaderParser var5 = this.parserPool.obtain(var1);

      GifDrawableResource var8;
      try {
         var8 = this.decode(var1, var2, var3, var5, var4);
      } finally {
         this.parserPool.release(var5);
      }

      return var8;
   }

   public boolean handles(ByteBuffer var1, Options var2) throws IOException {
      return !(Boolean)var2.get(GifOptions.DISABLE_ANIMATION) && ImageHeaderParserUtils.getType(this.parsers, var1) == ImageHeaderParser.ImageType.GIF;
   }

   static class GifDecoderFactory {
      GifDecoder build(GifDecoder.BitmapProvider var1, GifHeader var2, ByteBuffer var3, int var4) {
         return new StandardGifDecoder(var1, var2, var3, var4);
      }
   }

   static class GifHeaderParserPool {
      private final Queue pool = Util.createQueue(0);

      GifHeaderParser obtain(ByteBuffer var1) {
         synchronized(this){}

         Throwable var10000;
         label116: {
            boolean var10001;
            GifHeaderParser var3;
            try {
               var3 = (GifHeaderParser)this.pool.poll();
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label116;
            }

            GifHeaderParser var2 = var3;
            if (var3 == null) {
               try {
                  var2 = new GifHeaderParser();
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label116;
               }
            }

            label104:
            try {
               GifHeaderParser var17 = var2.setData(var1);
               return var17;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label104;
            }
         }

         Throwable var16 = var10000;
         throw var16;
      }

      void release(GifHeaderParser var1) {
         synchronized(this){}

         try {
            var1.clear();
            this.pool.offer(var1);
         } finally {
            ;
         }

      }
   }
}
