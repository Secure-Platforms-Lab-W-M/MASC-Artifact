// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.util.Log;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

class CompoundButtonCompatDonut
{
    private static final String TAG = "CompoundButtonCompatDonut";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;
    
    static Drawable getButtonDrawable(final CompoundButton compoundButton) {
        while (true) {
            if (!CompoundButtonCompatDonut.sButtonDrawableFieldFetched) {
                while (true) {
                    try {
                        (CompoundButtonCompatDonut.sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable")).setAccessible(true);
                        CompoundButtonCompatDonut.sButtonDrawableFieldFetched = true;
                        if (CompoundButtonCompatDonut.sButtonDrawableField != null) {
                            final Field field = CompoundButtonCompatDonut.sButtonDrawableField;
                            final CompoundButton compoundButton2 = compoundButton;
                            final Object o = field.get(compoundButton2);
                            final Drawable drawable = (Drawable)o;
                            return drawable;
                        }
                        return null;
                    }
                    catch (NoSuchFieldException ex) {
                        Log.i("CompoundButtonCompatDonut", "Failed to retrieve mButtonDrawable field", (Throwable)ex);
                        continue;
                    }
                    break;
                }
                try {
                    final Field field = CompoundButtonCompatDonut.sButtonDrawableField;
                    final CompoundButton compoundButton2 = compoundButton;
                    final Object o = field.get(compoundButton2);
                    final Drawable drawable2;
                    final Drawable drawable = drawable2 = (Drawable)o;
                    return drawable2;
                }
                catch (IllegalAccessException ex2) {
                    Log.i("CompoundButtonCompatDonut", "Failed to get button drawable via reflection", (Throwable)ex2);
                    CompoundButtonCompatDonut.sButtonDrawableField = null;
                }
                return null;
            }
            continue;
        }
    }
    
    static ColorStateList getButtonTintList(final CompoundButton compoundButton) {
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintList();
        }
        return null;
    }
    
    static PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintMode();
        }
        return null;
    }
    
    static void setButtonTintList(final CompoundButton compoundButton, final ColorStateList supportButtonTintList) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintList(supportButtonTintList);
        }
    }
    
    static void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode supportButtonTintMode) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintMode(supportButtonTintMode);
        }
    }
}
