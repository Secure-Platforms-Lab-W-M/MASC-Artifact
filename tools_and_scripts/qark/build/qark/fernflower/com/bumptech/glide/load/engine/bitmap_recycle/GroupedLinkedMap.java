package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GroupedLinkedMap {
   private final GroupedLinkedMap.LinkedEntry head = new GroupedLinkedMap.LinkedEntry();
   private final Map keyToEntry = new HashMap();

   private void makeHead(GroupedLinkedMap.LinkedEntry var1) {
      removeEntry(var1);
      var1.prev = this.head;
      var1.next = this.head.next;
      updateEntry(var1);
   }

   private void makeTail(GroupedLinkedMap.LinkedEntry var1) {
      removeEntry(var1);
      var1.prev = this.head.prev;
      var1.next = this.head;
      updateEntry(var1);
   }

   private static void removeEntry(GroupedLinkedMap.LinkedEntry var0) {
      var0.prev.next = var0.next;
      var0.next.prev = var0.prev;
   }

   private static void updateEntry(GroupedLinkedMap.LinkedEntry var0) {
      var0.next.prev = var0;
      var0.prev.next = var0;
   }

   public Object get(Poolable var1) {
      GroupedLinkedMap.LinkedEntry var2 = (GroupedLinkedMap.LinkedEntry)this.keyToEntry.get(var1);
      GroupedLinkedMap.LinkedEntry var3;
      if (var2 == null) {
         var2 = new GroupedLinkedMap.LinkedEntry(var1);
         this.keyToEntry.put(var1, var2);
         var3 = var2;
      } else {
         var1.offer();
         var3 = var2;
      }

      this.makeHead(var3);
      return var3.removeLast();
   }

   public void put(Poolable var1, Object var2) {
      GroupedLinkedMap.LinkedEntry var3 = (GroupedLinkedMap.LinkedEntry)this.keyToEntry.get(var1);
      GroupedLinkedMap.LinkedEntry var4;
      if (var3 == null) {
         var3 = new GroupedLinkedMap.LinkedEntry(var1);
         this.makeTail(var3);
         this.keyToEntry.put(var1, var3);
         var4 = var3;
      } else {
         var1.offer();
         var4 = var3;
      }

      var4.add(var2);
   }

   public Object removeLast() {
      for(GroupedLinkedMap.LinkedEntry var1 = this.head.prev; !var1.equals(this.head); var1 = var1.prev) {
         Object var2 = var1.removeLast();
         if (var2 != null) {
            return var2;
         }

         removeEntry(var1);
         this.keyToEntry.remove(var1.key);
         ((Poolable)var1.key).offer();
      }

      return null;
   }

   public String toString() {
      StringBuilder var3 = new StringBuilder("GroupedLinkedMap( ");
      GroupedLinkedMap.LinkedEntry var2 = this.head.next;

      boolean var1;
      for(var1 = false; !var2.equals(this.head); var2 = var2.next) {
         var1 = true;
         var3.append('{');
         var3.append(var2.key);
         var3.append(':');
         var3.append(var2.size());
         var3.append("}, ");
      }

      if (var1) {
         var3.delete(var3.length() - 2, var3.length());
      }

      var3.append(" )");
      return var3.toString();
   }

   private static class LinkedEntry {
      final Object key;
      GroupedLinkedMap.LinkedEntry next;
      GroupedLinkedMap.LinkedEntry prev;
      private List values;

      LinkedEntry() {
         this((Object)null);
      }

      LinkedEntry(Object var1) {
         this.prev = this;
         this.next = this;
         this.key = var1;
      }

      public void add(Object var1) {
         if (this.values == null) {
            this.values = new ArrayList();
         }

         this.values.add(var1);
      }

      public Object removeLast() {
         int var1 = this.size();
         return var1 > 0 ? this.values.remove(var1 - 1) : null;
      }

      public int size() {
         List var1 = this.values;
         return var1 != null ? var1.size() : 0;
      }
   }
}
