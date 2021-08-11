/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextHelper;
import android.support.v7.widget.TintInfo;
import android.util.AttributeSet;
import android.widget.TextView;

@RequiresApi(value=17)
class AppCompatTextHelperV17
extends AppCompatTextHelper {
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableStartTint;

    AppCompatTextHelperV17(TextView textView) {
        super(textView);
    }

    @Override
    void applyCompoundDrawablesTints() {
        super.applyCompoundDrawablesTints();
        if (this.mDrawableStartTint == null && this.mDrawableEndTint == null) {
            return;
        }
        Drawable[] arrdrawable = this.mView.getCompoundDrawablesRelative();
        this.applyCompoundDrawableTint(arrdrawable[0], this.mDrawableStartTint);
        this.applyCompoundDrawableTint(arrdrawable[2], this.mDrawableEndTint);
    }

    @Override
    void loadFromAttributes(AttributeSet attributeSet, int n) {
        super.loadFromAttributes(attributeSet, n);
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.AppCompatTextHelper, n, 0);
        if (attributeSet.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
            this.mDrawableStartTint = AppCompatTextHelperV17.createTintInfo(context, appCompatDrawableManager, attributeSet.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
        }
        if (attributeSet.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
            this.mDrawableEndTint = AppCompatTextHelperV17.createTintInfo(context, appCompatDrawableManager, attributeSet.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
        }
        attributeSet.recycle();
    }
}

