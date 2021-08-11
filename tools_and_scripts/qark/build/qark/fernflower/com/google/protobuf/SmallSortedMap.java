package com.google.protobuf;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

class SmallSortedMap extends AbstractMap {
   private List entryList;
   private boolean isImmutable;
   private volatile SmallSortedMap.EntrySet lazyEntrySet;
   private final int maxArraySize;
   private Map overflowEntries;

   private SmallSortedMap(int var1) {
      this.maxArraySize = var1;
      this.entryList = Collections.emptyList();
      this.overflowEntries = Collections.emptyMap();
   }

   // $FF: synthetic method
   SmallSortedMap(int var1, Object var2) {
      this(var1);
   }

   private int binarySearchInArray(Comparable var1) {
      byte var4 = 0;
      int var5 = this.entryList.size() - 1;
      int var2 = var4;
      int var3 = var5;
      if (var5 >= 0) {
         int var6 = var1.compareTo(((SmallSortedMap.Entry)this.entryList.get(var5)).getKey());
         if (var6 > 0) {
            return -(var5 + 2);
         }

         var2 = var4;
         var3 = var5;
         if (var6 == 0) {
            return var5;
         }
      }

      while(var2 <= var3) {
         int var7 = (var2 + var3) / 2;
         var5 = var1.compareTo(((SmallSortedMap.Entry)this.entryList.get(var7)).getKey());
         if (var5 < 0) {
            var3 = var7 - 1;
         } else {
            if (var5 <= 0) {
               return var7;
            }

            var2 = var7 + 1;
         }
      }

      return -(var2 + 1);
   }

   private void checkMutable() {
      if (this.isImmutable) {
         throw new UnsupportedOperationException();
      }
   }

   private void ensureEntryArrayMutable() {
      this.checkMutable();
      if (this.entryList.isEmpty() && !(this.entryList instanceof ArrayList)) {
         this.entryList = new ArrayList(this.maxArraySize);
      }

   }

   private SortedMap getOverflowEntriesMutable() {
      this.checkMutable();
      if (this.overflowEntries.isEmpty() && !(this.overflowEntries instanceof TreeMap)) {
         this.overflowEntries = new TreeMap();
      }

      return (SortedMap)this.overflowEntries;
   }

   static SmallSortedMap newFieldMap(int var0) {
      return new SmallSortedMap(var0) {
         public void makeImmutable() {
            if (!this.isImmutable()) {
               for(int var1 = 0; var1 < this.getNumArrayEntries(); ++var1) {
                  java.util.Map.Entry var2 = this.getArrayEntryAt(var1);
                  if (((FieldSet.FieldDescriptorLite)var2.getKey()).isRepeated()) {
                     var2.setValue(Collections.unmodifiableList((List)var2.getValue()));
                  }
               }

               Iterator var4 = this.getOverflowEntries().iterator();

               while(var4.hasNext()) {
                  java.util.Map.Entry var3 = (java.util.Map.Entry)var4.next();
                  if (((FieldSet.FieldDescriptorLite)var3.getKey()).isRepeated()) {
                     var3.setValue(Collections.unmodifiableList((List)var3.getValue()));
                  }
               }
            }

            super.makeImmutable();
         }
      };
   }

   static SmallSortedMap newInstanceForTest(int var0) {
      return new SmallSortedMap(var0);
   }

   private Object removeArrayEntryAt(int var1) {
      this.checkMutable();
      Object var2 = ((SmallSortedMap.Entry)this.entryList.remove(var1)).getValue();
      if (!this.overflowEntries.isEmpty()) {
         Iterator var3 = this.getOverflowEntriesMutable().entrySet().iterator();
         this.entryList.add(new SmallSortedMap.Entry((java.util.Map.Entry)var3.next()));
         var3.remove();
      }

      return var2;
   }

   public void clear() {
      this.checkMutable();
      if (!this.entryList.isEmpty()) {
         this.entryList.clear();
      }

      if (!this.overflowEntries.isEmpty()) {
         this.overflowEntries.clear();
      }

   }

