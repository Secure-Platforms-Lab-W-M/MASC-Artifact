/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.browse.MediaBrowser
 *  android.media.browse.MediaBrowser$MediaItem
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.service.media.MediaBrowserService
 *  android.service.media.MediaBrowserService$Result
 *  android.util.Log
 */
package androidx.media;

import android.content.Context;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.media.MediaBrowserService;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import androidx.media.MediaBrowserServiceCompatApi21;
import androidx.media.MediaBrowserServiceCompatApi23;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class MediaBrowserServiceCompatApi26 {
    private static final String TAG = "MBSCompatApi26";
    static Field sResultFlags;

    static {
        try {
            Field field;
            sResultFlags = field = MediaBrowserService.Result.class.getDeclaredField("mFlags");
            field.setAccessible(true);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.w((String)"MBSCompatApi26", (Throwable)noSuchFieldException);
        }
    }

    private MediaBrowserServiceCompatApi26() {
    }

    public static Object createService(Context context, ServiceCompatProxy serviceCompatProxy) {
        return new MediaBrowserServiceAdaptor(context, serviceCompatProxy);
    }

    public static Bundle getBrowserRootHints(Object object) {
        return ((MediaBrowserService)object).getBrowserRootHints();
    }

    public static void notifyChildrenChanged(Object object, String string2, Bundle bundle) {
        ((MediaBrowserService)object).notifyChildrenChanged(string2, bundle);
    }

    static class MediaBrowserServiceAdaptor
    extends MediaBrowserServiceCompatApi23.MediaBrowserServiceAdaptor {
        MediaBrowserServiceAdaptor(Context context, ServiceCompatProxy serviceCompatProxy) {
            super(context, serviceCompatProxy);
        }

        public void onLoadChildren(String string2, MediaBrowserService.Result<List<MediaBrowser.MediaItem>> result, Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((ServiceCompatProxy)this.mServiceProxy).onLoadChildren(string2, new ResultWrapper(result), bundle);
        }
    }

    static class ResultWrapper {
        MediaBrowserService.Result mResultObj;

        ResultWrapper(MediaBrowserService.Result result) {
            this.mResultObj = result;
        }

        public void detach() {
            this.mResultObj.detach();
        }

        List<MediaBrowser.MediaItem> parcelListToItemList(List<Parcel> object) {
            if (object == null) {
                return null;
            }
            ArrayList<MediaBrowser.MediaItem> arrayList = new ArrayList<MediaBrowser.MediaItem>();
            object = object.iterator();
            while (object.hasNext()) {
                Parcel parcel = (Parcel)object.next();
                parcel.setDataPosition(0);
                arrayList.add((MediaBrowser.MediaItem)MediaBrowser.MediaItem.CREATOR.createFromParcel(parcel));
                parcel.recycle();
            }
            return arrayList;
        }

        public void sendResult(List<Parcel> list, int n) {
            try {
                MediaBrowserServiceCompatApi26.sResultFlags.setInt((Object)this.mResultObj, n);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.w((String)"MBSCompatApi26", (Throwable)illegalAccessException);
            }
            this.mResultObj.sendResult(this.parcelListToItemList(list));
        }
    }

    public static interface ServiceCompatProxy
    extends MediaBrowserServiceCompatApi23.ServiceCompatProxy {
        public void onLoadChildren(String var1, ResultWrapper var2, Bundle var3);
    }

}

