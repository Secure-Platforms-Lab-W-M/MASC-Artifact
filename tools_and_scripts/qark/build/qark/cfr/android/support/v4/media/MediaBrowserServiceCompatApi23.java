/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.browse.MediaBrowser
 *  android.media.browse.MediaBrowser$MediaItem
 *  android.os.Parcel
 *  android.service.media.MediaBrowserService
 *  android.service.media.MediaBrowserService$Result
 */
package android.support.v4.media;

import android.content.Context;
import android.media.browse.MediaBrowser;
import android.os.Parcel;
import android.service.media.MediaBrowserService;
import android.support.annotation.RequiresApi;
import android.support.v4.media.MediaBrowserServiceCompatApi21;

@RequiresApi(value=23)
class MediaBrowserServiceCompatApi23 {
    MediaBrowserServiceCompatApi23() {
    }

    public static Object createService(Context context, ServiceCompatProxy serviceCompatProxy) {
        return new MediaBrowserServiceAdaptor(context, serviceCompatProxy);
    }

    static class MediaBrowserServiceAdaptor
    extends MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptor {
        MediaBrowserServiceAdaptor(Context context, ServiceCompatProxy serviceCompatProxy) {
            super(context, serviceCompatProxy);
        }

        public void onLoadItem(String string2, MediaBrowserService.Result<MediaBrowser.MediaItem> result) {
            ((ServiceCompatProxy)this.mServiceProxy).onLoadItem(string2, new MediaBrowserServiceCompatApi21.ResultWrapper<Parcel>(result));
        }
    }

    public static interface ServiceCompatProxy
    extends MediaBrowserServiceCompatApi21.ServiceCompatProxy {
        public void onLoadItem(String var1, MediaBrowserServiceCompatApi21.ResultWrapper<Parcel> var2);
    }

}

