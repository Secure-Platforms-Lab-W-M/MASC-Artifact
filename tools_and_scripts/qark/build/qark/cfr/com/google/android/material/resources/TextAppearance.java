/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.graphics.Typeface
 *  android.text.TextPaint
 *  android.util.Log
 *  com.google.android.material.R
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearanceConfig;
import com.google.android.material.resources.TextAppearanceFontCallback;

public class TextAppearance {
    private static final String TAG = "TextAppearance";
    private static final int TYPEFACE_MONOSPACE = 3;
    private static final int TYPEFACE_SANS = 1;
    private static final int TYPEFACE_SERIF = 2;
    private Typeface font;
    public final String fontFamily;
    private final int fontFamilyResourceId;
    private boolean fontResolved = false;
    public final ColorStateList shadowColor;
    public final float shadowDx;
    public final float shadowDy;
    public final float shadowRadius;
    public final boolean textAllCaps;
    public final ColorStateList textColor;
    public final ColorStateList textColorHint;
    public final ColorStateList textColorLink;
    public final float textSize;
    public final int textStyle;
    public final int typeface;

    public TextAppearance(Context context, int n) {
        TypedArray typedArray = context.obtainStyledAttributes(n, R.styleable.TextAppearance);
        this.textSize = typedArray.getDimension(R.styleable.TextAppearance_android_textSize, 0.0f);
        this.textColor = MaterialResources.getColorStateList(context, typedArray, R.styleable.TextAppearance_android_textColor);
        this.textColorHint = MaterialResources.getColorStateList(context, typedArray, R.styleable.TextAppearance_android_textColorHint);
        this.textColorLink = MaterialResources.getColorStateList(context, typedArray, R.styleable.TextAppearance_android_textColorLink);
        this.textStyle = typedArray.getInt(R.styleable.TextAppearance_android_textStyle, 0);
        this.typeface = typedArray.getInt(R.styleable.TextAppearance_android_typeface, 1);
        n = MaterialResources.getIndexWithValue(typedArray, R.styleable.TextAppearance_fontFamily, R.styleable.TextAppearance_android_fontFamily);
        this.fontFamilyResourceId = typedArray.getResourceId(n, 0);
        this.fontFamily = typedArray.getString(n);
        this.textAllCaps = typedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        this.shadowColor = MaterialResources.getColorStateList(context, typedArray, R.styleable.TextAppearance_android_shadowColor);
        this.shadowDx = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.shadowDy = typedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.shadowRadius = typedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        typedArray.recycle();
    }

    private void createFallbackFont() {
        String string2;
        if (this.font == null && (string2 = this.fontFamily) != null) {
            this.font = Typeface.create((String)string2, (int)this.textStyle);
        }
        if (this.font == null) {
            int n = this.typeface;
            this.font = n != 1 ? (n != 2 ? (n != 3 ? Typeface.DEFAULT : Typeface.MONOSPACE) : Typeface.SERIF) : Typeface.SANS_SERIF;
            this.font = Typeface.create((Typeface)this.font, (int)this.textStyle);
        }
    }

    public Typeface getFallbackFont() {
        this.createFallbackFont();
        return this.font;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Typeface getFont(Context context) {
        if (this.fontResolved) {
            return this.font;
        }
        if (!context.isRestricted()) {
            try {
                context = ResourcesCompat.getFont(context, this.fontFamilyResourceId);
                this.font = context;
                if (context != null) {
                    this.font = Typeface.create((Typeface)context, (int)this.textStyle);
                }
            }
            catch (Exception exception) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error loading font ");
                stringBuilder.append(this.fontFamily);
                Log.d((String)"TextAppearance", (String)stringBuilder.toString(), (Throwable)exception);
            }
            catch (Resources.NotFoundException notFoundException) {
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                // empty catch block
            }
        }
        this.createFallbackFont();
        this.fontResolved = true;
        return this.font;
    }

