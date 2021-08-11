package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class NotificationCompatJellybean {
   static final String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";
   static final String EXTRA_DATA_ONLY_REMOTE_INPUTS = "android.support.dataRemoteInputs";
   private static final String KEY_ACTION_INTENT = "actionIntent";
   private static final String KEY_ALLOWED_DATA_TYPES = "allowedDataTypes";
   private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
   private static final String KEY_CHOICES = "choices";
   private static final String KEY_DATA_ONLY_REMOTE_INPUTS = "dataOnlyRemoteInputs";
   private static final String KEY_EXTRAS = "extras";
   private static final String KEY_ICON = "icon";
   private static final String KEY_LABEL = "label";
   private static final String KEY_REMOTE_INPUTS = "remoteInputs";
   private static final String KEY_RESULT_KEY = "resultKey";
   private static final String KEY_SEMANTIC_ACTION = "semanticAction";
   private static final String KEY_SHOWS_USER_INTERFACE = "showsUserInterface";
   private static final String KEY_TITLE = "title";
   public static final String TAG = "NotificationCompat";
   private static Field sActionIconField;
   private static Field sActionIntentField;
   private static Field sActionTitleField;
   private static boolean sActionsAccessFailed;
   private static Field sActionsField;
   private static final Object sActionsLock = new Object();
   private static Field sExtrasField;
   private static boolean sExtrasFieldAccessFailed;
   private static final Object sExtrasLock = new Object();

   private NotificationCompatJellybean() {
   }

   public static SparseArray buildActionExtrasMap(List var0) {
      SparseArray var3 = null;
      int var1 = 0;

      SparseArray var4;
      for(int var2 = var0.size(); var1 < var2; var3 = var4) {
         Bundle var5 = (Bundle)var0.get(var1);
         var4 = var3;
         if (var5 != null) {
            var4 = var3;
            if (var3 == null) {
               var4 = new SparseArray();
            }

            var4.put(var1, var5);
         }

         ++var1;
      }

      return var3;
   }

   private static boolean ensureActionReflectionReadyLocked() {
      if (sActionsAccessFailed) {
         return false;
      } else {
         try {
            if (sActionsField == null) {
               Class var0 = Class.forName("android.app.Notification$Action");
               sActionIconField = var0.getDeclaredField("icon");
               sActionTitleField = var0.getDeclaredField("title");
               sActionIntentField = var0.getDeclaredField("actionIntent");
               Field var3 = Notification.class.getDeclaredField("actions");
               sActionsField = var3;
               var3.setAccessible(true);
            }
         } catch (ClassNotFoundException var1) {
            Log.e("NotificationCompat", "Unable to access notification actions", var1);
            sActionsAccessFailed = true;
         } catch (NoSuchFieldException var2) {
            Log.e("NotificationCompat", "Unable to access notification actions", var2);
            sActionsAccessFailed = true;
         }

         return sActionsAccessFailed ^ true;
      }
   }

   private static RemoteInput fromBundle(Bundle var0) {
      ArrayList var2 = var0.getStringArrayList("allowedDataTypes");
      HashSet var1 = new HashSet();
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            var1.add((String)var3.next());
         }
      }

      return new RemoteInput(var0.getString("resultKey"), var0.getCharSequence("label"), var0.getCharSequenceArray("choices"), var0.getBoolean("allowFreeFormInput"), var0.getBundle("extras"), var1);
   }

   private static RemoteInput[] fromBundleArray(Bundle[] var0) {
      if (var0 == null) {
         return null;
      } else {
         RemoteInput[] var2 = new RemoteInput[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = fromBundle(var0[var1]);
         }

         return var2;
      }
   }

   public static NotificationCompat.Action getAction(Notification param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int getActionCount(Notification var0) {
      Object var2 = sActionsLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label191: {
         Object[] var23;
         try {
            var23 = getActionObjectsLocked(var0);
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label191;
         }

         int var1;
         if (var23 != null) {
            try {
               var1 = var23.length;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label191;
            }
         } else {
            var1 = 0;
         }

         label179:
         try {
            return var1;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label179;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   static NotificationCompat.Action getActionFromBundle(Bundle var0) {
      Bundle var2 = var0.getBundle("extras");
      boolean var1 = false;
      if (var2 != null) {
         var1 = var2.getBoolean("android.support.allowGeneratedReplies", false);
      }

      return new NotificationCompat.Action(var0.getInt("icon"), var0.getCharSequence("title"), (PendingIntent)var0.getParcelable("actionIntent"), var0.getBundle("extras"), fromBundleArray(getBundleArrayFromBundle(var0, "remoteInputs")), fromBundleArray(getBundleArrayFromBundle(var0, "dataOnlyRemoteInputs")), var1, var0.getInt("semanticAction"), var0.getBoolean("showsUserInterface"));
   }

   private static Object[] getActionObjectsLocked(Notification param0) {
      // $FF: Couldn't be decompiled
   }

   private static Bundle[] getBundleArrayFromBundle(Bundle var0, String var1) {
      Parcelable[] var2 = var0.getParcelableArray(var1);
      if (!(var2 instanceof Bundle[]) && var2 != null) {
         Bundle[] var3 = (Bundle[])Arrays.copyOf(var2, var2.length, Bundle[].class);
         var0.putParcelableArray(var1, var3);
         return var3;
      } else {
         return (Bundle[])((Bundle[])var2);
      }
   }

   static Bundle getBundleForAction(NotificationCompat.Action var0) {
      Bundle var2 = new Bundle();
      var2.putInt("icon", var0.getIcon());
      var2.putCharSequence("title", var0.getTitle());
      var2.putParcelable("actionIntent", var0.getActionIntent());
      Bundle var1;
      if (var0.getExtras() != null) {
         var1 = new Bundle(var0.getExtras());
      } else {
         var1 = new Bundle();
      }

      var1.putBoolean("android.support.allowGeneratedReplies", var0.getAllowGeneratedReplies());
      var2.putBundle("extras", var1);
      var2.putParcelableArray("remoteInputs", toBundleArray(var0.getRemoteInputs()));
      var2.putBoolean("showsUserInterface", var0.getShowsUserInterface());
      var2.putInt("semanticAction", var0.getSemanticAction());
      return var2;
   }

   public static Bundle getExtras(Notification param0) {
      // $FF: Couldn't be decompiled
   }

   public static NotificationCompat.Action readAction(int var0, CharSequence var1, PendingIntent var2, Bundle var3) {
      boolean var4;
      RemoteInput[] var5;
      RemoteInput[] var6;
      if (var3 != null) {
         var5 = fromBundleArray(getBundleArrayFromBundle(var3, "android.support.remoteInputs"));
         var6 = fromBundleArray(getBundleArrayFromBundle(var3, "android.support.dataRemoteInputs"));
         var4 = var3.getBoolean("android.support.allowGeneratedReplies");
      } else {
         var5 = null;
         var6 = null;
         var4 = false;
      }

      return new NotificationCompat.Action(var0, var1, var2, var3, var5, var6, var4, 0, true);
   }

   private static Bundle toBundle(RemoteInput var0) {
      Bundle var1 = new Bundle();
      var1.putString("resultKey", var0.getResultKey());
      var1.putCharSequence("label", var0.getLabel());
      var1.putCharSequenceArray("choices", var0.getChoices());
      var1.putBoolean("allowFreeFormInput", var0.getAllowFreeFormInput());
      var1.putBundle("extras", var0.getExtras());
      Set var2 = var0.getAllowedDataTypes();
      if (var2 != null && !var2.isEmpty()) {
         ArrayList var3 = new ArrayList(var2.size());
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.add((String)var4.next());
         }

         var1.putStringArrayList("allowedDataTypes", var3);
      }

      return var1;
   }

   private static Bundle[] toBundleArray(RemoteInput[] var0) {
      if (var0 == null) {
         return null;
      } else {
         Bundle[] var2 = new Bundle[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = toBundle(var0[var1]);
         }

         return var2;
      }
   }

   public static Bundle writeActionAndGetExtras(Builder var0, NotificationCompat.Action var1) {
      var0.addAction(var1.getIcon(), var1.getTitle(), var1.getActionIntent());
      Bundle var2 = new Bundle(var1.getExtras());
      if (var1.getRemoteInputs() != null) {
         var2.putParcelableArray("android.support.remoteInputs", toBundleArray(var1.getRemoteInputs()));
      }

      if (var1.getDataOnlyRemoteInputs() != null) {
         var2.putParcelableArray("android.support.dataRemoteInputs", toBundleArray(var1.getDataOnlyRemoteInputs()));
      }

      var2.putBoolean("android.support.allowGeneratedReplies", var1.getAllowGeneratedReplies());
      return var2;
   }
}
