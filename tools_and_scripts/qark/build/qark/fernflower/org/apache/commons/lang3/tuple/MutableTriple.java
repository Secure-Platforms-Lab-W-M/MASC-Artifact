package org.apache.commons.lang3.tuple;

public class MutableTriple extends Triple {
   private static final long serialVersionUID = 1L;
   public Object left;
   public Object middle;
   public Object right;

   public MutableTriple() {
   }

   public MutableTriple(Object var1, Object var2, Object var3) {
      this.left = var1;
      this.middle = var2;
      this.right = var3;
   }

   // $FF: renamed from: of (java.lang.Object, java.lang.Object, java.lang.Object) org.apache.commons.lang3.tuple.MutableTriple
   public static MutableTriple method_19(Object var0, Object var1, Object var2) {
      return new MutableTriple(var0, var1, var2);
   }

   public Object getLeft() {
      return this.left;
   }

   public Object getMiddle() {
      return this.middle;
   }

   public Object getRight() {
      return this.right;
   }

   public void setLeft(Object var1) {
      this.left = var1;
   }

   public void setMiddle(Object var1) {
      this.middle = var1;
   }

   public void setRight(Object var1) {
      this.right = var1;
   }
}
