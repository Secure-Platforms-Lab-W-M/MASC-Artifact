package com.bumptech.glide.load.resource.bitmap;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class VideoDecoder implements ResourceDecoder {
   private static final VideoDecoder.MediaMetadataRetrieverFactory DEFAULT_FACTORY = new VideoDecoder.MediaMetadataRetrieverFactory();
   public static final long DEFAULT_FRAME = -1L;
   static final int DEFAULT_FRAME_OPTION = 2;
   public static final Option FRAME_OPTION = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", 2, new Option.CacheKeyUpdater() {
      private final ByteBuffer buffer = ByteBuffer.allocate(4);

      public void update(byte[] param1, Integer param2, MessageDigest param3) {
         // $FF: Couldn't be decompiled
      }
   });
   private static final String TAG = "VideoDecoder";
   public static final Option TARGET_FRAME = Option.disk("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", -1L, new Option.CacheKeyUpdater() {
      private final ByteBuffer buffer = ByteBuffer.allocate(8);

      public void update(byte[] param1, Long param2, MessageDigest param3) {
         // $FF: Couldn't be decompiled
      }
   });
   private final BitmapPool bitmapPool;
   private final VideoDecoder.MediaMetadataRetrieverFactory factory;
   private final VideoDecoder.MediaMetadataRetrieverInitializer initializer;

   VideoDecoder(BitmapPool var1, VideoDecoder.MediaMetadataRetrieverInitializer var2) {
      this(var1, var2, DEFAULT_FACTORY);
   }

   VideoDecoder(BitmapPool var1, VideoDecoder.MediaMetadataRetrieverInitializer var2, VideoDecoder.MediaMetadataRetrieverFactory var3) {
      this.bitmapPool = var1;
      this.initializer = var2;
      this.factory = var3;
   }

   public static ResourceDecoder asset(BitmapPool var0) {
      return new VideoDecoder(var0, new VideoDecoder.AssetFileDescriptorInitializer());
   }

   public static ResourceDecoder byteBuffer(BitmapPool var0) {
      return new VideoDecoder(var0, new VideoDecoder.ByteBufferInitializer());
   }

   private static Bitmap decodeFrame(MediaMetadataRetriever var0, long var1, int var3, int var4, int var5, DownsampleStrategy var6) {
      Object var8 = null;
      Bitmap var7 = (Bitmap)var8;
      if (VERSION.SDK_INT >= 27) {
         var7 = (Bitmap)var8;
         if (var4 != Integer.MIN_VALUE) {
            var7 = (Bitmap)var8;
            if (var5 != Integer.MIN_VALUE) {
               var7 = (Bitmap)var8;
               if (var6 != DownsampleStrategy.NONE) {
                  var7 = decodeScaledFrame(var0, var1, var3, var4, var5, var6);
               }
            }
         }
      }

      Bitmap var9 = var7;
      if (var7 == null) {
         var9 = decodeOriginalFrame(var0, var1, var3);
      }

      return var9;
   }

   private static Bitmap decodeOriginalFrame(MediaMetadataRetriever var0, long var1, int var3) {
      return var0.getFrameAtTime(var1, var3);
   }

   private static Bitmap decodeScaledFrame(MediaMetadataRetriever param0, long param1, int param3, int param4, int param5, DownsampleStrategy param6) {
      // $FF: Couldn't be decompiled
   }

   public static ResourceDecoder parcel(BitmapPool var0) {
      return new VideoDecoder(var0, new VideoDecoder.ParcelFileDescriptorInitializer());
   }

   public Resource decode(Object param1, int param2, int param3, Options param4) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public boolean handles(Object var1, Options var2) {
      return true;
   }

   private static final class AssetFileDescriptorInitializer implements VideoDecoder.MediaMetadataRetrieverInitializer {
      private AssetFileDescriptorInitializer() {
      }

      // $FF: synthetic method
      AssetFileDescriptorInitializer(Object var1) {
         this();
      }

      public void initialize(MediaMetadataRetriever var1, AssetFileDescriptor var2) {
         var1.setDataSource(var2.getFileDescriptor(), var2.getStartOffset(), var2.getLength());
      }
   }

   static final class ByteBufferInitializer implements VideoDecoder.MediaMetadataRetrieverInitializer {
      public void initialize(MediaMetadataRetriever var1, final ByteBuffer var2) {
         var1.setDataSource(new MediaDataSource() {
            public void close() {
            }

            public long getSize() {
               return (long)var2.limit();
            }

            public int readAt(long var1, byte[] var3, int var4, int var5) {
               if (var1 >= (long)var2.limit()) {
                  return -1;
               } else {
                  var2.position((int)var1);
                  var5 = Math.min(var5, var2.remaining());
                  var2.get(var3, var4, var5);
                  return var5;
               }
            }
         });
      }
   }

   static class MediaMetadataRetrieverFactory {
      public MediaMetadataRetriever build() {
         return new MediaMetadataRetriever();
      }
   }

   interface MediaMetadataRetrieverInitializer {
      void initialize(MediaMetadataRetriever var1, Object var2);
   }

   static final class ParcelFileDescriptorInitializer implements VideoDecoder.MediaMetadataRetrieverInitializer {
      public void initialize(MediaMetadataRetriever var1, ParcelFileDescriptor var2) {
         var1.setDataSource(var2.getFileDescriptor());
      }
   }
}
