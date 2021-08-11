// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media.session;

import android.media.session.MediaSession;

class MediaSessionCompatApi26
{
    public static Object createCallback(final Callback callback) {
        return new CallbackProxy(callback);
    }
    
    public static void setRepeatMode(final Object o, final int repeatMode) {
        ((MediaSession)o).setRepeatMode(repeatMode);
    }
    
    public static void setShuffleModeEnabled(final Object o, final boolean shuffleModeEnabled) {
        ((MediaSession)o).setShuffleModeEnabled(shuffleModeEnabled);
    }
    
    public interface Callback extends MediaSessionCompatApi24.Callback
    {
        void onSetRepeatMode(final int p0);
        
        void onSetShuffleModeEnabled(final boolean p0);
    }
    
    static class CallbackProxy<T extends MediaSessionCompatApi26.Callback> extends MediaSessionCompatApi24.CallbackProxy<T>
    {
        CallbackProxy(final T t) {
            super(t);
        }
        
        public void onSetRepeatMode(final int n) {
            ((MediaSessionCompatApi26.Callback)this.mCallback).onSetRepeatMode(n);
        }
        
        public void onSetShuffleModeEnabled(final boolean b) {
            ((MediaSessionCompatApi26.Callback)this.mCallback).onSetShuffleModeEnabled(b);
        }
    }
}
