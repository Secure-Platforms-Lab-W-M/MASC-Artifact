package android.support.v4.media;

import android.content.Context;
import android.service.media.MediaBrowserService.Result;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class MediaBrowserServiceCompatApi23 {
   public static Object createService(Context var0, MediaBrowserServiceCompatApi23.ServiceCompatProxy var1) {
      return new MediaBrowserServiceCompatApi23.MediaBrowserServiceAdaptor(var0, var1);
   }

   static class MediaBrowserServiceAdaptor extends MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptor {
      MediaBrowserServiceAdaptor(Context var1, MediaBrowserServiceCompatApi23.ServiceCompatProxy var2) {
         super(var1, var2);
      }

      public void onLoadItem(String var1, Result var2) {
         ((MediaBrowserServiceCompatApi23.ServiceCompatProxy)this.mServiceProxy).onLoadItem(var1, new MediaBrowserServiceCompatApi21.ResultWrapper(var2));
      }
   }

   public interface ServiceCompatProxy extends MediaBrowserServiceCompatApi21.ServiceCompatProxy {
      void onLoadItem(String var1, MediaBrowserServiceCompatApi21.ResultWrapper var2);
   }
}
