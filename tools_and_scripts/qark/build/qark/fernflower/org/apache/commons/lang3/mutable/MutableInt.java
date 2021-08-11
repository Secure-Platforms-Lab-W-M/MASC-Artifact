package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;

public class MutableInt extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = 512176391864L;
   private int value;

   public MutableInt() {
   }

   public MutableInt(int var1) {
      this.value = var1;
   }

   public MutableInt(Number var1) {
      this.value = var1.intValue();
   }

   public MutableInt(String var1) {
      this.value = Integer.parseInt(var1);
   }

   public void add(int var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.intValue();
   }

   public int addAndGet(int var1) {
      var1 += this.value;
      this.value = var1;
      return var1;
   }

   public int addAndGet(Number var1) {
      int var2 = this.value + var1.intValue();
      this.value = var2;
      return var2;
   }

   public int compareTo(MutableInt var1) {
      return NumberUtils.compare(this.value, var1.value);
   }

   public void decrement() {
      --this.value;
   }

   public int decrementAndGet() {
      int var1 = this.value - 1;
      this.value = var1;
      return var1;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof MutableInt;
      boolean var2 = false;
      if (var3) {
         if (this.value == ((MutableInt)var1).intValue()) {
            var2 = true;
         }

         return var2;
      } else {
         return false;
      }
   }

   public float floatValue() {
      return (float)this.value;
   }

   public int getAndAdd(int var1) {
      int var2 = this.value;
      this.value += var1;
      return var2;
   }

   public int getAndAdd(Number var1) {
      int var2 = this.value;
      this.value += var1.intValue();
      return var2;
   }

   public int getAndDecrement() {
      int var1 = this.value--;
      return var1;
   }

   public int getAndIncrement() {
      int var1 = this.value++;
      return var1;
   }

   public Integer getValue() {
      return this.value;
   }

   public int hashCode() {
      return this.value;
   }

   public void increment() {
      ++this.value;
   }

   public int incrementAndGet() {
      int var1 = this.value + 1;
      this.value = var1;
      return var1;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public void setValue(int var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.intValue();
   }

   public void subtract(int var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.intValue();
   }

   public Integer toInteger() {
      return this.intValue();
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
