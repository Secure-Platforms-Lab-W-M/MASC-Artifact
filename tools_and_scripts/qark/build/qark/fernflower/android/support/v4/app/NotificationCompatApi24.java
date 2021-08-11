package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.Action;
import android.app.Notification.MessagingStyle;
import android.app.Notification.MessagingStyle.Message;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiresApi(24)
class NotificationCompatApi24 {
   public static void addAction(android.app.Notification.Builder var0, NotificationCompatBase.Action var1) {
      android.app.Notification.Action.Builder var5 = new android.app.Notification.Action.Builder(var1.getIcon(), var1.getTitle(), var1.getActionIntent());
      if (var1.getRemoteInputs() != null) {
         android.app.RemoteInput[] var4 = RemoteInputCompatApi20.fromCompat(var1.getRemoteInputs());
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var5.addRemoteInput(var4[var2]);
         }
      }

      Bundle var6;
      if (var1.getExtras() != null) {
         var6 = new Bundle(var1.getExtras());
      } else {
         var6 = new Bundle();
      }

      var6.putBoolean("android.support.allowGeneratedReplies", var1.getAllowGeneratedReplies());
      var5.setAllowGeneratedReplies(var1.getAllowGeneratedReplies());
      var5.addExtras(var6);
      var0.addAction(var5.build());
   }

   public static void addMessagingStyle(NotificationBuilderWithBuilderAccessor var0, CharSequence var1, CharSequence var2, List var3, List var4, List var5, List var6, List var7) {
      MessagingStyle var9 = (new MessagingStyle(var1)).setConversationTitle(var2);

      for(int var8 = 0; var8 < var3.size(); ++var8) {
         Message var10 = new Message((CharSequence)var3.get(var8), (Long)var4.get(var8), (CharSequence)var5.get(var8));
         if (var6.get(var8) != null) {
            var10.setData((String)var6.get(var8), (Uri)var7.get(var8));
         }

         var9.addMessage(var10);
      }

      var9.setBuilder(var0.getBuilder());
   }

   public static NotificationCompatBase.Action getAction(Notification var0, int var1, NotificationCompatBase.Action.Factory var2, RemoteInputCompatBase.RemoteInput.Factory var3) {
      return getActionCompatFromAction(var0.actions[var1], var2, var3);
   }

   private static NotificationCompatBase.Action getActionCompatFromAction(Action var0, NotificationCompatBase.Action.Factory var1, RemoteInputCompatBase.RemoteInput.Factory var2) {
      RemoteInputCompatBase.RemoteInput[] var4 = RemoteInputCompatApi20.toCompat(var0.getRemoteInputs(), var2);
      boolean var3;
      if (!var0.getExtras().getBoolean("android.support.allowGeneratedReplies") && !var0.getAllowGeneratedReplies()) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var1.build(var0.icon, var0.title, var0.actionIntent, var0.getExtras(), var4, (RemoteInputCompatBase.RemoteInput[])null, var3);
   }

   private static Action getActionFromActionCompat(NotificationCompatBase.Action var0) {
      android.app.Notification.Action.Builder var4 = new android.app.Notification.Action.Builder(var0.getIcon(), var0.getTitle(), var0.getActionIntent());
      Bundle var3;
      if (var0.getExtras() != null) {
         var3 = new Bundle(var0.getExtras());
      } else {
         var3 = new Bundle();
      }

      var3.putBoolean("android.support.allowGeneratedReplies", var0.getAllowGeneratedReplies());
      var4.setAllowGeneratedReplies(var0.getAllowGeneratedReplies());
      var4.addExtras(var3);
      RemoteInputCompatBase.RemoteInput[] var5 = var0.getRemoteInputs();
      if (var5 != null) {
         android.app.RemoteInput[] var6 = RemoteInputCompatApi20.fromCompat(var5);
         int var2 = var6.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var4.addRemoteInput(var6[var1]);
         }
      }

      return var4.build();
   }

   public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(ArrayList var0, NotificationCompatBase.Action.Factory var1, RemoteInputCompatBase.RemoteInput.Factory var2) {
      if (var0 == null) {
         return null;
      } else {
         NotificationCompatBase.Action[] var4 = var1.newArray(var0.size());

         for(int var3 = 0; var3 < var4.length; ++var3) {
            var4[var3] = getActionCompatFromAction((Action)var0.get(var3), var1, var2);
         }

         return var4;
      }
   }

   public static ArrayList getParcelableArrayListForActions(NotificationCompatBase.Action[] var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var3 = new ArrayList(var0.length);
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var3.add(getActionFromActionCompat(var0[var1]));
         }

         return var3;
      }
   }

   public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
      // $FF: renamed from: b android.app.Notification.Builder
      private android.app.Notification.Builder field_6;
      private int mGroupAlertBehavior;

      public Builder(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, boolean var15, int var16, CharSequence var17, boolean var18, String var19, ArrayList var20, Bundle var21, int var22, int var23, Notification var24, String var25, boolean var26, String var27, CharSequence[] var28, RemoteViews var29, RemoteViews var30, RemoteViews var31, int var32) {
         android.app.Notification.Builder var35 = (new android.app.Notification.Builder(var1)).setWhen(var2.when).setShowWhen(var14).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
         int var33 = var2.flags;
         boolean var34 = false;
         if ((var33 & 2) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var35 = var35.setOngoing(var14);
         if ((var2.flags & 8) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var35 = var35.setOnlyAlertOnce(var14);
         if ((var2.flags & 16) != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var35 = var35.setAutoCancel(var14).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var17).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
         var14 = var34;
         if ((var2.flags & 128) != 0) {
            var14 = true;
         }

         this.field_6 = var35.setFullScreenIntent(var9, var14).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var15).setPriority(var16).setProgress(var11, var12, var13).setLocalOnly(var18).setExtras(var21).setGroup(var25).setGroupSummary(var26).setSortKey(var27).setCategory(var19).setColor(var22).setVisibility(var23).setPublicVersion(var24).setRemoteInputHistory(var28);
         if (var29 != null) {
            this.field_6.setCustomContentView(var29);
         }

         if (var30 != null) {
            this.field_6.setCustomBigContentView(var30);
         }

         if (var31 != null) {
            this.field_6.setCustomHeadsUpContentView(var31);
         }

         Iterator var37 = var20.iterator();

         while(var37.hasNext()) {
            String var36 = (String)var37.next();
            this.field_6.addPerson(var36);
         }

         this.mGroupAlertBehavior = var32;
      }

      private void removeSoundAndVibration(Notification var1) {
         var1.sound = null;
         var1.vibrate = null;
         var1.defaults &= -2;
         var1.defaults &= -3;
      }

      public void addAction(NotificationCompatBase.Action var1) {
         NotificationCompatApi24.addAction(this.field_6, var1);
      }

      public Notification build() {
         Notification var1 = this.field_6.build();
         if (this.mGroupAlertBehavior != 0) {
            if (var1.getGroup() != null && (var1.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
               this.removeSoundAndVibration(var1);
            }

            if (var1.getGroup() != null && (var1.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
               this.removeSoundAndVibration(var1);
            }
         }

         return var1;
      }

      public android.app.Notification.Builder getBuilder() {
         return this.field_6;
      }
   }
}
