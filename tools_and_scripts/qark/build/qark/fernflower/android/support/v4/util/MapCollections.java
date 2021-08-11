package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

abstract class MapCollections {
   MapCollections.EntrySet mEntrySet;
   MapCollections.KeySet mKeySet;
   MapCollections.ValuesCollection mValues;

   public static boolean containsAllHelper(Map var0, Collection var1) {
      Iterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(var0.containsKey(var2.next()));

      return false;
   }

   public static boolean equalsSetHelper(Set var0, Object var1) {
      if (var0 == var1) {
         return true;
      } else if (var1 instanceof Set) {
         Set var5 = (Set)var1;

         boolean var2;
         try {
            if (var0.size() != var5.size()) {
               return false;
            }

            var2 = var0.containsAll(var5);
         } catch (NullPointerException var3) {
            return false;
         } catch (ClassCastException var4) {
            return false;
         }

         if (var2) {
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean removeAllHelper(Map var0, Collection var1) {
      int var2 = var0.size();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         var0.remove(var3.next());
      }

      return var2 != var0.size();
   }

   public static boolean retainAllHelper(Map var0, Collection var1) {
      int var2 = var0.size();
      Iterator var3 = var0.keySet().iterator();

      while(var3.hasNext()) {
         if (!var1.contains(var3.next())) {
            var3.remove();
         }
      }

      if (var2 != var0.size()) {
         return true;
      } else {
         return false;
      }
   }

   protected abstract void colClear();

   protected abstract Object colGetEntry(int var1, int var2);

   protected abstract Map colGetMap();

   protected abstract int colGetSize();

   protected abstract int colIndexOfKey(Object var1);

   protected abstract int colIndexOfValue(Object var1);

   protected abstract void colPut(Object var1, Object var2);

   protected abstract void colRemoveAt(int var1);

   protected abstract Object colSetValue(int var1, Object var2);

   public Set getEntrySet() {
      if (this.mEntrySet == null) {
         this.mEntrySet = new MapCollections.EntrySet();
      }

      return this.mEntrySet;
   }

   public Set getKeySet() {
      if (this.mKeySet == null) {
         this.mKeySet = new MapCollections.KeySet();
      }

      return this.mKeySet;
   }

   public Collection getValues() {
      if (this.mValues == null) {
         this.mValues = new MapCollections.ValuesCollection();
      }

      return this.mValues;
   }

   public Object[] toArrayHelper(int var1) {
      int var3 = this.colGetSize();
      Object[] var4 = new Object[var3];

      for(int var2 = 0; var2 < var3; ++var2) {
         var4[var2] = this.colGetEntry(var2, var1);
      }

      return var4;
   }

   public Object[] toArrayHelper(Object[] var1, int var2) {
      int var4 = this.colGetSize();
      Object[] var5 = var1;
      if (var1.length < var4) {
         var5 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), var4));
      }

      for(int var3 = 0; var3 < var4; ++var3) {
         var5[var3] = this.colGetEntry(var3, var2);
      }

      if (var5.length > var4) {
         var5[var4] = null;
      }

