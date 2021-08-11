package javax.media.protocol;

import java.io.Serializable;

public class RateRange implements Serializable {
   private boolean exact;
   private float max;
   private float min;
   private float value;

   public RateRange(float var1, float var2, float var3, boolean var4) {
      this.value = var1;
      this.min = var2;
      this.max = var3;
      this.exact = var4;
   }

   public RateRange(RateRange var1) {
      this(var1.value, var1.min, var1.max, var1.exact);
   }

   public float getCurrentRate() {
      return this.value;
   }

   public float getMaximumRate() {
      return this.max;
   }

   public float getMinimumRate() {
      return this.min;
   }

   public boolean inRange(float var1) {
      throw new UnsupportedOperationException();
   }

   public boolean isExact() {
      return this.exact;
   }

   public float setCurrentRate(float var1) {
      this.value = var1;
      return var1;
   }
}
