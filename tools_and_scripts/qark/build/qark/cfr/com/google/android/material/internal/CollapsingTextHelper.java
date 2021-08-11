/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Color
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.view.View
 */
package com.google.android.material.internal;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import androidx.core.math.MathUtils;
import androidx.core.text.TextDirectionHeuristicCompat;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.resources.CancelableFontCallback;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;

public final class CollapsingTextHelper {
    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT;
    private static final boolean USE_SCALING_TEXTURE;
    private boolean boundsChanged;
    private final Rect collapsedBounds;
    private float collapsedDrawX;
    private float collapsedDrawY;
    private CancelableFontCallback collapsedFontCallback;
    private ColorStateList collapsedShadowColor;
    private float collapsedShadowDx;
    private float collapsedShadowDy;
    private float collapsedShadowRadius;
    private ColorStateList collapsedTextColor;
    private int collapsedTextGravity = 16;
    private float collapsedTextSize = 15.0f;
    private Typeface collapsedTypeface;
    private final RectF currentBounds;
    private float currentDrawX;
    private float currentDrawY;
    private float currentTextSize;
    private Typeface currentTypeface;
    private boolean drawTitle;
    private final Rect expandedBounds;
    private float expandedDrawX;
    private float expandedDrawY;
    private CancelableFontCallback expandedFontCallback;
    private float expandedFraction;
    private ColorStateList expandedShadowColor;
    private float expandedShadowDx;
    private float expandedShadowDy;
    private float expandedShadowRadius;
    private ColorStateList expandedTextColor;
    private int expandedTextGravity = 16;
    private float expandedTextSize = 15.0f;
    private Bitmap expandedTitleTexture;
    private Typeface expandedTypeface;
    private boolean isRtl;
    private TimeInterpolator positionInterpolator;
    private float scale;
    private int[] state;
    private CharSequence text;
    private final TextPaint textPaint;
    private TimeInterpolator textSizeInterpolator;
    private CharSequence textToDraw;
    private float textureAscent;
    private float textureDescent;
    private Paint texturePaint;
    private final TextPaint tmpPaint;
    private boolean useTexture;
    private final View view;

    static {
        boolean bl = Build.VERSION.SDK_INT < 18;
        USE_SCALING_TEXTURE = bl;
        DEBUG_DRAW_PAINT = null;
        if (false) {
            throw new NullPointerException();
        }
    }

    public CollapsingTextHelper(View view) {
        this.view = view;
        this.textPaint = new TextPaint(129);
        this.tmpPaint = new TextPaint((Paint)this.textPaint);
        this.collapsedBounds = new Rect();
        this.expandedBounds = new Rect();
        this.currentBounds = new RectF();
    }

    private static int blendColors(int n, int n2, float f) {
        float f2 = 1.0f - f;
        float f3 = Color.alpha((int)n);
        float f4 = Color.alpha((int)n2);
        float f5 = Color.red((int)n);
        float f6 = Color.red((int)n2);
        float f7 = Color.green((int)n);
        float f8 = Color.green((int)n2);
        float f9 = Color.blue((int)n);
        float f10 = Color.blue((int)n2);
        return Color.argb((int)((int)(f3 * f2 + f4 * f)), (int)((int)(f5 * f2 + f6 * f)), (int)((int)(f7 * f2 + f8 * f)), (int)((int)(f9 * f2 + f10 * f)));
    }

    private void calculateBaseOffsets() {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s2stmt(TypeTransformer.java:820)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:843)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

    private void calculateCurrentOffsets() {
        this.calculateOffsets(this.expandedFraction);
    }

    private boolean calculateIsRtl(CharSequence charSequence) {
        int n = ViewCompat.getLayoutDirection(this.view);
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        TextDirectionHeuristicCompat textDirectionHeuristicCompat = bl ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        return textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
    }

