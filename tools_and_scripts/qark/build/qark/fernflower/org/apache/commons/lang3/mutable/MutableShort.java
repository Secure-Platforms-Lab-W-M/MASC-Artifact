package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;

public class MutableShort extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = -2135791679L;
   private short value;

   public MutableShort() {
   }

   public MutableShort(Number var1) {
      this.value = var1.shortValue();
   }

   public MutableShort(String var1) {
      this.value = Short.parseShort(var1);
   }

   public MutableShort(short var1) {
      this.value = var1;
   }

   public void add(Number var1) {
      this.value += var1.shortValue();
   }

   public void add(short var1) {
      this.value += var1;
   }

   public short addAndGet(Number var1) {
      short var2 = (short)(this.value + var1.shortValue());
      this.value = var2;
      return var2;
   }

   public short addAndGet(short var1) {
      short var2 = (short)(this.value + var1);
      this.value = var2;
      return var2;
   }

   public int compareTo(MutableShort var1) {
      return NumberUtils.compare(this.value, var1.value);
   }

   public void decrement() {
      --this.value;
   }

   public short decrementAndGet() {
      short var1 = (short)(this.value - 1);
      this.value = var1;
      return var1;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof MutableShort;
      boolean var2 = false;
      if (var3) {
         if (this.value == ((MutableShort)var1).shortValue()) {
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

   public short getAndAdd(Number var1) {
      short var2 = this.value;
      this.value += var1.shortValue();
      return var2;
   }

   public short getAndAdd(short var1) {
      short var2 = this.value;
      this.value += var1;
      return var2;
   }

   public short getAndDecrement() {
      short var1 = this.value--;
      return var1;
   }

   public short getAndIncrement() {
      short var1 = this.value++;
      return var1;
   }

   public Short getValue() {
      return this.value;
   }

   public int hashCode() {
      return this.value;
   }

   public void increment() {
      ++this.value;
   }

   public short incrementAndGet() {
      short var1 = (short)(this.value + 1);
      this.value = var1;
      return var1;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public void setValue(Number var1) {
      this.value = var1.shortValue();
   }

   public void setValue(short var1) {
      this.value = var1;
   }

   public short shortValue() {
      return this.value;
   }

   public void subtract(Number var1) {
      this.value -= var1.shortValue();
   }

   public void subtract(short var1) {
      this.value -= var1;
   }

   public Short toShort() {
      return this.shortValue();
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
