package com.google.android.material.internal;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;
import androidx.appcompat.R.attr;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;

public class CheckableImageButton extends AppCompatImageButton implements Checkable {
   private static final int[] DRAWABLE_STATE_CHECKED = new int[]{16842912};
   private boolean checkable;
   private boolean checked;
   private boolean pressable;

   public CheckableImageButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CheckableImageButton(Context var1, AttributeSet var2) {
      this(var1, var2, attr.imageButtonStyle);
   }

   public CheckableImageButton(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.checkable = true;
      this.pressable = true;
      ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() {
         public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
            super.onInitializeAccessibilityEvent(var1, var2);
            var2.setChecked(CheckableImageButton.this.isChecked());
         }

         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            var2.setCheckable(CheckableImageButton.this.isCheckable());
            var2.setChecked(CheckableImageButton.this.isChecked());
         }
      });
   }

   public boolean isCheckable() {
      return this.checkable;
   }

   public boolean isChecked() {
      return this.checked;
   }

   public boolean isPressable() {
      return this.pressable;
   }

   public int[] onCreateDrawableState(int var1) {
      return this.checked ? mergeDrawableStates(super.onCreateDrawableState(DRAWABLE_STATE_CHECKED.length + var1), DRAWABLE_STATE_CHECKED) : super.onCreateDrawableState(var1);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof CheckableImageButton.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         CheckableImageButton.SavedState var2 = (CheckableImageButton.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.setChecked(var2.checked);
      }
   }

   protected Parcelable onSaveInstanceState() {
      CheckableImageButton.SavedState var1 = new CheckableImageButton.SavedState(super.onSaveInstanceState());
      var1.checked = this.checked;
      return var1;
   }

   public void setCheckable(boolean var1) {
      if (this.checkable != var1) {
         this.checkable = var1;
         this.sendAccessibilityEvent(0);
      }

   }

   public void setChecked(boolean var1) {
      if (this.checkable && this.checked != var1) {
         this.checked = var1;
         this.refreshDrawableState();
         this.sendAccessibilityEvent(2048);
      }

   }

   public void setPressable(boolean var1) {
      this.pressable = var1;
   }

   public void setPressed(boolean var1) {
      if (this.pressable) {
         super.setPressed(var1);
      }

   }

   public void toggle() {
      this.setChecked(this.checked ^ true);
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public CheckableImageButton.SavedState createFromParcel(Parcel var1) {
            return new CheckableImageButton.SavedState(var1, (ClassLoader)null);
         }

         public CheckableImageButton.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new CheckableImageButton.SavedState(var1, var2);
         }

         public CheckableImageButton.SavedState[] newArray(int var1) {
            return new CheckableImageButton.SavedState[var1];
         }
      };
      boolean checked;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.readFromParcel(var1);
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      private void readFromParcel(Parcel var1) {
         int var2 = var1.readInt();
         boolean var3 = true;
         if (var2 != 1) {
            var3 = false;
         }

         this.checked = var3;
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }
}
