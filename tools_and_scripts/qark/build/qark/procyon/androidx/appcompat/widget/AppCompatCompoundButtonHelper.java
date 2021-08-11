// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.content.res.Resources$NotFoundException;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.R$styleable;
import android.util.AttributeSet;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import android.widget.CompoundButton;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;

class AppCompatCompoundButtonHelper
{
    private ColorStateList mButtonTintList;
    private PorterDuff$Mode mButtonTintMode;
    private boolean mHasButtonTint;
    private boolean mHasButtonTintMode;
    private boolean mSkipNextApply;
    private final CompoundButton mView;
    
    AppCompatCompoundButtonHelper(final CompoundButton mView) {
        this.mButtonTintList = null;
        this.mButtonTintMode = null;
        this.mHasButtonTint = false;
        this.mHasButtonTintMode = false;
        this.mView = mView;
    }
    
    void applyButtonTint() {
        final Drawable buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (buttonDrawable != null && (this.mHasButtonTint || this.mHasButtonTintMode)) {
            final Drawable mutate = DrawableCompat.wrap(buttonDrawable).mutate();
            if (this.mHasButtonTint) {
                DrawableCompat.setTintList(mutate, this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                DrawableCompat.setTintMode(mutate, this.mButtonTintMode);
            }
            if (mutate.isStateful()) {
                mutate.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(mutate);
        }
    }
    
    int getCompoundPaddingLeft(final int n) {
        int n2 = n;
        if (Build$VERSION.SDK_INT < 17) {
            final Drawable buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView);
            n2 = n;
            if (buttonDrawable != null) {
                n2 = n + buttonDrawable.getIntrinsicWidth();
            }
        }
        return n2;
    }
    
    ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }
    
    PorterDuff$Mode getSupportButtonTintMode() {
        return this.mButtonTintMode;
    }
    
    void loadFromAttributes(AttributeSet obtainStyledAttributes, int resourceId) {
        obtainStyledAttributes = (AttributeSet)this.mView.getContext().obtainStyledAttributes(obtainStyledAttributes, R$styleable.CompoundButton, resourceId, 0);
        final int n = resourceId = 0;
        try {
            if (((TypedArray)obtainStyledAttributes).hasValue(R$styleable.CompoundButton_buttonCompat)) {
                final int resourceId2 = ((TypedArray)obtainStyledAttributes).getResourceId(R$styleable.CompoundButton_buttonCompat, 0);
                resourceId = n;
                if (resourceId2 != 0) {
                    try {
                        this.mView.setButtonDrawable(AppCompatResources.getDrawable(this.mView.getContext(), resourceId2));
                        resourceId = 1;
                    }
                    catch (Resources$NotFoundException ex) {
                        resourceId = n;
                    }
                }
            }
            if (resourceId == 0 && ((TypedArray)obtainStyledAttributes).hasValue(R$styleable.CompoundButton_android_button)) {
                resourceId = ((TypedArray)obtainStyledAttributes).getResourceId(R$styleable.CompoundButton_android_button, 0);
                if (resourceId != 0) {
                    this.mView.setButtonDrawable(AppCompatResources.getDrawable(this.mView.getContext(), resourceId));
                }
            }
            if (((TypedArray)obtainStyledAttributes).hasValue(R$styleable.CompoundButton_buttonTint)) {
                CompoundButtonCompat.setButtonTintList(this.mView, ((TypedArray)obtainStyledAttributes).getColorStateList(R$styleable.CompoundButton_buttonTint));
            }
            if (((TypedArray)obtainStyledAttributes).hasValue(R$styleable.CompoundButton_buttonTintMode)) {
                CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(((TypedArray)obtainStyledAttributes).getInt(R$styleable.CompoundButton_buttonTintMode, -1), null));
            }
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
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
    
    void setSupportButtonTintList(final ColorStateList mButtonTintList) {
        this.mButtonTintList = mButtonTintList;
        this.mHasButtonTint = true;
        this.applyButtonTint();
    }
    
    void setSupportButtonTintMode(final PorterDuff$Mode mButtonTintMode) {
        this.mButtonTintMode = mButtonTintMode;
        this.mHasButtonTintMode = true;
        this.applyButtonTint();
    }
    
    interface DirectSetButtonDrawableInterface
    {
        void setButtonDrawable(final Drawable p0);
    }
}