    public void getFontAsync(Context context, final TextPaint textPaint, final TextAppearanceFontCallback textAppearanceFontCallback) {
        this.updateTextPaintMeasureState(textPaint, this.getFallbackFont());
        this.getFontAsync(context, new TextAppearanceFontCallback(){

            @Override
            public void onFontRetrievalFailed(int n) {
                textAppearanceFontCallback.onFontRetrievalFailed(n);
            }

            @Override
            public void onFontRetrieved(Typeface typeface, boolean bl) {
                TextAppearance.this.updateTextPaintMeasureState(textPaint, typeface);
                textAppearanceFontCallback.onFontRetrieved(typeface, bl);
            }
        });
    }

    public void getFontAsync(Context context, final TextAppearanceFontCallback textAppearanceFontCallback) {
        if (TextAppearanceConfig.shouldLoadFontSynchronously()) {
            this.getFont(context);
        } else {
            this.createFallbackFont();
        }
        if (this.fontFamilyResourceId == 0) {
            this.fontResolved = true;
        }
        if (this.fontResolved) {
            textAppearanceFontCallback.onFontRetrieved(this.font, true);
            return;
        }
        try {
            ResourcesCompat.getFont(context, this.fontFamilyResourceId, new ResourcesCompat.FontCallback(){

                @Override
                public void onFontRetrievalFailed(int n) {
                    TextAppearance.this.fontResolved = true;
                    textAppearanceFontCallback.onFontRetrievalFailed(n);
                }

                @Override
                public void onFontRetrieved(Typeface typeface) {
                    TextAppearance textAppearance = TextAppearance.this;
                    textAppearance.font = Typeface.create((Typeface)typeface, (int)textAppearance.textStyle);
                    TextAppearance.this.fontResolved = true;
                    textAppearanceFontCallback.onFontRetrieved(TextAppearance.this.font, false);
                }
            }, null);
        }
        catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error loading font ");
            stringBuilder.append(this.fontFamily);
            Log.d((String)"TextAppearance", (String)stringBuilder.toString(), (Throwable)exception);
            this.fontResolved = true;
            textAppearanceFontCallback.onFontRetrievalFailed(-3);
            return;
        }
        catch (Resources.NotFoundException notFoundException) {
            this.fontResolved = true;
            textAppearanceFontCallback.onFontRetrievalFailed(1);
        }
    }

    public void updateDrawState(Context context, TextPaint textPaint, TextAppearanceFontCallback textAppearanceFontCallback) {
        this.updateMeasureState(context, textPaint, textAppearanceFontCallback);
        context = this.textColor;
        int n = context != null ? context.getColorForState(textPaint.drawableState, this.textColor.getDefaultColor()) : -16777216;
        textPaint.setColor(n);
        float f = this.shadowRadius;
        float f2 = this.shadowDx;
        float f3 = this.shadowDy;
        context = this.shadowColor;
        n = context != null ? context.getColorForState(textPaint.drawableState, this.shadowColor.getDefaultColor()) : 0;
        textPaint.setShadowLayer(f, f2, f3, n);
    }

    public void updateMeasureState(Context context, TextPaint textPaint, TextAppearanceFontCallback textAppearanceFontCallback) {
        if (TextAppearanceConfig.shouldLoadFontSynchronously()) {
            this.updateTextPaintMeasureState(textPaint, this.getFont(context));
            return;
        }
        this.getFontAsync(context, textPaint, textAppearanceFontCallback);
    }

    public void updateTextPaintMeasureState(TextPaint textPaint, Typeface typeface) {
        textPaint.setTypeface(typeface);
        int n = this.textStyle & typeface.getStyle();
        boolean bl = (n & 1) != 0;
        textPaint.setFakeBoldText(bl);
        float f = (n & 2) != 0 ? -0.25f : 0.0f;
        textPaint.setTextSkewX(f);
        textPaint.setTextSize(this.textSize);
    }

}

