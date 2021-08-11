package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FitWindowsFrameLayout extends FrameLayout implements FitWindowsViewGroup {
   private FitWindowsViewGroup.OnFitSystemWindowsListener mListener;

   public FitWindowsFrameLayout(Context var1) {
      super(var1);
   }

   public FitWindowsFrameLayout(Context var1, AttributeSet var2) {
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
