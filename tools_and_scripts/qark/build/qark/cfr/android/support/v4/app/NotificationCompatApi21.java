/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.app.RemoteInput
 *  android.app.RemoteInput$Builder
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.widget.RemoteViews
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithActions;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatApi20;
import android.support.v4.app.NotificationCompatBase;
import android.support.v4.app.RemoteInputCompatBase;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

@RequiresApi(value=21)
class NotificationCompatApi21 {
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_MESSAGES = "messages";
    private static final String KEY_ON_READ = "on_read";
    private static final String KEY_ON_REPLY = "on_reply";
    private static final String KEY_PARTICIPANTS = "participants";
    private static final String KEY_REMOTE_INPUT = "remote_input";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIMESTAMP = "timestamp";

    NotificationCompatApi21() {
    }

    private static RemoteInput fromCompatRemoteInput(RemoteInputCompatBase.RemoteInput remoteInput) {
        return new RemoteInput.Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build();
    }

    static Bundle getBundleForUnreadConversation(NotificationCompatBase.UnreadConversation unreadConversation) {
        if (unreadConversation == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        Object object = null;
        if (unreadConversation.getParticipants() != null && unreadConversation.getParticipants().length > 1) {
            object = unreadConversation.getParticipants()[0];
        }
        Parcelable[] arrparcelable = new Parcelable[unreadConversation.getMessages().length];
        for (int i = 0; i < arrparcelable.length; ++i) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("text", unreadConversation.getMessages()[i]);
            bundle2.putString("author", (String)object);
            arrparcelable[i] = bundle2;
        }
        bundle.putParcelableArray("messages", arrparcelable);
        object = unreadConversation.getRemoteInput();
        if (object != null) {
            bundle.putParcelable("remote_input", (Parcelable)NotificationCompatApi21.fromCompatRemoteInput((RemoteInputCompatBase.RemoteInput)object));
        }
        bundle.putParcelable("on_reply", (Parcelable)unreadConversation.getReplyPendingIntent());
        bundle.putParcelable("on_read", (Parcelable)unreadConversation.getReadPendingIntent());
        bundle.putStringArray("participants", unreadConversation.getParticipants());
        bundle.putLong("timestamp", unreadConversation.getLatestTimestamp());
        return bundle;
    }

    static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(Bundle bundle, NotificationCompatBase.UnreadConversation.Factory factory, RemoteInputCompatBase.RemoteInput.Factory object) {
        RemoteInputCompatBase.RemoteInput remoteInput = null;
        if (bundle == null) {
            return null;
        }
        PendingIntent pendingIntent = bundle.getParcelableArray("messages");
        String[] arrstring = null;
        if (pendingIntent != null) {
            boolean bl;
            arrstring = new String[pendingIntent.length];
            boolean bl2 = true;
            int n = 0;
            do {
                bl = bl2;
                if (n >= arrstring.length) break;
                if (!(pendingIntent[n] instanceof Bundle)) {
                    bl = false;
                    break;
                }
                arrstring[n] = ((Bundle)pendingIntent[n]).getString("text");
                if (arrstring[n] == null) {
                    bl = false;
                    break;
                }
                ++n;
            } while (true);
            if (!bl) {
                return null;
            }
        }
        pendingIntent = (PendingIntent)bundle.getParcelable("on_read");
        PendingIntent pendingIntent2 = (PendingIntent)bundle.getParcelable("on_reply");
        RemoteInput remoteInput2 = (RemoteInput)bundle.getParcelable("remote_input");
        String[] arrstring2 = bundle.getStringArray("participants");
        if (arrstring2 != null) {
            if (arrstring2.length != 1) {
                return null;
            }
            object = remoteInput2 != null ? NotificationCompatApi21.toCompatRemoteInput(remoteInput2, (RemoteInputCompatBase.RemoteInput.Factory)object) : remoteInput;
            return factory.build(arrstring, (RemoteInputCompatBase.RemoteInput)object, pendingIntent2, pendingIntent, arrstring2, bundle.getLong("timestamp"));
        }
        return null;
    }

