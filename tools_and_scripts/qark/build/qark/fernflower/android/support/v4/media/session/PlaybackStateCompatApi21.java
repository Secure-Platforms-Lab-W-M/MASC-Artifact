package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.media.session.PlaybackState.Builder;
import android.os.Bundle;
import java.util.Iterator;
import java.util.List;

class PlaybackStateCompatApi21 {
   private PlaybackStateCompatApi21() {
   }

   public static long getActions(Object var0) {
      return ((PlaybackState)var0).getActions();
   }

   public static long getActiveQueueItemId(Object var0) {
      return ((PlaybackState)var0).getActiveQueueItemId();
   }

   public static long getBufferedPosition(Object var0) {
      return ((PlaybackState)var0).getBufferedPosition();
   }

   public static List getCustomActions(Object var0) {
      return ((PlaybackState)var0).getCustomActions();
   }

   public static CharSequence getErrorMessage(Object var0) {
      return ((PlaybackState)var0).getErrorMessage();
   }

   public static long getLastPositionUpdateTime(Object var0) {
      return ((PlaybackState)var0).getLastPositionUpdateTime();
   }

   public static float getPlaybackSpeed(Object var0) {
      return ((PlaybackState)var0).getPlaybackSpeed();
   }

   public static long getPosition(Object var0) {
      return ((PlaybackState)var0).getPosition();
   }

   public static int getState(Object var0) {
      return ((PlaybackState)var0).getState();
   }

   public static Object newInstance(int var0, long var1, long var3, float var5, long var6, CharSequence var8, long var9, List var11, long var12) {
      Builder var14 = new Builder();
      var14.setState(var0, var1, var5, var9);
      var14.setBufferedPosition(var3);
      var14.setActions(var6);
      var14.setErrorMessage(var8);
      Iterator var15 = var11.iterator();

      while(var15.hasNext()) {
         var14.addCustomAction((android.media.session.PlaybackState.CustomAction)var15.next());
      }

      var14.setActiveQueueItemId(var12);
      return var14.build();
   }

   static final class CustomAction {
      private CustomAction() {
      }

      public static String getAction(Object var0) {
         return ((android.media.session.PlaybackState.CustomAction)var0).getAction();
      }

      public static Bundle getExtras(Object var0) {
         return ((android.media.session.PlaybackState.CustomAction)var0).getExtras();
      }

      public static int getIcon(Object var0) {
         return ((android.media.session.PlaybackState.CustomAction)var0).getIcon();
      }

      public static CharSequence getName(Object var0) {
         return ((android.media.session.PlaybackState.CustomAction)var0).getName();
      }

      public static Object newInstance(String var0, CharSequence var1, int var2, Bundle var3) {
         android.media.session.PlaybackState.CustomAction.Builder var4 = new android.media.session.PlaybackState.CustomAction.Builder(var0, var1, var2);
         var4.setExtras(var3);
         return var4.build();
      }
   }
}
