package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import androidx.appcompat.R.dimen;
import androidx.appcompat.R.layout;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.view.ViewCompat;

final class StandardMenuPopup extends MenuPopup implements OnDismissListener, OnItemClickListener, MenuPresenter, OnKeyListener {
   private static final int ITEM_LAYOUT;
   private final MenuAdapter mAdapter;
   private View mAnchorView;
   private final OnAttachStateChangeListener mAttachStateChangeListener = new OnAttachStateChangeListener() {
      public void onViewAttachedToWindow(View var1) {
      }

      public void onViewDetachedFromWindow(View var1) {
         if (StandardMenuPopup.this.mTreeObserver != null) {
            if (!StandardMenuPopup.this.mTreeObserver.isAlive()) {
               StandardMenuPopup.this.mTreeObserver = var1.getViewTreeObserver();
            }

            StandardMenuPopup.this.mTreeObserver.removeGlobalOnLayoutListener(StandardMenuPopup.this.mGlobalLayoutListener);
         }

         var1.removeOnAttachStateChangeListener(this);
      }
   };
   private int mContentWidth;
   private final Context mContext;
   private int mDropDownGravity = 0;
   final OnGlobalLayoutListener mGlobalLayoutListener = new OnGlobalLayoutListener() {
      public void onGlobalLayout() {
         if (StandardMenuPopup.this.isShowing() && !StandardMenuPopup.this.mPopup.isModal()) {
            View var1 = StandardMenuPopup.this.mShownAnchorView;
            if (var1 != null && var1.isShown()) {
               StandardMenuPopup.this.mPopup.show();
               return;
            }

            StandardMenuPopup.this.dismiss();
         }

      }
   };
   private boolean mHasContentWidth;
   private final MenuBuilder mMenu;
   private OnDismissListener mOnDismissListener;
   private final boolean mOverflowOnly;
   final MenuPopupWindow mPopup;
   private final int mPopupMaxWidth;
   private final int mPopupStyleAttr;
   private final int mPopupStyleRes;
   private MenuPresenter.Callback mPresenterCallback;
   private boolean mShowTitle;
   View mShownAnchorView;
   ViewTreeObserver mTreeObserver;
   private boolean mWasDismissed;

   static {
      ITEM_LAYOUT = layout.abc_popup_menu_item_layout;
   }

   public StandardMenuPopup(Context var1, MenuBuilder var2, View var3, int var4, int var5, boolean var6) {
      this.mContext = var1;
      this.mMenu = var2;
      this.mOverflowOnly = var6;
      this.mAdapter = new MenuAdapter(var2, LayoutInflater.from(var1), this.mOverflowOnly, ITEM_LAYOUT);
      this.mPopupStyleAttr = var4;
      this.mPopupStyleRes = var5;
      Resources var7 = var1.getResources();
      this.mPopupMaxWidth = Math.max(var7.getDisplayMetrics().widthPixels / 2, var7.getDimensionPixelSize(dimen.abc_config_prefDialogWidth));
      this.mAnchorView = var3;
      this.mPopup = new MenuPopupWindow(this.mContext, (AttributeSet)null, this.mPopupStyleAttr, this.mPopupStyleRes);
      var2.addMenuPresenter(this, var1);
   }

   private boolean tryShow() {
      if (this.isShowing()) {
         return true;
      } else if (!this.mWasDismissed) {
         View var2 = this.mAnchorView;
         if (var2 == null) {
            return false;
         } else {
            this.mShownAnchorView = var2;
            this.mPopup.setOnDismissListener(this);
            this.mPopup.setOnItemClickListener(this);
            this.mPopup.setModal(true);
            var2 = this.mShownAnchorView;
            boolean var1;
            if (this.mTreeObserver == null) {
               var1 = true;
            } else {
               var1 = false;
            }

            ViewTreeObserver var3 = var2.getViewTreeObserver();
            this.mTreeObserver = var3;
            if (var1) {
               var3.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }

            var2.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
            this.mPopup.setAnchorView(var2);
            this.mPopup.setDropDownGravity(this.mDropDownGravity);
            if (!this.mHasContentWidth) {
               this.mContentWidth = measureIndividualMenuWidth(this.mAdapter, (ViewGroup)null, this.mContext, this.mPopupMaxWidth);
               this.mHasContentWidth = true;
            }

            this.mPopup.setContentWidth(this.mContentWidth);
            this.mPopup.setInputMethodMode(2);
            this.mPopup.setEpicenterBounds(this.getEpicenterBounds());
            this.mPopup.show();
            ListView var5 = this.mPopup.getListView();
            var5.setOnKeyListener(this);
            if (this.mShowTitle && this.mMenu.getHeaderTitle() != null) {
               FrameLayout var6 = (FrameLayout)LayoutInflater.from(this.mContext).inflate(layout.abc_popup_menu_header_item_layout, var5, false);
               TextView var4 = (TextView)var6.findViewById(16908310);
               if (var4 != null) {
                  var4.setText(this.mMenu.getHeaderTitle());
               }

               var6.setEnabled(false);
               var5.addHeaderView(var6, (Object)null, false);
            }

            this.mPopup.setAdapter(this.mAdapter);
            this.mPopup.show();
            return true;
         }
      } else {
         return false;
      }
   }

