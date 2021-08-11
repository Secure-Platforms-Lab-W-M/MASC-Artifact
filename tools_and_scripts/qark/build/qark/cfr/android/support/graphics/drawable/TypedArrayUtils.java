/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.TypedArray
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.graphics.drawable;

import android.content.res.TypedArray;
import org.xmlpull.v1.XmlPullParser;

class TypedArrayUtils {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

    TypedArrayUtils() {
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

    public static boolean hasAttribute(XmlPullParser xmlPullParser, String string2) {
        if (xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", string2) != null) {
            return true;
        }
        return false;
    }
}

