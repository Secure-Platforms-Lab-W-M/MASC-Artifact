package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.util.Preconditions;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class DiskCacheWriteLocker {
   private final Map locks = new HashMap();
   private final DiskCacheWriteLocker.WriteLockPool writeLockPool = new DiskCacheWriteLocker.WriteLockPool();

   void acquire(String var1) {
      synchronized(this){}

      DiskCacheWriteLocker.WriteLock var2;
      label189: {
         Throwable var10000;
         boolean var10001;
         label190: {
            DiskCacheWriteLocker.WriteLock var3;
            try {
               var3 = (DiskCacheWriteLocker.WriteLock)this.locks.get(var1);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label190;
            }

            var2 = var3;
            if (var3 == null) {
               try {
                  var2 = this.writeLockPool.obtain();
                  this.locks.put(var1, var2);
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label190;
               }
            }

            label177:
            try {
               ++var2.interestedThreads;
               break label189;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label177;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               continue;
            }
         }
      }

      var2.lock.lock();
   }

   void release(String var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label325: {
         DiskCacheWriteLocker.WriteLock var2;
         DiskCacheWriteLocker.WriteLock var3;
         label329: {
            label332: {
               try {
                  var2 = (DiskCacheWriteLocker.WriteLock)Preconditions.checkNotNull(this.locks.get(var1));
                  if (var2.interestedThreads >= 1) {
                     --var2.interestedThreads;
                     if (var2.interestedThreads == 0) {
                        var3 = (DiskCacheWriteLocker.WriteLock)this.locks.remove(var1);
                        if (!var3.equals(var2)) {
                           break label329;
                        }

                        this.writeLockPool.offer(var3);
                     }
                     break label332;
                  }
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label325;
               }

               try {
                  StringBuilder var36 = new StringBuilder();
                  var36.append("Cannot release a lock that is not held, safeKey: ");
                  var36.append(var1);
                  var36.append(", interestedThreads: ");
                  var36.append(var2.interestedThreads);
                  throw new IllegalStateException(var36.toString());
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label325;
               }
            }

            try {
               ;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label325;
            }

            var2.lock.unlock();
            return;
         }

         label305:
         try {
            StringBuilder var4 = new StringBuilder();
            var4.append("Removed the wrong lock, expected to remove: ");
            var4.append(var2);
            var4.append(", but actually removed: ");
            var4.append(var3);
            var4.append(", safeKey: ");
            var4.append(var1);
            throw new IllegalStateException(var4.toString());
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label305;
         }
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   private static class WriteLock {
      int interestedThreads;
      final Lock lock = new ReentrantLock();

      WriteLock() {
      }
   }

   private static class WriteLockPool {
      private static final int MAX_POOL_SIZE = 10;
      private final Queue pool = new ArrayDeque();

      WriteLockPool() {
      }

      DiskCacheWriteLocker.WriteLock obtain() {
         // $FF: Couldn't be decompiled
      }

      void offer(DiskCacheWriteLocker.WriteLock var1) {
         Queue var2 = this.pool;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label122: {
            try {
               if (this.pool.size() < 10) {
                  this.pool.offer(var1);
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label122;
            }

            label119:
            try {
               return;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label119;
            }
         }

         while(true) {
            Throwable var15 = var10000;

            try {
               throw var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
