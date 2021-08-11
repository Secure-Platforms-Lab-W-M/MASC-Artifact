/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.graphics.Color
 *  android.util.TypedValue
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.ColorUtils;

class ThemeUtils {
    static final int[] ACTIVATED_STATE_SET;
    static final int[] CHECKED_STATE_SET;
    static final int[] DISABLED_STATE_SET;
    static final int[] EMPTY_STATE_SET;
    static final int[] FOCUSED_STATE_SET;
    static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET;
    static final int[] PRESSED_STATE_SET;
    static final int[] SELECTED_STATE_SET;
    private static final int[] TEMP_ARRAY;
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE;

    static {
        TL_TYPED_VALUE = new ThreadLocal();
        DISABLED_STATE_SET = new int[]{-16842910};
        FOCUSED_STATE_SET = new int[]{16842908};
        ACTIVATED_STATE_SET = new int[]{16843518};
        PRESSED_STATE_SET = new int[]{16842919};
        CHECKED_STATE_SET = new int[]{16842912};
        SELECTED_STATE_SET = new int[]{16842913};
        NOT_PRESSED_OR_FOCUSED_STATE_SET = new int[]{-16842919, -16842908};
        EMPTY_STATE_SET = new int[0];
        TEMP_ARRAY = new int[1];
    }

    private ThemeUtils() {
    }

    public static ColorStateList createDisabledStateList(int n, int n2) {
        int[][] arrarrn = new int[2][];
        int[] arrn = new int[2];
        arrarrn[0] = DISABLED_STATE_SET;
        arrn[0] = n2;
        n2 = 0 + 1;
        arrarrn[n2] = EMPTY_STATE_SET;
        arrn[n2] = n;
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    public static int getDisabledThemeAttrColor(Context context, int n) {
        ColorStateList colorStateList = ThemeUtils.getThemeAttrColorStateList(context, n);
        if (colorStateList != null && colorStateList.isStateful()) {
            return colorStateList.getColorForState(DISABLED_STATE_SET, colorStateList.getDefaultColor());
        }
        colorStateList = ThemeUtils.getTypedValue();
        context.getTheme().resolveAttribute(16842803, (TypedValue)colorStateList, true);
        return ThemeUtils.getThemeAttrColor(context, n, colorStateList.getFloat());
    }

    public static int getThemeAttrColor(Context object, int n) {
        int[] arrn = TEMP_ARRAY;
        arrn[0] = n;
        object = TintTypedArray.obtainStyledAttributes((Context)object, null, arrn);
        try {
            n = object.getColor(0, 0);
            return n;
        }
        finally {
            object.recycle();
        }
    }

    static int getThemeAttrColor(Context context, int n, float f) {
        n = ThemeUtils.getThemeAttrColor(context, n);
        return ColorUtils.setAlphaComponent(n, Math.round((float)Color.alpha((int)n) * f));
    }

    public static ColorStateList getThemeAttrColorStateList(Context object, int n) {
        ColorStateList colorStateList = TEMP_ARRAY;
        colorStateList[0] = n;
        object = TintTypedArray.obtainStyledAttributes((Context)object, null, (int[])colorStateList);
        try {
            colorStateList = object.getColorStateList(0);
            return colorStateList;
        }
        finally {
            object.recycle();
        }
    }

    private static TypedValue getTypedValue() {
        TypedValue typedValue;
        TypedValue typedValue2 = typedValue = TL_TYPED_VALUE.get();
        if (typedValue == null) {
            typedValue2 = new TypedValue();
            TL_TYPED_VALUE.set(typedValue2);
        }
        return typedValue2;
    }
}

