package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.compat.R$color;
import android.support.compat.R$dimen;
import android.support.compat.R$drawable;
import android.support.compat.R$id;
import android.support.compat.R$integer;
import android.support.compat.R$layout;
import android.support.compat.R$string;
import android.support.v4.text.BidiFormatter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.RemoteViews;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NotificationCompat {
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
   static final NotificationCompat.NotificationCompatImpl IMPL;
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
      if (VERSION.SDK_INT >= 26) {
         IMPL = new NotificationCompat.NotificationCompatApi26Impl();
      } else if (VERSION.SDK_INT >= 24) {
         IMPL = new NotificationCompat.NotificationCompatApi24Impl();
      } else if (VERSION.SDK_INT >= 21) {
         IMPL = new NotificationCompat.NotificationCompatApi21Impl();
      } else if (VERSION.SDK_INT >= 20) {
         IMPL = new NotificationCompat.NotificationCompatApi20Impl();
      } else if (VERSION.SDK_INT >= 19) {
         IMPL = new NotificationCompat.NotificationCompatApi19Impl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new NotificationCompat.NotificationCompatApi16Impl();
      } else {
         IMPL = new NotificationCompat.NotificationCompatBaseImpl();
      }
   }

   static void addActionsToBuilder(NotificationBuilderWithActions var0, ArrayList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         var0.addAction((NotificationCompat.Action)var2.next());
      }

   }

   public static NotificationCompat.Action getAction(Notification var0, int var1) {
      return IMPL.getAction(var0, var1);
   }

   public static int getActionCount(Notification var0) {
      int var2 = VERSION.SDK_INT;
      int var1 = 0;
      if (var2 >= 19) {
         if (var0.actions != null) {
            var1 = var0.actions.length;
         }

         return var1;
      } else {
         return VERSION.SDK_INT >= 16 ? NotificationCompatJellybean.getActionCount(var0) : 0;
      }
   }

   public static int getBadgeIconType(Notification var0) {
      return VERSION.SDK_INT >= 26 ? var0.getBadgeIconType() : 0;
   }

   public static String getCategory(Notification var0) {
      return VERSION.SDK_INT >= 21 ? var0.category : null;
   }

   public static String getChannelId(Notification var0) {
      return VERSION.SDK_INT >= 26 ? var0.getChannelId() : null;
   }

   public static Bundle getExtras(Notification var0) {
      if (VERSION.SDK_INT >= 19) {
         return var0.extras;
      } else {
         return VERSION.SDK_INT >= 16 ? NotificationCompatJellybean.getExtras(var0) : null;
      }
   }

   public static String getGroup(Notification var0) {
      if (VERSION.SDK_INT >= 20) {
         return var0.getGroup();
      } else if (VERSION.SDK_INT >= 19) {
         return var0.extras.getString("android.support.groupKey");
      } else {
         return VERSION.SDK_INT >= 16 ? NotificationCompatJellybean.getExtras(var0).getString("android.support.groupKey") : null;
      }
   }

   public static int getGroupAlertBehavior(Notification var0) {
      return VERSION.SDK_INT >= 26 ? var0.getGroupAlertBehavior() : 0;
   }

   public static boolean getLocalOnly(Notification var0) {
      int var1 = VERSION.SDK_INT;
      boolean var2 = false;
      if (var1 >= 20) {
         if ((var0.flags & 256) != 0) {
            var2 = true;
         }

         return var2;
      } else if (VERSION.SDK_INT >= 19) {
         return var0.extras.getBoolean("android.support.localOnly");
      } else {
         return VERSION.SDK_INT >= 16 ? NotificationCompatJellybean.getExtras(var0).getBoolean("android.support.localOnly") : false;
      }
   }

   static Notification[] getNotificationArrayFromBundle(Bundle var0, String var1) {
      Parcelable[] var3 = var0.getParcelableArray(var1);
      if (!(var3 instanceof Notification[]) && var3 != null) {
         Notification[] var4 = new Notification[var3.length];

         for(int var2 = 0; var2 < var3.length; ++var2) {
            var4[var2] = (Notification)var3[var2];
         }

         var0.putParcelableArray(var1, var4);
         return var4;
      } else {
         return (Notification[])((Notification[])var3);
      }
   }

   public static String getShortcutId(Notification var0) {
      return VERSION.SDK_INT >= 26 ? var0.getShortcutId() : null;
   }

   public static String getSortKey(Notification var0) {
      if (VERSION.SDK_INT >= 20) {
         return var0.getSortKey();
      } else if (VERSION.SDK_INT >= 19) {
         return var0.extras.getString("android.support.sortKey");
      } else {
         return VERSION.SDK_INT >= 16 ? NotificationCompatJellybean.getExtras(var0).getString("android.support.sortKey") : null;
      }
   }

   public static long getTimeoutAfter(Notification var0) {
      return VERSION.SDK_INT >= 26 ? var0.getTimeoutAfter() : 0L;
   }

   public static boolean isGroupSummary(Notification var0) {
      int var1 = VERSION.SDK_INT;
      boolean var2 = false;
      if (var1 >= 20) {
         if ((var0.flags & 512) != 0) {
            var2 = true;
         }

         return var2;
      } else if (VERSION.SDK_INT >= 19) {
         return var0.extras.getBoolean("android.support.isGroupSummary");
      } else {
         return VERSION.SDK_INT >= 16 ? NotificationCompatJellybean.getExtras(var0).getBoolean("android.support.isGroupSummary") : false;
      }
   }

   public static class Action extends NotificationCompatBase.Action {
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public static final NotificationCompatBase.Action.Factory FACTORY = new NotificationCompatBase.Action.Factory() {
         public NotificationCompatBase.Action build(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInputCompatBase.RemoteInput[] var5, RemoteInputCompatBase.RemoteInput[] var6, boolean var7) {
            return new NotificationCompat.Action(var1, var2, var3, var4, (RemoteInput[])((RemoteInput[])var5), (RemoteInput[])((RemoteInput[])var6), var7);
         }

         public NotificationCompat.Action[] newArray(int var1) {
            return new NotificationCompat.Action[var1];
         }
      };
      public PendingIntent actionIntent;
      public int icon;
      private boolean mAllowGeneratedReplies;
      private final RemoteInput[] mDataOnlyRemoteInputs;
      final Bundle mExtras;
      private final RemoteInput[] mRemoteInputs;
      public CharSequence title;

      public Action(int var1, CharSequence var2, PendingIntent var3) {
         this(var1, var2, var3, new Bundle(), (RemoteInput[])null, (RemoteInput[])null, true);
      }

      Action(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInput[] var5, RemoteInput[] var6, boolean var7) {
         this.icon = var1;
         this.title = NotificationCompat.Builder.limitCharSequenceLength(var2);
         this.actionIntent = var3;
         if (var4 == null) {
            var4 = new Bundle();
         }

         this.mExtras = var4;
         this.mRemoteInputs = var5;
         this.mDataOnlyRemoteInputs = var6;
         this.mAllowGeneratedReplies = var7;
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

      public CharSequence getTitle() {
         return this.title;
      }

      public static final class Builder {
         private boolean mAllowGeneratedReplies;
         private final Bundle mExtras;
         private final int mIcon;
         private final PendingIntent mIntent;
         private ArrayList mRemoteInputs;
         private final CharSequence mTitle;

         public Builder(int var1, CharSequence var2, PendingIntent var3) {
            this(var1, var2, var3, new Bundle(), (RemoteInput[])null, true);
         }

         private Builder(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInput[] var5, boolean var6) {
            this.mAllowGeneratedReplies = true;
            this.mIcon = var1;
            this.mTitle = NotificationCompat.Builder.limitCharSequenceLength(var2);
            this.mIntent = var3;
            this.mExtras = var4;
            ArrayList var7;
            if (var5 == null) {
               var7 = null;
            } else {
               var7 = new ArrayList(Arrays.asList(var5));
            }

            this.mRemoteInputs = var7;
            this.mAllowGeneratedReplies = var6;
         }

         public Builder(NotificationCompat.Action var1) {
            this(var1.icon, var1.title, var1.actionIntent, new Bundle(var1.mExtras), var1.getRemoteInputs(), var1.getAllowGeneratedReplies());
         }

         public NotificationCompat.Action.Builder addExtras(Bundle var1) {
            if (var1 != null) {
               this.mExtras.putAll(var1);
            }

            return this;
         }

         public NotificationCompat.Action.Builder addRemoteInput(RemoteInput var1) {
            if (this.mRemoteInputs == null) {
               this.mRemoteInputs = new ArrayList();
            }

            this.mRemoteInputs.add(var1);
            return this;
         }

         public NotificationCompat.Action build() {
            ArrayList var1 = new ArrayList();
            ArrayList var2 = new ArrayList();
            ArrayList var3 = this.mRemoteInputs;
            if (var3 != null) {
               Iterator var7 = var3.iterator();

               while(var7.hasNext()) {
                  RemoteInput var4 = (RemoteInput)var7.next();
                  if (var4.isDataOnly()) {
                     var1.add(var4);
                  } else {
                     var2.add(var4);
                  }
               }
            }

            RemoteInput[] var5;
            if (var1.isEmpty()) {
               var5 = null;
            } else {
               var5 = (RemoteInput[])var1.toArray(new RemoteInput[var1.size()]);
            }

            RemoteInput[] var6;
            if (var2.isEmpty()) {
               var6 = null;
            } else {
               var6 = (RemoteInput[])var2.toArray(new RemoteInput[var2.size()]);
            }

            return new NotificationCompat.Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, var6, var5, this.mAllowGeneratedReplies);
         }

         public NotificationCompat.Action.Builder extend(NotificationCompat.Action.Extender var1) {
            var1.extend(this);
            return this;
         }

         public Bundle getExtras() {
            return this.mExtras;
         }

         public NotificationCompat.Action.Builder setAllowGeneratedReplies(boolean var1) {
            this.mAllowGeneratedReplies = var1;
            return this;
         }
      }

      public interface Extender {
         NotificationCompat.Action.Builder extend(NotificationCompat.Action.Builder var1);
      }

      public static final class WearableExtender implements NotificationCompat.Action.Extender {
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
         private int mFlags = 1;
         private CharSequence mInProgressLabel;

         public WearableExtender() {
         }

         public WearableExtender(NotificationCompat.Action var1) {
            Bundle var2 = var1.getExtras().getBundle("android.wearable.EXTENSIONS");
            if (var2 != null) {
               this.mFlags = var2.getInt("flags", 1);
               this.mInProgressLabel = var2.getCharSequence("inProgressLabel");
               this.mConfirmLabel = var2.getCharSequence("confirmLabel");
               this.mCancelLabel = var2.getCharSequence("cancelLabel");
            }

         }

         private void setFlag(int var1, boolean var2) {
            if (var2) {
               this.mFlags |= var1;
            } else {
               this.mFlags &= ~var1;
            }
         }

         public NotificationCompat.Action.WearableExtender clone() {
            NotificationCompat.Action.WearableExtender var1 = new NotificationCompat.Action.WearableExtender();
            var1.mFlags = this.mFlags;
            var1.mInProgressLabel = this.mInProgressLabel;
            var1.mConfirmLabel = this.mConfirmLabel;
            var1.mCancelLabel = this.mCancelLabel;
            return var1;
         }

         public NotificationCompat.Action.Builder extend(NotificationCompat.Action.Builder var1) {
            Bundle var3 = new Bundle();
            int var2 = this.mFlags;
            if (var2 != 1) {
               var3.putInt("flags", var2);
            }

            CharSequence var4 = this.mInProgressLabel;
            if (var4 != null) {
               var3.putCharSequence("inProgressLabel", var4);
            }

            var4 = this.mConfirmLabel;
            if (var4 != null) {
               var3.putCharSequence("confirmLabel", var4);
            }

            var4 = this.mCancelLabel;
            if (var4 != null) {
               var3.putCharSequence("cancelLabel", var4);
            }

            var1.getExtras().putBundle("android.wearable.EXTENSIONS", var3);
            return var1;
         }

         public CharSequence getCancelLabel() {
            return this.mCancelLabel;
         }

         public CharSequence getConfirmLabel() {
            return this.mConfirmLabel;
         }

         public boolean getHintDisplayActionInline() {
            return (this.mFlags & 4) != 0;
         }

         public boolean getHintLaunchesActivity() {
            return (this.mFlags & 2) != 0;
         }

         public CharSequence getInProgressLabel() {
            return this.mInProgressLabel;
         }

         public boolean isAvailableOffline() {
            return (this.mFlags & 1) != 0;
         }

         public NotificationCompat.Action.WearableExtender setAvailableOffline(boolean var1) {
            this.setFlag(1, var1);
            return this;
         }

         public NotificationCompat.Action.WearableExtender setCancelLabel(CharSequence var1) {
            this.mCancelLabel = var1;
            return this;
         }

         public NotificationCompat.Action.WearableExtender setConfirmLabel(CharSequence var1) {
            this.mConfirmLabel = var1;
            return this;
         }

         public NotificationCompat.Action.WearableExtender setHintDisplayActionInline(boolean var1) {
            this.setFlag(4, var1);
            return this;
         }

         public NotificationCompat.Action.WearableExtender setHintLaunchesActivity(boolean var1) {
            this.setFlag(2, var1);
            return this;
         }

         public NotificationCompat.Action.WearableExtender setInProgressLabel(CharSequence var1) {
            this.mInProgressLabel = var1;
            return this;
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface BadgeIconType {
   }

   public static class BigPictureStyle extends NotificationCompat.Style {
      private Bitmap mBigLargeIcon;
      private boolean mBigLargeIconSet;
      private Bitmap mPicture;

      public BigPictureStyle() {
      }

      public BigPictureStyle(NotificationCompat.Builder var1) {
         this.setBuilder(var1);
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            NotificationCompatJellybean.addBigPictureStyle(var1, this.mBigContentTitle, this.mSummaryTextSet, this.mSummaryText, this.mPicture, this.mBigLargeIcon, this.mBigLargeIconSet);
         }

      }

      public NotificationCompat.BigPictureStyle bigLargeIcon(Bitmap var1) {
         this.mBigLargeIcon = var1;
         this.mBigLargeIconSet = true;
         return this;
      }

      public NotificationCompat.BigPictureStyle bigPicture(Bitmap var1) {
         this.mPicture = var1;
         return this;
      }

      public NotificationCompat.BigPictureStyle setBigContentTitle(CharSequence var1) {
         this.mBigContentTitle = NotificationCompat.Builder.limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.BigPictureStyle setSummaryText(CharSequence var1) {
         this.mSummaryText = NotificationCompat.Builder.limitCharSequenceLength(var1);
         this.mSummaryTextSet = true;
         return this;
      }
   }

   public static class BigTextStyle extends NotificationCompat.Style {
      private CharSequence mBigText;

      public BigTextStyle() {
      }

      public BigTextStyle(NotificationCompat.Builder var1) {
         this.setBuilder(var1);
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            NotificationCompatJellybean.addBigTextStyle(var1, this.mBigContentTitle, this.mSummaryTextSet, this.mSummaryText, this.mBigText);
         }

      }

      public NotificationCompat.BigTextStyle bigText(CharSequence var1) {
         this.mBigText = NotificationCompat.Builder.limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.BigTextStyle setBigContentTitle(CharSequence var1) {
         this.mBigContentTitle = NotificationCompat.Builder.limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.BigTextStyle setSummaryText(CharSequence var1) {
         this.mSummaryText = NotificationCompat.Builder.limitCharSequenceLength(var1);
         this.mSummaryTextSet = true;
         return this;
      }
   }

   public static class Builder {
      private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public ArrayList mActions;
      int mBadgeIcon;
      RemoteViews mBigContentView;
      String mCategory;
      String mChannelId;
      int mColor;
      boolean mColorized;
      boolean mColorizedSet;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public CharSequence mContentInfo;
      PendingIntent mContentIntent;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public CharSequence mContentText;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public CharSequence mContentTitle;
      RemoteViews mContentView;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public Context mContext;
      Bundle mExtras;
      PendingIntent mFullScreenIntent;
      private int mGroupAlertBehavior;
      String mGroupKey;
      boolean mGroupSummary;
      RemoteViews mHeadsUpContentView;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public Bitmap mLargeIcon;
      boolean mLocalOnly;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public Notification mNotification;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public int mNumber;
      public ArrayList mPeople;
      int mPriority;
      int mProgress;
      boolean mProgressIndeterminate;
      int mProgressMax;
      Notification mPublicVersion;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public CharSequence[] mRemoteInputHistory;
      String mShortcutId;
      boolean mShowWhen;
      String mSortKey;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public NotificationCompat.Style mStyle;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public CharSequence mSubText;
      RemoteViews mTickerView;
      long mTimeout;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public boolean mUseChronometer;
      int mVisibility;

      @Deprecated
      public Builder(Context var1) {
         this(var1, (String)null);
      }

      public Builder(@NonNull Context var1, @NonNull String var2) {
         this.mShowWhen = true;
         this.mActions = new ArrayList();
         this.mLocalOnly = false;
         this.mColor = 0;
         this.mVisibility = 0;
         this.mBadgeIcon = 0;
         this.mGroupAlertBehavior = 0;
         this.mNotification = new Notification();
         this.mContext = var1;
         this.mChannelId = var2;
         this.mNotification.when = System.currentTimeMillis();
         this.mNotification.audioStreamType = -1;
         this.mPriority = 0;
         this.mPeople = new ArrayList();
      }

      protected static CharSequence limitCharSequenceLength(CharSequence var0) {
         if (var0 == null) {
            return var0;
         } else {
            CharSequence var1 = var0;
            if (var0.length() > 5120) {
               var1 = var0.subSequence(0, 5120);
            }

            return var1;
         }
      }

      private void setFlag(int var1, boolean var2) {
         Notification var3;
         if (var2) {
            var3 = this.mNotification;
            var3.flags |= var1;
         } else {
            var3 = this.mNotification;
            var3.flags &= ~var1;
         }
      }

      public NotificationCompat.Builder addAction(int var1, CharSequence var2, PendingIntent var3) {
         this.mActions.add(new NotificationCompat.Action(var1, var2, var3));
         return this;
      }

      public NotificationCompat.Builder addAction(NotificationCompat.Action var1) {
         this.mActions.add(var1);
         return this;
      }

      public NotificationCompat.Builder addExtras(Bundle var1) {
         if (var1 != null) {
            Bundle var2 = this.mExtras;
            if (var2 == null) {
               this.mExtras = new Bundle(var1);
               return this;
            }

            var2.putAll(var1);
         }

         return this;
      }

      public NotificationCompat.Builder addPerson(String var1) {
         this.mPeople.add(var1);
         return this;
      }

      public Notification build() {
         return NotificationCompat.IMPL.build(this, this.getExtender());
      }

      public NotificationCompat.Builder extend(NotificationCompat.Extender var1) {
         var1.extend(this);
         return this;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews getBigContentView() {
         return this.mBigContentView;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public int getColor() {
         return this.mColor;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews getContentView() {
         return this.mContentView;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      protected NotificationCompat.BuilderExtender getExtender() {
         return new NotificationCompat.BuilderExtender();
      }

      public Bundle getExtras() {
         if (this.mExtras == null) {
            this.mExtras = new Bundle();
         }

         return this.mExtras;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews getHeadsUpContentView() {
         return this.mHeadsUpContentView;
      }

      @Deprecated
      public Notification getNotification() {
         return this.build();
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public int getPriority() {
         return this.mPriority;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public long getWhenIfShowing() {
         return this.mShowWhen ? this.mNotification.when : 0L;
      }

      public NotificationCompat.Builder setAutoCancel(boolean var1) {
         this.setFlag(16, var1);
         return this;
      }

      public NotificationCompat.Builder setBadgeIconType(int var1) {
         this.mBadgeIcon = var1;
         return this;
      }

      public NotificationCompat.Builder setCategory(String var1) {
         this.mCategory = var1;
         return this;
      }

      public NotificationCompat.Builder setChannelId(@NonNull String var1) {
         this.mChannelId = var1;
         return this;
      }

      public NotificationCompat.Builder setColor(@ColorInt int var1) {
         this.mColor = var1;
         return this;
      }

      public NotificationCompat.Builder setColorized(boolean var1) {
         this.mColorized = var1;
         this.mColorizedSet = true;
         return this;
      }

      public NotificationCompat.Builder setContent(RemoteViews var1) {
         this.mNotification.contentView = var1;
         return this;
      }

      public NotificationCompat.Builder setContentInfo(CharSequence var1) {
         this.mContentInfo = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setContentIntent(PendingIntent var1) {
         this.mContentIntent = var1;
         return this;
      }

      public NotificationCompat.Builder setContentText(CharSequence var1) {
         this.mContentText = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setContentTitle(CharSequence var1) {
         this.mContentTitle = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setCustomBigContentView(RemoteViews var1) {
         this.mBigContentView = var1;
         return this;
      }

      public NotificationCompat.Builder setCustomContentView(RemoteViews var1) {
         this.mContentView = var1;
         return this;
      }

      public NotificationCompat.Builder setCustomHeadsUpContentView(RemoteViews var1) {
         this.mHeadsUpContentView = var1;
         return this;
      }

      public NotificationCompat.Builder setDefaults(int var1) {
         Notification var2 = this.mNotification;
         var2.defaults = var1;
         if ((var1 & 4) != 0) {
            var2.flags |= 1;
         }

         return this;
      }

      public NotificationCompat.Builder setDeleteIntent(PendingIntent var1) {
         this.mNotification.deleteIntent = var1;
         return this;
      }

      public NotificationCompat.Builder setExtras(Bundle var1) {
         this.mExtras = var1;
         return this;
      }

      public NotificationCompat.Builder setFullScreenIntent(PendingIntent var1, boolean var2) {
         this.mFullScreenIntent = var1;
         this.setFlag(128, var2);
         return this;
      }

      public NotificationCompat.Builder setGroup(String var1) {
         this.mGroupKey = var1;
         return this;
      }

      public NotificationCompat.Builder setGroupAlertBehavior(int var1) {
         this.mGroupAlertBehavior = var1;
         return this;
      }

      public NotificationCompat.Builder setGroupSummary(boolean var1) {
         this.mGroupSummary = var1;
         return this;
      }

      public NotificationCompat.Builder setLargeIcon(Bitmap var1) {
         this.mLargeIcon = var1;
         return this;
      }

      public NotificationCompat.Builder setLights(@ColorInt int var1, int var2, int var3) {
         Notification var4 = this.mNotification;
         var4.ledARGB = var1;
         var4.ledOnMS = var2;
         var4.ledOffMS = var3;
         var1 = var4.ledOnMS;
         byte var6 = 1;
         boolean var5;
         if (var1 != 0 && this.mNotification.ledOffMS != 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         var4 = this.mNotification;
         var3 = var4.flags;
         byte var7;
         if (var5) {
            var7 = var6;
         } else {
            var7 = 0;
         }

         var4.flags = var7 | var3 & -2;
         return this;
      }

      public NotificationCompat.Builder setLocalOnly(boolean var1) {
         this.mLocalOnly = var1;
         return this;
      }

      public NotificationCompat.Builder setNumber(int var1) {
         this.mNumber = var1;
         return this;
      }

      public NotificationCompat.Builder setOngoing(boolean var1) {
         this.setFlag(2, var1);
         return this;
      }

      public NotificationCompat.Builder setOnlyAlertOnce(boolean var1) {
         this.setFlag(8, var1);
         return this;
      }

      public NotificationCompat.Builder setPriority(int var1) {
         this.mPriority = var1;
         return this;
      }

      public NotificationCompat.Builder setProgress(int var1, int var2, boolean var3) {
         this.mProgressMax = var1;
         this.mProgress = var2;
         this.mProgressIndeterminate = var3;
         return this;
      }

      public NotificationCompat.Builder setPublicVersion(Notification var1) {
         this.mPublicVersion = var1;
         return this;
      }

      public NotificationCompat.Builder setRemoteInputHistory(CharSequence[] var1) {
         this.mRemoteInputHistory = var1;
         return this;
      }

      public NotificationCompat.Builder setShortcutId(String var1) {
         this.mShortcutId = var1;
         return this;
      }

      public NotificationCompat.Builder setShowWhen(boolean var1) {
         this.mShowWhen = var1;
         return this;
      }

      public NotificationCompat.Builder setSmallIcon(int var1) {
         this.mNotification.icon = var1;
         return this;
      }

      public NotificationCompat.Builder setSmallIcon(int var1, int var2) {
         Notification var3 = this.mNotification;
         var3.icon = var1;
         var3.iconLevel = var2;
         return this;
      }

      public NotificationCompat.Builder setSortKey(String var1) {
         this.mSortKey = var1;
         return this;
      }

      public NotificationCompat.Builder setSound(Uri var1) {
         Notification var2 = this.mNotification;
         var2.sound = var1;
         var2.audioStreamType = -1;
         return this;
      }

      public NotificationCompat.Builder setSound(Uri var1, int var2) {
         Notification var3 = this.mNotification;
         var3.sound = var1;
         var3.audioStreamType = var2;
         return this;
      }

      public NotificationCompat.Builder setStyle(NotificationCompat.Style var1) {
         if (this.mStyle != var1) {
            this.mStyle = var1;
            var1 = this.mStyle;
            if (var1 != null) {
               var1.setBuilder(this);
            }
         }

         return this;
      }

      public NotificationCompat.Builder setSubText(CharSequence var1) {
         this.mSubText = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setTicker(CharSequence var1) {
         this.mNotification.tickerText = limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.Builder setTicker(CharSequence var1, RemoteViews var2) {
         this.mNotification.tickerText = limitCharSequenceLength(var1);
         this.mTickerView = var2;
         return this;
      }

      public NotificationCompat.Builder setTimeoutAfter(long var1) {
         this.mTimeout = var1;
         return this;
      }

      public NotificationCompat.Builder setUsesChronometer(boolean var1) {
         this.mUseChronometer = var1;
         return this;
      }

      public NotificationCompat.Builder setVibrate(long[] var1) {
         this.mNotification.vibrate = var1;
         return this;
      }

      public NotificationCompat.Builder setVisibility(int var1) {
         this.mVisibility = var1;
         return this;
      }

      public NotificationCompat.Builder setWhen(long var1) {
         this.mNotification.when = var1;
         return this;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   protected static class BuilderExtender {
      public Notification build(NotificationCompat.Builder var1, NotificationBuilderWithBuilderAccessor var2) {
         RemoteViews var3;
         if (var1.mStyle != null) {
            var3 = var1.mStyle.makeContentView(var2);
         } else {
            var3 = null;
         }

         Notification var4 = var2.build();
         if (var3 != null) {
            var4.contentView = var3;
         } else if (var1.mContentView != null) {
            var4.contentView = var1.mContentView;
         }

         if (VERSION.SDK_INT >= 16 && var1.mStyle != null) {
            var3 = var1.mStyle.makeBigContentView(var2);
            if (var3 != null) {
               var4.bigContentView = var3;
            }
         }

         if (VERSION.SDK_INT >= 21 && var1.mStyle != null) {
            RemoteViews var5 = var1.mStyle.makeHeadsUpContentView(var2);
            if (var5 != null) {
               var4.headsUpContentView = var5;
            }
         }

         return var4;
      }
   }

   public static final class CarExtender implements NotificationCompat.Extender {
      private static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
      private static final String EXTRA_COLOR = "app_color";
      private static final String EXTRA_CONVERSATION = "car_conversation";
      private static final String EXTRA_LARGE_ICON = "large_icon";
      private static final String TAG = "CarExtender";
      private int mColor = 0;
      private Bitmap mLargeIcon;
      private NotificationCompat.CarExtender.UnreadConversation mUnreadConversation;

      public CarExtender() {
      }

      public CarExtender(Notification var1) {
         if (VERSION.SDK_INT >= 21) {
            Bundle var2;
            if (NotificationCompat.getExtras(var1) == null) {
               var2 = null;
            } else {
               var2 = NotificationCompat.getExtras(var1).getBundle("android.car.EXTENSIONS");
            }

            if (var2 != null) {
               this.mLargeIcon = (Bitmap)var2.getParcelable("large_icon");
               this.mColor = var2.getInt("app_color", 0);
               var2 = var2.getBundle("car_conversation");
               this.mUnreadConversation = (NotificationCompat.CarExtender.UnreadConversation)NotificationCompat.IMPL.getUnreadConversationFromBundle(var2, NotificationCompat.CarExtender.UnreadConversation.FACTORY, RemoteInput.FACTORY);
            }

         }
      }

      public NotificationCompat.Builder extend(NotificationCompat.Builder var1) {
         if (VERSION.SDK_INT < 21) {
            return var1;
         } else {
            Bundle var3 = new Bundle();
            Bitmap var4 = this.mLargeIcon;
            if (var4 != null) {
               var3.putParcelable("large_icon", var4);
            }

            int var2 = this.mColor;
            if (var2 != 0) {
               var3.putInt("app_color", var2);
            }

            if (this.mUnreadConversation != null) {
               var3.putBundle("car_conversation", NotificationCompat.IMPL.getBundleForUnreadConversation(this.mUnreadConversation));
            }

            var1.getExtras().putBundle("android.car.EXTENSIONS", var3);
            return var1;
         }
      }

      @ColorInt
      public int getColor() {
         return this.mColor;
      }

      public Bitmap getLargeIcon() {
         return this.mLargeIcon;
      }

      public NotificationCompat.CarExtender.UnreadConversation getUnreadConversation() {
         return this.mUnreadConversation;
      }

      public NotificationCompat.CarExtender setColor(@ColorInt int var1) {
         this.mColor = var1;
         return this;
      }

      public NotificationCompat.CarExtender setLargeIcon(Bitmap var1) {
         this.mLargeIcon = var1;
         return this;
      }

      public NotificationCompat.CarExtender setUnreadConversation(NotificationCompat.CarExtender.UnreadConversation var1) {
         this.mUnreadConversation = var1;
         return this;
      }

      public static class UnreadConversation extends NotificationCompatBase.UnreadConversation {
         static final NotificationCompatBase.UnreadConversation.Factory FACTORY = new NotificationCompatBase.UnreadConversation.Factory() {
            public NotificationCompat.CarExtender.UnreadConversation build(String[] var1, RemoteInputCompatBase.RemoteInput var2, PendingIntent var3, PendingIntent var4, String[] var5, long var6) {
               return new NotificationCompat.CarExtender.UnreadConversation(var1, (RemoteInput)var2, var3, var4, var5, var6);
            }
         };
         private final long mLatestTimestamp;
         private final String[] mMessages;
         private final String[] mParticipants;
         private final PendingIntent mReadPendingIntent;
         private final RemoteInput mRemoteInput;
         private final PendingIntent mReplyPendingIntent;

         UnreadConversation(String[] var1, RemoteInput var2, PendingIntent var3, PendingIntent var4, String[] var5, long var6) {
            this.mMessages = var1;
            this.mRemoteInput = var2;
            this.mReadPendingIntent = var4;
            this.mReplyPendingIntent = var3;
            this.mParticipants = var5;
            this.mLatestTimestamp = var6;
         }

         public long getLatestTimestamp() {
            return this.mLatestTimestamp;
         }

         public String[] getMessages() {
            return this.mMessages;
         }

         public String getParticipant() {
            String[] var1 = this.mParticipants;
            return var1.length > 0 ? var1[0] : null;
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

         public static class Builder {
            private long mLatestTimestamp;
            private final List mMessages = new ArrayList();
            private final String mParticipant;
            private PendingIntent mReadPendingIntent;
            private RemoteInput mRemoteInput;
            private PendingIntent mReplyPendingIntent;

            public Builder(String var1) {
               this.mParticipant = var1;
            }

            public NotificationCompat.CarExtender.UnreadConversation.Builder addMessage(String var1) {
               this.mMessages.add(var1);
               return this;
            }

            public NotificationCompat.CarExtender.UnreadConversation build() {
               List var3 = this.mMessages;
               String[] var8 = (String[])var3.toArray(new String[var3.size()]);
               String var4 = this.mParticipant;
               RemoteInput var5 = this.mRemoteInput;
               PendingIntent var6 = this.mReplyPendingIntent;
               PendingIntent var7 = this.mReadPendingIntent;
               long var1 = this.mLatestTimestamp;
               return new NotificationCompat.CarExtender.UnreadConversation(var8, var5, var6, var7, new String[]{var4}, var1);
            }

            public NotificationCompat.CarExtender.UnreadConversation.Builder setLatestTimestamp(long var1) {
               this.mLatestTimestamp = var1;
               return this;
            }

            public NotificationCompat.CarExtender.UnreadConversation.Builder setReadPendingIntent(PendingIntent var1) {
               this.mReadPendingIntent = var1;
               return this;
            }

            public NotificationCompat.CarExtender.UnreadConversation.Builder setReplyAction(PendingIntent var1, RemoteInput var2) {
               this.mRemoteInput = var2;
               this.mReplyPendingIntent = var1;
               return this;
            }
         }
      }
   }

   public static class DecoratedCustomViewStyle extends NotificationCompat.Style {
      private static final int MAX_ACTION_BUTTONS = 3;

      private RemoteViews createRemoteViews(RemoteViews var1, boolean var2) {
         int var3 = R$layout.notification_template_custom_big;
         byte var5 = 0;
         RemoteViews var8 = this.applyStandardTemplate(true, var3, false);
         var8.removeAllViews(R$id.actions);
         boolean var4 = false;
         boolean var10 = var4;
         if (var2) {
            var10 = var4;
            if (this.mBuilder.mActions != null) {
               int var7 = Math.min(this.mBuilder.mActions.size(), 3);
               var10 = var4;
               if (var7 > 0) {
                  boolean var6 = true;
                  int var11 = 0;

                  while(true) {
                     var10 = var6;
                     if (var11 >= var7) {
                        break;
                     }

                     RemoteViews var9 = this.generateActionButton((NotificationCompat.Action)this.mBuilder.mActions.get(var11));
                     var8.addView(R$id.actions, var9);
                     ++var11;
                  }
               }
            }
         }

         byte var12;
         if (var10) {
            var12 = var5;
         } else {
            var12 = 8;
         }

         var8.setViewVisibility(R$id.actions, var12);
         var8.setViewVisibility(R$id.action_divider, var12);
         this.buildIntoRemoteViews(var8, var1);
         return var8;
      }

      private RemoteViews generateActionButton(NotificationCompat.Action var1) {
         boolean var2;
         if (var1.actionIntent == null) {
            var2 = true;
         } else {
            var2 = false;
         }

         String var4 = this.mBuilder.mContext.getPackageName();
         int var3;
         if (var2) {
            var3 = R$layout.notification_action_tombstone;
         } else {
            var3 = R$layout.notification_action;
         }

         RemoteViews var5 = new RemoteViews(var4, var3);
         var5.setImageViewBitmap(R$id.action_image, this.createColoredBitmap(var1.getIcon(), this.mBuilder.mContext.getResources().getColor(R$color.notification_action_color_filter)));
         var5.setTextViewText(R$id.action_text, var1.title);
         if (!var2) {
            var5.setOnClickPendingIntent(R$id.action_container, var1.actionIntent);
         }

         if (VERSION.SDK_INT >= 15) {
            var5.setContentDescription(R$id.action_container, var1.title);
         }

         return var5;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            var1.getBuilder().setStyle(new android.app.Notification.DecoratedCustomViewStyle());
         }

      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            return null;
         } else {
            RemoteViews var2 = this.mBuilder.getBigContentView();
            if (var2 == null) {
               var2 = this.mBuilder.getContentView();
            }

            return var2 == null ? null : this.createRemoteViews(var2, true);
         }
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            return null;
         } else {
            return this.mBuilder.getContentView() == null ? null : this.createRemoteViews(this.mBuilder.getContentView(), false);
         }
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            return null;
         } else {
            RemoteViews var2 = this.mBuilder.getHeadsUpContentView();
            RemoteViews var3;
            if (var2 != null) {
               var3 = var2;
            } else {
               var3 = this.mBuilder.getContentView();
            }

            return var2 == null ? null : this.createRemoteViews(var3, true);
         }
      }
   }

   public interface Extender {
      NotificationCompat.Builder extend(NotificationCompat.Builder var1);
   }

   public static class InboxStyle extends NotificationCompat.Style {
      private ArrayList mTexts = new ArrayList();

      public InboxStyle() {
      }

      public InboxStyle(NotificationCompat.Builder var1) {
         this.setBuilder(var1);
      }

      public NotificationCompat.InboxStyle addLine(CharSequence var1) {
         this.mTexts.add(NotificationCompat.Builder.limitCharSequenceLength(var1));
         return this;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            NotificationCompatJellybean.addInboxStyle(var1, this.mBigContentTitle, this.mSummaryTextSet, this.mSummaryText, this.mTexts);
         }

      }

      public NotificationCompat.InboxStyle setBigContentTitle(CharSequence var1) {
         this.mBigContentTitle = NotificationCompat.Builder.limitCharSequenceLength(var1);
         return this;
      }

      public NotificationCompat.InboxStyle setSummaryText(CharSequence var1) {
         this.mSummaryText = NotificationCompat.Builder.limitCharSequenceLength(var1);
         this.mSummaryTextSet = true;
         return this;
      }
   }

   public static class MessagingStyle extends NotificationCompat.Style {
      public static final int MAXIMUM_RETAINED_MESSAGES = 25;
      CharSequence mConversationTitle;
      List mMessages = new ArrayList();
      CharSequence mUserDisplayName;

      MessagingStyle() {
      }

      public MessagingStyle(@NonNull CharSequence var1) {
         this.mUserDisplayName = var1;
      }

      public static NotificationCompat.MessagingStyle extractMessagingStyleFromNotification(Notification var0) {
         Bundle var3 = NotificationCompat.getExtras(var0);
         if (var3 != null && !var3.containsKey("android.selfDisplayName")) {
            return null;
         } else {
            try {
               NotificationCompat.MessagingStyle var1 = new NotificationCompat.MessagingStyle();
               var1.restoreFromCompatExtras(var3);
               return var1;
            } catch (ClassCastException var2) {
               return null;
            }
         }
      }

      @Nullable
      private NotificationCompat.MessagingStyle.Message findLatestIncomingMessage() {
         for(int var1 = this.mMessages.size() - 1; var1 >= 0; --var1) {
            NotificationCompat.MessagingStyle.Message var2 = (NotificationCompat.MessagingStyle.Message)this.mMessages.get(var1);
            if (!TextUtils.isEmpty(var2.getSender())) {
               return var2;
            }
         }

         if (!this.mMessages.isEmpty()) {
            List var3 = this.mMessages;
            return (NotificationCompat.MessagingStyle.Message)var3.get(var3.size() - 1);
         } else {
            return null;
         }
      }

      private boolean hasMessagesWithoutSender() {
         for(int var1 = this.mMessages.size() - 1; var1 >= 0; --var1) {
            if (((NotificationCompat.MessagingStyle.Message)this.mMessages.get(var1)).getSender() == null) {
               return true;
            }
         }

         return false;
      }

      @NonNull
      private TextAppearanceSpan makeFontColorSpan(int var1) {
         return new TextAppearanceSpan((String)null, 0, 0, ColorStateList.valueOf(var1), (ColorStateList)null);
      }

      private CharSequence makeMessageLine(NotificationCompat.MessagingStyle.Message var1) {
         BidiFormatter var9 = BidiFormatter.getInstance();
         SpannableStringBuilder var10 = new SpannableStringBuilder();
         boolean var3;
         if (VERSION.SDK_INT >= 21) {
            var3 = true;
         } else {
            var3 = false;
         }

         int var2;
         if (var3) {
            var2 = -16777216;
         } else {
            var2 = -1;
         }

         Object var6 = var1.getSender();
         boolean var5 = TextUtils.isEmpty(var1.getSender());
         String var7 = "";
         int var4 = var2;
         if (var5) {
            CharSequence var8 = this.mUserDisplayName;
            var6 = var8;
            if (var8 == null) {
               var6 = "";
            }

            if (var3 && this.mBuilder.getColor() != 0) {
               var2 = this.mBuilder.getColor();
            }

            var4 = var2;
         }

         CharSequence var12 = var9.unicodeWrap((CharSequence)var6);
         var10.append(var12);
         var10.setSpan(this.makeFontColorSpan(var4), var10.length() - var12.length(), var10.length(), 33);
         Object var11;
         if (var1.getText() == null) {
            var11 = var7;
         } else {
            var11 = var1.getText();
         }

         var10.append("  ").append(var9.unicodeWrap((CharSequence)var11));
         return var10;
      }

      public void addCompatExtras(Bundle var1) {
         super.addCompatExtras(var1);
         CharSequence var2 = this.mUserDisplayName;
         if (var2 != null) {
            var1.putCharSequence("android.selfDisplayName", var2);
         }

         var2 = this.mConversationTitle;
         if (var2 != null) {
            var1.putCharSequence("android.conversationTitle", var2);
         }

         if (!this.mMessages.isEmpty()) {
            var1.putParcelableArray("android.messages", NotificationCompat.MessagingStyle.Message.getBundleArrayForMessages(this.mMessages));
         }

      }

      public NotificationCompat.MessagingStyle addMessage(NotificationCompat.MessagingStyle.Message var1) {
         this.mMessages.add(var1);
         if (this.mMessages.size() > 25) {
            this.mMessages.remove(0);
         }

         return this;
      }

      public NotificationCompat.MessagingStyle addMessage(CharSequence var1, long var2, CharSequence var4) {
         this.mMessages.add(new NotificationCompat.MessagingStyle.Message(var1, var2, var4));
         if (this.mMessages.size() > 25) {
            this.mMessages.remove(0);
         }

         return this;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            ArrayList var14 = new ArrayList();
            ArrayList var13 = new ArrayList();
            ArrayList var6 = new ArrayList();
            ArrayList var7 = new ArrayList();
            ArrayList var8 = new ArrayList();
            Iterator var9 = this.mMessages.iterator();

            while(var9.hasNext()) {
               NotificationCompat.MessagingStyle.Message var10 = (NotificationCompat.MessagingStyle.Message)var9.next();
               var14.add(var10.getText());
               var13.add(var10.getTimestamp());
               var6.add(var10.getSender());
               var7.add(var10.getDataMimeType());
               var8.add(var10.getDataUri());
            }

            NotificationCompatApi24.addMessagingStyle(var1, this.mUserDisplayName, this.mConversationTitle, var14, var13, var6, var7, var8);
         } else {
            NotificationCompat.MessagingStyle.Message var4 = this.findLatestIncomingMessage();
            if (this.mConversationTitle != null) {
               var1.getBuilder().setContentTitle(this.mConversationTitle);
            } else if (var4 != null) {
               var1.getBuilder().setContentTitle(var4.getSender());
            }

            CharSequence var11;
            if (var4 != null) {
               android.app.Notification.Builder var5 = var1.getBuilder();
               if (this.mConversationTitle != null) {
                  var11 = this.makeMessageLine(var4);
               } else {
                  var11 = var4.getText();
               }

               var5.setContentText(var11);
            }

            if (VERSION.SDK_INT >= 16) {
               SpannableStringBuilder var12 = new SpannableStringBuilder();
               boolean var2;
               if (this.mConversationTitle == null && !this.hasMessagesWithoutSender()) {
                  var2 = false;
               } else {
                  var2 = true;
               }

               for(int var3 = this.mMessages.size() - 1; var3 >= 0; --var3) {
                  var4 = (NotificationCompat.MessagingStyle.Message)this.mMessages.get(var3);
                  if (var2) {
                     var11 = this.makeMessageLine(var4);
                  } else {
                     var11 = var4.getText();
                  }

                  if (var3 != this.mMessages.size() - 1) {
                     var12.insert(0, "\n");
                  }

                  var12.insert(0, var11);
               }

               NotificationCompatJellybean.addBigTextStyle(var1, (CharSequence)null, false, (CharSequence)null, var12);
            }

         }
      }

      public CharSequence getConversationTitle() {
         return this.mConversationTitle;
      }

      public List getMessages() {
         return this.mMessages;
      }

      public CharSequence getUserDisplayName() {
         return this.mUserDisplayName;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      protected void restoreFromCompatExtras(Bundle var1) {
         this.mMessages.clear();
         this.mUserDisplayName = var1.getString("android.selfDisplayName");
         this.mConversationTitle = var1.getString("android.conversationTitle");
         Parcelable[] var2 = var1.getParcelableArray("android.messages");
         if (var2 != null) {
            this.mMessages = NotificationCompat.MessagingStyle.Message.getMessagesFromBundleArray(var2);
         }

      }

      public NotificationCompat.MessagingStyle setConversationTitle(CharSequence var1) {
         this.mConversationTitle = var1;
         return this;
      }

      public static final class Message {
         static final String KEY_DATA_MIME_TYPE = "type";
         static final String KEY_DATA_URI = "uri";
         static final String KEY_EXTRAS_BUNDLE = "extras";
         static final String KEY_SENDER = "sender";
         static final String KEY_TEXT = "text";
         static final String KEY_TIMESTAMP = "time";
         private String mDataMimeType;
         private Uri mDataUri;
         private Bundle mExtras = new Bundle();
         private final CharSequence mSender;
         private final CharSequence mText;
         private final long mTimestamp;

         public Message(CharSequence var1, long var2, CharSequence var4) {
            this.mText = var1;
            this.mTimestamp = var2;
            this.mSender = var4;
         }

         static Bundle[] getBundleArrayForMessages(List var0) {
            Bundle[] var3 = new Bundle[var0.size()];
            int var2 = var0.size();

            for(int var1 = 0; var1 < var2; ++var1) {
               var3[var1] = ((NotificationCompat.MessagingStyle.Message)var0.get(var1)).toBundle();
            }

            return var3;
         }

         static NotificationCompat.MessagingStyle.Message getMessageFromBundle(Bundle var0) {
            try {
               if (var0.containsKey("text")) {
                  if (!var0.containsKey("time")) {
                     return null;
                  } else {
                     NotificationCompat.MessagingStyle.Message var1 = new NotificationCompat.MessagingStyle.Message(var0.getCharSequence("text"), var0.getLong("time"), var0.getCharSequence("sender"));
                     if (var0.containsKey("type") && var0.containsKey("uri")) {
                        var1.setData(var0.getString("type"), (Uri)var0.getParcelable("uri"));
                     }

                     if (var0.containsKey("extras")) {
                        var1.getExtras().putAll(var0.getBundle("extras"));
                     }

                     return var1;
                  }
               } else {
                  return null;
               }
            } catch (ClassCastException var2) {
               return null;
            }
         }

         static List getMessagesFromBundleArray(Parcelable[] var0) {
            ArrayList var2 = new ArrayList(var0.length);

            for(int var1 = 0; var1 < var0.length; ++var1) {
               if (var0[var1] instanceof Bundle) {
                  NotificationCompat.MessagingStyle.Message var3 = getMessageFromBundle((Bundle)var0[var1]);
                  if (var3 != null) {
                     var2.add(var3);
                  }
               }
            }

            return var2;
         }

         private Bundle toBundle() {
            Bundle var1 = new Bundle();
            CharSequence var2 = this.mText;
            if (var2 != null) {
               var1.putCharSequence("text", var2);
            }

            var1.putLong("time", this.mTimestamp);
            var2 = this.mSender;
            if (var2 != null) {
               var1.putCharSequence("sender", var2);
            }

            String var3 = this.mDataMimeType;
            if (var3 != null) {
               var1.putString("type", var3);
            }

            Uri var4 = this.mDataUri;
            if (var4 != null) {
               var1.putParcelable("uri", var4);
            }

            Bundle var5 = this.mExtras;
            if (var5 != null) {
               var1.putBundle("extras", var5);
            }

            return var1;
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

         public NotificationCompat.MessagingStyle.Message setData(String var1, Uri var2) {
            this.mDataMimeType = var1;
            this.mDataUri = var2;
            return this;
         }
      }
   }

   @RequiresApi(16)
   static class NotificationCompatApi16Impl extends NotificationCompat.NotificationCompatBaseImpl {
      public Notification build(NotificationCompat.Builder var1, NotificationCompat.BuilderExtender var2) {
         NotificationCompatJellybean.Builder var3 = new NotificationCompatJellybean.Builder(var1.mContext, var1.mNotification, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mTickerView, var1.mNumber, var1.mContentIntent, var1.mFullScreenIntent, var1.mLargeIcon, var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate, var1.mUseChronometer, var1.mPriority, var1.mSubText, var1.mLocalOnly, var1.mExtras, var1.mGroupKey, var1.mGroupSummary, var1.mSortKey, var1.mContentView, var1.mBigContentView);
         NotificationCompat.addActionsToBuilder(var3, var1.mActions);
         if (var1.mStyle != null) {
            var1.mStyle.apply(var3);
         }

         Notification var4 = var2.build(var1, var3);
         if (var1.mStyle != null) {
            Bundle var5 = NotificationCompat.getExtras(var4);
            if (var5 != null) {
               var1.mStyle.addCompatExtras(var5);
            }
         }

         return var4;
      }

      public NotificationCompat.Action getAction(Notification var1, int var2) {
         return (NotificationCompat.Action)NotificationCompatJellybean.getAction(var1, var2, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
      }

      public NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList var1) {
         return (NotificationCompat.Action[])((NotificationCompat.Action[])NotificationCompatJellybean.getActionsFromParcelableArrayList(var1, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY));
      }

      public ArrayList getParcelableArrayListForActions(NotificationCompat.Action[] var1) {
         return NotificationCompatJellybean.getParcelableArrayListForActions(var1);
      }
   }

   @RequiresApi(19)
   static class NotificationCompatApi19Impl extends NotificationCompat.NotificationCompatApi16Impl {
      public Notification build(NotificationCompat.Builder var1, NotificationCompat.BuilderExtender var2) {
         NotificationCompatKitKat.Builder var3 = new NotificationCompatKitKat.Builder(var1.mContext, var1.mNotification, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mTickerView, var1.mNumber, var1.mContentIntent, var1.mFullScreenIntent, var1.mLargeIcon, var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate, var1.mShowWhen, var1.mUseChronometer, var1.mPriority, var1.mSubText, var1.mLocalOnly, var1.mPeople, var1.mExtras, var1.mGroupKey, var1.mGroupSummary, var1.mSortKey, var1.mContentView, var1.mBigContentView);
         NotificationCompat.addActionsToBuilder(var3, var1.mActions);
         if (var1.mStyle != null) {
            var1.mStyle.apply(var3);
         }

         return var2.build(var1, var3);
      }

      public NotificationCompat.Action getAction(Notification var1, int var2) {
         return (NotificationCompat.Action)NotificationCompatKitKat.getAction(var1, var2, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
      }
   }

   @RequiresApi(20)
   static class NotificationCompatApi20Impl extends NotificationCompat.NotificationCompatApi19Impl {
      public Notification build(NotificationCompat.Builder var1, NotificationCompat.BuilderExtender var2) {
         NotificationCompatApi20.Builder var3 = new NotificationCompatApi20.Builder(var1.mContext, var1.mNotification, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mTickerView, var1.mNumber, var1.mContentIntent, var1.mFullScreenIntent, var1.mLargeIcon, var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate, var1.mShowWhen, var1.mUseChronometer, var1.mPriority, var1.mSubText, var1.mLocalOnly, var1.mPeople, var1.mExtras, var1.mGroupKey, var1.mGroupSummary, var1.mSortKey, var1.mContentView, var1.mBigContentView, var1.mGroupAlertBehavior);
         NotificationCompat.addActionsToBuilder(var3, var1.mActions);
         if (var1.mStyle != null) {
            var1.mStyle.apply(var3);
         }

         Notification var4 = var2.build(var1, var3);
         if (var1.mStyle != null) {
            var1.mStyle.addCompatExtras(NotificationCompat.getExtras(var4));
         }

         return var4;
      }

      public NotificationCompat.Action getAction(Notification var1, int var2) {
         return (NotificationCompat.Action)NotificationCompatApi20.getAction(var1, var2, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
      }

      public NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList var1) {
         return (NotificationCompat.Action[])((NotificationCompat.Action[])NotificationCompatApi20.getActionsFromParcelableArrayList(var1, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY));
      }

      public ArrayList getParcelableArrayListForActions(NotificationCompat.Action[] var1) {
         return NotificationCompatApi20.getParcelableArrayListForActions(var1);
      }
   }

   @RequiresApi(21)
   static class NotificationCompatApi21Impl extends NotificationCompat.NotificationCompatApi20Impl {
      public Notification build(NotificationCompat.Builder var1, NotificationCompat.BuilderExtender var2) {
         NotificationCompatApi21.Builder var3 = new NotificationCompatApi21.Builder(var1.mContext, var1.mNotification, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mTickerView, var1.mNumber, var1.mContentIntent, var1.mFullScreenIntent, var1.mLargeIcon, var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate, var1.mShowWhen, var1.mUseChronometer, var1.mPriority, var1.mSubText, var1.mLocalOnly, var1.mCategory, var1.mPeople, var1.mExtras, var1.mColor, var1.mVisibility, var1.mPublicVersion, var1.mGroupKey, var1.mGroupSummary, var1.mSortKey, var1.mContentView, var1.mBigContentView, var1.mHeadsUpContentView, var1.mGroupAlertBehavior);
         NotificationCompat.addActionsToBuilder(var3, var1.mActions);
         if (var1.mStyle != null) {
            var1.mStyle.apply(var3);
         }

         Notification var4 = var2.build(var1, var3);
         if (var1.mStyle != null) {
            var1.mStyle.addCompatExtras(NotificationCompat.getExtras(var4));
         }

         return var4;
      }

      public Bundle getBundleForUnreadConversation(NotificationCompatBase.UnreadConversation var1) {
         return NotificationCompatApi21.getBundleForUnreadConversation(var1);
      }

      public NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle var1, NotificationCompatBase.UnreadConversation.Factory var2, RemoteInputCompatBase.RemoteInput.Factory var3) {
         return NotificationCompatApi21.getUnreadConversationFromBundle(var1, var2, var3);
      }
   }

   @RequiresApi(24)
   static class NotificationCompatApi24Impl extends NotificationCompat.NotificationCompatApi21Impl {
      public Notification build(NotificationCompat.Builder var1, NotificationCompat.BuilderExtender var2) {
         NotificationCompatApi24.Builder var3 = new NotificationCompatApi24.Builder(var1.mContext, var1.mNotification, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mTickerView, var1.mNumber, var1.mContentIntent, var1.mFullScreenIntent, var1.mLargeIcon, var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate, var1.mShowWhen, var1.mUseChronometer, var1.mPriority, var1.mSubText, var1.mLocalOnly, var1.mCategory, var1.mPeople, var1.mExtras, var1.mColor, var1.mVisibility, var1.mPublicVersion, var1.mGroupKey, var1.mGroupSummary, var1.mSortKey, var1.mRemoteInputHistory, var1.mContentView, var1.mBigContentView, var1.mHeadsUpContentView, var1.mGroupAlertBehavior);
         NotificationCompat.addActionsToBuilder(var3, var1.mActions);
         if (var1.mStyle != null) {
            var1.mStyle.apply(var3);
         }

         Notification var4 = var2.build(var1, var3);
         if (var1.mStyle != null) {
            var1.mStyle.addCompatExtras(NotificationCompat.getExtras(var4));
         }

         return var4;
      }

      public NotificationCompat.Action getAction(Notification var1, int var2) {
         return (NotificationCompat.Action)NotificationCompatApi24.getAction(var1, var2, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
      }

      public NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList var1) {
         return (NotificationCompat.Action[])((NotificationCompat.Action[])NotificationCompatApi24.getActionsFromParcelableArrayList(var1, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY));
      }

      public ArrayList getParcelableArrayListForActions(NotificationCompat.Action[] var1) {
         return NotificationCompatApi24.getParcelableArrayListForActions(var1);
      }
   }

   @RequiresApi(26)
   static class NotificationCompatApi26Impl extends NotificationCompat.NotificationCompatApi24Impl {
      public Notification build(NotificationCompat.Builder var1, NotificationCompat.BuilderExtender var2) {
         NotificationCompatApi26.Builder var3 = new NotificationCompatApi26.Builder(var1.mContext, var1.mNotification, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mTickerView, var1.mNumber, var1.mContentIntent, var1.mFullScreenIntent, var1.mLargeIcon, var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate, var1.mShowWhen, var1.mUseChronometer, var1.mPriority, var1.mSubText, var1.mLocalOnly, var1.mCategory, var1.mPeople, var1.mExtras, var1.mColor, var1.mVisibility, var1.mPublicVersion, var1.mGroupKey, var1.mGroupSummary, var1.mSortKey, var1.mRemoteInputHistory, var1.mContentView, var1.mBigContentView, var1.mHeadsUpContentView, var1.mChannelId, var1.mBadgeIcon, var1.mShortcutId, var1.mTimeout, var1.mColorized, var1.mColorizedSet, var1.mGroupAlertBehavior);
         NotificationCompat.addActionsToBuilder(var3, var1.mActions);
         if (var1.mStyle != null) {
            var1.mStyle.apply(var3);
         }

         Notification var4 = var2.build(var1, var3);
         if (var1.mStyle != null) {
            var1.mStyle.addCompatExtras(NotificationCompat.getExtras(var4));
         }

         return var4;
      }
   }

   static class NotificationCompatBaseImpl implements NotificationCompat.NotificationCompatImpl {
      public Notification build(NotificationCompat.Builder var1, NotificationCompat.BuilderExtender var2) {
         return var2.build(var1, new NotificationCompat.NotificationCompatBaseImpl.BuilderBase(var1.mContext, var1.mNotification, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mTickerView, var1.mNumber, var1.mContentIntent, var1.mFullScreenIntent, var1.mLargeIcon, var1.mProgressMax, var1.mProgress, var1.mProgressIndeterminate));
      }

      public NotificationCompat.Action getAction(Notification var1, int var2) {
         return null;
      }

      public NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList var1) {
         return null;
      }

      public Bundle getBundleForUnreadConversation(NotificationCompatBase.UnreadConversation var1) {
         return null;
      }

      public ArrayList getParcelableArrayListForActions(NotificationCompat.Action[] var1) {
         return null;
      }

      public NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle var1, NotificationCompatBase.UnreadConversation.Factory var2, RemoteInputCompatBase.RemoteInput.Factory var3) {
         return null;
      }

      public static class BuilderBase implements NotificationBuilderWithBuilderAccessor {
         private android.app.Notification.Builder mBuilder;

         BuilderBase(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13) {
            android.app.Notification.Builder var17 = (new android.app.Notification.Builder(var1)).setWhen(var2.when).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
            int var14 = var2.flags;
            boolean var16 = true;
            boolean var15;
            if ((var14 & 2) != 0) {
               var15 = true;
            } else {
               var15 = false;
            }

            var17 = var17.setOngoing(var15);
            if ((var2.flags & 8) != 0) {
               var15 = true;
            } else {
               var15 = false;
            }

            var17 = var17.setOnlyAlertOnce(var15);
            if ((var2.flags & 16) != 0) {
               var15 = true;
            } else {
               var15 = false;
            }

            var17 = var17.setAutoCancel(var15).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
            if ((var2.flags & 128) != 0) {
               var15 = var16;
            } else {
               var15 = false;
            }

            this.mBuilder = var17.setFullScreenIntent(var9, var15).setLargeIcon(var10).setNumber(var7).setProgress(var11, var12, var13);
         }

         public Notification build() {
            return this.mBuilder.getNotification();
         }

         public android.app.Notification.Builder getBuilder() {
            return this.mBuilder;
         }
      }
   }

   interface NotificationCompatImpl {
      Notification build(NotificationCompat.Builder var1, NotificationCompat.BuilderExtender var2);

      NotificationCompat.Action getAction(Notification var1, int var2);

      NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList var1);

      Bundle getBundleForUnreadConversation(NotificationCompatBase.UnreadConversation var1);

      ArrayList getParcelableArrayListForActions(NotificationCompat.Action[] var1);

      NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle var1, NotificationCompatBase.UnreadConversation.Factory var2, RemoteInputCompatBase.RemoteInput.Factory var3);
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface NotificationVisibility {
   }

   public abstract static class Style {
      CharSequence mBigContentTitle;
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      protected NotificationCompat.Builder mBuilder;
      CharSequence mSummaryText;
      boolean mSummaryTextSet = false;

      private int calculateTopPadding() {
         Resources var4 = this.mBuilder.mContext.getResources();
         int var2 = var4.getDimensionPixelSize(R$dimen.notification_top_pad);
         int var3 = var4.getDimensionPixelSize(R$dimen.notification_top_pad_large_text);
         float var1 = (constrain(var4.getConfiguration().fontScale, 1.0F, 1.3F) - 1.0F) / 0.29999995F;
         return Math.round((1.0F - var1) * (float)var2 + (float)var3 * var1);
      }

      private static float constrain(float var0, float var1, float var2) {
         if (var0 < var1) {
            return var1;
         } else {
            return var0 > var2 ? var2 : var0;
         }
      }

      private Bitmap createColoredBitmap(int var1, int var2, int var3) {
         Drawable var4 = this.mBuilder.mContext.getResources().getDrawable(var1);
         if (var3 == 0) {
            var1 = var4.getIntrinsicWidth();
         } else {
            var1 = var3;
         }

         if (var3 == 0) {
            var3 = var4.getIntrinsicHeight();
         }

         Bitmap var5 = Bitmap.createBitmap(var1, var3, Config.ARGB_8888);
         var4.setBounds(0, 0, var1, var3);
         if (var2 != 0) {
            var4.mutate().setColorFilter(new PorterDuffColorFilter(var2, Mode.SRC_IN));
         }

         var4.draw(new Canvas(var5));
         return var5;
      }

      private Bitmap createIconWithBackground(int var1, int var2, int var3, int var4) {
         int var5 = R$drawable.notification_icon_background;
         if (var4 == 0) {
            var4 = 0;
         }

         Bitmap var6 = this.createColoredBitmap(var5, var4, var2);
         Canvas var7 = new Canvas(var6);
         Drawable var8 = this.mBuilder.mContext.getResources().getDrawable(var1).mutate();
         var8.setFilterBitmap(true);
         var1 = (var2 - var3) / 2;
         var8.setBounds(var1, var1, var3 + var1, var3 + var1);
         var8.setColorFilter(new PorterDuffColorFilter(-1, Mode.SRC_ATOP));
         var8.draw(var7);
         return var6;
      }

      private void hideNormalContent(RemoteViews var1) {
         var1.setViewVisibility(R$id.title, 8);
         var1.setViewVisibility(R$id.text2, 8);
         var1.setViewVisibility(R$id.text, 8);
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public void addCompatExtras(Bundle var1) {
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public void apply(NotificationBuilderWithBuilderAccessor var1) {
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews applyStandardTemplate(boolean var1, int var2, boolean var3) {
         Resources var9 = this.mBuilder.mContext.getResources();
         RemoteViews var10 = new RemoteViews(this.mBuilder.mContext.getPackageName(), var2);
         boolean var5 = false;
         var2 = this.mBuilder.getPriority();
         byte var7 = 0;
         boolean var12;
         if (var2 < -1) {
            var12 = true;
         } else {
            var12 = false;
         }

         if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT < 21) {
            if (var12) {
               var10.setInt(R$id.notification_background, "setBackgroundResource", R$drawable.notification_bg_low);
               var10.setInt(R$id.icon, "setBackgroundResource", R$drawable.notification_template_icon_low_bg);
            } else {
               var10.setInt(R$id.notification_background, "setBackgroundResource", R$drawable.notification_bg);
               var10.setInt(R$id.icon, "setBackgroundResource", R$drawable.notification_template_icon_bg);
            }
         }

         int var6;
         Bitmap var11;
         if (this.mBuilder.mLargeIcon != null) {
            if (VERSION.SDK_INT >= 16) {
               var10.setViewVisibility(R$id.icon, 0);
               var10.setImageViewBitmap(R$id.icon, this.mBuilder.mLargeIcon);
            } else {
               var10.setViewVisibility(R$id.icon, 8);
            }

            if (var1 && this.mBuilder.mNotification.icon != 0) {
               var2 = var9.getDimensionPixelSize(R$dimen.notification_right_icon_size);
               var6 = var9.getDimensionPixelSize(R$dimen.notification_small_icon_background_padding);
               if (VERSION.SDK_INT >= 21) {
                  var11 = this.createIconWithBackground(this.mBuilder.mNotification.icon, var2, var2 - var6 * 2, this.mBuilder.getColor());
                  var10.setImageViewBitmap(R$id.right_icon, var11);
               } else {
                  var10.setImageViewBitmap(R$id.right_icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
               }

               var10.setViewVisibility(R$id.right_icon, 0);
            }
         } else if (var1 && this.mBuilder.mNotification.icon != 0) {
            var10.setViewVisibility(R$id.icon, 0);
            if (VERSION.SDK_INT >= 21) {
               var2 = var9.getDimensionPixelSize(R$dimen.notification_large_icon_width);
               var6 = var9.getDimensionPixelSize(R$dimen.notification_big_circle_margin);
               int var8 = var9.getDimensionPixelSize(R$dimen.notification_small_icon_size_as_large);
               var11 = this.createIconWithBackground(this.mBuilder.mNotification.icon, var2 - var6, var8, this.mBuilder.getColor());
               var10.setImageViewBitmap(R$id.icon, var11);
            } else {
               var10.setImageViewBitmap(R$id.icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
            }
         }

         if (this.mBuilder.mContentTitle != null) {
            var10.setTextViewText(R$id.title, this.mBuilder.mContentTitle);
         }

         if (this.mBuilder.mContentText != null) {
            var10.setTextViewText(R$id.text, this.mBuilder.mContentText);
            var5 = true;
         }

         if (VERSION.SDK_INT < 21 && this.mBuilder.mLargeIcon != null) {
            var12 = true;
         } else {
            var12 = false;
         }

         if (this.mBuilder.mContentInfo != null) {
            var10.setTextViewText(R$id.info, this.mBuilder.mContentInfo);
            var10.setViewVisibility(R$id.info, 0);
            var5 = true;
            var12 = true;
         } else if (this.mBuilder.mNumber > 0) {
            var2 = var9.getInteger(R$integer.status_bar_notification_info_maxnum);
            if (this.mBuilder.mNumber > var2) {
               var10.setTextViewText(R$id.info, var9.getString(R$string.status_bar_notification_info_overflow));
            } else {
               NumberFormat var15 = NumberFormat.getIntegerInstance();
               var10.setTextViewText(R$id.info, var15.format((long)this.mBuilder.mNumber));
            }

            var10.setViewVisibility(R$id.info, 0);
            var5 = true;
            var12 = true;
         } else {
            var10.setViewVisibility(R$id.info, 8);
         }

         boolean var13;
         label106: {
            if (this.mBuilder.mSubText != null && VERSION.SDK_INT >= 16) {
               var10.setTextViewText(R$id.text, this.mBuilder.mSubText);
               if (this.mBuilder.mContentText != null) {
                  var10.setTextViewText(R$id.text2, this.mBuilder.mContentText);
                  var10.setViewVisibility(R$id.text2, 0);
                  var13 = true;
                  break label106;
               }

               var10.setViewVisibility(R$id.text2, 8);
            }

            var13 = false;
         }

         if (var13 && VERSION.SDK_INT >= 16) {
            if (var3) {
               float var4 = (float)var9.getDimensionPixelSize(R$dimen.notification_subtext_size);
               var10.setTextViewTextSize(R$id.text, 0, var4);
            }

            var10.setViewPadding(R$id.line1, 0, 0, 0, 0);
         }

         if (this.mBuilder.getWhenIfShowing() != 0L) {
            if (this.mBuilder.mUseChronometer && VERSION.SDK_INT >= 16) {
               var10.setViewVisibility(R$id.chronometer, 0);
               var10.setLong(R$id.chronometer, "setBase", this.mBuilder.getWhenIfShowing() + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
               var10.setBoolean(R$id.chronometer, "setStarted", true);
            } else {
               var10.setViewVisibility(R$id.time, 0);
               var10.setLong(R$id.time, "setTime", this.mBuilder.getWhenIfShowing());
            }

            var12 = true;
         }

         var6 = R$id.right_side;
         byte var14;
         if (var12) {
            var14 = 0;
         } else {
            var14 = 8;
         }

         var10.setViewVisibility(var6, var14);
         var6 = R$id.line3;
         if (var5) {
            var14 = var7;
         } else {
            var14 = 8;
         }

         var10.setViewVisibility(var6, var14);
         return var10;
      }

      public Notification build() {
         Notification var1 = null;
         NotificationCompat.Builder var2 = this.mBuilder;
         if (var2 != null) {
            var1 = var2.build();
         }

         return var1;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public void buildIntoRemoteViews(RemoteViews var1, RemoteViews var2) {
         this.hideNormalContent(var1);
         var1.removeAllViews(R$id.notification_main_column);
         var1.addView(R$id.notification_main_column, var2.clone());
         var1.setViewVisibility(R$id.notification_main_column, 0);
         if (VERSION.SDK_INT >= 21) {
            var1.setViewPadding(R$id.notification_main_column_container, 0, this.calculateTopPadding(), 0, 0);
         }

      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public Bitmap createColoredBitmap(int var1, int var2) {
         return this.createColoredBitmap(var1, var2, 0);
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      protected void restoreFromCompatExtras(Bundle var1) {
      }

      public void setBuilder(NotificationCompat.Builder var1) {
         if (this.mBuilder != var1) {
            this.mBuilder = var1;
            var1 = this.mBuilder;
            if (var1 != null) {
               var1.setStyle(this);
            }
         }

      }
   }

   public static final class WearableExtender implements NotificationCompat.Extender {
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
      private ArrayList mActions = new ArrayList();
      private Bitmap mBackground;
      private String mBridgeTag;
      private int mContentActionIndex = -1;
      private int mContentIcon;
      private int mContentIconGravity = 8388613;
      private int mCustomContentHeight;
      private int mCustomSizePreset = 0;
      private String mDismissalId;
      private PendingIntent mDisplayIntent;
      private int mFlags = 1;
      private int mGravity = 80;
      private int mHintScreenTimeout;
      private ArrayList mPages = new ArrayList();

      public WearableExtender() {
      }

      public WearableExtender(Notification var1) {
         Bundle var3 = NotificationCompat.getExtras(var1);
         if (var3 != null) {
            var3 = var3.getBundle("android.wearable.EXTENSIONS");
         } else {
            var3 = null;
         }

         if (var3 != null) {
            NotificationCompat.Action[] var2 = NotificationCompat.IMPL.getActionsFromParcelableArrayList(var3.getParcelableArrayList("actions"));
            if (var2 != null) {
               Collections.addAll(this.mActions, var2);
            }

            this.mFlags = var3.getInt("flags", 1);
            this.mDisplayIntent = (PendingIntent)var3.getParcelable("displayIntent");
            Notification[] var4 = NotificationCompat.getNotificationArrayFromBundle(var3, "pages");
            if (var4 != null) {
               Collections.addAll(this.mPages, var4);
            }

            this.mBackground = (Bitmap)var3.getParcelable("background");
            this.mContentIcon = var3.getInt("contentIcon");
            this.mContentIconGravity = var3.getInt("contentIconGravity", 8388613);
            this.mContentActionIndex = var3.getInt("contentActionIndex", -1);
            this.mCustomSizePreset = var3.getInt("customSizePreset", 0);
            this.mCustomContentHeight = var3.getInt("customContentHeight");
            this.mGravity = var3.getInt("gravity", 80);
            this.mHintScreenTimeout = var3.getInt("hintScreenTimeout");
            this.mDismissalId = var3.getString("dismissalId");
            this.mBridgeTag = var3.getString("bridgeTag");
         }

      }

      private void setFlag(int var1, boolean var2) {
         if (var2) {
            this.mFlags |= var1;
         } else {
            this.mFlags &= ~var1;
         }
      }

      public NotificationCompat.WearableExtender addAction(NotificationCompat.Action var1) {
         this.mActions.add(var1);
         return this;
      }

      public NotificationCompat.WearableExtender addActions(List var1) {
         this.mActions.addAll(var1);
         return this;
      }

      public NotificationCompat.WearableExtender addPage(Notification var1) {
         this.mPages.add(var1);
         return this;
      }

      public NotificationCompat.WearableExtender addPages(List var1) {
         this.mPages.addAll(var1);
         return this;
      }

      public NotificationCompat.WearableExtender clearActions() {
         this.mActions.clear();
         return this;
      }

      public NotificationCompat.WearableExtender clearPages() {
         this.mPages.clear();
         return this;
      }

      public NotificationCompat.WearableExtender clone() {
         NotificationCompat.WearableExtender var1 = new NotificationCompat.WearableExtender();
         var1.mActions = new ArrayList(this.mActions);
         var1.mFlags = this.mFlags;
         var1.mDisplayIntent = this.mDisplayIntent;
         var1.mPages = new ArrayList(this.mPages);
         var1.mBackground = this.mBackground;
         var1.mContentIcon = this.mContentIcon;
         var1.mContentIconGravity = this.mContentIconGravity;
         var1.mContentActionIndex = this.mContentActionIndex;
         var1.mCustomSizePreset = this.mCustomSizePreset;
         var1.mCustomContentHeight = this.mCustomContentHeight;
         var1.mGravity = this.mGravity;
         var1.mHintScreenTimeout = this.mHintScreenTimeout;
         var1.mDismissalId = this.mDismissalId;
         var1.mBridgeTag = this.mBridgeTag;
         return var1;
      }

      public NotificationCompat.Builder extend(NotificationCompat.Builder var1) {
         Bundle var3 = new Bundle();
         if (!this.mActions.isEmpty()) {
            NotificationCompat.NotificationCompatImpl var4 = NotificationCompat.IMPL;
            ArrayList var5 = this.mActions;
            var3.putParcelableArrayList("actions", var4.getParcelableArrayListForActions((NotificationCompat.Action[])var5.toArray(new NotificationCompat.Action[var5.size()])));
         }

         int var2 = this.mFlags;
         if (var2 != 1) {
            var3.putInt("flags", var2);
         }

         PendingIntent var6 = this.mDisplayIntent;
         if (var6 != null) {
            var3.putParcelable("displayIntent", var6);
         }

         if (!this.mPages.isEmpty()) {
            ArrayList var7 = this.mPages;
            var3.putParcelableArray("pages", (Parcelable[])var7.toArray(new Notification[var7.size()]));
         }

         Bitmap var8 = this.mBackground;
         if (var8 != null) {
            var3.putParcelable("background", var8);
         }

         var2 = this.mContentIcon;
         if (var2 != 0) {
            var3.putInt("contentIcon", var2);
         }

         var2 = this.mContentIconGravity;
         if (var2 != 8388613) {
            var3.putInt("contentIconGravity", var2);
         }

         var2 = this.mContentActionIndex;
         if (var2 != -1) {
            var3.putInt("contentActionIndex", var2);
         }

         var2 = this.mCustomSizePreset;
         if (var2 != 0) {
            var3.putInt("customSizePreset", var2);
         }

         var2 = this.mCustomContentHeight;
         if (var2 != 0) {
            var3.putInt("customContentHeight", var2);
         }

         var2 = this.mGravity;
         if (var2 != 80) {
            var3.putInt("gravity", var2);
         }

         var2 = this.mHintScreenTimeout;
         if (var2 != 0) {
            var3.putInt("hintScreenTimeout", var2);
         }

         String var9 = this.mDismissalId;
         if (var9 != null) {
            var3.putString("dismissalId", var9);
         }

         var9 = this.mBridgeTag;
         if (var9 != null) {
            var3.putString("bridgeTag", var9);
         }

         var1.getExtras().putBundle("android.wearable.EXTENSIONS", var3);
         return var1;
      }

      public List getActions() {
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
         return (this.mFlags & 1) != 0;
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
         return (this.mFlags & 32) != 0;
      }

      public boolean getHintAvoidBackgroundClipping() {
         return (this.mFlags & 16) != 0;
      }

      public boolean getHintContentIntentLaunchesActivity() {
         return (this.mFlags & 64) != 0;
      }

      public boolean getHintHideIcon() {
         return (this.mFlags & 2) != 0;
      }

      public int getHintScreenTimeout() {
         return this.mHintScreenTimeout;
      }

      public boolean getHintShowBackgroundOnly() {
         return (this.mFlags & 4) != 0;
      }

      public List getPages() {
         return this.mPages;
      }

      public boolean getStartScrollBottom() {
         return (this.mFlags & 8) != 0;
      }

      public NotificationCompat.WearableExtender setBackground(Bitmap var1) {
         this.mBackground = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setBridgeTag(String var1) {
         this.mBridgeTag = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setContentAction(int var1) {
         this.mContentActionIndex = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setContentIcon(int var1) {
         this.mContentIcon = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setContentIconGravity(int var1) {
         this.mContentIconGravity = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setContentIntentAvailableOffline(boolean var1) {
         this.setFlag(1, var1);
         return this;
      }

      public NotificationCompat.WearableExtender setCustomContentHeight(int var1) {
         this.mCustomContentHeight = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setCustomSizePreset(int var1) {
         this.mCustomSizePreset = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setDismissalId(String var1) {
         this.mDismissalId = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setDisplayIntent(PendingIntent var1) {
         this.mDisplayIntent = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setGravity(int var1) {
         this.mGravity = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setHintAmbientBigPicture(boolean var1) {
         this.setFlag(32, var1);
         return this;
      }

      public NotificationCompat.WearableExtender setHintAvoidBackgroundClipping(boolean var1) {
         this.setFlag(16, var1);
         return this;
      }

      public NotificationCompat.WearableExtender setHintContentIntentLaunchesActivity(boolean var1) {
         this.setFlag(64, var1);
         return this;
      }

      public NotificationCompat.WearableExtender setHintHideIcon(boolean var1) {
         this.setFlag(2, var1);
         return this;
      }

      public NotificationCompat.WearableExtender setHintScreenTimeout(int var1) {
         this.mHintScreenTimeout = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setHintShowBackgroundOnly(boolean var1) {
         this.setFlag(4, var1);
         return this;
      }

      public NotificationCompat.WearableExtender setStartScrollBottom(boolean var1) {
         this.setFlag(8, var1);
         return this;
      }
   }
}
