package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;

public interface TintableCompoundDrawablesView {
   ColorStateList getSupportCompoundDrawablesTintList();

   Mode getSupportCompoundDrawablesTintMode();

   void setSupportCompoundDrawablesTintList(ColorStateList var1);

   void setSupportCompoundDrawablesTintMode(Mode var1);
}
