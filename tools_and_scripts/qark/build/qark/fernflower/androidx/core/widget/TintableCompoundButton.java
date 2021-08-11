package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;

public interface TintableCompoundButton {
   ColorStateList getSupportButtonTintList();

   Mode getSupportButtonTintMode();

   void setSupportButtonTintList(ColorStateList var1);

   void setSupportButtonTintMode(Mode var1);
}
