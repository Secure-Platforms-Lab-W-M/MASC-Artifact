// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.widget.ListView;
import androidx.appcompat.view.SupportMenuInflater;
import android.view.MenuInflater;
import android.view.Menu;
import androidx.appcompat.view.menu.ShowableListMenu;
import android.widget.PopupWindow$OnDismissListener;
import android.view.MenuItem;
import androidx.appcompat.R$attr;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuBuilder;
import android.view.View$OnTouchListener;
import android.content.Context;
import android.view.View;

public class PopupMenu
{
    private final View mAnchor;
    private final Context mContext;
    private View$OnTouchListener mDragListener;
    private final MenuBuilder mMenu;
    OnMenuItemClickListener mMenuItemClickListener;
    OnDismissListener mOnDismissListener;
    final MenuPopupHelper mPopup;
    
    public PopupMenu(final Context context, final View view) {
        this(context, view, 0);
    }
    
    public PopupMenu(final Context context, final View view, final int n) {
        this(context, view, n, R$attr.popupMenuStyle, 0);
    }
    
    public PopupMenu(final Context mContext, final View mAnchor, final int gravity, final int n, final int n2) {
        this.mContext = mContext;
        this.mAnchor = mAnchor;
        (this.mMenu = new MenuBuilder(mContext)).setCallback((MenuBuilder.Callback)new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
                return PopupMenu.this.mMenuItemClickListener != null && PopupMenu.this.mMenuItemClickListener.onMenuItemClick(menuItem);
            }
            
            @Override
            public void onMenuModeChange(final MenuBuilder menuBuilder) {
            }
        });
        (this.mPopup = new MenuPopupHelper(mContext, this.mMenu, mAnchor, false, n, n2)).setGravity(gravity);
        this.mPopup.setOnDismissListener((PopupWindow$OnDismissListener)new PopupWindow$OnDismissListener() {
            public void onDismiss() {
                if (PopupMenu.this.mOnDismissListener != null) {
                    PopupMenu.this.mOnDismissListener.onDismiss(PopupMenu.this);
                }
            }
        });
    }
    
    public void dismiss() {
        this.mPopup.dismiss();
    }
    
    public View$OnTouchListener getDragToOpenListener() {
        if (this.mDragListener == null) {
            this.mDragListener = (View$OnTouchListener)new ForwardingListener(this.mAnchor) {
                @Override
                public ShowableListMenu getPopup() {
                    return PopupMenu.this.mPopup.getPopup();
                }
                
                @Override
                protected boolean onForwardingStarted() {
                    PopupMenu.this.show();
                    return true;
                }
                
                @Override
                protected boolean onForwardingStopped() {
                    PopupMenu.this.dismiss();
                    return true;
                }
            };
        }
        return this.mDragListener;
    }
    
    public int getGravity() {
        return this.mPopup.getGravity();
    }
    
    public Menu getMenu() {
        return (Menu)this.mMenu;
    }
    
    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.mContext);
    }
    
    ListView getMenuListView() {
        if (!this.mPopup.isShowing()) {
            return null;
        }
        return this.mPopup.getListView();
    }
    
    public void inflate(final int n) {
        this.getMenuInflater().inflate(n, (Menu)this.mMenu);
    }
    
    public void setGravity(final int gravity) {
        this.mPopup.setGravity(gravity);
    }
    
    public void setOnDismissListener(final OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }
    
    public void show() {
        this.mPopup.show();
    }
    
    public interface OnDismissListener
    {
        void onDismiss(final PopupMenu p0);
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
}
