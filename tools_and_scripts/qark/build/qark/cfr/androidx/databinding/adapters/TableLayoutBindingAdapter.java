/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.SparseBooleanArray
 *  android.widget.TableLayout
 */
package androidx.databinding.adapters;

import android.util.SparseBooleanArray;
import android.widget.TableLayout;
import java.util.regex.Pattern;

public class TableLayoutBindingAdapter {
    private static final int MAX_COLUMNS = 20;
    private static Pattern sColumnPattern = Pattern.compile("\\s*,\\s*");

    private static SparseBooleanArray parseColumns(CharSequence arrstring) {
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        if (arrstring == null) {
            return sparseBooleanArray;
        }
        for (String string2 : sColumnPattern.split((CharSequence)arrstring)) {
            int n = Integer.parseInt(string2);
            if (n < 0) continue;
            try {
                sparseBooleanArray.put(n, true);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return sparseBooleanArray;
    }

    public static void setCollapseColumns(TableLayout tableLayout, CharSequence charSequence) {
        charSequence = TableLayoutBindingAdapter.parseColumns(charSequence);
        for (int i = 0; i < 20; ++i) {
            boolean bl = charSequence.get(i, false);
            if (bl == tableLayout.isColumnCollapsed(i)) continue;
            tableLayout.setColumnCollapsed(i, bl);
        }
    }

    public static void setShrinkColumns(TableLayout tableLayout, CharSequence charSequence) {
        if (charSequence != null && charSequence.length() > 0 && charSequence.charAt(0) == '*') {
            tableLayout.setShrinkAllColumns(true);
            return;
        }
        tableLayout.setShrinkAllColumns(false);
        charSequence = TableLayoutBindingAdapter.parseColumns(charSequence);
        int n = charSequence.size();
        for (int i = 0; i < n; ++i) {
            int n2 = charSequence.keyAt(i);
            boolean bl = charSequence.valueAt(i);
            if (!bl) continue;
            tableLayout.setColumnShrinkable(n2, bl);
        }
    }

    public static void setStretchColumns(TableLayout tableLayout, CharSequence charSequence) {
        if (charSequence != null && charSequence.length() > 0 && charSequence.charAt(0) == '*') {
            tableLayout.setStretchAllColumns(true);
            return;
        }
        tableLayout.setStretchAllColumns(false);
        charSequence = TableLayoutBindingAdapter.parseColumns(charSequence);
        int n = charSequence.size();
        for (int i = 0; i < n; ++i) {
            int n2 = charSequence.keyAt(i);
            boolean bl = charSequence.valueAt(i);
            if (!bl) continue;
            tableLayout.setColumnStretchable(n2, bl);
        }
    }
}

