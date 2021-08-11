/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$color
 *  com.google.android.material.R$dimen
 *  com.google.android.material.R$style
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationPresenter;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;

public class BottomNavigationView
extends FrameLayout {
    private static final int DEF_STYLE_RES = R.style.Widget_Design_BottomNavigationView;
    private static final int MENU_PRESENTER_ID = 1;
    private ColorStateList itemRippleColor;
    private final MenuBuilder menu;
    private MenuInflater menuInflater;
    final BottomNavigationMenuView menuView;
    private final BottomNavigationPresenter presenter = new BottomNavigationPresenter();
    private OnNavigationItemReselectedListener reselectedListener;
    private OnNavigationItemSelectedListener selectedListener;

    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.bottomNavigationStyle);
    }

    public BottomNavigationView(Context context, AttributeSet object, int n) {
        BottomNavigationMenuView bottomNavigationMenuView;
        super(ThemeEnforcement.createThemedContext(context, (AttributeSet)object, n, DEF_STYLE_RES), (AttributeSet)object, n);
        context = this.getContext();
        this.menu = new BottomNavigationMenu(context);
        this.menuView = new BottomNavigationMenuView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        this.menuView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.presenter.setBottomNavigationMenuView(this.menuView);
        this.presenter.setId(1);
        this.menuView.setPresenter(this.presenter);
        this.menu.addMenuPresenter(this.presenter);
        this.presenter.initForMenu(this.getContext(), this.menu);
        object = ThemeEnforcement.obtainTintedStyledAttributes(context, (AttributeSet)object, R.styleable.BottomNavigationView, n, R.style.Widget_Design_BottomNavigationView, R.styleable.BottomNavigationView_itemTextAppearanceInactive, R.styleable.BottomNavigationView_itemTextAppearanceActive);
        if (object.hasValue(R.styleable.BottomNavigationView_itemIconTint)) {
            this.menuView.setIconTintList(object.getColorStateList(R.styleable.BottomNavigationView_itemIconTint));
        } else {
            bottomNavigationMenuView = this.menuView;
            bottomNavigationMenuView.setIconTintList(bottomNavigationMenuView.createDefaultColorStateList(16842808));
        }
        this.setItemIconSize(object.getDimensionPixelSize(R.styleable.BottomNavigationView_itemIconSize, this.getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_icon_size)));
        if (object.hasValue(R.styleable.BottomNavigationView_itemTextAppearanceInactive)) {
            this.setItemTextAppearanceInactive(object.getResourceId(R.styleable.BottomNavigationView_itemTextAppearanceInactive, 0));
        }
        if (object.hasValue(R.styleable.BottomNavigationView_itemTextAppearanceActive)) {
            this.setItemTextAppearanceActive(object.getResourceId(R.styleable.BottomNavigationView_itemTextAppearanceActive, 0));
        }
        if (object.hasValue(R.styleable.BottomNavigationView_itemTextColor)) {
            this.setItemTextColor(object.getColorStateList(R.styleable.BottomNavigationView_itemTextColor));
        }
        if (this.getBackground() == null || this.getBackground() instanceof ColorDrawable) {
            ViewCompat.setBackground((View)this, this.createMaterialShapeDrawableBackground(context));
        }
        if (object.hasValue(R.styleable.BottomNavigationView_elevation)) {
            ViewCompat.setElevation((View)this, object.getDimensionPixelSize(R.styleable.BottomNavigationView_elevation, 0));
        }
        bottomNavigationMenuView = MaterialResources.getColorStateList(context, (TintTypedArray)object, R.styleable.BottomNavigationView_backgroundTint);
        DrawableCompat.setTintList(this.getBackground().mutate(), (ColorStateList)bottomNavigationMenuView);
        this.setLabelVisibilityMode(object.getInteger(R.styleable.BottomNavigationView_labelVisibilityMode, -1));
        this.setItemHorizontalTranslationEnabled(object.getBoolean(R.styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
        n = object.getResourceId(R.styleable.BottomNavigationView_itemBackground, 0);
        if (n != 0) {
            this.menuView.setItemBackgroundRes(n);
        } else {
            this.setItemRippleColor(MaterialResources.getColorStateList(context, (TintTypedArray)object, R.styleable.BottomNavigationView_itemRippleColor));
        }
        if (object.hasValue(R.styleable.BottomNavigationView_menu)) {
            this.inflateMenu(object.getResourceId(R.styleable.BottomNavigationView_menu, 0));
        }
        object.recycle();
        this.addView((View)this.menuView, (ViewGroup.LayoutParams)layoutParams);
        if (Build.VERSION.SDK_INT < 21) {
            this.addCompatibilityTopDivider(context);
        }
        this.menu.setCallback(new MenuBuilder.Callback(){

            @Override
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                if (BottomNavigationView.this.reselectedListener != null && menuItem.getItemId() == BottomNavigationView.this.getSelectedItemId()) {
                    BottomNavigationView.this.reselectedListener.onNavigationItemReselected(menuItem);
                    return true;
                }
                if (BottomNavigationView.this.selectedListener != null && !BottomNavigationView.this.selectedListener.onNavigationItemSelected(menuItem)) {
                    return true;
                }
                return false;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }
        });
        this.applyWindowInsets();
    }

    private void addCompatibilityTopDivider(Context context) {
        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.design_bottom_navigation_shadow_color));
        view.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, this.getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_shadow_height)));
        this.addView(view);
    }

    private void applyWindowInsets() {
        ViewUtils.doOnApplyWindowInsets((View)this, new ViewUtils.OnApplyWindowInsetsListener(){

            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                relativePadding.bottom += windowInsetsCompat.getSystemWindowInsetBottom();
                relativePadding.applyToView(view);
                return windowInsetsCompat;
            }
        });
    }

    private MaterialShapeDrawable createMaterialShapeDrawableBackground(Context context) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        Drawable drawable2 = this.getBackground();
        if (drawable2 instanceof ColorDrawable) {
            materialShapeDrawable.setFillColor(ColorStateList.valueOf((int)((ColorDrawable)drawable2).getColor()));
        }
        materialShapeDrawable.initializeElevationOverlay(context);
        return materialShapeDrawable;
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(this.getContext());
        }
        return this.menuInflater;
    }

    public BadgeDrawable getBadge(int n) {
        return this.menuView.getBadge(n);
    }

    public Drawable getItemBackground() {
        return this.menuView.getItemBackground();
    }

    @Deprecated
    public int getItemBackgroundResource() {
        return this.menuView.getItemBackgroundRes();
    }

    public int getItemIconSize() {
        return this.menuView.getItemIconSize();
    }

    public ColorStateList getItemIconTintList() {
        return this.menuView.getIconTintList();
    }

    public ColorStateList getItemRippleColor() {
        return this.itemRippleColor;
    }

    public int getItemTextAppearanceActive() {
        return this.menuView.getItemTextAppearanceActive();
    }

    public int getItemTextAppearanceInactive() {
        return this.menuView.getItemTextAppearanceInactive();
    }

    public ColorStateList getItemTextColor() {
        return this.menuView.getItemTextColor();
    }

    public int getLabelVisibilityMode() {
        return this.menuView.getLabelVisibilityMode();
    }

    public int getMaxItemCount() {
        return 5;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public BadgeDrawable getOrCreateBadge(int n) {
        return this.menuView.getOrCreateBadge(n);
    }

    public int getSelectedItemId() {
        return this.menuView.getSelectedItemId();
    }

    public void inflateMenu(int n) {
        this.presenter.setUpdateSuspended(true);
        this.getMenuInflater().inflate(n, (Menu)this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(true);
    }

    public boolean isItemHorizontalTranslationEnabled() {
        return this.menuView.isItemHorizontalTranslationEnabled();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation((View)this);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.menu.restorePresenterStates(parcelable.menuPresenterState);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuPresenterState = new Bundle();
        this.menu.savePresenterStates(savedState.menuPresenterState);
        return savedState;
    }

    public void removeBadge(int n) {
        this.menuView.removeBadge(n);
    }

    public void setElevation(float f) {
        super.setElevation(f);
        MaterialShapeUtils.setElevation((View)this, f);
    }

    public void setItemBackground(Drawable drawable2) {
        this.menuView.setItemBackground(drawable2);
        this.itemRippleColor = null;
    }

    public void setItemBackgroundResource(int n) {
        this.menuView.setItemBackgroundRes(n);
        this.itemRippleColor = null;
    }

    public void setItemHorizontalTranslationEnabled(boolean bl) {
        if (this.menuView.isItemHorizontalTranslationEnabled() != bl) {
            this.menuView.setItemHorizontalTranslationEnabled(bl);
            this.presenter.updateMenuView(false);
        }
    }

    public void setItemIconSize(int n) {
        this.menuView.setItemIconSize(n);
    }

    public void setItemIconSizeRes(int n) {
        this.setItemIconSize(this.getResources().getDimensionPixelSize(n));
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.menuView.setIconTintList(colorStateList);
    }

    public void setItemRippleColor(ColorStateList colorStateList) {
        if (this.itemRippleColor == colorStateList) {
            if (colorStateList == null && this.menuView.getItemBackground() != null) {
                this.menuView.setItemBackground(null);
            }
            return;
        }
        this.itemRippleColor = colorStateList;
        if (colorStateList == null) {
            this.menuView.setItemBackground(null);
            return;
        }
        colorStateList = RippleUtils.convertToRippleDrawableColor(colorStateList);
        if (Build.VERSION.SDK_INT >= 21) {
            this.menuView.setItemBackground((Drawable)new RippleDrawable(colorStateList, null, null));
            return;
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(1.0E-5f);
        gradientDrawable = DrawableCompat.wrap((Drawable)gradientDrawable);
        DrawableCompat.setTintList((Drawable)gradientDrawable, colorStateList);
        this.menuView.setItemBackground((Drawable)gradientDrawable);
    }

    public void setItemTextAppearanceActive(int n) {
        this.menuView.setItemTextAppearanceActive(n);
    }

    public void setItemTextAppearanceInactive(int n) {
        this.menuView.setItemTextAppearanceInactive(n);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.menuView.setItemTextColor(colorStateList);
    }

    public void setLabelVisibilityMode(int n) {
        if (this.menuView.getLabelVisibilityMode() != n) {
            this.menuView.setLabelVisibilityMode(n);
            this.presenter.updateMenuView(false);
        }
    }

    public void setOnNavigationItemReselectedListener(OnNavigationItemReselectedListener onNavigationItemReselectedListener) {
        this.reselectedListener = onNavigationItemReselectedListener;
    }

    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.selectedListener = onNavigationItemSelectedListener;
    }

    public void setSelectedItemId(int n) {
        MenuItem menuItem = this.menu.findItem(n);
        if (menuItem != null && !this.menu.performItemAction(menuItem, this.presenter, 0)) {
            menuItem.setChecked(true);
        }
    }

    public static interface OnNavigationItemReselectedListener {
        public void onNavigationItemReselected(MenuItem var1);
    }

    public static interface OnNavigationItemSelectedListener {
        public boolean onNavigationItemSelected(MenuItem var1);
    }

    static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        Bundle menuPresenterState;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            ClassLoader classLoader2 = classLoader;
            if (classLoader == null) {
                classLoader2 = this.getClass().getClassLoader();
            }
            this.readFromParcel(parcel, classLoader2);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void readFromParcel(Parcel parcel, ClassLoader classLoader) {
            this.menuPresenterState = parcel.readBundle(classLoader);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeBundle(this.menuPresenterState);
        }

    }

}

