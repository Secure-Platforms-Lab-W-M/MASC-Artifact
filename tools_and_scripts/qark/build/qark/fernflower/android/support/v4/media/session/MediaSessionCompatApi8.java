package android.support.v4.media.session;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;

class MediaSessionCompatApi8 {
   public static void registerMediaButtonEventReceiver(Context var0, ComponentName var1) {
      ((AudioManager)var0.getSystemService("audio")).registerMediaButtonEventReceiver(var1);
   }

   public static void unregisterMediaButtonEventReceiver(Context var0, ComponentName var1) {
      ((AudioManager)var0.getSystemService("audio")).unregisterMediaButtonEventReceiver(var1);
   }
}
