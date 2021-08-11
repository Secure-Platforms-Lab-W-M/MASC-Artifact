package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class HugePackedSet implements Set {
   private static final String IDX_VERSION = "1.0";
   private int count = 0;
   private String loadedFromPath = null;
   private ObjectPackagingManager objMgr;
   private int slotCount;
   private int[] slotSizes = null;
   private PackedSortedList[] subsets = null;

   public HugePackedSet(int var1, ObjectPackagingManager var2) {
      this.objMgr = var2;
      this.slotCount = var1;
      this.slotSizes = new int[this.slotCount];
      this.subsets = new PackedSortedList[this.slotCount];

      for(var1 = 0; var1 < this.slotCount; ++var1) {
         this.slotSizes[var1] = 0;
      }

   }

   private HugePackedSet(String var1, PackedSortedList[] var2, int var3, int var4, ObjectPackagingManager var5) {
      this.loadedFromPath = var1;
      this.objMgr = var5;
      this.slotCount = var3;
      this.subsets = var2;
      this.count = var4;
   }

   public static boolean checkIndexVersion(String var0) throws IOException {
      StringBuilder var2 = new StringBuilder();
      var2.append(var0);
      var2.append("/IDX_VERSION");
      if ((new File(var2.toString())).exists()) {
         var2 = new StringBuilder();
         var2.append(var0);
         var2.append("/IDX_VERSION");
         FileInputStream var3 = new FileInputStream(var2.toString());
         byte[] var4 = new byte[10];
         int var1 = var3.read(var4);
         var3.close();
         return (new String(var4, 0, var1)).equals("1.0");
      } else {
         return false;
      }
   }

   public static HugePackedSet load(String var0, boolean var1, ObjectPackagingManager var2) throws IOException {
      if (!checkIndexVersion(var0)) {
         throw new IOException("Incompatible Index Version - Rebuild Index!");
      } else {
         int var4 = 0;
         int var3 = 0;
         int var5 = 0;

         while(true) {
            StringBuilder var6 = new StringBuilder();
            var6.append(var0);
            var6.append("/idx");
            var6.append(var3);
            if (!(new File(var6.toString())).exists()) {
               PackedSortedList[] var8;
               for(var8 = new PackedSortedList[var3]; var4 < var8.length; ++var4) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append(var0);
                  var7.append("/idx");
                  var7.append(var4);
                  var8[var4] = PackedSortedList.load(var7.toString(), var1, var2);
                  var5 += var8[var4].size();
               }

               return new HugePackedSet(var0, var8, var3, var5, var2);
            }

            ++var3;
         }
      }
   }

   public boolean add(Object var1) {
      boolean var2 = this.subsets[Math.abs(var1.hashCode() % this.slotCount)].add(var1);
      if (var2) {
         ++this.count;
      }

      return var2;
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public void clear() {
      this.slotSizes = new int[this.slotCount];

      for(int var1 = 0; var1 < this.slotCount; ++var1) {
         this.subsets[var1].clearAndReleaseAllMemory();
         this.slotSizes[var1] = 0;
      }

   }

   public boolean contains(Object var1) {
      return this.subsets[Math.abs(var1.hashCode() % this.slotCount)].contains(var1);
   }

   public boolean containsAll(Collection var1) {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public void finalPrepare() {
      for(int var1 = 0; var1 < this.slotCount; ++var1) {
         this.subsets[var1] = new PackedSortedList(this.slotSizes[var1], this.objMgr);
      }

      this.slotSizes = null;
   }

   public void finalPrepare(int var1) {
      int var2 = (int)((double)var1 * 1.2D) / this.slotCount;

      for(var1 = 0; var1 < this.slotCount; ++var1) {
         this.subsets[var1] = new PackedSortedList(var2, this.objMgr);
      }

      this.slotSizes = null;
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   public Iterator iterator() {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public void persist(String var1) throws IOException {
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append(".tmp");
      File var8 = new File(var5.toString());
      byte var4 = 0;

      int var2;
      for(var2 = 0; var2 < 2; ++var2) {
         if (var8.exists()) {
            File[] var6 = var8.listFiles();

            for(int var3 = 0; var3 < var6.length; ++var3) {
               var6[var3].delete();
            }

            var8.delete();
         }

         var8 = new File(var1);
      }

      var5 = new StringBuilder();
      var5.append(var1);
      var5.append(".tmp");
      var8 = new File(var5.toString());
      var8.mkdir();
      StringBuilder var9 = new StringBuilder();
      var9.append(var8.getAbsolutePath());
      var9.append("/IDX_VERSION");
      FileOutputStream var10 = new FileOutputStream(var9.toString());
      var10.write("1.0".getBytes());
      var10.flush();
      var10.close();

      for(var2 = var4; var2 < this.slotCount; ++var2) {
         PackedSortedList var11 = this.subsets[var2];
         StringBuilder var7 = new StringBuilder();
         var7.append(var8.getAbsolutePath());
         var7.append("/idx");
         var7.append(var2);
         var11.persist(var7.toString());
      }

      var8.renameTo(new File(var1));
      this.loadedFromPath = var1;
   }

   public void prepareInsert(Object var1) {
      int[] var3 = this.slotSizes;
      int var2 = Math.abs(var1.hashCode() % this.slotCount);
      int var10002 = var3[var2]++;
   }

   public boolean remove(Object var1) {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public int size() {
      return this.count;
   }

   public Object[] toArray() {
      throw new UnsupportedOperationException("Not supported!");
   }

   public Object[] toArray(Object[] var1) {
      throw new UnsupportedOperationException("Not supported!");
   }

   public void updatePersist() throws IOException {
      if (this.loadedFromPath == null) {
         throw new IOException("Can not update non persisted index!");
      } else {
         File var2 = new File(this.loadedFromPath);

         for(int var1 = 0; var1 < this.slotCount; ++var1) {
            PackedSortedList var3 = this.subsets[var1];
            StringBuilder var4 = new StringBuilder();
            var4.append(var2.getAbsolutePath());
            var4.append("/idx");
            var4.append(var1);
            var3.persist(var4.toString());
         }

      }
   }
}
