/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.media.AudioManager
 */
package android.support.v4.media.session;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;

class MediaSessionCompatApi8 {
    MediaSessionCompatApi8() {
    }

    public static void registerMediaButtonEventReceiver(Context context, ComponentName componentName) {
        ((AudioManager)context.getSystemService("audio")).registerMediaButtonEventReceiver(componentName);
    }

    public static void unregisterMediaButtonEventReceiver(Context context, ComponentName componentName) {
        ((AudioManager)context.getSystemService("audio")).unregisterMediaButtonEventReceiver(componentName);
    }
}