    private void calculateOffsets(float f) {
        this.interpolateBounds(f);
        this.currentDrawX = CollapsingTextHelper.lerp(this.expandedDrawX, this.collapsedDrawX, f, this.positionInterpolator);
        this.currentDrawY = CollapsingTextHelper.lerp(this.expandedDrawY, this.collapsedDrawY, f, this.positionInterpolator);
        this.setInterpolatedTextSize(CollapsingTextHelper.lerp(this.expandedTextSize, this.collapsedTextSize, f, this.textSizeInterpolator));
        if (this.collapsedTextColor != this.expandedTextColor) {
            this.textPaint.setColor(CollapsingTextHelper.blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), f));
        } else {
            this.textPaint.setColor(this.getCurrentCollapsedTextColor());
        }
        this.textPaint.setShadowLayer(CollapsingTextHelper.lerp(this.expandedShadowRadius, this.collapsedShadowRadius, f, null), CollapsingTextHelper.lerp(this.expandedShadowDx, this.collapsedShadowDx, f, null), CollapsingTextHelper.lerp(this.expandedShadowDy, this.collapsedShadowDy, f, null), CollapsingTextHelper.blendColors(this.getCurrentColor(this.expandedShadowColor), this.getCurrentColor(this.collapsedShadowColor), f));
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private void calculateUsingTextSize(float f) {
        float f2;
        Object object;
        if (this.text == null) {
            return;
        }
        float f3 = this.collapsedBounds.width();
        float f4 = this.expandedBounds.width();
        boolean bl = false;
        boolean bl2 = false;
        if (CollapsingTextHelper.isClose(f, this.collapsedTextSize)) {
            f2 = this.collapsedTextSize;
            this.scale = 1.0f;
            object = this.currentTypeface;
            Typeface typeface = this.collapsedTypeface;
            if (object != typeface) {
                this.currentTypeface = typeface;
                bl2 = true;
            }
            f = f3;
        } else {
            f2 = this.expandedTextSize;
            object = this.currentTypeface;
            Typeface typeface = this.expandedTypeface;
            bl2 = bl;
            if (object != typeface) {
                this.currentTypeface = typeface;
                bl2 = true;
            }
            this.scale = CollapsingTextHelper.isClose(f, this.expandedTextSize) ? 1.0f : f / this.expandedTextSize;
            f = this.collapsedTextSize / this.expandedTextSize;
            f = f4 * f > f3 ? Math.min(f3 / f, f4) : f4;
        }
        boolean bl3 = true;
        bl = bl2;
        if (f > 0.0f) {
            bl2 = this.currentTextSize != f2 || this.boundsChanged || bl2;
            this.currentTextSize = f2;
            this.boundsChanged = false;
            bl = bl2;
        }
        if (this.textToDraw == null || bl) {
            this.textPaint.setTextSize(this.currentTextSize);
            this.textPaint.setTypeface(this.currentTypeface);
            object = this.textPaint;
            if (this.scale == 1.0f) {
                bl3 = false;
            }
            object.setLinearText(bl3);
            object = TextUtils.ellipsize((CharSequence)this.text, (TextPaint)this.textPaint, (float)f, (TextUtils.TruncateAt)TextUtils.TruncateAt.END);
            if (!TextUtils.equals((CharSequence)object, (CharSequence)this.textToDraw)) {
                this.textToDraw = object;
                this.isRtl = this.calculateIsRtl((CharSequence)object);
            }
        }
    }

    private void clearTexture() {
        Bitmap bitmap = this.expandedTitleTexture;
        if (bitmap != null) {
            bitmap.recycle();
            this.expandedTitleTexture = null;
        }
    }

    private void ensureExpandedTexture() {
        if (this.expandedTitleTexture == null && !this.expandedBounds.isEmpty()) {
            if (TextUtils.isEmpty((CharSequence)this.textToDraw)) {
                return;
            }
            this.calculateOffsets(0.0f);
            this.textureAscent = this.textPaint.ascent();
            this.textureDescent = this.textPaint.descent();
            TextPaint textPaint = this.textPaint;
            CharSequence charSequence = this.textToDraw;
            int n = Math.round(textPaint.measureText(charSequence, 0, charSequence.length()));
            int n2 = Math.round(this.textureDescent - this.textureAscent);
            if (n > 0) {
                if (n2 <= 0) {
                    return;
                }
                this.expandedTitleTexture = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
                textPaint = new Canvas(this.expandedTitleTexture);
                charSequence = this.textToDraw;
                textPaint.drawText(charSequence, 0, charSequence.length(), 0.0f, (float)n2 - this.textPaint.descent(), (Paint)this.textPaint);
                if (this.texturePaint == null) {
                    this.texturePaint = new Paint(3);
                }
                return;
            }
            return;
        }
    }

    private int getCurrentColor(ColorStateList colorStateList) {
        if (colorStateList == null) {
            return 0;
        }
        int[] arrn = this.state;
        if (arrn != null) {
            return colorStateList.getColorForState(arrn, 0);
        }
        return colorStateList.getDefaultColor();
    }

    private int getCurrentExpandedTextColor() {
        return this.getCurrentColor(this.expandedTextColor);
    }

    private void getTextPaintCollapsed(TextPaint textPaint) {
        textPaint.setTextSize(this.collapsedTextSize);
        textPaint.setTypeface(this.collapsedTypeface);
    }

    private void getTextPaintExpanded(TextPaint textPaint) {
        textPaint.setTextSize(this.expandedTextSize);
        textPaint.setTypeface(this.expandedTypeface);
    }

    private void interpolateBounds(float f) {
        this.currentBounds.left = CollapsingTextHelper.lerp(this.expandedBounds.left, this.collapsedBounds.left, f, this.positionInterpolator);
        this.currentBounds.top = CollapsingTextHelper.lerp(this.expandedDrawY, this.collapsedDrawY, f, this.positionInterpolator);
        this.currentBounds.right = CollapsingTextHelper.lerp(this.expandedBounds.right, this.collapsedBounds.right, f, this.positionInterpolator);
        this.currentBounds.bottom = CollapsingTextHelper.lerp(this.expandedBounds.bottom, this.collapsedBounds.bottom, f, this.positionInterpolator);
    }

    private static boolean isClose(float f, float f2) {
        if (Math.abs(f - f2) < 0.001f) {
            return true;
        }
        return false;
    }

    private static float lerp(float f, float f2, float f3, TimeInterpolator timeInterpolator) {
        float f4 = f3;
        if (timeInterpolator != null) {
            f4 = timeInterpolator.getInterpolation(f3);
        }
        return AnimationUtils.lerp(f, f2, f4);
    }

    private static boolean rectEquals(Rect rect, int n, int n2, int n3, int n4) {
        if (rect.left == n && rect.top == n2 && rect.right == n3 && rect.bottom == n4) {
            return true;
        }
        return false;
    }

    private boolean setCollapsedTypefaceInternal(Typeface typeface) {
        CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        if (this.collapsedTypeface != typeface) {
            this.collapsedTypeface = typeface;
            return true;
        }
        return false;
    }

    private boolean setExpandedTypefaceInternal(Typeface typeface) {
        CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        if (this.expandedTypeface != typeface) {
            this.expandedTypeface = typeface;
            return true;
        }
        return false;
    }

    private void setInterpolatedTextSize(float f) {
        this.calculateUsingTextSize(f);
        boolean bl = USE_SCALING_TEXTURE && this.scale != 1.0f;
        this.useTexture = bl;
        if (bl) {
            this.ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    public float calculateCollapsedTextWidth() {
        if (this.text == null) {
            return 0.0f;
        }
        this.getTextPaintCollapsed(this.tmpPaint);
        TextPaint textPaint = this.tmpPaint;
        CharSequence charSequence = this.text;
        return textPaint.measureText(charSequence, 0, charSequence.length());
    }

    public void draw(Canvas canvas) {
        int n = canvas.save();
        if (this.textToDraw != null && this.drawTitle) {
            float f;
            float f2 = this.currentDrawX;
            float f3 = this.currentDrawY;
            boolean bl = this.useTexture && this.expandedTitleTexture != null;
            if (bl) {
                f = this.textureAscent;
                float f4 = this.scale;
                float f5 = this.textureDescent;
                f *= f4;
            } else {
                f = this.textPaint.ascent();
                float f6 = this.scale;
                this.textPaint.descent();
                float f7 = this.scale;
                f *= f6;
            }
            f = bl ? f3 + f : f3;
            f3 = this.scale;
            if (f3 != 1.0f) {
                canvas.scale(f3, f3, f2, f);
            }
            if (bl) {
                canvas.drawBitmap(this.expandedTitleTexture, f2, f, this.texturePaint);
            } else {
                CharSequence charSequence = this.textToDraw;
                canvas.drawText(charSequence, 0, charSequence.length(), f2, f, (Paint)this.textPaint);
            }
        }
        canvas.restoreToCount(n);
    }

    public void getCollapsedTextActualBounds(RectF rectF) {
        boolean bl = this.calculateIsRtl(this.text);
        Rect rect = this.collapsedBounds;
        float f = !bl ? (float)rect.left : (float)rect.right - this.calculateCollapsedTextWidth();
        rectF.left = f;
        rectF.top = this.collapsedBounds.top;
        f = !bl ? rectF.left + this.calculateCollapsedTextWidth() : (float)this.collapsedBounds.right;
        rectF.right = f;
        rectF.bottom = (float)this.collapsedBounds.top + this.getCollapsedTextHeight();
    }

    public ColorStateList getCollapsedTextColor() {
        return this.collapsedTextColor;
    }

    public int getCollapsedTextGravity() {
        return this.collapsedTextGravity;
    }

    public float getCollapsedTextHeight() {
        this.getTextPaintCollapsed(this.tmpPaint);
        return - this.tmpPaint.ascent();
    }

    public float getCollapsedTextSize() {
        return this.collapsedTextSize;
    }

    public Typeface getCollapsedTypeface() {
        Typeface typeface = this.collapsedTypeface;
        if (typeface != null) {
            return typeface;
        }
        return Typeface.DEFAULT;
    }

    public int getCurrentCollapsedTextColor() {
        return this.getCurrentColor(this.collapsedTextColor);
    }

    public ColorStateList getExpandedTextColor() {
        return this.expandedTextColor;
    }

    public int getExpandedTextGravity() {
        return this.expandedTextGravity;
    }

    public float getExpandedTextHeight() {
        this.getTextPaintExpanded(this.tmpPaint);
        return - this.tmpPaint.ascent();
    }

    public float getExpandedTextSize() {
        return this.expandedTextSize;
    }

    public Typeface getExpandedTypeface() {
        Typeface typeface = this.expandedTypeface;
        if (typeface != null) {
            return typeface;
        }
        return Typeface.DEFAULT;
    }

    public float getExpansionFraction() {
        return this.expandedFraction;
    }

    public CharSequence getText() {
        return this.text;
    }

    public final boolean isStateful() {
        ColorStateList colorStateList = this.collapsedTextColor;
        if (colorStateList != null && colorStateList.isStateful() || (colorStateList = this.expandedTextColor) != null && colorStateList.isStateful()) {
            return true;
        }
        return false;
    }

    void onBoundsChanged() {
        boolean bl = this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0;
        this.drawTitle = bl;
    }

    public void recalculate() {
        if (this.view.getHeight() > 0 && this.view.getWidth() > 0) {
            this.calculateBaseOffsets();
            this.calculateCurrentOffsets();
        }
    }

    public void setCollapsedBounds(int n, int n2, int n3, int n4) {
        if (!CollapsingTextHelper.rectEquals(this.collapsedBounds, n, n2, n3, n4)) {
            this.collapsedBounds.set(n, n2, n3, n4);
            this.boundsChanged = true;
            this.onBoundsChanged();
        }
    }

    public void setCollapsedBounds(Rect rect) {
        this.setCollapsedBounds(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void setCollapsedTextAppearance(int n) {
        TextAppearance textAppearance = new TextAppearance(this.view.getContext(), n);
        if (textAppearance.textColor != null) {
            this.collapsedTextColor = textAppearance.textColor;
        }
        if (textAppearance.textSize != 0.0f) {
            this.collapsedTextSize = textAppearance.textSize;
        }
        if (textAppearance.shadowColor != null) {
            this.collapsedShadowColor = textAppearance.shadowColor;
        }
        this.collapsedShadowDx = textAppearance.shadowDx;
        this.collapsedShadowDy = textAppearance.shadowDy;
        this.collapsedShadowRadius = textAppearance.shadowRadius;
        CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        this.collapsedFontCallback = new CancelableFontCallback(new CancelableFontCallback.ApplyFont(){

            @Override
            public void apply(Typeface typeface) {
                CollapsingTextHelper.this.setCollapsedTypeface(typeface);
            }
        }, textAppearance.getFallbackFont());
        textAppearance.getFontAsync(this.view.getContext(), this.collapsedFontCallback);
        this.recalculate();
    }

    public void setCollapsedTextColor(ColorStateList colorStateList) {
        if (this.collapsedTextColor != colorStateList) {
            this.collapsedTextColor = colorStateList;
            this.recalculate();
        }
    }

    public void setCollapsedTextGravity(int n) {
        if (this.collapsedTextGravity != n) {
            this.collapsedTextGravity = n;
            this.recalculate();
        }
    }

    public void setCollapsedTextSize(float f) {
        if (this.collapsedTextSize != f) {
            this.collapsedTextSize = f;
            this.recalculate();
        }
    }

    public void setCollapsedTypeface(Typeface typeface) {
        if (this.setCollapsedTypefaceInternal(typeface)) {
            this.recalculate();
        }
    }

    public void setExpandedBounds(int n, int n2, int n3, int n4) {
        if (!CollapsingTextHelper.rectEquals(this.expandedBounds, n, n2, n3, n4)) {
            this.expandedBounds.set(n, n2, n3, n4);
            this.boundsChanged = true;
            this.onBoundsChanged();
        }
    }

    public void setExpandedBounds(Rect rect) {
        this.setExpandedBounds(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void setExpandedTextAppearance(int n) {
        TextAppearance textAppearance = new TextAppearance(this.view.getContext(), n);
        if (textAppearance.textColor != null) {
            this.expandedTextColor = textAppearance.textColor;
        }
        if (textAppearance.textSize != 0.0f) {
            this.expandedTextSize = textAppearance.textSize;
        }
        if (textAppearance.shadowColor != null) {
            this.expandedShadowColor = textAppearance.shadowColor;
        }
        this.expandedShadowDx = textAppearance.shadowDx;
        this.expandedShadowDy = textAppearance.shadowDy;
        this.expandedShadowRadius = textAppearance.shadowRadius;
        CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        this.expandedFontCallback = new CancelableFontCallback(new CancelableFontCallback.ApplyFont(){

            @Override
            public void apply(Typeface typeface) {
                CollapsingTextHelper.this.setExpandedTypeface(typeface);
            }
        }, textAppearance.getFallbackFont());
        textAppearance.getFontAsync(this.view.getContext(), this.expandedFontCallback);
        this.recalculate();
    }

    public void setExpandedTextColor(ColorStateList colorStateList) {
        if (this.expandedTextColor != colorStateList) {
            this.expandedTextColor = colorStateList;
            this.recalculate();
        }
    }

    public void setExpandedTextGravity(int n) {
        if (this.expandedTextGravity != n) {
            this.expandedTextGravity = n;
            this.recalculate();
        }
    }

    public void setExpandedTextSize(float f) {
        if (this.expandedTextSize != f) {
            this.expandedTextSize = f;
            this.recalculate();
        }
    }

    public void setExpandedTypeface(Typeface typeface) {
        if (this.setExpandedTypefaceInternal(typeface)) {
            this.recalculate();
        }
    }

    public void setExpansionFraction(float f) {
        if ((f = MathUtils.clamp(f, 0.0f, 1.0f)) != this.expandedFraction) {
            this.expandedFraction = f;
            this.calculateCurrentOffsets();
        }
    }

    public void setPositionInterpolator(TimeInterpolator timeInterpolator) {
        this.positionInterpolator = timeInterpolator;
        this.recalculate();
    }

    public final boolean setState(int[] arrn) {
        this.state = arrn;
        if (this.isStateful()) {
            this.recalculate();
            return true;
        }
        return false;
    }

    public void setText(CharSequence charSequence) {
        if (charSequence == null || !TextUtils.equals((CharSequence)this.text, (CharSequence)charSequence)) {
            this.text = charSequence;
            this.textToDraw = null;
            this.clearTexture();
            this.recalculate();
        }
    }

    public void setTextSizeInterpolator(TimeInterpolator timeInterpolator) {
        this.textSizeInterpolator = timeInterpolator;
        this.recalculate();
    }

    public void setTypefaces(Typeface typeface) {
        boolean bl = this.setCollapsedTypefaceInternal(typeface);
        boolean bl2 = this.setExpandedTypefaceInternal(typeface);
        if (bl || bl2) {
            this.recalculate();
        }
    }

}

