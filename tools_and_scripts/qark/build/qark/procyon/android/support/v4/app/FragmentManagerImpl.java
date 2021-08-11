// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.support.annotation.CallSuper;
import android.support.v4.util.DebugUtils;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.animation.AnimatorInflater;
import android.content.res.Resources$NotFoundException;
import android.view.animation.AnimationUtils;
import java.util.Collections;
import java.util.Arrays;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;
import java.io.FileDescriptor;
import java.io.Writer;
import java.io.PrintWriter;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import java.util.Iterator;
import android.graphics.Paint;
import java.util.List;
import android.animation.PropertyValuesHolder;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.ScaleAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AlphaAnimation;
import android.content.Context;
import android.util.Log;
import java.util.Collection;
import android.os.Looper;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.view.ViewGroup;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import android.util.SparseArray;
import java.lang.reflect.Field;
import android.view.animation.Interpolator;
import android.view.LayoutInflater$Factory2;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater$Factory2
{
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    SparseArray<Fragment> mActive;
    final ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks;
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;
    
    static {
        FragmentManagerImpl.DEBUG = false;
        FragmentManagerImpl.sAnimationListenerField = null;
        DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5f);
    }
    
    FragmentManagerImpl() {
        this.mNextFragmentIndex = 0;
        this.mAdded = new ArrayList<Fragment>();
        this.mLifecycleCallbacks = new CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>>();
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }
    
    private void addAddedFragments(final ArraySet<Fragment> set) {
        final int mCurState = this.mCurState;
        if (mCurState < 1) {
            return;
        }
        final int min = Math.min(mCurState, 4);
        for (int size = this.mAdded.size(), i = 0; i < size; ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment.mState < min) {
                this.moveToState(fragment, min, fragment.getNextAnim(), fragment.getNextTransition(), false);
                if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                    set.add(fragment);
                }
            }
        }
    }
    
    private void animateRemoveFragment(@NonNull final Fragment fragment, @NonNull final AnimationOrAnimator animationOrAnimator, final int stateAfterAnimating) {
        final View mView = fragment.mView;
        fragment.setStateAfterAnimating(stateAfterAnimating);
        if (animationOrAnimator.animation != null) {
            final Animation animation = animationOrAnimator.animation;
            fragment.setAnimatingAway(fragment.mView);
            animation.setAnimationListener((Animation$AnimationListener)new AnimationListenerWrapper(getAnimationListener(animation)) {
                @Override
                public void onAnimationEnd(final Animation animation) {
                    super.onAnimationEnd(animation);
                    if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        final FragmentManagerImpl this$0 = FragmentManagerImpl.this;
                        final Fragment val$fragment = fragment;
                        this$0.moveToState(val$fragment, val$fragment.getStateAfterAnimating(), 0, 0, false);
                    }
                }
            });
            setHWLayerAnimListenerIfAlpha(mView, animationOrAnimator);
            fragment.mView.startAnimation(animation);
            return;
        }
        final Animator animator = animationOrAnimator.animator;
        fragment.setAnimator(animationOrAnimator.animator);
        final ViewGroup mContainer = fragment.mContainer;
        if (mContainer != null) {
            mContainer.startViewTransition(mView);
        }
        animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                final ViewGroup val$container = mContainer;
                if (val$container != null) {
                    val$container.endViewTransition(mView);
                }
                if (fragment.getAnimator() != null) {
                    fragment.setAnimator(null);
                    final FragmentManagerImpl this$0 = FragmentManagerImpl.this;
                    final Fragment val$fragment = fragment;
                    this$0.moveToState(val$fragment, val$fragment.getStateAfterAnimating(), 0, 0, false);
                }
            }
        });
        animator.setTarget((Object)fragment.mView);
        setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
        animator.start();
    }
    
    private void burpActive() {
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null) {
            for (int i = mActive.size() - 1; i >= 0; --i) {
                if (this.mActive.valueAt(i) == null) {
                    final SparseArray<Fragment> mActive2 = this.mActive;
                    mActive2.delete(mActive2.keyAt(i));
                }
            }
        }
    }
    
    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (this.mNoTransactionsBecause == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Can not perform this action inside of ");
        sb.append(this.mNoTransactionsBecause);
        throw new IllegalStateException(sb.toString());
    }
    
    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }
    
    private void completeExecute(final BackStackRecord backStackRecord, final boolean b, final boolean b2, final boolean b3) {
        if (b) {
            backStackRecord.executePopOps(b3);
        }
        else {
            backStackRecord.executeOps();
        }
        final ArrayList<BackStackRecord> list = new ArrayList<BackStackRecord>(1);
        final ArrayList<Boolean> list2 = new ArrayList<Boolean>(1);
        list.add(backStackRecord);
        list2.add(b);
        if (b2) {
            FragmentTransition.startTransitions(this, list, list2, 0, 1, true);
        }
        if (b3) {
            this.moveToState(this.mCurState, true);
        }
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null) {
            for (int size = mActive.size(), i = 0; i < size; ++i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded) {
                    if (backStackRecord.interactsWith(fragment.mContainerId)) {
                        if (fragment.mPostponedAlpha > 0.0f) {
                            fragment.mView.setAlpha(fragment.mPostponedAlpha);
                        }
                        if (b3) {
                            fragment.mPostponedAlpha = 0.0f;
                        }
                        else {
                            fragment.mPostponedAlpha = -1.0f;
                            fragment.mIsNewlyAdded = false;
                        }
                    }
                }
            }
        }
    }
    
    private void dispatchStateChange(final int n) {
        try {
            this.mExecutingActions = true;
            this.moveToState(n, false);
            this.mExecutingActions = false;
            this.execPendingActions();
        }
        finally {
            this.mExecutingActions = false;
        }
    }
    
    private void endAnimatingAwayFragments() {
        final SparseArray<Fragment> mActive = this.mActive;
        int size;
        if (mActive == null) {
            size = 0;
        }
        else {
            size = mActive.size();
        }
        for (int i = 0; i < size; ++i) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null) {
                if (fragment.getAnimatingAway() != null) {
                    final int stateAfterAnimating = fragment.getStateAfterAnimating();
                    final View animatingAway = fragment.getAnimatingAway();
                    fragment.setAnimatingAway(null);
                    final Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    this.moveToState(fragment, stateAfterAnimating, 0, 0, false);
                }
                else if (fragment.getAnimator() != null) {
                    fragment.getAnimator().end();
                }
            }
        }
    }
    
    private void ensureExecReady(final boolean b) {
        if (!this.mExecutingActions) {
            if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
                if (!b) {
                    this.checkStateLoss();
                }
                if (this.mTmpRecords == null) {
                    this.mTmpRecords = new ArrayList<BackStackRecord>();
                    this.mTmpIsPop = new ArrayList<Boolean>();
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
    
    private static void executeOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, int i, final int n) {
        while (i < n) {
            final BackStackRecord backStackRecord = list.get(i);
            final boolean booleanValue = list2.get(i);
            boolean b = true;
            if (booleanValue) {
                backStackRecord.bumpBackStackNesting(-1);
                if (i != n - 1) {
                    b = false;
                }
                backStackRecord.executePopOps(b);
            }
            else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            ++i;
        }
    }
    
    private void executeOpsTogether(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, int i, final int n) {
        final boolean mReorderingAllowed = list.get(i).mReorderingAllowed;
        final ArrayList<Fragment> mTmpAddedFragments = this.mTmpAddedFragments;
        if (mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList<Fragment>();
        }
        else {
            mTmpAddedFragments.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        Fragment fragment = this.getPrimaryNavigationFragment();
        int n2 = i;
        int n3 = 0;
        while (true) {
            final boolean b = true;
            if (n2 >= n) {
                break;
            }
            final BackStackRecord backStackRecord = list.get(n2);
            if (!list2.get(n2)) {
                fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
            }
            else {
                fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
            }
            boolean b2 = b;
            if (n3 == 0) {
                b2 = (backStackRecord.mAddToBackStack && b);
            }
            ++n2;
            n3 = (b2 ? 1 : 0);
        }
        this.mTmpAddedFragments.clear();
        if (!mReorderingAllowed) {
            FragmentTransition.startTransitions(this, list, list2, i, n, false);
        }
        executeOps(list, list2, i, n);
        int postponePostponableTransactions = n;
        if (mReorderingAllowed) {
            final ArraySet<Fragment> set = new ArraySet<Fragment>();
            this.addAddedFragments(set);
            postponePostponableTransactions = this.postponePostponableTransactions(list, list2, i, n, set);
            this.makeRemovedFragmentsInvisible(set);
        }
        if (postponePostponableTransactions != i && mReorderingAllowed) {
            FragmentTransition.startTransitions(this, list, list2, i, postponePostponableTransactions, true);
            this.moveToState(this.mCurState, true);
        }
        while (i < n) {
            final BackStackRecord backStackRecord2 = list.get(i);
            if (list2.get(i) && backStackRecord2.mIndex >= 0) {
                this.freeBackStackIndex(backStackRecord2.mIndex);
                backStackRecord2.mIndex = -1;
            }
            backStackRecord2.runOnCommitRunnables();
            ++i;
        }
        if (n3 != 0) {
            this.reportBackStackChanged();
        }
    }
    
    private void executePostponedTransaction(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        final ArrayList<StartEnterTransitionListener> mPostponedTransactions = this.mPostponedTransactions;
        int size;
        if (mPostponedTransactions == null) {
            size = 0;
        }
        else {
            size = mPostponedTransactions.size();
        }
        for (int i = 0; i < size; ++i) {
            final StartEnterTransitionListener startEnterTransitionListener = this.mPostponedTransactions.get(i);
            if (list != null && !startEnterTransitionListener.mIsBack) {
                final int index = list.indexOf(startEnterTransitionListener.mRecord);
                if (index != -1 && list2.get(index)) {
                    startEnterTransitionListener.cancelTransaction();
                    continue;
                }
            }
            if (!startEnterTransitionListener.isReady()) {
                if (list == null) {
                    continue;
                }
                if (!startEnterTransitionListener.mRecord.interactsWith(list, 0, list.size())) {
                    continue;
                }
            }
            this.mPostponedTransactions.remove(i);
            --i;
            --size;
            if (list != null && !startEnterTransitionListener.mIsBack) {
                final int index2 = list.indexOf(startEnterTransitionListener.mRecord);
                if (index2 != -1) {
                    if (list2.get(index2)) {
                        startEnterTransitionListener.cancelTransaction();
                        continue;
                    }
                }
            }
            startEnterTransitionListener.completeTransaction();
        }
    }
    
    private Fragment findFragmentUnder(Fragment fragment) {
        final ViewGroup mContainer = fragment.mContainer;
        final View mView = fragment.mView;
        if (mContainer == null) {
            return null;
        }
        if (mView == null) {
            return null;
        }
        for (int i = this.mAdded.indexOf(fragment) - 1; i >= 0; --i) {
            fragment = this.mAdded.get(i);
            if (fragment.mContainer == mContainer && fragment.mView != null) {
                return fragment;
            }
        }
        return null;
    }
    
    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }
    
    private boolean generateOpsForPendingActions(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        boolean b = false;
        synchronized (this) {
            if (this.mPendingActions != null && this.mPendingActions.size() != 0) {
                for (int size = this.mPendingActions.size(), i = 0; i < size; ++i) {
                    b |= this.mPendingActions.get(i).generateOps(list, list2);
                }
                this.mPendingActions.clear();
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                return b;
            }
            return false;
        }
    }
    
    private static Animation$AnimationListener getAnimationListener(final Animation animation) {
        while (true) {
            while (true) {
                Label_0067: {
                    try {
                        if (FragmentManagerImpl.sAnimationListenerField == null) {
                            (FragmentManagerImpl.sAnimationListenerField = Animation.class.getDeclaredField("mListener")).setAccessible(true);
                            return (Animation$AnimationListener)FragmentManagerImpl.sAnimationListenerField.get(animation);
                        }
                        break Label_0067;
                    }
                    catch (IllegalAccessException ex) {
                        Log.e("FragmentManager", "Cannot access Animation's mListener field", (Throwable)ex);
                        return null;
                    }
                    catch (NoSuchFieldException ex2) {
                        Log.e("FragmentManager", "No field with the name mListener is found in Animation class", (Throwable)ex2);
                        return null;
                    }
                }
                continue;
            }
        }
    }
    
    static AnimationOrAnimator makeFadeAnimation(final Context context, final float n, final float n2) {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n, n2);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        return new AnimationOrAnimator((Animation)alphaAnimation);
    }
    
    static AnimationOrAnimator makeOpenCloseAnimation(final Context context, final float n, final float n2, final float n3, final float n4) {
        final AnimationSet set = new AnimationSet(false);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(n, n2, n, n2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        set.addAnimation((Animation)scaleAnimation);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n3, n4);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        set.addAnimation((Animation)alphaAnimation);
        return new AnimationOrAnimator((Animation)set);
    }
    
    private void makeRemovedFragmentsInvisible(final ArraySet<Fragment> set) {
        for (int size = set.size(), i = 0; i < size; ++i) {
            final Fragment fragment = set.valueAt(i);
            if (!fragment.mAdded) {
                final View view = fragment.getView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }
    
    static boolean modifiesAlpha(final Animator animator) {
        if (animator == null) {
            return false;
        }
        if (animator instanceof ValueAnimator) {
            final PropertyValuesHolder[] values = ((ValueAnimator)animator).getValues();
            for (int i = 0; i < values.length; ++i) {
                if ("alpha".equals(values[i].getPropertyName())) {
                    return true;
                }
            }
        }
        else if (animator instanceof AnimatorSet) {
            final ArrayList childAnimations = ((AnimatorSet)animator).getChildAnimations();
            for (int j = 0; j < childAnimations.size(); ++j) {
                if (modifiesAlpha((Animator)childAnimations.get(j))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    static boolean modifiesAlpha(final AnimationOrAnimator animationOrAnimator) {
        if (animationOrAnimator.animation instanceof AlphaAnimation) {
            return true;
        }
        if (animationOrAnimator.animation instanceof AnimationSet) {
            final List animations = ((AnimationSet)animationOrAnimator.animation).getAnimations();
            for (int i = 0; i < animations.size(); ++i) {
                if (animations.get(i) instanceof AlphaAnimation) {
                    return true;
                }
            }
            return false;
        }
        return modifiesAlpha(animationOrAnimator.animator);
    }
    
    private boolean popBackStackImmediate(final String s, final int n, final int n2) {
        this.execPendingActions();
        this.ensureExecReady(true);
        final Fragment mPrimaryNav = this.mPrimaryNav;
        if (mPrimaryNav != null && n < 0 && s == null) {
            final FragmentManager peekChildFragmentManager = mPrimaryNav.peekChildFragmentManager();
            if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                return true;
            }
        }
        final boolean popBackStackState = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, s, n, n2);
        if (popBackStackState) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return popBackStackState;
    }
    
    private int postponePostponableTransactions(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n, final int n2, final ArraySet<Fragment> set) {
        int n3 = n2;
        for (int i = n2 - 1; i >= n; --i) {
            final BackStackRecord backStackRecord = list.get(i);
            final boolean booleanValue = list2.get(i);
            if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(list, i + 1, n2)) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<StartEnterTransitionListener>();
                }
                final StartEnterTransitionListener onStartPostponedListener = new StartEnterTransitionListener(backStackRecord, booleanValue);
                this.mPostponedTransactions.add(onStartPostponedListener);
                backStackRecord.setOnStartPostponedListener(onStartPostponedListener);
                if (booleanValue) {
                    backStackRecord.executeOps();
                }
                else {
                    backStackRecord.executePopOps(false);
                }
                --n3;
                if (i != n3) {
                    list.remove(i);
                    list.add(n3, backStackRecord);
                }
                this.addAddedFragments(set);
            }
        }
        return n3;
    }
    
    private void removeRedundantOperationsAndExecute(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        if (list == null) {
            return;
        }
        if (list.isEmpty()) {
            return;
        }
        if (list2 == null || list.size() != list2.size()) {
            throw new IllegalStateException("Internal error with the back stack records");
        }
        this.executePostponedTransaction(list, list2);
        final int size = list.size();
        int n = 0;
        for (int i = 0; i < size; ++i) {
            if (!list.get(i).mReorderingAllowed) {
                if (n != i) {
                    this.executeOpsTogether(list, list2, n, i);
                }
                int j = i + 1;
                if (list2.get(i)) {
                    while (j < size) {
                        if (!list2.get(j)) {
                            break;
                        }
                        if (list.get(j).mReorderingAllowed) {
                            break;
                        }
                        ++j;
                    }
                }
                this.executeOpsTogether(list, list2, i, j);
                final int n2 = j;
                final int n3 = j - 1;
                n = n2;
                i = n3;
            }
        }
        if (n != size) {
            this.executeOpsTogether(list, list2, n, size);
        }
    }
    
    public static int reverseTransit(final int n) {
        if (n == 4097) {
            return 8194;
        }
        if (n == 4099) {
            return 4099;
        }
        if (n != 8194) {
            return 0;
        }
        return 4097;
    }
    
    private void scheduleCommit() {
    Label_0051_Outer:
        while (true) {
            while (true) {
                boolean b = false;
                boolean b2 = false;
            Label_0096:
                while (true) {
                    Label_0091: {
                        synchronized (this) {
                            final ArrayList<StartEnterTransitionListener> mPostponedTransactions = this.mPostponedTransactions;
                            b = false;
                            if (mPostponedTransactions == null || this.mPostponedTransactions.isEmpty()) {
                                break Label_0091;
                            }
                            b2 = true;
                            if (this.mPendingActions != null && this.mPendingActions.size() == 1) {
                                b = true;
                            }
                            break Label_0096;
                            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                            this.mHost.getHandler().post(this.mExecCommit);
                            return;
                        }
                    }
                    b2 = false;
                    continue Label_0051_Outer;
                }
                if (!b2 && !b) {
                    return;
                }
                continue;
            }
        }
    }
    
    private static void setHWLayerAnimListenerIfAlpha(final View view, final AnimationOrAnimator animationOrAnimator) {
        if (view == null) {
            return;
        }
        if (animationOrAnimator == null) {
            return;
        }
        if (!shouldRunOnHWLayer(view, animationOrAnimator)) {
            return;
        }
        if (animationOrAnimator.animator != null) {
            animationOrAnimator.animator.addListener((Animator$AnimatorListener)new AnimatorOnHWLayerIfNeededListener(view));
            return;
        }
        final Animation$AnimationListener animationListener = getAnimationListener(animationOrAnimator.animation);
        view.setLayerType(2, (Paint)null);
        animationOrAnimator.animation.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(view, animationListener));
    }
    
    private static void setRetaining(final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (fragmentManagerNonConfig == null) {
            return;
        }
        final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
        if (fragments != null) {
            final Iterator<Fragment> iterator = fragments.iterator();
            while (iterator.hasNext()) {
                iterator.next().mRetaining = true;
            }
        }
        final List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
        if (childNonConfigs != null) {
            final Iterator<FragmentManagerNonConfig> iterator2 = childNonConfigs.iterator();
            while (iterator2.hasNext()) {
                setRetaining(iterator2.next());
            }
        }
    }
    
    static boolean shouldRunOnHWLayer(final View view, final AnimationOrAnimator animationOrAnimator) {
        if (view == null) {
            return false;
        }
        if (animationOrAnimator == null) {
            return false;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            if (view.getLayerType() == 0) {
                if (ViewCompat.hasOverlappingRendering(view)) {
                    if (modifiesAlpha(animationOrAnimator)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void throwException(final RuntimeException ex) {
        Log.e("FragmentManager", ex.getMessage());
        Log.e("FragmentManager", "Activity state:");
        final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        final FragmentHostCallback mHost = this.mHost;
        if (mHost != null) {
            try {
                mHost.onDump("  ", null, printWriter, new String[0]);
            }
            catch (Exception ex2) {
                Log.e("FragmentManager", "Failed dumping state", (Throwable)ex2);
            }
        }
        else {
            try {
                this.dump("  ", null, printWriter, new String[0]);
            }
            catch (Exception ex3) {
                Log.e("FragmentManager", "Failed dumping state", (Throwable)ex3);
            }
        }
        throw ex;
    }
    
    public static int transitToStyleIndex(int n, final boolean b) {
        if (n == 4097) {
            if (b) {
                n = 1;
            }
            else {
                n = 2;
            }
            return n;
        }
        if (n == 4099) {
            if (b) {
                n = 5;
            }
            else {
                n = 6;
            }
            return n;
        }
        if (n != 8194) {
            return -1;
        }
        if (b) {
            n = 3;
        }
        else {
            n = 4;
        }
        return n;
    }
    
    void addBackStackState(final BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<BackStackRecord>();
        }
        this.mBackStack.add(backStackRecord);
    }
    
    public void addFragment(final Fragment fragment, final boolean b) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("add: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        this.makeActive(fragment);
        if (!fragment.mDetached) {
            if (!this.mAdded.contains(fragment)) {
                synchronized (this.mAdded) {
                    this.mAdded.add(fragment);
                    // monitorexit(this.mAdded)
                    fragment.mAdded = true;
                    fragment.mRemoving = false;
                    if (fragment.mView == null) {
                        fragment.mHiddenChanged = false;
                    }
                    if (fragment.mHasMenu && fragment.mMenuVisible) {
                        this.mNeedMenuInvalidate = true;
                    }
                    if (b) {
                        this.moveToState(fragment);
                    }
                    return;
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Fragment already added: ");
            sb2.append(fragment);
            throw new IllegalStateException(sb2.toString());
        }
    }
    
    @Override
    public void addOnBackStackChangedListener(final OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<OnBackStackChangedListener>();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }
    
    public int allocBackStackIndex(final BackStackRecord backStackRecord) {
        while (true) {
            while (true) {
                Label_0210: {
                    synchronized (this) {
                        if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                            final int intValue = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Adding back stack index ");
                                sb.append(intValue);
                                sb.append(" with ");
                                sb.append(backStackRecord);
                                Log.v("FragmentManager", sb.toString());
                            }
                            this.mBackStackIndices.set(intValue, backStackRecord);
                            return intValue;
                        }
                        if (this.mBackStackIndices == null) {
                            this.mBackStackIndices = new ArrayList<BackStackRecord>();
                            final int size = this.mBackStackIndices.size();
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Setting back stack index ");
                                sb2.append(size);
                                sb2.append(" to ");
                                sb2.append(backStackRecord);
                                Log.v("FragmentManager", sb2.toString());
                            }
                            this.mBackStackIndices.add(backStackRecord);
                            return size;
                        }
                        break Label_0210;
                    }
                }
                continue;
            }
        }
    }
    
    public void attachController(final FragmentHostCallback mHost, final FragmentContainer mContainer, final Fragment mParent) {
        if (this.mHost == null) {
            this.mHost = mHost;
            this.mContainer = mContainer;
            this.mParent = mParent;
            return;
        }
        throw new IllegalStateException("Already attached");
    }
    
    public void attachFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("attach: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        if (!fragment.mDetached) {
            return;
        }
        fragment.mDetached = false;
        if (!fragment.mAdded) {
            if (!this.mAdded.contains(fragment)) {
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("add from attach: ");
                    sb2.append(fragment);
                    Log.v("FragmentManager", sb2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.add(fragment);
                    // monitorexit(this.mAdded)
                    fragment.mAdded = true;
                    if (fragment.mHasMenu && fragment.mMenuVisible) {
                        this.mNeedMenuInvalidate = true;
                    }
                    return;
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Fragment already added: ");
            sb3.append(fragment);
            throw new IllegalStateException(sb3.toString());
        }
    }
    
    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }
    
    void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            final AnimationOrAnimator loadAnimation = this.loadAnimation(fragment, fragment.getNextTransition(), fragment.mHidden ^ true, fragment.getNextTransitionStyle());
            if (loadAnimation != null && loadAnimation.animator != null) {
                loadAnimation.animator.setTarget((Object)fragment.mView);
                if (fragment.mHidden) {
                    if (fragment.isHideReplaced()) {
                        fragment.setHideReplaced(false);
                    }
                    else {
                        final ViewGroup mContainer = fragment.mContainer;
                        final View mView = fragment.mView;
                        mContainer.startViewTransition(mView);
                        loadAnimation.animator.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                mContainer.endViewTransition(mView);
                                animator.removeListener((Animator$AnimatorListener)this);
                                if (fragment.mView != null) {
                                    fragment.mView.setVisibility(8);
                                }
                            }
                        });
                    }
                }
                else {
                    fragment.mView.setVisibility(0);
                }
                setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                loadAnimation.animator.start();
            }
            else {
                if (loadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    fragment.mView.startAnimation(loadAnimation.animation);
                    loadAnimation.animation.start();
                }
                int visibility;
                if (fragment.mHidden && !fragment.isHideReplaced()) {
                    visibility = 8;
                }
                else {
                    visibility = 0;
                }
                fragment.mView.setVisibility(visibility);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }
    
    public void detachFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("detach: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("remove from detach: ");
                    sb2.append(fragment);
                    Log.v("FragmentManager", sb2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(fragment);
                    // monitorexit(this.mAdded)
                    if (fragment.mHasMenu && fragment.mMenuVisible) {
                        this.mNeedMenuInvalidate = true;
                    }
                    fragment.mAdded = false;
                }
            }
        }
    }
    
    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.dispatchStateChange(2);
    }
    
    public void dispatchConfigurationChanged(final Configuration configuration) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performConfigurationChanged(configuration);
            }
        }
    }
    
    public boolean dispatchContextItemSelected(final MenuItem menuItem) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    public void dispatchCreate() {
        this.mStateSaved = false;
        this.dispatchStateChange(1);
    }
    
    public boolean dispatchCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        boolean b = false;
        ArrayList<Fragment> mCreatedMenus = null;
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                if (fragment.performCreateOptionsMenu(menu, menuInflater)) {
                    b = true;
                    if (mCreatedMenus == null) {
                        mCreatedMenus = new ArrayList<Fragment>();
                    }
                    mCreatedMenus.add(fragment);
                }
            }
        }
        if (this.mCreatedMenus != null) {
            for (int j = 0; j < this.mCreatedMenus.size(); ++j) {
                final Fragment fragment2 = this.mCreatedMenus.get(j);
                if (mCreatedMenus == null || !mCreatedMenus.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = mCreatedMenus;
        return b;
    }
    
    public void dispatchDestroy() {
        this.mDestroyed = true;
        this.execPendingActions();
        this.dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }
    
    public void dispatchDestroyView() {
        this.dispatchStateChange(1);
    }
    
    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performLowMemory();
            }
        }
    }
    
    public void dispatchMultiWindowModeChanged(final boolean b) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performMultiWindowModeChanged(b);
            }
        }
    }
    
    void dispatchOnFragmentActivityCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(fragment, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentActivityCreated(this, fragment, bundle);
        }
    }
    
    void dispatchOnFragmentAttached(final Fragment fragment, final Context context, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(fragment, context, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentAttached(this, fragment, context);
        }
    }
    
    void dispatchOnFragmentCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(fragment, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentCreated(this, fragment, bundle);
        }
    }
    
    void dispatchOnFragmentDestroyed(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentDestroyed(this, fragment);
        }
    }
    
    void dispatchOnFragmentDetached(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentDetached(this, fragment);
        }
    }
    
    void dispatchOnFragmentPaused(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentPaused(this, fragment);
        }
    }
    
    void dispatchOnFragmentPreAttached(final Fragment fragment, final Context context, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(fragment, context, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentPreAttached(this, fragment, context);
        }
    }
    
    void dispatchOnFragmentPreCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreCreated(fragment, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentPreCreated(this, fragment, bundle);
        }
    }
    
    void dispatchOnFragmentResumed(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentResumed(this, fragment);
        }
    }
    
    void dispatchOnFragmentSaveInstanceState(final Fragment fragment, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentSaveInstanceState(this, fragment, bundle);
        }
    }
    
    void dispatchOnFragmentStarted(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentStarted(this, fragment);
        }
    }
    
    void dispatchOnFragmentStopped(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentStopped(this, fragment);
        }
    }
    
    void dispatchOnFragmentViewCreated(final Fragment fragment, final View view, final Bundle bundle, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentViewCreated(this, fragment, view, bundle);
        }
    }
    
    void dispatchOnFragmentViewDestroyed(final Fragment fragment, final boolean b) {
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            final FragmentManager fragmentManager = mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(fragment, true);
            }
        }
        for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (b && !pair.second) {
                continue;
            }
            pair.first.onFragmentViewDestroyed(this, fragment);
        }
    }
    
    public boolean dispatchOptionsItemSelected(final MenuItem menuItem) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }
    
    public void dispatchOptionsMenuClosed(final Menu menu) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performOptionsMenuClosed(menu);
            }
        }
    }
    
    public void dispatchPause() {
        this.dispatchStateChange(4);
    }
    
    public void dispatchPictureInPictureModeChanged(final boolean b) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performPictureInPictureModeChanged(b);
            }
        }
    }
    
    public boolean dispatchPrepareOptionsMenu(final Menu menu) {
        boolean b = false;
        for (int i = 0; i < this.mAdded.size(); ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                if (fragment.performPrepareOptionsMenu(menu)) {
                    b = true;
                }
            }
        }
        return b;
    }
    
    public void dispatchReallyStop() {
        this.dispatchStateChange(2);
    }
    
    public void dispatchResume() {
        this.mStateSaved = false;
        this.dispatchStateChange(5);
    }
    
    public void dispatchStart() {
        this.mStateSaved = false;
        this.dispatchStateChange(4);
    }
    
    public void dispatchStop() {
        this.mStateSaved = true;
        this.dispatchStateChange(3);
    }
    
    void doPendingDeferredStart() {
        if (!this.mHavePendingDeferredStart) {
            return;
        }
        boolean b = false;
        for (int i = 0; i < this.mActive.size(); ++i) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null && fragment.mLoaderManager != null) {
                b |= fragment.mLoaderManager.hasRunningLoaders();
            }
        }
        if (!b) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
        }
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("    ");
        final String string = sb.toString();
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null) {
            final int size = mActive.size();
            if (size > 0) {
                printWriter.print(s);
                printWriter.print("Active Fragments in ");
                printWriter.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter.println(":");
                for (int i = 0; i < size; ++i) {
                    final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(i);
                    printWriter.print(": ");
                    printWriter.println(fragment);
                    if (fragment != null) {
                        fragment.dump(string, fileDescriptor, printWriter, array);
                    }
                }
            }
        }
        final int size2 = this.mAdded.size();
        if (size2 > 0) {
            printWriter.print(s);
            printWriter.println("Added Fragments:");
            for (int j = 0; j < size2; ++j) {
                final Fragment fragment2 = this.mAdded.get(j);
                printWriter.print(s);
                printWriter.print("  #");
                printWriter.print(j);
                printWriter.print(": ");
                printWriter.println(fragment2.toString());
            }
        }
        final ArrayList<Fragment> mCreatedMenus = this.mCreatedMenus;
        if (mCreatedMenus != null) {
            final int size3 = mCreatedMenus.size();
            if (size3 > 0) {
                printWriter.print(s);
                printWriter.println("Fragments Created Menus:");
                for (int k = 0; k < size3; ++k) {
                    final Fragment fragment3 = this.mCreatedMenus.get(k);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(k);
                    printWriter.print(": ");
                    printWriter.println(fragment3.toString());
                }
            }
        }
        final ArrayList<BackStackRecord> mBackStack = this.mBackStack;
        if (mBackStack != null) {
            final int size4 = mBackStack.size();
            if (size4 > 0) {
                printWriter.print(s);
                printWriter.println("Back Stack:");
                for (int l = 0; l < size4; ++l) {
                    final BackStackRecord backStackRecord = this.mBackStack.get(l);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(l);
                    printWriter.print(": ");
                    printWriter.println(backStackRecord.toString());
                    backStackRecord.dump(string, fileDescriptor, printWriter, array);
                }
            }
        }
    Label_0677_Outer:
        while (true) {
            int size5;
            BackStackRecord backStackRecord2;
            int n2;
            OpGenerator opGenerator;
            final String s2;
            ArrayList<OpGenerator> mPendingActions;
            int size6;
            Label_0640:Label_0594_Outer:
            while (true) {
                while (true) {
                    Label_0944: {
                        Label_0941: {
                            Label_0938: {
                                synchronized (this) {
                                    if (this.mBackStackIndices == null) {
                                        break Label_0944;
                                    }
                                    size5 = this.mBackStackIndices.size();
                                    if (size5 > 0) {
                                        printWriter.print(s);
                                        printWriter.println("Back Stack Indices:");
                                        for (int n = 0; n < size5; ++n) {
                                            backStackRecord2 = this.mBackStackIndices.get(n);
                                            printWriter.print(s);
                                            printWriter.print("  #");
                                            printWriter.print(n);
                                            printWriter.print(": ");
                                            printWriter.println(backStackRecord2);
                                        }
                                        break Label_0938;
                                    }
                                    break Label_0941;
                                    // monitorexit(this)
                                    // iftrue(Label_0742:, mPendingActions == null)
                                    // iftrue(Label_0736:, n2 >= size6)
                                    // iftrue(Label_0904:, !this.mNeedMenuInvalidate)
                                    Label_0742:Label_0824_Outer:Block_21_Outer:Block_23_Outer:
                                    while (true) {
                                        while (true) {
                                            while (true) {
                                                Block_20: {
                                                    while (true) {
                                                    Block_25_Outer:
                                                        while (true) {
                                                            while (true) {
                                                            Label_0677:
                                                                while (true) {
                                                                    while (true) {
                                                                        while (true) {
                                                                            opGenerator = this.mPendingActions.get(n2);
                                                                            printWriter.print(s2);
                                                                            printWriter.print("  #");
                                                                            printWriter.print(n2);
                                                                            printWriter.print(": ");
                                                                            printWriter.println(opGenerator);
                                                                            ++n2;
                                                                            break Label_0677;
                                                                            Label_0736: {
                                                                                break Label_0742;
                                                                            }
                                                                            mPendingActions = this.mPendingActions;
                                                                            break Block_20;
                                                                            printWriter.print(s2);
                                                                            printWriter.print("  mNeedMenuInvalidate=");
                                                                            printWriter.println(this.mNeedMenuInvalidate);
                                                                            break Label_0677;
                                                                            continue Label_0677_Outer;
                                                                        }
                                                                        printWriter.print(s2);
                                                                        printWriter.print("  mCurState=");
                                                                        printWriter.print(this.mCurState);
                                                                        printWriter.print(" mStateSaved=");
                                                                        printWriter.print(this.mStateSaved);
                                                                        printWriter.print(" mDestroyed=");
                                                                        printWriter.println(this.mDestroyed);
                                                                        continue Label_0824_Outer;
                                                                    }
                                                                    printWriter.print(s2);
                                                                    printWriter.print("  mNoTransactionsBecause=");
                                                                    printWriter.println(this.mNoTransactionsBecause);
                                                                    return;
                                                                    printWriter.print(s2);
                                                                    printWriter.println("Pending Actions:");
                                                                    n2 = 0;
                                                                    continue Label_0677;
                                                                }
                                                                continue Block_21_Outer;
                                                            }
                                                            printWriter.print(s2);
                                                            printWriter.print("mAvailBackStackIndices: ");
                                                            printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
                                                            continue Label_0640;
                                                            printWriter.print(s2);
                                                            printWriter.print("  mParent=");
                                                            printWriter.println(this.mParent);
                                                            continue Block_25_Outer;
                                                        }
                                                        continue Block_23_Outer;
                                                    }
                                                }
                                                size6 = mPendingActions.size();
                                                continue Block_23_Outer;
                                            }
                                            Label_0932: {
                                                return;
                                            }
                                            printWriter.print(s2);
                                            printWriter.println("FragmentManager misc state:");
                                            printWriter.print(s2);
                                            printWriter.print("  mHost=");
                                            printWriter.println(this.mHost);
                                            printWriter.print(s2);
                                            printWriter.print("  mContainer=");
                                            printWriter.println(this.mContainer);
                                            continue Label_0594_Outer;
                                        }
                                        Label_0739: {
                                            continue Label_0742;
                                        }
                                    }
                                }
                                // iftrue(Label_0932:, this.mNoTransactionsBecause == null)
                                // iftrue(Label_0947:, this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0)
                                // iftrue(Label_0739:, size6 <= 0)
                                // iftrue(Label_0824:, this.mParent == null)
                            }
                            continue;
                        }
                        continue;
                    }
                    continue;
                }
                Label_0947: {
                    continue Label_0640;
                }
            }
        }
    }
    
    public void enqueueAction(final OpGenerator opGenerator, final boolean b) {
        if (!b) {
            this.checkStateLoss();
        }
        while (true) {
            while (true) {
                Label_0090: {
                    synchronized (this) {
                        if (!this.mDestroyed && this.mHost != null) {
                            if (this.mPendingActions == null) {
                                this.mPendingActions = new ArrayList<OpGenerator>();
                                this.mPendingActions.add(opGenerator);
                                this.scheduleCommit();
                                return;
                            }
                            break Label_0090;
                        }
                        else {
                            if (b) {
                                return;
                            }
                            throw new IllegalStateException("Activity has been destroyed");
                        }
                    }
                }
                continue;
            }
        }
    }
    
    void ensureInflatedFragmentView(final Fragment fragment) {
        if (!fragment.mFromLayout || fragment.mPerformedCreateView) {
            return;
        }
        fragment.mView = fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
        if (fragment.mView != null) {
            fragment.mInnerView = fragment.mView;
            fragment.mView.setSaveFromParentEnabled(false);
            if (fragment.mHidden) {
                fragment.mView.setVisibility(8);
            }
            fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
            this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
            return;
        }
        fragment.mInnerView = null;
    }
    
    public boolean execPendingActions() {
        this.ensureExecReady(true);
        boolean b = false;
        while (this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                this.cleanupExec();
                b = true;
                continue;
            }
            finally {
                this.cleanupExec();
            }
            break;
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return b;
    }
    
    public void execSingleAction(final OpGenerator opGenerator, final boolean b) {
        if (b && (this.mHost == null || this.mDestroyed)) {
            return;
        }
        this.ensureExecReady(b);
        if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            }
            finally {
                this.cleanupExec();
            }
        }
        this.doPendingDeferredStart();
        this.burpActive();
    }
    
    @Override
    public boolean executePendingTransactions() {
        final boolean execPendingActions = this.execPendingActions();
        this.forcePostponedTransactions();
        return execPendingActions;
    }
    
    @Override
    public Fragment findFragmentById(final int n) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.mFragmentId == n) {
                return fragment;
            }
        }
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null) {
            for (int j = mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null && fragment2.mFragmentId == n) {
                    return fragment2;
                }
            }
        }
        return null;
    }
    
    @Override
    public Fragment findFragmentByTag(final String s) {
        if (s != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && s.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null && s != null) {
            for (int j = mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null && s.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }
    
    public Fragment findFragmentByWho(final String s) {
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive != null && s != null) {
            for (int i = mActive.size() - 1; i >= 0; --i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null) {
                    final Fragment fragmentByWho = fragment.findFragmentByWho(s);
                    if (fragmentByWho != null) {
                        return fragmentByWho;
                    }
                }
            }
        }
        return null;
    }
    
    public void freeBackStackIndex(final int n) {
        while (true) {
            while (true) {
                Label_0091: {
                    synchronized (this) {
                        this.mBackStackIndices.set(n, null);
                        if (this.mAvailBackStackIndices == null) {
                            this.mAvailBackStackIndices = new ArrayList<Integer>();
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Freeing back stack index ");
                                sb.append(n);
                                Log.v("FragmentManager", sb.toString());
                            }
                            this.mAvailBackStackIndices.add(n);
                            return;
                        }
                        break Label_0091;
                    }
                }
                continue;
            }
        }
    }
    
    int getActiveFragmentCount() {
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive == null) {
            return 0;
        }
        return mActive.size();
    }
    
    List<Fragment> getActiveFragments() {
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive == null) {
            return null;
        }
        final int size = mActive.size();
        final ArrayList list = new ArrayList<Fragment>(size);
        for (int i = 0; i < size; ++i) {
            list.add((Fragment)this.mActive.valueAt(i));
        }
        return (List<Fragment>)list;
    }
    
    @Override
    public BackStackEntry getBackStackEntryAt(final int n) {
        return this.mBackStack.get(n);
    }
    
    @Override
    public int getBackStackEntryCount() {
        final ArrayList<BackStackRecord> mBackStack = this.mBackStack;
        if (mBackStack != null) {
            return mBackStack.size();
        }
        return 0;
    }
    
    @Override
    public Fragment getFragment(final Bundle bundle, final String s) {
        final int int1 = bundle.getInt(s, -1);
        if (int1 == -1) {
            return null;
        }
        final Fragment fragment = (Fragment)this.mActive.get(int1);
        if (fragment == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment no longer exists for key ");
            sb.append(s);
            sb.append(": index ");
            sb.append(int1);
            this.throwException(new IllegalStateException(sb.toString()));
            return fragment;
        }
        return fragment;
    }
    
    @Override
    public List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return (List<Fragment>)Collections.EMPTY_LIST;
        }
        synchronized (this.mAdded) {
            return (List<Fragment>)this.mAdded.clone();
        }
    }
    
    LayoutInflater$Factory2 getLayoutInflaterFactory() {
        return (LayoutInflater$Factory2)this;
    }
    
    @Override
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }
    
    public void hideFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("hide: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged ^= true;
        }
    }
    
    @Override
    public boolean isDestroyed() {
        return this.mDestroyed;
    }
    
    boolean isStateAtLeast(final int n) {
        return this.mCurState >= n;
    }
    
    @Override
    public boolean isStateSaved() {
        return this.mStateSaved;
    }
    
    AnimationOrAnimator loadAnimation(final Fragment fragment, int transitToStyleIndex, final boolean b, int onGetWindowAnimations) {
        final int nextAnim = fragment.getNextAnim();
        final Animation onCreateAnimation = fragment.onCreateAnimation(transitToStyleIndex, b, nextAnim);
        if (onCreateAnimation != null) {
            return new AnimationOrAnimator(onCreateAnimation);
        }
        final Animator onCreateAnimator = fragment.onCreateAnimator(transitToStyleIndex, b, nextAnim);
        if (onCreateAnimator != null) {
            return new AnimationOrAnimator(onCreateAnimator);
        }
        if (nextAnim != 0) {
            final boolean equals = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim));
            boolean b2 = false;
            if (equals) {
                try {
                    final Animation loadAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (loadAnimation != null) {
                        return new AnimationOrAnimator(loadAnimation);
                    }
                    b2 = true;
                }
                catch (RuntimeException ex3) {}
                catch (Resources$NotFoundException ex) {
                    throw ex;
                }
            }
            if (!b2) {
                try {
                    final Animator loadAnimator = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
                    if (loadAnimator != null) {
                        return new AnimationOrAnimator(loadAnimator);
                    }
                }
                catch (RuntimeException ex2) {
                    if (equals) {
                        throw ex2;
                    }
                    final Animation loadAnimation2 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (loadAnimation2 != null) {
                        return new AnimationOrAnimator(loadAnimation2);
                    }
                }
            }
        }
        if (transitToStyleIndex == 0) {
            return null;
        }
        transitToStyleIndex = transitToStyleIndex(transitToStyleIndex, b);
        if (transitToStyleIndex < 0) {
            return null;
        }
        switch (transitToStyleIndex) {
            default: {
                if (onGetWindowAnimations == 0 && this.mHost.onHasWindowAnimations()) {
                    onGetWindowAnimations = this.mHost.onGetWindowAnimations();
                }
                if (onGetWindowAnimations == 0) {
                    return null;
                }
                return null;
            }
            case 6: {
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            }
            case 5: {
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            }
            case 4: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            }
            case 3: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            }
            case 2: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            }
            case 1: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            }
        }
    }
    
    void makeActive(final Fragment fragment) {
        if (fragment.mIndex >= 0) {
            return;
        }
        fragment.setIndex(this.mNextFragmentIndex++, this.mParent);
        if (this.mActive == null) {
            this.mActive = (SparseArray<Fragment>)new SparseArray();
        }
        this.mActive.put(fragment.mIndex, (Object)fragment);
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Allocated fragment index ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
    }
    
    void makeInactive(final Fragment fragment) {
        if (fragment.mIndex < 0) {
            return;
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Freeing fragment index ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        this.mActive.put(fragment.mIndex, (Object)null);
        this.mHost.inactivateFragment(fragment.mWho);
        fragment.initState();
    }
    
    void moveFragmentToExpectedState(final Fragment fragment) {
        if (fragment == null) {
            return;
        }
        int n = this.mCurState;
        if (fragment.mRemoving) {
            if (fragment.isInBackStack()) {
                n = Math.min(n, 1);
            }
            else {
                n = Math.min(n, 0);
            }
        }
        this.moveToState(fragment, n, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
        if (fragment.mView != null) {
            final Fragment fragmentUnder = this.findFragmentUnder(fragment);
            if (fragmentUnder != null) {
                final View mView = fragmentUnder.mView;
                final ViewGroup mContainer = fragment.mContainer;
                final int indexOfChild = mContainer.indexOfChild(mView);
                final int indexOfChild2 = mContainer.indexOfChild(fragment.mView);
                if (indexOfChild2 < indexOfChild) {
                    mContainer.removeViewAt(indexOfChild2);
                    mContainer.addView(fragment.mView, indexOfChild);
                }
            }
            if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                if (fragment.mPostponedAlpha > 0.0f) {
                    fragment.mView.setAlpha(fragment.mPostponedAlpha);
                }
                fragment.mPostponedAlpha = 0.0f;
                fragment.mIsNewlyAdded = false;
                final AnimationOrAnimator loadAnimation = this.loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                if (loadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    if (loadAnimation.animation != null) {
                        fragment.mView.startAnimation(loadAnimation.animation);
                    }
                    else {
                        loadAnimation.animator.setTarget((Object)fragment.mView);
                        loadAnimation.animator.start();
                    }
                }
            }
        }
        if (fragment.mHiddenChanged) {
            this.completeShowHideFragment(fragment);
        }
    }
    
    void moveToState(int mCurState, final boolean b) {
        if (this.mHost == null && mCurState != 0) {
            throw new IllegalStateException("No activity");
        }
        if (!b && mCurState == this.mCurState) {
            return;
        }
        this.mCurState = mCurState;
        if (this.mActive != null) {
            mCurState = 0;
            for (int size = this.mAdded.size(), i = 0; i < size; ++i) {
                final Fragment fragment = this.mAdded.get(i);
                this.moveFragmentToExpectedState(fragment);
                if (fragment.mLoaderManager != null) {
                    mCurState |= (fragment.mLoaderManager.hasRunningLoaders() ? 1 : 0);
                }
            }
            for (int size2 = this.mActive.size(), j = 0; j < size2; ++j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null && (fragment2.mRemoving || fragment2.mDetached) && !fragment2.mIsNewlyAdded) {
                    this.moveFragmentToExpectedState(fragment2);
                    if (fragment2.mLoaderManager != null) {
                        mCurState |= (fragment2.mLoaderManager.hasRunningLoaders() ? 1 : 0);
                    }
                }
            }
            if (mCurState == 0) {
                this.startPendingDeferredFragments();
            }
            if (this.mNeedMenuInvalidate) {
                final FragmentHostCallback mHost = this.mHost;
                if (mHost != null && this.mCurState == 5) {
                    mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }
    
    void moveToState(final Fragment fragment) {
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }
    
    void moveToState(final Fragment fragment, int mState, final int n, final int n2, final boolean b) {
        final boolean mAdded = fragment.mAdded;
        final boolean b2 = true;
        if (!mAdded || fragment.mDetached) {
            if (mState > 1) {
                mState = 1;
            }
        }
        if (fragment.mRemoving && mState > fragment.mState) {
            if (fragment.mState == 0 && fragment.isInBackStack()) {
                mState = 1;
            }
            else {
                mState = fragment.mState;
            }
        }
        if (fragment.mDeferStart && fragment.mState < 4 && mState > 3) {
            mState = 3;
        }
        if (fragment.mState <= mState) {
            if (fragment.mFromLayout && !fragment.mInLayout) {
                return;
            }
            if (fragment.getAnimatingAway() != null || fragment.getAnimator() != null) {
                fragment.setAnimatingAway(null);
                fragment.setAnimator(null);
                this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, true);
            }
            Label_1357: {
                Label_1287: {
                    Label_1227: {
                        Label_1214: {
                            switch (fragment.mState) {
                                default: {
                                    break Label_1357;
                                }
                                case 4: {
                                    break Label_1287;
                                }
                                case 3: {
                                    break Label_1227;
                                }
                                case 2: {
                                    break Label_1214;
                                }
                                case 1: {
                                    break;
                                }
                                case 0: {
                                    if (mState <= 0) {
                                        break;
                                    }
                                    if (FragmentManagerImpl.DEBUG) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("moveto CREATED: ");
                                        sb.append(fragment);
                                        Log.v("FragmentManager", sb.toString());
                                    }
                                    if (fragment.mSavedFragmentState != null) {
                                        fragment.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                                        fragment.mSavedViewState = (SparseArray<Parcelable>)fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                                        fragment.mTarget = this.getFragment(fragment.mSavedFragmentState, "android:target_state");
                                        if (fragment.mTarget != null) {
                                            fragment.mTargetRequestCode = fragment.mSavedFragmentState.getInt("android:target_req_state", 0);
                                        }
                                        if (!(fragment.mUserVisibleHint = fragment.mSavedFragmentState.getBoolean("android:user_visible_hint", true))) {
                                            fragment.mDeferStart = true;
                                            if (mState > 3) {
                                                mState = 3;
                                            }
                                        }
                                    }
                                    final FragmentHostCallback mHost = this.mHost;
                                    fragment.mHost = mHost;
                                    final Fragment mParent = this.mParent;
                                    FragmentManagerImpl mFragmentManager;
                                    if ((fragment.mParentFragment = mParent) != null) {
                                        mFragmentManager = mParent.mChildFragmentManager;
                                    }
                                    else {
                                        mFragmentManager = mHost.getFragmentManagerImpl();
                                    }
                                    fragment.mFragmentManager = mFragmentManager;
                                    if (fragment.mTarget != null) {
                                        if (this.mActive.get(fragment.mTarget.mIndex) != fragment.mTarget) {
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append("Fragment ");
                                            sb2.append(fragment);
                                            sb2.append(" declared target fragment ");
                                            sb2.append(fragment.mTarget);
                                            sb2.append(" that does not belong to this FragmentManager!");
                                            throw new IllegalStateException(sb2.toString());
                                        }
                                        if (fragment.mTarget.mState < 1) {
                                            this.moveToState(fragment.mTarget, 1, 0, 0, true);
                                        }
                                    }
                                    this.dispatchOnFragmentPreAttached(fragment, this.mHost.getContext(), false);
                                    fragment.mCalled = false;
                                    fragment.onAttach(this.mHost.getContext());
                                    if (fragment.mCalled) {
                                        if (fragment.mParentFragment == null) {
                                            this.mHost.onAttachFragment(fragment);
                                        }
                                        else {
                                            fragment.mParentFragment.onAttachFragment(fragment);
                                        }
                                        this.dispatchOnFragmentAttached(fragment, this.mHost.getContext(), false);
                                        if (!fragment.mIsCreated) {
                                            this.dispatchOnFragmentPreCreated(fragment, fragment.mSavedFragmentState, false);
                                            fragment.performCreate(fragment.mSavedFragmentState);
                                            this.dispatchOnFragmentCreated(fragment, fragment.mSavedFragmentState, false);
                                        }
                                        else {
                                            fragment.restoreChildFragmentState(fragment.mSavedFragmentState);
                                            fragment.mState = 1;
                                        }
                                        fragment.mRetaining = false;
                                        break;
                                    }
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Fragment ");
                                    sb3.append(fragment);
                                    sb3.append(" did not call through to super.onAttach()");
                                    throw new SuperNotCalledException(sb3.toString());
                                }
                            }
                            this.ensureInflatedFragmentView(fragment);
                            if (mState > 1) {
                                if (FragmentManagerImpl.DEBUG) {
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("moveto ACTIVITY_CREATED: ");
                                    sb4.append(fragment);
                                    Log.v("FragmentManager", sb4.toString());
                                }
                                if (!fragment.mFromLayout) {
                                    ViewGroup mContainer = null;
                                    if (fragment.mContainerId != 0) {
                                        if (fragment.mContainerId == -1) {
                                            final StringBuilder sb5 = new StringBuilder();
                                            sb5.append("Cannot create fragment ");
                                            sb5.append(fragment);
                                            sb5.append(" for a container view with no id");
                                            this.throwException(new IllegalArgumentException(sb5.toString()));
                                        }
                                        final ViewGroup viewGroup = (ViewGroup)this.mContainer.onFindViewById(fragment.mContainerId);
                                        if (viewGroup == null && !fragment.mRestored) {
                                            String resourceName;
                                            try {
                                                resourceName = fragment.getResources().getResourceName(fragment.mContainerId);
                                            }
                                            catch (Resources$NotFoundException ex) {
                                                resourceName = "unknown";
                                            }
                                            final StringBuilder sb6 = new StringBuilder();
                                            sb6.append("No view found for id 0x");
                                            sb6.append(Integer.toHexString(fragment.mContainerId));
                                            sb6.append(" (");
                                            sb6.append(resourceName);
                                            sb6.append(") for fragment ");
                                            sb6.append(fragment);
                                            this.throwException(new IllegalArgumentException(sb6.toString()));
                                        }
                                        mContainer = viewGroup;
                                    }
                                    fragment.mContainer = mContainer;
                                    fragment.mView = fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), mContainer, fragment.mSavedFragmentState);
                                    if (fragment.mView != null) {
                                        fragment.mInnerView = fragment.mView;
                                        fragment.mView.setSaveFromParentEnabled(false);
                                        if (mContainer != null) {
                                            mContainer.addView(fragment.mView);
                                        }
                                        if (fragment.mHidden) {
                                            fragment.mView.setVisibility(8);
                                        }
                                        fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                                        this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
                                        fragment.mIsNewlyAdded = (fragment.mView.getVisibility() == 0 && fragment.mContainer != null && b2);
                                    }
                                    else {
                                        fragment.mInnerView = null;
                                    }
                                }
                                fragment.performActivityCreated(fragment.mSavedFragmentState);
                                this.dispatchOnFragmentActivityCreated(fragment, fragment.mSavedFragmentState, false);
                                if (fragment.mView != null) {
                                    fragment.restoreViewState(fragment.mSavedFragmentState);
                                }
                                fragment.mSavedFragmentState = null;
                            }
                        }
                        if (mState > 2) {
                            fragment.mState = 3;
                        }
                    }
                    if (mState > 3) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb7 = new StringBuilder();
                            sb7.append("moveto STARTED: ");
                            sb7.append(fragment);
                            Log.v("FragmentManager", sb7.toString());
                        }
                        fragment.performStart();
                        this.dispatchOnFragmentStarted(fragment, false);
                    }
                }
                if (mState > 4) {
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb8 = new StringBuilder();
                        sb8.append("moveto RESUMED: ");
                        sb8.append(fragment);
                        Log.v("FragmentManager", sb8.toString());
                    }
                    fragment.performResume();
                    this.dispatchOnFragmentResumed(fragment, false);
                    fragment.mSavedFragmentState = null;
                    fragment.mSavedViewState = null;
                }
            }
        }
        else if (fragment.mState > mState) {
            switch (fragment.mState) {
                case 5: {
                    if (mState < 5) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb9 = new StringBuilder();
                            sb9.append("movefrom RESUMED: ");
                            sb9.append(fragment);
                            Log.v("FragmentManager", sb9.toString());
                        }
                        fragment.performPause();
                        this.dispatchOnFragmentPaused(fragment, false);
                    }
                }
                case 4: {
                    if (mState < 4) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb10 = new StringBuilder();
                            sb10.append("movefrom STARTED: ");
                            sb10.append(fragment);
                            Log.v("FragmentManager", sb10.toString());
                        }
                        fragment.performStop();
                        this.dispatchOnFragmentStopped(fragment, false);
                    }
                }
                case 3: {
                    if (mState < 3) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb11 = new StringBuilder();
                            sb11.append("movefrom STOPPED: ");
                            sb11.append(fragment);
                            Log.v("FragmentManager", sb11.toString());
                        }
                        fragment.performReallyStop();
                    }
                }
                case 2: {
                    if (mState < 2) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb12 = new StringBuilder();
                            sb12.append("movefrom ACTIVITY_CREATED: ");
                            sb12.append(fragment);
                            Log.v("FragmentManager", sb12.toString());
                        }
                        if (fragment.mView != null) {
                            if (this.mHost.onShouldSaveFragmentState(fragment) && fragment.mSavedViewState == null) {
                                this.saveFragmentViewState(fragment);
                            }
                        }
                        fragment.performDestroyView();
                        this.dispatchOnFragmentViewDestroyed(fragment, false);
                        if (fragment.mView != null && fragment.mContainer != null) {
                            fragment.mView.clearAnimation();
                            fragment.mContainer.endViewTransition(fragment.mView);
                            AnimationOrAnimator loadAnimation = null;
                            if (this.mCurState > 0 && !this.mDestroyed) {
                                if (fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f) {
                                    loadAnimation = this.loadAnimation(fragment, n, false, n2);
                                }
                            }
                            fragment.mPostponedAlpha = 0.0f;
                            if (loadAnimation != null) {
                                this.animateRemoveFragment(fragment, loadAnimation, mState);
                            }
                            fragment.mContainer.removeView(fragment.mView);
                        }
                        fragment.mContainer = null;
                        fragment.mView = null;
                        fragment.mInnerView = null;
                        fragment.mInLayout = false;
                    }
                }
                case 1: {
                    if (mState >= 1) {
                        break;
                    }
                    if (this.mDestroyed) {
                        if (fragment.getAnimatingAway() != null) {
                            final View animatingAway = fragment.getAnimatingAway();
                            fragment.setAnimatingAway(null);
                            animatingAway.clearAnimation();
                        }
                        else if (fragment.getAnimator() != null) {
                            final Animator animator = fragment.getAnimator();
                            fragment.setAnimator(null);
                            animator.cancel();
                        }
                    }
                    if (fragment.getAnimatingAway() != null || fragment.getAnimator() != null) {
                        fragment.setStateAfterAnimating(mState);
                        mState = 1;
                        break;
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb13 = new StringBuilder();
                        sb13.append("movefrom CREATED: ");
                        sb13.append(fragment);
                        Log.v("FragmentManager", sb13.toString());
                    }
                    if (!fragment.mRetaining) {
                        fragment.performDestroy();
                        this.dispatchOnFragmentDestroyed(fragment, false);
                    }
                    else {
                        fragment.mState = 0;
                    }
                    fragment.performDetach();
                    this.dispatchOnFragmentDetached(fragment, false);
                    if (b) {
                        break;
                    }
                    if (!fragment.mRetaining) {
                        this.makeInactive(fragment);
                        break;
                    }
                    fragment.mHost = null;
                    fragment.mParentFragment = null;
                    fragment.mFragmentManager = null;
                    break;
                }
            }
        }
        if (fragment.mState != mState) {
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("moveToState: Fragment state for ");
            sb14.append(fragment);
            sb14.append(" not updated inline; ");
            sb14.append("expected state ");
            sb14.append(mState);
            sb14.append(" found ");
            sb14.append(fragment.mState);
            Log.w("FragmentManager", sb14.toString());
            fragment.mState = mState;
        }
    }
    
    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        for (int size = this.mAdded.size(), i = 0; i < size; ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }
    
    public View onCreateView(final View view, String s, final Context context, final AttributeSet set) {
        if (!"fragment".equals(s)) {
            return null;
        }
        s = set.getAttributeValue((String)null, "class");
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, FragmentTag.Fragment);
        int id = 0;
        if (s == null) {
            s = obtainStyledAttributes.getString(0);
        }
        final int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        final String string = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), s)) {
            return null;
        }
        if (view != null) {
            id = view.getId();
        }
        if (id == -1 && resourceId == -1 && string == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(set.getPositionDescription());
            sb.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        Fragment fragment;
        if (resourceId != -1) {
            fragment = this.findFragmentById(resourceId);
        }
        else {
            fragment = null;
        }
        if (fragment == null && string != null) {
            fragment = this.findFragmentByTag(string);
        }
        if (fragment == null && id != -1) {
            fragment = this.findFragmentById(id);
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("onCreateView: id=0x");
            sb2.append(Integer.toHexString(resourceId));
            sb2.append(" fname=");
            sb2.append(s);
            sb2.append(" existing=");
            sb2.append(fragment);
            Log.v("FragmentManager", sb2.toString());
        }
        if (fragment == null) {
            fragment = this.mContainer.instantiate(context, s, null);
            fragment.mFromLayout = true;
            int mFragmentId;
            if (resourceId != 0) {
                mFragmentId = resourceId;
            }
            else {
                mFragmentId = id;
            }
            fragment.mFragmentId = mFragmentId;
            fragment.mContainerId = id;
            fragment.mTag = string;
            fragment.mInLayout = true;
            fragment.mFragmentManager = this;
            final FragmentHostCallback mHost = this.mHost;
            fragment.mHost = mHost;
            fragment.onInflate(mHost.getContext(), set, fragment.mSavedFragmentState);
            this.addFragment(fragment, true);
        }
        else {
            if (fragment.mInLayout) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(set.getPositionDescription());
                sb3.append(": Duplicate id 0x");
                sb3.append(Integer.toHexString(resourceId));
                sb3.append(", tag ");
                sb3.append(string);
                sb3.append(", or parent id 0x");
                sb3.append(Integer.toHexString(id));
                sb3.append(" with another fragment for ");
                sb3.append(s);
                throw new IllegalArgumentException(sb3.toString());
            }
            fragment.mInLayout = true;
            fragment.mHost = this.mHost;
            if (!fragment.mRetaining) {
                fragment.onInflate(this.mHost.getContext(), set, fragment.mSavedFragmentState);
            }
        }
        if (this.mCurState < 1 && fragment.mFromLayout) {
            this.moveToState(fragment, 1, 0, 0, false);
        }
        else {
            this.moveToState(fragment);
        }
        if (fragment.mView != null) {
            if (resourceId != 0) {
                fragment.mView.setId(resourceId);
            }
            if (fragment.mView.getTag() == null) {
                fragment.mView.setTag((Object)string);
            }
            return fragment.mView;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Fragment ");
        sb4.append(s);
        sb4.append(" did not create a view.");
        throw new IllegalStateException(sb4.toString());
    }
    
    public View onCreateView(final String s, final Context context, final AttributeSet set) {
        return this.onCreateView(null, s, context, set);
    }
    
    public void performPendingDeferredStart(final Fragment fragment) {
        if (!fragment.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        fragment.mDeferStart = false;
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }
    
    @Override
    public void popBackStack() {
        this.enqueueAction((OpGenerator)new PopBackStackState(null, -1, 0), false);
    }
    
    @Override
    public void popBackStack(final int n, final int n2) {
        if (n >= 0) {
            this.enqueueAction((OpGenerator)new PopBackStackState(null, n, n2), false);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad id: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public void popBackStack(final String s, final int n) {
        this.enqueueAction((OpGenerator)new PopBackStackState(s, -1, n), false);
    }
    
    @Override
    public boolean popBackStackImmediate() {
        this.checkStateLoss();
        return this.popBackStackImmediate(null, -1, 0);
    }
    
    @Override
    public boolean popBackStackImmediate(final int n, final int n2) {
        this.checkStateLoss();
        this.execPendingActions();
        if (n >= 0) {
            return this.popBackStackImmediate(null, n, n2);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad id: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public boolean popBackStackImmediate(final String s, final int n) {
        this.checkStateLoss();
        return this.popBackStackImmediate(s, -1, n);
    }
    
    boolean popBackStackState(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final String s, int n, int i) {
        final ArrayList<BackStackRecord> mBackStack = this.mBackStack;
        if (mBackStack == null) {
            return false;
        }
        if (s == null && n < 0 && (i & 0x1) == 0x0) {
            n = mBackStack.size() - 1;
            if (n < 0) {
                return false;
            }
            list.add(this.mBackStack.remove(n));
            list2.add(true);
            return true;
        }
        else {
            final int n2 = -1;
            Label_0274: {
                if (s == null && n < 0) {
                    n = n2;
                }
                else {
                    int j;
                    for (j = this.mBackStack.size() - 1; j >= 0; --j) {
                        final BackStackRecord backStackRecord = this.mBackStack.get(j);
                        if (s != null && s.equals(backStackRecord.getName())) {
                            break;
                        }
                        if (n >= 0 && n == backStackRecord.mIndex) {
                            break;
                        }
                    }
                    if (j < 0) {
                        return false;
                    }
                    if ((i & 0x1) != 0x0) {
                        BackStackRecord backStackRecord2;
                        for (i = j - 1; i >= 0; --i) {
                            backStackRecord2 = this.mBackStack.get(i);
                            if ((s == null || !s.equals(backStackRecord2.getName())) && (n < 0 || n != backStackRecord2.mIndex)) {
                                n = i;
                                break Label_0274;
                            }
                        }
                        n = i;
                    }
                    else {
                        n = j;
                    }
                }
            }
            if (n == this.mBackStack.size() - 1) {
                return false;
            }
            for (i = this.mBackStack.size() - 1; i > n; --i) {
                list.add(this.mBackStack.remove(i));
                list2.add(true);
            }
            return true;
        }
    }
    
    @Override
    public void putFragment(final Bundle bundle, final String s, final Fragment fragment) {
        if (fragment.mIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(sb.toString()));
        }
        bundle.putInt(s, fragment.mIndex);
    }
    
    @Override
    public void registerFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks fragmentLifecycleCallbacks, final boolean b) {
        this.mLifecycleCallbacks.add(new Pair<FragmentLifecycleCallbacks, Boolean>(fragmentLifecycleCallbacks, b));
    }
    
    public void removeFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("remove: ");
            sb.append(fragment);
            sb.append(" nesting=");
            sb.append(fragment.mBackStackNesting);
            Log.v("FragmentManager", sb.toString());
        }
        final boolean inBackStack = fragment.isInBackStack();
        if (fragment.mDetached && !(inBackStack ^ true)) {
            return;
        }
        synchronized (this.mAdded) {
            this.mAdded.remove(fragment);
            // monitorexit(this.mAdded)
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }
    
    @Override
    public void removeOnBackStackChangedListener(final OnBackStackChangedListener onBackStackChangedListener) {
        final ArrayList<OnBackStackChangedListener> mBackStackChangeListeners = this.mBackStackChangeListeners;
        if (mBackStackChangeListeners != null) {
            mBackStackChangeListeners.remove(onBackStackChangedListener);
        }
    }
    
    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }
    
    void restoreAllState(final Parcelable parcelable, final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (parcelable == null) {
            return;
        }
        final FragmentManagerState fragmentManagerState = (FragmentManagerState)parcelable;
        if (fragmentManagerState.mActive == null) {
            return;
        }
        List<FragmentManagerNonConfig> childNonConfigs = null;
        if (fragmentManagerNonConfig != null) {
            final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
            childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
            int size;
            if (fragments != null) {
                size = fragments.size();
            }
            else {
                size = 0;
            }
            for (int i = 0; i < size; ++i) {
                final Fragment mInstance = fragments.get(i);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("restoreAllState: re-attaching retained ");
                    sb.append(mInstance);
                    Log.v("FragmentManager", sb.toString());
                }
                int n;
                for (n = 0; n < fragmentManagerState.mActive.length && fragmentManagerState.mActive[n].mIndex != mInstance.mIndex; ++n) {}
                if (n == fragmentManagerState.mActive.length) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not find active fragment with index ");
                    sb2.append(mInstance.mIndex);
                    this.throwException(new IllegalStateException(sb2.toString()));
                }
                final FragmentState fragmentState = fragmentManagerState.mActive[n];
                fragmentState.mInstance = mInstance;
                mInstance.mSavedViewState = null;
                mInstance.mBackStackNesting = 0;
                mInstance.mInLayout = false;
                mInstance.mAdded = false;
                mInstance.mTarget = null;
                if (fragmentState.mSavedFragmentState != null) {
                    fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                    mInstance.mSavedViewState = (SparseArray<Parcelable>)fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                    mInstance.mSavedFragmentState = fragmentState.mSavedFragmentState;
                }
            }
        }
        this.mActive = (SparseArray<Fragment>)new SparseArray(fragmentManagerState.mActive.length);
        for (int j = 0; j < fragmentManagerState.mActive.length; ++j) {
            final FragmentState fragmentState2 = fragmentManagerState.mActive[j];
            if (fragmentState2 != null) {
                FragmentManagerNonConfig fragmentManagerNonConfig2 = null;
                if (childNonConfigs != null && j < childNonConfigs.size()) {
                    fragmentManagerNonConfig2 = childNonConfigs.get(j);
                }
                final Fragment instantiate = fragmentState2.instantiate(this.mHost, this.mContainer, this.mParent, fragmentManagerNonConfig2);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("restoreAllState: active #");
                    sb3.append(j);
                    sb3.append(": ");
                    sb3.append(instantiate);
                    Log.v("FragmentManager", sb3.toString());
                }
                this.mActive.put(instantiate.mIndex, (Object)instantiate);
                fragmentState2.mInstance = null;
            }
        }
        if (fragmentManagerNonConfig != null) {
            final List<Fragment> fragments2 = fragmentManagerNonConfig.getFragments();
            int size2;
            if (fragments2 != null) {
                size2 = fragments2.size();
            }
            else {
                size2 = 0;
            }
            for (int k = 0; k < size2; ++k) {
                final Fragment fragment = fragments2.get(k);
                if (fragment.mTargetIndex >= 0) {
                    fragment.mTarget = (Fragment)this.mActive.get(fragment.mTargetIndex);
                    if (fragment.mTarget == null) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Re-attaching retained fragment ");
                        sb4.append(fragment);
                        sb4.append(" target no longer exists: ");
                        sb4.append(fragment.mTargetIndex);
                        Log.w("FragmentManager", sb4.toString());
                    }
                }
            }
        }
        this.mAdded.clear();
        if (fragmentManagerState.mAdded != null) {
            int l = 0;
            while (l < fragmentManagerState.mAdded.length) {
                final Fragment fragment2 = (Fragment)this.mActive.get(fragmentManagerState.mAdded[l]);
                if (fragment2 == null) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("No instantiated fragment for index #");
                    sb5.append(fragmentManagerState.mAdded[l]);
                    this.throwException(new IllegalStateException(sb5.toString()));
                }
                fragment2.mAdded = true;
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("restoreAllState: added #");
                    sb6.append(l);
                    sb6.append(": ");
                    sb6.append(fragment2);
                    Log.v("FragmentManager", sb6.toString());
                }
                if (!this.mAdded.contains(fragment2)) {
                    synchronized (this.mAdded) {
                        this.mAdded.add(fragment2);
                        // monitorexit(this.mAdded)
                        ++l;
                        continue;
                    }
                }
                throw new IllegalStateException("Already added!");
            }
        }
        if (fragmentManagerState.mBackStack != null) {
            this.mBackStack = new ArrayList<BackStackRecord>(fragmentManagerState.mBackStack.length);
            for (int n2 = 0; n2 < fragmentManagerState.mBackStack.length; ++n2) {
                final BackStackRecord instantiate2 = fragmentManagerState.mBackStack[n2].instantiate(this);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("restoreAllState: back stack #");
                    sb7.append(n2);
                    sb7.append(" (index ");
                    sb7.append(instantiate2.mIndex);
                    sb7.append("): ");
                    sb7.append(instantiate2);
                    Log.v("FragmentManager", sb7.toString());
                    final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
                    instantiate2.dump("  ", printWriter, false);
                    printWriter.close();
                }
                this.mBackStack.add(instantiate2);
                if (instantiate2.mIndex >= 0) {
                    this.setBackStackIndex(instantiate2.mIndex, instantiate2);
                }
            }
        }
        else {
            this.mBackStack = null;
        }
        if (fragmentManagerState.mPrimaryNavActiveIndex >= 0) {
            this.mPrimaryNav = (Fragment)this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex);
        }
        this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
    }
    
    FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }
    
    Parcelable saveAllState() {
        this.forcePostponedTransactions();
        this.endAnimatingAwayFragments();
        this.execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        final SparseArray<Fragment> mActive = this.mActive;
        if (mActive == null) {
            return null;
        }
        if (mActive.size() <= 0) {
            return null;
        }
        final int size = this.mActive.size();
        final FragmentState[] mActive2 = new FragmentState[size];
        boolean b = false;
        for (int i = 0; i < size; ++i) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null) {
                if (fragment.mIndex < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failure saving state: active ");
                    sb.append(fragment);
                    sb.append(" has cleared index: ");
                    sb.append(fragment.mIndex);
                    this.throwException(new IllegalStateException(sb.toString()));
                }
                b = true;
                final FragmentState fragmentState = new FragmentState(fragment);
                mActive2[i] = fragmentState;
                if (fragment.mState > 0 && fragmentState.mSavedFragmentState == null) {
                    fragmentState.mSavedFragmentState = this.saveFragmentBasicState(fragment);
                    if (fragment.mTarget != null) {
                        if (fragment.mTarget.mIndex < 0) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failure saving state: ");
                            sb2.append(fragment);
                            sb2.append(" has target not in fragment manager: ");
                            sb2.append(fragment.mTarget);
                            this.throwException(new IllegalStateException(sb2.toString()));
                        }
                        if (fragmentState.mSavedFragmentState == null) {
                            fragmentState.mSavedFragmentState = new Bundle();
                        }
                        this.putFragment(fragmentState.mSavedFragmentState, "android:target_state", fragment.mTarget);
                        if (fragment.mTargetRequestCode != 0) {
                            fragmentState.mSavedFragmentState.putInt("android:target_req_state", fragment.mTargetRequestCode);
                        }
                    }
                }
                else {
                    fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Saved state of ");
                    sb3.append(fragment);
                    sb3.append(": ");
                    sb3.append(fragmentState.mSavedFragmentState);
                    Log.v("FragmentManager", sb3.toString());
                }
            }
        }
        if (!b) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "saveAllState: no fragments!");
            }
            return null;
        }
        int[] mAdded = null;
        BackStackState[] mBackStack = null;
        final int size2 = this.mAdded.size();
        if (size2 > 0) {
            mAdded = new int[size2];
            for (int j = 0; j < size2; ++j) {
                mAdded[j] = this.mAdded.get(j).mIndex;
                if (mAdded[j] < 0) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failure saving state: active ");
                    sb4.append(this.mAdded.get(j));
                    sb4.append(" has cleared index: ");
                    sb4.append(mAdded[j]);
                    this.throwException(new IllegalStateException(sb4.toString()));
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("saveAllState: adding fragment #");
                    sb5.append(j);
                    sb5.append(": ");
                    sb5.append(this.mAdded.get(j));
                    Log.v("FragmentManager", sb5.toString());
                }
            }
        }
        final ArrayList<BackStackRecord> mBackStack2 = this.mBackStack;
        if (mBackStack2 != null) {
            final int size3 = mBackStack2.size();
            if (size3 > 0) {
                mBackStack = new BackStackState[size3];
                for (int k = 0; k < size3; ++k) {
                    mBackStack[k] = new BackStackState(this.mBackStack.get(k));
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("saveAllState: adding back stack #");
                        sb6.append(k);
                        sb6.append(": ");
                        sb6.append(this.mBackStack.get(k));
                        Log.v("FragmentManager", sb6.toString());
                    }
                }
            }
        }
        final FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.mActive = mActive2;
        fragmentManagerState.mAdded = mAdded;
        fragmentManagerState.mBackStack = mBackStack;
        final Fragment mPrimaryNav = this.mPrimaryNav;
        if (mPrimaryNav != null) {
            fragmentManagerState.mPrimaryNavActiveIndex = mPrimaryNav.mIndex;
        }
        fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
        this.saveNonConfig();
        return (Parcelable)fragmentManagerState;
    }
    
    Bundle saveFragmentBasicState(final Fragment fragment) {
        Bundle mStateBundle = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        this.dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            mStateBundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment.mView != null) {
            this.saveFragmentViewState(fragment);
        }
        if (fragment.mSavedViewState != null) {
            if (mStateBundle == null) {
                mStateBundle = new Bundle();
            }
            mStateBundle.putSparseParcelableArray("android:view_state", (SparseArray)fragment.mSavedViewState);
        }
        if (!fragment.mUserVisibleHint) {
            if (mStateBundle == null) {
                mStateBundle = new Bundle();
            }
            mStateBundle.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
            return mStateBundle;
        }
        return mStateBundle;
    }
    
    @Override
    public Fragment.SavedState saveFragmentInstanceState(final Fragment fragment) {
        if (fragment.mIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(sb.toString()));
        }
        final int mState = fragment.mState;
        final Fragment.SavedState savedState = null;
        if (mState > 0) {
            final Bundle saveFragmentBasicState = this.saveFragmentBasicState(fragment);
            Object o = savedState;
            if (saveFragmentBasicState != null) {
                o = new Fragment.SavedState(saveFragmentBasicState);
            }
            return (Fragment.SavedState)o;
        }
        return null;
    }
    
    void saveFragmentViewState(final Fragment fragment) {
        if (fragment.mInnerView == null) {
            return;
        }
        final SparseArray<Parcelable> mStateArray = this.mStateArray;
        if (mStateArray == null) {
            this.mStateArray = (SparseArray<Parcelable>)new SparseArray();
        }
        else {
            mStateArray.clear();
        }
        fragment.mInnerView.saveHierarchyState((SparseArray)this.mStateArray);
        if (this.mStateArray.size() > 0) {
            fragment.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
        }
    }
    
    void saveNonConfig() {
        final ArrayList<Fragment> list = null;
        ArrayList<Fragment> list2 = null;
        final ArrayList<FragmentManagerNonConfig> list3 = null;
        ArrayList<FragmentManagerNonConfig> list4 = null;
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); ++i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null) {
                    if (fragment.mRetainInstance) {
                        if (list2 == null) {
                            list2 = new ArrayList<Fragment>();
                        }
                        list2.add(fragment);
                        int mIndex;
                        if (fragment.mTarget != null) {
                            mIndex = fragment.mTarget.mIndex;
                        }
                        else {
                            mIndex = -1;
                        }
                        fragment.mTargetIndex = mIndex;
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("retainNonConfig: keeping retained ");
                            sb.append(fragment);
                            Log.v("FragmentManager", sb.toString());
                        }
                    }
                    FragmentManagerNonConfig fragmentManagerNonConfig;
                    if (fragment.mChildFragmentManager != null) {
                        fragment.mChildFragmentManager.saveNonConfig();
                        fragmentManagerNonConfig = fragment.mChildFragmentManager.mSavedNonConfig;
                    }
                    else {
                        fragmentManagerNonConfig = fragment.mChildNonConfig;
                    }
                    if (list4 == null && fragmentManagerNonConfig != null) {
                        list4 = new ArrayList<FragmentManagerNonConfig>(this.mActive.size());
                        for (int j = 0; j < i; ++j) {
                            list4.add(null);
                        }
                    }
                    if (list4 != null) {
                        list4.add(fragmentManagerNonConfig);
                    }
                }
            }
        }
        else {
            list4 = list3;
            list2 = list;
        }
        if (list2 == null && list4 == null) {
            this.mSavedNonConfig = null;
            return;
        }
        this.mSavedNonConfig = new FragmentManagerNonConfig(list2, list4);
    }
    
    public void setBackStackIndex(final int n, final BackStackRecord backStackRecord) {
    Label_0142_Outer:
        while (true) {
        Label_0142:
            while (true) {
                while (true) {
                    int size;
                    StringBuilder sb;
                    StringBuilder sb2;
                    StringBuilder sb3;
                    Label_0281:Block_6_Outer:
                    while (true) {
                        Label_0278: {
                            synchronized (this) {
                                if (this.mBackStackIndices != null) {
                                    break Label_0278;
                                }
                                this.mBackStackIndices = new ArrayList<BackStackRecord>();
                                size = this.mBackStackIndices.size();
                                if (n < size) {
                                    if (FragmentManagerImpl.DEBUG) {
                                        sb = new StringBuilder();
                                        sb.append("Setting back stack index ");
                                        sb.append(n);
                                        sb.append(" to ");
                                        sb.append(backStackRecord);
                                        Log.v("FragmentManager", sb.toString());
                                    }
                                    this.mBackStackIndices.set(n, backStackRecord);
                                    return;
                                }
                                break Label_0281;
                                // iftrue(Label_0284:, this.mAvailBackStackIndices != null)
                                // iftrue(Label_0184:, !FragmentManagerImpl.DEBUG)
                                // iftrue(Label_0261:, !FragmentManagerImpl.DEBUG)
                                while (true) {
                                Label_0184:
                                    while (true) {
                                        sb2 = new StringBuilder();
                                        sb2.append("Adding back stack index ");
                                        sb2.append(n);
                                        sb2.append(" with ");
                                        sb2.append(backStackRecord);
                                        Log.v("FragmentManager", sb2.toString());
                                        Block_7: {
                                            Label_0261: {
                                                break Label_0261;
                                                this.mBackStackIndices.add(null);
                                                break Block_7;
                                            }
                                            this.mBackStackIndices.add(backStackRecord);
                                            return;
                                            sb3 = new StringBuilder();
                                            sb3.append("Adding available back stack index ");
                                            sb3.append(size);
                                            Log.v("FragmentManager", sb3.toString());
                                            break Label_0184;
                                        }
                                        this.mAvailBackStackIndices = new ArrayList<Integer>();
                                        continue Label_0142;
                                        Label_0203: {
                                            continue Block_6_Outer;
                                        }
                                    }
                                    this.mAvailBackStackIndices.add(size);
                                    ++size;
                                    continue Label_0142_Outer;
                                }
                            }
                            // iftrue(Label_0203:, size >= n)
                        }
                        continue Label_0142_Outer;
                    }
                    continue;
                }
                Label_0284: {
                    continue Label_0142;
                }
            }
        }
    }
    
    public void setPrimaryNavigationFragment(final Fragment mPrimaryNav) {
        Label_0088: {
            if (mPrimaryNav != null) {
                if (this.mActive.get(mPrimaryNav.mIndex) == mPrimaryNav) {
                    if (mPrimaryNav.mHost == null) {
                        break Label_0088;
                    }
                    if (mPrimaryNav.getFragmentManager() == this) {
                        break Label_0088;
                    }
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Fragment ");
                sb.append(mPrimaryNav);
                sb.append(" is not an active fragment of FragmentManager ");
                sb.append(this);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        this.mPrimaryNav = mPrimaryNav;
    }
    
    public void showFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("show: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged ^= true;
        }
    }
    
    void startPendingDeferredFragments() {
        if (this.mActive == null) {
            return;
        }
        for (int i = 0; i < this.mActive.size(); ++i) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null) {
                this.performPendingDeferredStart(fragment);
            }
        }
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        final Fragment mParent = this.mParent;
        if (mParent != null) {
            DebugUtils.buildShortClassTag(mParent, sb);
        }
        else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }
    
    @Override
    public void unregisterFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        while (true) {
            final CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = this.mLifecycleCallbacks;
            // monitorenter(mLifecycleCallbacks)
            int n = 0;
            while (true) {
                Label_0064: {
                    try {
                        final int size = this.mLifecycleCallbacks.size();
                        if (n < size) {
                            if (this.mLifecycleCallbacks.get(n).first != fragmentLifecycleCallbacks) {
                                break Label_0064;
                            }
                            this.mLifecycleCallbacks.remove(n);
                        }
                        return;
                    }
                    finally {
                    }
                    // monitorexit(mLifecycleCallbacks)
                }
                ++n;
                continue;
            }
        }
    }
    
    private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper
    {
        View mView;
        
        AnimateOnHWLayerIfNeededListener(final View mView, final Animation$AnimationListener animation$AnimationListener) {
            super(animation$AnimationListener);
            this.mView = mView;
        }
        
        @CallSuper
        @Override
        public void onAnimationEnd(final Animation animation) {
            if (!ViewCompat.isAttachedToWindow(this.mView) && Build$VERSION.SDK_INT < 24) {
                this.mView.setLayerType(0, (Paint)null);
            }
            else {
                this.mView.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, (Paint)null);
                    }
                });
            }
            super.onAnimationEnd(animation);
        }
    }
    
    private static class AnimationListenerWrapper implements Animation$AnimationListener
    {
        private final Animation$AnimationListener mWrapped;
        
        private AnimationListenerWrapper(final Animation$AnimationListener mWrapped) {
            this.mWrapped = mWrapped;
        }
        
        @CallSuper
        public void onAnimationEnd(final Animation animation) {
            final Animation$AnimationListener mWrapped = this.mWrapped;
            if (mWrapped != null) {
                mWrapped.onAnimationEnd(animation);
            }
        }
        
        @CallSuper
        public void onAnimationRepeat(final Animation animation) {
            final Animation$AnimationListener mWrapped = this.mWrapped;
            if (mWrapped != null) {
                mWrapped.onAnimationRepeat(animation);
            }
        }
        
        @CallSuper
        public void onAnimationStart(final Animation animation) {
            final Animation$AnimationListener mWrapped = this.mWrapped;
            if (mWrapped != null) {
                mWrapped.onAnimationStart(animation);
            }
        }
    }
    
    private static class AnimationOrAnimator
    {
        public final Animation animation;
        public final Animator animator;
        
        private AnimationOrAnimator(final Animator animator) {
            this.animation = null;
            this.animator = animator;
            if (animator != null) {
                return;
            }
            throw new IllegalStateException("Animator cannot be null");
        }
        
        private AnimationOrAnimator(final Animation animation) {
            this.animation = animation;
            this.animator = null;
            if (animation != null) {
                return;
            }
            throw new IllegalStateException("Animation cannot be null");
        }
    }
    
    private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter
    {
        View mView;
        
        AnimatorOnHWLayerIfNeededListener(final View mView) {
            this.mView = mView;
        }
        
        public void onAnimationEnd(final Animator animator) {
            this.mView.setLayerType(0, (Paint)null);
            animator.removeListener((Animator$AnimatorListener)this);
        }
        
        public void onAnimationStart(final Animator animator) {
            this.mView.setLayerType(2, (Paint)null);
        }
    }
    
    static class FragmentTag
    {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;
        
        static {
            Fragment = new int[] { 16842755, 16842960, 16842961 };
        }
    }
    
    interface OpGenerator
    {
        boolean generateOps(final ArrayList<BackStackRecord> p0, final ArrayList<Boolean> p1);
    }
    
    private class PopBackStackState implements OpGenerator
    {
        final int mFlags;
        final int mId;
        final String mName;
        
        PopBackStackState(final String mName, final int mId, final int mFlags) {
            this.mName = mName;
            this.mId = mId;
            this.mFlags = mFlags;
        }
        
        @Override
        public boolean generateOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
                final FragmentManager peekChildFragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
                if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManagerImpl.this.popBackStackState(list, list2, this.mName, this.mId, this.mFlags);
        }
    }
    
    static class StartEnterTransitionListener implements OnStartEnterTransitionListener
    {
        private final boolean mIsBack;
        private int mNumPostponed;
        private final BackStackRecord mRecord;
        
        StartEnterTransitionListener(final BackStackRecord mRecord, final boolean mIsBack) {
            this.mIsBack = mIsBack;
            this.mRecord = mRecord;
        }
        
        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
        
        public void completeTransaction() {
            final int mNumPostponed = this.mNumPostponed;
            boolean b = false;
            final boolean b2 = mNumPostponed > 0;
            final FragmentManagerImpl mManager = this.mRecord.mManager;
            for (int size = mManager.mAdded.size(), i = 0; i < size; ++i) {
                final Fragment fragment = mManager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (b2 && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            final FragmentManagerImpl mManager2 = this.mRecord.mManager;
            final BackStackRecord mRecord = this.mRecord;
            final boolean mIsBack = this.mIsBack;
            if (!b2) {
                b = true;
            }
            mManager2.completeExecute(mRecord, mIsBack, b, true);
        }
        
        public boolean isReady() {
            return this.mNumPostponed == 0;
        }
        
        @Override
        public void onStartEnterTransition() {
            --this.mNumPostponed;
            if (this.mNumPostponed != 0) {
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
