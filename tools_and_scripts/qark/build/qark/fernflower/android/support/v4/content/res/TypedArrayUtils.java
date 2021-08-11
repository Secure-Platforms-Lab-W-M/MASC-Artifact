package android.support.v4.content.res;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnyRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypedArrayUtils {
   private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

   public static int getAttr(Context var0, int var1, int var2) {
      TypedValue var3 = new TypedValue();
      var0.getTheme().resolveAttribute(var1, var3, true);
      return var3.resourceId != 0 ? var1 : var2;
   }

   public static boolean getBoolean(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2, boolean var3) {
      return var0.getBoolean(var1, var0.getBoolean(var2, var3));
   }

   public static Drawable getDrawable(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2) {
      Drawable var4 = var0.getDrawable(var1);
      Drawable var3 = var4;
      if (var4 == null) {
         var3 = var0.getDrawable(var2);
      }

      return var3;
   }

   public static int getInt(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2, int var3) {
      return var0.getInt(var1, var0.getInt(var2, var3));
   }

   public static boolean getNamedBoolean(@NonNull TypedArray var0, @NonNull XmlPullParser var1, String var2, @StyleableRes int var3, boolean var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getBoolean(var3, var4);
   }

   @ColorInt
   public static int getNamedColor(@NonNull TypedArray var0, @NonNull XmlPullParser var1, String var2, @StyleableRes int var3, @ColorInt int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getColor(var3, var4);
   }

   public static float getNamedFloat(@NonNull TypedArray var0, @NonNull XmlPullParser var1, @NonNull String var2, @StyleableRes int var3, float var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getFloat(var3, var4);
   }

   public static int getNamedInt(@NonNull TypedArray var0, @NonNull XmlPullParser var1, String var2, @StyleableRes int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getInt(var3, var4);
   }

   @AnyRes
   public static int getNamedResourceId(@NonNull TypedArray var0, @NonNull XmlPullParser var1, String var2, @StyleableRes int var3, @AnyRes int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getResourceId(var3, var4);
   }

   public static String getNamedString(@NonNull TypedArray var0, @NonNull XmlPullParser var1, String var2, @StyleableRes int var3) {
      return !hasAttribute(var1, var2) ? null : var0.getString(var3);
   }

   @AnyRes
   public static int getResourceId(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2, @AnyRes int var3) {
      return var0.getResourceId(var1, var0.getResourceId(var2, var3));
   }

   public static String getString(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2) {
      String var4 = var0.getString(var1);
      String var3 = var4;
      if (var4 == null) {
         var3 = var0.getString(var2);
      }

      return var3;
   }

   public static CharSequence getText(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2) {
      CharSequence var4 = var0.getText(var1);
      CharSequence var3 = var4;
      if (var4 == null) {
         var3 = var0.getText(var2);
      }

      return var3;
   }

   public static CharSequence[] getTextArray(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2) {
      CharSequence[] var4 = var0.getTextArray(var1);
      CharSequence[] var3 = var4;
      if (var4 == null) {
         var3 = var0.getTextArray(var2);
      }

      return var3;
   }

   public static boolean hasAttribute(@NonNull XmlPullParser var0, @NonNull String var1) {
      return var0.getAttributeValue("http://schemas.android.com/apk/res/android", var1) != null;
   }

   public static TypedArray obtainAttributes(Resources var0, Theme var1, AttributeSet var2, int[] var3) {
      return var1 == null ? var0.obtainAttributes(var2, var3) : var1.obtainStyledAttributes(var2, var3, 0, 0);
   }

   public static TypedValue peekNamedValue(TypedArray var0, XmlPullParser var1, String var2, int var3) {
      return !hasAttribute(var1, var2) ? null : var0.peekValue(var3);
   }
}
