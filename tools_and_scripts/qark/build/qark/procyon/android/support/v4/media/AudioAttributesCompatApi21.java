// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import android.support.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import android.media.AudioAttributes;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class AudioAttributesCompatApi21
{
    private static final String TAG = "AudioAttributesCompat";
    private static Method sAudioAttributesToLegacyStreamType;
    
    public static int toLegacyStreamType(final Wrapper wrapper) {
        while (true) {
            final AudioAttributes unwrap = wrapper.unwrap();
            while (true) {
                Label_0069: {
                    try {
                        if (AudioAttributesCompatApi21.sAudioAttributesToLegacyStreamType != null) {
                            break Label_0069;
                        }
                        AudioAttributesCompatApi21.sAudioAttributesToLegacyStreamType = AudioAttributes.class.getMethod("toLegacyStreamType", AudioAttributes.class);
                        try {
                            return (int)AudioAttributesCompatApi21.sAudioAttributesToLegacyStreamType.invoke(null, unwrap);
                        }
                        catch (InvocationTargetException | IllegalAccessException ex) {
                            final Throwable t;
                            Log.w("AudioAttributesCompat", "getLegacyStreamType() failed on API21+", t);
                            return -1;
                        }
                    }
                    catch (NoSuchMethodException ex2) {}
                    catch (InvocationTargetException ex3) {}
                    catch (IllegalAccessException ex4) {}
                    catch (ClassCastException ex5) {}
                }
                continue;
            }
        }
    }
    
    static final class Wrapper
    {
        private AudioAttributes mWrapped;
        
        private Wrapper(final AudioAttributes mWrapped) {
            this.mWrapped = mWrapped;
        }
        
        public static Wrapper wrap(@NonNull final AudioAttributes audioAttributes) {
            if (audioAttributes != null) {
                return new Wrapper(audioAttributes);
            }
            throw new IllegalArgumentException("AudioAttributesApi21.Wrapper cannot wrap null");
        }
        
        public AudioAttributes unwrap() {
            return this.mWrapped;
        }
    }
}
