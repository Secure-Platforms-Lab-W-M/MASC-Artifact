package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class FitWindowsLinearLayout extends LinearLayout implements FitWindowsViewGroup {
   private FitWindowsViewGroup.OnFitSystemWindowsListener mListener;

   public FitWindowsLinearLayout(Context var1) {
      super(var1);
   }

   public FitWindowsLinearLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   protected boolean fitSystemWindows(Rect var1) {
      FitWindowsViewGroup.OnFitSystemWindowsListener var2 = this.mListener;
      if (var2 != null) {
         var2.onFitSystemWindows(var1);
      }

      return super.fitSystemWindows(var1);
   }

   public void setOnFitSystemWindowsListener(FitWindowsViewGroup.OnFitSystemWindowsListener var1) {
      this.mListener = var1;
   }
}
