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
 */
package androidx.appcompat.widget;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.graphics.drawable.WrappedDrawable;

public class DrawableUtils {
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final int[] EMPTY_STATE_SET = new int[0];
    public static final Rect INSETS_NONE = new Rect();
    private static final String TAG = "DrawableUtils";
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    private static Class<?> sInsetsClazz;

    static {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                sInsetsClazz = Class.forName("android.graphics.Insets");
                return;
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
    }

    private DrawableUtils() {
    }

    public static boolean canSafelyMutateDrawable(Drawable arrdrawable) {
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
            if (arrdrawable instanceof WrappedDrawable) {
                return DrawableUtils.canSafelyMutateDrawable(((WrappedDrawable)arrdrawable).getWrappedDrawable());
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

    static void fixDrawable(Drawable drawable2) {
        if (Build.VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(drawable2.getClass().getName())) {
            DrawableUtils.fixVectorDrawableTinting(drawable2);
        }
    }

    private static void fixVectorDrawableTinting(Drawable drawable2) {
        int[] arrn = drawable2.getState();
        if (arrn != null && arrn.length != 0) {
            drawable2.setState(EMPTY_STATE_SET);
        } else {
            drawable2.setState(CHECKED_STATE_SET);
        }
        drawable2.setState(arrn);
    }

    /*
     * Exception decompiling
     */
    public static Rect getOpticalBounds(Drawable var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:486)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
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
                            return PorterDuff.Mode.ADD;
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

