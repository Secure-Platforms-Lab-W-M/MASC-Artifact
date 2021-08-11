package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

class FragmentLifecycleCallbacksDispatcher {
   private final FragmentManager mFragmentManager;
   private final CopyOnWriteArrayList mLifecycleCallbacks = new CopyOnWriteArrayList();

   FragmentLifecycleCallbacksDispatcher(FragmentManager var1) {
      this.mFragmentManager = var1;
   }

   void dispatchOnFragmentActivityCreated(Fragment var1, Bundle var2, boolean var3) {
      Fragment var4 = this.mFragmentManager.getParent();
      if (var4 != null) {
         var4.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentActivityCreated(var1, var2, true);
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var6.next();
         } while(var3 && !var5.mRecursive);

         var5.mCallback.onFragmentActivityCreated(this.mFragmentManager, var1, var2);
      }
   }

   void dispatchOnFragmentAttached(Fragment var1, Context var2, boolean var3) {
      Fragment var4 = this.mFragmentManager.getParent();
      if (var4 != null) {
         var4.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentAttached(var1, var2, true);
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var6.next();
         } while(var3 && !var5.mRecursive);

         var5.mCallback.onFragmentAttached(this.mFragmentManager, var1, var2);
      }
   }

   void dispatchOnFragmentCreated(Fragment var1, Bundle var2, boolean var3) {
      Fragment var4 = this.mFragmentManager.getParent();
      if (var4 != null) {
         var4.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentCreated(var1, var2, true);
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var6.next();
         } while(var3 && !var5.mRecursive);

         var5.mCallback.onFragmentCreated(this.mFragmentManager, var1, var2);
      }
   }

   void dispatchOnFragmentDestroyed(Fragment var1, boolean var2) {
      Fragment var3 = this.mFragmentManager.getParent();
      if (var3 != null) {
         var3.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentDestroyed(var1, true);
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var5.next();
         } while(var2 && !var4.mRecursive);

         var4.mCallback.onFragmentDestroyed(this.mFragmentManager, var1);
      }
   }

   void dispatchOnFragmentDetached(Fragment var1, boolean var2) {
      Fragment var3 = this.mFragmentManager.getParent();
      if (var3 != null) {
         var3.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentDetached(var1, true);
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var5.next();
         } while(var2 && !var4.mRecursive);

         var4.mCallback.onFragmentDetached(this.mFragmentManager, var1);
      }
   }

   void dispatchOnFragmentPaused(Fragment var1, boolean var2) {
      Fragment var3 = this.mFragmentManager.getParent();
      if (var3 != null) {
         var3.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentPaused(var1, true);
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var5.next();
         } while(var2 && !var4.mRecursive);

         var4.mCallback.onFragmentPaused(this.mFragmentManager, var1);
      }
   }

   void dispatchOnFragmentPreAttached(Fragment var1, Context var2, boolean var3) {
      Fragment var4 = this.mFragmentManager.getParent();
      if (var4 != null) {
         var4.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentPreAttached(var1, var2, true);
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var6.next();
         } while(var3 && !var5.mRecursive);

         var5.mCallback.onFragmentPreAttached(this.mFragmentManager, var1, var2);
      }
   }

   void dispatchOnFragmentPreCreated(Fragment var1, Bundle var2, boolean var3) {
      Fragment var4 = this.mFragmentManager.getParent();
      if (var4 != null) {
         var4.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentPreCreated(var1, var2, true);
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var6.next();
         } while(var3 && !var5.mRecursive);

         var5.mCallback.onFragmentPreCreated(this.mFragmentManager, var1, var2);
      }
   }

   void dispatchOnFragmentResumed(Fragment var1, boolean var2) {
      Fragment var3 = this.mFragmentManager.getParent();
      if (var3 != null) {
         var3.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentResumed(var1, true);
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var5.next();
         } while(var2 && !var4.mRecursive);

         var4.mCallback.onFragmentResumed(this.mFragmentManager, var1);
      }
   }

   void dispatchOnFragmentSaveInstanceState(Fragment var1, Bundle var2, boolean var3) {
      Fragment var4 = this.mFragmentManager.getParent();
      if (var4 != null) {
         var4.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentSaveInstanceState(var1, var2, true);
      }

      Iterator var6 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var5;
         do {
            if (!var6.hasNext()) {
               return;
            }

            var5 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var6.next();
         } while(var3 && !var5.mRecursive);

         var5.mCallback.onFragmentSaveInstanceState(this.mFragmentManager, var1, var2);
      }
   }

   void dispatchOnFragmentStarted(Fragment var1, boolean var2) {
      Fragment var3 = this.mFragmentManager.getParent();
      if (var3 != null) {
         var3.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentStarted(var1, true);
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var5.next();
         } while(var2 && !var4.mRecursive);

         var4.mCallback.onFragmentStarted(this.mFragmentManager, var1);
      }
   }

   void dispatchOnFragmentStopped(Fragment var1, boolean var2) {
      Fragment var3 = this.mFragmentManager.getParent();
      if (var3 != null) {
         var3.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentStopped(var1, true);
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var5.next();
         } while(var2 && !var4.mRecursive);

         var4.mCallback.onFragmentStopped(this.mFragmentManager, var1);
      }
   }

   void dispatchOnFragmentViewCreated(Fragment var1, View var2, Bundle var3, boolean var4) {
      Fragment var5 = this.mFragmentManager.getParent();
      if (var5 != null) {
         var5.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentViewCreated(var1, var2, var3, true);
      }

      Iterator var7 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var6;
         do {
            if (!var7.hasNext()) {
               return;
            }

            var6 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var7.next();
         } while(var4 && !var6.mRecursive);

         var6.mCallback.onFragmentViewCreated(this.mFragmentManager, var1, var2, var3);
      }
   }

   void dispatchOnFragmentViewDestroyed(Fragment var1, boolean var2) {
      Fragment var3 = this.mFragmentManager.getParent();
      if (var3 != null) {
         var3.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentViewDestroyed(var1, true);
      }

      Iterator var5 = this.mLifecycleCallbacks.iterator();

      while(true) {
         FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder var4;
         do {
            if (!var5.hasNext()) {
               return;
            }

            var4 = (FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)var5.next();
         } while(var2 && !var4.mRecursive);

         var4.mCallback.onFragmentViewDestroyed(this.mFragmentManager, var1);
      }
   }

   public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1, boolean var2) {
      this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder(var1, var2));
   }

   public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1) {
      CopyOnWriteArrayList var4 = this.mLifecycleCallbacks;
      synchronized(var4){}
      int var2 = 0;

      Throwable var10000;
      boolean var10001;
      label245: {
         int var3;
         try {
            var3 = this.mLifecycleCallbacks.size();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label245;
         }

         for(; var2 < var3; ++var2) {
            try {
               if (((FragmentLifecycleCallbacksDispatcher.FragmentLifecycleCallbacksHolder)this.mLifecycleCallbacks.get(var2)).mCallback == var1) {
                  this.mLifecycleCallbacks.remove(var2);
                  break;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label245;
            }
         }

         label224:
         try {
            return;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label224;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   private static final class FragmentLifecycleCallbacksHolder {
      final FragmentManager.FragmentLifecycleCallbacks mCallback;
      final boolean mRecursive;

      FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks var1, boolean var2) {
         this.mCallback = var1;
         this.mRecursive = var2;
      }
   }
}
