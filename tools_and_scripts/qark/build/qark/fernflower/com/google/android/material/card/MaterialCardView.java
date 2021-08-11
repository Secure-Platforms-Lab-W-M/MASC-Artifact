package com.google.android.material.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;

public class MaterialCardView extends CardView implements Checkable, Shapeable {
   private static final int[] CHECKABLE_STATE_SET = new int[]{16842911};
   private static final int[] CHECKED_STATE_SET = new int[]{16842912};
   private static final int DEF_STYLE_RES;
   private static final int[] DRAGGED_STATE_SET;
   private static final String LOG_TAG = "MaterialCardView";
   private final MaterialCardViewHelper cardViewHelper;
   private boolean checked;
   private boolean dragged;
   private boolean isParentCardViewDoneInitializing;
   private MaterialCardView.OnCheckedChangeListener onCheckedChangeListener;

   static {
      DRAGGED_STATE_SET = new int[]{attr.state_dragged};
      DEF_STYLE_RES = style.Widget_MaterialComponents_CardView;
   }

   public MaterialCardView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public MaterialCardView(Context var1, AttributeSet var2) {
      this(var1, var2, attr.materialCardViewStyle);
   }

   public MaterialCardView(Context var1, AttributeSet var2, int var3) {
      super(ThemeEnforcement.createThemedContext(var1, var2, var3, DEF_STYLE_RES), var2, var3);
      this.checked = false;
      this.dragged = false;
      this.isParentCardViewDoneInitializing = true;
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(this.getContext(), var2, styleable.MaterialCardView, var3, DEF_STYLE_RES);
      MaterialCardViewHelper var5 = new MaterialCardViewHelper(this, var2, var3, DEF_STYLE_RES);
      this.cardViewHelper = var5;
      var5.setCardBackgroundColor(super.getCardBackgroundColor());
      this.cardViewHelper.setUserContentPadding(super.getContentPaddingLeft(), super.getContentPaddingTop(), super.getContentPaddingRight(), super.getContentPaddingBottom());
      this.cardViewHelper.loadFromAttributes(var4);
      var4.recycle();
   }

   // $FF: synthetic method
   static float access$001(MaterialCardView var0) {
      return var0.getRadius();
   }

   private void forceRippleRedrawIfNeeded() {
      if (VERSION.SDK_INT > 26) {
         this.cardViewHelper.forceRippleRedraw();
      }

   }

   public ColorStateList getCardBackgroundColor() {
      return this.cardViewHelper.getCardBackgroundColor();
   }

   float getCardViewRadius() {
      return access$001(this);
   }

   public Drawable getCheckedIcon() {
      return this.cardViewHelper.getCheckedIcon();
   }

   public ColorStateList getCheckedIconTint() {
      return this.cardViewHelper.getCheckedIconTint();
   }

   public int getContentPaddingBottom() {
      return this.cardViewHelper.getUserContentPadding().bottom;
   }

   public int getContentPaddingLeft() {
      return this.cardViewHelper.getUserContentPadding().left;
   }

   public int getContentPaddingRight() {
      return this.cardViewHelper.getUserContentPadding().right;
   }

   public int getContentPaddingTop() {
      return this.cardViewHelper.getUserContentPadding().top;
   }

   public float getProgress() {
      return this.cardViewHelper.getProgress();
   }

   public float getRadius() {
      return this.cardViewHelper.getCornerRadius();
   }

   public ColorStateList getRippleColor() {
      return this.cardViewHelper.getRippleColor();
   }

   public ShapeAppearanceModel getShapeAppearanceModel() {
      return this.cardViewHelper.getShapeAppearanceModel();
   }

   @Deprecated
   public int getStrokeColor() {
      return this.cardViewHelper.getStrokeColor();
   }

   public ColorStateList getStrokeColorStateList() {
      return this.cardViewHelper.getStrokeColorStateList();
   }

   public int getStrokeWidth() {
      return this.cardViewHelper.getStrokeWidth();
   }

   public boolean isCheckable() {
      MaterialCardViewHelper var1 = this.cardViewHelper;
      return var1 != null && var1.isCheckable();
   }

   public boolean isChecked() {
      return this.checked;
   }

   public boolean isDragged() {
      return this.dragged;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      MaterialShapeUtils.setParentAbsoluteElevation(this, this.cardViewHelper.getBackground());
   }

   protected int[] onCreateDrawableState(int var1) {
      int[] var2 = super.onCreateDrawableState(var1 + 3);
      if (this.isCheckable()) {
         mergeDrawableStates(var2, CHECKABLE_STATE_SET);
      }

      if (this.isChecked()) {
         mergeDrawableStates(var2, CHECKED_STATE_SET);
      }

      if (this.isDragged()) {
         mergeDrawableStates(var2, DRAGGED_STATE_SET);
      }

      return var2;
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      var1.setClassName(CardView.class.getName());
      var1.setChecked(this.isChecked());
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName(CardView.class.getName());
      var1.setCheckable(this.isCheckable());
      var1.setClickable(this.isClickable());
      var1.setChecked(this.isChecked());
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      this.cardViewHelper.onMeasure(this.getMeasuredWidth(), this.getMeasuredHeight());
   }

