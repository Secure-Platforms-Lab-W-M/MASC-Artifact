package com.bumptech.glide.load.engine.bitmap_recycle;

public final class IntegerArrayAdapter implements ArrayAdapterInterface {
   private static final String TAG = "IntegerArrayPool";

   public int getArrayLength(int[] var1) {
      return var1.length;
   }

   public int getElementSizeInBytes() {
      return 4;
   }

   public String getTag() {
      return "IntegerArrayPool";
   }

   public int[] newArray(int var1) {
      return new int[var1];
   }
}
