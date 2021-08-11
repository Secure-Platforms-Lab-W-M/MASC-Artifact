package android.support.v4.media;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;

@TargetApi(18)
@RequiresApi(18)
interface TransportMediatorCallback {
   long getPlaybackPosition();

   void handleAudioFocusChange(int var1);

   void handleKey(KeyEvent var1);

   void playbackPositionUpdate(long var1);
}
