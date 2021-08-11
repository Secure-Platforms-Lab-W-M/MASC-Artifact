package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public class PackedSortedList implements List, RandomAccess {
   private int count = 0;
   private byte[] datapack = null;
   private boolean keepInMemory;
   private boolean loaded = false;
   private ObjectPackagingManager objMgr;
   private int object_size;
   private RandomAccessFile persistedPackData = null;
   private int persistedPackDataRefs = 0;
   private File persistedPackFile;
   private boolean persistentOutdated;

   public PackedSortedList(int var1, ObjectPackagingManager var2) {
      this.objMgr = var2;
      this.object_size = var2.objectSize();
      this.datapack = new byte[this.object_size * var1];
      this.keepInMemory = true;
      this.loaded = true;
   }

   private PackedSortedList(byte[] var1, int var2, boolean var3, File var4, ObjectPackagingManager var5) throws IOException {
      this.objMgr = var5;
      this.object_size = var5.objectSize();
      this.datapack = var1;
      this.count = var2;
      this.keepInMemory = var3;
      this.persistedPackFile = var4;
      this.persistentOutdated = false;
      if (var3) {
         this.loadinMemory();
      }

   }

   private void addInternal(int var1, Object var2) {
      if (!this.loaded) {
         this.loadinMemory();
      }

      byte[] var3 = this.datapack;
      if (this.count >= this.datapack.length / this.object_size) {
         var3 = new byte[this.datapack.length + 1000 * this.object_size];
         System.arraycopy(this.datapack, 0, var3, 0, this.object_size * var1);
      }

      if (var1 != this.count) {
         System.arraycopy(this.datapack, this.object_size * var1, var3, this.object_size * var1 + this.object_size, (this.count - var1) * this.object_size);
      }

      this.datapack = var3;
      this.objMgr.objectToBytes(var2, this.datapack, this.object_size * var1);
      this.persistentOutdated = true;
      ++this.count;
   }

   private void aquireDataPack() throws FileNotFoundException {
      synchronized(this){}

      try {
         if (this.persistedPackData == null) {
            if (this.persistedPackDataRefs > 0) {
               StringBuilder var1 = new StringBuilder();
               var1.append("Inconsistent State! persistedPackData is null but there are ");
               var1.append(this.persistedPackDataRefs);
               var1.append(" references!");
               throw new IllegalStateException(var1.toString());
            }

            this.persistedPackData = new RandomAccessFile(this.persistedPackFile, "r");
         }

         ++this.persistedPackDataRefs;
      } finally {
         ;
      }

   }

   private int binarySearch(Object var1) {
      return Collections.binarySearch(this, var1);
   }

   public static PackedSortedList load(String var0, boolean var1, ObjectPackagingManager var2) throws IOException {
      File var4 = new File(var0);
      int var3 = (int)var4.length();
      if (var4.exists() && var4.canRead()) {
         return new PackedSortedList((byte[])null, var3 / var2.objectSize(), var1, var4, var2);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Cannot read ");
         var5.append(var0);
         throw new IOException(var5.toString());
      }
   }

   private void loadinMemory() {
      IOException var10000;
      label37: {
         boolean var10001;
         int var2;
         FileInputStream var4;
         try {
            var4 = new FileInputStream(this.persistedPackFile);
            var2 = this.count * this.objMgr.objectSize();
            this.datapack = new byte[var2];
         } catch (IOException var7) {
            var10000 = var7;
            var10001 = false;
            break label37;
         }

         int var1 = 0;

         while(true) {
            int var3;
            try {
               var3 = var4.read(this.datapack, var1, var2 - var1);
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }

            if (var3 == -1 || var1 == var2) {
               try {
                  var4.close();
                  this.loaded = true;
                  return;
               } catch (IOException var5) {
                  var10000 = var5;
                  var10001 = false;
                  break;
               }
            }

            var1 += var3;
         }
      }

      IOException var8 = var10000;
      throw new IllegalStateException(var8);
   }

   private void releaseDataPack() {
      synchronized(this){}

      try {
         --this.persistedPackDataRefs;
         if (this.persistedPackDataRefs < 0) {
            StringBuilder var1 = new StringBuilder();
            var1.append("Inconsistent State! persistedPackDataRefs = ");
            var1.append(this.persistedPackDataRefs);
            throw new IllegalStateException(var1.toString());
         }

         if (this.persistedPackDataRefs == 0) {
            this.persistedPackData.close();
            this.persistedPackData = null;
         }
      } catch (IOException var4) {
      } finally {
         ;
      }

   }

   public void add(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public boolean add(Object var1) {
      int var2 = -(this.binarySearch(var1) + 1);
      if (var2 < 0) {
         return false;
      } else {
         this.addInternal(var2, var1);
         return true;
      }
   }

   public boolean addAll(int var1, Collection var2) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.add(var2.next());
      }

      return true;
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public void clearAndReleaseAllMemory() {
      this.count = 0;
      this.datapack = new byte[0];
   }

   public boolean contains(Object var1) {
      int var2;
      if (!this.loaded) {
         try {
            this.aquireDataPack();
            var2 = this.binarySearch(var1);
         } catch (Exception var5) {
            throw new IllegalStateException(var5);
         } finally {
            this.releaseDataPack();
         }
      } else {
         var2 = this.binarySearch(var1);
      }

      return var2 > -1;
   }

   public boolean containsAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public Object get(int param1) {
      // $FF: Couldn't be decompiled
   }

   public int indexOf(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   public Iterator iterator() {
      throw new UnsupportedOperationException();
   }

   public int lastIndexOf(Object var1) {
      throw new UnsupportedOperationException();
   }

   public ListIterator listIterator() {
      throw new UnsupportedOperationException();
   }

   public ListIterator listIterator(int var1) {
      throw new UnsupportedOperationException();
   }

   public void persist(String var1) throws IOException {
      if (this.persistentOutdated) {
         if (!this.loaded) {
            throw new IOException("PackedSortedList can not be persisted when not in Memory!");
         }

         this.persistedPackFile = new File(var1);
         FileOutputStream var5 = new FileOutputStream(this.persistedPackFile);

         try {
            var5.write(this.datapack, 0, this.count * this.object_size);
            var5.flush();
         } finally {
            var5.close();
         }

         this.persistentOutdated = false;
      }

      if (!this.keepInMemory) {
         this.datapack = null;
         this.loaded = false;
      }

   }

   public Object remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public Object set(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public int size() {
      return this.count;
   }

   public List subList(int var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public Object[] toArray() {
      throw new UnsupportedOperationException();
   }

   public Object[] toArray(Object[] var1) {
      throw new UnsupportedOperationException();
   }
}
