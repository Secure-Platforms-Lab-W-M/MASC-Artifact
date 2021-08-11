/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.os.Bundle
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@TargetApi(value=16)
@RequiresApi(value=16)
class ActivityCompatJB {
    ActivityCompatJB() {
    }

    public static void finishAffinity(Activity activity) {
        activity.finishAffinity();
    }

    public static void startActivityForResult(Activity activity, Intent intent, int n, Bundle bundle) {
        activity.startActivityForResult(intent, n, bundle);
    }

    public static void startIntentSenderForResult(Activity activity, IntentSender intentSender, int n, Intent intent, int n2, int n3, int n4, Bundle bundle) throws IntentSender.SendIntentException {
        activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
    }
}

