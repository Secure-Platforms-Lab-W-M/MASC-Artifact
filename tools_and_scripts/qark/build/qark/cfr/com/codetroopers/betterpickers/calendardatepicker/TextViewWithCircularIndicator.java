/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Align
 *  android.graphics.Paint$Style
 *  android.util.AttributeSet
 *  android.widget.TextView
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$color
 *  com.codetroopers.betterpickers.R$dimen
 *  com.codetroopers.betterpickers.R$string
 */
package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
import com.codetroopers.betterpickers.R;

public class TextViewWithCircularIndicator
extends TextView {
    private static final int SELECTED_CIRCLE_ALPHA = 60;
    private int mCircleColor;
    Paint mCirclePaint = new Paint();
    private boolean mDrawCircle;
    private final String mItemIsSelectedText;
    private final int mRadius;

    public TextViewWithCircularIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        attributeSet = context.getResources();
        this.mCircleColor = attributeSet.getColor(R.color.bpBlue);
        this.mRadius = attributeSet.getDimensionPixelOffset(R.dimen.month_select_circle_radius);
        this.mItemIsSelectedText = context.getResources().getString(R.string.item_is_selected);
        this.init();
    }

    private void init() {
        this.mCirclePaint.setFakeBoldText(true);
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setColor(this.mCircleColor);
        this.mCirclePaint.setTextAlign(Paint.Align.CENTER);
        this.mCirclePaint.setStyle(Paint.Style.FILL);
        this.mCirclePaint.setAlpha(60);
    }

    public void drawIndicator(boolean bl) {
        this.mDrawCircle = bl;
    }

    public CharSequence getContentDescription() {
        CharSequence charSequence = this.getText();
        if (this.mDrawCircle) {
            return String.format(this.mItemIsSelectedText, charSequence);
        }
        return charSequence;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawCircle) {
            int n = this.getWidth();
            int n2 = this.getHeight();
            int n3 = Math.min(n, n2) / 2;
            canvas.drawCircle((float)(n / 2), (float)(n2 / 2), (float)n3, this.mCirclePaint);
        }
    }

    public void setCircleColor(int n) {
        this.mCircleColor = n;
        this.init();
    }
}

