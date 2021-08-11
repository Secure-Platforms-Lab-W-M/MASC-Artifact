package androidx.fragment.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.collection.SimpleArrayMap;
import androidx.core.util.Preconditions;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentController {
   private final FragmentHostCallback mHost;

   private FragmentController(FragmentHostCallback var1) {
      this.mHost = var1;
   }

   public static FragmentController createController(FragmentHostCallback var0) {
      return new FragmentController((FragmentHostCallback)Preconditions.checkNotNull(var0, "callbacks == null"));
   }

   public void attachHost(Fragment var1) {
      FragmentManager var2 = this.mHost.mFragmentManager;
      FragmentHostCallback var3 = this.mHost;
      var2.attachController(var3, var3, var1);
   }

   public void dispatchActivityCreated() {
      this.mHost.mFragmentManager.dispatchActivityCreated();
   }

   public void dispatchConfigurationChanged(Configuration var1) {
      this.mHost.mFragmentManager.dispatchConfigurationChanged(var1);
   }

   public boolean dispatchContextItemSelected(MenuItem var1) {
      return this.mHost.mFragmentManager.dispatchContextItemSelected(var1);
   }

   public void dispatchCreate() {
      this.mHost.mFragmentManager.dispatchCreate();
   }

   public boolean dispatchCreateOptionsMenu(Menu var1, MenuInflater var2) {
      return this.mHost.mFragmentManager.dispatchCreateOptionsMenu(var1, var2);
   }

   public void dispatchDestroy() {
      this.mHost.mFragmentManager.dispatchDestroy();
   }

   public void dispatchDestroyView() {
      this.mHost.mFragmentManager.dispatchDestroyView();
   }

   public void dispatchLowMemory() {
      this.mHost.mFragmentManager.dispatchLowMemory();
   }

   public void dispatchMultiWindowModeChanged(boolean var1) {
      this.mHost.mFragmentManager.dispatchMultiWindowModeChanged(var1);
   }

   public boolean dispatchOptionsItemSelected(MenuItem var1) {
      return this.mHost.mFragmentManager.dispatchOptionsItemSelected(var1);
   }

   public void dispatchOptionsMenuClosed(Menu var1) {
      this.mHost.mFragmentManager.dispatchOptionsMenuClosed(var1);
   }

   public void dispatchPause() {
      this.mHost.mFragmentManager.dispatchPause();
   }

   public void dispatchPictureInPictureModeChanged(boolean var1) {
      this.mHost.mFragmentManager.dispatchPictureInPictureModeChanged(var1);
   }

   public boolean dispatchPrepareOptionsMenu(Menu var1) {
      return this.mHost.mFragmentManager.dispatchPrepareOptionsMenu(var1);
   }

   @Deprecated
   public void dispatchReallyStop() {
   }

   public void dispatchResume() {
      this.mHost.mFragmentManager.dispatchResume();
   }

   public void dispatchStart() {
      this.mHost.mFragmentManager.dispatchStart();
   }

   public void dispatchStop() {
      this.mHost.mFragmentManager.dispatchStop();
   }

   @Deprecated
   public void doLoaderDestroy() {
   }

   @Deprecated
   public void doLoaderRetain() {
   }

   @Deprecated
   public void doLoaderStart() {
   }

   @Deprecated
   public void doLoaderStop(boolean var1) {
   }

   @Deprecated
   public void dumpLoaders(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
   }

   public boolean execPendingActions() {
      return this.mHost.mFragmentManager.execPendingActions(true);
   }

   public Fragment findFragmentByWho(String var1) {
      return this.mHost.mFragmentManager.findFragmentByWho(var1);
   }

   public List getActiveFragments(List var1) {
      return this.mHost.mFragmentManager.getActiveFragments();
   }

   public int getActiveFragmentsCount() {
      return this.mHost.mFragmentManager.getActiveFragmentCount();
   }

   public FragmentManager getSupportFragmentManager() {
      return this.mHost.mFragmentManager;
   }

   @Deprecated
   public LoaderManager getSupportLoaderManager() {
      throw new UnsupportedOperationException("Loaders are managed separately from FragmentController, use LoaderManager.getInstance() to obtain a LoaderManager.");
   }

   public void noteStateNotSaved() {
      this.mHost.mFragmentManager.noteStateNotSaved();
   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.mHost.mFragmentManager.getLayoutInflaterFactory().onCreateView(var1, var2, var3, var4);
   }

   @Deprecated
   public void reportLoaderStart() {
   }

   @Deprecated
   public void restoreAllState(Parcelable var1, FragmentManagerNonConfig var2) {
      this.mHost.mFragmentManager.restoreAllState(var1, var2);
   }

   @Deprecated
   public void restoreAllState(Parcelable var1, List var2) {
      this.mHost.mFragmentManager.restoreAllState(var1, new FragmentManagerNonConfig(var2, (Map)null, (Map)null));
   }

   @Deprecated
   public void restoreLoaderNonConfig(SimpleArrayMap var1) {
   }

   public void restoreSaveState(Parcelable var1) {
      FragmentHostCallback var2 = this.mHost;
      if (var2 instanceof ViewModelStoreOwner) {
         var2.mFragmentManager.restoreSaveState(var1);
      } else {
         throw new IllegalStateException("Your FragmentHostCallback must implement ViewModelStoreOwner to call restoreSaveState(). Call restoreAllState()  if you're still using retainNestedNonConfig().");
      }
   }

   @Deprecated
   public SimpleArrayMap retainLoaderNonConfig() {
      return null;
   }

   @Deprecated
   public FragmentManagerNonConfig retainNestedNonConfig() {
      return this.mHost.mFragmentManager.retainNonConfig();
   }

   @Deprecated
   public List retainNonConfig() {
      FragmentManagerNonConfig var1 = this.mHost.mFragmentManager.retainNonConfig();
      return var1 != null && var1.getFragments() != null ? new ArrayList(var1.getFragments()) : null;
   }

   public Parcelable saveAllState() {
      return this.mHost.mFragmentManager.saveAllState();
   }
}
