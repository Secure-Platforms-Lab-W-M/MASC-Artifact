package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import androidx.appcompat.R.styleable;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.ForwardingListener;
import androidx.appcompat.widget.TooltipCompat;

public class ActionMenuItemView extends AppCompatTextView implements MenuView.ItemView, OnClickListener, ActionMenuView.ActionMenuChildView {
   private static final int MAX_ICON_SIZE = 32;
   private static final String TAG = "ActionMenuItemView";
   private boolean mAllowTextWithIcon;
   private boolean mExpandedFormat;
   private ForwardingListener mForwardingListener;
   private Drawable mIcon;
   MenuItemImpl mItemData;
   MenuBuilder.ItemInvoker mItemInvoker;
   private int mMaxIconSize;
   private int mMinWidth;
   ActionMenuItemView.PopupCallback mPopupCallback;
   private int mSavedPaddingLeft;
   private CharSequence mTitle;

   public ActionMenuItemView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionMenuItemView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ActionMenuItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      Resources var4 = var1.getResources();
      this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
      TypedArray var5 = var1.obtainStyledAttributes(var2, styleable.ActionMenuItemView, var3, 0);
      this.mMinWidth = var5.getDimensionPixelSize(styleable.ActionMenuItemView_android_minWidth, 0);
      var5.recycle();
      this.mMaxIconSize = (int)(32.0F * var4.getDisplayMetrics().density + 0.5F);
      this.setOnClickListener(this);
      this.mSavedPaddingLeft = -1;
      this.setSaveEnabled(false);
   }

   private boolean shouldAllowTextWithIcon() {
      Configuration var3 = this.getContext().getResources().getConfiguration();
      int var1 = var3.screenWidthDp;
      int var2 = var3.screenHeightDp;
      return var1 >= 480 || var1 >= 640 && var2 >= 480 || var3.orientation == 2;
   }

   private void updateTextButtonVisibility() {
      boolean var2 = TextUtils.isEmpty(this.mTitle);
      boolean var1 = true;
      if (this.mIcon != null && (!this.mItemData.showsTextAsAction() || !this.mAllowTextWithIcon && !this.mExpandedFormat)) {
         var1 = false;
      }

      var1 &= var2 ^ true;
      Object var4 = null;
      CharSequence var3;
      if (var1) {
         var3 = this.mTitle;
      } else {
         var3 = null;
      }

      this.setText(var3);
      var3 = this.mItemData.getContentDescription();
      if (TextUtils.isEmpty(var3)) {
         if (var1) {
            var3 = null;
         } else {
            var3 = this.mItemData.getTitle();
         }

         this.setContentDescription(var3);
      } else {
         this.setContentDescription(var3);
      }

      var3 = this.mItemData.getTooltipText();
      if (TextUtils.isEmpty(var3)) {
         if (var1) {
            var3 = (CharSequence)var4;
         } else {
            var3 = this.mItemData.getTitle();
         }

         TooltipCompat.setTooltipText(this, var3);
      } else {
         TooltipCompat.setTooltipText(this, var3);
      }
   }

   public MenuItemImpl getItemData() {
      return this.mItemData;
   }

   public boolean hasText() {
      return TextUtils.isEmpty(this.getText()) ^ true;
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.mItemData = var1;
      this.setIcon(var1.getIcon());
      this.setTitle(var1.getTitleForItemView(this));
      this.setId(var1.getItemId());
      byte var3;
      if (var1.isVisible()) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      this.setVisibility(var3);
      this.setEnabled(var1.isEnabled());
      if (var1.hasSubMenu() && this.mForwardingListener == null) {
         this.mForwardingListener = new ActionMenuItemView.ActionMenuItemForwardingListener();
      }

   }

   public boolean needsDividerAfter() {
      return this.hasText();
   }

   public boolean needsDividerBefore() {
      return this.hasText() && this.mItemData.getIcon() == null;
   }

   public void onClick(View var1) {
      MenuBuilder.ItemInvoker var2 = this.mItemInvoker;
      if (var2 != null) {
         var2.invokeItem(this.mItemData);
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
      this.updateTextButtonVisibility();
   }

   protected void onMeasure(int var1, int var2) {
      boolean var5 = this.hasText();
      int var3;
      if (var5) {
         var3 = this.mSavedPaddingLeft;
         if (var3 >= 0) {
            super.setPadding(var3, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
         }
      }

      super.onMeasure(var1, var2);
      var3 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      int var4 = this.getMeasuredWidth();
      if (var3 == Integer.MIN_VALUE) {
         var1 = Math.min(var1, this.mMinWidth);
      } else {
         var1 = this.mMinWidth;
      }

      if (var3 != 1073741824 && this.mMinWidth > 0 && var4 < var1) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(var1, 1073741824), var2);
      }

      if (!var5 && this.mIcon != null) {
         super.setPadding((this.getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      super.onRestoreInstanceState((Parcelable)null);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (this.mItemData.hasSubMenu()) {
         ForwardingListener var2 = this.mForwardingListener;
         if (var2 != null && var2.onTouch(this, var1)) {
            return true;
         }
      }

      return super.onTouchEvent(var1);
   }

   public boolean prefersCondensedTitle() {
      return true;
   }

   public void setCheckable(boolean var1) {
   }

   public void setChecked(boolean var1) {
   }

   public void setExpandedFormat(boolean var1) {
      if (this.mExpandedFormat != var1) {
         this.mExpandedFormat = var1;
         MenuItemImpl var2 = this.mItemData;
         if (var2 != null) {
            var2.actionFormatChanged();
         }
      }

   }

   public void setIcon(Drawable var1) {
      this.mIcon = var1;
      if (var1 != null) {
         int var6 = var1.getIntrinsicWidth();
         int var5 = var1.getIntrinsicHeight();
         int var7 = this.mMaxIconSize;
         int var3 = var6;
         int var4 = var5;
         float var2;
         if (var6 > var7) {
            var2 = (float)var7 / (float)var6;
            var3 = this.mMaxIconSize;
            var4 = (int)((float)var5 * var2);
         }

         var7 = this.mMaxIconSize;
         var6 = var3;
         var5 = var4;
         if (var4 > var7) {
            var2 = (float)var7 / (float)var4;
            var5 = this.mMaxIconSize;
            var6 = (int)((float)var3 * var2);
         }

         var1.setBounds(0, 0, var6, var5);
      }

      this.setCompoundDrawables(var1, (Drawable)null, (Drawable)null, (Drawable)null);
      this.updateTextButtonVisibility();
   }

   public void setItemInvoker(MenuBuilder.ItemInvoker var1) {
      this.mItemInvoker = var1;
   }

   public void setPadding(int var1, int var2, int var3, int var4) {
      this.mSavedPaddingLeft = var1;
      super.setPadding(var1, var2, var3, var4);
   }

   public void setPopupCallback(ActionMenuItemView.PopupCallback var1) {
      this.mPopupCallback = var1;
   }

   public void setShortcut(boolean var1, char var2) {
   }

   public void setTitle(CharSequence var1) {
      this.mTitle = var1;
      this.updateTextButtonVisibility();
   }

   public boolean showsIcon() {
      return true;
   }

   private class ActionMenuItemForwardingListener extends ForwardingListener {
      public ActionMenuItemForwardingListener() {
         super(ActionMenuItemView.this);
      }

      public ShowableListMenu getPopup() {
         return ActionMenuItemView.this.mPopupCallback != null ? ActionMenuItemView.this.mPopupCallback.getPopup() : null;
      }

      protected boolean onForwardingStarted() {
         MenuBuilder.ItemInvoker var3 = ActionMenuItemView.this.mItemInvoker;
         boolean var2 = false;
         if (var3 != null && ActionMenuItemView.this.mItemInvoker.invokeItem(ActionMenuItemView.this.mItemData)) {
            ShowableListMenu var4 = this.getPopup();
            boolean var1 = var2;
            if (var4 != null) {
               var1 = var2;
               if (var4.isShowing()) {
                  var1 = true;
               }
            }

            return var1;
         } else {
            return false;
         }
      }
   }

   public abstract static class PopupCallback {
      public abstract ShowableListMenu getPopup();
   }
}
