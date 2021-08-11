/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Property
 */
package com.google.android.material.animation;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import java.util.WeakHashMap;

public class DrawableAlphaProperty
extends Property<Drawable, Integer> {
    public static final Property<Drawable, Integer> DRAWABLE_ALPHA_COMPAT = new DrawableAlphaProperty();
    private final WeakHashMap<Drawable, Integer> alphaCache = new WeakHashMap();

    private DrawableAlphaProperty() {
        super(Integer.class, "drawableAlphaCompat");
    }

    public Integer get(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return drawable2.getAlpha();
        }
        if (this.alphaCache.containsKey((Object)drawable2)) {
            return this.alphaCache.get((Object)drawable2);
        }
        return 255;
    }

    public void set(Drawable drawable2, Integer n) {
        if (Build.VERSION.SDK_INT < 19) {
            this.alphaCache.put(drawable2, n);
        }
        drawable2.setAlpha(n.intValue());
    }
}

