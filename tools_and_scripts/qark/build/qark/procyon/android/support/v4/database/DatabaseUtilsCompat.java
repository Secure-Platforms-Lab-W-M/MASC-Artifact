// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.database;

import android.text.TextUtils;

public final class DatabaseUtilsCompat
{
    private DatabaseUtilsCompat() {
    }
    
    public static String[] appendSelectionArgs(final String[] array, final String[] array2) {
        if (array == null) {
            return array2;
        }
        if (array.length == 0) {
            return array2;
        }
        final String[] array3 = new String[array.length + array2.length];
        System.arraycopy(array, 0, array3, 0, array.length);
        System.arraycopy(array2, 0, array3, array.length, array2.length);
        return array3;
    }
    
    public static String concatenateWhere(final String s, final String s2) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return s2;
        }
        if (TextUtils.isEmpty((CharSequence)s2)) {
            return s;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(s);
        sb.append(") AND (");
        sb.append(s2);
        sb.append(")");
        return sb.toString();
    }
}
