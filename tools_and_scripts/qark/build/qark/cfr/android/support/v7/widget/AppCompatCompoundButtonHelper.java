/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.widget.CompoundButton
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DrawableUtils;
import android.util.AttributeSet;
import android.widget.CompoundButton;

class AppCompatCompoundButtonHelper {
    private ColorStateList mButtonTintList = null;
    private PorterDuff.Mode mButtonTintMode = null;
    private boolean mHasButtonTint = false;
    private boolean mHasButtonTintMode = false;
    private boolean mSkipNextApply;
    private final CompoundButton mView;

    AppCompatCompoundButtonHelper(CompoundButton compoundButton) {
        this.mView = compoundButton;
    }

    void applyButtonTint() {
        Drawable drawable2 = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (drawable2 != null && (this.mHasButtonTint || this.mHasButtonTintMode)) {
            drawable2 = DrawableCompat.wrap(drawable2).mutate();
            if (this.mHasButtonTint) {
                DrawableCompat.setTintList(drawable2, this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                DrawableCompat.setTintMode(drawable2, this.mButtonTintMode);
            }
            if (drawable2.isStateful()) {
                drawable2.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(drawable2);
            return;
        }
    }

    int getCompoundPaddingLeft(int n) {
        if (Build.VERSION.SDK_INT < 17) {
            Drawable drawable2 = CompoundButtonCompat.getButtonDrawable(this.mView);
            if (drawable2 != null) {
                return n + drawable2.getIntrinsicWidth();
            }
            return n;
        }
        return n;
    }

    ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }

    PorterDuff.Mode getSupportButtonTintMode() {
        return this.mButtonTintMode;
    }

    void loadFromAttributes(AttributeSet attributeSet, int n) {
        attributeSet = this.mView.getContext().obtainStyledAttributes(attributeSet, R.styleable.CompoundButton, n, 0);
        try {
            if (attributeSet.hasValue(R.styleable.CompoundButton_android_button) && (n = attributeSet.getResourceId(R.styleable.CompoundButton_android_button, 0)) != 0) {
                this.mView.setButtonDrawable(AppCompatResources.getDrawable(this.mView.getContext(), n));
            }
            if (attributeSet.hasValue(R.styleable.CompoundButton_buttonTint)) {
                CompoundButtonCompat.setButtonTintList(this.mView, attributeSet.getColorStateList(R.styleable.CompoundButton_buttonTint));
            }
            if (attributeSet.hasValue(R.styleable.CompoundButton_buttonTintMode)) {
                CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(attributeSet.getInt(R.styleable.CompoundButton_buttonTintMode, -1), null));
            }
            return;
        }
        finally {
            attributeSet.recycle();
        }
    }

    void onSetButtonDrawable() {
        if (this.mSkipNextApply) {
            this.mSkipNextApply = false;
            return;
        }
        this.mSkipNextApply = true;
        this.applyButtonTint();
    }

    void setSupportButtonTintList(ColorStateList colorStateList) {
        this.mButtonTintList = colorStateList;
        this.mHasButtonTint = true;
        this.applyButtonTint();
    }

    void setSupportButtonTintMode(@Nullable PorterDuff.Mode mode) {
        this.mButtonTintMode = mode;
        this.mHasButtonTintMode = true;
        this.applyButtonTint();
    }

    static interface DirectSetButtonDrawableInterface {
        public void setButtonDrawable(Drawable var1);
    }

}

