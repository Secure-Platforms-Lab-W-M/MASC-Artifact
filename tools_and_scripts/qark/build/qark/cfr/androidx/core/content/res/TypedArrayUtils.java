/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.core.content.res.ColorStateListInflaterCompat;
import androidx.core.content.res.ComplexColorCompat;
import org.xmlpull.v1.XmlPullParser;

public class TypedArrayUtils {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

    private TypedArrayUtils() {
    }

    public static int getAttr(Context context, int n, int n2) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(n, typedValue, true);
        if (typedValue.resourceId != 0) {
            return n;
        }
        return n2;
    }

    public static boolean getBoolean(TypedArray typedArray, int n, int n2, boolean bl) {
        return typedArray.getBoolean(n, typedArray.getBoolean(n2, bl));
    }

    public static Drawable getDrawable(TypedArray typedArray, int n, int n2) {
        Drawable drawable2;
        Drawable drawable3 = drawable2 = typedArray.getDrawable(n);
        if (drawable2 == null) {
            drawable3 = typedArray.getDrawable(n2);
        }
        return drawable3;
    }

    public static int getInt(TypedArray typedArray, int n, int n2, int n3) {
        return typedArray.getInt(n, typedArray.getInt(n2, n3));
    }

    public static boolean getNamedBoolean(TypedArray typedArray, XmlPullParser xmlPullParser, String string2, int n, boolean bl) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return bl;
        }
        return typedArray.getBoolean(n, bl);
    }

    public static int getNamedColor(TypedArray typedArray, XmlPullParser xmlPullParser, String string2, int n, int n2) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return n2;
        }
        return typedArray.getColor(n, n2);
    }

    public static ColorStateList getNamedColorStateList(TypedArray object, XmlPullParser xmlPullParser, Resources.Theme theme, String string2, int n) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            xmlPullParser = new TypedValue();
            object.getValue(n, (TypedValue)xmlPullParser);
            if (xmlPullParser.type != 2) {
                if (xmlPullParser.type >= 28 && xmlPullParser.type <= 31) {
                    return TypedArrayUtils.getNamedColorStateListFromInt((TypedValue)xmlPullParser);
                }
                return ColorStateListInflaterCompat.inflate(object.getResources(), object.getResourceId(n, 0), theme);
            }
            object = new StringBuilder();
            object.append("Failed to resolve attribute at index ");
            object.append(n);
            object.append(": ");
            object.append((Object)xmlPullParser);
            throw new UnsupportedOperationException(object.toString());
        }
        return null;
    }

    private static ColorStateList getNamedColorStateListFromInt(TypedValue typedValue) {
        return ColorStateList.valueOf((int)typedValue.data);
    }

    public static ComplexColorCompat getNamedComplexColor(TypedArray object, XmlPullParser xmlPullParser, Resources.Theme theme, String string2, int n, int n2) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            xmlPullParser = new TypedValue();
            object.getValue(n, (TypedValue)xmlPullParser);
            if (xmlPullParser.type >= 28 && xmlPullParser.type <= 31) {
                return ComplexColorCompat.from(xmlPullParser.data);
            }
            if ((object = ComplexColorCompat.inflate(object.getResources(), object.getResourceId(n, 0), theme)) != null) {
                return object;
            }
        }
        return ComplexColorCompat.from(n2);
    }

    public static float getNamedFloat(TypedArray typedArray, XmlPullParser xmlPullParser, String string2, int n, float f) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return f;
        }
        return typedArray.getFloat(n, f);
    }

    public static int getNamedInt(TypedArray typedArray, XmlPullParser xmlPullParser, String string2, int n, int n2) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return n2;
        }
        return typedArray.getInt(n, n2);
    }

    public static int getNamedResourceId(TypedArray typedArray, XmlPullParser xmlPullParser, String string2, int n, int n2) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return n2;
        }
        return typedArray.getResourceId(n, n2);
    }

    public static String getNamedString(TypedArray typedArray, XmlPullParser xmlPullParser, String string2, int n) {
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, string2)) {
            return null;
        }
        return typedArray.getString(n);
    }

    public static int getResourceId(TypedArray typedArray, int n, int n2, int n3) {
        return typedArray.getResourceId(n, typedArray.getResourceId(n2, n3));
    }

    public static String getString(TypedArray typedArray, int n, int n2) {
        String string2;
        String string3 = string2 = typedArray.getString(n);
        if (string2 == null) {
            string3 = typedArray.getString(n2);
        }
        return string3;
    }

    public static CharSequence getText(TypedArray typedArray, int n, int n2) {
        CharSequence charSequence;
        CharSequence charSequence2 = charSequence = typedArray.getText(n);
        if (charSequence == null) {
            charSequence2 = typedArray.getText(n2);
        }
        return charSequence2;
    }

    public static CharSequence[] getTextArray(TypedArray typedArray, int n, int n2) {
        CharSequence[] arrcharSequence;
        CharSequence[] arrcharSequence2 = arrcharSequence = typedArray.getTextArray(n);
        if (arrcharSequence == null) {
            arrcharSequence2 = typedArray.getTextArray(n2);
        }
        return arrcharSequence2;
    }

    public static boolean hasAttribute(XmlPullParser xmlPullParser, String string2) {
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

