// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media.session;

import android.media.session.MediaController$TransportControls;
import android.os.Bundle;
import android.net.Uri;

class MediaControllerCompatApi23
{
    private MediaControllerCompatApi23() {
    }
    
    public static class TransportControls
    {
        private TransportControls() {
        }
        
        public static void playFromUri(final Object o, final Uri uri, final Bundle bundle) {
            ((MediaController$TransportControls)o).playFromUri(uri, bundle);
        }
    }
}
