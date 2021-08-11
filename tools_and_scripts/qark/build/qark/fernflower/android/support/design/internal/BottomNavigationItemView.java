package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.R$dimen;
import android.support.design.R$drawable;
import android.support.design.R$id;
import android.support.design.R$layout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class BottomNavigationItemView extends FrameLayout implements MenuView.ItemView {
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   public static final int INVALID_ITEM_POSITION = -1;
   private final int mDefaultMargin;
   private ImageView mIcon;
   private ColorStateList mIconTint;
   private MenuItemImpl mItemData;
   private int mItemPosition;
   private final TextView mLargeLabel;
   private final float mScaleDownFactor;
   private final float mScaleUpFactor;
   private final int mShiftAmount;
   private boolean mShiftingMode;
   private final TextView mSmallLabel;

   public BottomNavigationItemView(@NonNull Context var1) {
      this(var1, (AttributeSet)null);
   }

   public BottomNavigationItemView(@NonNull Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public BottomNavigationItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mItemPosition = -1;
      Resources var5 = this.getResources();
      var3 = var5.getDimensionPixelSize(R$dimen.design_bottom_navigation_text_size);
      int var4 = var5.getDimensionPixelSize(R$dimen.design_bottom_navigation_active_text_size);
      this.mDefaultMargin = var5.getDimensionPixelSize(R$dimen.design_bottom_navigation_margin);
      this.mShiftAmount = var3 - var4;
      this.mScaleUpFactor = (float)var4 * 1.0F / (float)var3;
      this.mScaleDownFactor = (float)var3 * 1.0F / (float)var4;
      LayoutInflater.from(var1).inflate(R$layout.design_bottom_navigation_item, this, true);
      this.setBackgroundResource(R$drawable.design_bottom_navigation_item_background);
      this.mIcon = (ImageView)this.findViewById(R$id.icon);
      this.mSmallLabel = (TextView)this.findViewById(R$id.smallLabel);
      this.mLargeLabel = (TextView)this.findViewById(R$id.largeLabel);
   }

   public MenuItemImpl getItemData() {
      return this.mItemData;
   }

   public int getItemPosition() {
      return this.mItemPosition;
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.mItemData = var1;
      this.setCheckable(var1.isCheckable());
      this.setChecked(var1.isChecked());
      this.setEnabled(var1.isEnabled());
      this.setIcon(var1.getIcon());
      this.setTitle(var1.getTitle());
      this.setId(var1.getItemId());
      this.setContentDescription(var1.getContentDescription());
      TooltipCompat.setTooltipText(this, var1.getTooltipText());
   }

   public int[] onCreateDrawableState(int var1) {
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

   public void setCheckable(boolean var1) {
      this.refreshDrawableState();
   }

   public void setChecked(boolean var1) {
      TextView var2 = this.mLargeLabel;
      var2.setPivotX((float)(var2.getWidth() / 2));
      var2 = this.mLargeLabel;
      var2.setPivotY((float)var2.getBaseline());
      var2 = this.mSmallLabel;
      var2.setPivotX((float)(var2.getWidth() / 2));
      var2 = this.mSmallLabel;
      var2.setPivotY((float)var2.getBaseline());
      LayoutParams var3;
      if (this.mShiftingMode) {
         if (var1) {
            var3 = (LayoutParams)this.mIcon.getLayoutParams();
            var3.gravity = 49;
            var3.topMargin = this.mDefaultMargin;
            this.mIcon.setLayoutParams(var3);
            this.mLargeLabel.setVisibility(0);
            this.mLargeLabel.setScaleX(1.0F);
            this.mLargeLabel.setScaleY(1.0F);
         } else {
            var3 = (LayoutParams)this.mIcon.getLayoutParams();
            var3.gravity = 17;
            var3.topMargin = this.mDefaultMargin;
            this.mIcon.setLayoutParams(var3);
            this.mLargeLabel.setVisibility(4);
            this.mLargeLabel.setScaleX(0.5F);
            this.mLargeLabel.setScaleY(0.5F);
         }

         this.mSmallLabel.setVisibility(4);
      } else if (var1) {
         var3 = (LayoutParams)this.mIcon.getLayoutParams();
         var3.gravity = 49;
         var3.topMargin = this.mDefaultMargin + this.mShiftAmount;
         this.mIcon.setLayoutParams(var3);
         this.mLargeLabel.setVisibility(0);
         this.mSmallLabel.setVisibility(4);
         this.mLargeLabel.setScaleX(1.0F);
         this.mLargeLabel.setScaleY(1.0F);
         this.mSmallLabel.setScaleX(this.mScaleUpFactor);
         this.mSmallLabel.setScaleY(this.mScaleUpFactor);
      } else {
         var3 = (LayoutParams)this.mIcon.getLayoutParams();
         var3.gravity = 49;
         var3.topMargin = this.mDefaultMargin;
         this.mIcon.setLayoutParams(var3);
         this.mLargeLabel.setVisibility(4);
         this.mSmallLabel.setVisibility(0);
         this.mLargeLabel.setScaleX(this.mScaleDownFactor);
         this.mLargeLabel.setScaleY(this.mScaleDownFactor);
         this.mSmallLabel.setScaleX(1.0F);
         this.mSmallLabel.setScaleY(1.0F);
      }

      this.refreshDrawableState();
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      this.mSmallLabel.setEnabled(var1);
      this.mLargeLabel.setEnabled(var1);
      this.mIcon.setEnabled(var1);
      if (var1) {
         ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
      } else {
         ViewCompat.setPointerIcon(this, (PointerIconCompat)null);
      }
   }

   public void setIcon(Drawable var1) {
      if (var1 != null) {
         ConstantState var2 = var1.getConstantState();
         if (var2 != null) {
            var1 = var2.newDrawable();
         }

         var1 = DrawableCompat.wrap(var1).mutate();
         DrawableCompat.setTintList(var1, this.mIconTint);
      }

      this.mIcon.setImageDrawable(var1);
   }

   public void setIconTintList(ColorStateList var1) {
      this.mIconTint = var1;
      MenuItemImpl var2 = this.mItemData;
      if (var2 != null) {
         this.setIcon(var2.getIcon());
      }
   }

   public void setItemBackground(int var1) {
      Drawable var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         var2 = ContextCompat.getDrawable(this.getContext(), var1);
      }

      ViewCompat.setBackground(this, var2);
   }

   public void setItemPosition(int var1) {
      this.mItemPosition = var1;
   }

   public void setShiftingMode(boolean var1) {
      this.mShiftingMode = var1;
   }

   public void setShortcut(boolean var1, char var2) {
   }

   public void setTextColor(ColorStateList var1) {
      this.mSmallLabel.setTextColor(var1);
      this.mLargeLabel.setTextColor(var1);
   }

   public void setTitle(CharSequence var1) {
      this.mSmallLabel.setText(var1);
      this.mLargeLabel.setText(var1);
   }

   public boolean showsIcon() {
      return true;
   }
}
