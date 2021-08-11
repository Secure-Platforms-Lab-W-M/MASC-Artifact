/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.session.PlaybackState
 *  android.media.session.PlaybackState$Builder
 *  android.media.session.PlaybackState$CustomAction
 *  android.media.session.PlaybackState$CustomAction$Builder
 *  android.os.Bundle
 */
package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;
import java.util.Iterator;
import java.util.List;

class PlaybackStateCompatApi21 {
    private PlaybackStateCompatApi21() {
    }

    public static long getActions(Object object) {
        return ((PlaybackState)object).getActions();
    }

    public static long getActiveQueueItemId(Object object) {
        return ((PlaybackState)object).getActiveQueueItemId();
    }

    public static long getBufferedPosition(Object object) {
        return ((PlaybackState)object).getBufferedPosition();
    }

    public static List<Object> getCustomActions(Object object) {
        return ((PlaybackState)object).getCustomActions();
    }

    public static CharSequence getErrorMessage(Object object) {
        return ((PlaybackState)object).getErrorMessage();
    }

    public static long getLastPositionUpdateTime(Object object) {
        return ((PlaybackState)object).getLastPositionUpdateTime();
    }

    public static float getPlaybackSpeed(Object object) {
        return ((PlaybackState)object).getPlaybackSpeed();
    }

    public static long getPosition(Object object) {
        return ((PlaybackState)object).getPosition();
    }

    public static int getState(Object object) {
        return ((PlaybackState)object).getState();
    }

    public static Object newInstance(int n, long l, long l2, float f, long l3, CharSequence object, long l4, List<Object> list, long l5) {
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
        return builder.build();
    }

    static final class CustomAction {
        private CustomAction() {
        }

        public static String getAction(Object object) {
            return ((PlaybackState.CustomAction)object).getAction();
        }

        public static Bundle getExtras(Object object) {
            return ((PlaybackState.CustomAction)object).getExtras();
        }

        public static int getIcon(Object object) {
            return ((PlaybackState.CustomAction)object).getIcon();
        }

        public static CharSequence getName(Object object) {
            return ((PlaybackState.CustomAction)object).getName();
        }

        public static Object newInstance(String string, CharSequence charSequence, int n, Bundle bundle) {
            string = new PlaybackState.CustomAction.Builder(string, charSequence, n);
            string.setExtras(bundle);
            return string.build();
        }
    }

}

