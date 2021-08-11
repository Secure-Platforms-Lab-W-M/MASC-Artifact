package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

interface LruPoolStrategy {
   Bitmap get(int var1, int var2, Config var3);

   int getSize(Bitmap var1);

   String logBitmap(int var1, int var2, Config var3);

   String logBitmap(Bitmap var1);

   void put(Bitmap var1);

   Bitmap removeLast();
}
