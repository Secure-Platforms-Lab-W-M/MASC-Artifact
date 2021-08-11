/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.speech.tts.TextToSpeech
 *  android.speech.tts.TextToSpeech$OnUtteranceCompletedListener
 *  android.speech.tts.UtteranceProgressListener
 */
package android.support.v4.speech.tts;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import java.util.Locale;
import java.util.Set;

class TextToSpeechICSMR1 {
    public static final String KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts";
    public static final String KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts";

    TextToSpeechICSMR1() {
    }

    static Set<String> getFeatures(TextToSpeech textToSpeech, Locale locale) {
        if (Build.VERSION.SDK_INT >= 15) {
            return textToSpeech.getFeatures(locale);
        }
        return null;
    }

    static void setUtteranceProgressListener(TextToSpeech textToSpeech, final UtteranceProgressListenerICSMR1 utteranceProgressListenerICSMR1) {
        if (Build.VERSION.SDK_INT >= 15) {
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener(){

                public void onDone(String string) {
                    utteranceProgressListenerICSMR1.onDone(string);
                }

                public void onError(String string) {
                    utteranceProgressListenerICSMR1.onError(string);
                }

                public void onStart(String string) {
                    utteranceProgressListenerICSMR1.onStart(string);
                }
            });
            return;
        }
        textToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener(){

            public void onUtteranceCompleted(String string) {
                utteranceProgressListenerICSMR1.onStart(string);
                utteranceProgressListenerICSMR1.onDone(string);
            }
        });
    }

    static interface UtteranceProgressListenerICSMR1 {
        public void onDone(String var1);

        public void onError(String var1);

        public void onStart(String var1);
    }

}

