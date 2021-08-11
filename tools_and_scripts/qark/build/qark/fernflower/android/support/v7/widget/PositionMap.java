package android.support.v7.widget;

import java.util.ArrayList;

class PositionMap implements Cloneable {
   private static final Object DELETED = new Object();
   private boolean mGarbage;
   private int[] mKeys;
   private int mSize;
   private Object[] mValues;

   PositionMap() {
      this(10);
   }

   PositionMap(int var1) {
      this.mGarbage = false;
      if (var1 == 0) {
         this.mKeys = PositionMap.ContainerHelpers.EMPTY_INTS;
         this.mValues = PositionMap.ContainerHelpers.EMPTY_OBJECTS;
      } else {
         var1 = idealIntArraySize(var1);
         this.mKeys = new int[var1];
         this.mValues = new Object[var1];
      }

      this.mSize = 0;
   }

   // $FF: renamed from: gc () void
   private void method_4() {
      int var3 = this.mSize;
      int var2 = 0;
      int[] var4 = this.mKeys;
      Object[] var5 = this.mValues;

      for(int var1 = 0; var1 < var3; ++var1) {
         Object var6 = var5[var1];
         if (var6 != DELETED) {
            if (var1 != var2) {
               var4[var2] = var4[var1];
               var5[var2] = var6;
               var5[var1] = null;
            }

            ++var2;
         }
      }

      this.mGarbage = false;
      this.mSize = var2;
   }

   static int idealBooleanArraySize(int var0) {
      return idealByteArraySize(var0);
   }

   static int idealByteArraySize(int var0) {
      for(int var1 = 4; var1 < 32; ++var1) {
         if (var0 <= (1 << var1) - 12) {
            return (1 << var1) - 12;
         }
      }

      return var0;
   }

   static int idealCharArraySize(int var0) {
      return idealByteArraySize(var0 * 2) / 2;
   }

   static int idealFloatArraySize(int var0) {
      return idealByteArraySize(var0 * 4) / 4;
   }

   static int idealIntArraySize(int var0) {
      return idealByteArraySize(var0 * 4) / 4;
   }

   static int idealLongArraySize(int var0) {
      return idealByteArraySize(var0 * 8) / 8;
   }

   static int idealObjectArraySize(int var0) {
      return idealByteArraySize(var0 * 4) / 4;
   }

   static int idealShortArraySize(int var0) {
      return idealByteArraySize(var0 * 2) / 2;
   }

