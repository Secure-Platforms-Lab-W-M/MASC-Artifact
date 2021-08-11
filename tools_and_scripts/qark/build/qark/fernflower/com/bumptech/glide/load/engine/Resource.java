package com.bumptech.glide.load.engine;

public interface Resource {
   Object get();

   Class getResourceClass();

   int getSize();

   void recycle();
}
