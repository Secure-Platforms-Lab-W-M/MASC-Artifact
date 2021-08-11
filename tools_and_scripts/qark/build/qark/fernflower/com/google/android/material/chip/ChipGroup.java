package com.google.android.material.chip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.CompoundButton;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.internal.FlowLayout;
import com.google.android.material.internal.ThemeEnforcement;

public class ChipGroup extends FlowLayout {
   private int checkedId;
   private final ChipGroup.CheckedStateTracker checkedStateTracker;
   private int chipSpacingHorizontal;
   private int chipSpacingVertical;
   private ChipGroup.OnCheckedChangeListener onCheckedChangeListener;
   private ChipGroup.PassThroughHierarchyChangeListener passThroughListener;
   private boolean protectFromCheckedChange;
   private boolean singleSelection;

   public ChipGroup(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ChipGroup(Context var1, AttributeSet var2) {
      this(var1, var2, attr.chipGroupStyle);
   }

   public ChipGroup(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.checkedStateTracker = new ChipGroup.CheckedStateTracker();
      this.passThroughListener = new ChipGroup.PassThroughHierarchyChangeListener();
      this.checkedId = -1;
      this.protectFromCheckedChange = false;
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.ChipGroup, var3, style.Widget_MaterialComponents_ChipGroup);
      var3 = var4.getDimensionPixelOffset(styleable.ChipGroup_chipSpacing, 0);
      this.setChipSpacingHorizontal(var4.getDimensionPixelOffset(styleable.ChipGroup_chipSpacingHorizontal, var3));
      this.setChipSpacingVertical(var4.getDimensionPixelOffset(styleable.ChipGroup_chipSpacingVertical, var3));
      this.setSingleLine(var4.getBoolean(styleable.ChipGroup_singleLine, false));
      this.setSingleSelection(var4.getBoolean(styleable.ChipGroup_singleSelection, false));
      var3 = var4.getResourceId(styleable.ChipGroup_checkedChip, -1);
      if (var3 != -1) {
         this.checkedId = var3;
      }

      var4.recycle();
      super.setOnHierarchyChangeListener(this.passThroughListener);
   }

   private void setCheckedId(int var1) {
      this.checkedId = var1;
      ChipGroup.OnCheckedChangeListener var2 = this.onCheckedChangeListener;
      if (var2 != null && this.singleSelection) {
         var2.onCheckedChanged(this, var1);
      }

   }

   private void setCheckedStateForView(int var1, boolean var2) {
      View var3 = this.findViewById(var1);
      if (var3 instanceof Chip) {
         this.protectFromCheckedChange = true;
         ((Chip)var3).setChecked(var2);
         this.protectFromCheckedChange = false;
      }

   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      if (var1 instanceof Chip) {
         Chip var5 = (Chip)var1;
         if (var5.isChecked()) {
            int var4 = this.checkedId;
            if (var4 != -1 && this.singleSelection) {
               this.setCheckedStateForView(var4, false);
            }

            this.setCheckedId(var5.getId());
         }
      }

      super.addView(var1, var2, var3);
   }

   public void check(int var1) {
      int var2 = this.checkedId;
      if (var1 != var2) {
         if (var2 != -1 && this.singleSelection) {
            this.setCheckedStateForView(var2, false);
         }

         if (var1 != -1) {
            this.setCheckedStateForView(var1, true);
         }

         this.setCheckedId(var1);
      }
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return super.checkLayoutParams(var1) && var1 instanceof ChipGroup.LayoutParams;
   }

   public void clearCheck() {
      this.protectFromCheckedChange = true;

      for(int var1 = 0; var1 < this.getChildCount(); ++var1) {
         View var2 = this.getChildAt(var1);
         if (var2 instanceof Chip) {
            ((Chip)var2).setChecked(false);
         }
      }

      this.protectFromCheckedChange = false;
      this.setCheckedId(-1);
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new ChipGroup.LayoutParams(-2, -2);
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ChipGroup.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return new ChipGroup.LayoutParams(var1);
   }

   public int getCheckedChipId() {
      return this.singleSelection ? this.checkedId : -1;
   }

   public int getChipSpacingHorizontal() {
      return this.chipSpacingHorizontal;
   }

   public int getChipSpacingVertical() {
      return this.chipSpacingVertical;
   }

   public boolean isSingleLine() {
      return super.isSingleLine();
   }

   public boolean isSingleSelection() {
      return this.singleSelection;
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      int var1 = this.checkedId;
      if (var1 != -1) {
         this.setCheckedStateForView(var1, true);
         this.setCheckedId(this.checkedId);
      }

   }

   public void setChipSpacing(int var1) {
      this.setChipSpacingHorizontal(var1);
      this.setChipSpacingVertical(var1);
   }

   public void setChipSpacingHorizontal(int var1) {
      if (this.chipSpacingHorizontal != var1) {
         this.chipSpacingHorizontal = var1;
         this.setItemSpacing(var1);
         this.requestLayout();
      }

   }

