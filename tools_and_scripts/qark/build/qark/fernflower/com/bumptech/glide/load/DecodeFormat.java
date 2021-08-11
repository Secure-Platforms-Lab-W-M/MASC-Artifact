package com.bumptech.glide.load;

public enum DecodeFormat {
   public static final DecodeFormat DEFAULT;
   PREFER_ARGB_8888,
   PREFER_RGB_565;

   static {
      DecodeFormat var0 = new DecodeFormat("PREFER_RGB_565", 1);
      PREFER_RGB_565 = var0;
      DecodeFormat var1 = PREFER_ARGB_8888;
      DEFAULT = var1;
   }
}
