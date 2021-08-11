// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.R$attr;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.RatingBar;

public class AppCompatRatingBar extends RatingBar
{
    private final AppCompatProgressBarHelper mAppCompatProgressBarHelper;
    
    public AppCompatRatingBar(final Context context) {
        this(context, null);
    }
    
    public AppCompatRatingBar(final Context context, final AttributeSet set) {
        this(context, set, R$attr.ratingBarStyle);
    }
    
    public AppCompatRatingBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        (this.mAppCompatProgressBarHelper = new AppCompatProgressBarHelper((ProgressBar)this)).loadFromAttributes(set, n);
    }
    
    protected void onMeasure(final int n, final int n2) {
        synchronized (this) {
            super.onMeasure(n, n2);
            final Bitmap sampleTile = this.mAppCompatProgressBarHelper.getSampleTile();
            if (sampleTile != null) {
                this.setMeasuredDimension(View.resolveSizeAndState(sampleTile.getWidth() * this.getNumStars(), n, 0), this.getMeasuredHeight());
            }
        }
    }
}
