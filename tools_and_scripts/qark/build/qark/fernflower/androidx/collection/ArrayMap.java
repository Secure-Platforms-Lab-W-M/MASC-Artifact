package androidx.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ArrayMap extends SimpleArrayMap implements Map {
   MapCollections mCollections;

   public ArrayMap() {
   }

   public ArrayMap(int var1) {
      super(var1);
   }

   public ArrayMap(SimpleArrayMap var1) {
      super(var1);
   }

   private MapCollections getCollection() {
      if (this.mCollections == null) {
         this.mCollections = new MapCollections() {
            protected void colClear() {
               ArrayMap.this.clear();
            }

            protected Object colGetEntry(int var1, int var2) {
               return ArrayMap.this.mArray[(var1 << 1) + var2];
            }

            protected Map colGetMap() {
               return ArrayMap.this;
            }

            protected int colGetSize() {
               return ArrayMap.this.mSize;
            }

            protected int colIndexOfKey(Object var1) {
               return ArrayMap.this.indexOfKey(var1);
            }

            protected int colIndexOfValue(Object var1) {
               return ArrayMap.this.indexOfValue(var1);
            }

            protected void colPut(Object var1, Object var2) {
               ArrayMap.this.put(var1, var2);
            }

            protected void colRemoveAt(int var1) {
               ArrayMap.this.removeAt(var1);
            }

            protected Object colSetValue(int var1, Object var2) {
               return ArrayMap.this.setValueAt(var1, var2);
            }
         };
      }

      return this.mCollections;
   }

   public boolean containsAll(Collection var1) {
      return MapCollections.containsAllHelper(this, var1);
   }

   public Set entrySet() {
      return this.getCollection().getEntrySet();
   }

   public Set keySet() {
      return this.getCollection().getKeySet();
   }

   public void putAll(Map var1) {
      this.ensureCapacity(this.mSize + var1.size());
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         this.put(var2.getKey(), var2.getValue());
      }

   }

   public boolean removeAll(Collection var1) {
      return MapCollections.removeAllHelper(this, var1);
   }

   public boolean retainAll(Collection var1) {
      return MapCollections.retainAllHelper(this, var1);
   }

   public Collection values() {
      return this.getCollection().getValues();
   }
}
