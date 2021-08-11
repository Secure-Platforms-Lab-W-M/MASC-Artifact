/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.ProgressBar
 *  android.widget.RatingBar
 *  androidx.appcompat.R
 *  androidx.appcompat.R$attr
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatProgressBarHelper;

public class AppCompatRatingBar
extends RatingBar {
    private final AppCompatProgressBarHelper mAppCompatProgressBarHelper;

    public AppCompatRatingBar(Context context) {
        this(context, null);
    }

    public AppCompatRatingBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.ratingBarStyle);
    }

    public AppCompatRatingBar(Context object, AttributeSet attributeSet, int n) {
        super((Context)object, attributeSet, n);
        this.mAppCompatProgressBarHelper = object = new AppCompatProgressBarHelper((ProgressBar)this);
        object.loadFromAttributes(attributeSet, n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onMeasure(int n, int n2) {
        synchronized (this) {
            super.onMeasure(n, n2);
            Bitmap bitmap = this.mAppCompatProgressBarHelper.getSampleTile();
            if (bitmap != null) {
                this.setMeasuredDimension(View.resolveSizeAndState((int)(bitmap.getWidth() * this.getNumStars()), (int)n, (int)0), this.getMeasuredHeight());
            }
            return;
        }
    }
}

