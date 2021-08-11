package androidx.media;

import android.content.Context;
import android.media.browse.MediaBrowser.MediaItem;
import android.os.Bundle;
import android.os.Parcel;
import android.service.media.MediaBrowserService;
import android.service.media.MediaBrowserService.Result;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class MediaBrowserServiceCompatApi26 {
   private static final String TAG = "MBSCompatApi26";
   static Field sResultFlags;

   static {
      try {
         Field var0 = Result.class.getDeclaredField("mFlags");
         sResultFlags = var0;
         var0.setAccessible(true);
      } catch (NoSuchFieldException var1) {
         Log.w("MBSCompatApi26", var1);
      }
   }

   private MediaBrowserServiceCompatApi26() {
   }

   public static Object createService(Context var0, MediaBrowserServiceCompatApi26.ServiceCompatProxy var1) {
      return new MediaBrowserServiceCompatApi26.MediaBrowserServiceAdaptor(var0, var1);
   }

   public static Bundle getBrowserRootHints(Object var0) {
      return ((MediaBrowserService)var0).getBrowserRootHints();
   }

   public static void notifyChildrenChanged(Object var0, String var1, Bundle var2) {
      ((MediaBrowserService)var0).notifyChildrenChanged(var1, var2);
   }

   static class MediaBrowserServiceAdaptor extends MediaBrowserServiceCompatApi23.MediaBrowserServiceAdaptor {
      MediaBrowserServiceAdaptor(Context var1, MediaBrowserServiceCompatApi26.ServiceCompatProxy var2) {
         super(var1, var2);
      }

      public void onLoadChildren(String var1, Result var2, Bundle var3) {
         MediaSessionCompat.ensureClassLoader(var3);
         ((MediaBrowserServiceCompatApi26.ServiceCompatProxy)this.mServiceProxy).onLoadChildren(var1, new MediaBrowserServiceCompatApi26.ResultWrapper(var2), var3);
      }
   }

   static class ResultWrapper {
      Result mResultObj;

      ResultWrapper(Result var1) {
         this.mResultObj = var1;
      }

      public void detach() {
         this.mResultObj.detach();
      }

      List parcelListToItemList(List var1) {
         if (var1 == null) {
            return null;
         } else {
            ArrayList var2 = new ArrayList();
            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
               Parcel var3 = (Parcel)var4.next();
               var3.setDataPosition(0);
               var2.add(MediaItem.CREATOR.createFromParcel(var3));
               var3.recycle();
            }

            return var2;
         }
      }

      public void sendResult(List var1, int var2) {
         try {
            MediaBrowserServiceCompatApi26.sResultFlags.setInt(this.mResultObj, var2);
         } catch (IllegalAccessException var4) {
            Log.w("MBSCompatApi26", var4);
         }

         this.mResultObj.sendResult(this.parcelListToItemList(var1));
      }
   }

   public interface ServiceCompatProxy extends MediaBrowserServiceCompatApi23.ServiceCompatProxy {
      void onLoadChildren(String var1, MediaBrowserServiceCompatApi26.ResultWrapper var2, Bundle var3);
   }
}
