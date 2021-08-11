// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.graphics.PorterDuff$Mode;
import java.lang.reflect.Field;
import android.graphics.Insets;
import android.util.Log;
import androidx.core.graphics.drawable.DrawableCompat;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.drawable.ScaleDrawable;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.graphics.drawable.WrappedDrawable;
import android.graphics.drawable.DrawableContainer$DrawableContainerState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.graphics.Rect;

public class DrawableUtils
{
    private static final int[] CHECKED_STATE_SET;
    private static final int[] EMPTY_STATE_SET;
    public static final Rect INSETS_NONE;
    private static final String TAG = "DrawableUtils";
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    private static Class<?> sInsetsClazz;
    
    static {
        CHECKED_STATE_SET = new int[] { 16842912 };
        EMPTY_STATE_SET = new int[0];
        INSETS_NONE = new Rect();
        if (Build$VERSION.SDK_INT >= 18) {
            try {
                DrawableUtils.sInsetsClazz = Class.forName("android.graphics.Insets");
            }
            catch (ClassNotFoundException ex) {}
        }
    }
    
    private DrawableUtils() {
    }
    
    public static boolean canSafelyMutateDrawable(final Drawable drawable) {
        if (Build$VERSION.SDK_INT < 15 && drawable instanceof InsetDrawable) {
            return false;
        }
        if (Build$VERSION.SDK_INT < 15 && drawable instanceof GradientDrawable) {
            return false;
        }
        if (Build$VERSION.SDK_INT < 17 && drawable instanceof LayerDrawable) {
            return false;
        }
        if (drawable instanceof DrawableContainer) {
            final Drawable$ConstantState constantState = drawable.getConstantState();
            if (constantState instanceof DrawableContainer$DrawableContainerState) {
                final Drawable[] children = ((DrawableContainer$DrawableContainerState)constantState).getChildren();
                for (int length = children.length, i = 0; i < length; ++i) {
                    if (!canSafelyMutateDrawable(children[i])) {
                        return false;
                    }
                }
            }
        }
        else {
            if (drawable instanceof WrappedDrawable) {
                return canSafelyMutateDrawable(((WrappedDrawable)drawable).getWrappedDrawable());
            }
            if (drawable instanceof DrawableWrapper) {
                return canSafelyMutateDrawable(((DrawableWrapper)drawable).getWrappedDrawable());
            }
            if (drawable instanceof ScaleDrawable) {
                return canSafelyMutateDrawable(((ScaleDrawable)drawable).getDrawable());
            }
        }
        return true;
    }
    
    static void fixDrawable(final Drawable drawable) {
        if (Build$VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName())) {
            fixVectorDrawableTinting(drawable);
        }
    }
    
    private static void fixVectorDrawableTinting(final Drawable drawable) {
        final int[] state = drawable.getState();
        if (state != null && state.length != 0) {
            drawable.setState(DrawableUtils.EMPTY_STATE_SET);
        }
        else {
            drawable.setState(DrawableUtils.CHECKED_STATE_SET);
        }
        drawable.setState(state);
    }
    
    public static Rect getOpticalBounds(Drawable unwrap) {
        if (Build$VERSION.SDK_INT >= 29) {
            final Insets opticalInsets = unwrap.getOpticalInsets();
            final Rect rect = new Rect();
            rect.left = opticalInsets.left;
            rect.right = opticalInsets.right;
            rect.top = opticalInsets.top;
            rect.bottom = opticalInsets.bottom;
            return rect;
        }
        if (DrawableUtils.sInsetsClazz != null) {
            Object invoke;
            Rect rect2;
            Field[] fields;
            int length;
            int n;
            Field field;
            String name;
            int n2 = 0;
            Label_0301_Outer:Label_0248_Outer:
            while (true) {
                while (true) {
                    Label_0290_Outer:Label_0262_Outer:
                    while (true) {
                        while (true) {
                            while (true) {
                                while (true) {
                                    Label_0327: {
                                        try {
                                            unwrap = DrawableCompat.unwrap(unwrap);
                                            invoke = unwrap.getClass().getMethod("getOpticalInsets", (Class<?>[])new Class[0]).invoke(unwrap, new Object[0]);
                                            if (invoke == null) {
                                                break;
                                            }
                                            rect2 = new Rect();
                                            fields = DrawableUtils.sInsetsClazz.getFields();
                                            length = fields.length;
                                            n = 0;
                                            while (true) {
                                                if (n >= length) {
                                                    return rect2;
                                                }
                                                field = fields[n];
                                                name = field.getName();
                                                n2 = -1;
                                                switch (name.hashCode()) {
                                                    case 108511772: {
                                                        if (name.equals("right")) {
                                                            n2 = 2;
                                                        }
                                                        break Label_0327;
                                                    }
                                                    case 3317767: {
                                                        if (name.equals("left")) {
                                                            n2 = 0;
                                                        }
                                                        break Label_0327;
                                                    }
                                                    case 115029: {
                                                        if (name.equals("top")) {
                                                            n2 = 1;
                                                        }
                                                        break Label_0327;
                                                    }
                                                    case -1383228885: {
                                                        if (name.equals("bottom")) {
                                                            n2 = 3;
                                                        }
                                                        break Label_0327;
                                                    }
                                                    default: {
                                                        break Label_0327;
                                                    }
                                                }
                                                ++n;
                                                continue Label_0301_Outer;
                                            }
                                            rect2.left = field.getInt(invoke);
                                            continue Label_0290_Outer;
                                            rect2.bottom = field.getInt(invoke);
                                            continue Label_0290_Outer;
                                            rect2.top = field.getInt(invoke);
                                            continue Label_0290_Outer;
                                            rect2.right = field.getInt(invoke);
                                            continue Label_0290_Outer;
                                        }
                                        catch (Exception ex) {
                                            Log.e("DrawableUtils", "Couldn't obtain the optical insets. Ignoring.");
                                        }
                                        break;
                                    }
                                    if (n2 == 0) {
                                        continue Label_0248_Outer;
                                    }
                                    break;
                                }
                                if (n2 == 1) {
                                    continue Label_0262_Outer;
                                }
                                break;
                            }
                            if (n2 == 2) {
                                continue;
                            }
                            break;
                        }
                        if (n2 != 3) {
                            continue Label_0290_Outer;
                        }
                        break;
                    }
                    continue;
                }
            }
        }
        return DrawableUtils.INSETS_NONE;
    }
    
    public static PorterDuff$Mode parseTintMode(final int n, final PorterDuff$Mode porterDuff$Mode) {
        if (n == 3) {
            return PorterDuff$Mode.SRC_OVER;
        }
        if (n == 5) {
            return PorterDuff$Mode.SRC_IN;
        }
        if (n == 9) {
            return PorterDuff$Mode.SRC_ATOP;
        }
        switch (n) {
            default: {
                return porterDuff$Mode;
            }
            case 16: {
                return PorterDuff$Mode.ADD;
            }
            case 15: {
                return PorterDuff$Mode.SCREEN;
            }
            case 14: {
                return PorterDuff$Mode.MULTIPLY;
            }
        }
    }
}
