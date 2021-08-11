// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.view.menu;

import android.widget.PopupWindow$OnDismissListener;
import android.widget.AdapterView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.widget.HeaderViewListAdapter;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.view.View$MeasureSpec;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.graphics.Rect;
import android.widget.AdapterView$OnItemClickListener;

abstract class MenuPopup implements ShowableListMenu, MenuPresenter, AdapterView$OnItemClickListener
{
    private Rect mEpicenterBounds;
    
    protected static int measureIndividualMenuWidth(final ListAdapter listAdapter, final ViewGroup viewGroup, final Context context, final int n) {
        int n2 = 0;
        final View view = null;
        int n3 = 0;
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
        final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(0, 0);
        final int count = listAdapter.getCount();
        int i = 0;
        Object o = viewGroup;
        View view2 = view;
        while (i < count) {
            final int itemViewType = listAdapter.getItemViewType(i);
            if (itemViewType != n3) {
                n3 = itemViewType;
                view2 = null;
            }
            if (o == null) {
                o = new FrameLayout(context);
            }
            view2 = listAdapter.getView(i, view2, (ViewGroup)o);
            view2.measure(measureSpec, measureSpec2);
            final int measuredWidth = view2.getMeasuredWidth();
            if (measuredWidth >= n) {
                return n;
            }
            if (measuredWidth > n2) {
                n2 = measuredWidth;
            }
            ++i;
        }
        return n2;
    }
    
    protected static boolean shouldPreserveIconSpacing(final MenuBuilder menuBuilder) {
        for (int size = menuBuilder.size(), i = 0; i < size; ++i) {
            final MenuItem item = menuBuilder.getItem(i);
            if (item.isVisible() && item.getIcon() != null) {
                return true;
            }
        }
        return false;
    }
    
    protected static MenuAdapter toMenuAdapter(final ListAdapter listAdapter) {
        if (listAdapter instanceof HeaderViewListAdapter) {
            return (MenuAdapter)((HeaderViewListAdapter)listAdapter).getWrappedAdapter();
        }
        return (MenuAdapter)listAdapter;
    }
    
    public abstract void addMenu(final MenuBuilder p0);
    
    protected boolean closeMenuOnSubMenuOpened() {
        return true;
    }
    
    @Override
    public boolean collapseItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
        return false;
    }
    
    @Override
    public boolean expandItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
        return false;
    }
    
    public Rect getEpicenterBounds() {
        return this.mEpicenterBounds;
    }
    
    @Override
    public int getId() {
        return 0;
    }
    
    @Override
    public MenuView getMenuView(final ViewGroup viewGroup) {
        throw new UnsupportedOperationException("MenuPopups manage their own views");
    }
    
    @Override
    public void initForMenu(@NonNull final Context context, @Nullable final MenuBuilder menuBuilder) {
    }
    
    public void onItemClick(final AdapterView<?> adapterView, final View view, int n, final long n2) {
        final ListAdapter listAdapter = (ListAdapter)adapterView.getAdapter();
        final MenuBuilder mAdapterMenu = toMenuAdapter(listAdapter).mAdapterMenu;
        final MenuItem menuItem = (MenuItem)listAdapter.getItem(n);
        if (this.closeMenuOnSubMenuOpened()) {
            n = 0;
        }
        else {
            n = 4;
        }
        mAdapterMenu.performItemAction(menuItem, this, n);
    }
    
    public abstract void setAnchorView(final View p0);
    
    public void setEpicenterBounds(final Rect mEpicenterBounds) {
        this.mEpicenterBounds = mEpicenterBounds;
    }
    
    public abstract void setForceShowIcon(final boolean p0);
    
    public abstract void setGravity(final int p0);
    
    public abstract void setHorizontalOffset(final int p0);
    
    public abstract void setOnDismissListener(final PopupWindow$OnDismissListener p0);
    
    public abstract void setShowTitle(final boolean p0);
    
    public abstract void setVerticalOffset(final int p0);
}
