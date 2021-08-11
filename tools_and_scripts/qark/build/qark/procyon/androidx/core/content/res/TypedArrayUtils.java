// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.res;

import android.util.AttributeSet;
import android.content.res.Resources;
import android.content.res.ColorStateList;
import android.content.res.Resources$Theme;
import org.xmlpull.v1.XmlPullParser;
import android.graphics.drawable.Drawable;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.content.Context;

public class TypedArrayUtils
{
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    
    private TypedArrayUtils() {
    }
    
    public static int getAttr(final Context context, final int n, final int n2) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(n, typedValue, true);
        if (typedValue.resourceId != 0) {
            return n;
        }
        return n2;
    }
    
    public static boolean getBoolean(final TypedArray typedArray, final int n, final int n2, final boolean b) {
        return typedArray.getBoolean(n, typedArray.getBoolean(n2, b));
    }
    
    public static Drawable getDrawable(final TypedArray typedArray, final int n, final int n2) {
        Drawable drawable;
        if ((drawable = typedArray.getDrawable(n)) == null) {
            drawable = typedArray.getDrawable(n2);
        }
        return drawable;
    }
    
    public static int getInt(final TypedArray typedArray, final int n, final int n2, final int n3) {
        return typedArray.getInt(n, typedArray.getInt(n2, n3));
    }
    
    public static boolean getNamedBoolean(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final boolean b) {
        if (!hasAttribute(xmlPullParser, s)) {
            return b;
        }
        return typedArray.getBoolean(n, b);
    }
    
    public static int getNamedColor(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getColor(n, n2);
    }
    
    public static ColorStateList getNamedColorStateList(final TypedArray typedArray, final XmlPullParser xmlPullParser, final Resources$Theme resources$Theme, final String s, final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        final TypedValue typedValue = new TypedValue();
        typedArray.getValue(n, typedValue);
        if (typedValue.type == 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to resolve attribute at index ");
            sb.append(n);
            sb.append(": ");
            sb.append(typedValue);
            throw new UnsupportedOperationException(sb.toString());
        }
        if (typedValue.type >= 28 && typedValue.type <= 31) {
            return getNamedColorStateListFromInt(typedValue);
        }
        return ColorStateListInflaterCompat.inflate(typedArray.getResources(), typedArray.getResourceId(n, 0), resources$Theme);
    }
    
    private static ColorStateList getNamedColorStateListFromInt(final TypedValue typedValue) {
        return ColorStateList.valueOf(typedValue.data);
    }
    
    public static ComplexColorCompat getNamedComplexColor(final TypedArray typedArray, final XmlPullParser xmlPullParser, final Resources$Theme resources$Theme, final String s, final int n, final int n2) {
        if (hasAttribute(xmlPullParser, s)) {
            final TypedValue typedValue = new TypedValue();
            typedArray.getValue(n, typedValue);
            if (typedValue.type >= 28 && typedValue.type <= 31) {
                return ComplexColorCompat.from(typedValue.data);
            }
            final ComplexColorCompat inflate = ComplexColorCompat.inflate(typedArray.getResources(), typedArray.getResourceId(n, 0), resources$Theme);
            if (inflate != null) {
                return inflate;
            }
        }
        return ComplexColorCompat.from(n2);
    }
    
    public static float getNamedFloat(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final float n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getFloat(n, n2);
    }
    
    public static int getNamedInt(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getInt(n, n2);
    }
    
    public static int getNamedResourceId(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n, final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getResourceId(n, n2);
    }
    
    public static String getNamedString(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        return typedArray.getString(n);
    }
    
    public static int getResourceId(final TypedArray typedArray, final int n, final int n2, final int n3) {
        return typedArray.getResourceId(n, typedArray.getResourceId(n2, n3));
    }
    
    public static String getString(final TypedArray typedArray, final int n, final int n2) {
        String s;
        if ((s = typedArray.getString(n)) == null) {
            s = typedArray.getString(n2);
        }
        return s;
    }
    
    public static CharSequence getText(final TypedArray typedArray, final int n, final int n2) {
        CharSequence charSequence;
        if ((charSequence = typedArray.getText(n)) == null) {
            charSequence = typedArray.getText(n2);
        }
        return charSequence;
    }
    
    public static CharSequence[] getTextArray(final TypedArray typedArray, final int n, final int n2) {
        CharSequence[] array;
        if ((array = typedArray.getTextArray(n)) == null) {
            array = typedArray.getTextArray(n2);
        }
        return array;
    }
    
    public static boolean hasAttribute(final XmlPullParser xmlPullParser, final String s) {
        return xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", s) != null;
    }
    
    public static TypedArray obtainAttributes(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final int[] array) {
        if (resources$Theme == null) {
            return resources.obtainAttributes(set, array);
        }
        return resources$Theme.obtainStyledAttributes(set, array, 0, 0);
    }
    
    public static TypedValue peekNamedValue(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        return typedArray.peekValue(n);
    }
}
