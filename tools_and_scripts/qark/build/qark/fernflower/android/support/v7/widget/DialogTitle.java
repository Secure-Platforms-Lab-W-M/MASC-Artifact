package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R$styleable;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class DialogTitle extends TextView {
   public DialogTitle(Context var1) {
      super(var1);
   }

   public DialogTitle(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public DialogTitle(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      Layout var4 = this.getLayout();
      if (var4 != null) {
         int var3 = var4.getLineCount();
         if (var3 > 0 && var4.getEllipsisCount(var3 - 1) > 0) {
            this.setSingleLine(false);
            this.setMaxLines(2);
            TypedArray var5 = this.getContext().obtainStyledAttributes((AttributeSet)null, R$styleable.TextAppearance, 16842817, 16973892);
            var3 = var5.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, 0);
            if (var3 != 0) {
               this.setTextSize(0, (float)var3);
            }

            var5.recycle();
            super.onMeasure(var1, var2);
         }
      }

   }
}
