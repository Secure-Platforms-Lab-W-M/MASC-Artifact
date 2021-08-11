// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.internal;

import android.view.View$MeasureSpec;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.content.res.TypedArray;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.Button;
import android.support.annotation.RestrictTo;
import android.support.design.widget.BaseTransientBottomBar;
import android.widget.LinearLayout;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class SnackbarContentLayout extends LinearLayout implements ContentViewCallback
{
    private Button mActionView;
    private int mMaxInlineActionWidth;
    private int mMaxWidth;
    private TextView mMessageView;
    
    public SnackbarContentLayout(final Context context) {
        this(context, null);
    }
    
    public SnackbarContentLayout(final Context context, final AttributeSet set) {
        super(context, set);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.SnackbarLayout);
        this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
        this.mMaxInlineActionWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
        obtainStyledAttributes.recycle();
    }
    
    private static void updateTopBottomPadding(final View view, final int n, final int n2) {
        if (ViewCompat.isPaddingRelative(view)) {
            ViewCompat.setPaddingRelative(view, ViewCompat.getPaddingStart(view), n, ViewCompat.getPaddingEnd(view), n2);
            return;
        }
        view.setPadding(view.getPaddingLeft(), n, view.getPaddingRight(), n2);
    }
    
    private boolean updateViewsWithinLayout(final int orientation, final int n, final int n2) {
        boolean b = false;
        if (orientation != this.getOrientation()) {
            this.setOrientation(orientation);
            b = true;
        }
        if (this.mMessageView.getPaddingTop() == n && this.mMessageView.getPaddingBottom() == n2) {
            return b;
        }
        updateTopBottomPadding((View)this.mMessageView, n, n2);
        return true;
    }
    
    public void animateContentIn(final int n, final int n2) {
        this.mMessageView.setAlpha(0.0f);
        this.mMessageView.animate().alpha(1.0f).setDuration((long)n2).setStartDelay((long)n).start();
        if (this.mActionView.getVisibility() == 0) {
            this.mActionView.setAlpha(0.0f);
            this.mActionView.animate().alpha(1.0f).setDuration((long)n2).setStartDelay((long)n).start();
        }
    }
    
    public void animateContentOut(final int n, final int n2) {
        this.mMessageView.setAlpha(1.0f);
        this.mMessageView.animate().alpha(0.0f).setDuration((long)n2).setStartDelay((long)n).start();
        if (this.mActionView.getVisibility() == 0) {
            this.mActionView.setAlpha(1.0f);
            this.mActionView.animate().alpha(0.0f).setDuration((long)n2).setStartDelay((long)n).start();
        }
    }
    
    public Button getActionView() {
        return this.mActionView;
    }
    
    public TextView getMessageView() {
        return this.mMessageView;
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mMessageView = (TextView)this.findViewById(R.id.snackbar_text);
        this.mActionView = (Button)this.findViewById(R.id.snackbar_action);
    }
    
    protected void onMeasure(int n, final int n2) {
        super.onMeasure(n, n2);
        int measureSpec = 0;
        Label_0049: {
            if (this.mMaxWidth > 0) {
                final int measuredWidth = this.getMeasuredWidth();
                final int mMaxWidth = this.mMaxWidth;
                if (measuredWidth > mMaxWidth) {
                    measureSpec = View$MeasureSpec.makeMeasureSpec(mMaxWidth, 1073741824);
                    super.onMeasure(measureSpec, n2);
                    break Label_0049;
                }
            }
            measureSpec = n;
        }
        final int dimensionPixelSize = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
        final int dimensionPixelSize2 = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
        if (this.mMessageView.getLayout().getLineCount() > 1) {
            n = 1;
        }
        else {
            n = 0;
        }
        final int n3 = 0;
        if (n != 0 && this.mMaxInlineActionWidth > 0 && this.mActionView.getMeasuredWidth() > this.mMaxInlineActionWidth) {
            if (this.updateViewsWithinLayout(1, dimensionPixelSize, dimensionPixelSize - dimensionPixelSize2)) {
                n = 1;
            }
            else {
                n = n3;
            }
        }
        else {
            if (n != 0) {
                n = dimensionPixelSize;
            }
            else {
                n = dimensionPixelSize2;
            }
            if (this.updateViewsWithinLayout(0, n, n)) {
                n = 1;
            }
            else {
                n = n3;
            }
        }
        if (n != 0) {
            super.onMeasure(measureSpec, n2);
        }
    }
}
