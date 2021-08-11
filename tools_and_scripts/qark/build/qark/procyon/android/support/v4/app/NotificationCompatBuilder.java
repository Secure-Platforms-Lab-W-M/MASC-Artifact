// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.util.SparseArray;
import android.app.Notification$Action$Builder;
import java.util.Iterator;
import android.app.Notification;
import android.net.Uri;
import android.text.TextUtils;
import android.os.Build$VERSION;
import java.util.ArrayList;
import android.app.Notification$Builder;
import android.widget.RemoteViews;
import android.os.Bundle;
import java.util.List;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor
{
    private final List<Bundle> mActionExtrasList;
    private RemoteViews mBigContentView;
    private final Notification$Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private final Bundle mExtras;
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;
    
    NotificationCompatBuilder(final NotificationCompat.Builder mBuilderCompat) {
        this.mActionExtrasList = new ArrayList<Bundle>();
        this.mExtras = new Bundle();
        this.mBuilderCompat = mBuilderCompat;
        if (Build$VERSION.SDK_INT >= 26) {
            this.mBuilder = new Notification$Builder(mBuilderCompat.mContext, mBuilderCompat.mChannelId);
        }
        else {
            this.mBuilder = new Notification$Builder(mBuilderCompat.mContext);
        }
        final Notification mNotification = mBuilderCompat.mNotification;
        this.mBuilder.setWhen(mNotification.when).setSmallIcon(mNotification.icon, mNotification.iconLevel).setContent(mNotification.contentView).setTicker(mNotification.tickerText, mBuilderCompat.mTickerView).setVibrate(mNotification.vibrate).setLights(mNotification.ledARGB, mNotification.ledOnMS, mNotification.ledOffMS).setOngoing((mNotification.flags & 0x2) != 0x0).setOnlyAlertOnce((mNotification.flags & 0x8) != 0x0).setAutoCancel((mNotification.flags & 0x10) != 0x0).setDefaults(mNotification.defaults).setContentTitle(mBuilderCompat.mContentTitle).setContentText(mBuilderCompat.mContentText).setContentInfo(mBuilderCompat.mContentInfo).setContentIntent(mBuilderCompat.mContentIntent).setDeleteIntent(mNotification.deleteIntent).setFullScreenIntent(mBuilderCompat.mFullScreenIntent, (mNotification.flags & 0x80) != 0x0).setLargeIcon(mBuilderCompat.mLargeIcon).setNumber(mBuilderCompat.mNumber).setProgress(mBuilderCompat.mProgressMax, mBuilderCompat.mProgress, mBuilderCompat.mProgressIndeterminate);
        if (Build$VERSION.SDK_INT < 21) {
            this.mBuilder.setSound(mNotification.sound, mNotification.audioStreamType);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            this.mBuilder.setSubText(mBuilderCompat.mSubText).setUsesChronometer(mBuilderCompat.mUseChronometer).setPriority(mBuilderCompat.mPriority);
            final Iterator<NotificationCompat.Action> iterator = mBuilderCompat.mActions.iterator();
            while (iterator.hasNext()) {
                this.addAction(iterator.next());
            }
            if (mBuilderCompat.mExtras != null) {
                this.mExtras.putAll(mBuilderCompat.mExtras);
            }
            if (Build$VERSION.SDK_INT < 20) {
                if (mBuilderCompat.mLocalOnly) {
                    this.mExtras.putBoolean("android.support.localOnly", true);
                }
                if (mBuilderCompat.mGroupKey != null) {
                    this.mExtras.putString("android.support.groupKey", mBuilderCompat.mGroupKey);
                    if (mBuilderCompat.mGroupSummary) {
                        this.mExtras.putBoolean("android.support.isGroupSummary", true);
                    }
                    else {
                        this.mExtras.putBoolean("android.support.useSideChannel", true);
                    }
                }
                if (mBuilderCompat.mSortKey != null) {
                    this.mExtras.putString("android.support.sortKey", mBuilderCompat.mSortKey);
                }
            }
            this.mContentView = mBuilderCompat.mContentView;
            this.mBigContentView = mBuilderCompat.mBigContentView;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            this.mBuilder.setShowWhen(mBuilderCompat.mShowWhen);
            if (Build$VERSION.SDK_INT < 21 && mBuilderCompat.mPeople != null && !mBuilderCompat.mPeople.isEmpty()) {
                this.mExtras.putStringArray("android.people", (String[])mBuilderCompat.mPeople.toArray(new String[mBuilderCompat.mPeople.size()]));
            }
        }
        if (Build$VERSION.SDK_INT >= 20) {
            this.mBuilder.setLocalOnly(mBuilderCompat.mLocalOnly).setGroup(mBuilderCompat.mGroupKey).setGroupSummary(mBuilderCompat.mGroupSummary).setSortKey(mBuilderCompat.mSortKey);
            this.mGroupAlertBehavior = mBuilderCompat.mGroupAlertBehavior;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mBuilder.setCategory(mBuilderCompat.mCategory).setColor(mBuilderCompat.mColor).setVisibility(mBuilderCompat.mVisibility).setPublicVersion(mBuilderCompat.mPublicVersion).setSound(mNotification.sound, mNotification.audioAttributes);
            final Iterator<String> iterator2 = mBuilderCompat.mPeople.iterator();
            while (iterator2.hasNext()) {
                this.mBuilder.addPerson((String)iterator2.next());
            }
            this.mHeadsUpContentView = mBuilderCompat.mHeadsUpContentView;
        }
        if (Build$VERSION.SDK_INT >= 24) {
            this.mBuilder.setExtras(mBuilderCompat.mExtras).setRemoteInputHistory(mBuilderCompat.mRemoteInputHistory);
            if (mBuilderCompat.mContentView != null) {
                this.mBuilder.setCustomContentView(mBuilderCompat.mContentView);
            }
            if (mBuilderCompat.mBigContentView != null) {
                this.mBuilder.setCustomBigContentView(mBuilderCompat.mBigContentView);
            }
            if (mBuilderCompat.mHeadsUpContentView != null) {
                this.mBuilder.setCustomHeadsUpContentView(mBuilderCompat.mHeadsUpContentView);
            }
        }
        if (Build$VERSION.SDK_INT >= 26) {
            this.mBuilder.setBadgeIconType(mBuilderCompat.mBadgeIcon).setShortcutId(mBuilderCompat.mShortcutId).setTimeoutAfter(mBuilderCompat.mTimeout).setGroupAlertBehavior(mBuilderCompat.mGroupAlertBehavior);
            if (mBuilderCompat.mColorizedSet) {
                this.mBuilder.setColorized(mBuilderCompat.mColorized);
            }
            if (!TextUtils.isEmpty((CharSequence)mBuilderCompat.mChannelId)) {
                this.mBuilder.setSound((Uri)null).setDefaults(0).setLights(0, 0, 0).setVibrate((long[])null);
            }
        }
    }
    
    private void addAction(final NotificationCompat.Action action) {
        if (Build$VERSION.SDK_INT >= 20) {
            final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
            if (action.getRemoteInputs() != null) {
                final android.app.RemoteInput[] fromCompat = RemoteInput.fromCompat(action.getRemoteInputs());
                for (int length = fromCompat.length, i = 0; i < length; ++i) {
                    notification$Action$Builder.addRemoteInput(fromCompat[i]);
                }
            }
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
            this.mBuilder.addAction(notification$Action$Builder.build());
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, action));
        }
    }
    
    private void removeSoundAndVibration(final Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults &= 0xFFFFFFFE;
        notification.defaults &= 0xFFFFFFFD;
    }
    
    public Notification build() {
        final NotificationCompat.Style mStyle = this.mBuilderCompat.mStyle;
        if (mStyle != null) {
            mStyle.apply(this);
        }
        RemoteViews contentView;
        if (mStyle != null) {
            contentView = mStyle.makeContentView(this);
        }
        else {
            contentView = null;
        }
        final Notification buildInternal = this.buildInternal();
        if (contentView != null) {
            buildInternal.contentView = contentView;
        }
        else if (this.mBuilderCompat.mContentView != null) {
            buildInternal.contentView = this.mBuilderCompat.mContentView;
        }
        if (Build$VERSION.SDK_INT >= 16 && mStyle != null) {
            final RemoteViews bigContentView = mStyle.makeBigContentView(this);
            if (bigContentView != null) {
                buildInternal.bigContentView = bigContentView;
            }
        }
        if (Build$VERSION.SDK_INT >= 21 && mStyle != null) {
            final RemoteViews headsUpContentView = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this);
            if (headsUpContentView != null) {
                buildInternal.headsUpContentView = headsUpContentView;
            }
        }
        if (Build$VERSION.SDK_INT >= 16 && mStyle != null) {
            final Bundle extras = NotificationCompat.getExtras(buildInternal);
            if (extras != null) {
                mStyle.addCompatExtras(extras);
            }
        }
        return buildInternal;
    }
    
    protected Notification buildInternal() {
        Notification notification;
        if (Build$VERSION.SDK_INT >= 26) {
            notification = this.mBuilder.build();
        }
        else if (Build$VERSION.SDK_INT >= 24) {
            final Notification notification2 = notification = this.mBuilder.build();
            if (this.mGroupAlertBehavior != 0) {
                if (notification2.getGroup() != null && (notification2.flags & 0x200) != 0x0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(notification2);
                }
                notification = notification2;
                if (notification2.getGroup() != null) {
                    notification = notification2;
                    if ((notification2.flags & 0x200) == 0x0) {
                        notification = notification2;
                        if (this.mGroupAlertBehavior == 1) {
                            this.removeSoundAndVibration(notification2);
                            return notification2;
                        }
                    }
                }
            }
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            this.mBuilder.setExtras(this.mExtras);
            final Notification build = this.mBuilder.build();
            if (this.mContentView != null) {
                build.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                build.bigContentView = this.mBigContentView;
            }
            if (this.mHeadsUpContentView != null) {
                build.headsUpContentView = this.mHeadsUpContentView;
            }
            notification = build;
            if (this.mGroupAlertBehavior != 0) {
                if (build.getGroup() != null && (build.flags & 0x200) != 0x0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(build);
                }
                notification = build;
                if (build.getGroup() != null) {
                    notification = build;
                    if ((build.flags & 0x200) == 0x0) {
                        notification = build;
                        if (this.mGroupAlertBehavior == 1) {
                            this.removeSoundAndVibration(build);
                            return build;
                        }
                    }
                }
            }
        }
        else if (Build$VERSION.SDK_INT >= 20) {
            this.mBuilder.setExtras(this.mExtras);
            final Notification build2 = this.mBuilder.build();
            if (this.mContentView != null) {
                build2.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                build2.bigContentView = this.mBigContentView;
            }
            notification = build2;
            if (this.mGroupAlertBehavior != 0) {
                if (build2.getGroup() != null && (build2.flags & 0x200) != 0x0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(build2);
                }
                notification = build2;
                if (build2.getGroup() != null) {
                    notification = build2;
                    if ((build2.flags & 0x200) == 0x0) {
                        notification = build2;
                        if (this.mGroupAlertBehavior == 1) {
                            this.removeSoundAndVibration(build2);
                            return build2;
                        }
                    }
                }
            }
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            final SparseArray<Bundle> buildActionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (buildActionExtrasMap != null) {
                this.mExtras.putSparseParcelableArray("android.support.actionExtras", (SparseArray)buildActionExtrasMap);
            }
            this.mBuilder.setExtras(this.mExtras);
            final Notification build3 = this.mBuilder.build();
            if (this.mContentView != null) {
                build3.contentView = this.mContentView;
            }
            notification = build3;
            if (this.mBigContentView != null) {
                build3.bigContentView = this.mBigContentView;
                return build3;
            }
        }
        else {
            if (Build$VERSION.SDK_INT < 16) {
                return this.mBuilder.getNotification();
            }
            final Notification build4 = this.mBuilder.build();
            final Bundle extras = NotificationCompat.getExtras(build4);
            final Bundle bundle = new Bundle(this.mExtras);
            for (final String s : this.mExtras.keySet()) {
                if (extras.containsKey(s)) {
                    bundle.remove(s);
                }
            }
            extras.putAll(bundle);
            final SparseArray<Bundle> buildActionExtrasMap2 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (buildActionExtrasMap2 != null) {
                NotificationCompat.getExtras(build4).putSparseParcelableArray("android.support.actionExtras", (SparseArray)buildActionExtrasMap2);
            }
            if (this.mContentView != null) {
                build4.contentView = this.mContentView;
            }
            notification = build4;
            if (this.mBigContentView != null) {
                build4.bigContentView = this.mBigContentView;
                return build4;
            }
        }
        return notification;
    }
    
    @Override
    public Notification$Builder getBuilder() {
        return this.mBuilder;
    }
}
