package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;

public interface TintAwareDrawable {
   void setTint(int var1);

   void setTintList(ColorStateList var1);

   void setTintMode(Mode var1);
}
