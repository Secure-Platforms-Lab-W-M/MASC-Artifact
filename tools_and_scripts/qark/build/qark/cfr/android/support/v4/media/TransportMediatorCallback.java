/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.KeyEvent
 */
package android.support.v4.media;

import android.view.KeyEvent;

interface TransportMediatorCallback {
    public long getPlaybackPosition();

    public void handleAudioFocusChange(int var1);

    public void handleKey(KeyEvent var1);

    public void playbackPositionUpdate(long var1);
}

