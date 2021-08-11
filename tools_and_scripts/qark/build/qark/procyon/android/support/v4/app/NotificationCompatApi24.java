// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import java.util.Iterator;
import android.graphics.Bitmap;
import android.app.PendingIntent;
import android.widget.RemoteViews;
import android.content.Context;
import android.os.Parcelable;
import java.util.ArrayList;
import android.app.Notification$Action;
import android.app.Notification;
import android.app.Notification$MessagingStyle$Message;
import android.app.Notification$MessagingStyle;
import android.net.Uri;
import java.util.List;
import android.app.RemoteInput;
import android.os.Bundle;
import android.app.Notification$Action$Builder;
import android.app.Notification$Builder;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class NotificationCompatApi24
{
    public static void addAction(final Notification$Builder notification$Builder, final NotificationCompatBase.Action action) {
        final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
        if (action.getRemoteInputs() != null) {
            final RemoteInput[] fromCompat = RemoteInputCompatApi20.fromCompat(action.getRemoteInputs());
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
        notification$Action$Builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
        notification$Action$Builder.addExtras(bundle);
        notification$Builder.addAction(notification$Action$Builder.build());
    }
    
    public static void addMessagingStyle(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final CharSequence charSequence, final CharSequence conversationTitle, final List<CharSequence> list, final List<Long> list2, final List<CharSequence> list3, final List<String> list4, final List<Uri> list5) {
        final Notification$MessagingStyle setConversationTitle = new Notification$MessagingStyle(charSequence).setConversationTitle(conversationTitle);
        for (int i = 0; i < list.size(); ++i) {
            final Notification$MessagingStyle$Message notification$MessagingStyle$Message = new Notification$MessagingStyle$Message((CharSequence)list.get(i), (long)list2.get(i), (CharSequence)list3.get(i));
            if (list4.get(i) != null) {
                notification$MessagingStyle$Message.setData((String)list4.get(i), (Uri)list5.get(i));
            }
            setConversationTitle.addMessage(notification$MessagingStyle$Message);
        }
        setConversationTitle.setBuilder(notificationBuilderWithBuilderAccessor.getBuilder());
    }
    
    public static NotificationCompatBase.Action getAction(final Notification notification, final int n, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        return getActionCompatFromAction(notification.actions[n], factory, factory2);
    }
    
    private static NotificationCompatBase.Action getActionCompatFromAction(final Notification$Action notification$Action, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        return factory.build(notification$Action.icon, notification$Action.title, notification$Action.actionIntent, notification$Action.getExtras(), RemoteInputCompatApi20.toCompat(notification$Action.getRemoteInputs(), factory2), null, notification$Action.getExtras().getBoolean("android.support.allowGeneratedReplies") || notification$Action.getAllowGeneratedReplies());
    }
    
    private static Notification$Action getActionFromActionCompat(final NotificationCompatBase.Action action) {
        final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
        Bundle bundle;
        if (action.getExtras() != null) {
            bundle = new Bundle(action.getExtras());
        }
        else {
            bundle = new Bundle();
        }
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        notification$Action$Builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
        notification$Action$Builder.addExtras(bundle);
        final RemoteInputCompatBase.RemoteInput[] remoteInputs = action.getRemoteInputs();
        if (remoteInputs != null) {
            final RemoteInput[] fromCompat = RemoteInputCompatApi20.fromCompat(remoteInputs);
            for (int length = fromCompat.length, i = 0; i < length; ++i) {
                notification$Action$Builder.addRemoteInput(fromCompat[i]);
            }
        }
        return notification$Action$Builder.build();
    }
    
    public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        if (list == null) {
            return null;
        }
        final NotificationCompatBase.Action[] array = factory.newArray(list.size());
        for (int i = 0; i < array.length; ++i) {
            array[i] = getActionCompatFromAction((Notification$Action)list.get(i), factory, factory2);
        }
        return array;
    }
    
    public static ArrayList<Parcelable> getParcelableArrayListForActions(final NotificationCompatBase.Action[] array) {
        if (array == null) {
            return null;
        }
        final ArrayList<Parcelable> list = new ArrayList<Parcelable>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add((Parcelable)getActionFromActionCompat(array[i]));
        }
        return list;
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        private int mGroupAlertBehavior;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean localOnly, final String category, final ArrayList<String> list, final Bundle extras, final int color, final int visibility, final Notification publicVersion, final String group, final boolean groupSummary, final String sortKey, final CharSequence[] remoteInputHistory, final RemoteViews customContentView, final RemoteViews customBigContentView, final RemoteViews customHeadsUpContentView, final int mGroupAlertBehavior) {
            final Notification$Builder setLights = new Notification$Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            final int flags = notification.flags;
            final boolean b2 = false;
            final Notification$Builder setDeleteIntent = setLights.setOngoing((flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent);
            boolean b3 = b2;
            if ((notification.flags & 0x80) != 0x0) {
                b3 = true;
            }
            this.b = setDeleteIntent.setFullScreenIntent(pendingIntent, b3).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b).setLocalOnly(localOnly).setExtras(extras).setGroup(group).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion).setRemoteInputHistory(remoteInputHistory);
            if (customContentView != null) {
                this.b.setCustomContentView(customContentView);
            }
            if (customBigContentView != null) {
                this.b.setCustomBigContentView(customBigContentView);
            }
            if (customHeadsUpContentView != null) {
                this.b.setCustomHeadsUpContentView(customHeadsUpContentView);
            }
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.b.addPerson((String)iterator.next());
            }
            this.mGroupAlertBehavior = mGroupAlertBehavior;
        }
        
        private void removeSoundAndVibration(final Notification notification) {
            notification.sound = null;
            notification.vibrate = null;
            notification.defaults &= 0xFFFFFFFE;
            notification.defaults &= 0xFFFFFFFD;
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            NotificationCompatApi24.addAction(this.b, action);
        }
        
        @Override
        public Notification build() {
            final Notification build = this.b.build();
            if (this.mGroupAlertBehavior != 0) {
                if (build.getGroup() != null && (build.flags & 0x200) != 0x0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(build);
                }
                if (build.getGroup() != null && (build.flags & 0x200) == 0x0 && this.mGroupAlertBehavior == 1) {
                    this.removeSoundAndVibration(build);
                }
            }
            return build;
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
