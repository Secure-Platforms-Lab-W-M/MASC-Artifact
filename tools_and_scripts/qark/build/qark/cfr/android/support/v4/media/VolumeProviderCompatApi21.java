/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.VolumeProvider
 */
package android.support.v4.media;

import android.media.VolumeProvider;
import android.support.annotation.RequiresApi;

@RequiresApi(value=21)
class VolumeProviderCompatApi21 {
    VolumeProviderCompatApi21() {
    }

    public static Object createVolumeProvider(int n, int n2, int n3, final Delegate delegate) {
        return new VolumeProvider(n, n2, n3){

            public void onAdjustVolume(int n) {
                delegate.onAdjustVolume(n);
            }

            public void onSetVolumeTo(int n) {
                delegate.onSetVolumeTo(n);
            }
        };
    }

    public static void setCurrentVolume(Object object, int n) {
        ((VolumeProvider)object).setCurrentVolume(n);
    }

    public static interface Delegate {
        public void onAdjustVolume(int var1);

        public void onSetVolumeTo(int var1);
    }

}

