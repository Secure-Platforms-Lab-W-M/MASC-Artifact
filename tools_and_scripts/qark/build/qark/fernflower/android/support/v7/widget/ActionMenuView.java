package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;

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
      int var19 = MeasureSpec.getMode(var2);
      var1 = MeasureSpec.getSize(var1);
      int var18 = MeasureSpec.getSize(var2);
      int var13 = this.getPaddingLeft() + this.getPaddingRight();
      int var9 = this.getPaddingTop() + this.getPaddingBottom();
      int var20 = getChildMeasureSpec(var2, var9, -2);
      int var15 = var1 - var13;
      var1 = this.mMinCellSize;
      int var12 = var15 / var1;
      int var8 = var15 % var1;
      if (var12 == 0) {
         this.setMeasuredDimension(var15, 0);
      } else {
         int var21 = var1 + var8 / var12;
         boolean var5 = false;
         int var16 = this.getChildCount();
         int var7 = 0;
         int var11 = 0;
         int var6 = 0;
         int var10 = 0;
         var1 = var12;
         int var14 = 0;

         int var17;
         long var22;
         View var29;
         ActionMenuView.LayoutParams var30;
         for(var22 = 0L; var14 < var16; var6 = var2) {
            var29 = this.getChildAt(var14);
            if (var29.getVisibility() == 8) {
               var2 = var6;
            } else {
               boolean var28 = var29 instanceof ActionMenuItemView;
               ++var11;
               if (var28) {
                  var2 = this.mGeneratedItemPadding;
                  var29.setPadding(var2, 0, var2, 0);
               }

               var30 = (ActionMenuView.LayoutParams)var29.getLayoutParams();
               var30.expanded = false;
               var30.extraPixels = 0;
               var30.cellsUsed = 0;
               var30.expandable = false;
               var30.leftMargin = 0;
               var30.rightMargin = 0;
               if (var28 && ((ActionMenuItemView)var29).hasText()) {
                  var28 = true;
               } else {
                  var28 = false;
               }

               var30.preventEdgeOffset = var28;
               if (var30.isOverflowButton) {
                  var2 = 1;
               } else {
                  var2 = var1;
               }

               var17 = measureChildForCells(var29, var21, var2, var20, var9);
               var10 = Math.max(var10, var17);
               var2 = var6;
               if (var30.expandable) {
                  var2 = var6 + 1;
               }

               if (var30.isOverflowButton) {
                  var5 = true;
               }

               var1 -= var17;
               var7 = Math.max(var7, var29.getMeasuredHeight());
               if (var17 == 1) {
                  var22 |= (long)(1 << var14);
               }
            }

            ++var14;
         }

         boolean var36;
         if (var5 && var11 == 2) {
            var36 = true;
         } else {
            var36 = false;
         }

         boolean var34 = false;
         var14 = var1;
         var2 = var16;
         boolean var31 = var34;

         while(true) {
            if (var6 <= 0 || var14 <= 0) {
               var6 = var2;
               break;
            }

            long var26 = 0L;
            var15 = Integer.MAX_VALUE;
            var8 = 0;

            long var24;
            for(var16 = 0; var16 < var2; var26 = var24) {
               ActionMenuView.LayoutParams var37 = (ActionMenuView.LayoutParams)this.getChildAt(var16).getLayoutParams();
               if (!var37.expandable) {
                  var9 = var8;
                  var17 = var15;
                  var24 = var26;
               } else if (var37.cellsUsed < var15) {
                  var17 = var37.cellsUsed;
                  var24 = (long)(1 << var16);
                  var9 = 1;
               } else {
                  var9 = var8;
                  var17 = var15;
                  var24 = var26;
                  if (var37.cellsUsed == var15) {
                     var24 = (long)(1 << var16);
                     var9 = var8 + 1;
                     var24 |= var26;
                     var17 = var15;
                  }
               }

               ++var16;
               var8 = var9;
               var15 = var17;
            }

            var1 = var2;
            var22 |= var26;
            if (var8 > var14) {
               var6 = var2;
               var31 = var31;
               break;
            }

            for(var2 = 0; var2 < var1; var22 = var24) {
               var29 = this.getChildAt(var2);
               var30 = (ActionMenuView.LayoutParams)var29.getLayoutParams();
               if ((var26 & (long)(1 << var2)) == 0L) {
                  var9 = var14;
                  var24 = var22;
                  if (var30.cellsUsed == var15 + 1) {
                     var24 = var22 | (long)(1 << var2);
                     var9 = var14;
                  }
               } else {
                  if (var36 && var30.preventEdgeOffset && var14 == 1) {
                     var9 = this.mGeneratedItemPadding;
                     var29.setPadding(var9 + var21, 0, var9, 0);
                  }

                  ++var30.cellsUsed;
                  var30.expanded = true;
                  var9 = var14 - 1;
                  var24 = var22;
               }

               ++var2;
               var14 = var9;
            }

            var34 = true;
            var2 = var1;
            var31 = var34;
         }

         boolean var32;
         if (!var5 && var11 == 1) {
            var32 = true;
         } else {
            var32 = false;
         }

         var5 = var31;
         if (var14 > 0) {
            var5 = var31;
            if (var22 != 0L) {
               label220: {
                  if (var14 >= var11 - 1 && !var32) {
                     var5 = var31;
                     if (var10 <= 1) {
                        break label220;
                     }
                  }

                  float var4 = (float)Long.bitCount(var22);
                  if (!var32) {
                     float var3;
                     if ((var22 & 1L) != 0L) {
                        var3 = var4;
                        if (!((ActionMenuView.LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                           var3 = var4 - 0.5F;
                        }
                     } else {
                        var3 = var4;
                     }

                     var4 = var3;
                     if ((var22 & (long)(1 << var6 - 1)) != 0L) {
                        var4 = var3;
                        if (!((ActionMenuView.LayoutParams)this.getChildAt(var6 - 1).getLayoutParams()).preventEdgeOffset) {
                           var4 = var3 - 0.5F;
                        }
                     }
                  }

                  int var33 = 0;
                  if (var4 > 0.0F) {
                     var33 = (int)((float)(var14 * var21) / var4);
                  }

                  for(var8 = 0; var8 < var6; var31 = var32) {
                     if ((var22 & (long)(1 << var8)) == 0L) {
                        var32 = var31;
                     } else {
                        var29 = this.getChildAt(var8);
                        var30 = (ActionMenuView.LayoutParams)var29.getLayoutParams();
                        if (var29 instanceof ActionMenuItemView) {
                           var30.extraPixels = var33;
                           var30.expanded = true;
                           if (var8 == 0 && !var30.preventEdgeOffset) {
                              var30.leftMargin = -var33 / 2;
                           }

                           var32 = true;
                        } else if (var30.isOverflowButton) {
                           var30.extraPixels = var33;
                           var30.expanded = true;
                           var30.rightMargin = -var33 / 2;
                           var32 = true;
                        } else {
                           if (var8 != 0) {
                              var30.leftMargin = var33 / 2;
                           }

                           var32 = var31;
                           if (var8 != var6 - 1) {
                              var30.rightMargin = var33 / 2;
                              var32 = var31;
                           }
                        }
                     }

                     ++var8;
                  }

                  var5 = var31;
               }
            }
         }

         if (var5) {
            for(var1 = 0; var1 < var6; ++var1) {
               var29 = this.getChildAt(var1);
               var30 = (ActionMenuView.LayoutParams)var29.getLayoutParams();
               if (var30.expanded) {
                  var29.measure(MeasureSpec.makeMeasureSpec(var30.cellsUsed * var21 + var30.extraPixels, 1073741824), var20);
               }
            }
         }

         if (var19 != 1073741824) {
            var1 = var7;
         } else {
            var1 = var18;
         }

         this.setMeasuredDimension(var15, var1);
      }
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 != null && var1 instanceof ActionMenuView.LayoutParams;
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

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public ActionMenuView.LayoutParams generateOverflowButtonLayoutParams() {
      ActionMenuView.LayoutParams var1 = this.generateDefaultLayoutParams();
      var1.isOverflowButton = true;
      return var1;
   }

   public Menu getMenu() {
      if (this.mMenu == null) {
         Context var1 = this.getContext();
         this.mMenu = new MenuBuilder(var1);
         this.mMenu.setCallback(new ActionMenuView.MenuBuilderCallback());
         this.mPresenter = new ActionMenuPresenter(var1);
         this.mPresenter.setReserveOverflow(true);
         ActionMenuPresenter var2 = this.mPresenter;
         Object var3 = this.mActionMenuPresenterCallback;
         if (var3 == null) {
            var3 = new ActionMenuView.ActionMenuPresenterCallback();
         }

         var2.setCallback((MenuPresenter.Callback)var3);
         this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
         this.mPresenter.setMenuView(this);
      }

      return this.mMenu;
   }

   @Nullable
   public Drawable getOverflowIcon() {
      this.getMenu();
      return this.mPresenter.getOverflowIcon();
   }

   public int getPopupTheme() {
      return this.mPopupTheme;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int getWindowAnimations() {
      return 0;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void initialize(MenuBuilder var1) {
      this.mMenu = var1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean invokeItem(MenuItemImpl var1) {
      return this.mMenu.performItemAction(var1, 0);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean isOverflowMenuShowPending() {
      ActionMenuPresenter var1 = this.mPresenter;
      return var1 != null && var1.isOverflowMenuShowPending();
   }

   public boolean isOverflowMenuShowing() {
      ActionMenuPresenter var1 = this.mPresenter;
      return var1 != null && var1.isOverflowMenuShowing();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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
         boolean var20 = false;
         int var6 = 0;
         int var9 = 0;
         var5 = var4 - var2 - this.getPaddingRight() - this.getPaddingLeft();
         boolean var10 = false;
         var1 = ViewUtils.isLayoutRtl(this);

         int var8;
         View var17;
         ActionMenuView.LayoutParams var18;
         int var22;
         for(var8 = 0; var8 < var13; ++var8) {
            var17 = var16.getChildAt(var8);
            if (var17.getVisibility() != 8) {
               var18 = (ActionMenuView.LayoutParams)var17.getLayoutParams();
               int var11;
               int var14;
               if (var18.isOverflowButton) {
                  var22 = var17.getMeasuredWidth();
                  var3 = var22;
                  if (var16.hasSupportDividerBeforeChildAt(var8)) {
                     var3 = var22 + var12;
                  }

                  var14 = var17.getMeasuredHeight();
                  if (var1) {
                     var22 = this.getPaddingLeft() + var18.leftMargin;
                     var11 = var22 + var3;
                  } else {
                     var11 = this.getWidth() - this.getPaddingRight() - var18.rightMargin;
                     var22 = var11 - var3;
                  }

                  int var15 = var7 - var14 / 2;
                  var17.layout(var22, var15, var11, var15 + var14);
                  var5 -= var3;
                  var10 = true;
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

         byte var21 = 1;
         View var23;
         if (var13 == 1 && !var10) {
            var23 = var16.getChildAt(0);
            var3 = var23.getMeasuredWidth();
            var5 = var23.getMeasuredHeight();
            var2 = (var4 - var2) / 2 - var3 / 2;
            var4 = var7 - var5 / 2;
            var23.layout(var2, var4, var2 + var3, var4 + var5);
         } else {
            byte var19 = var21;
            if (var10) {
               var19 = 0;
            }

            var2 = var9 - var19;
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
                     var22 = var7 - var9 / 2;
                     var17.layout(var5 - var8, var22, var5, var22 + var9);
                     var5 -= var18.leftMargin + var8 + var6;
                  }
               }

            } else {
               var3 = this.getPaddingLeft();

               for(var2 = 0; var2 < var13; var3 = var4) {
                  var23 = this.getChildAt(var2);
                  ActionMenuView.LayoutParams var24 = (ActionMenuView.LayoutParams)var23.getLayoutParams();
                  var4 = var3;
                  if (var23.getVisibility() != 8) {
                     if (var24.isOverflowButton) {
                        var4 = var3;
                     } else {
                        var3 += var24.leftMargin;
                        var4 = var23.getMeasuredWidth();
                        var5 = var23.getMeasuredHeight();
                        var8 = var7 - var5 / 2;
                        var23.layout(var3, var8, var3 + var4, var8 + var5);
                        var4 = var3 + var24.rightMargin + var4 + var6;
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
      if (var6 != this.mFormatItems) {
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

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public MenuBuilder peekMenu() {
      return this.mMenu;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setExpandedActionViewsExclusive(boolean var1) {
      this.mPresenter.setExpandedActionViewsExclusive(var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setMenuCallbacks(MenuPresenter.Callback var1, MenuBuilder.Callback var2) {
      this.mActionMenuPresenterCallback = var1;
      this.mMenuBuilderCallback = var2;
   }

   public void setOnMenuItemClickListener(ActionMenuView.OnMenuItemClickListener var1) {
      this.mOnMenuItemClickListener = var1;
   }

   public void setOverflowIcon(@Nullable Drawable var1) {
      this.getMenu();
      this.mPresenter.setOverflowIcon(var1);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setOverflowReserved(boolean var1) {
      this.mReserveOverflow = var1;
   }

   public void setPopupTheme(@StyleRes int var1) {
      if (this.mPopupTheme != var1) {
         this.mPopupTheme = var1;
         if (var1 == 0) {
            this.mPopupContext = this.getContext();
            return;
         }

         this.mPopupContext = new ContextThemeWrapper(this.getContext(), var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setPresenter(ActionMenuPresenter var1) {
      this.mPresenter = var1;
      this.mPresenter.setMenuView(this);
   }

   public boolean showOverflowMenu() {
      ActionMenuPresenter var1 = this.mPresenter;
      return var1 != null && var1.showOverflowMenu();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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

      public LayoutParams(ActionMenuView.LayoutParams var1) {
         super((android.view.ViewGroup.LayoutParams)var1);
         this.isOverflowButton = var1.isOverflowButton;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
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
