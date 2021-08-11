package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

class PrettyPrintTreeMap extends TreeMap {
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("( ");
      Iterator var2 = this.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append('{');
         var1.append(var3.getKey());
         var1.append(':');
         var1.append(var3.getValue());
         var1.append("}, ");
      }

      if (!this.isEmpty()) {
         var1.replace(var1.length() - 2, var1.length(), "");
      }

      var1.append(" )");
      return var1.toString();
   }
}
