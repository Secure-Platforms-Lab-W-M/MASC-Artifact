// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.annotation.DrawableRes;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.RestrictTo;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.widget.CompoundButton;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.TintableCompoundButton;
import android.widget.CheckBox;

public class AppCompatCheckBox extends CheckBox implements TintableCompoundButton
{
    private final AppCompatCompoundButtonHelper mCompoundButtonHelper;
    
    public AppCompatCheckBox(final Context context) {
        this(context, null);
    }
    
    public AppCompatCheckBox(final Context context, final AttributeSet set) {
        this(context, set, R.attr.checkboxStyle);
    }
    
    public AppCompatCheckBox(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper((CompoundButton)this)).loadFromAttributes(set, n);
    }
    
    public int getCompoundPaddingLeft() {
        final int compoundPaddingLeft = super.getCompoundPaddingLeft();
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            return mCompoundButtonHelper.getCompoundPaddingLeft(compoundPaddingLeft);
        }
        return compoundPaddingLeft;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportButtonTintList() {
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            return mCompoundButtonHelper.getSupportButtonTintList();
        }
        return null;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportButtonTintMode() {
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            return mCompoundButtonHelper.getSupportButtonTintMode();
        }
        return null;
    }
    
    public void setButtonDrawable(@DrawableRes final int n) {
        this.setButtonDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setButtonDrawable(final Drawable buttonDrawable) {
        super.setButtonDrawable(buttonDrawable);
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.onSetButtonDrawable();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportButtonTintList(@Nullable final ColorStateList supportButtonTintList) {
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.setSupportButtonTintList(supportButtonTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportButtonTintMode(@Nullable final PorterDuff$Mode supportButtonTintMode) {
        final AppCompatCompoundButtonHelper mCompoundButtonHelper = this.mCompoundButtonHelper;
        if (mCompoundButtonHelper != null) {
            mCompoundButtonHelper.setSupportButtonTintMode(supportButtonTintMode);
        }
    }
}
