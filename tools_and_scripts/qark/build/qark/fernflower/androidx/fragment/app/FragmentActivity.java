package androidx.fragment.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;

public class FragmentActivity extends ComponentActivity implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompat.RequestPermissionsRequestCodeValidator {
   static final String ALLOCATED_REQUEST_INDICIES_TAG = "android:support:request_indicies";
   static final String FRAGMENTS_TAG = "android:support:fragments";
   static final int MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS = 65534;
   static final String NEXT_CANDIDATE_REQUEST_INDEX_TAG = "android:support:next_request_index";
   static final String REQUEST_FRAGMENT_WHO_TAG = "android:support:request_fragment_who";
   private static final String TAG = "FragmentActivity";
   boolean mCreated;
   final LifecycleRegistry mFragmentLifecycleRegistry = new LifecycleRegistry(this);
   final FragmentController mFragments = FragmentController.createController(new FragmentActivity.HostCallbacks());
   int mNextCandidateRequestIndex;
   SparseArrayCompat mPendingFragmentActivityResults;
   boolean mRequestedPermissionsFromFragment;
   boolean mResumed;
   boolean mStartedActivityFromFragment;
   boolean mStartedIntentSenderFromFragment;
   boolean mStopped = true;

   public FragmentActivity() {
   }

   public FragmentActivity(int var1) {
      super(var1);
   }

