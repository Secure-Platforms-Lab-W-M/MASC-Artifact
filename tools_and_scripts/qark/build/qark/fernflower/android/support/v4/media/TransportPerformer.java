package android.support.v4.media;

import android.os.SystemClock;
import android.view.KeyEvent;

@Deprecated
public abstract class TransportPerformer {
   static final int AUDIOFOCUS_GAIN = 1;
   static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
   static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
   static final int AUDIOFOCUS_LOSS = -1;
   static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
   static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;

   @Deprecated
   public void onAudioFocusChange(int var1) {
      byte var2 = 0;
      byte var5;
      switch(var1) {
      case -1:
         var5 = 127;
         break;
      default:
         var5 = var2;
      }

      if (var5 != 0) {
         long var3 = SystemClock.uptimeMillis();
         this.onMediaButtonDown(var5, new KeyEvent(var3, var3, 0, var5, 0));
         this.onMediaButtonUp(var5, new KeyEvent(var3, var3, 1, var5, 0));
      }

   }

   @Deprecated
   public int onGetBufferPercentage() {
      return 100;
   }

   @Deprecated
   public abstract long onGetCurrentPosition();

   @Deprecated
   public abstract long onGetDuration();

   @Deprecated
   public int onGetTransportControlFlags() {
      return 60;
   }

   @Deprecated
   public abstract boolean onIsPlaying();

   @Deprecated
   public boolean onMediaButtonDown(int var1, KeyEvent var2) {
      switch(var1) {
      case 79:
      case 85:
         if (this.onIsPlaying()) {
            this.onPause();
            return true;
         }

         this.onStart();
         return true;
      case 86:
         this.onStop();
         return true;
      case 126:
         this.onStart();
         return true;
      case 127:
         this.onPause();
         return true;
      default:
         return true;
      }
   }

   @Deprecated
   public boolean onMediaButtonUp(int var1, KeyEvent var2) {
      return true;
   }

   @Deprecated
   public abstract void onPause();

   @Deprecated
   public abstract void onSeekTo(long var1);

   @Deprecated
   public abstract void onStart();

   @Deprecated
   public abstract void onStop();
}
