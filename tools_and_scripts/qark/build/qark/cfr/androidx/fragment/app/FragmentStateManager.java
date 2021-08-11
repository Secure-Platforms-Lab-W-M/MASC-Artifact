/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  androidx.fragment.R
 *  androidx.fragment.R$id
 */
package androidx.fragment.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentLifecycleCallbacksDispatcher;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManagerViewModel;
import androidx.fragment.app.FragmentState;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStoreOwner;

class FragmentStateManager {
    private static final String TAG = "FragmentManager";
    private static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    private static final String TARGET_STATE_TAG = "android:target_state";
    private static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    private static final String VIEW_STATE_TAG = "android:view_state";
    private final FragmentLifecycleCallbacksDispatcher mDispatcher;
    private final Fragment mFragment;
    private int mFragmentManagerState = -1;

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, Fragment fragment) {
        this.mDispatcher = fragmentLifecycleCallbacksDispatcher;
        this.mFragment = fragment;
    }

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher object, Fragment fragment, FragmentState fragmentState) {
        this.mDispatcher = object;
        this.mFragment = fragment;
        fragment.mSavedViewState = null;
        this.mFragment.mBackStackNesting = 0;
        this.mFragment.mInLayout = false;
        this.mFragment.mAdded = false;
        fragment = this.mFragment;
        object = fragment.mTarget != null ? this.mFragment.mTarget.mWho : null;
        fragment.mTargetWho = object;
        this.mFragment.mTarget = null;
        if (fragmentState.mSavedFragmentState != null) {
            this.mFragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
            return;
        }
        this.mFragment.mSavedFragmentState = new Bundle();
    }

    FragmentStateManager(FragmentLifecycleCallbacksDispatcher object, ClassLoader classLoader, FragmentFactory fragmentFactory, FragmentState fragmentState) {
        this.mDispatcher = object;
        this.mFragment = fragmentFactory.instantiate(classLoader, fragmentState.mClassName);
        if (fragmentState.mArguments != null) {
            fragmentState.mArguments.setClassLoader(classLoader);
        }
        this.mFragment.setArguments(fragmentState.mArguments);
        this.mFragment.mWho = fragmentState.mWho;
        this.mFragment.mFromLayout = fragmentState.mFromLayout;
        this.mFragment.mRestored = true;
        this.mFragment.mFragmentId = fragmentState.mFragmentId;
        this.mFragment.mContainerId = fragmentState.mContainerId;
        this.mFragment.mTag = fragmentState.mTag;
        this.mFragment.mRetainInstance = fragmentState.mRetainInstance;
        this.mFragment.mRemoving = fragmentState.mRemoving;
        this.mFragment.mDetached = fragmentState.mDetached;
        this.mFragment.mHidden = fragmentState.mHidden;
        this.mFragment.mMaxState = Lifecycle.State.values()[fragmentState.mMaxLifecycleState];
        this.mFragment.mSavedFragmentState = fragmentState.mSavedFragmentState != null ? fragmentState.mSavedFragmentState : new Bundle();
        if (FragmentManager.isLoggingEnabled(2)) {
            object = new StringBuilder();
            object.append("Instantiated fragment ");
            object.append(this.mFragment);
            Log.v((String)"FragmentManager", (String)object.toString());
        }
    }

    private Bundle saveBasicState() {
        Bundle bundle = new Bundle();
        this.mFragment.performSaveInstanceState(bundle);
        this.mDispatcher.dispatchOnFragmentSaveInstanceState(this.mFragment, bundle, false);
        Bundle bundle2 = bundle;
        if (bundle.isEmpty()) {
            bundle2 = null;
        }
        if (this.mFragment.mView != null) {
            this.saveViewState();
        }
        bundle = bundle2;
        if (this.mFragment.mSavedViewState != null) {
            bundle = bundle2;
            if (bundle2 == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", this.mFragment.mSavedViewState);
        }
        bundle2 = bundle;
        if (!this.mFragment.mUserVisibleHint) {
            bundle2 = bundle;
            if (bundle == null) {
                bundle2 = new Bundle();
            }
            bundle2.putBoolean("android:user_visible_hint", this.mFragment.mUserVisibleHint);
        }
        return bundle2;
    }

    void activityCreated() {
        Object object;
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            object.append("moveto ACTIVITY_CREATED: ");
            object.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)object.toString());
        }
        object = this.mFragment;
        object.performActivityCreated(object.mSavedFragmentState);
        object = this.mDispatcher;
        Fragment fragment = this.mFragment;
        object.dispatchOnFragmentActivityCreated(fragment, fragment.mSavedFragmentState, false);
    }

    void attach(FragmentHostCallback<?> fragmentHostCallback, FragmentManager fragmentManager, Fragment fragment) {
        this.mFragment.mHost = fragmentHostCallback;
        this.mFragment.mParentFragment = fragment;
        this.mFragment.mFragmentManager = fragmentManager;
        this.mDispatcher.dispatchOnFragmentPreAttached(this.mFragment, fragmentHostCallback.getContext(), false);
        this.mFragment.performAttach();
        if (this.mFragment.mParentFragment == null) {
            fragmentHostCallback.onAttachFragment(this.mFragment);
        } else {
            this.mFragment.mParentFragment.onAttachFragment(this.mFragment);
        }
        this.mDispatcher.dispatchOnFragmentAttached(this.mFragment, fragmentHostCallback.getContext(), false);
    }

    int computeMaxState() {
        int n;
        int n2 = n = this.mFragmentManagerState;
        if (this.mFragment.mFromLayout) {
            n2 = this.mFragment.mInLayout ? Math.max(this.mFragmentManagerState, 1) : Math.min(n, 1);
        }
        n = n2;
        if (!this.mFragment.mAdded) {
            n = Math.min(n2, 1);
        }
        n2 = n;
        if (this.mFragment.mRemoving) {
            n2 = this.mFragment.isInBackStack() ? Math.min(n, 1) : Math.min(n, -1);
        }
        n = n2;
        if (this.mFragment.mDeferStart) {
            n = n2;
            if (this.mFragment.mState < 3) {
                n = Math.min(n2, 2);
            }
        }
        if ((n2 = .$SwitchMap$androidx$lifecycle$Lifecycle$State[this.mFragment.mMaxState.ordinal()]) != 1) {
            if (n2 != 2) {
                if (n2 != 3) {
                    return Math.min(n, -1);
                }
                return Math.min(n, 1);
            }
            return Math.min(n, 3);
        }
        return n;
    }

    void create() {
        Object object;
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            object.append("moveto CREATED: ");
            object.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)object.toString());
        }
        if (!this.mFragment.mIsCreated) {
            object = this.mDispatcher;
            Fragment fragment = this.mFragment;
            object.dispatchOnFragmentPreCreated(fragment, fragment.mSavedFragmentState, false);
            object = this.mFragment;
            object.performCreate(object.mSavedFragmentState);
            object = this.mDispatcher;
            fragment = this.mFragment;
            object.dispatchOnFragmentCreated(fragment, fragment.mSavedFragmentState, false);
            return;
        }
        object = this.mFragment;
        object.restoreChildFragmentState(object.mSavedFragmentState);
        this.mFragment.mState = 1;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void createView(FragmentContainer object) {
        Object object2;
        if (this.mFragment.mFromLayout) {
            return;
        }
        if (FragmentManager.isLoggingEnabled(3)) {
            object2 = new StringBuilder();
            object2.append("moveto CREATE_VIEW: ");
            object2.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)object2.toString());
        }
        object2 = null;
        if (this.mFragment.mContainer != null) {
            object2 = this.mFragment.mContainer;
        } else if (this.mFragment.mContainerId != 0) {
            if (this.mFragment.mContainerId == -1) {
                object = new StringBuilder();
                object.append("Cannot create fragment ");
                object.append(this.mFragment);
                object.append(" for a container view with no id");
                throw new IllegalArgumentException(object.toString());
            }
            object2 = object = (ViewGroup)object.onFindViewById(this.mFragment.mContainerId);
            if (object == null) {
                if (this.mFragment.mRestored) {
                    object2 = object;
                } else {
                    try {
                        object = this.mFragment.getResources().getResourceName(this.mFragment.mContainerId);
                    }
                    catch (Resources.NotFoundException notFoundException) {
                        object = "unknown";
                    }
                    object2 = new StringBuilder();
                    object2.append("No view found for id 0x");
                    object2.append(Integer.toHexString(this.mFragment.mContainerId));
                    object2.append(" (");
                    object2.append((String)object);
                    object2.append(") for fragment ");
                    object2.append(this.mFragment);
                    throw new IllegalArgumentException(object2.toString());
                }
            }
        }
        this.mFragment.mContainer = object2;
        object = this.mFragment;
        object.performCreateView(object.performGetLayoutInflater(object.mSavedFragmentState), (ViewGroup)object2, this.mFragment.mSavedFragmentState);
        if (this.mFragment.mView != null) {
            object = this.mFragment.mView;
            boolean bl = false;
            object.setSaveFromParentEnabled(false);
            this.mFragment.mView.setTag(R.id.fragment_container_view_tag, (Object)this.mFragment);
            if (object2 != null) {
                object2.addView(this.mFragment.mView);
            }
            if (this.mFragment.mHidden) {
                this.mFragment.mView.setVisibility(8);
            }
            ViewCompat.requestApplyInsets(this.mFragment.mView);
            object = this.mFragment;
            object.onViewCreated(object.mView, this.mFragment.mSavedFragmentState);
            object = this.mDispatcher;
            object2 = this.mFragment;
            object.dispatchOnFragmentViewCreated((Fragment)object2, object2.mView, this.mFragment.mSavedFragmentState, false);
            object = this.mFragment;
            boolean bl2 = bl;
            if (object.mView.getVisibility() == 0) {
                bl2 = bl;
                if (this.mFragment.mContainer != null) {
                    bl2 = true;
                }
            }
            object.mIsNewlyAdded = bl2;
        }
    }

    void destroy(FragmentHostCallback<?> fragmentHostCallback, FragmentManagerViewModel fragmentManagerViewModel) {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("movefrom CREATED: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)stringBuilder.toString());
        }
        boolean bl = this.mFragment.mRemoving && !this.mFragment.isInBackStack();
        boolean bl2 = bl || fragmentManagerViewModel.shouldDestroy(this.mFragment);
        if (bl2) {
            boolean bl3 = fragmentHostCallback instanceof ViewModelStoreOwner ? fragmentManagerViewModel.isCleared() : (fragmentHostCallback.getContext() instanceof Activity ? true ^ ((Activity)fragmentHostCallback.getContext()).isChangingConfigurations() : true);
            if (bl || bl3) {
                fragmentManagerViewModel.clearNonConfigState(this.mFragment);
            }
            this.mFragment.performDestroy();
            this.mDispatcher.dispatchOnFragmentDestroyed(this.mFragment, false);
            return;
        }
        this.mFragment.mState = 0;
    }

    void detach(FragmentManagerViewModel object) {
        Object object2;
        if (FragmentManager.isLoggingEnabled(3)) {
            object2 = new StringBuilder();
            object2.append("movefrom ATTACHED: ");
            object2.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)object2.toString());
        }
        this.mFragment.performDetach();
        object2 = this.mDispatcher;
        Fragment fragment = this.mFragment;
        boolean bl = false;
        object2.dispatchOnFragmentDetached(fragment, false);
        this.mFragment.mState = -1;
        this.mFragment.mHost = null;
        this.mFragment.mParentFragment = null;
        this.mFragment.mFragmentManager = null;
        boolean bl2 = bl;
        if (this.mFragment.mRemoving) {
            bl2 = bl;
            if (!this.mFragment.isInBackStack()) {
                bl2 = true;
            }
        }
        if (bl2 || object.shouldDestroy(this.mFragment)) {
            if (FragmentManager.isLoggingEnabled(3)) {
                object = new StringBuilder();
                object.append("initState called for fragment: ");
                object.append(this.mFragment);
                Log.d((String)"FragmentManager", (String)object.toString());
            }
            this.mFragment.initState();
        }
    }

    void ensureInflatedView() {
        if (this.mFragment.mFromLayout && this.mFragment.mInLayout && !this.mFragment.mPerformedCreateView) {
            Object object;
            if (FragmentManager.isLoggingEnabled(3)) {
                object = new StringBuilder();
                object.append("moveto CREATE_VIEW: ");
                object.append(this.mFragment);
                Log.d((String)"FragmentManager", (String)object.toString());
            }
            object = this.mFragment;
            object.performCreateView(object.performGetLayoutInflater(object.mSavedFragmentState), null, this.mFragment.mSavedFragmentState);
            if (this.mFragment.mView != null) {
                this.mFragment.mView.setSaveFromParentEnabled(false);
                if (this.mFragment.mHidden) {
                    this.mFragment.mView.setVisibility(8);
                }
                object = this.mFragment;
                object.onViewCreated(object.mView, this.mFragment.mSavedFragmentState);
                object = this.mDispatcher;
                Fragment fragment = this.mFragment;
                object.dispatchOnFragmentViewCreated(fragment, fragment.mView, this.mFragment.mSavedFragmentState, false);
            }
        }
    }

    Fragment getFragment() {
        return this.mFragment;
    }

    void pause() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("movefrom RESUMED: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.mFragment.performPause();
        this.mDispatcher.dispatchOnFragmentPaused(this.mFragment, false);
    }

    void restoreState(ClassLoader object) {
        if (this.mFragment.mSavedFragmentState == null) {
            return;
        }
        this.mFragment.mSavedFragmentState.setClassLoader((ClassLoader)object);
        object = this.mFragment;
        object.mSavedViewState = object.mSavedFragmentState.getSparseParcelableArray("android:view_state");
        object = this.mFragment;
        object.mTargetWho = object.mSavedFragmentState.getString("android:target_state");
        if (this.mFragment.mTargetWho != null) {
            object = this.mFragment;
            object.mTargetRequestCode = object.mSavedFragmentState.getInt("android:target_req_state", 0);
        }
        if (this.mFragment.mSavedUserVisibleHint != null) {
            object = this.mFragment;
            object.mUserVisibleHint = object.mSavedUserVisibleHint;
            this.mFragment.mSavedUserVisibleHint = null;
        } else {
            object = this.mFragment;
            object.mUserVisibleHint = object.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
        }
        if (!this.mFragment.mUserVisibleHint) {
            this.mFragment.mDeferStart = true;
        }
    }

    void restoreViewState() {
        Object object;
        if (FragmentManager.isLoggingEnabled(3)) {
            object = new StringBuilder();
            object.append("moveto RESTORE_VIEW_STATE: ");
            object.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)object.toString());
        }
        if (this.mFragment.mView != null) {
            object = this.mFragment;
            object.restoreViewState(object.mSavedFragmentState);
        }
        this.mFragment.mSavedFragmentState = null;
    }

    void resume() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("moveto RESUMED: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.mFragment.performResume();
        this.mDispatcher.dispatchOnFragmentResumed(this.mFragment, false);
        this.mFragment.mSavedFragmentState = null;
        this.mFragment.mSavedViewState = null;
    }

    Fragment.SavedState saveInstanceState() {
        int n = this.mFragment.mState;
        Fragment.SavedState savedState = null;
        if (n > -1) {
            Bundle bundle = this.saveBasicState();
            if (bundle != null) {
                savedState = new Fragment.SavedState(bundle);
            }
            return savedState;
        }
        return null;
    }

    FragmentState saveState() {
        FragmentState fragmentState = new FragmentState(this.mFragment);
        if (this.mFragment.mState > -1 && fragmentState.mSavedFragmentState == null) {
            fragmentState.mSavedFragmentState = this.saveBasicState();
            if (this.mFragment.mTargetWho != null) {
                if (fragmentState.mSavedFragmentState == null) {
                    fragmentState.mSavedFragmentState = new Bundle();
                }
                fragmentState.mSavedFragmentState.putString("android:target_state", this.mFragment.mTargetWho);
                if (this.mFragment.mTargetRequestCode != 0) {
                    fragmentState.mSavedFragmentState.putInt("android:target_req_state", this.mFragment.mTargetRequestCode);
                    return fragmentState;
                }
            }
        } else {
            fragmentState.mSavedFragmentState = this.mFragment.mSavedFragmentState;
        }
        return fragmentState;
    }

    void saveViewState() {
        if (this.mFragment.mView == null) {
            return;
        }
        SparseArray sparseArray = new SparseArray();
        this.mFragment.mView.saveHierarchyState(sparseArray);
        if (sparseArray.size() > 0) {
            this.mFragment.mSavedViewState = sparseArray;
        }
    }

    void setFragmentManagerState(int n) {
        this.mFragmentManagerState = n;
    }

    void start() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("moveto STARTED: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.mFragment.performStart();
        this.mDispatcher.dispatchOnFragmentStarted(this.mFragment, false);
    }

    void stop() {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("movefrom STARTED: ");
            stringBuilder.append(this.mFragment);
            Log.d((String)"FragmentManager", (String)stringBuilder.toString());
        }
        this.mFragment.performStop();
        this.mDispatcher.dispatchOnFragmentStopped(this.mFragment, false);
    }

}

