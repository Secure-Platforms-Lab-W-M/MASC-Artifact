package org.apache.commons.lang3.tuple;

public class MutablePair extends Pair {
   private static final long serialVersionUID = 4954918890077093841L;
   public Object left;
   public Object right;

   public MutablePair() {
   }

   public MutablePair(Object var1, Object var2) {
      this.left = var1;
      this.right = var2;
   }

   // $FF: renamed from: of (java.lang.Object, java.lang.Object) org.apache.commons.lang3.tuple.MutablePair
   public static MutablePair method_16(Object var0, Object var1) {
      return new MutablePair(var0, var1);
   }

   public Object getLeft() {
      return this.left;
   }

   public Object getRight() {
      return this.right;
   }

   public void setLeft(Object var1) {
      this.left = var1;
   }

   public void setRight(Object var1) {
      this.right = var1;
   }

   public Object setValue(Object var1) {
      Object var2 = this.getRight();
      this.setRight(var1);
      return var2;
   }
}
