/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.Configuration
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Parcelable
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory2
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.animation.Animation
 *  androidx.fragment.R
 *  androidx.fragment.R$id
 */
package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.collection.ArraySet;
import androidx.core.os.CancellationSignal;
import androidx.core.util.LogWriter;
import androidx.fragment.R;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.BackStackState;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentLayoutInflaterFactory;
import androidx.fragment.app.FragmentLifecycleCallbacksDispatcher;
import androidx.fragment.app.FragmentManagerNonConfig;
import androidx.fragment.app.FragmentManagerState;
import androidx.fragment.app.FragmentManagerViewModel;
import androidx.fragment.app.FragmentState;
import androidx.fragment.app.FragmentStateManager;
import androidx.fragment.app.FragmentStore;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransition;
import androidx.fragment.app.FragmentViewLifecycleOwner;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FragmentManager {
    private static boolean DEBUG = false;
    public static final int POP_BACK_STACK_INCLUSIVE = 1;
    static final String TAG = "FragmentManager";
    ArrayList<BackStackRecord> mBackStack;
    private ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    private final AtomicInteger mBackStackIndex;
    FragmentContainer mContainer;
    private ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    private boolean mDestroyed;
    private Runnable mExecCommit;
    private boolean mExecutingActions;
    private ConcurrentHashMap<Fragment, HashSet<CancellationSignal>> mExitAnimationCancellationSignals;
    private FragmentFactory mFragmentFactory;
    private final FragmentStore mFragmentStore = new FragmentStore();
    private final FragmentTransition.Callback mFragmentTransitionCallback;
    private boolean mHavePendingDeferredStart;
    FragmentHostCallback<?> mHost;
    private FragmentFactory mHostFragmentFactory;
    private final FragmentLayoutInflaterFactory mLayoutInflaterFactory;
    private final FragmentLifecycleCallbacksDispatcher mLifecycleCallbacksDispatcher;
    private boolean mNeedMenuInvalidate;
    private FragmentManagerViewModel mNonConfig;
    private final OnBackPressedCallback mOnBackPressedCallback;
    private OnBackPressedDispatcher mOnBackPressedDispatcher;
    private Fragment mParent;
    private final ArrayList<OpGenerator> mPendingActions = new ArrayList();
    private ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    private boolean mStateSaved;
    private boolean mStopped;
    private ArrayList<Fragment> mTmpAddedFragments;
    private ArrayList<Boolean> mTmpIsPop;
    private ArrayList<BackStackRecord> mTmpRecords;

    static {
        DEBUG = false;
    }

    public FragmentManager() {
        this.mLayoutInflaterFactory = new FragmentLayoutInflaterFactory(this);
        this.mOnBackPressedCallback = new OnBackPressedCallback(false){

            @Override
            public void handleOnBackPressed() {
                FragmentManager.this.handleOnBackPressed();
            }
        };
        this.mBackStackIndex = new AtomicInteger();
        this.mExitAnimationCancellationSignals = new ConcurrentHashMap();
        this.mFragmentTransitionCallback = new FragmentTransition.Callback(){

            @Override
            public void onComplete(Fragment fragment, CancellationSignal cancellationSignal) {
                if (!cancellationSignal.isCanceled()) {
                    FragmentManager.this.removeCancellationSignal(fragment, cancellationSignal);
                }
            }

            @Override
            public void onStart(Fragment fragment, CancellationSignal cancellationSignal) {
                FragmentManager.this.addCancellationSignal(fragment, cancellationSignal);
            }
        };
        this.mLifecycleCallbacksDispatcher = new FragmentLifecycleCallbacksDispatcher(this);
        this.mCurState = -1;
        this.mFragmentFactory = null;
        this.mHostFragmentFactory = new FragmentFactory(){

            @Override
            public Fragment instantiate(ClassLoader classLoader, String string2) {
                return FragmentManager.this.mHost.instantiate(FragmentManager.this.mHost.getContext(), string2, null);
            }
        };
        this.mExecCommit = new Runnable(){

            @Override
            public void run() {
                FragmentManager.this.execPendingActions(true);
            }
        };
    }

    private void addAddedFragments(ArraySet<Fragment> arraySet) {
        int n = this.mCurState;
        if (n < 1) {
            return;
        }
        n = Math.min(n, 3);
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment.mState >= n) continue;
            this.moveToState(fragment, n);
            if (fragment.mView == null || fragment.mHidden || !fragment.mIsNewlyAdded) continue;
            arraySet.add(fragment);
        }
    }

    private void cancelExitAnimation(Fragment fragment) {
        HashSet<CancellationSignal> hashSet = this.mExitAnimationCancellationSignals.get(fragment);
        if (hashSet != null) {
            Iterator<CancellationSignal> iterator = hashSet.iterator();
            while (iterator.hasNext()) {
                iterator.next().cancel();
            }
            hashSet.clear();
            this.destroyFragmentView(fragment);
            this.mExitAnimationCancellationSignals.remove(fragment);
        }
    }

    private void checkStateLoss() {
        if (!this.isStateSaved()) {
            return;
        }
        throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    private void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            FragmentAnim.AnimationOrAnimator animationOrAnimator = FragmentAnim.loadAnimation(this.mHost.getContext(), this.mContainer, fragment, fragment.mHidden ^ true);
            if (animationOrAnimator != null && animationOrAnimator.animator != null) {
                animationOrAnimator.animator.setTarget((Object)fragment.mView);
                if (fragment.mHidden) {
                    if (fragment.isHideReplaced()) {
                        fragment.setHideReplaced(false);
                    } else {
                        final ViewGroup viewGroup = fragment.mContainer;
                        final View view = fragment.mView;
                        viewGroup.startViewTransition(view);
                        animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                            public void onAnimationEnd(Animator animator) {
                                viewGroup.endViewTransition(view);
                                animator.removeListener((Animator.AnimatorListener)this);
                                if (fragment.mView != null && fragment.mHidden) {
                                    fragment.mView.setVisibility(8);
                                }
                            }
                        });
                    }
                } else {
                    fragment.mView.setVisibility(0);
                }
                animationOrAnimator.animator.start();
            } else {
                if (animationOrAnimator != null) {
                    fragment.mView.startAnimation(animationOrAnimator.animation);
                    animationOrAnimator.animation.start();
                }
                int n = fragment.mHidden && !fragment.isHideReplaced() ? 8 : 0;
                fragment.mView.setVisibility(n);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            }
        }
        if (fragment.mAdded && this.isMenuAvailable(fragment)) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    private void destroyFragmentView(Fragment fragment) {
        fragment.performDestroyView();
        this.mLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(fragment, false);
        fragment.mContainer = null;
        fragment.mView = null;
        fragment.mViewLifecycleOwner = null;
        fragment.mViewLifecycleOwnerLiveData.setValue(null);
        fragment.mInLayout = false;
    }

    private void dispatchParentPrimaryNavigationFragmentChanged(Fragment fragment) {
        if (fragment != null && fragment.equals(this.findActiveFragment(fragment.mWho))) {
            fragment.performPrimaryNavigationFragmentChanged();
        }
    }

    private void dispatchStateChange(int n) {
        try {
            this.mExecutingActions = true;
            this.mFragmentStore.dispatchStateChange(n);
            this.moveToState(n, false);
            this.execPendingActions(true);
            return;
        }
        finally {
            this.mExecutingActions = false;
        }
    }

    private void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
        }
    }

    @Deprecated
    public static void enableDebugLogging(boolean bl) {
        DEBUG = bl;
    }

    private void endAnimatingAwayFragments() {
        if (!this.mExitAnimationCancellationSignals.isEmpty()) {
            for (Fragment fragment : this.mExitAnimationCancellationSignals.keySet()) {
                this.cancelExitAnimation(fragment);
                this.moveToState(fragment, fragment.getStateAfterAnimating());
            }
        }
    }

    private void ensureExecReady(boolean bl) {
        if (!this.mExecutingActions) {
            if (this.mHost == null) {
                if (this.mDestroyed) {
                    throw new IllegalStateException("FragmentManager has been destroyed");
                }
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
                if (!bl) {
                    this.checkStateLoss();
                }
                if (this.mTmpRecords == null) {
                    this.mTmpRecords = new ArrayList();
                    this.mTmpIsPop = new ArrayList();
                }
                this.mExecutingActions = true;
                try {
                    this.executePostponedTransaction(null, null);
                    return;
                }
                finally {
                    this.mExecutingActions = false;
                }
            }
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        throw new IllegalStateException("FragmentManager is already executing transactions");
    }

    private static void executeOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2) {
        while (n < n2) {
            BackStackRecord backStackRecord = arrayList.get(n);
            boolean bl = arrayList2.get(n);
            boolean bl2 = true;
            if (bl) {
                backStackRecord.bumpBackStackNesting(-1);
                if (n != n2 - 1) {
                    bl2 = false;
                }
                backStackRecord.executePopOps(bl2);
            } else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            ++n;
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2) {
        int n3;
        boolean bl = arrayList.get((int)n).mReorderingAllowed;
        ArrayList<Fragment> arrayList3 = this.mTmpAddedFragments;
        if (arrayList3 == null) {
            this.mTmpAddedFragments = new ArrayList();
        } else {
            arrayList3.clear();
        }
        this.mTmpAddedFragments.addAll(this.mFragmentStore.getFragments());
        arrayList3 = this.getPrimaryNavigationFragment();
        int n4 = n;
        int n5 = 0;
        do {
            int n6 = 1;
            if (n4 >= n2) break;
            BackStackRecord backStackRecord = arrayList.get(n4);
            arrayList3 = arrayList2.get(n4) == false ? backStackRecord.expandOps(this.mTmpAddedFragments, (Fragment)((Object)arrayList3)) : backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, (Fragment)((Object)arrayList3));
            n3 = n6;
            if (n5 == 0) {
                n3 = backStackRecord.mAddToBackStack ? n6 : 0;
            }
            ++n4;
            n5 = n3;
        } while (true);
        this.mTmpAddedFragments.clear();
        if (!bl) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, n, n2, false, this.mFragmentTransitionCallback);
        }
        FragmentManager.executeOps(arrayList, arrayList2, n, n2);
        n3 = n2;
        if (bl) {
            arrayList3 = new ArraySet<Fragment>();
            this.addAddedFragments((ArraySet<Fragment>)((Object)arrayList3));
            n3 = this.postponePostponableTransactions(arrayList, arrayList2, n, n2, (ArraySet<Fragment>)((Object)arrayList3));
            this.makeRemovedFragmentsInvisible((ArraySet<Fragment>)((Object)arrayList3));
        }
        if (n3 != n && bl) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, n, n3, true, this.mFragmentTransitionCallback);
            this.moveToState(this.mCurState, true);
        }
        while (n < n2) {
            arrayList3 = arrayList.get(n);
            if (arrayList2.get(n).booleanValue() && arrayList3.mIndex >= 0) {
                arrayList3.mIndex = -1;
            }
            arrayList3.runOnCommitRunnables();
            ++n;
        }
        if (n5 != 0) {
            this.reportBackStackChanged();
        }
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        ArrayList<StartEnterTransitionListener> arrayList3 = this.mPostponedTransactions;
        int n = arrayList3 == null ? 0 : arrayList3.size();
        int n2 = 0;
        int n3 = n;
        while (n2 < n3) {
            int n4;
            block7 : {
                block8 : {
                    block6 : {
                        arrayList3 = this.mPostponedTransactions.get(n2);
                        if (arrayList == null || arrayList3.mIsBack || (n = arrayList.indexOf(arrayList3.mRecord)) == -1 || arrayList2 == null || !arrayList2.get(n).booleanValue()) break block6;
                        this.mPostponedTransactions.remove(n2);
                        n4 = n2 - 1;
                        n = n3 - 1;
                        arrayList3.cancelTransaction();
                        break block7;
                    }
                    if (arrayList3.isReady()) break block8;
                    n = n3;
                    n4 = n2;
                    if (arrayList == null) break block7;
                    n = n3;
                    n4 = n2;
                    if (!arrayList3.mRecord.interactsWith(arrayList, 0, arrayList.size())) break block7;
                }
                this.mPostponedTransactions.remove(n2);
                n4 = n2 - 1;
                n = n3 - 1;
                if (arrayList != null && !arrayList3.mIsBack && (n2 = arrayList.indexOf(arrayList3.mRecord)) != -1 && arrayList2 != null && arrayList2.get(n2).booleanValue()) {
                    arrayList3.cancelTransaction();
                } else {
                    arrayList3.completeTransaction();
                }
            }
            n2 = n4 + 1;
            n3 = n;
        }
    }

    public static <F extends Fragment> F findFragment(View view) {
        Object object = FragmentManager.findViewFragment(view);
        if (object != null) {
            return (F)object;
        }
        object = new StringBuilder();
        object.append("View ");
        object.append((Object)view);
        object.append(" does not have a Fragment set");
        throw new IllegalStateException(object.toString());
    }

    static FragmentManager findFragmentManager(View view) {
        FragmentActivity fragmentActivity;
        Object object = FragmentManager.findViewFragment(view);
        if (object != null) {
            return object.getChildFragmentManager();
        }
        object = view.getContext();
        FragmentActivity fragmentActivity2 = null;
        do {
            fragmentActivity = fragmentActivity2;
            if (!(object instanceof ContextWrapper)) break;
            if (object instanceof FragmentActivity) {
                fragmentActivity = (FragmentActivity)object;
                break;
            }
            object = ((ContextWrapper)object).getBaseContext();
        } while (true);
        if (fragmentActivity != null) {
            return fragmentActivity.getSupportFragmentManager();
        }
        object = new StringBuilder();
        object.append("View ");
        object.append((Object)view);
        object.append(" is not within a subclass of FragmentActivity.");
        throw new IllegalStateException(object.toString());
    }

    private static Fragment findViewFragment(View view) {
        do {
            Object var1_1 = null;
            if (view == null) break;
            Fragment fragment = FragmentManager.getViewFragment(view);
            if (fragment != null) {
                return fragment;
            }
            fragment = view.getParent();
            view = var1_1;
            if (!(fragment instanceof View)) continue;
            view = (View)fragment;
        } while (true);
        return null;
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        boolean bl = false;
        ArrayList<OpGenerator> arrayList3 = this.mPendingActions;
        synchronized (arrayList3) {
            if (this.mPendingActions.isEmpty()) {
                return false;
            }
            int n = this.mPendingActions.size();
            int n2 = 0;
            do {
                if (n2 >= n) {
                    this.mPendingActions.clear();
                    this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                    return bl;
                }
                bl |= this.mPendingActions.get(n2).generateOps(arrayList, arrayList2);
                ++n2;
            } while (true);
        }
    }

    private FragmentManagerViewModel getChildNonConfig(Fragment fragment) {
        return this.mNonConfig.getChildNonConfig(fragment);
    }

    private ViewGroup getFragmentContainer(Fragment fragment) {
        if (fragment.mContainerId <= 0) {
            return null;
        }
        if (this.mContainer.onHasView() && (fragment = this.mContainer.onFindViewById(fragment.mContainerId)) instanceof ViewGroup) {
            return (ViewGroup)fragment;
        }
        return null;
    }

    static Fragment getViewFragment(View object) {
        if ((object = object.getTag(R.id.fragment_container_view_tag)) instanceof Fragment) {
            return (Fragment)object;
        }
        return null;
    }

    static boolean isLoggingEnabled(int n) {
        if (!DEBUG && !Log.isLoggable((String)"FragmentManager", (int)n)) {
            return false;
        }
        return true;
    }

    private boolean isMenuAvailable(Fragment fragment) {
        if (fragment.mHasMenu && fragment.mMenuVisible || fragment.mChildFragmentManager.checkForMenus()) {
            return true;
        }
        return false;
    }

    private void makeInactive(FragmentStateManager fragmentStateManager) {
        Fragment fragment = fragmentStateManager.getFragment();
        if (!this.mFragmentStore.containsActiveFragment(fragment.mWho)) {
            return;
        }
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Removed fragment from active set ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.mFragmentStore.makeInactive(fragmentStateManager);
        this.removeRetainedFragment(fragment);
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> arraySet) {
        int n = arraySet.size();
        for (int i = 0; i < n; ++i) {
            Fragment fragment = arraySet.valueAt(i);
            if (fragment.mAdded) continue;
            View view = fragment.requireView();
            fragment.mPostponedAlpha = view.getAlpha();
            view.setAlpha(0.0f);
        }
    }

    private boolean popBackStackImmediate(String string2, int n, int n2) {
        boolean bl;
        this.execPendingActions(false);
        this.ensureExecReady(true);
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null && n < 0 && string2 == null && fragment.getChildFragmentManager().popBackStackImmediate()) {
            return true;
        }
        bl = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, string2, n, n2);
        if (bl) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.updateOnBackPressedCallbackEnabled();
        this.doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return bl;
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2, ArraySet<Fragment> arraySet) {
        int n3 = n2;
        for (int i = n2 - 1; i >= n; --i) {
            BackStackRecord backStackRecord = arrayList.get(i);
            boolean bl = arrayList2.get(i);
            boolean bl2 = backStackRecord.isPostponed() && !backStackRecord.interactsWith(arrayList, i + 1, n2);
            int n4 = n3;
            if (bl2) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList();
                }
                StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, bl);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
                if (bl) {
                    backStackRecord.executeOps();
                } else {
                    backStackRecord.executePopOps(false);
                }
                n4 = n3 - 1;
                if (i != n4) {
                    arrayList.remove(i);
                    arrayList.add(n4, backStackRecord);
                }
                this.addAddedFragments(arraySet);
            }
            n3 = n4;
        }
        return n3;
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (arrayList.isEmpty()) {
            return;
        }
        if (arrayList.size() == arrayList2.size()) {
            this.executePostponedTransaction(arrayList, arrayList2);
            int n = arrayList.size();
            int n2 = 0;
            int n3 = 0;
            while (n3 < n) {
                int n4 = n2;
                int n5 = n3;
                if (!arrayList.get((int)n3).mReorderingAllowed) {
                    if (n2 != n3) {
                        this.executeOpsTogether(arrayList, arrayList2, n2, n3);
                    }
                    n2 = n4 = n3 + 1;
                    if (arrayList2.get(n3).booleanValue()) {
                        do {
                            n2 = n4;
                            if (n4 >= n) break;
                            n2 = n4;
                            if (!arrayList2.get(n4).booleanValue()) break;
                            n2 = n4++;
                        } while (!arrayList.get((int)n4).mReorderingAllowed);
                    }
                    this.executeOpsTogether(arrayList, arrayList2, n3, n2);
                    n4 = n2;
                    n5 = n2 - 1;
                }
                n3 = n5 + 1;
                n2 = n4;
            }
            if (n2 != n) {
                this.executeOpsTogether(arrayList, arrayList2, n2, n);
            }
            return;
        }
        throw new IllegalStateException("Internal error with the back stack records");
    }

    private void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    static int reverseTransit(int n) {
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    return 0;
                }
                return 4097;
            }
            return 4099;
        }
        return 8194;
    }

    private void setVisibleRemovingFragment(Fragment fragment) {
        ViewGroup viewGroup = this.getFragmentContainer(fragment);
        if (viewGroup != null) {
            if (viewGroup.getTag(R.id.visible_removing_fragment_view_tag) == null) {
                viewGroup.setTag(R.id.visible_removing_fragment_view_tag, (Object)fragment);
            }
            ((Fragment)viewGroup.getTag(R.id.visible_removing_fragment_view_tag)).setNextAnim(fragment.getNextAnim());
        }
    }

    private void startPendingDeferredFragments() {
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment == null) continue;
            this.performPendingDeferredStart(fragment);
        }
    }

    private void throwException(RuntimeException runtimeException) {
        Log.e((String)"FragmentManager", (String)runtimeException.getMessage());
        Log.e((String)"FragmentManager", (String)"Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.onDump("  ", null, printWriter, new String[0]);
            }
            catch (Exception exception) {
                Log.e((String)"FragmentManager", (String)"Failed dumping state", (Throwable)exception);
            }
        } else {
            try {
                this.dump("  ", null, printWriter, new String[0]);
            }
            catch (Exception exception) {
                Log.e((String)"FragmentManager", (String)"Failed dumping state", (Throwable)exception);
            }
        }
        throw runtimeException;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateOnBackPressedCallbackEnabled() {
        Object object = this.mPendingActions;
        synchronized (object) {
            boolean bl = this.mPendingActions.isEmpty();
            boolean bl2 = true;
            if (!bl) {
                this.mOnBackPressedCallback.setEnabled(true);
                return;
            }
            object = this.mOnBackPressedCallback;
            if (this.getBackStackEntryCount() <= 0 || !this.isPrimaryNavigation(this.mParent)) {
                bl2 = false;
            }
            object.setEnabled(bl2);
            return;
        }
    }

    void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(backStackRecord);
    }

    void addCancellationSignal(Fragment fragment, CancellationSignal cancellationSignal) {
        if (this.mExitAnimationCancellationSignals.get(fragment) == null) {
            this.mExitAnimationCancellationSignals.put(fragment, new HashSet());
        }
        this.mExitAnimationCancellationSignals.get(fragment).add(cancellationSignal);
    }

    void addFragment(Fragment fragment) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("add: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.makeActive(fragment);
        if (!fragment.mDetached) {
            this.mFragmentStore.addFragment(fragment);
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (this.isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
        }
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    void addRetainedFragment(Fragment fragment) {
        if (this.isStateSaved()) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v((String)"FragmentManager", (String)"Ignoring addRetainedFragment as the state is already saved");
            }
            return;
        }
        if (this.mNonConfig.addRetainedFragment(fragment) && FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Updating retained Fragments: Added ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
    }

    int allocBackStackIndex() {
        return this.mBackStackIndex.getAndIncrement();
    }

    void attachController(FragmentHostCallback<?> fragmentHostCallback, FragmentContainer object, Fragment fragment) {
        if (this.mHost == null) {
            this.mHost = fragmentHostCallback;
            this.mContainer = object;
            this.mParent = fragment;
            if (fragment != null) {
                this.updateOnBackPressedCallbackEnabled();
            }
            if (fragmentHostCallback instanceof OnBackPressedDispatcherOwner) {
                object = (OnBackPressedDispatcherOwner)((Object)fragmentHostCallback);
                this.mOnBackPressedDispatcher = object.getOnBackPressedDispatcher();
                if (fragment != null) {
                    object = fragment;
                }
                this.mOnBackPressedDispatcher.addCallback((LifecycleOwner)object, this.mOnBackPressedCallback);
            }
            if (fragment != null) {
                this.mNonConfig = fragment.mFragmentManager.getChildNonConfig(fragment);
                return;
            }
            if (fragmentHostCallback instanceof ViewModelStoreOwner) {
                this.mNonConfig = FragmentManagerViewModel.getInstance(((ViewModelStoreOwner)((Object)fragmentHostCallback)).getViewModelStore());
                return;
            }
            this.mNonConfig = new FragmentManagerViewModel(false);
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    void attachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (FragmentManager.isLoggingEnabled(2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("attach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                this.mFragmentStore.addFragment(fragment);
                if (FragmentManager.isLoggingEnabled(2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("add from attach: ");
                    stringBuilder.append(fragment);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                if (this.isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
            }
        }
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    boolean checkForMenus() {
        boolean bl = false;
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment != null) {
                bl = this.isMenuAvailable(fragment);
            }
            if (!bl) continue;
            return true;
        }
        return false;
    }

    void completeExecute(BackStackRecord backStackRecord, boolean bl, boolean bl2, boolean bl3) {
        if (bl) {
            backStackRecord.executePopOps(bl3);
        } else {
            backStackRecord.executeOps();
        }
        ArrayList<BackStackRecord> arrayList = new ArrayList<BackStackRecord>(1);
        ArrayList<Boolean> object2 = new ArrayList<Boolean>(1);
        arrayList.add(backStackRecord);
        object2.add(bl);
        if (bl2) {
            FragmentTransition.startTransitions(this, arrayList, object2, 0, 1, true, this.mFragmentTransitionCallback);
        }
        if (bl3) {
            this.moveToState(this.mCurState, true);
        }
        for (Fragment fragment : this.mFragmentStore.getActiveFragments()) {
            if (fragment == null || fragment.mView == null || !fragment.mIsNewlyAdded || !backStackRecord.interactsWith(fragment.mContainerId)) continue;
            if (fragment.mPostponedAlpha > 0.0f) {
                fragment.mView.setAlpha(fragment.mPostponedAlpha);
            }
            if (bl3) {
                fragment.mPostponedAlpha = 0.0f;
                continue;
            }
            fragment.mPostponedAlpha = -1.0f;
            fragment.mIsNewlyAdded = false;
        }
    }

    void detachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (FragmentManager.isLoggingEnabled(2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("detach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("remove from detach: ");
                    stringBuilder.append(fragment);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                this.mFragmentStore.removeFragment(fragment);
                if (this.isMenuAvailable(fragment)) {
                    this.mNeedMenuInvalidate = true;
                }
                this.setVisibleRemovingFragment(fragment);
            }
        }
    }

    void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(2);
    }

    void dispatchConfigurationChanged(Configuration configuration) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performConfigurationChanged(configuration);
        }
    }

    boolean dispatchContextItemSelected(MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null || !fragment.performContextItemSelected(menuItem)) continue;
            return true;
        }
        return false;
    }

    void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        this.dispatchStateChange(1);
    }

    boolean dispatchCreateOptionsMenu(Menu object, MenuInflater menuInflater) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean bl = false;
        ArrayList<Fragment> arrayList = null;
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            boolean bl2 = bl;
            ArrayList<Fragment> arrayList2 = arrayList;
            if (fragment != null) {
                bl2 = bl;
                arrayList2 = arrayList;
                if (fragment.performCreateOptionsMenu((Menu)object, menuInflater)) {
                    bl2 = true;
                    arrayList2 = arrayList;
                    if (arrayList == null) {
                        arrayList2 = new ArrayList<Fragment>();
                    }
                    arrayList2.add(fragment);
                }
            }
            bl = bl2;
            arrayList = arrayList2;
        }
        if (this.mCreatedMenus != null) {
            for (int i = 0; i < this.mCreatedMenus.size(); ++i) {
                object = this.mCreatedMenus.get(i);
                if (arrayList != null && arrayList.contains(object)) continue;
                object.onDestroyOptionsMenu();
            }
        }
        this.mCreatedMenus = arrayList;
        return bl;
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
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performLowMemory();
        }
    }

    void dispatchMultiWindowModeChanged(boolean bl) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performMultiWindowModeChanged(bl);
        }
    }

    boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null || !fragment.performOptionsItemSelected(menuItem)) continue;
            return true;
        }
        return false;
    }

    void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState < 1) {
            return;
        }
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performOptionsMenuClosed(menu);
        }
    }

    void dispatchPause() {
        this.dispatchStateChange(3);
    }

    void dispatchPictureInPictureModeChanged(boolean bl) {
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.performPictureInPictureModeChanged(bl);
        }
    }

    boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean bl = false;
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            boolean bl2 = bl;
            if (fragment != null) {
                bl2 = bl;
                if (fragment.performPrepareOptionsMenu(menu)) {
                    bl2 = true;
                }
            }
            bl = bl2;
        }
        return bl;
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void dump(String string2, FileDescriptor arrayList, PrintWriter printWriter, String[] object) {
        int n;
        int n2;
        CharSequence charSequence = new StringBuilder();
        charSequence.append(string2);
        charSequence.append("    ");
        charSequence = charSequence.toString();
        this.mFragmentStore.dump(string2, (FileDescriptor)((Object)arrayList), printWriter, (String[])object);
        arrayList = this.mCreatedMenus;
        if (arrayList != null && (n2 = arrayList.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Fragments Created Menus:");
            for (n = 0; n < n2; ++n) {
                arrayList = this.mCreatedMenus.get(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(arrayList.toString());
            }
        }
        if ((arrayList = this.mBackStack) != null && (n2 = arrayList.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Back Stack:");
            for (n = 0; n < n2; ++n) {
                arrayList = this.mBackStack.get(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(arrayList.toString());
                arrayList.dump((String)charSequence, printWriter);
            }
        }
        printWriter.print(string2);
        arrayList = new StringBuilder();
        arrayList.append("Back Stack Index: ");
        arrayList.append(this.mBackStackIndex.get());
        printWriter.println(arrayList.toString());
        arrayList = this.mPendingActions;
        // MONITORENTER : arrayList
        n2 = this.mPendingActions.size();
        if (n2 > 0) {
            printWriter.print(string2);
            printWriter.println("Pending Actions:");
            for (n = 0; n < n2; ++n) {
                object = this.mPendingActions.get(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(object);
            }
        }
        // MONITOREXIT : arrayList
        printWriter.print(string2);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(string2);
        printWriter.print("  mHost=");
        printWriter.println(this.mHost);
        printWriter.print(string2);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(string2);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(string2);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mStopped=");
        printWriter.print(this.mStopped);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (!this.mNeedMenuInvalidate) return;
        printWriter.print(string2);
        printWriter.print("  mNeedMenuInvalidate=");
        printWriter.println(this.mNeedMenuInvalidate);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void enqueueAction(OpGenerator opGenerator, boolean bl) {
        if (!bl) {
            if (this.mHost == null) {
                if (this.mDestroyed) {
                    throw new IllegalStateException("FragmentManager has been destroyed");
                }
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            this.checkStateLoss();
        }
        ArrayList<OpGenerator> arrayList = this.mPendingActions;
        synchronized (arrayList) {
            if (this.mHost != null) {
                this.mPendingActions.add(opGenerator);
                this.scheduleCommit();
                return;
            }
            if (bl) {
                return;
            }
            throw new IllegalStateException("Activity has been destroyed");
        }
    }

    boolean execPendingActions(boolean bl) {
        this.ensureExecReady(bl);
        bl = false;
        while (this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                bl = true;
            }
            finally {
                this.cleanupExec();
            }
        }
        this.updateOnBackPressedCallbackEnabled();
        this.doPendingDeferredStart();
        this.mFragmentStore.burpActive();
        return bl;
    }

    void execSingleAction(OpGenerator opGenerator, boolean bl) {
        if (bl && (this.mHost == null || this.mDestroyed)) {
            return;
        }
        this.ensureExecReady(bl);
        if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.updateOnBackPressedCallbackEnabled();
        this.doPendingDeferredStart();
        this.mFragmentStore.burpActive();
    }

    public boolean executePendingTransactions() {
        boolean bl = this.execPendingActions(true);
        this.forcePostponedTransactions();
        return bl;
    }

    Fragment findActiveFragment(String string2) {
        return this.mFragmentStore.findActiveFragment(string2);
    }

    public Fragment findFragmentById(int n) {
        return this.mFragmentStore.findFragmentById(n);
    }

    public Fragment findFragmentByTag(String string2) {
        return this.mFragmentStore.findFragmentByTag(string2);
    }

    Fragment findFragmentByWho(String string2) {
        return this.mFragmentStore.findFragmentByWho(string2);
    }

    int getActiveFragmentCount() {
        return this.mFragmentStore.getActiveFragmentCount();
    }

    List<Fragment> getActiveFragments() {
        return this.mFragmentStore.getActiveFragments();
    }

    public BackStackEntry getBackStackEntryAt(int n) {
        return this.mBackStack.get(n);
    }

    public int getBackStackEntryCount() {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public Fragment getFragment(Bundle object, String string2) {
        if ((object = object.getString(string2)) == null) {
            return null;
        }
        Fragment fragment = this.findActiveFragment((String)object);
        if (fragment == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment no longer exists for key ");
            stringBuilder.append(string2);
            stringBuilder.append(": unique id ");
            stringBuilder.append((String)object);
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        return fragment;
    }

    public FragmentFactory getFragmentFactory() {
        Object object = this.mFragmentFactory;
        if (object != null) {
            return object;
        }
        object = this.mParent;
        if (object != null) {
            return object.mFragmentManager.getFragmentFactory();
        }
        return this.mHostFragmentFactory;
    }

    public List<Fragment> getFragments() {
        return this.mFragmentStore.getFragments();
    }

    LayoutInflater.Factory2 getLayoutInflaterFactory() {
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

    ViewModelStore getViewModelStore(Fragment fragment) {
        return this.mNonConfig.getViewModelStore(fragment);
    }

    void handleOnBackPressed() {
        this.execPendingActions(true);
        if (this.mOnBackPressedCallback.isEnabled()) {
            this.popBackStackImmediate();
            return;
        }
        this.mOnBackPressedDispatcher.onBackPressed();
    }

    void hideFragment(Fragment fragment) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hide: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
            this.setVisibleRemovingFragment(fragment);
        }
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    boolean isPrimaryNavigation(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        FragmentManager fragmentManager = fragment.mFragmentManager;
        if (fragment.equals(fragmentManager.getPrimaryNavigationFragment()) && this.isPrimaryNavigation(fragmentManager.mParent)) {
            return true;
        }
        return false;
    }

    boolean isStateAtLeast(int n) {
        if (this.mCurState >= n) {
            return true;
        }
        return false;
    }

    public boolean isStateSaved() {
        if (!this.mStateSaved && !this.mStopped) {
            return false;
        }
        return true;
    }

    void makeActive(Fragment fragment) {
        if (this.mFragmentStore.containsActiveFragment(fragment.mWho)) {
            return;
        }
        Object object = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, fragment);
        object.restoreState(this.mHost.getContext().getClassLoader());
        this.mFragmentStore.makeActive((FragmentStateManager)object);
        if (fragment.mRetainInstanceChangedWhileDetached) {
            if (fragment.mRetainInstance) {
                this.addRetainedFragment(fragment);
            } else {
                this.removeRetainedFragment(fragment);
            }
            fragment.mRetainInstanceChangedWhileDetached = false;
        }
        object.setFragmentManagerState(this.mCurState);
        if (FragmentManager.isLoggingEnabled(2)) {
            object = new StringBuilder();
            object.append("Added fragment to active set ");
            object.append(fragment);
            Log.v((String)"FragmentManager", (String)object.toString());
        }
    }

    void moveFragmentToExpectedState(Fragment fragment) {
        if (!this.mFragmentStore.containsActiveFragment(fragment.mWho)) {
            if (FragmentManager.isLoggingEnabled(3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Ignoring moving ");
                stringBuilder.append(fragment);
                stringBuilder.append(" to state ");
                stringBuilder.append(this.mCurState);
                stringBuilder.append("since it is not added to ");
                stringBuilder.append(this);
                Log.d((String)"FragmentManager", (String)stringBuilder.toString());
            }
            return;
        }
        this.moveToState(fragment);
        if (fragment.mView != null) {
            Object object = this.mFragmentStore.findFragmentUnder(fragment);
            if (object != null) {
                object = object.mView;
                ViewGroup viewGroup = fragment.mContainer;
                int n = viewGroup.indexOfChild((View)object);
                int n2 = viewGroup.indexOfChild(fragment.mView);
                if (n2 < n) {
                    viewGroup.removeViewAt(n2);
                    viewGroup.addView(fragment.mView, n);
                }
            }
            if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                if (fragment.mPostponedAlpha > 0.0f) {
                    fragment.mView.setAlpha(fragment.mPostponedAlpha);
                }
                fragment.mPostponedAlpha = 0.0f;
                fragment.mIsNewlyAdded = false;
                object = FragmentAnim.loadAnimation(this.mHost.getContext(), this.mContainer, fragment, true);
                if (object != null) {
                    if (object.animation != null) {
                        fragment.mView.startAnimation(object.animation);
                    } else {
                        object.animator.setTarget((Object)fragment.mView);
                        object.animator.start();
                    }
                }
            }
        }
        if (fragment.mHiddenChanged) {
            this.completeShowHideFragment(fragment);
        }
    }

    void moveToState(int n, boolean bl) {
        if (this.mHost == null && n != -1) {
            throw new IllegalStateException("No activity");
        }
        if (!bl && n == this.mCurState) {
            return;
        }
        this.mCurState = n;
        Object object = this.mFragmentStore.getFragments().iterator();
        while (object.hasNext()) {
            this.moveFragmentToExpectedState(object.next());
        }
        object = this.mFragmentStore.getActiveFragments().iterator();
        while (object.hasNext()) {
            Fragment fragment = object.next();
            if (fragment == null || fragment.mIsNewlyAdded) continue;
            this.moveFragmentToExpectedState(fragment);
        }
        this.startPendingDeferredFragments();
        if (this.mNeedMenuInvalidate && (object = this.mHost) != null && this.mCurState == 4) {
            object.onSupportInvalidateOptionsMenu();
            this.mNeedMenuInvalidate = false;
        }
    }

    void moveToState(Fragment fragment) {
        this.moveToState(fragment, this.mCurState);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void moveToState(Fragment var1_1, int var2_2) {
        block47 : {
            block40 : {
                block46 : {
                    block45 : {
                        block44 : {
                            block43 : {
                                block42 : {
                                    block41 : {
                                        var7_3 = this.mFragmentStore.getFragmentStateManager(var1_1.mWho);
                                        var5_4 = 1;
                                        var6_5 = var7_3;
                                        if (var7_3 == null) {
                                            var6_5 = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, var1_1);
                                            var6_5.setFragmentManagerState(1);
                                        }
                                        if (var1_1.mState > (var3_6 = Math.min(var2_2, var6_5.computeMaxState()))) break block40;
                                        if (var1_1.mState < var3_6 && !this.mExitAnimationCancellationSignals.isEmpty()) {
                                            this.cancelExitAnimation(var1_1);
                                        }
                                        if ((var2_2 = var1_1.mState) == -1) break block41;
                                        if (var2_2 == 0) break block42;
                                        if (var2_2 == 1) break block43;
                                        if (var2_2 == 2) break block44;
                                        if (var2_2 == 3) break block45;
                                        break block46;
                                    }
                                    if (var3_6 > -1) {
                                        if (FragmentManager.isLoggingEnabled(3)) {
                                            var7_3 = new StringBuilder();
                                            var7_3.append("moveto ATTACHED: ");
                                            var7_3.append(var1_1);
                                            Log.d((String)"FragmentManager", (String)var7_3.toString());
                                        }
                                        if (var1_1.mTarget != null) {
                                            if (!var1_1.mTarget.equals(this.findActiveFragment(var1_1.mTarget.mWho))) {
                                                var6_5 = new StringBuilder();
                                                var6_5.append("Fragment ");
                                                var6_5.append(var1_1);
                                                var6_5.append(" declared target fragment ");
                                                var6_5.append(var1_1.mTarget);
                                                var6_5.append(" that does not belong to this FragmentManager!");
                                                throw new IllegalStateException(var6_5.toString());
                                            }
                                            if (var1_1.mTarget.mState < 1) {
                                                this.moveToState(var1_1.mTarget, 1);
                                            }
                                            var1_1.mTargetWho = var1_1.mTarget.mWho;
                                            var1_1.mTarget = null;
                                        }
                                        if (var1_1.mTargetWho != null) {
                                            var7_3 = this.findActiveFragment(var1_1.mTargetWho);
                                            if (var7_3 == null) {
                                                var6_5 = new StringBuilder();
                                                var6_5.append("Fragment ");
                                                var6_5.append(var1_1);
                                                var6_5.append(" declared target fragment ");
                                                var6_5.append(var1_1.mTargetWho);
                                                var6_5.append(" that does not belong to this FragmentManager!");
                                                throw new IllegalStateException(var6_5.toString());
                                            }
                                            if (var7_3.mState < 1) {
                                                this.moveToState((Fragment)var7_3, 1);
                                            }
                                        }
                                        var6_5.attach(this.mHost, this, this.mParent);
                                    }
                                }
                                if (var3_6 > 0) {
                                    var6_5.create();
                                }
                            }
                            if (var3_6 > -1) {
                                var6_5.ensureInflatedView();
                            }
                            if (var3_6 > 1) {
                                var6_5.createView(this.mContainer);
                                var6_5.activityCreated();
                                var6_5.restoreViewState();
                            }
                        }
                        if (var3_6 > 2) {
                            var6_5.start();
                        }
                    }
                    if (var3_6 > 3) {
                        var6_5.resume();
                    }
                }
                var4_7 = var3_6;
                break block47;
            }
            var4_7 = var3_6;
            if (var1_1.mState <= var3_6) break block47;
            var4_7 = var1_1.mState;
            var2_2 = var3_6;
            if (var4_7 == 0) ** GOTO lbl133
            if (var4_7 == 1) ** GOTO lbl119
            if (var4_7 == 2) ** GOTO lbl84
            if (var4_7 == 3) ** GOTO lbl82
            if (var4_7 != 4) {
                var4_7 = var3_6;
            } else {
                if (var3_6 < 4) {
                    var6_5.pause();
                }
lbl82: // 4 sources:
                if (var3_6 < 3) {
                    var6_5.stop();
                }
lbl84: // 4 sources:
                if (var3_6 < 2) {
                    if (FragmentManager.isLoggingEnabled(3)) {
                        var7_3 = new StringBuilder();
                        var7_3.append("movefrom ACTIVITY_CREATED: ");
                        var7_3.append(var1_1);
                        Log.d((String)"FragmentManager", (String)var7_3.toString());
                    }
                    if (var1_1.mView != null && this.mHost.onShouldSaveFragmentState(var1_1) && var1_1.mSavedViewState == null) {
                        var6_5.saveViewState();
                    }
                    var8_8 = null;
                    if (var1_1.mView != null && var1_1.mContainer != null) {
                        var1_1.mContainer.endViewTransition(var1_1.mView);
                        var1_1.mView.clearAnimation();
                        if (!var1_1.isRemovingParent()) {
                            var7_3 = var8_8;
                            if (this.mCurState > -1) {
                                var7_3 = var8_8;
                                if (!this.mDestroyed) {
                                    var7_3 = var8_8;
                                    if (var1_1.mView.getVisibility() == 0) {
                                        var7_3 = var8_8;
                                        if (var1_1.mPostponedAlpha >= 0.0f) {
                                            var7_3 = FragmentAnim.loadAnimation(this.mHost.getContext(), this.mContainer, var1_1, false);
                                        }
                                    }
                                }
                            }
                            var1_1.mPostponedAlpha = 0.0f;
                            var8_8 = var1_1.mContainer;
                            var9_9 = var1_1.mView;
                            if (var7_3 != null) {
                                var1_1.setStateAfterAnimating(var3_6);
                                FragmentAnim.animateRemoveFragment(var1_1, (FragmentAnim.AnimationOrAnimator)var7_3, this.mFragmentTransitionCallback);
                            }
                            var8_8.removeView(var9_9);
                            if (var8_8 != var1_1.mContainer) {
                                return;
                            }
                        }
                    }
                    if (this.mExitAnimationCancellationSignals.get(var1_1) == null) {
                        this.destroyFragmentView(var1_1);
                    } else {
                        var1_1.setStateAfterAnimating(var3_6);
                    }
                }
lbl119: // 5 sources:
                var2_2 = var3_6;
                if (var3_6 < 1) {
                    var2_2 = var1_1.mRemoving != false && var1_1.isInBackStack() == false ? var5_4 : 0;
                    if (var2_2 == 0 && !this.mNonConfig.shouldDestroy(var1_1)) {
                        if (var1_1.mTargetWho != null && (var7_3 = this.findActiveFragment(var1_1.mTargetWho)) != null && var7_3.getRetainInstance()) {
                            var1_1.mTarget = var7_3;
                        }
                    } else {
                        this.makeInactive((FragmentStateManager)var6_5);
                    }
                    if (this.mExitAnimationCancellationSignals.get(var1_1) != null) {
                        var1_1.setStateAfterAnimating(var3_6);
                        var2_2 = 1;
                    } else {
                        var6_5.destroy(this.mHost, this.mNonConfig);
                        var2_2 = var3_6;
                    }
                }
lbl133: // 5 sources:
                var4_7 = var2_2;
                if (var2_2 < 0) {
                    var6_5.detach(this.mNonConfig);
                    var4_7 = var2_2;
                }
            }
        }
        if (var1_1.mState == var4_7) return;
        if (FragmentManager.isLoggingEnabled(3)) {
            var6_5 = new StringBuilder();
            var6_5.append("moveToState: Fragment state for ");
            var6_5.append(var1_1);
            var6_5.append(" not updated inline; expected state ");
            var6_5.append(var4_7);
            var6_5.append(" found ");
            var6_5.append(var1_1.mState);
            Log.d((String)"FragmentManager", (String)var6_5.toString());
        }
        var1_1.mState = var4_7;
    }

    void noteStateNotSaved() {
        this.mStateSaved = false;
        this.mStopped = false;
        for (Fragment fragment : this.mFragmentStore.getFragments()) {
            if (fragment == null) continue;
            fragment.noteStateNotSaved();
        }
    }

    @Deprecated
    public FragmentTransaction openTransaction() {
        return this.beginTransaction();
    }

    void performPendingDeferredStart(Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            this.moveToState(fragment, this.mCurState);
        }
    }

    public void popBackStack() {
        this.enqueueAction(new PopBackStackState(null, -1, 0), false);
    }

    public void popBackStack(int n, int n2) {
        if (n >= 0) {
            this.enqueueAction(new PopBackStackState(null, n, n2), false);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void popBackStack(String string2, int n) {
        this.enqueueAction(new PopBackStackState(string2, -1, n), false);
    }

    public boolean popBackStackImmediate() {
        return this.popBackStackImmediate(null, -1, 0);
    }

    public boolean popBackStackImmediate(int n, int n2) {
        if (n >= 0) {
            return this.popBackStackImmediate(null, n, n2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public boolean popBackStackImmediate(String string2, int n) {
        return this.popBackStackImmediate(string2, -1, n);
    }

    boolean popBackStackState(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, String string2, int n, int n2) {
        ArrayList<BackStackRecord> arrayList3 = this.mBackStack;
        if (arrayList3 == null) {
            return false;
        }
        if (string2 == null && n < 0 && (n2 & 1) == 0) {
            n = arrayList3.size() - 1;
            if (n < 0) {
                return false;
            }
            arrayList.add(this.mBackStack.remove(n));
            arrayList2.add(true);
            return true;
        }
        int n3 = -1;
        if (string2 != null || n >= 0) {
            int n4;
            for (n4 = this.mBackStack.size() - 1; n4 >= 0; --n4) {
                arrayList3 = this.mBackStack.get(n4);
                if (string2 != null && string2.equals(arrayList3.getName()) || n >= 0 && n == arrayList3.mIndex) break;
            }
            if (n4 < 0) {
                return false;
            }
            n3 = n4;
            if ((n2 & 1) != 0) {
                n2 = n4 - 1;
                do {
                    n3 = n2;
                    if (n2 < 0) break;
                    arrayList3 = this.mBackStack.get(n2);
                    if (string2 == null || !string2.equals(arrayList3.getName())) {
                        n3 = n2;
                        if (n < 0) break;
                        n3 = n2;
                        if (n != arrayList3.mIndex) break;
                    }
                    --n2;
                } while (true);
            }
        }
        if (n3 == this.mBackStack.size() - 1) {
            return false;
        }
        for (n = this.mBackStack.size() - 1; n > n3; --n) {
            arrayList.add(this.mBackStack.remove(n));
            arrayList2.add(true);
        }
        return true;
    }

    public void putFragment(Bundle bundle, String string2, Fragment fragment) {
        if (fragment.mFragmentManager != this) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        bundle.putString(string2, fragment.mWho);
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean bl) {
        this.mLifecycleCallbacksDispatcher.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, bl);
    }

    void removeCancellationSignal(Fragment fragment, CancellationSignal cancellationSignal) {
        HashSet<CancellationSignal> hashSet = this.mExitAnimationCancellationSignals.get(fragment);
        if (hashSet != null && hashSet.remove(cancellationSignal) && hashSet.isEmpty()) {
            this.mExitAnimationCancellationSignals.remove(fragment);
            if (fragment.mState < 3) {
                this.destroyFragmentView(fragment);
                this.moveToState(fragment, fragment.getStateAfterAnimating());
            }
        }
    }

    void removeFragment(Fragment fragment) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("remove: ");
            stringBuilder.append(fragment);
            stringBuilder.append(" nesting=");
            stringBuilder.append(fragment.mBackStackNesting);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        boolean bl = fragment.isInBackStack();
        if (!fragment.mDetached || bl ^ true) {
            this.mFragmentStore.removeFragment(fragment);
            if (this.isMenuAvailable(fragment)) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mRemoving = true;
            this.setVisibleRemovingFragment(fragment);
        }
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        ArrayList<OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(onBackStackChangedListener);
        }
    }

    void removeRetainedFragment(Fragment fragment) {
        if (this.isStateSaved()) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v((String)"FragmentManager", (String)"Ignoring removeRetainedFragment as the state is already saved");
            }
            return;
        }
        if (this.mNonConfig.removeRetainedFragment(fragment) && FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Updating retained Fragments: Removed ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
    }

    void restoreAllState(Parcelable parcelable, FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (this.mHost instanceof ViewModelStoreOwner) {
            this.throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner"));
        }
        this.mNonConfig.restoreFromSnapshot(fragmentManagerNonConfig);
        this.restoreSaveState(parcelable);
    }

    void restoreSaveState(Parcelable object) {
        Object object2;
        if (object == null) {
            return;
        }
        FragmentManagerState fragmentManagerState = (FragmentManagerState)object;
        if (fragmentManagerState.mActive == null) {
            return;
        }
        this.mFragmentStore.resetActiveFragments();
        Object object3 = fragmentManagerState.mActive.iterator();
        while (object3.hasNext()) {
            StringBuilder stringBuilder;
            object = object3.next();
            if (object == null) continue;
            object2 = this.mNonConfig.findRetainedFragmentByWho(object.mWho);
            if (object2 != null) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("restoreSaveState: re-attaching retained ");
                    stringBuilder.append(object2);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                object = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, (Fragment)object2, (FragmentState)object);
            } else {
                object = new FragmentStateManager(this.mLifecycleCallbacksDispatcher, this.mHost.getContext().getClassLoader(), this.getFragmentFactory(), (FragmentState)object);
            }
            object2 = object.getFragment();
            object2.mFragmentManager = this;
            if (FragmentManager.isLoggingEnabled(2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("restoreSaveState: active (");
                stringBuilder.append(object2.mWho);
                stringBuilder.append("): ");
                stringBuilder.append(object2);
                Log.v((String)"FragmentManager", (String)stringBuilder.toString());
            }
            object.restoreState(this.mHost.getContext().getClassLoader());
            this.mFragmentStore.makeActive((FragmentStateManager)object);
            object.setFragmentManagerState(this.mCurState);
        }
        object = this.mNonConfig.getRetainedFragments().iterator();
        while (object.hasNext()) {
            object3 = (Fragment)object.next();
            if (this.mFragmentStore.containsActiveFragment(object3.mWho)) continue;
            if (FragmentManager.isLoggingEnabled(2)) {
                object2 = new StringBuilder();
                object2.append("Discarding retained Fragment ");
                object2.append(object3);
                object2.append(" that was not found in the set of active Fragments ");
                object2.append(fragmentManagerState.mActive);
                Log.v((String)"FragmentManager", (String)object2.toString());
            }
            this.moveToState((Fragment)object3, 1);
            object3.mRemoving = true;
            this.moveToState((Fragment)object3, -1);
        }
        this.mFragmentStore.restoreAddedFragments(fragmentManagerState.mAdded);
        if (fragmentManagerState.mBackStack != null) {
            this.mBackStack = new ArrayList(fragmentManagerState.mBackStack.length);
            for (int i = 0; i < fragmentManagerState.mBackStack.length; ++i) {
                object = fragmentManagerState.mBackStack[i].instantiate(this);
                if (FragmentManager.isLoggingEnabled(2)) {
                    object3 = new StringBuilder();
                    object3.append("restoreAllState: back stack #");
                    object3.append(i);
                    object3.append(" (index ");
                    object3.append(object.mIndex);
                    object3.append("): ");
                    object3.append(object);
                    Log.v((String)"FragmentManager", (String)object3.toString());
                    object3 = new PrintWriter(new LogWriter("FragmentManager"));
                    object.dump("  ", (PrintWriter)object3, false);
                    object3.close();
                }
                this.mBackStack.add((BackStackRecord)object);
            }
        } else {
            this.mBackStack = null;
        }
        this.mBackStackIndex.set(fragmentManagerState.mBackStackIndex);
        if (fragmentManagerState.mPrimaryNavActiveWho != null) {
            this.mPrimaryNav = object = this.findActiveFragment(fragmentManagerState.mPrimaryNavActiveWho);
            this.dispatchParentPrimaryNavigationFragmentChanged((Fragment)object);
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
        ArrayList<FragmentState> arrayList = this.mFragmentStore.saveActiveFragments();
        if (arrayList.isEmpty()) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v((String)"FragmentManager", (String)"saveAllState: no fragments!");
            }
            return null;
        }
        ArrayList<String> arrayList2 = this.mFragmentStore.saveAddedFragments();
        BackStackState[] arrbackStackState = null;
        ArrayList<BackStackRecord> arrayList3 = this.mBackStack;
        Object object = arrbackStackState;
        if (arrayList3 != null) {
            int n = arrayList3.size();
            object = arrbackStackState;
            if (n > 0) {
                arrbackStackState = new BackStackState[n];
                int n2 = 0;
                do {
                    object = arrbackStackState;
                    if (n2 >= n) break;
                    arrbackStackState[n2] = new BackStackState(this.mBackStack.get(n2));
                    if (FragmentManager.isLoggingEnabled(2)) {
                        object = new StringBuilder();
                        object.append("saveAllState: adding back stack #");
                        object.append(n2);
                        object.append(": ");
                        object.append(this.mBackStack.get(n2));
                        Log.v((String)"FragmentManager", (String)object.toString());
                    }
                    ++n2;
                } while (true);
            }
        }
        arrbackStackState = new BackStackState[]();
        arrbackStackState.mActive = arrayList;
        arrbackStackState.mAdded = arrayList2;
        arrbackStackState.mBackStack = object;
        arrbackStackState.mBackStackIndex = this.mBackStackIndex.get();
        object = this.mPrimaryNav;
        if (object != null) {
            arrbackStackState.mPrimaryNavActiveWho = object.mWho;
        }
        return arrbackStackState;
    }

    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        FragmentStateManager fragmentStateManager = this.mFragmentStore.getFragmentStateManager(fragment.mWho);
        if (fragmentStateManager == null || !fragmentStateManager.getFragment().equals(fragment)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        return fragmentStateManager.saveInstanceState();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void scheduleCommit() {
        ArrayList<OpGenerator> arrayList = this.mPendingActions;
        synchronized (arrayList) {
            block5 : {
                block4 : {
                    ArrayList<StartEnterTransitionListener> arrayList2 = this.mPostponedTransactions;
                    boolean bl = false;
                    boolean bl2 = arrayList2 != null && !this.mPostponedTransactions.isEmpty();
                    if (this.mPendingActions.size() != 1) break block4;
                    bl = true;
                    if (!bl2 && !bl) break block5;
                }
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
                this.updateOnBackPressedCallbackEnabled();
            }
            return;
        }
    }

    void setExitAnimationOrder(Fragment fragment, boolean bl) {
        if ((fragment = this.getFragmentContainer(fragment)) != null && fragment instanceof FragmentContainerView) {
            ((FragmentContainerView)((Object)fragment)).setDrawDisappearingViewsLast(bl ^ true);
        }
    }

    public void setFragmentFactory(FragmentFactory fragmentFactory) {
        this.mFragmentFactory = fragmentFactory;
    }

    void setMaxLifecycle(Fragment fragment, Lifecycle.State object) {
        if (fragment.equals(this.findActiveFragment(fragment.mWho)) && (fragment.mHost == null || fragment.mFragmentManager == this)) {
            fragment.mMaxState = object;
            return;
        }
        object = new StringBuilder();
        object.append("Fragment ");
        object.append(fragment);
        object.append(" is not an active fragment of FragmentManager ");
        object.append(this);
        throw new IllegalArgumentException(object.toString());
    }

    void setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment != null && (!fragment.equals(this.findActiveFragment(fragment.mWho)) || fragment.mHost != null && fragment.mFragmentManager != this)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not an active fragment of FragmentManager ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        Fragment fragment2 = this.mPrimaryNav;
        this.mPrimaryNav = fragment;
        this.dispatchParentPrimaryNavigationFragmentChanged(fragment2);
        this.dispatchParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
    }

    void showFragment(Fragment fragment) {
        if (FragmentManager.isLoggingEnabled(2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("show: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged ^= true;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("FragmentManager{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            stringBuilder.append(fragment.getClass().getSimpleName());
            stringBuilder.append("{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this.mParent)));
            stringBuilder.append("}");
        } else {
            stringBuilder.append(this.mHost.getClass().getSimpleName());
            stringBuilder.append("{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this.mHost)));
            stringBuilder.append("}");
        }
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        this.mLifecycleCallbacksDispatcher.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks);
    }

    public static interface BackStackEntry {
        @Deprecated
        public CharSequence getBreadCrumbShortTitle();

        @Deprecated
        public int getBreadCrumbShortTitleRes();

        @Deprecated
        public CharSequence getBreadCrumbTitle();

        @Deprecated
        public int getBreadCrumbTitleRes();

        public int getId();

        public String getName();
    }

    public static abstract class FragmentLifecycleCallbacks {
        public void onFragmentActivityCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentAttached(FragmentManager fragmentManager, Fragment fragment, Context context) {
        }

        public void onFragmentCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentDestroyed(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentDetached(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentPaused(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentPreAttached(FragmentManager fragmentManager, Fragment fragment, Context context) {
        }

        public void onFragmentPreCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentResumed(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentSaveInstanceState(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void onFragmentStarted(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentStopped(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void onFragmentViewCreated(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
        }

        public void onFragmentViewDestroyed(FragmentManager fragmentManager, Fragment fragment) {
        }
    }

    public static interface OnBackStackChangedListener {
        public void onBackStackChanged();
    }

    static interface OpGenerator {
        public boolean generateOps(ArrayList<BackStackRecord> var1, ArrayList<Boolean> var2);
    }

    private class PopBackStackState
    implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String string2, int n, int n2) {
            this.mName = string2;
            this.mId = n;
            this.mFlags = n2;
        }

        @Override
        public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
            if (FragmentManager.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && FragmentManager.this.mPrimaryNav.getChildFragmentManager().popBackStackImmediate()) {
                return false;
            }
            return FragmentManager.this.popBackStackState(arrayList, arrayList2, this.mName, this.mId, this.mFlags);
        }
    }

    static class StartEnterTransitionListener
    implements Fragment.OnStartEnterTransitionListener {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord backStackRecord, boolean bl) {
            this.mIsBack = bl;
            this.mRecord = backStackRecord;
        }

        void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }

        void completeTransaction() {
            int n = this.mNumPostponed;
            boolean bl = false;
            n = n > 0 ? 1 : 0;
            for (Fragment object2 : this.mRecord.mManager.getFragments()) {
                object2.setOnStartEnterTransitionListener(null);
                if (n == 0 || !object2.isPostponed()) continue;
                object2.startPostponedEnterTransition();
            }
            FragmentManager fragmentManager = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean bl2 = this.mIsBack;
            if (n == 0) {
                bl = true;
            }
            fragmentManager.completeExecute(backStackRecord, bl2, bl, true);
        }

        public boolean isReady() {
            if (this.mNumPostponed == 0) {
                return true;
            }
            return false;
        }

        @Override
        public void onStartEnterTransition() {
            int n;
            this.mNumPostponed = n = this.mNumPostponed - 1;
            if (n != 0) {
                return;
            }
            this.mRecord.mManager.scheduleCommit();
        }

        @Override
        public void startListening() {
            ++this.mNumPostponed;
        }
    }

}

