package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.media.session.PlaybackState.Builder;
import android.media.session.PlaybackState.CustomAction;
import android.os.Bundle;
import java.util.Iterator;
import java.util.List;

class PlaybackStateCompatApi22 {
   private PlaybackStateCompatApi22() {
   }

   public static Bundle getExtras(Object var0) {
      return ((PlaybackState)var0).getExtras();
   }

   public static Object newInstance(int var0, long var1, long var3, float var5, long var6, CharSequence var8, long var9, List var11, long var12, Bundle var14) {
      Builder var15 = new Builder();
      var15.setState(var0, var1, var5, var9);
      var15.setBufferedPosition(var3);
      var15.setActions(var6);
      var15.setErrorMessage(var8);
      Iterator var16 = var11.iterator();

      while(var16.hasNext()) {
         var15.addCustomAction((CustomAction)var16.next());
      }

      var15.setActiveQueueItemId(var12);
      var15.setExtras(var14);
      return var15.build();
   }
}
