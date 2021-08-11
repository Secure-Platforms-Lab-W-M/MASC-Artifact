package com.nineoldandroids.animation;

public class FloatEvaluator implements TypeEvaluator {
   public Float evaluate(float var1, Number var2, Number var3) {
      float var4 = var2.floatValue();
      return (var3.floatValue() - var4) * var1 + var4;
   }
}
