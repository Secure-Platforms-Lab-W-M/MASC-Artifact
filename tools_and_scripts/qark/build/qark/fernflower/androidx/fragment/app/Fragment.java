package androidx.fragment.app;

import android.animation.Animator;
import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.Animation;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.LayoutInflaterCompat;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Fragment implements ComponentCallbacks, OnCreateContextMenuListener, LifecycleOwner, ViewModelStoreOwner, HasDefaultViewModelProviderFactory, SavedStateRegistryOwner {
   static final int ACTIVITY_CREATED = 2;
   static final int ATTACHED = 0;
   static final int CREATED = 1;
   static final int INITIALIZING = -1;
   static final int RESUMED = 4;
   static final int STARTED = 3;
   static final Object USE_DEFAULT_TRANSITION = new Object();
   boolean mAdded;
   Fragment.AnimationInfo mAnimationInfo;
   Bundle mArguments;
   int mBackStackNesting;
   private boolean mCalled;
   FragmentManager mChildFragmentManager;
   ViewGroup mContainer;
   int mContainerId;
   private int mContentLayoutId;
   private ViewModelProvider.Factory mDefaultFactory;
   boolean mDeferStart;
   boolean mDetached;
   int mFragmentId;
   FragmentManager mFragmentManager;
   boolean mFromLayout;
   boolean mHasMenu;
   boolean mHidden;
   boolean mHiddenChanged;
   FragmentHostCallback mHost;
   boolean mInLayout;
   boolean mIsCreated;
   boolean mIsNewlyAdded;
   private Boolean mIsPrimaryNavigationFragment;
   LayoutInflater mLayoutInflater;
   LifecycleRegistry mLifecycleRegistry;
   Lifecycle.State mMaxState;
   boolean mMenuVisible;
   Fragment mParentFragment;
   boolean mPerformedCreateView;
   float mPostponedAlpha;
   Runnable mPostponedDurationRunnable;
   boolean mRemoving;
   boolean mRestored;
   boolean mRetainInstance;
   boolean mRetainInstanceChangedWhileDetached;
   Bundle mSavedFragmentState;
   SavedStateRegistryController mSavedStateRegistryController;
   Boolean mSavedUserVisibleHint;
   SparseArray mSavedViewState;
   int mState;
   String mTag;
   Fragment mTarget;
   int mTargetRequestCode;
   String mTargetWho;
   boolean mUserVisibleHint;
   View mView;
   FragmentViewLifecycleOwner mViewLifecycleOwner;
   MutableLiveData mViewLifecycleOwnerLiveData;
   String mWho;

   public Fragment() {
      this.mState = -1;
      this.mWho = UUID.randomUUID().toString();
      this.mTargetWho = null;
      this.mIsPrimaryNavigationFragment = null;
      this.mChildFragmentManager = new FragmentManagerImpl();
      this.mMenuVisible = true;
      this.mUserVisibleHint = true;
      this.mPostponedDurationRunnable = new Runnable() {
         public void run() {
            Fragment.this.startPostponedEnterTransition();
         }
      };
      this.mMaxState = Lifecycle.State.RESUMED;
      this.mViewLifecycleOwnerLiveData = new MutableLiveData();
      this.initLifecycle();
   }

   public Fragment(int var1) {
      this();
      this.mContentLayoutId = var1;
   }

   private Fragment.AnimationInfo ensureAnimationInfo() {
      if (this.mAnimationInfo == null) {
         this.mAnimationInfo = new Fragment.AnimationInfo();
      }

      return this.mAnimationInfo;
   }

   private void initLifecycle() {
      this.mLifecycleRegistry = new LifecycleRegistry(this);
      this.mSavedStateRegistryController = SavedStateRegistryController.create(this);
      if (VERSION.SDK_INT >= 19) {
         this.mLifecycleRegistry.addObserver(new LifecycleEventObserver() {
            public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
               if (var2 == Lifecycle.Event.ON_STOP && Fragment.this.mView != null) {
                  Fragment.this.mView.cancelPendingInputEvents();
               }

            }
         });
      }

   }

   @Deprecated
   public static Fragment instantiate(Context var0, String var1) {
      return instantiate(var0, var1, (Bundle)null);
   }

   @Deprecated
   public static Fragment instantiate(Context param0, String param1, Bundle param2) {
      // $FF: Couldn't be decompiled
   }

   void callStartTransitionListener() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      Fragment.OnStartEnterTransitionListener var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var1.mEnterTransitionPostponed = false;
         var2 = this.mAnimationInfo.mStartEnterTransitionListener;
         this.mAnimationInfo.mStartEnterTransitionListener = null;
      }

      if (var2 != null) {
         var2.onStartEnterTransition();
      }

   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      var3.print(var1);
      var3.print("mFragmentId=#");
      var3.print(Integer.toHexString(this.mFragmentId));
      var3.print(" mContainerId=#");
      var3.print(Integer.toHexString(this.mContainerId));
      var3.print(" mTag=");
      var3.println(this.mTag);
      var3.print(var1);
      var3.print("mState=");
      var3.print(this.mState);
      var3.print(" mWho=");
      var3.print(this.mWho);
      var3.print(" mBackStackNesting=");
      var3.println(this.mBackStackNesting);
      var3.print(var1);
      var3.print("mAdded=");
      var3.print(this.mAdded);
      var3.print(" mRemoving=");
      var3.print(this.mRemoving);
      var3.print(" mFromLayout=");
      var3.print(this.mFromLayout);
      var3.print(" mInLayout=");
      var3.println(this.mInLayout);
      var3.print(var1);
      var3.print("mHidden=");
      var3.print(this.mHidden);
      var3.print(" mDetached=");
      var3.print(this.mDetached);
      var3.print(" mMenuVisible=");
      var3.print(this.mMenuVisible);
      var3.print(" mHasMenu=");
      var3.println(this.mHasMenu);
      var3.print(var1);
      var3.print("mRetainInstance=");
      var3.print(this.mRetainInstance);
      var3.print(" mUserVisibleHint=");
      var3.println(this.mUserVisibleHint);
      if (this.mFragmentManager != null) {
         var3.print(var1);
         var3.print("mFragmentManager=");
         var3.println(this.mFragmentManager);
      }

      if (this.mHost != null) {
         var3.print(var1);
         var3.print("mHost=");
         var3.println(this.mHost);
      }

      if (this.mParentFragment != null) {
         var3.print(var1);
         var3.print("mParentFragment=");
         var3.println(this.mParentFragment);
      }

      if (this.mArguments != null) {
         var3.print(var1);
         var3.print("mArguments=");
         var3.println(this.mArguments);
      }

      if (this.mSavedFragmentState != null) {
         var3.print(var1);
         var3.print("mSavedFragmentState=");
         var3.println(this.mSavedFragmentState);
      }

      if (this.mSavedViewState != null) {
         var3.print(var1);
         var3.print("mSavedViewState=");
         var3.println(this.mSavedViewState);
      }

      Fragment var5 = this.getTargetFragment();
      if (var5 != null) {
         var3.print(var1);
         var3.print("mTarget=");
         var3.print(var5);
         var3.print(" mTargetRequestCode=");
         var3.println(this.mTargetRequestCode);
      }

      if (this.getNextAnim() != 0) {
         var3.print(var1);
         var3.print("mNextAnim=");
         var3.println(this.getNextAnim());
      }

      if (this.mContainer != null) {
         var3.print(var1);
         var3.print("mContainer=");
         var3.println(this.mContainer);
      }

      if (this.mView != null) {
         var3.print(var1);
         var3.print("mView=");
         var3.println(this.mView);
      }

      if (this.getAnimatingAway() != null) {
         var3.print(var1);
         var3.print("mAnimatingAway=");
         var3.println(this.getAnimatingAway());
         var3.print(var1);
         var3.print("mStateAfterAnimating=");
         var3.println(this.getStateAfterAnimating());
      }

      if (this.getContext() != null) {
         LoaderManager.getInstance(this).dump(var1, var2, var3, var4);
      }

      var3.print(var1);
      StringBuilder var7 = new StringBuilder();
      var7.append("Child ");
      var7.append(this.mChildFragmentManager);
      var7.append(":");
      var3.println(var7.toString());
      FragmentManager var8 = this.mChildFragmentManager;
      StringBuilder var6 = new StringBuilder();
      var6.append(var1);
      var6.append("  ");
      var8.dump(var6.toString(), var2, var3, var4);
   }

   public final boolean equals(Object var1) {
      return super.equals(var1);
   }

   Fragment findFragmentByWho(String var1) {
      return var1.equals(this.mWho) ? this : this.mChildFragmentManager.findFragmentByWho(var1);
   }

   public final FragmentActivity getActivity() {
      FragmentHostCallback var1 = this.mHost;
      return var1 == null ? null : (FragmentActivity)var1.getActivity();
   }

   public boolean getAllowEnterTransitionOverlap() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 != null && var1.mAllowEnterTransitionOverlap != null ? this.mAnimationInfo.mAllowEnterTransitionOverlap : true;
   }

   public boolean getAllowReturnTransitionOverlap() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 != null && var1.mAllowReturnTransitionOverlap != null ? this.mAnimationInfo.mAllowReturnTransitionOverlap : true;
   }

   View getAnimatingAway() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? null : var1.mAnimatingAway;
   }

   Animator getAnimator() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? null : var1.mAnimator;
   }

   public final Bundle getArguments() {
      return this.mArguments;
   }

   public final FragmentManager getChildFragmentManager() {
      if (this.mHost != null) {
         return this.mChildFragmentManager;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" has not been attached yet.");
         throw new IllegalStateException(var1.toString());
      }
   }

   public Context getContext() {
      FragmentHostCallback var1 = this.mHost;
      return var1 == null ? null : var1.getContext();
   }

   public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
      if (this.mFragmentManager != null) {
         if (this.mDefaultFactory == null) {
            this.mDefaultFactory = new SavedStateViewModelFactory(this.requireActivity().getApplication(), this, this.getArguments());
         }

         return this.mDefaultFactory;
      } else {
         throw new IllegalStateException("Can't access ViewModels from detached fragment");
      }
   }

   public Object getEnterTransition() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? null : var1.mEnterTransition;
   }

   SharedElementCallback getEnterTransitionCallback() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? null : var1.mEnterTransitionCallback;
   }

   public Object getExitTransition() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? null : var1.mExitTransition;
   }

   SharedElementCallback getExitTransitionCallback() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? null : var1.mExitTransitionCallback;
   }

   @Deprecated
   public final FragmentManager getFragmentManager() {
      return this.mFragmentManager;
   }

   public final Object getHost() {
      FragmentHostCallback var1 = this.mHost;
      return var1 == null ? null : var1.onGetHost();
   }

   public final int getId() {
      return this.mFragmentId;
   }

   public final LayoutInflater getLayoutInflater() {
      LayoutInflater var1 = this.mLayoutInflater;
      return var1 == null ? this.performGetLayoutInflater((Bundle)null) : var1;
   }

   @Deprecated
   public LayoutInflater getLayoutInflater(Bundle var1) {
      FragmentHostCallback var2 = this.mHost;
      if (var2 != null) {
         LayoutInflater var3 = var2.onGetLayoutInflater();
         LayoutInflaterCompat.setFactory2(var3, this.mChildFragmentManager.getLayoutInflaterFactory());
         return var3;
      } else {
         throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
      }
   }

   public Lifecycle getLifecycle() {
      return this.mLifecycleRegistry;
   }

   @Deprecated
   public LoaderManager getLoaderManager() {
      return LoaderManager.getInstance(this);
   }

   int getNextAnim() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? 0 : var1.mNextAnim;
   }

   int getNextTransition() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? 0 : var1.mNextTransition;
   }

   public final Fragment getParentFragment() {
      return this.mParentFragment;
   }

   public final FragmentManager getParentFragmentManager() {
      FragmentManager var1 = this.mFragmentManager;
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" not associated with a fragment manager.");
         throw new IllegalStateException(var2.toString());
      }
   }

   public Object getReenterTransition() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      if (var1 == null) {
         return null;
      } else {
         return var1.mReenterTransition == USE_DEFAULT_TRANSITION ? this.getExitTransition() : this.mAnimationInfo.mReenterTransition;
      }
   }

   public final Resources getResources() {
      return this.requireContext().getResources();
   }

   public final boolean getRetainInstance() {
      return this.mRetainInstance;
   }

   public Object getReturnTransition() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      if (var1 == null) {
         return null;
      } else {
         return var1.mReturnTransition == USE_DEFAULT_TRANSITION ? this.getEnterTransition() : this.mAnimationInfo.mReturnTransition;
      }
   }

   public final SavedStateRegistry getSavedStateRegistry() {
      return this.mSavedStateRegistryController.getSavedStateRegistry();
   }

   public Object getSharedElementEnterTransition() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? null : var1.mSharedElementEnterTransition;
   }

   public Object getSharedElementReturnTransition() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      if (var1 == null) {
         return null;
      } else {
         return var1.mSharedElementReturnTransition == USE_DEFAULT_TRANSITION ? this.getSharedElementEnterTransition() : this.mAnimationInfo.mSharedElementReturnTransition;
      }
   }

   int getStateAfterAnimating() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? 0 : var1.mStateAfterAnimating;
   }

   public final String getString(int var1) {
      return this.getResources().getString(var1);
   }

   public final String getString(int var1, Object... var2) {
      return this.getResources().getString(var1, var2);
   }

   public final String getTag() {
      return this.mTag;
   }

   public final Fragment getTargetFragment() {
      Fragment var1 = this.mTarget;
      if (var1 != null) {
         return var1;
      } else {
         FragmentManager var3 = this.mFragmentManager;
         if (var3 != null) {
            String var2 = this.mTargetWho;
            if (var2 != null) {
               return var3.findActiveFragment(var2);
            }
         }

         return null;
      }
   }

   public final int getTargetRequestCode() {
      return this.mTargetRequestCode;
   }

   public final CharSequence getText(int var1) {
      return this.getResources().getText(var1);
   }

   @Deprecated
   public boolean getUserVisibleHint() {
      return this.mUserVisibleHint;
   }

   public View getView() {
      return this.mView;
   }

   public LifecycleOwner getViewLifecycleOwner() {
      FragmentViewLifecycleOwner var1 = this.mViewLifecycleOwner;
      if (var1 != null) {
         return var1;
      } else {
         throw new IllegalStateException("Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()");
      }
   }

   public LiveData getViewLifecycleOwnerLiveData() {
      return this.mViewLifecycleOwnerLiveData;
   }

   public ViewModelStore getViewModelStore() {
      FragmentManager var1 = this.mFragmentManager;
      if (var1 != null) {
         return var1.getViewModelStore(this);
      } else {
         throw new IllegalStateException("Can't access ViewModels from detached fragment");
      }
   }

   public final boolean hasOptionsMenu() {
      return this.mHasMenu;
   }

   public final int hashCode() {
      return super.hashCode();
   }

   void initState() {
      this.initLifecycle();
      this.mWho = UUID.randomUUID().toString();
      this.mAdded = false;
      this.mRemoving = false;
      this.mFromLayout = false;
      this.mInLayout = false;
      this.mRestored = false;
      this.mBackStackNesting = 0;
      this.mFragmentManager = null;
      this.mChildFragmentManager = new FragmentManagerImpl();
      this.mHost = null;
      this.mFragmentId = 0;
      this.mContainerId = 0;
      this.mTag = null;
      this.mHidden = false;
      this.mDetached = false;
   }

   public final boolean isAdded() {
      return this.mHost != null && this.mAdded;
   }

   public final boolean isDetached() {
      return this.mDetached;
   }

   public final boolean isHidden() {
      return this.mHidden;
   }

   boolean isHideReplaced() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? false : var1.mIsHideReplaced;
   }

   final boolean isInBackStack() {
      return this.mBackStackNesting > 0;
   }

   public final boolean isInLayout() {
      return this.mInLayout;
   }

   public final boolean isMenuVisible() {
      return this.mMenuVisible;
   }

   boolean isPostponed() {
      Fragment.AnimationInfo var1 = this.mAnimationInfo;
      return var1 == null ? false : var1.mEnterTransitionPostponed;
   }

   public final boolean isRemoving() {
      return this.mRemoving;
   }

   final boolean isRemovingParent() {
      Fragment var1 = this.getParentFragment();
      return var1 != null && (var1.isRemoving() || var1.isRemovingParent());
   }

   public final boolean isResumed() {
      return this.mState >= 4;
   }

   public final boolean isStateSaved() {
      FragmentManager var1 = this.mFragmentManager;
      return var1 == null ? false : var1.isStateSaved();
   }

   public final boolean isVisible() {
      if (this.isAdded() && !this.isHidden()) {
         View var1 = this.mView;
         if (var1 != null && var1.getWindowToken() != null && this.mView.getVisibility() == 0) {
            return true;
         }
      }

      return false;
   }

   void noteStateNotSaved() {
      this.mChildFragmentManager.noteStateNotSaved();
   }

   public void onActivityCreated(Bundle var1) {
      this.mCalled = true;
   }

   public void onActivityResult(int var1, int var2, Intent var3) {
   }

   @Deprecated
   public void onAttach(Activity var1) {
      this.mCalled = true;
   }

   public void onAttach(Context var1) {
      this.mCalled = true;
      FragmentHostCallback var2 = this.mHost;
      Activity var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = var2.getActivity();
      }

      if (var3 != null) {
         this.mCalled = false;
         this.onAttach(var3);
      }

   }

   public void onAttachFragment(Fragment var1) {
   }

   public void onConfigurationChanged(Configuration var1) {
      this.mCalled = true;
   }

   public boolean onContextItemSelected(MenuItem var1) {
      return false;
   }

   public void onCreate(Bundle var1) {
      this.mCalled = true;
      this.restoreChildFragmentState(var1);
      if (!this.mChildFragmentManager.isStateAtLeast(1)) {
         this.mChildFragmentManager.dispatchCreate();
      }

   }

   public Animation onCreateAnimation(int var1, boolean var2, int var3) {
      return null;
   }

   public Animator onCreateAnimator(int var1, boolean var2, int var3) {
      return null;
   }

   public void onCreateContextMenu(ContextMenu var1, View var2, ContextMenuInfo var3) {
      this.requireActivity().onCreateContextMenu(var1, var2, var3);
   }

   public void onCreateOptionsMenu(Menu var1, MenuInflater var2) {
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      int var4 = this.mContentLayoutId;
      return var4 != 0 ? var1.inflate(var4, var2, false) : null;
   }

   public void onDestroy() {
      this.mCalled = true;
   }

   public void onDestroyOptionsMenu() {
   }

   public void onDestroyView() {
      this.mCalled = true;
   }

   public void onDetach() {
      this.mCalled = true;
   }

   public LayoutInflater onGetLayoutInflater(Bundle var1) {
      return this.getLayoutInflater(var1);
   }

   public void onHiddenChanged(boolean var1) {
   }

   @Deprecated
   public void onInflate(Activity var1, AttributeSet var2, Bundle var3) {
      this.mCalled = true;
   }

   public void onInflate(Context var1, AttributeSet var2, Bundle var3) {
      this.mCalled = true;
      FragmentHostCallback var4 = this.mHost;
      Activity var5;
      if (var4 == null) {
         var5 = null;
      } else {
         var5 = var4.getActivity();
      }

      if (var5 != null) {
         this.mCalled = false;
         this.onInflate(var5, var2, var3);
      }

   }

   public void onLowMemory() {
      this.mCalled = true;
   }

   public void onMultiWindowModeChanged(boolean var1) {
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      return false;
   }

   public void onOptionsMenuClosed(Menu var1) {
   }

   public void onPause() {
      this.mCalled = true;
   }

   public void onPictureInPictureModeChanged(boolean var1) {
   }

   public void onPrepareOptionsMenu(Menu var1) {
   }

   public void onPrimaryNavigationFragmentChanged(boolean var1) {
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
   }

   public void onResume() {
      this.mCalled = true;
   }

   public void onSaveInstanceState(Bundle var1) {
   }

   public void onStart() {
      this.mCalled = true;
   }

   public void onStop() {
      this.mCalled = true;
   }

   public void onViewCreated(View var1, Bundle var2) {
   }

   public void onViewStateRestored(Bundle var1) {
      this.mCalled = true;
   }

   void performActivityCreated(Bundle var1) {
      this.mChildFragmentManager.noteStateNotSaved();
      this.mState = 2;
      this.mCalled = false;
      this.onActivityCreated(var1);
      if (this.mCalled) {
         this.mChildFragmentManager.dispatchActivityCreated();
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" did not call through to super.onActivityCreated()");
         throw new SuperNotCalledException(var2.toString());
      }
   }

   void performAttach() {
      this.mChildFragmentManager.attachController(this.mHost, new FragmentContainer() {
         public View onFindViewById(int var1) {
            if (Fragment.this.mView != null) {
               return Fragment.this.mView.findViewById(var1);
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("Fragment ");
               var2.append(this);
               var2.append(" does not have a view");
               throw new IllegalStateException(var2.toString());
            }
         }

         public boolean onHasView() {
            return Fragment.this.mView != null;
         }
      }, this);
      this.mState = 0;
      this.mCalled = false;
      this.onAttach(this.mHost.getContext());
      if (!this.mCalled) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onAttach()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performConfigurationChanged(Configuration var1) {
      this.onConfigurationChanged(var1);
      this.mChildFragmentManager.dispatchConfigurationChanged(var1);
   }

   boolean performContextItemSelected(MenuItem var1) {
      if (!this.mHidden) {
         if (this.onContextItemSelected(var1)) {
            return true;
         }

         if (this.mChildFragmentManager.dispatchContextItemSelected(var1)) {
            return true;
         }
      }

      return false;
   }

   void performCreate(Bundle var1) {
      this.mChildFragmentManager.noteStateNotSaved();
      this.mState = 1;
      this.mCalled = false;
      this.mSavedStateRegistryController.performRestore(var1);
      this.onCreate(var1);
      this.mIsCreated = true;
      if (this.mCalled) {
         this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" did not call through to super.onCreate()");
         throw new SuperNotCalledException(var2.toString());
      }
   }

   boolean performCreateOptionsMenu(Menu var1, MenuInflater var2) {
      boolean var5 = false;
      boolean var4 = false;
      if (!this.mHidden) {
         boolean var3 = var4;
         if (this.mHasMenu) {
            var3 = var4;
            if (this.mMenuVisible) {
               var3 = true;
               this.onCreateOptionsMenu(var1, var2);
            }
         }

         var5 = var3 | this.mChildFragmentManager.dispatchCreateOptionsMenu(var1, var2);
      }

      return var5;
   }

   void performCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      this.mChildFragmentManager.noteStateNotSaved();
      this.mPerformedCreateView = true;
      this.mViewLifecycleOwner = new FragmentViewLifecycleOwner();
      View var4 = this.onCreateView(var1, var2, var3);
      this.mView = var4;
      if (var4 != null) {
         this.mViewLifecycleOwner.initialize();
         this.mViewLifecycleOwnerLiveData.setValue(this.mViewLifecycleOwner);
      } else if (!this.mViewLifecycleOwner.isInitialized()) {
         this.mViewLifecycleOwner = null;
      } else {
         throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
      }
   }

   void performDestroy() {
      this.mChildFragmentManager.dispatchDestroy();
      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
      this.mState = 0;
      this.mCalled = false;
      this.mIsCreated = false;
      this.onDestroy();
      if (!this.mCalled) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onDestroy()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performDestroyView() {
      this.mChildFragmentManager.dispatchDestroyView();
      if (this.mView != null) {
         this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
      }

      this.mState = 1;
      this.mCalled = false;
      this.onDestroyView();
      if (this.mCalled) {
         LoaderManager.getInstance(this).markForRedelivery();
         this.mPerformedCreateView = false;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onDestroyView()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performDetach() {
      this.mState = -1;
      this.mCalled = false;
      this.onDetach();
      this.mLayoutInflater = null;
      if (this.mCalled) {
         if (!this.mChildFragmentManager.isDestroyed()) {
            this.mChildFragmentManager.dispatchDestroy();
            this.mChildFragmentManager = new FragmentManagerImpl();
         }

      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onDetach()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   LayoutInflater performGetLayoutInflater(Bundle var1) {
      LayoutInflater var2 = this.onGetLayoutInflater(var1);
      this.mLayoutInflater = var2;
      return var2;
   }

   void performLowMemory() {
      this.onLowMemory();
      this.mChildFragmentManager.dispatchLowMemory();
   }

   void performMultiWindowModeChanged(boolean var1) {
      this.onMultiWindowModeChanged(var1);
      this.mChildFragmentManager.dispatchMultiWindowModeChanged(var1);
   }

   boolean performOptionsItemSelected(MenuItem var1) {
      if (!this.mHidden) {
         if (this.mHasMenu && this.mMenuVisible && this.onOptionsItemSelected(var1)) {
            return true;
         }

         if (this.mChildFragmentManager.dispatchOptionsItemSelected(var1)) {
            return true;
         }
      }

      return false;
   }

   void performOptionsMenuClosed(Menu var1) {
      if (!this.mHidden) {
         if (this.mHasMenu && this.mMenuVisible) {
            this.onOptionsMenuClosed(var1);
         }

         this.mChildFragmentManager.dispatchOptionsMenuClosed(var1);
      }

   }

   void performPause() {
      this.mChildFragmentManager.dispatchPause();
      if (this.mView != null) {
         this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
      }

      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
      this.mState = 3;
      this.mCalled = false;
      this.onPause();
      if (!this.mCalled) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onPause()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performPictureInPictureModeChanged(boolean var1) {
      this.onPictureInPictureModeChanged(var1);
      this.mChildFragmentManager.dispatchPictureInPictureModeChanged(var1);
   }

   boolean performPrepareOptionsMenu(Menu var1) {
      boolean var4 = false;
      boolean var3 = false;
      if (!this.mHidden) {
         boolean var2 = var3;
         if (this.mHasMenu) {
            var2 = var3;
            if (this.mMenuVisible) {
               var2 = true;
               this.onPrepareOptionsMenu(var1);
            }
         }

         var4 = var2 | this.mChildFragmentManager.dispatchPrepareOptionsMenu(var1);
      }

      return var4;
   }

   void performPrimaryNavigationFragmentChanged() {
      boolean var1 = this.mFragmentManager.isPrimaryNavigation(this);
      Boolean var2 = this.mIsPrimaryNavigationFragment;
      if (var2 == null || var2 != var1) {
         this.mIsPrimaryNavigationFragment = var1;
         this.onPrimaryNavigationFragmentChanged(var1);
         this.mChildFragmentManager.dispatchPrimaryNavigationFragmentChanged();
      }

   }

   void performResume() {
      this.mChildFragmentManager.noteStateNotSaved();
      this.mChildFragmentManager.execPendingActions(true);
      this.mState = 4;
      this.mCalled = false;
      this.onResume();
      if (this.mCalled) {
         this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
         if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
         }

         this.mChildFragmentManager.dispatchResume();
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onResume()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performSaveInstanceState(Bundle var1) {
      this.onSaveInstanceState(var1);
      this.mSavedStateRegistryController.performSave(var1);
      Parcelable var2 = this.mChildFragmentManager.saveAllState();
      if (var2 != null) {
         var1.putParcelable("android:support:fragments", var2);
      }

   }

   void performStart() {
      this.mChildFragmentManager.noteStateNotSaved();
      this.mChildFragmentManager.execPendingActions(true);
      this.mState = 3;
      this.mCalled = false;
      this.onStart();
      if (this.mCalled) {
         this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
         if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START);
         }

         this.mChildFragmentManager.dispatchStart();
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onStart()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   void performStop() {
      this.mChildFragmentManager.dispatchStop();
      if (this.mView != null) {
         this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
      }

      this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
      this.mState = 2;
      this.mCalled = false;
      this.onStop();
      if (!this.mCalled) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Fragment ");
         var1.append(this);
         var1.append(" did not call through to super.onStop()");
         throw new SuperNotCalledException(var1.toString());
      }
   }

   public void postponeEnterTransition() {
      this.ensureAnimationInfo().mEnterTransitionPostponed = true;
   }

   public final void postponeEnterTransition(long var1, TimeUnit var3) {
      this.ensureAnimationInfo().mEnterTransitionPostponed = true;
      FragmentManager var4 = this.mFragmentManager;
      Handler var5;
      if (var4 != null) {
         var5 = var4.mHost.getHandler();
      } else {
         var5 = new Handler(Looper.getMainLooper());
      }

      var5.removeCallbacks(this.mPostponedDurationRunnable);
      var5.postDelayed(this.mPostponedDurationRunnable, var3.toMillis(var1));
   }

   public void registerForContextMenu(View var1) {
      var1.setOnCreateContextMenuListener(this);
   }

   public final void requestPermissions(String[] var1, int var2) {
      FragmentHostCallback var3 = this.mHost;
      if (var3 != null) {
         var3.onRequestPermissionsFromFragment(this, var1, var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Fragment ");
         var4.append(this);
         var4.append(" not attached to Activity");
         throw new IllegalStateException(var4.toString());
      }
   }

   public final FragmentActivity requireActivity() {
      FragmentActivity var1 = this.getActivity();
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" not attached to an activity.");
         throw new IllegalStateException(var2.toString());
      }
   }

   public final Bundle requireArguments() {
      Bundle var1 = this.getArguments();
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" does not have any arguments.");
         throw new IllegalStateException(var2.toString());
      }
   }

   public final Context requireContext() {
      Context var1 = this.getContext();
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" not attached to a context.");
         throw new IllegalStateException(var2.toString());
      }
   }

   @Deprecated
   public final FragmentManager requireFragmentManager() {
      return this.getParentFragmentManager();
   }

   public final Object requireHost() {
      Object var1 = this.getHost();
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" not attached to a host.");
         throw new IllegalStateException(var2.toString());
      }
   }

   public final Fragment requireParentFragment() {
      Fragment var1 = this.getParentFragment();
      if (var1 == null) {
         StringBuilder var2;
         if (this.getContext() == null) {
            var2 = new StringBuilder();
            var2.append("Fragment ");
            var2.append(this);
            var2.append(" is not attached to any Fragment or host");
            throw new IllegalStateException(var2.toString());
         } else {
            var2 = new StringBuilder();
            var2.append("Fragment ");
            var2.append(this);
            var2.append(" is not a child Fragment, it is directly attached to ");
            var2.append(this.getContext());
            throw new IllegalStateException(var2.toString());
         }
      } else {
         return var1;
      }
   }

   public final View requireView() {
      View var1 = this.getView();
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Fragment ");
         var2.append(this);
         var2.append(" did not return a View from onCreateView() or this was called before onCreateView().");
         throw new IllegalStateException(var2.toString());
      }
   }

   void restoreChildFragmentState(Bundle var1) {
      if (var1 != null) {
         Parcelable var2 = var1.getParcelable("android:support:fragments");
         if (var2 != null) {
            this.mChildFragmentManager.restoreSaveState(var2);
            this.mChildFragmentManager.dispatchCreate();
         }
      }

   }

   final void restoreViewState(Bundle var1) {
      SparseArray var2 = this.mSavedViewState;
      if (var2 != null) {
         this.mView.restoreHierarchyState(var2);
         this.mSavedViewState = null;
      }

      this.mCalled = false;
      this.onViewStateRestored(var1);
      if (this.mCalled) {
         if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
         }

      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Fragment ");
         var3.append(this);
         var3.append(" did not call through to super.onViewStateRestored()");
         throw new SuperNotCalledException(var3.toString());
      }
   }

   public void setAllowEnterTransitionOverlap(boolean var1) {
      this.ensureAnimationInfo().mAllowEnterTransitionOverlap = var1;
   }

   public void setAllowReturnTransitionOverlap(boolean var1) {
      this.ensureAnimationInfo().mAllowReturnTransitionOverlap = var1;
   }

   void setAnimatingAway(View var1) {
      this.ensureAnimationInfo().mAnimatingAway = var1;
   }

   void setAnimator(Animator var1) {
      this.ensureAnimationInfo().mAnimator = var1;
   }

   public void setArguments(Bundle var1) {
      if (this.mFragmentManager != null && this.isStateSaved()) {
         throw new IllegalStateException("Fragment already added and state has been saved");
      } else {
         this.mArguments = var1;
      }
   }

   public void setEnterSharedElementCallback(SharedElementCallback var1) {
      this.ensureAnimationInfo().mEnterTransitionCallback = var1;
   }

   public void setEnterTransition(Object var1) {
      this.ensureAnimationInfo().mEnterTransition = var1;
   }

   public void setExitSharedElementCallback(SharedElementCallback var1) {
      this.ensureAnimationInfo().mExitTransitionCallback = var1;
   }

   public void setExitTransition(Object var1) {
      this.ensureAnimationInfo().mExitTransition = var1;
   }

   public void setHasOptionsMenu(boolean var1) {
      if (this.mHasMenu != var1) {
         this.mHasMenu = var1;
         if (this.isAdded() && !this.isHidden()) {
            this.mHost.onSupportInvalidateOptionsMenu();
         }
      }

   }

   void setHideReplaced(boolean var1) {
      this.ensureAnimationInfo().mIsHideReplaced = var1;
   }

   public void setInitialSavedState(Fragment.SavedState var1) {
      if (this.mFragmentManager != null) {
         throw new IllegalStateException("Fragment already added");
      } else {
         Bundle var2;
         if (var1 != null && var1.mState != null) {
            var2 = var1.mState;
         } else {
            var2 = null;
         }

         this.mSavedFragmentState = var2;
      }
   }

   public void setMenuVisibility(boolean var1) {
      if (this.mMenuVisible != var1) {
         this.mMenuVisible = var1;
         if (this.mHasMenu && this.isAdded() && !this.isHidden()) {
            this.mHost.onSupportInvalidateOptionsMenu();
         }
      }

   }

   void setNextAnim(int var1) {
      if (this.mAnimationInfo != null || var1 != 0) {
         this.ensureAnimationInfo().mNextAnim = var1;
      }
   }

   void setNextTransition(int var1) {
      if (this.mAnimationInfo != null || var1 != 0) {
         this.ensureAnimationInfo();
         this.mAnimationInfo.mNextTransition = var1;
      }
   }

   void setOnStartEnterTransitionListener(Fragment.OnStartEnterTransitionListener var1) {
      this.ensureAnimationInfo();
      if (var1 != this.mAnimationInfo.mStartEnterTransitionListener) {
         if (var1 != null && this.mAnimationInfo.mStartEnterTransitionListener != null) {
            StringBuilder var2 = new StringBuilder();
            var2.append("Trying to set a replacement startPostponedEnterTransition on ");
            var2.append(this);
            throw new IllegalStateException(var2.toString());
         } else {
            if (this.mAnimationInfo.mEnterTransitionPostponed) {
               this.mAnimationInfo.mStartEnterTransitionListener = var1;
            }

            if (var1 != null) {
               var1.startListening();
            }

         }
      }
   }

   public void setReenterTransition(Object var1) {
      this.ensureAnimationInfo().mReenterTransition = var1;
   }

   public void setRetainInstance(boolean var1) {
      this.mRetainInstance = var1;
      FragmentManager var2 = this.mFragmentManager;
      if (var2 != null) {
         if (var1) {
            var2.addRetainedFragment(this);
         } else {
            var2.removeRetainedFragment(this);
         }
      } else {
         this.mRetainInstanceChangedWhileDetached = true;
      }
   }

   public void setReturnTransition(Object var1) {
      this.ensureAnimationInfo().mReturnTransition = var1;
   }

   public void setSharedElementEnterTransition(Object var1) {
      this.ensureAnimationInfo().mSharedElementEnterTransition = var1;
   }

   public void setSharedElementReturnTransition(Object var1) {
      this.ensureAnimationInfo().mSharedElementReturnTransition = var1;
   }

   void setStateAfterAnimating(int var1) {
      this.ensureAnimationInfo().mStateAfterAnimating = var1;
   }

   public void setTargetFragment(Fragment var1, int var2) {
      FragmentManager var4 = this.mFragmentManager;
      FragmentManager var3;
      if (var1 != null) {
         var3 = var1.mFragmentManager;
      } else {
         var3 = null;
      }

      StringBuilder var6;
      if (var4 != null && var3 != null && var4 != var3) {
         var6 = new StringBuilder();
         var6.append("Fragment ");
         var6.append(var1);
         var6.append(" must share the same FragmentManager to be set as a target fragment");
         throw new IllegalArgumentException(var6.toString());
      } else {
         for(Fragment var5 = var1; var5 != null; var5 = var5.getTargetFragment()) {
            if (var5 == this) {
               var6 = new StringBuilder();
               var6.append("Setting ");
               var6.append(var1);
               var6.append(" as the target of ");
               var6.append(this);
               var6.append(" would create a target cycle");
               throw new IllegalArgumentException(var6.toString());
            }
         }

         if (var1 == null) {
            this.mTargetWho = null;
            this.mTarget = null;
         } else if (this.mFragmentManager != null && var1.mFragmentManager != null) {
            this.mTargetWho = var1.mWho;
            this.mTarget = null;
         } else {
            this.mTargetWho = null;
            this.mTarget = var1;
         }

         this.mTargetRequestCode = var2;
      }
   }

   @Deprecated
   public void setUserVisibleHint(boolean var1) {
      if (!this.mUserVisibleHint && var1 && this.mState < 3 && this.mFragmentManager != null && this.isAdded() && this.mIsCreated) {
         this.mFragmentManager.performPendingDeferredStart(this);
      }

      this.mUserVisibleHint = var1;
      boolean var2;
      if (this.mState < 3 && !var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mDeferStart = var2;
      if (this.mSavedFragmentState != null) {
         this.mSavedUserVisibleHint = var1;
      }

   }

   public boolean shouldShowRequestPermissionRationale(String var1) {
      FragmentHostCallback var2 = this.mHost;
      return var2 != null ? var2.onShouldShowRequestPermissionRationale(var1) : false;
   }

   public void startActivity(Intent var1) {
      this.startActivity(var1, (Bundle)null);
   }

   public void startActivity(Intent var1, Bundle var2) {
      FragmentHostCallback var3 = this.mHost;
      if (var3 != null) {
         var3.onStartActivityFromFragment(this, var1, -1, var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Fragment ");
         var4.append(this);
         var4.append(" not attached to Activity");
         throw new IllegalStateException(var4.toString());
      }
   }

   public void startActivityForResult(Intent var1, int var2) {
      this.startActivityForResult(var1, var2, (Bundle)null);
   }

   public void startActivityForResult(Intent var1, int var2, Bundle var3) {
      FragmentHostCallback var4 = this.mHost;
      if (var4 != null) {
         var4.onStartActivityFromFragment(this, var1, var2, var3);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Fragment ");
         var5.append(this);
         var5.append(" not attached to Activity");
         throw new IllegalStateException(var5.toString());
      }
   }

   public void startIntentSenderForResult(IntentSender var1, int var2, Intent var3, int var4, int var5, int var6, Bundle var7) throws SendIntentException {
      FragmentHostCallback var8 = this.mHost;
      if (var8 != null) {
         var8.onStartIntentSenderFromFragment(this, var1, var2, var3, var4, var5, var6, var7);
      } else {
         StringBuilder var9 = new StringBuilder();
         var9.append("Fragment ");
         var9.append(this);
         var9.append(" not attached to Activity");
         throw new IllegalStateException(var9.toString());
      }
   }

   public void startPostponedEnterTransition() {
      FragmentManager var1 = this.mFragmentManager;
      if (var1 != null && var1.mHost != null) {
         if (Looper.myLooper() != this.mFragmentManager.mHost.getHandler().getLooper()) {
            this.mFragmentManager.mHost.getHandler().postAtFrontOfQueue(new Runnable() {
               public void run() {
                  Fragment.this.callStartTransitionListener();
               }
            });
         } else {
            this.callStartTransitionListener();
         }
      } else {
         this.ensureAnimationInfo().mEnterTransitionPostponed = false;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append(this.getClass().getSimpleName());
      var1.append("{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append("}");
      var1.append(" (");
      var1.append(this.mWho);
      var1.append(")");
      if (this.mFragmentId != 0) {
         var1.append(" id=0x");
         var1.append(Integer.toHexString(this.mFragmentId));
      }

      if (this.mTag != null) {
         var1.append(" ");
         var1.append(this.mTag);
      }

      var1.append('}');
      return var1.toString();
   }

   public void unregisterForContextMenu(View var1) {
      var1.setOnCreateContextMenuListener((OnCreateContextMenuListener)null);
   }

   static class AnimationInfo {
      Boolean mAllowEnterTransitionOverlap;
      Boolean mAllowReturnTransitionOverlap;
      View mAnimatingAway;
      Animator mAnimator;
      Object mEnterTransition = null;
      SharedElementCallback mEnterTransitionCallback;
      boolean mEnterTransitionPostponed;
      Object mExitTransition;
      SharedElementCallback mExitTransitionCallback;
      boolean mIsHideReplaced;
      int mNextAnim;
      int mNextTransition;
      Object mReenterTransition;
      Object mReturnTransition;
      Object mSharedElementEnterTransition;
      Object mSharedElementReturnTransition;
      Fragment.OnStartEnterTransitionListener mStartEnterTransitionListener;
      int mStateAfterAnimating;

      AnimationInfo() {
         this.mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
         this.mExitTransition = null;
         this.mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;
         this.mSharedElementEnterTransition = null;
         this.mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
         this.mEnterTransitionCallback = null;
         this.mExitTransitionCallback = null;
      }
   }

   public static class InstantiationException extends RuntimeException {
      public InstantiationException(String var1, Exception var2) {
         super(var1, var2);
      }
   }

   interface OnStartEnterTransitionListener {
      void onStartEnterTransition();

      void startListening();
   }

   public static class SavedState implements Parcelable {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public Fragment.SavedState createFromParcel(Parcel var1) {
            return new Fragment.SavedState(var1, (ClassLoader)null);
         }

         public Fragment.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new Fragment.SavedState(var1, var2);
         }

         public Fragment.SavedState[] newArray(int var1) {
            return new Fragment.SavedState[var1];
         }
      };
      final Bundle mState;

      SavedState(Bundle var1) {
         this.mState = var1;
      }

      SavedState(Parcel var1, ClassLoader var2) {
         Bundle var3 = var1.readBundle();
         this.mState = var3;
         if (var2 != null && var3 != null) {
            var3.setClassLoader(var2);
         }

      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeBundle(this.mState);
      }
   }
}
