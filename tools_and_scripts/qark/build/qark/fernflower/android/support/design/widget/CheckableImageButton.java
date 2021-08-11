package android.support.design.widget;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.R$attr;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class CheckableImageButton extends AppCompatImageButton implements Checkable {
   private static final int[] DRAWABLE_STATE_CHECKED = new int[]{16842912};
   private boolean mChecked;

   public CheckableImageButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CheckableImageButton(Context var1, AttributeSet var2) {
      this(var1, var2, R$attr.imageButtonStyle);
   }

   public CheckableImageButton(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() {
         public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
            super.onInitializeAccessibilityEvent(var1, var2);
            var2.setChecked(CheckableImageButton.this.isChecked());
         }

         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            var2.setCheckable(true);
            var2.setChecked(CheckableImageButton.this.isChecked());
         }
      });
   }

   public boolean isChecked() {
      return this.mChecked;
   }

   public int[] onCreateDrawableState(int var1) {
      return this.mChecked ? mergeDrawableStates(super.onCreateDrawableState(DRAWABLE_STATE_CHECKED.length + var1), DRAWABLE_STATE_CHECKED) : super.onCreateDrawableState(var1);
   }

   public void setChecked(boolean var1) {
      if (this.mChecked != var1) {
         this.mChecked = var1;
         this.refreshDrawableState();
         this.sendAccessibilityEvent(2048);
      }
   }

   public void toggle() {
      this.setChecked(this.mChecked ^ true);
   }
}