   public void setChipSpacingHorizontalResource(int var1) {
      this.setChipSpacingHorizontal(this.getResources().getDimensionPixelOffset(var1));
   }

   public void setChipSpacingResource(int var1) {
      this.setChipSpacing(this.getResources().getDimensionPixelOffset(var1));
   }

   public void setChipSpacingVertical(int var1) {
      if (this.chipSpacingVertical != var1) {
         this.chipSpacingVertical = var1;
         this.setLineSpacing(var1);
         this.requestLayout();
      }

   }

   public void setChipSpacingVerticalResource(int var1) {
      this.setChipSpacingVertical(this.getResources().getDimensionPixelOffset(var1));
   }

   @Deprecated
   public void setDividerDrawableHorizontal(Drawable var1) {
      throw new UnsupportedOperationException("Changing divider drawables have no effect. ChipGroup do not use divider drawables as spacing.");
   }

   @Deprecated
   public void setDividerDrawableVertical(Drawable var1) {
      throw new UnsupportedOperationException("Changing divider drawables have no effect. ChipGroup do not use divider drawables as spacing.");
   }

   @Deprecated
   public void setFlexWrap(int var1) {
      throw new UnsupportedOperationException("Changing flex wrap not allowed. ChipGroup exposes a singleLine attribute instead.");
   }

   public void setOnCheckedChangeListener(ChipGroup.OnCheckedChangeListener var1) {
      this.onCheckedChangeListener = var1;
   }

   public void setOnHierarchyChangeListener(OnHierarchyChangeListener var1) {
      this.passThroughListener.onHierarchyChangeListener = var1;
   }

   @Deprecated
   public void setShowDividerHorizontal(int var1) {
      throw new UnsupportedOperationException("Changing divider modes has no effect. ChipGroup do not use divider drawables as spacing.");
   }

   @Deprecated
   public void setShowDividerVertical(int var1) {
      throw new UnsupportedOperationException("Changing divider modes has no effect. ChipGroup do not use divider drawables as spacing.");
   }

   public void setSingleLine(int var1) {
      this.setSingleLine(this.getResources().getBoolean(var1));
   }

   public void setSingleLine(boolean var1) {
      super.setSingleLine(var1);
   }

   public void setSingleSelection(int var1) {
      this.setSingleSelection(this.getResources().getBoolean(var1));
   }

   public void setSingleSelection(boolean var1) {
      if (this.singleSelection != var1) {
         this.singleSelection = var1;
         this.clearCheck();
      }

   }

   private class CheckedStateTracker implements android.widget.CompoundButton.OnCheckedChangeListener {
      private CheckedStateTracker() {
      }

      // $FF: synthetic method
      CheckedStateTracker(Object var2) {
         this();
      }

      public void onCheckedChanged(CompoundButton var1, boolean var2) {
         if (!ChipGroup.this.protectFromCheckedChange) {
            int var3 = var1.getId();
            if (var2) {
               if (ChipGroup.this.checkedId != -1 && ChipGroup.this.checkedId != var3 && ChipGroup.this.singleSelection) {
                  ChipGroup var4 = ChipGroup.this;
                  var4.setCheckedStateForView(var4.checkedId, false);
               }

               ChipGroup.this.setCheckedId(var3);
            } else {
               if (ChipGroup.this.checkedId == var3) {
                  ChipGroup.this.setCheckedId(-1);
               }

            }
         }
      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }
   }

   public interface OnCheckedChangeListener {
      void onCheckedChanged(ChipGroup var1, int var2);
   }

   private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
      private OnHierarchyChangeListener onHierarchyChangeListener;

      private PassThroughHierarchyChangeListener() {
      }

      // $FF: synthetic method
      PassThroughHierarchyChangeListener(Object var2) {
         this();
      }

      public void onChildViewAdded(View var1, View var2) {
         if (var1 == ChipGroup.this && var2 instanceof Chip) {
            if (var2.getId() == -1) {
               int var3;
               if (VERSION.SDK_INT >= 17) {
                  var3 = View.generateViewId();
               } else {
                  var3 = var2.hashCode();
               }

               var2.setId(var3);
            }

            ((Chip)var2).setOnCheckedChangeListenerInternal(ChipGroup.this.checkedStateTracker);
         }

         OnHierarchyChangeListener var4 = this.onHierarchyChangeListener;
         if (var4 != null) {
            var4.onChildViewAdded(var1, var2);
         }

      }

      public void onChildViewRemoved(View var1, View var2) {
         if (var1 == ChipGroup.this && var2 instanceof Chip) {
            ((Chip)var2).setOnCheckedChangeListenerInternal((android.widget.CompoundButton.OnCheckedChangeListener)null);
         }

         OnHierarchyChangeListener var3 = this.onHierarchyChangeListener;
         if (var3 != null) {
            var3.onChildViewRemoved(var1, var2);
         }

      }
   }
}
