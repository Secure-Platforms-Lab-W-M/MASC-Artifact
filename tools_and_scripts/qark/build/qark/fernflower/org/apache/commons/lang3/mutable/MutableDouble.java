package org.apache.commons.lang3.mutable;

public class MutableDouble extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = 1587163916L;
   private double value;

   public MutableDouble() {
   }

   public MutableDouble(double var1) {
      this.value = var1;
   }

   public MutableDouble(Number var1) {
      this.value = var1.doubleValue();
   }

   public MutableDouble(String var1) {
      this.value = Double.parseDouble(var1);
   }

   public void add(double var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.doubleValue();
   }

   public double addAndGet(double var1) {
      var1 += this.value;
      this.value = var1;
      return var1;
   }

   public double addAndGet(Number var1) {
      double var2 = this.value + var1.doubleValue();
      this.value = var2;
      return var2;
   }

   public int compareTo(MutableDouble var1) {
      return Double.compare(this.value, var1.value);
   }

   public void decrement() {
      --this.value;
   }

   public double decrementAndGet() {
      double var1 = this.value - 1.0D;
      this.value = var1;
      return var1;
   }

   public double doubleValue() {
      return this.value;
   }

   public boolean equals(Object var1) {
      return var1 instanceof MutableDouble && Double.doubleToLongBits(((MutableDouble)var1).value) == Double.doubleToLongBits(this.value);
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double getAndAdd(double var1) {
      double var3 = this.value;
      this.value += var1;
      return var3;
   }

   public double getAndAdd(Number var1) {
      double var2 = this.value;
      this.value += var1.doubleValue();
      return var2;
   }

   public double getAndDecrement() {
      double var1 = (double)(this.value--);
      return var1;
   }

   public double getAndIncrement() {
      double var1 = (double)(this.value++);
      return var1;
   }

   public Double getValue() {
      return this.value;
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.value);
      return (int)(var1 >>> 32 ^ var1);
   }

   public void increment() {
      ++this.value;
   }

   public double incrementAndGet() {
      double var1 = this.value + 1.0D;
      this.value = var1;
      return var1;
   }

   public int intValue() {
      return (int)this.value;
   }

   public boolean isInfinite() {
      return Double.isInfinite(this.value);
   }

   public boolean isNaN() {
      return Double.isNaN(this.value);
   }

   public long longValue() {
      return (long)this.value;
   }

   public void setValue(double var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.doubleValue();
   }

   public void subtract(double var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.doubleValue();
   }

   public Double toDouble() {
      return this.doubleValue();
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
