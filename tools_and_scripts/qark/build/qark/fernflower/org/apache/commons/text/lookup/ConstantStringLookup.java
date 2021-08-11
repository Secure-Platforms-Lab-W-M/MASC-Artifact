package org.apache.commons.text.lookup;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.ClassUtils;

class ConstantStringLookup extends AbstractStringLookup {
   private static final char FIELD_SEPRATOR = '.';
   static final ConstantStringLookup INSTANCE = new ConstantStringLookup();
   private static ConcurrentHashMap constantCache = new ConcurrentHashMap();

   static void clear() {
      constantCache.clear();
   }

   protected Class fetchClass(String var1) throws ClassNotFoundException {
      return ClassUtils.getClass(var1);
   }

   public String lookup(String param1) {
      // $FF: Couldn't be decompiled
   }

   protected Object resolveField(String var1, String var2) throws Exception {
      Class var3 = this.fetchClass(var1);
      return var3 == null ? null : var3.getField(var2).get((Object)null);
   }
}
