package com.bumptech.glide.load.model;

public interface ModelLoaderFactory {
   ModelLoader build(MultiModelLoaderFactory var1);

   void teardown();
}
