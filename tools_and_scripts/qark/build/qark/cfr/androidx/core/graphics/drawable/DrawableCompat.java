/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.graphics.drawable.DrawableContainer
 *  android.graphics.drawable.DrawableContainer$DrawableContainerState
 *  android.graphics.drawable.InsetDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import androidx.core.graphics.drawable.TintAwareDrawable;
import androidx.core.graphics.drawable.WrappedDrawable;
import androidx.core.graphics.drawable.WrappedDrawableApi14;
import androidx.core.graphics.drawable.WrappedDrawableApi21;
import java.io.IOException;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class DrawableCompat {
    private static final String TAG = "DrawableCompat";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;

    private DrawableCompat() {
    }

    public static void applyTheme(Drawable drawable2, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 21) {
            drawable2.applyTheme(theme);
        }
    }

    public static boolean canApplyTheme(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return drawable2.canApplyTheme();
        }
        return false;
    }

    public static void clearColorFilter(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 23) {
            drawable2.clearColorFilter();
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            drawable2.clearColorFilter();
            if (drawable2 instanceof InsetDrawable) {
                DrawableCompat.clearColorFilter(((InsetDrawable)drawable2).getDrawable());
                return;
            }
            if (drawable2 instanceof WrappedDrawable) {
                DrawableCompat.clearColorFilter(((WrappedDrawable)drawable2).getWrappedDrawable());
                return;
            }
            if (drawable2 instanceof DrawableContainer) {
                if ((drawable2 = (DrawableContainer.DrawableContainerState)((DrawableContainer)drawable2).getConstantState()) != null) {
                    int n = drawable2.getChildCount();
                    for (int i = 0; i < n; ++i) {
                        Drawable drawable3 = drawable2.getChild(i);
                        if (drawable3 == null) continue;
                        DrawableCompat.clearColorFilter(drawable3);
                    }
                }
                return;
            }
        } else {
            drawable2.clearColorFilter();
        }
    }

    public static int getAlpha(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return drawable2.getAlpha();
        }
        return 0;
    }

    public static ColorFilter getColorFilter(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return drawable2.getColorFilter();
        }
        return null;
    }

    public static int getLayoutDirection(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return drawable2.getLayoutDirection();
        }
        if (Build.VERSION.SDK_INT >= 17) {
            Method method;
            if (!sGetLayoutDirectionMethodFetched) {
                try {
                    sGetLayoutDirectionMethod = method = Drawable.class.getDeclaredMethod("getLayoutDirection", new Class[0]);
                    method.setAccessible(true);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    Log.i((String)"DrawableCompat", (String)"Failed to retrieve getLayoutDirection() method", (Throwable)noSuchMethodException);
                }
                sGetLayoutDirectionMethodFetched = true;
            }
            if ((method = sGetLayoutDirectionMethod) != null) {
                try {
                    int n = (Integer)method.invoke((Object)drawable2, new Object[0]);
                    return n;
                }
                catch (Exception exception) {
                    Log.i((String)"DrawableCompat", (String)"Failed to invoke getLayoutDirection() via reflection", (Throwable)exception);
                    sGetLayoutDirectionMethod = null;
                }
            }
            return 0;
        }
        return 0;
    }

    public static void inflate(Drawable drawable2, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        if (Build.VERSION.SDK_INT >= 21) {
            drawable2.inflate(resources, xmlPullParser, attributeSet, theme);
            return;
        }
        drawable2.inflate(resources, xmlPullParser, attributeSet);
    }

    public static boolean isAutoMirrored(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return drawable2.isAutoMirrored();
        }
        return false;
    }

    @Deprecated
    public static void jumpToCurrentState(Drawable drawable2) {
        drawable2.jumpToCurrentState();
    }

    public static void setAutoMirrored(Drawable drawable2, boolean bl) {
        if (Build.VERSION.SDK_INT >= 19) {
            drawable2.setAutoMirrored(bl);
        }
    }

    public static void setHotspot(Drawable drawable2, float f, float f2) {
        if (Build.VERSION.SDK_INT >= 21) {
            drawable2.setHotspot(f, f2);
        }
    }

    public static void setHotspotBounds(Drawable drawable2, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 21) {
            drawable2.setHotspotBounds(n, n2, n3, n4);
        }
    }

    public static boolean setLayoutDirection(Drawable drawable2, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            return drawable2.setLayoutDirection(n);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            Method method;
            if (!sSetLayoutDirectionMethodFetched) {
                try {
                    sSetLayoutDirectionMethod = method = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE);
                    method.setAccessible(true);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    Log.i((String)"DrawableCompat", (String)"Failed to retrieve setLayoutDirection(int) method", (Throwable)noSuchMethodException);
                }
                sSetLayoutDirectionMethodFetched = true;
            }
            if ((method = sSetLayoutDirectionMethod) != null) {
                try {
                    method.invoke((Object)drawable2, n);
                    return true;
                }
                catch (Exception exception) {
                    Log.i((String)"DrawableCompat", (String)"Failed to invoke setLayoutDirection(int) via reflection", (Throwable)exception);
                    sSetLayoutDirectionMethod = null;
                }
            }
            return false;
        }
        return false;
    }

    public static void setTint(Drawable drawable2, int n) {
        if (Build.VERSION.SDK_INT >= 21) {
            drawable2.setTint(n);
            return;
        }
        if (drawable2 instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable2).setTint(n);
        }
    }

    public static void setTintList(Drawable drawable2, ColorStateList colorStateList) {
        if (Build.VERSION.SDK_INT >= 21) {
            drawable2.setTintList(colorStateList);
            return;
        }
        if (drawable2 instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable2).setTintList(colorStateList);
        }
    }

    public static void setTintMode(Drawable drawable2, PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT >= 21) {
            drawable2.setTintMode(mode);
            return;
        }
        if (drawable2 instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable2).setTintMode(mode);
        }
    }

    public static <T extends Drawable> T unwrap(Drawable drawable2) {
        if (drawable2 instanceof WrappedDrawable) {
            return (T)((WrappedDrawable)drawable2).getWrappedDrawable();
        }
        return (T)drawable2;
    }

    public static Drawable wrap(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return drawable2;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (!(drawable2 instanceof TintAwareDrawable)) {
                return new WrappedDrawableApi21(drawable2);
            }
            return drawable2;
        }
        if (!(drawable2 instanceof TintAwareDrawable)) {
            return new WrappedDrawableApi14(drawable2);
        }
        return drawable2;
    }
}

