package androidx.appcompat.widget;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import androidx.appcompat.R.attr;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.ShowableListMenu;

public class PopupMenu {
   private final View mAnchor;
   private final Context mContext;
   private OnTouchListener mDragListener;
   private final MenuBuilder mMenu;
   PopupMenu.OnMenuItemClickListener mMenuItemClickListener;
   PopupMenu.OnDismissListener mOnDismissListener;
   final MenuPopupHelper mPopup;

   public PopupMenu(Context var1, View var2) {
      this(var1, var2, 0);
   }

   public PopupMenu(Context var1, View var2, int var3) {
      this(var1, var2, var3, attr.popupMenuStyle, 0);
   }

   public PopupMenu(Context var1, View var2, int var3, int var4, int var5) {
      this.mContext = var1;
      this.mAnchor = var2;
      MenuBuilder var6 = new MenuBuilder(var1);
      this.mMenu = var6;
      var6.setCallback(new MenuBuilder.Callback() {
         public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
            return PopupMenu.this.mMenuItemClickListener != null ? PopupMenu.this.mMenuItemClickListener.onMenuItemClick(var2) : false;
         }

         public void onMenuModeChange(MenuBuilder var1) {
         }
      });
      MenuPopupHelper var7 = new MenuPopupHelper(var1, this.mMenu, var2, false, var4, var5);
      this.mPopup = var7;
      var7.setGravity(var3);
      this.mPopup.setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
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

   public OnTouchListener getDragToOpenListener() {
      if (this.mDragListener == null) {
         this.mDragListener = new ForwardingListener(this.mAnchor) {
            public ShowableListMenu getPopup() {
               return PopupMenu.this.mPopup.getPopup();
            }

            protected boolean onForwardingStarted() {
               PopupMenu.this.show();
               return true;
            }

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
      return this.mMenu;
   }

   public MenuInflater getMenuInflater() {
      return new SupportMenuInflater(this.mContext);
   }

   ListView getMenuListView() {
      return !this.mPopup.isShowing() ? null : this.mPopup.getListView();
   }

   public void inflate(int var1) {
      this.getMenuInflater().inflate(var1, this.mMenu);
   }

   public void setGravity(int var1) {
      this.mPopup.setGravity(var1);
   }

   public void setOnDismissListener(PopupMenu.OnDismissListener var1) {
      this.mOnDismissListener = var1;
   }

   public void setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener var1) {
      this.mMenuItemClickListener = var1;
   }

   public void show() {
      this.mPopup.show();
   }

   public interface OnDismissListener {
      void onDismiss(PopupMenu var1);
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }
}
