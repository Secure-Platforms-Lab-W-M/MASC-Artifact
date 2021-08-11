// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.res;

import android.graphics.Color;
import android.content.res.TypedArray;
import android.util.StateSet;
import androidx.core.R$attr;
import androidx.core.R$styleable;
import android.util.Log;
import java.io.IOException;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;
import android.content.res.ColorStateList;
import android.content.res.Resources$Theme;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources;

public final class ColorStateListInflaterCompat
{
    private ColorStateListInflaterCompat() {
    }
    
    public static ColorStateList createFromXml(final Resources resources, final XmlPullParser xmlPullParser, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        int next;
        do {
            next = xmlPullParser.next();
        } while (next != 2 && next != 1);
        if (next == 2) {
            return createFromXmlInner(resources, xmlPullParser, attributeSet, resources$Theme);
        }
        throw new XmlPullParserException("No start tag found");
    }
    
    public static ColorStateList createFromXmlInner(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final String name = xmlPullParser.getName();
        if (name.equals("selector")) {
            return inflate(resources, xmlPullParser, set, resources$Theme);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(xmlPullParser.getPositionDescription());
        sb.append(": invalid color state list tag ");
        sb.append(name);
        throw new XmlPullParserException(sb.toString());
    }
    
    public static ColorStateList inflate(final Resources resources, final int n, final Resources$Theme resources$Theme) {
        try {
            return createFromXml(resources, (XmlPullParser)resources.getXml(n), resources$Theme);
        }
        catch (Exception ex) {
            Log.e("CSLCompat", "Failed to inflate ColorStateList.", (Throwable)ex);
            return null;
        }
    }
    
    private static ColorStateList inflate(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final int n = xmlPullParser.getDepth() + 1;
        int[][] array = new int[20][];
        int[] append = new int[array.length];
        int n2 = 0;
        while (true) {
            final int next = xmlPullParser.next();
            if (next == 1) {
                break;
            }
            final int depth = xmlPullParser.getDepth();
            if (depth < n && next == 3) {
                break;
            }
            if (next != 2 || depth > n || !xmlPullParser.getName().equals("item")) {
                continue;
            }
            final TypedArray obtainAttributes = obtainAttributes(resources, resources$Theme, set, R$styleable.ColorStateListItem);
            final int color = obtainAttributes.getColor(R$styleable.ColorStateListItem_android_color, -65281);
            float n3 = 1.0f;
            if (obtainAttributes.hasValue(R$styleable.ColorStateListItem_android_alpha)) {
                n3 = obtainAttributes.getFloat(R$styleable.ColorStateListItem_android_alpha, 1.0f);
            }
            else if (obtainAttributes.hasValue(R$styleable.ColorStateListItem_alpha)) {
                n3 = obtainAttributes.getFloat(R$styleable.ColorStateListItem_alpha, 1.0f);
            }
            obtainAttributes.recycle();
            int n4 = 0;
            final int attributeCount = set.getAttributeCount();
            final int[] array2 = new int[attributeCount];
            int n5;
            for (int i = 0; i < attributeCount; ++i, n4 = n5) {
                final int attributeNameResource = set.getAttributeNameResource(i);
                n5 = n4;
                if (attributeNameResource != 16843173) {
                    n5 = n4;
                    if (attributeNameResource != 16843551) {
                        n5 = n4;
                        if (attributeNameResource != R$attr.alpha) {
                            int n6;
                            if (set.getAttributeBooleanValue(i, false)) {
                                n6 = attributeNameResource;
                            }
                            else {
                                n6 = -attributeNameResource;
                            }
                            array2[n4] = n6;
                            n5 = n4 + 1;
                        }
                    }
                }
            }
            final int[] trimStateSet = StateSet.trimStateSet(array2, n4);
            append = GrowingArrayUtils.append(append, n2, modulateColorAlpha(color, n3));
            array = GrowingArrayUtils.append(array, n2, trimStateSet);
            ++n2;
        }
        final int[] array3 = new int[n2];
        final int[][] array4 = new int[n2][];
        System.arraycopy(append, 0, array3, 0, n2);
        System.arraycopy(array, 0, array4, 0, n2);
        return new ColorStateList(array4, array3);
    }
    
    private static int modulateColorAlpha(final int n, final float n2) {
        return (0xFFFFFF & n) | Math.round(Color.alpha(n) * n2) << 24;
    }
    
    private static TypedArray obtainAttributes(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final int[] array) {
        if (resources$Theme == null) {
            return resources.obtainAttributes(set, array);
        }
        return resources$Theme.obtainStyledAttributes(set, array, 0, 0);
    }
}
