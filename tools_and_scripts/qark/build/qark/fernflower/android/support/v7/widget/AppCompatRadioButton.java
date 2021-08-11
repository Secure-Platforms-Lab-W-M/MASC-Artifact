package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.widget.TintableCompoundButton;
import android.support.v7.appcompat.R$attr;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class AppCompatRadioButton extends RadioButton implements TintableCompoundButton {
   private final AppCompatCompoundButtonHelper mCompoundButtonHelper;

   public AppCompatRadioButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatRadioButton(Context var1, AttributeSet var2) {
      this(var1, var2, R$attr.radioButtonStyle);
   }

   public AppCompatRadioButton(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper(this);
      this.mCompoundButtonHelper.loadFromAttributes(var2, var3);
   }

   public int getCompoundPaddingLeft() {
      int var1 = super.getCompoundPaddingLeft();
      AppCompatCompoundButtonHelper var2 = this.mCompoundButtonHelper;
      return var2 != null ? var2.getCompoundPaddingLeft(var1) : var1;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public ColorStateList getSupportButtonTintList() {
      AppCompatCompoundButtonHelper var1 = this.mCompoundButtonHelper;
      return var1 != null ? var1.getSupportButtonTintList() : null;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public Mode getSupportButtonTintMode() {
      AppCompatCompoundButtonHelper var1 = this.mCompoundButtonHelper;
      return var1 != null ? var1.getSupportButtonTintMode() : null;
   }

   public void setButtonDrawable(@DrawableRes int var1) {
      this.setButtonDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setButtonDrawable(Drawable var1) {
      super.setButtonDrawable(var1);
      AppCompatCompoundButtonHelper var2 = this.mCompoundButtonHelper;
      if (var2 != null) {
         var2.onSetButtonDrawable();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportButtonTintList(@Nullable ColorStateList var1) {
      AppCompatCompoundButtonHelper var2 = this.mCompoundButtonHelper;
      if (var2 != null) {
         var2.setSupportButtonTintList(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportButtonTintMode(@Nullable Mode var1) {
      AppCompatCompoundButtonHelper var2 = this.mCompoundButtonHelper;
      if (var2 != null) {
         var2.setSupportButtonTintMode(var1);
      }

   }
}
