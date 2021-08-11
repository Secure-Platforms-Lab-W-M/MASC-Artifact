package android.support.constraint;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;

public class Group extends ConstraintHelper {
   public Group(Context var1) {
      super(var1);
   }

   public Group(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public Group(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void init(AttributeSet var1) {
      super.init(var1);
      this.mUseViewMeasure = false;
   }

   public void updatePostLayout(ConstraintLayout var1) {
      ConstraintLayout.LayoutParams var2 = (ConstraintLayout.LayoutParams)this.getLayoutParams();
      var2.widget.setWidth(0);
      var2.widget.setHeight(0);
   }

   public void updatePreLayout(ConstraintLayout var1) {
      int var4 = this.getVisibility();
      float var2 = 0.0F;
      if (VERSION.SDK_INT >= 21) {
         var2 = this.getElevation();
      }

      for(int var3 = 0; var3 < this.mCount; ++var3) {
         View var5 = var1.getViewById(this.mIds[var3]);
         if (var5 != null) {
            var5.setVisibility(var4);
            if (var2 > 0.0F && VERSION.SDK_INT >= 21) {
               var5.setElevation(var2);
            }
         }
      }

   }
}
