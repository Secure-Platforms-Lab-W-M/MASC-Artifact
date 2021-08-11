package com.nineoldandroids.animation;

public class IntEvaluator implements TypeEvaluator {
   public Integer evaluate(float var1, Integer var2, Integer var3) {
      int var4 = var2;
      return (int)((float)var4 + (float)(var3 - var4) * var1);
   }
}
