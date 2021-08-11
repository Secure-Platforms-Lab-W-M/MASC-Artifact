package com.google.android.material.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatDialogFragment;

public class BottomSheetDialogFragment extends AppCompatDialogFragment {
   private boolean waitingForDismissAllowingStateLoss;

   private void dismissAfterAnimation() {
      if (this.waitingForDismissAllowingStateLoss) {
         super.dismissAllowingStateLoss();
      } else {
         super.dismiss();
      }
   }

   private void dismissWithAnimation(BottomSheetBehavior var1, boolean var2) {
      this.waitingForDismissAllowingStateLoss = var2;
      if (var1.getState() == 5) {
         this.dismissAfterAnimation();
      } else {
         if (this.getDialog() instanceof BottomSheetDialog) {
            ((BottomSheetDialog)this.getDialog()).removeDefaultCallback();
         }

         var1.addBottomSheetCallback(new BottomSheetDialogFragment.BottomSheetDismissCallback());
         var1.setState(5);
      }
   }

   private boolean tryDismissWithAnimation(boolean var1) {
      Dialog var2 = this.getDialog();
      if (var2 instanceof BottomSheetDialog) {
         BottomSheetDialog var4 = (BottomSheetDialog)var2;
         BottomSheetBehavior var3 = var4.getBehavior();
         if (var3.isHideable() && var4.getDismissWithAnimation()) {
            this.dismissWithAnimation(var3, var1);
            return true;
         }
      }

      return false;
   }

   public void dismiss() {
      if (!this.tryDismissWithAnimation(false)) {
         super.dismiss();
      }

   }

   public void dismissAllowingStateLoss() {
      if (!this.tryDismissWithAnimation(true)) {
         super.dismissAllowingStateLoss();
      }

   }

   public Dialog onCreateDialog(Bundle var1) {
      return new BottomSheetDialog(this.getContext(), this.getTheme());
   }

   private class BottomSheetDismissCallback extends BottomSheetBehavior.BottomSheetCallback {
      private BottomSheetDismissCallback() {
      }

      // $FF: synthetic method
      BottomSheetDismissCallback(Object var2) {
         this();
      }

      public void onSlide(View var1, float var2) {
      }

      public void onStateChanged(View var1, int var2) {
         if (var2 == 5) {
            BottomSheetDialogFragment.this.dismissAfterAnimation();
         }

      }
   }
}
