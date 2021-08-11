/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Action
 *  android.app.Notification$Action$Builder
 *  android.app.Notification$Builder
 *  android.app.Notification$MessagingStyle
 *  android.app.Notification$MessagingStyle$Message
 *  android.app.PendingIntent
 *  android.app.RemoteInput
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
import android.support.v4.app.NotificationCompatBase;
import android.support.v4.app.RemoteInputCompatApi20;
import android.support.v4.app.RemoteInputCompatBase;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiresApi(value=24)
class NotificationCompatApi24 {
    NotificationCompatApi24() {
    }

    public static void addAction(Notification.Builder builder, NotificationCompatBase.Action action) {
        Bundle bundle;
        Notification.Action.Builder builder2 = new Notification.Action.Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
        if (action.getRemoteInputs() != null) {
            bundle = RemoteInputCompatApi20.fromCompat(action.getRemoteInputs());
            int n = bundle.length;
            for (int i = 0; i < n; ++i) {
                builder2.addRemoteInput((RemoteInput)bundle[i]);
            }
        }
        bundle = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        builder2.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
        builder2.addExtras(bundle);
        builder.addAction(builder2.build());
    }

    public static void addMessagingStyle(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, CharSequence charSequence, CharSequence charSequence2, List<CharSequence> list, List<Long> list2, List<CharSequence> list3, List<String> list4, List<Uri> list5) {
        charSequence = new Notification.MessagingStyle(charSequence).setConversationTitle(charSequence2);
        for (int i = 0; i < list.size(); ++i) {
            charSequence2 = new Notification.MessagingStyle.Message(list.get(i), list2.get(i).longValue(), list3.get(i));
            if (list4.get(i) != null) {
                charSequence2.setData(list4.get(i), list5.get(i));
            }
            charSequence.addMessage((Notification.MessagingStyle.Message)charSequence2);
        }
        charSequence.setBuilder(notificationBuilderWithBuilderAccessor.getBuilder());
    }

    public static NotificationCompatBase.Action getAction(Notification notification, int n, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory2) {
        return NotificationCompatApi24.getActionCompatFromAction(notification.actions[n], factory, factory2);
    }

    private static NotificationCompatBase.Action getActionCompatFromAction(Notification.Action action, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory arrremoteInput) {
        arrremoteInput = RemoteInputCompatApi20.toCompat(action.getRemoteInputs(), (RemoteInputCompatBase.RemoteInput.Factory)arrremoteInput);
        boolean bl = action.getExtras().getBoolean("android.support.allowGeneratedReplies") || action.getAllowGeneratedReplies();
        return factory.build(action.icon, action.title, action.actionIntent, action.getExtras(), arrremoteInput, null, bl);
    }

    private static Notification.Action getActionFromActionCompat(NotificationCompatBase.Action arrremoteInput) {
        Notification.Action.Builder builder = new Notification.Action.Builder(arrremoteInput.getIcon(), arrremoteInput.getTitle(), arrremoteInput.getActionIntent());
        Bundle bundle = arrremoteInput.getExtras() != null ? new Bundle(arrremoteInput.getExtras()) : new Bundle();
        bundle.putBoolean("android.support.allowGeneratedReplies", arrremoteInput.getAllowGeneratedReplies());
        builder.setAllowGeneratedReplies(arrremoteInput.getAllowGeneratedReplies());
        builder.addExtras(bundle);
        arrremoteInput = arrremoteInput.getRemoteInputs();
        if (arrremoteInput != null) {
            arrremoteInput = RemoteInputCompatApi20.fromCompat(arrremoteInput);
            int n = arrremoteInput.length;
            for (int i = 0; i < n; ++i) {
                builder.addRemoteInput((RemoteInput)arrremoteInput[i]);
            }
        }
        return builder.build();
    }

