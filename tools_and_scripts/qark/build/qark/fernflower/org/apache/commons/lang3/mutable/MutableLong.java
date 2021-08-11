package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;

public class MutableLong extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = 62986528375L;
   private long value;

   public MutableLong() {
   }

   public MutableLong(long var1) {
      this.value = var1;
   }

   public MutableLong(Number var1) {
      this.value = var1.longValue();
   }

   public MutableLong(String var1) {
      this.value = Long.parseLong(var1);
   }

   public void add(long var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.longValue();
   }

   public long addAndGet(long var1) {
      var1 += this.value;
      this.value = var1;
      return var1;
   }

   public long addAndGet(Number var1) {
      long var2 = this.value + var1.longValue();
      this.value = var2;
      return var2;
   }

   public int compareTo(MutableLong var1) {
      return NumberUtils.compare(this.value, var1.value);
   }

   public void decrement() {
      --this.value;
   }

   public long decrementAndGet() {
      long var1 = this.value - 1L;
      this.value = var1;
      return var1;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof MutableLong;
      boolean var2 = false;
      if (var3) {
         if (this.value == ((MutableLong)var1).longValue()) {
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

   public long getAndAdd(long var1) {
      long var3 = this.value;
      this.value += var1;
      return var3;
   }

   public long getAndAdd(Number var1) {
      long var2 = this.value;
      this.value += var1.longValue();
      return var2;
   }

   public long getAndDecrement() {
      long var1 = (long)(this.value--);
      return var1;
   }

   public long getAndIncrement() {
      long var1 = (long)(this.value++);
      return var1;
   }

   public Long getValue() {
      return this.value;
   }

   public int hashCode() {
      long var1 = this.value;
      return (int)(var1 ^ var1 >>> 32);
   }

   public void increment() {
      ++this.value;
   }

   public long incrementAndGet() {
      long var1 = this.value + 1L;
      this.value = var1;
      return var1;
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return this.value;
   }

   public void setValue(long var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.longValue();
   }

   public void subtract(long var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.longValue();
   }

   public Long toLong() {
      return this.longValue();
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
