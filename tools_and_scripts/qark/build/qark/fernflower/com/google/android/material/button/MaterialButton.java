package com.google.android.material.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MaterialButton extends AppCompatButton implements Checkable, Shapeable {
   private static final int[] CHECKABLE_STATE_SET = new int[]{16842911};
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private static final int DEF_STYLE_RES;
   public static final int ICON_GRAVITY_END = 3;
   public static final int ICON_GRAVITY_START = 1;
   public static final int ICON_GRAVITY_TEXT_END = 4;
   public static final int ICON_GRAVITY_TEXT_START = 2;
   private static final String LOG_TAG = "MaterialButton";
   private boolean broadcasting;
   private boolean checked;
   private Drawable icon;
   private int iconGravity;
   private int iconLeft;
   private int iconPadding;
   private int iconSize;
   private ColorStateList iconTint;
   private Mode iconTintMode;
   private final MaterialButtonHelper materialButtonHelper;
   private final LinkedHashSet onCheckedChangeListeners;
   private MaterialButton.OnPressedChangeListener onPressedChangeListenerInternal;

   static {
      DEF_STYLE_RES = style.Widget_MaterialComponents_Button;
   }

   public MaterialButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public MaterialButton(Context var1, AttributeSet var2) {
      this(var1, var2, attr.materialButtonStyle);
   }

   public MaterialButton(Context var1, AttributeSet var2, int var3) {
      super(ThemeEnforcement.createThemedContext(var1, var2, var3, DEF_STYLE_RES), var2, var3);
      this.onCheckedChangeListeners = new LinkedHashSet();
      boolean var4 = false;
      this.checked = false;
      this.broadcasting = false;
      Context var5 = this.getContext();
      TypedArray var6 = ThemeEnforcement.obtainStyledAttributes(var5, var2, styleable.MaterialButton, var3, DEF_STYLE_RES);
      this.iconPadding = var6.getDimensionPixelSize(styleable.MaterialButton_iconPadding, 0);
      this.iconTintMode = ViewUtils.parseTintMode(var6.getInt(styleable.MaterialButton_iconTintMode, -1), Mode.SRC_IN);
      this.iconTint = MaterialResources.getColorStateList(this.getContext(), var6, styleable.MaterialButton_iconTint);
      this.icon = MaterialResources.getDrawable(this.getContext(), var6, styleable.MaterialButton_icon);
      this.iconGravity = var6.getInteger(styleable.MaterialButton_iconGravity, 1);
      this.iconSize = var6.getDimensionPixelSize(styleable.MaterialButton_iconSize, 0);
      MaterialButtonHelper var7 = new MaterialButtonHelper(this, ShapeAppearanceModel.builder(var5, var2, var3, DEF_STYLE_RES).build());
      this.materialButtonHelper = var7;
      var7.loadFromAttributes(var6);
      var6.recycle();
      this.setCompoundDrawablePadding(this.iconPadding);
      if (this.icon != null) {
         var4 = true;
      }

      this.updateIcon(var4);
   }

   private String getA11yClassName() {
      Class var1;
      if (this.isCheckable()) {
         var1 = CompoundButton.class;
      } else {
         var1 = Button.class;
      }

      return var1.getName();
   }

   private boolean isLayoutRTL() {
      return ViewCompat.getLayoutDirection(this) == 1;
   }

   private boolean isUsingOriginalBackground() {
      MaterialButtonHelper var1 = this.materialButtonHelper;
      return var1 != null && !var1.isBackgroundOverwritten();
   }

   private void resetIconDrawable(boolean var1) {
      if (var1) {
         TextViewCompat.setCompoundDrawablesRelative(this, this.icon, (Drawable)null, (Drawable)null, (Drawable)null);
      } else {
         TextViewCompat.setCompoundDrawablesRelative(this, (Drawable)null, (Drawable)null, this.icon, (Drawable)null);
      }
   }

   private void updateIcon(boolean var1) {
      Drawable var7 = this.icon;
      boolean var4 = false;
      int var2;
      if (var7 != null) {
         var7 = DrawableCompat.wrap(var7).mutate();
         this.icon = var7;
         DrawableCompat.setTintList(var7, this.iconTint);
         Mode var10 = this.iconTintMode;
         if (var10 != null) {
            DrawableCompat.setTintMode(this.icon, var10);
         }

         var2 = this.iconSize;
         if (var2 == 0) {
            var2 = this.icon.getIntrinsicWidth();
         }

         int var3 = this.iconSize;
         if (var3 == 0) {
            var3 = this.icon.getIntrinsicHeight();
         }

         var7 = this.icon;
         int var5 = this.iconLeft;
         var7.setBounds(var5, 0, var5 + var2, var3);
      }

      var2 = this.iconGravity;
      boolean var6;
      if (var2 != 1 && var2 != 2) {
         var6 = false;
      } else {
         var6 = true;
      }

      if (var1) {
         this.resetIconDrawable(var6);
      } else {
         boolean var9;
         label62: {
            Drawable[] var8 = TextViewCompat.getCompoundDrawablesRelative(this);
            var7 = var8[0];
            Drawable var11 = var8[2];
            if (!var6 || var7 == this.icon) {
               var9 = var4;
               if (var6) {
                  break label62;
               }

               var9 = var4;
               if (var11 == this.icon) {
                  break label62;
               }
            }

            var9 = true;
         }

         if (var9) {
            this.resetIconDrawable(var6);
         }

      }
   }

   private void updateIconPosition() {
      if (this.icon != null) {
         if (this.getLayout() != null) {
            int var1 = this.iconGravity;
            boolean var4 = true;
            if (var1 != 1 && var1 != 3) {
               TextPaint var8 = this.getPaint();
               String var7 = this.getText().toString();
               String var6 = var7;
               if (this.getTransformationMethod() != null) {
                  var6 = this.getTransformationMethod().getTransformation(var7, this).toString();
               }

               int var3 = Math.min((int)var8.measureText(var6), this.getLayout().getEllipsizedWidth());
               int var2 = this.iconSize;
               var1 = var2;
               if (var2 == 0) {
                  var1 = this.icon.getIntrinsicWidth();
               }

               var2 = (this.getMeasuredWidth() - var3 - ViewCompat.getPaddingEnd(this) - var1 - this.iconPadding - ViewCompat.getPaddingStart(this)) / 2;
               boolean var5 = this.isLayoutRTL();
               if (this.iconGravity != 4) {
                  var4 = false;
               }

               var1 = var2;
               if (var5 != var4) {
                  var1 = -var2;
               }

               if (this.iconLeft != var1) {
                  this.iconLeft = var1;
                  this.updateIcon(false);
               }

            } else {
               this.iconLeft = 0;
               this.updateIcon(false);
            }
         }
      }
   }

   public void addOnCheckedChangeListener(MaterialButton.OnCheckedChangeListener var1) {
      this.onCheckedChangeListeners.add(var1);
   }

   public void clearOnCheckedChangeListeners() {
      this.onCheckedChangeListeners.clear();
   }

   public ColorStateList getBackgroundTintList() {
      return this.getSupportBackgroundTintList();
   }

   public Mode getBackgroundTintMode() {
      return this.getSupportBackgroundTintMode();
   }

   public int getCornerRadius() {
      return this.isUsingOriginalBackground() ? this.materialButtonHelper.getCornerRadius() : 0;
   }

   public Drawable getIcon() {
      return this.icon;
   }

   public int getIconGravity() {
      return this.iconGravity;
   }

   public int getIconPadding() {
      return this.iconPadding;
   }

   public int getIconSize() {
      return this.iconSize;
   }

   public ColorStateList getIconTint() {
      return this.iconTint;
   }

   public Mode getIconTintMode() {
      return this.iconTintMode;
   }

   public ColorStateList getRippleColor() {
      return this.isUsingOriginalBackground() ? this.materialButtonHelper.getRippleColor() : null;
   }

   public ShapeAppearanceModel getShapeAppearanceModel() {
      if (this.isUsingOriginalBackground()) {
         return this.materialButtonHelper.getShapeAppearanceModel();
      } else {
         throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
      }
   }

   public ColorStateList getStrokeColor() {
      return this.isUsingOriginalBackground() ? this.materialButtonHelper.getStrokeColor() : null;
   }

   public int getStrokeWidth() {
      return this.isUsingOriginalBackground() ? this.materialButtonHelper.getStrokeWidth() : 0;
   }

   public ColorStateList getSupportBackgroundTintList() {
      return this.isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintList() : super.getSupportBackgroundTintList();
   }

   public Mode getSupportBackgroundTintMode() {
      return this.isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintMode() : super.getSupportBackgroundTintMode();
   }

   public boolean isCheckable() {
      MaterialButtonHelper var1 = this.materialButtonHelper;
      return var1 != null && var1.isCheckable();
   }

   public boolean isChecked() {
      return this.checked;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      MaterialShapeUtils.setParentAbsoluteElevation(this, this.materialButtonHelper.getMaterialShapeDrawable());
   }

   protected int[] onCreateDrawableState(int var1) {
      int[] var2 = super.onCreateDrawableState(var1 + 2);
      if (this.isCheckable()) {
         mergeDrawableStates(var2, CHECKABLE_STATE_SET);
      }

      if (this.isChecked()) {
         mergeDrawableStates(var2, CHECKED_STATE_SET);
      }

      return var2;
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      var1.setClassName(this.getA11yClassName());
      var1.setChecked(this.isChecked());
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName(this.getA11yClassName());
      var1.setCheckable(this.isCheckable());
      var1.setChecked(this.isChecked());
      var1.setClickable(this.isClickable());
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (VERSION.SDK_INT == 21) {
         MaterialButtonHelper var6 = this.materialButtonHelper;
         if (var6 != null) {
            var6.updateMaskBounds(var5 - var3, var4 - var2);
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      this.updateIconPosition();
   }

   protected void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      super.onTextChanged(var1, var2, var3, var4);
      this.updateIconPosition();
   }

   public boolean performClick() {
      this.toggle();
      return super.performClick();
   }

   public void removeOnCheckedChangeListener(MaterialButton.OnCheckedChangeListener var1) {
      this.onCheckedChangeListeners.remove(var1);
   }

   public void setBackground(Drawable var1) {
      this.setBackgroundDrawable(var1);
   }

   public void setBackgroundColor(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setBackgroundColor(var1);
      } else {
         super.setBackgroundColor(var1);
      }
   }

   public void setBackgroundDrawable(Drawable var1) {
      if (this.isUsingOriginalBackground()) {
         if (var1 != this.getBackground()) {
            Log.w("MaterialButton", "Do not set the background; MaterialButton manages its own background drawable.");
            this.materialButtonHelper.setBackgroundOverwritten();
            super.setBackgroundDrawable(var1);
         } else {
            this.getBackground().setState(var1.getState());
         }
      } else {
         super.setBackgroundDrawable(var1);
      }
   }

   public void setBackgroundResource(int var1) {
      Drawable var2 = null;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      }

      this.setBackgroundDrawable(var2);
   }

   public void setBackgroundTintList(ColorStateList var1) {
      this.setSupportBackgroundTintList(var1);
   }

   public void setBackgroundTintMode(Mode var1) {
      this.setSupportBackgroundTintMode(var1);
   }

   public void setCheckable(boolean var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setCheckable(var1);
      }

   }

   public void setChecked(boolean var1) {
      if (this.isCheckable() && this.isEnabled() && this.checked != var1) {
         this.checked = var1;
         this.refreshDrawableState();
         if (this.broadcasting) {
            return;
         }

         this.broadcasting = true;
         Iterator var2 = this.onCheckedChangeListeners.iterator();

         while(var2.hasNext()) {
            ((MaterialButton.OnCheckedChangeListener)var2.next()).onCheckedChanged(this, this.checked);
         }

         this.broadcasting = false;
      }

   }

   public void setCornerRadius(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setCornerRadius(var1);
      }

   }

   public void setCornerRadiusResource(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.setCornerRadius(this.getResources().getDimensionPixelSize(var1));
      }

   }

   public void setElevation(float var1) {
      super.setElevation(var1);
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.getMaterialShapeDrawable().setElevation(var1);
      }

   }

   public void setIcon(Drawable var1) {
      if (this.icon != var1) {
         this.icon = var1;
         this.updateIcon(true);
      }

   }

   public void setIconGravity(int var1) {
      if (this.iconGravity != var1) {
         this.iconGravity = var1;
         this.updateIconPosition();
      }

   }

   public void setIconPadding(int var1) {
      if (this.iconPadding != var1) {
         this.iconPadding = var1;
         this.setCompoundDrawablePadding(var1);
      }

   }

   public void setIconResource(int var1) {
      Drawable var2 = null;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      }

      this.setIcon(var2);
   }

   public void setIconSize(int var1) {
      if (var1 >= 0) {
         if (this.iconSize != var1) {
            this.iconSize = var1;
            this.updateIcon(true);
         }

      } else {
         throw new IllegalArgumentException("iconSize cannot be less than 0");
      }
   }

   public void setIconTint(ColorStateList var1) {
      if (this.iconTint != var1) {
         this.iconTint = var1;
         this.updateIcon(false);
      }

   }

   public void setIconTintMode(Mode var1) {
      if (this.iconTintMode != var1) {
         this.iconTintMode = var1;
         this.updateIcon(false);
      }

   }

   public void setIconTintResource(int var1) {
      this.setIconTint(AppCompatResources.getColorStateList(this.getContext(), var1));
   }

   void setInternalBackground(Drawable var1) {
      super.setBackgroundDrawable(var1);
   }

   void setOnPressedChangeListenerInternal(MaterialButton.OnPressedChangeListener var1) {
      this.onPressedChangeListenerInternal = var1;
   }

   public void setPressed(boolean var1) {
      MaterialButton.OnPressedChangeListener var2 = this.onPressedChangeListenerInternal;
      if (var2 != null) {
         var2.onPressedChanged(this, var1);
      }

      super.setPressed(var1);
   }

   public void setRippleColor(ColorStateList var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setRippleColor(var1);
      }

   }

   public void setRippleColorResource(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.setRippleColor(AppCompatResources.getColorStateList(this.getContext(), var1));
      }

   }

   public void setShapeAppearanceModel(ShapeAppearanceModel var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setShapeAppearanceModel(var1);
      } else {
         throw new IllegalStateException("Attempted to set ShapeAppearanceModel on a MaterialButton which has an overwritten background.");
      }
   }

   void setShouldDrawSurfaceColorStroke(boolean var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setShouldDrawSurfaceColorStroke(var1);
      }

   }

   public void setStrokeColor(ColorStateList var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setStrokeColor(var1);
      }

   }

   public void setStrokeColorResource(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.setStrokeColor(AppCompatResources.getColorStateList(this.getContext(), var1));
      }

   }

   public void setStrokeWidth(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setStrokeWidth(var1);
      }

   }

   public void setStrokeWidthResource(int var1) {
      if (this.isUsingOriginalBackground()) {
         this.setStrokeWidth(this.getResources().getDimensionPixelSize(var1));
      }

   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setSupportBackgroundTintList(var1);
      } else {
         super.setSupportBackgroundTintList(var1);
      }
   }

   public void setSupportBackgroundTintMode(Mode var1) {
      if (this.isUsingOriginalBackground()) {
         this.materialButtonHelper.setSupportBackgroundTintMode(var1);
      } else {
         super.setSupportBackgroundTintMode(var1);
      }
   }

   public void toggle() {
      this.setChecked(this.checked ^ true);
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface IconGravity {
   }

   public interface OnCheckedChangeListener {
      void onCheckedChanged(MaterialButton var1, boolean var2);
   }

   interface OnPressedChangeListener {
      void onPressedChanged(MaterialButton var1, boolean var2);
   }
}
