package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.Action;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(19)
class NotificationCompatKitKat {
   public static NotificationCompatBase.Action getAction(Notification var0, int var1, NotificationCompatBase.Action.Factory var2, RemoteInputCompatBase.RemoteInput.Factory var3) {
      Action var5 = var0.actions[var1];
      Object var4 = null;
      SparseArray var6 = var0.extras.getSparseParcelableArray("android.support.actionExtras");
      Bundle var7 = (Bundle)var4;
      if (var6 != null) {
         var7 = (Bundle)var6.get(var1);
      }

      return NotificationCompatJellybean.readAction(var2, var3, var5.icon, var5.title, var5.actionIntent, var7);
   }

   public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
      // $FF: renamed from: b android.app.Notification.Builder
      private android.app.Notification.Builder field_13;
      private List mActionExtrasList = new ArrayList();
      private RemoteViews mBigContentView;
      private RemoteViews mContentView;
      private Bundle mExtras;

      public Builder(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, boolean var15, int var16, CharSequence var17, boolean var18, ArrayList var19, Bundle var20, String var21, boolean var22, String var23, RemoteViews var24, RemoteViews var25) {
         android.app.Notification.Builder var26 = (new android.app.Notification.Builder(var1)).setWhen(var2.when).setShowWhen(var14).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
         if ((var2.flags & 2) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var26 = var26.setOngoing(var14);
         if ((var2.flags & 8) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var26 = var26.setOnlyAlertOnce(var14);
         if ((var2.flags & 16) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var26 = var26.setAutoCancel(var14).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var17).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
         if ((var2.flags & 128) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         this.field_13 = var26.setFullScreenIntent(var9, var14).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var15).setPriority(var16).setProgress(var11, var12, var13);
         this.mExtras = new Bundle();
         if (var20 != null) {
            this.mExtras.putAll(var20);
         }

         if (var19 != null && !var19.isEmpty()) {
            this.mExtras.putStringArray("android.people", (String[])var19.toArray(new String[var19.size()]));
         }

         if (var18) {
            this.mExtras.putBoolean("android.support.localOnly", true);
         }

         if (var21 != null) {
            this.mExtras.putString("android.support.groupKey", var21);
            if (var22) {
               this.mExtras.putBoolean("android.support.isGroupSummary", true);
            } else {
               this.mExtras.putBoolean("android.support.useSideChannel", true);
            }
         }

         if (var23 != null) {
            this.mExtras.putString("android.support.sortKey", var23);
         }

         this.mContentView = var24;
         this.mBigContentView = var25;
      }

      public void addAction(NotificationCompatBase.Action var1) {
         this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.field_13, var1));
      }

      public Notification build() {
         SparseArray var1 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
         if (var1 != null) {
            this.mExtras.putSparseParcelableArray("android.support.actionExtras", var1);
         }

         this.field_13.setExtras(this.mExtras);
         Notification var3 = this.field_13.build();
         RemoteViews var2 = this.mContentView;
         if (var2 != null) {
            var3.contentView = var2;
         }

         var2 = this.mBigContentView;
         if (var2 != null) {
            var3.bigContentView = var2;
         }

         return var3;
      }

      public android.app.Notification.Builder getBuilder() {
         return this.field_13;
      }
   }
}
