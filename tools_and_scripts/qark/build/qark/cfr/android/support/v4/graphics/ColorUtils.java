/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Color
 */
package android.support.v4.graphics;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public final class ColorUtils {
    private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
    private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
    private static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal();
    private static final double XYZ_EPSILON = 0.008856;
    private static final double XYZ_KAPPA = 903.3;
    private static final double XYZ_WHITE_REFERENCE_X = 95.047;
    private static final double XYZ_WHITE_REFERENCE_Y = 100.0;
    private static final double XYZ_WHITE_REFERENCE_Z = 108.883;

    private ColorUtils() {
    }

    @ColorInt
    public static int HSLToColor(@NonNull float[] arrf) {
        float f = arrf[0];
        float f2 = arrf[1];
        float f3 = arrf[2];
        f2 = (1.0f - Math.abs(f3 * 2.0f - 1.0f)) * f2;
        f3 -= 0.5f * f2;
        float f4 = (1.0f - Math.abs(f / 60.0f % 2.0f - 1.0f)) * f2;
        int n = (int)f / 60;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        switch (n) {
            default: {
                break;
            }
            case 5: 
            case 6: {
                n2 = Math.round((f2 + f3) * 255.0f);
                n3 = Math.round(f3 * 255.0f);
                n4 = Math.round((f4 + f3) * 255.0f);
                break;
            }
            case 4: {
                n2 = Math.round((f4 + f3) * 255.0f);
                n3 = Math.round(f3 * 255.0f);
                n4 = Math.round((f2 + f3) * 255.0f);
                break;
            }
            case 3: {
                n2 = Math.round(f3 * 255.0f);
                n3 = Math.round((f4 + f3) * 255.0f);
                n4 = Math.round((f2 + f3) * 255.0f);
                break;
            }
            case 2: {
                n2 = Math.round(f3 * 255.0f);
                n3 = Math.round((f2 + f3) * 255.0f);
                n4 = Math.round((f4 + f3) * 255.0f);
                break;
            }
            case 1: {
                n2 = Math.round((f4 + f3) * 255.0f);
                n3 = Math.round((f2 + f3) * 255.0f);
                n4 = Math.round(255.0f * f3);
                break;
            }
            case 0: {
                n2 = Math.round((f2 + f3) * 255.0f);
                n3 = Math.round((f4 + f3) * 255.0f);
                n4 = Math.round(255.0f * f3);
            }
        }
        return Color.rgb((int)ColorUtils.constrain(n2, 0, 255), (int)ColorUtils.constrain(n3, 0, 255), (int)ColorUtils.constrain(n4, 0, 255));
    }

    @ColorInt
    public static int LABToColor(@FloatRange(from=0.0, to=100.0) double d, @FloatRange(from=-128.0, to=127.0) double d2, @FloatRange(from=-128.0, to=127.0) double d3) {
        double[] arrd = ColorUtils.getTempDouble3Array();
        ColorUtils.LABToXYZ(d, d2, d3, arrd);
        return ColorUtils.XYZToColor(arrd[0], arrd[1], arrd[2]);
    }

    public static void LABToXYZ(@FloatRange(from=0.0, to=100.0) double d, @FloatRange(from=-128.0, to=127.0) double d2, @FloatRange(from=-128.0, to=127.0) double d3, @NonNull double[] arrd) {
        double d4 = (d + 16.0) / 116.0;
        double d5 = d2 / 500.0 + d4;
        double d6 = d4 - d3 / 200.0;
        d2 = Math.pow(d5, 3.0);
        if (d2 <= 0.008856) {
            d2 = (d5 * 116.0 - 16.0) / 903.3;
        }
        d = d > 7.9996247999999985 ? Math.pow(d4, 3.0) : (d /= 903.3);
        d3 = Math.pow(d6, 3.0);
        if (d3 <= 0.008856) {
            d3 = (116.0 * d6 - 16.0) / 903.3;
        }
        arrd[0] = 95.047 * d2;
        arrd[1] = 100.0 * d;
        arrd[2] = 108.883 * d3;
    }

    public static void RGBToHSL(@IntRange(from=0L, to=255L) int n, @IntRange(from=0L, to=255L) int n2, @IntRange(from=0L, to=255L) int n3, @NonNull float[] arrf) {
        float f = (float)n / 255.0f;
        float f2 = (float)n2 / 255.0f;
        float f3 = (float)n3 / 255.0f;
        float f4 = Math.max(f, Math.max(f2, f3));
        float f5 = Math.min(f, Math.min(f2, f3));
        float f6 = f4 - f5;
        float f7 = (f4 + f5) / 2.0f;
        if (f4 == f5) {
            f = 0.0f;
            f6 = 0.0f;
        } else {
            f = f4 == f ? (f2 - f3) / f6 % 6.0f : (f4 == f2 ? (f3 - f) / f6 + 2.0f : (f - f2) / f6 + 4.0f);
            f2 = f6 / (1.0f - Math.abs(2.0f * f7 - 1.0f));
            f6 = f;
            f = f2;
        }
        f6 = 60.0f * f6 % 360.0f;
        if (f6 < 0.0f) {
            f6 += 360.0f;
        }
        arrf[0] = ColorUtils.constrain(f6, 0.0f, 360.0f);
        arrf[1] = ColorUtils.constrain(f, 0.0f, 1.0f);
        arrf[2] = ColorUtils.constrain(f7, 0.0f, 1.0f);
    }

    public static void RGBToLAB(@IntRange(from=0L, to=255L) int n, @IntRange(from=0L, to=255L) int n2, @IntRange(from=0L, to=255L) int n3, @NonNull double[] arrd) {
        ColorUtils.RGBToXYZ(n, n2, n3, arrd);
        ColorUtils.XYZToLAB(arrd[0], arrd[1], arrd[2], arrd);
    }

    public static void RGBToXYZ(@IntRange(from=0L, to=255L) int n, @IntRange(from=0L, to=255L) int n2, @IntRange(from=0L, to=255L) int n3, @NonNull double[] arrd) {
        if (arrd.length == 3) {
            double d = n;
            Double.isNaN(d);
            d = (d /= 255.0) < 0.04045 ? (d /= 12.92) : Math.pow((d + 0.055) / 1.055, 2.4);
            double d2 = n2;
            Double.isNaN(d2);
            d2 = (d2 /= 255.0) < 0.04045 ? (d2 /= 12.92) : Math.pow((d2 + 0.055) / 1.055, 2.4);
            double d3 = n3;
            Double.isNaN(d3);
            d3 = (d3 /= 255.0) < 0.04045 ? (d3 /= 12.92) : Math.pow((0.055 + d3) / 1.055, 2.4);
            arrd[0] = (0.4124 * d + 0.3576 * d2 + 0.1805 * d3) * 100.0;
            arrd[1] = (0.2126 * d + 0.7152 * d2 + 0.0722 * d3) * 100.0;
            arrd[2] = (0.0193 * d + 0.1192 * d2 + 0.9505 * d3) * 100.0;
            return;
        }
        throw new IllegalArgumentException("outXyz must have a length of 3.");
    }

    @ColorInt
    public static int XYZToColor(@FloatRange(from=0.0, to=95.047) double d, @FloatRange(from=0.0, to=100.0) double d2, @FloatRange(from=0.0, to=108.883) double d3) {
        double d4 = (3.2406 * d + -1.5372 * d2 + -0.4986 * d3) / 100.0;
        double d5 = (-0.9689 * d + 1.8758 * d2 + 0.0415 * d3) / 100.0;
        d3 = (0.0557 * d + -0.204 * d2 + 1.057 * d3) / 100.0;
        d = d4 > 0.0031308 ? Math.pow(d4, 0.4166666666666667) * 1.055 - 0.055 : d4 * 12.92;
        d2 = d5 > 0.0031308 ? Math.pow(d5, 0.4166666666666667) * 1.055 - 0.055 : d5 * 12.92;
        d3 = d3 > 0.0031308 ? Math.pow(d3, 0.4166666666666667) * 1.055 - 0.055 : (d3 *= 12.92);
        return Color.rgb((int)ColorUtils.constrain((int)Math.round(d * 255.0), 0, 255), (int)ColorUtils.constrain((int)Math.round(d2 * 255.0), 0, 255), (int)ColorUtils.constrain((int)Math.round(255.0 * d3), 0, 255));
    }

    public static void XYZToLAB(@FloatRange(from=0.0, to=95.047) double d, @FloatRange(from=0.0, to=100.0) double d2, @FloatRange(from=0.0, to=108.883) double d3, @NonNull double[] arrd) {
        if (arrd.length == 3) {
            d = ColorUtils.pivotXyzComponent(d / 95.047);
            d2 = ColorUtils.pivotXyzComponent(d2 / 100.0);
            d3 = ColorUtils.pivotXyzComponent(d3 / 108.883);
            arrd[0] = Math.max(0.0, 116.0 * d2 - 16.0);
            arrd[1] = (d - d2) * 500.0;
            arrd[2] = (d2 - d3) * 200.0;
            return;
        }
        throw new IllegalArgumentException("outLab must have a length of 3.");
    }

    @ColorInt
    public static int blendARGB(@ColorInt int n, @ColorInt int n2, @FloatRange(from=0.0, to=1.0) float f) {
        float f2 = 1.0f - f;
        float f3 = Color.alpha((int)n);
        float f4 = Color.alpha((int)n2);
        float f5 = Color.red((int)n);
        float f6 = Color.red((int)n2);
        float f7 = Color.green((int)n);
        float f8 = Color.green((int)n2);
        float f9 = Color.blue((int)n);
        float f10 = Color.blue((int)n2);
        return Color.argb((int)((int)(f3 * f2 + f4 * f)), (int)((int)(f5 * f2 + f6 * f)), (int)((int)(f7 * f2 + f8 * f)), (int)((int)(f9 * f2 + f10 * f)));
    }

    public static void blendHSL(@NonNull float[] arrf, @NonNull float[] arrf2, @FloatRange(from=0.0, to=1.0) float f, @NonNull float[] arrf3) {
        if (arrf3.length == 3) {
            float f2 = 1.0f - f;
            arrf3[0] = ColorUtils.circularInterpolate(arrf[0], arrf2[0], f);
            arrf3[1] = arrf[1] * f2 + arrf2[1] * f;
            arrf3[2] = arrf[2] * f2 + arrf2[2] * f;
            return;
        }
        throw new IllegalArgumentException("result must have a length of 3.");
    }

    public static void blendLAB(@NonNull double[] arrd, @NonNull double[] arrd2, @FloatRange(from=0.0, to=1.0) double d, @NonNull double[] arrd3) {
        if (arrd3.length == 3) {
            double d2 = 1.0 - d;
            arrd3[0] = arrd[0] * d2 + arrd2[0] * d;
            arrd3[1] = arrd[1] * d2 + arrd2[1] * d;
            arrd3[2] = arrd[2] * d2 + arrd2[2] * d;
            return;
        }
        throw new IllegalArgumentException("outResult must have a length of 3.");
    }

    public static double calculateContrast(@ColorInt int n, @ColorInt int n2) {
        if (Color.alpha((int)n2) == 255) {
            if (Color.alpha((int)n) < 255) {
                n = ColorUtils.compositeColors(n, n2);
            }
            double d = ColorUtils.calculateLuminance(n) + 0.05;
            double d2 = ColorUtils.calculateLuminance(n2) + 0.05;
            return Math.max(d, d2) / Math.min(d, d2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("background can not be translucent: #");
        stringBuilder.append(Integer.toHexString(n2));
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @FloatRange(from=0.0, to=1.0)
    public static double calculateLuminance(@ColorInt int n) {
        double[] arrd = ColorUtils.getTempDouble3Array();
        ColorUtils.colorToXYZ(n, arrd);
        return arrd[1] / 100.0;
    }

    public static int calculateMinimumAlpha(@ColorInt int n, @ColorInt int n2, float f) {
        if (Color.alpha((int)n2) == 255) {
            if (ColorUtils.calculateContrast(ColorUtils.setAlphaComponent(n, 255), n2) < (double)f) {
                return -1;
            }
            int n3 = 0;
            int n4 = 255;
            for (int i = 0; i <= 10 && n4 - n3 > 1; ++i) {
                int n5 = (n3 + n4) / 2;
                if (ColorUtils.calculateContrast(ColorUtils.setAlphaComponent(n, n5), n2) < (double)f) {
                    n3 = n5;
                    continue;
                }
                n4 = n5;
            }
            return n4;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("background can not be translucent: #");
        stringBuilder.append(Integer.toHexString(n2));
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @VisibleForTesting
    static float circularInterpolate(float f, float f2, float f3) {
        if (Math.abs(f2 - f) > 180.0f) {
            if (f2 > f) {
                f += 360.0f;
            } else {
                f2 += 360.0f;
            }
        }
        return ((f2 - f) * f3 + f) % 360.0f;
    }

    public static void colorToHSL(@ColorInt int n, @NonNull float[] arrf) {
        ColorUtils.RGBToHSL(Color.red((int)n), Color.green((int)n), Color.blue((int)n), arrf);
    }

    public static void colorToLAB(@ColorInt int n, @NonNull double[] arrd) {
        ColorUtils.RGBToLAB(Color.red((int)n), Color.green((int)n), Color.blue((int)n), arrd);
    }

    public static void colorToXYZ(@ColorInt int n, @NonNull double[] arrd) {
        ColorUtils.RGBToXYZ(Color.red((int)n), Color.green((int)n), Color.blue((int)n), arrd);
    }

    private static int compositeAlpha(int n, int n2) {
        return 255 - (255 - n2) * (255 - n) / 255;
    }

    public static int compositeColors(@ColorInt int n, @ColorInt int n2) {
        int n3 = Color.alpha((int)n2);
        int n4 = Color.alpha((int)n);
        int n5 = ColorUtils.compositeAlpha(n4, n3);
        return Color.argb((int)n5, (int)ColorUtils.compositeComponent(Color.red((int)n), n4, Color.red((int)n2), n3, n5), (int)ColorUtils.compositeComponent(Color.green((int)n), n4, Color.green((int)n2), n3, n5), (int)ColorUtils.compositeComponent(Color.blue((int)n), n4, Color.blue((int)n2), n3, n5));
    }

    private static int compositeComponent(int n, int n2, int n3, int n4, int n5) {
        if (n5 == 0) {
            return 0;
        }
        return (n * 255 * n2 + n3 * n4 * (255 - n2)) / (n5 * 255);
    }

    private static float constrain(float f, float f2, float f3) {
        if (f < f2) {
            return f2;
        }
        if (f > f3) {
            return f3;
        }
        return f;
    }

    private static int constrain(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }

    public static double distanceEuclidean(@NonNull double[] arrd, @NonNull double[] arrd2) {
        return Math.sqrt(Math.pow(arrd[0] - arrd2[0], 2.0) + Math.pow(arrd[1] - arrd2[1], 2.0) + Math.pow(arrd[2] - arrd2[2], 2.0));
    }

    private static double[] getTempDouble3Array() {
        double[] arrd = TEMP_ARRAY.get();
        if (arrd == null) {
            arrd = new double[3];
            TEMP_ARRAY.set(arrd);
            return arrd;
        }
        return arrd;
    }

    private static double pivotXyzComponent(double d) {
        if (d > 0.008856) {
            return Math.pow(d, 0.3333333333333333);
        }
        return (903.3 * d + 16.0) / 116.0;
    }

    @ColorInt
    public static int setAlphaComponent(@ColorInt int n, @IntRange(from=0L, to=255L) int n2) {
        if (n2 >= 0 && n2 <= 255) {
            return 16777215 & n | n2 << 24;
        }
        throw new IllegalArgumentException("alpha must be between 0 and 255.");
    }
}

