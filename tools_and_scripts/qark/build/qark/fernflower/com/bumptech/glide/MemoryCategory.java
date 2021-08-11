package com.bumptech.glide;

public enum MemoryCategory {
   HIGH,
   LOW(0.5F),
   NORMAL(1.0F);

   private final float multiplier;

   static {
      MemoryCategory var0 = new MemoryCategory("HIGH", 2, 1.5F);
      HIGH = var0;
   }

   private MemoryCategory(float var3) {
      this.multiplier = var3;
   }

   public float getMultiplier() {
      return this.multiplier;
   }
}
