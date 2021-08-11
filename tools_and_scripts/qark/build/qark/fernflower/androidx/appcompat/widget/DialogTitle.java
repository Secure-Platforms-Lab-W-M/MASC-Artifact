package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import androidx.appcompat.R.styleable;

public class DialogTitle extends AppCompatTextView {
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
            TypedArray var5 = this.getContext().obtainStyledAttributes((AttributeSet)null, styleable.TextAppearance, 16842817, 16973892);
            var3 = var5.getDimensionPixelSize(styleable.TextAppearance_android_textSize, 0);
            if (var3 != 0) {
               this.setTextSize(0, (float)var3);
            }

            var5.recycle();
            super.onMeasure(var1, var2);
         }
      }

   }
}
