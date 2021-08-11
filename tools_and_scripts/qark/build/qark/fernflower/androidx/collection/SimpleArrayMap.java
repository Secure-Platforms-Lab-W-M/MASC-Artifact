package androidx.collection;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SimpleArrayMap {
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
   private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
   private static final boolean DEBUG = false;
   private static final String TAG = "ArrayMap";
   static Object[] mBaseCache;
   static int mBaseCacheSize;
   static Object[] mTwiceBaseCache;
   static int mTwiceBaseCacheSize;
   Object[] mArray;
   int[] mHashes;
   int mSize;

   public SimpleArrayMap() {
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      this.mSize = 0;
   }

   public SimpleArrayMap(int var1) {
      if (var1 == 0) {
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         this.allocArrays(var1);
      }

      this.mSize = 0;
   }

   public SimpleArrayMap(SimpleArrayMap var1) {
      this();
      if (var1 != null) {
         this.putAll(var1);
      }

   }

   private void allocArrays(int var1) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      label752: {
         Object[] var2;
         label758: {
            if (var1 == 8) {
               label754: {
                  synchronized(SimpleArrayMap.class){}

                  label735: {
                     label755: {
                        try {
                           if (mTwiceBaseCache == null) {
                              break label755;
                           }

                           var2 = mTwiceBaseCache;
                           this.mArray = var2;
                           mTwiceBaseCache = (Object[])((Object[])var2[0]);
                           this.mHashes = (int[])((int[])var2[1]);
                        } catch (Throwable var73) {
                           var10000 = var73;
                           var10001 = false;
                           break label735;
                        }

                        var2[1] = null;
                        var2[0] = null;

                        try {
                           --mTwiceBaseCacheSize;
                           return;
                        } catch (Throwable var69) {
                           var10000 = var69;
                           var10001 = false;
                           break label735;
                        }
                     }

                     label728:
                     try {
                        break label754;
                     } catch (Throwable var72) {
                        var10000 = var72;
                        var10001 = false;
                        break label728;
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
            } else if (var1 == 4) {
               synchronized(SimpleArrayMap.class){}

               try {
                  if (mBaseCache != null) {
                     var2 = mBaseCache;
                     this.mArray = var2;
                     mBaseCache = (Object[])((Object[])var2[0]);
                     this.mHashes = (int[])((int[])var2[1]);
                     break label758;
                  }
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label752;
               }

               try {
                  ;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label752;
               }
            }

            this.mHashes = new int[var1];
            this.mArray = new Object[var1 << 1];
            return;
         }

         var2[1] = null;
         var2[0] = null;

         label719:
         try {
            --mBaseCacheSize;
            return;
         } catch (Throwable var70) {
            var10000 = var70;
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

   private static int binarySearchHashes(int[] var0, int var1, int var2) {
      try {
         var1 = ContainerHelpers.binarySearch(var0, var1, var2);
         return var1;
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new ConcurrentModificationException();
      }
   }

   private static void freeArrays(int[] var0, Object[] var1, int var2) {
      Throwable var75;
      Throwable var10000;
      boolean var10001;
      if (var0.length == 8) {
         synchronized(SimpleArrayMap.class){}

         label801: {
            label828: {
               try {
                  if (mTwiceBaseCacheSize >= 10) {
                     break label828;
                  }

                  var1[0] = mTwiceBaseCache;
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break label801;
               }

               var1[1] = var0;
               var2 = (var2 << 1) - 1;

               while(true) {
                  if (var2 < 2) {
                     try {
                        mTwiceBaseCache = var1;
                        ++mTwiceBaseCacheSize;
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
            } catch (Throwable var68) {
               var10000 = var68;
               var10001 = false;
               continue;
            }
         }
      } else if (var0.length == 4) {
         synchronized(SimpleArrayMap.class){}

         label820: {
            label830: {
               try {
                  if (mBaseCacheSize >= 10) {
                     break label830;
                  }

                  var1[0] = mBaseCache;
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label820;
               }

               var1[1] = var0;
               var2 = (var2 << 1) - 1;

               while(true) {
                  if (var2 < 2) {
                     try {
                        mBaseCache = var1;
                        ++mBaseCacheSize;
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
            } catch (Throwable var67) {
               var10000 = var67;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void clear() {
      if (this.mSize > 0) {
         int[] var2 = this.mHashes;
         Object[] var3 = this.mArray;
         int var1 = this.mSize;
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
         freeArrays(var2, var3, var1);
      }

      if (this.mSize > 0) {
         throw new ConcurrentModificationException();
      }
   }

   public boolean containsKey(Object var1) {
      return this.indexOfKey(var1) >= 0;
   }

   public boolean containsValue(Object var1) {
      return this.indexOfValue(var1) >= 0;
   }

   public void ensureCapacity(int var1) {
      int var2 = this.mSize;
      if (this.mHashes.length < var1) {
         int[] var3 = this.mHashes;
         Object[] var4 = this.mArray;
         this.allocArrays(var1);
         if (this.mSize > 0) {
            System.arraycopy(var3, 0, this.mHashes, 0, var2);
            System.arraycopy(var4, 0, this.mArray, 0, var2 << 1);
         }

         freeArrays(var3, var4, var2);
      }

      if (this.mSize != var2) {
         throw new ConcurrentModificationException();
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         boolean var10001;
         int var2;
         boolean var3;
         Object var4;
         Object var5;
         Object var6;
         if (var1 instanceof SimpleArrayMap) {
            SimpleArrayMap var20 = (SimpleArrayMap)var1;
            if (this.size() != var20.size()) {
               return false;
            } else {
               var2 = 0;

               while(true) {
                  label100: {
                     label167: {
                        try {
                           if (var2 >= this.mSize) {
                              return true;
                           }

                           var4 = this.keyAt(var2);
                           var5 = this.valueAt(var2);
                           var6 = var20.get(var4);
                        } catch (NullPointerException var11) {
                           var10001 = false;
                           return false;
                        } catch (ClassCastException var12) {
                           var10001 = false;
                           return false;
                        }

                        if (var5 == null) {
                           if (var6 != null) {
                              return false;
                           }

                           try {
                              if (!var20.containsKey(var4)) {
                                 return false;
                              }
                              break label100;
                           } catch (NullPointerException var7) {
                              var10001 = false;
                              return false;
                           } catch (ClassCastException var8) {
                              var10001 = false;
                           }
                        } else {
                           try {
                              var3 = var5.equals(var6);
                              break label167;
                           } catch (NullPointerException var9) {
                              var10001 = false;
                              return false;
                           } catch (ClassCastException var10) {
                              var10001 = false;
                           }
                        }

                        return false;
                     }

                     if (!var3) {
                        return false;
                     }
                  }

                  ++var2;
               }
            }
         } else if (!(var1 instanceof Map)) {
            return false;
         } else {
            Map var19 = (Map)var1;
            if (this.size() != var19.size()) {
               return false;
            } else {
               var2 = 0;

               while(true) {
                  try {
                     if (var2 >= this.mSize) {
                        return true;
                     }

                     var4 = this.keyAt(var2);
                     var5 = this.valueAt(var2);
                     var6 = var19.get(var4);
                  } catch (NullPointerException var17) {
                     var10001 = false;
                     return false;
                  } catch (ClassCastException var18) {
                     var10001 = false;
                     return false;
                  }

                  if (var5 == null) {
                     if (var6 != null) {
                        return false;
                     }

                     try {
                        if (!var19.containsKey(var4)) {
                           return false;
                        }
                     } catch (NullPointerException var13) {
                        var10001 = false;
                        return false;
                     } catch (ClassCastException var14) {
                        var10001 = false;
                        return false;
                     }
                  } else {
                     try {
                        var3 = var5.equals(var6);
                     } catch (NullPointerException var15) {
                        var10001 = false;
                        return false;
                     } catch (ClassCastException var16) {
                        var10001 = false;
                        return false;
                     }

                     if (!var3) {
                        return false;
                     }
                  }

                  ++var2;
               }
            }
         }
      }
   }

   public Object get(Object var1) {
      return this.getOrDefault(var1, (Object)null);
   }

   public Object getOrDefault(Object var1, Object var2) {
      int var3 = this.indexOfKey(var1);
      return var3 >= 0 ? this.mArray[(var3 << 1) + 1] : var2;
   }

   public int hashCode() {
      int[] var7 = this.mHashes;
      Object[] var8 = this.mArray;
      int var3 = 0;
      int var2 = 0;
      int var1 = 1;

      for(int var5 = this.mSize; var2 < var5; var1 += 2) {
         Object var9 = var8[var1];
         int var6 = var7[var2];
         int var4;
         if (var9 == null) {
            var4 = 0;
         } else {
            var4 = var9.hashCode();
         }

         var3 += var6 ^ var4;
         ++var2;
      }

      return var3;
   }

   int indexOf(Object var1, int var2) {
      int var4 = this.mSize;
      if (var4 == 0) {
         return -1;
      } else {
         int var5 = binarySearchHashes(this.mHashes, var4, var2);
         if (var5 < 0) {
            return var5;
         } else if (var1.equals(this.mArray[var5 << 1])) {
            return var5;
         } else {
            int var3;
            for(var3 = var5 + 1; var3 < var4 && this.mHashes[var3] == var2; ++var3) {
               if (var1.equals(this.mArray[var3 << 1])) {
                  return var3;
               }
            }

            for(var4 = var5 - 1; var4 >= 0 && this.mHashes[var4] == var2; --var4) {
               if (var1.equals(this.mArray[var4 << 1])) {
                  return var4;
               }
            }

            return var3;
         }
      }
   }

   public int indexOfKey(Object var1) {
      return var1 == null ? this.indexOfNull() : this.indexOf(var1, var1.hashCode());
   }

   int indexOfNull() {
      int var2 = this.mSize;
      if (var2 == 0) {
         return -1;
      } else {
         int var3 = binarySearchHashes(this.mHashes, var2, 0);
         if (var3 < 0) {
            return var3;
         } else if (this.mArray[var3 << 1] == null) {
            return var3;
         } else {
            int var1;
            for(var1 = var3 + 1; var1 < var2 && this.mHashes[var1] == 0; ++var1) {
               if (this.mArray[var1 << 1] == null) {
                  return var1;
               }
            }

            for(var2 = var3 - 1; var2 >= 0 && this.mHashes[var2] == 0; --var2) {
               if (this.mArray[var2 << 1] == null) {
                  return var2;
               }
            }

            return var1;
         }
      }
   }

   int indexOfValue(Object var1) {
      int var3 = this.mSize * 2;
      Object[] var4 = this.mArray;
      int var2;
      if (var1 == null) {
         for(var2 = 1; var2 < var3; var2 += 2) {
            if (var4[var2] == null) {
               return var2 >> 1;
            }
         }
      } else {
         for(var2 = 1; var2 < var3; var2 += 2) {
            if (var1.equals(var4[var2])) {
               return var2 >> 1;
            }
         }
      }

      return -1;
   }

   public boolean isEmpty() {
      return this.mSize <= 0;
   }

   public Object keyAt(int var1) {
      return this.mArray[var1 << 1];
   }

   public Object put(Object var1, Object var2) {
      int var5 = this.mSize;
      int var3;
      int var4;
      if (var1 == null) {
         var4 = 0;
         var3 = this.indexOfNull();
      } else {
         var4 = var1.hashCode();
         var3 = this.indexOf(var1, var4);
      }

      if (var3 >= 0) {
         var3 = (var3 << 1) + 1;
         Object[] var10 = this.mArray;
         Object var12 = var10[var3];
         var10[var3] = var2;
         return var12;
      } else {
         int var6 = var3;
         int[] var7;
         if (var5 >= this.mHashes.length) {
            var3 = 4;
            if (var5 >= 8) {
               var3 = (var5 >> 1) + var5;
            } else if (var5 >= 4) {
               var3 = 8;
            }

            var7 = this.mHashes;
            Object[] var8 = this.mArray;
            this.allocArrays(var3);
            if (var5 != this.mSize) {
               throw new ConcurrentModificationException();
            }

            int[] var9 = this.mHashes;
            if (var9.length > 0) {
               System.arraycopy(var7, 0, var9, 0, var7.length);
               System.arraycopy(var8, 0, this.mArray, 0, var8.length);
            }

            freeArrays(var7, var8, var5);
         }

         Object[] var11;
         if (var3 < var5) {
            var7 = this.mHashes;
            System.arraycopy(var7, var3, var7, var3 + 1, var5 - var3);
            var11 = this.mArray;
            System.arraycopy(var11, var3 << 1, var11, var3 + 1 << 1, this.mSize - var3 << 1);
         }

         var3 = this.mSize;
         if (var5 == var3) {
            var7 = this.mHashes;
            if (var6 < var7.length) {
               var7[var6] = var4;
               var11 = this.mArray;
               var11[var6 << 1] = var1;
               var11[(var6 << 1) + 1] = var2;
               this.mSize = var3 + 1;
               return null;
            }
         }

         throw new ConcurrentModificationException();
      }
   }

   public void putAll(SimpleArrayMap var1) {
      int var3 = var1.mSize;
      this.ensureCapacity(this.mSize + var3);
      if (this.mSize == 0) {
         if (var3 > 0) {
            System.arraycopy(var1.mHashes, 0, this.mHashes, 0, var3);
            System.arraycopy(var1.mArray, 0, this.mArray, 0, var3 << 1);
            this.mSize = var3;
            return;
         }
      } else {
         for(int var2 = 0; var2 < var3; ++var2) {
            this.put(var1.keyAt(var2), var1.valueAt(var2));
         }
      }

   }

   public Object putIfAbsent(Object var1, Object var2) {
      Object var4 = this.get(var1);
      Object var3 = var4;
      if (var4 == null) {
         var3 = this.put(var1, var2);
      }

      return var3;
   }

   public Object remove(Object var1) {
      int var2 = this.indexOfKey(var1);
      return var2 >= 0 ? this.removeAt(var2) : null;
   }

   public boolean remove(Object var1, Object var2) {
      int var3 = this.indexOfKey(var1);
      if (var3 >= 0) {
         var1 = this.valueAt(var3);
         if (var2 == var1 || var2 != null && var2.equals(var1)) {
            this.removeAt(var3);
            return true;
         }
      }

      return false;
   }

   public Object removeAt(int var1) {
      Object[] var7 = this.mArray;
      Object var6 = var7[(var1 << 1) + 1];
      int var4 = this.mSize;
      if (var4 <= 1) {
         freeArrays(this.mHashes, var7, var4);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         var1 = 0;
      } else {
         int var3 = var4 - 1;
         int[] var9 = this.mHashes;
         int var5 = var9.length;
         int var2 = 8;
         if (var5 > 8 && this.mSize < var9.length / 3) {
            if (var4 > 8) {
               var2 = var4 + (var4 >> 1);
            }

            var9 = this.mHashes;
            Object[] var8 = this.mArray;
            this.allocArrays(var2);
            if (var4 != this.mSize) {
               throw new ConcurrentModificationException();
            }

            if (var1 > 0) {
               System.arraycopy(var9, 0, this.mHashes, 0, var1);
               System.arraycopy(var8, 0, this.mArray, 0, var1 << 1);
            }

            if (var1 < var3) {
               System.arraycopy(var9, var1 + 1, this.mHashes, var1, var3 - var1);
               System.arraycopy(var8, var1 + 1 << 1, this.mArray, var1 << 1, var3 - var1 << 1);
            }

            var1 = var3;
         } else {
            if (var1 < var3) {
               var9 = this.mHashes;
               System.arraycopy(var9, var1 + 1, var9, var1, var3 - var1);
               var7 = this.mArray;
               System.arraycopy(var7, var1 + 1 << 1, var7, var1 << 1, var3 - var1 << 1);
            }

            var7 = this.mArray;
            var7[var3 << 1] = null;
            var7[(var3 << 1) + 1] = null;
            var1 = var3;
         }
      }

      if (var4 == this.mSize) {
         this.mSize = var1;
         return var6;
      } else {
         throw new ConcurrentModificationException();
      }
   }

   public Object replace(Object var1, Object var2) {
      int var3 = this.indexOfKey(var1);
      return var3 >= 0 ? this.setValueAt(var3, var2) : null;
   }

   public boolean replace(Object var1, Object var2, Object var3) {
      int var4 = this.indexOfKey(var1);
      if (var4 >= 0) {
         var1 = this.valueAt(var4);
         if (var1 == var2 || var2 != null && var2.equals(var1)) {
            this.setValueAt(var4, var3);
            return true;
         }
      }

      return false;
   }

   public Object setValueAt(int var1, Object var2) {
      var1 = (var1 << 1) + 1;
      Object[] var3 = this.mArray;
      Object var4 = var3[var1];
      var3[var1] = var2;
      return var4;
   }

   public int size() {
      return this.mSize;
   }

   public String toString() {
      if (this.isEmpty()) {
         return "{}";
      } else {
         StringBuilder var2 = new StringBuilder(this.mSize * 28);
         var2.append('{');

         for(int var1 = 0; var1 < this.mSize; ++var1) {
            if (var1 > 0) {
               var2.append(", ");
            }

            Object var3 = this.keyAt(var1);
            if (var3 != this) {
               var2.append(var3);
            } else {
               var2.append("(this Map)");
            }

            var2.append('=');
            var3 = this.valueAt(var1);
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
      return this.mArray[(var1 << 1) + 1];
   }
}
