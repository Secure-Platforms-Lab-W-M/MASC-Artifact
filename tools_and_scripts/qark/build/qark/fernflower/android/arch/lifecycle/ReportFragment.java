package android.arch.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ReportFragment extends Fragment {
   private static final String REPORT_FRAGMENT_TAG = "android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag";
   private ReportFragment.ActivityInitializationListener mProcessListener;

   private void dispatch(Lifecycle.Event var1) {
      Activity var2 = this.getActivity();
      if (var2 instanceof LifecycleRegistryOwner) {
         ((LifecycleRegistryOwner)var2).getLifecycle().handleLifecycleEvent(var1);
      } else if (var2 instanceof LifecycleOwner) {
         Lifecycle var3 = ((LifecycleOwner)var2).getLifecycle();
         if (var3 instanceof LifecycleRegistry) {
            ((LifecycleRegistry)var3).handleLifecycleEvent(var1);
         }
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
      return (ReportFragment)var0.getFragmentManager().findFragmentByTag("android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag");
   }

   public static void injectIfNeededIn(Activity var0) {
      FragmentManager var1 = var0.getFragmentManager();
      if (var1.findFragmentByTag("android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag") == null) {
         var1.beginTransaction().add(new ReportFragment(), "android.arch.lifecycle.LifecycleDispatcher.report_fragment_tag").commit();
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
}
