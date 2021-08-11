// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.res;

import android.util.Log;
import java.io.IOException;
import android.util.AttributeSet;
import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.graphics.Shader;
import android.content.res.ColorStateList;

public final class ComplexColorCompat
{
    private static final String LOG_TAG = "ComplexColorCompat";
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;
    
    private ComplexColorCompat(final Shader mShader, final ColorStateList mColorStateList, final int mColor) {
        this.mShader = mShader;
        this.mColorStateList = mColorStateList;
        this.mColor = mColor;
    }
    
    private static ComplexColorCompat createFromXml(final Resources resources, int next, final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
        final XmlResourceParser xml = resources.getXml(next);
        final AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xml);
        do {
            next = ((XmlPullParser)xml).next();
        } while (next != 2 && next != 1);
        if (next != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        final String name = ((XmlPullParser)xml).getName();
        next = -1;
        final int hashCode = name.hashCode();
        if (hashCode != 89650992) {
            if (hashCode == 1191572447 && name.equals("selector")) {
                next = 0;
            }
        }
        else if (name.equals("gradient")) {
            next = 1;
        }
        if (next == 0) {
            return from(ColorStateListInflaterCompat.createFromXmlInner(resources, (XmlPullParser)xml, attributeSet, resources$Theme));
        }
        if (next == 1) {
            return from(GradientColorInflaterCompat.createFromXmlInner(resources, (XmlPullParser)xml, attributeSet, resources$Theme));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(((XmlPullParser)xml).getPositionDescription());
        sb.append(": unsupported complex color tag ");
        sb.append(name);
        throw new XmlPullParserException(sb.toString());
    }
    
    static ComplexColorCompat from(final int n) {
        return new ComplexColorCompat(null, null, n);
    }
    
    static ComplexColorCompat from(final ColorStateList list) {
        return new ComplexColorCompat(null, list, list.getDefaultColor());
    }
    
    static ComplexColorCompat from(final Shader shader) {
        return new ComplexColorCompat(shader, null, 0);
    }
    
    public static ComplexColorCompat inflate(final Resources resources, final int n, final Resources$Theme resources$Theme) {
        try {
            return createFromXml(resources, n, resources$Theme);
        }
        catch (Exception ex) {
            Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", (Throwable)ex);
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
        return this.mShader != null;
    }
    
    public boolean isStateful() {
        if (this.mShader == null) {
            final ColorStateList mColorStateList = this.mColorStateList;
            if (mColorStateList != null && mColorStateList.isStateful()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean onStateChanged(final int[] array) {
        boolean b = false;
        if (this.isStateful()) {
            final ColorStateList mColorStateList = this.mColorStateList;
            final int colorForState = mColorStateList.getColorForState(array, mColorStateList.getDefaultColor());
            b = b;
            if (colorForState != this.mColor) {
                b = true;
                this.mColor = colorForState;
            }
        }
        return b;
    }
    
    public void setColor(final int mColor) {
        this.mColor = mColor;
    }
    
    public boolean willDraw() {
        return this.isGradient() || this.mColor != 0;
    }
}
