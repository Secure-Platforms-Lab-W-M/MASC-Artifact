/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.media;

import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.media.VolumeProviderCompatApi21;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class VolumeProviderCompat {
    public static final int VOLUME_CONTROL_ABSOLUTE = 2;
    public static final int VOLUME_CONTROL_FIXED = 0;
    public static final int VOLUME_CONTROL_RELATIVE = 1;
    private Callback mCallback;
    private final int mControlType;
    private int mCurrentVolume;
    private final int mMaxVolume;
    private Object mVolumeProviderObj;

    public VolumeProviderCompat(int n, int n2, int n3) {
        this.mControlType = n;
        this.mMaxVolume = n2;
        this.mCurrentVolume = n3;
    }

    public final int getCurrentVolume() {
        return this.mCurrentVolume;
    }

    public final int getMaxVolume() {
        return this.mMaxVolume;
    }

    public final int getVolumeControl() {
        return this.mControlType;
    }

    public Object getVolumeProvider() {
        if (this.mVolumeProviderObj == null && Build.VERSION.SDK_INT >= 21) {
            this.mVolumeProviderObj = VolumeProviderCompatApi21.createVolumeProvider(this.mControlType, this.mMaxVolume, this.mCurrentVolume, new VolumeProviderCompatApi21.Delegate(){

                @Override
                public void onAdjustVolume(int n) {
                    VolumeProviderCompat.this.onAdjustVolume(n);
                }

                @Override
                public void onSetVolumeTo(int n) {
                    VolumeProviderCompat.this.onSetVolumeTo(n);
                }
            });
        }
        return this.mVolumeProviderObj;
    }

    public void onAdjustVolume(int n) {
    }

    public void onSetVolumeTo(int n) {
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public final void setCurrentVolume(int n) {
        this.mCurrentVolume = n;
        Object object = this.getVolumeProvider();
        if (object != null && Build.VERSION.SDK_INT >= 21) {
            VolumeProviderCompatApi21.setCurrentVolume(object, n);
        }
        if ((object = this.mCallback) != null) {
            object.onVolumeChanged(this);
            return;
        }
    }

    public static abstract class Callback {
        public abstract void onVolumeChanged(VolumeProviderCompat var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface ControlType {
    }

}

