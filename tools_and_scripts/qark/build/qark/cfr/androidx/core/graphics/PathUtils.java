/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Path
 *  android.graphics.PointF
 */
package androidx.core.graphics;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.core.graphics.PathSegment;
import java.util.ArrayList;
import java.util.Collection;

public final class PathUtils {
    private PathUtils() {
    }

    public static Collection<PathSegment> flatten(Path path) {
        return PathUtils.flatten(path, 0.5f);
    }

    public static Collection<PathSegment> flatten(Path arrf, float f) {
        arrf = arrf.approximate(f);
        int n = arrf.length / 3;
        ArrayList<PathSegment> arrayList = new ArrayList<PathSegment>(n);
        for (int i = 1; i < n; ++i) {
            int n2 = i * 3;
            int n3 = (i - 1) * 3;
            f = arrf[n2];
            float f2 = arrf[n2 + 1];
            float f3 = arrf[n2 + 2];
            float f4 = arrf[n3];
            float f5 = arrf[n3 + 1];
            float f6 = arrf[n3 + 2];
            if (f == f4 || f2 == f5 && f3 == f6) continue;
            arrayList.add(new PathSegment(new PointF(f5, f6), f4, new PointF(f2, f3), f));
        }
        return arrayList;
    }
}

