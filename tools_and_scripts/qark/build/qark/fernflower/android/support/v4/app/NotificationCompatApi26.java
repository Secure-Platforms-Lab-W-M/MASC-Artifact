package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

@RequiresApi(26)
class NotificationCompatApi26 {
   public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
      // $FF: renamed from: mB android.app.Notification.Builder
      private android.app.Notification.Builder field_17;

      Builder(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, boolean var15, int var16, CharSequence var17, boolean var18, String var19, ArrayList var20, Bundle var21, int var22, int var23, Notification var24, String var25, boolean var26, String var27, CharSequence[] var28, RemoteViews var29, RemoteViews var30, RemoteViews var31, String var32, int var33, String var34, long var35, boolean var37, boolean var38, int var39) {
         android.app.Notification.Builder var40 = (new android.app.Notification.Builder(var1, var32)).setWhen(var2.when).setShowWhen(var14).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
         if ((var2.flags & 2) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var40 = var40.setOngoing(var14);
         if ((var2.flags & 8) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var40 = var40.setOnlyAlertOnce(var14);
         if ((var2.flags & 16) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var40 = var40.setAutoCancel(var14).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var17).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
         if ((var2.flags & 128) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         this.field_17 = var40.setFullScreenIntent(var9, var14).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var15).setPriority(var16).setProgress(var11, var12, var13).setLocalOnly(var18).setExtras(var21).setGroup(var25).setGroupSummary(var26).setSortKey(var27).setCategory(var19).setColor(var22).setVisibility(var23).setPublicVersion(var24).setRemoteInputHistory(var28).setChannelId(var32).setBadgeIconType(var33).setShortcutId(var34).setTimeoutAfter(var35).setGroupAlertBehavior(var39);
         if (var38) {
            this.field_17.setColorized(var37);
         }

         if (var29 != null) {
            this.field_17.setCustomContentView(var29);
         }

         if (var30 != null) {
            this.field_17.setCustomBigContentView(var30);
         }

         if (var31 != null) {
            this.field_17.setCustomHeadsUpContentView(var31);
         }

         Iterator var42 = var20.iterator();

         while(var42.hasNext()) {
            String var41 = (String)var42.next();
            this.field_17.addPerson(var41);
         }

      }

      public void addAction(NotificationCompatBase.Action var1) {
         NotificationCompatApi24.addAction(this.field_17, var1);
      }

      public Notification build() {
         return this.field_17.build();
      }

      public android.app.Notification.Builder getBuilder() {
         return this.field_17;
      }
   }
}
