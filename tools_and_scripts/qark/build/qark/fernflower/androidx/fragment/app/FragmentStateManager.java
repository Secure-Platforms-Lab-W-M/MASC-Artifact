package androidx.fragment.app;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.fragment.R.id;
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

   FragmentStateManager(FragmentLifecycleCallbacksDispatcher var1, Fragment var2) {
      this.mDispatcher = var1;
      this.mFragment = var2;
   }

   FragmentStateManager(FragmentLifecycleCallbacksDispatcher var1, Fragment var2, FragmentState var3) {
      this.mDispatcher = var1;
      this.mFragment = var2;
      var2.mSavedViewState = null;
      this.mFragment.mBackStackNesting = 0;
      this.mFragment.mInLayout = false;
      this.mFragment.mAdded = false;
      var2 = this.mFragment;
      String var4;
      if (var2.mTarget != null) {
         var4 = this.mFragment.mTarget.mWho;
      } else {
         var4 = null;
      }

      var2.mTargetWho = var4;
      this.mFragment.mTarget = null;
      if (var3.mSavedFragmentState != null) {
         this.mFragment.mSavedFragmentState = var3.mSavedFragmentState;
      } else {
         this.mFragment.mSavedFragmentState = new Bundle();
      }
   }

   FragmentStateManager(FragmentLifecycleCallbacksDispatcher var1, ClassLoader var2, FragmentFactory var3, FragmentState var4) {
      this.mDispatcher = var1;
      this.mFragment = var3.instantiate(var2, var4.mClassName);
      if (var4.mArguments != null) {
         var4.mArguments.setClassLoader(var2);
      }

      this.mFragment.setArguments(var4.mArguments);
      this.mFragment.mWho = var4.mWho;
      this.mFragment.mFromLayout = var4.mFromLayout;
      this.mFragment.mRestored = true;
      this.mFragment.mFragmentId = var4.mFragmentId;
      this.mFragment.mContainerId = var4.mContainerId;
      this.mFragment.mTag = var4.mTag;
      this.mFragment.mRetainInstance = var4.mRetainInstance;
      this.mFragment.mRemoving = var4.mRemoving;
      this.mFragment.mDetached = var4.mDetached;
      this.mFragment.mHidden = var4.mHidden;
      this.mFragment.mMaxState = Lifecycle.State.values()[var4.mMaxLifecycleState];
      if (var4.mSavedFragmentState != null) {
         this.mFragment.mSavedFragmentState = var4.mSavedFragmentState;
      } else {
         this.mFragment.mSavedFragmentState = new Bundle();
      }

      if (FragmentManager.isLoggingEnabled(2)) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Instantiated fragment ");
         var5.append(this.mFragment);
         Log.v("FragmentManager", var5.toString());
      }

   }

   private Bundle saveBasicState() {
      Bundle var2 = new Bundle();
      this.mFragment.performSaveInstanceState(var2);
      this.mDispatcher.dispatchOnFragmentSaveInstanceState(this.mFragment, var2, false);
      Bundle var1 = var2;
      if (var2.isEmpty()) {
         var1 = null;
      }

      if (this.mFragment.mView != null) {
         this.saveViewState();
      }

      var2 = var1;
      if (this.mFragment.mSavedViewState != null) {
         var2 = var1;
         if (var1 == null) {
            var2 = new Bundle();
         }

         var2.putSparseParcelableArray("android:view_state", this.mFragment.mSavedViewState);
      }

      var1 = var2;
      if (!this.mFragment.mUserVisibleHint) {
         var1 = var2;
         if (var2 == null) {
            var1 = new Bundle();
         }

         var1.putBoolean("android:user_visible_hint", this.mFragment.mUserVisibleHint);
      }

      return var1;
   }

   void activityCreated() {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var1 = new StringBuilder();
         var1.append("moveto ACTIVITY_CREATED: ");
         var1.append(this.mFragment);
         Log.d("FragmentManager", var1.toString());
      }

      Fragment var3 = this.mFragment;
      var3.performActivityCreated(var3.mSavedFragmentState);
      FragmentLifecycleCallbacksDispatcher var4 = this.mDispatcher;
      Fragment var2 = this.mFragment;
      var4.dispatchOnFragmentActivityCreated(var2, var2.mSavedFragmentState, false);
   }

   void attach(FragmentHostCallback var1, FragmentManager var2, Fragment var3) {
      this.mFragment.mHost = var1;
      this.mFragment.mParentFragment = var3;
      this.mFragment.mFragmentManager = var2;
      this.mDispatcher.dispatchOnFragmentPreAttached(this.mFragment, var1.getContext(), false);
      this.mFragment.performAttach();
      if (this.mFragment.mParentFragment == null) {
         var1.onAttachFragment(this.mFragment);
      } else {
         this.mFragment.mParentFragment.onAttachFragment(this.mFragment);
      }

      this.mDispatcher.dispatchOnFragmentAttached(this.mFragment, var1.getContext(), false);
   }

   int computeMaxState() {
      int var2 = this.mFragmentManagerState;
      int var1 = var2;
      if (this.mFragment.mFromLayout) {
         if (this.mFragment.mInLayout) {
            var1 = Math.max(this.mFragmentManagerState, 1);
         } else {
            var1 = Math.min(var2, 1);
         }
      }

      var2 = var1;
      if (!this.mFragment.mAdded) {
         var2 = Math.min(var1, 1);
      }

      var1 = var2;
      if (this.mFragment.mRemoving) {
         if (this.mFragment.isInBackStack()) {
            var1 = Math.min(var2, 1);
         } else {
            var1 = Math.min(var2, -1);
         }
      }

      var2 = var1;
      if (this.mFragment.mDeferStart) {
         var2 = var1;
         if (this.mFragment.mState < 3) {
            var2 = Math.min(var1, 2);
         }
      }

      var1 = null.$SwitchMap$androidx$lifecycle$Lifecycle$State[this.mFragment.mMaxState.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            return var1 != 3 ? Math.min(var2, -1) : Math.min(var2, 1);
         } else {
            return Math.min(var2, 3);
         }
      } else {
         return var2;
      }
   }

   void create() {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var1 = new StringBuilder();
         var1.append("moveto CREATED: ");
         var1.append(this.mFragment);
         Log.d("FragmentManager", var1.toString());
      }

      Fragment var3;
      if (!this.mFragment.mIsCreated) {
         FragmentLifecycleCallbacksDispatcher var4 = this.mDispatcher;
         Fragment var2 = this.mFragment;
         var4.dispatchOnFragmentPreCreated(var2, var2.mSavedFragmentState, false);
         var3 = this.mFragment;
         var3.performCreate(var3.mSavedFragmentState);
         var4 = this.mDispatcher;
         var2 = this.mFragment;
         var4.dispatchOnFragmentCreated(var2, var2.mSavedFragmentState, false);
      } else {
         var3 = this.mFragment;
         var3.restoreChildFragmentState(var3.mSavedFragmentState);
         this.mFragment.mState = 1;
      }
   }

   void createView(FragmentContainer var1) {
      if (!this.mFragment.mFromLayout) {
         StringBuilder var4;
         if (FragmentManager.isLoggingEnabled(3)) {
            var4 = new StringBuilder();
            var4.append("moveto CREATE_VIEW: ");
            var4.append(this.mFragment);
            Log.d("FragmentManager", var4.toString());
         }

         ViewGroup var11 = null;
         if (this.mFragment.mContainer != null) {
            var11 = this.mFragment.mContainer;
         } else if (this.mFragment.mContainerId != 0) {
            if (this.mFragment.mContainerId == -1) {
               StringBuilder var12 = new StringBuilder();
               var12.append("Cannot create fragment ");
               var12.append(this.mFragment);
               var12.append(" for a container view with no id");
               throw new IllegalArgumentException(var12.toString());
            }

            ViewGroup var6 = (ViewGroup)var1.onFindViewById(this.mFragment.mContainerId);
            var11 = var6;
            if (var6 == null) {
               if (!this.mFragment.mRestored) {
                  String var10;
                  try {
                     var10 = this.mFragment.getResources().getResourceName(this.mFragment.mContainerId);
                  } catch (NotFoundException var5) {
                     var10 = "unknown";
                  }

                  var4 = new StringBuilder();
                  var4.append("No view found for id 0x");
                  var4.append(Integer.toHexString(this.mFragment.mContainerId));
                  var4.append(" (");
                  var4.append(var10);
                  var4.append(") for fragment ");
                  var4.append(this.mFragment);
                  throw new IllegalArgumentException(var4.toString());
               }

               var11 = var6;
            }
         }

         this.mFragment.mContainer = var11;
         Fragment var7 = this.mFragment;
         var7.performCreateView(var7.performGetLayoutInflater(var7.mSavedFragmentState), var11, this.mFragment.mSavedFragmentState);
         if (this.mFragment.mView != null) {
            View var8 = this.mFragment.mView;
            boolean var3 = false;
            var8.setSaveFromParentEnabled(false);
            this.mFragment.mView.setTag(id.fragment_container_view_tag, this.mFragment);
            if (var11 != null) {
               var11.addView(this.mFragment.mView);
            }

            if (this.mFragment.mHidden) {
               this.mFragment.mView.setVisibility(8);
            }

            ViewCompat.requestApplyInsets(this.mFragment.mView);
            var7 = this.mFragment;
            var7.onViewCreated(var7.mView, this.mFragment.mSavedFragmentState);
            FragmentLifecycleCallbacksDispatcher var9 = this.mDispatcher;
            Fragment var13 = this.mFragment;
            var9.dispatchOnFragmentViewCreated(var13, var13.mView, this.mFragment.mSavedFragmentState, false);
            var7 = this.mFragment;
            boolean var2 = var3;
            if (var7.mView.getVisibility() == 0) {
               var2 = var3;
               if (this.mFragment.mContainer != null) {
                  var2 = true;
               }
            }

            var7.mIsNewlyAdded = var2;
         }

      }
   }

   void destroy(FragmentHostCallback var1, FragmentManagerViewModel var2) {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var6 = new StringBuilder();
         var6.append("movefrom CREATED: ");
         var6.append(this.mFragment);
         Log.d("FragmentManager", var6.toString());
      }

      boolean var3;
      if (this.mFragment.mRemoving && !this.mFragment.isInBackStack()) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var4;
      if (!var3 && !var2.shouldDestroy(this.mFragment)) {
         var4 = false;
      } else {
         var4 = true;
      }

      if (var4) {
         boolean var5;
         if (var1 instanceof ViewModelStoreOwner) {
            var5 = var2.isCleared();
         } else if (var1.getContext() instanceof Activity) {
            var5 = true ^ ((Activity)var1.getContext()).isChangingConfigurations();
         } else {
            var5 = true;
         }

         if (var3 || var5) {
            var2.clearNonConfigState(this.mFragment);
         }

         this.mFragment.performDestroy();
         this.mDispatcher.dispatchOnFragmentDestroyed(this.mFragment, false);
      } else {
         this.mFragment.mState = 0;
      }
   }

   void detach(FragmentManagerViewModel var1) {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var4 = new StringBuilder();
         var4.append("movefrom ATTACHED: ");
         var4.append(this.mFragment);
         Log.d("FragmentManager", var4.toString());
      }

      this.mFragment.performDetach();
      FragmentLifecycleCallbacksDispatcher var7 = this.mDispatcher;
      Fragment var5 = this.mFragment;
      boolean var3 = false;
      var7.dispatchOnFragmentDetached(var5, false);
      this.mFragment.mState = -1;
      this.mFragment.mHost = null;
      this.mFragment.mParentFragment = null;
      this.mFragment.mFragmentManager = null;
      boolean var2 = var3;
      if (this.mFragment.mRemoving) {
         var2 = var3;
         if (!this.mFragment.isInBackStack()) {
            var2 = true;
         }
      }

      if (var2 || var1.shouldDestroy(this.mFragment)) {
         if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder var6 = new StringBuilder();
            var6.append("initState called for fragment: ");
            var6.append(this.mFragment);
            Log.d("FragmentManager", var6.toString());
         }

         this.mFragment.initState();
      }

   }

   void ensureInflatedView() {
      if (this.mFragment.mFromLayout && this.mFragment.mInLayout && !this.mFragment.mPerformedCreateView) {
         if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder var1 = new StringBuilder();
            var1.append("moveto CREATE_VIEW: ");
            var1.append(this.mFragment);
            Log.d("FragmentManager", var1.toString());
         }

         Fragment var3 = this.mFragment;
         var3.performCreateView(var3.performGetLayoutInflater(var3.mSavedFragmentState), (ViewGroup)null, this.mFragment.mSavedFragmentState);
         if (this.mFragment.mView != null) {
            this.mFragment.mView.setSaveFromParentEnabled(false);
            if (this.mFragment.mHidden) {
               this.mFragment.mView.setVisibility(8);
            }

            var3 = this.mFragment;
            var3.onViewCreated(var3.mView, this.mFragment.mSavedFragmentState);
            FragmentLifecycleCallbacksDispatcher var4 = this.mDispatcher;
            Fragment var2 = this.mFragment;
            var4.dispatchOnFragmentViewCreated(var2, var2.mView, this.mFragment.mSavedFragmentState, false);
         }
      }

   }

   Fragment getFragment() {
      return this.mFragment;
   }

   void pause() {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var1 = new StringBuilder();
         var1.append("movefrom RESUMED: ");
         var1.append(this.mFragment);
         Log.d("FragmentManager", var1.toString());
      }

      this.mFragment.performPause();
      this.mDispatcher.dispatchOnFragmentPaused(this.mFragment, false);
   }

   void restoreState(ClassLoader var1) {
      if (this.mFragment.mSavedFragmentState != null) {
         this.mFragment.mSavedFragmentState.setClassLoader(var1);
         Fragment var2 = this.mFragment;
         var2.mSavedViewState = var2.mSavedFragmentState.getSparseParcelableArray("android:view_state");
         var2 = this.mFragment;
         var2.mTargetWho = var2.mSavedFragmentState.getString("android:target_state");
         if (this.mFragment.mTargetWho != null) {
            var2 = this.mFragment;
            var2.mTargetRequestCode = var2.mSavedFragmentState.getInt("android:target_req_state", 0);
         }

         if (this.mFragment.mSavedUserVisibleHint != null) {
            var2 = this.mFragment;
            var2.mUserVisibleHint = var2.mSavedUserVisibleHint;
            this.mFragment.mSavedUserVisibleHint = null;
         } else {
            var2 = this.mFragment;
            var2.mUserVisibleHint = var2.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
         }

         if (!this.mFragment.mUserVisibleHint) {
            this.mFragment.mDeferStart = true;
         }

      }
   }

   void restoreViewState() {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var1 = new StringBuilder();
         var1.append("moveto RESTORE_VIEW_STATE: ");
         var1.append(this.mFragment);
         Log.d("FragmentManager", var1.toString());
      }

      if (this.mFragment.mView != null) {
         Fragment var2 = this.mFragment;
         var2.restoreViewState(var2.mSavedFragmentState);
      }

      this.mFragment.mSavedFragmentState = null;
   }

   void resume() {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var1 = new StringBuilder();
         var1.append("moveto RESUMED: ");
         var1.append(this.mFragment);
         Log.d("FragmentManager", var1.toString());
      }

      this.mFragment.performResume();
      this.mDispatcher.dispatchOnFragmentResumed(this.mFragment, false);
      this.mFragment.mSavedFragmentState = null;
      this.mFragment.mSavedViewState = null;
   }

   Fragment.SavedState saveInstanceState() {
      int var1 = this.mFragment.mState;
      Fragment.SavedState var2 = null;
      if (var1 > -1) {
         Bundle var3 = this.saveBasicState();
         if (var3 != null) {
            var2 = new Fragment.SavedState(var3);
         }

         return var2;
      } else {
         return null;
      }
   }

   FragmentState saveState() {
      FragmentState var1 = new FragmentState(this.mFragment);
      if (this.mFragment.mState > -1 && var1.mSavedFragmentState == null) {
         var1.mSavedFragmentState = this.saveBasicState();
         if (this.mFragment.mTargetWho != null) {
            if (var1.mSavedFragmentState == null) {
               var1.mSavedFragmentState = new Bundle();
            }

            var1.mSavedFragmentState.putString("android:target_state", this.mFragment.mTargetWho);
            if (this.mFragment.mTargetRequestCode != 0) {
               var1.mSavedFragmentState.putInt("android:target_req_state", this.mFragment.mTargetRequestCode);
               return var1;
            }
         }
      } else {
         var1.mSavedFragmentState = this.mFragment.mSavedFragmentState;
      }

      return var1;
   }

   void saveViewState() {
      if (this.mFragment.mView != null) {
         SparseArray var1 = new SparseArray();
         this.mFragment.mView.saveHierarchyState(var1);
         if (var1.size() > 0) {
            this.mFragment.mSavedViewState = var1;
         }

      }
   }

   void setFragmentManagerState(int var1) {
      this.mFragmentManagerState = var1;
   }

   void start() {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var1 = new StringBuilder();
         var1.append("moveto STARTED: ");
         var1.append(this.mFragment);
         Log.d("FragmentManager", var1.toString());
      }

      this.mFragment.performStart();
      this.mDispatcher.dispatchOnFragmentStarted(this.mFragment, false);
   }

   void stop() {
      if (FragmentManager.isLoggingEnabled(3)) {
         StringBuilder var1 = new StringBuilder();
         var1.append("movefrom STARTED: ");
         var1.append(this.mFragment);
         Log.d("FragmentManager", var1.toString());
      }

      this.mFragment.performStop();
      this.mDispatcher.dispatchOnFragmentStopped(this.mFragment, false);
   }
}
