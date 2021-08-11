/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorInflater
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.PropertyValuesHolder
 *  android.animation.ValueAnimator
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.graphics.Paint
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory2
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.AlphaAnimation
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationSet
 *  android.view.animation.AnimationUtils
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.view.animation.ScaleAnimation
 */
package android.support.v4.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.BackStackRecord;
import android.support.v4.app.BackStackState;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerNonConfig;
import android.support.v4.app.FragmentManagerState;
import android.support.v4.app.FragmentState;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransition;
import android.support.v4.app.LoaderManagerImpl;
import android.support.v4.app.SuperNotCalledException;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl
extends FragmentManager
implements LayoutInflater.Factory2 {
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
    final ArrayList<Fragment> mAdded = new ArrayList();
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = new CopyOnWriteArrayList();
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex = 0;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;

    static {
        DEBUG = false;
        sAnimationListenerField = null;
        DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    }

    FragmentManagerImpl() {
        this.mExecCommit = new Runnable(){

            @Override
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }

    private void addAddedFragments(ArraySet<Fragment> arraySet) {
        int n = this.mCurState;
        if (n < 1) {
            return;
        }
        int n2 = Math.min(n, 4);
        int n3 = this.mAdded.size();
        for (n = 0; n < n3; ++n) {
            Fragment fragment = this.mAdded.get(n);
            if (fragment.mState >= n2) continue;
            this.moveToState(fragment, n2, fragment.getNextAnim(), fragment.getNextTransition(), false);
            if (fragment.mView == null || fragment.mHidden || !fragment.mIsNewlyAdded) continue;
            arraySet.add(fragment);
        }
    }

    private void animateRemoveFragment(final @NonNull Fragment fragment, @NonNull AnimationOrAnimator animationOrAnimator, int n) {
        final View view = fragment.mView;
        fragment.setStateAfterAnimating(n);
        if (animationOrAnimator.animation != null) {
            Animation animation = animationOrAnimator.animation;
            fragment.setAnimatingAway(fragment.mView);
            animation.setAnimationListener((Animation.AnimationListener)new AnimationListenerWrapper(FragmentManagerImpl.getAnimationListener(animation)){

                @Override
                public void onAnimationEnd(Animation object) {
                    super.onAnimationEnd((Animation)object);
                    if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        object = FragmentManagerImpl.this;
                        Fragment fragment2 = fragment;
                        object.moveToState(fragment2, fragment2.getStateAfterAnimating(), 0, 0, false);
                        return;
                    }
                }
            });
            FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(view, animationOrAnimator);
            fragment.mView.startAnimation(animation);
            return;
        }
        Animator animator2 = animationOrAnimator.animator;
        fragment.setAnimator(animationOrAnimator.animator);
        final ViewGroup viewGroup = fragment.mContainer;
        if (viewGroup != null) {
            viewGroup.startViewTransition(view);
        }
        animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator object) {
                object = viewGroup;
                if (object != null) {
                    object.endViewTransition(view);
                }
                if (fragment.getAnimator() != null) {
                    fragment.setAnimator(null);
                    object = FragmentManagerImpl.this;
                    Fragment fragment2 = fragment;
                    object.moveToState(fragment2, fragment2.getStateAfterAnimating(), 0, 0, false);
                    return;
                }
            }
        });
        animator2.setTarget((Object)fragment.mView);
        FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
        animator2.start();
    }

    private void burpActive() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray != null) {
            for (int i = sparseArray.size() - 1; i >= 0; --i) {
                if (this.mActive.valueAt(i) != null) continue;
                sparseArray = this.mActive;
                sparseArray.delete(sparseArray.keyAt(i));
            }
            return;
        }
    }

    private void checkStateLoss() {
        if (!this.mStateSaved) {
            if (this.mNoTransactionsBecause == null) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can not perform this action inside of ");
            stringBuilder.append(this.mNoTransactionsBecause);
            throw new IllegalStateException(stringBuilder.toString());
        }
        throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    private void completeExecute(BackStackRecord backStackRecord, boolean bl, boolean bl2, boolean bl3) {
        if (bl) {
            backStackRecord.executePopOps(bl3);
        } else {
            backStackRecord.executeOps();
        }
        Object object = new ArrayList<BackStackRecord>(1);
        ArrayList<Boolean> arrayList = new ArrayList<Boolean>(1);
        object.add((BackStackRecord)backStackRecord);
        arrayList.add(bl);
        if (bl2) {
            FragmentTransition.startTransitions(this, object, arrayList, 0, 1, true);
        }
        if (bl3) {
            this.moveToState(this.mCurState, true);
        }
        if ((object = this.mActive) != null) {
            int n = object.size();
            for (int i = 0; i < n; ++i) {
                object = (Fragment)this.mActive.valueAt(i);
                if (object == null || object.mView == null || !object.mIsNewlyAdded || !backStackRecord.interactsWith(object.mContainerId)) continue;
                if (object.mPostponedAlpha > 0.0f) {
                    object.mView.setAlpha(object.mPostponedAlpha);
                }
                if (bl3) {
                    object.mPostponedAlpha = 0.0f;
                    continue;
                }
                object.mPostponedAlpha = -1.0f;
                object.mIsNewlyAdded = false;
            }
            return;
        }
    }

    private void dispatchStateChange(int n) {
        try {
            this.mExecutingActions = true;
            this.moveToState(n, false);
            this.execPendingActions();
            return;
        }
        finally {
            this.mExecutingActions = false;
        }
    }

    private void endAnimatingAwayFragments() {
        Object object = this.mActive;
        int n = object == null ? 0 : object.size();
        for (int i = 0; i < n; ++i) {
            object = (Fragment)this.mActive.valueAt(i);
            if (object == null) continue;
            if (object.getAnimatingAway() != null) {
                int n2 = object.getStateAfterAnimating();
                View view = object.getAnimatingAway();
                object.setAnimatingAway(null);
                Animation animation = view.getAnimation();
                if (animation != null) {
                    animation.cancel();
                    view.clearAnimation();
                }
                this.moveToState((Fragment)object, n2, 0, 0, false);
                continue;
            }
            if (object.getAnimator() == null) continue;
            object.getAnimator().end();
        }
    }

    private void ensureExecReady(boolean bl) {
        if (!this.mExecutingActions) {
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
        this.mTmpAddedFragments.addAll(this.mAdded);
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
            FragmentTransition.startTransitions(this, arrayList, arrayList2, n, n2, false);
        }
        FragmentManagerImpl.executeOps(arrayList, arrayList2, n, n2);
        n3 = n2;
        if (bl) {
            arrayList3 = new ArraySet<Fragment>();
            this.addAddedFragments((ArraySet<Fragment>)((Object)arrayList3));
            n3 = this.postponePostponableTransactions(arrayList, arrayList2, n, n2, (ArraySet<Fragment>)((Object)arrayList3));
            this.makeRemovedFragmentsInvisible((ArraySet<Fragment>)((Object)arrayList3));
        }
        if (n3 != n && bl) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, n, n3, true);
            this.moveToState(this.mCurState, true);
        }
        while (n < n2) {
            arrayList3 = arrayList.get(n);
            if (arrayList2.get(n).booleanValue() && arrayList3.mIndex >= 0) {
                this.freeBackStackIndex(arrayList3.mIndex);
                arrayList3.mIndex = -1;
            }
            arrayList3.runOnCommitRunnables();
            ++n;
        }
        if (n5 != 0) {
            this.reportBackStackChanged();
            return;
        }
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        ArrayList<StartEnterTransitionListener> arrayList3 = this.mPostponedTransactions;
        int n = arrayList3 == null ? 0 : arrayList3.size();
        for (int i = 0; i < n; ++i) {
            int n2;
            arrayList3 = this.mPostponedTransactions.get(i);
            if (arrayList != null && !((StartEnterTransitionListener)((Object)arrayList3)).mIsBack && (n2 = arrayList.indexOf(((StartEnterTransitionListener)((Object)arrayList3)).mRecord)) != -1 && arrayList2.get(n2).booleanValue()) {
                arrayList3.cancelTransaction();
                continue;
            }
            if (!arrayList3.isReady() && (arrayList == null || !((StartEnterTransitionListener)((Object)arrayList3)).mRecord.interactsWith(arrayList, 0, arrayList.size()))) continue;
            this.mPostponedTransactions.remove(i);
            --i;
            --n;
            if (arrayList != null && !((StartEnterTransitionListener)((Object)arrayList3)).mIsBack && (n2 = arrayList.indexOf(((StartEnterTransitionListener)((Object)arrayList3)).mRecord)) != -1 && arrayList2.get(n2).booleanValue()) {
                arrayList3.cancelTransaction();
                continue;
            }
            arrayList3.completeTransaction();
        }
    }

    private Fragment findFragmentUnder(Fragment fragment) {
        ViewGroup viewGroup = fragment.mContainer;
        View view = fragment.mView;
        if (viewGroup != null) {
            if (view == null) {
                return null;
            }
            for (int i = this.mAdded.indexOf((Object)fragment) - 1; i >= 0; --i) {
                fragment = this.mAdded.get(i);
                if (fragment.mContainer != viewGroup || fragment.mView == null) continue;
                return fragment;
            }
            return null;
        }
        return null;
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        boolean bl = false;
        synchronized (this) {
            if (this.mPendingActions != null && this.mPendingActions.size() != 0) {
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
            return false;
        }
    }

    private static Animation.AnimationListener getAnimationListener(Animation animation) {
        try {
            if (sAnimationListenerField == null) {
                sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                sAnimationListenerField.setAccessible(true);
            }
            animation = (Animation.AnimationListener)sAnimationListenerField.get((Object)animation);
            return animation;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.e((String)"FragmentManager", (String)"Cannot access Animation's mListener field", (Throwable)illegalAccessException);
            return null;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.e((String)"FragmentManager", (String)"No field with the name mListener is found in Animation class", (Throwable)noSuchFieldException);
            return null;
        }
    }

    static AnimationOrAnimator makeFadeAnimation(Context context, float f, float f2) {
        context = new AlphaAnimation(f, f2);
        context.setInterpolator(DECELERATE_CUBIC);
        context.setDuration(220L);
        return new AnimationOrAnimator((Animation)context);
    }

    static AnimationOrAnimator makeOpenCloseAnimation(Context context, float f, float f2, float f3, float f4) {
        context = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(f, f2, f, f2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        context.addAnimation((Animation)scaleAnimation);
        scaleAnimation = new AlphaAnimation(f3, f4);
        scaleAnimation.setInterpolator(DECELERATE_CUBIC);
        scaleAnimation.setDuration(220L);
        context.addAnimation((Animation)scaleAnimation);
        return new AnimationOrAnimator((Animation)context);
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> arraySet) {
        int n = arraySet.size();
        for (int i = 0; i < n; ++i) {
            Fragment fragment = arraySet.valueAt(i);
            if (fragment.mAdded) continue;
            View view = fragment.getView();
            fragment.mPostponedAlpha = view.getAlpha();
            view.setAlpha(0.0f);
        }
    }

    static boolean modifiesAlpha(Animator object) {
        if (object == null) {
            return false;
        }
        if (object instanceof ValueAnimator) {
            object = ((ValueAnimator)object).getValues();
            for (int i = 0; i < object.length; ++i) {
                if (!"alpha".equals(object[i].getPropertyName())) continue;
                return true;
            }
        } else if (object instanceof AnimatorSet) {
            object = ((AnimatorSet)object).getChildAnimations();
            for (int i = 0; i < object.size(); ++i) {
                if (!FragmentManagerImpl.modifiesAlpha((Animator)object.get(i))) continue;
                return true;
            }
            return false;
        }
        return false;
    }

    static boolean modifiesAlpha(AnimationOrAnimator object) {
        if (object.animation instanceof AlphaAnimation) {
            return true;
        }
        if (object.animation instanceof AnimationSet) {
            object = ((AnimationSet)object.animation).getAnimations();
            for (int i = 0; i < object.size(); ++i) {
                if (!(object.get(i) instanceof AlphaAnimation)) continue;
                return true;
            }
            return false;
        }
        return FragmentManagerImpl.modifiesAlpha(object.animator);
    }

    private boolean popBackStackImmediate(String string2, int n, int n2) {
        boolean bl;
        this.execPendingActions();
        this.ensureExecReady(true);
        Object object = this.mPrimaryNav;
        if (object != null && n < 0 && string2 == null && (object = object.peekChildFragmentManager()) != null && object.popBackStackImmediate()) {
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
        this.doPendingDeferredStart();
        this.burpActive();
        return bl;
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2, ArraySet<Fragment> arraySet) {
        int n3 = n2;
        for (int i = n2 - 1; i >= n; --i) {
            BackStackRecord backStackRecord = arrayList.get(i);
            boolean bl = arrayList2.get(i);
            boolean bl2 = backStackRecord.isPostponed() && !backStackRecord.interactsWith(arrayList, i + 1, n2);
            if (!bl2) continue;
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
            if (i != --n3) {
                arrayList.remove(i);
                arrayList.add(n3, backStackRecord);
            }
            this.addAddedFragments(arraySet);
        }
        return n3;
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (arrayList != null) {
            if (arrayList.isEmpty()) {
                return;
            }
            if (arrayList2 != null && arrayList.size() == arrayList2.size()) {
                this.executePostponedTransaction(arrayList, arrayList2);
                int n = arrayList.size();
                int n2 = 0;
                for (int i = 0; i < n; ++i) {
                    if (arrayList.get((int)i).mReorderingAllowed) continue;
                    if (n2 != i) {
                        this.executeOpsTogether(arrayList, arrayList2, n2, i);
                    }
                    if (arrayList2.get(i).booleanValue()) {
                        for (n2 = i + 1; n2 < n && arrayList2.get(n2).booleanValue() && !arrayList.get((int)n2).mReorderingAllowed; ++n2) {
                        }
                    }
                    this.executeOpsTogether(arrayList, arrayList2, i, n2);
                    i = n2;
                    int n3 = n2 - 1;
                    n2 = i;
                    i = n3;
                }
                if (n2 != n) {
                    this.executeOpsTogether(arrayList, arrayList2, n2, n);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Internal error with the back stack records");
        }
    }

    public static int reverseTransit(int n) {
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void scheduleCommit() {
        synchronized (this) {
            block7 : {
                block6 : {
                    ArrayList<StartEnterTransitionListener> arrayList = this.mPostponedTransactions;
                    boolean bl = false;
                    boolean bl2 = arrayList != null && !this.mPostponedTransactions.isEmpty();
                    if (this.mPendingActions == null) break block6;
                    if (this.mPendingActions.size() == 1) {
                        bl = true;
                    }
                    if (!bl2 && !bl) break block7;
                }
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
            return;
        }
    }

    private static void setHWLayerAnimListenerIfAlpha(View view, AnimationOrAnimator animationOrAnimator) {
        if (view != null) {
            if (animationOrAnimator == null) {
                return;
            }
            if (FragmentManagerImpl.shouldRunOnHWLayer(view, animationOrAnimator)) {
                if (animationOrAnimator.animator != null) {
                    animationOrAnimator.animator.addListener((Animator.AnimatorListener)new AnimatorOnHWLayerIfNeededListener(view));
                    return;
                }
                Animation.AnimationListener animationListener = FragmentManagerImpl.getAnimationListener(animationOrAnimator.animation);
                view.setLayerType(2, null);
                animationOrAnimator.animation.setAnimationListener((Animation.AnimationListener)new AnimateOnHWLayerIfNeededListener(view, animationListener));
                return;
            }
            return;
        }
    }

    private static void setRetaining(FragmentManagerNonConfig iterator) {
        if (iterator == null) {
            return;
        }
        List<Fragment> list = iterator.getFragments();
        if (list != null) {
            list = list.iterator();
            while (list.hasNext()) {
                ((Fragment)list.next()).mRetaining = true;
            }
        }
        if ((iterator = iterator.getChildNonConfigs()) != null) {
            iterator = iterator.iterator();
            while (iterator.hasNext()) {
                FragmentManagerImpl.setRetaining((FragmentManagerNonConfig)iterator.next());
            }
            return;
        }
    }

    static boolean shouldRunOnHWLayer(View view, AnimationOrAnimator animationOrAnimator) {
        if (view != null) {
            if (animationOrAnimator == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 19 && view.getLayerType() == 0 && ViewCompat.hasOverlappingRendering(view) && FragmentManagerImpl.modifiesAlpha(animationOrAnimator)) {
                return true;
            }
            return false;
        }
        return false;
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

    public static int transitToStyleIndex(int n, boolean bl) {
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    return -1;
                }
                n = bl ? 3 : 4;
                return n;
            }
            n = bl ? 5 : 6;
            return n;
        }
        n = bl ? 1 : 2;
        return n;
    }

    void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(backStackRecord);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void addFragment(Fragment fragment, boolean bl) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("add: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (this.mAdded.contains(fragment)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment already added: ");
            stringBuilder.append(fragment);
            throw new IllegalStateException(stringBuilder.toString());
        }
        stringBuilder = this.mAdded;
        synchronized (stringBuilder) {
            this.mAdded.add(fragment);
        }
        fragment.mAdded = true;
        fragment.mRemoving = false;
        if (fragment.mView == null) {
            fragment.mHiddenChanged = false;
        }
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        if (bl) {
            this.moveToState(fragment);
            return;
        }
    }

    @Override
    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int allocBackStackIndex(BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                int n = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Adding back stack index ");
                    stringBuilder.append(n);
                    stringBuilder.append(" with ");
                    stringBuilder.append(backStackRecord);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                this.mBackStackIndices.set(n, backStackRecord);
                return n;
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int n = this.mBackStackIndices.size();
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Setting back stack index ");
                stringBuilder.append(n);
                stringBuilder.append(" to ");
                stringBuilder.append(backStackRecord);
                Log.v((String)"FragmentManager", (String)stringBuilder.toString());
            }
            this.mBackStackIndices.add(backStackRecord);
            return n;
        }
    }

    public void attachController(FragmentHostCallback fragmentHostCallback, FragmentContainer fragmentContainer, Fragment fragment) {
        if (this.mHost == null) {
            this.mHost = fragmentHostCallback;
            this.mContainer = fragmentContainer;
            this.mParent = fragment;
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void attachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("attach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (!fragment.mDetached) {
            return;
        }
        fragment.mDetached = false;
        if (fragment.mAdded) {
            return;
        }
        if (this.mAdded.contains(fragment)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment already added: ");
            stringBuilder.append(fragment);
            throw new IllegalStateException(stringBuilder.toString());
        }
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("add from attach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        stringBuilder = this.mAdded;
        synchronized (stringBuilder) {
            this.mAdded.add(fragment);
        }
        fragment.mAdded = true;
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
            return;
        }
    }

    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            AnimationOrAnimator animationOrAnimator = this.loadAnimation(fragment, fragment.getNextTransition(), fragment.mHidden ^ true, fragment.getNextTransitionStyle());
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

                            public void onAnimationEnd(Animator animator2) {
                                viewGroup.endViewTransition(view);
                                animator2.removeListener((Animator.AnimatorListener)this);
                                if (fragment.mView != null) {
                                    fragment.mView.setVisibility(8);
                                    return;
                                }
                            }
                        });
                    }
                } else {
                    fragment.mView.setVisibility(0);
                }
                FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
                animationOrAnimator.animator.start();
            } else {
                if (animationOrAnimator != null) {
                    FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
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
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void detachFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("detach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (fragment.mDetached) return;
        fragment.mDetached = true;
        if (!fragment.mAdded) return;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("remove from detach: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        stringBuilder = this.mAdded;
        // MONITORENTER : stringBuilder
        this.mAdded.remove(fragment);
        // MONITOREXIT : stringBuilder
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mAdded = false;
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.dispatchStateChange(2);
    }

    public void dispatchConfigurationChanged(Configuration configuration) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performConfigurationChanged(configuration);
        }
    }

    public boolean dispatchContextItemSelected(MenuItem menuItem) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null || !fragment.performContextItemSelected(menuItem)) continue;
            return true;
        }
        return false;
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        this.dispatchStateChange(1);
    }

    public boolean dispatchCreateOptionsMenu(Menu object, MenuInflater menuInflater) {
        int n;
        boolean bl = false;
        ArrayList<Fragment> arrayList = null;
        for (n = 0; n < this.mAdded.size(); ++n) {
            Fragment fragment = this.mAdded.get(n);
            if (fragment == null || !fragment.performCreateOptionsMenu((Menu)object, menuInflater)) continue;
            bl = true;
            if (arrayList == null) {
                arrayList = new ArrayList<Fragment>();
            }
            arrayList.add(fragment);
        }
        if (this.mCreatedMenus != null) {
            for (n = 0; n < this.mCreatedMenus.size(); ++n) {
                object = this.mCreatedMenus.get(n);
                if (arrayList != null && arrayList.contains(object)) continue;
                object.onDestroyOptionsMenu();
            }
        }
        this.mCreatedMenus = arrayList;
        return bl;
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
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performLowMemory();
        }
    }

    public void dispatchMultiWindowModeChanged(boolean bl) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performMultiWindowModeChanged(bl);
        }
    }

    void dispatchOnFragmentActivityCreated(Fragment fragment, Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentActivityCreated(fragment, bundle, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentActivityCreated(this, fragment, bundle);
        }
    }

    void dispatchOnFragmentAttached(Fragment fragment, Context context, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentAttached(fragment, context, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentAttached(this, fragment, context);
        }
    }

    void dispatchOnFragmentCreated(Fragment fragment, Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentCreated(fragment, bundle, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentCreated(this, fragment, bundle);
        }
    }

    void dispatchOnFragmentDestroyed(Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentDestroyed(fragment, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentDestroyed(this, fragment);
        }
    }

    void dispatchOnFragmentDetached(Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentDetached(fragment, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentDetached(this, fragment);
        }
    }

    void dispatchOnFragmentPaused(Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentPaused(fragment, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentPaused(this, fragment);
        }
    }

    void dispatchOnFragmentPreAttached(Fragment fragment, Context context, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentPreAttached(fragment, context, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentPreAttached(this, fragment, context);
        }
    }

    void dispatchOnFragmentPreCreated(Fragment fragment, Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentPreCreated(fragment, bundle, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentPreCreated(this, fragment, bundle);
        }
    }

    void dispatchOnFragmentResumed(Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentResumed(fragment, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentResumed(this, fragment);
        }
    }

    void dispatchOnFragmentSaveInstanceState(Fragment fragment, Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentSaveInstanceState(this, fragment, bundle);
        }
    }

    void dispatchOnFragmentStarted(Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentStarted(fragment, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentStarted(this, fragment);
        }
    }

    void dispatchOnFragmentStopped(Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentStopped(fragment, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentStopped(this, fragment);
        }
    }

    void dispatchOnFragmentViewCreated(Fragment fragment, View view, Bundle bundle, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentViewCreated(this, fragment, view, bundle);
        }
    }

    void dispatchOnFragmentViewDestroyed(Fragment fragment, boolean bl) {
        Object object = this.mParent;
        if (object != null && (object = object.getFragmentManager()) instanceof FragmentManagerImpl) {
            ((FragmentManagerImpl)object).dispatchOnFragmentViewDestroyed(fragment, true);
        }
        for (Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
            if (bl && !((Boolean)pair.second).booleanValue()) continue;
            ((FragmentManager.FragmentLifecycleCallbacks)pair.first).onFragmentViewDestroyed(this, fragment);
        }
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null || !fragment.performOptionsItemSelected(menuItem)) continue;
            return true;
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performOptionsMenuClosed(menu);
        }
    }

    public void dispatchPause() {
        this.dispatchStateChange(4);
    }

    public void dispatchPictureInPictureModeChanged(boolean bl) {
        for (int i = this.mAdded.size() - 1; i >= 0; --i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.performPictureInPictureModeChanged(bl);
        }
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        boolean bl = false;
        for (int i = 0; i < this.mAdded.size(); ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null || !fragment.performPrepareOptionsMenu(menu)) continue;
            bl = true;
        }
        return bl;
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
        if (this.mHavePendingDeferredStart) {
            boolean bl = false;
            for (int i = 0; i < this.mActive.size(); ++i) {
                Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment == null || fragment.mLoaderManager == null) continue;
                bl |= fragment.mLoaderManager.hasRunningLoaders();
            }
            if (!bl) {
                this.mHavePendingDeferredStart = false;
                this.startPendingDeferredFragments();
                return;
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void dump(String string2, FileDescriptor object, PrintWriter printWriter, String[] arrstring) {
        int n;
        int n2;
        CharSequence charSequence = new StringBuilder();
        charSequence.append(string2);
        charSequence.append("    ");
        charSequence = charSequence.toString();
        Object object2 = this.mActive;
        if (object2 != null && (n2 = object2.size()) > 0) {
            printWriter.print(string2);
            printWriter.print("Active Fragments in ");
            printWriter.print(Integer.toHexString(System.identityHashCode(this)));
            printWriter.println(":");
            for (n = 0; n < n2; ++n) {
                object2 = (Fragment)this.mActive.valueAt(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(object2);
                if (object2 == null) continue;
                object2.dump((String)charSequence, (FileDescriptor)object, printWriter, arrstring);
            }
        }
        if ((n2 = this.mAdded.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Added Fragments:");
            for (n = 0; n < n2; ++n) {
                object2 = this.mAdded.get(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(object2.toString());
            }
        }
        if ((object2 = this.mCreatedMenus) != null && (n2 = object2.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Fragments Created Menus:");
            for (n = 0; n < n2; ++n) {
                object2 = this.mCreatedMenus.get(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(object2.toString());
            }
        }
        if ((object2 = this.mBackStack) != null && (n2 = object2.size()) > 0) {
            printWriter.print(string2);
            printWriter.println("Back Stack:");
            for (n = 0; n < n2; ++n) {
                object2 = this.mBackStack.get(n);
                printWriter.print(string2);
                printWriter.print("  #");
                printWriter.print(n);
                printWriter.print(": ");
                printWriter.println(object2.toString());
                object2.dump((String)charSequence, (FileDescriptor)object, printWriter, arrstring);
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null && (n2 = this.mBackStackIndices.size()) > 0) {
                printWriter.print(string2);
                printWriter.println("Back Stack Indices:");
                for (n = 0; n < n2; ++n) {
                    object = this.mBackStackIndices.get(n);
                    printWriter.print(string2);
                    printWriter.print("  #");
                    printWriter.print(n);
                    printWriter.print(": ");
                    printWriter.println(object);
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(string2);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        object = this.mPendingActions;
        if (object != null && (n2 = object.size()) > 0) {
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
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(string2);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            printWriter.print(string2);
            printWriter.print("  mNoTransactionsBecause=");
            printWriter.println(this.mNoTransactionsBecause);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void enqueueAction(OpGenerator opGenerator, boolean bl) {
        if (!bl) {
            this.checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed && this.mHost != null) {
                if (this.mPendingActions == null) {
                    this.mPendingActions = new ArrayList();
                }
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

    void ensureInflatedFragmentView(Fragment fragment) {
        if (fragment.mFromLayout && !fragment.mPerformedCreateView) {
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
            return;
        }
    }

    public boolean execPendingActions() {
        this.ensureExecReady(true);
        boolean bl = false;
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
        this.doPendingDeferredStart();
        this.burpActive();
        return bl;
    }

    public void execSingleAction(OpGenerator opGenerator, boolean bl) {
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
        this.doPendingDeferredStart();
        this.burpActive();
    }

    @Override
    public boolean executePendingTransactions() {
        boolean bl = this.execPendingActions();
        this.forcePostponedTransactions();
        return bl;
    }

    @Override
    public Fragment findFragmentById(int n) {
        int n2;
        Object object;
        for (n2 = this.mAdded.size() - 1; n2 >= 0; --n2) {
            object = this.mAdded.get(n2);
            if (object == null || object.mFragmentId != n) continue;
            return object;
        }
        object = this.mActive;
        if (object != null) {
            for (n2 = object.size() - 1; n2 >= 0; --n2) {
                object = (Fragment)this.mActive.valueAt(n2);
                if (object == null || object.mFragmentId != n) continue;
                return object;
            }
        }
        return null;
    }

    @Override
    public Fragment findFragmentByTag(String string2) {
        int n;
        Object object;
        if (string2 != null) {
            for (n = this.mAdded.size() - 1; n >= 0; --n) {
                object = this.mAdded.get(n);
                if (object == null || !string2.equals(object.mTag)) continue;
                return object;
            }
        }
        if ((object = this.mActive) != null && string2 != null) {
            for (n = object.size() - 1; n >= 0; --n) {
                object = (Fragment)this.mActive.valueAt(n);
                if (object == null || !string2.equals(object.mTag)) continue;
                return object;
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String string2) {
        Object object = this.mActive;
        if (object != null && string2 != null) {
            for (int i = object.size() - 1; i >= 0; --i) {
                object = (Fragment)this.mActive.valueAt(i);
                if (object == null || (object = object.findFragmentByWho(string2)) == null) continue;
                return object;
            }
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void freeBackStackIndex(int n) {
        synchronized (this) {
            this.mBackStackIndices.set(n, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList();
            }
            if (DEBUG) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Freeing back stack index ");
                stringBuilder.append(n);
                Log.v((String)"FragmentManager", (String)stringBuilder.toString());
            }
            this.mAvailBackStackIndices.add(n);
            return;
        }
    }

    int getActiveFragmentCount() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null) {
            return 0;
        }
        return sparseArray.size();
    }

    List<Fragment> getActiveFragments() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null) {
            return null;
        }
        int n = sparseArray.size();
        sparseArray = new ArrayList(n);
        for (int i = 0; i < n; ++i) {
            sparseArray.add((Fragment)this.mActive.valueAt(i));
        }
        return sparseArray;
    }

    @Override
    public FragmentManager.BackStackEntry getBackStackEntryAt(int n) {
        return this.mBackStack.get(n);
    }

    @Override
    public int getBackStackEntryCount() {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public Fragment getFragment(Bundle object, String string2) {
        int n = object.getInt(string2, -1);
        if (n == -1) {
            return null;
        }
        object = (Fragment)this.mActive.get(n);
        if (object == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment no longer exists for key ");
            stringBuilder.append(string2);
            stringBuilder.append(": index ");
            stringBuilder.append(n);
            this.throwException(new IllegalStateException(stringBuilder.toString()));
            return object;
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<Fragment> getFragments() {
        if (this.mAdded.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        ArrayList<Fragment> arrayList = this.mAdded;
        synchronized (arrayList) {
            return (List)this.mAdded.clone();
        }
    }

    LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this;
    }

    @Override
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hide: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
            return;
        }
    }

    @Override
    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    boolean isStateAtLeast(int n) {
        if (this.mCurState >= n) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isStateSaved() {
        return this.mStateSaved;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    AnimationOrAnimator loadAnimation(Fragment object, int n, boolean bl, int n2) {
        block21 : {
            boolean bl2;
            int n3;
            block22 : {
                n3 = object.getNextAnim();
                Animation animation = object.onCreateAnimation(n, bl, n3);
                if (animation != null) {
                    return new AnimationOrAnimator(animation);
                }
                if ((object = object.onCreateAnimator(n, bl, n3)) != null) {
                    return new AnimationOrAnimator((Animator)object);
                }
                if (n3 == 0) break block21;
                bl2 = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(n3));
                boolean bl3 = false;
                if (!bl2) break block22;
                try {
                    object = AnimationUtils.loadAnimation((Context)this.mHost.getContext(), (int)n3);
                    if (object != null) {
                        return new AnimationOrAnimator((Animation)object);
                    }
                    bl3 = true;
                }
                catch (RuntimeException runtimeException) {
                }
                catch (Resources.NotFoundException notFoundException) {
                    throw notFoundException;
                }
                if (bl3) break block21;
            }
            try {
                object = AnimatorInflater.loadAnimator((Context)this.mHost.getContext(), (int)n3);
                if (object != null) {
                    return new AnimationOrAnimator((Animator)object);
                }
            }
            catch (RuntimeException runtimeException) {
                if (bl2) {
                    throw runtimeException;
                }
                Animation animation = AnimationUtils.loadAnimation((Context)this.mHost.getContext(), (int)n3);
                if (animation == null) break block21;
                return new AnimationOrAnimator(animation);
            }
        }
        if (n == 0) {
            return null;
        }
        if ((n = FragmentManagerImpl.transitToStyleIndex(n, bl)) < 0) {
            return null;
        }
        switch (n) {
            default: {
                if (n2 != 0 || !this.mHost.onHasWindowAnimations()) break;
                n2 = this.mHost.onGetWindowAnimations();
                break;
            }
            case 6: {
                return FragmentManagerImpl.makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            }
            case 5: {
                return FragmentManagerImpl.makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            }
            case 4: {
                return FragmentManagerImpl.makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            }
            case 3: {
                return FragmentManagerImpl.makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            }
            case 2: {
                return FragmentManagerImpl.makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            }
            case 1: {
                return FragmentManagerImpl.makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            }
        }
        if (n2 != 0) return null;
        return null;
    }

    void makeActive(Fragment fragment) {
        if (fragment.mIndex >= 0) {
            return;
        }
        int n = this.mNextFragmentIndex;
        this.mNextFragmentIndex = n + 1;
        fragment.setIndex(n, this.mParent);
        if (this.mActive == null) {
            this.mActive = new SparseArray();
        }
        this.mActive.put(fragment.mIndex, (Object)fragment);
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Allocated fragment index ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
    }

    void makeInactive(Fragment fragment) {
        if (fragment.mIndex < 0) {
            return;
        }
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Freeing fragment index ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.mActive.put(fragment.mIndex, (Object)null);
        this.mHost.inactivateFragment(fragment.mWho);
        fragment.initState();
    }

    void moveFragmentToExpectedState(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        int n = this.mCurState;
        if (fragment.mRemoving) {
            n = fragment.isInBackStack() ? Math.min(n, 1) : Math.min(n, 0);
        }
        this.moveToState(fragment, n, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
        if (fragment.mView != null) {
            Object object = this.findFragmentUnder(fragment);
            if (object != null) {
                object = object.mView;
                ViewGroup viewGroup = fragment.mContainer;
                n = viewGroup.indexOfChild((View)object);
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
                object = this.loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                if (object != null) {
                    FragmentManagerImpl.setHWLayerAnimListenerIfAlpha(fragment.mView, (AnimationOrAnimator)object);
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
            return;
        }
    }

    void moveToState(int n, boolean bl) {
        if (this.mHost == null && n != 0) {
            throw new IllegalStateException("No activity");
        }
        if (!bl && n == this.mCurState) {
            return;
        }
        this.mCurState = n;
        if (this.mActive != null) {
            int n2;
            Object object;
            n = 0;
            int n3 = this.mAdded.size();
            for (n2 = 0; n2 < n3; ++n2) {
                object = this.mAdded.get(n2);
                this.moveFragmentToExpectedState((Fragment)object);
                if (object.mLoaderManager == null) continue;
                n |= object.mLoaderManager.hasRunningLoaders();
            }
            n3 = this.mActive.size();
            for (n2 = 0; n2 < n3; ++n2) {
                object = (Fragment)this.mActive.valueAt(n2);
                if (object == null || !object.mRemoving && !object.mDetached || object.mIsNewlyAdded) continue;
                this.moveFragmentToExpectedState((Fragment)object);
                if (object.mLoaderManager == null) continue;
                n |= object.mLoaderManager.hasRunningLoaders();
            }
            if (n == 0) {
                this.startPendingDeferredFragments();
            }
            if (this.mNeedMenuInvalidate && (object = this.mHost) != null && this.mCurState == 5) {
                object.onSupportInvalidateOptionsMenu();
                this.mNeedMenuInvalidate = false;
                return;
            }
            return;
        }
    }

    void moveToState(Fragment fragment) {
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }

    void moveToState(Fragment fragment, int n, int n2, int n3, boolean bl) {
        Object object;
        block75 : {
            block74 : {
                block76 : {
                    block77 : {
                        block78 : {
                            Fragment fragment2;
                            boolean bl2 = fragment.mAdded;
                            boolean bl3 = true;
                            if (!bl2 || fragment.mDetached) {
                                if (n > 1) {
                                    n = 1;
                                }
                            }
                            if (fragment.mRemoving && n > fragment.mState) {
                                n = fragment.mState == 0 && fragment.isInBackStack() ? 1 : fragment.mState;
                            }
                            if (fragment.mDeferStart && fragment.mState < 4 && n > 3) {
                                n = 3;
                            }
                            if (fragment.mState > n) break block74;
                            if (fragment.mFromLayout && !fragment.mInLayout) {
                                return;
                            }
                            if (fragment.getAnimatingAway() != null || fragment.getAnimator() != null) {
                                fragment.setAnimatingAway(null);
                                fragment.setAnimator(null);
                                this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, true);
                            }
                            switch (fragment.mState) {
                                default: {
                                    break block75;
                                }
                                case 4: {
                                    break block76;
                                }
                                case 3: {
                                    break block77;
                                }
                                case 2: {
                                    break block78;
                                }
                                case 1: {
                                    break;
                                }
                                case 0: {
                                    if (n <= 0) break;
                                    if (DEBUG) {
                                        object = new StringBuilder();
                                        object.append("moveto CREATED: ");
                                        object.append(fragment);
                                        Log.v((String)"FragmentManager", (String)object.toString());
                                    }
                                    if (fragment.mSavedFragmentState != null) {
                                        fragment.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                                        fragment.mSavedViewState = fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                                        fragment.mTarget = this.getFragment(fragment.mSavedFragmentState, "android:target_state");
                                        if (fragment.mTarget != null) {
                                            fragment.mTargetRequestCode = fragment.mSavedFragmentState.getInt("android:target_req_state", 0);
                                        }
                                        fragment.mUserVisibleHint = fragment.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                                        if (!fragment.mUserVisibleHint) {
                                            fragment.mDeferStart = true;
                                            if (n > 3) {
                                                n = 3;
                                            }
                                        }
                                    }
                                    fragment.mHost = object = this.mHost;
                                    fragment.mParentFragment = fragment2 = this.mParent;
                                    object = fragment2 != null ? fragment2.mChildFragmentManager : object.getFragmentManagerImpl();
                                    fragment.mFragmentManager = object;
                                    if (fragment.mTarget != null) {
                                        if (this.mActive.get(fragment.mTarget.mIndex) == fragment.mTarget) {
                                            if (fragment.mTarget.mState < 1) {
                                                this.moveToState(fragment.mTarget, 1, 0, 0, true);
                                            }
                                        } else {
                                            object = new StringBuilder();
                                            object.append("Fragment ");
                                            object.append(fragment);
                                            object.append(" declared target fragment ");
                                            object.append(fragment.mTarget);
                                            object.append(" that does not belong to this FragmentManager!");
                                            throw new IllegalStateException(object.toString());
                                        }
                                    }
                                    this.dispatchOnFragmentPreAttached(fragment, this.mHost.getContext(), false);
                                    fragment.mCalled = false;
                                    fragment.onAttach(this.mHost.getContext());
                                    if (fragment.mCalled) {
                                        if (fragment.mParentFragment == null) {
                                            this.mHost.onAttachFragment(fragment);
                                        } else {
                                            fragment.mParentFragment.onAttachFragment(fragment);
                                        }
                                        this.dispatchOnFragmentAttached(fragment, this.mHost.getContext(), false);
                                        if (!fragment.mIsCreated) {
                                            this.dispatchOnFragmentPreCreated(fragment, fragment.mSavedFragmentState, false);
                                            fragment.performCreate(fragment.mSavedFragmentState);
                                            this.dispatchOnFragmentCreated(fragment, fragment.mSavedFragmentState, false);
                                        } else {
                                            fragment.restoreChildFragmentState(fragment.mSavedFragmentState);
                                            fragment.mState = 1;
                                        }
                                        fragment.mRetaining = false;
                                        break;
                                    }
                                    object = new StringBuilder();
                                    object.append("Fragment ");
                                    object.append(fragment);
                                    object.append(" did not call through to super.onAttach()");
                                    throw new SuperNotCalledException(object.toString());
                                }
                            }
                            this.ensureInflatedFragmentView(fragment);
                            if (n > 1) {
                                if (DEBUG) {
                                    object = new StringBuilder();
                                    object.append("moveto ACTIVITY_CREATED: ");
                                    object.append(fragment);
                                    Log.v((String)"FragmentManager", (String)object.toString());
                                }
                                if (!fragment.mFromLayout) {
                                    object = null;
                                    if (fragment.mContainerId != 0) {
                                        if (fragment.mContainerId == -1) {
                                            object = new StringBuilder();
                                            object.append("Cannot create fragment ");
                                            object.append(fragment);
                                            object.append(" for a container view with no id");
                                            this.throwException(new IllegalArgumentException(object.toString()));
                                        }
                                        if ((fragment2 = (ViewGroup)this.mContainer.onFindViewById(fragment.mContainerId)) == null && !fragment.mRestored) {
                                            try {
                                                object = fragment.getResources().getResourceName(fragment.mContainerId);
                                            }
                                            catch (Resources.NotFoundException notFoundException) {
                                                object = "unknown";
                                            }
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append("No view found for id 0x");
                                            stringBuilder.append(Integer.toHexString(fragment.mContainerId));
                                            stringBuilder.append(" (");
                                            stringBuilder.append((String)object);
                                            stringBuilder.append(") for fragment ");
                                            stringBuilder.append(fragment);
                                            this.throwException(new IllegalArgumentException(stringBuilder.toString()));
                                        }
                                        object = fragment2;
                                    }
                                    fragment.mContainer = object;
                                    fragment.mView = fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), (ViewGroup)object, fragment.mSavedFragmentState);
                                    if (fragment.mView != null) {
                                        fragment.mInnerView = fragment.mView;
                                        fragment.mView.setSaveFromParentEnabled(false);
                                        if (object != null) {
                                            object.addView(fragment.mView);
                                        }
                                        if (fragment.mHidden) {
                                            fragment.mView.setVisibility(8);
                                        }
                                        fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                                        this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
                                        bl = fragment.mView.getVisibility() == 0 && fragment.mContainer != null ? bl3 : false;
                                        fragment.mIsNewlyAdded = bl;
                                    } else {
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
                        if (n > 2) {
                            fragment.mState = 3;
                        }
                    }
                    if (n > 3) {
                        if (DEBUG) {
                            object = new StringBuilder();
                            object.append("moveto STARTED: ");
                            object.append(fragment);
                            Log.v((String)"FragmentManager", (String)object.toString());
                        }
                        fragment.performStart();
                        this.dispatchOnFragmentStarted(fragment, false);
                    }
                }
                if (n <= 4) break block75;
                if (DEBUG) {
                    object = new StringBuilder();
                    object.append("moveto RESUMED: ");
                    object.append(fragment);
                    Log.v((String)"FragmentManager", (String)object.toString());
                }
                fragment.performResume();
                this.dispatchOnFragmentResumed(fragment, false);
                fragment.mSavedFragmentState = null;
                fragment.mSavedViewState = null;
                break block75;
            }
            if (fragment.mState > n) {
                switch (fragment.mState) {
                    default: {
                        break;
                    }
                    case 5: {
                        if (n < 5) {
                            if (DEBUG) {
                                object = new StringBuilder();
                                object.append("movefrom RESUMED: ");
                                object.append(fragment);
                                Log.v((String)"FragmentManager", (String)object.toString());
                            }
                            fragment.performPause();
                            this.dispatchOnFragmentPaused(fragment, false);
                        }
                    }
                    case 4: {
                        if (n < 4) {
                            if (DEBUG) {
                                object = new StringBuilder();
                                object.append("movefrom STARTED: ");
                                object.append(fragment);
                                Log.v((String)"FragmentManager", (String)object.toString());
                            }
                            fragment.performStop();
                            this.dispatchOnFragmentStopped(fragment, false);
                        }
                    }
                    case 3: {
                        if (n < 3) {
                            if (DEBUG) {
                                object = new StringBuilder();
                                object.append("movefrom STOPPED: ");
                                object.append(fragment);
                                Log.v((String)"FragmentManager", (String)object.toString());
                            }
                            fragment.performReallyStop();
                        }
                    }
                    case 2: {
                        if (n < 2) {
                            if (DEBUG) {
                                object = new StringBuilder();
                                object.append("movefrom ACTIVITY_CREATED: ");
                                object.append(fragment);
                                Log.v((String)"FragmentManager", (String)object.toString());
                            }
                            if (fragment.mView != null && this.mHost.onShouldSaveFragmentState(fragment) && fragment.mSavedViewState == null) {
                                this.saveFragmentViewState(fragment);
                            }
                            fragment.performDestroyView();
                            this.dispatchOnFragmentViewDestroyed(fragment, false);
                            if (fragment.mView != null && fragment.mContainer != null) {
                                fragment.mView.clearAnimation();
                                fragment.mContainer.endViewTransition(fragment.mView);
                                object = null;
                                if (this.mCurState > 0 && !this.mDestroyed && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f) {
                                    object = this.loadAnimation(fragment, n2, false, n3);
                                }
                                fragment.mPostponedAlpha = 0.0f;
                                if (object != null) {
                                    this.animateRemoveFragment(fragment, (AnimationOrAnimator)object, n);
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
                        if (n >= 1) break;
                        if (this.mDestroyed) {
                            if (fragment.getAnimatingAway() != null) {
                                object = fragment.getAnimatingAway();
                                fragment.setAnimatingAway(null);
                                object.clearAnimation();
                            } else if (fragment.getAnimator() != null) {
                                object = fragment.getAnimator();
                                fragment.setAnimator(null);
                                object.cancel();
                            }
                        }
                        if (fragment.getAnimatingAway() == null && fragment.getAnimator() == null) {
                            if (DEBUG) {
                                object = new StringBuilder();
                                object.append("movefrom CREATED: ");
                                object.append(fragment);
                                Log.v((String)"FragmentManager", (String)object.toString());
                            }
                            if (!fragment.mRetaining) {
                                fragment.performDestroy();
                                this.dispatchOnFragmentDestroyed(fragment, false);
                            } else {
                                fragment.mState = 0;
                            }
                            fragment.performDetach();
                            this.dispatchOnFragmentDetached(fragment, false);
                            if (bl) break;
                            if (!fragment.mRetaining) {
                                this.makeInactive(fragment);
                                break;
                            }
                            fragment.mHost = null;
                            fragment.mParentFragment = null;
                            fragment.mFragmentManager = null;
                            break;
                        }
                        fragment.setStateAfterAnimating(n);
                        n = 1;
                        break;
                    }
                }
            }
        }
        if (fragment.mState != n) {
            object = new StringBuilder();
            object.append("moveToState: Fragment state for ");
            object.append(fragment);
            object.append(" not updated inline; ");
            object.append("expected state ");
            object.append(n);
            object.append(" found ");
            object.append(fragment.mState);
            Log.w((String)"FragmentManager", (String)object.toString());
            fragment.mState = n;
            return;
        }
    }

    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        int n = this.mAdded.size();
        for (int i = 0; i < n; ++i) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment == null) continue;
            fragment.noteStateNotSaved();
        }
    }

    public View onCreateView(View object, String string2, Context object2, AttributeSet attributeSet) {
        int n;
        int n2;
        String string3;
        block19 : {
            block18 : {
                block17 : {
                    if (!"fragment".equals(string2)) {
                        return null;
                    }
                    string2 = attributeSet.getAttributeValue(null, "class");
                    Object object3 = object2.obtainStyledAttributes(attributeSet, FragmentTag.Fragment);
                    n2 = 0;
                    if (string2 == null) {
                        string2 = object3.getString(0);
                    }
                    n = object3.getResourceId(1, -1);
                    string3 = object3.getString(2);
                    object3.recycle();
                    if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), string2)) {
                        return null;
                    }
                    if (object != null) {
                        n2 = object.getId();
                    }
                    if (n2 == -1 && n == -1 && string3 == null) {
                        object = new StringBuilder();
                        object.append(attributeSet.getPositionDescription());
                        object.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
                        object.append(string2);
                        throw new IllegalArgumentException(object.toString());
                    }
                    object = n != -1 ? this.findFragmentById(n) : null;
                    if (object == null && string3 != null) {
                        object = this.findFragmentByTag(string3);
                    }
                    if (object == null && n2 != -1) {
                        object = this.findFragmentById(n2);
                    }
                    if (DEBUG) {
                        object3 = new StringBuilder();
                        object3.append("onCreateView: id=0x");
                        object3.append(Integer.toHexString(n));
                        object3.append(" fname=");
                        object3.append(string2);
                        object3.append(" existing=");
                        object3.append(object);
                        Log.v((String)"FragmentManager", (String)object3.toString());
                    }
                    if (object != null) break block17;
                    object = this.mContainer.instantiate((Context)object2, string2, null);
                    object.mFromLayout = true;
                    int n3 = n != 0 ? n : n2;
                    object.mFragmentId = n3;
                    object.mContainerId = n2;
                    object.mTag = string3;
                    object.mInLayout = true;
                    object.mFragmentManager = this;
                    object.mHost = object2 = this.mHost;
                    object.onInflate(object2.getContext(), attributeSet, object.mSavedFragmentState);
                    this.addFragment((Fragment)object, true);
                    break block18;
                }
                if (object.mInLayout) break block19;
                object.mInLayout = true;
                object.mHost = this.mHost;
                if (!object.mRetaining) {
                    object.onInflate(this.mHost.getContext(), attributeSet, object.mSavedFragmentState);
                }
            }
            if (this.mCurState < 1 && object.mFromLayout) {
                this.moveToState((Fragment)object, 1, 0, 0, false);
            } else {
                this.moveToState((Fragment)object);
            }
            if (object.mView != null) {
                if (n != 0) {
                    object.mView.setId(n);
                }
                if (object.mView.getTag() == null) {
                    object.mView.setTag((Object)string3);
                }
                return object.mView;
            }
            object = new StringBuilder();
            object.append("Fragment ");
            object.append(string2);
            object.append(" did not create a view.");
            throw new IllegalStateException(object.toString());
        }
        object = new StringBuilder();
        object.append(attributeSet.getPositionDescription());
        object.append(": Duplicate id 0x");
        object.append(Integer.toHexString(n));
        object.append(", tag ");
        object.append(string3);
        object.append(", or parent id 0x");
        object.append(Integer.toHexString(n2));
        object.append(" with another fragment for ");
        object.append(string2);
        throw new IllegalArgumentException(object.toString());
    }

    public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
        return this.onCreateView(null, string2, context, attributeSet);
    }

    public void performPendingDeferredStart(Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            this.moveToState(fragment, this.mCurState, 0, 0, false);
            return;
        }
    }

    @Override
    public void popBackStack() {
        this.enqueueAction(new PopBackStackState(null, -1, 0), false);
    }

    @Override
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

    @Override
    public void popBackStack(String string2, int n) {
        this.enqueueAction(new PopBackStackState(string2, -1, n), false);
    }

    @Override
    public boolean popBackStackImmediate() {
        this.checkStateLoss();
        return this.popBackStackImmediate(null, -1, 0);
    }

    @Override
    public boolean popBackStackImmediate(int n, int n2) {
        this.checkStateLoss();
        this.execPendingActions();
        if (n >= 0) {
            return this.popBackStackImmediate(null, n, n2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad id: ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @Override
    public boolean popBackStackImmediate(String string2, int n) {
        this.checkStateLoss();
        return this.popBackStackImmediate(string2, -1, n);
    }

    boolean popBackStackState(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, String string2, int n, int n2) {
        block13 : {
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
            if (string2 == null && n < 0) {
                n = n3;
            } else {
                for (n3 = this.mBackStack.size() - 1; n3 >= 0; --n3) {
                    arrayList3 = this.mBackStack.get(n3);
                    if (string2 != null && string2.equals(arrayList3.getName()) || n >= 0 && n == arrayList3.mIndex) break;
                }
                if (n3 < 0) {
                    return false;
                }
                if ((n2 & 1) != 0) {
                    for (n2 = n3 - 1; n2 >= 0; --n2) {
                        arrayList3 = this.mBackStack.get(n2);
                        if (string2 != null && string2.equals(arrayList3.getName())) continue;
                        if (n >= 0 && n == arrayList3.mIndex) {
                            continue;
                        }
                        n = n2;
                        break block13;
                    }
                    n = n2;
                } else {
                    n = n3;
                }
            }
        }
        if (n == this.mBackStack.size() - 1) {
            return false;
        }
        for (n2 = this.mBackStack.size() - 1; n2 > n; --n2) {
            arrayList.add(this.mBackStack.remove(n2));
            arrayList2.add(true);
        }
        return true;
    }

    @Override
    public void putFragment(Bundle bundle, String string2, Fragment fragment) {
        if (fragment.mIndex < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        bundle.putInt(string2, fragment.mIndex);
    }

    @Override
    public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean bl) {
        this.mLifecycleCallbacks.add(new Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>(fragmentLifecycleCallbacks, bl));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void removeFragment(Fragment fragment) {
        StringBuilder stringBuilder;
        if (DEBUG) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("remove: ");
            stringBuilder.append(fragment);
            stringBuilder.append(" nesting=");
            stringBuilder.append(fragment.mBackStackNesting);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        boolean bl = fragment.isInBackStack();
        if (fragment.mDetached) {
            if (!(bl ^ true)) return;
        }
        stringBuilder = this.mAdded;
        // MONITORENTER : stringBuilder
        this.mAdded.remove(fragment);
        // MONITOREXIT : stringBuilder
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mAdded = false;
        fragment.mRemoving = true;
    }

    @Override
    public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        ArrayList<FragmentManager.OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(onBackStackChangedListener);
            return;
        }
    }

    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void restoreAllState(Parcelable object, FragmentManagerNonConfig object2) {
        int n;
        Object object3;
        int n2;
        Object object4;
        Object object5;
        if (object == null) {
            return;
        }
        FragmentManagerState fragmentManagerState = (FragmentManagerState)object;
        if (fragmentManagerState.mActive == null) {
            return;
        }
        object = null;
        if (object2 != null) {
            object4 = object2.getFragments();
            object = object2.getChildNonConfigs();
            n2 = object4 != null ? object4.size() : 0;
            for (n = 0; n < n2; ++n) {
                int n3;
                object5 = object4.get(n);
                if (DEBUG) {
                    object3 = new StringBuilder();
                    object3.append("restoreAllState: re-attaching retained ");
                    object3.append(object5);
                    Log.v((String)"FragmentManager", (String)object3.toString());
                }
                for (n3 = 0; n3 < fragmentManagerState.mActive.length && fragmentManagerState.mActive[n3].mIndex != object5.mIndex; ++n3) {
                }
                if (n3 == fragmentManagerState.mActive.length) {
                    object3 = new StringBuilder();
                    object3.append("Could not find active fragment with index ");
                    object3.append(object5.mIndex);
                    this.throwException(new IllegalStateException(object3.toString()));
                }
                object3 = fragmentManagerState.mActive[n3];
                object3.mInstance = object5;
                object5.mSavedViewState = null;
                object5.mBackStackNesting = 0;
                object5.mInLayout = false;
                object5.mAdded = false;
                object5.mTarget = null;
                if (object3.mSavedFragmentState == null) continue;
                object3.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                object5.mSavedViewState = object3.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                object5.mSavedFragmentState = object3.mSavedFragmentState;
            }
        }
        this.mActive = new SparseArray(fragmentManagerState.mActive.length);
        for (n2 = 0; n2 < fragmentManagerState.mActive.length; ++n2) {
            object5 = fragmentManagerState.mActive[n2];
            if (object5 == null) continue;
            object4 = null;
            if (object != null && n2 < object.size()) {
                object4 = (FragmentManagerNonConfig)((Object)object.get(n2));
            }
            object4 = object5.instantiate(this.mHost, this.mContainer, this.mParent, (FragmentManagerNonConfig)object4);
            if (DEBUG) {
                object3 = new StringBuilder();
                object3.append("restoreAllState: active #");
                object3.append(n2);
                object3.append(": ");
                object3.append(object4);
                Log.v((String)"FragmentManager", (String)object3.toString());
            }
            this.mActive.put(object4.mIndex, object4);
            object5.mInstance = null;
        }
        if (object2 != null) {
            object = object2.getFragments();
            n2 = object != null ? object.size() : 0;
            for (n = 0; n < n2; ++n) {
                object2 = object.get(n);
                if (object2.mTargetIndex < 0) continue;
                object2.mTarget = (Fragment)this.mActive.get(object2.mTargetIndex);
                if (object2.mTarget != null) continue;
                object4 = new StringBuilder();
                object4.append("Re-attaching retained fragment ");
                object4.append(object2);
                object4.append(" target no longer exists: ");
                object4.append(object2.mTargetIndex);
                Log.w((String)"FragmentManager", (String)object4.toString());
            }
        }
        this.mAdded.clear();
        if (fragmentManagerState.mAdded != null) {
            for (n2 = 0; n2 < fragmentManagerState.mAdded.length; ++n2) {
                object = (Fragment)this.mActive.get(fragmentManagerState.mAdded[n2]);
                if (object == null) {
                    object2 = new StringBuilder();
                    object2.append("No instantiated fragment for index #");
                    object2.append(fragmentManagerState.mAdded[n2]);
                    this.throwException(new IllegalStateException(object2.toString()));
                }
                object.mAdded = true;
                if (DEBUG) {
                    object2 = new StringBuilder();
                    object2.append("restoreAllState: added #");
                    object2.append(n2);
                    object2.append(": ");
                    object2.append(object);
                    Log.v((String)"FragmentManager", (String)object2.toString());
                }
                if (this.mAdded.contains(object)) {
                    throw new IllegalStateException("Already added!");
                }
                object2 = this.mAdded;
                synchronized (object2) {
                    this.mAdded.add((Fragment)object);
                    continue;
                }
            }
        }
        if (fragmentManagerState.mBackStack != null) {
            this.mBackStack = new ArrayList(fragmentManagerState.mBackStack.length);
            for (n2 = 0; n2 < fragmentManagerState.mBackStack.length; ++n2) {
                object = fragmentManagerState.mBackStack[n2].instantiate(this);
                if (DEBUG) {
                    object2 = new StringBuilder();
                    object2.append("restoreAllState: back stack #");
                    object2.append(n2);
                    object2.append(" (index ");
                    object2.append(object.mIndex);
                    object2.append("): ");
                    object2.append(object);
                    Log.v((String)"FragmentManager", (String)object2.toString());
                    object2 = new PrintWriter(new LogWriter("FragmentManager"));
                    object.dump("  ", (PrintWriter)object2, false);
                    object2.close();
                }
                this.mBackStack.add((BackStackRecord)object);
                if (object.mIndex < 0) continue;
                this.setBackStackIndex(object.mIndex, (BackStackRecord)object);
            }
        } else {
            this.mBackStack = null;
        }
        if (fragmentManagerState.mPrimaryNavActiveIndex >= 0) {
            this.mPrimaryNav = (Fragment)this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex);
        }
        this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
    }

    FragmentManagerNonConfig retainNonConfig() {
        FragmentManagerImpl.setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }

    Parcelable saveAllState() {
        this.forcePostponedTransactions();
        this.endAnimatingAwayFragments();
        this.execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        Object object = this.mActive;
        if (object != null) {
            Object object2;
            int n;
            BackStackState[] arrbackStackState;
            if (object.size() <= 0) {
                return null;
            }
            int n2 = this.mActive.size();
            FragmentState[] arrfragmentState = new FragmentState[n2];
            int n3 = 0;
            for (n = 0; n < n2; ++n) {
                object = (Fragment)this.mActive.valueAt(n);
                if (object == null) continue;
                if (object.mIndex < 0) {
                    arrbackStackState = new StringBuilder();
                    arrbackStackState.append("Failure saving state: active ");
                    arrbackStackState.append(object);
                    arrbackStackState.append(" has cleared index: ");
                    arrbackStackState.append(object.mIndex);
                    this.throwException(new IllegalStateException(arrbackStackState.toString()));
                }
                n3 = 1;
                arrbackStackState = new FragmentState((Fragment)object);
                arrfragmentState[n] = arrbackStackState;
                if (object.mState > 0 && arrbackStackState.mSavedFragmentState == null) {
                    arrbackStackState.mSavedFragmentState = this.saveFragmentBasicState((Fragment)object);
                    if (object.mTarget != null) {
                        if (object.mTarget.mIndex < 0) {
                            object2 = new StringBuilder();
                            object2.append("Failure saving state: ");
                            object2.append(object);
                            object2.append(" has target not in fragment manager: ");
                            object2.append(object.mTarget);
                            this.throwException(new IllegalStateException(object2.toString()));
                        }
                        if (arrbackStackState.mSavedFragmentState == null) {
                            arrbackStackState.mSavedFragmentState = new Bundle();
                        }
                        this.putFragment(arrbackStackState.mSavedFragmentState, "android:target_state", object.mTarget);
                        if (object.mTargetRequestCode != 0) {
                            arrbackStackState.mSavedFragmentState.putInt("android:target_req_state", object.mTargetRequestCode);
                        }
                    }
                } else {
                    arrbackStackState.mSavedFragmentState = object.mSavedFragmentState;
                }
                if (!DEBUG) continue;
                object2 = new StringBuilder();
                object2.append("Saved state of ");
                object2.append(object);
                object2.append(": ");
                object2.append((Object)arrbackStackState.mSavedFragmentState);
                Log.v((String)"FragmentManager", (String)object2.toString());
            }
            if (n3 == 0) {
                if (DEBUG) {
                    Log.v((String)"FragmentManager", (String)"saveAllState: no fragments!");
                }
                return null;
            }
            object = null;
            arrbackStackState = null;
            n3 = this.mAdded.size();
            if (n3 > 0) {
                object = new int[n3];
                for (n = 0; n < n3; ++n) {
                    object[n] = this.mAdded.get((int)n).mIndex;
                    if (object[n] < 0) {
                        object2 = new StringBuilder();
                        object2.append("Failure saving state: active ");
                        object2.append(this.mAdded.get(n));
                        object2.append(" has cleared index: ");
                        object2.append((int)object[n]);
                        this.throwException(new IllegalStateException(object2.toString()));
                    }
                    if (!DEBUG) continue;
                    object2 = new StringBuilder();
                    object2.append("saveAllState: adding fragment #");
                    object2.append(n);
                    object2.append(": ");
                    object2.append(this.mAdded.get(n));
                    Log.v((String)"FragmentManager", (String)object2.toString());
                }
            }
            if ((object2 = this.mBackStack) != null && (n3 = object2.size()) > 0) {
                arrbackStackState = new BackStackState[n3];
                for (n = 0; n < n3; ++n) {
                    arrbackStackState[n] = new BackStackState(this.mBackStack.get(n));
                    if (!DEBUG) continue;
                    object2 = new StringBuilder();
                    object2.append("saveAllState: adding back stack #");
                    object2.append(n);
                    object2.append(": ");
                    object2.append(this.mBackStack.get(n));
                    Log.v((String)"FragmentManager", (String)object2.toString());
                }
            }
            object2 = new FragmentManagerState();
            object2.mActive = arrfragmentState;
            object2.mAdded = object;
            object2.mBackStack = arrbackStackState;
            object = this.mPrimaryNav;
            if (object != null) {
                object2.mPrimaryNavActiveIndex = object.mIndex;
            }
            object2.mNextFragmentIndex = this.mNextFragmentIndex;
            this.saveNonConfig();
            return object2;
        }
        return null;
    }

    Bundle saveFragmentBasicState(Fragment fragment) {
        Bundle bundle = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        this.dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            bundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment.mView != null) {
            this.saveFragmentViewState(fragment);
        }
        if (fragment.mSavedViewState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", fragment.mSavedViewState);
        }
        if (!fragment.mUserVisibleHint) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
            return bundle;
        }
        return bundle;
    }

    @Override
    public Fragment.SavedState saveFragmentInstanceState(Fragment object) {
        StringBuilder stringBuilder;
        if (object.mIndex < 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(object);
            stringBuilder.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(stringBuilder.toString()));
        }
        int n = object.mState;
        stringBuilder = null;
        if (n > 0) {
            Bundle bundle = this.saveFragmentBasicState((Fragment)object);
            object = stringBuilder;
            if (bundle != null) {
                object = new Fragment.SavedState(bundle);
            }
            return object;
        }
        return null;
    }

    void saveFragmentViewState(Fragment fragment) {
        if (fragment.mInnerView == null) {
            return;
        }
        SparseArray<Parcelable> sparseArray = this.mStateArray;
        if (sparseArray == null) {
            this.mStateArray = new SparseArray();
        } else {
            sparseArray.clear();
        }
        fragment.mInnerView.saveHierarchyState(this.mStateArray);
        if (this.mStateArray.size() > 0) {
            fragment.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
            return;
        }
    }

    void saveNonConfig() {
        Object object = null;
        Object object2 = null;
        Serializable serializable = null;
        ArrayList<Object> arrayList = null;
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); ++i) {
                int n;
                object = (Fragment)this.mActive.valueAt(i);
                if (object == null) continue;
                if (object.mRetainInstance) {
                    if (object2 == null) {
                        object2 = new ArrayList();
                    }
                    object2.add(object);
                    n = object.mTarget != null ? object.mTarget.mIndex : -1;
                    object.mTargetIndex = n;
                    if (DEBUG) {
                        serializable = new StringBuilder();
                        serializable.append("retainNonConfig: keeping retained ");
                        serializable.append(object);
                        Log.v((String)"FragmentManager", (String)serializable.toString());
                    }
                }
                if (object.mChildFragmentManager != null) {
                    object.mChildFragmentManager.saveNonConfig();
                    object = object.mChildFragmentManager.mSavedNonConfig;
                } else {
                    object = object.mChildNonConfig;
                }
                if (arrayList == null && object != null) {
                    arrayList = new ArrayList<Object>(this.mActive.size());
                    for (n = 0; n < i; ++n) {
                        arrayList.add(null);
                    }
                }
                if (arrayList == null) continue;
                arrayList.add(object);
            }
        } else {
            arrayList = serializable;
            object2 = object;
        }
        if (object2 == null && arrayList == null) {
            this.mSavedNonConfig = null;
            return;
        }
        this.mSavedNonConfig = new FragmentManagerNonConfig((List<Fragment>)object2, (List<FragmentManagerNonConfig>)arrayList);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setBackStackIndex(int n, BackStackRecord backStackRecord) {
        synchronized (this) {
            int n2;
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            if (n < (n2 = this.mBackStackIndices.size())) {
                if (DEBUG) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Setting back stack index ");
                    stringBuilder.append(n);
                    stringBuilder.append(" to ");
                    stringBuilder.append(backStackRecord);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                this.mBackStackIndices.set(n, backStackRecord);
            } else {
                StringBuilder stringBuilder;
                while (n2 < n) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList();
                    }
                    if (DEBUG) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Adding available back stack index ");
                        stringBuilder.append(n2);
                        Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                    }
                    this.mAvailBackStackIndices.add(n2);
                    ++n2;
                }
                if (DEBUG) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Adding back stack index ");
                    stringBuilder.append(n);
                    stringBuilder.append(" with ");
                    stringBuilder.append(backStackRecord);
                    Log.v((String)"FragmentManager", (String)stringBuilder.toString());
                }
                this.mBackStackIndices.add(backStackRecord);
            }
            return;
        }
    }

    public void setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment != null && (this.mActive.get(fragment.mIndex) != fragment || fragment.mHost != null && fragment.getFragmentManager() != this)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fragment ");
            stringBuilder.append(fragment);
            stringBuilder.append(" is not an active fragment of FragmentManager ");
            stringBuilder.append(this);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mPrimaryNav = fragment;
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("show: ");
            stringBuilder.append(fragment);
            Log.v((String)"FragmentManager", (String)stringBuilder.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged ^= true;
            return;
        }
    }

    void startPendingDeferredFragments() {
        if (this.mActive == null) {
            return;
        }
        for (int i = 0; i < this.mActive.size(); ++i) {
            Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment == null) continue;
            this.performPendingDeferredStart(fragment);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append("FragmentManager{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            DebugUtils.buildShortClassTag(fragment, stringBuilder);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
        }
        stringBuilder.append("}}");
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        CopyOnWriteArrayList<Pair<FragmentManager.FragmentLifecycleCallbacks, Boolean>> copyOnWriteArrayList = this.mLifecycleCallbacks;
        synchronized (copyOnWriteArrayList) {
            int n = 0;
            int n2 = this.mLifecycleCallbacks.size();
            do {
                block6 : {
                    block5 : {
                        if (n >= n2) break block5;
                        if (this.mLifecycleCallbacks.get((int)n).first != fragmentLifecycleCallbacks) break block6;
                        this.mLifecycleCallbacks.remove(n);
                    }
                    return;
                }
                ++n;
            } while (true);
        }
    }

    private static class AnimateOnHWLayerIfNeededListener
    extends AnimationListenerWrapper {
        View mView;

        AnimateOnHWLayerIfNeededListener(View view, Animation.AnimationListener animationListener) {
            super(animationListener);
            this.mView = view;
        }

        @CallSuper
        @Override
        public void onAnimationEnd(Animation animation) {
            if (!ViewCompat.isAttachedToWindow(this.mView) && Build.VERSION.SDK_INT < 24) {
                this.mView.setLayerType(0, null);
            } else {
                this.mView.post(new Runnable(){

                    @Override
                    public void run() {
                        AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, null);
                    }
                });
            }
            super.onAnimationEnd(animation);
        }

    }

    private static class AnimationListenerWrapper
    implements Animation.AnimationListener {
        private final Animation.AnimationListener mWrapped;

        private AnimationListenerWrapper(Animation.AnimationListener animationListener) {
            this.mWrapped = animationListener;
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationEnd(animation);
                return;
            }
        }

        @CallSuper
        public void onAnimationRepeat(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationRepeat(animation);
                return;
            }
        }

        @CallSuper
        public void onAnimationStart(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationStart(animation);
                return;
            }
        }
    }

    private static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        private AnimationOrAnimator(Animator animator2) {
            this.animation = null;
            this.animator = animator2;
            if (animator2 != null) {
                return;
            }
            throw new IllegalStateException("Animator cannot be null");
        }

        private AnimationOrAnimator(Animation animation) {
            this.animation = animation;
            this.animator = null;
            if (animation != null) {
                return;
            }
            throw new IllegalStateException("Animation cannot be null");
        }
    }

    private static class AnimatorOnHWLayerIfNeededListener
    extends AnimatorListenerAdapter {
        View mView;

        AnimatorOnHWLayerIfNeededListener(View view) {
            this.mView = view;
        }

        public void onAnimationEnd(Animator animator2) {
            this.mView.setLayerType(0, null);
            animator2.removeListener((Animator.AnimatorListener)this);
        }

        public void onAnimationStart(Animator animator2) {
            this.mView.setLayerType(2, null);
        }
    }

    static class FragmentTag {
        public static final int[] Fragment = new int[]{16842755, 16842960, 16842961};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        FragmentTag() {
        }
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
            FragmentManager fragmentManager;
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null && (fragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager()) != null && fragmentManager.popBackStackImmediate()) {
                return false;
            }
            return FragmentManagerImpl.this.popBackStackState(arrayList, arrayList2, this.mName, this.mId, this.mFlags);
        }
    }

    static class StartEnterTransitionListener
    implements Fragment.OnStartEnterTransitionListener {
        private final boolean mIsBack;
        private int mNumPostponed;
        private final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord backStackRecord, boolean bl) {
            this.mIsBack = bl;
            this.mRecord = backStackRecord;
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }

        public void completeTransaction() {
            Object object;
            int n = this.mNumPostponed;
            boolean bl = false;
            n = n > 0 ? 1 : 0;
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            int n2 = fragmentManagerImpl.mAdded.size();
            for (int i = 0; i < n2; ++i) {
                object = fragmentManagerImpl.mAdded.get(i);
                object.setOnStartEnterTransitionListener(null);
                if (n == 0 || !object.isPostponed()) continue;
                object.startPostponedEnterTransition();
            }
            fragmentManagerImpl = this.mRecord.mManager;
            object = this.mRecord;
            boolean bl2 = this.mIsBack;
            if (n == 0) {
                bl = true;
            }
            fragmentManagerImpl.completeExecute((BackStackRecord)object, bl2, bl, true);
        }

        public boolean isReady() {
            if (this.mNumPostponed == 0) {
                return true;
            }
            return false;
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

