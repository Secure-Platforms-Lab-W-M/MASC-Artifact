package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R$dimen;
import android.support.v7.appcompat.R$layout;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.MenuPopupWindow;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

final class CascadingMenuPopup extends MenuPopup implements MenuPresenter, OnKeyListener, OnDismissListener {
   static final int HORIZ_POSITION_LEFT = 0;
   static final int HORIZ_POSITION_RIGHT = 1;
   static final int SUBMENU_TIMEOUT_MS = 200;
   private View mAnchorView;
   private final OnAttachStateChangeListener mAttachStateChangeListener = new OnAttachStateChangeListener() {
      public void onViewAttachedToWindow(View var1) {
      }

      public void onViewDetachedFromWindow(View var1) {
         if (CascadingMenuPopup.this.mTreeObserver != null) {
            if (!CascadingMenuPopup.this.mTreeObserver.isAlive()) {
               CascadingMenuPopup.this.mTreeObserver = var1.getViewTreeObserver();
            }

            CascadingMenuPopup.this.mTreeObserver.removeGlobalOnLayoutListener(CascadingMenuPopup.this.mGlobalLayoutListener);
         }

         var1.removeOnAttachStateChangeListener(this);
      }
   };
   private final Context mContext;
   private int mDropDownGravity = 0;
   private boolean mForceShowIcon;
   private final OnGlobalLayoutListener mGlobalLayoutListener = new OnGlobalLayoutListener() {
      public void onGlobalLayout() {
         if (CascadingMenuPopup.this.isShowing() && CascadingMenuPopup.this.mShowingMenus.size() > 0 && !((CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(0)).window.isModal()) {
            View var1 = CascadingMenuPopup.this.mShownAnchorView;
            if (var1 != null && var1.isShown()) {
               Iterator var2 = CascadingMenuPopup.this.mShowingMenus.iterator();

               while(var2.hasNext()) {
                  ((CascadingMenuPopup.CascadingMenuInfo)var2.next()).window.show();
               }
            } else {
               CascadingMenuPopup.this.dismiss();
            }
         }

      }
   };
   private boolean mHasXOffset;
   private boolean mHasYOffset;
   private int mLastPosition;
   private final MenuItemHoverListener mMenuItemHoverListener = new MenuItemHoverListener() {
      public void onItemHoverEnter(@NonNull final MenuBuilder var1, @NonNull final MenuItem var2) {
         CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages((Object)null);
         byte var5 = -1;
         int var3 = 0;
         int var6 = CascadingMenuPopup.this.mShowingMenus.size();

         int var4;
         while(true) {
            var4 = var5;
            if (var3 >= var6) {
               break;
            }

            if (var1 == ((CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(var3)).menu) {
               var4 = var3;
               break;
            }

            ++var3;
         }

         if (var4 != -1) {
            var3 = var4 + 1;
            final CascadingMenuPopup.CascadingMenuInfo var9;
            if (var3 < CascadingMenuPopup.this.mShowingMenus.size()) {
               var9 = (CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(var3);
            } else {
               var9 = null;
            }

            Runnable var10 = new Runnable() {
               public void run() {
                  if (var9 != null) {
                     CascadingMenuPopup.this.mShouldCloseImmediately = true;
                     var9.menu.close(false);
                     CascadingMenuPopup.this.mShouldCloseImmediately = false;
                  }

                  if (var2.isEnabled() && var2.hasSubMenu()) {
                     var1.performItemAction(var2, 4);
                  }

               }
            };
            long var7 = SystemClock.uptimeMillis();
            CascadingMenuPopup.this.mSubMenuHoverHandler.postAtTime(var10, var1, var7 + 200L);
         }
      }

      public void onItemHoverExit(@NonNull MenuBuilder var1, @NonNull MenuItem var2) {
         CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(var1);
      }
   };
   private final int mMenuMaxWidth;
   private OnDismissListener mOnDismissListener;
   private final boolean mOverflowOnly;
   private final List mPendingMenus = new LinkedList();
   private final int mPopupStyleAttr;
   private final int mPopupStyleRes;
   private MenuPresenter.Callback mPresenterCallback;
   private int mRawDropDownGravity = 0;
   boolean mShouldCloseImmediately;
   private boolean mShowTitle;
   final List mShowingMenus = new ArrayList();
   View mShownAnchorView;
   final Handler mSubMenuHoverHandler;
   private ViewTreeObserver mTreeObserver;
   private int mXOffset;
   private int mYOffset;

   public CascadingMenuPopup(@NonNull Context var1, @NonNull View var2, @AttrRes int var3, @StyleRes int var4, boolean var5) {
      this.mContext = var1;
      this.mAnchorView = var2;
      this.mPopupStyleAttr = var3;
      this.mPopupStyleRes = var4;
      this.mOverflowOnly = var5;
      this.mForceShowIcon = false;
      this.mLastPosition = this.getInitialMenuPosition();
      Resources var6 = var1.getResources();
      this.mMenuMaxWidth = Math.max(var6.getDisplayMetrics().widthPixels / 2, var6.getDimensionPixelSize(R$dimen.abc_config_prefDialogWidth));
      this.mSubMenuHoverHandler = new Handler();
   }

   private MenuPopupWindow createPopupWindow() {
      MenuPopupWindow var1 = new MenuPopupWindow(this.mContext, (AttributeSet)null, this.mPopupStyleAttr, this.mPopupStyleRes);
      var1.setHoverListener(this.mMenuItemHoverListener);
      var1.setOnItemClickListener(this);
      var1.setOnDismissListener(this);
      var1.setAnchorView(this.mAnchorView);
      var1.setDropDownGravity(this.mDropDownGravity);
      var1.setModal(true);
      var1.setInputMethodMode(2);
      return var1;
   }

   private int findIndexOfAddedMenu(@NonNull MenuBuilder var1) {
      int var2 = 0;

      for(int var3 = this.mShowingMenus.size(); var2 < var3; ++var2) {
         if (var1 == ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var2)).menu) {
            return var2;
         }
      }

      return -1;
   }

   private MenuItem findMenuItemForSubmenu(@NonNull MenuBuilder var1, @NonNull MenuBuilder var2) {
      int var3 = 0;

      for(int var4 = var1.size(); var3 < var4; ++var3) {
         MenuItem var5 = var1.getItem(var3);
         if (var5.hasSubMenu() && var2 == var5.getSubMenu()) {
            return var5;
         }
      }

      return null;
   }

   @Nullable
   private View findParentViewForSubmenu(@NonNull CascadingMenuPopup.CascadingMenuInfo var1, @NonNull MenuBuilder var2) {
      MenuItem var12 = this.findMenuItemForSubmenu(var1.menu, var2);
      if (var12 == null) {
         return null;
      } else {
         ListView var8 = var1.getListView();
         ListAdapter var9 = var8.getAdapter();
         int var4;
         MenuAdapter var11;
         if (var9 instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter var10 = (HeaderViewListAdapter)var9;
            var4 = var10.getHeadersCount();
            var11 = (MenuAdapter)var10.getWrappedAdapter();
         } else {
            var4 = 0;
            var11 = (MenuAdapter)var9;
         }

         byte var6 = -1;
         int var3 = 0;
         int var7 = var11.getCount();

         int var5;
         while(true) {
            var5 = var6;
            if (var3 >= var7) {
               break;
            }

            if (var12 == var11.getItem(var3)) {
               var5 = var3;
               break;
            }

            ++var3;
         }

         if (var5 == -1) {
            return null;
         } else {
            var3 = var5 + var4 - var8.getFirstVisiblePosition();
            if (var3 >= 0) {
               return var3 >= var8.getChildCount() ? null : var8.getChildAt(var3);
            } else {
               return null;
            }
         }
      }
   }

   private int getInitialMenuPosition() {
      int var2 = ViewCompat.getLayoutDirection(this.mAnchorView);
      byte var1 = 1;
      if (var2 == 1) {
         var1 = 0;
      }

      return var1;
   }

   private int getNextMenuPosition(int var1) {
      List var2 = this.mShowingMenus;
      ListView var5 = ((CascadingMenuPopup.CascadingMenuInfo)var2.get(var2.size() - 1)).getListView();
      int[] var3 = new int[2];
      var5.getLocationOnScreen(var3);
      Rect var4 = new Rect();
      this.mShownAnchorView.getWindowVisibleDisplayFrame(var4);
      if (this.mLastPosition == 1) {
         return var3[0] + var5.getWidth() + var1 > var4.right ? 0 : 1;
      } else {
         return var3[0] - var1 < 0 ? 1 : 0;
      }
   }

   private void showMenu(@NonNull MenuBuilder var1) {
      LayoutInflater var9 = LayoutInflater.from(this.mContext);
      MenuAdapter var6 = new MenuAdapter(var1, var9, this.mOverflowOnly);
      if (!this.isShowing() && this.mForceShowIcon) {
         var6.setForceShowIcon(true);
      } else if (this.isShowing()) {
         var6.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(var1));
      }

      int var5 = measureIndividualMenuWidth(var6, (ViewGroup)null, this.mContext, this.mMenuMaxWidth);
      MenuPopupWindow var8 = this.createPopupWindow();
      var8.setAdapter(var6);
      var8.setContentWidth(var5);
      var8.setDropDownGravity(this.mDropDownGravity);
      View var7;
      CascadingMenuPopup.CascadingMenuInfo var14;
      if (this.mShowingMenus.size() > 0) {
         List var13 = this.mShowingMenus;
         var14 = (CascadingMenuPopup.CascadingMenuInfo)var13.get(var13.size() - 1);
         var7 = this.findParentViewForSubmenu(var14, var1);
      } else {
         var14 = null;
         var7 = null;
      }

      if (var7 != null) {
         var8.setTouchModal(false);
         var8.setEnterTransition((Object)null);
         int var3 = this.getNextMenuPosition(var5);
         boolean var2;
         if (var3 == 1) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.mLastPosition = var3;
         int var4;
         if (VERSION.SDK_INT >= 26) {
            var8.setAnchorView(var7);
            var3 = 0;
            var4 = 0;
         } else {
            int[] var10 = new int[2];
            this.mAnchorView.getLocationOnScreen(var10);
            int[] var11 = new int[2];
            var7.getLocationOnScreen(var11);
            var4 = var11[0] - var10[0];
            var3 = var11[1] - var10[1];
         }

         int var12;
         if ((this.mDropDownGravity & 5) == 5) {
            if (var2) {
               var12 = var4 + var5;
            } else {
               var12 = var4 - var7.getWidth();
            }
         } else if (var2) {
            var12 = var7.getWidth() + var4;
         } else {
            var12 = var4 - var5;
         }

         var8.setHorizontalOffset(var12);
         var8.setOverlapAnchor(true);
         var8.setVerticalOffset(var3);
      } else {
         if (this.mHasXOffset) {
            var8.setHorizontalOffset(this.mXOffset);
         }

         if (this.mHasYOffset) {
            var8.setVerticalOffset(this.mYOffset);
         }

         var8.setEpicenterBounds(this.getEpicenterBounds());
      }

      CascadingMenuPopup.CascadingMenuInfo var16 = new CascadingMenuPopup.CascadingMenuInfo(var8, var1, this.mLastPosition);
      this.mShowingMenus.add(var16);
      var8.show();
      ListView var17 = var8.getListView();
      var17.setOnKeyListener(this);
      if (var14 == null && this.mShowTitle && var1.getHeaderTitle() != null) {
         FrameLayout var15 = (FrameLayout)var9.inflate(R$layout.abc_popup_menu_header_item_layout, var17, false);
         TextView var18 = (TextView)var15.findViewById(16908310);
         var15.setEnabled(false);
         var18.setText(var1.getHeaderTitle());
         var17.addHeaderView(var15, (Object)null, false);
         var8.show();
      }

   }

