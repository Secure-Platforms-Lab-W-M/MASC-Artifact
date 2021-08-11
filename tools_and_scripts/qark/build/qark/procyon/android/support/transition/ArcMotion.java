// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.graphics.Path;
import android.content.res.TypedArray;
import android.support.v4.content.res.TypedArrayUtils;
import org.xmlpull.v1.XmlPullParser;
import android.util.AttributeSet;
import android.content.Context;

public class ArcMotion extends PathMotion
{
    private static final float DEFAULT_MAX_ANGLE_DEGREES = 70.0f;
    private static final float DEFAULT_MAX_TANGENT;
    private static final float DEFAULT_MIN_ANGLE_DEGREES = 0.0f;
    private float mMaximumAngle;
    private float mMaximumTangent;
    private float mMinimumHorizontalAngle;
    private float mMinimumHorizontalTangent;
    private float mMinimumVerticalAngle;
    private float mMinimumVerticalTangent;
    
    static {
        DEFAULT_MAX_TANGENT = (float)Math.tan(Math.toRadians(35.0));
    }
    
    public ArcMotion() {
        this.mMinimumHorizontalAngle = 0.0f;
        this.mMinimumVerticalAngle = 0.0f;
        this.mMaximumAngle = 70.0f;
        this.mMinimumHorizontalTangent = 0.0f;
        this.mMinimumVerticalTangent = 0.0f;
        this.mMaximumTangent = ArcMotion.DEFAULT_MAX_TANGENT;
    }
    
    public ArcMotion(final Context context, final AttributeSet set) {
        super(context, set);
        this.mMinimumHorizontalAngle = 0.0f;
        this.mMinimumVerticalAngle = 0.0f;
        this.mMaximumAngle = 70.0f;
        this.mMinimumHorizontalTangent = 0.0f;
        this.mMinimumVerticalTangent = 0.0f;
        this.mMaximumTangent = ArcMotion.DEFAULT_MAX_TANGENT;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, Styleable.ARC_MOTION);
        final XmlPullParser xmlPullParser = (XmlPullParser)set;
        this.setMinimumVerticalAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "minimumVerticalAngle", 1, 0.0f));
        this.setMinimumHorizontalAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "minimumHorizontalAngle", 0, 0.0f));
        this.setMaximumAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "maximumAngle", 2, 70.0f));
        obtainStyledAttributes.recycle();
    }
    
    private static float toTangent(final float n) {
        if (n >= 0.0f && n <= 90.0f) {
            return (float)Math.tan(Math.toRadians(n / 2.0f));
        }
        throw new IllegalArgumentException("Arc must be between 0 and 90 degrees");
    }
    
    public float getMaximumAngle() {
        return this.mMaximumAngle;
    }
    
    public float getMinimumHorizontalAngle() {
        return this.mMinimumHorizontalAngle;
    }
    
    public float getMinimumVerticalAngle() {
        return this.mMinimumVerticalAngle;
    }
    
    @Override
    public Path getPath(final float n, final float n2, final float n3, final float n4) {
        final Path path = new Path();
        path.moveTo(n, n2);
        final float n5 = n3 - n;
        final float n6 = n4 - n2;
        final float n7 = n5 * n5 + n6 * n6;
        final float n8 = (n + n3) / 2.0f;
        final float n9 = (n2 + n4) / 2.0f;
        final float n10 = n7 * 0.25f;
        final boolean b = n2 > n4;
        float n11;
        float n12;
        float n13;
        if (Math.abs(n5) < Math.abs(n6)) {
            final float abs = Math.abs(n7 / (n6 * 2.0f));
            if (b) {
                n11 = n4 + abs;
                n12 = n3;
            }
            else {
                n11 = n2 + abs;
                n12 = n;
            }
            final float mMinimumVerticalTangent = this.mMinimumVerticalTangent;
            n13 = n10 * mMinimumVerticalTangent * mMinimumVerticalTangent;
        }
        else {
            final float n14 = n7 / (n5 * 2.0f);
            if (b) {
                n12 = n + n14;
                n11 = n2;
            }
            else {
                n12 = n3 - n14;
                n11 = n4;
            }
            final float mMinimumHorizontalTangent = this.mMinimumHorizontalTangent;
            n13 = n10 * mMinimumHorizontalTangent * mMinimumHorizontalTangent;
        }
        final float n15 = n8 - n12;
        final float n16 = n9 - n11;
        final float n17 = n15 * n15 + n16 * n16;
        final float mMaximumTangent = this.mMaximumTangent;
        final float n18 = n10 * mMaximumTangent * mMaximumTangent;
        if (n17 >= n13) {
            if (n17 > n18) {
                n13 = n18;
            }
            else {
                n13 = 0.0f;
            }
        }
        if (n13 != 0.0f) {
            final float n19 = (float)Math.sqrt(n13 / n17);
            n11 = n9 + (n11 - n9) * n19;
            n12 = n8 + (n12 - n8) * n19;
        }
        path.cubicTo((n + n12) / 2.0f, (n2 + n11) / 2.0f, (n12 + n3) / 2.0f, (n11 + n4) / 2.0f, n3, n4);
        return path;
    }
    
    public void setMaximumAngle(final float mMaximumAngle) {
        this.mMaximumAngle = mMaximumAngle;
        this.mMaximumTangent = toTangent(mMaximumAngle);
    }
    
    public void setMinimumHorizontalAngle(final float mMinimumHorizontalAngle) {
        this.mMinimumHorizontalAngle = mMinimumHorizontalAngle;
        this.mMinimumHorizontalTangent = toTangent(mMinimumHorizontalAngle);
    }
    
    public void setMinimumVerticalAngle(final float mMinimumVerticalAngle) {
        this.mMinimumVerticalAngle = mMinimumVerticalAngle;
        this.mMinimumVerticalTangent = toTangent(mMinimumVerticalAngle);
    }
}
