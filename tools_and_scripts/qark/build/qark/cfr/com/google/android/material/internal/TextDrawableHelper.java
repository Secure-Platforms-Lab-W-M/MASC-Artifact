/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Typeface
 *  android.text.TextPaint
 */
package com.google.android.material.internal;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;
import java.lang.ref.WeakReference;

public class TextDrawableHelper {
    private WeakReference<TextDrawableDelegate> delegate;
    private final TextAppearanceFontCallback fontCallback;
    private TextAppearance textAppearance;
    private final TextPaint textPaint = new TextPaint(1);
    private float textWidth;
    private boolean textWidthDirty;

    public TextDrawableHelper(TextDrawableDelegate textDrawableDelegate) {
        this.fontCallback = new TextAppearanceFontCallback(){

            @Override
            public void onFontRetrievalFailed(int n) {
                TextDrawableHelper.this.textWidthDirty = true;
                TextDrawableDelegate textDrawableDelegate = (TextDrawableDelegate)TextDrawableHelper.this.delegate.get();
                if (textDrawableDelegate != null) {
                    textDrawableDelegate.onTextSizeChange();
                }
            }

            @Override
            public void onFontRetrieved(Typeface object, boolean bl) {
                if (bl) {
                    return;
                }
                TextDrawableHelper.this.textWidthDirty = true;
                object = (TextDrawableDelegate)TextDrawableHelper.this.delegate.get();
                if (object != null) {
                    object.onTextSizeChange();
                }
            }
        };
        this.textWidthDirty = true;
        this.delegate = new WeakReference<Object>(null);
        this.setDelegate(textDrawableDelegate);
    }

    private float calculateTextWidth(CharSequence charSequence) {
        if (charSequence == null) {
            return 0.0f;
        }
        return this.textPaint.measureText(charSequence, 0, charSequence.length());
    }

    public TextAppearance getTextAppearance() {
        return this.textAppearance;
    }

    public TextPaint getTextPaint() {
        return this.textPaint;
    }

    public float getTextWidth(String string2) {
        float f;
        if (!this.textWidthDirty) {
            return this.textWidth;
        }
        this.textWidth = f = this.calculateTextWidth(string2);
        this.textWidthDirty = false;
        return f;
    }

    public boolean isTextWidthDirty() {
        return this.textWidthDirty;
    }

    public void setDelegate(TextDrawableDelegate textDrawableDelegate) {
        this.delegate = new WeakReference<TextDrawableDelegate>(textDrawableDelegate);
    }

    public void setTextAppearance(TextAppearance object, Context context) {
        if (this.textAppearance != object) {
            this.textAppearance = object;
            if (object != null) {
                object.updateMeasureState(context, this.textPaint, this.fontCallback);
                TextDrawableDelegate textDrawableDelegate = this.delegate.get();
                if (textDrawableDelegate != null) {
                    this.textPaint.drawableState = textDrawableDelegate.getState();
                }
                object.updateDrawState(context, this.textPaint, this.fontCallback);
                this.textWidthDirty = true;
            }
            if ((object = this.delegate.get()) != null) {
                object.onTextSizeChange();
                object.onStateChange(object.getState());
            }
        }
    }

    public void setTextWidthDirty(boolean bl) {
        this.textWidthDirty = bl;
    }

    public void updateTextPaintDrawState(Context context) {
        this.textAppearance.updateDrawState(context, this.textPaint, this.fontCallback);
    }

    public static interface TextDrawableDelegate {
        public int[] getState();

        public boolean onStateChange(int[] var1);

        public void onTextSizeChange();
    }

}

