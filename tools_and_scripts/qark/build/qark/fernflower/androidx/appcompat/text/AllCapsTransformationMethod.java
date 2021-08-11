package androidx.appcompat.text;

import android.content.Context;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;
import java.util.Locale;

public class AllCapsTransformationMethod implements TransformationMethod {
   private Locale mLocale;

   public AllCapsTransformationMethod(Context var1) {
      this.mLocale = var1.getResources().getConfiguration().locale;
   }

   public CharSequence getTransformation(CharSequence var1, View var2) {
      return var1 != null ? var1.toString().toUpperCase(this.mLocale) : null;
   }

   public void onFocusChanged(View var1, CharSequence var2, boolean var3, int var4, Rect var5) {
   }
}
