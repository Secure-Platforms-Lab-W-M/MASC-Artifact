package com.google.i18n.phonenumbers.internal;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class RegexCache {
   private RegexCache.LRUCache cache;

   public RegexCache(int var1) {
      this.cache = new RegexCache.LRUCache(var1);
   }

   boolean containsRegex(String var1) {
      return this.cache.containsKey(var1);
   }

   public Pattern getPatternForRegex(String var1) {
      Pattern var3 = (Pattern)this.cache.get(var1);
      Pattern var2 = var3;
      if (var3 == null) {
         var2 = Pattern.compile(var1);
         this.cache.put(var1, var2);
      }

      return var2;
   }

   private static class LRUCache {
      private LinkedHashMap map;
      private int size;

      public LRUCache(int var1) {
         this.size = var1;
         this.map = new LinkedHashMap(var1 * 4 / 3 + 1, 0.75F, true) {
            protected boolean removeEldestEntry(Entry var1) {
               return this.size() > LRUCache.this.size;
            }
         };
      }

      public boolean containsKey(Object var1) {
         synchronized(this){}

         boolean var2;
         try {
            var2 = this.map.containsKey(var1);
         } finally {
            ;
         }

         return var2;
      }

      public Object get(Object var1) {
         synchronized(this){}

         try {
            var1 = this.map.get(var1);
         } finally {
            ;
         }

         return var1;
      }

      public void put(Object var1, Object var2) {
         synchronized(this){}

         try {
            this.map.put(var1, var2);
         } finally {
            ;
         }

      }
   }
}
