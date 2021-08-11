package org.jsoup.helper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class DescendableLinkedList extends LinkedList {
   public Iterator descendingIterator() {
      return new DescendableLinkedList.DescendingIterator(this.size());
   }

   public Object peekLast() {
      return this.size() == 0 ? null : this.getLast();
   }

   public Object pollLast() {
      return this.size() == 0 ? null : this.removeLast();
   }

   public void push(Object var1) {
      this.addFirst(var1);
   }

   private class DescendingIterator implements Iterator {
      private final ListIterator iter;

      private DescendingIterator(int var2) {
         this.iter = DescendableLinkedList.this.listIterator(var2);
      }

      // $FF: synthetic method
      DescendingIterator(int var2, Object var3) {
         this(var2);
      }

      public boolean hasNext() {
         return this.iter.hasPrevious();
      }

      public Object next() {
         return this.iter.previous();
      }

      public void remove() {
         this.iter.remove();
      }
   }
}
