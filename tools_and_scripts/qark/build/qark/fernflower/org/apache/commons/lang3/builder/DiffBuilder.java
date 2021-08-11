package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

public class DiffBuilder implements Builder {
   private final List diffs;
   private final Object left;
   private final boolean objectsTriviallyEqual;
   private final Object right;
   private final ToStringStyle style;

   public DiffBuilder(Object var1, Object var2, ToStringStyle var3) {
      this(var1, var2, var3, true);
   }

   public DiffBuilder(Object var1, Object var2, ToStringStyle var3, boolean var4) {
      boolean var6 = true;
      boolean var5;
      if (var1 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "lhs cannot be null");
      if (var2 != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "rhs cannot be null");
      this.diffs = new ArrayList();
      this.left = var1;
      this.right = var2;
      this.style = var3;
      if (!var4 || var1 != var2 && !var1.equals(var2)) {
         var4 = false;
      } else {
         var4 = var6;
      }

      this.objectsTriviallyEqual = var4;
   }

   private void validateFieldNameNotNull(String var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Field name cannot be null");
   }

   public DiffBuilder append(String var1, final byte var2, final byte var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Byte getLeft() {
                  return var2;
               }

               public Byte getRight() {
                  return var3;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final char var2, final char var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Character getLeft() {
                  return var2;
               }

               public Character getRight() {
                  return var3;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final double var2, final double var4) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (Double.doubleToLongBits(var2) != Double.doubleToLongBits(var4)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Double getLeft() {
                  return var2;
               }

               public Double getRight() {
                  return var4;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final float var2, final float var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (Float.floatToIntBits(var2) != Float.floatToIntBits(var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Float getLeft() {
                  return var2;
               }

               public Float getRight() {
                  return var3;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final int var2, final int var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Integer getLeft() {
                  return var2;
               }

               public Integer getRight() {
                  return var3;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final long var2, final long var4) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var4) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Long getLeft() {
                  return var2;
               }

               public Long getRight() {
                  return var4;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final Object var2, final Object var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else if (var2 == var3) {
         return this;
      } else {
         Object var4;
         if (var2 != null) {
            var4 = var2;
         } else {
            var4 = var3;
         }

         if (var4.getClass().isArray()) {
            if (var4 instanceof boolean[]) {
               return this.append(var1, (boolean[])var2, (boolean[])var3);
            } else if (var4 instanceof byte[]) {
               return this.append(var1, (byte[])var2, (byte[])var3);
            } else if (var4 instanceof char[]) {
               return this.append(var1, (char[])var2, (char[])var3);
            } else if (var4 instanceof double[]) {
               return this.append(var1, (double[])var2, (double[])var3);
            } else if (var4 instanceof float[]) {
               return this.append(var1, (float[])var2, (float[])var3);
            } else if (var4 instanceof int[]) {
               return this.append(var1, (int[])var2, (int[])var3);
            } else if (var4 instanceof long[]) {
               return this.append(var1, (long[])var2, (long[])var3);
            } else {
               return var4 instanceof short[] ? this.append(var1, (short[])var2, (short[])var3) : this.append(var1, (Object[])var2, (Object[])var3);
            }
         } else if (var2 != null && var2.equals(var3)) {
            return this;
         } else {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Object getLeft() {
                  return var2;
               }

               public Object getRight() {
                  return var3;
               }
            });
            return this;
         }
      }
   }

   public DiffBuilder append(String var1, DiffResult var2) {
      this.validateFieldNameNotNull(var1);
      boolean var3;
      if (var2 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "Diff result cannot be null");
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         Iterator var6 = var2.getDiffs().iterator();

         while(var6.hasNext()) {
            Diff var4 = (Diff)var6.next();
            StringBuilder var5 = new StringBuilder();
            var5.append(var1);
            var5.append(".");
            var5.append(var4.getFieldName());
            this.append(var5.toString(), var4.getLeft(), var4.getRight());
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final short var2, final short var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Short getLeft() {
                  return var2;
               }

               public Short getRight() {
                  return var3;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final boolean var2, final boolean var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Boolean getLeft() {
                  return var2;
               }

               public Boolean getRight() {
                  return var3;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final byte[] var2, final byte[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Byte[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Byte[] getRight() {
                  return ArrayUtils.toObject(var3);
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final char[] var2, final char[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Character[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Character[] getRight() {
                  return ArrayUtils.toObject(var3);
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final double[] var2, final double[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Double[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Double[] getRight() {
                  return ArrayUtils.toObject(var3);
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final float[] var2, final float[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Float[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Float[] getRight() {
                  return ArrayUtils.toObject(var3);
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final int[] var2, final int[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Integer[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Integer[] getRight() {
                  return ArrayUtils.toObject(var3);
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final long[] var2, final long[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Long[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Long[] getRight() {
                  return ArrayUtils.toObject(var3);
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final Object[] var2, final Object[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Object[] getLeft() {
                  return var2;
               }

               public Object[] getRight() {
                  return var3;
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final short[] var2, final short[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Short[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Short[] getRight() {
                  return ArrayUtils.toObject(var3);
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(String var1, final boolean[] var2, final boolean[] var3) {
      this.validateFieldNameNotNull(var1);
      if (this.objectsTriviallyEqual) {
         return this;
      } else {
         if (!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Boolean[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Boolean[] getRight() {
                  return ArrayUtils.toObject(var3);
               }
            });
         }

         return this;
      }
   }

   public DiffResult build() {
      return new DiffResult(this.left, this.right, this.diffs, this.style);
   }
}
