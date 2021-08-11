/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Bundle
 *  android.widget.RemoteViews
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationBuilderWithActions;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatApi24;
import android.support.v4.app.NotificationCompatBase;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

@RequiresApi(value=26)
class NotificationCompatApi26 {
    NotificationCompatApi26() {
    }

    public static class Builder
    implements NotificationBuilderWithBuilderAccessor,
    NotificationBuilderWithActions {
        private Notification.Builder mB;

        Builder(Context object, Notification object22, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int n, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap, int n2, int n3, boolean bl, boolean bl2, boolean bl3, int n4, CharSequence charSequence4, boolean bl4, String string2, ArrayList<String> arrayList, Bundle bundle, int n5, int n6, Notification notification, String string3, boolean bl5, String string4, CharSequence[] arrcharSequence, RemoteViews remoteViews2, RemoteViews remoteViews3, RemoteViews remoteViews4, String string5, int n7, String string6, long l, boolean bl6, boolean bl7, int n8) {
            void var17_19;
            void var29_31;
            void var20_22;
            void var31_33;
            void var34_36;
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
            void var33_35;
            void var5_7;
            void var38_39;
            boolean bl8;
            void var13_15;
            void var19_21;
            void var16_18;
            void var27_29;
            void var35_37;
            void var21_23;
            void var9_11;
            void var30_32;
            void var11_13;
            void var32_34;
            void var22_24;
            void var24_26;
            void var39_40;
            object = new Notification.Builder((Context)object, (String)var32_34).setWhen(object22.when).setShowWhen(bl8).setSmallIcon(object22.icon, object22.iconLevel).setContent(object22.contentView).setTicker(object22.tickerText, (RemoteViews)var6_8).setSound(object22.sound, object22.audioStreamType).setVibrate(object22.vibrate).setLights(object22.ledARGB, object22.ledOnMS, object22.ledOffMS);
            bl8 = (object22.flags & 2) != 0;
            object = object.setOngoing(bl8);
            bl8 = (object22.flags & 8) != 0;
            object = object.setOnlyAlertOnce(bl8);
            bl8 = (object22.flags & 16) != 0;
            object = object.setAutoCancel(bl8).setDefaults(object22.defaults).setContentTitle((CharSequence)var3_5).setContentText((CharSequence)var4_6).setSubText((CharSequence)var17_19).setContentInfo((CharSequence)var5_7).setContentIntent((PendingIntent)var8_10).setDeleteIntent(object22.deleteIntent);
            bl8 = (object22.flags & 128) != 0;
            this.mB = object.setFullScreenIntent((PendingIntent)var9_11, bl8).setLargeIcon((Bitmap)var10_12).setNumber((int)var7_9).setUsesChronometer((boolean)var15_17).setPriority((int)var16_18).setProgress((int)var11_13, (int)var12_14, (boolean)var13_15).setLocalOnly((boolean)var18_20).setExtras((Bundle)var21_23).setGroup((String)var25_27).setGroupSummary((boolean)var26_28).setSortKey((String)var27_29).setCategory((String)var19_21).setColor((int)var22_24).setVisibility((int)var23_25).setPublicVersion((Notification)var24_26).setRemoteInputHistory((CharSequence[])var28_30).setChannelId((String)var32_34).setBadgeIconType((int)var33_35).setShortcutId((String)var34_36).setTimeoutAfter((long)var35_37).setGroupAlertBehavior((int)var39_40);
            if (var38_39 != false) {
                void var37_38;
                this.mB.setColorized((boolean)var37_38);
            }
            if (var29_31 != null) {
                this.mB.setCustomContentView((RemoteViews)var29_31);
            }
            if (var30_32 != null) {
                this.mB.setCustomBigContentView((RemoteViews)var30_32);
            }
            if (var31_33 != null) {
                this.mB.setCustomHeadsUpContentView((RemoteViews)var31_33);
            }
            for (String string7 : var20_22) {
                this.mB.addPerson(string7);
            }
        }

        @Override
        public void addAction(NotificationCompatBase.Action action) {
            NotificationCompatApi24.addAction(this.mB, action);
        }

        @Override
        public Notification build() {
            return this.mB.build();
        }

        @Override
        public Notification.Builder getBuilder() {
            return this.mB;
        }
    }

}

