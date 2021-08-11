// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.speech.tts;

import android.util.Log;
import android.os.Build$VERSION;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech$OnInitListener;
import android.content.Context;

class TextToSpeechICS
{
    private static final String TAG = "android.support.v4.speech.tts";
    
    static TextToSpeech construct(final Context context, final TextToSpeech$OnInitListener textToSpeech$OnInitListener, final String s) {
        if (Build$VERSION.SDK_INT >= 14) {
            return new TextToSpeech(context, textToSpeech$OnInitListener, s);
        }
        if (s == null) {
            return new TextToSpeech(context, textToSpeech$OnInitListener);
        }
        Log.w("android.support.v4.speech.tts", "Can't specify tts engine on this device");
        return new TextToSpeech(context, textToSpeech$OnInitListener);
    }
}
