package android.support.v4.speech.tts;

import android.os.Build.VERSION;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import java.util.Locale;
import java.util.Set;

class TextToSpeechICSMR1 {
   public static final String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";
   public static final String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";

   static Set getFeatures(TextToSpeech var0, Locale var1) {
      return VERSION.SDK_INT >= 15 ? var0.getFeatures(var1) : null;
   }

   static void setUtteranceProgressListener(TextToSpeech var0, final TextToSpeechICSMR1.UtteranceProgressListenerICSMR1 var1) {
      if (VERSION.SDK_INT >= 15) {
         var0.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            public void onDone(String var1x) {
               var1.onDone(var1x);
            }

            public void onError(String var1x) {
               var1.onError(var1x);
            }

            public void onStart(String var1x) {
               var1.onStart(var1x);
            }
         });
      } else {
         var0.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
            public void onUtteranceCompleted(String var1x) {
               var1.onStart(var1x);
               var1.onDone(var1x);
            }
         });
      }
   }

   interface UtteranceProgressListenerICSMR1 {
      void onDone(String var1);

      void onError(String var1);

      void onStart(String var1);
   }
}
