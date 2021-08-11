package android.support.v7.view.menu;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.appcompat.R$layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.KeyEvent.DispatcherState;
import android.view.WindowManager.LayoutParams;

class MenuDialogHelper implements OnKeyListener, OnClickListener, OnDismissListener, MenuPresenter.Callback {
   private AlertDialog mDialog;
   private MenuBuilder mMenu;
   ListMenuPresenter mPresenter;
   private MenuPresenter.Callback mPresenterCallback;

   public MenuDialogHelper(MenuBuilder var1) {
      this.mMenu = var1;
   }

   public void dismiss() {
      AlertDialog var1 = this.mDialog;
      if (var1 != null) {
         var1.dismiss();
      }

   }

   public void onClick(DialogInterface var1, int var2) {
      this.mMenu.performItemAction((MenuItemImpl)this.mPresenter.getAdapter().getItem(var2), 0);
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      if (var2 || var1 == this.mMenu) {
         this.dismiss();
      }

      MenuPresenter.Callback var3 = this.mPresenterCallback;
      if (var3 != null) {
         var3.onCloseMenu(var1, var2);
      }

   }

   public void onDismiss(DialogInterface var1) {
      this.mPresenter.onCloseMenu(this.mMenu, true);
   }

   public boolean onKey(DialogInterface var1, int var2, KeyEvent var3) {
      if (var2 == 82 || var2 == 4) {
         if (var3.getAction() == 0 && var3.getRepeatCount() == 0) {
            Window var5 = this.mDialog.getWindow();
            if (var5 != null) {
               View var6 = var5.getDecorView();
               if (var6 != null) {
                  DispatcherState var7 = var6.getKeyDispatcherState();
                  if (var7 != null) {
                     var7.startTracking(var3, this);
                     return true;
                  }
               }
            }
         } else if (var3.getAction() == 1 && !var3.isCanceled()) {
            Window var4 = this.mDialog.getWindow();
            if (var4 != null) {
               View var8 = var4.getDecorView();
               if (var8 != null) {
                  DispatcherState var9 = var8.getKeyDispatcherState();
                  if (var9 != null && var9.isTracking(var3)) {
                     this.mMenu.close(true);
                     var1.dismiss();
                     return true;
                  }
               }
            }
         }
      }

      return this.mMenu.performShortcut(var2, var3, 0);
   }

   public boolean onOpenSubMenu(MenuBuilder var1) {
      MenuPresenter.Callback var2 = this.mPresenterCallback;
      return var2 != null ? var2.onOpenSubMenu(var1) : false;
   }

   public void setPresenterCallback(MenuPresenter.Callback var1) {
      this.mPresenterCallback = var1;
   }

   public void show(IBinder var1) {
      MenuBuilder var2 = this.mMenu;
      AlertDialog.Builder var3 = new AlertDialog.Builder(var2.getContext());
      this.mPresenter = new ListMenuPresenter(var3.getContext(), R$layout.abc_list_menu_item_layout);
      this.mPresenter.setCallback(this);
      this.mMenu.addMenuPresenter(this.mPresenter);
      var3.setAdapter(this.mPresenter.getAdapter(), this);
      View var4 = var2.getHeaderView();
      if (var4 != null) {
         var3.setCustomTitle(var4);
      } else {
         var3.setIcon(var2.getHeaderIcon()).setTitle(var2.getHeaderTitle());
      }

      var3.setOnKeyListener(this);
      this.mDialog = var3.create();
      this.mDialog.setOnDismissListener(this);
      LayoutParams var5 = this.mDialog.getWindow().getAttributes();
      var5.type = 1003;
      if (var1 != null) {
         var5.token = var1;
      }

      var5.flags |= 131072;
      this.mDialog.show();
   }
}
