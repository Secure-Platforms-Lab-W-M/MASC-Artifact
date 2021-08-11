/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.design.R;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.widget.ThemeUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class NavigationView
extends ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final int[] DISABLED_STATE_SET = new int[]{-16842910};
    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
    OnNavigationItemSelectedListener mListener;
    private int mMaxWidth;
    private final NavigationMenu mMenu;
    private MenuInflater mMenuInflater;
    private final NavigationMenuPresenter mPresenter = new NavigationMenuPresenter();

    public NavigationView(Context context) {
        this(context, null);
    }

    public NavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NavigationView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        ThemeUtils.checkAppCompatTheme(context);
        this.mMenu = new NavigationMenu(context);
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.NavigationView, n, R.style.Widget_Design_NavigationView);
        ViewCompat.setBackground((View)this, tintTypedArray.getDrawable(R.styleable.NavigationView_android_background));
        if (tintTypedArray.hasValue(R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation((View)this, tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows((View)this, tintTypedArray.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.mMaxWidth = tintTypedArray.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
        ColorStateList colorStateList = tintTypedArray.hasValue(R.styleable.NavigationView_itemIconTint) ? tintTypedArray.getColorStateList(R.styleable.NavigationView_itemIconTint) : this.createDefaultColorStateList(16842808);
        n = 0;
        int n2 = 0;
        if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
            n2 = tintTypedArray.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
            n = 1;
        }
        attributeSet = null;
        if (tintTypedArray.hasValue(R.styleable.NavigationView_itemTextColor)) {
            attributeSet = tintTypedArray.getColorStateList(R.styleable.NavigationView_itemTextColor);
        }
        if (n == 0 && attributeSet == null) {
            attributeSet = this.createDefaultColorStateList(16842806);
        }
        Drawable drawable2 = tintTypedArray.getDrawable(R.styleable.NavigationView_itemBackground);
        this.mMenu.setCallback(new MenuBuilder.Callback(){

            @Override
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                if (NavigationView.this.mListener != null && NavigationView.this.mListener.onNavigationItemSelected(menuItem)) {
                    return true;
                }
                return false;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }
        });
        this.mPresenter.setId(1);
        this.mPresenter.initForMenu(context, this.mMenu);
        this.mPresenter.setItemIconTintList(colorStateList);
        if (n != 0) {
            this.mPresenter.setItemTextAppearance(n2);
        }
        this.mPresenter.setItemTextColor((ColorStateList)attributeSet);
        this.mPresenter.setItemBackground(drawable2);
        this.mMenu.addMenuPresenter(this.mPresenter);
        this.addView((View)this.mPresenter.getMenuView((ViewGroup)this));
        if (tintTypedArray.hasValue(R.styleable.NavigationView_menu)) {
            this.inflateMenu(tintTypedArray.getResourceId(R.styleable.NavigationView_menu, 0));
        }
        if (tintTypedArray.hasValue(R.styleable.NavigationView_headerLayout)) {
            this.inflateHeaderView(tintTypedArray.getResourceId(R.styleable.NavigationView_headerLayout, 0));
        }
        tintTypedArray.recycle();
    }

    private ColorStateList createDefaultColorStateList(int n) {
        int[] arrn = new int[]();
        if (!this.getContext().getTheme().resolveAttribute(n, (TypedValue)arrn, true)) {
            return null;
        }
        ColorStateList colorStateList = AppCompatResources.getColorStateList(this.getContext(), arrn.resourceId);
        if (!this.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, (TypedValue)arrn, true)) {
            return null;
        }
        n = arrn.data;
        int n2 = colorStateList.getDefaultColor();
        arrn = DISABLED_STATE_SET;
        int[] arrn2 = CHECKED_STATE_SET;
        int[] arrn3 = EMPTY_STATE_SET;
        int n3 = colorStateList.getColorForState(DISABLED_STATE_SET, n2);
        return new ColorStateList((int[][])new int[][]{arrn, arrn2, arrn3}, new int[]{n3, n, n2});
    }

    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(this.getContext());
        }
        return this.mMenuInflater;
    }

    public void addHeaderView(@NonNull View view) {
        this.mPresenter.addHeaderView(view);
    }

    public int getHeaderCount() {
        return this.mPresenter.getHeaderCount();
    }

    public View getHeaderView(int n) {
        return this.mPresenter.getHeaderView(n);
    }

    @Nullable
    public Drawable getItemBackground() {
        return this.mPresenter.getItemBackground();
    }

    @Nullable
    public ColorStateList getItemIconTintList() {
        return this.mPresenter.getItemTintList();
    }

    @Nullable
    public ColorStateList getItemTextColor() {
        return this.mPresenter.getItemTextColor();
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public View inflateHeaderView(@LayoutRes int n) {
        return this.mPresenter.inflateHeaderView(n);
    }

    public void inflateMenu(int n) {
        this.mPresenter.setUpdateSuspended(true);
        this.getMenuInflater().inflate(n, (Menu)this.mMenu);
        this.mPresenter.setUpdateSuspended(false);
        this.mPresenter.updateMenuView(false);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    protected void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
        this.mPresenter.dispatchApplyWindowInsets(windowInsetsCompat);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        if (n3 != Integer.MIN_VALUE) {
            if (n3 != 0) {
                if (n3 == 1073741824) {
                    // empty if block
                }
            } else {
                n = View.MeasureSpec.makeMeasureSpec((int)this.mMaxWidth, (int)1073741824);
            }
        } else {
            n = View.MeasureSpec.makeMeasureSpec((int)Math.min(View.MeasureSpec.getSize((int)n), this.mMaxWidth), (int)1073741824);
        }
        super.onMeasure(n, n2);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.mMenu.restorePresenterStates(parcelable.menuState);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.mMenu.savePresenterStates(savedState.menuState);
        return savedState;
    }

    public void removeHeaderView(@NonNull View view) {
        this.mPresenter.removeHeaderView(view);
    }

    public void setCheckedItem(@IdRes int n) {
        MenuItem menuItem = this.mMenu.findItem(n);
        if (menuItem != null) {
            this.mPresenter.setCheckedItem((MenuItemImpl)menuItem);
            return;
        }
    }

    public void setItemBackground(@Nullable Drawable drawable2) {
        this.mPresenter.setItemBackground(drawable2);
    }

    public void setItemBackgroundResource(@DrawableRes int n) {
        this.setItemBackground(ContextCompat.getDrawable(this.getContext(), n));
    }

    public void setItemIconTintList(@Nullable ColorStateList colorStateList) {
        this.mPresenter.setItemIconTintList(colorStateList);
    }

    public void setItemTextAppearance(@StyleRes int n) {
        this.mPresenter.setItemTextAppearance(n);
    }

    public void setItemTextColor(@Nullable ColorStateList colorStateList) {
        this.mPresenter.setItemTextColor(colorStateList);
    }

    public void setNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.mListener = onNavigationItemSelectedListener;
    }

    public static interface OnNavigationItemSelectedListener {
        public boolean onNavigationItemSelected(@NonNull MenuItem var1);
    }

    public static class SavedState
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
        public Bundle menuState;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.menuState = parcel.readBundle(classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeBundle(this.menuState);
        }

    }

}

