// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics.drawable;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources;
import android.util.Log;
import android.graphics.ColorFilter;
import android.graphics.drawable.DrawableContainer$DrawableContainerState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.os.Build$VERSION;
import android.content.res.Resources$Theme;
import android.graphics.drawable.Drawable;
import java.lang.reflect.Method;

public final class DrawableCompat
{
    private static final String TAG = "DrawableCompat";
    private static Method sGetLayoutDirectionMethod;
    private static boolean sGetLayoutDirectionMethodFetched;
    private static Method sSetLayoutDirectionMethod;
    private static boolean sSetLayoutDirectionMethodFetched;
    
    private DrawableCompat() {
    }
    
    public static void applyTheme(final Drawable drawable, final Resources$Theme resources$Theme) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.applyTheme(resources$Theme);
        }
    }
    
    public static boolean canApplyTheme(final Drawable drawable) {
        return Build$VERSION.SDK_INT >= 21 && drawable.canApplyTheme();
    }
    
    public static void clearColorFilter(final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 23) {
            drawable.clearColorFilter();
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.clearColorFilter();
            if (drawable instanceof InsetDrawable) {
                clearColorFilter(((InsetDrawable)drawable).getDrawable());
                return;
            }
            if (drawable instanceof WrappedDrawable) {
                clearColorFilter(((WrappedDrawable)drawable).getWrappedDrawable());
                return;
            }
            if (drawable instanceof DrawableContainer) {
                final DrawableContainer$DrawableContainerState drawableContainer$DrawableContainerState = (DrawableContainer$DrawableContainerState)((DrawableContainer)drawable).getConstantState();
                if (drawableContainer$DrawableContainerState != null) {
                    for (int i = 0; i < drawableContainer$DrawableContainerState.getChildCount(); ++i) {
                        final Drawable child = drawableContainer$DrawableContainerState.getChild(i);
                        if (child != null) {
                            clearColorFilter(child);
                        }
                    }
                }
            }
        }
        else {
            drawable.clearColorFilter();
        }
    }
    
    public static int getAlpha(final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 19) {
            return drawable.getAlpha();
        }
        return 0;
    }
    
    public static ColorFilter getColorFilter(final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 21) {
            return drawable.getColorFilter();
        }
        return null;
    }
    
    public static int getLayoutDirection(final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 23) {
            return drawable.getLayoutDirection();
        }
        if (Build$VERSION.SDK_INT >= 17) {
            if (!DrawableCompat.sGetLayoutDirectionMethodFetched) {
                try {
                    (DrawableCompat.sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", (Class<?>[])new Class[0])).setAccessible(true);
                }
                catch (NoSuchMethodException ex) {
                    Log.i("DrawableCompat", "Failed to retrieve getLayoutDirection() method", (Throwable)ex);
                }
                DrawableCompat.sGetLayoutDirectionMethodFetched = true;
            }
            final Method sGetLayoutDirectionMethod = DrawableCompat.sGetLayoutDirectionMethod;
            if (sGetLayoutDirectionMethod != null) {
                try {
                    return (int)sGetLayoutDirectionMethod.invoke(drawable, new Object[0]);
                }
                catch (Exception ex2) {
                    Log.i("DrawableCompat", "Failed to invoke getLayoutDirection() via reflection", (Throwable)ex2);
                    DrawableCompat.sGetLayoutDirectionMethod = null;
                }
            }
            return 0;
        }
        return 0;
    }
    
    public static void inflate(final Drawable drawable, final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.inflate(resources, xmlPullParser, set, resources$Theme);
            return;
        }
        drawable.inflate(resources, xmlPullParser, set);
    }
    
    public static boolean isAutoMirrored(final Drawable drawable) {
        return Build$VERSION.SDK_INT >= 19 && drawable.isAutoMirrored();
    }
    
    @Deprecated
    public static void jumpToCurrentState(final Drawable drawable) {
        drawable.jumpToCurrentState();
    }
    
    public static void setAutoMirrored(final Drawable drawable, final boolean autoMirrored) {
        if (Build$VERSION.SDK_INT >= 19) {
            drawable.setAutoMirrored(autoMirrored);
        }
    }
    
    public static void setHotspot(final Drawable drawable, final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setHotspot(n, n2);
        }
    }
    
    public static void setHotspotBounds(final Drawable drawable, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setHotspotBounds(n, n2, n3, n4);
        }
    }
    
    public static boolean setLayoutDirection(final Drawable drawable, final int layoutDirection) {
        if (Build$VERSION.SDK_INT >= 23) {
            return drawable.setLayoutDirection(layoutDirection);
        }
        if (Build$VERSION.SDK_INT >= 17) {
            if (!DrawableCompat.sSetLayoutDirectionMethodFetched) {
                try {
                    (DrawableCompat.sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE)).setAccessible(true);
                }
                catch (NoSuchMethodException ex) {
                    Log.i("DrawableCompat", "Failed to retrieve setLayoutDirection(int) method", (Throwable)ex);
                }
                DrawableCompat.sSetLayoutDirectionMethodFetched = true;
            }
            final Method sSetLayoutDirectionMethod = DrawableCompat.sSetLayoutDirectionMethod;
            if (sSetLayoutDirectionMethod != null) {
                try {
                    sSetLayoutDirectionMethod.invoke(drawable, layoutDirection);
                    return true;
                }
                catch (Exception ex2) {
                    Log.i("DrawableCompat", "Failed to invoke setLayoutDirection(int) via reflection", (Throwable)ex2);
                    DrawableCompat.sSetLayoutDirectionMethod = null;
                }
            }
            return false;
        }
        return false;
    }
    
    public static void setTint(final Drawable drawable, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setTint(n);
            return;
        }
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTint(n);
        }
    }
    
    public static void setTintList(final Drawable drawable, final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setTintList(list);
            return;
        }
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTintList(list);
        }
    }
    
    public static void setTintMode(final Drawable drawable, final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            drawable.setTintMode(porterDuff$Mode);
            return;
        }
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable)drawable).setTintMode(porterDuff$Mode);
        }
    }
    
    public static <T extends Drawable> T unwrap(final Drawable drawable) {
        if (drawable instanceof WrappedDrawable) {
            return (T)((WrappedDrawable)drawable).getWrappedDrawable();
        }
        return (T)drawable;
    }
    
    public static Drawable wrap(final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 23) {
            return drawable;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            if (!(drawable instanceof TintAwareDrawable)) {
                return new WrappedDrawableApi21(drawable);
            }
            return drawable;
        }
        else {
            if (!(drawable instanceof TintAwareDrawable)) {
                return new WrappedDrawableApi14(drawable);
            }
            return drawable;
        }
    }
}