    private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(RemoteInput remoteInput, RemoteInputCompatBase.RemoteInput.Factory factory) {
        return factory.build(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), remoteInput.getExtras(), null);
    }

    public static class Builder
    implements NotificationBuilderWithBuilderAccessor,
    NotificationBuilderWithActions {
        private Notification.Builder b;
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private Bundle mExtras;
        private int mGroupAlertBehavior;
        private RemoteViews mHeadsUpContentView;

        public Builder(Context object, Notification object22, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int n, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap, int n2, int n3, boolean bl, boolean bl2, boolean bl3, int n4, CharSequence charSequence4, boolean bl4, String string2, ArrayList<String> arrayList, Bundle bundle, int n5, int n6, Notification notification, String string3, boolean bl5, String string4, RemoteViews remoteViews2, RemoteViews remoteViews3, RemoteViews remoteViews4, int n7) {
            void var17_19;
            void var29_31;
            void var20_22;
            void var31_33;
            void var8_10;
            void var7_9;
            void var6_8;
            void var26_28;
            void var18_20;
            void var12_14;
            void var3_5;
            void var15_17;
            void var10_12;
            void var4_6;
            void var28_30;
            void var25_27;
            void var23_25;
            void var5_7;
            boolean bl6;
            void var13_15;
            void var19_21;
            void var16_18;
            void var27_29;
            void var21_23;
            void var9_11;
            void var30_32;
            void var11_13;
            void var22_24;
            void var24_26;
            object = new Notification.Builder((Context)object).setWhen(object22.when).setShowWhen(bl6).setSmallIcon(object22.icon, object22.iconLevel).setContent(object22.contentView).setTicker(object22.tickerText, (RemoteViews)var6_8).setSound(object22.sound, object22.audioStreamType).setVibrate(object22.vibrate).setLights(object22.ledARGB, object22.ledOnMS, object22.ledOffMS);
            int n8 = object22.flags;
            boolean bl7 = true;
            bl6 = (n8 & 2) != 0;
            object = object.setOngoing(bl6);
            bl6 = (object22.flags & 8) != 0;
            object = object.setOnlyAlertOnce(bl6);
            bl6 = (object22.flags & 16) != 0;
            object = object.setAutoCancel(bl6).setDefaults(object22.defaults).setContentTitle((CharSequence)var3_5).setContentText((CharSequence)var4_6).setSubText((CharSequence)var17_19).setContentInfo((CharSequence)var5_7).setContentIntent((PendingIntent)var8_10).setDeleteIntent(object22.deleteIntent);
            bl6 = (object22.flags & 128) != 0 ? bl7 : false;
            this.b = object.setFullScreenIntent((PendingIntent)var9_11, bl6).setLargeIcon((Bitmap)var10_12).setNumber((int)var7_9).setUsesChronometer((boolean)var15_17).setPriority((int)var16_18).setProgress((int)var11_13, (int)var12_14, (boolean)var13_15).setLocalOnly((boolean)var18_20).setGroup((String)var25_27).setGroupSummary((boolean)var26_28).setSortKey((String)var27_29).setCategory((String)var19_21).setColor((int)var22_24).setVisibility((int)var23_25).setPublicVersion((Notification)var24_26);
            this.mExtras = new Bundle();
            if (var21_23 != null) {
                this.mExtras.putAll((Bundle)var21_23);
            }
            for (String string5 : var20_22) {
                this.b.addPerson(string5);
            }
            this.mContentView = var28_30;
            this.mBigContentView = var29_31;
            this.mHeadsUpContentView = var30_32;
            this.mGroupAlertBehavior = var31_33;
        }

        private void removeSoundAndVibration(Notification notification) {
            notification.sound = null;
            notification.vibrate = null;
            notification.defaults &= -2;
            notification.defaults &= -3;
        }

        @Override
        public void addAction(NotificationCompatBase.Action action) {
            NotificationCompatApi20.addAction(this.b, action);
        }

        @Override
        public Notification build() {
            this.b.setExtras(this.mExtras);
            Notification notification = this.b.build();
            RemoteViews remoteViews = this.mContentView;
            if (remoteViews != null) {
                notification.contentView = remoteViews;
            }
            if ((remoteViews = this.mBigContentView) != null) {
                notification.bigContentView = remoteViews;
            }
            if ((remoteViews = this.mHeadsUpContentView) != null) {
                notification.headsUpContentView = remoteViews;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (notification.getGroup() != null && (notification.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                    this.removeSoundAndVibration(notification);
                }
                if (notification.getGroup() != null && (notification.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    this.removeSoundAndVibration(notification);
                    return notification;
                }
                return notification;
            }
            return notification;
        }

        @Override
        public Notification.Builder getBuilder() {
            return this.b;
        }
    }

}

