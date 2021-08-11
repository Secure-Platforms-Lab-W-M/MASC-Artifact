package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Triple implements Comparable, Serializable {
   private static final long serialVersionUID = 1L;

   // $FF: renamed from: of (java.lang.Object, java.lang.Object, java.lang.Object) org.apache.commons.lang3.tuple.Triple
   public static Triple method_18(Object var0, Object var1, Object var2) {
      return new ImmutableTriple(var0, var1, var2);
   }

   public int compareTo(Triple var1) {
      return (new CompareToBuilder()).append(this.getLeft(), var1.getLeft()).append(this.getMiddle(), var1.getMiddle()).append(this.getRight(), var1.getRight()).toComparison();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof Triple) {
         Triple var2 = (Triple)var1;
         return Objects.equals(this.getLeft(), var2.getLeft()) && Objects.equals(this.getMiddle(), var2.getMiddle()) && Objects.equals(this.getRight(), var2.getRight());
      } else {
         return false;
      }
   }

   public abstract Object getLeft();

   public abstract Object getMiddle();

   public abstract Object getRight();

   public int hashCode() {
      Object var4 = this.getLeft();
      int var3 = 0;
      int var1;
      if (var4 == null) {
         var1 = 0;
      } else {
         var1 = this.getLeft().hashCode();
      }

      int var2;
      if (this.getMiddle() == null) {
         var2 = 0;
      } else {
         var2 = this.getMiddle().hashCode();
      }

      if (this.getRight() != null) {
         var3 = this.getRight().hashCode();
      }

      return var1 ^ var2 ^ var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("(");
      var1.append(this.getLeft());
      var1.append(",");
      var1.append(this.getMiddle());
      var1.append(",");
      var1.append(this.getRight());
      var1.append(")");
      return var1.toString();
   }

   public String toString(String var1) {
      return String.format(var1, this.getLeft(), this.getMiddle(), this.getRight());
   }
}
