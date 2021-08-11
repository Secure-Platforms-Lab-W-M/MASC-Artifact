package androidx.appcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class AppCompatToggleButton extends ToggleButton {
   private final AppCompatTextHelper mTextHelper;

   public AppCompatToggleButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatToggleButton(Context var1, AttributeSet var2) {
      this(var1, var2, 16842827);
   }

   public AppCompatToggleButton(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      AppCompatTextHelper var4 = new AppCompatTextHelper(this);
      this.mTextHelper = var4;
      var4.loadFromAttributes(var2, var3);
   }
}
