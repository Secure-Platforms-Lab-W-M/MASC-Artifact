package org.apache.commons.lang3.tuple;

public final class ImmutablePair extends Pair {
   private static final ImmutablePair NULL = method_15((Object)null, (Object)null);
   private static final long serialVersionUID = 4954918890077093841L;
   public final Object left;
   public final Object right;

   public ImmutablePair(Object var1, Object var2) {
      this.left = var1;
      this.right = var2;
   }

   public static ImmutablePair nullPair() {
      return NULL;
   }

   // $FF: renamed from: of (java.lang.Object, java.lang.Object) org.apache.commons.lang3.tuple.ImmutablePair
   public static ImmutablePair method_15(Object var0, Object var1) {
      return new ImmutablePair(var0, var1);
   }

   public Object getLeft() {
      return this.left;
   }

   public Object getRight() {
      return this.right;
   }

   public Object setValue(Object var1) {
      throw new UnsupportedOperationException();
   }
}
