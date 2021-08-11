package org.apache.commons.lang3.mutable;

public class MutableFloat extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = 5787169186L;
   private float value;

   public MutableFloat() {
   }

   public MutableFloat(float var1) {
      this.value = var1;
   }

   public MutableFloat(Number var1) {
      this.value = var1.floatValue();
   }

   public MutableFloat(String var1) {
      this.value = Float.parseFloat(var1);
   }

   public void add(float var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.floatValue();
   }

   public float addAndGet(float var1) {
      var1 += this.value;
      this.value = var1;
      return var1;
   }

   public float addAndGet(Number var1) {
      float var2 = this.value + var1.floatValue();
      this.value = var2;
      return var2;
   }

   public int compareTo(MutableFloat var1) {
      return Float.compare(this.value, var1.value);
   }

   public void decrement() {
      --this.value;
   }

   public float decrementAndGet() {
      float var1 = this.value - 1.0F;
      this.value = var1;
      return var1;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public boolean equals(Object var1) {
      return var1 instanceof MutableFloat && Float.floatToIntBits(((MutableFloat)var1).value) == Float.floatToIntBits(this.value);
   }

   public float floatValue() {
      return this.value;
   }

   public float getAndAdd(float var1) {
      float var2 = this.value;
      this.value += var1;
      return var2;
   }

   public float getAndAdd(Number var1) {
      float var2 = this.value;
      this.value += var1.floatValue();
      return var2;
   }

   public float getAndDecrement() {
      float var1 = (float)(this.value--);
      return var1;
   }

   public float getAndIncrement() {
      float var1 = (float)(this.value++);
      return var1;
   }

   public Float getValue() {
      return this.value;
   }

   public int hashCode() {
      return Float.floatToIntBits(this.value);
   }

   public void increment() {
      ++this.value;
   }

   public float incrementAndGet() {
      float var1 = this.value + 1.0F;
      this.value = var1;
      return var1;
   }

   public int intValue() {
      return (int)this.value;
   }

   public boolean isInfinite() {
      return Float.isInfinite(this.value);
   }

   public boolean isNaN() {
      return Float.isNaN(this.value);
   }

   public long longValue() {
      return (long)this.value;
   }

   public void setValue(float var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.floatValue();
   }

   public void subtract(float var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.floatValue();
   }

   public Float toFloat() {
      return this.floatValue();
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
