// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.res;

import android.graphics.Shader$TileMode;
import java.util.List;
import java.util.ArrayList;
import android.content.res.TypedArray;
import android.graphics.RadialGradient;
import android.graphics.SweepGradient;
import android.graphics.LinearGradient;
import androidx.core.R$styleable;
import java.io.IOException;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;
import android.graphics.Shader;
import android.content.res.Resources$Theme;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources;

final class GradientColorInflaterCompat
{
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;
    
    private GradientColorInflaterCompat() {
    }
    
    private static ColorStops checkColors(final ColorStops colorStops, final int n, final int n2, final boolean b, final int n3) {
        if (colorStops != null) {
            return colorStops;
        }
        if (b) {
            return new ColorStops(n, n3, n2);
        }
        return new ColorStops(n, n2);
    }
    
    static Shader createFromXml(final Resources resources, final XmlPullParser xmlPullParser, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
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
    
    static Shader createFromXmlInner(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
        final String name = xmlPullParser.getName();
        if (!name.equals("gradient")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(xmlPullParser.getPositionDescription());
            sb.append(": invalid gradient color tag ");
            sb.append(name);
            throw new XmlPullParserException(sb.toString());
        }
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, R$styleable.GradientColor);
        final float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "startX", R$styleable.GradientColor_android_startX, 0.0f);
        final float namedFloat2 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "startY", R$styleable.GradientColor_android_startY, 0.0f);
        final float namedFloat3 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "endX", R$styleable.GradientColor_android_endX, 0.0f);
        final float namedFloat4 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "endY", R$styleable.GradientColor_android_endY, 0.0f);
        final float namedFloat5 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "centerX", R$styleable.GradientColor_android_centerX, 0.0f);
        final float namedFloat6 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "centerY", R$styleable.GradientColor_android_centerY, 0.0f);
        final int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "type", R$styleable.GradientColor_android_type, 0);
        final int namedColor = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser, "startColor", R$styleable.GradientColor_android_startColor, 0);
        final boolean hasAttribute = TypedArrayUtils.hasAttribute(xmlPullParser, "centerColor");
        final int namedColor2 = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser, "centerColor", R$styleable.GradientColor_android_centerColor, 0);
        final int namedColor3 = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser, "endColor", R$styleable.GradientColor_android_endColor, 0);
        final int namedInt2 = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "tileMode", R$styleable.GradientColor_android_tileMode, 0);
        final float namedFloat7 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "gradientRadius", R$styleable.GradientColor_android_gradientRadius, 0.0f);
        obtainAttributes.recycle();
        final ColorStops checkColors = checkColors(inflateChildElements(resources, xmlPullParser, set, resources$Theme), namedColor, namedColor3, hasAttribute, namedColor2);
        if (namedInt != 1) {
            if (namedInt != 2) {
                return (Shader)new LinearGradient(namedFloat, namedFloat2, namedFloat3, namedFloat4, checkColors.mColors, checkColors.mOffsets, parseTileMode(namedInt2));
            }
            return (Shader)new SweepGradient(namedFloat5, namedFloat6, checkColors.mColors, checkColors.mOffsets);
        }
        else {
            if (namedFloat7 > 0.0f) {
                return (Shader)new RadialGradient(namedFloat5, namedFloat6, namedFloat7, checkColors.mColors, checkColors.mOffsets, parseTileMode(namedInt2));
            }
            throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
        }
    }
    
    private static ColorStops inflateChildElements(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final int n = xmlPullParser.getDepth() + 1;
        final ArrayList<Float> list = new ArrayList<Float>(20);
        final ArrayList<Integer> list2 = new ArrayList<Integer>(20);
        while (true) {
            final int next = xmlPullParser.next();
            if (next == 1) {
                break;
            }
            final int depth = xmlPullParser.getDepth();
            if (depth < n && next == 3) {
                break;
            }
            if (next != 2) {
                continue;
            }
            if (depth > n) {
                continue;
            }
            if (!xmlPullParser.getName().equals("item")) {
                continue;
            }
            final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, R$styleable.GradientColorItem);
            final boolean hasValue = obtainAttributes.hasValue(R$styleable.GradientColorItem_android_color);
            final boolean hasValue2 = obtainAttributes.hasValue(R$styleable.GradientColorItem_android_offset);
            if (!hasValue || !hasValue2) {
                final StringBuilder sb = new StringBuilder();
                sb.append(xmlPullParser.getPositionDescription());
                sb.append(": <item> tag requires a 'color' attribute and a 'offset' attribute!");
                throw new XmlPullParserException(sb.toString());
            }
            final int color = obtainAttributes.getColor(R$styleable.GradientColorItem_android_color, 0);
            final float float1 = obtainAttributes.getFloat(R$styleable.GradientColorItem_android_offset, 0.0f);
            obtainAttributes.recycle();
            list2.add(color);
            list.add(float1);
        }
        if (list2.size() > 0) {
            return new ColorStops(list2, list);
        }
        return null;
    }
    
    private static Shader$TileMode parseTileMode(final int n) {
        if (n == 1) {
            return Shader$TileMode.REPEAT;
        }
        if (n != 2) {
            return Shader$TileMode.CLAMP;
        }
        return Shader$TileMode.MIRROR;
    }
    
    static final class ColorStops
    {
        final int[] mColors;
        final float[] mOffsets;
        
        ColorStops(final int n, final int n2) {
            this.mColors = new int[] { n, n2 };
            this.mOffsets = new float[] { 0.0f, 1.0f };
        }
        
        ColorStops(final int n, final int n2, final int n3) {
            this.mColors = new int[] { n, n2, n3 };
            this.mOffsets = new float[] { 0.0f, 0.5f, 1.0f };
        }
        
        ColorStops(final List<Integer> list, final List<Float> list2) {
            final int size = list.size();
            this.mColors = new int[size];
            this.mOffsets = new float[size];
            for (int i = 0; i < size; ++i) {
                this.mColors[i] = list.get(i);
                this.mOffsets[i] = list2.get(i);
            }
        }
    }
}
