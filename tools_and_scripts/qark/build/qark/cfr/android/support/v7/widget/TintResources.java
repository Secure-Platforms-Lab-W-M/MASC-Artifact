/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.drawable.Drawable
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ResourcesWrapper;
import java.lang.ref.WeakReference;

class TintResources
extends ResourcesWrapper {
    private final WeakReference<Context> mContextRef;

    public TintResources(@NonNull Context context, @NonNull Resources resources) {
        super(resources);
        this.mContextRef = new WeakReference<Context>(context);
    }

    @Override
    public Drawable getDrawable(int n) throws Resources.NotFoundException {
        Drawable drawable2 = super.getDrawable(n);
        Context context = this.mContextRef.get();
        if (drawable2 != null && context != null) {
            AppCompatDrawableManager.get();
            AppCompatDrawableManager.tintDrawableUsingColorFilter(context, n, drawable2);
            return drawable2;
        }
        return drawable2;
    }
}

