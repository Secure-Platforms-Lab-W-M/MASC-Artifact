// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.support.v7.content.res.AppCompatResources;
import android.support.annotation.DrawableRes;
import android.widget.TextView;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.CheckedTextView;

public class AppCompatCheckedTextView extends CheckedTextView
{
    private static final int[] TINT_ATTRS;
    private final AppCompatTextHelper mTextHelper;
    
    static {
        TINT_ATTRS = new int[] { 16843016 };
    }
    
    public AppCompatCheckedTextView(final Context context) {
        this(context, null);
    }
    
    public AppCompatCheckedTextView(final Context context, final AttributeSet set) {
        this(context, set, 16843720);
    }
    
    public AppCompatCheckedTextView(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mTextHelper = AppCompatTextHelper.create((TextView)this)).loadFromAttributes(set, n);
        this.mTextHelper.applyCompoundDrawablesTints();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, AppCompatCheckedTextView.TINT_ATTRS, n, 0);
        this.setCheckMarkDrawable(obtainStyledAttributes.getDrawable(0));
        obtainStyledAttributes.recycle();
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            mTextHelper.applyCompoundDrawablesTints();
        }
    }
    
    public void setCheckMarkDrawable(@DrawableRes final int n) {
        this.setCheckMarkDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setTextAppearance(final Context context, final int n) {
        super.setTextAppearance(context, n);
        final AppCompatTextHelper mTextHelper = this.mTextHelper;
        if (mTextHelper != null) {
            mTextHelper.onSetTextAppearance(context, n);
        }
    }
}
