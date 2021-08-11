// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.CompoundButtonCompat;
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
        if (Build$VERSION.SDK_INT >= 17) {
            return n;
        }
        final Drawable buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (buttonDrawable != null) {
            return n + buttonDrawable.getIntrinsicWidth();
        }
        return n;
    }
    
    ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }
    
    PorterDuff$Mode getSupportButtonTintMode() {
        return this.mButtonTintMode;
    }
    
    void loadFromAttributes(AttributeSet obtainStyledAttributes, int resourceId) {
    Label_0088_Outer:
        while (true) {
            obtainStyledAttributes = (AttributeSet)this.mView.getContext().obtainStyledAttributes(obtainStyledAttributes, R.styleable.CompoundButton, resourceId, 0);
            while (true) {
            Label_0138:
                while (true) {
                    Label_0135: {
                        Label_0132: {
                            try {
                                if (!((TypedArray)obtainStyledAttributes).hasValue(R.styleable.CompoundButton_android_button)) {
                                    break Label_0135;
                                }
                                resourceId = ((TypedArray)obtainStyledAttributes).getResourceId(R.styleable.CompoundButton_android_button, 0);
                                if (resourceId == 0) {
                                    break Label_0132;
                                }
                                this.mView.setButtonDrawable(AppCompatResources.getDrawable(this.mView.getContext(), resourceId));
                                if (((TypedArray)obtainStyledAttributes).hasValue(R.styleable.CompoundButton_buttonTint)) {
                                    CompoundButtonCompat.setButtonTintList(this.mView, ((TypedArray)obtainStyledAttributes).getColorStateList(R.styleable.CompoundButton_buttonTint));
                                    if (((TypedArray)obtainStyledAttributes).hasValue(R.styleable.CompoundButton_buttonTintMode)) {
                                        CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(((TypedArray)obtainStyledAttributes).getInt(R.styleable.CompoundButton_buttonTintMode, -1), null));
                                    }
                                    return;
                                }
                                break Label_0138;
                            }
                            finally {
                                ((TypedArray)obtainStyledAttributes).recycle();
                            }
                        }
                        continue Label_0088_Outer;
                    }
                    continue Label_0088_Outer;
                }
                continue;
            }
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
    
    void setSupportButtonTintMode(@Nullable final PorterDuff$Mode mButtonTintMode) {
        this.mButtonTintMode = mButtonTintMode;
        this.mHasButtonTintMode = true;
        this.applyButtonTint();
    }
    
    interface DirectSetButtonDrawableInterface
    {
        void setButtonDrawable(final Drawable p0);
    }
}
