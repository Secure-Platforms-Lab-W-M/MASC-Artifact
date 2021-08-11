package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ClassUtils;

public class MultilineRecursiveToStringStyle extends RecursiveToStringStyle {
   private static final int INDENT = 2;
   private static final long serialVersionUID = 1L;
   private int spaces = 2;

   public MultilineRecursiveToStringStyle() {
      this.resetIndent();
   }

   private void resetIndent() {
      StringBuilder var1 = new StringBuilder();
      var1.append("{");
      var1.append(System.lineSeparator());
      var1.append(this.spacer(this.spaces));
      this.setArrayStart(var1.toString());
      var1 = new StringBuilder();
      var1.append(",");
      var1.append(System.lineSeparator());
      var1.append(this.spacer(this.spaces));
      this.setArraySeparator(var1.toString());
      var1 = new StringBuilder();
      var1.append(System.lineSeparator());
      var1.append(this.spacer(this.spaces - 2));
      var1.append("}");
      this.setArrayEnd(var1.toString());
      var1 = new StringBuilder();
      var1.append("[");
      var1.append(System.lineSeparator());
      var1.append(this.spacer(this.spaces));
      this.setContentStart(var1.toString());
      var1 = new StringBuilder();
      var1.append(",");
      var1.append(System.lineSeparator());
      var1.append(this.spacer(this.spaces));
      this.setFieldSeparator(var1.toString());
      var1 = new StringBuilder();
      var1.append(System.lineSeparator());
      var1.append(this.spacer(this.spaces - 2));
      var1.append("]");
      this.setContentEnd(var1.toString());
   }

   private StringBuilder spacer(int var1) {
      StringBuilder var3 = new StringBuilder();

      for(int var2 = 0; var2 < var1; ++var2) {
         var3.append(" ");
      }

      return var3;
   }

   public void appendDetail(StringBuffer var1, String var2, Object var3) {
      if (!ClassUtils.isPrimitiveWrapper(var3.getClass()) && !String.class.equals(var3.getClass()) && this.accept(var3.getClass())) {
         this.spaces += 2;
         this.resetIndent();
         var1.append(ReflectionToStringBuilder.toString(var3, this));
         this.spaces -= 2;
         this.resetIndent();
      } else {
         super.appendDetail(var1, var2, var3);
      }
   }

   protected void appendDetail(StringBuffer var1, String var2, byte[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (byte[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void appendDetail(StringBuffer var1, String var2, char[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (char[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void appendDetail(StringBuffer var1, String var2, double[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (double[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void appendDetail(StringBuffer var1, String var2, float[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (float[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void appendDetail(StringBuffer var1, String var2, int[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (int[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void appendDetail(StringBuffer var1, String var2, long[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (long[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void appendDetail(StringBuffer var1, String var2, Object[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (Object[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void appendDetail(StringBuffer var1, String var2, short[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (short[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void appendDetail(StringBuffer var1, String var2, boolean[] var3) {
      this.spaces += 2;
      this.resetIndent();
      super.appendDetail(var1, var2, (boolean[])var3);
      this.spaces -= 2;
      this.resetIndent();
   }

   protected void reflectionAppendArrayDetail(StringBuffer var1, String var2, Object var3) {
      this.spaces += 2;
      this.resetIndent();
      super.reflectionAppendArrayDetail(var1, var2, var3);
      this.spaces -= 2;
      this.resetIndent();
   }
}
