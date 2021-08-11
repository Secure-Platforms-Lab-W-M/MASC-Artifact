package com.nineoldandroids.animation;

public class ArgbEvaluator implements TypeEvaluator {
   public Object evaluate(float var1, Object var2, Object var3) {
      int var7 = (Integer)var2;
      int var4 = var7 >> 24;
      int var5 = var7 >> 16 & 255;
      int var6 = var7 >> 8 & 255;
      var7 &= 255;
      int var8 = (Integer)var3;
      return (int)((float)((var8 >> 24) - var4) * var1) + var4 << 24 | (int)((float)((var8 >> 16 & 255) - var5) * var1) + var5 << 16 | (int)((float)((var8 >> 8 & 255) - var6) * var1) + var6 << 8 | (int)((float)((var8 & 255) - var7) * var1) + var7;
   }
}
