package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@TargetApi(9)
@RequiresApi(9)
class DrawableCompatBase {
   public static void inflate(Drawable var0, Resources var1, XmlPullParser var2, AttributeSet var3, Theme var4) throws IOException, XmlPullParserException {
      var0.inflate(var1, var2, var3);
   }

   public static void setTint(Drawable var0, int var1) {
      if (var0 instanceof TintAwareDrawable) {
         ((TintAwareDrawable)var0).setTint(var1);
      }

   }

   public static void setTintList(Drawable var0, ColorStateList var1) {
      if (var0 instanceof TintAwareDrawable) {
         ((TintAwareDrawable)var0).setTintList(var1);
      }

   }

   public static void setTintMode(Drawable var0, Mode var1) {
      if (var0 instanceof TintAwareDrawable) {
         ((TintAwareDrawable)var0).setTintMode(var1);
      }

   }

   public static Drawable wrapForTinting(Drawable var0) {
      Object var1 = var0;
      if (!(var0 instanceof TintAwareDrawable)) {
         var1 = new DrawableWrapperGingerbread(var0);
      }

      return (Drawable)var1;
   }
}