   public void addMenu(MenuBuilder var1) {
      var1.addMenuPresenter(this, this.mContext);
      if (this.isShowing()) {
         this.showMenu(var1);
      } else {
         this.mPendingMenus.add(var1);
      }
   }

   protected boolean closeMenuOnSubMenuOpened() {
      return false;
   }

   public void dismiss() {
      int var1 = this.mShowingMenus.size();
      if (var1 > 0) {
         CascadingMenuPopup.CascadingMenuInfo[] var2 = (CascadingMenuPopup.CascadingMenuInfo[])this.mShowingMenus.toArray(new CascadingMenuPopup.CascadingMenuInfo[var1]);
         --var1;

         for(; var1 >= 0; --var1) {
            CascadingMenuPopup.CascadingMenuInfo var3 = var2[var1];
            if (var3.window.isShowing()) {
               var3.window.dismiss();
            }
         }
      }

   }

   public boolean flagActionItems() {
      return false;
   }

   public ListView getListView() {
      if (this.mShowingMenus.isEmpty()) {
         return null;
      } else {
         List var1 = this.mShowingMenus;
         return ((CascadingMenuPopup.CascadingMenuInfo)var1.get(var1.size() - 1)).getListView();
      }
   }

   public boolean isShowing() {
      int var1 = this.mShowingMenus.size();
      boolean var3 = false;
      boolean var2 = var3;
      if (var1 > 0) {
         var2 = var3;
         if (((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(0)).window.isShowing()) {
            var2 = true;
         }
      }

      return var2;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      int var3 = this.findIndexOfAddedMenu(var1);
      if (var3 >= 0) {
         int var4 = var3 + 1;
         if (var4 < this.mShowingMenus.size()) {
            ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var4)).menu.close(false);
         }

         CascadingMenuPopup.CascadingMenuInfo var5 = (CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.remove(var3);
         var5.menu.removeMenuPresenter(this);
         if (this.mShouldCloseImmediately) {
            var5.window.setExitTransition((Object)null);
            var5.window.setAnimationStyle(0);
         }

         var5.window.dismiss();
         var3 = this.mShowingMenus.size();
         if (var3 > 0) {
            this.mLastPosition = ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var3 - 1)).position;
         } else {
            this.mLastPosition = this.getInitialMenuPosition();
         }

