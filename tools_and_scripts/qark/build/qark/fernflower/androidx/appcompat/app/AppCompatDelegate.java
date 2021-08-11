package androidx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.collection.ArraySet;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Iterator;

public abstract class AppCompatDelegate {
   public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
   public static final int FEATURE_SUPPORT_ACTION_BAR = 108;
   public static final int FEATURE_SUPPORT_ACTION_BAR_OVERLAY = 109;
   @Deprecated
   public static final int MODE_NIGHT_AUTO = 0;
   public static final int MODE_NIGHT_AUTO_BATTERY = 3;
   @Deprecated
   public static final int MODE_NIGHT_AUTO_TIME = 0;
   public static final int MODE_NIGHT_FOLLOW_SYSTEM = -1;
   public static final int MODE_NIGHT_NO = 1;
   public static final int MODE_NIGHT_UNSPECIFIED = -100;
   public static final int MODE_NIGHT_YES = 2;
   static final String TAG = "AppCompatDelegate";
   private static final ArraySet sActiveDelegates = new ArraySet();
   private static final Object sActiveDelegatesLock = new Object();
   private static int sDefaultNightMode = -100;

   AppCompatDelegate() {
   }

   private static void applyDayNightToActiveDelegates() {
      Object var0 = sActiveDelegatesLock;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label278: {
         Iterator var1;
         try {
            var1 = sActiveDelegates.iterator();
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label278;
         }

         while(true) {
            AppCompatDelegate var2;
            try {
               if (!var1.hasNext()) {
                  break;
               }

               var2 = (AppCompatDelegate)((WeakReference)var1.next()).get();
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label278;
            }

            if (var2 != null) {
               try {
                  var2.applyDayNight();
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label278;
               }
            }
         }

         label260:
         try {
            return;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label260;
         }
      }

      while(true) {
         Throwable var33 = var10000;

         try {
            throw var33;
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            continue;
         }
      }
   }

   public static AppCompatDelegate create(Activity var0, AppCompatCallback var1) {
      return new AppCompatDelegateImpl(var0, var1);
   }

   public static AppCompatDelegate create(Dialog var0, AppCompatCallback var1) {
      return new AppCompatDelegateImpl(var0, var1);
   }

   public static AppCompatDelegate create(Context var0, Activity var1, AppCompatCallback var2) {
      return new AppCompatDelegateImpl(var0, var1, var2);
   }

   public static AppCompatDelegate create(Context var0, Window var1, AppCompatCallback var2) {
      return new AppCompatDelegateImpl(var0, var1, var2);
   }

   public static int getDefaultNightMode() {
      return sDefaultNightMode;
   }

   public static boolean isCompatVectorFromResourcesEnabled() {
      return VectorEnabledTintResources.isCompatVectorFromResourcesEnabled();
   }

   static void markStarted(AppCompatDelegate param0) {
      // $FF: Couldn't be decompiled
   }

   static void markStopped(AppCompatDelegate param0) {
      // $FF: Couldn't be decompiled
   }

   private static void removeDelegateFromActives(AppCompatDelegate var0) {
      Object var1 = sActiveDelegatesLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label298: {
         Iterator var2;
         try {
            var2 = sActiveDelegates.iterator();
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label298;
         }

         while(true) {
            AppCompatDelegate var3;
            label295: {
               try {
                  if (var2.hasNext()) {
                     var3 = (AppCompatDelegate)((WeakReference)var2.next()).get();
                     break label295;
                  }
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break;
               }

               try {
                  return;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break;
               }
            }

            if (var3 == var0 || var3 == null) {
               try {
                  var2.remove();
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break;
               }
            }
         }
      }

      while(true) {
         Throwable var34 = var10000;

         try {
            throw var34;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            continue;
         }
      }
   }

   public static void setCompatVectorFromResourcesEnabled(boolean var0) {
      VectorEnabledTintResources.setCompatVectorFromResourcesEnabled(var0);
   }

   public static void setDefaultNightMode(int var0) {
      if (var0 != -1 && var0 != 0 && var0 != 1 && var0 != 2 && var0 != 3) {
         Log.d("AppCompatDelegate", "setDefaultNightMode() called with an unknown mode");
      } else {
         if (sDefaultNightMode != var0) {
            sDefaultNightMode = var0;
            applyDayNightToActiveDelegates();
         }

      }
   }

   public abstract void addContentView(View var1, LayoutParams var2);

   public abstract boolean applyDayNight();

   public void attachBaseContext(Context var1) {
   }

   public abstract View createView(View var1, String var2, Context var3, AttributeSet var4);

   public abstract View findViewById(int var1);

   public abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();

   public int getLocalNightMode() {
      return -100;
   }

   public abstract MenuInflater getMenuInflater();

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

   public abstract void setContentView(int var1);

   public abstract void setContentView(View var1);

   public abstract void setContentView(View var1, LayoutParams var2);

   public abstract void setHandleNativeActionModesEnabled(boolean var1);

   public abstract void setLocalNightMode(int var1);

   public abstract void setSupportActionBar(Toolbar var1);

   public void setTheme(int var1) {
   }

   public abstract void setTitle(CharSequence var1);

   public abstract ActionMode startSupportActionMode(ActionMode.Callback var1);

   @Retention(RetentionPolicy.SOURCE)
   public @interface NightMode {
   }
}
