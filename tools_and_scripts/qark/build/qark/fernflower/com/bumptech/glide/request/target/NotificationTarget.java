package com.bumptech.glide.request.target;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Preconditions;

public class NotificationTarget extends CustomTarget {
   private final Context context;
   private final Notification notification;
   private final int notificationId;
   private final String notificationTag;
   private final RemoteViews remoteViews;
   private final int viewId;

   public NotificationTarget(Context var1, int var2, int var3, int var4, RemoteViews var5, Notification var6, int var7, String var8) {
      super(var2, var3);
      this.context = (Context)Preconditions.checkNotNull(var1, "Context must not be null!");
      this.notification = (Notification)Preconditions.checkNotNull(var6, "Notification object can not be null!");
      this.remoteViews = (RemoteViews)Preconditions.checkNotNull(var5, "RemoteViews object can not be null!");
      this.viewId = var4;
      this.notificationId = var7;
      this.notificationTag = var8;
   }

   public NotificationTarget(Context var1, int var2, RemoteViews var3, Notification var4, int var5) {
      this(var1, var2, var3, var4, var5, (String)null);
   }

   public NotificationTarget(Context var1, int var2, RemoteViews var3, Notification var4, int var5, String var6) {
      this(var1, Integer.MIN_VALUE, Integer.MIN_VALUE, var2, var3, var4, var5, var6);
   }

   private void setBitmap(Bitmap var1) {
      this.remoteViews.setImageViewBitmap(this.viewId, var1);
      this.update();
   }

   private void update() {
      ((NotificationManager)Preconditions.checkNotNull((NotificationManager)this.context.getSystemService("notification"))).notify(this.notificationTag, this.notificationId, this.notification);
   }

   public void onLoadCleared(Drawable var1) {
      this.setBitmap((Bitmap)null);
   }

   public void onResourceReady(Bitmap var1, Transition var2) {
      this.setBitmap(var1);
   }
}
