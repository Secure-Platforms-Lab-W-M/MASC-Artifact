package com.bumptech.glide.request.target;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Preconditions;

public class AppWidgetTarget extends CustomTarget {
   private final ComponentName componentName;
   private final Context context;
   private final RemoteViews remoteViews;
   private final int viewId;
   private final int[] widgetIds;

   public AppWidgetTarget(Context var1, int var2, int var3, int var4, RemoteViews var5, ComponentName var6) {
      super(var2, var3);
      this.context = (Context)Preconditions.checkNotNull(var1, "Context can not be null!");
      this.remoteViews = (RemoteViews)Preconditions.checkNotNull(var5, "RemoteViews object can not be null!");
      this.componentName = (ComponentName)Preconditions.checkNotNull(var6, "ComponentName can not be null!");
      this.viewId = var4;
      this.widgetIds = null;
   }

   public AppWidgetTarget(Context var1, int var2, int var3, int var4, RemoteViews var5, int... var6) {
      super(var2, var3);
      if (var6.length != 0) {
         this.context = (Context)Preconditions.checkNotNull(var1, "Context can not be null!");
         this.remoteViews = (RemoteViews)Preconditions.checkNotNull(var5, "RemoteViews object can not be null!");
         this.widgetIds = (int[])Preconditions.checkNotNull(var6, "WidgetIds can not be null!");
         this.viewId = var4;
         this.componentName = null;
      } else {
         throw new IllegalArgumentException("WidgetIds must have length > 0");
      }
   }

   public AppWidgetTarget(Context var1, int var2, RemoteViews var3, ComponentName var4) {
      this(var1, Integer.MIN_VALUE, Integer.MIN_VALUE, var2, var3, var4);
   }

   public AppWidgetTarget(Context var1, int var2, RemoteViews var3, int... var4) {
      this(var1, Integer.MIN_VALUE, Integer.MIN_VALUE, var2, var3, var4);
   }

   private void setBitmap(Bitmap var1) {
      this.remoteViews.setImageViewBitmap(this.viewId, var1);
      this.update();
   }

   private void update() {
      AppWidgetManager var1 = AppWidgetManager.getInstance(this.context);
      ComponentName var2 = this.componentName;
      if (var2 != null) {
         var1.updateAppWidget(var2, this.remoteViews);
      } else {
         var1.updateAppWidget(this.widgetIds, this.remoteViews);
      }
   }

   public void onLoadCleared(Drawable var1) {
      this.setBitmap((Bitmap)null);
   }

   public void onResourceReady(Bitmap var1, Transition var2) {
      this.setBitmap(var1);
   }
}
