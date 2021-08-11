package org.apache.commons.text.lookup;

abstract class AbstractStringLookup implements StringLookup {
   private static final String EMPTY = "";
   protected static final char SPLIT_CH = ':';
   protected static final String SPLIT_STR = String.valueOf(':');

   protected String substringAfter(String var1, char var2) {
      int var3 = var1.indexOf(var2);
      return var3 > -1 ? var1.substring(var3 + 1) : "";
   }

   protected String substringAfter(String var1, String var2) {
      int var3 = var1.indexOf(var2);
      return var3 > -1 ? var1.substring(var2.length() + var3) : "";
   }

   protected String substringAfterLast(String var1, char var2) {
      int var3 = var1.lastIndexOf(var2);
      return var3 > -1 ? var1.substring(var3 + 1) : "";
   }
}
