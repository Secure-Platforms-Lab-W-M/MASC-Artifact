// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import java.util.Collections;
import android.os.SystemClock;
import java.text.NumberFormat;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Bitmap$Config;
import android.content.res.Resources;
import android.support.annotation.RequiresApi;
import android.app.Notification$Builder;
import android.text.SpannableStringBuilder;
import android.support.v4.text.BidiFormatter;
import android.content.res.ColorStateList;
import android.text.style.TextAppearanceSpan;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.app.Notification$Style;
import android.app.Notification$DecoratedCustomViewStyle;
import android.support.compat.R;
import java.util.List;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.content.Context;
import android.widget.RemoteViews;
import android.graphics.Bitmap;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Arrays;
import android.app.PendingIntent;
import android.support.annotation.RestrictTo;
import android.os.Parcelable;
import android.os.Bundle;
import android.app.Notification;
import java.util.Iterator;
import java.util.ArrayList;
import android.os.Build$VERSION;
import android.support.annotation.ColorInt;

public class NotificationCompat
{
    public static final int BADGE_ICON_LARGE = 2;
    public static final int BADGE_ICON_NONE = 0;
    public static final int BADGE_ICON_SMALL = 1;
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_REMINDER = "reminder";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    @ColorInt
    public static final int COLOR_DEFAULT = 0;
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_LIGHTS = 4;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    public static final String EXTRA_AUDIO_CONTENTS_URI = "android.audioContents";
    public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";
    public static final String EXTRA_BIG_TEXT = "android.bigText";
    public static final String EXTRA_COMPACT_ACTIONS = "android.compactActions";
    public static final String EXTRA_CONVERSATION_TITLE = "android.conversationTitle";
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
    public static final String EXTRA_MESSAGES = "android.messages";
    public static final String EXTRA_PEOPLE = "android.people";
    public static final String EXTRA_PICTURE = "android.picture";
    public static final String EXTRA_PROGRESS = "android.progress";
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
    public static final String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";
    public static final String EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName";
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
    public static final String EXTRA_SHOW_WHEN = "android.showWhen";
    public static final String EXTRA_SMALL_ICON = "android.icon";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEMPLATE = "android.template";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    public static final int FLAG_GROUP_SUMMARY = 512;
    @Deprecated
    public static final int FLAG_HIGH_PRIORITY = 128;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_LOCAL_ONLY = 256;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    public static final int FLAG_SHOW_LIGHTS = 1;
    public static final int GROUP_ALERT_ALL = 0;
    public static final int GROUP_ALERT_CHILDREN = 2;
    public static final int GROUP_ALERT_SUMMARY = 1;
    static final NotificationCompatImpl IMPL;
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;
    public static final int STREAM_DEFAULT = -1;
    public static final int VISIBILITY_PRIVATE = 0;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_SECRET = -1;
    
    static {
        if (Build$VERSION.SDK_INT >= 26) {
            IMPL = (NotificationCompatImpl)new NotificationCompatApi26Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = (NotificationCompatImpl)new NotificationCompatApi24Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (NotificationCompatImpl)new NotificationCompatApi21Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 20) {
            IMPL = (NotificationCompatImpl)new NotificationCompatApi20Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (NotificationCompatImpl)new NotificationCompatApi19Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (NotificationCompatImpl)new NotificationCompatApi16Impl();
            return;
        }
        IMPL = (NotificationCompatImpl)new NotificationCompatBaseImpl();
    }
    
    static void addActionsToBuilder(final NotificationBuilderWithActions notificationBuilderWithActions, final ArrayList<Action> list) {
        final Iterator<Action> iterator = list.iterator();
        while (iterator.hasNext()) {
            notificationBuilderWithActions.addAction(iterator.next());
        }
    }
    
    public static Action getAction(final Notification notification, final int n) {
        return NotificationCompat.IMPL.getAction(notification, n);
    }
    
