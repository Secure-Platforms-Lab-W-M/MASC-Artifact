// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.app.NotificationManager;
import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;

abstract class RemoteViewsAction extends Action<RemoteViewsTarget>
{
    final RemoteViews remoteViews;
    private RemoteViewsTarget target;
    final int viewId;
    
    RemoteViewsAction(final Picasso picasso, final Request request, final RemoteViews remoteViews, final int viewId, final int n, final int n2, final int n3, final Object o, final String s) {
        super(picasso, null, request, n2, n3, n, null, s, o, false);
        this.remoteViews = remoteViews;
        this.viewId = viewId;
    }
    
    @Override
    void complete(final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom) {
        this.remoteViews.setImageViewBitmap(this.viewId, bitmap);
        this.update();
    }
    
    public void error() {
        if (this.errorResId != 0) {
            this.setImageResource(this.errorResId);
        }
    }
    
    @Override
    RemoteViewsTarget getTarget() {
        if (this.target == null) {
            this.target = new RemoteViewsTarget(this.remoteViews, this.viewId);
        }
        return this.target;
    }
    
    void setImageResource(final int n) {
        this.remoteViews.setImageViewResource(this.viewId, n);
        this.update();
    }
    
    abstract void update();
    
    static class AppWidgetAction extends RemoteViewsAction
    {
        private final int[] appWidgetIds;
        
        AppWidgetAction(final Picasso picasso, final Request request, final RemoteViews remoteViews, final int n, final int[] appWidgetIds, final int n2, final int n3, final String s, final Object o, final int n4) {
            super(picasso, request, remoteViews, n, n4, n2, n3, o, s);
            this.appWidgetIds = appWidgetIds;
        }
        
        @Override
        void update() {
            AppWidgetManager.getInstance(this.picasso.context).updateAppWidget(this.appWidgetIds, this.remoteViews);
        }
    }
    
    static class NotificationAction extends RemoteViewsAction
    {
        private final Notification notification;
        private final int notificationId;
        
        NotificationAction(final Picasso picasso, final Request request, final RemoteViews remoteViews, final int n, final int notificationId, final Notification notification, final int n2, final int n3, final String s, final Object o, final int n4) {
            super(picasso, request, remoteViews, n, n4, n2, n3, o, s);
            this.notificationId = notificationId;
            this.notification = notification;
        }
        
        @Override
        void update() {
            Utils.getService(this.picasso.context, "notification").notify(this.notificationId, this.notification);
        }
    }
    
    static class RemoteViewsTarget
    {
        final RemoteViews remoteViews;
        final int viewId;
        
        RemoteViewsTarget(final RemoteViews remoteViews, final int viewId) {
            this.remoteViews = remoteViews;
            this.viewId = viewId;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final RemoteViewsTarget remoteViewsTarget = (RemoteViewsTarget)o;
                if (this.viewId != remoteViewsTarget.viewId || !this.remoteViews.equals(remoteViewsTarget.remoteViews)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return this.remoteViews.hashCode() * 31 + this.viewId;
        }
    }
}
