package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;

public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
   static final int GENERATED_ITEM_PADDING = 4;
   static final int MIN_CELL_SIZE = 56;
   private static final String TAG = "ActionMenuView";
   private MenuPresenter.Callback mActionMenuPresenterCallback;
   private boolean mFormatItems;
   private int mFormatItemsWidth;
   private int mGeneratedItemPadding;
   private MenuBuilder mMenu;
   MenuBuilder.Callback mMenuBuilderCallback;
   private int mMinCellSize;
   ActionMenuView.OnMenuItemClickListener mOnMenuItemClickListener;
   private Context mPopupContext;
   private int mPopupTheme;
   private ActionMenuPresenter mPresenter;
   private boolean mReserveOverflow;

   public ActionMenuView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionMenuView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setBaselineAligned(false);
      float var3 = var1.getResources().getDisplayMetrics().density;
      this.mMinCellSize = (int)(56.0F * var3);
      this.mGeneratedItemPadding = (int)(4.0F * var3);
      this.mPopupContext = var1;
      this.mPopupTheme = 0;
   }

   static int measureChildForCells(View var0, int var1, int var2, int var3, int var4) {
      ActionMenuView.LayoutParams var10 = (ActionMenuView.LayoutParams)var0.getLayoutParams();
      int var6 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var3) - var4, MeasureSpec.getMode(var3));
      ActionMenuItemView var9;
      if (var0 instanceof ActionMenuItemView) {
         var9 = (ActionMenuItemView)var0;
      } else {
         var9 = null;
      }

      boolean var8 = false;
      boolean var11;
      if (var9 != null && var9.hasText()) {
         var11 = true;
      } else {
         var11 = false;
      }

      byte var5 = 0;
      var3 = var5;
      if (var2 > 0) {
         label44: {
            if (var11) {
               var3 = var5;
               if (var2 < 2) {
                  break label44;
               }
            }

            var0.measure(MeasureSpec.makeMeasureSpec(var1 * var2, Integer.MIN_VALUE), var6);
            int var12 = var0.getMeasuredWidth();
            var3 = var12 / var1;
            var2 = var3;
            if (var12 % var1 != 0) {
               var2 = var3 + 1;
            }

            var3 = var2;
            if (var11) {
               var3 = var2;
               if (var2 < 2) {
                  var3 = 2;
               }
            }
         }
      }

      boolean var7 = var8;
      if (!var10.isOverflowButton) {
         var7 = var8;
         if (var11) {
            var7 = true;
         }
      }

      var10.expandable = var7;
      var10.cellsUsed = var3;
      var0.measure(MeasureSpec.makeMeasureSpec(var3 * var1, 1073741824), var6);
      return var3;
   }

   private void onMeasureExactFormat(int var1, int var2) {
      int var17 = MeasureSpec.getMode(var2);
      var1 = MeasureSpec.getSize(var1);
      int var20 = MeasureSpec.getSize(var2);
      int var6 = this.getPaddingLeft();
      int var7 = this.getPaddingRight();
      int var15 = this.getPaddingTop() + this.getPaddingBottom();
      int var21 = getChildMeasureSpec(var2, var15, -2);
      int var18 = var1 - (var6 + var7);
      var1 = this.mMinCellSize;
      int var12 = var18 / var1;
      int var10 = var18 % var1;
      if (var12 == 0) {
         this.setMeasuredDimension(var18, 0);
      } else {
         int var22 = var1 + var10 / var12;
         var1 = var12;
         var6 = 0;
         int var11 = 0;
         var2 = 0;
         boolean var8 = false;
         long var24 = 0L;
         int var23 = this.getChildCount();
         var7 = 0;

         int var9;
         long var26;
         View var31;
         ActionMenuView.LayoutParams var32;
         for(int var13 = 0; var13 < var23; var7 = var9) {
            var31 = this.getChildAt(var13);
            if (var31.getVisibility() == 8) {
               var9 = var7;
            } else {
               boolean var30 = var31 instanceof ActionMenuItemView;
               var9 = var7 + 1;
               if (var30) {
                  var7 = this.mGeneratedItemPadding;
                  var31.setPadding(var7, 0, var7, 0);
               }

               var32 = (ActionMenuView.LayoutParams)var31.getLayoutParams();
               var32.expanded = false;
               var32.extraPixels = 0;
               var32.cellsUsed = 0;
               var32.expandable = false;
               var32.leftMargin = 0;
               var32.rightMargin = 0;
               if (var30 && ((ActionMenuItemView)var31).hasText()) {
                  var30 = true;
               } else {
                  var30 = false;
               }

               var32.preventEdgeOffset = var30;
               if (var32.isOverflowButton) {
                  var7 = 1;
               } else {
                  var7 = var1;
               }

               int var14 = measureChildForCells(var31, var22, var7, var21, var15);
               var11 = Math.max(var11, var14);
               var7 = var2;
               if (var32.expandable) {
                  var7 = var2 + 1;
               }

               if (var32.isOverflowButton) {
                  var8 = true;
               }

               var1 -= var14;
               var6 = Math.max(var6, var31.getMeasuredHeight());
               if (var14 == 1) {
                  var26 = (long)(1 << var13);
                  var24 |= var26;
                  var2 = var7;
               } else {
                  var2 = var7;
               }
            }

            ++var13;
         }

         boolean var38;
         if (var8 && var7 == 2) {
            var38 = true;
         } else {
            var38 = false;
         }

         boolean var36 = false;
         int var16 = var1;

         boolean var33;
         for(var33 = var36; var2 > 0 && var16 > 0; var33 = true) {
            var17 = Integer.MAX_VALUE;
            long var28 = 0L;
            byte var37 = 0;
            var18 = 0;

            for(var1 = var37; var18 < var23; var28 = var26) {
               ActionMenuView.LayoutParams var39 = (ActionMenuView.LayoutParams)this.getChildAt(var18).getLayoutParams();
               int var19;
               if (!var39.expandable) {
                  var10 = var1;
                  var19 = var17;
                  var26 = var28;
               } else if (var39.cellsUsed < var17) {
                  var19 = var39.cellsUsed;
                  var26 = 1L << var18;
                  var10 = 1;
               } else {
                  var10 = var1;
                  var19 = var17;
                  var26 = var28;
                  if (var39.cellsUsed == var17) {
                     var26 = var28 | 1L << var18;
                     var10 = var1 + 1;
                     var19 = var17;
                  }
               }

               ++var18;
               var1 = var10;
               var17 = var19;
            }

            var24 |= var28;
            if (var1 > var16) {
               var33 = var33;
               break;
            }

            for(var9 = 0; var9 < var23; var24 = var26) {
               var31 = this.getChildAt(var9);
               var32 = (ActionMenuView.LayoutParams)var31.getLayoutParams();
               if ((var28 & (long)(1 << var9)) == 0L) {
                  var10 = var16;
                  var26 = var24;
                  if (var32.cellsUsed == var17 + 1) {
                     var26 = var24 | (long)(1 << var9);
                     var10 = var16;
                  }
               } else {
                  if (var38 && var32.preventEdgeOffset && var16 == 1) {
                     var10 = this.mGeneratedItemPadding;
                     var31.setPadding(var10 + var22, 0, var10, 0);
                  }

                  ++var32.cellsUsed;
                  var32.expanded = true;
                  var10 = var16 - 1;
                  var26 = var24;
               }

               ++var9;
               var16 = var10;
            }
         }

         boolean var34;
         if (!var8 && var7 == 1) {
            var34 = true;
         } else {
            var34 = false;
         }

         if (var16 > 0 && var24 != 0L && (var16 < var7 - 1 || var34 || var11 > 1)) {
            float var5 = (float)Long.bitCount(var24);
            float var3 = var5;
            if (!var34) {
               float var4;
               if ((var24 & 1L) != 0L) {
                  var4 = var5;
                  if (!((ActionMenuView.LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                     var4 = var5 - 0.5F;
                  }
               } else {
                  var4 = var5;
               }

               var3 = var4;
               if ((var24 & (long)(1 << var23 - 1)) != 0L) {
                  var3 = var4;
                  if (!((ActionMenuView.LayoutParams)this.getChildAt(var23 - 1).getLayoutParams()).preventEdgeOffset) {
                     var3 = var4 - 0.5F;
                  }
               }
            }

            int var35;
            if (var3 > 0.0F) {
               var35 = (int)((float)(var16 * var22) / var3);
            } else {
               var35 = 0;
            }

            for(var10 = 0; var10 < var23; var33 = var34) {
               if ((var24 & (long)(1 << var10)) == 0L) {
                  var34 = var33;
               } else {
                  var31 = this.getChildAt(var10);
                  var32 = (ActionMenuView.LayoutParams)var31.getLayoutParams();
                  if (var31 instanceof ActionMenuItemView) {
                     var32.extraPixels = var35;
                     var32.expanded = true;
                     if (var10 == 0 && !var32.preventEdgeOffset) {
                        var32.leftMargin = -var35 / 2;
                     }

                     var34 = true;
                  } else if (var32.isOverflowButton) {
                     var32.extraPixels = var35;
                     var32.expanded = true;
                     var32.rightMargin = -var35 / 2;
                     var34 = true;
                  } else {
                     if (var10 != 0) {
                        var32.leftMargin = var35 / 2;
                     }

                     var34 = var33;
                     if (var10 != var23 - 1) {
                        var32.rightMargin = var35 / 2;
                        var34 = var33;
                     }
                  }
               }

               ++var10;
            }
         }

         if (var33) {
            for(var1 = 0; var1 < var23; ++var1) {
               var31 = this.getChildAt(var1);
               var32 = (ActionMenuView.LayoutParams)var31.getLayoutParams();
               if (var32.expanded) {
                  var31.measure(MeasureSpec.makeMeasureSpec(var32.cellsUsed * var22 + var32.extraPixels, 1073741824), var21);
               }
            }
         }

         if (var17 != 1073741824) {
            var1 = var6;
         } else {
            var1 = var20;
         }

         this.setMeasuredDimension(var18, var1);
      }
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof ActionMenuView.LayoutParams;
   }

   public void dismissPopupMenus() {
      ActionMenuPresenter var1 = this.mPresenter;
      if (var1 != null) {
         var1.dismissPopupMenus();
      }

   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      return false;
   }

   protected ActionMenuView.LayoutParams generateDefaultLayoutParams() {
      ActionMenuView.LayoutParams var1 = new ActionMenuView.LayoutParams(-2, -2);
      var1.gravity = 16;
      return var1;
   }

   public ActionMenuView.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ActionMenuView.LayoutParams(this.getContext(), var1);
   }

   protected ActionMenuView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (var1 != null) {
         ActionMenuView.LayoutParams var2;
         if (var1 instanceof ActionMenuView.LayoutParams) {
            var2 = new ActionMenuView.LayoutParams((ActionMenuView.LayoutParams)var1);
         } else {
            var2 = new ActionMenuView.LayoutParams(var1);
         }

         if (var2.gravity <= 0) {
            var2.gravity = 16;
         }

         return var2;
      } else {
         return this.generateDefaultLayoutParams();
      }
   }

   public ActionMenuView.LayoutParams generateOverflowButtonLayoutParams() {
      ActionMenuView.LayoutParams var1 = this.generateDefaultLayoutParams();
      var1.isOverflowButton = true;
      return var1;
   }

   public Menu getMenu() {
      if (this.mMenu == null) {
         Context var1 = this.getContext();
         MenuBuilder var2 = new MenuBuilder(var1);
         this.mMenu = var2;
         var2.setCallback(new ActionMenuView.MenuBuilderCallback());
         ActionMenuPresenter var3 = new ActionMenuPresenter(var1);
         this.mPresenter = var3;
         var3.setReserveOverflow(true);
         ActionMenuPresenter var5 = this.mPresenter;
         Object var4 = this.mActionMenuPresenterCallback;
         if (var4 == null) {
            var4 = new ActionMenuView.ActionMenuPresenterCallback();
         }

         var5.setCallback((MenuPresenter.Callback)var4);
         this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
         this.mPresenter.setMenuView(this);
      }

      return this.mMenu;
   }

   public Drawable getOverflowIcon() {
      this.getMenu();
      return this.mPresenter.getOverflowIcon();
   }

   public int getPopupTheme() {
      return this.mPopupTheme;
   }

   public int getWindowAnimations() {
      return 0;
   }

   protected boolean hasSupportDividerBeforeChildAt(int var1) {
      if (var1 == 0) {
         return false;
      } else {
         View var4 = this.getChildAt(var1 - 1);
         View var5 = this.getChildAt(var1);
         boolean var3 = false;
         boolean var2 = var3;
         if (var1 < this.getChildCount()) {
            var2 = var3;
            if (var4 instanceof ActionMenuView.ActionMenuChildView) {
               var2 = false | ((ActionMenuView.ActionMenuChildView)var4).needsDividerAfter();
            }
         }

         var3 = var2;
         if (var1 > 0) {
            var3 = var2;
            if (var5 instanceof ActionMenuView.ActionMenuChildView) {
               var3 = var2 | ((ActionMenuView.ActionMenuChildView)var5).needsDividerBefore();
            }
         }

         return var3;
      }
   }

   public boolean hideOverflowMenu() {
      ActionMenuPresenter var1 = this.mPresenter;
      return var1 != null && var1.hideOverflowMenu();
   }

   public void initialize(MenuBuilder var1) {
      this.mMenu = var1;
   }

   public boolean invokeItem(MenuItemImpl var1) {
      return this.mMenu.performItemAction(var1, 0);
   }

   public boolean isOverflowMenuShowPending() {
      ActionMenuPresenter var1 = this.mPresenter;
      return var1 != null && var1.isOverflowMenuShowPending();
   }

   public boolean isOverflowMenuShowing() {
      ActionMenuPresenter var1 = this.mPresenter;
      return var1 != null && var1.isOverflowMenuShowing();
   }

   public boolean isOverflowReserved() {
      return this.mReserveOverflow;
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      ActionMenuPresenter var2 = this.mPresenter;
      if (var2 != null) {
         var2.updateMenuView(false);
         if (this.mPresenter.isOverflowMenuShowing()) {
            this.mPresenter.hideOverflowMenu();
            this.mPresenter.showOverflowMenu();
         }
      }

   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.dismissPopupMenus();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      ActionMenuView var16 = this;
      if (!this.mFormatItems) {
         super.onLayout(var1, var2, var3, var4, var5);
      } else {
         int var13 = this.getChildCount();
         int var7 = (var5 - var3) / 2;
         int var12 = this.getDividerWidth();
         boolean var19 = false;
         int var6 = 0;
         int var9 = 0;
         var5 = var4 - var2 - this.getPaddingRight() - this.getPaddingLeft();
         byte var10 = 0;
         var1 = ViewUtils.isLayoutRtl(this);

         int var8;
         View var17;
         ActionMenuView.LayoutParams var18;
         int var20;
         for(var8 = 0; var8 < var13; ++var8) {
            var17 = var16.getChildAt(var8);
            if (var17.getVisibility() != 8) {
               var18 = (ActionMenuView.LayoutParams)var17.getLayoutParams();
               int var11;
               int var14;
               if (var18.isOverflowButton) {
                  var20 = var17.getMeasuredWidth();
                  var3 = var20;
                  if (var16.hasSupportDividerBeforeChildAt(var8)) {
                     var3 = var20 + var12;
                  }

                  var14 = var17.getMeasuredHeight();
                  if (var1) {
                     var20 = this.getPaddingLeft() + var18.leftMargin;
                     var11 = var20 + var3;
                  } else {
                     var11 = this.getWidth() - this.getPaddingRight() - var18.rightMargin;
                     var20 = var11 - var3;
                  }

                  int var15 = var7 - var14 / 2;
                  var17.layout(var20, var15, var11, var15 + var14);
                  var5 -= var3;
                  var10 = 1;
               } else {
                  var14 = var17.getMeasuredWidth() + var18.leftMargin + var18.rightMargin;
                  var11 = var6 + var14;
                  var5 -= var14;
                  var6 = var11;
                  if (var16.hasSupportDividerBeforeChildAt(var8)) {
                     var6 = var11 + var12;
                  }

                  ++var9;
               }
            }
         }

         View var21;
         if (var13 == 1 && var10 == 0) {
            var21 = var16.getChildAt(0);
            var3 = var21.getMeasuredWidth();
            var5 = var21.getMeasuredHeight();
            var2 = (var4 - var2) / 2 - var3 / 2;
            var4 = var7 - var5 / 2;
            var21.layout(var2, var4, var2 + var3, var4 + var5);
         } else {
            var2 = var9 - (var10 ^ 1);
            if (var2 > 0) {
               var2 = var5 / var2;
            } else {
               var2 = 0;
            }

            var6 = Math.max(0, var2);
            if (var1) {
               var5 = this.getWidth() - this.getPaddingRight();

               for(var2 = 0; var2 < var13; ++var2) {
                  var17 = var16.getChildAt(var2);
                  var18 = (ActionMenuView.LayoutParams)var17.getLayoutParams();
                  if (var17.getVisibility() != 8 && !var18.isOverflowButton) {
                     var5 -= var18.rightMargin;
                     var8 = var17.getMeasuredWidth();
                     var9 = var17.getMeasuredHeight();
                     var20 = var7 - var9 / 2;
                     var17.layout(var5 - var8, var20, var5, var20 + var9);
                     var5 -= var18.leftMargin + var8 + var6;
                  }
               }

            } else {
               var3 = this.getPaddingLeft();

               for(var2 = 0; var2 < var13; var3 = var4) {
                  var21 = this.getChildAt(var2);
                  ActionMenuView.LayoutParams var22 = (ActionMenuView.LayoutParams)var21.getLayoutParams();
                  var4 = var3;
                  if (var21.getVisibility() != 8) {
                     if (var22.isOverflowButton) {
                        var4 = var3;
                     } else {
                        var3 += var22.leftMargin;
                        var4 = var21.getMeasuredWidth();
                        var5 = var21.getMeasuredHeight();
                        var8 = var7 - var5 / 2;
                        var21.layout(var3, var8, var3 + var4, var8 + var5);
                        var4 = var3 + var22.rightMargin + var4 + var6;
                     }
                  }

                  ++var2;
               }

            }
         }
      }
   }

   protected void onMeasure(int var1, int var2) {
      boolean var6 = this.mFormatItems;
      boolean var5;
      if (MeasureSpec.getMode(var1) == 1073741824) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.mFormatItems = var5;
      if (var6 != var5) {
         this.mFormatItemsWidth = 0;
      }

      int var3 = MeasureSpec.getSize(var1);
      if (this.mFormatItems) {
         MenuBuilder var7 = this.mMenu;
         if (var7 != null && var3 != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = var3;
            var7.onItemsChanged(true);
         }
      }

      int var4 = this.getChildCount();
      if (this.mFormatItems && var4 > 0) {
         this.onMeasureExactFormat(var1, var2);
      } else {
         for(var3 = 0; var3 < var4; ++var3) {
            ActionMenuView.LayoutParams var8 = (ActionMenuView.LayoutParams)this.getChildAt(var3).getLayoutParams();
            var8.rightMargin = 0;
            var8.leftMargin = 0;
         }

         super.onMeasure(var1, var2);
      }
   }

   public MenuBuilder peekMenu() {
      return this.mMenu;
   }

   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mPresenter.setExpandedActionViewsExclusive(var1);
   }

   public void setMenuCallbacks(MenuPresenter.Callback var1, MenuBuilder.Callback var2) {
      this.mActionMenuPresenterCallback = var1;
      this.mMenuBuilderCallback = var2;
   }

   public void setOnMenuItemClickListener(ActionMenuView.OnMenuItemClickListener var1) {
      this.mOnMenuItemClickListener = var1;
   }

   public void setOverflowIcon(Drawable var1) {
      this.getMenu();
      this.mPresenter.setOverflowIcon(var1);
   }

   public void setOverflowReserved(boolean var1) {
      this.mReserveOverflow = var1;
   }

   public void setPopupTheme(int var1) {
      if (this.mPopupTheme != var1) {
         this.mPopupTheme = var1;
         if (var1 == 0) {
            this.mPopupContext = this.getContext();
            return;
         }

         this.mPopupContext = new ContextThemeWrapper(this.getContext(), var1);
      }

   }

   public void setPresenter(ActionMenuPresenter var1) {
      this.mPresenter = var1;
      var1.setMenuView(this);
   }

   public boolean showOverflowMenu() {
      ActionMenuPresenter var1 = this.mPresenter;
      return var1 != null && var1.showOverflowMenu();
   }

   public interface ActionMenuChildView {
      boolean needsDividerAfter();

      boolean needsDividerBefore();
   }

   private static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      ActionMenuPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         return false;
      }
   }

   public static class LayoutParams extends LinearLayoutCompat.LayoutParams {
      @ExportedProperty
      public int cellsUsed;
      @ExportedProperty
      public boolean expandable;
      boolean expanded;
      @ExportedProperty
      public int extraPixels;
      @ExportedProperty
      public boolean isOverflowButton;
      @ExportedProperty
      public boolean preventEdgeOffset;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.isOverflowButton = false;
      }

      LayoutParams(int var1, int var2, boolean var3) {
         super(var1, var2);
         this.isOverflowButton = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(ActionMenuView.LayoutParams var1) {
         super((android.view.ViewGroup.LayoutParams)var1);
         this.isOverflowButton = var1.isOverflowButton;
      }
   }

   private class MenuBuilderCallback implements MenuBuilder.Callback {
      MenuBuilderCallback() {
      }

      public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
         return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(var2);
      }

      public void onMenuModeChange(MenuBuilder var1) {
         if (ActionMenuView.this.mMenuBuilderCallback != null) {
            ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(var1);
         }

      }
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }
}
