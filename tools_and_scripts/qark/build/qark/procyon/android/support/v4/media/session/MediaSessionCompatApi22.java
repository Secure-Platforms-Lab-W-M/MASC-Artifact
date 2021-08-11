// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media.session;

import android.media.session.MediaSession;

class MediaSessionCompatApi22
{
    private MediaSessionCompatApi22() {
    }
    
    public static void setRatingType(final Object o, final int ratingType) {
        ((MediaSession)o).setRatingType(ratingType);
    }
}
