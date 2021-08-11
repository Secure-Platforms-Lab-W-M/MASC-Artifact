package android.support.graphics.drawable;

import android.content.res.TypedArray;
import org.xmlpull.v1.XmlPullParser;

class TypedArrayUtils {
   private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

   public static boolean getNamedBoolean(TypedArray var0, XmlPullParser var1, String var2, int var3, boolean var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getBoolean(var3, var4);
   }

   public static int getNamedColor(TypedArray var0, XmlPullParser var1, String var2, int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getColor(var3, var4);
   }

   public static float getNamedFloat(TypedArray var0, XmlPullParser var1, String var2, int var3, float var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getFloat(var3, var4);
   }

   public static int getNamedInt(TypedArray var0, XmlPullParser var1, String var2, int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getInt(var3, var4);
   }

   public static boolean hasAttribute(XmlPullParser var0, String var1) {
      return var0.getAttributeValue("http://schemas.android.com/apk/res/android", var1) != null;
   }
}
