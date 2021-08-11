package net.sf.fmj.media.util;

import javax.media.Format;

class FormatTable {
   public int[] hits;
   public Format[] keys;
   public int last;
   public Format[][] table;

   public FormatTable(int var1) {
      this.keys = new Format[var1];
      this.table = new Format[var1][];
      this.hits = new int[var1];
      this.last = 0;
   }

   public int findLeastHit() {
      int var2 = this.hits[0];
      int var3 = 0;

      int var4;
      for(int var1 = 1; var1 < this.last; var2 = var4) {
         int[] var5 = this.hits;
         var4 = var2;
         if (var5[var1] < var2) {
            var4 = var5[var1];
            var3 = var1;
         }

         ++var1;
      }

      return var3;
   }

   Format[] get(Format var1) {
      Format[] var3 = null;

      for(int var2 = 0; var2 < this.last; ++var2) {
         if (var3 == null && this.keys[var2].matches(var1)) {
            var3 = this.table[var2];
            this.hits[var2] = this.keys.length;
         } else {
            int[] var4 = this.hits;
            int var10002 = var4[var2]--;
         }
      }

      return var3;
   }

   public void save(Format var1, Format[] var2) {
      int var4 = this.last;
      int var3;
      if (var4 >= this.keys.length) {
         var3 = this.findLeastHit();
      } else {
         var3 = this.last;
         this.last = var4 + 1;
      }

      Format[] var5 = this.keys;
      var5[var3] = var1;
      this.table[var3] = var2;
      this.hits[var3] = var5.length;
   }
}
