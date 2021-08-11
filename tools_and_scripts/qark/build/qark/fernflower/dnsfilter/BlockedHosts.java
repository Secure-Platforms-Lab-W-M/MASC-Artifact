package dnsfilter;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import util.ExecutionEnvironment;
import util.HugePackedSet;
import util.LRUCache;
import util.Logger;
import util.LoggerInterface;
import util.ObjectPackagingManager;
import util.Utils;

public class BlockedHosts implements Set {
   private static Object NOT_NULL = new Object();
   private static ObjectPackagingManager PACK_MGR = new BlockedHosts.MyPackagingManager();
   private HugePackedSet blockedHostsHashes;
   private Vector blockedPatterns;
   private boolean exclusiveLock = false;
   private LRUCache filterListCache;
   private Hashtable hostsFilterOverRule;
   private LRUCache okCache;
   private int sharedLocks = 0;

   public BlockedHosts(int var1, int var2, int var3, Hashtable var4) {
      this.okCache = new LRUCache(var2);
      this.filterListCache = new LRUCache(var3);
      if (ExecutionEnvironment.getEnvironment().debug()) {
         LoggerInterface var5 = Logger.getLogger();
         StringBuilder var6 = new StringBuilder();
         var6.append("CACHE SIZE:");
         var6.append(var2);
         var6.append(", ");
         var6.append(var3);
         var5.logLine(var6.toString());
      }

      this.hostsFilterOverRule = var4;
      var2 = var1 / 6000;
      var1 = var2;
      if (var2 % 2 == 0) {
         var1 = var2 + 1;
      }

      this.blockedHostsHashes = new HugePackedSet(var1, PACK_MGR);
   }

   private BlockedHosts(HugePackedSet var1, Vector var2, int var3, int var4, Hashtable var5) {
      this.blockedHostsHashes = var1;
      this.blockedPatterns = var2;
      this.okCache = new LRUCache(var3);
      this.filterListCache = new LRUCache(var4);
      if (ExecutionEnvironment.getEnvironment().debug()) {
         LoggerInterface var6 = Logger.getLogger();
         StringBuilder var7 = new StringBuilder();
         var7.append("CACHE SIZE:");
         var7.append(var3);
         var7.append(", ");
         var7.append(var4);
         var6.logLine(var7.toString());
      }

      this.hostsFilterOverRule = var5;
   }

   public static boolean checkIndexVersion(String var0) throws IOException {
      return HugePackedSet.checkIndexVersion(var0);
   }

   private boolean contains(String var1, long var2, boolean var4, boolean var5) {
      int var6 = 0;

      while(var6 != -1) {
         if (this.hostsFilterOverRule != null) {
            Object var8 = this.hostsFilterOverRule.get(var1);
            if (var8 != null) {
               return (Boolean)var8;
            }
         }

         if (this.blockedHostsHashes.contains(var2)) {
            return true;
         }

         if (var5 && this.containsPatternMatch(var1)) {
            return true;
         }

         if (var4) {
            int var7 = var1.indexOf(46);
            var6 = var7;
            if (var7 != -1) {
               var1 = var1.substring(var7 + 1);
               var2 = Utils.getLongStringHash(var1);
               var6 = var7;
            }
         } else {
            var6 = -1;
         }
      }

      return false;
   }

   private boolean containsPatternMatch(String var1) {
      if (this.blockedPatterns == null) {
         return false;
      } else {
         Iterator var2 = this.blockedPatterns.iterator();

         do {
            if (!var2.hasNext()) {
               return false;
            }
         } while(!wildCardMatch((String[])var2.next(), var1));

         return true;
      }
   }

   private Vector getInitializedPatternStruct() {
      if (this.blockedPatterns == null) {
         this.blockedPatterns = new Vector();
      }

      return this.blockedPatterns;
   }

   public static BlockedHosts loadPersistedIndex(String var0, boolean var1, int var2, int var3, Hashtable var4) throws IOException {
      Vector var5 = null;
      StringBuilder var6 = new StringBuilder();
      var6.append(var0);
      var6.append("/blockedpatterns");
      File var8 = new File(var6.toString());
      if (var8.exists()) {
         BufferedReader var9 = new BufferedReader(new InputStreamReader(new FileInputStream(var8)));
         var5 = new Vector();

         while(true) {
            String var7 = var9.readLine();
            if (var7 == null) {
               var9.close();
               break;
            }

            var5.addElement(var7.trim().split("\\*", -1));
         }
      }

      return new BlockedHosts(HugePackedSet.load(var0, var1, PACK_MGR), var5, var2, var3, var4);
   }

