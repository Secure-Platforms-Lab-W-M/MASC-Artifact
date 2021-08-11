package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;

public class TypedArrayUtils {
   private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

   private TypedArrayUtils() {
   }

   public static int getAttr(Context var0, int var1, int var2) {
      TypedValue var3 = new TypedValue();
      var0.getTheme().resolveAttribute(var1, var3, true);
      return var3.resourceId != 0 ? var1 : var2;
   }

   public static boolean getBoolean(TypedArray var0, int var1, int var2, boolean var3) {
      return var0.getBoolean(var1, var0.getBoolean(var2, var3));
   }

   public static Drawable getDrawable(TypedArray var0, int var1, int var2) {
      Drawable var4 = var0.getDrawable(var1);
      Drawable var3 = var4;
      if (var4 == null) {
         var3 = var0.getDrawable(var2);
      }

      return var3;
   }

   public static int getInt(TypedArray var0, int var1, int var2, int var3) {
      return var0.getInt(var1, var0.getInt(var2, var3));
   }

   public static boolean getNamedBoolean(TypedArray var0, XmlPullParser var1, String var2, int var3, boolean var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getBoolean(var3, var4);
   }

   public static int getNamedColor(TypedArray var0, XmlPullParser var1, String var2, int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getColor(var3, var4);
   }

   public static ColorStateList getNamedColorStateList(TypedArray var0, XmlPullParser var1, Theme var2, String var3, int var4) {
      if (hasAttribute(var1, var3)) {
         TypedValue var6 = new TypedValue();
         var0.getValue(var4, var6);
         if (var6.type != 2) {
            return var6.type >= 28 && var6.type <= 31 ? getNamedColorStateListFromInt(var6) : ColorStateListInflaterCompat.inflate(var0.getResources(), var0.getResourceId(var4, 0), var2);
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("Failed to resolve attribute at index ");
            var5.append(var4);
            var5.append(": ");
            var5.append(var6);
            throw new UnsupportedOperationException(var5.toString());
         }
      } else {
         return null;
      }
   }

   private static ColorStateList getNamedColorStateListFromInt(TypedValue var0) {
      return ColorStateList.valueOf(var0.data);
   }

   public static ComplexColorCompat getNamedComplexColor(TypedArray var0, XmlPullParser var1, Theme var2, String var3, int var4, int var5) {
      if (hasAttribute(var1, var3)) {
         TypedValue var7 = new TypedValue();
         var0.getValue(var4, var7);
         if (var7.type >= 28 && var7.type <= 31) {
            return ComplexColorCompat.from(var7.data);
         }

         ComplexColorCompat var6 = ComplexColorCompat.inflate(var0.getResources(), var0.getResourceId(var4, 0), var2);
         if (var6 != null) {
            return var6;
         }
      }

      return ComplexColorCompat.from(var5);
   }

   public static float getNamedFloat(TypedArray var0, XmlPullParser var1, String var2, int var3, float var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getFloat(var3, var4);
   }

   public static int getNamedInt(TypedArray var0, XmlPullParser var1, String var2, int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getInt(var3, var4);
   }

   public static int getNamedResourceId(TypedArray var0, XmlPullParser var1, String var2, int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getResourceId(var3, var4);
   }

   public static String getNamedString(TypedArray var0, XmlPullParser var1, String var2, int var3) {
      return !hasAttribute(var1, var2) ? null : var0.getString(var3);
   }

   public static int getResourceId(TypedArray var0, int var1, int var2, int var3) {
      return var0.getResourceId(var1, var0.getResourceId(var2, var3));
   }

   public static String getString(TypedArray var0, int var1, int var2) {
      String var4 = var0.getString(var1);
      String var3 = var4;
      if (var4 == null) {
         var3 = var0.getString(var2);
      }

      return var3;
   }

   public static CharSequence getText(TypedArray var0, int var1, int var2) {
      CharSequence var4 = var0.getText(var1);
      CharSequence var3 = var4;
      if (var4 == null) {
         var3 = var0.getText(var2);
      }

      return var3;
   }

   public static CharSequence[] getTextArray(TypedArray var0, int var1, int var2) {
      CharSequence[] var4 = var0.getTextArray(var1);
      CharSequence[] var3 = var4;
      if (var4 == null) {
         var3 = var0.getTextArray(var2);
      }

      return var3;
   }

   public static boolean hasAttribute(XmlPullParser var0, String var1) {
      return var0.getAttributeValue("http://schemas.android.com/apk/res/android", var1) != null;
   }

   public static TypedArray obtainAttributes(Resources var0, Theme var1, AttributeSet var2, int[] var3) {
      return var1 == null ? var0.obtainAttributes(var2, var3) : var1.obtainStyledAttributes(var2, var3, 0, 0);
   }

   public static TypedValue peekNamedValue(TypedArray var0, XmlPullParser var1, String var2, int var3) {
      return !hasAttribute(var1, var2) ? null : var0.peekValue(var3);
   }
}
