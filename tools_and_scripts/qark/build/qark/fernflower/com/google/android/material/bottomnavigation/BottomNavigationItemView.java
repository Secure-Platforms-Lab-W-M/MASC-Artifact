package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R.dimen;
import com.google.android.material.R.drawable;
import com.google.android.material.R.id;
import com.google.android.material.R.layout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

public class BottomNavigationItemView extends FrameLayout implements MenuView.ItemView {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   public static final int INVALID_ITEM_POSITION = -1;
   private BadgeDrawable badgeDrawable;
   private final int defaultMargin;
   private ImageView icon;
   private ColorStateList iconTint;
   private boolean isShifting;
   private MenuItemImpl itemData;
   private int itemPosition;
   private int labelVisibilityMode;
   private final TextView largeLabel;
   private Drawable originalIconDrawable;
   private float scaleDownFactor;
   private float scaleUpFactor;
   private float shiftAmount;
   private final TextView smallLabel;
   private Drawable wrappedIconDrawable;

   public BottomNavigationItemView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public BottomNavigationItemView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public BottomNavigationItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.itemPosition = -1;
      Resources var5 = this.getResources();
      LayoutInflater.from(var1).inflate(layout.design_bottom_navigation_item, this, true);
      this.setBackgroundResource(drawable.design_bottom_navigation_item_background);
      this.defaultMargin = var5.getDimensionPixelSize(dimen.design_bottom_navigation_margin);
      this.icon = (ImageView)this.findViewById(id.icon);
      this.smallLabel = (TextView)this.findViewById(id.smallLabel);
      this.largeLabel = (TextView)this.findViewById(id.largeLabel);
      ViewCompat.setImportantForAccessibility(this.smallLabel, 2);
      ViewCompat.setImportantForAccessibility(this.largeLabel, 2);
      this.setFocusable(true);
      this.calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
      ImageView var4 = this.icon;
      if (var4 != null) {
         var4.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            public void onLayoutChange(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
               if (BottomNavigationItemView.this.icon.getVisibility() == 0) {
                  BottomNavigationItemView var10 = BottomNavigationItemView.this;
                  var10.tryUpdateBadgeBounds(var10.icon);
               }

            }
         });
      }

      ViewCompat.setAccessibilityDelegate(this, (AccessibilityDelegateCompat)null);
   }

   private void calculateTextScaleFactors(float var1, float var2) {
      this.shiftAmount = var1 - var2;
      this.scaleUpFactor = var2 * 1.0F / var1;
      this.scaleDownFactor = 1.0F * var1 / var2;
   }

   private FrameLayout getCustomParentForBadge(View var1) {
      ImageView var3 = this.icon;
      Object var2 = null;
      if (var1 == var3) {
         FrameLayout var4 = (FrameLayout)var2;
         if (BadgeUtils.USE_COMPAT_PARENT) {
            var4 = (FrameLayout)this.icon.getParent();
         }

         return var4;
      } else {
         return null;
      }
   }

   private boolean hasBadge() {
      return this.badgeDrawable != null;
   }

   private void setViewLayoutParams(View var1, int var2, int var3) {
      LayoutParams var4 = (LayoutParams)var1.getLayoutParams();
      var4.topMargin = var2;
      var4.gravity = var3;
      var1.setLayoutParams(var4);
   }

   private void setViewValues(View var1, float var2, float var3, int var4) {
      var1.setScaleX(var2);
      var1.setScaleY(var3);
      var1.setVisibility(var4);
   }

   private void tryAttachBadgeToAnchor(View var1) {
      if (this.hasBadge()) {
         if (var1 != null) {
            this.setClipChildren(false);
            this.setClipToPadding(false);
            BadgeUtils.attachBadgeDrawable(this.badgeDrawable, var1, this.getCustomParentForBadge(var1));
         }

      }
   }

   private void tryRemoveBadgeFromAnchor(View var1) {
      if (this.hasBadge()) {
         if (var1 != null) {
            this.setClipChildren(true);
            this.setClipToPadding(true);
            BadgeUtils.detachBadgeDrawable(this.badgeDrawable, var1, this.getCustomParentForBadge(var1));
         }

         this.badgeDrawable = null;
      }
   }

   private void tryUpdateBadgeBounds(View var1) {
      if (this.hasBadge()) {
         BadgeUtils.setBadgeDrawableBounds(this.badgeDrawable, var1, this.getCustomParentForBadge(var1));
      }
   }

   BadgeDrawable getBadge() {
      return this.badgeDrawable;
   }

   public MenuItemImpl getItemData() {
      return this.itemData;
   }

   public int getItemPosition() {
      return this.itemPosition;
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.itemData = var1;
      this.setCheckable(var1.isCheckable());
      this.setChecked(var1.isChecked());
      this.setEnabled(var1.isEnabled());
      this.setIcon(var1.getIcon());
      this.setTitle(var1.getTitle());
      this.setId(var1.getItemId());
      if (!TextUtils.isEmpty(var1.getContentDescription())) {
         this.setContentDescription(var1.getContentDescription());
      }

      CharSequence var3;
      if (!TextUtils.isEmpty(var1.getTooltipText())) {
         var3 = var1.getTooltipText();
      } else {
         var3 = var1.getTitle();
      }

      TooltipCompat.setTooltipText(this, var3);
      byte var4;
      if (var1.isVisible()) {
         var4 = 0;
      } else {
         var4 = 8;
      }

      this.setVisibility(var4);
   }

   public int[] onCreateDrawableState(int var1) {
      int[] var2 = super.onCreateDrawableState(var1 + 1);
      MenuItemImpl var3 = this.itemData;
      if (var3 != null && var3.isCheckable() && this.itemData.isChecked()) {
         mergeDrawableStates(var2, CHECKED_STATE_SET);
      }

      return var2;
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      BadgeDrawable var2 = this.badgeDrawable;
      if (var2 != null && var2.isVisible()) {
         CharSequence var4 = this.itemData.getTitle();
         if (!TextUtils.isEmpty(this.itemData.getContentDescription())) {
            var4 = this.itemData.getContentDescription();
         }

         StringBuilder var3 = new StringBuilder();
         var3.append(var4);
         var3.append(", ");
         var3.append(this.badgeDrawable.getContentDescription());
         var1.setContentDescription(var3.toString());
      }

   }

   public boolean prefersCondensedTitle() {
      return false;
   }

   void removeBadge() {
      this.tryRemoveBadgeFromAnchor(this.icon);
   }

   void setBadge(BadgeDrawable var1) {
      this.badgeDrawable = var1;
      ImageView var2 = this.icon;
      if (var2 != null) {
         this.tryAttachBadgeToAnchor(var2);
      }

   }

   public void setCheckable(boolean var1) {
      this.refreshDrawableState();
   }

   public void setChecked(boolean var1) {
      TextView var4 = this.largeLabel;
      var4.setPivotX((float)(var4.getWidth() / 2));
      var4 = this.largeLabel;
      var4.setPivotY((float)var4.getBaseline());
      var4 = this.smallLabel;
      var4.setPivotX((float)(var4.getWidth() / 2));
      var4 = this.smallLabel;
      var4.setPivotY((float)var4.getBaseline());
      int var3 = this.labelVisibilityMode;
      float var2;
      if (var3 != -1) {
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  this.setViewLayoutParams(this.icon, this.defaultMargin, 17);
                  this.largeLabel.setVisibility(8);
                  this.smallLabel.setVisibility(8);
               }
            } else if (var1) {
               this.setViewLayoutParams(this.icon, (int)((float)this.defaultMargin + this.shiftAmount), 49);
               this.setViewValues(this.largeLabel, 1.0F, 1.0F, 0);
               var4 = this.smallLabel;
               var2 = this.scaleUpFactor;
               this.setViewValues(var4, var2, var2, 4);
            } else {
               this.setViewLayoutParams(this.icon, this.defaultMargin, 49);
               var4 = this.largeLabel;
               var2 = this.scaleDownFactor;
               this.setViewValues(var4, var2, var2, 4);
               this.setViewValues(this.smallLabel, 1.0F, 1.0F, 0);
            }
         } else {
            if (var1) {
               this.setViewLayoutParams(this.icon, this.defaultMargin, 49);
               this.setViewValues(this.largeLabel, 1.0F, 1.0F, 0);
            } else {
               this.setViewLayoutParams(this.icon, this.defaultMargin, 17);
               this.setViewValues(this.largeLabel, 0.5F, 0.5F, 4);
            }

            this.smallLabel.setVisibility(4);
         }
      } else if (this.isShifting) {
         if (var1) {
            this.setViewLayoutParams(this.icon, this.defaultMargin, 49);
            this.setViewValues(this.largeLabel, 1.0F, 1.0F, 0);
         } else {
            this.setViewLayoutParams(this.icon, this.defaultMargin, 17);
            this.setViewValues(this.largeLabel, 0.5F, 0.5F, 4);
         }

         this.smallLabel.setVisibility(4);
      } else if (var1) {
         this.setViewLayoutParams(this.icon, (int)((float)this.defaultMargin + this.shiftAmount), 49);
         this.setViewValues(this.largeLabel, 1.0F, 1.0F, 0);
         var4 = this.smallLabel;
         var2 = this.scaleUpFactor;
         this.setViewValues(var4, var2, var2, 4);
      } else {
         this.setViewLayoutParams(this.icon, this.defaultMargin, 49);
         var4 = this.largeLabel;
         var2 = this.scaleDownFactor;
         this.setViewValues(var4, var2, var2, 4);
         this.setViewValues(this.smallLabel, 1.0F, 1.0F, 0);
      }

      this.refreshDrawableState();
      this.setSelected(var1);
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      this.smallLabel.setEnabled(var1);
      this.largeLabel.setEnabled(var1);
      this.icon.setEnabled(var1);
      if (var1) {
         ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
      } else {
         ViewCompat.setPointerIcon(this, (PointerIconCompat)null);
      }
   }

   public void setIcon(Drawable var1) {
      if (var1 != this.originalIconDrawable) {
         this.originalIconDrawable = var1;
         Drawable var2 = var1;
         if (var1 != null) {
            ConstantState var4 = var1.getConstantState();
            if (var4 != null) {
               var1 = var4.newDrawable();
            }

            var1 = DrawableCompat.wrap(var1).mutate();
            this.wrappedIconDrawable = var1;
            ColorStateList var3 = this.iconTint;
            var2 = var1;
            if (var3 != null) {
               DrawableCompat.setTintList(var1, var3);
               var2 = var1;
            }
         }

         this.icon.setImageDrawable(var2);
      }
   }

   public void setIconSize(int var1) {
      LayoutParams var2 = (LayoutParams)this.icon.getLayoutParams();
      var2.width = var1;
      var2.height = var1;
      this.icon.setLayoutParams(var2);
   }

   public void setIconTintList(ColorStateList var1) {
      this.iconTint = var1;
      if (this.itemData != null) {
         Drawable var2 = this.wrappedIconDrawable;
         if (var2 != null) {
            DrawableCompat.setTintList(var2, var1);
            this.wrappedIconDrawable.invalidateSelf();
         }
      }

   }

   public void setItemBackground(int var1) {
      Drawable var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         var2 = ContextCompat.getDrawable(this.getContext(), var1);
      }

      this.setItemBackground(var2);
   }

   public void setItemBackground(Drawable var1) {
      Drawable var2 = var1;
      if (var1 != null) {
         var2 = var1;
         if (var1.getConstantState() != null) {
            var2 = var1.getConstantState().newDrawable().mutate();
         }
      }

      ViewCompat.setBackground(this, var2);
   }

   public void setItemPosition(int var1) {
      this.itemPosition = var1;
   }

   public void setLabelVisibilityMode(int var1) {
      if (this.labelVisibilityMode != var1) {
         this.labelVisibilityMode = var1;
         boolean var2;
         if (this.itemData != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            this.setChecked(this.itemData.isChecked());
         }
      }

   }

   public void setShifting(boolean var1) {
      if (this.isShifting != var1) {
         this.isShifting = var1;
         boolean var2;
         if (this.itemData != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            this.setChecked(this.itemData.isChecked());
         }
      }

   }

   public void setShortcut(boolean var1, char var2) {
   }

   public void setTextAppearanceActive(int var1) {
      TextViewCompat.setTextAppearance(this.largeLabel, var1);
      this.calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
   }

   public void setTextAppearanceInactive(int var1) {
      TextViewCompat.setTextAppearance(this.smallLabel, var1);
      this.calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
   }

   public void setTextColor(ColorStateList var1) {
      if (var1 != null) {
         this.smallLabel.setTextColor(var1);
         this.largeLabel.setTextColor(var1);
      }

   }

   public void setTitle(CharSequence var1) {
      this.smallLabel.setText(var1);
      this.largeLabel.setText(var1);
      MenuItemImpl var2 = this.itemData;
      if (var2 == null || TextUtils.isEmpty(var2.getContentDescription())) {
         this.setContentDescription(var1);
      }

      var2 = this.itemData;
      if (var2 != null && !TextUtils.isEmpty(var2.getTooltipText())) {
         var1 = this.itemData.getTooltipText();
      }

      TooltipCompat.setTooltipText(this, var1);
   }

   public boolean showsIcon() {
      return true;
   }
}
