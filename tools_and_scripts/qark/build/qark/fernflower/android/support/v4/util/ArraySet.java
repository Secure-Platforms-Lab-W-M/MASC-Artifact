package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet implements Collection, Set {
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
   private static final boolean DEBUG = false;
   private static final int[] INT = new int[0];
   private static final Object[] OBJECT = new Object[0];
   private static final String TAG = "ArraySet";
   static Object[] sBaseCache;
   static int sBaseCacheSize;
   static Object[] sTwiceBaseCache;
   static int sTwiceBaseCacheSize;
   Object[] mArray;
   MapCollections mCollections;
   int[] mHashes;
   final boolean mIdentityHashCode;
   int mSize;

   public ArraySet() {
      this(0, false);
   }

   public ArraySet(int var1) {
      this(var1, false);
   }

   public ArraySet(int var1, boolean var2) {
      this.mIdentityHashCode = var2;
      if (var1 == 0) {
         this.mHashes = INT;
         this.mArray = OBJECT;
      } else {
         this.allocArrays(var1);
      }

      this.mSize = 0;
   }

   public ArraySet(ArraySet var1) {
      this();
      if (var1 != null) {
         this.addAll(var1);
      }

   }

   public ArraySet(Collection var1) {
      this();
      if (var1 != null) {
         this.addAll(var1);
      }

   }

   private void allocArrays(int var1) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      label755: {
         Object[] var2;
         label761: {
            if (var1 == 8) {
               synchronized(ArraySet.class){}

               try {
                  if (sTwiceBaseCache != null) {
                     var2 = sTwiceBaseCache;
                     this.mArray = var2;
                     sTwiceBaseCache = (Object[])((Object[])var2[0]);
                     this.mHashes = (int[])((int[])var2[1]);
                     break label761;
                  }
               } catch (Throwable var72) {
                  var10000 = var72;
                  var10001 = false;
                  break label755;
               }

               try {
                  ;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label755;
               }
            } else if (var1 == 4) {
               label760: {
                  synchronized(ArraySet.class){}

                  label745: {
                     label758: {
                        try {
                           if (sBaseCache == null) {
                              break label758;
                           }

                           var2 = sBaseCache;
                           this.mArray = var2;
                           sBaseCache = (Object[])((Object[])var2[0]);
                           this.mHashes = (int[])((int[])var2[1]);
                        } catch (Throwable var74) {
                           var10000 = var74;
                           var10001 = false;
                           break label745;
                        }

                        var2[1] = null;
                        var2[0] = null;

                        try {
                           --sBaseCacheSize;
                           return;
                        } catch (Throwable var70) {
                           var10000 = var70;
                           var10001 = false;
                           break label745;
                        }
                     }

                     label738:
                     try {
                        break label760;
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label738;
                     }
                  }

                  while(true) {
                     var75 = var10000;

                     try {
                        throw var75;
                     } catch (Throwable var68) {
                        var10000 = var68;
                        var10001 = false;
                        continue;
                     }
                  }
               }
            }

            this.mHashes = new int[var1];
            this.mArray = new Object[var1];
            return;
         }

         var2[1] = null;
         var2[0] = null;

         label719:
         try {
            --sTwiceBaseCacheSize;
            return;
         } catch (Throwable var69) {
            var10000 = var69;
            var10001 = false;
            break label719;
         }
      }

      while(true) {
         var75 = var10000;

         try {
            throw var75;
         } catch (Throwable var67) {
            var10000 = var67;
            var10001 = false;
            continue;
         }
      }
   }

   private static void freeArrays(int[] var0, Object[] var1, int var2) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      if (var0.length == 8) {
         synchronized(ArraySet.class){}

         label801: {
            label828: {
               try {
                  if (sTwiceBaseCacheSize >= 10) {
                     break label828;
                  }

                  var1[0] = sTwiceBaseCache;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label801;
               }

               var1[1] = var0;
               --var2;

               while(true) {
                  if (var2 < 2) {
                     try {
                        sTwiceBaseCache = var1;
                        ++sTwiceBaseCacheSize;
                        break;
                     } catch (Throwable var70) {
                        var10000 = var70;
                        var10001 = false;
                        break label801;
                     }
                  }

                  var1[var2] = null;
                  --var2;
               }
            }

            label788:
            try {
               return;
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label788;
            }
         }

         while(true) {
            var75 = var10000;

            try {
               throw var75;
            } catch (Throwable var67) {
               var10000 = var67;
               var10001 = false;
               continue;
            }
         }
      } else if (var0.length == 4) {
         synchronized(ArraySet.class){}

         label820: {
            label830: {
               try {
                  if (sBaseCacheSize >= 10) {
                     break label830;
                  }

                  var1[0] = sBaseCache;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label820;
               }

               var1[1] = var0;
               --var2;

               while(true) {
                  if (var2 < 2) {
                     try {
                        sBaseCache = var1;
                        ++sBaseCacheSize;
                        break;
                     } catch (Throwable var73) {
                        var10000 = var73;
                        var10001 = false;
                        break label820;
                     }
                  }

                  var1[var2] = null;
                  --var2;
               }
            }

            label807:
            try {
               return;
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label807;
            }
         }

         while(true) {
            var75 = var10000;

            try {
               throw var75;
            } catch (Throwable var68) {
               var10000 = var68;
               var10001 = false;
               continue;
            }
         }
      }
   }

   private MapCollections getCollection() {
      if (this.mCollections == null) {
         this.mCollections = new MapCollections() {
            protected void colClear() {
               ArraySet.this.clear();
            }

            protected Object colGetEntry(int var1, int var2) {
               return ArraySet.this.mArray[var1];
            }

            protected Map colGetMap() {
               throw new UnsupportedOperationException("not a map");
            }

            protected int colGetSize() {
               return ArraySet.this.mSize;
            }

            protected int colIndexOfKey(Object var1) {
               return ArraySet.this.indexOf(var1);
            }

            protected int colIndexOfValue(Object var1) {
               return ArraySet.this.indexOf(var1);
            }

            protected void colPut(Object var1, Object var2) {
               ArraySet.this.add(var1);
            }

            protected void colRemoveAt(int var1) {
               ArraySet.this.removeAt(var1);
            }

            protected Object colSetValue(int var1, Object var2) {
               throw new UnsupportedOperationException("not a map");
            }
         };
      }

      return this.mCollections;
   }

   private int indexOf(Object var1, int var2) {
      int var4 = this.mSize;
      if (var4 == 0) {
         return -1;
      } else {
         int var5 = ContainerHelpers.binarySearch(this.mHashes, var4, var2);
         if (var5 < 0) {
            return var5;
         } else if (var1.equals(this.mArray[var5])) {
            return var5;
         } else {
            int var3;
            for(var3 = var5 + 1; var3 < var4 && this.mHashes[var3] == var2; ++var3) {
               if (var1.equals(this.mArray[var3])) {
                  return var3;
               }
            }

            for(var4 = var5 - 1; var4 >= 0 && this.mHashes[var4] == var2; --var4) {
               if (var1.equals(this.mArray[var4])) {
                  return var4;
               }
            }

            return ~var3;
         }
      }
   }

   private int indexOfNull() {
      int var2 = this.mSize;
      if (var2 == 0) {
         return -1;
      } else {
         int var3 = ContainerHelpers.binarySearch(this.mHashes, var2, 0);
         if (var3 < 0) {
            return var3;
         } else if (this.mArray[var3] == null) {
            return var3;
         } else {
            int var1;
            for(var1 = var3 + 1; var1 < var2 && this.mHashes[var1] == 0; ++var1) {
               if (this.mArray[var1] == null) {
                  return var1;
               }
            }

            for(var2 = var3 - 1; var2 >= 0 && this.mHashes[var2] == 0; --var2) {
               if (this.mArray[var2] == null) {
                  return var2;
               }
            }

            return ~var1;
         }
      }
   }

   public boolean add(Object var1) {
      int var2;
      int var3;
      int var4;
      if (var1 == null) {
         var3 = 0;
         var4 = this.indexOfNull();
      } else {
         if (this.mIdentityHashCode) {
            var2 = System.identityHashCode(var1);
         } else {
            var2 = var1.hashCode();
         }

         var4 = this.indexOf(var1, var2);
         var3 = var2;
      }

      if (var4 >= 0) {
         return false;
      } else {
         var4 = ~var4;
         int var5 = this.mSize;
         int[] var6;
         if (var5 >= this.mHashes.length) {
            var2 = 4;
            if (var5 >= 8) {
               var2 = (var5 >> 1) + var5;
            } else if (var5 >= 4) {
               var2 = 8;
            }

            var6 = this.mHashes;
            Object[] var7 = this.mArray;
            this.allocArrays(var2);
            int[] var8 = this.mHashes;
            if (var8.length > 0) {
               System.arraycopy(var6, 0, var8, 0, var6.length);
               System.arraycopy(var7, 0, this.mArray, 0, var7.length);
            }

            freeArrays(var6, var7, this.mSize);
         }

         var2 = this.mSize;
         if (var4 < var2) {
            var6 = this.mHashes;
            System.arraycopy(var6, var4, var6, var4 + 1, var2 - var4);
            Object[] var9 = this.mArray;
            System.arraycopy(var9, var4, var9, var4 + 1, this.mSize - var4);
         }

         this.mHashes[var4] = var3;
         this.mArray[var4] = var1;
         ++this.mSize;
         return true;
      }
   }

   public void addAll(ArraySet var1) {
      int var3 = var1.mSize;
      this.ensureCapacity(this.mSize + var3);
      if (this.mSize == 0) {
         if (var3 > 0) {
            System.arraycopy(var1.mHashes, 0, this.mHashes, 0, var3);
            System.arraycopy(var1.mArray, 0, this.mArray, 0, var3);
            this.mSize = var3;
            return;
         }
      } else {
         for(int var2 = 0; var2 < var3; ++var2) {
            this.add(var1.valueAt(var2));
         }
      }

   }

   public boolean addAll(Collection var1) {
      this.ensureCapacity(this.mSize + var1.size());
      boolean var2 = false;

      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 |= this.add(var3.next())) {
      }

      return var2;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void append(Object var1) {
      int var3 = this.mSize;
      int var2;
      if (var1 == null) {
         var2 = 0;
      } else if (this.mIdentityHashCode) {
         var2 = System.identityHashCode(var1);
      } else {
         var2 = var1.hashCode();
      }

      int[] var4 = this.mHashes;
      if (var3 < var4.length) {
         if (var3 > 0 && var4[var3 - 1] > var2) {
            this.add(var1);
         } else {
            this.mSize = var3 + 1;
            this.mHashes[var3] = var2;
            this.mArray[var3] = var1;
         }
      } else {
         throw new IllegalStateException("Array is full");
      }
   }

   public void clear() {
      int var1 = this.mSize;
      if (var1 != 0) {
         freeArrays(this.mHashes, this.mArray, var1);
         this.mHashes = INT;
         this.mArray = OBJECT;
         this.mSize = 0;
      }

   }

   public boolean contains(Object var1) {
      return this.indexOf(var1) >= 0;
   }

   public boolean containsAll(Collection var1) {
      Iterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(this.contains(var2.next()));

      return false;
   }

   public void ensureCapacity(int var1) {
      if (this.mHashes.length < var1) {
         int[] var2 = this.mHashes;
         Object[] var3 = this.mArray;
         this.allocArrays(var1);
         var1 = this.mSize;
         if (var1 > 0) {
            System.arraycopy(var2, 0, this.mHashes, 0, var1);
            System.arraycopy(var3, 0, this.mArray, 0, this.mSize);
         }

         freeArrays(var2, var3, this.mSize);
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Set)) {
         return false;
      } else {
         Set var6 = (Set)var1;
         if (this.size() != var6.size()) {
            return false;
         } else {
            int var2 = 0;

            while(true) {
               boolean var3;
               try {
                  if (var2 >= this.mSize) {
                     return true;
                  }

                  var3 = var6.contains(this.valueAt(var2));
               } catch (NullPointerException var4) {
                  return false;
               } catch (ClassCastException var5) {
                  return false;
               }

               if (!var3) {
                  return false;
               }

               ++var2;
            }
         }
      }
   }

   public int hashCode() {
      int[] var4 = this.mHashes;
      int var2 = 0;
      int var1 = 0;

      for(int var3 = this.mSize; var1 < var3; ++var1) {
         var2 += var4[var1];
      }

      return var2;
   }

   public int indexOf(Object var1) {
      if (var1 == null) {
         return this.indexOfNull();
      } else {
         int var2;
         if (this.mIdentityHashCode) {
            var2 = System.identityHashCode(var1);
         } else {
            var2 = var1.hashCode();
         }

         return this.indexOf(var1, var2);
      }
   }

   public boolean isEmpty() {
      return this.mSize <= 0;
   }

   public Iterator iterator() {
      return this.getCollection().getKeySet().iterator();
   }

   public boolean remove(Object var1) {
      int var2 = this.indexOf(var1);
      if (var2 >= 0) {
         this.removeAt(var2);
         return true;
      } else {
         return false;
      }
   }

   public boolean removeAll(ArraySet var1) {
      int var3 = var1.mSize;
      int var4 = this.mSize;

      for(int var2 = 0; var2 < var3; ++var2) {
         this.remove(var1.valueAt(var2));
      }

      return var4 != this.mSize;
   }

   public boolean removeAll(Collection var1) {
      boolean var2 = false;

      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 |= this.remove(var3.next())) {
      }

      return var2;
   }

   public Object removeAt(int var1) {
      Object[] var6 = this.mArray;
      Object var5 = var6[var1];
      int var3 = this.mSize;
      if (var3 <= 1) {
         freeArrays(this.mHashes, var6, var3);
         this.mHashes = INT;
         this.mArray = OBJECT;
         this.mSize = 0;
         return var5;
      } else {
         int[] var8 = this.mHashes;
         int var4 = var8.length;
         int var2 = 8;
         if (var4 > 8 && var3 < var8.length / 3) {
            if (var3 > 8) {
               var2 = var3 + (var3 >> 1);
            }

            var8 = this.mHashes;
            Object[] var7 = this.mArray;
            this.allocArrays(var2);
            --this.mSize;
            if (var1 > 0) {
               System.arraycopy(var8, 0, this.mHashes, 0, var1);
               System.arraycopy(var7, 0, this.mArray, 0, var1);
            }

            var2 = this.mSize;
            if (var1 < var2) {
               System.arraycopy(var8, var1 + 1, this.mHashes, var1, var2 - var1);
               System.arraycopy(var7, var1 + 1, this.mArray, var1, this.mSize - var1);
            }

            return var5;
         } else {
            --this.mSize;
            var2 = this.mSize;
            if (var1 < var2) {
               var8 = this.mHashes;
               System.arraycopy(var8, var1 + 1, var8, var1, var2 - var1);
               var6 = this.mArray;
               System.arraycopy(var6, var1 + 1, var6, var1, this.mSize - var1);
            }

            this.mArray[this.mSize] = null;
            return var5;
         }
      }
   }

   public boolean retainAll(Collection var1) {
      boolean var3 = false;

      for(int var2 = this.mSize - 1; var2 >= 0; --var2) {
         if (!var1.contains(this.mArray[var2])) {
            this.removeAt(var2);
            var3 = true;
         }
      }

      return var3;
   }

   public int size() {
      return this.mSize;
   }

   public Object[] toArray() {
      int var1 = this.mSize;
      Object[] var2 = new Object[var1];
      System.arraycopy(this.mArray, 0, var2, 0, var1);
      return var2;
   }

   public Object[] toArray(Object[] var1) {
      Object[] var4 = var1;
      if (var1.length < this.mSize) {
         var4 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), this.mSize));
      }

      System.arraycopy(this.mArray, 0, var4, 0, this.mSize);
      int var2 = var4.length;
      int var3 = this.mSize;
      if (var2 > var3) {
         var4[var3] = null;
      }

      return var4;
   }

   public String toString() {
      if (this.isEmpty()) {
         return "{}";
      } else {
         StringBuilder var2 = new StringBuilder(this.mSize * 14);
         var2.append('{');

         for(int var1 = 0; var1 < this.mSize; ++var1) {
            if (var1 > 0) {
               var2.append(", ");
            }

            Object var3 = this.valueAt(var1);
            if (var3 != this) {
               var2.append(var3);
            } else {
               var2.append("(this Set)");
            }
         }

         var2.append('}');
         return var2.toString();
      }
   }

   public Object valueAt(int var1) {
      return this.mArray[var1];
   }
}
