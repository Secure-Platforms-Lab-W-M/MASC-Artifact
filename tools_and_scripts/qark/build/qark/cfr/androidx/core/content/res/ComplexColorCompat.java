/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.graphics.Shader
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import androidx.core.content.res.ColorStateListInflaterCompat;
import androidx.core.content.res.GradientColorInflaterCompat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ComplexColorCompat {
    private static final String LOG_TAG = "ComplexColorCompat";
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;

    private ComplexColorCompat(Shader shader, ColorStateList colorStateList, int n) {
        this.mShader = shader;
        this.mColorStateList = colorStateList;
        this.mColor = n;
    }

    private static ComplexColorCompat createFromXml(Resources object, int n, Resources.Theme theme) throws IOException, XmlPullParserException {
        XmlResourceParser xmlResourceParser = object.getXml(n);
        AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xmlResourceParser);
        while ((n = xmlResourceParser.next()) != 2 && n != 1) {
        }
        if (n == 2) {
            String string2 = xmlResourceParser.getName();
            n = -1;
            int n2 = string2.hashCode();
            if (n2 != 89650992) {
                if (n2 == 1191572447 && string2.equals("selector")) {
                    n = 0;
                }
            } else if (string2.equals("gradient")) {
                n = 1;
            }
            if (n != 0) {
                if (n == 1) {
                    return ComplexColorCompat.from(GradientColorInflaterCompat.createFromXmlInner((Resources)object, (XmlPullParser)xmlResourceParser, attributeSet, theme));
                }
                object = new StringBuilder();
                object.append(xmlResourceParser.getPositionDescription());
                object.append(": unsupported complex color tag ");
                object.append(string2);
                throw new XmlPullParserException(object.toString());
            }
            return ComplexColorCompat.from(ColorStateListInflaterCompat.createFromXmlInner((Resources)object, (XmlPullParser)xmlResourceParser, attributeSet, theme));
        }
        throw new XmlPullParserException("No start tag found");
    }

    static ComplexColorCompat from(int n) {
        return new ComplexColorCompat(null, null, n);
    }

    static ComplexColorCompat from(ColorStateList colorStateList) {
        return new ComplexColorCompat(null, colorStateList, colorStateList.getDefaultColor());
    }

    static ComplexColorCompat from(Shader shader) {
        return new ComplexColorCompat(shader, null, 0);
    }

    public static ComplexColorCompat inflate(Resources object, int n, Resources.Theme theme) {
        try {
            object = ComplexColorCompat.createFromXml((Resources)object, n, theme);
            return object;
        }
        catch (Exception exception) {
            Log.e((String)"ComplexColorCompat", (String)"Failed to inflate ComplexColor.", (Throwable)exception);
            return null;
        }
    }

    public int getColor() {
        return this.mColor;
    }

    public Shader getShader() {
        return this.mShader;
    }

    public boolean isGradient() {
        if (this.mShader != null) {
            return true;
        }
        return false;
    }

    public boolean isStateful() {
        ColorStateList colorStateList;
        if (this.mShader == null && (colorStateList = this.mColorStateList) != null && colorStateList.isStateful()) {
            return true;
        }
        return false;
    }

    public boolean onStateChanged(int[] arrn) {
        boolean bl;
        boolean bl2 = bl = false;
        if (this.isStateful()) {
            ColorStateList colorStateList = this.mColorStateList;
            int n = colorStateList.getColorForState(arrn, colorStateList.getDefaultColor());
            bl2 = bl;
            if (n != this.mColor) {
                bl2 = true;
                this.mColor = n;
            }
        }
        return bl2;
    }

    public void setColor(int n) {
        this.mColor = n;
    }

    public boolean willDraw() {
        if (!this.isGradient() && this.mColor == 0) {
            return false;
        }
        return true;
    }
}

