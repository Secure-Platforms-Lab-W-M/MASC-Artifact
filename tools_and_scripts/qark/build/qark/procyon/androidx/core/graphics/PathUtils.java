// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Collection;
import android.graphics.Path;

public final class PathUtils
{
    private PathUtils() {
    }
    
    public static Collection<PathSegment> flatten(final Path path) {
        return flatten(path, 0.5f);
    }
    
    public static Collection<PathSegment> flatten(final Path path, float n) {
        final float[] approximate = path.approximate(n);
        final int n2 = approximate.length / 3;
        final ArrayList list = new ArrayList<PathSegment>(n2);
        for (int i = 1; i < n2; ++i) {
            final int n3 = i * 3;
            final int n4 = (i - 1) * 3;
            n = approximate[n3];
            final float n5 = approximate[n3 + 1];
            final float n6 = approximate[n3 + 2];
            final float n7 = approximate[n4];
            final float n8 = approximate[n4 + 1];
            final float n9 = approximate[n4 + 2];
            if (n != n7 && (n5 != n8 || n6 != n9)) {
                list.add(new PathSegment(new PointF(n8, n9), n7, new PointF(n5, n6), n));
            }
        }
        return (Collection<PathSegment>)list;
    }
}
