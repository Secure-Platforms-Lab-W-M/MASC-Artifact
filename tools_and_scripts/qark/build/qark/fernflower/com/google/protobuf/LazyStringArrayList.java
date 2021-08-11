package com.google.protobuf;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public class LazyStringArrayList extends AbstractList implements LazyStringList, RandomAccess {
   public static final LazyStringList EMPTY = new UnmodifiableLazyStringList(new LazyStringArrayList());
   private final List list;

   public LazyStringArrayList() {
      this.list = new ArrayList();
   }

   public LazyStringArrayList(LazyStringList var1) {
      this.list = new ArrayList(var1.size());
      this.addAll(var1);
   }

   public LazyStringArrayList(List var1) {
      this.list = new ArrayList(var1);
   }

   private String asString(Object var1) {
      return var1 instanceof String ? (String)var1 : ((ByteString)var1).toStringUtf8();
   }

   public void add(int var1, String var2) {
      this.list.add(var1, var2);
      ++this.modCount;
   }

   public void add(ByteString var1) {
      this.list.add(var1);
      ++this.modCount;
   }

   public boolean addAll(int var1, Collection var2) {
      if (var2 instanceof LazyStringList) {
         var2 = ((LazyStringList)var2).getUnderlyingElements();
      }

      boolean var3 = this.list.addAll(var1, (Collection)var2);
      ++this.modCount;
      return var3;
   }

   public boolean addAll(Collection var1) {
      return this.addAll(this.size(), var1);
   }

   public void clear() {
      this.list.clear();
      ++this.modCount;
   }

   public String get(int var1) {
      Object var2 = this.list.get(var1);
      if (var2 instanceof String) {
         return (String)var2;
      } else {
         ByteString var4 = (ByteString)var2;
         String var3 = var4.toStringUtf8();
         if (var4.isValidUtf8()) {
            this.list.set(var1, var3);
         }

         return var3;
      }
   }

   public ByteString getByteString(int var1) {
      Object var2 = this.list.get(var1);
      if (var2 instanceof String) {
         ByteString var3 = ByteString.copyFromUtf8((String)var2);
         this.list.set(var1, var3);
         return var3;
      } else {
         return (ByteString)var2;
      }
   }

   public List getUnderlyingElements() {
      return Collections.unmodifiableList(this.list);
   }

   public String remove(int var1) {
      Object var2 = this.list.remove(var1);
      ++this.modCount;
      return this.asString(var2);
   }

   public String set(int var1, String var2) {
      return this.asString(this.list.set(var1, var2));
   }

   public int size() {
      return this.list.size();
   }
}
