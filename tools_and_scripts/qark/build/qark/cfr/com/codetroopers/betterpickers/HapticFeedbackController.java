/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.ContentObserver
 *  android.net.Uri
 *  android.os.Handler
 *  android.os.SystemClock
 *  android.os.Vibrator
 *  android.provider.Settings
 *  android.provider.Settings$System
 */
package com.codetroopers.betterpickers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;

public class HapticFeedbackController {
    private static final int VIBRATE_DELAY_MS = 125;
    private static final int VIBRATE_LENGTH_MS = 5;
    private final ContentObserver mContentObserver;
    private final Context mContext;
    private boolean mHasPermissions;
    private boolean mIsGloballyEnabled;
    private long mLastVibrate;
    private Vibrator mVibrator;

    public HapticFeedbackController(Context context) {
        this.mContext = context;
        this.mContentObserver = new ContentObserver(null){

            public void onChange(boolean bl) {
                HapticFeedbackController hapticFeedbackController = HapticFeedbackController.this;
                hapticFeedbackController.mIsGloballyEnabled = HapticFeedbackController.checkGlobalSetting(hapticFeedbackController.mContext);
            }
        };
    }

    private static boolean checkAppPermissions(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.VIBRATE") == 0) {
            return true;
        }
        return false;
    }

    private static boolean checkGlobalSetting(Context context) {
        context = context.getContentResolver();
        boolean bl = false;
        if (Settings.System.getInt((ContentResolver)context, (String)"haptic_feedback_enabled", (int)0) == 1) {
            bl = true;
        }
        return bl;
    }

    public void start() {
        this.mVibrator = (Vibrator)this.mContext.getSystemService("vibrator");
        this.mIsGloballyEnabled = HapticFeedbackController.checkGlobalSetting(this.mContext);
        this.mHasPermissions = HapticFeedbackController.checkAppPermissions(this.mContext);
        Uri uri = Settings.System.getUriFor((String)"haptic_feedback_enabled");
        this.mContext.getContentResolver().registerContentObserver(uri, false, this.mContentObserver);
    }

    public void stop() {
        this.mVibrator = null;
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    public void tryVibrate() {
        long l;
        if (this.mVibrator != null && this.mIsGloballyEnabled && this.mHasPermissions && (l = SystemClock.uptimeMillis()) - this.mLastVibrate >= 125L) {
            this.mVibrator.vibrate(5L);
            this.mLastVibrate = l;
        }
    }

}

