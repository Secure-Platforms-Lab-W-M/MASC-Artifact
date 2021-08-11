/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.LinearGradient
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Path
 *  android.graphics.RadialGradient
 *  android.graphics.RectF
 *  android.graphics.Region
 *  android.graphics.Region$Op
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 */
package com.google.android.material.shadow;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import androidx.core.graphics.ColorUtils;

public class ShadowRenderer {
    private static final int COLOR_ALPHA_END = 0;
    private static final int COLOR_ALPHA_MIDDLE = 20;
    private static final int COLOR_ALPHA_START = 68;
    private static final int[] cornerColors;
    private static final float[] cornerPositions;
    private static final int[] edgeColors;
    private static final float[] edgePositions;
    private final Paint cornerShadowPaint;
    private final Paint edgeShadowPaint;
    private final Path scratch = new Path();
    private int shadowEndColor;
    private int shadowMiddleColor;
    private final Paint shadowPaint;
    private int shadowStartColor;

    static {
        edgeColors = new int[3];
        edgePositions = new float[]{0.0f, 0.5f, 1.0f};
        cornerColors = new int[4];
        cornerPositions = new float[]{0.0f, 0.0f, 0.5f, 1.0f};
    }

    public ShadowRenderer() {
        this(-16777216);
    }

    public ShadowRenderer(int n) {
        Paint paint;
        this.setShadowColor(n);
        this.cornerShadowPaint = paint = new Paint(4);
        paint.setStyle(Paint.Style.FILL);
        this.shadowPaint = paint = new Paint();
        paint.setColor(this.shadowStartColor);
        this.edgeShadowPaint = new Paint(this.cornerShadowPaint);
    }

    public void drawCornerShadow(Canvas canvas, Matrix matrix, RectF rectF, int n, float f, float f2) {
        int[] arrn;
        boolean bl = f2 < 0.0f;
        Path path = this.scratch;
        if (bl) {
            arrn = cornerColors;
            arrn[0] = 0;
            arrn[1] = this.shadowEndColor;
            arrn[2] = this.shadowMiddleColor;
            arrn[3] = this.shadowStartColor;
        } else {
            path.rewind();
            path.moveTo(rectF.centerX(), rectF.centerY());
            path.arcTo(rectF, f, f2);
            path.close();
            rectF.inset((float)(- n), (float)(- n));
            arrn = cornerColors;
            arrn[0] = 0;
            arrn[1] = this.shadowStartColor;
            arrn[2] = this.shadowMiddleColor;
            arrn[3] = this.shadowEndColor;
        }
        float f3 = 1.0f - (float)n / (rectF.width() / 2.0f);
        float f4 = (1.0f - f3) / 2.0f;
        arrn = cornerPositions;
        arrn[1] = (int)f3;
        arrn[2] = (int)(f3 + f4);
        this.cornerShadowPaint.setShader((Shader)new RadialGradient(rectF.centerX(), rectF.centerY(), rectF.width() / 2.0f, cornerColors, cornerPositions, Shader.TileMode.CLAMP));
        canvas.save();
        canvas.concat(matrix);
        if (!bl) {
            canvas.clipPath(path, Region.Op.DIFFERENCE);
        }
        canvas.drawArc(rectF, f, f2, true, this.cornerShadowPaint);
        canvas.restore();
    }

    public void drawEdgeShadow(Canvas canvas, Matrix matrix, RectF rectF, int n) {
        rectF.bottom += (float)n;
        rectF.offset(0.0f, (float)(- n));
        int[] arrn = edgeColors;
        arrn[0] = this.shadowEndColor;
        arrn[1] = this.shadowMiddleColor;
        arrn[2] = this.shadowStartColor;
        this.edgeShadowPaint.setShader((Shader)new LinearGradient(rectF.left, rectF.top, rectF.left, rectF.bottom, edgeColors, edgePositions, Shader.TileMode.CLAMP));
        canvas.save();
        canvas.concat(matrix);
        canvas.drawRect(rectF, this.edgeShadowPaint);
        canvas.restore();
    }

    public Paint getShadowPaint() {
        return this.shadowPaint;
    }

    public void setShadowColor(int n) {
        this.shadowStartColor = ColorUtils.setAlphaComponent(n, 68);
        this.shadowMiddleColor = ColorUtils.setAlphaComponent(n, 20);
        this.shadowEndColor = ColorUtils.setAlphaComponent(n, 0);
    }
}

