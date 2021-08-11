/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Color
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.StateSet
 *  android.util.Xml
 *  androidx.core.R
 *  androidx.core.R$attr
 *  androidx.core.R$styleable
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import androidx.core.R;
import androidx.core.content.res.GrowingArrayUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ColorStateListInflaterCompat {
    private ColorStateListInflaterCompat() {
    }

    public static ColorStateList createFromXml(Resources resources, XmlPullParser xmlPullParser, Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlPullParser);
        while ((n = xmlPullParser.next()) != 2 && n != 1) {
        }
        if (n == 2) {
            return ColorStateListInflaterCompat.createFromXmlInner(resources, xmlPullParser, attributeSet, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static ColorStateList createFromXmlInner(Resources object, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        String string2 = xmlPullParser.getName();
        if (string2.equals("selector")) {
            return ColorStateListInflaterCompat.inflate((Resources)object, xmlPullParser, attributeSet, theme);
        }
        object = new StringBuilder();
        object.append(xmlPullParser.getPositionDescription());
        object.append(": invalid color state list tag ");
        object.append(string2);
        throw new XmlPullParserException(object.toString());
    }

    public static ColorStateList inflate(Resources resources, int n, Resources.Theme theme) {
        try {
            resources = ColorStateListInflaterCompat.createFromXml(resources, (XmlPullParser)resources.getXml(n), theme);
            return resources;
        }
        catch (Exception exception) {
            Log.e((String)"CSLCompat", (String)"Failed to inflate ColorStateList.", (Throwable)exception);
            return null;
        }
    }

    private static ColorStateList inflate(Resources arrn, XmlPullParser arrarrn, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        int n;
        int n2;
        int n3 = arrarrn.getDepth() + 1;
        int[][] arrarrn2 = new int[20][];
        int[] arrn2 = new int[arrarrn2.length];
        int n4 = 0;
        while ((n = arrarrn.next()) != 1 && ((n2 = arrarrn.getDepth()) >= n3 || n != 3)) {
            if (n != 2 || n2 > n3 || !arrarrn.getName().equals("item")) continue;
            int[] arrn3 = ColorStateListInflaterCompat.obtainAttributes((Resources)arrn, theme, attributeSet, R.styleable.ColorStateListItem);
            int n5 = arrn3.getColor(R.styleable.ColorStateListItem_android_color, -65281);
            float f = 1.0f;
            if (arrn3.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
                f = arrn3.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0f);
            } else if (arrn3.hasValue(R.styleable.ColorStateListItem_alpha)) {
                f = arrn3.getFloat(R.styleable.ColorStateListItem_alpha, 1.0f);
            }
            arrn3.recycle();
            n2 = 0;
            int n6 = attributeSet.getAttributeCount();
            int[] arrn4 = new int[n6];
            for (n = 0; n < n6; ++n) {
                int n7 = attributeSet.getAttributeNameResource(n);
                int n8 = n2;
                if (n7 != 16843173) {
                    n8 = n2;
                    if (n7 != 16843551) {
                        n8 = n2;
                        if (n7 != R.attr.alpha) {
                            n8 = attributeSet.getAttributeBooleanValue(n, false) ? n7 : - n7;
                            arrn4[n2] = n8;
                            n8 = n2 + 1;
                        }
                    }
                }
                n2 = n8;
            }
            arrn3 = StateSet.trimStateSet((int[])arrn4, (int)n2);
            arrn2 = GrowingArrayUtils.append(arrn2, n4, ColorStateListInflaterCompat.modulateColorAlpha(n5, f));
            arrarrn2 = GrowingArrayUtils.append(arrarrn2, n4, arrn3);
            ++n4;
        }
        arrn = new int[n4];
        arrarrn = new int[n4][];
        System.arraycopy(arrn2, 0, arrn, 0, n4);
        System.arraycopy(arrarrn2, 0, arrarrn, 0, n4);
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private static int modulateColorAlpha(int n, float f) {
        return 16777215 & n | Math.round((float)Color.alpha((int)n) * f) << 24;
    }

    private static TypedArray obtainAttributes(Resources resources, Resources.Theme theme, AttributeSet attributeSet, int[] arrn) {
        if (theme == null) {
            return resources.obtainAttributes(attributeSet, arrn);
        }
        return theme.obtainStyledAttributes(attributeSet, arrn, 0, 0);
    }
}