   private int allocateRequestIndex(Fragment var1) {
      if (this.mPendingFragmentActivityResults.size() >= 65534) {
         throw new IllegalStateException("Too many pending Fragment activity results.");
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

   static void checkForValidRequestCode(int var0) {
      if ((-65536 & var0) != 0) {
         throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
      }
   }

   private void markFragmentsCreated() {
      while(markState(this.getSupportFragmentManager(), Lifecycle.State.CREATED)) {
      }

   }

   private static boolean markState(FragmentManager var0, Lifecycle.State var1) {
      boolean var2 = false;
      Iterator var5 = var0.getFragments().iterator();

      while(var5.hasNext()) {
         Fragment var4 = (Fragment)var5.next();
         if (var4 != null) {
            boolean var3 = var2;
            if (var4.getHost() != null) {
               var3 = var2 | markState(var4.getChildFragmentManager(), var1);
            }

            var2 = var3;
            if (var4.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
               var4.mLifecycleRegistry.setCurrentState(var1);
               var2 = true;
            }
         }
      }

      return var2;
   }

   final View dispatchFragmentsOnCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.mFragments.onCreateView(var1, var2, var3, var4);
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
      var3.print(" mResumed=");
      var3.print(this.mResumed);
      var3.print(" mStopped=");
      var3.print(this.mStopped);
      if (this.getApplication() != null) {
         LoaderManager.getInstance(this).dump(var6, var2, var3, var4);
      }

      this.mFragments.getSupportFragmentManager().dump(var1, var2, var3, var4);
   }

   public FragmentManager getSupportFragmentManager() {
      return this.mFragments.getSupportFragmentManager();
   }

   @Deprecated
   public LoaderManager getSupportLoaderManager() {
      return LoaderManager.getInstance(this);
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      this.mFragments.noteStateNotSaved();
      int var4 = var1 >> 16;
      if (var4 != 0) {
         --var4;
         String var8 = (String)this.mPendingFragmentActivityResults.get(var4);
         this.mPendingFragmentActivityResults.remove(var4);
         if (var8 == null) {
            Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
         } else {
            Fragment var6 = this.mFragments.findFragmentByWho(var8);
            if (var6 == null) {
               StringBuilder var7 = new StringBuilder();
               var7.append("Activity result no fragment exists for who: ");
               var7.append(var8);
               Log.w("FragmentActivity", var7.toString());
            } else {
               var6.onActivityResult('\uffff' & var1, var2, var3);
            }
         }
      } else {
         ActivityCompat.PermissionCompatDelegate var5 = ActivityCompat.getPermissionCompatDelegate();
         if (var5 == null || !var5.onActivityResult(this, var1, var2, var3)) {
            super.onActivityResult(var1, var2, var3);
         }
      }
   }

   public void onAttachFragment(Fragment var1) {
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.mFragments.noteStateNotSaved();
      this.mFragments.dispatchConfigurationChanged(var1);
   }

   protected void onCreate(Bundle var1) {
      this.mFragments.attachHost((Fragment)null);
      if (var1 != null) {
         Parcelable var3 = var1.getParcelable("android:support:fragments");
         this.mFragments.restoreSaveState(var3);
         if (var1.containsKey("android:support:next_request_index")) {
            this.mNextCandidateRequestIndex = var1.getInt("android:support:next_request_index");
            int[] var5 = var1.getIntArray("android:support:request_indicies");
            String[] var4 = var1.getStringArray("android:support:request_fragment_who");
            if (var5 != null && var4 != null && var5.length == var4.length) {
               this.mPendingFragmentActivityResults = new SparseArrayCompat(var5.length);

               for(int var2 = 0; var2 < var5.length; ++var2) {
                  this.mPendingFragmentActivityResults.put(var5[var2], var4[var2]);
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

      super.onCreate(var1);
      this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
      this.mFragments.dispatchCreate();
   }

   public boolean onCreatePanelMenu(int var1, Menu var2) {
      return var1 == 0 ? super.onCreatePanelMenu(var1, var2) | this.mFragments.dispatchCreateOptionsMenu(var2, this.getMenuInflater()) : super.onCreatePanelMenu(var1, var2);
   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      View var5 = this.dispatchFragmentsOnCreateView(var1, var2, var3, var4);
      return var5 == null ? super.onCreateView(var1, var2, var3, var4) : var5;
   }

   public View onCreateView(String var1, Context var2, AttributeSet var3) {
      View var4 = this.dispatchFragmentsOnCreateView((View)null, var1, var2, var3);
      return var4 == null ? super.onCreateView(var1, var2, var3) : var4;
   }

   protected void onDestroy() {
      super.onDestroy();
      this.mFragments.dispatchDestroy();
      this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
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
      this.mFragments.dispatchPause();
      this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
   }

   public void onPictureInPictureModeChanged(boolean var1) {
      this.mFragments.dispatchPictureInPictureModeChanged(var1);
   }

   protected void onPostResume() {
      super.onPostResume();
      this.onResumeFragments();
   }

   @Deprecated
   protected boolean onPrepareOptionsPanel(View var1, Menu var2) {
      return super.onPreparePanel(0, var1, var2);
   }

   public boolean onPreparePanel(int var1, View var2, Menu var3) {
      return var1 == 0 ? this.onPrepareOptionsPanel(var2, var3) | this.mFragments.dispatchPrepareOptionsMenu(var3) : super.onPreparePanel(var1, var2, var3);
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      this.mFragments.noteStateNotSaved();
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
      this.mResumed = true;
      this.mFragments.noteStateNotSaved();
      this.mFragments.execPendingActions();
   }

   protected void onResumeFragments() {
      this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
      this.mFragments.dispatchResume();
   }

   protected void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      this.markFragmentsCreated();
      this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
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
      if (!this.mCreated) {
         this.mCreated = true;
         this.mFragments.dispatchActivityCreated();
      }

      this.mFragments.noteStateNotSaved();
      this.mFragments.execPendingActions();
      this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
      this.mFragments.dispatchStart();
   }

   public void onStateNotSaved() {
      this.mFragments.noteStateNotSaved();
   }

   protected void onStop() {
      super.onStop();
      this.mStopped = true;
      this.markFragmentsCreated();
      this.mFragments.dispatchStop();
      this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
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

   public void startActivityForResult(Intent var1, int var2, Bundle var3) {
      if (!this.mStartedActivityFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startActivityForResult(var1, var2, var3);
   }

   public void startActivityFromFragment(Fragment var1, Intent var2, int var3) {
      this.startActivityFromFragment(var1, var2, var3, (Bundle)null);
   }

   public void startActivityFromFragment(Fragment var1, Intent var2, int var3, Bundle var4) {
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

   public void startIntentSenderForResult(IntentSender var1, int var2, Intent var3, int var4, int var5, int var6) throws SendIntentException {
      if (!this.mStartedIntentSenderFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startIntentSenderForResult(var1, var2, var3, var4, var5, var6);
   }

   public void startIntentSenderForResult(IntentSender var1, int var2, Intent var3, int var4, int var5, int var6, Bundle var7) throws SendIntentException {
      if (!this.mStartedIntentSenderFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startIntentSenderForResult(var1, var2, var3, var4, var5, var6, var7);
   }

   public void startIntentSenderFromFragment(Fragment var1, IntentSender var2, int var3, Intent var4, int var5, int var6, int var7, Bundle var8) throws SendIntentException {
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

   class HostCallbacks extends FragmentHostCallback implements ViewModelStoreOwner, OnBackPressedDispatcherOwner {
      public HostCallbacks() {
         super(FragmentActivity.this);
      }

      public Lifecycle getLifecycle() {
         return FragmentActivity.this.mFragmentLifecycleRegistry;
      }

      public OnBackPressedDispatcher getOnBackPressedDispatcher() {
         return FragmentActivity.this.getOnBackPressedDispatcher();
      }

      public ViewModelStore getViewModelStore() {
         return FragmentActivity.this.getViewModelStore();
      }

      public void onAttachFragment(Fragment var1) {
         FragmentActivity.this.onAttachFragment(var1);
      }

      public void onDump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
         FragmentActivity.this.dump(var1, var2, var3, var4);
      }

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

      public void onRequestPermissionsFromFragment(Fragment var1, String[] var2, int var3) {
         FragmentActivity.this.requestPermissionsFromFragment(var1, var2, var3);
      }

      public boolean onShouldSaveFragmentState(Fragment var1) {
         return FragmentActivity.this.isFinishing() ^ true;
      }

      public boolean onShouldShowRequestPermissionRationale(String var1) {
         return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, var1);
      }

      public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3) {
         FragmentActivity.this.startActivityFromFragment(var1, var2, var3);
      }

      public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3, Bundle var4) {
         FragmentActivity.this.startActivityFromFragment(var1, var2, var3, var4);
      }

      public void onStartIntentSenderFromFragment(Fragment var1, IntentSender var2, int var3, Intent var4, int var5, int var6, int var7, Bundle var8) throws SendIntentException {
         FragmentActivity.this.startIntentSenderFromFragment(var1, var2, var3, var4, var5, var6, var7, var8);
      }

      public void onSupportInvalidateOptionsMenu() {
         FragmentActivity.this.supportInvalidateOptionsMenu();
      }
   }
}
