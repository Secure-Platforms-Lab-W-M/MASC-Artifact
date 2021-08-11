package com.bumptech.glide;

public enum Priority {
   HIGH,
   IMMEDIATE,
   LOW,
   NORMAL;

   static {
      Priority var0 = new Priority("LOW", 3);
      LOW = var0;
   }
}