      return var5;
   }

   final class ArrayIterator implements Iterator {
      boolean mCanRemove = false;
      int mIndex;
      final int mOffset;
      int mSize;

      ArrayIterator(int var2) {
         this.mOffset = var2;
         this.mSize = MapCollections.this.colGetSize();
      }

      public boolean hasNext() {
         return this.mIndex < this.mSize;
      }

      public Object next() {
         if (this.hasNext()) {
            Object var1 = MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
            ++this.mIndex;
            this.mCanRemove = true;
            return var1;
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         if (this.mCanRemove) {
            --this.mIndex;
            --this.mSize;
            this.mCanRemove = false;
            MapCollections.this.colRemoveAt(this.mIndex);
         } else {
            throw new IllegalStateException();
         }
      }
   }

   final class EntrySet implements Set {
      public boolean add(Entry var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection var1) {
         int var2 = MapCollections.this.colGetSize();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Entry var3 = (Entry)var4.next();
            MapCollections.this.colPut(var3.getKey(), var3.getValue());
         }

         return var2 != MapCollections.this.colGetSize();
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object var1) {
         if (!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var3 = (Entry)var1;
            int var2 = MapCollections.this.colIndexOfKey(var3.getKey());
            return var2 < 0 ? false : ContainerHelpers.equal(MapCollections.this.colGetEntry(var2, 1), var3.getValue());
         }
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

      public boolean equals(Object var1) {
         return MapCollections.equalsSetHelper(this, var1);
      }

      public int hashCode() {
         int var2 = 0;

         for(int var1 = MapCollections.this.colGetSize() - 1; var1 >= 0; --var1) {
            MapCollections var5 = MapCollections.this;
            int var4 = 0;
            Object var7 = var5.colGetEntry(var1, 0);
            Object var6 = MapCollections.this.colGetEntry(var1, 1);
            int var3;
            if (var7 == null) {
               var3 = 0;
            } else {
               var3 = var7.hashCode();
            }

            if (var6 != null) {
               var4 = var6.hashCode();
            }

            var2 += var4 ^ var3;
         }

         return var2;
      }

      public boolean isEmpty() {
         return MapCollections.this.colGetSize() == 0;
      }

      public Iterator iterator() {
         return MapCollections.this.new MapIterator();
      }

      public boolean remove(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         throw new UnsupportedOperationException();
      }

      public Object[] toArray(Object[] var1) {
         throw new UnsupportedOperationException();
      }
   }

   final class KeySet implements Set {
      public boolean add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object var1) {
         return MapCollections.this.colIndexOfKey(var1) >= 0;
      }

      public boolean containsAll(Collection var1) {
         return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), var1);
      }

      public boolean equals(Object var1) {
         return MapCollections.equalsSetHelper(this, var1);
      }

      public int hashCode() {
         int var2 = 0;

         for(int var1 = MapCollections.this.colGetSize() - 1; var1 >= 0; --var1) {
            MapCollections var4 = MapCollections.this;
            int var3 = 0;
            Object var5 = var4.colGetEntry(var1, 0);
            if (var5 != null) {
               var3 = var5.hashCode();
            }

            var2 += var3;
         }

         return var2;
      }

      public boolean isEmpty() {
         return MapCollections.this.colGetSize() == 0;
      }

      public Iterator iterator() {
         return MapCollections.this.new ArrayIterator(0);
      }

      public boolean remove(Object var1) {
         int var2 = MapCollections.this.colIndexOfKey(var1);
         if (var2 >= 0) {
            MapCollections.this.colRemoveAt(var2);
            return true;
         } else {
            return false;
         }
      }

      public boolean removeAll(Collection var1) {
         return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), var1);
      }

      public boolean retainAll(Collection var1) {
         return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), var1);
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         return MapCollections.this.toArrayHelper(0);
      }

      public Object[] toArray(Object[] var1) {
         return MapCollections.this.toArrayHelper(var1, 0);
      }
   }

   final class MapIterator implements Iterator, Entry {
      int mEnd = MapCollections.this.colGetSize() - 1;
      boolean mEntryValid = false;
      int mIndex = -1;

      public final boolean equals(Object var1) {
         if (this.mEntryValid) {
            if (!(var1 instanceof Entry)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               return ContainerHelpers.equal(var2.getKey(), MapCollections.this.colGetEntry(this.mIndex, 0)) && ContainerHelpers.equal(var2.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1));
            }
         } else {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         }
      }

      public Object getKey() {
         if (this.mEntryValid) {
            return MapCollections.this.colGetEntry(this.mIndex, 0);
         } else {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         }
      }

      public Object getValue() {
         if (this.mEntryValid) {
            return MapCollections.this.colGetEntry(this.mIndex, 1);
         } else {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         }
      }

      public boolean hasNext() {
         return this.mIndex < this.mEnd;
      }

      public final int hashCode() {
         if (this.mEntryValid) {
            MapCollections var3 = MapCollections.this;
            int var1 = this.mIndex;
            int var2 = 0;
            Object var5 = var3.colGetEntry(var1, 0);
            Object var4 = MapCollections.this.colGetEntry(this.mIndex, 1);
            if (var5 == null) {
               var1 = 0;
            } else {
               var1 = var5.hashCode();
            }

            if (var4 != null) {
               var2 = var4.hashCode();
            }

            return var2 ^ var1;
         } else {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         }
      }

      public Entry next() {
         if (this.hasNext()) {
            ++this.mIndex;
            this.mEntryValid = true;
            return this;
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         if (this.mEntryValid) {
            MapCollections.this.colRemoveAt(this.mIndex);
            --this.mIndex;
            --this.mEnd;
            this.mEntryValid = false;
         } else {
            throw new IllegalStateException();
         }
      }

      public Object setValue(Object var1) {
         if (this.mEntryValid) {
            return MapCollections.this.colSetValue(this.mIndex, var1);
         } else {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         }
      }

      public final String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.getKey());
         var1.append("=");
         var1.append(this.getValue());
         return var1.toString();
      }
   }

   final class ValuesCollection implements Collection {
      public boolean add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object var1) {
         return MapCollections.this.colIndexOfValue(var1) >= 0;
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

      public boolean isEmpty() {
         return MapCollections.this.colGetSize() == 0;
      }

      public Iterator iterator() {
         return MapCollections.this.new ArrayIterator(1);
      }

      public boolean remove(Object var1) {
         int var2 = MapCollections.this.colIndexOfValue(var1);
         if (var2 >= 0) {
            MapCollections.this.colRemoveAt(var2);
            return true;
         } else {
            return false;
         }
      }

      public boolean removeAll(Collection var1) {
         int var3 = MapCollections.this.colGetSize();
         boolean var6 = false;

         int var4;
         for(int var2 = 0; var2 < var3; var3 = var4) {
            var4 = var3;
            int var5 = var2;
            if (var1.contains(MapCollections.this.colGetEntry(var2, 1))) {
               MapCollections.this.colRemoveAt(var2);
               var5 = var2 - 1;
               var4 = var3 - 1;
               var6 = true;
            }

            var2 = var5 + 1;
         }

         return var6;
      }

      public boolean retainAll(Collection var1) {
         int var3 = MapCollections.this.colGetSize();
         boolean var6 = false;

         int var4;
         for(int var2 = 0; var2 < var3; var3 = var4) {
            var4 = var3;
            int var5 = var2;
            if (!var1.contains(MapCollections.this.colGetEntry(var2, 1))) {
               MapCollections.this.colRemoveAt(var2);
               var5 = var2 - 1;
               var4 = var3 - 1;
               var6 = true;
            }

            var2 = var5 + 1;
         }

         return var6;
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         return MapCollections.this.toArrayHelper(1);
      }

      public Object[] toArray(Object[] var1) {
         return MapCollections.this.toArrayHelper(var1, 1);
      }
   }
}
