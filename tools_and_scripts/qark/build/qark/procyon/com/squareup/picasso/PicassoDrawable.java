// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.os.Build$VERSION;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

final class PicassoDrawable extends BitmapDrawable
{
    private static final Paint DEBUG_PAINT;
    private static final float FADE_DURATION = 200.0f;
    int alpha;
    boolean animating;
    private final boolean debugging;
    private final float density;
    private final Picasso.LoadedFrom loadedFrom;
    Drawable placeholder;
    long startTimeMillis;
    
    static {
        DEBUG_PAINT = new Paint();
    }
    
    PicassoDrawable(final Context context, final Bitmap bitmap, final Drawable placeholder, final Picasso.LoadedFrom loadedFrom, final boolean b, final boolean debugging) {
        super(context.getResources(), bitmap);
        this.alpha = 255;
        this.debugging = debugging;
        this.density = context.getResources().getDisplayMetrics().density;
        this.loadedFrom = loadedFrom;
        int n;
        if (loadedFrom != Picasso.LoadedFrom.MEMORY && !b) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            this.placeholder = placeholder;
            this.animating = true;
            this.startTimeMillis = SystemClock.uptimeMillis();
        }
    }
    
    private void drawDebugIndicator(final Canvas canvas) {
        PicassoDrawable.DEBUG_PAINT.setColor(-1);
        canvas.drawPath(getTrianglePath(new Point(0, 0), (int)(16.0f * this.density)), PicassoDrawable.DEBUG_PAINT);
        PicassoDrawable.DEBUG_PAINT.setColor(this.loadedFrom.debugColor);
        canvas.drawPath(getTrianglePath(new Point(0, 0), (int)(15.0f * this.density)), PicassoDrawable.DEBUG_PAINT);
    }
    
    private static Path getTrianglePath(final Point point, final int n) {
        final Point point2 = new Point(point.x + n, point.y);
        final Point point3 = new Point(point.x, point.y + n);
        final Path path = new Path();
        path.moveTo((float)point.x, (float)point.y);
        path.lineTo((float)point2.x, (float)point2.y);
        path.lineTo((float)point3.x, (float)point3.y);
        return path;
    }
    
    static void setBitmap(final ImageView imageView, final Context context, final Bitmap bitmap, final Picasso.LoadedFrom loadedFrom, final boolean b, final boolean b2) {
        final Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable)drawable).stop();
        }
        imageView.setImageDrawable((Drawable)new PicassoDrawable(context, bitmap, drawable, loadedFrom, b, b2));
    }
    
    static void setPlaceholder(final ImageView imageView, final Drawable imageDrawable) {
        imageView.setImageDrawable(imageDrawable);
        if (imageView.getDrawable() instanceof AnimationDrawable) {
            ((AnimationDrawable)imageView.getDrawable()).start();
        }
    }
    
    public void draw(final Canvas canvas) {
        if (!this.animating) {
            super.draw(canvas);
        }
        else {
            final float n = (SystemClock.uptimeMillis() - this.startTimeMillis) / 200.0f;
            if (n >= 1.0f) {
                this.animating = false;
                this.placeholder = null;
                super.draw(canvas);
            }
            else {
                if (this.placeholder != null) {
                    this.placeholder.draw(canvas);
                }
                super.setAlpha((int)(this.alpha * n));
                super.draw(canvas);
                super.setAlpha(this.alpha);
                if (Build$VERSION.SDK_INT <= 10) {
                    this.invalidateSelf();
                }
            }
        }
        if (this.debugging) {
            this.drawDebugIndicator(canvas);
        }
    }
    
    protected void onBoundsChange(final Rect bounds) {
        if (this.placeholder != null) {
            this.placeholder.setBounds(bounds);
        }
        super.onBoundsChange(bounds);
    }
    
    public void setAlpha(final int alpha) {
        this.alpha = alpha;
        if (this.placeholder != null) {
            this.placeholder.setAlpha(alpha);
        }
        super.setAlpha(alpha);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        if (this.placeholder != null) {
            this.placeholder.setColorFilter(colorFilter);
        }
        super.setColorFilter(colorFilter);
    }
}
