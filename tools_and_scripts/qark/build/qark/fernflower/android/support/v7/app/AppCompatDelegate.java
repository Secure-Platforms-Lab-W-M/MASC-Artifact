package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class AppCompatDelegate {
   public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
   public static final int FEATURE_SUPPORT_ACTION_BAR = 108;
   public static final int FEATURE_SUPPORT_ACTION_BAR_OVERLAY = 109;
   public static final int MODE_NIGHT_AUTO = 0;
   public static final int MODE_NIGHT_FOLLOW_SYSTEM = -1;
   public static final int MODE_NIGHT_NO = 1;
   static final int MODE_NIGHT_UNSPECIFIED = -100;
   public static final int MODE_NIGHT_YES = 2;
   static final String TAG = "AppCompatDelegate";
   private static boolean sCompatVectorFromResourcesEnabled = false;
   private static int sDefaultNightMode = -1;

   AppCompatDelegate() {
   }

   public static AppCompatDelegate create(Activity var0, AppCompatCallback var1) {
      return create(var0, var0.getWindow(), var1);
   }

   public static AppCompatDelegate create(Dialog var0, AppCompatCallback var1) {
      return create(var0.getContext(), var0.getWindow(), var1);
   }

   private static AppCompatDelegate create(Context var0, Window var1, AppCompatCallback var2) {
      if (VERSION.SDK_INT >= 24) {
         return new AppCompatDelegateImplN(var0, var1, var2);
      } else if (VERSION.SDK_INT >= 23) {
         return new AppCompatDelegateImplV23(var0, var1, var2);
      } else if (VERSION.SDK_INT >= 14) {
         return new AppCompatDelegateImplV14(var0, var1, var2);
      } else {
         return (AppCompatDelegate)(VERSION.SDK_INT >= 11 ? new AppCompatDelegateImplV11(var0, var1, var2) : new AppCompatDelegateImplV9(var0, var1, var2));
      }
   }

   public static int getDefaultNightMode() {
      return sDefaultNightMode;
   }

   public static boolean isCompatVectorFromResourcesEnabled() {
      return sCompatVectorFromResourcesEnabled;
   }

   public static void setCompatVectorFromResourcesEnabled(boolean var0) {
      sCompatVectorFromResourcesEnabled = var0;
   }

   public static void setDefaultNightMode(int var0) {
      if (var0 != -1 && var0 != 0 && var0 != 1 && var0 != 2) {
         Log.d("AppCompatDelegate", "setDefaultNightMode() called with an unknown mode");
      } else {
         sDefaultNightMode = var0;
      }
   }

   public abstract void addContentView(View var1, LayoutParams var2);

   public abstract boolean applyDayNight();

   public abstract View createView(@Nullable View var1, String var2, @NonNull Context var3, @NonNull AttributeSet var4);

   @Nullable
   public abstract View findViewById(@IdRes int var1);

   @Nullable
   public abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();

   public abstract MenuInflater getMenuInflater();

   @Nullable
   public abstract ActionBar getSupportActionBar();

   public abstract boolean hasWindowFeature(int var1);

   public abstract void installViewFactory();

   public abstract void invalidateOptionsMenu();

   public abstract boolean isHandleNativeActionModesEnabled();

   public abstract void onConfigurationChanged(Configuration var1);

   public abstract void onCreate(Bundle var1);

   public abstract void onDestroy();

   public abstract void onPostCreate(Bundle var1);

   public abstract void onPostResume();

   public abstract void onSaveInstanceState(Bundle var1);

   public abstract void onStart();

   public abstract void onStop();

   public abstract boolean requestWindowFeature(int var1);

   public abstract void setContentView(@LayoutRes int var1);

   public abstract void setContentView(View var1);

   public abstract void setContentView(View var1, LayoutParams var2);

   public abstract void setHandleNativeActionModesEnabled(boolean var1);

   public abstract void setLocalNightMode(int var1);

   public abstract void setSupportActionBar(@Nullable Toolbar var1);

   public abstract void setTitle(@Nullable CharSequence var1);

   @Nullable
   public abstract ActionMode startSupportActionMode(@NonNull ActionMode.Callback var1);

   @Retention(RetentionPolicy.SOURCE)
   @interface ApplyableNightMode {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface NightMode {
   }
}
