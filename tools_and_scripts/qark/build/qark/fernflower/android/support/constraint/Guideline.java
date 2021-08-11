package android.support.constraint;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class Guideline extends View {
   public Guideline(Context var1) {
      super(var1);
      super.setVisibility(8);
   }

   public Guideline(Context var1, AttributeSet var2) {
      super(var1, var2);
      super.setVisibility(8);
   }

   public Guideline(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      super.setVisibility(8);
   }

   public Guideline(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3);
      super.setVisibility(8);
   }

   public void draw(Canvas var1) {
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(0, 0);
   }

   public void setVisibility(int var1) {
   }
}
