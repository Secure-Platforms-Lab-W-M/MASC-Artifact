/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Canvas
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.ProgressBar
 *  android.widget.SeekBar
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatProgressBarHelper;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

class AppCompatSeekBarHelper
extends AppCompatProgressBarHelper {
    private boolean mHasTickMarkTint = false;
    private boolean mHasTickMarkTintMode = false;
    private Drawable mTickMark;
    private ColorStateList mTickMarkTintList = null;
    private PorterDuff.Mode mTickMarkTintMode = null;
    private final SeekBar mView;

    AppCompatSeekBarHelper(SeekBar seekBar) {
        super((ProgressBar)seekBar);
        this.mView = seekBar;
    }

    private void applyTickMarkTint() {
        if (this.mTickMark != null && (this.mHasTickMarkTint || this.mHasTickMarkTintMode)) {
            this.mTickMark = DrawableCompat.wrap(this.mTickMark.mutate());
            if (this.mHasTickMarkTint) {
                DrawableCompat.setTintList(this.mTickMark, this.mTickMarkTintList);
            }
            if (this.mHasTickMarkTintMode) {
                DrawableCompat.setTintMode(this.mTickMark, this.mTickMarkTintMode);
            }
            if (this.mTickMark.isStateful()) {
                this.mTickMark.setState(this.mView.getDrawableState());
                return;
            }
            return;
        }
    }

    void drawTickMarks(Canvas canvas) {
        if (this.mTickMark != null) {
            int n = this.mView.getMax();
            int n2 = 1;
            if (n > 1) {
                int n3 = this.mTickMark.getIntrinsicWidth();
                int n4 = this.mTickMark.getIntrinsicHeight();
                n3 = n3 >= 0 ? (n3 /= 2) : 1;
                if (n4 >= 0) {
                    n2 = n4 / 2;
                }
                this.mTickMark.setBounds(- n3, - n2, n3, n2);
                float f = (float)(this.mView.getWidth() - this.mView.getPaddingLeft() - this.mView.getPaddingRight()) / (float)n;
                n2 = canvas.save();
                canvas.translate((float)this.mView.getPaddingLeft(), (float)(this.mView.getHeight() / 2));
                for (n3 = 0; n3 <= n; ++n3) {
                    this.mTickMark.draw(canvas);
                    canvas.translate(f, 0.0f);
                }
                canvas.restoreToCount(n2);
                return;
            }
            return;
        }
    }

    void drawableStateChanged() {
        Drawable drawable2 = this.mTickMark;
        if (drawable2 != null && drawable2.isStateful()) {
            if (drawable2.setState(this.mView.getDrawableState())) {
                this.mView.invalidateDrawable(drawable2);
                return;
            }
            return;
        }
    }

    @Nullable
    Drawable getTickMark() {
        return this.mTickMark;
    }

    @Nullable
    ColorStateList getTickMarkTintList() {
        return this.mTickMarkTintList;
    }

    @Nullable
    PorterDuff.Mode getTickMarkTintMode() {
        return this.mTickMarkTintMode;
    }

    @RequiresApi(value=11)
    void jumpDrawablesToCurrentState() {
        Drawable drawable2 = this.mTickMark;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
            return;
        }
    }

    @Override
    void loadFromAttributes(AttributeSet object, int n) {
        super.loadFromAttributes((AttributeSet)object, n);
        object = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), (AttributeSet)object, R.styleable.AppCompatSeekBar, n, 0);
        Drawable drawable2 = object.getDrawableIfKnown(R.styleable.AppCompatSeekBar_android_thumb);
        if (drawable2 != null) {
            this.mView.setThumb(drawable2);
        }
        this.setTickMark(object.getDrawable(R.styleable.AppCompatSeekBar_tickMark));
        if (object.hasValue(R.styleable.AppCompatSeekBar_tickMarkTintMode)) {
            this.mTickMarkTintMode = DrawableUtils.parseTintMode(object.getInt(R.styleable.AppCompatSeekBar_tickMarkTintMode, -1), this.mTickMarkTintMode);
            this.mHasTickMarkTintMode = true;
        }
        if (object.hasValue(R.styleable.AppCompatSeekBar_tickMarkTint)) {
            this.mTickMarkTintList = object.getColorStateList(R.styleable.AppCompatSeekBar_tickMarkTint);
            this.mHasTickMarkTint = true;
        }
        object.recycle();
        this.applyTickMarkTint();
    }

    void setTickMark(@Nullable Drawable drawable2) {
        Drawable drawable3 = this.mTickMark;
        if (drawable3 != null) {
            drawable3.setCallback(null);
        }
        this.mTickMark = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this.mView);
            DrawableCompat.setLayoutDirection(drawable2, ViewCompat.getLayoutDirection((View)this.mView));
            if (drawable2.isStateful()) {
                drawable2.setState(this.mView.getDrawableState());
            }
            this.applyTickMarkTint();
        }
        this.mView.invalidate();
    }

    void setTickMarkTintList(@Nullable ColorStateList colorStateList) {
        this.mTickMarkTintList = colorStateList;
        this.mHasTickMarkTint = true;
        this.applyTickMarkTint();
    }

    void setTickMarkTintMode(@Nullable PorterDuff.Mode mode) {
        this.mTickMarkTintMode = mode;
        this.mHasTickMarkTintMode = true;
        this.applyTickMarkTint();
    }
}

