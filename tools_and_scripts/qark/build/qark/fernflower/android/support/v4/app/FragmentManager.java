package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public abstract class FragmentManager {
   public static final int POP_BACK_STACK_INCLUSIVE = 1;

   public static void enableDebugLogging(boolean var0) {
      FragmentManagerImpl.DEBUG = var0;
   }

   public abstract void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1);

   public abstract FragmentTransaction beginTransaction();

   public abstract void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4);

   public abstract boolean executePendingTransactions();

   public abstract Fragment findFragmentById(@IdRes int var1);

   public abstract Fragment findFragmentByTag(String var1);

   public abstract FragmentManager.BackStackEntry getBackStackEntryAt(int var1);

   public abstract int getBackStackEntryCount();

   public abstract Fragment getFragment(Bundle var1, String var2);

   public abstract List getFragments();

   public abstract Fragment getPrimaryNavigationFragment();

   public abstract boolean isDestroyed();

   public abstract boolean isStateSaved();

   @Deprecated
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public FragmentTransaction openTransaction() {
      return this.beginTransaction();
   }

   public abstract void popBackStack();

   public abstract void popBackStack(int var1, int var2);

   public abstract void popBackStack(String var1, int var2);

   public abstract boolean popBackStackImmediate();

   public abstract boolean popBackStackImmediate(int var1, int var2);

   public abstract boolean popBackStackImmediate(String var1, int var2);

   public abstract void putFragment(Bundle var1, String var2, Fragment var3);

   public abstract void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1, boolean var2);

   public abstract void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1);

   public abstract Fragment.SavedState saveFragmentInstanceState(Fragment var1);

   public abstract void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1);

   public interface BackStackEntry {
      CharSequence getBreadCrumbShortTitle();

      @StringRes
      int getBreadCrumbShortTitleRes();

      CharSequence getBreadCrumbTitle();

      @StringRes
      int getBreadCrumbTitleRes();

      int getId();

      String getName();
   }

   public abstract static class FragmentLifecycleCallbacks {
      public void onFragmentActivityCreated(FragmentManager var1, Fragment var2, Bundle var3) {
      }

      public void onFragmentAttached(FragmentManager var1, Fragment var2, Context var3) {
      }

      public void onFragmentCreated(FragmentManager var1, Fragment var2, Bundle var3) {
      }

      public void onFragmentDestroyed(FragmentManager var1, Fragment var2) {
      }

      public void onFragmentDetached(FragmentManager var1, Fragment var2) {
      }

      public void onFragmentPaused(FragmentManager var1, Fragment var2) {
      }

      public void onFragmentPreAttached(FragmentManager var1, Fragment var2, Context var3) {
      }

      public void onFragmentPreCreated(FragmentManager var1, Fragment var2, Bundle var3) {
      }

      public void onFragmentResumed(FragmentManager var1, Fragment var2) {
      }

      public void onFragmentSaveInstanceState(FragmentManager var1, Fragment var2, Bundle var3) {
      }

      public void onFragmentStarted(FragmentManager var1, Fragment var2) {
      }

      public void onFragmentStopped(FragmentManager var1, Fragment var2) {
      }

      public void onFragmentViewCreated(FragmentManager var1, Fragment var2, View var3, Bundle var4) {
      }

      public void onFragmentViewDestroyed(FragmentManager var1, Fragment var2) {
      }
   }

   public interface OnBackStackChangedListener {
      void onBackStackChanged();
   }
}
