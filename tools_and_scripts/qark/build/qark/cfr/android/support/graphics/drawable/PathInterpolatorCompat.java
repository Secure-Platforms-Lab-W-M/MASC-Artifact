/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Path
 *  android.graphics.PathMeasure
 *  android.util.AttributeSet
 *  android.view.InflateException
 *  android.view.animation.Interpolator
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.RestrictTo;
import android.support.graphics.drawable.AndroidResources;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class PathInterpolatorCompat
implements Interpolator {
    public static final double EPSILON = 1.0E-5;
    public static final int MAX_NUM_POINTS = 3000;
    private static final float PRECISION = 0.002f;
    private float[] mX;
    private float[] mY;

    public PathInterpolatorCompat(Context context, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        this(context.getResources(), context.getTheme(), attributeSet, xmlPullParser);
    }

    public PathInterpolatorCompat(Resources resources, Resources.Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        resources = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        this.parseInterpolatorFromTypeArray((TypedArray)resources, xmlPullParser);
        resources.recycle();
    }

    private void initCubic(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f, f2, f3, f4, 1.0f, 1.0f);
        this.initPath(path);
    }

    private void initPath(Path path) {
        int n;
        float f = (path = new PathMeasure(path, false)).getLength();
        int n2 = Math.min(3000, (int)(f / 0.002f) + 1);
        if (n2 <= 0) {
            throw new IllegalArgumentException("The Path has a invalid length " + f);
        }
        this.mX = new float[n2];
        this.mY = new float[n2];
        float[] arrf = new float[2];
        for (n = 0; n < n2; ++n) {
            path.getPosTan((float)n * f / (float)(n2 - 1), arrf, null);
            this.mX[n] = arrf[0];
            this.mY[n] = arrf[1];
        }
        if ((double)Math.abs(this.mX[0]) > 1.0E-5 || (double)Math.abs(this.mY[0]) > 1.0E-5 || (double)Math.abs(this.mX[n2 - 1] - 1.0f) > 1.0E-5 || (double)Math.abs(this.mY[n2 - 1] - 1.0f) > 1.0E-5) {
            throw new IllegalArgumentException("The Path must start at (0,0) and end at (1,1) start: " + this.mX[0] + "," + this.mY[0] + " end:" + this.mX[n2 - 1] + "," + this.mY[n2 - 1]);
        }
        f = 0.0f;
        int n3 = 0;
        n = 0;
        while (n3 < n2) {
            float f2 = this.mX[n];
            if (f2 < f) {
                throw new IllegalArgumentException("The Path cannot loop back on itself, x :" + f2);
            }
            this.mX[n3] = f2;
            f = f2;
            ++n3;
            ++n;
        }
        if (path.nextContour()) {
            throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
        }
    }

    private void initQuad(float f, float f2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f, f2, 1.0f, 1.0f);
        this.initPath(path);
    }

    private void parseInterpolatorFromTypeArray(TypedArray object, XmlPullParser xmlPullParser) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "pathData")) {
            object = TypedArrayUtils.getNamedString((TypedArray)object, xmlPullParser, "pathData", 4);
            xmlPullParser = PathParser.createPathFromPathData((String)object);
            if (xmlPullParser == null) {
                throw new InflateException("The path is null, which is created from " + (String)object);
            }
            this.initPath((Path)xmlPullParser);
            return;
        }
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, "controlX1")) {
            throw new InflateException("pathInterpolator requires the controlX1 attribute");
        }
        if (!TypedArrayUtils.hasAttribute(xmlPullParser, "controlY1")) {
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
        }
        float f = TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "controlX1", 0, 0.0f);
        float f2 = TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "controlY1", 1, 0.0f);
        boolean bl = TypedArrayUtils.hasAttribute(xmlPullParser, "controlX2");
        if (bl != TypedArrayUtils.hasAttribute(xmlPullParser, "controlY2")) {
            throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
        }
        if (!bl) {
            this.initQuad(f, f2);
            return;
        }
        this.initCubic(f, f2, TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "controlX2", 2, 0.0f), TypedArrayUtils.getNamedFloat((TypedArray)object, xmlPullParser, "controlY2", 3, 0.0f));
    }

    public float getInterpolation(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        int n = 0;
        int n2 = this.mX.length - 1;
        while (n2 - n > 1) {
            int n3 = (n + n2) / 2;
            if (f < this.mX[n3]) {
                n2 = n3;
                continue;
            }
            n = n3;
        }
        float f2 = this.mX[n2] - this.mX[n];
        if (f2 == 0.0f) {
            return this.mY[n];
        }
        f = (f - this.mX[n]) / f2;
        f2 = this.mY[n];
        return (this.mY[n2] - f2) * f + f2;
    }
}

