package androidx.activity;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.lang.reflect.Field;

final class ImmLeaksCleaner implements LifecycleEventObserver {
   private static final int INIT_FAILED = 2;
   private static final int INIT_SUCCESS = 1;
   private static final int NOT_INITIALIAZED = 0;
   private static Field sHField;
   private static Field sNextServedViewField;
   private static int sReflectedFieldsInitialized = 0;
   private static Field sServedViewField;
   private Activity mActivity;

   ImmLeaksCleaner(Activity var1) {
      this.mActivity = var1;
   }

   private static void initializeReflectiveFields() {
      try {
         sReflectedFieldsInitialized = 2;
         Field var0 = InputMethodManager.class.getDeclaredField("mServedView");
         sServedViewField = var0;
         var0.setAccessible(true);
         var0 = InputMethodManager.class.getDeclaredField("mNextServedView");
         sNextServedViewField = var0;
         var0.setAccessible(true);
         var0 = InputMethodManager.class.getDeclaredField("mH");
         sHField = var0;
         var0.setAccessible(true);
         sReflectedFieldsInitialized = 1;
      } catch (NoSuchFieldException var1) {
      }
   }

   public void onStateChanged(LifecycleOwner param1, Lifecycle.Event param2) {
      // $FF: Couldn't be decompiled
   }
}
