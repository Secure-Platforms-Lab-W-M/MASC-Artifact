// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import java.util.Iterator;
import java.util.ArrayList;
import android.os.Parcel;
import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import android.service.media.MediaBrowserService$Result;
import android.service.media.MediaBrowserService$BrowserRoot;
import android.os.Bundle;
import android.media.session.MediaSession$Token;
import android.os.IBinder;
import android.content.Intent;
import android.service.media.MediaBrowserService;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class MediaBrowserServiceCompatApi21
{
    public static Object createService(final Context context, final ServiceCompatProxy serviceCompatProxy) {
        return new MediaBrowserServiceAdaptor(context, serviceCompatProxy);
    }
    
    public static void notifyChildrenChanged(final Object o, final String s) {
        ((MediaBrowserService)o).notifyChildrenChanged(s);
    }
    
    public static IBinder onBind(final Object o, final Intent intent) {
        return ((MediaBrowserService)o).onBind(intent);
    }
    
    public static void onCreate(final Object o) {
        ((MediaBrowserService)o).onCreate();
    }
    
    public static void setSessionToken(final Object o, final Object o2) {
        ((MediaBrowserService)o).setSessionToken((MediaSession$Token)o2);
    }
    
    static class BrowserRoot
    {
        final Bundle mExtras;
        final String mRootId;
        
        BrowserRoot(final String mRootId, final Bundle mExtras) {
            this.mRootId = mRootId;
            this.mExtras = mExtras;
        }
    }
    
    static class MediaBrowserServiceAdaptor extends MediaBrowserService
    {
        final ServiceCompatProxy mServiceProxy;
        
        MediaBrowserServiceAdaptor(final Context context, final ServiceCompatProxy mServiceProxy) {
            this.attachBaseContext(context);
            this.mServiceProxy = mServiceProxy;
        }
        
        public MediaBrowserService$BrowserRoot onGetRoot(final String s, final int n, Bundle bundle) {
            final ServiceCompatProxy mServiceProxy = this.mServiceProxy;
            if (bundle == null) {
                bundle = null;
            }
            else {
                bundle = new Bundle(bundle);
            }
            final BrowserRoot onGetRoot = mServiceProxy.onGetRoot(s, n, bundle);
            if (onGetRoot == null) {
                return null;
            }
            return new MediaBrowserService$BrowserRoot(onGetRoot.mRootId, onGetRoot.mExtras);
        }
        
        public void onLoadChildren(final String s, final MediaBrowserService$Result<List<MediaBrowser$MediaItem>> mediaBrowserService$Result) {
            this.mServiceProxy.onLoadChildren(s, (ResultWrapper<List<Parcel>>)new ResultWrapper(mediaBrowserService$Result));
        }
    }
    
    static class ResultWrapper<T>
    {
        MediaBrowserService$Result mResultObj;
        
        ResultWrapper(final MediaBrowserService$Result mResultObj) {
            this.mResultObj = mResultObj;
        }
        
        public void detach() {
            this.mResultObj.detach();
        }
        
        List<MediaBrowser$MediaItem> parcelListToItemList(final List<Parcel> list) {
            if (list == null) {
                return null;
            }
            final ArrayList<Object> list2 = (ArrayList<Object>)new ArrayList<MediaBrowser$MediaItem>();
            for (final Parcel parcel : list) {
                parcel.setDataPosition(0);
                list2.add(MediaBrowser$MediaItem.CREATOR.createFromParcel(parcel));
                parcel.recycle();
            }
            return (List<MediaBrowser$MediaItem>)list2;
        }
        
        public void sendResult(final T t) {
            if (t instanceof List) {
                this.mResultObj.sendResult((Object)this.parcelListToItemList((List<Parcel>)t));
                return;
            }
            if (t instanceof Parcel) {
                final Parcel parcel = (Parcel)t;
                parcel.setDataPosition(0);
                this.mResultObj.sendResult(MediaBrowser$MediaItem.CREATOR.createFromParcel(parcel));
                parcel.recycle();
                return;
            }
            this.mResultObj.sendResult((Object)null);
        }
    }
    
    public interface ServiceCompatProxy
    {
        BrowserRoot onGetRoot(final String p0, final int p1, final Bundle p2);
        
        void onLoadChildren(final String p0, final ResultWrapper<List<Parcel>> p1);
    }
}
