package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.annotation.RestrictTo;
import android.support.design.R$dimen;
import android.support.design.R$drawable;
import android.support.design.R$id;
import android.support.design.R$layout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R$attr;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class NavigationMenuItemView extends ForegroundLinearLayout implements MenuView.ItemView {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private final AccessibilityDelegateCompat mAccessibilityDelegate;
   private FrameLayout mActionArea;
   boolean mCheckable;
   private Drawable mEmptyDrawable;
   private boolean mHasIconTintList;
   private final int mIconSize;
   private ColorStateList mIconTintList;
   private MenuItemImpl mItemData;
   private boolean mNeedsEmptyIcon;
   private final CheckedTextView mTextView;

   public NavigationMenuItemView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NavigationMenuItemView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public NavigationMenuItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mAccessibilityDelegate = new AccessibilityDelegateCompat() {
         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            var2.setCheckable(NavigationMenuItemView.this.mCheckable);
         }
      };
      this.setOrientation(0);
      LayoutInflater.from(var1).inflate(R$layout.design_navigation_menu_item, this, true);
      this.mIconSize = var1.getResources().getDimensionPixelSize(R$dimen.design_navigation_icon_size);
      this.mTextView = (CheckedTextView)this.findViewById(R$id.design_menu_item_text);
      this.mTextView.setDuplicateParentStateEnabled(true);
      ViewCompat.setAccessibilityDelegate(this.mTextView, this.mAccessibilityDelegate);
   }

   private void adjustAppearance() {
      FrameLayout var1;
      LinearLayoutCompat.LayoutParams var2;
      if (this.shouldExpandActionArea()) {
         this.mTextView.setVisibility(8);
         var1 = this.mActionArea;
         if (var1 != null) {
            var2 = (LinearLayoutCompat.LayoutParams)var1.getLayoutParams();
            var2.width = -1;
            this.mActionArea.setLayoutParams(var2);
         }
      } else {
         this.mTextView.setVisibility(0);
         var1 = this.mActionArea;
         if (var1 != null) {
            var2 = (LinearLayoutCompat.LayoutParams)var1.getLayoutParams();
            var2.width = -2;
            this.mActionArea.setLayoutParams(var2);
         }
      }
   }

   private StateListDrawable createDefaultBackground() {
      TypedValue var1 = new TypedValue();
      if (this.getContext().getTheme().resolveAttribute(R$attr.colorControlHighlight, var1, true)) {
         StateListDrawable var2 = new StateListDrawable();
         var2.addState(CHECKED_STATE_SET, new ColorDrawable(var1.data));
         var2.addState(EMPTY_STATE_SET, new ColorDrawable(0));
         return var2;
      } else {
         return null;
      }
   }

   private void setActionView(View var1) {
      if (var1 != null) {
         if (this.mActionArea == null) {
            this.mActionArea = (FrameLayout)((ViewStub)this.findViewById(R$id.design_menu_item_action_area_stub)).inflate();
         }

         this.mActionArea.removeAllViews();
         this.mActionArea.addView(var1);
      }
   }

   private boolean shouldExpandActionArea() {
      return this.mItemData.getTitle() == null && this.mItemData.getIcon() == null && this.mItemData.getActionView() != null;
   }

   public MenuItemImpl getItemData() {
      return this.mItemData;
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.mItemData = var1;
      byte var3;
      if (var1.isVisible()) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      this.setVisibility(var3);
      if (this.getBackground() == null) {
         ViewCompat.setBackground(this, this.createDefaultBackground());
      }

      this.setCheckable(var1.isCheckable());
      this.setChecked(var1.isChecked());
      this.setEnabled(var1.isEnabled());
      this.setTitle(var1.getTitle());
      this.setIcon(var1.getIcon());
      this.setActionView(var1.getActionView());
      this.setContentDescription(var1.getContentDescription());
      TooltipCompat.setTooltipText(this, var1.getTooltipText());
      this.adjustAppearance();
   }

   protected int[] onCreateDrawableState(int var1) {
      int[] var2 = super.onCreateDrawableState(var1 + 1);
      MenuItemImpl var3 = this.mItemData;
      if (var3 != null && var3.isCheckable() && this.mItemData.isChecked()) {
         mergeDrawableStates(var2, CHECKED_STATE_SET);
         return var2;
      } else {
         return var2;
      }
   }

   public boolean prefersCondensedTitle() {
      return false;
   }

   public void recycle() {
      FrameLayout var1 = this.mActionArea;
      if (var1 != null) {
         var1.removeAllViews();
      }

      this.mTextView.setCompoundDrawables((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
   }

   public void setCheckable(boolean var1) {
      this.refreshDrawableState();
      if (this.mCheckable != var1) {
         this.mCheckable = var1;
         this.mAccessibilityDelegate.sendAccessibilityEvent(this.mTextView, 2048);
      }
   }

   public void setChecked(boolean var1) {
      this.refreshDrawableState();
      this.mTextView.setChecked(var1);
   }

   public void setIcon(Drawable var1) {
      int var2;
      if (var1 != null) {
         if (this.mHasIconTintList) {
            ConstantState var3 = var1.getConstantState();
            if (var3 != null) {
               var1 = var3.newDrawable();
            }

            var1 = DrawableCompat.wrap(var1).mutate();
            DrawableCompat.setTintList(var1, this.mIconTintList);
         }

         var2 = this.mIconSize;
         var1.setBounds(0, 0, var2, var2);
      } else if (this.mNeedsEmptyIcon) {
         if (this.mEmptyDrawable == null) {
            this.mEmptyDrawable = ResourcesCompat.getDrawable(this.getResources(), R$drawable.navigation_empty_icon, this.getContext().getTheme());
            var1 = this.mEmptyDrawable;
            if (var1 != null) {
               var2 = this.mIconSize;
               var1.setBounds(0, 0, var2, var2);
            }
         }

         var1 = this.mEmptyDrawable;
      }

      TextViewCompat.setCompoundDrawablesRelative(this.mTextView, var1, (Drawable)null, (Drawable)null, (Drawable)null);
   }

   void setIconTintList(ColorStateList var1) {
      this.mIconTintList = var1;
      boolean var2;
      if (this.mIconTintList != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mHasIconTintList = var2;
      MenuItemImpl var3 = this.mItemData;
      if (var3 != null) {
         this.setIcon(var3.getIcon());
      }
   }

   public void setNeedsEmptyIcon(boolean var1) {
      this.mNeedsEmptyIcon = var1;
   }

   public void setShortcut(boolean var1, char var2) {
   }

   public void setTextAppearance(int var1) {
      TextViewCompat.setTextAppearance(this.mTextView, var1);
   }

   public void setTextColor(ColorStateList var1) {
      this.mTextView.setTextColor(var1);
   }

   public void setTitle(CharSequence var1) {
      this.mTextView.setText(var1);
   }

   public boolean showsIcon() {
      return true;
   }
}
