package android.support.v4.speech.tts;

import android.content.Context;
import android.os.Build.VERSION;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

class TextToSpeechICS {
   private static final String TAG = "android.support.v4.speech.tts";

   static TextToSpeech construct(Context var0, OnInitListener var1, String var2) {
      if (VERSION.SDK_INT < 14) {
         if (var2 == null) {
            return new TextToSpeech(var0, var1);
         } else {
            Log.w("android.support.v4.speech.tts", "Can't specify tts engine on this device");
            return new TextToSpeech(var0, var1);
         }
      } else {
         return new TextToSpeech(var0, var1, var2);
      }
   }
}
