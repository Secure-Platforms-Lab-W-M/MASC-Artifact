package androidx.core.app;

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
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.SparseArray;
import android.widget.RemoteViews;
import androidx.core.R.color;
import androidx.core.R.dimen;
import androidx.core.R.drawable;
import androidx.core.R.id;
import androidx.core.R.integer;
import androidx.core.R.layout;
import androidx.core.R.string;
import androidx.core.text.BidiFormatter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

   public static NotificationCompat.Action getAction(Notification var0, int var1) {
      if (VERSION.SDK_INT >= 20) {
         return getActionCompatFromAction(var0.actions[var1]);
      } else if (VERSION.SDK_INT >= 19) {
         android.app.Notification.Action var3 = var0.actions[var1];
         Object var2 = null;
         SparseArray var4 = var0.extras.getSparseParcelableArray("android.support.actionExtras");
         Bundle var5 = (Bundle)var2;
         if (var4 != null) {
            var5 = (Bundle)var4.get(var1);
         }

         return NotificationCompatJellybean.readAction(var3.icon, var3.title, var3.actionIntent, var5);
      } else {
         return VERSION.SDK_INT >= 16 ? NotificationCompatJellybean.getAction(var0, var1) : null;
      }
   }

   static NotificationCompat.Action getActionCompatFromAction(android.app.Notification.Action var0) {
      android.app.RemoteInput[] var6 = var0.getRemoteInputs();
      int var1;
      RemoteInput[] var4;
      if (var6 == null) {
         var4 = null;
      } else {
         RemoteInput[] var5 = new RemoteInput[var6.length];
         var1 = 0;

         while(true) {
            var4 = var5;
            if (var1 >= var6.length) {
               break;
            }

            android.app.RemoteInput var7 = var6[var1];
            var5[var1] = new RemoteInput(var7.getResultKey(), var7.getLabel(), var7.getChoices(), var7.getAllowFreeFormInput(), var7.getExtras(), (Set)null);
            ++var1;
         }
      }

      boolean var2;
      if (VERSION.SDK_INT >= 24) {
         if (!var0.getExtras().getBoolean("android.support.allowGeneratedReplies") && !var0.getAllowGeneratedReplies()) {
            var2 = false;
         } else {
            var2 = true;
         }
      } else {
         var2 = var0.getExtras().getBoolean("android.support.allowGeneratedReplies");
      }

      boolean var3 = var0.getExtras().getBoolean("android.support.action.showsUserInterface", true);
      if (VERSION.SDK_INT >= 28) {
         var1 = var0.getSemanticAction();
      } else {
         var1 = var0.getExtras().getInt("android.support.action.semanticAction", 0);
      }

      return new NotificationCompat.Action(var0.icon, var0.title, var0.actionIntent, var0.getExtras(), var4, (RemoteInput[])null, var2, var1, var3);
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

   public static CharSequence getContentTitle(Notification var0) {
      return var0.extras.getCharSequence("android.title");
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

   public static List getInvisibleActions(Notification var0) {
      ArrayList var2 = new ArrayList();
      Bundle var3 = var0.extras.getBundle("android.car.EXTENSIONS");
      if (var3 == null) {
         return var2;
      } else {
         var3 = var3.getBundle("invisible_actions");
         if (var3 != null) {
            for(int var1 = 0; var1 < var3.size(); ++var1) {
               var2.add(NotificationCompatJellybean.getActionFromBundle(var3.getBundle(Integer.toString(var1))));
            }
         }

         return var2;
      }
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

   public static class Action {
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

      public Action(int var1, CharSequence var2, PendingIntent var3) {
         this(var1, var2, var3, new Bundle(), (RemoteInput[])null, (RemoteInput[])null, true, 0, true);
      }

      Action(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInput[] var5, RemoteInput[] var6, boolean var7, int var8, boolean var9) {
         this.mShowsUserInterface = true;
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
         this.mSemanticAction = var8;
         this.mShowsUserInterface = var9;
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

      public static final class Builder {
         private boolean mAllowGeneratedReplies;
         private final Bundle mExtras;
         private final int mIcon;
         private final PendingIntent mIntent;
         private ArrayList mRemoteInputs;
         private int mSemanticAction;
         private boolean mShowsUserInterface;
         private final CharSequence mTitle;

         public Builder(int var1, CharSequence var2, PendingIntent var3) {
            this(var1, var2, var3, new Bundle(), (RemoteInput[])null, true, 0, true);
         }

         private Builder(int var1, CharSequence var2, PendingIntent var3, Bundle var4, RemoteInput[] var5, boolean var6, int var7, boolean var8) {
            this.mAllowGeneratedReplies = true;
            this.mShowsUserInterface = true;
            this.mIcon = var1;
            this.mTitle = NotificationCompat.Builder.limitCharSequenceLength(var2);
            this.mIntent = var3;
            this.mExtras = var4;
            ArrayList var9;
            if (var5 == null) {
               var9 = null;
            } else {
               var9 = new ArrayList(Arrays.asList(var5));
            }

            this.mRemoteInputs = var9;
            this.mAllowGeneratedReplies = var6;
            this.mSemanticAction = var7;
            this.mShowsUserInterface = var8;
         }

         public Builder(NotificationCompat.Action var1) {
            this(var1.icon, var1.title, var1.actionIntent, new Bundle(var1.mExtras), var1.getRemoteInputs(), var1.getAllowGeneratedReplies(), var1.getSemanticAction(), var1.mShowsUserInterface);
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

            return new NotificationCompat.Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, var6, var5, this.mAllowGeneratedReplies, this.mSemanticAction, this.mShowsUserInterface);
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

         public NotificationCompat.Action.Builder setSemanticAction(int var1) {
            this.mSemanticAction = var1;
            return this;
         }

         public NotificationCompat.Action.Builder setShowsUserInterface(boolean var1) {
            this.mShowsUserInterface = var1;
            return this;
         }
      }

      public interface Extender {
         NotificationCompat.Action.Builder extend(NotificationCompat.Action.Builder var1);
      }

      @Retention(RetentionPolicy.SOURCE)
      public @interface SemanticAction {
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
               this.mFlags &= var1;
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

         @Deprecated
         public CharSequence getCancelLabel() {
            return this.mCancelLabel;
         }

         @Deprecated
         public CharSequence getConfirmLabel() {
            return this.mConfirmLabel;
         }

         public boolean getHintDisplayActionInline() {
            return (this.mFlags & 4) != 0;
         }

         public boolean getHintLaunchesActivity() {
            return (this.mFlags & 2) != 0;
         }

         @Deprecated
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

         @Deprecated
         public NotificationCompat.Action.WearableExtender setCancelLabel(CharSequence var1) {
            this.mCancelLabel = var1;
            return this;
         }

         @Deprecated
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

         @Deprecated
         public NotificationCompat.Action.WearableExtender setInProgressLabel(CharSequence var1) {
            this.mInProgressLabel = var1;
            return this;
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
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

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            android.app.Notification.BigPictureStyle var2 = (new android.app.Notification.BigPictureStyle(var1.getBuilder())).setBigContentTitle(this.mBigContentTitle).bigPicture(this.mPicture);
            if (this.mBigLargeIconSet) {
               var2.bigLargeIcon(this.mBigLargeIcon);
            }

            if (this.mSummaryTextSet) {
               var2.setSummaryText(this.mSummaryText);
            }
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

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            android.app.Notification.BigTextStyle var2 = (new android.app.Notification.BigTextStyle(var1.getBuilder())).setBigContentTitle(this.mBigContentTitle).bigText(this.mBigText);
            if (this.mSummaryTextSet) {
               var2.setSummaryText(this.mSummaryText);
            }
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
      public ArrayList mActions;
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
      ArrayList mInvisibleActions;
      Bitmap mLargeIcon;
      boolean mLocalOnly;
      Notification mNotification;
      int mNumber;
      @Deprecated
      public ArrayList mPeople;
      int mPriority;
      int mProgress;
      boolean mProgressIndeterminate;
      int mProgressMax;
      Notification mPublicVersion;
      CharSequence[] mRemoteInputHistory;
      String mShortcutId;
      boolean mShowWhen;
      String mSortKey;
      NotificationCompat.Style mStyle;
      CharSequence mSubText;
      RemoteViews mTickerView;
      long mTimeout;
      boolean mUseChronometer;
      int mVisibility;

      @Deprecated
      public Builder(Context var1) {
         this(var1, (String)null);
      }

      public Builder(Context var1, String var2) {
         this.mActions = new ArrayList();
         this.mInvisibleActions = new ArrayList();
         this.mShowWhen = true;
         this.mLocalOnly = false;
         this.mColor = 0;
         this.mVisibility = 0;
         this.mBadgeIcon = 0;
         this.mGroupAlertBehavior = 0;
         Notification var3 = new Notification();
         this.mNotification = var3;
         this.mContext = var1;
         this.mChannelId = var2;
         var3.when = System.currentTimeMillis();
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

      private Bitmap reduceLargeIconSize(Bitmap var1) {
         if (var1 != null) {
            if (VERSION.SDK_INT >= 27) {
               return var1;
            } else {
               Resources var6 = this.mContext.getResources();
               int var4 = var6.getDimensionPixelSize(dimen.compat_notification_large_icon_max_width);
               int var5 = var6.getDimensionPixelSize(dimen.compat_notification_large_icon_max_height);
               if (var1.getWidth() <= var4 && var1.getHeight() <= var5) {
                  return var1;
               } else {
                  double var2 = Math.min((double)var4 / (double)Math.max(1, var1.getWidth()), (double)var5 / (double)Math.max(1, var1.getHeight()));
                  return Bitmap.createScaledBitmap(var1, (int)Math.ceil((double)var1.getWidth() * var2), (int)Math.ceil((double)var1.getHeight() * var2), true);
               }
            }
         } else {
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
            var3.flags &= var1;
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

      public NotificationCompat.Builder addInvisibleAction(int var1, CharSequence var2, PendingIntent var3) {
         return this.addInvisibleAction(new NotificationCompat.Action(var1, var2, var3));
      }

      public NotificationCompat.Builder addInvisibleAction(NotificationCompat.Action var1) {
         this.mInvisibleActions.add(var1);
         return this;
      }

      public NotificationCompat.Builder addPerson(String var1) {
         this.mPeople.add(var1);
         return this;
      }

      public Notification build() {
         return (new NotificationCompatBuilder(this)).build();
      }

      public NotificationCompat.Builder extend(NotificationCompat.Extender var1) {
         var1.extend(this);
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

      public NotificationCompat.Builder setChannelId(String var1) {
         this.mChannelId = var1;
         return this;
      }

      public NotificationCompat.Builder setColor(int var1) {
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
         this.mNotification.defaults = var1;
         if ((var1 & 4) != 0) {
            Notification var2 = this.mNotification;
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
         this.mLargeIcon = this.reduceLargeIconSize(var1);
         return this;
      }

      public NotificationCompat.Builder setLights(int var1, int var2, int var3) {
         this.mNotification.ledARGB = var1;
         this.mNotification.ledOnMS = var2;
         this.mNotification.ledOffMS = var3;
         var1 = this.mNotification.ledOnMS;
         byte var6 = 1;
         boolean var5;
         if (var1 != 0 && this.mNotification.ledOffMS != 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         Notification var4 = this.mNotification;
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
         this.mNotification.icon = var1;
         this.mNotification.iconLevel = var2;
         return this;
      }

      public NotificationCompat.Builder setSortKey(String var1) {
         this.mSortKey = var1;
         return this;
      }

      public NotificationCompat.Builder setSound(Uri var1) {
         this.mNotification.sound = var1;
         this.mNotification.audioStreamType = -1;
         if (VERSION.SDK_INT >= 21) {
            this.mNotification.audioAttributes = (new android.media.AudioAttributes.Builder()).setContentType(4).setUsage(5).build();
         }

         return this;
      }

      public NotificationCompat.Builder setSound(Uri var1, int var2) {
         this.mNotification.sound = var1;
         this.mNotification.audioStreamType = var2;
         if (VERSION.SDK_INT >= 21) {
            this.mNotification.audioAttributes = (new android.media.AudioAttributes.Builder()).setContentType(4).setLegacyStreamType(var2).build();
         }

         return this;
      }

      public NotificationCompat.Builder setStyle(NotificationCompat.Style var1) {
         if (this.mStyle != var1) {
            this.mStyle = var1;
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

   public static final class CarExtender implements NotificationCompat.Extender {
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
               this.mUnreadConversation = getUnreadConversationFromBundle(var2.getBundle("car_conversation"));
            }

         }
      }

      private static Bundle getBundleForUnreadConversation(NotificationCompat.CarExtender.UnreadConversation var0) {
         Bundle var4 = new Bundle();
         Parcelable[] var3 = null;
         String var2 = var3;
         if (var0.getParticipants() != null) {
            var2 = var3;
            if (var0.getParticipants().length > 1) {
               var2 = var0.getParticipants()[0];
            }
         }

         var3 = new Parcelable[var0.getMessages().length];

         for(int var1 = 0; var1 < var3.length; ++var1) {
            Bundle var5 = new Bundle();
            var5.putString("text", var0.getMessages()[var1]);
            var5.putString("author", var2);
            var3[var1] = var5;
         }

         var4.putParcelableArray("messages", var3);
         RemoteInput var6 = var0.getRemoteInput();
         if (var6 != null) {
            var4.putParcelable("remote_input", (new android.app.RemoteInput.Builder(var6.getResultKey())).setLabel(var6.getLabel()).setChoices(var6.getChoices()).setAllowFreeFormInput(var6.getAllowFreeFormInput()).addExtras(var6.getExtras()).build());
         }

         var4.putParcelable("on_reply", var0.getReplyPendingIntent());
         var4.putParcelable("on_read", var0.getReadPendingIntent());
         var4.putStringArray("participants", var0.getParticipants());
         var4.putLong("timestamp", var0.getLatestTimestamp());
         return var4;
      }

      private static NotificationCompat.CarExtender.UnreadConversation getUnreadConversationFromBundle(Bundle var0) {
         RemoteInput var5 = null;
         if (var0 == null) {
            return null;
         } else {
            Parcelable[] var6 = var0.getParcelableArray("messages");
            String[] var4 = null;
            if (var6 != null) {
               var4 = new String[var6.length];
               boolean var3 = true;
               int var2 = 0;

               boolean var1;
               while(true) {
                  var1 = var3;
                  if (var2 >= var4.length) {
                     break;
                  }

                  if (!(var6[var2] instanceof Bundle)) {
                     var1 = false;
                     break;
                  }

                  var4[var2] = ((Bundle)var6[var2]).getString("text");
                  if (var4[var2] == null) {
                     var1 = false;
                     break;
                  }

                  ++var2;
               }

               if (!var1) {
                  return null;
               }
            }

            PendingIntent var10 = (PendingIntent)var0.getParcelable("on_read");
            PendingIntent var7 = (PendingIntent)var0.getParcelable("on_reply");
            android.app.RemoteInput var9 = (android.app.RemoteInput)var0.getParcelable("remote_input");
            String[] var8 = var0.getStringArray("participants");
            if (var8 != null) {
               if (var8.length != 1) {
                  return null;
               } else {
                  if (var9 != null) {
                     var5 = new RemoteInput(var9.getResultKey(), var9.getLabel(), var9.getChoices(), var9.getAllowFreeFormInput(), var9.getExtras(), (Set)null);
                  }

                  return new NotificationCompat.CarExtender.UnreadConversation(var4, var5, var7, var10, var8, var0.getLong("timestamp"));
               }
            } else {
               return null;
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

            NotificationCompat.CarExtender.UnreadConversation var5 = this.mUnreadConversation;
            if (var5 != null) {
               var3.putBundle("car_conversation", getBundleForUnreadConversation(var5));
            }

            var1.getExtras().putBundle("android.car.EXTENSIONS", var3);
            return var1;
         }
      }

      public int getColor() {
         return this.mColor;
      }

      public Bitmap getLargeIcon() {
         return this.mLargeIcon;
      }

      public NotificationCompat.CarExtender.UnreadConversation getUnreadConversation() {
         return this.mUnreadConversation;
      }

      public NotificationCompat.CarExtender setColor(int var1) {
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

      public static class UnreadConversation {
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
         int var3 = layout.notification_template_custom_big;
         byte var5 = 0;
         RemoteViews var8 = this.applyStandardTemplate(true, var3, false);
         var8.removeAllViews(id.actions);
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
                     var8.addView(id.actions, var9);
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

         var8.setViewVisibility(id.actions, var12);
         var8.setViewVisibility(id.action_divider, var12);
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
            var3 = layout.notification_action_tombstone;
         } else {
            var3 = layout.notification_action;
         }

         RemoteViews var5 = new RemoteViews(var4, var3);
         var5.setImageViewBitmap(id.action_image, this.createColoredBitmap(var1.getIcon(), this.mBuilder.mContext.getResources().getColor(color.notification_action_color_filter)));
         var5.setTextViewText(id.action_text, var1.title);
         if (!var2) {
            var5.setOnClickPendingIntent(id.action_container, var1.actionIntent);
         }

         if (VERSION.SDK_INT >= 15) {
            var5.setContentDescription(id.action_container, var1.title);
         }

         return var5;
      }

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            var1.getBuilder().setStyle(new android.app.Notification.DecoratedCustomViewStyle());
         }

      }

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

      public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 24) {
            return null;
         } else {
            return this.mBuilder.getContentView() == null ? null : this.createRemoteViews(this.mBuilder.getContentView(), false);
         }
      }

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

   @Retention(RetentionPolicy.SOURCE)
   public @interface GroupAlertBehavior {
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

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         if (VERSION.SDK_INT >= 16) {
            android.app.Notification.InboxStyle var3 = (new android.app.Notification.InboxStyle(var1.getBuilder())).setBigContentTitle(this.mBigContentTitle);
            if (this.mSummaryTextSet) {
               var3.setSummaryText(this.mSummaryText);
            }

            Iterator var2 = this.mTexts.iterator();

            while(var2.hasNext()) {
               var3.addLine((CharSequence)var2.next());
            }
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
      private CharSequence mConversationTitle;
      private Boolean mIsGroupConversation;
      private final List mMessages = new ArrayList();
      private Person mUser;

      private MessagingStyle() {
      }

      public MessagingStyle(Person var1) {
         if (!TextUtils.isEmpty(var1.getName())) {
            this.mUser = var1;
         } else {
            throw new IllegalArgumentException("User's name must not be empty.");
         }
      }

      @Deprecated
      public MessagingStyle(CharSequence var1) {
         this.mUser = (new Person.Builder()).setName(var1).build();
      }

      public static NotificationCompat.MessagingStyle extractMessagingStyleFromNotification(Notification var0) {
         Bundle var3 = NotificationCompat.getExtras(var0);
         if (var3 != null && !var3.containsKey("android.selfDisplayName") && !var3.containsKey("android.messagingStyleUser")) {
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

      private NotificationCompat.MessagingStyle.Message findLatestIncomingMessage() {
         for(int var1 = this.mMessages.size() - 1; var1 >= 0; --var1) {
            NotificationCompat.MessagingStyle.Message var2 = (NotificationCompat.MessagingStyle.Message)this.mMessages.get(var1);
            if (var2.getPerson() != null && !TextUtils.isEmpty(var2.getPerson().getName())) {
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
            NotificationCompat.MessagingStyle.Message var2 = (NotificationCompat.MessagingStyle.Message)this.mMessages.get(var1);
            if (var2.getPerson() != null && var2.getPerson().getName() == null) {
               return true;
            }
         }

         return false;
      }

      private TextAppearanceSpan makeFontColorSpan(int var1) {
         return new TextAppearanceSpan((String)null, 0, 0, ColorStateList.valueOf(var1), (ColorStateList)null);
      }

      private CharSequence makeMessageLine(NotificationCompat.MessagingStyle.Message var1) {
         BidiFormatter var8 = BidiFormatter.getInstance();
         SpannableStringBuilder var9 = new SpannableStringBuilder();
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

         Person var5 = var1.getPerson();
         String var7 = "";
         Object var11;
         if (var5 == null) {
            var11 = "";
         } else {
            var11 = var1.getPerson().getName();
         }

         int var4 = var2;
         Object var6 = var11;
         if (TextUtils.isEmpty((CharSequence)var11)) {
            var6 = this.mUser.getName();
            if (var3 && this.mBuilder.getColor() != 0) {
               var2 = this.mBuilder.getColor();
            }

            var4 = var2;
         }

         CharSequence var12 = var8.unicodeWrap((CharSequence)var6);
         var9.append(var12);
         var9.setSpan(this.makeFontColorSpan(var4), var9.length() - var12.length(), var9.length(), 33);
         Object var10;
         if (var1.getText() == null) {
            var10 = var7;
         } else {
            var10 = var1.getText();
         }

         var9.append("  ").append(var8.unicodeWrap((CharSequence)var10));
         return var9;
      }

      public void addCompatExtras(Bundle var1) {
         super.addCompatExtras(var1);
         var1.putCharSequence("android.selfDisplayName", this.mUser.getName());
         var1.putBundle("android.messagingStyleUser", this.mUser.toBundle());
         var1.putCharSequence("android.hiddenConversationTitle", this.mConversationTitle);
         if (this.mConversationTitle != null && this.mIsGroupConversation) {
            var1.putCharSequence("android.conversationTitle", this.mConversationTitle);
         }

         if (!this.mMessages.isEmpty()) {
            var1.putParcelableArray("android.messages", NotificationCompat.MessagingStyle.Message.getBundleArrayForMessages(this.mMessages));
         }

         Boolean var2 = this.mIsGroupConversation;
         if (var2 != null) {
            var1.putBoolean("android.isGroupConversation", var2);
         }

      }

      public NotificationCompat.MessagingStyle addMessage(NotificationCompat.MessagingStyle.Message var1) {
         this.mMessages.add(var1);
         if (this.mMessages.size() > 25) {
            this.mMessages.remove(0);
         }

         return this;
      }

      public NotificationCompat.MessagingStyle addMessage(CharSequence var1, long var2, Person var4) {
         this.addMessage(new NotificationCompat.MessagingStyle.Message(var1, var2, var4));
         return this;
      }

      @Deprecated
      public NotificationCompat.MessagingStyle addMessage(CharSequence var1, long var2, CharSequence var4) {
         this.mMessages.add(new NotificationCompat.MessagingStyle.Message(var1, var2, (new Person.Builder()).setName(var4).build()));
         if (this.mMessages.size() > 25) {
            this.mMessages.remove(0);
         }

         return this;
      }

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
         this.setGroupConversation(this.isGroupConversation());
         if (VERSION.SDK_INT >= 24) {
            android.app.Notification.MessagingStyle var15;
            if (VERSION.SDK_INT >= 28) {
               var15 = new android.app.Notification.MessagingStyle(this.mUser.toAndroidPerson());
            } else {
               var15 = new android.app.Notification.MessagingStyle(this.mUser.getName());
            }

            if (this.mIsGroupConversation || VERSION.SDK_INT >= 28) {
               var15.setConversationTitle(this.mConversationTitle);
            }

            if (VERSION.SDK_INT >= 28) {
               var15.setGroupConversation(this.mIsGroupConversation);
            }

            android.app.Notification.MessagingStyle.Message var16;
            for(Iterator var8 = this.mMessages.iterator(); var8.hasNext(); var15.addMessage(var16)) {
               NotificationCompat.MessagingStyle.Message var9 = (NotificationCompat.MessagingStyle.Message)var8.next();
               if (VERSION.SDK_INT >= 28) {
                  Person var13 = var9.getPerson();
                  CharSequence var10 = var9.getText();
                  long var4 = var9.getTimestamp();
                  android.app.Person var14;
                  if (var13 == null) {
                     var14 = null;
                  } else {
                     var14 = var13.toAndroidPerson();
                  }

                  var16 = new android.app.Notification.MessagingStyle.Message(var10, var4, var14);
               } else {
                  CharSequence var17 = null;
                  if (var9.getPerson() != null) {
                     var17 = var9.getPerson().getName();
                  }

                  var16 = new android.app.Notification.MessagingStyle.Message(var9.getText(), var9.getTimestamp(), var17);
               }

               if (var9.getDataMimeType() != null) {
                  var16.setData(var9.getDataMimeType(), var9.getDataUri());
               }
            }

            var15.setBuilder(var1.getBuilder());
         } else {
            NotificationCompat.MessagingStyle.Message var6 = this.findLatestIncomingMessage();
            if (this.mConversationTitle != null && this.mIsGroupConversation) {
               var1.getBuilder().setContentTitle(this.mConversationTitle);
            } else if (var6 != null) {
               var1.getBuilder().setContentTitle("");
               if (var6.getPerson() != null) {
                  var1.getBuilder().setContentTitle(var6.getPerson().getName());
               }
            }

            CharSequence var11;
            if (var6 != null) {
               android.app.Notification.Builder var7 = var1.getBuilder();
               if (this.mConversationTitle != null) {
                  var11 = this.makeMessageLine(var6);
               } else {
                  var11 = var6.getText();
               }

               var7.setContentText(var11);
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
                  var6 = (NotificationCompat.MessagingStyle.Message)this.mMessages.get(var3);
                  if (var2) {
                     var11 = this.makeMessageLine(var6);
                  } else {
                     var11 = var6.getText();
                  }

                  if (var3 != this.mMessages.size() - 1) {
                     var12.insert(0, "\n");
                  }

                  var12.insert(0, var11);
               }

               (new android.app.Notification.BigTextStyle(var1.getBuilder())).setBigContentTitle((CharSequence)null).bigText(var12);
            }

         }
      }

      public CharSequence getConversationTitle() {
         return this.mConversationTitle;
      }

      public List getMessages() {
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
         NotificationCompat.Builder var3 = this.mBuilder;
         boolean var2 = false;
         boolean var1 = false;
         if (var3 != null && this.mBuilder.mContext.getApplicationInfo().targetSdkVersion < 28 && this.mIsGroupConversation == null) {
            if (this.mConversationTitle != null) {
               var1 = true;
            }

            return var1;
         } else {
            Boolean var4 = this.mIsGroupConversation;
            var1 = var2;
            if (var4 != null) {
               var1 = var4;
            }

            return var1;
         }
      }

      protected void restoreFromCompatExtras(Bundle var1) {
         this.mMessages.clear();
         if (var1.containsKey("android.messagingStyleUser")) {
            this.mUser = Person.fromBundle(var1.getBundle("android.messagingStyleUser"));
         } else {
            this.mUser = (new Person.Builder()).setName(var1.getString("android.selfDisplayName")).build();
         }

         CharSequence var2 = var1.getCharSequence("android.conversationTitle");
         this.mConversationTitle = var2;
         if (var2 == null) {
            this.mConversationTitle = var1.getCharSequence("android.hiddenConversationTitle");
         }

         Parcelable[] var3 = var1.getParcelableArray("android.messages");
         if (var3 != null) {
            this.mMessages.addAll(NotificationCompat.MessagingStyle.Message.getMessagesFromBundleArray(var3));
         }

         if (var1.containsKey("android.isGroupConversation")) {
            this.mIsGroupConversation = var1.getBoolean("android.isGroupConversation");
         }

      }

      public NotificationCompat.MessagingStyle setConversationTitle(CharSequence var1) {
         this.mConversationTitle = var1;
         return this;
      }

      public NotificationCompat.MessagingStyle setGroupConversation(boolean var1) {
         this.mIsGroupConversation = var1;
         return this;
      }

      public static final class Message {
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

         public Message(CharSequence var1, long var2, Person var4) {
            this.mExtras = new Bundle();
            this.mText = var1;
            this.mTimestamp = var2;
            this.mPerson = var4;
         }

         @Deprecated
         public Message(CharSequence var1, long var2, CharSequence var4) {
            this(var1, var2, (new Person.Builder()).setName(var4).build());
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
            boolean var10001;
            try {
               if (!var0.containsKey("text")) {
                  return null;
               }

               if (!var0.containsKey("time")) {
                  return null;
               }
            } catch (ClassCastException var7) {
               var10001 = false;
               return null;
            }

            Person var1 = null;

            label85: {
               try {
                  if (var0.containsKey("person")) {
                     var1 = Person.fromBundle(var0.getBundle("person"));
                     break label85;
                  }
               } catch (ClassCastException var6) {
                  var10001 = false;
                  return null;
               }

               try {
                  if (var0.containsKey("sender_person") && VERSION.SDK_INT >= 28) {
                     var1 = Person.fromAndroidPerson((android.app.Person)var0.getParcelable("sender_person"));
                     break label85;
                  }
               } catch (ClassCastException var5) {
                  var10001 = false;
                  return null;
               }

               try {
                  if (var0.containsKey("sender")) {
                     var1 = (new Person.Builder()).setName(var0.getCharSequence("sender")).build();
                  }
               } catch (ClassCastException var4) {
                  var10001 = false;
                  return null;
               }
            }

            NotificationCompat.MessagingStyle.Message var8;
            try {
               var8 = new NotificationCompat.MessagingStyle.Message(var0.getCharSequence("text"), var0.getLong("time"), var1);
               if (var0.containsKey("type") && var0.containsKey("uri")) {
                  var8.setData(var0.getString("type"), (Uri)var0.getParcelable("uri"));
               }
            } catch (ClassCastException var3) {
               var10001 = false;
               return null;
            }

            try {
               if (var0.containsKey("extras")) {
                  var8.getExtras().putAll(var0.getBundle("extras"));
               }

               return var8;
            } catch (ClassCastException var2) {
               var10001 = false;
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
            Person var3 = this.mPerson;
            if (var3 != null) {
               var1.putCharSequence("sender", var3.getName());
               if (VERSION.SDK_INT >= 28) {
                  var1.putParcelable("sender_person", this.mPerson.toAndroidPerson());
               } else {
                  var1.putBundle("person", this.mPerson.toBundle());
               }
            }

            String var4 = this.mDataMimeType;
            if (var4 != null) {
               var1.putString("type", var4);
            }

            Uri var5 = this.mDataUri;
            if (var5 != null) {
               var1.putParcelable("uri", var5);
            }

            Bundle var6 = this.mExtras;
            if (var6 != null) {
               var1.putBundle("extras", var6);
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

         public Person getPerson() {
            return this.mPerson;
         }

         @Deprecated
         public CharSequence getSender() {
            Person var1 = this.mPerson;
            return var1 == null ? null : var1.getName();
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

   @Retention(RetentionPolicy.SOURCE)
   public @interface NotificationVisibility {
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface StreamType {
   }

   public abstract static class Style {
      CharSequence mBigContentTitle;
      protected NotificationCompat.Builder mBuilder;
      CharSequence mSummaryText;
      boolean mSummaryTextSet = false;

      private int calculateTopPadding() {
         Resources var4 = this.mBuilder.mContext.getResources();
         int var2 = var4.getDimensionPixelSize(dimen.notification_top_pad);
         int var3 = var4.getDimensionPixelSize(dimen.notification_top_pad_large_text);
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
         int var5 = drawable.notification_icon_background;
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
         var1.setViewVisibility(id.title, 8);
         var1.setViewVisibility(id.text2, 8);
         var1.setViewVisibility(id.text, 8);
      }

      public void addCompatExtras(Bundle var1) {
      }

      public void apply(NotificationBuilderWithBuilderAccessor var1) {
      }

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
               var10.setInt(id.notification_background, "setBackgroundResource", drawable.notification_bg_low);
               var10.setInt(id.icon, "setBackgroundResource", drawable.notification_template_icon_low_bg);
            } else {
               var10.setInt(id.notification_background, "setBackgroundResource", drawable.notification_bg);
               var10.setInt(id.icon, "setBackgroundResource", drawable.notification_template_icon_bg);
            }
         }

         int var6;
         Bitmap var11;
         if (this.mBuilder.mLargeIcon != null) {
            if (VERSION.SDK_INT >= 16) {
               var10.setViewVisibility(id.icon, 0);
               var10.setImageViewBitmap(id.icon, this.mBuilder.mLargeIcon);
            } else {
               var10.setViewVisibility(id.icon, 8);
            }

            if (var1 && this.mBuilder.mNotification.icon != 0) {
               var2 = var9.getDimensionPixelSize(dimen.notification_right_icon_size);
               var6 = var9.getDimensionPixelSize(dimen.notification_small_icon_background_padding);
               if (VERSION.SDK_INT >= 21) {
                  var11 = this.createIconWithBackground(this.mBuilder.mNotification.icon, var2, var2 - var6 * 2, this.mBuilder.getColor());
                  var10.setImageViewBitmap(id.right_icon, var11);
               } else {
                  var10.setImageViewBitmap(id.right_icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
               }

               var10.setViewVisibility(id.right_icon, 0);
            }
         } else if (var1 && this.mBuilder.mNotification.icon != 0) {
            var10.setViewVisibility(id.icon, 0);
            if (VERSION.SDK_INT >= 21) {
               var2 = var9.getDimensionPixelSize(dimen.notification_large_icon_width);
               var6 = var9.getDimensionPixelSize(dimen.notification_big_circle_margin);
               int var8 = var9.getDimensionPixelSize(dimen.notification_small_icon_size_as_large);
               var11 = this.createIconWithBackground(this.mBuilder.mNotification.icon, var2 - var6, var8, this.mBuilder.getColor());
               var10.setImageViewBitmap(id.icon, var11);
            } else {
               var10.setImageViewBitmap(id.icon, this.createColoredBitmap(this.mBuilder.mNotification.icon, -1));
            }
         }

         if (this.mBuilder.mContentTitle != null) {
            var10.setTextViewText(id.title, this.mBuilder.mContentTitle);
         }

         if (this.mBuilder.mContentText != null) {
            var10.setTextViewText(id.text, this.mBuilder.mContentText);
            var5 = true;
         }

         if (VERSION.SDK_INT < 21 && this.mBuilder.mLargeIcon != null) {
            var12 = true;
         } else {
            var12 = false;
         }

         if (this.mBuilder.mContentInfo != null) {
            var10.setTextViewText(id.info, this.mBuilder.mContentInfo);
            var10.setViewVisibility(id.info, 0);
            var5 = true;
            var12 = true;
         } else if (this.mBuilder.mNumber > 0) {
            var2 = var9.getInteger(integer.status_bar_notification_info_maxnum);
            if (this.mBuilder.mNumber > var2) {
               var10.setTextViewText(id.info, var9.getString(string.status_bar_notification_info_overflow));
            } else {
               NumberFormat var15 = NumberFormat.getIntegerInstance();
               var10.setTextViewText(id.info, var15.format((long)this.mBuilder.mNumber));
            }

            var10.setViewVisibility(id.info, 0);
            var5 = true;
            var12 = true;
         } else {
            var10.setViewVisibility(id.info, 8);
         }

         boolean var13;
         label106: {
            if (this.mBuilder.mSubText != null && VERSION.SDK_INT >= 16) {
               var10.setTextViewText(id.text, this.mBuilder.mSubText);
               if (this.mBuilder.mContentText != null) {
                  var10.setTextViewText(id.text2, this.mBuilder.mContentText);
                  var10.setViewVisibility(id.text2, 0);
                  var13 = true;
                  break label106;
               }

               var10.setViewVisibility(id.text2, 8);
            }

            var13 = false;
         }

         if (var13 && VERSION.SDK_INT >= 16) {
            if (var3) {
               float var4 = (float)var9.getDimensionPixelSize(dimen.notification_subtext_size);
               var10.setTextViewTextSize(id.text, 0, var4);
            }

            var10.setViewPadding(id.line1, 0, 0, 0, 0);
         }

         if (this.mBuilder.getWhenIfShowing() != 0L) {
            if (this.mBuilder.mUseChronometer && VERSION.SDK_INT >= 16) {
               var10.setViewVisibility(id.chronometer, 0);
               var10.setLong(id.chronometer, "setBase", this.mBuilder.getWhenIfShowing() + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
               var10.setBoolean(id.chronometer, "setStarted", true);
            } else {
               var10.setViewVisibility(id.time, 0);
               var10.setLong(id.time, "setTime", this.mBuilder.getWhenIfShowing());
            }

            var12 = true;
         }

         var6 = id.right_side;
         byte var14;
         if (var12) {
            var14 = 0;
         } else {
            var14 = 8;
         }

         var10.setViewVisibility(var6, var14);
         var6 = id.line3;
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

      public void buildIntoRemoteViews(RemoteViews var1, RemoteViews var2) {
         this.hideNormalContent(var1);
         var1.removeAllViews(id.notification_main_column);
         var1.addView(id.notification_main_column, var2.clone());
         var1.setViewVisibility(id.notification_main_column, 0);
         if (VERSION.SDK_INT >= 21) {
            var1.setViewPadding(id.notification_main_column_container, 0, this.calculateTopPadding(), 0, 0);
         }

      }

      public Bitmap createColoredBitmap(int var1, int var2) {
         return this.createColoredBitmap(var1, var2, 0);
      }

      public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor var1) {
         return null;
      }

      protected void restoreFromCompatExtras(Bundle var1) {
      }

      public void setBuilder(NotificationCompat.Builder var1) {
         if (this.mBuilder != var1) {
            this.mBuilder = var1;
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
         Bundle var5 = NotificationCompat.getExtras(var1);
         if (var5 != null) {
            var5 = var5.getBundle("android.wearable.EXTENSIONS");
         } else {
            var5 = null;
         }

         if (var5 != null) {
            ArrayList var3 = var5.getParcelableArrayList("actions");
            if (VERSION.SDK_INT >= 16 && var3 != null) {
               NotificationCompat.Action[] var4 = new NotificationCompat.Action[var3.size()];

               for(int var2 = 0; var2 < var4.length; ++var2) {
                  if (VERSION.SDK_INT >= 20) {
                     var4[var2] = NotificationCompat.getActionCompatFromAction((android.app.Notification.Action)var3.get(var2));
                  } else if (VERSION.SDK_INT >= 16) {
                     var4[var2] = NotificationCompatJellybean.getActionFromBundle((Bundle)var3.get(var2));
                  }
               }

               Collections.addAll(this.mActions, (NotificationCompat.Action[])var4);
            }

            this.mFlags = var5.getInt("flags", 1);
            this.mDisplayIntent = (PendingIntent)var5.getParcelable("displayIntent");
            Notification[] var6 = NotificationCompat.getNotificationArrayFromBundle(var5, "pages");
            if (var6 != null) {
               Collections.addAll(this.mPages, var6);
            }

            this.mBackground = (Bitmap)var5.getParcelable("background");
            this.mContentIcon = var5.getInt("contentIcon");
            this.mContentIconGravity = var5.getInt("contentIconGravity", 8388613);
            this.mContentActionIndex = var5.getInt("contentActionIndex", -1);
            this.mCustomSizePreset = var5.getInt("customSizePreset", 0);
            this.mCustomContentHeight = var5.getInt("customContentHeight");
            this.mGravity = var5.getInt("gravity", 80);
            this.mHintScreenTimeout = var5.getInt("hintScreenTimeout");
            this.mDismissalId = var5.getString("dismissalId");
            this.mBridgeTag = var5.getString("bridgeTag");
         }

      }

      private static android.app.Notification.Action getActionFromActionCompat(NotificationCompat.Action var0) {
         android.app.Notification.Action.Builder var4 = new android.app.Notification.Action.Builder(var0.getIcon(), var0.getTitle(), var0.getActionIntent());
         Bundle var3;
         if (var0.getExtras() != null) {
            var3 = new Bundle(var0.getExtras());
         } else {
            var3 = new Bundle();
         }

         var3.putBoolean("android.support.allowGeneratedReplies", var0.getAllowGeneratedReplies());
         if (VERSION.SDK_INT >= 24) {
            var4.setAllowGeneratedReplies(var0.getAllowGeneratedReplies());
         }

         var4.addExtras(var3);
         RemoteInput[] var5 = var0.getRemoteInputs();
         if (var5 != null) {
            android.app.RemoteInput[] var6 = RemoteInput.fromCompat(var5);
            int var2 = var6.length;

            for(int var1 = 0; var1 < var2; ++var1) {
               var4.addRemoteInput(var6[var1]);
            }
         }

         return var4.build();
      }

      private void setFlag(int var1, boolean var2) {
         if (var2) {
            this.mFlags |= var1;
         } else {
            this.mFlags &= var1;
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

      @Deprecated
      public NotificationCompat.WearableExtender addPage(Notification var1) {
         this.mPages.add(var1);
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender addPages(List var1) {
         this.mPages.addAll(var1);
         return this;
      }

      public NotificationCompat.WearableExtender clearActions() {
         this.mActions.clear();
         return this;
      }

      @Deprecated
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
         ArrayList var4;
         if (!this.mActions.isEmpty()) {
            if (VERSION.SDK_INT >= 16) {
               var4 = new ArrayList(this.mActions.size());
               Iterator var5 = this.mActions.iterator();

               while(var5.hasNext()) {
                  NotificationCompat.Action var6 = (NotificationCompat.Action)var5.next();
                  if (VERSION.SDK_INT >= 20) {
                     var4.add(getActionFromActionCompat(var6));
                  } else if (VERSION.SDK_INT >= 16) {
                     var4.add(NotificationCompatJellybean.getBundleForAction(var6));
                  }
               }

               var3.putParcelableArrayList("actions", var4);
            } else {
               var3.putParcelableArrayList("actions", (ArrayList)null);
            }
         }

         int var2 = this.mFlags;
         if (var2 != 1) {
            var3.putInt("flags", var2);
         }

         PendingIntent var7 = this.mDisplayIntent;
         if (var7 != null) {
            var3.putParcelable("displayIntent", var7);
         }

         if (!this.mPages.isEmpty()) {
            var4 = this.mPages;
            var3.putParcelableArray("pages", (Parcelable[])var4.toArray(new Notification[var4.size()]));
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
         return (this.mFlags & 1) != 0;
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
         return (this.mFlags & 32) != 0;
      }

      @Deprecated
      public boolean getHintAvoidBackgroundClipping() {
         return (this.mFlags & 16) != 0;
      }

      public boolean getHintContentIntentLaunchesActivity() {
         return (this.mFlags & 64) != 0;
      }

      @Deprecated
      public boolean getHintHideIcon() {
         return (this.mFlags & 2) != 0;
      }

      @Deprecated
      public int getHintScreenTimeout() {
         return this.mHintScreenTimeout;
      }

      @Deprecated
      public boolean getHintShowBackgroundOnly() {
         return (this.mFlags & 4) != 0;
      }

      @Deprecated
      public List getPages() {
         return this.mPages;
      }

      public boolean getStartScrollBottom() {
         return (this.mFlags & 8) != 0;
      }

      @Deprecated
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

      @Deprecated
      public NotificationCompat.WearableExtender setContentIcon(int var1) {
         this.mContentIcon = var1;
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setContentIconGravity(int var1) {
         this.mContentIconGravity = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setContentIntentAvailableOffline(boolean var1) {
         this.setFlag(1, var1);
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setCustomContentHeight(int var1) {
         this.mCustomContentHeight = var1;
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setCustomSizePreset(int var1) {
         this.mCustomSizePreset = var1;
         return this;
      }

      public NotificationCompat.WearableExtender setDismissalId(String var1) {
         this.mDismissalId = var1;
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setDisplayIntent(PendingIntent var1) {
         this.mDisplayIntent = var1;
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setGravity(int var1) {
         this.mGravity = var1;
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setHintAmbientBigPicture(boolean var1) {
         this.setFlag(32, var1);
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setHintAvoidBackgroundClipping(boolean var1) {
         this.setFlag(16, var1);
         return this;
      }

      public NotificationCompat.WearableExtender setHintContentIntentLaunchesActivity(boolean var1) {
         this.setFlag(64, var1);
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setHintHideIcon(boolean var1) {
         this.setFlag(2, var1);
         return this;
      }

      @Deprecated
      public NotificationCompat.WearableExtender setHintScreenTimeout(int var1) {
         this.mHintScreenTimeout = var1;
         return this;
      }

      @Deprecated
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