    public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> arrayList, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory2) {
        if (arrayList == null) {
            return null;
        }
        NotificationCompatBase.Action[] arraction = factory.newArray(arrayList.size());
        for (int i = 0; i < arraction.length; ++i) {
            arraction[i] = NotificationCompatApi24.getActionCompatFromAction((Notification.Action)arrayList.get(i), factory, factory2);
        }
        return arraction;
    }

    public static ArrayList<Parcelable> getParcelableArrayListForActions(NotificationCompatBase.Action[] arraction) {
        if (arraction == null) {
            return null;
        }
        ArrayList<Parcelable> arrayList = new ArrayList<Parcelable>(arraction.length);
        int n = arraction.length;
        for (int i = 0; i < n; ++i) {
            arrayList.add((Parcelable)NotificationCompatApi24.getActionFromActionCompat(arraction[i]));
        }
        return arrayList;
    }

    public static class Builder
    implements NotificationBuilderWithBuilderAccessor,
    NotificationBuilderWithActions {
        private Notification.Builder b;
        private int mGroupAlertBehavior;

        public Builder(Context object, Notification object22, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int n, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap, int n2, int n3, boolean bl, boolean bl2, boolean bl3, int n4, CharSequence charSequence4, boolean bl4, String string2, ArrayList<String> arrayList, Bundle bundle, int n5, int n6, Notification notification, String string3, boolean bl5, String string4, CharSequence[] arrcharSequence, RemoteViews remoteViews2, RemoteViews remoteViews3, RemoteViews remoteViews4, int n7) {
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
            void var32_34;
            void var22_24;
            void var24_26;
            object = new Notification.Builder((Context)object).setWhen(object22.when).setShowWhen(bl6).setSmallIcon(object22.icon, object22.iconLevel).setContent(object22.contentView).setTicker(object22.tickerText, (RemoteViews)var6_8).setSound(object22.sound, object22.audioStreamType).setVibrate(object22.vibrate).setLights(object22.ledARGB, object22.ledOnMS, object22.ledOffMS);
            int n8 = object22.flags;
            boolean bl7 = false;
            bl6 = (n8 & 2) != 0;
            object = object.setOngoing(bl6);
            bl6 = (object22.flags & 8) != 0;
            object = object.setOnlyAlertOnce(bl6);
            bl6 = (object22.flags & 16) != 0;
            object = object.setAutoCancel(bl6).setDefaults(object22.defaults).setContentTitle((CharSequence)var3_5).setContentText((CharSequence)var4_6).setSubText((CharSequence)var17_19).setContentInfo((CharSequence)var5_7).setContentIntent((PendingIntent)var8_10).setDeleteIntent(object22.deleteIntent);
            bl6 = bl7;
            if ((object22.flags & 128) != 0) {
                bl6 = true;
            }
            this.b = object.setFullScreenIntent((PendingIntent)var9_11, bl6).setLargeIcon((Bitmap)var10_12).setNumber((int)var7_9).setUsesChronometer((boolean)var15_17).setPriority((int)var16_18).setProgress((int)var11_13, (int)var12_14, (boolean)var13_15).setLocalOnly((boolean)var18_20).setExtras((Bundle)var21_23).setGroup((String)var25_27).setGroupSummary((boolean)var26_28).setSortKey((String)var27_29).setCategory((String)var19_21).setColor((int)var22_24).setVisibility((int)var23_25).setPublicVersion((Notification)var24_26).setRemoteInputHistory((CharSequence[])var28_30);
            if (var29_31 != null) {
                this.b.setCustomContentView((RemoteViews)var29_31);
            }
            if (var30_32 != null) {
                this.b.setCustomBigContentView((RemoteViews)var30_32);
            }
            if (var31_33 != null) {
                this.b.setCustomHeadsUpContentView((RemoteViews)var31_33);
            }
            for (String string5 : var20_22) {
                this.b.addPerson(string5);
            }
            this.mGroupAlertBehavior = var32_34;
        }

        private void removeSoundAndVibration(Notification notification) {
            notification.sound = null;
            notification.vibrate = null;
            notification.defaults &= -2;
            notification.defaults &= -3;
        }

        @Override
        public void addAction(NotificationCompatBase.Action action) {
            NotificationCompatApi24.addAction(this.b, action);
        }

        @Override
        public Notification build() {
            Notification notification = this.b.build();
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

