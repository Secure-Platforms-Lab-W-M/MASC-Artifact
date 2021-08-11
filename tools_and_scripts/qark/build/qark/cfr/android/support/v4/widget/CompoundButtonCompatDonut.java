/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.widget.CompoundButton
 */
package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TintableCompoundButton;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

class CompoundButtonCompatDonut {
    private static final String TAG = "CompoundButtonCompatDonut";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;

    CompoundButtonCompatDonut() {
    }

    static Drawable getButtonDrawable(CompoundButton compoundButton) {
        if (!sButtonDrawableFieldFetched) {
            try {
                sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable");
                sButtonDrawableField.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.i((String)"CompoundButtonCompatDonut", (String)"Failed to retrieve mButtonDrawable field", (Throwable)noSuchFieldException);
            }
            sButtonDrawableFieldFetched = true;
        }
        if (sButtonDrawableField != null) {
            try {
                compoundButton = (Drawable)sButtonDrawableField.get((Object)compoundButton);
                return compoundButton;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.i((String)"CompoundButtonCompatDonut", (String)"Failed to get button drawable via reflection", (Throwable)illegalAccessException);
                sButtonDrawableField = null;
            }
        }
        return null;
    }

    static ColorStateList getButtonTintList(CompoundButton compoundButton) {
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintList();
        }
        return null;
    }

    static PorterDuff.Mode getButtonTintMode(CompoundButton compoundButton) {
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintMode();
        }
        return null;
    }

    static void setButtonTintList(CompoundButton compoundButton, ColorStateList colorStateList) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintList(colorStateList);
        }
    }

    static void setButtonTintMode(CompoundButton compoundButton, PorterDuff.Mode mode) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintMode(mode);
        }
    }
}

