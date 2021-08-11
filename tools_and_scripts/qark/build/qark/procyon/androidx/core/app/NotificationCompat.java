// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import android.app.Notification$Action$Builder;
import java.util.Collections;
import android.os.SystemClock;
import java.text.NumberFormat;
import androidx.core.R$string;
import androidx.core.R$integer;
import androidx.core.R$drawable;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Bitmap$Config;
import android.app.Notification$Builder;
import android.app.Notification$MessagingStyle$Message;
import android.app.Notification$MessagingStyle;
import android.text.SpannableStringBuilder;
import androidx.core.text.BidiFormatter;
import android.content.res.ColorStateList;
import android.text.style.TextAppearanceSpan;
import android.text.TextUtils;
import android.app.Notification$InboxStyle;
import android.app.Notification$Style;
import android.app.Notification$DecoratedCustomViewStyle;
import androidx.core.R$color;
import androidx.core.R$id;
import androidx.core.R$layout;
import android.app.RemoteInput$Builder;
import android.media.AudioAttributes$Builder;
import android.net.Uri;
import android.content.res.Resources;
import androidx.core.R$dimen;
import android.content.Context;
import android.widget.RemoteViews;
import android.app.Notification$BigTextStyle;
import android.app.Notification$BigPictureStyle;
import android.graphics.Bitmap;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import android.app.PendingIntent;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import android.util.SparseArray;
import android.app.Notification$Action;
import android.os.Bundle;
import android.os.Build$VERSION;
import android.app.Notification;

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
    public static final String CATEGORY_NAVIGATION = "navigation";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_REMINDER = "reminder";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
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
    public static final String EXTRA_HIDDEN_CONVERSATION_TITLE = "android.hiddenConversationTitle";
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_IS_GROUP_CONVERSATION = "android.isGroupConversation";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
    public static final String EXTRA_MESSAGES = "android.messages";
    public static final String EXTRA_MESSAGING_STYLE_USER = "android.messagingStyleUser";
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
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;
    public static final int STREAM_DEFAULT = -1;
    public static final int VISIBILITY_PRIVATE = 0;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_SECRET = -1;
    
    @Deprecated
    public NotificationCompat() {
    }
    
    public static Action getAction(final Notification notification, final int n) {
        if (Build$VERSION.SDK_INT >= 20) {
            return getActionCompatFromAction(notification.actions[n]);
        }
        if (Build$VERSION.SDK_INT >= 19) {
            final Notification$Action notification$Action = notification.actions[n];
            final Bundle bundle = null;
            final SparseArray sparseParcelableArray = notification.extras.getSparseParcelableArray("android.support.actionExtras");
            Bundle bundle2 = bundle;
            if (sparseParcelableArray != null) {
                bundle2 = (Bundle)sparseParcelableArray.get(n);
            }
            return NotificationCompatJellybean.readAction(notification$Action.icon, notification$Action.title, notification$Action.actionIntent, bundle2);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getAction(notification, n);
        }
        return null;
    }
    
    static Action getActionCompatFromAction(final Notification$Action notification$Action) {
        final android.app.RemoteInput[] remoteInputs = notification$Action.getRemoteInputs();
        RemoteInput[] array;
        if (remoteInputs == null) {
            array = null;
        }
        else {
            final RemoteInput[] array2 = new RemoteInput[remoteInputs.length];
            int n = 0;
            while (true) {
                array = array2;
                if (n >= remoteInputs.length) {
                    break;
                }
                final android.app.RemoteInput remoteInput = remoteInputs[n];
                array2[n] = new RemoteInput(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), remoteInput.getExtras(), null);
                ++n;
            }
        }
        boolean boolean1;
        if (Build$VERSION.SDK_INT >= 24) {
            boolean1 = (notification$Action.getExtras().getBoolean("android.support.allowGeneratedReplies") || notification$Action.getAllowGeneratedReplies());
        }
        else {
            boolean1 = notification$Action.getExtras().getBoolean("android.support.allowGeneratedReplies");
        }
        final boolean boolean2 = notification$Action.getExtras().getBoolean("android.support.action.showsUserInterface", true);
        int n2;
        if (Build$VERSION.SDK_INT >= 28) {
            n2 = notification$Action.getSemanticAction();
        }
        else {
            n2 = notification$Action.getExtras().getInt("android.support.action.semanticAction", 0);
        }
        return new Action(notification$Action.icon, notification$Action.title, notification$Action.actionIntent, notification$Action.getExtras(), array, null, boolean1, n2, boolean2);
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
    
    public static CharSequence getContentTitle(final Notification notification) {
        return notification.extras.getCharSequence("android.title");
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
    
    public static List<Action> getInvisibleActions(final Notification notification) {
        final ArrayList<Action> list = new ArrayList<Action>();
        final Bundle bundle = notification.extras.getBundle("android.car.EXTENSIONS");
        if (bundle == null) {
            return list;
        }
        final Bundle bundle2 = bundle.getBundle("invisible_actions");
        if (bundle2 != null) {
            for (int i = 0; i < bundle2.size(); ++i) {
                list.add(NotificationCompatJellybean.getActionFromBundle(bundle2.getBundle(Integer.toString(i))));
            }
        }
        return list;
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
    
    public static class Action
    {
        static final String EXTRA_SEMANTIC_ACTION = "android.support.action.semanticAction";
        static final String EXTRA_SHOWS_USER_INTERFACE = "android.support.action.showsUserInterface";
        public static final int SEMANTIC_ACTION_ARCHIVE = 5;
        public static final int SEMANTIC_ACTION_CALL = 10;
        public static final int SEMANTIC_ACTION_DELETE = 4;
        public static final int SEMANTIC_ACTION_MARK_AS_READ = 2;
        public static final int SEMANTIC_ACTION_MARK_AS_UNREAD = 3;
        public static final int SEMANTIC_ACTION_MUTE = 6;
        public static final int SEMANTIC_ACTION_NONE = 0;
        public static final int SEMANTIC_ACTION_REPLY = 1;
        public static final int SEMANTIC_ACTION_THUMBS_DOWN = 9;
        public static final int SEMANTIC_ACTION_THUMBS_UP = 8;
        public static final int SEMANTIC_ACTION_UNMUTE = 7;
        public PendingIntent actionIntent;
        public int icon;
        private boolean mAllowGeneratedReplies;
        private final RemoteInput[] mDataOnlyRemoteInputs;
        final Bundle mExtras;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        boolean mShowsUserInterface;
        public CharSequence title;
        
        public Action(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
            this(n, charSequence, pendingIntent, new Bundle(), null, null, true, 0, true);
        }
        
        Action(final int icon, final CharSequence charSequence, final PendingIntent actionIntent, Bundle mExtras, final RemoteInput[] mRemoteInputs, final RemoteInput[] mDataOnlyRemoteInputs, final boolean mAllowGeneratedReplies, final int mSemanticAction, final boolean mShowsUserInterface) {
            this.mShowsUserInterface = true;
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
            this.mSemanticAction = mSemanticAction;
            this.mShowsUserInterface = mShowsUserInterface;
        }
        
        public PendingIntent getActionIntent() {
            return this.actionIntent;
        }
        
        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }
        
        public RemoteInput[] getDataOnlyRemoteInputs() {
            return this.mDataOnlyRemoteInputs;
        }
        
        public Bundle getExtras() {
            return this.mExtras;
        }
        
        public int getIcon() {
            return this.icon;
        }
        
        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }
        
        public int getSemanticAction() {
            return this.mSemanticAction;
        }
        
        public boolean getShowsUserInterface() {
            return this.mShowsUserInterface;
        }
        
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
            private int mSemanticAction;
            private boolean mShowsUserInterface;
            private final CharSequence mTitle;
            
            public Builder(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
                this(n, charSequence, pendingIntent, new Bundle(), null, true, 0, true);
            }
            
            private Builder(final int mIcon, final CharSequence charSequence, final PendingIntent mIntent, final Bundle mExtras, final RemoteInput[] array, final boolean mAllowGeneratedReplies, final int mSemanticAction, final boolean mShowsUserInterface) {
                this.mAllowGeneratedReplies = true;
                this.mShowsUserInterface = true;
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
                this.mSemanticAction = mSemanticAction;
                this.mShowsUserInterface = mShowsUserInterface;
            }
            
            public Builder(final Action action) {
                this(action.icon, action.title, action.actionIntent, new Bundle(action.mExtras), action.getRemoteInputs(), action.getAllowGeneratedReplies(), action.getSemanticAction(), action.mShowsUserInterface);
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
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, array2, array, this.mAllowGeneratedReplies, this.mSemanticAction, this.mShowsUserInterface);
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
            
            public Builder setSemanticAction(final int mSemanticAction) {
                this.mSemanticAction = mSemanticAction;
                return this;
            }
            
            public Builder setShowsUserInterface(final boolean mShowsUserInterface) {
                this.mShowsUserInterface = mShowsUserInterface;
                return this;
            }
        }
        
        public interface Extender
        {
            Builder extend(final Builder p0);
        }
        
        @Retention(RetentionPolicy.SOURCE)
        public @interface SemanticAction {
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
                this.mFlags &= n;
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
            
            @Deprecated
            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }
            
            @Deprecated
            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }
            
            public boolean getHintDisplayActionInline() {
                return (this.mFlags & 0x4) != 0x0;
            }
            
            public boolean getHintLaunchesActivity() {
                return (this.mFlags & 0x2) != 0x0;
            }
            
            @Deprecated
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
            
            @Deprecated
            public WearableExtender setCancelLabel(final CharSequence mCancelLabel) {
                this.mCancelLabel = mCancelLabel;
                return this;
            }
            
            @Deprecated
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
            
            @Deprecated
            public WearableExtender setInProgressLabel(final CharSequence mInProgressLabel) {
                this.mInProgressLabel = mInProgressLabel;
                return this;
            }
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
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
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                final Notification$BigPictureStyle bigPicture = new Notification$BigPictureStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigPicture(this.mPicture);
                if (this.mBigLargeIconSet) {
                    bigPicture.bigLargeIcon(this.mBigLargeIcon);
                }
                if (this.mSummaryTextSet) {
                    bigPicture.setSummaryText(this.mSummaryText);
                }
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
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                final Notification$BigTextStyle bigText = new Notification$BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigText(this.mBigText);
                if (this.mSummaryTextSet) {
                    bigText.setSummaryText(this.mSummaryText);
                }
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
        public ArrayList<Action> mActions;
        int mBadgeIcon;
        RemoteViews mBigContentView;
        String mCategory;
        String mChannelId;
        int mColor;
        boolean mColorized;
        boolean mColorizedSet;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        RemoteViews mContentView;
        public Context mContext;
        Bundle mExtras;
        PendingIntent mFullScreenIntent;
        int mGroupAlertBehavior;
        String mGroupKey;
        boolean mGroupSummary;
        RemoteViews mHeadsUpContentView;
        ArrayList<Action> mInvisibleActions;
        Bitmap mLargeIcon;
        boolean mLocalOnly;
        Notification mNotification;
        int mNumber;
        @Deprecated
        public ArrayList<String> mPeople;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        CharSequence[] mRemoteInputHistory;
        String mShortcutId;
        boolean mShowWhen;
        String mSortKey;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        long mTimeout;
        boolean mUseChronometer;
        int mVisibility;
        
        @Deprecated
        public Builder(final Context context) {
            this(context, null);
        }
        
        public Builder(final Context mContext, final String mChannelId) {
            this.mActions = new ArrayList<Action>();
            this.mInvisibleActions = new ArrayList<Action>();
            this.mShowWhen = true;
            this.mLocalOnly = false;
            this.mColor = 0;
            this.mVisibility = 0;
            this.mBadgeIcon = 0;
            this.mGroupAlertBehavior = 0;
            final Notification mNotification = new Notification();
            this.mNotification = mNotification;
            this.mContext = mContext;
            this.mChannelId = mChannelId;
            mNotification.when = System.currentTimeMillis();
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
        
        private Bitmap reduceLargeIconSize(final Bitmap bitmap) {
            if (bitmap == null) {
                return bitmap;
            }
            if (Build$VERSION.SDK_INT >= 27) {
                return bitmap;
            }
            final Resources resources = this.mContext.getResources();
            final int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.compat_notification_large_icon_max_width);
            final int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.compat_notification_large_icon_max_height);
            if (bitmap.getWidth() <= dimensionPixelSize && bitmap.getHeight() <= dimensionPixelSize2) {
                return bitmap;
            }
            final double min = Math.min(dimensionPixelSize / (double)Math.max(1, bitmap.getWidth()), dimensionPixelSize2 / (double)Math.max(1, bitmap.getHeight()));
            return Bitmap.createScaledBitmap(bitmap, (int)Math.ceil(bitmap.getWidth() * min), (int)Math.ceil(bitmap.getHeight() * min), true);
        }
        
        private void setFlag(final int n, final boolean b) {
            if (b) {
                final Notification mNotification = this.mNotification;
                mNotification.flags |= n;
                return;
            }
            final Notification mNotification2 = this.mNotification;
            mNotification2.flags &= n;
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
        
        public Builder addInvisibleAction(final int n, final CharSequence charSequence, final PendingIntent pendingIntent) {
            return this.addInvisibleAction(new Action(n, charSequence, pendingIntent));
        }
        
        public Builder addInvisibleAction(final Action action) {
            this.mInvisibleActions.add(action);
            return this;
        }
        
        public Builder addPerson(final String s) {
            this.mPeople.add(s);
            return this;
        }
        
        public Notification build() {
            return new NotificationCompatBuilder(this).build();
        }
        
        public Builder extend(final NotificationCompat.Extender extender) {
            extender.extend(this);
            return this;
        }
        
        public RemoteViews getBigContentView() {
            return this.mBigContentView;
        }
        
        public int getColor() {
            return this.mColor;
        }
        
        public RemoteViews getContentView() {
            return this.mContentView;
        }
        
        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }
        
        public RemoteViews getHeadsUpContentView() {
            return this.mHeadsUpContentView;
        }
        
        @Deprecated
        public Notification getNotification() {
            return this.build();
        }
        
        public int getPriority() {
            return this.mPriority;
        }
        
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
        
        public Builder setChannelId(final String mChannelId) {
            this.mChannelId = mChannelId;
            return this;
        }
        
        public Builder setColor(final int mColor) {
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
            this.mNotification.defaults = defaults;
            if ((defaults & 0x4) != 0x0) {
                final Notification mNotification = this.mNotification;
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
        
        public Builder setLargeIcon(final Bitmap bitmap) {
            this.mLargeIcon = this.reduceLargeIconSize(bitmap);
            return this;
        }
        
        public Builder setLights(int ledOnMS, int ledOnMS2, int flags) {
            this.mNotification.ledARGB = ledOnMS;
            this.mNotification.ledOnMS = ledOnMS2;
            this.mNotification.ledOffMS = flags;
            ledOnMS = this.mNotification.ledOnMS;
            ledOnMS2 = 1;
            if (ledOnMS != 0 && this.mNotification.ledOffMS != 0) {
                ledOnMS = 1;
            }
            else {
                ledOnMS = 0;
            }
            final Notification mNotification = this.mNotification;
            flags = mNotification.flags;
            if (ledOnMS != 0) {
                ledOnMS = ledOnMS2;
            }
            else {
                ledOnMS = 0;
            }
            mNotification.flags = (ledOnMS | (flags & 0xFFFFFFFE));
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
            this.mNotification.icon = icon;
            this.mNotification.iconLevel = iconLevel;
            return this;
        }
        
        public Builder setSortKey(final String mSortKey) {
            this.mSortKey = mSortKey;
            return this;
        }
        
        public Builder setSound(final Uri sound) {
            this.mNotification.sound = sound;
            this.mNotification.audioStreamType = -1;
            if (Build$VERSION.SDK_INT >= 21) {
                this.mNotification.audioAttributes = new AudioAttributes$Builder().setContentType(4).setUsage(5).build();
            }
            return this;
        }
        
        public Builder setSound(final Uri sound, final int n) {
            this.mNotification.sound = sound;
            this.mNotification.audioStreamType = n;
            if (Build$VERSION.SDK_INT >= 21) {
                this.mNotification.audioAttributes = new AudioAttributes$Builder().setContentType(4).setLegacyStreamType(n).build();
            }
            return this;
        }
        
        public Builder setStyle(final Style mStyle) {
            if (this.mStyle != mStyle && (this.mStyle = mStyle) != null) {
                mStyle.setBuilder(this);
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
    
    public static final class CarExtender implements NotificationCompat.Extender
    {
        static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_COLOR = "app_color";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        static final String EXTRA_INVISIBLE_ACTIONS = "invisible_actions";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String KEY_AUTHOR = "author";
        private static final String KEY_MESSAGES = "messages";
        private static final String KEY_ON_READ = "on_read";
        private static final String KEY_ON_REPLY = "on_reply";
        private static final String KEY_PARTICIPANTS = "participants";
        private static final String KEY_REMOTE_INPUT = "remote_input";
        private static final String KEY_TEXT = "text";
        private static final String KEY_TIMESTAMP = "timestamp";
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
                this.mUnreadConversation = getUnreadConversationFromBundle(bundle.getBundle("car_conversation"));
            }
        }
        
        private static Bundle getBundleForUnreadConversation(final UnreadConversation unreadConversation) {
            final Bundle bundle = new Bundle();
            String s = null;
            if (unreadConversation.getParticipants() != null) {
                s = s;
                if (unreadConversation.getParticipants().length > 1) {
                    s = unreadConversation.getParticipants()[0];
                }
            }
            final Parcelable[] array = new Parcelable[unreadConversation.getMessages().length];
            for (int i = 0; i < array.length; ++i) {
                final Bundle bundle2 = new Bundle();
                bundle2.putString("text", unreadConversation.getMessages()[i]);
                bundle2.putString("author", s);
                array[i] = (Parcelable)bundle2;
            }
            bundle.putParcelableArray("messages", array);
            final RemoteInput remoteInput = unreadConversation.getRemoteInput();
            if (remoteInput != null) {
                bundle.putParcelable("remote_input", (Parcelable)new RemoteInput$Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build());
            }
            bundle.putParcelable("on_reply", (Parcelable)unreadConversation.getReplyPendingIntent());
            bundle.putParcelable("on_read", (Parcelable)unreadConversation.getReadPendingIntent());
            bundle.putStringArray("participants", unreadConversation.getParticipants());
            bundle.putLong("timestamp", unreadConversation.getLatestTimestamp());
            return bundle;
        }
        
        private static UnreadConversation getUnreadConversationFromBundle(final Bundle bundle) {
            RemoteInput remoteInput = null;
            if (bundle == null) {
                return null;
            }
            final Parcelable[] parcelableArray = bundle.getParcelableArray("messages");
            String[] array = null;
            if (parcelableArray != null) {
                array = new String[parcelableArray.length];
                final boolean b = true;
                int n = 0;
                boolean b2;
                while (true) {
                    b2 = b;
                    if (n >= array.length) {
                        break;
                    }
                    if (!(parcelableArray[n] instanceof Bundle)) {
                        b2 = false;
                        break;
                    }
                    array[n] = ((Bundle)parcelableArray[n]).getString("text");
                    if (array[n] == null) {
                        b2 = false;
                        break;
                    }
                    ++n;
                }
                if (!b2) {
                    return null;
                }
            }
            final PendingIntent pendingIntent = (PendingIntent)bundle.getParcelable("on_read");
            final PendingIntent pendingIntent2 = (PendingIntent)bundle.getParcelable("on_reply");
            final android.app.RemoteInput remoteInput2 = (android.app.RemoteInput)bundle.getParcelable("remote_input");
            final String[] stringArray = bundle.getStringArray("participants");
            if (stringArray == null) {
                return null;
            }
            if (stringArray.length != 1) {
                return null;
            }
            if (remoteInput2 != null) {
                remoteInput = new RemoteInput(remoteInput2.getResultKey(), remoteInput2.getLabel(), remoteInput2.getChoices(), remoteInput2.getAllowFreeFormInput(), remoteInput2.getExtras(), null);
            }
            return new UnreadConversation(array, remoteInput, pendingIntent2, pendingIntent, stringArray, bundle.getLong("timestamp"));
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
            final UnreadConversation mUnreadConversation = this.mUnreadConversation;
            if (mUnreadConversation != null) {
                bundle.putBundle("car_conversation", getBundleForUnreadConversation(mUnreadConversation));
            }
            builder.getExtras().putBundle("android.car.EXTENSIONS", bundle);
            return builder;
        }
        
        public int getColor() {
            return this.mColor;
        }
        
        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }
        
        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }
        
        public CarExtender setColor(final int mColor) {
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
        
        public static class UnreadConversation
        {
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;
            
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
            final int notification_template_custom_big = R$layout.notification_template_custom_big;
            final int n = 0;
            final RemoteViews applyStandardTemplate = ((Style)this).applyStandardTemplate(true, notification_template_custom_big, false);
            applyStandardTemplate.removeAllViews(R$id.actions);
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
                            applyStandardTemplate.addView(R$id.actions, this.generateActionButton(this.mBuilder.mActions.get(n2)));
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
            applyStandardTemplate.setViewVisibility(R$id.actions, n3);
            applyStandardTemplate.setViewVisibility(R$id.action_divider, n3);
            ((Style)this).buildIntoRemoteViews(applyStandardTemplate, remoteViews);
            return applyStandardTemplate;
        }
        
        private RemoteViews generateActionButton(final Action action) {
            final boolean b = action.actionIntent == null;
            final String packageName = this.mBuilder.mContext.getPackageName();
            int n;
            if (b) {
                n = R$layout.notification_action_tombstone;
            }
            else {
                n = R$layout.notification_action;
            }
            final RemoteViews remoteViews = new RemoteViews(packageName, n);
            remoteViews.setImageViewBitmap(R$id.action_image, ((Style)this).createColoredBitmap(action.getIcon(), this.mBuilder.mContext.getResources().getColor(R$color.notification_action_color_filter)));
            remoteViews.setTextViewText(R$id.action_text, action.title);
            if (!b) {
                remoteViews.setOnClickPendingIntent(R$id.action_container, action.actionIntent);
            }
            if (Build$VERSION.SDK_INT >= 15) {
                remoteViews.setContentDescription(R$id.action_container, action.title);
            }
            return remoteViews;
        }
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 24) {
                notificationBuilderWithBuilderAccessor.getBuilder().setStyle((Notification$Style)new Notification$DecoratedCustomViewStyle());
            }
        }
        
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
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface GroupAlertBehavior {
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
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (Build$VERSION.SDK_INT >= 16) {
                final Notification$InboxStyle setBigContentTitle = new Notification$InboxStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(this.mBigContentTitle);
                if (this.mSummaryTextSet) {
                    setBigContentTitle.setSummaryText(this.mSummaryText);
                }
                final Iterator<CharSequence> iterator = this.mTexts.iterator();
                while (iterator.hasNext()) {
                    setBigContentTitle.addLine((CharSequence)iterator.next());
                }
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
        private CharSequence mConversationTitle;
        private Boolean mIsGroupConversation;
        private final List<Message> mMessages;
        private Person mUser;
        
        private MessagingStyle() {
            this.mMessages = new ArrayList<Message>();
        }
        
        public MessagingStyle(final Person mUser) {
            this.mMessages = new ArrayList<Message>();
            if (!TextUtils.isEmpty(mUser.getName())) {
                this.mUser = mUser;
                return;
            }
            throw new IllegalArgumentException("User's name must not be empty.");
        }
        
        @Deprecated
        public MessagingStyle(final CharSequence name) {
            this.mMessages = new ArrayList<Message>();
            this.mUser = new Person.Builder().setName(name).build();
        }
        
        public static MessagingStyle extractMessagingStyleFromNotification(final Notification notification) {
            final Bundle extras = NotificationCompat.getExtras(notification);
            if (extras != null && !extras.containsKey("android.selfDisplayName") && !extras.containsKey("android.messagingStyleUser")) {
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
        
        private Message findLatestIncomingMessage() {
            for (int i = this.mMessages.size() - 1; i >= 0; --i) {
                final Message message = this.mMessages.get(i);
                if (message.getPerson() != null && !TextUtils.isEmpty(message.getPerson().getName())) {
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
                final Message message = this.mMessages.get(i);
                if (message.getPerson() != null && message.getPerson().getName() == null) {
                    return true;
                }
            }
            return false;
        }
        
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
            final Person person = message.getPerson();
            final String s = "";
            CharSequence name;
            if (person == null) {
                name = "";
            }
            else {
                name = message.getPerson().getName();
            }
            int n = color;
            CharSequence name2 = name;
            if (TextUtils.isEmpty(name)) {
                name2 = this.mUser.getName();
                if (b && this.mBuilder.getColor() != 0) {
                    color = this.mBuilder.getColor();
                }
                n = color;
            }
            final CharSequence unicodeWrap = instance.unicodeWrap(name2);
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
            bundle.putCharSequence("android.selfDisplayName", this.mUser.getName());
            bundle.putBundle("android.messagingStyleUser", this.mUser.toBundle());
            bundle.putCharSequence("android.hiddenConversationTitle", this.mConversationTitle);
            if (this.mConversationTitle != null && this.mIsGroupConversation) {
                bundle.putCharSequence("android.conversationTitle", this.mConversationTitle);
            }
            if (!this.mMessages.isEmpty()) {
                bundle.putParcelableArray("android.messages", (Parcelable[])Message.getBundleArrayForMessages(this.mMessages));
            }
            final Boolean mIsGroupConversation = this.mIsGroupConversation;
            if (mIsGroupConversation != null) {
                bundle.putBoolean("android.isGroupConversation", (boolean)mIsGroupConversation);
            }
        }
        
        public MessagingStyle addMessage(final Message message) {
            this.mMessages.add(message);
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }
        
        public MessagingStyle addMessage(final CharSequence charSequence, final long n, final Person person) {
            this.addMessage(new Message(charSequence, n, person));
            return this;
        }
        
        @Deprecated
        public MessagingStyle addMessage(final CharSequence charSequence, final long n, final CharSequence name) {
            this.mMessages.add(new Message(charSequence, n, new Person.Builder().setName(name).build()));
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }
        
        @Override
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            this.setGroupConversation(this.isGroupConversation());
            if (Build$VERSION.SDK_INT >= 24) {
                Notification$MessagingStyle notification$MessagingStyle;
                if (Build$VERSION.SDK_INT >= 28) {
                    notification$MessagingStyle = new Notification$MessagingStyle(this.mUser.toAndroidPerson());
                }
                else {
                    notification$MessagingStyle = new Notification$MessagingStyle(this.mUser.getName());
                }
                if (this.mIsGroupConversation || Build$VERSION.SDK_INT >= 28) {
                    notification$MessagingStyle.setConversationTitle(this.mConversationTitle);
                }
                if (Build$VERSION.SDK_INT >= 28) {
                    notification$MessagingStyle.setGroupConversation((boolean)this.mIsGroupConversation);
                }
                for (final Message message : this.mMessages) {
                    Notification$MessagingStyle$Message notification$MessagingStyle$Message;
                    if (Build$VERSION.SDK_INT >= 28) {
                        final Person person = message.getPerson();
                        final CharSequence text = message.getText();
                        final long timestamp = message.getTimestamp();
                        android.app.Person androidPerson;
                        if (person == null) {
                            androidPerson = null;
                        }
                        else {
                            androidPerson = person.toAndroidPerson();
                        }
                        notification$MessagingStyle$Message = new Notification$MessagingStyle$Message(text, timestamp, androidPerson);
                    }
                    else {
                        CharSequence name = null;
                        if (message.getPerson() != null) {
                            name = message.getPerson().getName();
                        }
                        notification$MessagingStyle$Message = new Notification$MessagingStyle$Message(message.getText(), message.getTimestamp(), name);
                    }
                    if (message.getDataMimeType() != null) {
                        notification$MessagingStyle$Message.setData(message.getDataMimeType(), message.getDataUri());
                    }
                    notification$MessagingStyle.addMessage(notification$MessagingStyle$Message);
                }
                notification$MessagingStyle.setBuilder(notificationBuilderWithBuilderAccessor.getBuilder());
                return;
            }
            final Message latestIncomingMessage = this.findLatestIncomingMessage();
            if (this.mConversationTitle != null && this.mIsGroupConversation) {
                notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle(this.mConversationTitle);
            }
            else if (latestIncomingMessage != null) {
                notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle((CharSequence)"");
                if (latestIncomingMessage.getPerson() != null) {
                    notificationBuilderWithBuilderAccessor.getBuilder().setContentTitle(latestIncomingMessage.getPerson().getName());
                }
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
                new Notification$BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle((CharSequence)null).bigText((CharSequence)spannableStringBuilder);
            }
        }
        
        public CharSequence getConversationTitle() {
            return this.mConversationTitle;
        }
        
        public List<Message> getMessages() {
            return this.mMessages;
        }
        
        public Person getUser() {
            return this.mUser;
        }
        
        @Deprecated
        public CharSequence getUserDisplayName() {
            return this.mUser.getName();
        }
        
        public boolean isGroupConversation() {
            final NotificationCompat.Builder mBuilder = this.mBuilder;
            final boolean b = false;
            boolean b2 = false;
            if (mBuilder != null && this.mBuilder.mContext.getApplicationInfo().targetSdkVersion < 28 && this.mIsGroupConversation == null) {
                if (this.mConversationTitle != null) {
                    b2 = true;
                }
                return b2;
            }
            final Boolean mIsGroupConversation = this.mIsGroupConversation;
            boolean booleanValue = b;
            if (mIsGroupConversation != null) {
                booleanValue = mIsGroupConversation;
            }
            return booleanValue;
        }
        
        @Override
        protected void restoreFromCompatExtras(final Bundle bundle) {
            this.mMessages.clear();
            if (bundle.containsKey("android.messagingStyleUser")) {
                this.mUser = Person.fromBundle(bundle.getBundle("android.messagingStyleUser"));
            }
            else {
                this.mUser = new Person.Builder().setName(bundle.getString("android.selfDisplayName")).build();
            }
            final CharSequence charSequence = bundle.getCharSequence("android.conversationTitle");
            this.mConversationTitle = charSequence;
            if (charSequence == null) {
                this.mConversationTitle = bundle.getCharSequence("android.hiddenConversationTitle");
            }
            final Parcelable[] parcelableArray = bundle.getParcelableArray("android.messages");
            if (parcelableArray != null) {
                this.mMessages.addAll(Message.getMessagesFromBundleArray(parcelableArray));
            }
            if (bundle.containsKey("android.isGroupConversation")) {
                this.mIsGroupConversation = bundle.getBoolean("android.isGroupConversation");
            }
        }
        
        public MessagingStyle setConversationTitle(final CharSequence mConversationTitle) {
            this.mConversationTitle = mConversationTitle;
            return this;
        }
        
        public MessagingStyle setGroupConversation(final boolean b) {
            this.mIsGroupConversation = b;
            return this;
        }
        
        public static final class Message
        {
            static final String KEY_DATA_MIME_TYPE = "type";
            static final String KEY_DATA_URI = "uri";
            static final String KEY_EXTRAS_BUNDLE = "extras";
            static final String KEY_NOTIFICATION_PERSON = "sender_person";
            static final String KEY_PERSON = "person";
            static final String KEY_SENDER = "sender";
            static final String KEY_TEXT = "text";
            static final String KEY_TIMESTAMP = "time";
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras;
            private final Person mPerson;
            private final CharSequence mText;
            private final long mTimestamp;
            
            public Message(final CharSequence mText, final long mTimestamp, final Person mPerson) {
                this.mExtras = new Bundle();
                this.mText = mText;
                this.mTimestamp = mTimestamp;
                this.mPerson = mPerson;
            }
            
            @Deprecated
            public Message(final CharSequence charSequence, final long n, final CharSequence name) {
                this(charSequence, n, new Person.Builder().setName(name).build());
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
                    Person person = null;
                    if (bundle.containsKey("person")) {
                        person = Person.fromBundle(bundle.getBundle("person"));
                    }
                    else if (bundle.containsKey("sender_person") && Build$VERSION.SDK_INT >= 28) {
                        person = Person.fromAndroidPerson((android.app.Person)bundle.getParcelable("sender_person"));
                    }
                    else if (bundle.containsKey("sender")) {
                        person = new Person.Builder().setName(bundle.getCharSequence("sender")).build();
                    }
                    final Message message = new Message(bundle.getCharSequence("text"), bundle.getLong("time"), person);
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
                final Person mPerson = this.mPerson;
                if (mPerson != null) {
                    bundle.putCharSequence("sender", mPerson.getName());
                    if (Build$VERSION.SDK_INT >= 28) {
                        bundle.putParcelable("sender_person", (Parcelable)this.mPerson.toAndroidPerson());
                    }
                    else {
                        bundle.putBundle("person", this.mPerson.toBundle());
                    }
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
            
            public Person getPerson() {
                return this.mPerson;
            }
            
            @Deprecated
            public CharSequence getSender() {
                final Person mPerson = this.mPerson;
                if (mPerson == null) {
                    return null;
                }
                return mPerson.getName();
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
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationVisibility {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface StreamType {
    }
    
    public abstract static class Style
    {
        CharSequence mBigContentTitle;
        protected NotificationCompat.Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet;
        
        public Style() {
            this.mSummaryTextSet = false;
        }
        
        private int calculateTopPadding() {
            final Resources resources = this.mBuilder.mContext.getResources();
            final int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.notification_top_pad);
            final int dimensionPixelSize2 = resources.getDimensionPixelSize(R$dimen.notification_top_pad_large_text);
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
            final int notification_icon_background = R$drawable.notification_icon_background;
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
            remoteViews.setViewVisibility(R$id.title, 8);
            remoteViews.setViewVisibility(R$id.text2, 8);
            remoteViews.setViewVisibility(R$id.text, 8);
        }
        
        public void addCompatExtras(final Bundle bundle) {
        }
        
        public void apply(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
        }
        
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
                    remoteViews.setInt(R$id.notification_background, "setBackgroundResource", R$drawable.notification_bg_low);
                    remoteViews.setInt(R$id.icon, "setBackgroundResource", R$drawable.notification_template_icon_low_bg);
                }
                else {
                    remoteViews.setInt(R$id.notification_background, "setBackgroundResource", R$drawable.notification_bg);
                    remoteViews.setInt(R$id.icon, "setBackgroundResource", R$drawable.notification_template_icon_bg);
                }
            }
            if (this.mBuilder.mLargeIcon != null) {
                if (Build$VERSION.SDK_INT >= 16) {
                    remoteViews.setViewVisibility(R$id.icon, 0);
                    remoteViews.setImageViewBitmap(R$id.icon, this.mBuilder.mLargeIcon);
                }
                else {
                    remoteViews.setViewVisibility(R$id.icon, 8);
                }
                if (b && this.mBuilder.mNotification.icon != 0) {
                    n = resources.getDimensionPixelSize(R$dimen.notification_right_icon_size);
                    final int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.notification_small_icon_background_padding);
                    if (Build$VERSION.SDK_INT >= 21) {
                        remoteViews.setImageViewBitmap(R$id.right_icon, this.createIconWithBackground(this.mBuilder.mNotification.icon, n, n - dimensionPixelSize * 2, this.mBuilder.getColor()));
                    }
                    else {
                        remoteViews.setImageViewBitmap(R$id.right_icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
                    }
                    remoteViews.setViewVisibility(R$id.right_icon, 0);
                }
            }
            else if (b && this.mBuilder.mNotification.icon != 0) {
                remoteViews.setViewVisibility(R$id.icon, 0);
                if (Build$VERSION.SDK_INT >= 21) {
                    n = resources.getDimensionPixelSize(R$dimen.notification_large_icon_width);
                    remoteViews.setImageViewBitmap(R$id.icon, this.createIconWithBackground(this.mBuilder.mNotification.icon, n - resources.getDimensionPixelSize(R$dimen.notification_big_circle_margin), resources.getDimensionPixelSize(R$dimen.notification_small_icon_size_as_large), this.mBuilder.getColor()));
                }
                else {
                    remoteViews.setImageViewBitmap(R$id.icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
                }
            }
            if (this.mBuilder.mContentTitle != null) {
                remoteViews.setTextViewText(R$id.title, this.mBuilder.mContentTitle);
            }
            if (this.mBuilder.mContentText != null) {
                remoteViews.setTextViewText(R$id.text, this.mBuilder.mContentText);
                b3 = true;
            }
            if (Build$VERSION.SDK_INT < 21 && this.mBuilder.mLargeIcon != null) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (this.mBuilder.mContentInfo != null) {
                remoteViews.setTextViewText(R$id.info, this.mBuilder.mContentInfo);
                remoteViews.setViewVisibility(R$id.info, 0);
                b3 = true;
                n = 1;
            }
            else if (this.mBuilder.mNumber > 0) {
                n = resources.getInteger(R$integer.status_bar_notification_info_maxnum);
                if (this.mBuilder.mNumber > n) {
                    remoteViews.setTextViewText(R$id.info, (CharSequence)resources.getString(R$string.status_bar_notification_info_overflow));
                }
                else {
                    remoteViews.setTextViewText(R$id.info, (CharSequence)NumberFormat.getIntegerInstance().format(this.mBuilder.mNumber));
                }
                remoteViews.setViewVisibility(R$id.info, 0);
                b3 = true;
                n = 1;
            }
            else {
                remoteViews.setViewVisibility(R$id.info, 8);
            }
            boolean b4 = false;
            Label_0746: {
                if (this.mBuilder.mSubText != null && Build$VERSION.SDK_INT >= 16) {
                    remoteViews.setTextViewText(R$id.text, this.mBuilder.mSubText);
                    if (this.mBuilder.mContentText != null) {
                        remoteViews.setTextViewText(R$id.text2, this.mBuilder.mContentText);
                        remoteViews.setViewVisibility(R$id.text2, 0);
                        b4 = true;
                        break Label_0746;
                    }
                    remoteViews.setViewVisibility(R$id.text2, 8);
                }
                b4 = false;
            }
            if (b4 && Build$VERSION.SDK_INT >= 16) {
                if (b2) {
                    remoteViews.setTextViewTextSize(R$id.text, 0, (float)resources.getDimensionPixelSize(R$dimen.notification_subtext_size));
                }
                remoteViews.setViewPadding(R$id.line1, 0, 0, 0, 0);
            }
            if (this.mBuilder.getWhenIfShowing() != 0L) {
                if (this.mBuilder.mUseChronometer && Build$VERSION.SDK_INT >= 16) {
                    remoteViews.setViewVisibility(R$id.chronometer, 0);
                    remoteViews.setLong(R$id.chronometer, "setBase", this.mBuilder.getWhenIfShowing() + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
                    remoteViews.setBoolean(R$id.chronometer, "setStarted", true);
                }
                else {
                    remoteViews.setViewVisibility(R$id.time, 0);
                    remoteViews.setLong(R$id.time, "setTime", this.mBuilder.getWhenIfShowing());
                }
                n = 1;
            }
            final int right_side = R$id.right_side;
            if (n != 0) {
                n = 0;
            }
            else {
                n = 8;
            }
            remoteViews.setViewVisibility(right_side, n);
            final int line3 = R$id.line3;
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
        
        public void buildIntoRemoteViews(final RemoteViews remoteViews, final RemoteViews remoteViews2) {
            this.hideNormalContent(remoteViews);
            remoteViews.removeAllViews(R$id.notification_main_column);
            remoteViews.addView(R$id.notification_main_column, remoteViews2.clone());
            remoteViews.setViewVisibility(R$id.notification_main_column, 0);
            if (Build$VERSION.SDK_INT >= 21) {
                remoteViews.setViewPadding(R$id.notification_main_column_container, 0, this.calculateTopPadding(), 0, 0);
            }
        }
        
        public Bitmap createColoredBitmap(final int n, final int n2) {
            return this.createColoredBitmap(n, n2, 0);
        }
        
        public RemoteViews makeBigContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        public RemoteViews makeContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        public RemoteViews makeHeadsUpContentView(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            return null;
        }
        
        protected void restoreFromCompatExtras(final Bundle bundle) {
        }
        
        public void setBuilder(final NotificationCompat.Builder mBuilder) {
            if (this.mBuilder != mBuilder && (this.mBuilder = mBuilder) != null) {
                mBuilder.setStyle(this);
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
        @Deprecated
        public static final int SCREEN_TIMEOUT_LONG = -1;
        @Deprecated
        public static final int SCREEN_TIMEOUT_SHORT = 0;
        @Deprecated
        public static final int SIZE_DEFAULT = 0;
        @Deprecated
        public static final int SIZE_FULL_SCREEN = 5;
        @Deprecated
        public static final int SIZE_LARGE = 4;
        @Deprecated
        public static final int SIZE_MEDIUM = 3;
        @Deprecated
        public static final int SIZE_SMALL = 2;
        @Deprecated
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
                final ArrayList parcelableArrayList = bundle.getParcelableArrayList("actions");
                if (Build$VERSION.SDK_INT >= 16 && parcelableArrayList != null) {
                    final Action[] array = new Action[parcelableArrayList.size()];
                    for (int i = 0; i < array.length; ++i) {
                        if (Build$VERSION.SDK_INT >= 20) {
                            array[i] = NotificationCompat.getActionCompatFromAction(parcelableArrayList.get(i));
                        }
                        else if (Build$VERSION.SDK_INT >= 16) {
                            array[i] = NotificationCompatJellybean.getActionFromBundle((Bundle)parcelableArrayList.get(i));
                        }
                    }
                    Collections.addAll(this.mActions, (Action[])array);
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
        
        private static Notification$Action getActionFromActionCompat(final Action action) {
            final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
            Bundle bundle;
            if (action.getExtras() != null) {
                bundle = new Bundle(action.getExtras());
            }
            else {
                bundle = new Bundle();
            }
            bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
            if (Build$VERSION.SDK_INT >= 24) {
                notification$Action$Builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
            }
            notification$Action$Builder.addExtras(bundle);
            final RemoteInput[] remoteInputs = action.getRemoteInputs();
            if (remoteInputs != null) {
                final android.app.RemoteInput[] fromCompat = RemoteInput.fromCompat(remoteInputs);
                for (int length = fromCompat.length, i = 0; i < length; ++i) {
                    notification$Action$Builder.addRemoteInput(fromCompat[i]);
                }
            }
            return notification$Action$Builder.build();
        }
        
        private void setFlag(final int n, final boolean b) {
            if (b) {
                this.mFlags |= n;
                return;
            }
            this.mFlags &= n;
        }
        
        public WearableExtender addAction(final Action action) {
            this.mActions.add(action);
            return this;
        }
        
        public WearableExtender addActions(final List<Action> list) {
            this.mActions.addAll(list);
            return this;
        }
        
        @Deprecated
        public WearableExtender addPage(final Notification notification) {
            this.mPages.add(notification);
            return this;
        }
        
        @Deprecated
        public WearableExtender addPages(final List<Notification> list) {
            this.mPages.addAll(list);
            return this;
        }
        
        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }
        
        @Deprecated
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
                if (Build$VERSION.SDK_INT >= 16) {
                    final ArrayList<Bundle> list = new ArrayList<Bundle>(this.mActions.size());
                    for (final Action action : this.mActions) {
                        if (Build$VERSION.SDK_INT >= 20) {
                            list.add((Bundle)getActionFromActionCompat(action));
                        }
                        else {
                            if (Build$VERSION.SDK_INT < 16) {
                                continue;
                            }
                            list.add(NotificationCompatJellybean.getBundleForAction(action));
                        }
                    }
                    bundle.putParcelableArrayList("actions", (ArrayList)list);
                }
                else {
                    bundle.putParcelableArrayList("actions", (ArrayList)null);
                }
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
        
        @Deprecated
        public Bitmap getBackground() {
            return this.mBackground;
        }
        
        public String getBridgeTag() {
            return this.mBridgeTag;
        }
        
        public int getContentAction() {
            return this.mContentActionIndex;
        }
        
        @Deprecated
        public int getContentIcon() {
            return this.mContentIcon;
        }
        
        @Deprecated
        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }
        
        public boolean getContentIntentAvailableOffline() {
            return (this.mFlags & 0x1) != 0x0;
        }
        
        @Deprecated
        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }
        
        @Deprecated
        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }
        
        public String getDismissalId() {
            return this.mDismissalId;
        }
        
        @Deprecated
        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }
        
        @Deprecated
        public int getGravity() {
            return this.mGravity;
        }
        
        @Deprecated
        public boolean getHintAmbientBigPicture() {
            return (this.mFlags & 0x20) != 0x0;
        }
        
        @Deprecated
        public boolean getHintAvoidBackgroundClipping() {
            return (this.mFlags & 0x10) != 0x0;
        }
        
        public boolean getHintContentIntentLaunchesActivity() {
            return (this.mFlags & 0x40) != 0x0;
        }
        
        @Deprecated
        public boolean getHintHideIcon() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        @Deprecated
        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }
        
        @Deprecated
        public boolean getHintShowBackgroundOnly() {
            return (this.mFlags & 0x4) != 0x0;
        }
        
        @Deprecated
        public List<Notification> getPages() {
            return this.mPages;
        }
        
        public boolean getStartScrollBottom() {
            return (this.mFlags & 0x8) != 0x0;
        }
        
        @Deprecated
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
        
        @Deprecated
        public WearableExtender setContentIcon(final int mContentIcon) {
            this.mContentIcon = mContentIcon;
            return this;
        }
        
        @Deprecated
        public WearableExtender setContentIconGravity(final int mContentIconGravity) {
            this.mContentIconGravity = mContentIconGravity;
            return this;
        }
        
        public WearableExtender setContentIntentAvailableOffline(final boolean b) {
            this.setFlag(1, b);
            return this;
        }
        
        @Deprecated
        public WearableExtender setCustomContentHeight(final int mCustomContentHeight) {
            this.mCustomContentHeight = mCustomContentHeight;
            return this;
        }
        
        @Deprecated
        public WearableExtender setCustomSizePreset(final int mCustomSizePreset) {
            this.mCustomSizePreset = mCustomSizePreset;
            return this;
        }
        
        public WearableExtender setDismissalId(final String mDismissalId) {
            this.mDismissalId = mDismissalId;
            return this;
        }
        
        @Deprecated
        public WearableExtender setDisplayIntent(final PendingIntent mDisplayIntent) {
            this.mDisplayIntent = mDisplayIntent;
            return this;
        }
        
        @Deprecated
        public WearableExtender setGravity(final int mGravity) {
            this.mGravity = mGravity;
            return this;
        }
        
        @Deprecated
        public WearableExtender setHintAmbientBigPicture(final boolean b) {
            this.setFlag(32, b);
            return this;
        }
        
        @Deprecated
        public WearableExtender setHintAvoidBackgroundClipping(final boolean b) {
            this.setFlag(16, b);
            return this;
        }
        
        public WearableExtender setHintContentIntentLaunchesActivity(final boolean b) {
            this.setFlag(64, b);
            return this;
        }
        
        @Deprecated
        public WearableExtender setHintHideIcon(final boolean b) {
            this.setFlag(2, b);
            return this;
        }
        
        @Deprecated
        public WearableExtender setHintScreenTimeout(final int mHintScreenTimeout) {
            this.mHintScreenTimeout = mHintScreenTimeout;
            return this;
        }
        
        @Deprecated
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
