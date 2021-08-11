package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public interface BitmapPool {
   void clearMemory();

   Bitmap get(int var1, int var2, Config var3);

   Bitmap getDirty(int var1, int var2, Config var3);

   long getMaxSize();

   void put(Bitmap var1);

   void setSizeMultiplier(float var1);

   void trimMemory(int var1);
}
