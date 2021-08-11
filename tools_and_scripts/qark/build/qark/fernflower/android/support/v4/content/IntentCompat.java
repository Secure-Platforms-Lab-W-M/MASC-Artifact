package android.support.v4.content;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;

public final class IntentCompat {
   @Deprecated
   public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
   @Deprecated
   public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
   public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
   @Deprecated
   public static final String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
   @Deprecated
   public static final String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";
   public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
   public static final String EXTRA_START_PLAYBACK = "android.intent.extra.START_PLAYBACK";
   @Deprecated
   public static final int FLAG_ACTIVITY_CLEAR_TASK = 32768;
   @Deprecated
   public static final int FLAG_ACTIVITY_TASK_ON_HOME = 16384;
   private static final IntentCompat.IntentCompatBaseImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 15) {
         IMPL = new IntentCompat.IntentCompatApi15Impl();
      } else {
         IMPL = new IntentCompat.IntentCompatBaseImpl();
      }
   }

   private IntentCompat() {
   }

   @Deprecated
   public static Intent makeMainActivity(ComponentName var0) {
      return Intent.makeMainActivity(var0);
   }

   public static Intent makeMainSelectorActivity(String var0, String var1) {
      return IMPL.makeMainSelectorActivity(var0, var1);
   }

   @Deprecated
   public static Intent makeRestartActivityTask(ComponentName var0) {
      return Intent.makeRestartActivityTask(var0);
   }

   @RequiresApi(15)
   static class IntentCompatApi15Impl extends IntentCompat.IntentCompatBaseImpl {
      public Intent makeMainSelectorActivity(String var1, String var2) {
         return Intent.makeMainSelectorActivity(var1, var2);
      }
   }

   static class IntentCompatBaseImpl {
      public Intent makeMainSelectorActivity(String var1, String var2) {
         Intent var3 = new Intent(var1);
         var3.addCategory(var2);
         return var3;
      }
   }
}
