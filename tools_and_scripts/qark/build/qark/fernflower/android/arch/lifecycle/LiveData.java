package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class LiveData {
   private static final Object NOT_SET = new Object();
   static final int START_VERSION = -1;
   private int mActiveCount = 0;
   private volatile Object mData;
   private final Object mDataLock = new Object();
   private boolean mDispatchInvalidated;
   private boolean mDispatchingValue;
   private SafeIterableMap mObservers = new SafeIterableMap();
   private volatile Object mPendingData;
   private final Runnable mPostValueRunnable;
   private int mVersion;

   public LiveData() {
      this.mData = NOT_SET;
      this.mPendingData = NOT_SET;
      this.mVersion = -1;
      this.mPostValueRunnable = new Runnable() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      };
   }

   // $FF: synthetic method
   static Object access$000(LiveData var0) {
      return var0.mDataLock;
   }

   // $FF: synthetic method
   static Object access$100(LiveData var0) {
      return var0.mPendingData;
   }

   // $FF: synthetic method
   static Object access$102(LiveData var0, Object var1) {
      var0.mPendingData = var1;
      return var1;
   }

   // $FF: synthetic method
   static Object access$200() {
      return NOT_SET;
   }

   private static void assertMainThread(String var0) {
      if (!ArchTaskExecutor.getInstance().isMainThread()) {
         throw new IllegalStateException("Cannot invoke " + var0 + " on a background" + " thread");
      }
   }

   private void considerNotify(LiveData.ObserverWrapper var1) {
      if (var1.mActive) {
         if (!var1.shouldBeActive()) {
            var1.activeStateChanged(false);
            return;
         }

         if (var1.mLastVersion < this.mVersion) {
            var1.mLastVersion = this.mVersion;
            var1.mObserver.onChanged(this.mData);
            return;
         }
      }

   }

   private void dispatchingValue(@Nullable LiveData.ObserverWrapper var1) {
      if (this.mDispatchingValue) {
         this.mDispatchInvalidated = true;
      } else {
         this.mDispatchingValue = true;

         do {
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

            var1 = var2;
         } while(this.mDispatchInvalidated);

         this.mDispatchingValue = false;
      }
   }

   @Nullable
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

   @MainThread
   public void observe(@NonNull LifecycleOwner var1, @NonNull Observer var2) {
      if (var1.getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED) {
         LiveData.LifecycleBoundObserver var3 = new LiveData.LifecycleBoundObserver(var1, var2);
         LiveData.ObserverWrapper var4 = (LiveData.ObserverWrapper)this.mObservers.putIfAbsent(var2, var3);
         if (var4 != null && !var4.isAttachedTo(var1)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
         }

         if (var4 == null) {
            var1.getLifecycle().addObserver(var3);
            return;
         }
      }

   }

   @MainThread
   public void observeForever(@NonNull Observer var1) {
      LiveData.AlwaysActiveObserver var2 = new LiveData.AlwaysActiveObserver(var1);
      LiveData.ObserverWrapper var3 = (LiveData.ObserverWrapper)this.mObservers.putIfAbsent(var1, var2);
      if (var3 != null && var3 instanceof LiveData.LifecycleBoundObserver) {
         throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
      } else if (var3 == null) {
         var2.activeStateChanged(true);
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

   @MainThread
   public void removeObserver(@NonNull Observer var1) {
      assertMainThread("removeObserver");
      LiveData.ObserverWrapper var2 = (LiveData.ObserverWrapper)this.mObservers.remove(var1);
      if (var2 != null) {
         var2.detachObserver();
         var2.activeStateChanged(false);
      }
   }

   @MainThread
   public void removeObservers(@NonNull LifecycleOwner var1) {
      assertMainThread("removeObservers");
      Iterator var2 = this.mObservers.iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (((LiveData.ObserverWrapper)var3.getValue()).isAttachedTo(var1)) {
            this.removeObserver((Observer)var3.getKey());
         }
      }

   }

   @MainThread
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

   class LifecycleBoundObserver extends LiveData.ObserverWrapper implements GenericLifecycleObserver {
      @NonNull
      final LifecycleOwner mOwner;

      LifecycleBoundObserver(@NonNull LifecycleOwner var2, Observer var3) {
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
         byte var3 = 1;
         if (var1 != this.mActive) {
            this.mActive = var1;
            boolean var2;
            if (LiveData.this.mActiveCount == 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            LiveData var5 = LiveData.this;
            int var4 = var5.mActiveCount;
            if (!this.mActive) {
               var3 = -1;
            }

            var5.mActiveCount = var3 + var4;
            if (var2 && this.mActive) {
               LiveData.this.onActive();
            }

            if (LiveData.this.mActiveCount == 0 && !this.mActive) {
               LiveData.this.onInactive();
            }

            if (this.mActive) {
               LiveData.this.dispatchingValue(this);
               return;
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
