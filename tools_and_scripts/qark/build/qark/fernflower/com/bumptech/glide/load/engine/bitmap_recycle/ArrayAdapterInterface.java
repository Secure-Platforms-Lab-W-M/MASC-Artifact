package com.bumptech.glide.load.engine.bitmap_recycle;

interface ArrayAdapterInterface {
   int getArrayLength(Object var1);

   int getElementSizeInBytes();

   String getTag();

   Object newArray(int var1);
}