   public void append(int var1, Object var2) {
      int var3 = this.mSize;
      if (var3 != 0 && var1 <= this.mKeys[var3 - 1]) {
         this.put(var1, var2);
      } else {
         if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.method_4();
         }

         var3 = this.mSize;
         if (var3 >= this.mKeys.length) {
            int var4 = idealIntArraySize(var3 + 1);
            int[] var5 = new int[var4];
            Object[] var6 = new Object[var4];
            int[] var7 = this.mKeys;
            System.arraycopy(var7, 0, var5, 0, var7.length);
            Object[] var8 = this.mValues;
            System.arraycopy(var8, 0, var6, 0, var8.length);
            this.mKeys = var5;
            this.mValues = var6;
         }

         this.mKeys[var3] = var1;
         this.mValues[var3] = var2;
         this.mSize = var3 + 1;
      }
   }

   public void clear() {
      int var2 = this.mSize;
      Object[] var3 = this.mValues;

      for(int var1 = 0; var1 < var2; ++var1) {
         var3[var1] = null;
      }

      this.mSize = 0;
      this.mGarbage = false;
   }

   public PositionMap clone() {
      PositionMap var1 = null;

      boolean var10001;
      PositionMap var2;
      try {
         var2 = (PositionMap)super.clone();
      } catch (CloneNotSupportedException var5) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mKeys = (int[])this.mKeys.clone();
      } catch (CloneNotSupportedException var4) {
         var10001 = false;
         return var1;
      }

      var1 = var2;

      try {
         var2.mValues = (Object[])this.mValues.clone();
         return var2;
      } catch (CloneNotSupportedException var3) {
         var10001 = false;
         return var1;
      }
   }

   public void delete(int var1) {
      var1 = PositionMap.ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var1 >= 0) {
         Object[] var2 = this.mValues;
         Object var3 = var2[var1];
         Object var4 = DELETED;
         if (var3 != var4) {
            var2[var1] = var4;
            this.mGarbage = true;
         }
      }
   }

   public Object get(int var1) {
      return this.get(var1, (Object)null);
   }

   public Object get(int var1, Object var2) {
      var1 = PositionMap.ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var1 >= 0) {
         Object[] var3 = this.mValues;
         return var3[var1] == DELETED ? var2 : var3[var1];
      } else {
         return var2;
      }
   }

   public int indexOfKey(int var1) {
      if (this.mGarbage) {
         this.method_4();
      }

      return PositionMap.ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
   }

   public int indexOfValue(Object var1) {
      if (this.mGarbage) {
         this.method_4();
      }

      for(int var2 = 0; var2 < this.mSize; ++var2) {
         if (this.mValues[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public void insertKeyRange(int var1, int var2) {
   }

   public int keyAt(int var1) {
      if (this.mGarbage) {
         this.method_4();
      }

      return this.mKeys[var1];
   }

   public void put(int var1, Object var2) {
      int var3 = PositionMap.ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var3 >= 0) {
         this.mValues[var3] = var2;
      } else {
         var3 = ~var3;
         Object[] var5;
         if (var3 < this.mSize) {
            var5 = this.mValues;
            if (var5[var3] == DELETED) {
               this.mKeys[var3] = var1;
               var5[var3] = var2;
               return;
            }
         }

         if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.method_4();
            var3 = ~PositionMap.ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
         }

         int var4 = this.mSize;
         int[] var8;
         if (var4 >= this.mKeys.length) {
            var4 = idealIntArraySize(var4 + 1);
            var8 = new int[var4];
            Object[] var6 = new Object[var4];
            int[] var7 = this.mKeys;
            System.arraycopy(var7, 0, var8, 0, var7.length);
            Object[] var9 = this.mValues;
            System.arraycopy(var9, 0, var6, 0, var9.length);
            this.mKeys = var8;
            this.mValues = var6;
         }

         var4 = this.mSize;
         if (var4 - var3 != 0) {
            var8 = this.mKeys;
            System.arraycopy(var8, var3, var8, var3 + 1, var4 - var3);
            var5 = this.mValues;
            System.arraycopy(var5, var3, var5, var3 + 1, this.mSize - var3);
         }

         this.mKeys[var3] = var1;
         this.mValues[var3] = var2;
         ++this.mSize;
      }
   }

   public void remove(int var1) {
      this.delete(var1);
   }

   public void removeAt(int var1) {
      Object[] var2 = this.mValues;
      Object var3 = var2[var1];
      Object var4 = DELETED;
      if (var3 != var4) {
         var2[var1] = var4;
         this.mGarbage = true;
      }
   }

   public void removeAtRange(int var1, int var2) {
      for(var2 = Math.min(this.mSize, var1 + var2); var1 < var2; ++var1) {
         this.removeAt(var1);
      }

   }

   public void removeKeyRange(ArrayList var1, int var2, int var3) {
   }

   public void setValueAt(int var1, Object var2) {
      if (this.mGarbage) {
         this.method_4();
      }

      this.mValues[var1] = var2;
   }

   public int size() {
      if (this.mGarbage) {
         this.method_4();
      }

      return this.mSize;
   }

   public String toString() {
      if (this.size() <= 0) {
         return "{}";
      } else {
         StringBuilder var2 = new StringBuilder(this.mSize * 28);
         var2.append('{');

         for(int var1 = 0; var1 < this.mSize; ++var1) {
            if (var1 > 0) {
               var2.append(", ");
            }

            var2.append(this.keyAt(var1));
            var2.append('=');
            Object var3 = this.valueAt(var1);
            if (var3 != this) {
               var2.append(var3);
            } else {
               var2.append("(this Map)");
            }
         }

         var2.append('}');
         return var2.toString();
      }
   }

   public Object valueAt(int var1) {
      if (this.mGarbage) {
         this.method_4();
      }

      return this.mValues[var1];
   }

   static class ContainerHelpers {
      static final boolean[] EMPTY_BOOLEANS = new boolean[0];
      static final int[] EMPTY_INTS = new int[0];
      static final long[] EMPTY_LONGS = new long[0];
      static final Object[] EMPTY_OBJECTS = new Object[0];

      static int binarySearch(int[] var0, int var1, int var2) {
         int var3 = 0;
         --var1;

         while(var3 <= var1) {
            int var4 = var3 + var1 >>> 1;
            int var5 = var0[var4];
            if (var5 < var2) {
               var3 = var4 + 1;
            } else {
               if (var5 <= var2) {
                  return var4;
               }

               var1 = var4 - 1;
            }
         }

         return ~var3;
      }
   }
}
