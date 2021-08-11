// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.graphics.drawable;

import org.xmlpull.v1.XmlPullParser;
import android.content.res.TypedArray;

class TypedArrayUtils
{
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    
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
    
    public static boolean hasAttribute(final XmlPullParser xmlPullParser, final String s) {
        return xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", s) != null;
    }
}
