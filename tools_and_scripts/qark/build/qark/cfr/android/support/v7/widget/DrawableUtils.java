/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.graphics.drawable.DrawableContainer
 *  android.graphics.drawable.DrawableContainer$DrawableContainerState
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.ScaleDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package android.support.v7.widget;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.ThemeUtils;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class DrawableUtils {
    public static final Rect INSETS_NONE;
    private static final String TAG = "DrawableUtils";
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    private static Class<?> sInsetsClazz;

    static {
        block2 : {
            INSETS_NONE = new Rect();
            if (Build.VERSION.SDK_INT < 18) break block2;
            try {
                sInsetsClazz = Class.forName("android.graphics.Insets");
            }
            catch (ClassNotFoundException classNotFoundException) {}
        }
    }

    private DrawableUtils() {
    }

    public static boolean canSafelyMutateDrawable(@NonNull Drawable arrdrawable) {
        if (Build.VERSION.SDK_INT < 15 && arrdrawable instanceof InsetDrawable) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 15 && arrdrawable instanceof GradientDrawable) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 17 && arrdrawable instanceof LayerDrawable) {
            return false;
        }
        if (arrdrawable instanceof DrawableContainer) {
            if ((arrdrawable = arrdrawable.getConstantState()) instanceof DrawableContainer.DrawableContainerState) {
                arrdrawable = ((DrawableContainer.DrawableContainerState)arrdrawable).getChildren();
                int n = arrdrawable.length;
                for (int i = 0; i < n; ++i) {
                    if (DrawableUtils.canSafelyMutateDrawable(arrdrawable[i])) continue;
                    return false;
                }
            }
        } else {
            if (arrdrawable instanceof android.support.v4.graphics.drawable.DrawableWrapper) {
                return DrawableUtils.canSafelyMutateDrawable(((android.support.v4.graphics.drawable.DrawableWrapper)arrdrawable).getWrappedDrawable());
            }
            if (arrdrawable instanceof DrawableWrapper) {
                return DrawableUtils.canSafelyMutateDrawable(((DrawableWrapper)arrdrawable).getWrappedDrawable());
            }
            if (arrdrawable instanceof ScaleDrawable) {
                return DrawableUtils.canSafelyMutateDrawable(((ScaleDrawable)arrdrawable).getDrawable());
            }
        }
        return true;
    }

    static void fixDrawable(@NonNull Drawable drawable2) {
        if (Build.VERSION.SDK_INT == 21) {
            if ("android.graphics.drawable.VectorDrawable".equals(drawable2.getClass().getName())) {
                DrawableUtils.fixVectorDrawableTinting(drawable2);
                return;
            }
            return;
        }
    }

    private static void fixVectorDrawableTinting(Drawable drawable2) {
        int[] arrn = drawable2.getState();
        if (arrn != null && arrn.length != 0) {
            drawable2.setState(ThemeUtils.EMPTY_STATE_SET);
        } else {
            drawable2.setState(ThemeUtils.CHECKED_STATE_SET);
        }
        drawable2.setState(arrn);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Rect getOpticalBounds(Drawable object) {
        if (sInsetsClazz == null) return INSETS_NONE;
        object = DrawableCompat.unwrap((Drawable)object);
        object = object.getClass().getMethod("getOpticalInsets", new Class[0]).invoke(object, new Object[0]);
        if (object == null) return INSETS_NONE;
        Rect rect = new Rect();
        Field[] arrfield = sInsetsClazz.getFields();
        int n = arrfield.length;
        int n2 = 0;
        while (n2 < n) {
            int n3;
            Field field;
            block14 : {
                block13 : {
                    field = arrfield[n2];
                    try {
                        String string2 = field.getName();
                        n3 = string2.hashCode();
                        if (n3 != -1383228885) {
                            if (n3 != 115029) {
                                if (n3 != 3317767) {
                                    if (n3 != 108511772 || !string2.equals("right")) break block13;
                                    n3 = 2;
                                    break block14;
                                }
                                if (!string2.equals("left")) break block13;
                                n3 = 0;
                                break block14;
                            }
                            if (!string2.equals("top")) break block13;
                            n3 = 1;
                            break block14;
                        }
                        if (!string2.equals("bottom")) break block13;
                        n3 = 3;
                        break block14;
                    }
                    catch (Exception exception) {
                        Log.e((String)"DrawableUtils", (String)"Couldn't obtain the optical insets. Ignoring.");
                        return INSETS_NONE;
                    }
                }
                n3 = -1;
            }
            switch (n3) {
                case 3: {
                    rect.bottom = field.getInt(object);
                    break;
                }
                case 2: {
                    rect.right = field.getInt(object);
                    break;
                }
                case 1: {
                    rect.top = field.getInt(object);
                    break;
                }
                case 0: {
                    rect.left = field.getInt(object);
                    break;
                }
            }
            ++n2;
        }
        return rect;
    }

    public static PorterDuff.Mode parseTintMode(int n, PorterDuff.Mode mode) {
        if (n != 3) {
            if (n != 5) {
                if (n != 9) {
                    switch (n) {
                        default: {
                            return mode;
                        }
                        case 16: {
                            if (Build.VERSION.SDK_INT >= 11) {
                                return PorterDuff.Mode.valueOf((String)"ADD");
                            }
                            return mode;
                        }
                        case 15: {
                            return PorterDuff.Mode.SCREEN;
                        }
                        case 14: 
                    }
                    return PorterDuff.Mode.MULTIPLY;
                }
                return PorterDuff.Mode.SRC_ATOP;
            }
            return PorterDuff.Mode.SRC_IN;
        }
        return PorterDuff.Mode.SRC_OVER;
    }
}

