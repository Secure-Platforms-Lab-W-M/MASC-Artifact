/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.util;

import android.support.annotation.RestrictTo;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class DebugUtils {
    public static void buildShortClassTag(Object object, StringBuilder stringBuilder) {
        if (object == null) {
            stringBuilder.append("null");
            return;
        }
        String string2 = object.getClass().getSimpleName();
        if (string2 == null || string2.length() <= 0) {
            string2 = object.getClass().getName();
            int n = string2.lastIndexOf(46);
            if (n > 0) {
                string2 = string2.substring(n + 1);
            }
        }
        stringBuilder.append(string2);
        stringBuilder.append('{');
        stringBuilder.append(Integer.toHexString(System.identityHashCode(object)));
    }
}

