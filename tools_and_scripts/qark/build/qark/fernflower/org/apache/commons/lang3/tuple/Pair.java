package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Objects;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Pair implements Entry, Comparable, Serializable {
   private static final long serialVersionUID = 4954918890077093841L;

   // $FF: renamed from: of (java.lang.Object, java.lang.Object) org.apache.commons.lang3.tuple.Pair
   public static Pair method_14(Object var0, Object var1) {
      return new ImmutablePair(var0, var1);
   }

   public int compareTo(Pair var1) {
      return (new CompareToBuilder()).append(this.getLeft(), var1.getLeft()).append(this.getRight(), var1.getRight()).toComparison();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof Entry) {
         Entry var2 = (Entry)var1;
         return Objects.equals(this.getKey(), var2.getKey()) && Objects.equals(this.getValue(), var2.getValue());
      } else {
         return false;
      }
   }

   public final Object getKey() {
      return this.getLeft();
   }

   public abstract Object getLeft();

   public abstract Object getRight();

   public Object getValue() {
      return this.getRight();
   }

   public int hashCode() {
      Object var3 = this.getKey();
      int var2 = 0;
      int var1;
      if (var3 == null) {
         var1 = 0;
      } else {
         var1 = this.getKey().hashCode();
      }

      if (this.getValue() != null) {
         var2 = this.getValue().hashCode();
      }

      return var1 ^ var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("(");
      var1.append(this.getLeft());
      var1.append(',');
      var1.append(this.getRight());
      var1.append(')');
      return var1.toString();
   }

   public String toString(String var1) {
      return String.format(var1, this.getLeft(), this.getRight());
   }
}
