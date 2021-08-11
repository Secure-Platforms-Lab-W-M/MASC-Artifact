package androidx.lifecycle;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.internal.SafeIterableMap;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class LiveData {
   static final Object NOT_SET = new Object();
   static final int START_VERSION = -1;
   int mActiveCount = 0;
   private volatile Object mData;
   final Object mDataLock = new Object();
   private boolean mDispatchInvalidated;
   private boolean mDispatchingValue;
   private SafeIterableMap mObservers = new SafeIterableMap();
   volatile Object mPendingData;
   private final Runnable mPostValueRunnable;
   private int mVersion;

   public LiveData() {
      this.mPendingData = NOT_SET;
      this.mPostValueRunnable = new Runnable() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      };
      this.mData = NOT_SET;
      this.mVersion = -1;
   }

   public LiveData(Object var1) {
      this.mPendingData = NOT_SET;
      this.mPostValueRunnable = new Runnable() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      };
      this.mData = var1;
      this.mVersion = 0;
   }

   static void assertMainThread(String var0) {
      if (!ArchTaskExecutor.getInstance().isMainThread()) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Cannot invoke ");
         var1.append(var0);
         var1.append(" on a background thread");
         throw new IllegalStateException(var1.toString());
      }
   }

   private void considerNotify(LiveData.ObserverWrapper var1) {
      if (var1.mActive) {
         if (!var1.shouldBeActive()) {
            var1.activeStateChanged(false);
         } else {
            int var2 = var1.mLastVersion;
            int var3 = this.mVersion;
            if (var2 < var3) {
               var1.mLastVersion = var3;
               var1.mObserver.onChanged(this.mData);
            }
         }
      }
   }

   void dispatchingValue(LiveData.ObserverWrapper var1) {
      if (this.mDispatchingValue) {
         this.mDispatchInvalidated = true;
      } else {
         this.mDispatchingValue = true;

         while(true) {
            this.mDispatchInvalidated = false;
            LiveData.ObserverWrapper var2;
            if (var1 != null) {
               this.considerNotify(var1);
               var2 = null;
            } else {
               SafeIterableMap.IteratorWithAdditions var3 = this.mObservers.iteratorWithAdditions();

               while(true) {
                  var2 = var1;
                  if (!var3.hasNext()) {
                     break;
                  }

                  this.considerNotify((LiveData.ObserverWrapper)((Entry)var3.next()).getValue());
                  if (this.mDispatchInvalidated) {
                     var2 = var1;
                     break;
                  }
               }
            }

            if (!this.mDispatchInvalidated) {
               this.mDispatchingValue = false;
               return;
            }

            var1 = var2;
         }
      }
   }

   public Object getValue() {
      Object var1 = this.mData;
      return var1 != NOT_SET ? var1 : null;
   }

   int getVersion() {
      return this.mVersion;
   }

   public boolean hasActiveObservers() {
      return this.mActiveCount > 0;
   }

   public boolean hasObservers() {
      return this.mObservers.size() > 0;
   }

   public void observe(LifecycleOwner var1, Observer var2) {
      assertMainThread("observe");
      if (var1.getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED) {
         LiveData.LifecycleBoundObserver var3 = new LiveData.LifecycleBoundObserver(var1, var2);
         LiveData.ObserverWrapper var4 = (LiveData.ObserverWrapper)this.mObservers.putIfAbsent(var2, var3);
         if (var4 != null && !var4.isAttachedTo(var1)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
         } else if (var4 == null) {
            var1.getLifecycle().addObserver(var3);
         }
      }
   }

   public void observeForever(Observer var1) {
      assertMainThread("observeForever");
      LiveData.AlwaysActiveObserver var2 = new LiveData.AlwaysActiveObserver(var1);
      LiveData.ObserverWrapper var3 = (LiveData.ObserverWrapper)this.mObservers.putIfAbsent(var1, var2);
      if (!(var3 instanceof LiveData.LifecycleBoundObserver)) {
         if (var3 == null) {
            var2.activeStateChanged(true);
         }
      } else {
         throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
      }
   }

   protected void onActive() {
   }

   protected void onInactive() {
   }

   protected void postValue(Object var1) {
      Object var3 = this.mDataLock;
      synchronized(var3){}

      boolean var2;
      label164: {
         Throwable var10000;
         boolean var10001;
         label159: {
            label158: {
               label157: {
                  try {
                     if (this.mPendingData == NOT_SET) {
                        break label157;
                     }
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label159;
                  }

                  var2 = false;
                  break label158;
               }

               var2 = true;
            }

            label151:
            try {
               this.mPendingData = var1;
               break label164;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label151;
            }
         }

         while(true) {
            Throwable var16 = var10000;

            try {
               throw var16;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }

      if (var2) {
         ArchTaskExecutor.getInstance().postToMainThread(this.mPostValueRunnable);
      }
   }

   public void removeObserver(Observer var1) {
      assertMainThread("removeObserver");
      LiveData.ObserverWrapper var2 = (LiveData.ObserverWrapper)this.mObservers.remove(var1);
      if (var2 != null) {
         var2.detachObserver();
         var2.activeStateChanged(false);
      }
   }

   public void removeObservers(LifecycleOwner var1) {
      assertMainThread("removeObservers");
      Iterator var2 = this.mObservers.iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (((LiveData.ObserverWrapper)var3.getValue()).isAttachedTo(var1)) {
            this.removeObserver((Observer)var3.getKey());
         }
      }

   }

   protected void setValue(Object var1) {
      assertMainThread("setValue");
      ++this.mVersion;
      this.mData = var1;
      this.dispatchingValue((LiveData.ObserverWrapper)null);
   }

   private class AlwaysActiveObserver extends LiveData.ObserverWrapper {
      AlwaysActiveObserver(Observer var2) {
         super(var2);
      }

      boolean shouldBeActive() {
         return true;
      }
   }

   class LifecycleBoundObserver extends LiveData.ObserverWrapper implements LifecycleEventObserver {
      final LifecycleOwner mOwner;

      LifecycleBoundObserver(LifecycleOwner var2, Observer var3) {
         super(var3);
         this.mOwner = var2;
      }

      void detachObserver() {
         this.mOwner.getLifecycle().removeObserver(this);
      }

      boolean isAttachedTo(LifecycleOwner var1) {
         return this.mOwner == var1;
      }

      public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
         if (this.mOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            LiveData.this.removeObserver(this.mObserver);
         } else {
            this.activeStateChanged(this.shouldBeActive());
         }
      }

      boolean shouldBeActive() {
         return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
      }
   }

   private abstract class ObserverWrapper {
      boolean mActive;
      int mLastVersion = -1;
      final Observer mObserver;

      ObserverWrapper(Observer var2) {
         this.mObserver = var2;
      }

      void activeStateChanged(boolean var1) {
         if (var1 != this.mActive) {
            this.mActive = var1;
            int var2 = LiveData.this.mActiveCount;
            byte var3 = 1;
            boolean var6;
            if (var2 == 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            LiveData var5 = LiveData.this;
            int var4 = var5.mActiveCount;
            if (!this.mActive) {
               var3 = -1;
            }

            var5.mActiveCount = var4 + var3;
            if (var6 && this.mActive) {
               LiveData.this.onActive();
            }

            if (LiveData.this.mActiveCount == 0 && !this.mActive) {
               LiveData.this.onInactive();
            }

            if (this.mActive) {
               LiveData.this.dispatchingValue(this);
            }

         }
      }

      void detachObserver() {
      }

      boolean isAttachedTo(LifecycleOwner var1) {
         return false;
      }

      abstract boolean shouldBeActive();
   }
}
