/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Color
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.widget.FrameLayout
 *  androidx.cardview.R
 *  androidx.cardview.R$attr
 *  androidx.cardview.R$color
 *  androidx.cardview.R$style
 *  androidx.cardview.R$styleable
 */
package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.cardview.R;
import androidx.cardview.widget.CardViewApi17Impl;
import androidx.cardview.widget.CardViewApi21Impl;
import androidx.cardview.widget.CardViewBaseImpl;
import androidx.cardview.widget.CardViewDelegate;
import androidx.cardview.widget.CardViewImpl;

public class CardView
extends FrameLayout {
    private static final int[] COLOR_BACKGROUND_ATTR = new int[]{16842801};
    private static final CardViewImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new CardViewApi21Impl() : (Build.VERSION.SDK_INT >= 17 ? new CardViewApi17Impl() : new CardViewBaseImpl());
    private final CardViewDelegate mCardViewDelegate;
    private boolean mCompatPadding;
    final Rect mContentPadding = new Rect();
    private boolean mPreventCornerOverlap;
    final Rect mShadowBounds = new Rect();
    int mUserSetMinHeight;
    int mUserSetMinWidth;

    static {
        IMPL.initStatic();
    }

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.cardViewStyle);
    }

    public CardView(Context context, AttributeSet colorStateList, int n) {
        super(context, (AttributeSet)colorStateList, n);
        this.mCardViewDelegate = new CardViewDelegate(){
            private Drawable mCardBackground;

            @Override
            public Drawable getCardBackground() {
                return this.mCardBackground;
            }

            @Override
            public View getCardView() {
                return CardView.this;
            }

            @Override
            public boolean getPreventCornerOverlap() {
                return CardView.this.getPreventCornerOverlap();
            }

            @Override
            public boolean getUseCompatPadding() {
                return CardView.this.getUseCompatPadding();
            }

            @Override
            public void setCardBackground(Drawable drawable2) {
                this.mCardBackground = drawable2;
                CardView.this.setBackgroundDrawable(drawable2);
            }

            @Override
            public void setMinWidthHeightInternal(int n, int n2) {
                if (n > CardView.this.mUserSetMinWidth) {
                    CardView.super.setMinimumWidth(n);
                }
                if (n2 > CardView.this.mUserSetMinHeight) {
                    CardView.super.setMinimumHeight(n2);
                }
            }

            @Override
            public void setShadowPadding(int n, int n2, int n3, int n4) {
                CardView.this.mShadowBounds.set(n, n2, n3, n4);
                CardView cardView = CardView.this;
                CardView.super.setPadding(cardView.mContentPadding.left + n, CardView.this.mContentPadding.top + n2, CardView.this.mContentPadding.right + n3, CardView.this.mContentPadding.bottom + n4);
            }
        };
        TypedArray typedArray = context.obtainStyledAttributes((AttributeSet)colorStateList, R.styleable.CardView, n, R.style.CardView);
        if (typedArray.hasValue(R.styleable.CardView_cardBackgroundColor)) {
            colorStateList = typedArray.getColorStateList(R.styleable.CardView_cardBackgroundColor);
        } else {
            colorStateList = this.getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            n = colorStateList.getColor(0, 0);
            colorStateList.recycle();
            colorStateList = new float[3];
            Color.colorToHSV((int)n, (float[])colorStateList);
            n = colorStateList[2] > 0.5f ? this.getResources().getColor(R.color.cardview_light_background) : this.getResources().getColor(R.color.cardview_dark_background);
            colorStateList = ColorStateList.valueOf((int)n);
        }
        float f = typedArray.getDimension(R.styleable.CardView_cardCornerRadius, 0.0f);
        float f2 = typedArray.getDimension(R.styleable.CardView_cardElevation, 0.0f);
        float f3 = typedArray.getDimension(R.styleable.CardView_cardMaxElevation, 0.0f);
        this.mCompatPadding = typedArray.getBoolean(R.styleable.CardView_cardUseCompatPadding, false);
        this.mPreventCornerOverlap = typedArray.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true);
        n = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0);
        this.mContentPadding.left = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingLeft, n);
        this.mContentPadding.top = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingTop, n);
        this.mContentPadding.right = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingRight, n);
        this.mContentPadding.bottom = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingBottom, n);
        if (f2 > f3) {
            f3 = f2;
        }
        this.mUserSetMinWidth = typedArray.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0);
        this.mUserSetMinHeight = typedArray.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0);
        typedArray.recycle();
        IMPL.initialize(this.mCardViewDelegate, context, colorStateList, f, f2, f3);
    }

    public ColorStateList getCardBackgroundColor() {
        return IMPL.getBackgroundColor(this.mCardViewDelegate);
    }

    public float getCardElevation() {
        return IMPL.getElevation(this.mCardViewDelegate);
    }

    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }

    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }

    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }

    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }

    public float getMaxCardElevation() {
        return IMPL.getMaxElevation(this.mCardViewDelegate);
    }

    public boolean getPreventCornerOverlap() {
        return this.mPreventCornerOverlap;
    }

    public float getRadius() {
        return IMPL.getRadius(this.mCardViewDelegate);
    }

    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }

    protected void onMeasure(int n, int n2) {
        if (!(IMPL instanceof CardViewApi21Impl)) {
            int n3 = View.MeasureSpec.getMode((int)n);
            if (n3 == Integer.MIN_VALUE || n3 == 1073741824) {
                n = View.MeasureSpec.makeMeasureSpec((int)Math.max((int)Math.ceil(IMPL.getMinWidth(this.mCardViewDelegate)), View.MeasureSpec.getSize((int)n)), (int)n3);
            }
            n3 = View.MeasureSpec.getMode((int)n2);
            if (n3 == Integer.MIN_VALUE || n3 == 1073741824) {
                n2 = View.MeasureSpec.makeMeasureSpec((int)Math.max((int)Math.ceil(IMPL.getMinHeight(this.mCardViewDelegate)), View.MeasureSpec.getSize((int)n2)), (int)n3);
            }
            super.onMeasure(n, n2);
            return;
        }
        super.onMeasure(n, n2);
    }

    public void setCardBackgroundColor(int n) {
        IMPL.setBackgroundColor(this.mCardViewDelegate, ColorStateList.valueOf((int)n));
    }

    public void setCardBackgroundColor(ColorStateList colorStateList) {
        IMPL.setBackgroundColor(this.mCardViewDelegate, colorStateList);
    }

    public void setCardElevation(float f) {
        IMPL.setElevation(this.mCardViewDelegate, f);
    }

    public void setContentPadding(int n, int n2, int n3, int n4) {
        this.mContentPadding.set(n, n2, n3, n4);
        IMPL.updatePadding(this.mCardViewDelegate);
    }

    public void setMaxCardElevation(float f) {
        IMPL.setMaxElevation(this.mCardViewDelegate, f);
    }

    public void setMinimumHeight(int n) {
        this.mUserSetMinHeight = n;
        super.setMinimumHeight(n);
    }

    public void setMinimumWidth(int n) {
        this.mUserSetMinWidth = n;
        super.setMinimumWidth(n);
    }

    public void setPadding(int n, int n2, int n3, int n4) {
    }

    public void setPaddingRelative(int n, int n2, int n3, int n4) {
    }

    public void setPreventCornerOverlap(boolean bl) {
        if (bl != this.mPreventCornerOverlap) {
            this.mPreventCornerOverlap = bl;
            IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
        }
    }

    public void setRadius(float f) {
        IMPL.setRadius(this.mCardViewDelegate, f);
    }

    public void setUseCompatPadding(boolean bl) {
        if (this.mCompatPadding != bl) {
            this.mCompatPadding = bl;
            IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
        }
    }

}

