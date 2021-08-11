package android.support.v4.media;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import java.util.List;

class MediaBrowserCompatApi26 {
   private MediaBrowserCompatApi26() {
   }

   static Object createSubscriptionCallback(MediaBrowserCompatApi26.SubscriptionCallback var0) {
      return new MediaBrowserCompatApi26.SubscriptionCallbackProxy(var0);
   }

   public static void subscribe(Object var0, String var1, Bundle var2, Object var3) {
      ((MediaBrowser)var0).subscribe(var1, var2, (android.media.browse.MediaBrowser.SubscriptionCallback)var3);
   }

   public static void unsubscribe(Object var0, String var1, Object var2) {
      ((MediaBrowser)var0).unsubscribe(var1, (android.media.browse.MediaBrowser.SubscriptionCallback)var2);
   }

   interface SubscriptionCallback extends MediaBrowserCompatApi21.SubscriptionCallback {
      void onChildrenLoaded(String var1, List var2, Bundle var3);

      void onError(String var1, Bundle var2);
   }

   static class SubscriptionCallbackProxy extends MediaBrowserCompatApi21.SubscriptionCallbackProxy {
      SubscriptionCallbackProxy(MediaBrowserCompatApi26.SubscriptionCallback var1) {
         super(var1);
      }

      public void onChildrenLoaded(String var1, List var2, Bundle var3) {
         MediaSessionCompat.ensureClassLoader(var3);
         ((MediaBrowserCompatApi26.SubscriptionCallback)this.mSubscriptionCallback).onChildrenLoaded(var1, var2, var3);
      }

      public void onError(String var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         ((MediaBrowserCompatApi26.SubscriptionCallback)this.mSubscriptionCallback).onError(var1, var2);
      }
   }
}
