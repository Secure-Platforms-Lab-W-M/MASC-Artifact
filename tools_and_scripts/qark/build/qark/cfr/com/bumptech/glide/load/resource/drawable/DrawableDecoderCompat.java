/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public final class DrawableDecoderCompat {
    private static volatile boolean shouldCallAppCompatResources = true;

    private DrawableDecoderCompat() {
    }

    public static Drawable getDrawable(Context context, int n, Resources.Theme theme) {
        return DrawableDecoderCompat.getDrawable(context, context, n, theme);
    }

    public static Drawable getDrawable(Context context, Context context2, int n) {
        return DrawableDecoderCompat.getDrawable(context, context2, n, null);
    }

    private static Drawable getDrawable(Context context, Context context2, int n, Resources.Theme theme) {
        try {
            if (shouldCallAppCompatResources) {
                Drawable drawable2 = DrawableDecoderCompat.loadDrawableV7(context2, n, theme);
                return drawable2;
            }
        }
        catch (Resources.NotFoundException notFoundException) {
        }
        catch (IllegalStateException illegalStateException) {
            if (!context.getPackageName().equals(context2.getPackageName())) {
                return ContextCompat.getDrawable(context2, n);
            }
            throw illegalStateException;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            shouldCallAppCompatResources = false;
        }
        if (theme == null) {
            theme = context2.getTheme();
        }
        return DrawableDecoderCompat.loadDrawableV4(context2, n, theme);
    }

    private static Drawable loadDrawableV4(Context context, int n, Resources.Theme theme) {
        return ResourcesCompat.getDrawable(context.getResources(), n, theme);
    }

    private static Drawable loadDrawableV7(Context object, int n, Resources.Theme theme) {
        if (theme != null) {
            object = new ContextThemeWrapper((Context)object, theme);
        }
        return AppCompatResources.getDrawable((Context)object, n);
    }
}

