package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;
import androidx.appcompat.R.attr;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.TintableCompoundButton;

public class AppCompatRadioButton extends RadioButton implements TintableCompoundButton, TintableBackgroundView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private final AppCompatCompoundButtonHelper mCompoundButtonHelper;
   private final AppCompatTextHelper mTextHelper;

   public AppCompatRadioButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatRadioButton(Context var1, AttributeSet var2) {
      this(var1, var2, attr.radioButtonStyle);
   }

   public AppCompatRadioButton(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      AppCompatCompoundButtonHelper var4 = new AppCompatCompoundButtonHelper(this);
      this.mCompoundButtonHelper = var4;
      var4.loadFromAttributes(var2, var3);
      AppCompatBackgroundHelper var5 = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper = var5;
      var5.loadFromAttributes(var2, var3);
      AppCompatTextHelper var6 = new AppCompatTextHelper(this);
      this.mTextHelper = var6;
      var6.loadFromAttributes(var2, var3);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      if (var1 != null) {
         var1.applySupportBackgroundTint();
      }

      AppCompatTextHelper var2 = this.mTextHelper;
      if (var2 != null) {
         var2.applyCompoundDrawablesTints();
      }

   }

   public int getCompoundPaddingLeft() {
      int var1 = super.getCompoundPaddingLeft();
      AppCompatCompoundButtonHelper var2 = this.mCompoundButtonHelper;
      return var2 != null ? var2.getCompoundPaddingLeft(var1) : var1;
   }

   public ColorStateList getSupportBackgroundTintList() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      return var1 != null ? var1.getSupportBackgroundTintList() : null;
   }

   public Mode getSupportBackgroundTintMode() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      return var1 != null ? var1.getSupportBackgroundTintMode() : null;
   }

   public ColorStateList getSupportButtonTintList() {
      AppCompatCompoundButtonHelper var1 = this.mCompoundButtonHelper;
      return var1 != null ? var1.getSupportButtonTintList() : null;
   }

   public Mode getSupportButtonTintMode() {
      AppCompatCompoundButtonHelper var1 = this.mCompoundButtonHelper;
      return var1 != null ? var1.getSupportButtonTintMode() : null;
   }

   public void setBackgroundDrawable(Drawable var1) {
      super.setBackgroundDrawable(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundDrawable(var1);
      }

   }

   public void setBackgroundResource(int var1) {
      super.setBackgroundResource(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundResource(var1);
      }

   }

   public void setButtonDrawable(int var1) {
      this.setButtonDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setButtonDrawable(Drawable var1) {
      super.setButtonDrawable(var1);
      AppCompatCompoundButtonHelper var2 = this.mCompoundButtonHelper;
      if (var2 != null) {
         var2.onSetButtonDrawable();
      }

   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintList(var1);
      }

   }

   public void setSupportBackgroundTintMode(Mode var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintMode(var1);
      }

   }

   public void setSupportButtonTintList(ColorStateList var1) {
      AppCompatCompoundButtonHelper var2 = this.mCompoundButtonHelper;
      if (var2 != null) {
         var2.setSupportButtonTintList(var1);
      }

   }

   public void setSupportButtonTintMode(Mode var1) {
      AppCompatCompoundButtonHelper var2 = this.mCompoundButtonHelper;
      if (var2 != null) {
         var2.setSupportButtonTintMode(var1);
      }

   }
}
