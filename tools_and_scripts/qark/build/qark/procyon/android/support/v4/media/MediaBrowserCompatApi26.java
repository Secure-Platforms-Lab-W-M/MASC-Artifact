// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import android.support.v4.media.session.MediaSessionCompat;
import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import android.media.browse.MediaBrowser$SubscriptionCallback;
import android.media.browse.MediaBrowser;
import android.os.Bundle;

class MediaBrowserCompatApi26
{
    private MediaBrowserCompatApi26() {
    }
    
    static Object createSubscriptionCallback(final SubscriptionCallback subscriptionCallback) {
        return new SubscriptionCallbackProxy(subscriptionCallback);
    }
    
    public static void subscribe(final Object o, final String s, final Bundle bundle, final Object o2) {
        ((MediaBrowser)o).subscribe(s, bundle, (MediaBrowser$SubscriptionCallback)o2);
    }
    
    public static void unsubscribe(final Object o, final String s, final Object o2) {
        ((MediaBrowser)o).unsubscribe(s, (MediaBrowser$SubscriptionCallback)o2);
    }
    
    interface SubscriptionCallback extends MediaBrowserCompatApi21.SubscriptionCallback
    {
        void onChildrenLoaded(final String p0, final List<?> p1, final Bundle p2);
        
        void onError(final String p0, final Bundle p1);
    }
    
    static class SubscriptionCallbackProxy<T extends MediaBrowserCompatApi26.SubscriptionCallback> extends MediaBrowserCompatApi21.SubscriptionCallbackProxy<T>
    {
        SubscriptionCallbackProxy(final T t) {
            super(t);
        }
        
        public void onChildrenLoaded(final String s, final List<MediaBrowser$MediaItem> list, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((MediaBrowserCompatApi26.SubscriptionCallback)this.mSubscriptionCallback).onChildrenLoaded(s, list, bundle);
        }
        
        public void onError(final String s, final Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            ((MediaBrowserCompatApi26.SubscriptionCallback)this.mSubscriptionCallback).onError(s, bundle);
        }
    }
}
