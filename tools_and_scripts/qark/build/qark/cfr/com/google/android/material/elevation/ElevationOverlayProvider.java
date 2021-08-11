/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Color
 *  android.util.DisplayMetrics
 *  android.view.View
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 */
package com.google.android.material.elevation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;

public class ElevationOverlayProvider {
    private static final float FORMULA_MULTIPLIER = 4.5f;
    private static final float FORMULA_OFFSET = 2.0f;
    private final int colorSurface;
    private final float displayDensity;
    private final int elevationOverlayColor;
    private final boolean elevationOverlayEnabled;

    public ElevationOverlayProvider(Context context) {
        this.elevationOverlayEnabled = MaterialAttributes.resolveBoolean(context, R.attr.elevationOverlayEnabled, false);
        this.elevationOverlayColor = MaterialColors.getColor(context, R.attr.elevationOverlayColor, 0);
        this.colorSurface = MaterialColors.getColor(context, R.attr.colorSurface, 0);
        this.displayDensity = context.getResources().getDisplayMetrics().density;
    }

    private boolean isThemeSurfaceColor(int n) {
        if (ColorUtils.setAlphaComponent(n, 255) == this.colorSurface) {
            return true;
        }
        return false;
    }

    public int calculateOverlayAlpha(float f) {
        return Math.round(this.calculateOverlayAlphaFraction(f) * 255.0f);
    }

    public float calculateOverlayAlphaFraction(float f) {
        float f2 = this.displayDensity;
        if (f2 > 0.0f) {
            if (f <= 0.0f) {
                return 0.0f;
            }
            return Math.min(((float)Math.log1p(f / f2) * 4.5f + 2.0f) / 100.0f, 1.0f);
        }
        return 0.0f;
    }

    public int compositeOverlay(int n, float f) {
        f = this.calculateOverlayAlphaFraction(f);
        int n2 = Color.alpha((int)n);
        return ColorUtils.setAlphaComponent(MaterialColors.layer(ColorUtils.setAlphaComponent(n, 255), this.elevationOverlayColor, f), n2);
    }

    public int compositeOverlay(int n, float f, View view) {
        return this.compositeOverlay(n, f + this.getParentAbsoluteElevation(view));
    }

    public int compositeOverlayIfNeeded(int n, float f) {
        if (this.elevationOverlayEnabled && this.isThemeSurfaceColor(n)) {
            return this.compositeOverlay(n, f);
        }
        return n;
    }

    public int compositeOverlayIfNeeded(int n, float f, View view) {
        return this.compositeOverlayIfNeeded(n, f + this.getParentAbsoluteElevation(view));
    }

    public int compositeOverlayWithThemeSurfaceColorIfNeeded(float f) {
        return this.compositeOverlayIfNeeded(this.colorSurface, f);
    }

    public int compositeOverlayWithThemeSurfaceColorIfNeeded(float f, View view) {
        return this.compositeOverlayWithThemeSurfaceColorIfNeeded(f + this.getParentAbsoluteElevation(view));
    }

    public float getParentAbsoluteElevation(View view) {
        return ViewUtils.getParentAbsoluteElevation(view);
    }

    public int getThemeElevationOverlayColor() {
        return this.elevationOverlayColor;
    }

    public int getThemeSurfaceColor() {
        return this.colorSurface;
    }

    public boolean isThemeElevationOverlayEnabled() {
        return this.elevationOverlayEnabled;
    }
}

