/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.BroadcastReceiver
 *  android.content.BroadcastReceiver$PendingResult
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.ActivityInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.os.RemoteException
 *  android.util.Log
 *  android.view.KeyEvent
 */
package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import java.util.List;

public class MediaButtonReceiver
extends BroadcastReceiver {
    private static final String TAG = "MediaButtonReceiver";

    public static PendingIntent buildMediaButtonPendingIntent(Context context, long l) {
        ComponentName componentName = MediaButtonReceiver.getMediaButtonReceiverComponent(context);
        if (componentName == null) {
            Log.w((String)"MediaButtonReceiver", (String)"A unique media button receiver could not be found in the given context, so couldn't build a pending intent.");
            return null;
        }
        return MediaButtonReceiver.buildMediaButtonPendingIntent(context, componentName, l);
    }

    public static PendingIntent buildMediaButtonPendingIntent(Context object, ComponentName componentName, long l) {
        if (componentName == null) {
            Log.w((String)"MediaButtonReceiver", (String)"The component name of media button receiver should be provided.");
            return null;
        }
        int n = PlaybackStateCompat.toKeyCode(l);
        if (n == 0) {
            object = new StringBuilder();
            object.append("Cannot build a media button pending intent with the given action: ");
            object.append(l);
            Log.w((String)"MediaButtonReceiver", (String)object.toString());
            return null;
        }
        Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        intent.setComponent(componentName);
        intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)new KeyEvent(0, n));
        return PendingIntent.getBroadcast((Context)object, (int)n, (Intent)intent, (int)0);
    }

    static ComponentName getMediaButtonReceiverComponent(Context object) {
        Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        intent.setPackage(object.getPackageName());
        object = object.getPackageManager().queryBroadcastReceivers(intent, 0);
        if (object.size() == 1) {
            object = (ResolveInfo)object.get(0);
            return new ComponentName(object.activityInfo.packageName, object.activityInfo.name);
        }
        if (object.size() > 1) {
            Log.w((String)"MediaButtonReceiver", (String)"More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, returning null.");
        }
        return null;
    }

    private static ComponentName getServiceComponentByAction(Context object, String string2) {
        Object object2 = object.getPackageManager();
        Intent intent = new Intent(string2);
        intent.setPackage(object.getPackageName());
        object = object2.queryIntentServices(intent, 0);
        if (object.size() == 1) {
            object = (ResolveInfo)object.get(0);
            return new ComponentName(object.serviceInfo.packageName, object.serviceInfo.name);
        }
        if (object.isEmpty()) {
            return null;
        }
        object2 = new StringBuilder();
        object2.append("Expected 1 service that handles ");
        object2.append(string2);
        object2.append(", found ");
        object2.append(object.size());
        throw new IllegalStateException(object2.toString());
    }

    public static KeyEvent handleIntent(MediaSessionCompat mediaSessionCompat, Intent intent) {
        if (mediaSessionCompat != null && intent != null && "android.intent.action.MEDIA_BUTTON".equals(intent.getAction()) && intent.hasExtra("android.intent.extra.KEY_EVENT")) {
            intent = (KeyEvent)intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
            mediaSessionCompat.getController().dispatchMediaButtonEvent((KeyEvent)intent);
            return intent;
        }
        return null;
    }

    private static void startForegroundService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
            return;
        }
        context.startService(intent);
    }

    public void onReceive(Context object, Intent object2) {
        if (object2 != null && "android.intent.action.MEDIA_BUTTON".equals(object2.getAction()) && object2.hasExtra("android.intent.extra.KEY_EVENT")) {
            ComponentName componentName = MediaButtonReceiver.getServiceComponentByAction((Context)object, "android.intent.action.MEDIA_BUTTON");
            if (componentName != null) {
                object2.setComponent(componentName);
                MediaButtonReceiver.startForegroundService((Context)object, (Intent)object2);
                return;
            }
            componentName = MediaButtonReceiver.getServiceComponentByAction((Context)object, "android.media.browse.MediaBrowserService");
            if (componentName != null) {
                BroadcastReceiver.PendingResult pendingResult = this.goAsync();
                object = object.getApplicationContext();
                object2 = new MediaButtonConnectionCallback((Context)object, (Intent)object2, pendingResult);
                object = new MediaBrowserCompat((Context)object, componentName, (MediaBrowserCompat.ConnectionCallback)object2, null);
                object2.setMediaBrowser((MediaBrowserCompat)object);
                object.connect();
                return;
            }
            throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or implements a media browser service.");
        }
        object = new StringBuilder();
        object.append("Ignore unsupported intent: ");
        object.append(object2);
        Log.d((String)"MediaButtonReceiver", (String)object.toString());
    }

    private static class MediaButtonConnectionCallback
    extends MediaBrowserCompat.ConnectionCallback {
        private final Context mContext;
        private final Intent mIntent;
        private MediaBrowserCompat mMediaBrowser;
        private final BroadcastReceiver.PendingResult mPendingResult;

        MediaButtonConnectionCallback(Context context, Intent intent, BroadcastReceiver.PendingResult pendingResult) {
            this.mContext = context;
            this.mIntent = intent;
            this.mPendingResult = pendingResult;
        }

        private void finish() {
            this.mMediaBrowser.disconnect();
            this.mPendingResult.finish();
        }

        @Override
        public void onConnected() {
            try {
                new MediaControllerCompat(this.mContext, this.mMediaBrowser.getSessionToken()).dispatchMediaButtonEvent((KeyEvent)this.mIntent.getParcelableExtra("android.intent.extra.KEY_EVENT"));
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaButtonReceiver", (String)"Failed to create a media controller", (Throwable)remoteException);
            }
            this.finish();
        }

        @Override
        public void onConnectionFailed() {
            this.finish();
        }

        @Override
        public void onConnectionSuspended() {
            this.finish();
        }

        void setMediaBrowser(MediaBrowserCompat mediaBrowserCompat) {
            this.mMediaBrowser = mediaBrowserCompat;
        }
    }

}

