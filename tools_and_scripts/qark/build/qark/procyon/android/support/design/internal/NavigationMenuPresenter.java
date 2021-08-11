// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.internal;

import android.widget.TextView;
import android.view.SubMenu;
import java.util.ArrayList;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.SubMenuBuilder;
import android.os.Build$VERSION;
import android.util.SparseArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.design.R;
import android.support.v7.view.menu.MenuView;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.MenuItem;
import android.view.View;
import android.view.View$OnClickListener;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.widget.LinearLayout;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuPresenter;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class NavigationMenuPresenter implements MenuPresenter
{
    private static final String STATE_ADAPTER = "android:menu:adapter";
    private static final String STATE_HEADER = "android:menu:header";
    private static final String STATE_HIERARCHY = "android:menu:list";
    NavigationMenuAdapter mAdapter;
    private Callback mCallback;
    LinearLayout mHeaderLayout;
    ColorStateList mIconTintList;
    private int mId;
    Drawable mItemBackground;
    LayoutInflater mLayoutInflater;
    MenuBuilder mMenu;
    private NavigationMenuView mMenuView;
    final View$OnClickListener mOnClickListener;
    int mPaddingSeparator;
    private int mPaddingTopDefault;
    int mTextAppearance;
    boolean mTextAppearanceSet;
    ColorStateList mTextColor;
    
    public NavigationMenuPresenter() {
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)view;
                NavigationMenuPresenter.this.setUpdateSuspended(true);
                final MenuItemImpl itemData = navigationMenuItemView.getItemData();
                final boolean performItemAction = NavigationMenuPresenter.this.mMenu.performItemAction((MenuItem)itemData, NavigationMenuPresenter.this, 0);
                if (itemData != null && itemData.isCheckable() && performItemAction) {
                    NavigationMenuPresenter.this.mAdapter.setCheckedItem(itemData);
                }
                NavigationMenuPresenter.this.setUpdateSuspended(false);
                NavigationMenuPresenter.this.updateMenuView(false);
            }
        };
    }
    
    public void addHeaderView(@NonNull final View view) {
        this.mHeaderLayout.addView(view);
        final NavigationMenuView mMenuView = this.mMenuView;
        mMenuView.setPadding(0, 0, 0, mMenuView.getPaddingBottom());
    }
    
    @Override
    public boolean collapseItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
        return false;
    }
    
    public void dispatchApplyWindowInsets(final WindowInsetsCompat windowInsetsCompat) {
        final int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
        if (this.mPaddingTopDefault != systemWindowInsetTop) {
            this.mPaddingTopDefault = systemWindowInsetTop;
            if (this.mHeaderLayout.getChildCount() == 0) {
                final NavigationMenuView mMenuView = this.mMenuView;
                mMenuView.setPadding(0, this.mPaddingTopDefault, 0, mMenuView.getPaddingBottom());
            }
        }
        ViewCompat.dispatchApplyWindowInsets((View)this.mHeaderLayout, windowInsetsCompat);
    }
    
    @Override
    public boolean expandItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
        return false;
    }
    
    @Override
    public boolean flagActionItems() {
        return false;
    }
    
    public int getHeaderCount() {
        return this.mHeaderLayout.getChildCount();
    }
    
    public View getHeaderView(final int n) {
        return this.mHeaderLayout.getChildAt(n);
    }
    
    @Override
    public int getId() {
        return this.mId;
    }
    
    @Nullable
    public Drawable getItemBackground() {
        return this.mItemBackground;
    }
    
    @Nullable
    public ColorStateList getItemTextColor() {
        return this.mTextColor;
    }
    
    @Nullable
    public ColorStateList getItemTintList() {
        return this.mIconTintList;
    }
    
    @Override
    public MenuView getMenuView(final ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            this.mMenuView = (NavigationMenuView)this.mLayoutInflater.inflate(R.layout.design_navigation_menu, viewGroup, false);
            if (this.mAdapter == null) {
                this.mAdapter = new NavigationMenuAdapter();
            }
            this.mHeaderLayout = (LinearLayout)this.mLayoutInflater.inflate(R.layout.design_navigation_item_header, (ViewGroup)this.mMenuView, false);
            this.mMenuView.setAdapter((RecyclerView.Adapter)this.mAdapter);
        }
        return this.mMenuView;
    }
    
    public View inflateHeaderView(@LayoutRes final int n) {
        final View inflate = this.mLayoutInflater.inflate(n, (ViewGroup)this.mHeaderLayout, false);
        this.addHeaderView(inflate);
        return inflate;
    }
    
    @Override
    public void initForMenu(final Context context, final MenuBuilder mMenu) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mMenu = mMenu;
        this.mPaddingSeparator = context.getResources().getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
    }
    
    @Override
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        final Callback mCallback = this.mCallback;
        if (mCallback != null) {
            mCallback.onCloseMenu(menuBuilder, b);
        }
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof Bundle)) {
            return;
        }
        final Bundle bundle = (Bundle)parcelable;
        final SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
        if (sparseParcelableArray != null) {
            this.mMenuView.restoreHierarchyState(sparseParcelableArray);
        }
        final Bundle bundle2 = bundle.getBundle("android:menu:adapter");
        if (bundle2 != null) {
            this.mAdapter.restoreInstanceState(bundle2);
        }
        final SparseArray sparseParcelableArray2 = bundle.getSparseParcelableArray("android:menu:header");
        if (sparseParcelableArray2 != null) {
            this.mHeaderLayout.restoreHierarchyState(sparseParcelableArray2);
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        if (Build$VERSION.SDK_INT < 11) {
            return null;
        }
        final Bundle bundle = new Bundle();
        if (this.mMenuView != null) {
            final SparseArray sparseArray = new SparseArray();
            this.mMenuView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        final NavigationMenuAdapter mAdapter = this.mAdapter;
        if (mAdapter != null) {
            bundle.putBundle("android:menu:adapter", mAdapter.createInstanceState());
        }
        if (this.mHeaderLayout != null) {
            final SparseArray sparseArray2 = new SparseArray();
            this.mHeaderLayout.saveHierarchyState(sparseArray2);
            bundle.putSparseParcelableArray("android:menu:header", sparseArray2);
            return (Parcelable)bundle;
        }
        return (Parcelable)bundle;
    }
    
    @Override
    public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
        return false;
    }
    
    public void removeHeaderView(@NonNull final View view) {
        this.mHeaderLayout.removeView(view);
        if (this.mHeaderLayout.getChildCount() == 0) {
            final NavigationMenuView mMenuView = this.mMenuView;
            mMenuView.setPadding(0, this.mPaddingTopDefault, 0, mMenuView.getPaddingBottom());
        }
    }
    
    @Override
    public void setCallback(final Callback mCallback) {
        this.mCallback = mCallback;
    }
    
    public void setCheckedItem(final MenuItemImpl checkedItem) {
        this.mAdapter.setCheckedItem(checkedItem);
    }
    
    public void setId(final int mId) {
        this.mId = mId;
    }
    
    public void setItemBackground(@Nullable final Drawable mItemBackground) {
        this.mItemBackground = mItemBackground;
        this.updateMenuView(false);
    }
    
    public void setItemIconTintList(@Nullable final ColorStateList mIconTintList) {
        this.mIconTintList = mIconTintList;
        this.updateMenuView(false);
    }
    
    public void setItemTextAppearance(@StyleRes final int mTextAppearance) {
        this.mTextAppearance = mTextAppearance;
        this.mTextAppearanceSet = true;
        this.updateMenuView(false);
    }
    
    public void setItemTextColor(@Nullable final ColorStateList mTextColor) {
        this.mTextColor = mTextColor;
        this.updateMenuView(false);
    }
    
    public void setUpdateSuspended(final boolean updateSuspended) {
        final NavigationMenuAdapter mAdapter = this.mAdapter;
        if (mAdapter != null) {
            mAdapter.setUpdateSuspended(updateSuspended);
        }
    }
    
    @Override
    public void updateMenuView(final boolean b) {
        final NavigationMenuAdapter mAdapter = this.mAdapter;
        if (mAdapter != null) {
            mAdapter.update();
        }
    }
    
    private static class HeaderViewHolder extends ViewHolder
    {
        public HeaderViewHolder(final View view) {
            super(view);
        }
    }
    
    private class NavigationMenuAdapter extends Adapter<NavigationMenuPresenter.ViewHolder>
    {
        private static final String STATE_ACTION_VIEWS = "android:menu:action_views";
        private static final String STATE_CHECKED_ITEM = "android:menu:checked";
        private static final int VIEW_TYPE_HEADER = 3;
        private static final int VIEW_TYPE_NORMAL = 0;
        private static final int VIEW_TYPE_SEPARATOR = 2;
        private static final int VIEW_TYPE_SUBHEADER = 1;
        private MenuItemImpl mCheckedItem;
        private final ArrayList<NavigationMenuItem> mItems;
        private boolean mUpdateSuspended;
        
        NavigationMenuAdapter() {
            this.mItems = new ArrayList<NavigationMenuItem>();
            this.prepareMenuItems();
        }
        
        private void appendTransparentIconIfMissing(int i, final int n) {
            while (i < n) {
                this.mItems.get(i).needsEmptyIcon = true;
                ++i;
            }
        }
        
        private void prepareMenuItems() {
            if (this.mUpdateSuspended) {
                return;
            }
            this.mUpdateSuspended = true;
            this.mItems.clear();
            this.mItems.add(new NavigationMenuHeaderItem());
            int n = -1;
            int size = 0;
            boolean needsEmptyIcon = false;
            int n2 = 0;
            final int size2 = NavigationMenuPresenter.this.mMenu.getVisibleItems().size();
            while (true) {
                final boolean b = false;
                if (n2 >= size2) {
                    break;
                }
                final MenuItemImpl menuItemImpl = NavigationMenuPresenter.this.mMenu.getVisibleItems().get(n2);
                if (menuItemImpl.isChecked()) {
                    this.setCheckedItem(menuItemImpl);
                }
                if (menuItemImpl.isCheckable()) {
                    menuItemImpl.setExclusiveCheckable(false);
                }
                if (menuItemImpl.hasSubMenu()) {
                    final SubMenu subMenu = menuItemImpl.getSubMenu();
                    if (subMenu.hasVisibleItems()) {
                        if (n2 != 0) {
                            this.mItems.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, 0));
                        }
                        this.mItems.add(new NavigationMenuTextItem(menuItemImpl));
                        int n3 = 0;
                        final int size3 = this.mItems.size();
                        for (int i = 0; i < subMenu.size(); ++i) {
                            final MenuItemImpl menuItemImpl2 = (MenuItemImpl)subMenu.getItem(i);
                            if (menuItemImpl2.isVisible()) {
                                if (n3 == 0 && menuItemImpl2.getIcon() != null) {
                                    n3 = 1;
                                }
                                if (menuItemImpl2.isCheckable()) {
                                    menuItemImpl2.setExclusiveCheckable(false);
                                }
                                if (menuItemImpl.isChecked()) {
                                    this.setCheckedItem(menuItemImpl);
                                }
                                this.mItems.add(new NavigationMenuTextItem(menuItemImpl2));
                            }
                        }
                        if (n3 != 0) {
                            this.appendTransparentIconIfMissing(size3, this.mItems.size());
                        }
                    }
                }
                else {
                    final int groupId = menuItemImpl.getGroupId();
                    if (groupId != n) {
                        size = this.mItems.size();
                        needsEmptyIcon = b;
                        if (menuItemImpl.getIcon() != null) {
                            needsEmptyIcon = true;
                        }
                        if (n2 != 0) {
                            ++size;
                            this.mItems.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.mPaddingSeparator, NavigationMenuPresenter.this.mPaddingSeparator));
                        }
                    }
                    else if (!needsEmptyIcon && menuItemImpl.getIcon() != null) {
                        needsEmptyIcon = true;
                        this.appendTransparentIconIfMissing(size, this.mItems.size());
                    }
                    final NavigationMenuTextItem navigationMenuTextItem = new NavigationMenuTextItem(menuItemImpl);
                    navigationMenuTextItem.needsEmptyIcon = needsEmptyIcon;
                    this.mItems.add(navigationMenuTextItem);
                    n = groupId;
                }
                ++n2;
            }
            this.mUpdateSuspended = false;
        }
        
        public Bundle createInstanceState() {
            final Bundle bundle = new Bundle();
            final MenuItemImpl mCheckedItem = this.mCheckedItem;
            if (mCheckedItem != null) {
                bundle.putInt("android:menu:checked", mCheckedItem.getItemId());
            }
            final SparseArray sparseArray = new SparseArray();
            for (int i = 0; i < this.mItems.size(); ++i) {
                final NavigationMenuItem navigationMenuItem = this.mItems.get(i);
                if (navigationMenuItem instanceof NavigationMenuTextItem) {
                    final MenuItemImpl menuItem = ((NavigationMenuTextItem)navigationMenuItem).getMenuItem();
                    View actionView;
                    if (menuItem != null) {
                        actionView = menuItem.getActionView();
                    }
                    else {
                        actionView = null;
                    }
                    if (actionView != null) {
                        final ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
                        actionView.saveHierarchyState((SparseArray)parcelableSparseArray);
                        sparseArray.put(menuItem.getItemId(), (Object)parcelableSparseArray);
                    }
                }
            }
            bundle.putSparseParcelableArray("android:menu:action_views", sparseArray);
            return bundle;
        }
        
        @Override
        public int getItemCount() {
            return this.mItems.size();
        }
        
        @Override
        public long getItemId(final int n) {
            return n;
        }
        
        @Override
        public int getItemViewType(final int n) {
            final NavigationMenuItem navigationMenuItem = this.mItems.get(n);
            if (navigationMenuItem instanceof NavigationMenuSeparatorItem) {
                return 2;
            }
            if (navigationMenuItem instanceof NavigationMenuHeaderItem) {
                return 3;
            }
            if (!(navigationMenuItem instanceof NavigationMenuTextItem)) {
                throw new RuntimeException("Unknown item type.");
            }
            if (((NavigationMenuTextItem)navigationMenuItem).getMenuItem().hasSubMenu()) {
                return 1;
            }
            return 0;
        }
        
        public void onBindViewHolder(final NavigationMenuPresenter.ViewHolder viewHolder, final int n) {
            switch (this.getItemViewType(n)) {
                default: {}
                case 2: {
                    final NavigationMenuSeparatorItem navigationMenuSeparatorItem = this.mItems.get(n);
                    viewHolder.itemView.setPadding(0, navigationMenuSeparatorItem.getPaddingTop(), 0, navigationMenuSeparatorItem.getPaddingBottom());
                }
                case 1: {
                    ((TextView)viewHolder.itemView).setText(((NavigationMenuTextItem)this.mItems.get(n)).getMenuItem().getTitle());
                }
                case 0: {
                    final NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView)viewHolder.itemView;
                    navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.mIconTintList);
                    if (NavigationMenuPresenter.this.mTextAppearanceSet) {
                        navigationMenuItemView.setTextAppearance(NavigationMenuPresenter.this.mTextAppearance);
                    }
                    if (NavigationMenuPresenter.this.mTextColor != null) {
                        navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.mTextColor);
                    }
                    Drawable drawable;
                    if (NavigationMenuPresenter.this.mItemBackground != null) {
                        drawable = NavigationMenuPresenter.this.mItemBackground.getConstantState().newDrawable();
                    }
                    else {
                        drawable = null;
                    }
                    ViewCompat.setBackground((View)navigationMenuItemView, drawable);
                    final NavigationMenuTextItem navigationMenuTextItem = this.mItems.get(n);
                    navigationMenuItemView.setNeedsEmptyIcon(navigationMenuTextItem.needsEmptyIcon);
                    navigationMenuItemView.initialize(navigationMenuTextItem.getMenuItem(), 0);
                }
            }
        }
        
        public NavigationMenuPresenter.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            switch (n) {
                default: {
                    return null;
                }
                case 3: {
                    return new HeaderViewHolder((View)NavigationMenuPresenter.this.mHeaderLayout);
                }
                case 2: {
                    return new SeparatorViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup);
                }
                case 1: {
                    return new SubheaderViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup);
                }
                case 0: {
                    return new NormalViewHolder(NavigationMenuPresenter.this.mLayoutInflater, viewGroup, NavigationMenuPresenter.this.mOnClickListener);
                }
            }
        }
        
        public void onViewRecycled(final NavigationMenuPresenter.ViewHolder viewHolder) {
            if (viewHolder instanceof NormalViewHolder) {
                ((NavigationMenuItemView)viewHolder.itemView).recycle();
            }
        }
        
        public void restoreInstanceState(final Bundle bundle) {
            final int int1 = bundle.getInt("android:menu:checked", 0);
            if (int1 != 0) {
                this.mUpdateSuspended = true;
                for (int i = 0; i < this.mItems.size(); ++i) {
                    final NavigationMenuItem navigationMenuItem = this.mItems.get(i);
                    if (navigationMenuItem instanceof NavigationMenuTextItem) {
                        final MenuItemImpl menuItem = ((NavigationMenuTextItem)navigationMenuItem).getMenuItem();
                        if (menuItem != null && menuItem.getItemId() == int1) {
                            this.setCheckedItem(menuItem);
                            break;
                        }
                    }
                }
                this.mUpdateSuspended = false;
                this.prepareMenuItems();
            }
            final SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:action_views");
            if (sparseParcelableArray != null) {
                for (int j = 0; j < this.mItems.size(); ++j) {
                    final NavigationMenuItem navigationMenuItem2 = this.mItems.get(j);
                    if (navigationMenuItem2 instanceof NavigationMenuTextItem) {
                        final MenuItemImpl menuItem2 = ((NavigationMenuTextItem)navigationMenuItem2).getMenuItem();
                        if (menuItem2 != null) {
                            final View actionView = menuItem2.getActionView();
                            if (actionView != null) {
                                final ParcelableSparseArray parcelableSparseArray = (ParcelableSparseArray)sparseParcelableArray.get(menuItem2.getItemId());
                                if (parcelableSparseArray != null) {
                                    actionView.restoreHierarchyState((SparseArray)parcelableSparseArray);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        public void setCheckedItem(final MenuItemImpl mCheckedItem) {
            if (this.mCheckedItem == mCheckedItem) {
                return;
            }
            if (!mCheckedItem.isCheckable()) {
                return;
            }
            final MenuItemImpl mCheckedItem2 = this.mCheckedItem;
            if (mCheckedItem2 != null) {
                mCheckedItem2.setChecked(false);
            }
            (this.mCheckedItem = mCheckedItem).setChecked(true);
        }
        
        public void setUpdateSuspended(final boolean mUpdateSuspended) {
            this.mUpdateSuspended = mUpdateSuspended;
        }
        
        public void update() {
            this.prepareMenuItems();
            ((RecyclerView.Adapter)this).notifyDataSetChanged();
        }
    }
    
    private static class NavigationMenuHeaderItem implements NavigationMenuItem
    {
        NavigationMenuHeaderItem() {
        }
    }
    
    private interface NavigationMenuItem
    {
    }
    
    private static class NavigationMenuSeparatorItem implements NavigationMenuItem
    {
        private final int mPaddingBottom;
        private final int mPaddingTop;
        
        public NavigationMenuSeparatorItem(final int mPaddingTop, final int mPaddingBottom) {
            this.mPaddingTop = mPaddingTop;
            this.mPaddingBottom = mPaddingBottom;
        }
        
        public int getPaddingBottom() {
            return this.mPaddingBottom;
        }
        
        public int getPaddingTop() {
            return this.mPaddingTop;
        }
    }
    
    private static class NavigationMenuTextItem implements NavigationMenuItem
    {
        private final MenuItemImpl mMenuItem;
        boolean needsEmptyIcon;
        
        NavigationMenuTextItem(final MenuItemImpl mMenuItem) {
            this.mMenuItem = mMenuItem;
        }
        
        public MenuItemImpl getMenuItem() {
            return this.mMenuItem;
        }
    }
    
    private static class NormalViewHolder extends ViewHolder
    {
        public NormalViewHolder(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final View$OnClickListener onClickListener) {
            super(layoutInflater.inflate(R.layout.design_navigation_item, viewGroup, false));
            this.itemView.setOnClickListener(onClickListener);
        }
    }
    
    private static class SeparatorViewHolder extends ViewHolder
    {
        public SeparatorViewHolder(final LayoutInflater layoutInflater, final ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_separator, viewGroup, false));
        }
    }
    
    private static class SubheaderViewHolder extends ViewHolder
    {
        public SubheaderViewHolder(final LayoutInflater layoutInflater, final ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.design_navigation_item_subheader, viewGroup, false));
        }
    }
    
    private abstract static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(final View view) {
            super(view);
        }
    }
}
