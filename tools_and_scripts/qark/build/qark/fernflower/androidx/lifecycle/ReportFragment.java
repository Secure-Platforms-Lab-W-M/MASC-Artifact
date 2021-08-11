package androidx.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.os.Build.VERSION;

public class ReportFragment extends Fragment {
   private static final String REPORT_FRAGMENT_TAG = "androidx.lifecycle.LifecycleDispatcher.report_fragment_tag";
   private ReportFragment.ActivityInitializationListener mProcessListener;

   static void dispatch(Activity var0, Lifecycle.Event var1) {
      if (var0 instanceof LifecycleRegistryOwner) {
         ((LifecycleRegistryOwner)var0).getLifecycle().handleLifecycleEvent(var1);
      } else {
         if (var0 instanceof LifecycleOwner) {
            Lifecycle var2 = ((LifecycleOwner)var0).getLifecycle();
            if (var2 instanceof LifecycleRegistry) {
               ((LifecycleRegistry)var2).handleLifecycleEvent(var1);
            }
         }

      }
   }

   private void dispatch(Lifecycle.Event var1) {
      if (VERSION.SDK_INT < 29) {
         dispatch(this.getActivity(), var1);
      }

   }

   private void dispatchCreate(ReportFragment.ActivityInitializationListener var1) {
      if (var1 != null) {
         var1.onCreate();
      }

   }

   private void dispatchResume(ReportFragment.ActivityInitializationListener var1) {
      if (var1 != null) {
         var1.onResume();
      }

   }

   private void dispatchStart(ReportFragment.ActivityInitializationListener var1) {
      if (var1 != null) {
         var1.onStart();
      }

   }

   static ReportFragment get(Activity var0) {
      return (ReportFragment)var0.getFragmentManager().findFragmentByTag("androidx.lifecycle.LifecycleDispatcher.report_fragment_tag");
   }

   public static void injectIfNeededIn(Activity var0) {
      if (VERSION.SDK_INT >= 29) {
         var0.registerActivityLifecycleCallbacks(new ReportFragment.LifecycleCallbacks());
      }

      FragmentManager var1 = var0.getFragmentManager();
      if (var1.findFragmentByTag("androidx.lifecycle.LifecycleDispatcher.report_fragment_tag") == null) {
         var1.beginTransaction().add(new ReportFragment(), "androidx.lifecycle.LifecycleDispatcher.report_fragment_tag").commit();
         var1.executePendingTransactions();
      }

   }

   public void onActivityCreated(Bundle var1) {
      super.onActivityCreated(var1);
      this.dispatchCreate(this.mProcessListener);
      this.dispatch(Lifecycle.Event.ON_CREATE);
   }

   public void onDestroy() {
      super.onDestroy();
      this.dispatch(Lifecycle.Event.ON_DESTROY);
      this.mProcessListener = null;
   }

   public void onPause() {
      super.onPause();
      this.dispatch(Lifecycle.Event.ON_PAUSE);
   }

   public void onResume() {
      super.onResume();
      this.dispatchResume(this.mProcessListener);
      this.dispatch(Lifecycle.Event.ON_RESUME);
   }

   public void onStart() {
      super.onStart();
      this.dispatchStart(this.mProcessListener);
      this.dispatch(Lifecycle.Event.ON_START);
   }

   public void onStop() {
      super.onStop();
      this.dispatch(Lifecycle.Event.ON_STOP);
   }

   void setProcessListener(ReportFragment.ActivityInitializationListener var1) {
      this.mProcessListener = var1;
   }

   interface ActivityInitializationListener {
      void onCreate();

      void onResume();

      void onStart();
   }

   static class LifecycleCallbacks implements ActivityLifecycleCallbacks {
      public void onActivityCreated(Activity var1, Bundle var2) {
      }

      public void onActivityDestroyed(Activity var1) {
      }

      public void onActivityPaused(Activity var1) {
      }

      public void onActivityPostCreated(Activity var1, Bundle var2) {
         ReportFragment.dispatch(var1, Lifecycle.Event.ON_CREATE);
      }

      public void onActivityPostResumed(Activity var1) {
         ReportFragment.dispatch(var1, Lifecycle.Event.ON_RESUME);
      }

      public void onActivityPostStarted(Activity var1) {
         ReportFragment.dispatch(var1, Lifecycle.Event.ON_START);
      }

      public void onActivityPreDestroyed(Activity var1) {
         ReportFragment.dispatch(var1, Lifecycle.Event.ON_DESTROY);
      }

      public void onActivityPrePaused(Activity var1) {
         ReportFragment.dispatch(var1, Lifecycle.Event.ON_PAUSE);
      }

      public void onActivityPreStopped(Activity var1) {
         ReportFragment.dispatch(var1, Lifecycle.Event.ON_STOP);
      }

      public void onActivityResumed(Activity var1) {
      }

      public void onActivitySaveInstanceState(Activity var1, Bundle var2) {
      }

      public void onActivityStarted(Activity var1) {
      }

      public void onActivityStopped(Activity var1) {
      }
   }
}
