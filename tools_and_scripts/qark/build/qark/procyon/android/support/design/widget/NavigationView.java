// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.annotation.DrawableRes;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.support.annotation.RestrictTo;
import android.support.v4.view.WindowInsetsCompat;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.content.res.AppCompatResources;
import android.util.TypedValue;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.view.ViewGroup;
import android.support.v7.view.menu.MenuPresenter;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuBuilder;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.TintTypedArray;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.design.internal.NavigationMenuPresenter;
import android.view.MenuInflater;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.ScrimInsetsFrameLayout;

public class NavigationView extends ScrimInsetsFrameLayout
{
    private static final int[] CHECKED_STATE_SET;
    private static final int[] DISABLED_STATE_SET;
    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
    OnNavigationItemSelectedListener mListener;
    private int mMaxWidth;
    private final NavigationMenu mMenu;
    private MenuInflater mMenuInflater;
    private final NavigationMenuPresenter mPresenter;
    
    static {
        CHECKED_STATE_SET = new int[] { 16842912 };
        DISABLED_STATE_SET = new int[] { -16842910 };
    }
    
    public NavigationView(final Context context) {
        this(context, null);
    }
    
    public NavigationView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public NavigationView(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mPresenter = new NavigationMenuPresenter();
        ThemeUtils.checkAppCompatTheme(context);
        this.mMenu = new NavigationMenu(context);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.NavigationView, n, R.style.Widget_Design_NavigationView);
        ViewCompat.setBackground((View)this, obtainStyledAttributes.getDrawable(R.styleable.NavigationView_android_background));
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation((View)this, (float)obtainStyledAttributes.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows((View)this, obtainStyledAttributes.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
        ColorStateList itemIconTintList;
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_itemIconTint)) {
            itemIconTintList = obtainStyledAttributes.getColorStateList(R.styleable.NavigationView_itemIconTint);
        }
        else {
            itemIconTintList = this.createDefaultColorStateList(16842808);
        }
        n = 0;
        int resourceId = 0;
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_itemTextAppearance)) {
            resourceId = obtainStyledAttributes.getResourceId(R.styleable.NavigationView_itemTextAppearance, 0);
            n = 1;
        }
        ColorStateList itemTextColor = null;
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_itemTextColor)) {
            itemTextColor = obtainStyledAttributes.getColorStateList(R.styleable.NavigationView_itemTextColor);
        }
        if (n == 0 && itemTextColor == null) {
            itemTextColor = this.createDefaultColorStateList(16842806);
        }
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.NavigationView_itemBackground);
        this.mMenu.setCallback((MenuBuilder.Callback)new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
                return NavigationView.this.mListener != null && NavigationView.this.mListener.onNavigationItemSelected(menuItem);
            }
            
            @Override
            public void onMenuModeChange(final MenuBuilder menuBuilder) {
            }
        });
        this.mPresenter.setId(1);
        this.mPresenter.initForMenu(context, this.mMenu);
        this.mPresenter.setItemIconTintList(itemIconTintList);
        if (n != 0) {
            this.mPresenter.setItemTextAppearance(resourceId);
        }
        this.mPresenter.setItemTextColor(itemTextColor);
        this.mPresenter.setItemBackground(drawable);
        this.mMenu.addMenuPresenter(this.mPresenter);
        this.addView((View)this.mPresenter.getMenuView((ViewGroup)this));
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_menu)) {
            this.inflateMenu(obtainStyledAttributes.getResourceId(R.styleable.NavigationView_menu, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.NavigationView_headerLayout)) {
            this.inflateHeaderView(obtainStyledAttributes.getResourceId(R.styleable.NavigationView_headerLayout, 0));
        }
        obtainStyledAttributes.recycle();
    }
    
    private ColorStateList createDefaultColorStateList(int data) {
        final TypedValue typedValue = new TypedValue();
        if (!this.getContext().getTheme().resolveAttribute(data, typedValue, true)) {
            return null;
        }
        final ColorStateList colorStateList = AppCompatResources.getColorStateList(this.getContext(), typedValue.resourceId);
        if (!this.getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorPrimary, typedValue, true)) {
            return null;
        }
        data = typedValue.data;
        final int defaultColor = colorStateList.getDefaultColor();
        return new ColorStateList(new int[][] { NavigationView.DISABLED_STATE_SET, NavigationView.CHECKED_STATE_SET, NavigationView.EMPTY_STATE_SET }, new int[] { colorStateList.getColorForState(NavigationView.DISABLED_STATE_SET, defaultColor), data, defaultColor });
    }
    
    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(this.getContext());
        }
        return this.mMenuInflater;
    }
    
    public void addHeaderView(@NonNull final View view) {
        this.mPresenter.addHeaderView(view);
    }
    
    public int getHeaderCount() {
        return this.mPresenter.getHeaderCount();
    }
    
    public View getHeaderView(final int n) {
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
        return (Menu)this.mMenu;
    }
    
    public View inflateHeaderView(@LayoutRes final int n) {
        return this.mPresenter.inflateHeaderView(n);
    }
    
    public void inflateMenu(final int n) {
        this.mPresenter.setUpdateSuspended(true);
        this.getMenuInflater().inflate(n, (Menu)this.mMenu);
        this.mPresenter.setUpdateSuspended(false);
        this.mPresenter.updateMenuView(false);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    protected void onInsetsChanged(final WindowInsetsCompat windowInsetsCompat) {
        this.mPresenter.dispatchApplyWindowInsets(windowInsetsCompat);
    }
    
    protected void onMeasure(int n, final int n2) {
        final int mode = View$MeasureSpec.getMode(n);
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0) {
                if (mode != 1073741824) {}
            }
            else {
                n = View$MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
            }
        }
        else {
            n = View$MeasureSpec.makeMeasureSpec(Math.min(View$MeasureSpec.getSize(n), this.mMaxWidth), 1073741824);
        }
        super.onMeasure(n, n2);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mMenu.restorePresenterStates(savedState.menuState);
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.mMenu.savePresenterStates(savedState.menuState);
        return (Parcelable)savedState;
    }
    
    public void removeHeaderView(@NonNull final View view) {
        this.mPresenter.removeHeaderView(view);
    }
    
    public void setCheckedItem(@IdRes final int n) {
        final MenuItem item = this.mMenu.findItem(n);
        if (item != null) {
            this.mPresenter.setCheckedItem((MenuItemImpl)item);
        }
    }
    
    public void setItemBackground(@Nullable final Drawable itemBackground) {
        this.mPresenter.setItemBackground(itemBackground);
    }
    
    public void setItemBackgroundResource(@DrawableRes final int n) {
        this.setItemBackground(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setItemIconTintList(@Nullable final ColorStateList itemIconTintList) {
        this.mPresenter.setItemIconTintList(itemIconTintList);
    }
    
    public void setItemTextAppearance(@StyleRes final int itemTextAppearance) {
        this.mPresenter.setItemTextAppearance(itemTextAppearance);
    }
    
    public void setItemTextColor(@Nullable final ColorStateList itemTextColor) {
        this.mPresenter.setItemTextColor(itemTextColor);
    }
    
    public void setNavigationItemSelectedListener(@Nullable final OnNavigationItemSelectedListener mListener) {
        this.mListener = mListener;
    }
    
    public interface OnNavigationItemSelectedListener
    {
        boolean onNavigationItemSelected(@NonNull final MenuItem p0);
    }
    
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        public Bundle menuState;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.menuState = parcel.readBundle(classLoader);
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public void writeToParcel(@NonNull final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeBundle(this.menuState);
        }
    }
}
