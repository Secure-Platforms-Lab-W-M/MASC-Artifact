/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.graphics.drawable.StateListDrawable
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewStub
 *  android.widget.CheckedTextView
 *  android.widget.FrameLayout
 *  android.widget.TextView
 */
package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.RestrictTo;
import android.support.design.R;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class NavigationMenuItemView
extends ForegroundLinearLayout
implements MenuView.ItemView {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private final AccessibilityDelegateCompat mAccessibilityDelegate;
    private FrameLayout mActionArea;
    boolean mCheckable;
    private Drawable mEmptyDrawable;
    private boolean mHasIconTintList;
    private final int mIconSize;
    private ColorStateList mIconTintList;
    private MenuItemImpl mItemData;
    private boolean mNeedsEmptyIcon;
    private final CheckedTextView mTextView;

    public NavigationMenuItemView(Context context) {
        this(context, null);
    }

    public NavigationMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NavigationMenuItemView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mAccessibilityDelegate = new AccessibilityDelegateCompat(){

            @Override
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCheckable(NavigationMenuItemView.this.mCheckable);
            }
        };
        this.setOrientation(0);
        LayoutInflater.from((Context)context).inflate(R.layout.design_navigation_menu_item, (ViewGroup)this, true);
        this.mIconSize = context.getResources().getDimensionPixelSize(R.dimen.design_navigation_icon_size);
        this.mTextView = (CheckedTextView)this.findViewById(R.id.design_menu_item_text);
        this.mTextView.setDuplicateParentStateEnabled(true);
        ViewCompat.setAccessibilityDelegate((View)this.mTextView, this.mAccessibilityDelegate);
    }

    private void adjustAppearance() {
        if (this.shouldExpandActionArea()) {
            this.mTextView.setVisibility(8);
            Object object = this.mActionArea;
            if (object != null) {
                object = (LinearLayoutCompat.LayoutParams)object.getLayoutParams();
                object.width = -1;
                this.mActionArea.setLayoutParams((ViewGroup.LayoutParams)object);
                return;
            }
            return;
        }
        this.mTextView.setVisibility(0);
        Object object = this.mActionArea;
        if (object != null) {
            object = (LinearLayoutCompat.LayoutParams)object.getLayoutParams();
            object.width = -2;
            this.mActionArea.setLayoutParams((ViewGroup.LayoutParams)object);
            return;
        }
    }

    private StateListDrawable createDefaultBackground() {
        TypedValue typedValue = new TypedValue();
        if (this.getContext().getTheme().resolveAttribute(R.attr.colorControlHighlight, typedValue, true)) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(CHECKED_STATE_SET, (Drawable)new ColorDrawable(typedValue.data));
            stateListDrawable.addState(EMPTY_STATE_SET, (Drawable)new ColorDrawable(0));
            return stateListDrawable;
        }
        return null;
    }

    private void setActionView(View view) {
        if (view != null) {
            if (this.mActionArea == null) {
                this.mActionArea = (FrameLayout)((ViewStub)this.findViewById(R.id.design_menu_item_action_area_stub)).inflate();
            }
            this.mActionArea.removeAllViews();
            this.mActionArea.addView(view);
            return;
        }
    }

    private boolean shouldExpandActionArea() {
        if (this.mItemData.getTitle() == null && this.mItemData.getIcon() == null && this.mItemData.getActionView() != null) {
            return true;
        }
        return false;
    }

    @Override
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    @Override
    public void initialize(MenuItemImpl menuItemImpl, int n) {
        this.mItemData = menuItemImpl;
        n = menuItemImpl.isVisible() ? 0 : 8;
        this.setVisibility(n);
        if (this.getBackground() == null) {
            ViewCompat.setBackground((View)this, (Drawable)this.createDefaultBackground());
        }
        this.setCheckable(menuItemImpl.isCheckable());
        this.setChecked(menuItemImpl.isChecked());
        this.setEnabled(menuItemImpl.isEnabled());
        this.setTitle(menuItemImpl.getTitle());
        this.setIcon(menuItemImpl.getIcon());
        this.setActionView(menuItemImpl.getActionView());
        this.setContentDescription(menuItemImpl.getContentDescription());
        TooltipCompat.setTooltipText((View)this, menuItemImpl.getTooltipText());
        this.adjustAppearance();
    }

    protected int[] onCreateDrawableState(int n) {
        int[] arrn = super.onCreateDrawableState(n + 1);
        MenuItemImpl menuItemImpl = this.mItemData;
        if (menuItemImpl != null && menuItemImpl.isCheckable() && this.mItemData.isChecked()) {
            NavigationMenuItemView.mergeDrawableStates((int[])arrn, (int[])CHECKED_STATE_SET);
            return arrn;
        }
        return arrn;
    }

    @Override
    public boolean prefersCondensedTitle() {
        return false;
    }

    public void recycle() {
        FrameLayout frameLayout = this.mActionArea;
        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
        this.mTextView.setCompoundDrawables(null, null, null, null);
    }

    @Override
    public void setCheckable(boolean bl) {
        this.refreshDrawableState();
        if (this.mCheckable != bl) {
            this.mCheckable = bl;
            this.mAccessibilityDelegate.sendAccessibilityEvent((View)this.mTextView, 2048);
            return;
        }
    }

    @Override
    public void setChecked(boolean bl) {
        this.refreshDrawableState();
        this.mTextView.setChecked(bl);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        if (drawable2 != null) {
            if (this.mHasIconTintList) {
                Drawable.ConstantState constantState = drawable2.getConstantState();
                if (constantState != null) {
                    drawable2 = constantState.newDrawable();
                }
                drawable2 = DrawableCompat.wrap(drawable2).mutate();
                DrawableCompat.setTintList(drawable2, this.mIconTintList);
            }
            int n = this.mIconSize;
            drawable2.setBounds(0, 0, n, n);
        } else if (this.mNeedsEmptyIcon) {
            if (this.mEmptyDrawable == null && (drawable2 = (this.mEmptyDrawable = ResourcesCompat.getDrawable(this.getResources(), R.drawable.navigation_empty_icon, this.getContext().getTheme()))) != null) {
                int n = this.mIconSize;
                drawable2.setBounds(0, 0, n, n);
            }
            drawable2 = this.mEmptyDrawable;
        }
        TextViewCompat.setCompoundDrawablesRelative((TextView)this.mTextView, drawable2, null, null, null);
    }

    void setIconTintList(ColorStateList object) {
        this.mIconTintList = object;
        boolean bl = this.mIconTintList != null;
        this.mHasIconTintList = bl;
        object = this.mItemData;
        if (object != null) {
            this.setIcon(object.getIcon());
            return;
        }
    }

    public void setNeedsEmptyIcon(boolean bl) {
        this.mNeedsEmptyIcon = bl;
    }

    @Override
    public void setShortcut(boolean bl, char c) {
    }

    public void setTextAppearance(int n) {
        TextViewCompat.setTextAppearance((TextView)this.mTextView, n);
    }

    public void setTextColor(ColorStateList colorStateList) {
        this.mTextView.setTextColor(colorStateList);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
    }

    @Override
    public boolean showsIcon() {
        return true;
    }

}

