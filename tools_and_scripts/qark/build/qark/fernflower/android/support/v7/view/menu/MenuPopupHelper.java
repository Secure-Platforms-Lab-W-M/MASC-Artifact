package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$dimen;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MenuPopupHelper implements MenuHelper {
   private static final int TOUCH_EPICENTER_SIZE_DP = 48;
   private View mAnchorView;
   private final Context mContext;
   private int mDropDownGravity;
   private boolean mForceShowIcon;
   private final OnDismissListener mInternalOnDismissListener;
   private final MenuBuilder mMenu;
   private OnDismissListener mOnDismissListener;
   private final boolean mOverflowOnly;
   private MenuPopup mPopup;
   private final int mPopupStyleAttr;
   private final int mPopupStyleRes;
   private MenuPresenter.Callback mPresenterCallback;

   public MenuPopupHelper(@NonNull Context var1, @NonNull MenuBuilder var2) {
      this(var1, var2, (View)null, false, R$attr.popupMenuStyle, 0);
   }

   public MenuPopupHelper(@NonNull Context var1, @NonNull MenuBuilder var2, @NonNull View var3) {
      this(var1, var2, var3, false, R$attr.popupMenuStyle, 0);
   }

   public MenuPopupHelper(@NonNull Context var1, @NonNull MenuBuilder var2, @NonNull View var3, boolean var4, @AttrRes int var5) {
      this(var1, var2, var3, var4, var5, 0);
   }

   public MenuPopupHelper(@NonNull Context var1, @NonNull MenuBuilder var2, @NonNull View var3, boolean var4, @AttrRes int var5, @StyleRes int var6) {
      this.mDropDownGravity = 8388611;
      this.mInternalOnDismissListener = new OnDismissListener() {
         public void onDismiss() {
            MenuPopupHelper.this.onDismiss();
         }
      };
      this.mContext = var1;
      this.mMenu = var2;
      this.mAnchorView = var3;
      this.mOverflowOnly = var4;
      this.mPopupStyleAttr = var5;
      this.mPopupStyleRes = var6;
   }

   @NonNull
   private MenuPopup createPopup() {
      Display var2 = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
      Point var3 = new Point();
      if (VERSION.SDK_INT >= 17) {
         var2.getRealSize(var3);
      } else {
         var2.getSize(var3);
      }

      boolean var1;
      if (Math.min(var3.x, var3.y) >= this.mContext.getResources().getDimensionPixelSize(R$dimen.abc_cascading_menus_min_smallest_width)) {
         var1 = true;
      } else {
         var1 = false;
      }

      Object var4;
      if (var1) {
         var4 = new CascadingMenuPopup(this.mContext, this.mAnchorView, this.mPopupStyleAttr, this.mPopupStyleRes, this.mOverflowOnly);
      } else {
         var4 = new StandardMenuPopup(this.mContext, this.mMenu, this.mAnchorView, this.mPopupStyleAttr, this.mPopupStyleRes, this.mOverflowOnly);
      }

      ((MenuPopup)var4).addMenu(this.mMenu);
      ((MenuPopup)var4).setOnDismissListener(this.mInternalOnDismissListener);
      ((MenuPopup)var4).setAnchorView(this.mAnchorView);
      ((MenuPopup)var4).setCallback(this.mPresenterCallback);
      ((MenuPopup)var4).setForceShowIcon(this.mForceShowIcon);
      ((MenuPopup)var4).setGravity(this.mDropDownGravity);
      return (MenuPopup)var4;
   }

   private void showPopup(int var1, int var2, boolean var3, boolean var4) {
      MenuPopup var6 = this.getPopup();
      var6.setShowTitle(var4);
      if (var3) {
         int var5 = var1;
         if ((GravityCompat.getAbsoluteGravity(this.mDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView)) & 7) == 5) {
            var5 = var1 + this.mAnchorView.getWidth();
         }

         var6.setHorizontalOffset(var5);
         var6.setVerticalOffset(var2);
         var1 = (int)(48.0F * this.mContext.getResources().getDisplayMetrics().density / 2.0F);
         var6.setEpicenterBounds(new Rect(var5 - var1, var2 - var1, var5 + var1, var2 + var1));
      }

      var6.show();
   }

   public void dismiss() {
      if (this.isShowing()) {
         this.mPopup.dismiss();
      }

   }

   public int getGravity() {
      return this.mDropDownGravity;
   }

   @NonNull
   public MenuPopup getPopup() {
      if (this.mPopup == null) {
         this.mPopup = this.createPopup();
      }

      return this.mPopup;
   }

   public boolean isShowing() {
      MenuPopup var1 = this.mPopup;
      return var1 != null && var1.isShowing();
   }

   protected void onDismiss() {
      this.mPopup = null;
      OnDismissListener var1 = this.mOnDismissListener;
      if (var1 != null) {
         var1.onDismiss();
      }

   }

   public void setAnchorView(@NonNull View var1) {
      this.mAnchorView = var1;
   }

   public void setForceShowIcon(boolean var1) {
      this.mForceShowIcon = var1;
      MenuPopup var2 = this.mPopup;
      if (var2 != null) {
         var2.setForceShowIcon(var1);
      }

   }

   public void setGravity(int var1) {
      this.mDropDownGravity = var1;
   }

   public void setOnDismissListener(@Nullable OnDismissListener var1) {
      this.mOnDismissListener = var1;
   }

   public void setPresenterCallback(@Nullable MenuPresenter.Callback var1) {
      this.mPresenterCallback = var1;
      MenuPopup var2 = this.mPopup;
      if (var2 != null) {
         var2.setCallback(var1);
      }

   }

   public void show() {
      if (!this.tryShow()) {
         throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
      }
   }

   public void show(int var1, int var2) {
      if (!this.tryShow(var1, var2)) {
         throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
      }
   }

   public boolean tryShow() {
      if (this.isShowing()) {
         return true;
      } else if (this.mAnchorView == null) {
         return false;
      } else {
         this.showPopup(0, 0, false, false);
         return true;
      }
   }

   public boolean tryShow(int var1, int var2) {
      if (this.isShowing()) {
         return true;
      } else if (this.mAnchorView == null) {
         return false;
      } else {
         this.showPopup(var1, var2, true, true);
         return true;
      }
   }
}