   private static boolean wildCardMatch(String[] var0, String var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         String var4 = var0[var2];
         int var3;
         if (var2 < var0.length - 1) {
            var3 = var1.indexOf(var4);
         } else {
            var3 = var1.lastIndexOf(var4);
         }

         if (var2 == 0 && !var4.equals("") && var3 != 0) {
            return false;
         }

         if (var2 == var0.length - 1 && !var4.equals("") && var4.length() + var3 != var1.length()) {
            return false;
         }

         if (var3 == -1) {
            return false;
         }

         var1 = var1.substring(var4.length() + var3);
      }

      return true;
   }

   public boolean add(Object var1) {
      if (((String)var1).indexOf("*") == -1) {
         return this.blockedHostsHashes.add(Utils.getLongStringHash(((String)var1).toLowerCase()));
      } else {
         this.getInitializedPatternStruct().addElement(((String)var1).trim().toLowerCase().split("\\*", -1));
         return true;
      }
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException("Not supported!");
   }

   public void clear() {
      this.blockedHostsHashes.clear();
      this.filterListCache.clear();
      this.okCache.clear();
      this.hostsFilterOverRule = null;
      if (this.blockedPatterns != null) {
         this.blockedPatterns.clear();
      }

   }

   public void clearCache(String var1) {
      long var2 = Utils.getLongStringHash(var1.toLowerCase());
      this.okCache.remove(var2);
      this.filterListCache.remove(var2);
   }

   public boolean contains(Object var1) {
      Throwable var10000;
      label693: {
         boolean var10001;
         try {
            this.lock(0);
         } catch (Throwable var77) {
            var10000 = var77;
            var10001 = false;
            break label693;
         }

         boolean var2 = false;

         String var7;
         try {
            var7 = ((String)var1).toLowerCase();
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label693;
         }

         String var80 = var7;

         label694: {
            try {
               if (!var7.startsWith("%ip%")) {
                  break label694;
               }
            } catch (Throwable var79) {
               var10000 = var79;
               var10001 = false;
               break label693;
            }

            var2 = true;

            try {
               var80 = var7.substring(4);
            } catch (Throwable var75) {
               var10000 = var75;
               var10001 = false;
               break label693;
            }
         }

         long var3;
         Object var82;
         try {
            var3 = Utils.getLongStringHash(var80);
            var82 = this.okCache.get(var3);
         } catch (Throwable var74) {
            var10000 = var74;
            var10001 = false;
            break label693;
         }

         if (var82 != null) {
            this.unLock(0);
            return false;
         }

         try {
            var82 = this.filterListCache.get(var3);
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            break label693;
         }

         if (var82 != null) {
            this.unLock(0);
            return true;
         }

         boolean var5;
         if (!var2) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var6;
         if (!var2) {
            var6 = true;
         } else {
            var6 = false;
         }

         label677: {
            try {
               if (!this.contains(var80, var3, var5, var6)) {
                  break label677;
               }

               this.filterListCache.put(var3, NOT_NULL);
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break label693;
            }

            this.unLock(0);
            return true;
         }

         try {
            this.okCache.put(var3, NOT_NULL);
         } catch (Throwable var72) {
            var10000 = var72;
            var10001 = false;
            break label693;
         }

         this.unLock(0);
         return false;
      }

      Throwable var81 = var10000;
      this.unLock(0);
      throw var81;
   }

   public boolean containsAll(Collection var1) {
      throw new UnsupportedOperationException("Not supported!");
   }

   public void finalPrepare() {
      this.blockedHostsHashes.finalPrepare();
   }

   public void finalPrepare(int var1) {
      this.blockedHostsHashes.finalPrepare(var1);
   }

   public boolean isEmpty() {
      return this.blockedHostsHashes.isEmpty();
   }

   public Iterator iterator() {
      throw new UnsupportedOperationException("Not supported!");
   }

   public void lock(int param1) {
      // $FF: Couldn't be decompiled
   }

   protected void migrateTo(BlockedHosts var1) {
      this.okCache.clear();
      this.okCache = var1.okCache;
      this.filterListCache.clear();
      this.filterListCache = var1.filterListCache;
      this.hostsFilterOverRule = var1.hostsFilterOverRule;
      this.blockedPatterns = var1.blockedPatterns;
      this.blockedHostsHashes = var1.blockedHostsHashes;
   }

   public void persist(String var1) throws IOException {
      label310: {
         Throwable var10000;
         label309: {
            Iterator var4;
            BufferedOutputStream var38;
            boolean var10001;
            try {
               this.lock(1);
               this.blockedHostsHashes.persist(var1);
               if (this.blockedPatterns == null) {
                  break label310;
               }

               StringBuilder var3 = new StringBuilder();
               var3.append(var1);
               var3.append("/blockedpatterns");
               var38 = new BufferedOutputStream(new FileOutputStream(var3.toString()));
               var4 = this.blockedPatterns.iterator();
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label309;
            }

            label308:
            while(true) {
               String[] var5;
               label306: {
                  try {
                     if (var4.hasNext()) {
                        var5 = (String[])var4.next();
                        break label306;
                     }
                  } catch (Throwable var36) {
                     var10000 = var36;
                     var10001 = false;
                     break;
                  }

                  try {
                     var38.flush();
                     var38.close();
                     break label310;
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break;
                  }
               }

               var1 = var5[0];
               int var2 = 1;

               while(true) {
                  try {
                     if (var2 >= var5.length) {
                        break;
                     }

                     StringBuilder var6 = new StringBuilder();
                     var6.append(var1);
                     var6.append("*");
                     var6.append(var5[var2]);
                     var1 = var6.toString();
                  } catch (Throwable var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label308;
                  }

                  ++var2;
               }

               try {
                  StringBuilder var39 = new StringBuilder();
                  var39.append(var1);
                  var39.append("\n");
                  var38.write(var39.toString().getBytes());
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var37 = var10000;
         this.unLock(1);
         throw var37;
      }

      this.unLock(1);
   }

   public void prepareInsert(String var1) {
      if (var1.indexOf("*") == -1) {
         this.blockedHostsHashes.prepareInsert(Utils.getLongStringHash(var1.toLowerCase()));
      }

   }

   public boolean remove(Object var1) {
      throw new UnsupportedOperationException("Not supported!");
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException("Not supported!");
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException("Not supported!");
   }

   public void setHostsFilterOverRule(Hashtable var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Argument null not allowed!");
      } else {
         this.hostsFilterOverRule = var1;
      }
   }

   public int size() {
      return this.blockedHostsHashes.size();
   }

   public Object[] toArray() {
      throw new UnsupportedOperationException("Not supported!");
   }

   public Object[] toArray(Object[] var1) {
      throw new UnsupportedOperationException("Not supported!");
   }

   public void unLock(int var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 == 0) {
         label81:
         try {
            if (this.sharedLocks > 0) {
               --this.sharedLocks;
               if (this.sharedLocks == 0) {
                  this.notifyAll();
                  return;
               }
            }

            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label81;
         }
      } else {
         if (var1 != 1) {
            return;
         }

         label83:
         try {
            if (this.exclusiveLock) {
               this.exclusiveLock = false;
               this.notifyAll();
            }

            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label83;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public boolean update(Object var1) throws IOException {
      boolean var4;
      try {
         this.lock(1);
         if (((String)var1).indexOf("*") != -1) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Wildcard not supported for update:");
            var5.append(var1);
            throw new IOException(var5.toString());
         }

         long var2 = Utils.getLongStringHash(((String)var1).toLowerCase());
         this.okCache.remove(var2);
         this.filterListCache.remove(var2);
         var4 = this.blockedHostsHashes.add(var2);
      } finally {
         this.unLock(1);
      }

      return var4;
   }

   public void updatePersist() throws IOException {
      try {
         this.lock(1);
         this.blockedHostsHashes.updatePersist();
      } finally {
         this.unLock(1);
      }

   }

   private static class MyPackagingManager implements ObjectPackagingManager {
      private MyPackagingManager() {
      }

      // $FF: synthetic method
      MyPackagingManager(Object var1) {
         this();
      }

      public Object bytesToObject(byte[] var1, int var2) {
         return Utils.byteArrayToLong(var1, var2);
      }

      public int objectSize() {
         return 8;
      }

      public void objectToBytes(Object var1, byte[] var2, int var3) {
         Utils.writeLongToByteArray((Long)var1, var2, var3);
      }
   }
}
