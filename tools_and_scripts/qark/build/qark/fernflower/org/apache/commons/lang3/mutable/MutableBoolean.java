package org.apache.commons.lang3.mutable;

import java.io.Serializable;
import org.apache.commons.lang3.BooleanUtils;

public class MutableBoolean implements Mutable, Serializable, Comparable {
   private static final long serialVersionUID = -4830728138360036487L;
   private boolean value;

   public MutableBoolean() {
   }

   public MutableBoolean(Boolean var1) {
      this.value = var1;
   }

   public MutableBoolean(boolean var1) {
      this.value = var1;
   }

   public boolean booleanValue() {
      return this.value;
   }

   public int compareTo(MutableBoolean var1) {
      return BooleanUtils.compare(this.value, var1.value);
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof MutableBoolean;
      boolean var2 = false;
      if (var3) {
         if (this.value == ((MutableBoolean)var1).booleanValue()) {
            var2 = true;
         }

         return var2;
      } else {
         return false;
      }
   }

   public Boolean getValue() {
      return this.value;
   }

   public int hashCode() {
      Boolean var1;
      if (this.value) {
         var1 = Boolean.TRUE;
      } else {
         var1 = Boolean.FALSE;
      }

      return var1.hashCode();
   }

   public boolean isFalse() {
      return this.value ^ true;
   }

   public boolean isTrue() {
      return this.value;
   }

   public void setFalse() {
      this.value = false;
   }

   public void setTrue() {
      this.value = true;
   }

   public void setValue(Boolean var1) {
      this.value = var1;
   }

   public void setValue(boolean var1) {
      this.value = var1;
   }

   public Boolean toBoolean() {
      return this.booleanValue();
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
