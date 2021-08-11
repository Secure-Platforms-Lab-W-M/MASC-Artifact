// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media.session;

import android.media.session.MediaController$TransportControls;
import android.media.session.MediaController;

class MediaControllerCompatApi26
{
    public static Object createCallback(final Callback callback) {
        return new CallbackProxy(callback);
    }
    
    public static int getRepeatMode(final Object o) {
        return ((MediaController)o).getRepeatMode();
    }
    
    public static boolean isShuffleModeEnabled(final Object o) {
        return ((MediaController)o).isShuffleModeEnabled();
    }
    
    public interface Callback extends MediaControllerCompatApi21.Callback
    {
        void onRepeatModeChanged(final int p0);
        
        void onShuffleModeChanged(final boolean p0);
    }
    
    static class CallbackProxy<T extends MediaControllerCompatApi26.Callback> extends MediaControllerCompatApi21.CallbackProxy<T>
    {
        CallbackProxy(final T t) {
            super(t);
        }
        
        public void onRepeatModeChanged(final int n) {
            ((MediaControllerCompatApi26.Callback)this.mCallback).onRepeatModeChanged(n);
        }
        
        public void onShuffleModeChanged(final boolean b) {
            ((MediaControllerCompatApi26.Callback)this.mCallback).onShuffleModeChanged(b);
        }
    }
    
    public static class TransportControls extends MediaControllerCompatApi23.TransportControls
    {
        public static void setRepeatMode(final Object o, final int repeatMode) {
            ((MediaController$TransportControls)o).setRepeatMode(repeatMode);
        }
        
        public static void setShuffleModeEnabled(final Object o, final boolean shuffleModeEnabled) {
            ((MediaController$TransportControls)o).setShuffleModeEnabled(shuffleModeEnabled);
        }
    }
}