   public boolean containsKey(Object var1) {
      Comparable var2 = (Comparable)var1;
      return this.binarySearchInArray(var2) >= 0 || this.overflowEntries.containsKey(var2);
   }

   public Set entrySet() {
      if (this.lazyEntrySet == null) {
         this.lazyEntrySet = new SmallSortedMap.EntrySet();
      }

      return this.lazyEntrySet;
   }

   public Object get(Object var1) {
      Comparable var3 = (Comparable)var1;
      int var2 = this.binarySearchInArray(var3);
      return var2 >= 0 ? ((SmallSortedMap.Entry)this.entryList.get(var2)).getValue() : this.overflowEntries.get(var3);
   }

   public java.util.Map.Entry getArrayEntryAt(int var1) {
      return (java.util.Map.Entry)this.entryList.get(var1);
   }

   public int getNumArrayEntries() {
      return this.entryList.size();
   }

   public int getNumOverflowEntries() {
      return this.overflowEntries.size();
   }

   public Iterable getOverflowEntries() {
      return (Iterable)(this.overflowEntries.isEmpty() ? SmallSortedMap.EmptySet.iterable() : this.overflowEntries.entrySet());
   }

   public boolean isImmutable() {
      return this.isImmutable;
   }

   public void makeImmutable() {
      if (!this.isImmutable) {
         Map var1;
         if (this.overflowEntries.isEmpty()) {
            var1 = Collections.emptyMap();
         } else {
            var1 = Collections.unmodifiableMap(this.overflowEntries);
         }

         this.overflowEntries = var1;
         this.isImmutable = true;
      }

   }

   public Object put(Comparable var1, Object var2) {
      this.checkMutable();
      int var3 = this.binarySearchInArray(var1);
      if (var3 >= 0) {
         return ((SmallSortedMap.Entry)this.entryList.get(var3)).setValue(var2);
      } else {
         this.ensureEntryArrayMutable();
         var3 = -(var3 + 1);
         if (var3 >= this.maxArraySize) {
            return this.getOverflowEntriesMutable().put(var1, var2);
         } else {
            int var4 = this.entryList.size();
            int var5 = this.maxArraySize;
            if (var4 == var5) {
               SmallSortedMap.Entry var6 = (SmallSortedMap.Entry)this.entryList.remove(var5 - 1);
               this.getOverflowEntriesMutable().put(var6.getKey(), var6.getValue());
            }

            this.entryList.add(var3, new SmallSortedMap.Entry(var1, var2));
            return null;
         }
      }
   }

   public Object remove(Object var1) {
      this.checkMutable();
      Comparable var3 = (Comparable)var1;
      int var2 = this.binarySearchInArray(var3);
      if (var2 >= 0) {
         return this.removeArrayEntryAt(var2);
      } else {
         return this.overflowEntries.isEmpty() ? null : this.overflowEntries.remove(var3);
      }
   }

   public int size() {
      return this.entryList.size() + this.overflowEntries.size();
   }

   private static class EmptySet {
      private static final Iterable ITERABLE = new Iterable() {
         public Iterator iterator() {
            return SmallSortedMap.EmptySet.ITERATOR;
         }
      };
      private static final Iterator ITERATOR = new Iterator() {
         public boolean hasNext() {
            return false;
         }

         public Object next() {
            throw new NoSuchElementException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };

      static Iterable iterable() {
         return ITERABLE;
      }
   }

   private class Entry implements java.util.Map.Entry, Comparable {
      private final Comparable key;
      private Object value;

      Entry(Comparable var2, Object var3) {
         this.key = var2;
         this.value = var3;
      }

      Entry(java.util.Map.Entry var2) {
         this((Comparable)var2.getKey(), var2.getValue());
      }

      private boolean equals(Object var1, Object var2) {
         if (var1 == null) {
            return var2 == null;
         } else {
            return var1.equals(var2);
         }
      }

