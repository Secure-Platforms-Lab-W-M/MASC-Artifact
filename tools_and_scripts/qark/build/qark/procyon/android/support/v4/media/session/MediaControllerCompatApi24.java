// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media.session;

import android.net.Uri;
import android.os.Bundle;
import android.media.session.MediaController$TransportControls;

class MediaControllerCompatApi24
{
    private MediaControllerCompatApi24() {
    }
    
    public static class TransportControls
    {
        private TransportControls() {
        }
        
        public static void prepare(final Object o) {
            ((MediaController$TransportControls)o).prepare();
        }
        
        public static void prepareFromMediaId(final Object o, final String s, final Bundle bundle) {
            ((MediaController$TransportControls)o).prepareFromMediaId(s, bundle);
        }
        
        public static void prepareFromSearch(final Object o, final String s, final Bundle bundle) {
            ((MediaController$TransportControls)o).prepareFromSearch(s, bundle);
        }
        
        public static void prepareFromUri(final Object o, final Uri uri, final Bundle bundle) {
            ((MediaController$TransportControls)o).prepareFromUri(uri, bundle);
        }
    }
}
