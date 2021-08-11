/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.text.TextUtils
 */
package android.support.v4.media.session;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompatApi21;
import android.support.v4.media.session.PlaybackStateCompatApi22;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class PlaybackStateCompat
implements Parcelable {
    public static final long ACTION_FAST_FORWARD = 64L;
    public static final long ACTION_PAUSE = 2L;
    public static final long ACTION_PLAY = 4L;
    public static final long ACTION_PLAY_FROM_MEDIA_ID = 1024L;
    public static final long ACTION_PLAY_FROM_SEARCH = 2048L;
    public static final long ACTION_PLAY_FROM_URI = 8192L;
    public static final long ACTION_PLAY_PAUSE = 512L;
    public static final long ACTION_PREPARE = 16384L;
    public static final long ACTION_PREPARE_FROM_MEDIA_ID = 32768L;
    public static final long ACTION_PREPARE_FROM_SEARCH = 65536L;
    public static final long ACTION_PREPARE_FROM_URI = 131072L;
    public static final long ACTION_REWIND = 8L;
    public static final long ACTION_SEEK_TO = 256L;
    public static final long ACTION_SET_CAPTIONING_ENABLED = 0x100000L;
    public static final long ACTION_SET_RATING = 128L;
    public static final long ACTION_SET_REPEAT_MODE = 262144L;
    public static final long ACTION_SET_SHUFFLE_MODE = 0x200000L;
    @Deprecated
    public static final long ACTION_SET_SHUFFLE_MODE_ENABLED = 524288L;
    public static final long ACTION_SKIP_TO_NEXT = 32L;
    public static final long ACTION_SKIP_TO_PREVIOUS = 16L;
    public static final long ACTION_SKIP_TO_QUEUE_ITEM = 4096L;
    public static final long ACTION_STOP = 1L;
    public static final Parcelable.Creator<PlaybackStateCompat> CREATOR = new Parcelable.Creator<PlaybackStateCompat>(){

        public PlaybackStateCompat createFromParcel(Parcel parcel) {
            return new PlaybackStateCompat(parcel);
        }

        public PlaybackStateCompat[] newArray(int n) {
            return new PlaybackStateCompat[n];
        }
    };
    public static final int ERROR_CODE_ACTION_ABORTED = 10;
    public static final int ERROR_CODE_APP_ERROR = 1;
    public static final int ERROR_CODE_AUTHENTICATION_EXPIRED = 3;
    public static final int ERROR_CODE_CONCURRENT_STREAM_LIMIT = 5;
    public static final int ERROR_CODE_CONTENT_ALREADY_PLAYING = 8;
    public static final int ERROR_CODE_END_OF_QUEUE = 11;
    public static final int ERROR_CODE_NOT_AVAILABLE_IN_REGION = 7;
    public static final int ERROR_CODE_NOT_SUPPORTED = 2;
    public static final int ERROR_CODE_PARENTAL_CONTROL_RESTRICTED = 6;
    public static final int ERROR_CODE_PREMIUM_ACCOUNT_REQUIRED = 4;
    public static final int ERROR_CODE_SKIP_LIMIT_REACHED = 9;
    public static final int ERROR_CODE_UNKNOWN_ERROR = 0;
    private static final int KEYCODE_MEDIA_PAUSE = 127;
    private static final int KEYCODE_MEDIA_PLAY = 126;
    public static final long PLAYBACK_POSITION_UNKNOWN = -1L;
    public static final int REPEAT_MODE_ALL = 2;
    public static final int REPEAT_MODE_GROUP = 3;
    public static final int REPEAT_MODE_INVALID = -1;
    public static final int REPEAT_MODE_NONE = 0;
    public static final int REPEAT_MODE_ONE = 1;
    public static final int SHUFFLE_MODE_ALL = 1;
    public static final int SHUFFLE_MODE_GROUP = 2;
    public static final int SHUFFLE_MODE_INVALID = -1;
    public static final int SHUFFLE_MODE_NONE = 0;
    public static final int STATE_BUFFERING = 6;
    public static final int STATE_CONNECTING = 8;
    public static final int STATE_ERROR = 7;
    public static final int STATE_FAST_FORWARDING = 4;
    public static final int STATE_NONE = 0;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_REWINDING = 5;
    public static final int STATE_SKIPPING_TO_NEXT = 10;
    public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    public static final int STATE_STOPPED = 1;
    final long mActions;
    final long mActiveItemId;
    final long mBufferedPosition;
    List<CustomAction> mCustomActions;
    final int mErrorCode;
    final CharSequence mErrorMessage;
    final Bundle mExtras;
    final long mPosition;
    final float mSpeed;
    final int mState;
    private Object mStateObj;
    final long mUpdateTime;

    PlaybackStateCompat(int n, long l, long l2, float f, long l3, int n2, CharSequence charSequence, long l4, List<CustomAction> list, long l5, Bundle bundle) {
        this.mState = n;
        this.mPosition = l;
        this.mBufferedPosition = l2;
        this.mSpeed = f;
        this.mActions = l3;
        this.mErrorCode = n2;
        this.mErrorMessage = charSequence;
        this.mUpdateTime = l4;
        this.mCustomActions = new ArrayList<CustomAction>(list);
        this.mActiveItemId = l5;
        this.mExtras = bundle;
    }

    PlaybackStateCompat(Parcel parcel) {
        this.mState = parcel.readInt();
        this.mPosition = parcel.readLong();
        this.mSpeed = parcel.readFloat();
        this.mUpdateTime = parcel.readLong();
        this.mBufferedPosition = parcel.readLong();
        this.mActions = parcel.readLong();
        this.mErrorMessage = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mCustomActions = parcel.createTypedArrayList(CustomAction.CREATOR);
        this.mActiveItemId = parcel.readLong();
        this.mExtras = parcel.readBundle(MediaSessionCompat.class.getClassLoader());
        this.mErrorCode = parcel.readInt();
    }

    public static PlaybackStateCompat fromPlaybackState(Object object) {
        if (object != null && Build.VERSION.SDK_INT >= 21) {
            Bundle bundle;
            Object object2 = PlaybackStateCompatApi21.getCustomActions(object);
            Object object3 = null;
            if (object2 != null) {
                bundle = new Bundle(object2.size());
                object2 = object2.iterator();
                do {
                    object3 = bundle;
                    if (!object2.hasNext()) break;
                    bundle.add(CustomAction.fromCustomAction(object2.next()));
                } while (true);
            }
            bundle = Build.VERSION.SDK_INT >= 22 ? PlaybackStateCompatApi22.getExtras(object) : null;
            object3 = new PlaybackStateCompat(PlaybackStateCompatApi21.getState(object), PlaybackStateCompatApi21.getPosition(object), PlaybackStateCompatApi21.getBufferedPosition(object), PlaybackStateCompatApi21.getPlaybackSpeed(object), PlaybackStateCompatApi21.getActions(object), 0, PlaybackStateCompatApi21.getErrorMessage(object), PlaybackStateCompatApi21.getLastPositionUpdateTime(object), (List<CustomAction>)object3, PlaybackStateCompatApi21.getActiveQueueItemId(object), bundle);
            object3.mStateObj = object;
            return object3;
        }
        return null;
    }

    public static int toKeyCode(long l) {
        if (l == 4L) {
            return 126;
        }
        if (l == 2L) {
            return 127;
        }
        if (l == 32L) {
            return 87;
        }
        if (l == 16L) {
            return 88;
        }
        if (l == 1L) {
            return 86;
        }
        if (l == 64L) {
            return 90;
        }
        if (l == 8L) {
            return 89;
        }
        if (l == 512L) {
            return 85;
        }
        return 0;
    }

    public int describeContents() {
        return 0;
    }

    public long getActions() {
        return this.mActions;
    }

    public long getActiveQueueItemId() {
        return this.mActiveItemId;
    }

    public long getBufferedPosition() {
        return this.mBufferedPosition;
    }

    public long getCurrentPosition(Long l) {
        long l2 = this.mPosition;
        float f = this.mSpeed;
        long l3 = l != null ? l : SystemClock.elapsedRealtime() - this.mUpdateTime;
        return Math.max(0L, l2 + (long)(f * (float)l3));
    }

    public List<CustomAction> getCustomActions() {
        return this.mCustomActions;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public CharSequence getErrorMessage() {
        return this.mErrorMessage;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public long getLastPositionUpdateTime() {
        return this.mUpdateTime;
    }

    public float getPlaybackSpeed() {
        return this.mSpeed;
    }

    public Object getPlaybackState() {
        if (this.mStateObj == null && Build.VERSION.SDK_INT >= 21) {
            ArrayList<Object> arrayList = null;
            if (this.mCustomActions != null) {
                ArrayList<Object> arrayList2 = new ArrayList<Object>(this.mCustomActions.size());
                Iterator<CustomAction> iterator = this.mCustomActions.iterator();
                do {
                    arrayList = arrayList2;
                    if (!iterator.hasNext()) break;
                    arrayList2.add(iterator.next().getCustomAction());
                } while (true);
            }
            this.mStateObj = Build.VERSION.SDK_INT >= 22 ? PlaybackStateCompatApi22.newInstance(this.mState, this.mPosition, this.mBufferedPosition, this.mSpeed, this.mActions, this.mErrorMessage, this.mUpdateTime, arrayList, this.mActiveItemId, this.mExtras) : PlaybackStateCompatApi21.newInstance(this.mState, this.mPosition, this.mBufferedPosition, this.mSpeed, this.mActions, this.mErrorMessage, this.mUpdateTime, arrayList, this.mActiveItemId);
        }
        return this.mStateObj;
    }

    public long getPosition() {
        return this.mPosition;
    }

    public int getState() {
        return this.mState;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("PlaybackState {");
        stringBuilder.append("state=");
        stringBuilder.append(this.mState);
        stringBuilder.append(", position=");
        stringBuilder.append(this.mPosition);
        stringBuilder.append(", buffered position=");
        stringBuilder.append(this.mBufferedPosition);
        stringBuilder.append(", speed=");
        stringBuilder.append(this.mSpeed);
        stringBuilder.append(", updated=");
        stringBuilder.append(this.mUpdateTime);
        stringBuilder.append(", actions=");
        stringBuilder.append(this.mActions);
        stringBuilder.append(", error code=");
        stringBuilder.append(this.mErrorCode);
        stringBuilder.append(", error message=");
        stringBuilder.append(this.mErrorMessage);
        stringBuilder.append(", custom actions=");
        stringBuilder.append(this.mCustomActions);
        stringBuilder.append(", active item id=");
        stringBuilder.append(this.mActiveItemId);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeInt(this.mState);
        parcel.writeLong(this.mPosition);
        parcel.writeFloat(this.mSpeed);
        parcel.writeLong(this.mUpdateTime);
        parcel.writeLong(this.mBufferedPosition);
        parcel.writeLong(this.mActions);
        TextUtils.writeToParcel((CharSequence)this.mErrorMessage, (Parcel)parcel, (int)n);
        parcel.writeTypedList(this.mCustomActions);
        parcel.writeLong(this.mActiveItemId);
        parcel.writeBundle(this.mExtras);
        parcel.writeInt(this.mErrorCode);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Actions {
    }

    public static final class Builder {
        private long mActions;
        private long mActiveItemId = -1L;
        private long mBufferedPosition;
        private final List<CustomAction> mCustomActions = new ArrayList<CustomAction>();
        private int mErrorCode;
        private CharSequence mErrorMessage;
        private Bundle mExtras;
        private long mPosition;
        private float mRate;
        private int mState;
        private long mUpdateTime;

        public Builder() {
        }

        public Builder(PlaybackStateCompat playbackStateCompat) {
            this.mState = playbackStateCompat.mState;
            this.mPosition = playbackStateCompat.mPosition;
            this.mRate = playbackStateCompat.mSpeed;
            this.mUpdateTime = playbackStateCompat.mUpdateTime;
            this.mBufferedPosition = playbackStateCompat.mBufferedPosition;
            this.mActions = playbackStateCompat.mActions;
            this.mErrorCode = playbackStateCompat.mErrorCode;
            this.mErrorMessage = playbackStateCompat.mErrorMessage;
            if (playbackStateCompat.mCustomActions != null) {
                this.mCustomActions.addAll(playbackStateCompat.mCustomActions);
            }
            this.mActiveItemId = playbackStateCompat.mActiveItemId;
            this.mExtras = playbackStateCompat.mExtras;
        }

        public Builder addCustomAction(CustomAction customAction) {
            if (customAction != null) {
                this.mCustomActions.add(customAction);
                return this;
            }
            throw new IllegalArgumentException("You may not add a null CustomAction to PlaybackStateCompat.");
        }

        public Builder addCustomAction(String string, String string2, int n) {
            return this.addCustomAction(new CustomAction(string, string2, n, null));
        }

        public PlaybackStateCompat build() {
            return new PlaybackStateCompat(this.mState, this.mPosition, this.mBufferedPosition, this.mRate, this.mActions, this.mErrorCode, this.mErrorMessage, this.mUpdateTime, this.mCustomActions, this.mActiveItemId, this.mExtras);
        }

        public Builder setActions(long l) {
            this.mActions = l;
            return this;
        }

        public Builder setActiveQueueItemId(long l) {
            this.mActiveItemId = l;
            return this;
        }

        public Builder setBufferedPosition(long l) {
            this.mBufferedPosition = l;
            return this;
        }

        public Builder setErrorMessage(int n, CharSequence charSequence) {
            this.mErrorCode = n;
            this.mErrorMessage = charSequence;
            return this;
        }

        public Builder setErrorMessage(CharSequence charSequence) {
            this.mErrorMessage = charSequence;
            return this;
        }

        public Builder setExtras(Bundle bundle) {
            this.mExtras = bundle;
            return this;
        }

        public Builder setState(int n, long l, float f) {
            return this.setState(n, l, f, SystemClock.elapsedRealtime());
        }

        public Builder setState(int n, long l, float f, long l2) {
            this.mState = n;
            this.mPosition = l;
            this.mUpdateTime = l2;
            this.mRate = f;
            return this;
        }
    }

    public static final class CustomAction
    implements Parcelable {
        public static final Parcelable.Creator<CustomAction> CREATOR = new Parcelable.Creator<CustomAction>(){

            public CustomAction createFromParcel(Parcel parcel) {
                return new CustomAction(parcel);
            }

            public CustomAction[] newArray(int n) {
                return new CustomAction[n];
            }
        };
        private final String mAction;
        private Object mCustomActionObj;
        private final Bundle mExtras;
        private final int mIcon;
        private final CharSequence mName;

        CustomAction(Parcel parcel) {
            this.mAction = parcel.readString();
            this.mName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.mIcon = parcel.readInt();
            this.mExtras = parcel.readBundle(MediaSessionCompat.class.getClassLoader());
        }

        CustomAction(String string, CharSequence charSequence, int n, Bundle bundle) {
            this.mAction = string;
            this.mName = charSequence;
            this.mIcon = n;
            this.mExtras = bundle;
        }

        public static CustomAction fromCustomAction(Object object) {
            if (object != null && Build.VERSION.SDK_INT >= 21) {
                CustomAction customAction = new CustomAction(PlaybackStateCompatApi21.CustomAction.getAction(object), PlaybackStateCompatApi21.CustomAction.getName(object), PlaybackStateCompatApi21.CustomAction.getIcon(object), PlaybackStateCompatApi21.CustomAction.getExtras(object));
                customAction.mCustomActionObj = object;
                return customAction;
            }
            return null;
        }

        public int describeContents() {
            return 0;
        }

        public String getAction() {
            return this.mAction;
        }

        public Object getCustomAction() {
            if (this.mCustomActionObj == null && Build.VERSION.SDK_INT >= 21) {
                Object object;
                this.mCustomActionObj = object = PlaybackStateCompatApi21.CustomAction.newInstance(this.mAction, this.mName, this.mIcon, this.mExtras);
                return object;
            }
            return this.mCustomActionObj;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public int getIcon() {
            return this.mIcon;
        }

        public CharSequence getName() {
            return this.mName;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Action:mName='");
            stringBuilder.append((Object)this.mName);
            stringBuilder.append(", mIcon=");
            stringBuilder.append(this.mIcon);
            stringBuilder.append(", mExtras=");
            stringBuilder.append((Object)this.mExtras);
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeString(this.mAction);
            TextUtils.writeToParcel((CharSequence)this.mName, (Parcel)parcel, (int)n);
            parcel.writeInt(this.mIcon);
            parcel.writeBundle(this.mExtras);
        }

        public static final class Builder {
            private final String mAction;
            private Bundle mExtras;
            private final int mIcon;
            private final CharSequence mName;

            public Builder(String string, CharSequence charSequence, int n) {
                if (!TextUtils.isEmpty((CharSequence)string)) {
                    if (!TextUtils.isEmpty((CharSequence)charSequence)) {
                        if (n != 0) {
                            this.mAction = string;
                            this.mName = charSequence;
                            this.mIcon = n;
                            return;
                        }
                        throw new IllegalArgumentException("You must specify an icon resource id to build a CustomAction.");
                    }
                    throw new IllegalArgumentException("You must specify a name to build a CustomAction.");
                }
                throw new IllegalArgumentException("You must specify an action to build a CustomAction.");
            }

            public CustomAction build() {
                return new CustomAction(this.mAction, this.mName, this.mIcon, this.mExtras);
            }

            public Builder setExtras(Bundle bundle) {
                this.mExtras = bundle;
                return this;
            }
        }

    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ErrorCode {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface MediaKeyAction {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface RepeatMode {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ShuffleMode {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface State {
    }

}

