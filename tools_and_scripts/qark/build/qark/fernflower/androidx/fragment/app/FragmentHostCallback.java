package androidx.fragment.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class FragmentHostCallback extends FragmentContainer {
   private final Activity mActivity;
   private final Context mContext;
   final FragmentManager mFragmentManager;
   private final Handler mHandler;
   private final int mWindowAnimations;

   FragmentHostCallback(Activity var1, Context var2, Handler var3, int var4) {
      this.mFragmentManager = new FragmentManagerImpl();
      this.mActivity = var1;
      this.mContext = (Context)Preconditions.checkNotNull(var2, "context == null");
      this.mHandler = (Handler)Preconditions.checkNotNull(var3, "handler == null");
      this.mWindowAnimations = var4;
   }

   public FragmentHostCallback(Context var1, Handler var2, int var3) {
      Activity var4;
      if (var1 instanceof Activity) {
         var4 = (Activity)var1;
      } else {
         var4 = null;
      }

      this(var4, var1, var2, var3);
   }

   FragmentHostCallback(FragmentActivity var1) {
      this(var1, var1, new Handler(), 0);
   }

   Activity getActivity() {
      return this.mActivity;
   }

   Context getContext() {
      return this.mContext;
   }

   Handler getHandler() {
      return this.mHandler;
   }

   void onAttachFragment(Fragment var1) {
   }

   public void onDump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
   }

   public View onFindViewById(int var1) {
      return null;
   }

   public abstract Object onGetHost();

   public LayoutInflater onGetLayoutInflater() {
      return LayoutInflater.from(this.mContext);
   }

   public int onGetWindowAnimations() {
      return this.mWindowAnimations;
   }

   public boolean onHasView() {
      return true;
   }

   public boolean onHasWindowAnimations() {
      return true;
   }

   public void onRequestPermissionsFromFragment(Fragment var1, String[] var2, int var3) {
   }

   public boolean onShouldSaveFragmentState(Fragment var1) {
      return true;
   }

   public boolean onShouldShowRequestPermissionRationale(String var1) {
      return false;
   }

   public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3) {
      this.onStartActivityFromFragment(var1, var2, var3, (Bundle)null);
   }

   public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3, Bundle var4) {
      if (var3 == -1) {
         this.mContext.startActivity(var2);
      } else {
         throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
      }
   }

   public void onStartIntentSenderFromFragment(Fragment var1, IntentSender var2, int var3, Intent var4, int var5, int var6, int var7, Bundle var8) throws SendIntentException {
      if (var3 == -1) {
         ActivityCompat.startIntentSenderForResult(this.mActivity, var2, var3, var4, var5, var6, var7, var8);
      } else {
         throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
      }
   }

   public void onSupportInvalidateOptionsMenu() {
   }
}
