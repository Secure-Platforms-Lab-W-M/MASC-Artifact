/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.AudioAttributes
 *  android.media.AudioAttributes$Builder
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.SparseIntArray
 */
package android.support.v4.media;

import android.media.AudioAttributes;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.media.AudioAttributesCompatApi21;
import android.util.SparseIntArray;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public class AudioAttributesCompat {
    public static final int CONTENT_TYPE_MOVIE = 3;
    public static final int CONTENT_TYPE_MUSIC = 2;
    public static final int CONTENT_TYPE_SONIFICATION = 4;
    public static final int CONTENT_TYPE_SPEECH = 1;
    public static final int CONTENT_TYPE_UNKNOWN = 0;
    private static final int FLAG_ALL = 1023;
    private static final int FLAG_ALL_PUBLIC = 273;
    public static final int FLAG_AUDIBILITY_ENFORCED = 1;
    private static final int FLAG_BEACON = 8;
    private static final int FLAG_BYPASS_INTERRUPTION_POLICY = 64;
    private static final int FLAG_BYPASS_MUTE = 128;
    private static final int FLAG_DEEP_BUFFER = 512;
    public static final int FLAG_HW_AV_SYNC = 16;
    private static final int FLAG_HW_HOTWORD = 32;
    private static final int FLAG_LOW_LATENCY = 256;
    private static final int FLAG_SCO = 4;
    private static final int FLAG_SECURE = 2;
    private static final int[] SDK_USAGES;
    private static final int SUPPRESSIBLE_CALL = 2;
    private static final int SUPPRESSIBLE_NOTIFICATION = 1;
    private static final SparseIntArray SUPPRESSIBLE_USAGES;
    private static final String TAG = "AudioAttributesCompat";
    public static final int USAGE_ALARM = 4;
    public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;
    public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
    public static final int USAGE_ASSISTANCE_SONIFICATION = 13;
    public static final int USAGE_ASSISTANT = 16;
    public static final int USAGE_GAME = 14;
    public static final int USAGE_MEDIA = 1;
    public static final int USAGE_NOTIFICATION = 5;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
    public static final int USAGE_NOTIFICATION_EVENT = 10;
    public static final int USAGE_NOTIFICATION_RINGTONE = 6;
    public static final int USAGE_UNKNOWN = 0;
    private static final int USAGE_VIRTUAL_SOURCE = 15;
    public static final int USAGE_VOICE_COMMUNICATION = 2;
    public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;
    private static boolean sForceLegacyBehavior;
    private AudioAttributesCompatApi21.Wrapper mAudioAttributesWrapper;
    int mContentType = 0;
    int mFlags = 0;
    Integer mLegacyStream;
    int mUsage = 0;

    static {
        SUPPRESSIBLE_USAGES = new SparseIntArray();
        SUPPRESSIBLE_USAGES.put(5, 1);
        SUPPRESSIBLE_USAGES.put(6, 2);
        SUPPRESSIBLE_USAGES.put(7, 2);
        SUPPRESSIBLE_USAGES.put(8, 1);
        SUPPRESSIBLE_USAGES.put(9, 1);
        SUPPRESSIBLE_USAGES.put(10, 1);
        SDK_USAGES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16};
    }

    private AudioAttributesCompat() {
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void setForceLegacyBehavior(boolean bl) {
        sForceLegacyBehavior = bl;
    }

    static int toVolumeStreamType(boolean bl, int n, int n2) {
        if ((n & 1) == 1) {
            if (bl) {
                return 1;
            }
            return 7;
        }
        if ((n & 4) == 4) {
            if (bl) {
                return 0;
            }
            return 6;
        }
        n = 3;
        switch (n2) {
            default: {
                if (bl) break;
                return 3;
            }
            case 13: {
                return 1;
            }
            case 11: {
                return 10;
            }
            case 6: {
                return 2;
            }
            case 5: 
            case 7: 
            case 8: 
            case 9: 
            case 10: {
                return 5;
            }
            case 4: {
                return 4;
            }
            case 3: {
                if (bl) {
                    return 0;
                }
                return 8;
            }
            case 2: {
                return 0;
            }
            case 1: 
            case 12: 
            case 14: 
            case 16: {
                return 3;
            }
            case 0: {
                if (bl) {
                    n = Integer.MIN_VALUE;
                }
                return n;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown usage value ");
        stringBuilder.append(n2);
        stringBuilder.append(" in audio attributes");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    static int toVolumeStreamType(boolean bl, AudioAttributesCompat audioAttributesCompat) {
        return AudioAttributesCompat.toVolumeStreamType(bl, audioAttributesCompat.getFlags(), audioAttributesCompat.getUsage());
    }

    private static int usageForStreamType(int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 10: {
                return 11;
            }
            case 8: {
                return 3;
            }
            case 6: {
                return 2;
            }
            case 5: {
                return 5;
            }
            case 4: {
                return 4;
            }
            case 3: {
                return 1;
            }
            case 2: {
                return 6;
            }
            case 1: 
            case 7: {
                return 13;
            }
            case 0: 
        }
        return 2;
    }

    static String usageToString(int n) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unknown usage ");
                stringBuilder.append(n);
                return new String(stringBuilder.toString());
            }
            case 16: {
                return new String("USAGE_ASSISTANT");
            }
            case 14: {
                return new String("USAGE_GAME");
            }
            case 13: {
                return new String("USAGE_ASSISTANCE_SONIFICATION");
            }
            case 12: {
                return new String("USAGE_ASSISTANCE_NAVIGATION_GUIDANCE");
            }
            case 11: {
                return new String("USAGE_ASSISTANCE_ACCESSIBILITY");
            }
            case 10: {
                return new String("USAGE_NOTIFICATION_EVENT");
            }
            case 9: {
                return new String("USAGE_NOTIFICATION_COMMUNICATION_DELAYED");
            }
            case 8: {
                return new String("USAGE_NOTIFICATION_COMMUNICATION_INSTANT");
            }
            case 7: {
                return new String("USAGE_NOTIFICATION_COMMUNICATION_REQUEST");
            }
            case 6: {
                return new String("USAGE_NOTIFICATION_RINGTONE");
            }
            case 5: {
                return new String("USAGE_NOTIFICATION");
            }
            case 4: {
                return new String("USAGE_ALARM");
            }
            case 3: {
                return new String("USAGE_VOICE_COMMUNICATION_SIGNALLING");
            }
            case 2: {
                return new String("USAGE_VOICE_COMMUNICATION");
            }
            case 1: {
                return new String("USAGE_MEDIA");
            }
            case 0: 
        }
        return new String("USAGE_UNKNOWN");
    }

    @Nullable
    public static AudioAttributesCompat wrap(@NonNull Object object) {
        if (Build.VERSION.SDK_INT >= 21 && !sForceLegacyBehavior) {
            AudioAttributesCompat audioAttributesCompat = new AudioAttributesCompat();
            audioAttributesCompat.mAudioAttributesWrapper = AudioAttributesCompatApi21.Wrapper.wrap((AudioAttributes)object);
            return audioAttributesCompat;
        }
        return null;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null) {
            Object object2;
            if (this.getClass() != object.getClass()) {
                return false;
            }
            object = (AudioAttributesCompat)object;
            if (Build.VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && (object2 = this.mAudioAttributesWrapper) != null) {
                return object2.unwrap().equals(object.unwrap());
            }
            if (this.mContentType == object.getContentType() && this.mFlags == object.getFlags() && this.mUsage == object.getUsage() && ((object2 = this.mLegacyStream) != null ? object2.equals(object.mLegacyStream) : object.mLegacyStream == null)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int getContentType() {
        AudioAttributesCompatApi21.Wrapper wrapper;
        if (Build.VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && (wrapper = this.mAudioAttributesWrapper) != null) {
            return wrapper.unwrap().getContentType();
        }
        return this.mContentType;
    }

    public int getFlags() {
        AudioAttributesCompatApi21.Wrapper wrapper;
        if (Build.VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && (wrapper = this.mAudioAttributesWrapper) != null) {
            return wrapper.unwrap().getFlags();
        }
        int n = this.mFlags;
        int n2 = this.getLegacyStreamType();
        if (n2 == 6) {
            n |= 4;
        } else if (n2 == 7) {
            n |= 1;
        }
        return n & 273;
    }

    public int getLegacyStreamType() {
        Integer n = this.mLegacyStream;
        if (n != null) {
            return n;
        }
        if (Build.VERSION.SDK_INT >= 21 && !sForceLegacyBehavior) {
            return AudioAttributesCompatApi21.toLegacyStreamType(this.mAudioAttributesWrapper);
        }
        return AudioAttributesCompat.toVolumeStreamType(false, this.mFlags, this.mUsage);
    }

    public int getUsage() {
        AudioAttributesCompatApi21.Wrapper wrapper;
        if (Build.VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && (wrapper = this.mAudioAttributesWrapper) != null) {
            return wrapper.unwrap().getUsage();
        }
        return this.mUsage;
    }

    public int getVolumeControlStream() {
        if (Build.VERSION.SDK_INT >= 26 && !sForceLegacyBehavior && this.unwrap() != null) {
            return ((AudioAttributes)this.unwrap()).getVolumeControlStream();
        }
        return AudioAttributesCompat.toVolumeStreamType(true, this);
    }

    public int hashCode() {
        AudioAttributesCompatApi21.Wrapper wrapper;
        if (Build.VERSION.SDK_INT >= 21 && !sForceLegacyBehavior && (wrapper = this.mAudioAttributesWrapper) != null) {
            return wrapper.unwrap().hashCode();
        }
        return Arrays.hashCode(new Object[]{this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("AudioAttributesCompat:");
        if (this.unwrap() != null) {
            stringBuilder.append(" audioattributes=");
            stringBuilder.append(this.unwrap());
        } else {
            if (this.mLegacyStream != null) {
                stringBuilder.append(" stream=");
                stringBuilder.append(this.mLegacyStream);
                stringBuilder.append(" derived");
            }
            stringBuilder.append(" usage=");
            stringBuilder.append(this.usageToString());
            stringBuilder.append(" content=");
            stringBuilder.append(this.mContentType);
            stringBuilder.append(" flags=0x");
            stringBuilder.append(Integer.toHexString(this.mFlags).toUpperCase());
        }
        return stringBuilder.toString();
    }

    @Nullable
    public Object unwrap() {
        AudioAttributesCompatApi21.Wrapper wrapper = this.mAudioAttributesWrapper;
        if (wrapper != null) {
            return wrapper.unwrap();
        }
        return null;
    }

    String usageToString() {
        return AudioAttributesCompat.usageToString(this.mUsage);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface AttributeContentType {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface AttributeUsage {
    }

    private static abstract class AudioManagerHidden {
        public static final int STREAM_ACCESSIBILITY = 10;
        public static final int STREAM_BLUETOOTH_SCO = 6;
        public static final int STREAM_SYSTEM_ENFORCED = 7;
        public static final int STREAM_TTS = 9;

        private AudioManagerHidden() {
        }
    }

    public static class Builder {
        private Object mAAObject;
        private int mContentType = 0;
        private int mFlags = 0;
        private Integer mLegacyStream;
        private int mUsage = 0;

        public Builder() {
        }

        public Builder(AudioAttributesCompat audioAttributesCompat) {
            this.mUsage = audioAttributesCompat.mUsage;
            this.mContentType = audioAttributesCompat.mContentType;
            this.mFlags = audioAttributesCompat.mFlags;
            this.mLegacyStream = audioAttributesCompat.mLegacyStream;
            this.mAAObject = audioAttributesCompat.unwrap();
        }

        public AudioAttributesCompat build() {
            if (!sForceLegacyBehavior && Build.VERSION.SDK_INT >= 21) {
                Object object = this.mAAObject;
                if (object != null) {
                    return AudioAttributesCompat.wrap(object);
                }
                object = new AudioAttributes.Builder().setContentType(this.mContentType).setFlags(this.mFlags).setUsage(this.mUsage);
                Integer n = this.mLegacyStream;
                if (n != null) {
                    object.setLegacyStreamType(n.intValue());
                }
                return AudioAttributesCompat.wrap((Object)object.build());
            }
            AudioAttributesCompat audioAttributesCompat = new AudioAttributesCompat();
            audioAttributesCompat.mContentType = this.mContentType;
            audioAttributesCompat.mFlags = this.mFlags;
            audioAttributesCompat.mUsage = this.mUsage;
            audioAttributesCompat.mLegacyStream = this.mLegacyStream;
            audioAttributesCompat.mAudioAttributesWrapper = null;
            return audioAttributesCompat;
        }

        public Builder setContentType(int n) {
            switch (n) {
                default: {
                    this.mUsage = 0;
                    return this;
                }
                case 0: 
                case 1: 
                case 2: 
                case 3: 
                case 4: 
            }
            this.mContentType = n;
            return this;
        }

        public Builder setFlags(int n) {
            this.mFlags |= n & 1023;
            return this;
        }

        public Builder setLegacyStreamType(int n) {
            if (n != 10) {
                this.mLegacyStream = n;
                this.mUsage = AudioAttributesCompat.usageForStreamType(n);
                return this;
            }
            throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
        }

        public Builder setUsage(int n) {
            switch (n) {
                default: {
                    this.mUsage = 0;
                    return this;
                }
                case 16: {
                    if (!sForceLegacyBehavior && Build.VERSION.SDK_INT > 25) {
                        this.mUsage = n;
                        return this;
                    }
                    this.mUsage = 12;
                    return this;
                }
                case 0: 
                case 1: 
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 6: 
                case 7: 
                case 8: 
                case 9: 
                case 10: 
                case 11: 
                case 12: 
                case 13: 
                case 14: 
                case 15: 
            }
            this.mUsage = n;
            return this;
        }
    }

}