      public int compareTo(SmallSortedMap.Entry var1) {
         return this.getKey().compareTo(var1.getKey());
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof java.util.Map.Entry)) {
            return false;
         } else {
            java.util.Map.Entry var2 = (java.util.Map.Entry)var1;
            return this.equals(this.key, var2.getKey()) && this.equals(this.value, var2.getValue());
         }
      }

      public Comparable getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public int hashCode() {
         Comparable var3 = this.key;
         int var2 = 0;
         int var1;
         if (var3 == null) {
            var1 = 0;
         } else {
            var1 = var3.hashCode();
         }

         Object var4 = this.value;
         if (var4 != null) {
            var2 = var4.hashCode();
         }

         return var1 ^ var2;
      }

      public Object setValue(Object var1) {
         SmallSortedMap.this.checkMutable();
         Object var2 = this.value;
         this.value = var1;
         return var2;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.key);
         var1.append("=");
         var1.append(this.value);
         return var1.toString();
      }
   }

   private class EntryIterator implements Iterator {
      private Iterator lazyOverflowIterator;
      private boolean nextCalledBeforeRemove;
      private int pos;

      private EntryIterator() {
         this.pos = -1;
      }

      // $FF: synthetic method
      EntryIterator(Object var2) {
         this();
      }

      private Iterator getOverflowIterator() {
         if (this.lazyOverflowIterator == null) {
            this.lazyOverflowIterator = SmallSortedMap.this.overflowEntries.entrySet().iterator();
         }

         return this.lazyOverflowIterator;
      }

      public boolean hasNext() {
         int var1 = this.pos;
         boolean var2 = true;
         if (var1 + 1 >= SmallSortedMap.this.entryList.size()) {
            if (this.getOverflowIterator().hasNext()) {
               return true;
            }

            var2 = false;
         }

         return var2;
      }

      public java.util.Map.Entry next() {
         this.nextCalledBeforeRemove = true;
         int var1 = this.pos + 1;
         this.pos = var1;
         return var1 < SmallSortedMap.this.entryList.size() ? (java.util.Map.Entry)SmallSortedMap.this.entryList.get(this.pos) : (java.util.Map.Entry)this.getOverflowIterator().next();
      }

      public void remove() {
         if (this.nextCalledBeforeRemove) {
            this.nextCalledBeforeRemove = false;
            SmallSortedMap.this.checkMutable();
            if (this.pos < SmallSortedMap.this.entryList.size()) {
               SmallSortedMap var2 = SmallSortedMap.this;
               int var1 = this.pos--;
               var2.removeArrayEntryAt(var1);
            } else {
               this.getOverflowIterator().remove();
            }
         } else {
            throw new IllegalStateException("remove() was called before next()");
         }
      }
   }

   private class EntrySet extends AbstractSet {
      private EntrySet() {
      }

      // $FF: synthetic method
      EntrySet(Object var2) {
         this();
      }

      public boolean add(java.util.Map.Entry var1) {
         if (!this.contains(var1)) {
            SmallSortedMap.this.put((Comparable)var1.getKey(), var1.getValue());
            return true;
         } else {
            return false;
         }
      }

      public void clear() {
         SmallSortedMap.this.clear();
      }

      public boolean contains(Object var1) {
         java.util.Map.Entry var2 = (java.util.Map.Entry)var1;
         var1 = SmallSortedMap.this.get(var2.getKey());
         Object var3 = var2.getValue();
         return var1 == var3 || var1 != null && var1.equals(var3);
      }

      public Iterator iterator() {
         return SmallSortedMap.this.new EntryIterator();
      }

      public boolean remove(Object var1) {
         java.util.Map.Entry var2 = (java.util.Map.Entry)var1;
         if (this.contains(var2)) {
            SmallSortedMap.this.remove(var2.getKey());
            return true;
         } else {
            return false;
         }
      }

      public int size() {
         return SmallSortedMap.this.size();
      }
   }
}
