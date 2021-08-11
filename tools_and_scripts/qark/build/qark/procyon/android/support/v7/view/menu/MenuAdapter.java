// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.view.menu;

import android.view.ViewGroup;
import android.view.View;
import java.util.ArrayList;
import android.support.v7.appcompat.R;
import android.view.LayoutInflater;
import android.support.annotation.RestrictTo;
import android.widget.BaseAdapter;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class MenuAdapter extends BaseAdapter
{
    static final int ITEM_LAYOUT;
    MenuBuilder mAdapterMenu;
    private int mExpandedIndex;
    private boolean mForceShowIcon;
    private final LayoutInflater mInflater;
    private final boolean mOverflowOnly;
    
    static {
        ITEM_LAYOUT = R.layout.abc_popup_menu_item_layout;
    }
    
    public MenuAdapter(final MenuBuilder mAdapterMenu, final LayoutInflater mInflater, final boolean mOverflowOnly) {
        this.mExpandedIndex = -1;
        this.mOverflowOnly = mOverflowOnly;
        this.mInflater = mInflater;
        this.mAdapterMenu = mAdapterMenu;
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
    
    public MenuItemImpl getItem(int n) {
        ArrayList<MenuItemImpl> list;
        if (this.mOverflowOnly) {
            list = this.mAdapterMenu.getNonActionItems();
        }
        else {
            list = this.mAdapterMenu.getVisibleItems();
        }
        final int mExpandedIndex = this.mExpandedIndex;
        if (mExpandedIndex >= 0 && n >= mExpandedIndex) {
            ++n;
        }
        return list.get(n);
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        if (inflate == null) {
            inflate = this.mInflater.inflate(MenuAdapter.ITEM_LAYOUT, viewGroup, false);
        }
        final MenuView.ItemView itemView = (MenuView.ItemView)inflate;
        if (this.mForceShowIcon) {
            ((ListMenuItemView)inflate).setForceShowIcon(true);
        }
        itemView.initialize(this.getItem(n), 0);
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
