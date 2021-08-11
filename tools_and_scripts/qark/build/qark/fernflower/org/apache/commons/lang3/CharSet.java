package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CharSet implements Serializable {
   public static final CharSet ASCII_ALPHA = new CharSet(new String[]{"a-zA-Z"});
   public static final CharSet ASCII_ALPHA_LOWER = new CharSet(new String[]{"a-z"});
   public static final CharSet ASCII_ALPHA_UPPER = new CharSet(new String[]{"A-Z"});
   public static final CharSet ASCII_NUMERIC = new CharSet(new String[]{"0-9"});
   protected static final Map COMMON;
   public static final CharSet EMPTY = new CharSet(new String[]{(String)null});
   private static final long serialVersionUID = 5947847346149275958L;
   private final Set set = Collections.synchronizedSet(new HashSet());

   static {
      Map var0 = Collections.synchronizedMap(new HashMap());
      COMMON = var0;
      var0.put((Object)null, EMPTY);
      COMMON.put("", EMPTY);
      COMMON.put("a-zA-Z", ASCII_ALPHA);
      COMMON.put("A-Za-z", ASCII_ALPHA);
      COMMON.put("a-z", ASCII_ALPHA_LOWER);
      COMMON.put("A-Z", ASCII_ALPHA_UPPER);
      COMMON.put("0-9", ASCII_NUMERIC);
   }

   protected CharSet(String... var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         this.add(var1[var2]);
      }

   }

   public static CharSet getInstance(String... var0) {
      if (var0 == null) {
         return null;
      } else {
         if (var0.length == 1) {
            CharSet var1 = (CharSet)COMMON.get(var0[0]);
            if (var1 != null) {
               return var1;
            }
         }

         return new CharSet(var0);
      }
   }

   protected void add(String var1) {
      if (var1 != null) {
         int var3 = var1.length();
         int var2 = 0;

         while(true) {
            while(var2 < var3) {
               int var4 = var3 - var2;
               if (var4 >= 4 && var1.charAt(var2) == '^' && var1.charAt(var2 + 2) == '-') {
                  this.set.add(CharRange.isNotIn(var1.charAt(var2 + 1), var1.charAt(var2 + 3)));
                  var2 += 4;
               } else if (var4 >= 3 && var1.charAt(var2 + 1) == '-') {
                  this.set.add(CharRange.isIn(var1.charAt(var2), var1.charAt(var2 + 2)));
                  var2 += 3;
               } else if (var4 >= 2 && var1.charAt(var2) == '^') {
                  this.set.add(CharRange.isNot(var1.charAt(var2 + 1)));
                  var2 += 2;
               } else {
                  this.set.add(CharRange.method_3(var1.charAt(var2)));
                  ++var2;
               }
            }

            return;
         }
      }
   }

   public boolean contains(char var1) {
      Iterator var2 = this.set.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!((CharRange)var2.next()).contains(var1));

      return true;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CharSet)) {
         return false;
      } else {
         CharSet var2 = (CharSet)var1;
         return this.set.equals(var2.set);
      }
   }

   CharRange[] getCharRanges() {
      Set var1 = this.set;
      return (CharRange[])var1.toArray(new CharRange[var1.size()]);
   }

   public int hashCode() {
      return this.set.hashCode() + 89;
   }

   public String toString() {
      return this.set.toString();
   }
}
