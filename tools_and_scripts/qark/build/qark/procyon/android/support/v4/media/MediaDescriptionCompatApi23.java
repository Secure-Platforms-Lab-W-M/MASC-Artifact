// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import android.media.MediaDescription$Builder;
import android.media.MediaDescription;
import android.net.Uri;

class MediaDescriptionCompatApi23
{
    private MediaDescriptionCompatApi23() {
    }
    
    public static Uri getMediaUri(final Object o) {
        return ((MediaDescription)o).getMediaUri();
    }
    
    static class Builder
    {
        private Builder() {
        }
        
        public static void setMediaUri(final Object o, final Uri mediaUri) {
            ((MediaDescription$Builder)o).setMediaUri(mediaUri);
        }
    }
}