         if (var3 == 0) {
            this.dismiss();
            MenuPresenter.Callback var7 = this.mPresenterCallback;
            if (var7 != null) {
               var7.onCloseMenu(var1, true);
            }

            ViewTreeObserver var6 = this.mTreeObserver;
            if (var6 != null) {
               if (var6.isAlive()) {
                  this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
               }

               this.mTreeObserver = null;
            }

            this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
            this.mOnDismissListener.onDismiss();
         } else {
            if (var2) {
               ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(0)).menu.close(false);
            }

         }
      }
   }

   public void onDismiss() {
      Object var4 = null;
      int var1 = 0;
      int var2 = this.mShowingMenus.size();

      CascadingMenuPopup.CascadingMenuInfo var3;
      while(true) {
         var3 = (CascadingMenuPopup.CascadingMenuInfo)var4;
         if (var1 >= var2) {
            break;
         }

         var3 = (CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var1);
         if (!var3.window.isShowing()) {
            break;
         }

         ++var1;
      }

      if (var3 != null) {
         var3.menu.close(false);
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
      Iterator var2 = this.mShowingMenus.iterator();

      CascadingMenuPopup.CascadingMenuInfo var3;
      do {
         if (!var2.hasNext()) {
            if (var1.hasVisibleItems()) {
               this.addMenu(var1);
               MenuPresenter.Callback var4 = this.mPresenterCallback;
               if (var4 != null) {
                  var4.onOpenSubMenu(var1);
               }

               return true;
            }

            return false;
         }

         var3 = (CascadingMenuPopup.CascadingMenuInfo)var2.next();
      } while(var1 != var3.menu);

      var3.getListView().requestFocus();
      return true;
   }

   public void setAnchorView(@NonNull View var1) {
      if (this.mAnchorView != var1) {
         this.mAnchorView = var1;
         this.mDropDownGravity = GravityCompat.getAbsoluteGravity(this.mRawDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView));
      }

   }

   public void setCallback(MenuPresenter.Callback var1) {
      this.mPresenterCallback = var1;
   }

   public void setForceShowIcon(boolean var1) {
      this.mForceShowIcon = var1;
   }

   public void setGravity(int var1) {
      if (this.mRawDropDownGravity != var1) {
         this.mRawDropDownGravity = var1;
         this.mDropDownGravity = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this.mAnchorView));
      }

   }

   public void setHorizontalOffset(int var1) {
      this.mHasXOffset = true;
      this.mXOffset = var1;
   }

   public void setOnDismissListener(OnDismissListener var1) {
      this.mOnDismissListener = var1;
   }

   public void setShowTitle(boolean var1) {
      this.mShowTitle = var1;
   }

   public void setVerticalOffset(int var1) {
      this.mHasYOffset = true;
      this.mYOffset = var1;
   }

   public void show() {
      if (!this.isShowing()) {
         Iterator var2 = this.mPendingMenus.iterator();

         while(var2.hasNext()) {
            this.showMenu((MenuBuilder)var2.next());
         }

         this.mPendingMenus.clear();
         this.mShownAnchorView = this.mAnchorView;
         if (this.mShownAnchorView != null) {
            boolean var1;
            if (this.mTreeObserver == null) {
               var1 = true;
            } else {
               var1 = false;
            }

            this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
            if (var1) {
               this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }

            this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
         }

      }
   }

   public void updateMenuView(boolean var1) {
      Iterator var2 = this.mShowingMenus.iterator();

      while(var2.hasNext()) {
         toMenuAdapter(((CascadingMenuPopup.CascadingMenuInfo)var2.next()).getListView().getAdapter()).notifyDataSetChanged();
      }

   }

   private static class CascadingMenuInfo {
      public final MenuBuilder menu;
      public final int position;
      public final MenuPopupWindow window;

      public CascadingMenuInfo(@NonNull MenuPopupWindow var1, @NonNull MenuBuilder var2, int var3) {
         this.window = var1;
         this.menu = var2;
         this.position = var3;
      }

      public ListView getListView() {
         return this.window.getListView();
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface HorizPosition {
   }
}
