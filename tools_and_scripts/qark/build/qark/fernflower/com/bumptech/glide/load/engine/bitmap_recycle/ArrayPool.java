package com.bumptech.glide.load.engine.bitmap_recycle;

public interface ArrayPool {
   int STANDARD_BUFFER_SIZE_BYTES = 65536;

   void clearMemory();

   Object get(int var1, Class var2);

   Object getExact(int var1, Class var2);

   void put(Object var1);

   @Deprecated
   void put(Object var1, Class var2);

   void trimMemory(int var1);
}
