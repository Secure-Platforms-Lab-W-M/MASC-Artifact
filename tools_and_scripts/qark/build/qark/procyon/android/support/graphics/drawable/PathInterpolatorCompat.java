// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.graphics.drawable;

import android.view.InflateException;
import android.support.v4.graphics.PathParser;
import android.graphics.PathMeasure;
import android.graphics.Path;
import android.content.res.TypedArray;
import android.support.v4.content.res.TypedArrayUtils;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import org.xmlpull.v1.XmlPullParser;
import android.util.AttributeSet;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.view.animation.Interpolator;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class PathInterpolatorCompat implements Interpolator
{
    public static final double EPSILON = 1.0E-5;
    public static final int MAX_NUM_POINTS = 3000;
    private static final float PRECISION = 0.002f;
    private float[] mX;
    private float[] mY;
    
    public PathInterpolatorCompat(final Context context, final AttributeSet set, final XmlPullParser xmlPullParser) {
        this(context.getResources(), context.getTheme(), set, xmlPullParser);
    }
    
    public PathInterpolatorCompat(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final XmlPullParser xmlPullParser) {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        this.parseInterpolatorFromTypeArray(obtainAttributes, xmlPullParser);
        obtainAttributes.recycle();
    }
    
    private void initCubic(final float n, final float n2, final float n3, final float n4) {
        final Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(n, n2, n3, n4, 1.0f, 1.0f);
        this.initPath(path);
    }
    
    private void initPath(final Path path) {
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        final float length = pathMeasure.getLength();
        final int min = Math.min(3000, (int)(length / 0.002f) + 1);
        if (min <= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("The Path has a invalid length ");
            sb.append(length);
            throw new IllegalArgumentException(sb.toString());
        }
        this.mX = new float[min];
        this.mY = new float[min];
        final float[] array = new float[2];
        for (int i = 0; i < min; ++i) {
            pathMeasure.getPosTan(i * length / (min - 1), array, (float[])null);
            this.mX[i] = array[0];
            this.mY[i] = array[1];
        }
        if (Math.abs(this.mX[0]) > 1.0E-5 || Math.abs(this.mY[0]) > 1.0E-5 || Math.abs(this.mX[min - 1] - 1.0f) > 1.0E-5 || Math.abs(this.mY[min - 1] - 1.0f) > 1.0E-5) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("The Path must start at (0,0) and end at (1,1) start: ");
            sb2.append(this.mX[0]);
            sb2.append(",");
            sb2.append(this.mY[0]);
            sb2.append(" end:");
            sb2.append(this.mX[min - 1]);
            sb2.append(",");
            sb2.append(this.mY[min - 1]);
            throw new IllegalArgumentException(sb2.toString());
        }
        float n = 0.0f;
        for (int n2 = 0, j = 0; j < min; ++j, ++n2) {
            final float[] mx = this.mX;
            final float n3 = mx[n2];
            if (n3 < n) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("The Path cannot loop back on itself, x :");
                sb3.append(n3);
                throw new IllegalArgumentException(sb3.toString());
            }
            mx[j] = n3;
            n = n3;
        }
        if (!pathMeasure.nextContour()) {
            return;
        }
        throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
    }
    
    private void initQuad(final float n, final float n2) {
        final Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(n, n2, 1.0f, 1.0f);
        this.initPath(path);
    }
    
    private void parseInterpolatorFromTypeArray(final TypedArray typedArray, final XmlPullParser xmlPullParser) {
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "pathData")) {
            final String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "pathData", 4);
            final Path pathFromPathData = PathParser.createPathFromPathData(namedString);
            if (pathFromPathData != null) {
                this.initPath(pathFromPathData);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("The path is null, which is created from ");
            sb.append(namedString);
            throw new InflateException(sb.toString());
        }
        else {
            if (!TypedArrayUtils.hasAttribute(xmlPullParser, "controlX1")) {
                throw new InflateException("pathInterpolator requires the controlX1 attribute");
            }
            if (!TypedArrayUtils.hasAttribute(xmlPullParser, "controlY1")) {
                throw new InflateException("pathInterpolator requires the controlY1 attribute");
            }
            final float namedFloat = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlX1", 0, 0.0f);
            final float namedFloat2 = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlY1", 1, 0.0f);
            final boolean hasAttribute = TypedArrayUtils.hasAttribute(xmlPullParser, "controlX2");
            if (hasAttribute != TypedArrayUtils.hasAttribute(xmlPullParser, "controlY2")) {
                throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
            }
            if (!hasAttribute) {
                this.initQuad(namedFloat, namedFloat2);
                return;
            }
            this.initCubic(namedFloat, namedFloat2, TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlX2", 2, 0.0f), TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "controlY2", 3, 0.0f));
        }
    }
    
    public float getInterpolation(float n) {
        if (n <= 0.0f) {
            return 0.0f;
        }
        if (n >= 1.0f) {
            return 1.0f;
        }
        int n2 = 0;
        int n3 = this.mX.length - 1;
        while (n3 - n2 > 1) {
            final int n4 = (n2 + n3) / 2;
            if (n < this.mX[n4]) {
                n3 = n4;
            }
            else {
                n2 = n4;
            }
        }
        final float[] mx = this.mX;
        final float n5 = mx[n3] - mx[n2];
        if (n5 == 0.0f) {
            return this.mY[n2];
        }
        n = (n - mx[n2]) / n5;
        final float[] my = this.mY;
        final float n6 = my[n2];
        return (my[n3] - n6) * n + n6;
    }
}
