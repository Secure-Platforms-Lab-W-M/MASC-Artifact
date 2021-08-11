// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media.session;

import android.os.RemoteException;
import android.content.BroadcastReceiver$PendingResult;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.os.Build$VERSION;
import android.content.pm.PackageManager;
import java.util.List;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.content.Intent;
import android.content.ComponentName;
import android.util.Log;
import android.app.PendingIntent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class MediaButtonReceiver extends BroadcastReceiver
{
    private static final String TAG = "MediaButtonReceiver";
    
    public static PendingIntent buildMediaButtonPendingIntent(final Context context, final long n) {
        final ComponentName mediaButtonReceiverComponent = getMediaButtonReceiverComponent(context);
        if (mediaButtonReceiverComponent == null) {
            Log.w("MediaButtonReceiver", "A unique media button receiver could not be found in the given context, so couldn't build a pending intent.");
            return null;
        }
        return buildMediaButtonPendingIntent(context, mediaButtonReceiverComponent, n);
    }
    
    public static PendingIntent buildMediaButtonPendingIntent(final Context context, final ComponentName component, final long n) {
        if (component == null) {
            Log.w("MediaButtonReceiver", "The component name of media button receiver should be provided.");
            return null;
        }
        final int keyCode = PlaybackStateCompat.toKeyCode(n);
        if (keyCode == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot build a media button pending intent with the given action: ");
            sb.append(n);
            Log.w("MediaButtonReceiver", sb.toString());
            return null;
        }
        final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        intent.setComponent(component);
        intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)new KeyEvent(0, keyCode));
        return PendingIntent.getBroadcast(context, keyCode, intent, 0);
    }
    
    static ComponentName getMediaButtonReceiverComponent(final Context context) {
        final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        intent.setPackage(context.getPackageName());
        final List queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 0);
        if (queryBroadcastReceivers.size() == 1) {
            final ResolveInfo resolveInfo = queryBroadcastReceivers.get(0);
            return new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }
        if (queryBroadcastReceivers.size() > 1) {
            Log.w("MediaButtonReceiver", "More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, returning null.");
        }
        return null;
    }
    
    private static ComponentName getServiceComponentByAction(final Context context, final String s) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(s);
        intent.setPackage(context.getPackageName());
        final List queryIntentServices = packageManager.queryIntentServices(intent, 0);
        if (queryIntentServices.size() == 1) {
            final ResolveInfo resolveInfo = queryIntentServices.get(0);
            return new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
        }
        if (queryIntentServices.isEmpty()) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected 1 service that handles ");
        sb.append(s);
        sb.append(", found ");
        sb.append(queryIntentServices.size());
        throw new IllegalStateException(sb.toString());
    }
    
    public static KeyEvent handleIntent(final MediaSessionCompat mediaSessionCompat, final Intent intent) {
        if (mediaSessionCompat != null && intent != null) {
            if ("android.intent.action.MEDIA_BUTTON".equals(intent.getAction())) {
                if (intent.hasExtra("android.intent.extra.KEY_EVENT")) {
                    final KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
                    mediaSessionCompat.getController().dispatchMediaButtonEvent(keyEvent);
                    return keyEvent;
                }
            }
        }
        return null;
    }
    
    private static void startForegroundService(final Context context, final Intent intent) {
        if (Build$VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
            return;
        }
        context.startService(intent);
    }
    
    public void onReceive(Context applicationContext, final Intent intent) {
        if (intent != null) {
            if ("android.intent.action.MEDIA_BUTTON".equals(intent.getAction())) {
                if (intent.hasExtra("android.intent.extra.KEY_EVENT")) {
                    final ComponentName serviceComponentByAction = getServiceComponentByAction(applicationContext, "android.intent.action.MEDIA_BUTTON");
                    if (serviceComponentByAction != null) {
                        intent.setComponent(serviceComponentByAction);
                        startForegroundService(applicationContext, intent);
                        return;
                    }
                    final ComponentName serviceComponentByAction2 = getServiceComponentByAction(applicationContext, "android.media.browse.MediaBrowserService");
                    if (serviceComponentByAction2 != null) {
                        final BroadcastReceiver$PendingResult goAsync = this.goAsync();
                        applicationContext = applicationContext.getApplicationContext();
                        final MediaButtonConnectionCallback mediaButtonConnectionCallback = new MediaButtonConnectionCallback(applicationContext, intent, goAsync);
                        final MediaBrowserCompat mediaBrowser = new MediaBrowserCompat(applicationContext, serviceComponentByAction2, (MediaBrowserCompat.ConnectionCallback)mediaButtonConnectionCallback, null);
                        mediaButtonConnectionCallback.setMediaBrowser(mediaBrowser);
                        mediaBrowser.connect();
                        return;
                    }
                    throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or implements a media browser service.");
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Ignore unsupported intent: ");
        sb.append(intent);
        Log.d("MediaButtonReceiver", sb.toString());
    }
    
    private static class MediaButtonConnectionCallback extends ConnectionCallback
    {
        private final Context mContext;
        private final Intent mIntent;
        private MediaBrowserCompat mMediaBrowser;
        private final BroadcastReceiver$PendingResult mPendingResult;
        
        MediaButtonConnectionCallback(final Context mContext, final Intent mIntent, final BroadcastReceiver$PendingResult mPendingResult) {
            this.mContext = mContext;
            this.mIntent = mIntent;
            this.mPendingResult = mPendingResult;
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
            catch (RemoteException ex) {
                Log.e("MediaButtonReceiver", "Failed to create a media controller", (Throwable)ex);
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
        
        void setMediaBrowser(final MediaBrowserCompat mMediaBrowser) {
            this.mMediaBrowser = mMediaBrowser;
        }
    }
}
