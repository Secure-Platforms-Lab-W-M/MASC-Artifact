package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.LayoutInflater.Factory2;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.collection.ArraySet;
import androidx.core.os.CancellationSignal;
import androidx.core.util.LogWriter;
import androidx.fragment.R.id;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FragmentManager {
   private static boolean DEBUG = false;
   public static final int POP_BACK_STACK_INCLUSIVE = 1;
   static final String TAG = "FragmentManager";
   ArrayList mBackStack;
   private ArrayList mBackStackChangeListeners;
   private final AtomicInteger mBackStackIndex = new AtomicInteger();
   FragmentContainer mContainer;
   private ArrayList mCreatedMenus;
   int mCurState = -1;
   private boolean mDestroyed;
   private Runnable mExecCommit = new Runnable() {
      public void run() {
         FragmentManager.this.execPendingActions(true);
      }
   };
   private boolean mExecutingActions;
   private ConcurrentHashMap mExitAnimationCancellationSignals = new ConcurrentHashMap();
   private FragmentFactory mFragmentFactory = null;
   private final FragmentStore mFragmentStore = new FragmentStore();
   private final FragmentTransition.Callback mFragmentTransitionCallback = new FragmentTransition.Callback() {
      public void onComplete(Fragment var1, CancellationSignal var2) {
         if (!var2.isCanceled()) {
            FragmentManager.this.removeCancellationSignal(var1, var2);
         }

      }

      public void onStart(Fragment var1, CancellationSignal var2) {
         FragmentManager.this.addCancellationSignal(var1, var2);
      }
   };
   private boolean mHavePendingDeferredStart;
   FragmentHostCallback mHost;
   private FragmentFactory mHostFragmentFactory = new FragmentFactory() {
      public Fragment instantiate(ClassLoader var1, String var2) {
         return FragmentManager.this.mHost.instantiate(FragmentManager.this.mHost.getContext(), var2, (Bundle)null);
      }
   };
   private final FragmentLayoutInflaterFactory mLayoutInflaterFactory = new FragmentLayoutInflaterFactory(this);
   private final FragmentLifecycleCallbacksDispatcher mLifecycleCallbacksDispatcher = new FragmentLifecycleCallbacksDispatcher(this);
   private boolean mNeedMenuInvalidate;
   private FragmentManagerViewModel mNonConfig;
   private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) {
      public void handleOnBackPressed() {
         FragmentManager.this.handleOnBackPressed();
      }
   };
   private OnBackPressedDispatcher mOnBackPressedDispatcher;
   private Fragment mParent;
   private final ArrayList mPendingActions = new ArrayList();
   private ArrayList mPostponedTransactions;
   Fragment mPrimaryNav;
   private boolean mStateSaved;
   private boolean mStopped;
   private ArrayList mTmpAddedFragments;
   private ArrayList mTmpIsPop;
   private ArrayList mTmpRecords;

   private void addAddedFragments(ArraySet var1) {
      int var2 = this.mCurState;
      if (var2 >= 1) {
         var2 = Math.min(var2, 3);
         Iterator var3 = this.mFragmentStore.getFragments().iterator();

         while(var3.hasNext()) {
            Fragment var4 = (Fragment)var3.next();
            if (var4.mState < var2) {
               this.moveToState(var4, var2);
               if (var4.mView != null && !var4.mHidden && var4.mIsNewlyAdded) {
                  var1.add(var4);
               }
            }
         }

      }
   }

   private void cancelExitAnimation(Fragment var1) {
      HashSet var2 = (HashSet)this.mExitAnimationCancellationSignals.get(var1);
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            ((CancellationSignal)var3.next()).cancel();
         }

         var2.clear();
         this.destroyFragmentView(var1);
         this.mExitAnimationCancellationSignals.remove(var1);
      }

   }

   private void checkStateLoss() {
      if (this.isStateSaved()) {
         throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
      }
   }

   private void cleanupExec() {
      this.mExecutingActions = false;
      this.mTmpIsPop.clear();
      this.mTmpRecords.clear();
   }

   private void completeShowHideFragment(final Fragment var1) {
      if (var1.mView != null) {
         FragmentAnim.AnimationOrAnimator var3 = FragmentAnim.loadAnimation(this.mHost.getContext(), this.mContainer, var1, var1.mHidden ^ true);
         if (var3 != null && var3.animator != null) {
            var3.animator.setTarget(var1.mView);
            if (var1.mHidden) {
               if (var1.isHideReplaced()) {
                  var1.setHideReplaced(false);
               } else {
                  final ViewGroup var4 = var1.mContainer;
                  final View var5 = var1.mView;
                  var4.startViewTransition(var5);
                  var3.animator.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1x) {
                        var4.endViewTransition(var5);
                        var1x.removeListener(this);
                        if (var1.mView != null && var1.mHidden) {
                           var1.mView.setVisibility(8);
                        }

                     }
                  });
               }
            } else {
               var1.mView.setVisibility(0);
            }

            var3.animator.start();
         } else {
            if (var3 != null) {
               var1.mView.startAnimation(var3.animation);
               var3.animation.start();
            }

            byte var2;
            if (var1.mHidden && !var1.isHideReplaced()) {
               var2 = 8;
            } else {
               var2 = 0;
            }

            var1.mView.setVisibility(var2);
            if (var1.isHideReplaced()) {
               var1.setHideReplaced(false);
            }
         }
      }

      if (var1.mAdded && this.isMenuAvailable(var1)) {
         this.mNeedMenuInvalidate = true;
      }

      var1.mHiddenChanged = false;
      var1.onHiddenChanged(var1.mHidden);
   }

   private void destroyFragmentView(Fragment var1) {
      var1.performDestroyView();
      this.mLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(var1, false);
      var1.mContainer = null;
      var1.mView = null;
      var1.mViewLifecycleOwner = null;
      var1.mViewLifecycleOwnerLiveData.setValue((Object)null);
      var1.mInLayout = false;
   }

   private void dispatchParentPrimaryNavigationFragmentChanged(Fragment var1) {
      if (var1 != null && var1.equals(this.findActiveFragment(var1.mWho))) {
         var1.performPrimaryNavigationFragmentChanged();
      }

   }

   private void dispatchStateChange(int var1) {
      try {
         this.mExecutingActions = true;
         this.mFragmentStore.dispatchStateChange(var1);
         this.moveToState(var1, false);
      } finally {
         this.mExecutingActions = false;
      }

      this.execPendingActions(true);
   }

   private void doPendingDeferredStart() {
      if (this.mHavePendingDeferredStart) {
         this.mHavePendingDeferredStart = false;
         this.startPendingDeferredFragments();
      }

   }

   @Deprecated
   public static void enableDebugLogging(boolean var0) {
      DEBUG = var0;
   }

   private void endAnimatingAwayFragments() {
      if (!this.mExitAnimationCancellationSignals.isEmpty()) {
         Iterator var1 = this.mExitAnimationCancellationSignals.keySet().iterator();

         while(var1.hasNext()) {
            Fragment var2 = (Fragment)var1.next();
            this.cancelExitAnimation(var2);
            this.moveToState(var2, var2.getStateAfterAnimating());
         }
      }

   }

   private void ensureExecReady(boolean var1) {
      if (!this.mExecutingActions) {
         if (this.mHost == null) {
            if (this.mDestroyed) {
               throw new IllegalStateException("FragmentManager has been destroyed");
            } else {
               throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
         } else if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
            if (!var1) {
               this.checkStateLoss();
            }

            if (this.mTmpRecords == null) {
               this.mTmpRecords = new ArrayList();
               this.mTmpIsPop = new ArrayList();
            }

            this.mExecutingActions = true;

            try {
               this.executePostponedTransaction((ArrayList)null, (ArrayList)null);
            } finally {
               this.mExecutingActions = false;
            }

         } else {
            throw new IllegalStateException("Must be called from main thread of fragment host");
         }
      } else {
         throw new IllegalStateException("FragmentManager is already executing transactions");
      }
   }

   private static void executeOps(ArrayList var0, ArrayList var1, int var2, int var3) {
      for(; var2 < var3; ++var2) {
         BackStackRecord var6 = (BackStackRecord)var0.get(var2);
         boolean var5 = (Boolean)var1.get(var2);
         boolean var4 = true;
         if (var5) {
            var6.bumpBackStackNesting(-1);
            if (var2 != var3 - 1) {
               var4 = false;
            }

            var6.executePopOps(var4);
         } else {
            var6.bumpBackStackNesting(1);
            var6.executeOps();
         }
      }

   }

   private void executeOpsTogether(ArrayList var1, ArrayList var2, int var3, int var4) {
      boolean var9 = ((BackStackRecord)var1.get(var3)).mReorderingAllowed;
      ArrayList var10 = this.mTmpAddedFragments;
      if (var10 == null) {
         this.mTmpAddedFragments = new ArrayList();
      } else {
         var10.clear();
      }

      this.mTmpAddedFragments.addAll(this.mFragmentStore.getFragments());
      Fragment var13 = this.getPrimaryNavigationFragment();
      int var7 = var3;
      boolean var6 = false;

      while(true) {
         boolean var8 = true;
         if (var7 >= var4) {
            this.mTmpAddedFragments.clear();
            if (!var9) {
               FragmentTransition.startTransitions(this, var1, var2, var3, var4, false, this.mFragmentTransitionCallback);
            }

            executeOps(var1, var2, var3, var4);
            int var12 = var4;
            if (var9) {
               ArraySet var14 = new ArraySet();
               this.addAddedFragments(var14);
               var12 = this.postponePostponableTransactions(var1, var2, var3, var4, var14);
               this.makeRemovedFragmentsInvisible(var14);
            }

            if (var12 != var3 && var9) {
               FragmentTransition.startTransitions(this, var1, var2, var3, var12, true, this.mFragmentTransitionCallback);
               this.moveToState(this.mCurState, true);
            }

            while(var3 < var4) {
               BackStackRecord var15 = (BackStackRecord)var1.get(var3);
               if ((Boolean)var2.get(var3) && var15.mIndex >= 0) {
                  var15.mIndex = -1;
               }

               var15.runOnCommitRunnables();
               ++var3;
            }

            if (var6) {
               this.reportBackStackChanged();
            }

            return;
         }

         BackStackRecord var11 = (BackStackRecord)var1.get(var7);
         if (!(Boolean)var2.get(var7)) {
            var13 = var11.expandOps(this.mTmpAddedFragments, var13);
         } else {
            var13 = var11.trackAddedFragmentsInPop(this.mTmpAddedFragments, var13);
         }

         boolean var5 = var8;
         if (!var6) {
            if (var11.mAddToBackStack) {
               var5 = var8;
            } else {
               var5 = false;
            }
         }

         ++var7;
         var6 = var5;
      }
   }

   private void executePostponedTransaction(ArrayList var1, ArrayList var2) {
      ArrayList var7 = this.mPostponedTransactions;
      int var3;
      if (var7 == null) {
         var3 = 0;
      } else {
         var3 = var7.size();
      }

      int var4 = 0;

      for(int var6 = var3; var4 < var6; var6 = var3) {
         int var5;
         label54: {
            FragmentManager.StartEnterTransitionListener var8 = (FragmentManager.StartEnterTransitionListener)this.mPostponedTransactions.get(var4);
            if (var1 != null && !var8.mIsBack) {
               var3 = var1.indexOf(var8.mRecord);
               if (var3 != -1 && var2 != null && (Boolean)var2.get(var3)) {
                  this.mPostponedTransactions.remove(var4);
                  var5 = var4 - 1;
                  var3 = var6 - 1;
                  var8.cancelTransaction();
                  break label54;
               }
            }

            if (!var8.isReady()) {
               var3 = var6;
               var5 = var4;
               if (var1 == null) {
                  break label54;
               }

               var3 = var6;
               var5 = var4;
               if (!var8.mRecord.interactsWith(var1, 0, var1.size())) {
                  break label54;
               }
            }

            this.mPostponedTransactions.remove(var4);
            var5 = var4 - 1;
            var3 = var6 - 1;
            if (var1 != null && !var8.mIsBack) {
               var4 = var1.indexOf(var8.mRecord);
               if (var4 != -1 && var2 != null && (Boolean)var2.get(var4)) {
                  var8.cancelTransaction();
                  break label54;
               }
            }

            var8.completeTransaction();
         }

         var4 = var5 + 1;
      }

   }

   public static Fragment findFragment(View var0) {
      Fragment var1 = findViewFragment(var0);
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("View ");
         var2.append(var0);
         var2.append(" does not have a Fragment set");
         throw new IllegalStateException(var2.toString());
      }
   }

   static FragmentManager findFragmentManager(View var0) {
      Fragment var1 = findViewFragment(var0);
      if (var1 != null) {
         return var1.getChildFragmentManager();
      } else {
         Context var4 = var0.getContext();
         Object var3 = null;

         FragmentActivity var2;
         while(true) {
            var2 = (FragmentActivity)var3;
            if (!(var4 instanceof ContextWrapper)) {
               break;
            }

            if (var4 instanceof FragmentActivity) {
               var2 = (FragmentActivity)var4;
               break;
            }

            var4 = ((ContextWrapper)var4).getBaseContext();
         }

         if (var2 != null) {
            return var2.getSupportFragmentManager();
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("View ");
            var5.append(var0);
            var5.append(" is not within a subclass of FragmentActivity.");
            throw new IllegalStateException(var5.toString());
         }
      }
   }

   private static Fragment findViewFragment(View var0) {
      while(true) {
         Object var1 = null;
         if (var0 == null) {
            return null;
         }

         Fragment var2 = getViewFragment(var0);
         if (var2 != null) {
            return var2;
         }

         ViewParent var3 = var0.getParent();
         var0 = (View)var1;
         if (var3 instanceof View) {
            var0 = (View)var3;
         }
      }
   }

   private void forcePostponedTransactions() {
      if (this.mPostponedTransactions != null) {
         while(!this.mPostponedTransactions.isEmpty()) {
            ((FragmentManager.StartEnterTransitionListener)this.mPostponedTransactions.remove(0)).completeTransaction();
         }
      }

   }

   private boolean generateOpsForPendingActions(ArrayList var1, ArrayList var2) {
      boolean var5 = false;
      ArrayList var6 = this.mPendingActions;
      synchronized(var6){}

      Throwable var10000;
      boolean var10001;
      label313: {
         try {
            if (this.mPendingActions.isEmpty()) {
               return false;
            }
         } catch (Throwable var36) {
            var10000 = var36;
            var10001 = false;
            break label313;
         }

         int var4;
         try {
            var4 = this.mPendingActions.size();
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label313;
         }

         for(int var3 = 0; var3 < var4; ++var3) {
            try {
               var5 |= ((FragmentManager.OpGenerator)this.mPendingActions.get(var3)).generateOps(var1, var2);
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label313;
            }
         }

         label291:
         try {
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            return var5;
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label291;
         }
      }

      while(true) {
         Throwable var37 = var10000;

         try {
            throw var37;
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            continue;
         }
      }
   }

   private FragmentManagerViewModel getChildNonConfig(Fragment var1) {
      return this.mNonConfig.getChildNonConfig(var1);
   }

   private ViewGroup getFragmentContainer(Fragment var1) {
      if (var1.mContainerId <= 0) {
         return null;
      } else {
         if (this.mContainer.onHasView()) {
            View var2 = this.mContainer.onFindViewById(var1.mContainerId);
            if (var2 instanceof ViewGroup) {
               return (ViewGroup)var2;
            }
         }

         return null;
      }
   }

   static Fragment getViewFragment(View var0) {
      Object var1 = var0.getTag(id.fragment_container_view_tag);
      return var1 instanceof Fragment ? (Fragment)var1 : null;
   }

   static boolean isLoggingEnabled(int var0) {
      return DEBUG || Log.isLoggable("FragmentManager", var0);
   }

   private boolean isMenuAvailable(Fragment var1) {
      return var1.mHasMenu && var1.mMenuVisible || var1.mChildFragmentManager.checkForMenus();
   }

   private void makeInactive(FragmentStateManager var1) {
      Fragment var2 = var1.getFragment();
      if (this.mFragmentStore.containsActiveFragment(var2.mWho)) {
         if (isLoggingEnabled(2)) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Removed fragment from active set ");
            var3.append(var2);
            Log.v("FragmentManager", var3.toString());
         }

         this.mFragmentStore.makeInactive(var1);
         this.removeRetainedFragment(var2);
      }
   }

   private void makeRemovedFragmentsInvisible(ArraySet var1) {
      int var3 = var1.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         Fragment var4 = (Fragment)var1.valueAt(var2);
         if (!var4.mAdded) {
            View var5 = var4.requireView();
            var4.mPostponedAlpha = var5.getAlpha();
            var5.setAlpha(0.0F);
         }
      }

   }

   private boolean popBackStackImmediate(String var1, int var2, int var3) {
      this.execPendingActions(false);
      this.ensureExecReady(true);
      Fragment var5 = this.mPrimaryNav;
      if (var5 != null && var2 < 0 && var1 == null && var5.getChildFragmentManager().popBackStackImmediate()) {
         return true;
      } else {
         boolean var4 = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, var1, var2, var3);
         if (var4) {
            this.mExecutingActions = true;

            try {
               this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
               this.cleanupExec();
            }
         }

         this.updateOnBackPressedCallbackEnabled();
         this.doPendingDeferredStart();
         this.mFragmentStore.burpActive();
         return var4;
      }
   }

   private int postponePostponableTransactions(ArrayList var1, ArrayList var2, int var3, int var4, ArraySet var5) {
      int var7 = var4;

      int var8;
      for(int var6 = var4 - 1; var6 >= var3; var7 = var8) {
         BackStackRecord var11 = (BackStackRecord)var1.get(var6);
         boolean var10 = (Boolean)var2.get(var6);
         boolean var9;
         if (var11.isPostponed() && !var11.interactsWith(var1, var6 + 1, var4)) {
            var9 = true;
         } else {
            var9 = false;
         }

         var8 = var7;
         if (var9) {
            if (this.mPostponedTransactions == null) {
               this.mPostponedTransactions = new ArrayList();
            }

            FragmentManager.StartEnterTransitionListener var12 = new FragmentManager.StartEnterTransitionListener(var11, var10);
            this.mPostponedTransactions.add(var12);
            var11.setOnStartPostponedListener(var12);
            if (var10) {
               var11.executeOps();
            } else {
               var11.executePopOps(false);
            }

            var8 = var7 - 1;
            if (var6 != var8) {
               var1.remove(var6);
               var1.add(var8, var11);
            }

            this.addAddedFragments(var5);
         }

         --var6;
      }

      return var7;
   }

   private void removeRedundantOperationsAndExecute(ArrayList var1, ArrayList var2) {
      if (!var1.isEmpty()) {
         if (var1.size() != var2.size()) {
            throw new IllegalStateException("Internal error with the back stack records");
         } else {
            this.executePostponedTransaction(var1, var2);
            int var7 = var1.size();
            int var4 = 0;

            int var5;
            for(int var3 = 0; var3 < var7; var4 = var5) {
               var5 = var4;
               int var6 = var3;
               if (!((BackStackRecord)var1.get(var3)).mReorderingAllowed) {
                  if (var4 != var3) {
                     this.executeOpsTogether(var1, var2, var4, var3);
                  }

                  var5 = var3 + 1;
                  var4 = var5;
                  if ((Boolean)var2.get(var3)) {
                     while(true) {
                        var4 = var5;
                        if (var5 >= var7) {
                           break;
                        }

                        var4 = var5;
                        if (!(Boolean)var2.get(var5)) {
                           break;
                        }

                        var4 = var5;
                        if (((BackStackRecord)var1.get(var5)).mReorderingAllowed) {
                           break;
                        }

                        ++var5;
                     }
                  }

                  this.executeOpsTogether(var1, var2, var3, var4);
                  var5 = var4;
                  var6 = var4 - 1;
               }

               var3 = var6 + 1;
            }

            if (var4 != var7) {
               this.executeOpsTogether(var1, var2, var4, var7);
            }

         }
      }
   }

   private void reportBackStackChanged() {
      if (this.mBackStackChangeListeners != null) {
         for(int var1 = 0; var1 < this.mBackStackChangeListeners.size(); ++var1) {
            ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(var1)).onBackStackChanged();
         }
      }

   }

   static int reverseTransit(int var0) {
      if (var0 != 4097) {
         if (var0 != 4099) {
            return var0 != 8194 ? 0 : 4097;
         } else {
            return 4099;
         }
      } else {
         return 8194;
      }
   }

   private void setVisibleRemovingFragment(Fragment var1) {
      ViewGroup var2 = this.getFragmentContainer(var1);
      if (var2 != null) {
         if (var2.getTag(id.visible_removing_fragment_view_tag) == null) {
            var2.setTag(id.visible_removing_fragment_view_tag, var1);
         }

         ((Fragment)var2.getTag(id.visible_removing_fragment_view_tag)).setNextAnim(var1.getNextAnim());
      }

   }

   private void startPendingDeferredFragments() {
      Iterator var1 = this.mFragmentStore.getActiveFragments().iterator();

      while(var1.hasNext()) {
         Fragment var2 = (Fragment)var1.next();
         if (var2 != null) {
            this.performPendingDeferredStart(var2);
         }
      }

   }

   private void throwException(RuntimeException var1) {
      Log.e("FragmentManager", var1.getMessage());
      Log.e("FragmentManager", "Activity state:");
      PrintWriter var2 = new PrintWriter(new LogWriter("FragmentManager"));
      FragmentHostCallback var3 = this.mHost;
      if (var3 != null) {
         try {
            var3.onDump("  ", (FileDescriptor)null, var2, new String[0]);
         } catch (Exception var5) {
            Log.e("FragmentManager", "Failed dumping state", var5);
         }
      } else {
         try {
            this.dump("  ", (FileDescriptor)null, var2, new String[0]);
         } catch (Exception var4) {
            Log.e("FragmentManager", "Failed dumping state", var4);
         }
      }

      throw var1;
   }

   private void updateOnBackPressedCallbackEnabled() {
      ArrayList var3 = this.mPendingActions;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label218: {
         boolean var2;
         try {
            var2 = this.mPendingActions.isEmpty();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label218;
         }

         boolean var1 = true;
         if (!var2) {
            label211:
            try {
               this.mOnBackPressedCallback.setEnabled(true);
               return;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label211;
            }
         } else {
            label223: {
               try {
                  ;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label223;
               }

               OnBackPressedCallback var25 = this.mOnBackPressedCallback;
               if (this.getBackStackEntryCount() <= 0 || !this.isPrimaryNavigation(this.mParent)) {
                  var1 = false;
               }

               var25.setEnabled(var1);
               return;
            }
         }
      }

      while(true) {
         Throwable var4 = var10000;

         try {
            throw var4;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   void addBackStackState(BackStackRecord var1) {
      if (this.mBackStack == null) {
         this.mBackStack = new ArrayList();
      }

      this.mBackStack.add(var1);
   }

   void addCancellationSignal(Fragment var1, CancellationSignal var2) {
      if (this.mExitAnimationCancellationSignals.get(var1) == null) {
         this.mExitAnimationCancellationSignals.put(var1, new HashSet());
      }

      ((HashSet)this.mExitAnimationCancellationSignals.get(var1)).add(var2);
   }

   void addFragment(Fragment var1) {
      if (isLoggingEnabled(2)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("add: ");
         var2.append(var1);
         Log.v("FragmentManager", var2.toString());
      }

      this.makeActive(var1);
      if (!var1.mDetached) {
         this.mFragmentStore.addFragment(var1);
         var1.mRemoving = false;
         if (var1.mView == null) {
            var1.mHiddenChanged = false;
         }

         if (this.isMenuAvailable(var1)) {
            this.mNeedMenuInvalidate = true;
         }
      }

   }

   public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1) {
      if (this.mBackStackChangeListeners == null) {
         this.mBackStackChangeListeners = new ArrayList();
      }

      this.mBackStackChangeListeners.add(var1);
   }

   void addRetainedFragment(Fragment var1) {
      if (this.isStateSaved()) {
         if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "Ignoring addRetainedFragment as the state is already saved");
         }

      } else {
         if (this.mNonConfig.addRetainedFragment(var1) && isLoggingEnabled(2)) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Updating retained Fragments: Added ");
            var2.append(var1);
            Log.v("FragmentManager", var2.toString());
         }

      }
   }

   int allocBackStackIndex() {
      return this.mBackStackIndex.getAndIncrement();
   }

   void attachController(FragmentHostCallback var1, FragmentContainer var2, Fragment var3) {
      if (this.mHost == null) {
         this.mHost = var1;
         this.mContainer = var2;
         this.mParent = var3;
         if (var3 != null) {
            this.updateOnBackPressedCallbackEnabled();
         }

         if (var1 instanceof OnBackPressedDispatcherOwner) {
            Object var4 = (OnBackPressedDispatcherOwner)var1;
            this.mOnBackPressedDispatcher = ((OnBackPressedDispatcherOwner)var4).getOnBackPressedDispatcher();
            if (var3 != null) {
               var4 = var3;
            }

            this.mOnBackPressedDispatcher.addCallback((LifecycleOwner)var4, this.mOnBackPressedCallback);
         }

         if (var3 != null) {
            this.mNonConfig = var3.mFragmentManager.getChildNonConfig(var3);
         } else if (var1 instanceof ViewModelStoreOwner) {
            this.mNonConfig = FragmentManagerViewModel.getInstance(((ViewModelStoreOwner)var1).getViewModelStore());
         } else {
            this.mNonConfig = new FragmentManagerViewModel(false);
         }
      } else {
         throw new IllegalStateException("Already attached");
      }
   }

   void attachFragment(Fragment var1) {
      StringBuilder var2;
      if (isLoggingEnabled(2)) {
         var2 = new StringBuilder();
         var2.append("attach: ");
         var2.append(var1);
         Log.v("FragmentManager", var2.toString());
      }

      if (var1.mDetached) {
         var1.mDetached = false;
         if (!var1.mAdded) {
            this.mFragmentStore.addFragment(var1);
            if (isLoggingEnabled(2)) {
               var2 = new StringBuilder();
               var2.append("add from attach: ");
               var2.append(var1);
               Log.v("FragmentManager", var2.toString());
            }

            if (this.isMenuAvailable(var1)) {
               this.mNeedMenuInvalidate = true;
            }
         }
      }

   }

   public FragmentTransaction beginTransaction() {
      return new BackStackRecord(this);
   }

   boolean checkForMenus() {
      boolean var1 = false;
      Iterator var2 = this.mFragmentStore.getActiveFragments().iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }

         Fragment var3 = (Fragment)var2.next();
         if (var3 != null) {
            var1 = this.isMenuAvailable(var3);
         }
      } while(!var1);

      return true;
   }

   void completeExecute(BackStackRecord var1, boolean var2, boolean var3, boolean var4) {
      if (var2) {
         var1.executePopOps(var4);
      } else {
         var1.executeOps();
      }

      ArrayList var5 = new ArrayList(1);
      ArrayList var6 = new ArrayList(1);
      var5.add(var1);
      var6.add(var2);
      if (var3) {
         FragmentTransition.startTransitions(this, var5, var6, 0, 1, true, this.mFragmentTransitionCallback);
      }

      if (var4) {
         this.moveToState(this.mCurState, true);
      }

      Iterator var7 = this.mFragmentStore.getActiveFragments().iterator();

      while(var7.hasNext()) {
         Fragment var8 = (Fragment)var7.next();
         if (var8 != null && var8.mView != null && var8.mIsNewlyAdded && var1.interactsWith(var8.mContainerId)) {
            if (var8.mPostponedAlpha > 0.0F) {
               var8.mView.setAlpha(var8.mPostponedAlpha);
            }

            if (var4) {
               var8.mPostponedAlpha = 0.0F;
            } else {
               var8.mPostponedAlpha = -1.0F;
               var8.mIsNewlyAdded = false;
            }
         }
      }

   }

   void detachFragment(Fragment var1) {
      StringBuilder var2;
      if (isLoggingEnabled(2)) {
         var2 = new StringBuilder();
         var2.append("detach: ");
         var2.append(var1);
         Log.v("FragmentManager", var2.toString());
      }

      if (!var1.mDetached) {
         var1.mDetached = true;
         if (var1.mAdded) {
            if (isLoggingEnabled(2)) {
               var2 = new StringBuilder();
               var2.append("remove from detach: ");
               var2.append(var1);
               Log.v("FragmentManager", var2.toString());
            }

            this.mFragmentStore.removeFragment(var1);
            if (this.isMenuAvailable(var1)) {
               this.mNeedMenuInvalidate = true;
            }

            this.setVisibleRemovingFragment(var1);
         }
      }

   }

   void dispatchActivityCreated() {
      this.mStateSaved = false;
      this.mStopped = false;
      this.dispatchStateChange(2);
   }

   void dispatchConfigurationChanged(Configuration var1) {
      Iterator var2 = this.mFragmentStore.getFragments().iterator();

      while(var2.hasNext()) {
         Fragment var3 = (Fragment)var2.next();
         if (var3 != null) {
            var3.performConfigurationChanged(var1);
         }
      }

   }

   boolean dispatchContextItemSelected(MenuItem var1) {
      if (this.mCurState < 1) {
         return false;
      } else {
         Iterator var2 = this.mFragmentStore.getFragments().iterator();

         Fragment var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (Fragment)var2.next();
         } while(var3 == null || !var3.performContextItemSelected(var1));

         return true;
      }
   }

   void dispatchCreate() {
      this.mStateSaved = false;
      this.mStopped = false;
      this.dispatchStateChange(1);
   }

   boolean dispatchCreateOptionsMenu(Menu var1, MenuInflater var2) {
      if (this.mCurState < 1) {
         return false;
      } else {
         boolean var4 = false;
         ArrayList var6 = null;

         ArrayList var7;
         for(Iterator var8 = this.mFragmentStore.getFragments().iterator(); var8.hasNext(); var6 = var7) {
            Fragment var9 = (Fragment)var8.next();
            boolean var5 = var4;
            var7 = var6;
            if (var9 != null) {
               var5 = var4;
               var7 = var6;
               if (var9.performCreateOptionsMenu(var1, var2)) {
                  var5 = true;
                  var7 = var6;
                  if (var6 == null) {
                     var7 = new ArrayList();
                  }

                  var7.add(var9);
               }
            }

            var4 = var5;
         }

         if (this.mCreatedMenus != null) {
            for(int var3 = 0; var3 < this.mCreatedMenus.size(); ++var3) {
               Fragment var10 = (Fragment)this.mCreatedMenus.get(var3);
               if (var6 == null || !var6.contains(var10)) {
                  var10.onDestroyOptionsMenu();
               }
            }
         }

         this.mCreatedMenus = var6;
         return var4;
      }
   }

   void dispatchDestroy() {
      this.mDestroyed = true;
      this.execPendingActions(true);
      this.endAnimatingAwayFragments();
      this.dispatchStateChange(-1);
      this.mHost = null;
      this.mContainer = null;
      this.mParent = null;
      if (this.mOnBackPressedDispatcher != null) {
         this.mOnBackPressedCallback.remove();
         this.mOnBackPressedDispatcher = null;
      }

   }

   void dispatchDestroyView() {
      this.dispatchStateChange(1);
   }

   void dispatchLowMemory() {
      Iterator var1 = this.mFragmentStore.getFragments().iterator();

      while(var1.hasNext()) {
         Fragment var2 = (Fragment)var1.next();
         if (var2 != null) {
            var2.performLowMemory();
         }
      }

   }

   void dispatchMultiWindowModeChanged(boolean var1) {
      Iterator var2 = this.mFragmentStore.getFragments().iterator();

      while(var2.hasNext()) {
         Fragment var3 = (Fragment)var2.next();
         if (var3 != null) {
            var3.performMultiWindowModeChanged(var1);
         }
      }

   }

   boolean dispatchOptionsItemSelected(MenuItem var1) {
      if (this.mCurState < 1) {
         return false;
      } else {
         Iterator var2 = this.mFragmentStore.getFragments().iterator();

         Fragment var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (Fragment)var2.next();
         } while(var3 == null || !var3.performOptionsItemSelected(var1));

         return true;
      }
   }

   void dispatchOptionsMenuClosed(Menu var1) {
      if (this.mCurState >= 1) {
         Iterator var2 = this.mFragmentStore.getFragments().iterator();

         while(var2.hasNext()) {
            Fragment var3 = (Fragment)var2.next();
            if (var3 != null) {
               var3.performOptionsMenuClosed(var1);
            }
         }

      }
   }

   void dispatchPause() {
      this.dispatchStateChange(3);
   }

   void dispatchPictureInPictureModeChanged(boolean var1) {
      Iterator var2 = this.mFragmentStore.getFragments().iterator();

      while(var2.hasNext()) {
         Fragment var3 = (Fragment)var2.next();
         if (var3 != null) {
            var3.performPictureInPictureModeChanged(var1);
         }
      }

   }

   boolean dispatchPrepareOptionsMenu(Menu var1) {
      if (this.mCurState < 1) {
         return false;
      } else {
         boolean var2 = false;

         boolean var3;
         for(Iterator var4 = this.mFragmentStore.getFragments().iterator(); var4.hasNext(); var2 = var3) {
            Fragment var5 = (Fragment)var4.next();
            var3 = var2;
            if (var5 != null) {
               var3 = var2;
               if (var5.performPrepareOptionsMenu(var1)) {
                  var3 = true;
               }
            }
         }

         return var2;
      }
   }

   void dispatchPrimaryNavigationFragmentChanged() {
      this.updateOnBackPressedCallbackEnabled();
      this.dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
   }

   void dispatchResume() {
      this.mStateSaved = false;
      this.mStopped = false;
      this.dispatchStateChange(4);
   }

   void dispatchStart() {
      this.mStateSaved = false;
      this.mStopped = false;
      this.dispatchStateChange(3);
   }

   void dispatchStop() {
      this.mStopped = true;
      this.dispatchStateChange(2);
   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      StringBuilder var7 = new StringBuilder();
      var7.append(var1);
      var7.append("    ");
      String var44 = var7.toString();
      this.mFragmentStore.dump(var1, var2, var3, var4);
      ArrayList var39 = this.mCreatedMenus;
      int var5;
      int var6;
      if (var39 != null) {
         var6 = var39.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Fragments Created Menus:");

            for(var5 = 0; var5 < var6; ++var5) {
               Fragment var40 = (Fragment)this.mCreatedMenus.get(var5);
               var3.print(var1);
               var3.print("  #");
               var3.print(var5);
               var3.print(": ");
               var3.println(var40.toString());
            }
         }
      }

      var39 = this.mBackStack;
      if (var39 != null) {
         var6 = var39.size();
         if (var6 > 0) {
            var3.print(var1);
            var3.println("Back Stack:");

            for(var5 = 0; var5 < var6; ++var5) {
               BackStackRecord var41 = (BackStackRecord)this.mBackStack.get(var5);
               var3.print(var1);
               var3.print("  #");
               var3.print(var5);
               var3.print(": ");
               var3.println(var41.toString());
               var41.dump(var44, var3);
            }
         }
      }

      var3.print(var1);
      StringBuilder var42 = new StringBuilder();
      var42.append("Back Stack Index: ");
      var42.append(this.mBackStackIndex.get());
      var3.println(var42.toString());
      var39 = this.mPendingActions;
      synchronized(var39){}

      label519: {
         Throwable var10000;
         boolean var10001;
         label520: {
            try {
               var6 = this.mPendingActions.size();
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label520;
            }

            if (var6 > 0) {
               try {
                  var3.print(var1);
                  var3.println("Pending Actions:");
               } catch (Throwable var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label520;
               }

               for(var5 = 0; var5 < var6; ++var5) {
                  try {
                     FragmentManager.OpGenerator var43 = (FragmentManager.OpGenerator)this.mPendingActions.get(var5);
                     var3.print(var1);
                     var3.print("  #");
                     var3.print(var5);
                     var3.print(": ");
                     var3.println(var43);
                  } catch (Throwable var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label520;
                  }
               }
            }

            label483:
            try {
               break label519;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label483;
            }
         }

         while(true) {
            Throwable var38 = var10000;

            try {
               throw var38;
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               continue;
            }
         }
      }

      var3.print(var1);
      var3.println("FragmentManager misc state:");
      var3.print(var1);
      var3.print("  mHost=");
      var3.println(this.mHost);
      var3.print(var1);
      var3.print("  mContainer=");
      var3.println(this.mContainer);
      if (this.mParent != null) {
         var3.print(var1);
         var3.print("  mParent=");
         var3.println(this.mParent);
      }

      var3.print(var1);
      var3.print("  mCurState=");
      var3.print(this.mCurState);
      var3.print(" mStateSaved=");
      var3.print(this.mStateSaved);
      var3.print(" mStopped=");
      var3.print(this.mStopped);
      var3.print(" mDestroyed=");
      var3.println(this.mDestroyed);
      if (this.mNeedMenuInvalidate) {
         var3.print(var1);
         var3.print("  mNeedMenuInvalidate=");
         var3.println(this.mNeedMenuInvalidate);
      }

   }

   void enqueueAction(FragmentManager.OpGenerator var1, boolean var2) {
      if (!var2) {
         if (this.mHost == null) {
            if (this.mDestroyed) {
               throw new IllegalStateException("FragmentManager has been destroyed");
            }

            throw new IllegalStateException("FragmentManager has not been attached to a host.");
         }

         this.checkStateLoss();
      }

      ArrayList var3 = this.mPendingActions;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label309: {
         label308: {
            try {
               if (this.mHost == null) {
                  break label308;
               }
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label309;
            }

            try {
               this.mPendingActions.add(var1);
               this.scheduleCommit();
               return;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label309;
            }
         }

         if (var2) {
            label298:
            try {
               return;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label298;
            }
         } else {
            label300:
            try {
               throw new IllegalStateException("Activity has been destroyed");
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label300;
            }
         }
      }

      while(true) {
         Throwable var34 = var10000;

         try {
            throw var34;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            continue;
         }
      }
   }

   boolean execPendingActions(boolean var1) {
      this.ensureExecReady(var1);

      for(var1 = false; this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop); var1 = true) {
         this.mExecutingActions = true;

         try {
            this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
         } finally {
            this.cleanupExec();
         }
      }

      this.updateOnBackPressedCallbackEnabled();
      this.doPendingDeferredStart();
      this.mFragmentStore.burpActive();
      return var1;
   }

   void execSingleAction(FragmentManager.OpGenerator var1, boolean var2) {
      if (!var2 || this.mHost != null && !this.mDestroyed) {
         this.ensureExecReady(var2);
         if (var1.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;

            try {
               this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
               this.cleanupExec();
            }
         }

         this.updateOnBackPressedCallbackEnabled();
         this.doPendingDeferredStart();
         this.mFragmentStore.burpActive();
      }
   }

   public boolean executePendingTransactions() {
      boolean var1 = this.execPendingActions(true);
      this.forcePostponedTransactions();
      return var1;
   }

   Fragment findActiveFragment(String var1) {
      return this.mFragmentStore.findActiveFragment(var1);
   }

   public Fragment findFragmentById(int var1) {
      return this.mFragmentStore.findFragmentById(var1);
   }

   public Fragment findFragmentByTag(String var1) {
      return this.mFragmentStore.findFragmentByTag(var1);
   }

   Fragment findFragmentByWho(String var1) {
      return this.mFragmentStore.findFragmentByWho(var1);
   }

   int getActiveFragmentCount() {
      return this.mFragmentStore.getActiveFragmentCount();
   }

   List getActiveFragments() {
      return this.mFragmentStore.getActiveFragments();
   }

   public FragmentManager.BackStackEntry getBackStackEntryAt(int var1) {
      return (FragmentManager.BackStackEntry)this.mBackStack.get(var1);
   }

   public int getBackStackEntryCount() {
      ArrayList var1 = this.mBackStack;
      return var1 != null ? var1.size() : 0;
   }

   public Fragment getFragment(Bundle var1, String var2) {
      String var5 = var1.getString(var2);
      if (var5 == null) {
         return null;
      } else {
         Fragment var3 = this.findActiveFragment(var5);
         if (var3 == null) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Fragment no longer exists for key ");
            var4.append(var2);
            var4.append(": unique id ");
            var4.append(var5);
            this.throwException(new IllegalStateException(var4.toString()));
         }

         return var3;
      }
   }

   public FragmentFactory getFragmentFactory() {
      FragmentFactory var1 = this.mFragmentFactory;
      if (var1 != null) {
         return var1;
      } else {
         Fragment var2 = this.mParent;
         return var2 != null ? var2.mFragmentManager.getFragmentFactory() : this.mHostFragmentFactory;
      }
   }

   public List getFragments() {
      return this.mFragmentStore.getFragments();
   }

   Factory2 getLayoutInflaterFactory() {
      return this.mLayoutInflaterFactory;
   }

   FragmentLifecycleCallbacksDispatcher getLifecycleCallbacksDispatcher() {
      return this.mLifecycleCallbacksDispatcher;
   }

   Fragment getParent() {
      return this.mParent;
   }

   public Fragment getPrimaryNavigationFragment() {
      return this.mPrimaryNav;
   }

   ViewModelStore getViewModelStore(Fragment var1) {
      return this.mNonConfig.getViewModelStore(var1);
   }

   void handleOnBackPressed() {
      this.execPendingActions(true);
      if (this.mOnBackPressedCallback.isEnabled()) {
         this.popBackStackImmediate();
      } else {
         this.mOnBackPressedDispatcher.onBackPressed();
      }
   }

   void hideFragment(Fragment var1) {
      if (isLoggingEnabled(2)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("hide: ");
         var2.append(var1);
         Log.v("FragmentManager", var2.toString());
      }

      if (!var1.mHidden) {
         var1.mHidden = true;
         var1.mHiddenChanged ^= true;
         this.setVisibleRemovingFragment(var1);
      }

   }

   public boolean isDestroyed() {
      return this.mDestroyed;
   }

   boolean isPrimaryNavigation(Fragment var1) {
      if (var1 == null) {
         return true;
      } else {
         FragmentManager var2 = var1.mFragmentManager;
         return var1.equals(var2.getPrimaryNavigationFragment()) && this.isPrimaryNavigation(var2.mParent);
      }
   }

   boolean isStateAtLeast(int var1) {
      return this.mCurState >= var1;
   }

   public boolean isStateSaved() {
      return this.mStateSaved || this.mStopped;
   }

   void makeActive(Fragment var1) {
      if (!this.mFragmentStore.containsActiveFragment(var1.mWho)) {
         FragmentStateManager var2 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, var1);
         var2.restoreState(this.mHost.getContext().getClassLoader());
         this.mFragmentStore.makeActive(var2);
         if (var1.mRetainInstanceChangedWhileDetached) {
            if (var1.mRetainInstance) {
               this.addRetainedFragment(var1);
            } else {
               this.removeRetainedFragment(var1);
            }

            var1.mRetainInstanceChangedWhileDetached = false;
         }

         var2.setFragmentManagerState(this.mCurState);
         if (isLoggingEnabled(2)) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Added fragment to active set ");
            var3.append(var1);
            Log.v("FragmentManager", var3.toString());
         }

      }
   }

   void moveFragmentToExpectedState(Fragment var1) {
      if (!this.mFragmentStore.containsActiveFragment(var1.mWho)) {
         if (isLoggingEnabled(3)) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Ignoring moving ");
            var8.append(var1);
            var8.append(" to state ");
            var8.append(this.mCurState);
            var8.append("since it is not added to ");
            var8.append(this);
            Log.d("FragmentManager", var8.toString());
         }

      } else {
         this.moveToState(var1);
         if (var1.mView != null) {
            Fragment var4 = this.mFragmentStore.findFragmentUnder(var1);
            if (var4 != null) {
               View var6 = var4.mView;
               ViewGroup var5 = var1.mContainer;
               int var2 = var5.indexOfChild(var6);
               int var3 = var5.indexOfChild(var1.mView);
               if (var3 < var2) {
                  var5.removeViewAt(var3);
                  var5.addView(var1.mView, var2);
               }
            }

            if (var1.mIsNewlyAdded && var1.mContainer != null) {
               if (var1.mPostponedAlpha > 0.0F) {
                  var1.mView.setAlpha(var1.mPostponedAlpha);
               }

               var1.mPostponedAlpha = 0.0F;
               var1.mIsNewlyAdded = false;
               FragmentAnim.AnimationOrAnimator var7 = FragmentAnim.loadAnimation(this.mHost.getContext(), this.mContainer, var1, true);
               if (var7 != null) {
                  if (var7.animation != null) {
                     var1.mView.startAnimation(var7.animation);
                  } else {
                     var7.animator.setTarget(var1.mView);
                     var7.animator.start();
                  }
               }
            }
         }

         if (var1.mHiddenChanged) {
            this.completeShowHideFragment(var1);
         }

      }
   }

   void moveToState(int var1, boolean var2) {
      if (this.mHost == null && var1 != -1) {
         throw new IllegalStateException("No activity");
      } else if (var2 || var1 != this.mCurState) {
         this.mCurState = var1;
         Iterator var3 = this.mFragmentStore.getFragments().iterator();

         while(var3.hasNext()) {
            this.moveFragmentToExpectedState((Fragment)var3.next());
         }

         var3 = this.mFragmentStore.getActiveFragments().iterator();

         while(var3.hasNext()) {
            Fragment var4 = (Fragment)var3.next();
            if (var4 != null && !var4.mIsNewlyAdded) {
               this.moveFragmentToExpectedState(var4);
            }
         }

         this.startPendingDeferredFragments();
         if (this.mNeedMenuInvalidate) {
            FragmentHostCallback var5 = this.mHost;
            if (var5 != null && this.mCurState == 4) {
               var5.onSupportInvalidateOptionsMenu();
               this.mNeedMenuInvalidate = false;
            }
         }

      }
   }

   void moveToState(Fragment var1) {
      this.moveToState(var1, this.mCurState);
   }

   void moveToState(Fragment var1, int var2) {
      FragmentStateManager var7 = this.mFragmentStore.getFragmentStateManager(var1.mWho);
      boolean var5 = true;
      FragmentStateManager var6 = var7;
      if (var7 == null) {
         var6 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, var1);
         var6.setFragmentManagerState(1);
      }

      int var3 = Math.min(var2, var6.computeMaxState());
      int var4;
      StringBuilder var11;
      StringBuilder var12;
      Fragment var13;
      if (var1.mState <= var3) {
         if (var1.mState < var3 && !this.mExitAnimationCancellationSignals.isEmpty()) {
            this.cancelExitAnimation(var1);
         }

         label187: {
            label186: {
               label195: {
                  label184: {
                     var2 = var1.mState;
                     if (var2 != -1) {
                        if (var2 != 0) {
                           if (var2 != 1) {
                              if (var2 != 2) {
                                 if (var2 != 3) {
                                    break label187;
                                 }
                                 break label186;
                              }
                              break label195;
                           }
                           break label184;
                        }
                     } else if (var3 > -1) {
                        if (isLoggingEnabled(3)) {
                           var12 = new StringBuilder();
                           var12.append("moveto ATTACHED: ");
                           var12.append(var1);
                           Log.d("FragmentManager", var12.toString());
                        }

                        if (var1.mTarget != null) {
                           if (!var1.mTarget.equals(this.findActiveFragment(var1.mTarget.mWho))) {
                              var11 = new StringBuilder();
                              var11.append("Fragment ");
                              var11.append(var1);
                              var11.append(" declared target fragment ");
                              var11.append(var1.mTarget);
                              var11.append(" that does not belong to this FragmentManager!");
                              throw new IllegalStateException(var11.toString());
                           }

                           if (var1.mTarget.mState < 1) {
                              this.moveToState(var1.mTarget, 1);
                           }

                           var1.mTargetWho = var1.mTarget.mWho;
                           var1.mTarget = null;
                        }

                        if (var1.mTargetWho != null) {
                           var13 = this.findActiveFragment(var1.mTargetWho);
                           if (var13 == null) {
                              var11 = new StringBuilder();
                              var11.append("Fragment ");
                              var11.append(var1);
                              var11.append(" declared target fragment ");
                              var11.append(var1.mTargetWho);
                              var11.append(" that does not belong to this FragmentManager!");
                              throw new IllegalStateException(var11.toString());
                           }

                           if (var13.mState < 1) {
                              this.moveToState(var13, 1);
                           }
                        }

                        var6.attach(this.mHost, this, this.mParent);
                     }

                     if (var3 > 0) {
                        var6.create();
                     }
                  }

                  if (var3 > -1) {
                     var6.ensureInflatedView();
                  }

                  if (var3 > 1) {
                     var6.createView(this.mContainer);
                     var6.activityCreated();
                     var6.restoreViewState();
                  }
               }

               if (var3 > 2) {
                  var6.start();
               }
            }

            if (var3 > 3) {
               var6.resume();
            }
         }

         var4 = var3;
      } else {
         var4 = var3;
         if (var1.mState > var3) {
            label173: {
               var4 = var1.mState;
               var2 = var3;
               if (var4 != 0) {
                  if (var4 != 1) {
                     if (var4 != 2) {
                        if (var4 != 3) {
                           if (var4 != 4) {
                              var4 = var3;
                              break label173;
                           }

                           if (var3 < 4) {
                              var6.pause();
                           }
                        }

                        if (var3 < 3) {
                           var6.stop();
                        }
                     }

                     if (var3 < 2) {
                        if (isLoggingEnabled(3)) {
                           var12 = new StringBuilder();
                           var12.append("movefrom ACTIVITY_CREATED: ");
                           var12.append(var1);
                           Log.d("FragmentManager", var12.toString());
                        }

                        if (var1.mView != null && this.mHost.onShouldSaveFragmentState(var1) && var1.mSavedViewState == null) {
                           var6.saveViewState();
                        }

                        ViewGroup var8 = null;
                        if (var1.mView != null && var1.mContainer != null) {
                           var1.mContainer.endViewTransition(var1.mView);
                           var1.mView.clearAnimation();
                           if (!var1.isRemovingParent()) {
                              FragmentAnim.AnimationOrAnimator var14 = var8;
                              if (this.mCurState > -1) {
                                 var14 = var8;
                                 if (!this.mDestroyed) {
                                    var14 = var8;
                                    if (var1.mView.getVisibility() == 0) {
                                       var14 = var8;
                                       if (var1.mPostponedAlpha >= 0.0F) {
                                          var14 = FragmentAnim.loadAnimation(this.mHost.getContext(), this.mContainer, var1, false);
                                       }
                                    }
                                 }
                              }

                              var1.mPostponedAlpha = 0.0F;
                              var8 = var1.mContainer;
                              View var9 = var1.mView;
                              if (var14 != null) {
                                 var1.setStateAfterAnimating(var3);
                                 FragmentAnim.animateRemoveFragment(var1, var14, this.mFragmentTransitionCallback);
                              }

                              var8.removeView(var9);
                              if (var8 != var1.mContainer) {
                                 return;
                              }
                           }
                        }

                        if (this.mExitAnimationCancellationSignals.get(var1) == null) {
                           this.destroyFragmentView(var1);
                        } else {
                           var1.setStateAfterAnimating(var3);
                        }
                     }
                  }

                  var2 = var3;
                  if (var3 < 1) {
                     boolean var10;
                     if (var1.mRemoving && !var1.isInBackStack()) {
                        var10 = var5;
                     } else {
                        var10 = false;
                     }

                     if (!var10 && !this.mNonConfig.shouldDestroy(var1)) {
                        if (var1.mTargetWho != null) {
                           var13 = this.findActiveFragment(var1.mTargetWho);
                           if (var13 != null && var13.getRetainInstance()) {
                              var1.mTarget = var13;
                           }
                        }
                     } else {
                        this.makeInactive(var6);
                     }

                     if (this.mExitAnimationCancellationSignals.get(var1) != null) {
                        var1.setStateAfterAnimating(var3);
                        var2 = 1;
                     } else {
                        var6.destroy(this.mHost, this.mNonConfig);
                        var2 = var3;
                     }
                  }
               }

               var4 = var2;
               if (var2 < 0) {
                  var6.detach(this.mNonConfig);
                  var4 = var2;
               }
            }
         }
      }

      if (var1.mState != var4) {
         if (isLoggingEnabled(3)) {
            var11 = new StringBuilder();
            var11.append("moveToState: Fragment state for ");
            var11.append(var1);
            var11.append(" not updated inline; expected state ");
            var11.append(var4);
            var11.append(" found ");
            var11.append(var1.mState);
            Log.d("FragmentManager", var11.toString());
         }

         var1.mState = var4;
      }

   }

   void noteStateNotSaved() {
      this.mStateSaved = false;
      this.mStopped = false;
      Iterator var1 = this.mFragmentStore.getFragments().iterator();

      while(var1.hasNext()) {
         Fragment var2 = (Fragment)var1.next();
         if (var2 != null) {
            var2.noteStateNotSaved();
         }
      }

   }

   @Deprecated
   public FragmentTransaction openTransaction() {
      return this.beginTransaction();
   }

   void performPendingDeferredStart(Fragment var1) {
      if (var1.mDeferStart) {
         if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
         }

         var1.mDeferStart = false;
         this.moveToState(var1, this.mCurState);
      }

   }

   public void popBackStack() {
      this.enqueueAction(new FragmentManager.PopBackStackState((String)null, -1, 0), false);
   }

   public void popBackStack(int var1, int var2) {
      if (var1 >= 0) {
         this.enqueueAction(new FragmentManager.PopBackStackState((String)null, var1, var2), false);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Bad id: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void popBackStack(String var1, int var2) {
      this.enqueueAction(new FragmentManager.PopBackStackState(var1, -1, var2), false);
   }

   public boolean popBackStackImmediate() {
      return this.popBackStackImmediate((String)null, -1, 0);
   }

   public boolean popBackStackImmediate(int var1, int var2) {
      if (var1 >= 0) {
         return this.popBackStackImmediate((String)null, var1, var2);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Bad id: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public boolean popBackStackImmediate(String var1, int var2) {
      return this.popBackStackImmediate(var1, -1, var2);
   }

   boolean popBackStackState(ArrayList var1, ArrayList var2, String var3, int var4, int var5) {
      ArrayList var8 = this.mBackStack;
      if (var8 == null) {
         return false;
      } else if (var3 == null && var4 < 0 && (var5 & 1) == 0) {
         var4 = var8.size() - 1;
         if (var4 < 0) {
            return false;
         } else {
            var1.add(this.mBackStack.remove(var4));
            var2.add(true);
            return true;
         }
      } else {
         int var6 = -1;
         if (var3 != null || var4 >= 0) {
            int var7;
            BackStackRecord var9;
            for(var7 = this.mBackStack.size() - 1; var7 >= 0; --var7) {
               var9 = (BackStackRecord)this.mBackStack.get(var7);
               if (var3 != null && var3.equals(var9.getName()) || var4 >= 0 && var4 == var9.mIndex) {
                  break;
               }
            }

            if (var7 < 0) {
               return false;
            }

            var6 = var7;
            if ((var5 & 1) != 0) {
               var5 = var7 - 1;

               while(true) {
                  var6 = var5;
                  if (var5 < 0) {
                     break;
                  }

                  var9 = (BackStackRecord)this.mBackStack.get(var5);
                  if (var3 == null || !var3.equals(var9.getName())) {
                     var6 = var5;
                     if (var4 < 0) {
                        break;
                     }

                     var6 = var5;
                     if (var4 != var9.mIndex) {
                        break;
                     }
                  }

                  --var5;
               }
            }
         }

         if (var6 == this.mBackStack.size() - 1) {
            return false;
         } else {
            for(var4 = this.mBackStack.size() - 1; var4 > var6; --var4) {
               var1.add(this.mBackStack.remove(var4));
               var2.add(true);
            }

            return true;
         }
      }
   }

   public void putFragment(Bundle var1, String var2, Fragment var3) {
      if (var3.mFragmentManager != this) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Fragment ");
         var4.append(var3);
         var4.append(" is not currently in the FragmentManager");
         this.throwException(new IllegalStateException(var4.toString()));
      }

      var1.putString(var2, var3.mWho);
   }

   public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1, boolean var2) {
      this.mLifecycleCallbacksDispatcher.registerFragmentLifecycleCallbacks(var1, var2);
   }

   void removeCancellationSignal(Fragment var1, CancellationSignal var2) {
      HashSet var3 = (HashSet)this.mExitAnimationCancellationSignals.get(var1);
      if (var3 != null && var3.remove(var2) && var3.isEmpty()) {
         this.mExitAnimationCancellationSignals.remove(var1);
         if (var1.mState < 3) {
            this.destroyFragmentView(var1);
            this.moveToState(var1, var1.getStateAfterAnimating());
         }
      }

   }

   void removeFragment(Fragment var1) {
      if (isLoggingEnabled(2)) {
         StringBuilder var3 = new StringBuilder();
         var3.append("remove: ");
         var3.append(var1);
         var3.append(" nesting=");
         var3.append(var1.mBackStackNesting);
         Log.v("FragmentManager", var3.toString());
      }

      boolean var2 = var1.isInBackStack();
      if (!var1.mDetached || var2 ^ true) {
         this.mFragmentStore.removeFragment(var1);
         if (this.isMenuAvailable(var1)) {
            this.mNeedMenuInvalidate = true;
         }

         var1.mRemoving = true;
         this.setVisibleRemovingFragment(var1);
      }

   }

   public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1) {
      ArrayList var2 = this.mBackStackChangeListeners;
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   void removeRetainedFragment(Fragment var1) {
      if (this.isStateSaved()) {
         if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "Ignoring removeRetainedFragment as the state is already saved");
         }

      } else {
         if (this.mNonConfig.removeRetainedFragment(var1) && isLoggingEnabled(2)) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Updating retained Fragments: Removed ");
            var2.append(var1);
            Log.v("FragmentManager", var2.toString());
         }

      }
   }

   void restoreAllState(Parcelable var1, FragmentManagerNonConfig var2) {
      if (this.mHost instanceof ViewModelStoreOwner) {
         this.throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner"));
      }

      this.mNonConfig.restoreFromSnapshot(var2);
      this.restoreSaveState(var1);
   }

   void restoreSaveState(Parcelable var1) {
      if (var1 != null) {
         FragmentManagerState var3 = (FragmentManagerState)var1;
         if (var3.mActive != null) {
            this.mFragmentStore.resetActiveFragments();
            Iterator var4 = var3.mActive.iterator();

            while(var4.hasNext()) {
               FragmentState var7 = (FragmentState)var4.next();
               if (var7 != null) {
                  Fragment var5 = this.mNonConfig.findRetainedFragmentByWho(var7.mWho);
                  StringBuilder var6;
                  FragmentStateManager var8;
                  if (var5 != null) {
                     if (isLoggingEnabled(2)) {
                        var6 = new StringBuilder();
                        var6.append("restoreSaveState: re-attaching retained ");
                        var6.append(var5);
                        Log.v("FragmentManager", var6.toString());
                     }

                     var8 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, var5, var7);
                  } else {
                     var8 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mHost.getContext().getClassLoader(), this.getFragmentFactory(), var7);
                  }

                  var5 = var8.getFragment();
                  var5.mFragmentManager = this;
                  if (isLoggingEnabled(2)) {
                     var6 = new StringBuilder();
                     var6.append("restoreSaveState: active (");
                     var6.append(var5.mWho);
                     var6.append("): ");
                     var6.append(var5);
                     Log.v("FragmentManager", var6.toString());
                  }

                  var8.restoreState(this.mHost.getContext().getClassLoader());
                  this.mFragmentStore.makeActive(var8);
                  var8.setFragmentManagerState(this.mCurState);
               }
            }

            Iterator var9 = this.mNonConfig.getRetainedFragments().iterator();

            while(var9.hasNext()) {
               Fragment var12 = (Fragment)var9.next();
               if (!this.mFragmentStore.containsActiveFragment(var12.mWho)) {
                  if (isLoggingEnabled(2)) {
                     StringBuilder var15 = new StringBuilder();
                     var15.append("Discarding retained Fragment ");
                     var15.append(var12);
                     var15.append(" that was not found in the set of active Fragments ");
                     var15.append(var3.mActive);
                     Log.v("FragmentManager", var15.toString());
                  }

                  this.moveToState(var12, 1);
                  var12.mRemoving = true;
                  this.moveToState(var12, -1);
               }
            }

            this.mFragmentStore.restoreAddedFragments(var3.mAdded);
            if (var3.mBackStack != null) {
               this.mBackStack = new ArrayList(var3.mBackStack.length);

               for(int var2 = 0; var2 < var3.mBackStack.length; ++var2) {
                  BackStackRecord var10 = var3.mBackStack[var2].instantiate(this);
                  if (isLoggingEnabled(2)) {
                     StringBuilder var13 = new StringBuilder();
                     var13.append("restoreAllState: back stack #");
                     var13.append(var2);
                     var13.append(" (index ");
                     var13.append(var10.mIndex);
                     var13.append("): ");
                     var13.append(var10);
                     Log.v("FragmentManager", var13.toString());
                     PrintWriter var14 = new PrintWriter(new LogWriter("FragmentManager"));
                     var10.dump("  ", var14, false);
                     var14.close();
                  }

                  this.mBackStack.add(var10);
               }
            } else {
               this.mBackStack = null;
            }

            this.mBackStackIndex.set(var3.mBackStackIndex);
            if (var3.mPrimaryNavActiveWho != null) {
               Fragment var11 = this.findActiveFragment(var3.mPrimaryNavActiveWho);
               this.mPrimaryNav = var11;
               this.dispatchParentPrimaryNavigationFragmentChanged(var11);
            }

         }
      }
   }

   @Deprecated
   FragmentManagerNonConfig retainNonConfig() {
      if (this.mHost instanceof ViewModelStoreOwner) {
         this.throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner."));
      }

      return this.mNonConfig.getSnapshot();
   }

   Parcelable saveAllState() {
      this.forcePostponedTransactions();
      this.endAnimatingAwayFragments();
      this.execPendingActions(true);
      this.mStateSaved = true;
      ArrayList var5 = this.mFragmentStore.saveActiveFragments();
      if (var5.isEmpty()) {
         if (isLoggingEnabled(2)) {
            Log.v("FragmentManager", "saveAllState: no fragments!");
         }

         return null;
      } else {
         ArrayList var6 = this.mFragmentStore.saveAddedFragments();
         BackStackState[] var4 = null;
         ArrayList var7 = this.mBackStack;
         BackStackState[] var3 = var4;
         if (var7 != null) {
            int var2 = var7.size();
            var3 = var4;
            if (var2 > 0) {
               var4 = new BackStackState[var2];
               int var1 = 0;

               while(true) {
                  var3 = var4;
                  if (var1 >= var2) {
                     break;
                  }

                  var4[var1] = new BackStackState((BackStackRecord)this.mBackStack.get(var1));
                  if (isLoggingEnabled(2)) {
                     StringBuilder var8 = new StringBuilder();
                     var8.append("saveAllState: adding back stack #");
                     var8.append(var1);
                     var8.append(": ");
                     var8.append(this.mBackStack.get(var1));
                     Log.v("FragmentManager", var8.toString());
                  }

                  ++var1;
               }
            }
         }

         FragmentManagerState var9 = new FragmentManagerState();
         var9.mActive = var5;
         var9.mAdded = var6;
         var9.mBackStack = var3;
         var9.mBackStackIndex = this.mBackStackIndex.get();
         Fragment var10 = this.mPrimaryNav;
         if (var10 != null) {
            var9.mPrimaryNavActiveWho = var10.mWho;
         }

         return var9;
      }
   }

   public Fragment.SavedState saveFragmentInstanceState(Fragment var1) {
      FragmentStateManager var2 = this.mFragmentStore.getFragmentStateManager(var1.mWho);
      if (var2 == null || !var2.getFragment().equals(var1)) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Fragment ");
         var3.append(var1);
         var3.append(" is not currently in the FragmentManager");
         this.throwException(new IllegalStateException(var3.toString()));
      }

      return var2.saveInstanceState();
   }

   void scheduleCommit() {
      ArrayList var3 = this.mPendingActions;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label437: {
         ArrayList var4;
         try {
            var4 = this.mPostponedTransactions;
         } catch (Throwable var46) {
            var10000 = var46;
            var10001 = false;
            break label437;
         }

         boolean var1;
         boolean var2;
         label428: {
            label427: {
               var2 = false;
               if (var4 != null) {
                  try {
                     if (!this.mPostponedTransactions.isEmpty()) {
                        break label427;
                     }
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label437;
                  }
               }

               var1 = false;
               break label428;
            }

            var1 = true;
         }

         label419: {
            try {
               if (this.mPendingActions.size() != 1) {
                  break label419;
               }
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label437;
            }

            var2 = true;
         }

         if (var1 || var2) {
            try {
               this.mHost.getHandler().removeCallbacks(this.mExecCommit);
               this.mHost.getHandler().post(this.mExecCommit);
               this.updateOnBackPressedCallbackEnabled();
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label437;
            }
         }

         label407:
         try {
            return;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label407;
         }
      }

      while(true) {
         Throwable var47 = var10000;

         try {
            throw var47;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            continue;
         }
      }
   }

   void setExitAnimationOrder(Fragment var1, boolean var2) {
      ViewGroup var3 = this.getFragmentContainer(var1);
      if (var3 != null && var3 instanceof FragmentContainerView) {
         ((FragmentContainerView)var3).setDrawDisappearingViewsLast(var2 ^ true);
      }

   }

   public void setFragmentFactory(FragmentFactory var1) {
      this.mFragmentFactory = var1;
   }

   void setMaxLifecycle(Fragment var1, Lifecycle.State var2) {
      if (!var1.equals(this.findActiveFragment(var1.mWho)) || var1.mHost != null && var1.mFragmentManager != this) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Fragment ");
         var3.append(var1);
         var3.append(" is not an active fragment of FragmentManager ");
         var3.append(this);
         throw new IllegalArgumentException(var3.toString());
      } else {
         var1.mMaxState = var2;
      }
   }

   void setPrimaryNavigationFragment(Fragment var1) {
      if (var1 == null || var1.equals(this.findActiveFragment(var1.mWho)) && (var1.mHost == null || var1.mFragmentManager == this)) {
         Fragment var3 = this.mPrimaryNav;
         this.mPrimaryNav = var1;
         this.dispatchParentPrimaryNavigationFragmentChanged(var3);
         this.dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(var1);
         var2.append(" is not an active fragment of FragmentManager ");
         var2.append(this);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   void showFragment(Fragment var1) {
      if (isLoggingEnabled(2)) {
         StringBuilder var2 = new StringBuilder();
         var2.append("show: ");
         var2.append(var1);
         Log.v("FragmentManager", var2.toString());
      }

      if (var1.mHidden) {
         var1.mHidden = false;
         var1.mHiddenChanged ^= true;
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append("FragmentManager{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" in ");
      Fragment var2 = this.mParent;
      if (var2 != null) {
         var1.append(var2.getClass().getSimpleName());
         var1.append("{");
         var1.append(Integer.toHexString(System.identityHashCode(this.mParent)));
         var1.append("}");
      } else {
         var1.append(this.mHost.getClass().getSimpleName());
         var1.append("{");
         var1.append(Integer.toHexString(System.identityHashCode(this.mHost)));
         var1.append("}");
      }

      var1.append("}}");
      return var1.toString();
   }

   public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks var1) {
      this.mLifecycleCallbacksDispatcher.unregisterFragmentLifecycleCallbacks(var1);
   }

   public interface BackStackEntry {
      @Deprecated
      CharSequence getBreadCrumbShortTitle();

      @Deprecated
      int getBreadCrumbShortTitleRes();

      @Deprecated
      CharSequence getBreadCrumbTitle();

      @Deprecated
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

   interface OpGenerator {
      boolean generateOps(ArrayList var1, ArrayList var2);
   }

   private class PopBackStackState implements FragmentManager.OpGenerator {
      final int mFlags;
      final int mId;
      final String mName;

      PopBackStackState(String var2, int var3, int var4) {
         this.mName = var2;
         this.mId = var3;
         this.mFlags = var4;
      }

      public boolean generateOps(ArrayList var1, ArrayList var2) {
         return FragmentManager.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && FragmentManager.this.mPrimaryNav.getChildFragmentManager().popBackStackImmediate() ? false : FragmentManager.this.popBackStackState(var1, var2, this.mName, this.mId, this.mFlags);
      }
   }

   static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
      final boolean mIsBack;
      private int mNumPostponed;
      final BackStackRecord mRecord;

      StartEnterTransitionListener(BackStackRecord var1, boolean var2) {
         this.mIsBack = var2;
         this.mRecord = var1;
      }

      void cancelTransaction() {
         this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
      }

      void completeTransaction() {
         int var1 = this.mNumPostponed;
         boolean var2 = false;
         boolean var6;
         if (var1 > 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         Iterator var4 = this.mRecord.mManager.getFragments().iterator();

         while(var4.hasNext()) {
            Fragment var5 = (Fragment)var4.next();
            var5.setOnStartEnterTransitionListener((Fragment.OnStartEnterTransitionListener)null);
            if (var6 && var5.isPostponed()) {
               var5.startPostponedEnterTransition();
            }
         }

         FragmentManager var7 = this.mRecord.mManager;
         BackStackRecord var8 = this.mRecord;
         boolean var3 = this.mIsBack;
         if (!var6) {
            var2 = true;
         }

         var7.completeExecute(var8, var3, var2, true);
      }

      public boolean isReady() {
         return this.mNumPostponed == 0;
      }

      public void onStartEnterTransition() {
         int var1 = this.mNumPostponed - 1;
         this.mNumPostponed = var1;
         if (var1 == 0) {
            this.mRecord.mManager.scheduleCommit();
         }
      }

      public void startListening() {
         ++this.mNumPostponed;
      }
   }
}
