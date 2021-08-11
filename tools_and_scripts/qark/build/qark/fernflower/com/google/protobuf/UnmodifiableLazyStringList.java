package com.google.protobuf;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public class UnmodifiableLazyStringList extends AbstractList implements LazyStringList, RandomAccess {
   private final LazyStringList list;

   public UnmodifiableLazyStringList(LazyStringList var1) {
      this.list = var1;
   }

   public void add(ByteString var1) {
      throw new UnsupportedOperationException();
   }

   public String get(int var1) {
      return (String)this.list.get(var1);
   }

   public ByteString getByteString(int var1) {
      return this.list.getByteString(var1);
   }

   public List getUnderlyingElements() {
      return this.list.getUnderlyingElements();
   }

   public Iterator iterator() {
      return new Iterator() {
         Iterator iter;

         {
            this.iter = UnmodifiableLazyStringList.this.list.iterator();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public String next() {
            return (String)this.iter.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public ListIterator listIterator(final int var1) {
      return new ListIterator() {
         ListIterator iter;

         {
            this.iter = UnmodifiableLazyStringList.this.list.listIterator(var1);
         }

         public void add(String var1x) {
            throw new UnsupportedOperationException();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public boolean hasPrevious() {
            return this.iter.hasPrevious();
         }

         public String next() {
            return (String)this.iter.next();
         }

         public int nextIndex() {
            return this.iter.nextIndex();
         }

         public String previous() {
            return (String)this.iter.previous();
         }

         public int previousIndex() {
            return this.iter.previousIndex();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         public void set(String var1x) {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int size() {
      return this.list.size();
   }
}
