package com.bumptech.glide.load;

public enum PreferredColorSpace {
   DISPLAY_P3,
   SRGB;

   static {
      PreferredColorSpace var0 = new PreferredColorSpace("DISPLAY_P3", 1);
      DISPLAY_P3 = var0;
   }
}
