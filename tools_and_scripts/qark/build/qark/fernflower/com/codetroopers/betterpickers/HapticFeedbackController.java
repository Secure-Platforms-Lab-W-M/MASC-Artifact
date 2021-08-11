package com.codetroopers.betterpickers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings.System;

public class HapticFeedbackController {
   private static final int VIBRATE_DELAY_MS = 125;
   private static final int VIBRATE_LENGTH_MS = 5;
   private final ContentObserver mContentObserver;
   private final Context mContext;
   private boolean mHasPermissions;
   private boolean mIsGloballyEnabled;
   private long mLastVibrate;
   private Vibrator mVibrator;

   public HapticFeedbackController(Context var1) {
      this.mContext = var1;
      this.mContentObserver = new ContentObserver((Handler)null) {
         public void onChange(boolean var1) {
            HapticFeedbackController var2 = HapticFeedbackController.this;
            var2.mIsGloballyEnabled = HapticFeedbackController.checkGlobalSetting(var2.mContext);
         }
      };
   }

   private static boolean checkAppPermissions(Context var0) {
      return var0.checkCallingOrSelfPermission("android.permission.VIBRATE") == 0;
   }

   private static boolean checkGlobalSetting(Context var0) {
      ContentResolver var2 = var0.getContentResolver();
      boolean var1 = false;
      if (System.getInt(var2, "haptic_feedback_enabled", 0) == 1) {
         var1 = true;
      }

      return var1;
   }

   public void start() {
      this.mVibrator = (Vibrator)this.mContext.getSystemService("vibrator");
      this.mIsGloballyEnabled = checkGlobalSetting(this.mContext);
      this.mHasPermissions = checkAppPermissions(this.mContext);
      Uri var1 = System.getUriFor("haptic_feedback_enabled");
      this.mContext.getContentResolver().registerContentObserver(var1, false, this.mContentObserver);
   }

   public void stop() {
      this.mVibrator = null;
      this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
   }

   public void tryVibrate() {
      if (this.mVibrator != null && this.mIsGloballyEnabled && this.mHasPermissions) {
         long var1 = SystemClock.uptimeMillis();
         if (var1 - this.mLastVibrate >= 125L) {
            this.mVibrator.vibrate(5L);
            this.mLastVibrate = var1;
         }
      }

   }
}
