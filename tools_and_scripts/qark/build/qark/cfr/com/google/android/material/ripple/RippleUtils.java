/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.Color
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.StateSet
 */
package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.StateSet;
import androidx.core.graphics.ColorUtils;

public class RippleUtils {
    private static final int[] ENABLED_PRESSED_STATE_SET;
    private static final int[] FOCUSED_STATE_SET;
    private static final int[] HOVERED_FOCUSED_STATE_SET;
    private static final int[] HOVERED_STATE_SET;
    static final String LOG_TAG;
    private static final int[] PRESSED_STATE_SET;
    private static final int[] SELECTED_FOCUSED_STATE_SET;
    private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
    private static final int[] SELECTED_HOVERED_STATE_SET;
    private static final int[] SELECTED_PRESSED_STATE_SET;
    private static final int[] SELECTED_STATE_SET;
    static final String TRANSPARENT_DEFAULT_COLOR_WARNING = "Use a non-transparent color for the default color as it will be used to finish ripple animations.";
    public static final boolean USE_FRAMEWORK_RIPPLE;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 21;
        USE_FRAMEWORK_RIPPLE = bl;
        PRESSED_STATE_SET = new int[]{16842919};
        HOVERED_FOCUSED_STATE_SET = new int[]{16843623, 16842908};
        FOCUSED_STATE_SET = new int[]{16842908};
        HOVERED_STATE_SET = new int[]{16843623};
        SELECTED_PRESSED_STATE_SET = new int[]{16842913, 16842919};
        SELECTED_HOVERED_FOCUSED_STATE_SET = new int[]{16842913, 16843623, 16842908};
        SELECTED_FOCUSED_STATE_SET = new int[]{16842913, 16842908};
        SELECTED_HOVERED_STATE_SET = new int[]{16842913, 16843623};
        SELECTED_STATE_SET = new int[]{16842913};
        ENABLED_PRESSED_STATE_SET = new int[]{16842910, 16842919};
        LOG_TAG = RippleUtils.class.getSimpleName();
    }

    private RippleUtils() {
    }

    public static ColorStateList convertToRippleDrawableColor(ColorStateList colorStateList) {
        if (USE_FRAMEWORK_RIPPLE) {
            int[][] arrarrn = new int[2][];
            int[] arrn = new int[2];
            arrarrn[0] = SELECTED_STATE_SET;
            arrn[0] = RippleUtils.getColorForState(colorStateList, SELECTED_PRESSED_STATE_SET);
            int n = 0 + 1;
            arrarrn[n] = StateSet.NOTHING;
            arrn[n] = RippleUtils.getColorForState(colorStateList, PRESSED_STATE_SET);
            return new ColorStateList((int[][])arrarrn, arrn);
        }
        int[][] arrarrn = new int[10][];
        int[] arrn = new int[10];
        int[] arrn2 = SELECTED_PRESSED_STATE_SET;
        arrarrn[0] = arrn2;
        arrn[0] = RippleUtils.getColorForState(colorStateList, arrn2);
        int n = 0 + 1;
        arrn2 = SELECTED_HOVERED_FOCUSED_STATE_SET;
        arrarrn[n] = arrn2;
        arrn[n] = RippleUtils.getColorForState(colorStateList, arrn2);
        arrn2 = SELECTED_FOCUSED_STATE_SET;
        arrarrn[++n] = arrn2;
        arrn[n] = RippleUtils.getColorForState(colorStateList, arrn2);
        arrn2 = SELECTED_HOVERED_STATE_SET;
        arrarrn[++n] = arrn2;
        arrn[n] = RippleUtils.getColorForState(colorStateList, arrn2);
        arrarrn[++n] = SELECTED_STATE_SET;
        arrn[n] = 0;
        arrn2 = PRESSED_STATE_SET;
        arrarrn[++n] = arrn2;
        arrn[n] = RippleUtils.getColorForState(colorStateList, arrn2);
        arrn2 = HOVERED_FOCUSED_STATE_SET;
        arrarrn[++n] = arrn2;
        arrn[n] = RippleUtils.getColorForState(colorStateList, arrn2);
        arrn2 = FOCUSED_STATE_SET;
        arrarrn[++n] = arrn2;
        arrn[n] = RippleUtils.getColorForState(colorStateList, arrn2);
        arrn2 = HOVERED_STATE_SET;
        arrarrn[++n] = arrn2;
        arrn[n] = RippleUtils.getColorForState(colorStateList, arrn2);
        arrarrn[++n] = StateSet.NOTHING;
        arrn[n] = 0;
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private static int doubleAlpha(int n) {
        return ColorUtils.setAlphaComponent(n, Math.min(Color.alpha((int)n) * 2, 255));
    }

    private static int getColorForState(ColorStateList colorStateList, int[] arrn) {
        int n = colorStateList != null ? colorStateList.getColorForState(arrn, colorStateList.getDefaultColor()) : 0;
        if (USE_FRAMEWORK_RIPPLE) {
            return RippleUtils.doubleAlpha(n);
        }
        return n;
    }

    public static ColorStateList sanitizeRippleDrawableColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (Build.VERSION.SDK_INT >= 22 && Build.VERSION.SDK_INT <= 27 && Color.alpha((int)colorStateList.getDefaultColor()) == 0 && Color.alpha((int)colorStateList.getColorForState(ENABLED_PRESSED_STATE_SET, 0)) != 0) {
                Log.w((String)LOG_TAG, (String)"Use a non-transparent color for the default color as it will be used to finish ripple animations.");
            }
            return colorStateList;
        }
        return ColorStateList.valueOf((int)0);
    }

    public static boolean shouldDrawRippleCompat(int[] arrn) {
        boolean bl = false;
        boolean bl2 = false;
        int n = arrn.length;
        boolean bl3 = false;
        for (int i = 0; i < n; ++i) {
            boolean bl4;
            int n2 = arrn[i];
            if (n2 == 16842910) {
                bl4 = true;
            } else if (n2 == 16842908) {
                bl2 = true;
                bl4 = bl;
            } else if (n2 == 16842919) {
                bl2 = true;
                bl4 = bl;
            } else {
                bl4 = bl;
                if (n2 == 16843623) {
                    bl2 = true;
                    bl4 = bl;
                }
            }
            bl = bl4;
        }
        boolean bl5 = bl3;
        if (bl) {
            bl5 = bl3;
            if (bl2) {
                bl5 = true;
            }
        }
        return bl5;
    }
}

