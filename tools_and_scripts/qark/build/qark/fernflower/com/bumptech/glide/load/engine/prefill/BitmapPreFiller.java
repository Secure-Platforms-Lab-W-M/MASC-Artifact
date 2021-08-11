package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.util.Util;
import java.util.HashMap;

public final class BitmapPreFiller {
   private final BitmapPool bitmapPool;
   private BitmapPreFillRunner current;
   private final DecodeFormat defaultFormat;
   private final Handler handler = new Handler(Looper.getMainLooper());
   private final MemoryCache memoryCache;

   public BitmapPreFiller(MemoryCache var1, BitmapPool var2, DecodeFormat var3) {
      this.memoryCache = var1;
      this.bitmapPool = var2;
      this.defaultFormat = var3;
   }

   private static int getSizeInBytes(PreFillType var0) {
      return Util.getBitmapByteSize(var0.getWidth(), var0.getHeight(), var0.getConfig());
   }

   PreFillQueue generateAllocationOrder(PreFillType... var1) {
      long var7 = this.memoryCache.getMaxSize();
      long var9 = this.memoryCache.getCurrentSize();
      long var11 = this.bitmapPool.getMaxSize();
      int var4 = 0;
      int var6 = var1.length;
      byte var5 = 0;

      int var3;
      for(var3 = 0; var3 < var6; ++var3) {
         var4 += var1[var3].getWeight();
      }

      float var2 = (float)(var7 - var9 + var11) / (float)var4;
      HashMap var13 = new HashMap();
      var4 = var1.length;

      for(var3 = var5; var3 < var4; ++var3) {
         PreFillType var14 = var1[var3];
         var13.put(var14, Math.round((float)var14.getWeight() * var2) / getSizeInBytes(var14));
      }

      return new PreFillQueue(var13);
   }

   public void preFill(PreFillType.Builder... var1) {
      BitmapPreFillRunner var3 = this.current;
      if (var3 != null) {
         var3.cancel();
      }

      PreFillType[] var4 = new PreFillType[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         PreFillType.Builder var5 = var1[var2];
         if (var5.getConfig() == null) {
            Config var8;
            if (this.defaultFormat == DecodeFormat.PREFER_ARGB_8888) {
               var8 = Config.ARGB_8888;
            } else {
               var8 = Config.RGB_565;
            }

            var5.setConfig(var8);
         }

         var4[var2] = var5.build();
      }

      PreFillQueue var6 = this.generateAllocationOrder(var4);
      BitmapPreFillRunner var7 = new BitmapPreFillRunner(this.bitmapPool, this.memoryCache, var6);
      this.current = var7;
      this.handler.post(var7);
   }
}
