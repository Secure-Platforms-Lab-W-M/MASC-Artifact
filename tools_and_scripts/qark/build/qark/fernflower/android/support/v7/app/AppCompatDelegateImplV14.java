package android.support.v7.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v7.view.SupportActionModeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Window;
import android.view.Window.Callback;

@RequiresApi(14)
class AppCompatDelegateImplV14 extends AppCompatDelegateImplV11 {
   private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
   private boolean mApplyDayNightCalled;
   private AppCompatDelegateImplV14.AutoNightModeManager mAutoNightModeManager;
   private boolean mHandleNativeActionModes = true;
   private int mLocalNightMode = -100;

   AppCompatDelegateImplV14(Context var1, Window var2, AppCompatCallback var3) {
      super(var1, var2, var3);
   }

   private void ensureAutoNightModeManager() {
      if (this.mAutoNightModeManager == null) {
         this.mAutoNightModeManager = new AppCompatDelegateImplV14.AutoNightModeManager(TwilightManager.getInstance(this.mContext));
      }

   }

   private int getNightMode() {
      int var1 = this.mLocalNightMode;
      return var1 != -100 ? var1 : getDefaultNightMode();
   }

   private boolean shouldRecreateOnNightModeChange() {
      boolean var3 = this.mApplyDayNightCalled;
      boolean var2 = false;
      if (var3 && this.mContext instanceof Activity) {
         PackageManager var4 = this.mContext.getPackageManager();

         int var1;
         try {
            var1 = var4.getActivityInfo(new ComponentName(this.mContext, this.mContext.getClass()), 0).configChanges;
         } catch (NameNotFoundException var5) {
            Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", var5);
            return true;
         }

         if ((var1 & 512) == 0) {
            var2 = true;
         }

         return var2;
      } else {
         return false;
      }
   }

   private boolean updateForNightMode(int var1) {
      Resources var3 = this.mContext.getResources();
      Configuration var4 = var3.getConfiguration();
      int var2 = var4.uiMode;
      byte var6;
      if (var1 == 2) {
         var6 = 32;
      } else {
         var6 = 16;
      }

      if ((var2 & 48) != var6) {
         if (this.shouldRecreateOnNightModeChange()) {
            ((Activity)this.mContext).recreate();
         } else {
            var4 = new Configuration(var4);
            DisplayMetrics var5 = var3.getDisplayMetrics();
            var4.uiMode = var4.uiMode & -49 | var6;
            var3.updateConfiguration(var4, var5);
            if (VERSION.SDK_INT < 26) {
               ResourcesFlusher.flush(var3);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean applyDayNight() {
      boolean var3 = false;
      int var1 = this.getNightMode();
      int var2 = this.mapNightMode(var1);
      if (var2 != -1) {
         var3 = this.updateForNightMode(var2);
      }

      if (var1 == 0) {
         this.ensureAutoNightModeManager();
         this.mAutoNightModeManager.setup();
      }

      this.mApplyDayNightCalled = true;
      return var3;
   }

   @VisibleForTesting
   final AppCompatDelegateImplV14.AutoNightModeManager getAutoNightModeManager() {
      this.ensureAutoNightModeManager();
      return this.mAutoNightModeManager;
   }

   public boolean isHandleNativeActionModesEnabled() {
      return this.mHandleNativeActionModes;
   }

   int mapNightMode(int var1) {
      if (var1 != -100) {
         if (var1 != 0) {
            return var1;
         } else {
            this.ensureAutoNightModeManager();
            return this.mAutoNightModeManager.getApplyableNightMode();
         }
      } else {
         return -1;
      }
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (var1 != null && this.mLocalNightMode == -100) {
         this.mLocalNightMode = var1.getInt("appcompat:local_night_mode", -100);
      }

   }

   public void onDestroy() {
      super.onDestroy();
      AppCompatDelegateImplV14.AutoNightModeManager var1 = this.mAutoNightModeManager;
      if (var1 != null) {
         var1.cleanup();
      }

   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      int var2 = this.mLocalNightMode;
      if (var2 != -100) {
         var1.putInt("appcompat:local_night_mode", var2);
      }

   }

   public void onStart() {
      super.onStart();
      this.applyDayNight();
   }

   public void onStop() {
      super.onStop();
      AppCompatDelegateImplV14.AutoNightModeManager var1 = this.mAutoNightModeManager;
      if (var1 != null) {
         var1.cleanup();
      }

   }

   public void setHandleNativeActionModesEnabled(boolean var1) {
      this.mHandleNativeActionModes = var1;
   }

   public void setLocalNightMode(int var1) {
      if (var1 != -1 && var1 != 0 && var1 != 1 && var1 != 2) {
         Log.i("AppCompatDelegate", "setLocalNightMode() called with an unknown mode");
      } else {
         if (this.mLocalNightMode != var1) {
            this.mLocalNightMode = var1;
            if (this.mApplyDayNightCalled) {
               this.applyDayNight();
            }
         }

      }
   }

   Callback wrapWindowCallback(Callback var1) {
      return new AppCompatDelegateImplV14.AppCompatWindowCallbackV14(var1);
   }

   class AppCompatWindowCallbackV14 extends AppCompatDelegateImplBase.AppCompatWindowCallbackBase {
      AppCompatWindowCallbackV14(Callback var2) {
         super(var2);
      }

      public ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback var1) {
         return AppCompatDelegateImplV14.this.isHandleNativeActionModesEnabled() ? this.startAsSupportActionMode(var1) : super.onWindowStartingActionMode(var1);
      }

      final ActionMode startAsSupportActionMode(android.view.ActionMode.Callback var1) {
         SupportActionModeWrapper.CallbackWrapper var3 = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImplV14.this.mContext, var1);
         android.support.v7.view.ActionMode var2 = AppCompatDelegateImplV14.this.startSupportActionMode(var3);
         return var2 != null ? var3.getActionModeWrapper(var2) : null;
      }
   }

   @VisibleForTesting
   final class AutoNightModeManager {
      private BroadcastReceiver mAutoTimeChangeReceiver;
      private IntentFilter mAutoTimeChangeReceiverFilter;
      private boolean mIsNight;
      private TwilightManager mTwilightManager;

      AutoNightModeManager(@NonNull TwilightManager var2) {
         this.mTwilightManager = var2;
         this.mIsNight = var2.isNight();
      }

      final void cleanup() {
         if (this.mAutoTimeChangeReceiver != null) {
            AppCompatDelegateImplV14.this.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
            this.mAutoTimeChangeReceiver = null;
         }

      }

      final void dispatchTimeChanged() {
         boolean var1 = this.mTwilightManager.isNight();
         if (var1 != this.mIsNight) {
            this.mIsNight = var1;
            AppCompatDelegateImplV14.this.applyDayNight();
         }

      }

      final int getApplyableNightMode() {
         this.mIsNight = this.mTwilightManager.isNight();
         return this.mIsNight ? 2 : 1;
      }

      final void setup() {
         this.cleanup();
         if (this.mAutoTimeChangeReceiver == null) {
            this.mAutoTimeChangeReceiver = new BroadcastReceiver() {
               public void onReceive(Context var1, Intent var2) {
                  AutoNightModeManager.this.dispatchTimeChanged();
               }
            };
         }

         if (this.mAutoTimeChangeReceiverFilter == null) {
            this.mAutoTimeChangeReceiverFilter = new IntentFilter();
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_SET");
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
         }

         AppCompatDelegateImplV14.this.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
      }
   }
}
