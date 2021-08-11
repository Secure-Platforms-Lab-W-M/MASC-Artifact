package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public final class GifBitmapProvider implements GifDecoder.BitmapProvider {
   private final ArrayPool arrayPool;
   private final BitmapPool bitmapPool;

   public GifBitmapProvider(BitmapPool var1) {
      this(var1, (ArrayPool)null);
   }

   public GifBitmapProvider(BitmapPool var1, ArrayPool var2) {
      this.bitmapPool = var1;
      this.arrayPool = var2;
   }

   public Bitmap obtain(int var1, int var2, Config var3) {
      return this.bitmapPool.getDirty(var1, var2, var3);
   }

   public byte[] obtainByteArray(int var1) {
      ArrayPool var2 = this.arrayPool;
      return var2 == null ? new byte[var1] : (byte[])var2.get(var1, byte[].class);
   }

   public int[] obtainIntArray(int var1) {
      ArrayPool var2 = this.arrayPool;
      return var2 == null ? new int[var1] : (int[])var2.get(var1, int[].class);
   }

   public void release(Bitmap var1) {
      this.bitmapPool.put(var1);
   }

   public void release(byte[] var1) {
      ArrayPool var2 = this.arrayPool;
      if (var2 != null) {
         var2.put(var1);
      }
   }

   public void release(int[] var1) {
      ArrayPool var2 = this.arrayPool;
      if (var2 != null) {
         var2.put(var1);
      }
   }
}
