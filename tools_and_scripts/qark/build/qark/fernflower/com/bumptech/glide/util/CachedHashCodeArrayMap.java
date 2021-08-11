package com.bumptech.glide.util;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

public final class CachedHashCodeArrayMap extends ArrayMap {
   private int hashCode;

   public void clear() {
      this.hashCode = 0;
      super.clear();
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = super.hashCode();
      }

      return this.hashCode;
   }

   public Object put(Object var1, Object var2) {
      this.hashCode = 0;
      return super.put(var1, var2);
   }

   public void putAll(SimpleArrayMap var1) {
      this.hashCode = 0;
      super.putAll(var1);
   }

   public Object removeAt(int var1) {
      this.hashCode = 0;
      return super.removeAt(var1);
   }

   public Object setValueAt(int var1, Object var2) {
      this.hashCode = 0;
      return super.setValueAt(var1, var2);
   }
}
