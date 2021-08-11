package android.support.v4.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class FragmentActivity extends BaseFragmentActivityApi16 implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompat.RequestPermissionsRequestCodeValidator {
   static final String ALLOCATED_REQUEST_INDICIES_TAG = "android:support:request_indicies";
   static final String FRAGMENTS_TAG = "android:support:fragments";
   static final int MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS = 65534;
   static final int MSG_REALLY_STOPPED = 1;
   static final int MSG_RESUME_PENDING = 2;
   static final String NEXT_CANDIDATE_REQUEST_INDEX_TAG = "android:support:next_request_index";
   static final String REQUEST_FRAGMENT_WHO_TAG = "android:support:request_fragment_who";
   private static final String TAG = "FragmentActivity";
   boolean mCreated;
   final FragmentController mFragments = FragmentController.createController(new FragmentActivity.HostCallbacks());
   final Handler mHandler = new Handler() {
      public void handleMessage(Message var1) {
         int var2 = var1.what;
         if (var2 != 1) {
            if (var2 != 2) {
               super.handleMessage(var1);
            } else {
               FragmentActivity.this.onResumeFragments();
               FragmentActivity.this.mFragments.execPendingActions();
            }
         } else {
            if (FragmentActivity.this.mStopped) {
               FragmentActivity.this.doReallyStop(false);
            }

         }
      }
   };
   int mNextCandidateRequestIndex;
   SparseArrayCompat mPendingFragmentActivityResults;
   boolean mReallyStopped = true;
   boolean mRequestedPermissionsFromFragment;
   boolean mResumed;
   boolean mRetaining;
   boolean mStopped = true;

   private int allocateRequestIndex(Fragment var1) {
      if (this.mPendingFragmentActivityResults.size() >= 65534) {
         IllegalStateException var3 = new IllegalStateException("Too many pending Fragment activity results.");
         throw var3;
      } else {
         while(this.mPendingFragmentActivityResults.indexOfKey(this.mNextCandidateRequestIndex) >= 0) {
            this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % '\ufffe';
         }

         int var2 = this.mNextCandidateRequestIndex;
         this.mPendingFragmentActivityResults.put(var2, var1.mWho);
         this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % '\ufffe';
         return var2;
      }
   }

   final View dispatchFragmentsOnCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.mFragments.onCreateView(var1, var2, var3, var4);
   }

   void doReallyStop(boolean var1) {
      if (!this.mReallyStopped) {
         this.mReallyStopped = true;
         this.mRetaining = var1;
         this.mHandler.removeMessages(1);
         this.onReallyStop();
      } else {
         if (var1) {
            this.mFragments.doLoaderStart();
            this.mFragments.doLoaderStop(true);
         }

      }
   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      super.dump(var1, var2, var3, var4);
      var3.print(var1);
      var3.print("Local FragmentActivity ");
      var3.print(Integer.toHexString(System.identityHashCode(this)));
      var3.println(" State:");
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append("  ");
      String var6 = var5.toString();
      var3.print(var6);
      var3.print("mCreated=");
      var3.print(this.mCreated);
      var3.print("mResumed=");
      var3.print(this.mResumed);
      var3.print(" mStopped=");
      var3.print(this.mStopped);
      var3.print(" mReallyStopped=");
      var3.println(this.mReallyStopped);
      this.mFragments.dumpLoaders(var6, var2, var3, var4);
      this.mFragments.getSupportFragmentManager().dump(var1, var2, var3, var4);
   }

   public Object getLastCustomNonConfigurationInstance() {
      FragmentActivity.NonConfigurationInstances var1 = (FragmentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
      return var1 != null ? var1.custom : null;
   }

   public FragmentManager getSupportFragmentManager() {
      return this.mFragments.getSupportFragmentManager();
   }

   public LoaderManager getSupportLoaderManager() {
      return this.mFragments.getSupportLoaderManager();
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      this.mFragments.noteStateNotSaved();
      int var4 = var1 >> 16;
      if (var4 != 0) {
         --var4;
         String var5 = (String)this.mPendingFragmentActivityResults.get(var4);
         this.mPendingFragmentActivityResults.remove(var4);
         if (var5 == null) {
            Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
         } else {
            Fragment var6 = this.mFragments.findFragmentByWho(var5);
            if (var6 == null) {
               StringBuilder var7 = new StringBuilder();
               var7.append("Activity result no fragment exists for who: ");
               var7.append(var5);
               Log.w("FragmentActivity", var7.toString());
            } else {
               var6.onActivityResult('\uffff' & var1, var2, var3);
            }
         }
      } else {
         super.onActivityResult(var1, var2, var3);
      }
   }

   public void onAttachFragment(Fragment var1) {
   }

   public void onBackPressed() {
      FragmentManager var2 = this.mFragments.getSupportFragmentManager();
      boolean var1 = var2.isStateSaved();
      if (!var1 || VERSION.SDK_INT > 25) {
         if (var1 || !var2.popBackStackImmediate()) {
            super.onBackPressed();
         }

      }
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.mFragments.dispatchConfigurationChanged(var1);
   }

   protected void onCreate(@Nullable Bundle var1) {
      FragmentController var4 = this.mFragments;
      FragmentManagerNonConfig var3 = null;
      var4.attachHost((Fragment)null);
      super.onCreate(var1);
      FragmentActivity.NonConfigurationInstances var6 = (FragmentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
      if (var6 != null) {
         this.mFragments.restoreLoaderNonConfig(var6.loaders);
      }

      if (var1 != null) {
         Parcelable var9 = var1.getParcelable("android:support:fragments");
         FragmentController var5 = this.mFragments;
         if (var6 != null) {
            var3 = var6.fragments;
         }

         var5.restoreAllState(var9, var3);
         if (var1.containsKey("android:support:next_request_index")) {
            this.mNextCandidateRequestIndex = var1.getInt("android:support:next_request_index");
            int[] var8 = var1.getIntArray("android:support:request_indicies");
            String[] var7 = var1.getStringArray("android:support:request_fragment_who");
            if (var8 != null && var7 != null && var8.length == var7.length) {
               this.mPendingFragmentActivityResults = new SparseArrayCompat(var8.length);

               for(int var2 = 0; var2 < var8.length; ++var2) {
                  this.mPendingFragmentActivityResults.put(var8[var2], var7[var2]);
               }
            } else {
               Log.w("FragmentActivity", "Invalid requestCode mapping in savedInstanceState.");
            }
         }
      }

      if (this.mPendingFragmentActivityResults == null) {
         this.mPendingFragmentActivityResults = new SparseArrayCompat();
         this.mNextCandidateRequestIndex = 0;
      }

      this.mFragments.dispatchCreate();
   }

   public boolean onCreatePanelMenu(int var1, Menu var2) {
      return var1 == 0 ? super.onCreatePanelMenu(var1, var2) | this.mFragments.dispatchCreateOptionsMenu(var2, this.getMenuInflater()) : super.onCreatePanelMenu(var1, var2);
   }

   protected void onDestroy() {
      super.onDestroy();
      this.doReallyStop(false);
      this.mFragments.dispatchDestroy();
      this.mFragments.doLoaderDestroy();
   }

   public void onLowMemory() {
      super.onLowMemory();
      this.mFragments.dispatchLowMemory();
   }

   public boolean onMenuItemSelected(int var1, MenuItem var2) {
      if (super.onMenuItemSelected(var1, var2)) {
         return true;
      } else if (var1 != 0) {
         return var1 != 6 ? false : this.mFragments.dispatchContextItemSelected(var2);
      } else {
         return this.mFragments.dispatchOptionsItemSelected(var2);
      }
   }

   @CallSuper
   public void onMultiWindowModeChanged(boolean var1) {
      this.mFragments.dispatchMultiWindowModeChanged(var1);
   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      this.mFragments.noteStateNotSaved();
   }

   public void onPanelClosed(int var1, Menu var2) {
      if (var1 == 0) {
         this.mFragments.dispatchOptionsMenuClosed(var2);
      }

      super.onPanelClosed(var1, var2);
   }

   protected void onPause() {
      super.onPause();
      this.mResumed = false;
      if (this.mHandler.hasMessages(2)) {
         this.mHandler.removeMessages(2);
         this.onResumeFragments();
      }

      this.mFragments.dispatchPause();
   }

   @CallSuper
   public void onPictureInPictureModeChanged(boolean var1) {
      this.mFragments.dispatchPictureInPictureModeChanged(var1);
   }

   protected void onPostResume() {
      super.onPostResume();
      this.mHandler.removeMessages(2);
      this.onResumeFragments();
      this.mFragments.execPendingActions();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected boolean onPrepareOptionsPanel(View var1, Menu var2) {
      return super.onPreparePanel(0, var1, var2);
   }

   public boolean onPreparePanel(int var1, View var2, Menu var3) {
      return var1 == 0 && var3 != null ? this.onPrepareOptionsPanel(var2, var3) | this.mFragments.dispatchPrepareOptionsMenu(var3) : super.onPreparePanel(var1, var2, var3);
   }

   void onReallyStop() {
      this.mFragments.doLoaderStop(this.mRetaining);
      this.mFragments.dispatchReallyStop();
   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
      int var4 = var1 >> 16 & '\uffff';
      if (var4 != 0) {
         --var4;
         String var5 = (String)this.mPendingFragmentActivityResults.get(var4);
         this.mPendingFragmentActivityResults.remove(var4);
         if (var5 == null) {
            Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
            return;
         }

         Fragment var6 = this.mFragments.findFragmentByWho(var5);
         if (var6 == null) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Activity result no fragment exists for who: ");
            var7.append(var5);
            Log.w("FragmentActivity", var7.toString());
            return;
         }

         var6.onRequestPermissionsResult('\uffff' & var1, var2, var3);
      }

   }

   protected void onResume() {
      super.onResume();
      this.mHandler.sendEmptyMessage(2);
      this.mResumed = true;
      this.mFragments.execPendingActions();
   }

   protected void onResumeFragments() {
      this.mFragments.dispatchResume();
   }

   public Object onRetainCustomNonConfigurationInstance() {
      return null;
   }

   public final Object onRetainNonConfigurationInstance() {
      if (this.mStopped) {
         this.doReallyStop(true);
      }

      Object var1 = this.onRetainCustomNonConfigurationInstance();
      FragmentManagerNonConfig var2 = this.mFragments.retainNestedNonConfig();
      SimpleArrayMap var3 = this.mFragments.retainLoaderNonConfig();
      if (var2 == null && var3 == null && var1 == null) {
         return null;
      } else {
         FragmentActivity.NonConfigurationInstances var4 = new FragmentActivity.NonConfigurationInstances();
         var4.custom = var1;
         var4.fragments = var2;
         var4.loaders = var3;
         return var4;
      }
   }

   protected void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      Parcelable var3 = this.mFragments.saveAllState();
      if (var3 != null) {
         var1.putParcelable("android:support:fragments", var3);
      }

      if (this.mPendingFragmentActivityResults.size() > 0) {
         var1.putInt("android:support:next_request_index", this.mNextCandidateRequestIndex);
         int[] var5 = new int[this.mPendingFragmentActivityResults.size()];
         String[] var4 = new String[this.mPendingFragmentActivityResults.size()];

         for(int var2 = 0; var2 < this.mPendingFragmentActivityResults.size(); ++var2) {
            var5[var2] = this.mPendingFragmentActivityResults.keyAt(var2);
            var4[var2] = (String)this.mPendingFragmentActivityResults.valueAt(var2);
         }

         var1.putIntArray("android:support:request_indicies", var5);
         var1.putStringArray("android:support:request_fragment_who", var4);
      }

   }

   protected void onStart() {
      super.onStart();
      this.mStopped = false;
      this.mReallyStopped = false;
      this.mHandler.removeMessages(1);
      if (!this.mCreated) {
         this.mCreated = true;
         this.mFragments.dispatchActivityCreated();
      }

      this.mFragments.noteStateNotSaved();
      this.mFragments.execPendingActions();
      this.mFragments.doLoaderStart();
      this.mFragments.dispatchStart();
      this.mFragments.reportLoaderStart();
   }

   public void onStateNotSaved() {
      this.mFragments.noteStateNotSaved();
   }

   protected void onStop() {
      super.onStop();
      this.mStopped = true;
      this.mHandler.sendEmptyMessage(1);
      this.mFragments.dispatchStop();
   }

   void requestPermissionsFromFragment(Fragment var1, String[] var2, int var3) {
      if (var3 == -1) {
         ActivityCompat.requestPermissions(this, var2, var3);
      } else {
         checkForValidRequestCode(var3);

         try {
            this.mRequestedPermissionsFromFragment = true;
            ActivityCompat.requestPermissions(this, var2, (this.allocateRequestIndex(var1) + 1 << 16) + ('\uffff' & var3));
         } finally {
            this.mRequestedPermissionsFromFragment = false;
         }

      }
   }

   public void setEnterSharedElementCallback(SharedElementCallback var1) {
      ActivityCompat.setEnterSharedElementCallback(this, var1);
   }

   public void setExitSharedElementCallback(SharedElementCallback var1) {
      ActivityCompat.setExitSharedElementCallback(this, var1);
   }

   public void startActivityForResult(Intent var1, int var2) {
      if (!this.mStartedActivityFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startActivityForResult(var1, var2);
   }

   public void startActivityFromFragment(Fragment var1, Intent var2, int var3) {
      this.startActivityFromFragment(var1, var2, var3, (Bundle)null);
   }

   public void startActivityFromFragment(Fragment var1, Intent var2, int var3, @Nullable Bundle var4) {
      this.mStartedActivityFromFragment = true;
      Throwable var10000;
      boolean var10001;
      if (var3 == -1) {
         label55: {
            try {
               ActivityCompat.startActivityForResult(this, var2, -1, var4);
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label55;
            }

            this.mStartedActivityFromFragment = false;
            return;
         }
      } else {
         label58: {
            try {
               checkForValidRequestCode(var3);
               ActivityCompat.startActivityForResult(this, var2, (this.allocateRequestIndex(var1) + 1 << 16) + ('\uffff' & var3), var4);
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               break label58;
            }

            this.mStartedActivityFromFragment = false;
            return;
         }
      }

      Throwable var11 = var10000;
      this.mStartedActivityFromFragment = false;
      throw var11;
   }

   public void startIntentSenderFromFragment(Fragment var1, IntentSender var2, int var3, @Nullable Intent var4, int var5, int var6, int var7, Bundle var8) throws SendIntentException {
      this.mStartedIntentSenderFromFragment = true;
      Throwable var10000;
      boolean var10001;
      if (var3 == -1) {
         label55: {
            try {
               ActivityCompat.startIntentSenderForResult(this, var2, var3, var4, var5, var6, var7, var8);
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label55;
            }

            this.mStartedIntentSenderFromFragment = false;
            return;
         }
      } else {
         label58: {
            try {
               checkForValidRequestCode(var3);
               ActivityCompat.startIntentSenderForResult(this, var2, (this.allocateRequestIndex(var1) + 1 << 16) + ('\uffff' & var3), var4, var5, var6, var7, var8);
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label58;
            }

            this.mStartedIntentSenderFromFragment = false;
            return;
         }
      }

      Throwable var15 = var10000;
      this.mStartedIntentSenderFromFragment = false;
      throw var15;
   }

   public void supportFinishAfterTransition() {
      ActivityCompat.finishAfterTransition(this);
   }

   @Deprecated
   public void supportInvalidateOptionsMenu() {
      this.invalidateOptionsMenu();
   }

   public void supportPostponeEnterTransition() {
      ActivityCompat.postponeEnterTransition(this);
   }

   public void supportStartPostponedEnterTransition() {
      ActivityCompat.startPostponedEnterTransition(this);
   }

   public final void validateRequestPermissionsRequestCode(int var1) {
      if (!this.mRequestedPermissionsFromFragment && var1 != -1) {
         checkForValidRequestCode(var1);
      }

   }

   class HostCallbacks extends FragmentHostCallback {
      public HostCallbacks() {
         super(FragmentActivity.this);
      }

      public void onAttachFragment(Fragment var1) {
         FragmentActivity.this.onAttachFragment(var1);
      }

      public void onDump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
         FragmentActivity.this.dump(var1, var2, var3, var4);
      }

      @Nullable
      public View onFindViewById(int var1) {
         return FragmentActivity.this.findViewById(var1);
      }

      public FragmentActivity onGetHost() {
         return FragmentActivity.this;
      }

      public LayoutInflater onGetLayoutInflater() {
         return FragmentActivity.this.getLayoutInflater().cloneInContext(FragmentActivity.this);
      }

      public int onGetWindowAnimations() {
         Window var1 = FragmentActivity.this.getWindow();
         return var1 == null ? 0 : var1.getAttributes().windowAnimations;
      }

      public boolean onHasView() {
         Window var1 = FragmentActivity.this.getWindow();
         return var1 != null && var1.peekDecorView() != null;
      }

      public boolean onHasWindowAnimations() {
         return FragmentActivity.this.getWindow() != null;
      }

      public void onRequestPermissionsFromFragment(@NonNull Fragment var1, @NonNull String[] var2, int var3) {
         FragmentActivity.this.requestPermissionsFromFragment(var1, var2, var3);
      }

      public boolean onShouldSaveFragmentState(Fragment var1) {
         return FragmentActivity.this.isFinishing() ^ true;
      }

      public boolean onShouldShowRequestPermissionRationale(@NonNull String var1) {
         return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, var1);
      }

      public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3) {
         FragmentActivity.this.startActivityFromFragment(var1, var2, var3);
      }

      public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3, @Nullable Bundle var4) {
         FragmentActivity.this.startActivityFromFragment(var1, var2, var3, var4);
      }

      public void onStartIntentSenderFromFragment(Fragment var1, IntentSender var2, int var3, @Nullable Intent var4, int var5, int var6, int var7, Bundle var8) throws SendIntentException {
         FragmentActivity.this.startIntentSenderFromFragment(var1, var2, var3, var4, var5, var6, var7, var8);
      }

      public void onSupportInvalidateOptionsMenu() {
         FragmentActivity.this.supportInvalidateOptionsMenu();
      }
   }

   static final class NonConfigurationInstances {
      Object custom;
      FragmentManagerNonConfig fragments;
      SimpleArrayMap loaders;
   }
}
