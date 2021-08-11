package android.support.v7.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

class MessageThreadUtil implements ThreadUtil {
   public ThreadUtil.BackgroundCallback getBackgroundProxy(final ThreadUtil.BackgroundCallback var1) {
      return new ThreadUtil.BackgroundCallback() {
         static final int LOAD_TILE = 3;
         static final int RECYCLE_TILE = 4;
         static final int REFRESH = 1;
         static final int UPDATE_RANGE = 2;
         private Runnable mBackgroundRunnable;
         AtomicBoolean mBackgroundRunning;
         private final Executor mExecutor;
         final MessageThreadUtil.MessageQueue mQueue = new MessageThreadUtil.MessageQueue();

         {
            this.mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
            this.mBackgroundRunning = new AtomicBoolean(false);
            this.mBackgroundRunnable = new Runnable() {
               public void run() {
                  while(true) {
                     MessageThreadUtil.SyncQueueItem var1x = mQueue.next();
                     if (var1x == null) {
                        mBackgroundRunning.set(false);
                        return;
                     }

                     switch(var1x.what) {
                     case 1:
                        mQueue.removeMessages(1);
                        var1.refresh(var1x.arg1);
                        break;
                     case 2:
                        mQueue.removeMessages(2);
                        mQueue.removeMessages(3);
                        var1.updateRange(var1x.arg1, var1x.arg2, var1x.arg3, var1x.arg4, var1x.arg5);
                        break;
                     case 3:
                        var1.loadTile(var1x.arg1, var1x.arg2);
                        break;
                     case 4:
                        var1.recycleTile((TileList.Tile)var1x.data);
                        break;
                     default:
                        StringBuilder var2 = new StringBuilder();
                        var2.append("Unsupported message, what=");
                        var2.append(var1x.what);
                        Log.e("ThreadUtil", var2.toString());
                     }
                  }
               }
            };
         }

         private void maybeExecuteBackgroundRunnable() {
            if (this.mBackgroundRunning.compareAndSet(false, true)) {
               this.mExecutor.execute(this.mBackgroundRunnable);
            }
         }

         private void sendMessage(MessageThreadUtil.SyncQueueItem var1x) {
            this.mQueue.sendMessage(var1x);
            this.maybeExecuteBackgroundRunnable();
         }

         private void sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem var1x) {
            this.mQueue.sendMessageAtFrontOfQueue(var1x);
            this.maybeExecuteBackgroundRunnable();
         }

         public void loadTile(int var1x, int var2) {
            this.sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(3, var1x, var2));
         }