   public void addMenu(MenuBuilder var1) {
   }

   public void dismiss() {
      if (this.isShowing()) {
         this.mPopup.dismiss();
      }

   }

   public boolean flagActionItems() {
      return false;
   }

   public ListView getListView() {
      return this.mPopup.getListView();
   }

   public boolean isShowing() {
      return !this.mWasDismissed && this.mPopup.isShowing();
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      if (var1 == this.mMenu) {
         this.dismiss();
         MenuPresenter.Callback var3 = this.mPresenterCallback;
         if (var3 != null) {
            var3.onCloseMenu(var1, var2);
         }

      }
   }

   public void onDismiss() {
      this.mWasDismissed = true;
      this.mMenu.close();
      ViewTreeObserver var1 = this.mTreeObserver;
      if (var1 != null) {
         if (!var1.isAlive()) {
            this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
         }

         this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
         this.mTreeObserver = null;
      }

      this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
      OnDismissListener var2 = this.mOnDismissListener;
      if (var2 != null) {
         var2.onDismiss();
      }

   }

   public boolean onKey(View var1, int var2, KeyEvent var3) {
      if (var3.getAction() == 1 && var2 == 82) {
         this.dismiss();
         return true;
      } else {
         return false;
      }
   }

   public void onRestoreInstanceState(Parcelable var1) {
   }

   public Parcelable onSaveInstanceState() {
      return null;
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      if (var1.hasVisibleItems()) {
         MenuPopupHelper var5 = new MenuPopupHelper(this.mContext, var1, this.mShownAnchorView, this.mOverflowOnly, this.mPopupStyleAttr, this.mPopupStyleRes);
         var5.setPresenterCallback(this.mPresenterCallback);
         var5.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(var1));
         var5.setOnDismissListener(this.mOnDismissListener);
         this.mOnDismissListener = null;
         this.mMenu.close(false);
         int var3 = this.mPopup.getHorizontalOffset();
         int var4 = this.mPopup.getVerticalOffset();
         int var2 = var3;
         if ((Gravity.getAbsoluteGravity(this.mDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView)) & 7) == 5) {
            var2 = var3 + this.mAnchorView.getWidth();
         }

         if (var5.tryShow(var2, var4)) {
            MenuPresenter.Callback var6 = this.mPresenterCallback;
            if (var6 != null) {
               var6.onOpenSubMenu(var1);
            }

            return true;
         }
      }

      return false;
   }

   public void setAnchorView(View var1) {
      this.mAnchorView = var1;
   }

   public void setCallback(MenuPresenter.Callback var1) {
      this.mPresenterCallback = var1;
   }

   public void setForceShowIcon(boolean var1) {
      this.mAdapter.setForceShowIcon(var1);
   }

   public void setGravity(int var1) {
      this.mDropDownGravity = var1;
   }

   public void setHorizontalOffset(int var1) {
      this.mPopup.setHorizontalOffset(var1);
   }

   public void setOnDismissListener(OnDismissListener var1) {
      this.mOnDismissListener = var1;
   }

   public void setShowTitle(boolean var1) {
      this.mShowTitle = var1;
   }

   public void setVerticalOffset(int var1) {
      this.mPopup.setVerticalOffset(var1);
   }

   public void show() {
      if (!this.tryShow()) {
         throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
      }
   }

   public void updateMenuView(boolean var1) {
      this.mHasContentWidth = false;
      MenuAdapter var2 = this.mAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

   }
}