    public static int getActionCount(final Notification notification) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        int length = 0;
        if (sdk_INT >= 19) {
            if (notification.actions != null) {
                length = notification.actions.length;
            }
            return length;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getActionCount(notification);
        }
        return 0;
    }
    
    public static int getBadgeIconType(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 26) {
            return notification.getBadgeIconType();
        }
        return 0;
    }
    
    public static String getCategory(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 21) {
            return notification.category;
        }
        return null;
    }
    
    public static String getChannelId(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 26) {
            return notification.getChannelId();
        }
        return null;
    }
    
    public static Bundle getExtras(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 19) {
            return notification.extras;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification);
        }
        return null;
    }
    
    public static String getGroup(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 20) {
            return notification.getGroup();
        }
        if (Build$VERSION.SDK_INT >= 19) {
            return notification.extras.getString("android.support.groupKey");
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString("android.support.groupKey");
        }
        return null;
    }
    
    public static int getGroupAlertBehavior(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 26) {
            return notification.getGroupAlertBehavior();
        }
        return 0;
    }
    
    public static boolean getLocalOnly(final Notification notification) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = false;
        if (sdk_INT >= 20) {
            if ((notification.flags & 0x100) != 0x0) {
                b = true;
            }
            return b;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean("android.support.localOnly");
        }
        return Build$VERSION.SDK_INT >= 16 && NotificationCompatJellybean.getExtras(notification).getBoolean("android.support.localOnly");
    }
    
    static Notification[] getNotificationArrayFromBundle(final Bundle bundle, final String s) {
        final Parcelable[] parcelableArray = bundle.getParcelableArray(s);
        if (!(parcelableArray instanceof Notification[]) && parcelableArray != null) {
            final Notification[] array = new Notification[parcelableArray.length];
            for (int i = 0; i < parcelableArray.length; ++i) {
                array[i] = (Notification)parcelableArray[i];
            }
            bundle.putParcelableArray(s, (Parcelable[])array);
            return array;
        }
        return (Notification[])parcelableArray;
    }
    
    public static String getShortcutId(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 26) {
            return notification.getShortcutId();
        }
        return null;
    }
    
    public static String getSortKey(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 20) {
            return notification.getSortKey();
        }
        if (Build$VERSION.SDK_INT >= 19) {
            return notification.extras.getString("android.support.sortKey");
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString("android.support.sortKey");
        }
        return null;
    }
    
    public static long getTimeoutAfter(final Notification notification) {
        if (Build$VERSION.SDK_INT >= 26) {
            return notification.getTimeoutAfter();
        }
        return 0L;
    }
    
    public static boolean isGroupSummary(final Notification notification) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = false;
        if (sdk_INT >= 20) {
            if ((notification.flags & 0x200) != 0x0) {
                b = true;
            }
            return b;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean("android.support.isGroupSummary");
        }
        return Build$VERSION.SDK_INT >= 16 && NotificationCompatJellybean.getExtras(notification).getBoolean("android.support.isGroupSummary");
    }
    
    public static class Action extends NotificationCompatBase.Action
    {
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public static final Factory FACTORY;
        public PendingIntent actionIntent;
        public int icon;
        private boolean mAllowGeneratedReplies;
        private final RemoteInput[] mDataOnlyRemoteInputs;
        final Bundle mExtras;
        private final RemoteInput[] mRemoteInputs;
        public CharSequence title;
        
        static {
            FACTORY = new Factory() {
                @Override
                public NotificationCompatBase.Action build(final int n, final CharSequence charSequence, final PendingIntent pendingIntent, final Bundle bundle, final RemoteInputCompatBase.RemoteInput[] array, final RemoteInputCompatBase.RemoteInput[] array2, final boolean b) {
                    return new NotificationCompat.Action(n, charSequence, pendingIntent, bundle, (RemoteInput[])array, (RemoteInput[])array2, b);
                }
                
                public NotificationCompat.Action[] newArray(final int n) {
                    return new NotificationCompat.Action[n];
                }
            };
        }
        
        public Action(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
            this(n, charSequence, pendingIntent, new Bundle(), null, null, true);
        }
        
        Action(final int icon, final CharSequence charSequence, final PendingIntent actionIntent, Bundle mExtras, final RemoteInput[] mRemoteInputs, final RemoteInput[] mDataOnlyRemoteInputs, final boolean mAllowGeneratedReplies) {
            this.icon = icon;
            this.title = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            this.actionIntent = actionIntent;
            if (mExtras == null) {
                mExtras = new Bundle();
            }
            this.mExtras = mExtras;
            this.mRemoteInputs = mRemoteInputs;
            this.mDataOnlyRemoteInputs = mDataOnlyRemoteInputs;
            this.mAllowGeneratedReplies = mAllowGeneratedReplies;
        }
        
        @Override
        public PendingIntent getActionIntent() {
            return this.actionIntent;
        }
        
        @Override
        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }
        
        public RemoteInput[] getDataOnlyRemoteInputs() {
            return this.mDataOnlyRemoteInputs;
        }
        
        @Override
        public Bundle getExtras() {
            return this.mExtras;
        }
        
        @Override
        public int getIcon() {
            return this.icon;
        }
        
        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }
        
        @Override
        public CharSequence getTitle() {
            return this.title;
        }
        
        public static final class Builder
        {
            private boolean mAllowGeneratedReplies;
            private final Bundle mExtras;
            private final int mIcon;
            private final PendingIntent mIntent;
            private ArrayList<RemoteInput> mRemoteInputs;
            private final CharSequence mTitle;
            
            public Builder(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
                this(n, charSequence, pendingIntent, new Bundle(), null, true);
            }
            
            private Builder(final int mIcon, final CharSequence charSequence, final PendingIntent mIntent, final Bundle mExtras, final RemoteInput[] array, final boolean mAllowGeneratedReplies) {
                this.mAllowGeneratedReplies = true;
                this.mIcon = mIcon;
                this.mTitle = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
                this.mIntent = mIntent;
                this.mExtras = mExtras;
                ArrayList<RemoteInput> mRemoteInputs;
                if (array == null) {
                    mRemoteInputs = null;
                }
                else {
                    mRemoteInputs = new ArrayList<RemoteInput>(Arrays.asList(array));
                }
                this.mRemoteInputs = mRemoteInputs;
                this.mAllowGeneratedReplies = mAllowGeneratedReplies;
            }
            
            public Builder(final Action action) {
                this(action.icon, action.title, action.actionIntent, new Bundle(action.mExtras), action.getRemoteInputs(), action.getAllowGeneratedReplies());
            }
            
            public Builder addExtras(final Bundle bundle) {
                if (bundle != null) {
                    this.mExtras.putAll(bundle);
                }
                return this;
            }
            
            public Builder addRemoteInput(final RemoteInput remoteInput) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList<RemoteInput>();
                }
                this.mRemoteInputs.add(remoteInput);
                return this;
            }
            
            public Action build() {
                final ArrayList<RemoteInput> list = new ArrayList<RemoteInput>();
                final ArrayList<RemoteInput> list2 = new ArrayList<RemoteInput>();
                final ArrayList<RemoteInput> mRemoteInputs = this.mRemoteInputs;
                if (mRemoteInputs != null) {
                    for (final RemoteInput remoteInput : mRemoteInputs) {
                        if (remoteInput.isDataOnly()) {
                            list.add(remoteInput);
                        }
                        else {
                            list2.add(remoteInput);
                        }
                    }
                }
                RemoteInput[] array;
                if (list.isEmpty()) {
                    array = null;
                }
                else {
                    array = list.toArray(new RemoteInput[list.size()]);
                }
                RemoteInput[] array2;
                if (list2.isEmpty()) {
                    array2 = null;
                }
                else {
                    array2 = list2.toArray(new RemoteInput[list2.size()]);
                }
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, array2, array, this.mAllowGeneratedReplies);
            }
            
            public Builder extend(final Extender extender) {
                extender.extend(this);
                return this;
            }
            
            public Bundle getExtras() {
                return this.mExtras;
            }
            
            public Builder setAllowGeneratedReplies(final boolean mAllowGeneratedReplies) {
                this.mAllowGeneratedReplies = mAllowGeneratedReplies;
                return this;
            }
        }
        
        public interface Extender
        {
            Builder extend(final Builder p0);
        }
        
        public static final class WearableExtender implements Extender
        {
            private static final int DEFAULT_FLAGS = 1;
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
            private static final int FLAG_AVAILABLE_OFFLINE = 1;
            private static final int FLAG_HINT_DISPLAY_INLINE = 4;
            private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 2;
            private static final String KEY_CANCEL_LABEL = "cancelLabel";
            private static final String KEY_CONFIRM_LABEL = "confirmLabel";
            private static final String KEY_FLAGS = "flags";
            private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
            private CharSequence mCancelLabel;
            private CharSequence mConfirmLabel;
            private int mFlags;
            private CharSequence mInProgressLabel;
            
            public WearableExtender() {
                this.mFlags = 1;
            }
            
            public WearableExtender(final Action action) {
                this.mFlags = 1;
                final Bundle bundle = action.getExtras().getBundle("android.wearable.EXTENSIONS");
                if (bundle != null) {
                    this.mFlags = bundle.getInt("flags", 1);
                    this.mInProgressLabel = bundle.getCharSequence("inProgressLabel");
                    this.mConfirmLabel = bundle.getCharSequence("confirmLabel");
                    this.mCancelLabel = bundle.getCharSequence("cancelLabel");
                }
            }
            
            private void setFlag(final int n, final boolean b) {
                if (b) {
                    this.mFlags |= n;
                    return;
                }
                this.mFlags &= ~n;
            }
            
            public WearableExtender clone() {
                final WearableExtender wearableExtender = new WearableExtender();
                wearableExtender.mFlags = this.mFlags;
                wearableExtender.mInProgressLabel = this.mInProgressLabel;
                wearableExtender.mConfirmLabel = this.mConfirmLabel;
                wearableExtender.mCancelLabel = this.mCancelLabel;
                return wearableExtender;
            }
            
            @Override
            public Builder extend(final Builder builder) {
                final Bundle bundle = new Bundle();
                final int mFlags = this.mFlags;
                if (mFlags != 1) {
                    bundle.putInt("flags", mFlags);
                }
                final CharSequence mInProgressLabel = this.mInProgressLabel;
                if (mInProgressLabel != null) {
                    bundle.putCharSequence("inProgressLabel", mInProgressLabel);
                }
                final CharSequence mConfirmLabel = this.mConfirmLabel;
                if (mConfirmLabel != null) {
                    bundle.putCharSequence("confirmLabel", mConfirmLabel);
                }
                final CharSequence mCancelLabel = this.mCancelLabel;
                if (mCancelLabel != null) {
                    bundle.putCharSequence("cancelLabel", mCancelLabel);
                }
                builder.getExtras().putBundle("android.wearable.EXTENSIONS", bundle);
                return builder;
            }
            
            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }
            
            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }
            
            public boolean getHintDisplayActionInline() {
                return (this.mFlags & 0x4) != 0x0;
            }
            
            public boolean getHintLaunchesActivity() {
                return (this.mFlags & 0x2) != 0x0;
            }
            
            public CharSequence getInProgressLabel() {
                return this.mInProgressLabel;
            }
            
            public boolean isAvailableOffline() {
                return (this.mFlags & 0x1) != 0x0;
            }
            
            public WearableExtender setAvailableOffline(final boolean b) {
                this.setFlag(1, b);
                return this;
            }
            
            public WearableExtender setCancelLabel(final CharSequence mCancelLabel) {
                this.mCancelLabel = mCancelLabel;
                return this;
            }
            
            public WearableExtender setConfirmLabel(final CharSequence mConfirmLabel) {
                this.mConfirmLabel = mConfirmLabel;
                return this;
            }
            
            public WearableExtender setHintDisplayActionInline(final boolean b) {
                this.setFlag(4, b);
                return this;
            }
            
            public WearableExtender setHintLaunchesActivity(final boolean b) {
                this.setFlag(2, b);
                return this;
            }
            
            public WearableExtender setInProgressLabel(final CharSequence mInProgressLabel) {
                this.mInProgressLabel = mInProgressLabel;
                return this;
            }
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface BadgeIconType {
    }
    
    public static class BigPictureStyle extends Style
    {
        private Bitmap mBigLargeIcon;
        private boolean mBigLargeIconSet;
        private Bitmap mPicture;
        
        public BigPictureStyle() {
        }
        
        public BigPictureStyle(final NotificationCompat.Builder builder) {
            ((Style)this).setBuilder(builder);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                NotificationCompatJellybean.addBigPictureStyle(notificationBuilderWithBuilderAccessor, this.mBigContentTitle, this.mSummaryTextSet, this.mSummaryText, this.mPicture, this.mBigLargeIcon, this.mBigLargeIconSet);
            }
        }
        
        public BigPictureStyle bigLargeIcon(final Bitmap mBigLargeIcon) {
            this.mBigLargeIcon = mBigLargeIcon;
            this.mBigLargeIconSet = true;
            return this;
        }
        
        public BigPictureStyle bigPicture(final Bitmap mPicture) {
            this.mPicture = mPicture;
            return this;
        }
        
        public BigPictureStyle setBigContentTitle(final CharSequence charSequence) {
            this.mBigContentTitle = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            return this;
        }
        
        public BigPictureStyle setSummaryText(final CharSequence charSequence) {
            this.mSummaryText = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            this.mSummaryTextSet = true;
            return this;
        }
    }
    
    public static class BigTextStyle extends Style
    {
        private CharSequence mBigText;
        
        public BigTextStyle() {
        }
        
        public BigTextStyle(final NotificationCompat.Builder builder) {
            ((Style)this).setBuilder(builder);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                NotificationCompatJellybean.addBigTextStyle(notificationBuilderWithBuilderAccessor, this.mBigContentTitle, this.mSummaryTextSet, this.mSummaryText, this.mBigText);
            }
        }
        
        public BigTextStyle bigText(final CharSequence charSequence) {
            this.mBigText = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            return this;
        }
        
        public BigTextStyle setBigContentTitle(final CharSequence charSequence) {
            this.mBigContentTitle = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            return this;
        }
        
        public BigTextStyle setSummaryText(final CharSequence charSequence) {
            this.mSummaryText = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            this.mSummaryTextSet = true;
            return this;
        }
    }
    
    public static class Builder
    {
        private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public ArrayList<Action> mActions;
        int mBadgeIcon;
        RemoteViews mBigContentView;
        String mCategory;
        String mChannelId;
        int mColor;
        boolean mColorized;
        boolean mColorizedSet;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public CharSequence mContentInfo;
        PendingIntent mContentIntent;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public CharSequence mContentText;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public CharSequence mContentTitle;
        RemoteViews mContentView;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Context mContext;
        Bundle mExtras;
        PendingIntent mFullScreenIntent;
        private int mGroupAlertBehavior;
        String mGroupKey;
        boolean mGroupSummary;
        RemoteViews mHeadsUpContentView;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Bitmap mLargeIcon;
        boolean mLocalOnly;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Notification mNotification;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public int mNumber;
        public ArrayList<String> mPeople;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public CharSequence[] mRemoteInputHistory;
        String mShortcutId;
        boolean mShowWhen;
        String mSortKey;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Style mStyle;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public CharSequence mSubText;
        RemoteViews mTickerView;
        long mTimeout;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public boolean mUseChronometer;
        int mVisibility;
        
        @Deprecated
        public Builder(final Context context) {
            this(context, null);
        }
        
        public Builder(@NonNull final Context mContext, @NonNull final String mChannelId) {
            this.mShowWhen = true;
            this.mActions = new ArrayList<Action>();
            this.mLocalOnly = false;
            this.mColor = 0;
            this.mVisibility = 0;
            this.mBadgeIcon = 0;
            this.mGroupAlertBehavior = 0;
            this.mNotification = new Notification();
            this.mContext = mContext;
            this.mChannelId = mChannelId;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
            this.mPeople = new ArrayList<String>();
        }
        
        protected static CharSequence limitCharSequenceLength(final CharSequence charSequence) {
            if (charSequence == null) {
                return charSequence;
            }
            CharSequence subSequence = charSequence;
            if (charSequence.length() > 5120) {
                subSequence = charSequence.subSequence(0, 5120);
            }
            return subSequence;
        }
        
        private void setFlag(final int n, final boolean b) {
            if (b) {
                final Notification mNotification = this.mNotification;
                mNotification.flags |= n;
                return;
            }
            final Notification mNotification2 = this.mNotification;
            mNotification2.flags &= ~n;
        }
        
        public Builder addAction(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
            this.mActions.add(new Action(n, charSequence, pendingIntent));
            return this;
        }
        
        public Builder addAction(final Action action) {
            this.mActions.add(action);
            return this;
        }
        
        public Builder addExtras(final Bundle bundle) {
            if (bundle != null) {
                final Bundle mExtras = this.mExtras;
                if (mExtras == null) {
                    this.mExtras = new Bundle(bundle);
                    return this;
                }
                mExtras.putAll(bundle);
            }
            return this;
        }
        
        public Builder addPerson(final String s) {
            this.mPeople.add(s);
            return this;
        }
        
        public Notification build() {
            return NotificationCompat.IMPL.build(this, this.getExtender());
        }
        
        public Builder extend(final NotificationCompat.Extender extender) {
            extender.extend(this);
            return this;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public RemoteViews getBigContentView() {
            return this.mBigContentView;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public int getColor() {
            return this.mColor;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public RemoteViews getContentView() {
            return this.mContentView;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        protected BuilderExtender getExtender() {
            return new BuilderExtender();
        }
        
        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public RemoteViews getHeadsUpContentView() {
            return this.mHeadsUpContentView;
        }
        
        @Deprecated
        public Notification getNotification() {
            return this.build();
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public int getPriority() {
            return this.mPriority;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public long getWhenIfShowing() {
            if (this.mShowWhen) {
                return this.mNotification.when;
            }
            return 0L;
        }
        
        public Builder setAutoCancel(final boolean b) {
            this.setFlag(16, b);
            return this;
        }
        
        public Builder setBadgeIconType(final int mBadgeIcon) {
            this.mBadgeIcon = mBadgeIcon;
            return this;
        }
        
        public Builder setCategory(final String mCategory) {
            this.mCategory = mCategory;
            return this;
        }
        
        public Builder setChannelId(@NonNull final String mChannelId) {
            this.mChannelId = mChannelId;
            return this;
        }
        
        public Builder setColor(@ColorInt final int mColor) {
            this.mColor = mColor;
            return this;
        }
        
        public Builder setColorized(final boolean mColorized) {
            this.mColorized = mColorized;
            this.mColorizedSet = true;
            return this;
        }
        
        public Builder setContent(final RemoteViews contentView) {
            this.mNotification.contentView = contentView;
            return this;
        }
        
        public Builder setContentInfo(final CharSequence charSequence) {
            this.mContentInfo = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setContentIntent(final PendingIntent mContentIntent) {
            this.mContentIntent = mContentIntent;
            return this;
        }
        
        public Builder setContentText(final CharSequence charSequence) {
            this.mContentText = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setContentTitle(final CharSequence charSequence) {
            this.mContentTitle = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setCustomBigContentView(final RemoteViews mBigContentView) {
            this.mBigContentView = mBigContentView;
            return this;
        }
        
        public Builder setCustomContentView(final RemoteViews mContentView) {
            this.mContentView = mContentView;
            return this;
        }
        
        public Builder setCustomHeadsUpContentView(final RemoteViews mHeadsUpContentView) {
            this.mHeadsUpContentView = mHeadsUpContentView;
            return this;
        }
        
        public Builder setDefaults(final int defaults) {
            final Notification mNotification = this.mNotification;
            mNotification.defaults = defaults;
            if ((defaults & 0x4) != 0x0) {
                mNotification.flags |= 0x1;
            }
            return this;
        }
        
        public Builder setDeleteIntent(final PendingIntent deleteIntent) {
            this.mNotification.deleteIntent = deleteIntent;
            return this;
        }
        
        public Builder setExtras(final Bundle mExtras) {
            this.mExtras = mExtras;
            return this;
        }
        
        public Builder setFullScreenIntent(final PendingIntent mFullScreenIntent, final boolean b) {
            this.mFullScreenIntent = mFullScreenIntent;
            this.setFlag(128, b);
            return this;
        }
        
        public Builder setGroup(final String mGroupKey) {
            this.mGroupKey = mGroupKey;
            return this;
        }
        
        public Builder setGroupAlertBehavior(final int mGroupAlertBehavior) {
            this.mGroupAlertBehavior = mGroupAlertBehavior;
            return this;
        }
        
        public Builder setGroupSummary(final boolean mGroupSummary) {
            this.mGroupSummary = mGroupSummary;
            return this;
        }
        
        public Builder setLargeIcon(final Bitmap mLargeIcon) {
            this.mLargeIcon = mLargeIcon;
            return this;
        }
        
        public Builder setLights(@ColorInt int ledOnMS, int ledOnMS2, int flags) {
            final Notification mNotification = this.mNotification;
            mNotification.ledARGB = ledOnMS;
            mNotification.ledOnMS = ledOnMS2;
            mNotification.ledOffMS = flags;
            ledOnMS = mNotification.ledOnMS;
            ledOnMS2 = 1;
            if (ledOnMS != 0 && this.mNotification.ledOffMS != 0) {
                ledOnMS = 1;
            }
            else {
                ledOnMS = 0;
            }
            final Notification mNotification2 = this.mNotification;
            flags = mNotification2.flags;
            if (ledOnMS != 0) {
                ledOnMS = ledOnMS2;
            }
            else {
                ledOnMS = 0;
            }
            mNotification2.flags = (ledOnMS | (flags & 0xFFFFFFFE));
            return this;
        }
        
        public Builder setLocalOnly(final boolean mLocalOnly) {
            this.mLocalOnly = mLocalOnly;
            return this;
        }
        
        public Builder setNumber(final int mNumber) {
            this.mNumber = mNumber;
            return this;
        }
        
        public Builder setOngoing(final boolean b) {
            this.setFlag(2, b);
            return this;
        }
        
        public Builder setOnlyAlertOnce(final boolean b) {
            this.setFlag(8, b);
            return this;
        }
        
        public Builder setPriority(final int mPriority) {
            this.mPriority = mPriority;
            return this;
        }
        
        public Builder setProgress(final int mProgressMax, final int mProgress, final boolean mProgressIndeterminate) {
            this.mProgressMax = mProgressMax;
            this.mProgress = mProgress;
            this.mProgressIndeterminate = mProgressIndeterminate;
            return this;
        }
        
        public Builder setPublicVersion(final Notification mPublicVersion) {
            this.mPublicVersion = mPublicVersion;
            return this;
        }
        
        public Builder setRemoteInputHistory(final CharSequence[] mRemoteInputHistory) {
            this.mRemoteInputHistory = mRemoteInputHistory;
            return this;
        }
        
        public Builder setShortcutId(final String mShortcutId) {
            this.mShortcutId = mShortcutId;
            return this;
        }
        
        public Builder setShowWhen(final boolean mShowWhen) {
            this.mShowWhen = mShowWhen;
            return this;
        }
        
        public Builder setSmallIcon(final int icon) {
            this.mNotification.icon = icon;
            return this;
        }
        
        public Builder setSmallIcon(final int icon, final int iconLevel) {
            final Notification mNotification = this.mNotification;
            mNotification.icon = icon;
            mNotification.iconLevel = iconLevel;
            return this;
        }
        
        public Builder setSortKey(final String mSortKey) {
            this.mSortKey = mSortKey;
            return this;
        }
        
        public Builder setSound(final Uri sound) {
            final Notification mNotification = this.mNotification;
            mNotification.sound = sound;
            mNotification.audioStreamType = -1;
            return this;
        }
        
        public Builder setSound(final Uri sound, final int audioStreamType) {
            final Notification mNotification = this.mNotification;
            mNotification.sound = sound;
            mNotification.audioStreamType = audioStreamType;
            return this;
        }
        
        public Builder setStyle(Style mStyle) {
            if (this.mStyle != mStyle) {
                this.mStyle = mStyle;
                mStyle = this.mStyle;
                if (mStyle != null) {
                    mStyle.setBuilder(this);
                }
            }
            return this;
        }
        
        public Builder setSubText(final CharSequence charSequence) {
            this.mSubText = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setTicker(final CharSequence charSequence) {
            this.mNotification.tickerText = limitCharSequenceLength(charSequence);
            return this;
        }
        
        public Builder setTicker(final CharSequence charSequence, final RemoteViews mTickerView) {
            this.mNotification.tickerText = limitCharSequenceLength(charSequence);
            this.mTickerView = mTickerView;
            return this;
        }
        
        public Builder setTimeoutAfter(final long mTimeout) {
            this.mTimeout = mTimeout;
            return this;
        }
        
        public Builder setUsesChronometer(final boolean mUseChronometer) {
            this.mUseChronometer = mUseChronometer;
            return this;
        }
        
        public Builder setVibrate(final long[] vibrate) {
            this.mNotification.vibrate = vibrate;
            return this;
        }
        
        public Builder setVisibility(final int mVisibility) {
            this.mVisibility = mVisibility;
            return this;
        }
        
        public Builder setWhen(final long when) {
            this.mNotification.when = when;
            return this;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected static class BuilderExtender
    {
        public Notification build(final NotificationCompat.Builder builder, final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            RemoteViews contentView;
            if (builder.mStyle != null) {
                contentView = builder.mStyle.makeContentView(notificationBuilderWithBuilderAccessor);
            }
            else {
                contentView = null;
            }
            final Notification build = notificationBuilderWithBuilderAccessor.build();
            if (contentView != null) {
                build.contentView = contentView;
            }
            else if (builder.mContentView != null) {
                build.contentView = builder.mContentView;
            }
            if (Build$VERSION.SDK_INT >= 16 && builder.mStyle != null) {
                final RemoteViews bigContentView = builder.mStyle.makeBigContentView(notificationBuilderWithBuilderAccessor);
                if (bigContentView != null) {
                    build.bigContentView = bigContentView;
                }
            }
            if (Build$VERSION.SDK_INT >= 21 && builder.mStyle != null) {
                final RemoteViews headsUpContentView = builder.mStyle.makeHeadsUpContentView(notificationBuilderWithBuilderAccessor);
                if (headsUpContentView != null) {
                    build.headsUpContentView = headsUpContentView;
                }
            }
            return build;
        }
    }
    
    public static final class CarExtender implements NotificationCompat.Extender
    {
        private static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_COLOR = "app_color";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String TAG = "CarExtender";
        private int mColor;
        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;
        
        public CarExtender() {
            this.mColor = 0;
        }
        
        public CarExtender(final Notification notification) {
            this.mColor = 0;
            if (Build$VERSION.SDK_INT < 21) {
                return;
            }
            Bundle bundle;
            if (NotificationCompat.getExtras(notification) == null) {
                bundle = null;
            }
            else {
                bundle = NotificationCompat.getExtras(notification).getBundle("android.car.EXTENSIONS");
            }
            if (bundle != null) {
                this.mLargeIcon = (Bitmap)bundle.getParcelable("large_icon");
                this.mColor = bundle.getInt("app_color", 0);
                this.mUnreadConversation = (UnreadConversation)NotificationCompat.IMPL.getUnreadConversationFromBundle(bundle.getBundle("car_conversation"), UnreadConversation.FACTORY, RemoteInput.FACTORY);
            }
        }
        
        @Override
        public NotificationCompat.Builder extend(final NotificationCompat.Builder builder) {
            if (Build$VERSION.SDK_INT < 21) {
                return builder;
            }
            final Bundle bundle = new Bundle();
            final Bitmap mLargeIcon = this.mLargeIcon;
            if (mLargeIcon != null) {
                bundle.putParcelable("large_icon", (Parcelable)mLargeIcon);
            }
            final int mColor = this.mColor;
            if (mColor != 0) {
                bundle.putInt("app_color", mColor);
            }
            if (this.mUnreadConversation != null) {
                bundle.putBundle("car_conversation", NotificationCompat.IMPL.getBundleForUnreadConversation(this.mUnreadConversation));
            }
            builder.getExtras().putBundle("android.car.EXTENSIONS", bundle);
            return builder;
        }
        
        @ColorInt
        public int getColor() {
            return this.mColor;
        }
        
        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }
        
        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }
        
        public CarExtender setColor(@ColorInt final int mColor) {
            this.mColor = mColor;
            return this;
        }
        
        public CarExtender setLargeIcon(final Bitmap mLargeIcon) {
            this.mLargeIcon = mLargeIcon;
            return this;
        }
        
        public CarExtender setUnreadConversation(final UnreadConversation mUnreadConversation) {
            this.mUnreadConversation = mUnreadConversation;
            return this;
        }
        
        public static class UnreadConversation extends NotificationCompatBase.UnreadConversation
        {
            static final Factory FACTORY;
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;
            
            static {
                FACTORY = new Factory() {
                    public CarExtender.UnreadConversation build(final String[] array, final RemoteInputCompatBase.RemoteInput remoteInput, final PendingIntent pendingIntent, final PendingIntent pendingIntent2, final String[] array2, final long n) {
                        return new CarExtender.UnreadConversation(array, (RemoteInput)remoteInput, pendingIntent, pendingIntent2, array2, n);
                    }
                };
            }
            
            UnreadConversation(final String[] mMessages, final RemoteInput mRemoteInput, final PendingIntent mReplyPendingIntent, final PendingIntent mReadPendingIntent, final String[] mParticipants, final long mLatestTimestamp) {
                this.mMessages = mMessages;
                this.mRemoteInput = mRemoteInput;
                this.mReadPendingIntent = mReadPendingIntent;
                this.mReplyPendingIntent = mReplyPendingIntent;
                this.mParticipants = mParticipants;
                this.mLatestTimestamp = mLatestTimestamp;
            }
            
            public long getLatestTimestamp() {
                return this.mLatestTimestamp;
            }
            
            public String[] getMessages() {
                return this.mMessages;
            }
            
            public String getParticipant() {
                final String[] mParticipants = this.mParticipants;
                if (mParticipants.length > 0) {
                    return mParticipants[0];
                }
                return null;
            }
            
            public String[] getParticipants() {
                return this.mParticipants;
            }
            
            public PendingIntent getReadPendingIntent() {
                return this.mReadPendingIntent;
            }
            
            public RemoteInput getRemoteInput() {
                return this.mRemoteInput;
            }
            
            public PendingIntent getReplyPendingIntent() {
                return this.mReplyPendingIntent;
            }
            
            public static class Builder
            {
                private long mLatestTimestamp;
                private final List<String> mMessages;
                private final String mParticipant;
                private PendingIntent mReadPendingIntent;
                private RemoteInput mRemoteInput;
                private PendingIntent mReplyPendingIntent;
                
                public Builder(final String mParticipant) {
                    this.mMessages = new ArrayList<String>();
                    this.mParticipant = mParticipant;
                }
                
                public Builder addMessage(final String s) {
                    this.mMessages.add(s);
                    return this;
                }
                
                public UnreadConversation build() {
                    final List<String> mMessages = this.mMessages;
                    return new UnreadConversation(mMessages.toArray(new String[mMessages.size()]), this.mRemoteInput, this.mReplyPendingIntent, this.mReadPendingIntent, new String[] { this.mParticipant }, this.mLatestTimestamp);
                }
                
                public Builder setLatestTimestamp(final long mLatestTimestamp) {
                    this.mLatestTimestamp = mLatestTimestamp;
                    return this;
                }
                
                public Builder setReadPendingIntent(final PendingIntent mReadPendingIntent) {
                    this.mReadPendingIntent = mReadPendingIntent;
                    return this;
                }
                
                public Builder setReplyAction(final PendingIntent mReplyPendingIntent, final RemoteInput mRemoteInput) {
                    this.mRemoteInput = mRemoteInput;
                    this.mReplyPendingIntent = mReplyPendingIntent;
                    return this;
                }
            }
        }
    }
    
    public static class DecoratedCustomViewStyle extends Style
    {
        private static final int MAX_ACTION_BUTTONS = 3;
        
        private RemoteViews createRemoteViews(final RemoteViews remoteViews, final boolean b) {
            final int notification_template_custom_big = R.layout.notification_template_custom_big;
            final int n = 0;
            final RemoteViews applyStandardTemplate = ((Style)this).applyStandardTemplate(true, notification_template_custom_big, false);
            applyStandardTemplate.removeAllViews(R.id.actions);
            boolean b3;
            final boolean b2 = b3 = false;
            if (b) {
                b3 = b2;
                if (this.mBuilder.mActions != null) {
                    final int min = Math.min(this.mBuilder.mActions.size(), 3);
                    b3 = b2;
                    if (min > 0) {
                        final boolean b4 = true;
                        int n2 = 0;
                        while (true) {
                            b3 = b4;
                            if (n2 >= min) {
                                break;
                            }
                            applyStandardTemplate.addView(R.id.actions, this.generateActionButton(this.mBuilder.mActions.get(n2)));
                            ++n2;
                        }
                    }
                }
            }
            int n3;
            if (b3) {
                n3 = n;
            }
            else {
                n3 = 8;
            }
            applyStandardTemplate.setViewVisibility(R.id.actions, n3);
            applyStandardTemplate.setViewVisibility(R.id.action_divider, n3);
            ((Style)this).buildIntoRemoteViews(applyStandardTemplate, remoteViews);
            return applyStandardTemplate;
        }
        
        private RemoteViews generateActionButton(final Action action) {
            final boolean b = action.actionIntent == null;
            final String packageName = this.mBuilder.mContext.getPackageName();
            int n;
            if (b) {
                n = R.layout.notification_action_tombstone;
            }
            else {
                n = R.layout.notification_action;
            }
            final RemoteViews remoteViews = new RemoteViews(packageName, n);
            remoteViews.setImageViewBitmap(R.id.action_image, ((Style)this).createColoredBitmap(action.getIcon(), this.mBuilder.mContext.getResources().getColor(R.color.notification_action_color_filter)));
            remoteViews.setTextViewText(R.id.action_text, action.title);
            if (!b) {
                remoteViews.setOnClickPendingIntent(R.id.action_container, action.actionIntent);
            }
            if (Build$VERSION.SDK_INT >= 15) {
                remoteViews.setContentDescription(R.id.action_container, action.title);
            }
            return remoteViews;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 24) {
                notificationBuilderWithBuilderAccessor.getBuilder().setStyle((Notification$Style)new Notification$DecoratedCustomViewStyle());
            }
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        public RemoteViews makeBigContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 24) {
                return null;
            }
            RemoteViews remoteViews = this.mBuilder.getBigContentView();
            if (remoteViews == null) {
                remoteViews = this.mBuilder.getContentView();
            }
            if (remoteViews == null) {
                return null;
            }
            return this.createRemoteViews(remoteViews, true);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        public RemoteViews makeContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 24) {
                return null;
            }
            if (this.mBuilder.getContentView() == null) {
                return null;
            }
            return this.createRemoteViews(this.mBuilder.getContentView(), false);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        public RemoteViews makeHeadsUpContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 24) {
                return null;
            }
            final RemoteViews headsUpContentView = this.mBuilder.getHeadsUpContentView();
            RemoteViews contentView;
            if (headsUpContentView != null) {
                contentView = headsUpContentView;
            }
            else {
                contentView = this.mBuilder.getContentView();
            }
            if (headsUpContentView == null) {
                return null;
            }
            return this.createRemoteViews(contentView, true);
        }
    }
    
    public interface Extender
    {
        NotificationCompat.Builder extend(final NotificationCompat.Builder p0);
    }
    
    public static class InboxStyle extends Style
    {
        private ArrayList<CharSequence> mTexts;
        
        public InboxStyle() {
            this.mTexts = new ArrayList<CharSequence>();
        }
        
        public InboxStyle(final NotificationCompat.Builder builder) {
            this.mTexts = new ArrayList<CharSequence>();
            ((Style)this).setBuilder(builder);
        }
        
        public InboxStyle addLine(final CharSequence charSequence) {
            this.mTexts.add(NotificationCompat.Builder.limitCharSequenceLength(charSequence));
            return this;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                NotificationCompatJellybean.addInboxStyle(notificationBuilderWithBuilderAccessor, this.mBigContentTitle, this.mSummaryTextSet, this.mSummaryText, this.mTexts);
            }
        }
        
        public InboxStyle setBigContentTitle(final CharSequence charSequence) {
            this.mBigContentTitle = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            return this;
        }
        
        public InboxStyle setSummaryText(final CharSequence charSequence) {
            this.mSummaryText = NotificationCompat.Builder.limitCharSequenceLength(charSequence);
            this.mSummaryTextSet = true;
            return this;
        }
    }
    
    public static class MessagingStyle extends Style
    {
        public static final int MAXIMUM_RETAINED_MESSAGES = 25;
        CharSequence mConversationTitle;
        List<Message> mMessages;
        CharSequence mUserDisplayName;
        
        MessagingStyle() {
            this.mMessages = new ArrayList<Message>();
        }
        
        public MessagingStyle(@NonNull final CharSequence mUserDisplayName) {
            this.mMessages = new ArrayList<Message>();
            this.mUserDisplayName = mUserDisplayName;
        }
        
        public static MessagingStyle extractMessagingStyleFromNotification(final Notification notification) {
            final Bundle extras = NotificationCompat.getExtras(notification);
            if (extras != null && !extras.containsKey("android.selfDisplayName")) {
                return null;
            }
            try {
                final MessagingStyle messagingStyle = new MessagingStyle();
                messagingStyle.restoreFromCompatExtras(extras);
                return messagingStyle;
            }
            catch (ClassCastException ex) {
                return null;
            }
        }
        
        @Nullable
        private Message findLatestIncomingMessage() {
            for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                final Message message = this.mMessages.get(i);
                if (!TextUtils.isEmpty(message.getSender())) {
                    return message;
                }
            }
            if (!this.mMessages.isEmpty()) {
                final List<Message> mMessages = this.mMessages;
                return mMessages.get(mMessages.size() - 1);
            }
            return null;
        }
        
        private boolean hasMessagesWithoutSender() {
            for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                if (this.mMessages.get(i).getSender() == null) {
                    return true;
                }
            }
            return false;
        }
        
        @NonNull
        private TextAppearanceSpan makeFontColorSpan(final int n) {
            return new TextAppearanceSpan((String)null, 0, 0, ColorStateList.valueOf(n), (ColorStateList)null);
        }
        
        private CharSequence makeMessageLine(final Message message) {
            final BidiFormatter instance = BidiFormatter.getInstance();
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            final boolean b = Build$VERSION.SDK_INT >= 21;
            int color;
            if (b) {
                color = -16777216;
            }
            else {
                color = -1;
            }
            CharSequence charSequence = message.getSender();
            final boolean empty = TextUtils.isEmpty(message.getSender());
            final String s = "";
            int n = color;
            if (empty) {
                if ((charSequence = this.mUserDisplayName) == null) {
                    charSequence = "";
                }
                if (b && this.mBuilder.getColor() != 0) {
                    color = this.mBuilder.getColor();
                }
                n = color;
            }
            final CharSequence unicodeWrap = instance.unicodeWrap(charSequence);
            spannableStringBuilder.append(unicodeWrap);
            spannableStringBuilder.setSpan((Object)this.makeFontColorSpan(n), spannableStringBuilder.length() - unicodeWrap.length(), spannableStringBuilder.length(), 33);
            CharSequence text;
            if (message.getText() == null) {
                text = s;
            }
            else {
                text = message.getText();
            }
            spannableStringBuilder.append((CharSequence)"  ").append(instance.unicodeWrap(text));
            return (CharSequence)spannableStringBuilder;
        }
        
        @Override
        public void addCompatExtras(final Bundle bundle) {
            super.addCompatExtras(bundle);
            final CharSequence mUserDisplayName = this.mUserDisplayName;
            if (mUserDisplayName != null) {
                bundle.putCharSequence("android.selfDisplayName", mUserDisplayName);
            }
            final CharSequence mConversationTitle = this.mConversationTitle;
            if (mConversationTitle != null) {
                bundle.putCharSequence("android.conversationTitle", mConversationTitle);
            }
            if (!this.mMessages.isEmpty()) {
                bundle.putParcelableArray("android.messages", (Parcelable[])Message.getBundleArrayForMessages(this.mMessages));
            }
        }
        
        public MessagingStyle addMessage(final Message message) {
            this.mMessages.add(message);
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }
        
        public MessagingStyle addMessage(final CharSequence charSequence, final long n, final CharSequence charSequence2) {
            this.mMessages.add(new Message(charSequence, n, charSequence2));
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 24) {
                final ArrayList<CharSequence> list = new ArrayList<CharSequence>();
                final ArrayList<Long> list2 = new ArrayList<Long>();
                final ArrayList<CharSequence> list3 = new ArrayList<CharSequence>();
                final ArrayList<String> list4 = new ArrayList<String>();
                final ArrayList<Uri> list5 = new ArrayList<Uri>();
                for (final Message message : this.mMessages) {
                    list.add(message.getText());
                    list2.add(message.getTimestamp());
                    list3.add(message.getSender());
                    list4.add(message.getDataMimeType());
                    list5.add(message.getDataUri());
                }
                NotificationCompatApi24.addMessagingStyle(notificationBuilderWithBuilderAccessor, this.mUserDisplayName, this.mConversationTitle, list, list2, list3, list4, list5);
                return;
            }
            final Message latestIncomingMessage = this.findLatestIncomingMessage();
            if (this.mConversationTitle != null) {
                notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle(this.mConversationTitle);
            }
            else if (latestIncomingMessage != null) {
                notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle(latestIncomingMessage.getSender());
            }
            if (latestIncomingMessage != null) {
                final Notification$Builder builder = notificationBuilderWithBuilderAccessor.getBuilder();
                CharSequence contentText;
                if (this.mConversationTitle != null) {
                    contentText = this.makeMessageLine(latestIncomingMessage);
                }
                else {
                    contentText = latestIncomingMessage.getText();
                }
                builder.setContentText(contentText);
            }
            if (Build$VERSION.SDK_INT >= 16) {
                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                final boolean b = this.mConversationTitle != null || this.hasMessagesWithoutSender();
                for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                    final Message message2 = this.mMessages.get(i);
                    CharSequence charSequence;
                    if (b) {
                        charSequence = this.makeMessageLine(message2);
                    }
                    else {
                        charSequence = message2.getText();
                    }
                    if (i != this.mMessages.size() - 1) {
                        spannableStringBuilder.insert(0, (CharSequence)"\n");
                    }
                    spannableStringBuilder.insert(0, charSequence);
                }
                NotificationCompatJellybean.addBigTextStyle(notificationBuilderWithBuilderAccessor, null, false, null, (CharSequence)spannableStringBuilder);
            }
        }
        
        public CharSequence getConversationTitle() {
            return this.mConversationTitle;
        }
        
        public List<Message> getMessages() {
            return this.mMessages;
        }
        
        public CharSequence getUserDisplayName() {
            return this.mUserDisplayName;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        protected void restoreFromCompatExtras(final Bundle bundle) {
            this.mMessages.clear();
            this.mUserDisplayName = bundle.getString("android.selfDisplayName");
            this.mConversationTitle = bundle.getString("android.conversationTitle");
            final Parcelable[] parcelableArray = bundle.getParcelableArray("android.messages");
            if (parcelableArray != null) {
                this.mMessages = Message.getMessagesFromBundleArray(parcelableArray);
            }
        }
        
        public MessagingStyle setConversationTitle(final CharSequence mConversationTitle) {
            this.mConversationTitle = mConversationTitle;
            return this;
        }
        
        public static final class Message
        {
            static final String KEY_DATA_MIME_TYPE = "type";
            static final String KEY_DATA_URI = "uri";
            static final String KEY_EXTRAS_BUNDLE = "extras";
            static final String KEY_SENDER = "sender";
            static final String KEY_TEXT = "text";
            static final String KEY_TIMESTAMP = "time";
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras;
            private final CharSequence mSender;
            private final CharSequence mText;
            private final long mTimestamp;
            
            public Message(final CharSequence mText, final long mTimestamp, final CharSequence mSender) {
                this.mExtras = new Bundle();
                this.mText = mText;
                this.mTimestamp = mTimestamp;
                this.mSender = mSender;
            }
            
            static Bundle[] getBundleArrayForMessages(final List<Message> list) {
                final Bundle[] array = new Bundle[list.size()];
                for (int size = list.size(), i = 0; i < size; ++i) {
                    array[i] = list.get(i).toBundle();
                }
                return array;
            }
            
            static Message getMessageFromBundle(final Bundle bundle) {
                try {
                    if (!bundle.containsKey("text")) {
                        return null;
                    }
                    if (!bundle.containsKey("time")) {
                        return null;
                    }
                    final Message message = new Message(bundle.getCharSequence("text"), bundle.getLong("time"), bundle.getCharSequence("sender"));
                    if (bundle.containsKey("type") && bundle.containsKey("uri")) {
                        message.setData(bundle.getString("type"), (Uri)bundle.getParcelable("uri"));
                    }
                    if (bundle.containsKey("extras")) {
                        message.getExtras().putAll(bundle.getBundle("extras"));
                    }
                    return message;
                }
                catch (ClassCastException ex) {
                    return null;
                }
            }
            
            static List<Message> getMessagesFromBundleArray(final Parcelable[] array) {
                final ArrayList<Message> list = new ArrayList<Message>(array.length);
                for (int i = 0; i < array.length; ++i) {
                    if (array[i] instanceof Bundle) {
                        final Message messageFromBundle = getMessageFromBundle((Bundle)array[i]);
                        if (messageFromBundle != null) {
                            list.add(messageFromBundle);
                        }
                    }
                }
                return list;
            }
            
            private Bundle toBundle() {
                final Bundle bundle = new Bundle();
                final CharSequence mText = this.mText;
                if (mText != null) {
                    bundle.putCharSequence("text", mText);
                }
                bundle.putLong("time", this.mTimestamp);
                final CharSequence mSender = this.mSender;
                if (mSender != null) {
                    bundle.putCharSequence("sender", mSender);
                }
                final String mDataMimeType = this.mDataMimeType;
                if (mDataMimeType != null) {
                    bundle.putString("type", mDataMimeType);
                }
                final Uri mDataUri = this.mDataUri;
                if (mDataUri != null) {
                    bundle.putParcelable("uri", (Parcelable)mDataUri);
                }
                final Bundle mExtras = this.mExtras;
                if (mExtras != null) {
                    bundle.putBundle("extras", mExtras);
                }
                return bundle;
            }
            
            public String getDataMimeType() {
                return this.mDataMimeType;
            }
            
            public Uri getDataUri() {
                return this.mDataUri;
            }
            
            public Bundle getExtras() {
                return this.mExtras;
            }
            
            public CharSequence getSender() {
                return this.mSender;
            }
            
            public CharSequence getText() {
                return this.mText;
            }
            
            public long getTimestamp() {
                return this.mTimestamp;
            }
            
            public Message setData(final String mDataMimeType, final Uri mDataUri) {
                this.mDataMimeType = mDataMimeType;
                this.mDataUri = mDataUri;
                return this;
            }
        }
    }
    
    @RequiresApi(16)
    static class NotificationCompatApi16Impl extends NotificationCompatBaseImpl
    {
        @Override
        public Notification build(final NotificationCompat.Builder builder, final BuilderExtender builderExtender) {
            final NotificationCompatJellybean.Builder builder2 = new NotificationCompatJellybean.Builder(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mExtras, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mContentView, builder.mBigContentView);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            if (builder.mStyle != null) {
                builder.mStyle.apply(builder2);
            }
            final Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                final Bundle extras = NotificationCompat.getExtras(build);
                if (extras != null) {
                    builder.mStyle.addCompatExtras(extras);
                }
            }
            return build;
        }
        
        @Override
        public Action getAction(final Notification notification, final int n) {
            return (Action)NotificationCompatJellybean.getAction(notification, n, Action.FACTORY, RemoteInput.FACTORY);
        }
        
        @Override
        public Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list) {
            return (Action[])NotificationCompatJellybean.getActionsFromParcelableArrayList(list, Action.FACTORY, RemoteInput.FACTORY);
        }
        
        @Override
        public ArrayList<Parcelable> getParcelableArrayListForActions(final Action[] array) {
            return NotificationCompatJellybean.getParcelableArrayListForActions(array);
        }
    }
    
    @RequiresApi(19)
    static class NotificationCompatApi19Impl extends NotificationCompatApi16Impl
    {
        @Override
        public Notification build(final NotificationCompat.Builder builder, final BuilderExtender builderExtender) {
            final NotificationCompatKitKat.Builder builder2 = new NotificationCompatKitKat.Builder(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mPeople, builder.mExtras, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mContentView, builder.mBigContentView);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            if (builder.mStyle != null) {
                builder.mStyle.apply(builder2);
            }
            return builderExtender.build(builder, builder2);
        }
        
        @Override
        public Action getAction(final Notification notification, final int n) {
            return (Action)NotificationCompatKitKat.getAction(notification, n, Action.FACTORY, RemoteInput.FACTORY);
        }
    }
    
    @RequiresApi(20)
    static class NotificationCompatApi20Impl extends NotificationCompatApi19Impl
    {
        @Override
        public Notification build(final NotificationCompat.Builder builder, final BuilderExtender builderExtender) {
            final NotificationCompatApi20.Builder builder2 = new NotificationCompatApi20.Builder(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mPeople, builder.mExtras, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mContentView, builder.mBigContentView, builder.mGroupAlertBehavior);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            if (builder.mStyle != null) {
                builder.mStyle.apply(builder2);
            }
            final Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                builder.mStyle.addCompatExtras(NotificationCompat.getExtras(build));
            }
            return build;
        }
        
        @Override
        public Action getAction(final Notification notification, final int n) {
            return (Action)NotificationCompatApi20.getAction(notification, n, Action.FACTORY, RemoteInput.FACTORY);
        }
        
        @Override
        public Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list) {
            return (Action[])NotificationCompatApi20.getActionsFromParcelableArrayList(list, Action.FACTORY, RemoteInput.FACTORY);
        }
        
        @Override
        public ArrayList<Parcelable> getParcelableArrayListForActions(final Action[] array) {
            return NotificationCompatApi20.getParcelableArrayListForActions(array);
        }
    }
    
    @RequiresApi(21)
    static class NotificationCompatApi21Impl extends NotificationCompatApi20Impl
    {
        @Override
        public Notification build(final NotificationCompat.Builder builder, final BuilderExtender builderExtender) {
            final NotificationCompatApi21.Builder builder2 = new NotificationCompatApi21.Builder(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mCategory, builder.mPeople, builder.mExtras, builder.mColor, builder.mVisibility, builder.mPublicVersion, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mContentView, builder.mBigContentView, builder.mHeadsUpContentView, builder.mGroupAlertBehavior);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            if (builder.mStyle != null) {
                builder.mStyle.apply(builder2);
            }
            final Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                builder.mStyle.addCompatExtras(NotificationCompat.getExtras(build));
            }
            return build;
        }
        
        @Override
        public Bundle getBundleForUnreadConversation(final NotificationCompatBase.UnreadConversation unreadConversation) {
            return NotificationCompatApi21.getBundleForUnreadConversation(unreadConversation);
        }
        
        @Override
        public NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(final Bundle bundle, final NotificationCompatBase.UnreadConversation.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
            return NotificationCompatApi21.getUnreadConversationFromBundle(bundle, factory, factory2);
        }
    }
    
    @RequiresApi(24)
    static class NotificationCompatApi24Impl extends NotificationCompatApi21Impl
    {
        @Override
        public Notification build(final NotificationCompat.Builder builder, final BuilderExtender builderExtender) {
            final NotificationCompatApi24.Builder builder2 = new NotificationCompatApi24.Builder(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mCategory, builder.mPeople, builder.mExtras, builder.mColor, builder.mVisibility, builder.mPublicVersion, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mRemoteInputHistory, builder.mContentView, builder.mBigContentView, builder.mHeadsUpContentView, builder.mGroupAlertBehavior);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            if (builder.mStyle != null) {
                builder.mStyle.apply(builder2);
            }
            final Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                builder.mStyle.addCompatExtras(NotificationCompat.getExtras(build));
            }
            return build;
        }
        
        @Override
        public Action getAction(final Notification notification, final int n) {
            return (Action)NotificationCompatApi24.getAction(notification, n, Action.FACTORY, RemoteInput.FACTORY);
        }
        
        @Override
        public Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list) {
            return (Action[])NotificationCompatApi24.getActionsFromParcelableArrayList(list, Action.FACTORY, RemoteInput.FACTORY);
        }
        
        @Override
        public ArrayList<Parcelable> getParcelableArrayListForActions(final Action[] array) {
            return NotificationCompatApi24.getParcelableArrayListForActions(array);
        }
    }
    
    @RequiresApi(26)
    static class NotificationCompatApi26Impl extends NotificationCompatApi24Impl
    {
        @Override
        public Notification build(final NotificationCompat.Builder builder, final BuilderExtender builderExtender) {
            final NotificationCompatApi26.Builder builder2 = new NotificationCompatApi26.Builder(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate, builder.mShowWhen, builder.mUseChronometer, builder.mPriority, builder.mSubText, builder.mLocalOnly, builder.mCategory, builder.mPeople, builder.mExtras, builder.mColor, builder.mVisibility, builder.mPublicVersion, builder.mGroupKey, builder.mGroupSummary, builder.mSortKey, builder.mRemoteInputHistory, builder.mContentView, builder.mBigContentView, builder.mHeadsUpContentView, builder.mChannelId, builder.mBadgeIcon, builder.mShortcutId, builder.mTimeout, builder.mColorized, builder.mColorizedSet, builder.mGroupAlertBehavior);
            NotificationCompat.addActionsToBuilder(builder2, builder.mActions);
            if (builder.mStyle != null) {
                builder.mStyle.apply(builder2);
            }
            final Notification build = builderExtender.build(builder, builder2);
            if (builder.mStyle != null) {
                builder.mStyle.addCompatExtras(NotificationCompat.getExtras(build));
            }
            return build;
        }
    }
    
    static class NotificationCompatBaseImpl implements NotificationCompatImpl
    {
        @Override
        public Notification build(final NotificationCompat.Builder builder, final BuilderExtender builderExtender) {
            return builderExtender.build(builder, new BuilderBase(builder.mContext, builder.mNotification, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mTickerView, builder.mNumber, builder.mContentIntent, builder.mFullScreenIntent, builder.mLargeIcon, builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate));
        }
        
        @Override
        public Action getAction(final Notification notification, final int n) {
            return null;
        }
        
        @Override
        public Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list) {
            return null;
        }
        
        @Override
        public Bundle getBundleForUnreadConversation(final NotificationCompatBase.UnreadConversation unreadConversation) {
            return null;
        }
        
        @Override
        public ArrayList<Parcelable> getParcelableArrayListForActions(final Action[] array) {
            return null;
        }
        
        @Override
        public NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(final Bundle bundle, final NotificationCompatBase.UnreadConversation.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
            return null;
        }
        
        public static class BuilderBase implements NotificationBuilderWithBuilderAccessor
        {
            private Notification$Builder mBuilder;
            
            BuilderBase(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b) {
                final Notification$Builder setLights = new Notification$Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
                final int flags = notification.flags;
                final boolean b2 = true;
                this.mBuilder = setLights.setOngoing((flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0 && b2).setLargeIcon(largeIcon).setNumber(number).setProgress(n, n2, b);
            }
            
            @Override
            public Notification build() {
                return this.mBuilder.getNotification();
            }
            
            @Override
            public Notification$Builder getBuilder() {
                return this.mBuilder;
            }
        }
    }
    
    interface NotificationCompatImpl
    {
        Notification build(final NotificationCompat.Builder p0, final BuilderExtender p1);
        
        Action getAction(final Notification p0, final int p1);
        
        Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> p0);
        
        Bundle getBundleForUnreadConversation(final NotificationCompatBase.UnreadConversation p0);
        
        ArrayList<Parcelable> getParcelableArrayListForActions(final Action[] p0);
        
        NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(final Bundle p0, final NotificationCompatBase.UnreadConversation.Factory p1, final RemoteInputCompatBase.RemoteInput.Factory p2);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationVisibility {
    }
    
    public abstract static class Style
    {
        CharSequence mBigContentTitle;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        protected NotificationCompat.Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet;
        
        public Style() {
            this.mSummaryTextSet = false;
        }
        
        private int calculateTopPadding() {
            final Resources resources = this.mBuilder.mContext.getResources();
            final int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.notification_top_pad);
            final int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.notification_top_pad_large_text);
            final float n = (constrain(resources.getConfiguration().fontScale, 1.0f, 1.3f) - 1.0f) / 0.29999995f;
            return Math.round((1.0f - n) * dimensionPixelSize + dimensionPixelSize2 * n);
        }
        
        private static float constrain(final float n, final float n2, final float n3) {
            if (n < n2) {
                return n2;
            }
            if (n > n3) {
                return n3;
            }
            return n;
        }
        
        private Bitmap createColoredBitmap(int intrinsicWidth, final int n, int intrinsicHeight) {
            final Drawable drawable = this.mBuilder.mContext.getResources().getDrawable(intrinsicWidth);
            if (intrinsicHeight == 0) {
                intrinsicWidth = drawable.getIntrinsicWidth();
            }
            else {
                intrinsicWidth = intrinsicHeight;
            }
            if (intrinsicHeight == 0) {
                intrinsicHeight = drawable.getIntrinsicHeight();
            }
            final Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap$Config.ARGB_8888);
            drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
            if (n != 0) {
                drawable.mutate().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.SRC_IN));
            }
            drawable.draw(new Canvas(bitmap));
            return bitmap;
        }
        
        private Bitmap createIconWithBackground(int n, final int n2, final int n3, int n4) {
            final int notification_icon_background = R.drawable.notification_icon_background;
            if (n4 == 0) {
                n4 = 0;
            }
            final Bitmap coloredBitmap = this.createColoredBitmap(notification_icon_background, n4, n2);
            final Canvas canvas = new Canvas(coloredBitmap);
            final Drawable mutate = this.mBuilder.mContext.getResources().getDrawable(n).mutate();
            mutate.setFilterBitmap(true);
            n = (n2 - n3) / 2;
            mutate.setBounds(n, n, n3 + n, n3 + n);
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-1, PorterDuff$Mode.SRC_ATOP));
            mutate.draw(canvas);
            return coloredBitmap;
        }
        
        private void hideNormalContent(final RemoteViews remoteViews) {
            remoteViews.setViewVisibility(R.id.title, 8);
            remoteViews.setViewVisibility(R.id.text2, 8);
            remoteViews.setViewVisibility(R.id.text, 8);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public void addCompatExtras(final Bundle bundle) {
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public RemoteViews applyStandardTemplate(final boolean b, int n, final boolean b2) {
            final Resources resources = this.mBuilder.mContext.getResources();
            final RemoteViews remoteViews = new RemoteViews(this.mBuilder.mContext.getPackageName(), n);
            boolean b3 = false;
            n = this.mBuilder.getPriority();
            final int n2 = 0;
            if (n < -1) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (Build$VERSION.SDK_INT >= 16 && Build$VERSION.SDK_INT < 21) {
                if (n != 0) {
                    remoteViews.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg_low);
                    remoteViews.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_low_bg);
                }
                else {
                    remoteViews.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg);
                    remoteViews.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_bg);
                }
            }
            if (this.mBuilder.mLargeIcon != null) {
                if (Build$VERSION.SDK_INT >= 16) {
                    remoteViews.setViewVisibility(R.id.icon, 0);
                    remoteViews.setImageViewBitmap(R.id.icon, this.mBuilder.mLargeIcon);
                }
                else {
                    remoteViews.setViewVisibility(R.id.icon, 8);
                }
                if (b && this.mBuilder.mNotification.icon != 0) {
                    n = resources.getDimensionPixelSize(R.dimen.notification_right_icon_size);
                    final int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.notification_small_icon_background_padding);
                    if (Build$VERSION.SDK_INT >= 21) {
                        remoteViews.setImageViewBitmap(R.id.right_icon, this.createIconWithBackground(this.mBuilder.mNotification.icon, n, n - dimensionPixelSize * 2, this.mBuilder.getColor()));
                    }
                    else {
                        remoteViews.setImageViewBitmap(R.id.right_icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
                    }
                    remoteViews.setViewVisibility(R.id.right_icon, 0);
                }
            }
            else if (b && this.mBuilder.mNotification.icon != 0) {
                remoteViews.setViewVisibility(R.id.icon, 0);
                if (Build$VERSION.SDK_INT >= 21) {
                    n = resources.getDimensionPixelSize(R.dimen.notification_large_icon_width);
                    remoteViews.setImageViewBitmap(R.id.icon, this.createIconWithBackground(this.mBuilder.mNotification.icon, n - resources.getDimensionPixelSize(R.dimen.notification_big_circle_margin), resources.getDimensionPixelSize(R.dimen.notification_small_icon_size_as_large), this.mBuilder.getColor()));
                }
                else {
                    remoteViews.setImageViewBitmap(R.id.icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
                }
            }
            if (this.mBuilder.mContentTitle != null) {
                remoteViews.setTextViewText(R.id.title, this.mBuilder.mContentTitle);
            }
            if (this.mBuilder.mContentText != null) {
                remoteViews.setTextViewText(R.id.text, this.mBuilder.mContentText);
                b3 = true;
            }
            if (Build$VERSION.SDK_INT < 21 && this.mBuilder.mLargeIcon != null) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (this.mBuilder.mContentInfo != null) {
                remoteViews.setTextViewText(R.id.info, this.mBuilder.mContentInfo);
                remoteViews.setViewVisibility(R.id.info, 0);
                b3 = true;
                n = 1;
            }
            else if (this.mBuilder.mNumber > 0) {
                n = resources.getInteger(R.integer.status_bar_notification_info_maxnum);
                if (this.mBuilder.mNumber > n) {
                    remoteViews.setTextViewText(R.id.info, (CharSequence)resources.getString(R.string.status_bar_notification_info_overflow));
                }
                else {
                    remoteViews.setTextViewText(R.id.info, (CharSequence)NumberFormat.getIntegerInstance().format(this.mBuilder.mNumber));
                }
                remoteViews.setViewVisibility(R.id.info, 0);
                b3 = true;
                n = 1;
            }
            else {
                remoteViews.setViewVisibility(R.id.info, 8);
            }
            boolean b4 = false;
            Label_0746: {
                if (this.mBuilder.mSubText != null && Build$VERSION.SDK_INT >= 16) {
                    remoteViews.setTextViewText(R.id.text, this.mBuilder.mSubText);
                    if (this.mBuilder.mContentText != null) {
                        remoteViews.setTextViewText(R.id.text2, this.mBuilder.mContentText);
                        remoteViews.setViewVisibility(R.id.text2, 0);
                        b4 = true;
                        break Label_0746;
                    }
                    remoteViews.setViewVisibility(R.id.text2, 8);
                }
                b4 = false;
            }
            if (b4 && Build$VERSION.SDK_INT >= 16) {
                if (b2) {
                    remoteViews.setTextViewTextSize(R.id.text, 0, (float)resources.getDimensionPixelSize(R.dimen.notification_subtext_size));
                }
                remoteViews.setViewPadding(R.id.line1, 0, 0, 0, 0);
            }
            if (this.mBuilder.getWhenIfShowing() != 0L) {
                if (this.mBuilder.mUseChronometer && Build$VERSION.SDK_INT >= 16) {
                    remoteViews.setViewVisibility(R.id.chronometer, 0);
                    remoteViews.setLong(R.id.chronometer, "setBase", this.mBuilder.getWhenIfShowing() + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
                    remoteViews.setBoolean(R.id.chronometer, "setStarted", true);
                }
                else {
                    remoteViews.setViewVisibility(R.id.time, 0);
                    remoteViews.setLong(R.id.time, "setTime", this.mBuilder.getWhenIfShowing());
                }
                n = 1;
            }
            final int right_side = R.id.right_side;
            if (n != 0) {
                n = 0;
            }
            else {
                n = 8;
            }
            remoteViews.setViewVisibility(right_side, n);
            final int line3 = R.id.line3;
            if (b3) {
                n = n2;
            }
            else {
                n = 8;
            }
            remoteViews.setViewVisibility(line3, n);
            return remoteViews;
        }
        
        public Notification build() {
            Notification build = null;
            final NotificationCompat.Builder mBuilder = this.mBuilder;
            if (mBuilder != null) {
                build = mBuilder.build();
            }
            return build;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public void buildIntoRemoteViews(final RemoteViews remoteViews, final RemoteViews remoteViews2) {
            this.hideNormalContent(remoteViews);
            remoteViews.removeAllViews(R.id.notification_main_column);
            remoteViews.addView(R.id.notification_main_column, remoteViews2.clone());
            remoteViews.setViewVisibility(R.id.notification_main_column, 0);
            if (Build$VERSION.SDK_INT >= 21) {
                remoteViews.setViewPadding(R.id.notification_main_column_container, 0, this.calculateTopPadding(), 0, 0);
            }
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Bitmap createColoredBitmap(final int n, final int n2) {
            return this.createColoredBitmap(n, n2, 0);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public RemoteViews makeBigContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public RemoteViews makeContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public RemoteViews makeHeadsUpContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        protected void restoreFromCompatExtras(final Bundle bundle) {
        }
        
        public void setBuilder(NotificationCompat.Builder mBuilder) {
            if (this.mBuilder != mBuilder) {
                this.mBuilder = mBuilder;
                mBuilder = this.mBuilder;
                if (mBuilder != null) {
                    mBuilder.setStyle(this);
                }
            }
        }
    }
    
    public static final class WearableExtender implements NotificationCompat.Extender
    {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
        private static final int DEFAULT_FLAGS = 1;
        private static final int DEFAULT_GRAVITY = 80;
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
        private static final int FLAG_BIG_PICTURE_AMBIENT = 32;
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 16;
        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 64;
        private static final int FLAG_HINT_HIDE_ICON = 2;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
        private static final int FLAG_START_SCROLL_BOTTOM = 8;
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_BRIDGE_TAG = "bridgeTag";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_DISMISSAL_ID = "dismissalId";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
        private static final String KEY_PAGES = "pages";
        public static final int SCREEN_TIMEOUT_LONG = -1;
        public static final int SCREEN_TIMEOUT_SHORT = 0;
        public static final int SIZE_DEFAULT = 0;
        public static final int SIZE_FULL_SCREEN = 5;
        public static final int SIZE_LARGE = 4;
        public static final int SIZE_MEDIUM = 3;
        public static final int SIZE_SMALL = 2;
        public static final int SIZE_XSMALL = 1;
        public static final int UNSET_ACTION_INDEX = -1;
        private ArrayList<Action> mActions;
        private Bitmap mBackground;
        private String mBridgeTag;
        private int mContentActionIndex;
        private int mContentIcon;
        private int mContentIconGravity;
        private int mCustomContentHeight;
        private int mCustomSizePreset;
        private String mDismissalId;
        private PendingIntent mDisplayIntent;
        private int mFlags;
        private int mGravity;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages;
        
        public WearableExtender() {
            this.mActions = new ArrayList<Action>();
            this.mFlags = 1;
            this.mPages = new ArrayList<Notification>();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
        }
        
        public WearableExtender(final Notification notification) {
            this.mActions = new ArrayList<Action>();
            this.mFlags = 1;
            this.mPages = new ArrayList<Notification>();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
            final Bundle extras = NotificationCompat.getExtras(notification);
            Bundle bundle;
            if (extras != null) {
                bundle = extras.getBundle("android.wearable.EXTENSIONS");
            }
            else {
                bundle = null;
            }
            if (bundle != null) {
                final Action[] actionsFromParcelableArrayList = NotificationCompat.IMPL.getActionsFromParcelableArrayList(bundle.getParcelableArrayList("actions"));
                if (actionsFromParcelableArrayList != null) {
                    Collections.addAll(this.mActions, actionsFromParcelableArrayList);
                }
                this.mFlags = bundle.getInt("flags", 1);
                this.mDisplayIntent = (PendingIntent)bundle.getParcelable("displayIntent");
                final Notification[] notificationArrayFromBundle = NotificationCompat.getNotificationArrayFromBundle(bundle, "pages");
                if (notificationArrayFromBundle != null) {
                    Collections.addAll(this.mPages, notificationArrayFromBundle);
                }
                this.mBackground = (Bitmap)bundle.getParcelable("background");
                this.mContentIcon = bundle.getInt("contentIcon");
                this.mContentIconGravity = bundle.getInt("contentIconGravity", 8388613);
                this.mContentActionIndex = bundle.getInt("contentActionIndex", -1);
                this.mCustomSizePreset = bundle.getInt("customSizePreset", 0);
                this.mCustomContentHeight = bundle.getInt("customContentHeight");
                this.mGravity = bundle.getInt("gravity", 80);
                this.mHintScreenTimeout = bundle.getInt("hintScreenTimeout");
                this.mDismissalId = bundle.getString("dismissalId");
                this.mBridgeTag = bundle.getString("bridgeTag");
            }
        }
        
        private void setFlag(final int n, final boolean b) {
            if (b) {
                this.mFlags |= n;
                return;
            }
            this.mFlags &= ~n;
        }
        
        public WearableExtender addAction(final Action action) {
            this.mActions.add(action);
            return this;
        }
        
        public WearableExtender addActions(final List<Action> list) {
            this.mActions.addAll(list);
            return this;
        }
        
        public WearableExtender addPage(final Notification notification) {
            this.mPages.add(notification);
            return this;
        }
        
        public WearableExtender addPages(final List<Notification> list) {
            this.mPages.addAll(list);
            return this;
        }
        
        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }
        
        public WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }
        
        public WearableExtender clone() {
            final WearableExtender wearableExtender = new WearableExtender();
            wearableExtender.mActions = new ArrayList<Action>(this.mActions);
            wearableExtender.mFlags = this.mFlags;
            wearableExtender.mDisplayIntent = this.mDisplayIntent;
            wearableExtender.mPages = new ArrayList<Notification>(this.mPages);
            wearableExtender.mBackground = this.mBackground;
            wearableExtender.mContentIcon = this.mContentIcon;
            wearableExtender.mContentIconGravity = this.mContentIconGravity;
            wearableExtender.mContentActionIndex = this.mContentActionIndex;
            wearableExtender.mCustomSizePreset = this.mCustomSizePreset;
            wearableExtender.mCustomContentHeight = this.mCustomContentHeight;
            wearableExtender.mGravity = this.mGravity;
            wearableExtender.mHintScreenTimeout = this.mHintScreenTimeout;
            wearableExtender.mDismissalId = this.mDismissalId;
            wearableExtender.mBridgeTag = this.mBridgeTag;
            return wearableExtender;
        }
        
        @Override
        public NotificationCompat.Builder extend(final NotificationCompat.Builder builder) {
            final Bundle bundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                final NotificationCompatImpl impl = NotificationCompat.IMPL;
                final ArrayList<Action> mActions = this.mActions;
                bundle.putParcelableArrayList("actions", (ArrayList)impl.getParcelableArrayListForActions(mActions.toArray(new Action[mActions.size()])));
            }
            final int mFlags = this.mFlags;
            if (mFlags != 1) {
                bundle.putInt("flags", mFlags);
            }
            final PendingIntent mDisplayIntent = this.mDisplayIntent;
            if (mDisplayIntent != null) {
                bundle.putParcelable("displayIntent", (Parcelable)mDisplayIntent);
            }
            if (!this.mPages.isEmpty()) {
                final ArrayList<Notification> mPages = this.mPages;
                bundle.putParcelableArray("pages", (Parcelable[])mPages.toArray((Parcelable[])new Notification[mPages.size()]));
            }
            final Bitmap mBackground = this.mBackground;
            if (mBackground != null) {
                bundle.putParcelable("background", (Parcelable)mBackground);
            }
            final int mContentIcon = this.mContentIcon;
            if (mContentIcon != 0) {
                bundle.putInt("contentIcon", mContentIcon);
            }
            final int mContentIconGravity = this.mContentIconGravity;
            if (mContentIconGravity != 8388613) {
                bundle.putInt("contentIconGravity", mContentIconGravity);
            }
            final int mContentActionIndex = this.mContentActionIndex;
            if (mContentActionIndex != -1) {
                bundle.putInt("contentActionIndex", mContentActionIndex);
            }
            final int mCustomSizePreset = this.mCustomSizePreset;
            if (mCustomSizePreset != 0) {
                bundle.putInt("customSizePreset", mCustomSizePreset);
            }
            final int mCustomContentHeight = this.mCustomContentHeight;
            if (mCustomContentHeight != 0) {
                bundle.putInt("customContentHeight", mCustomContentHeight);
            }
            final int mGravity = this.mGravity;
            if (mGravity != 80) {
                bundle.putInt("gravity", mGravity);
            }
            final int mHintScreenTimeout = this.mHintScreenTimeout;
            if (mHintScreenTimeout != 0) {
                bundle.putInt("hintScreenTimeout", mHintScreenTimeout);
            }
            final String mDismissalId = this.mDismissalId;
            if (mDismissalId != null) {
                bundle.putString("dismissalId", mDismissalId);
            }
            final String mBridgeTag = this.mBridgeTag;
            if (mBridgeTag != null) {
                bundle.putString("bridgeTag", mBridgeTag);
            }
            builder.getExtras().putBundle("android.wearable.EXTENSIONS", bundle);
            return builder;
        }
        
        public List<Action> getActions() {
            return this.mActions;
        }
        
        public Bitmap getBackground() {
            return this.mBackground;
        }
        
        public String getBridgeTag() {
            return this.mBridgeTag;
        }
        
        public int getContentAction() {
            return this.mContentActionIndex;
        }
        
        public int getContentIcon() {
            return this.mContentIcon;
        }
        
        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }
        
        public boolean getContentIntentAvailableOffline() {
            return (this.mFlags & 0x1) != 0x0;
        }
        
        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }
        
        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }
        
        public String getDismissalId() {
            return this.mDismissalId;
        }
        
        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }
        
        public int getGravity() {
            return this.mGravity;
        }
        
        public boolean getHintAmbientBigPicture() {
            return (this.mFlags & 0x20) != 0x0;
        }
        
        public boolean getHintAvoidBackgroundClipping() {
            return (this.mFlags & 0x10) != 0x0;
        }
        
        public boolean getHintContentIntentLaunchesActivity() {
            return (this.mFlags & 0x40) != 0x0;
        }
        
        public boolean getHintHideIcon() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }
        
        public boolean getHintShowBackgroundOnly() {
            return (this.mFlags & 0x4) != 0x0;
        }
        
        public List<Notification> getPages() {
            return this.mPages;
        }
        
        public boolean getStartScrollBottom() {
            return (this.mFlags & 0x8) != 0x0;
        }
        
        public WearableExtender setBackground(final Bitmap mBackground) {
            this.mBackground = mBackground;
            return this;
        }
        
        public WearableExtender setBridgeTag(final String mBridgeTag) {
            this.mBridgeTag = mBridgeTag;
            return this;
        }
        
        public WearableExtender setContentAction(final int mContentActionIndex) {
            this.mContentActionIndex = mContentActionIndex;
            return this;
        }
        
        public WearableExtender setContentIcon(final int mContentIcon) {
            this.mContentIcon = mContentIcon;
            return this;
        }
        
        public WearableExtender setContentIconGravity(final int mContentIconGravity) {
            this.mContentIconGravity = mContentIconGravity;
            return this;
        }
        
        public WearableExtender setContentIntentAvailableOffline(final boolean b) {
            this.setFlag(1, b);
            return this;
        }
        
        public WearableExtender setCustomContentHeight(final int mCustomContentHeight) {
            this.mCustomContentHeight = mCustomContentHeight;
            return this;
        }
        
        public WearableExtender setCustomSizePreset(final int mCustomSizePreset) {
            this.mCustomSizePreset = mCustomSizePreset;
            return this;
        }
        
        public WearableExtender setDismissalId(final String mDismissalId) {
            this.mDismissalId = mDismissalId;
            return this;
        }
        
        public WearableExtender setDisplayIntent(final PendingIntent mDisplayIntent) {
            this.mDisplayIntent = mDisplayIntent;
            return this;
        }
        
        public WearableExtender setGravity(final int mGravity) {
            this.mGravity = mGravity;
            return this;
        }
        
        public WearableExtender setHintAmbientBigPicture(final boolean b) {
            this.setFlag(32, b);
            return this;
        }
        
        public WearableExtender setHintAvoidBackgroundClipping(final boolean b) {
            this.setFlag(16, b);
            return this;
        }
        
        public WearableExtender setHintContentIntentLaunchesActivity(final boolean b) {
            this.setFlag(64, b);
            return this;
        }
        
        public WearableExtender setHintHideIcon(final boolean b) {
            this.setFlag(2, b);
            return this;
        }
        
        public WearableExtender setHintScreenTimeout(final int mHintScreenTimeout) {
            this.mHintScreenTimeout = mHintScreenTimeout;
            return this;
        }
        
        public WearableExtender setHintShowBackgroundOnly(final boolean b) {
            this.setFlag(4, b);
            return this;
        }
        
        public WearableExtender setStartScrollBottom(final boolean b) {
            this.setFlag(8, b);
            return this;
        }
    }
}
