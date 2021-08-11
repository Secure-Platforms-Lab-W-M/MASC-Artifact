package androidx.collection;

public class LongSparseArray implements Cloneable {
   private static final Object DELETED = new Object();
   private boolean mGarbage;
   private long[] mKeys;
   private int mSize;
   private Object[] mValues;

   public LongSparseArray() {
      this(10);
   }

   public LongSparseArray(int var1) {
      this.mGarbage = false;
      if (var1 == 0) {
         this.mKeys = ContainerHelpers.EMPTY_LONGS;
         this.mValues = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         var1 = ContainerHelpers.idealLongArraySize(var1);
         this.mKeys = new long[var1];
         this.mValues = new Object[var1];
      }
   }

   // $FF: renamed from: gc () void
   private void method_47() {
      int var4 = this.mSize;
      int var2 = 0;
      long[] var5 = this.mKeys;
      Object[] var6 = this.mValues;

      int var3;
      for(int var1 = 0; var1 < var4; var2 = var3) {
         Object var7 = var6[var1];
         var3 = var2;
         if (var7 != DELETED) {
            if (var1 != var2) {
               var5[var2] = var5[var1];
               var6[var2] = var7;
               var6[var1] = null;
            }

            var3 = var2 + 1;
         }

         ++var1;
      }

      this.mGarbage = false;
      this.mSize = var2;
   }

   public void append(long var1, Object var3) {
      int var4 = this.mSize;
      if (var4 != 0 && var1 <= this.mKeys[var4 - 1]) {
         this.put(var1, var3);
      } else {
         if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.method_47();
         }

         var4 = this.mSize;
         if (var4 >= this.mKeys.length) {
            int var5 = ContainerHelpers.idealLongArraySize(var4 + 1);
            long[] var6 = new long[var5];
            Object[] var7 = new Object[var5];
            long[] var8 = this.mKeys;
            System.arraycopy(var8, 0, var6, 0, var8.length);
            Object[] var9 = this.mValues;
            System.arraycopy(var9, 0, var7, 0, var9.length);
            this.mKeys = var6;
            this.mValues = var7;
         }

         this.mKeys[var4] = var1;
         this.mValues[var4] = var3;
         this.mSize = var4 + 1;
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

   public LongSparseArray clone() {
      try {
         LongSparseArray var1 = (LongSparseArray)super.clone();
         var1.mKeys = (long[])this.mKeys.clone();
         var1.mValues = (Object[])this.mValues.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError(var2);
      }
   }

   public boolean containsKey(long var1) {
      return this.indexOfKey(var1) >= 0;
   }

   public boolean containsValue(Object var1) {
      return this.indexOfValue(var1) >= 0;
   }

   @Deprecated
   public void delete(long var1) {
      this.remove(var1);
   }

   public Object get(long var1) {
      return this.get(var1, (Object)null);
   }

   public Object get(long var1, Object var3) {
      int var4 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var4 >= 0) {
         Object[] var5 = this.mValues;
         return var5[var4] == DELETED ? var3 : var5[var4];
      } else {
         return var3;
      }
   }

   public int indexOfKey(long var1) {
      if (this.mGarbage) {
         this.method_47();
      }

      return ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
   }

   public int indexOfValue(Object var1) {
      if (this.mGarbage) {
         this.method_47();
      }

      for(int var2 = 0; var2 < this.mSize; ++var2) {
         if (this.mValues[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public long keyAt(int var1) {
      if (this.mGarbage) {
         this.method_47();
      }

      return this.mKeys[var1];
   }

   public void put(long var1, Object var3) {
      int var4 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var4 >= 0) {
         this.mValues[var4] = var3;
      } else {
         int var5 = var4;
         Object[] var6;
         if (var4 < this.mSize) {
            var6 = this.mValues;
            if (var6[var4] == DELETED) {
               this.mKeys[var4] = var1;
               var6[var4] = var3;
               return;
            }
         }

         var4 = var4;
         if (this.mGarbage) {
            var4 = var5;
            if (this.mSize >= this.mKeys.length) {
               this.method_47();
               var4 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
            }
         }

         var5 = this.mSize;
         long[] var9;
         if (var5 >= this.mKeys.length) {
            var5 = ContainerHelpers.idealLongArraySize(var5 + 1);
            var9 = new long[var5];
            Object[] var7 = new Object[var5];
            long[] var8 = this.mKeys;
            System.arraycopy(var8, 0, var9, 0, var8.length);
            Object[] var10 = this.mValues;
            System.arraycopy(var10, 0, var7, 0, var10.length);
            this.mKeys = var9;
            this.mValues = var7;
         }

         var5 = this.mSize;
         if (var5 - var4 != 0) {
            var9 = this.mKeys;
            System.arraycopy(var9, var4, var9, var4 + 1, var5 - var4);
            var6 = this.mValues;
            System.arraycopy(var6, var4, var6, var4 + 1, this.mSize - var4);
         }

         this.mKeys[var4] = var1;
         this.mValues[var4] = var3;
         ++this.mSize;
      }
   }

   public void putAll(LongSparseArray var1) {
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; ++var2) {
         this.put(var1.keyAt(var2), var1.valueAt(var2));
      }

   }

   public Object putIfAbsent(long var1, Object var3) {
      Object var4 = this.get(var1);
      if (var4 == null) {
         this.put(var1, var3);
      }

      return var4;
   }

   public void remove(long var1) {
      int var3 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, var1);
      if (var3 >= 0) {
         Object[] var4 = this.mValues;
         Object var5 = var4[var3];
         Object var6 = DELETED;
         if (var5 != var6) {
            var4[var3] = var6;
            this.mGarbage = true;
         }
      }

   }

   public boolean remove(long var1, Object var3) {
      int var4 = this.indexOfKey(var1);
      if (var4 >= 0) {
         Object var5 = this.valueAt(var4);
         if (var3 == var5 || var3 != null && var3.equals(var5)) {
            this.removeAt(var4);
            return true;
         }
      }

      return false;
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

   public Object replace(long var1, Object var3) {
      int var4 = this.indexOfKey(var1);
      if (var4 >= 0) {
         Object[] var5 = this.mValues;
         Object var6 = var5[var4];
         var5[var4] = var3;
         return var6;
      } else {
         return null;
      }
   }

   public boolean replace(long var1, Object var3, Object var4) {
      int var5 = this.indexOfKey(var1);
      if (var5 >= 0) {
         Object var6 = this.mValues[var5];
         if (var6 == var3 || var3 != null && var3.equals(var6)) {
            this.mValues[var5] = var4;
            return true;
         }
      }

      return false;
   }

   public void setValueAt(int var1, Object var2) {
      if (this.mGarbage) {
         this.method_47();
      }

      this.mValues[var1] = var2;
   }

   public int size() {
      if (this.mGarbage) {
         this.method_47();
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
         this.method_47();
      }

      return this.mValues[var1];
   }
}
