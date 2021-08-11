package com.lti.utils.collections;

import java.util.ArrayList;
import java.util.List;

public class Queue {
   // $FF: renamed from: v java.util.List
   private List field_131 = new ArrayList();

   public Object dequeue() {
      Object var1 = this.field_131.get(0);
      this.field_131.remove(0);
      return var1;
   }

   public void enqueue(Object var1) {
      this.field_131.add(var1);
   }

   public boolean isEmpty() {
      return this.field_131.size() == 0;
   }

   public Object peek() {
      return this.field_131.size() == 0 ? null : this.field_131.get(0);
   }

   public void removeAllElements() {
      this.field_131.clear();
   }

   public int size() {
      return this.field_131.size();
   }
}
