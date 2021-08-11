package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$id;
import android.support.v7.appcompat.R$layout;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ListMenuItemView extends LinearLayout implements MenuView.ItemView {
   private static final String TAG = "ListMenuItemView";
   private Drawable mBackground;
   private CheckBox mCheckBox;
   private boolean mForceShowIcon;
   private ImageView mIconView;
   private LayoutInflater mInflater;
   private MenuItemImpl mItemData;
   private int mMenuType;
   private boolean mPreserveIconSpacing;
   private RadioButton mRadioButton;
   private TextView mShortcutView;
   private Drawable mSubMenuArrow;
   private ImageView mSubMenuArrowView;
   private int mTextAppearance;
   private Context mTextAppearanceContext;
   private TextView mTitleView;

   public ListMenuItemView(Context var1, AttributeSet var2) {
      this(var1, var2, R$attr.listMenuViewStyle);
   }

   public ListMenuItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2);
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(this.getContext(), var2, R$styleable.MenuView, var3, 0);
      this.mBackground = var4.getDrawable(R$styleable.MenuView_android_itemBackground);
      this.mTextAppearance = var4.getResourceId(R$styleable.MenuView_android_itemTextAppearance, -1);
      this.mPreserveIconSpacing = var4.getBoolean(R$styleable.MenuView_preserveIconSpacing, false);
      this.mTextAppearanceContext = var1;
      this.mSubMenuArrow = var4.getDrawable(R$styleable.MenuView_subMenuArrow);
      var4.recycle();
   }

   private LayoutInflater getInflater() {
      if (this.mInflater == null) {
         this.mInflater = LayoutInflater.from(this.getContext());
      }

      return this.mInflater;
   }

   private void insertCheckBox() {
      this.mCheckBox = (CheckBox)this.getInflater().inflate(R$layout.abc_list_menu_item_checkbox, this, false);
      this.addView(this.mCheckBox);
   }

   private void insertIconView() {
      this.mIconView = (ImageView)this.getInflater().inflate(R$layout.abc_list_menu_item_icon, this, false);
      this.addView(this.mIconView, 0);
   }

   private void insertRadioButton() {
      this.mRadioButton = (RadioButton)this.getInflater().inflate(R$layout.abc_list_menu_item_radio, this, false);
      this.addView(this.mRadioButton);
   }

   private void setSubMenuArrowVisible(boolean var1) {
      ImageView var3 = this.mSubMenuArrowView;
      if (var3 != null) {
         byte var2;
         if (var1) {
            var2 = 0;
         } else {
            var2 = 8;
         }

         var3.setVisibility(var2);
      }

   }

   public MenuItemImpl getItemData() {
      return this.mItemData;
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.mItemData = var1;
      this.mMenuType = var2;
      byte var3;
      if (var1.isVisible()) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      this.setVisibility(var3);
      this.setTitle(var1.getTitleForItemView(this));
      this.setCheckable(var1.isCheckable());
      this.setShortcut(var1.shouldShowShortcut(), var1.getShortcut());
      this.setIcon(var1.getIcon());
      this.setEnabled(var1.isEnabled());
      this.setSubMenuArrowVisible(var1.hasSubMenu());
      this.setContentDescription(var1.getContentDescription());
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      ViewCompat.setBackground(this, this.mBackground);
      this.mTitleView = (TextView)this.findViewById(R$id.title);
      int var1 = this.mTextAppearance;
      if (var1 != -1) {
         this.mTitleView.setTextAppearance(this.mTextAppearanceContext, var1);
      }

      this.mShortcutView = (TextView)this.findViewById(R$id.shortcut);
      this.mSubMenuArrowView = (ImageView)this.findViewById(R$id.submenuarrow);
      ImageView var2 = this.mSubMenuArrowView;
      if (var2 != null) {
         var2.setImageDrawable(this.mSubMenuArrow);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (this.mIconView != null && this.mPreserveIconSpacing) {
         LayoutParams var3 = this.getLayoutParams();
         android.widget.LinearLayout.LayoutParams var4 = (android.widget.LinearLayout.LayoutParams)this.mIconView.getLayoutParams();
         if (var3.height > 0 && var4.width <= 0) {
            var4.width = var3.height;
         }
      }

      super.onMeasure(var1, var2);
   }

   public boolean prefersCondensedTitle() {
      return false;
   }

   public void setCheckable(boolean var1) {
      if (var1 || this.mRadioButton != null || this.mCheckBox != null) {
         Object var3;
         Object var4;
         if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
               this.insertRadioButton();
            }

            var3 = this.mRadioButton;
            var4 = this.mCheckBox;
         } else {
            if (this.mCheckBox == null) {
               this.insertCheckBox();
            }

            var3 = this.mCheckBox;
            var4 = this.mRadioButton;
         }

         if (var1) {
            ((CompoundButton)var3).setChecked(this.mItemData.isChecked());
            byte var2;
            if (var1) {
               var2 = 0;
            } else {
               var2 = 8;
            }

            if (((CompoundButton)var3).getVisibility() != var2) {
               ((CompoundButton)var3).setVisibility(var2);
            }

            if (var4 != null && ((CompoundButton)var4).getVisibility() != 8) {
               ((CompoundButton)var4).setVisibility(8);
            }

         } else {
            CheckBox var5 = this.mCheckBox;
            if (var5 != null) {
               var5.setVisibility(8);
            }

            RadioButton var6 = this.mRadioButton;
            if (var6 != null) {
               var6.setVisibility(8);
            }

         }
      }
   }

   public void setChecked(boolean var1) {
      Object var2;
      if (this.mItemData.isExclusiveCheckable()) {
         if (this.mRadioButton == null) {
            this.insertRadioButton();
         }

         var2 = this.mRadioButton;
      } else {
         if (this.mCheckBox == null) {
            this.insertCheckBox();
         }

         var2 = this.mCheckBox;
      }

      ((CompoundButton)var2).setChecked(var1);
   }

   public void setForceShowIcon(boolean var1) {
      this.mForceShowIcon = var1;
      this.mPreserveIconSpacing = var1;
   }

   public void setIcon(Drawable var1) {
      boolean var2;
      if (!this.mItemData.shouldShowIcon() && !this.mForceShowIcon) {
         var2 = false;
      } else {
         var2 = true;
      }

      if (var2 || this.mPreserveIconSpacing) {
         if (this.mIconView != null || var1 != null || this.mPreserveIconSpacing) {
            if (this.mIconView == null) {
               this.insertIconView();
            }

            if (var1 == null && !this.mPreserveIconSpacing) {
               this.mIconView.setVisibility(8);
            } else {
               ImageView var3 = this.mIconView;
               if (!var2) {
                  var1 = null;
               }

               var3.setImageDrawable(var1);
               if (this.mIconView.getVisibility() != 0) {
                  this.mIconView.setVisibility(0);
               }

            }
         }
      }
   }

   public void setShortcut(boolean var1, char var2) {
      byte var3;
      if (var1 && this.mItemData.shouldShowShortcut()) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      if (var3 == 0) {
         this.mShortcutView.setText(this.mItemData.getShortcutLabel());
      }

      if (this.mShortcutView.getVisibility() != var3) {
         this.mShortcutView.setVisibility(var3);
      }

   }

   public void setTitle(CharSequence var1) {
      if (var1 != null) {
         this.mTitleView.setText(var1);
         if (this.mTitleView.getVisibility() != 0) {
            this.mTitleView.setVisibility(0);
            return;
         }
      } else if (this.mTitleView.getVisibility() != 8) {
         this.mTitleView.setVisibility(8);
      }

   }

   public boolean showsIcon() {
      return this.mForceShowIcon;
   }
}
