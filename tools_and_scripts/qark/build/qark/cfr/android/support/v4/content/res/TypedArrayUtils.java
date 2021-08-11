/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.v4.content.res;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnyRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class TypedArrayUtils {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

    public static int getAttr(Context context, int n, int n2) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(n, typedValue, true);
        if (typedValue.resourceId != 0) {
            return n;
        }
        return n2;
    }

    public static boolean getBoolean(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2, boolean bl) {
        return typedArray.getBoolean(n, typedArray.getBoolean(n2, bl));
    }

    public static Drawable getDrawable(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2) {
        Drawable drawable2 = typedArray.getDrawable(n);
        if (drawable2 == null) {
            return typedArray.getDrawable(n2);
        }
        return drawable2;
    }

    public static int getInt(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2, int n3) {
        return typedArray.getInt(n, typedArray.getInt(n2, n3));
    }

    public static boolean getNamedBoolean(@NonNull TypedArray typedArray, @NonNull XmlPullParser xmlPullParser, String string2, @StyleableRes int n, boolean bl) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return bl;
        }
        return typedArray.getBoolean(n, bl);
    }

    @ColorInt
    public static int getNamedColor(@NonNull TypedArray typedArray, @NonNull XmlPullParser xmlPullParser, String string2, @StyleableRes int n, @ColorInt int n2) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return n2;
        }
        return typedArray.getColor(n, n2);
    }

    public static float getNamedFloat(@NonNull TypedArray typedArray, @NonNull XmlPullParser xmlPullParser, @NonNull String string2, @StyleableRes int n, float f) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return f;
        }
        return typedArray.getFloat(n, f);
    }

    public static int getNamedInt(@NonNull TypedArray typedArray, @NonNull XmlPullParser xmlPullParser, String string2, @StyleableRes int n, int n2) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return n2;
        }
        return typedArray.getInt(n, n2);
    }

    @AnyRes
    public static int getNamedResourceId(@NonNull TypedArray typedArray, @NonNull XmlPullParser xmlPullParser, String string2, @StyleableRes int n, @AnyRes int n2) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return n2;
        }
        return typedArray.getResourceId(n, n2);
    }

    public static String getNamedString(@NonNull TypedArray typedArray, @NonNull XmlPullParser xmlPullParser, String string2, @StyleableRes int n) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return null;
        }
        return typedArray.getString(n);
    }

    @AnyRes
    public static int getResourceId(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2, @AnyRes int n3) {
        return typedArray.getResourceId(n, typedArray.getResourceId(n2, n3));
    }

    public static String getString(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2) {
        String string2 = typedArray.getString(n);
        if (string2 == null) {
            return typedArray.getString(n2);
        }
        return string2;
    }

    public static CharSequence getText(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2) {
        CharSequence charSequence = typedArray.getText(n);
        if (charSequence == null) {
            return typedArray.getText(n2);
        }
        return charSequence;
    }

    public static CharSequence[] getTextArray(TypedArray typedArray, @StyleableRes int n, @StyleableRes int n2) {
        CharSequence[] arrcharSequence = typedArray.getTextArray(n);
        if (arrcharSequence == null) {
            return typedArray.getTextArray(n2);
        }
        return arrcharSequence;
    }

    public static boolean hasAttribute(@NonNull XmlPullParser xmlPullParser, @NonNull String string2) {
        if (xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", string2) != null) {
            return true;
        }
        return false;
    }

    public static TypedArray obtainAttributes(Resources resources, Resources.Theme theme, AttributeSet attributeSet, int[] arrn) {
        if (theme == null) {
            return resources.obtainAttributes(attributeSet, arrn);
        }
        return theme.obtainStyledAttributes(attributeSet, arrn, 0, 0);
    }

    public static TypedValue peekNamedValue(TypedArray typedArray, XmlPullParser xmlPullParser, String string2, int n) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return null;
        }
        return typedArray.peekValue(n);
    }
}

