/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class BottomNavigationItemView
extends FrameLayout
implements MenuView.ItemView {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    public static final int INVALID_ITEM_POSITION = -1;
    private final int mDefaultMargin;
    private ImageView mIcon;
    private ColorStateList mIconTint;
    private MenuItemImpl mItemData;
    private int mItemPosition = -1;
    private final TextView mLargeLabel;
    private final float mScaleDownFactor;
    private final float mScaleUpFactor;
    private final int mShiftAmount;
    private boolean mShiftingMode;
    private final TextView mSmallLabel;

    public BottomNavigationItemView(@NonNull Context context) {
        this(context, null);
    }

    public BottomNavigationItemView(@NonNull Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BottomNavigationItemView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        attributeSet = this.getResources();
        n = attributeSet.getDimensionPixelSize(R.dimen.design_bottom_navigation_text_size);
        int n2 = attributeSet.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_text_size);
        this.mDefaultMargin = attributeSet.getDimensionPixelSize(R.dimen.design_bottom_navigation_margin);
        this.mShiftAmount = n - n2;
        this.mScaleUpFactor = (float)n2 * 1.0f / (float)n;
        this.mScaleDownFactor = (float)n * 1.0f / (float)n2;
        LayoutInflater.from((Context)context).inflate(R.layout.design_bottom_navigation_item, (ViewGroup)this, true);
        this.setBackgroundResource(R.drawable.design_bottom_navigation_item_background);
        this.mIcon = (ImageView)this.findViewById(R.id.icon);
        this.mSmallLabel = (TextView)this.findViewById(R.id.smallLabel);
        this.mLargeLabel = (TextView)this.findViewById(R.id.largeLabel);
    }

    @Override
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public int getItemPosition() {
        return this.mItemPosition;
    }

    @Override
    public void initialize(MenuItemImpl menuItemImpl, int n) {
        this.mItemData = menuItemImpl;
        this.setCheckable(menuItemImpl.isCheckable());
        this.setChecked(menuItemImpl.isChecked());
        this.setEnabled(menuItemImpl.isEnabled());
        this.setIcon(menuItemImpl.getIcon());
        this.setTitle(menuItemImpl.getTitle());
        this.setId(menuItemImpl.getItemId());
        this.setContentDescription(menuItemImpl.getContentDescription());
        TooltipCompat.setTooltipText((View)this, menuItemImpl.getTooltipText());
    }

    public int[] onCreateDrawableState(int n) {
        int[] arrn = super.onCreateDrawableState(n + 1);
        MenuItemImpl menuItemImpl = this.mItemData;
        if (menuItemImpl != null && menuItemImpl.isCheckable() && this.mItemData.isChecked()) {
            BottomNavigationItemView.mergeDrawableStates((int[])arrn, (int[])CHECKED_STATE_SET);
            return arrn;
        }
        return arrn;
    }

    @Override
    public boolean prefersCondensedTitle() {
        return false;
    }

    @Override
    public void setCheckable(boolean bl) {
        this.refreshDrawableState();
    }

    @Override
    public void setChecked(boolean bl) {
        TextView textView = this.mLargeLabel;
        textView.setPivotX((float)(textView.getWidth() / 2));
        textView = this.mLargeLabel;
        textView.setPivotY((float)textView.getBaseline());
        textView = this.mSmallLabel;
        textView.setPivotX((float)(textView.getWidth() / 2));
        textView = this.mSmallLabel;
        textView.setPivotY((float)textView.getBaseline());
        if (this.mShiftingMode) {
            if (bl) {
                textView = (FrameLayout.LayoutParams)this.mIcon.getLayoutParams();
                textView.gravity = 49;
                textView.topMargin = this.mDefaultMargin;
                this.mIcon.setLayoutParams((ViewGroup.LayoutParams)textView);
                this.mLargeLabel.setVisibility(0);
                this.mLargeLabel.setScaleX(1.0f);
                this.mLargeLabel.setScaleY(1.0f);
            } else {
                textView = (FrameLayout.LayoutParams)this.mIcon.getLayoutParams();
                textView.gravity = 17;
                textView.topMargin = this.mDefaultMargin;
                this.mIcon.setLayoutParams((ViewGroup.LayoutParams)textView);
                this.mLargeLabel.setVisibility(4);
                this.mLargeLabel.setScaleX(0.5f);
                this.mLargeLabel.setScaleY(0.5f);
            }
            this.mSmallLabel.setVisibility(4);
        } else if (bl) {
            textView = (FrameLayout.LayoutParams)this.mIcon.getLayoutParams();
            textView.gravity = 49;
            textView.topMargin = this.mDefaultMargin + this.mShiftAmount;
            this.mIcon.setLayoutParams((ViewGroup.LayoutParams)textView);
            this.mLargeLabel.setVisibility(0);
            this.mSmallLabel.setVisibility(4);
            this.mLargeLabel.setScaleX(1.0f);
            this.mLargeLabel.setScaleY(1.0f);
            this.mSmallLabel.setScaleX(this.mScaleUpFactor);
            this.mSmallLabel.setScaleY(this.mScaleUpFactor);
        } else {
            textView = (FrameLayout.LayoutParams)this.mIcon.getLayoutParams();
            textView.gravity = 49;
            textView.topMargin = this.mDefaultMargin;
            this.mIcon.setLayoutParams((ViewGroup.LayoutParams)textView);
            this.mLargeLabel.setVisibility(4);
            this.mSmallLabel.setVisibility(0);
            this.mLargeLabel.setScaleX(this.mScaleDownFactor);
            this.mLargeLabel.setScaleY(this.mScaleDownFactor);
            this.mSmallLabel.setScaleX(1.0f);
            this.mSmallLabel.setScaleY(1.0f);
        }
        this.refreshDrawableState();
    }

    @Override
    public void setEnabled(boolean bl) {
        super.setEnabled(bl);
        this.mSmallLabel.setEnabled(bl);
        this.mLargeLabel.setEnabled(bl);
        this.mIcon.setEnabled(bl);
        if (bl) {
            ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
            return;
        }
        ViewCompat.setPointerIcon((View)this, null);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        if (drawable2 != null) {
            Drawable.ConstantState constantState = drawable2.getConstantState();
            if (constantState != null) {
                drawable2 = constantState.newDrawable();
            }
            drawable2 = DrawableCompat.wrap(drawable2).mutate();
            DrawableCompat.setTintList(drawable2, this.mIconTint);
        }
        this.mIcon.setImageDrawable(drawable2);
    }

    public void setIconTintList(ColorStateList object) {
        this.mIconTint = object;
        object = this.mItemData;
        if (object != null) {
            this.setIcon(object.getIcon());
            return;
        }
    }

    public void setItemBackground(int n) {
        Drawable drawable2 = n == 0 ? null : ContextCompat.getDrawable(this.getContext(), n);
        ViewCompat.setBackground((View)this, drawable2);
    }

    public void setItemPosition(int n) {
        this.mItemPosition = n;
    }

    public void setShiftingMode(boolean bl) {
        this.mShiftingMode = bl;
    }

    @Override
    public void setShortcut(boolean bl, char c) {
    }

    public void setTextColor(ColorStateList colorStateList) {
        this.mSmallLabel.setTextColor(colorStateList);
        this.mLargeLabel.setTextColor(colorStateList);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mSmallLabel.setText(charSequence);
        this.mLargeLabel.setText(charSequence);
    }

    @Override
    public boolean showsIcon() {
        return true;
    }
}

