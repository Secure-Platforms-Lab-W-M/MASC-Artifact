/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.browse.MediaBrowser
 *  android.media.browse.MediaBrowser$MediaItem
 *  android.media.browse.MediaBrowser$SubscriptionCallback
 *  android.os.Bundle
 */
package android.support.v4.media;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.media.MediaBrowserCompatApi21;
import java.util.List;

@RequiresApi(value=24)
class MediaBrowserCompatApi24 {
    MediaBrowserCompatApi24() {
    }

    public static Object createSubscriptionCallback(SubscriptionCallback subscriptionCallback) {
        return new SubscriptionCallbackProxy<SubscriptionCallback>(subscriptionCallback);
    }

    public static void subscribe(Object object, String string2, Bundle bundle, Object object2) {
        ((MediaBrowser)object).subscribe(string2, bundle, (MediaBrowser.SubscriptionCallback)object2);
    }

    public static void unsubscribe(Object object, String string2, Object object2) {
        ((MediaBrowser)object).unsubscribe(string2, (MediaBrowser.SubscriptionCallback)object2);
    }

    static interface SubscriptionCallback
    extends MediaBrowserCompatApi21.SubscriptionCallback {
        public void onChildrenLoaded(@NonNull String var1, List<?> var2, @NonNull Bundle var3);

        public void onError(@NonNull String var1, @NonNull Bundle var2);
    }

    static class SubscriptionCallbackProxy<T extends SubscriptionCallback>
    extends MediaBrowserCompatApi21.SubscriptionCallbackProxy<T> {
        public SubscriptionCallbackProxy(T t) {
            super(t);
        }

        public void onChildrenLoaded(@NonNull String string2, List<MediaBrowser.MediaItem> list, @NonNull Bundle bundle) {
            ((SubscriptionCallback)this.mSubscriptionCallback).onChildrenLoaded(string2, list, bundle);
        }

        public void onError(@NonNull String string2, @NonNull Bundle bundle) {
            ((SubscriptionCallback)this.mSubscriptionCallback).onError(string2, bundle);
        }
    }

}

