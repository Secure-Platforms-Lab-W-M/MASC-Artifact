package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;

public class MutableByte extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = -1585823265L;
   private byte value;

   public MutableByte() {
   }

   public MutableByte(byte var1) {
      this.value = var1;
   }

   public MutableByte(Number var1) {
      this.value = var1.byteValue();
   }

   public MutableByte(String var1) {
      this.value = Byte.parseByte(var1);
   }

   public void add(byte var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.byteValue();
   }

   public byte addAndGet(byte var1) {
      byte var2 = (byte)(this.value + var1);
      this.value = var2;
      return var2;
   }

   public byte addAndGet(Number var1) {
      byte var2 = (byte)(this.value + var1.byteValue());
      this.value = var2;
      return var2;
   }

   public byte byteValue() {
      return this.value;
   }

   public int compareTo(MutableByte var1) {
      return NumberUtils.compare(this.value, var1.value);
   }

   public void decrement() {
      --this.value;
   }

   public byte decrementAndGet() {
      byte var1 = (byte)(this.value - 1);
      this.value = var1;
      return var1;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof MutableByte;
      boolean var2 = false;
      if (var3) {
         if (this.value == ((MutableByte)var1).byteValue()) {
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

   public byte getAndAdd(byte var1) {
      byte var2 = this.value;
      this.value += var1;
      return var2;
   }

   public byte getAndAdd(Number var1) {
      byte var2 = this.value;
      this.value += var1.byteValue();
      return var2;
   }

   public byte getAndDecrement() {
      byte var1 = this.value--;
      return var1;
   }

   public byte getAndIncrement() {
      byte var1 = this.value++;
      return var1;
   }

   public Byte getValue() {
      return this.value;
   }

   public int hashCode() {
      return this.value;
   }

   public void increment() {
      ++this.value;
   }

   public byte incrementAndGet() {
      byte var1 = (byte)(this.value + 1);
      this.value = var1;
      return var1;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public void setValue(byte var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.byteValue();
   }

   public void subtract(byte var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.byteValue();
   }

   public Byte toByte() {
      return this.byteValue();
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
