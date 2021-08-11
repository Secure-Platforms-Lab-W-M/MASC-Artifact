// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.view.menu;

import android.view.ViewGroup;
import android.view.View;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public class MenuAdapter extends BaseAdapter
{
    MenuBuilder mAdapterMenu;
    private int mExpandedIndex;
    private boolean mForceShowIcon;
    private final LayoutInflater mInflater;
    private final int mItemLayoutRes;
    private final boolean mOverflowOnly;
    
    public MenuAdapter(final MenuBuilder mAdapterMenu, final LayoutInflater mInflater, final boolean mOverflowOnly, final int mItemLayoutRes) {
        this.mExpandedIndex = -1;
        this.mOverflowOnly = mOverflowOnly;
        this.mInflater = mInflater;
        this.mAdapterMenu = mAdapterMenu;
        this.mItemLayoutRes = mItemLayoutRes;
        this.findExpandedIndex();
    }
    
    void findExpandedIndex() {
        final MenuItemImpl expandedItem = this.mAdapterMenu.getExpandedItem();
        if (expandedItem != null) {
            final ArrayList<MenuItemImpl> nonActionItems = this.mAdapterMenu.getNonActionItems();
            for (int size = nonActionItems.size(), i = 0; i < size; ++i) {
                if (nonActionItems.get(i) == expandedItem) {
                    this.mExpandedIndex = i;
                    return;
                }
            }
        }
        this.mExpandedIndex = -1;
    }
    
    public MenuBuilder getAdapterMenu() {
        return this.mAdapterMenu;
    }
    
    public int getCount() {
        ArrayList<MenuItemImpl> list;
        if (this.mOverflowOnly) {
            list = this.mAdapterMenu.getNonActionItems();
        }
        else {
            list = this.mAdapterMenu.getVisibleItems();
        }
        if (this.mExpandedIndex < 0) {
            return list.size();
        }
        return list.size() - 1;
    }
    
    public boolean getForceShowIcon() {
        return this.mForceShowIcon;
    }
    
    public MenuItemImpl getItem(final int n) {
        ArrayList<MenuItemImpl> list;
        if (this.mOverflowOnly) {
            list = this.mAdapterMenu.getNonActionItems();
        }
        else {
            list = this.mAdapterMenu.getVisibleItems();
        }
        final int mExpandedIndex = this.mExpandedIndex;
        int n2 = n;
        if (mExpandedIndex >= 0 && (n2 = n) >= mExpandedIndex) {
            n2 = n + 1;
        }
        return list.get(n2);
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        View inflate = view;
        if (view == null) {
            inflate = this.mInflater.inflate(this.mItemLayoutRes, viewGroup, false);
        }
        final int groupId = this.getItem(n).getGroupId();
        int groupId2;
        if (n - 1 >= 0) {
            groupId2 = this.getItem(n - 1).getGroupId();
        }
        else {
            groupId2 = groupId;
        }
        ((ListMenuItemView)inflate).setGroupDividerEnabled(this.mAdapterMenu.isGroupDividerEnabled() && groupId != groupId2);
        final ListMenuItemView listMenuItemView = (ListMenuItemView)inflate;
        if (this.mForceShowIcon) {
            ((ListMenuItemView)inflate).setForceShowIcon(true);
        }
        ((MenuView.ItemView)listMenuItemView).initialize(this.getItem(n), 0);
        return inflate;
    }
    
    public void notifyDataSetChanged() {
        this.findExpandedIndex();
        super.notifyDataSetChanged();
    }
    
    public void setForceShowIcon(final boolean mForceShowIcon) {
        this.mForceShowIcon = mForceShowIcon;
    }
}
