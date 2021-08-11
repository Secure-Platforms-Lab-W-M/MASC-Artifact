package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;

public interface TintableImageSourceView {
   ColorStateList getSupportImageTintList();

   Mode getSupportImageTintMode();

   void setSupportImageTintList(ColorStateList var1);

   void setSupportImageTintMode(Mode var1);
}
