package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.security.InvalidParameterException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LookupTranslator extends CharSequenceTranslator {
   private final int longest;
   private final Map lookupMap;
   private final BitSet prefixSet;
   private final int shortest;

   public LookupTranslator(Map var1) {
      if (var1 != null) {
         this.lookupMap = new HashMap();
         this.prefixSet = new BitSet();
         int var5 = Integer.MAX_VALUE;
         int var2 = 0;

         int var6;
         for(Iterator var8 = var1.entrySet().iterator(); var8.hasNext(); var2 = var6) {
            Entry var7 = (Entry)var8.next();
            this.lookupMap.put(((CharSequence)var7.getKey()).toString(), ((CharSequence)var7.getValue()).toString());
            this.prefixSet.set(((CharSequence)var7.getKey()).charAt(0));
            int var3 = ((CharSequence)var7.getKey()).length();
            int var4 = var5;
            if (var3 < var5) {
               var4 = var3;
            }

            var6 = var2;
            if (var3 > var2) {
               var6 = var3;
            }

            var5 = var4;
         }

         this.shortest = var5;
         this.longest = var2;
      } else {
         throw new InvalidParameterException("lookupMap cannot be null");
      }
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      if (this.prefixSet.get(var1.charAt(var2))) {
         int var4 = this.longest;
         if (this.longest + var2 > var1.length()) {
            var4 = var1.length() - var2;
         }

         while(var4 >= this.shortest) {
            CharSequence var5 = var1.subSequence(var2, var2 + var4);
            String var6 = (String)this.lookupMap.get(var5.toString());
            if (var6 != null) {
               var3.write(var6);
               return var4;
            }

            --var4;
         }
      }

      return 0;
   }
}
