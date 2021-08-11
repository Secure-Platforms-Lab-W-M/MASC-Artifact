package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;

public class ProcessLifecycleOwner implements LifecycleOwner {
   static final long TIMEOUT_MS = 700L;
   private static final ProcessLifecycleOwner sInstance = new ProcessLifecycleOwner();
   private Runnable mDelayedPauseRunnable = new Runnable() {
      public void run() {
         ProcessLifecycleOwner.this.dispatchPauseIfNeeded();
         ProcessLifecycleOwner.this.dispatchStopIfNeeded();
      }
   };
   private Handler mHandler;
   ReportFragment.ActivityInitializationListener mInitializationListener = new ReportFragment.ActivityInitializationListener() {
      public void onCreate() {
      }

      public void onResume() {
         ProcessLifecycleOwner.this.activityResumed();
      }

      public void onStart() {
         ProcessLifecycleOwner.this.activityStarted();
      }
   };
   private boolean mPauseSent = true;
   private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
   private int mResumedCounter = 0;
   private int mStartedCounter = 0;
   private boolean mStopSent = true;

   private ProcessLifecycleOwner() {
   }

   public static LifecycleOwner get() {
      return sInstance;
   }

   static void init(Context var0) {
      sInstance.attach(var0);
   }

   void activityPaused() {
      int var1 = this.mResumedCounter - 1;
      this.mResumedCounter = var1;
      if (var1 == 0) {
         this.mHandler.postDelayed(this.mDelayedPauseRunnable, 700L);
      }

   }

   void activityResumed() {
      int var1 = this.mResumedCounter + 1;
      this.mResumedCounter = var1;
      if (var1 == 1) {
         if (this.mPauseSent) {
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            this.mPauseSent = false;
            return;
         }

         this.mHandler.removeCallbacks(this.mDelayedPauseRunnable);
      }

   }

   void activityStarted() {
      int var1 = this.mStartedCounter + 1;
      this.mStartedCounter = var1;
      if (var1 == 1 && this.mStopSent) {
         this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
         this.mStopSent = false;
      }

   }

   void activityStopped() {
      --this.mStartedCounter;
      this.dispatchStopIfNeeded();
   }

   void attach(Context var1) {
      this.mHandler = new Handler();
      this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
      ((Application)var1.getApplicationContext()).registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
         public void onActivityCreated(Activity var1, Bundle var2) {
            if (VERSION.SDK_INT < 29) {
               ReportFragment.get(var1).setProcessListener(ProcessLifecycleOwner.this.mInitializationListener);
            }

         }

         public void onActivityPaused(Activity var1) {
            ProcessLifecycleOwner.this.activityPaused();
         }

         public void onActivityPreCreated(Activity var1, Bundle var2) {
            var1.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
               public void onActivityPostResumed(Activity var1) {
                  ProcessLifecycleOwner.this.activityResumed();
               }

               public void onActivityPostStarted(Activity var1) {
                  ProcessLifecycleOwner.this.activityStarted();
               }
            });
         }

         public void onActivityStopped(Activity var1) {
            ProcessLifecycleOwner.this.activityStopped();
         }
      });
   }

   void dispatchPauseIfNeeded() {
      if (this.mResumedCounter == 0) {
         this.mPauseSent = true;
         this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
      }

   }

   void dispatchStopIfNeeded() {
      if (this.mStartedCounter == 0 && this.mPauseSent) {
         this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
         this.mStopSent = true;
      }

   }

   public Lifecycle getLifecycle() {
      return this.mRegistry;
   }
}
