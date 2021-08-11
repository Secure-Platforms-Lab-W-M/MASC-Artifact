// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics;

import androidx.core.util.Preconditions;
import android.graphics.PointF;

public final class PathSegment
{
    private final PointF mEnd;
    private final float mEndFraction;
    private final PointF mStart;
    private final float mStartFraction;
    
    public PathSegment(final PointF pointF, final float mStartFraction, final PointF pointF2, final float mEndFraction) {
        this.mStart = Preconditions.checkNotNull(pointF, "start == null");
        this.mStartFraction = mStartFraction;
        this.mEnd = Preconditions.checkNotNull(pointF2, "end == null");
        this.mEndFraction = mEndFraction;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PathSegment)) {
            return false;
        }
        final PathSegment pathSegment = (PathSegment)o;
        return Float.compare(this.mStartFraction, pathSegment.mStartFraction) == 0 && Float.compare(this.mEndFraction, pathSegment.mEndFraction) == 0 && this.mStart.equals((Object)pathSegment.mStart) && this.mEnd.equals((Object)pathSegment.mEnd);
    }
    
    public PointF getEnd() {
        return this.mEnd;
    }
    
    public float getEndFraction() {
        return this.mEndFraction;
    }
    
    public PointF getStart() {
        return this.mStart;
    }
    
    public float getStartFraction() {
        return this.mStartFraction;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.mStart.hashCode();
        final float mStartFraction = this.mStartFraction;
        int floatToIntBits = 0;
        int floatToIntBits2;
        if (mStartFraction != 0.0f) {
            floatToIntBits2 = Float.floatToIntBits(mStartFraction);
        }
        else {
            floatToIntBits2 = 0;
        }
        final int hashCode2 = this.mEnd.hashCode();
        final float mEndFraction = this.mEndFraction;
        if (mEndFraction != 0.0f) {
            floatToIntBits = Float.floatToIntBits(mEndFraction);
        }
        return ((hashCode * 31 + floatToIntBits2) * 31 + hashCode2) * 31 + floatToIntBits;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PathSegment{start=");
        sb.append(this.mStart);
        sb.append(", startFraction=");
        sb.append(this.mStartFraction);
        sb.append(", end=");
        sb.append(this.mEnd);
        sb.append(", endFraction=");
        sb.append(this.mEndFraction);
        sb.append('}');
        return sb.toString();
    }
}
