/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.widget.CompoundButton
 *  androidx.appcompat.R
 *  androidx.appcompat.R$styleable
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.DrawableUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;

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
        Drawable drawable = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (drawable != null && (this.mHasButtonTint || this.mHasButtonTintMode)) {
            drawable = DrawableCompat.wrap(drawable).mutate();
            if (this.mHasButtonTint) {
                DrawableCompat.setTintList(drawable, this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                DrawableCompat.setTintMode(drawable, this.mButtonTintMode);
            }
            if (drawable.isStateful()) {
                drawable.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(drawable);
        }
    }

    int getCompoundPaddingLeft(int n) {
        int n2 = n;
        if (Build.VERSION.SDK_INT < 17) {
            Drawable drawable = CompoundButtonCompat.getButtonDrawable(this.mView);
            n2 = n;
            if (drawable != null) {
                n2 = n + drawable.getIntrinsicWidth();
            }
        }
        return n2;
    }

    ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }

    PorterDuff.Mode getSupportButtonTintMode() {
        return this.mButtonTintMode;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void loadFromAttributes(AttributeSet attributeSet, int n) {
        int n2;
        attributeSet = this.mView.getContext().obtainStyledAttributes(attributeSet, R.styleable.CompoundButton, n, 0);
        n = n2 = 0;
        try {
            if (attributeSet.hasValue(R.styleable.CompoundButton_buttonCompat)) {
                int n3 = attributeSet.getResourceId(R.styleable.CompoundButton_buttonCompat, 0);
                n = n2;
                if (n3 != 0) {
                    try {
                        this.mView.setButtonDrawable(AppCompatResources.getDrawable(this.mView.getContext(), n3));
                        n = 1;
                    }
                    catch (Resources.NotFoundException notFoundException) {
                        n = n2;
                    }
                }
            }
            if (n == 0 && attributeSet.hasValue(R.styleable.CompoundButton_android_button) && (n = attributeSet.getResourceId(R.styleable.CompoundButton_android_button, 0)) != 0) {
                this.mView.setButtonDrawable(AppCompatResources.getDrawable(this.mView.getContext(), n));
            }
            if (attributeSet.hasValue(R.styleable.CompoundButton_buttonTint)) {
                CompoundButtonCompat.setButtonTintList(this.mView, attributeSet.getColorStateList(R.styleable.CompoundButton_buttonTint));
            }
            if (!attributeSet.hasValue(R.styleable.CompoundButton_buttonTintMode)) return;
            CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(attributeSet.getInt(R.styleable.CompoundButton_buttonTintMode, -1), null));
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

    void setSupportButtonTintMode(PorterDuff.Mode mode) {
        this.mButtonTintMode = mode;
        this.mHasButtonTintMode = true;
        this.applyButtonTint();
    }

    static interface DirectSetButtonDrawableInterface {
        public void setButtonDrawable(Drawable var1);
    }

}