   void setAncestorContentPadding(int var1, int var2, int var3, int var4) {
      super.setContentPadding(var1, var2, var3, var4);
   }

   public void setBackground(Drawable var1) {
      this.setBackgroundDrawable(var1);
   }

   public void setBackgroundDrawable(Drawable var1) {
      if (this.isParentCardViewDoneInitializing) {
         if (!this.cardViewHelper.isBackgroundOverwritten()) {
            Log.i("MaterialCardView", "Setting a custom background is not supported.");
            this.cardViewHelper.setBackgroundOverwritten(true);
         }

         super.setBackgroundDrawable(var1);
      }

   }

   void setBackgroundInternal(Drawable var1) {
      super.setBackgroundDrawable(var1);
   }

   public void setCardBackgroundColor(int var1) {
      this.cardViewHelper.setCardBackgroundColor(ColorStateList.valueOf(var1));
   }

   public void setCardBackgroundColor(ColorStateList var1) {
      this.cardViewHelper.setCardBackgroundColor(var1);
   }

   public void setCardElevation(float var1) {
      super.setCardElevation(var1);
      this.cardViewHelper.updateElevation();
   }

   public void setCheckable(boolean var1) {
      this.cardViewHelper.setCheckable(var1);
   }

   public void setChecked(boolean var1) {
      if (this.checked != var1) {
         this.toggle();
      }

   }

   public void setCheckedIcon(Drawable var1) {
      this.cardViewHelper.setCheckedIcon(var1);
   }

   public void setCheckedIconResource(int var1) {
      this.cardViewHelper.setCheckedIcon(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setCheckedIconTint(ColorStateList var1) {
      this.cardViewHelper.setCheckedIconTint(var1);
   }

   public void setClickable(boolean var1) {
      super.setClickable(var1);
      this.cardViewHelper.updateClickable();
   }

   public void setContentPadding(int var1, int var2, int var3, int var4) {
      this.cardViewHelper.setUserContentPadding(var1, var2, var3, var4);
   }

   public void setDragged(boolean var1) {
      if (this.dragged != var1) {
         this.dragged = var1;
         this.refreshDrawableState();
         this.forceRippleRedrawIfNeeded();
         this.invalidate();
      }

   }

   public void setMaxCardElevation(float var1) {
      super.setMaxCardElevation(var1);
      this.cardViewHelper.updateInsets();
   }

   public void setOnCheckedChangeListener(MaterialCardView.OnCheckedChangeListener var1) {
      this.onCheckedChangeListener = var1;
   }

   public void setPreventCornerOverlap(boolean var1) {
      super.setPreventCornerOverlap(var1);
      this.cardViewHelper.updateInsets();
      this.cardViewHelper.updateContentPadding();
   }

   public void setProgress(float var1) {
      this.cardViewHelper.setProgress(var1);
   }

   public void setRadius(float var1) {
      super.setRadius(var1);
      this.cardViewHelper.setCornerRadius(var1);
   }

   public void setRippleColor(ColorStateList var1) {
      this.cardViewHelper.setRippleColor(var1);
   }

   public void setRippleColorResource(int var1) {
      this.cardViewHelper.setRippleColor(AppCompatResources.getColorStateList(this.getContext(), var1));
   }

   public void setShapeAppearanceModel(ShapeAppearanceModel var1) {
      this.cardViewHelper.setShapeAppearanceModel(var1);
   }

   public void setStrokeColor(int var1) {
      this.cardViewHelper.setStrokeColor(ColorStateList.valueOf(var1));
   }

   public void setStrokeColor(ColorStateList var1) {
      this.cardViewHelper.setStrokeColor(var1);
   }

   public void setStrokeWidth(int var1) {
      this.cardViewHelper.setStrokeWidth(var1);
   }

   public void setUseCompatPadding(boolean var1) {
      super.setUseCompatPadding(var1);
      this.cardViewHelper.updateInsets();
      this.cardViewHelper.updateContentPadding();
   }

   public void toggle() {
      if (this.isCheckable() && this.isEnabled()) {
         this.checked ^= true;
         this.refreshDrawableState();
         this.forceRippleRedrawIfNeeded();
         MaterialCardView.OnCheckedChangeListener var1 = this.onCheckedChangeListener;
         if (var1 != null) {
            var1.onCheckedChanged(this, this.checked);
         }
      }

   }

   public interface OnCheckedChangeListener {
      void onCheckedChanged(MaterialCardView var1, boolean var2);
   }
}