         public void recycleTile(TileList.Tile var1x) {
            this.sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(4, 0, var1x));
         }

         public void refresh(int var1x) {
            this.sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem.obtainMessage(1, var1x, (Object)null));
         }

         public void updateRange(int var1x, int var2, int var3, int var4, int var5) {
            this.sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem.obtainMessage(2, var1x, var2, var3, var4, var5, (Object)null));
         }
      };
   }

   public ThreadUtil.MainThreadCallback getMainThreadProxy(final ThreadUtil.MainThreadCallback var1) {
      return new ThreadUtil.MainThreadCallback() {
         static final int ADD_TILE = 2;
         static final int REMOVE_TILE = 3;
         static final int UPDATE_ITEM_COUNT = 1;
         private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
         private Runnable mMainThreadRunnable = new Runnable() {
            public void run() {
               for(MessageThreadUtil.SyncQueueItem var1x = mQueue.next(); var1x != null; var1x = mQueue.next()) {
                  switch(var1x.what) {
                  case 1:
                     var1.updateItemCount(var1x.arg1, var1x.arg2);
                     break;
                  case 2:
                     var1.addTile(var1x.arg1, (TileList.Tile)var1x.data);
                     break;
                  case 3:
                     var1.removeTile(var1x.arg1, var1x.arg2);
                     break;
                  default:
                     StringBuilder var2 = new StringBuilder();
                     var2.append("Unsupported message, what=");
                     var2.append(var1x.what);
                     Log.e("ThreadUtil", var2.toString());
                  }
               }

            }
         };
         final MessageThreadUtil.MessageQueue mQueue = new MessageThreadUtil.MessageQueue();

         private void sendMessage(MessageThreadUtil.SyncQueueItem var1x) {
            this.mQueue.sendMessage(var1x);
            this.mMainThreadHandler.post(this.mMainThreadRunnable);
         }

         public void addTile(int var1x, TileList.Tile var2) {
            this.sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(2, var1x, var2));
         }

         public void removeTile(int var1x, int var2) {
            this.sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(3, var1x, var2));
         }

         public void updateItemCount(int var1x, int var2) {
            this.sendMessage(MessageThreadUtil.SyncQueueItem.obtainMessage(1, var1x, var2));
         }
      };
   }

   static class MessageQueue {
      private MessageThreadUtil.SyncQueueItem mRoot;

      MessageThreadUtil.SyncQueueItem next() {
         synchronized(this){}

         Throwable var10000;
         label78: {
            MessageThreadUtil.SyncQueueItem var1;
            boolean var10001;
            try {
               var1 = this.mRoot;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label78;
            }

            if (var1 == null) {
               return null;
            }

            try {
               var1 = this.mRoot;
               this.mRoot = this.mRoot.next;
            } catch (Throwable var6) {
               var10000 = var6;
               var10001 = false;
               break label78;
            }

            return var1;
         }

         Throwable var8 = var10000;
         throw var8;
      }

      void removeMessages(int var1) {
         synchronized(this){}

         Throwable var10000;
         label213:
         while(true) {
            boolean var10001;
            MessageThreadUtil.SyncQueueItem var2;
            try {
               if (this.mRoot != null && this.mRoot.what == var1) {
                  var2 = this.mRoot;
                  this.mRoot = this.mRoot.next;
                  var2.recycle();
                  continue;
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break;
            }

            MessageThreadUtil.SyncQueueItem var3;
            try {
               if (this.mRoot == null) {
                  return;
               }

               var3 = this.mRoot;
               var2 = var3.next;
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break;
            }

            MessageThreadUtil.SyncQueueItem var4;
            for(; var2 != null; var2 = var4) {
               try {
                  var4 = var2.next;
                  if (var2.what == var1) {
                     var3.next = var4;
                     var2.recycle();
                     continue;
                  }
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label213;
               }

               var3 = var2;
            }

            return;
         }

         Throwable var17 = var10000;
         throw var17;
      }

      void sendMessage(MessageThreadUtil.SyncQueueItem var1) {
         synchronized(this){}

         Throwable var10000;
         label228: {
            boolean var10001;
            try {
               if (this.mRoot == null) {
                  this.mRoot = var1;
                  return;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label228;
            }

            MessageThreadUtil.SyncQueueItem var2;
            try {
               var2 = this.mRoot;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label228;
            }

            while(true) {
               try {
                  if (var2.next == null) {
                     break;
                  }

                  var2 = var2.next;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label228;
               }
            }

            try {
               var2.next = var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label228;
            }

            return;
         }

         Throwable var23 = var10000;
         throw var23;
      }

      void sendMessageAtFrontOfQueue(MessageThreadUtil.SyncQueueItem var1) {
         synchronized(this){}

         try {
            var1.next = this.mRoot;
            this.mRoot = var1;
         } finally {
            ;
         }

      }
   }

   static class SyncQueueItem {
      private static MessageThreadUtil.SyncQueueItem sPool;
      private static final Object sPoolLock = new Object();
      public int arg1;
      public int arg2;
      public int arg3;
      public int arg4;
      public int arg5;
      public Object data;
      private MessageThreadUtil.SyncQueueItem next;
      public int what;

      static MessageThreadUtil.SyncQueueItem obtainMessage(int var0, int var1, int var2) {
         return obtainMessage(var0, var1, var2, 0, 0, 0, (Object)null);
      }

      static MessageThreadUtil.SyncQueueItem obtainMessage(int var0, int var1, int var2, int var3, int var4, int var5, Object var6) {
         Object var8 = sPoolLock;
         synchronized(var8){}

         Throwable var10000;
         boolean var10001;
         label195: {
            MessageThreadUtil.SyncQueueItem var7;
            label189: {
               try {
                  if (sPool == null) {
                     var7 = new MessageThreadUtil.SyncQueueItem();
                     break label189;
                  }
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label195;
               }

               try {
                  var7 = sPool;
                  sPool = sPool.next;
                  var7.next = null;
               } catch (Throwable var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label195;
               }
            }

            label180:
            try {
               var7.what = var0;
               var7.arg1 = var1;
               var7.arg2 = var2;
               var7.arg3 = var3;
               var7.arg4 = var4;
               var7.arg5 = var5;
               var7.data = var6;
               return var7;
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label180;
            }
         }

         while(true) {
            Throwable var29 = var10000;

            try {
               throw var29;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               continue;
            }
         }
      }

      static MessageThreadUtil.SyncQueueItem obtainMessage(int var0, int var1, Object var2) {
         return obtainMessage(var0, var1, 0, 0, 0, 0, var2);
      }

      void recycle() {
         this.next = null;
         this.arg5 = 0;
         this.arg4 = 0;
         this.arg3 = 0;
         this.arg2 = 0;
         this.arg1 = 0;
         this.what = 0;
         this.data = null;
         Object var1 = sPoolLock;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label150: {
            try {
               if (sPool != null) {
                  this.next = sPool;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label150;
            }

            label136:
            try {
               sPool = this;
               return;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
