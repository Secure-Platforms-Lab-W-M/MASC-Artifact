package util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class LRUCache extends LinkedHashMap {
   int MAX_ENTRIES;

   public LRUCache(int var1) {
      super(var1 + 1);
      this.MAX_ENTRIES = var1;
   }

   public void clear() {
      synchronized(this){}

      try {
         super.clear();
      } finally {
         ;
      }

   }

   public boolean containsKey(Object var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = super.containsKey(var1);
      } finally {
         ;
      }

      return var2;
   }

   public boolean containsValue(Object var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = super.containsValue(var1);
      } finally {
         ;
      }

      return var2;
   }

   public Set entrySet() {
      synchronized(this){}

      Set var1;
      try {
         var1 = super.entrySet();
      } finally {
         ;
      }

      return var1;
   }

   public boolean equals(Object var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = super.equals(var1);
      } finally {
         ;
      }

      return var2;
   }

   public Object get(Object var1) {
      synchronized(this){}

      try {
         var1 = super.get(var1);
      } finally {
         ;
      }

      return var1;
   }

   public int hashCode() {
      synchronized(this){}

      int var1;
      try {
         var1 = super.hashCode();
      } finally {
         ;
      }

      return var1;
   }

   public boolean isEmpty() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = super.isEmpty();
      } finally {
         ;
      }

      return var1;
   }

   public Set keySet() {
      synchronized(this){}

      Set var1;
      try {
         var1 = super.keySet();
      } finally {
         ;
      }

      return var1;
   }

   public Object put(Object var1, Object var2) {
      synchronized(this){}

      try {
         var1 = super.put(var1, var2);
      } finally {
         ;
      }

      return var1;
   }

   public void putAll(Map var1) {
      synchronized(this){}

      try {
         super.putAll(var1);
      } finally {
         ;
      }

   }

   public Object remove(Object var1) {
      synchronized(this){}

      try {
         var1 = super.remove(var1);
      } finally {
         ;
      }

      return var1;
   }

   protected boolean removeEldestEntry(Entry var1) {
      return this.size() > this.MAX_ENTRIES;
   }

   public int size() {
      synchronized(this){}

      int var1;
      try {
         var1 = super.size();
      } finally {
         ;
      }

      return var1;
   }

   public Collection values() {
      synchronized(this){}

      Collection var1;
      try {
         var1 = super.values();
      } finally {
         ;
      }

      return var1;
   }
}
