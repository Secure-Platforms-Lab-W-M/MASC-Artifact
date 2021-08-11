/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.session.PlaybackState
 *  android.media.session.PlaybackState$Builder
 *  android.media.session.PlaybackState$CustomAction
 *  android.os.Bundle
 */
package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;
import java.util.Iterator;
import java.util.List;

class PlaybackStateCompatApi22 {
    private PlaybackStateCompatApi22() {
    }

    public static Bundle getExtras(Object object) {
        return ((PlaybackState)object).getExtras();
    }

    public static Object newInstance(int n, long l, long l2, float f, long l3, CharSequence object, long l4, List<Object> list, long l5, Bundle bundle) {
        PlaybackState.Builder builder = new PlaybackState.Builder();
        builder.setState(n, l, f, l4);
        builder.setBufferedPosition(l2);
        builder.setActions(l3);
        builder.setErrorMessage((CharSequence)object);
        object = list.iterator();
        while (object.hasNext()) {
            builder.addCustomAction((PlaybackState.CustomAction)object.next());
        }
        builder.setActiveQueueItemId(l5);
        builder.setExtras(bundle);
        return builder.build();
    }
}

