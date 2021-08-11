// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import java.util.Iterator;
import java.util.ArrayList;
import android.os.Parcel;
import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import android.service.media.MediaBrowserService;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.service.media.MediaBrowserService$Result;
import java.lang.reflect.Field;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class MediaBrowserServiceCompatApi24
{
    private static final String TAG = "MBSCompatApi24";
    private static Field sResultFlags;
    
    static {
        try {
            (MediaBrowserServiceCompatApi24.sResultFlags = MediaBrowserService$Result.class.getDeclaredField("mFlags")).setAccessible(true);
        }
        catch (NoSuchFieldException ex) {
            Log.w("MBSCompatApi24", (Throwable)ex);
        }
    }
    
    public static Object createService(final Context context, final ServiceCompatProxy serviceCompatProxy) {
        return new MediaBrowserServiceAdaptor(context, serviceCompatProxy);
    }
    
    public static Bundle getBrowserRootHints(final Object o) {
        return ((MediaBrowserService)o).getBrowserRootHints();
    }
    
    public static void notifyChildrenChanged(final Object o, final String s, final Bundle bundle) {
        ((MediaBrowserService)o).notifyChildrenChanged(s, bundle);
    }
    
    static class MediaBrowserServiceAdaptor extends MediaBrowserServiceCompatApi23.MediaBrowserServiceAdaptor
    {
        MediaBrowserServiceAdaptor(final Context context, final MediaBrowserServiceCompatApi24.ServiceCompatProxy serviceCompatProxy) {
            super(context, serviceCompatProxy);
        }
        
        public void onLoadChildren(final String s, final MediaBrowserService$Result<List<MediaBrowser$MediaItem>> mediaBrowserService$Result, final Bundle bundle) {
            ((MediaBrowserServiceCompatApi24.ServiceCompatProxy)this.mServiceProxy).onLoadChildren(s, new MediaBrowserServiceCompatApi24.ResultWrapper(mediaBrowserService$Result), bundle);
        }
    }
    
    static class ResultWrapper
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
        
        public void sendResult(final List<Parcel> list, final int n) {
            try {
                MediaBrowserServiceCompatApi24.sResultFlags.setInt(this.mResultObj, n);
            }
            catch (IllegalAccessException ex) {
                Log.w("MBSCompatApi24", (Throwable)ex);
            }
            this.mResultObj.sendResult((Object)this.parcelListToItemList(list));
        }
    }
    
    public interface ServiceCompatProxy extends MediaBrowserServiceCompatApi23.ServiceCompatProxy
    {
        void onLoadChildren(final String p0, final MediaBrowserServiceCompatApi24.ResultWrapper p1, final Bundle p2);
    }
}
