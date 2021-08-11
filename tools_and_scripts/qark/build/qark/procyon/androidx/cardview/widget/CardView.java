// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.cardview.widget;

import android.view.View$MeasureSpec;
import android.content.res.TypedArray;
import android.content.res.ColorStateList;
import androidx.cardview.R$color;
import android.graphics.Color;
import androidx.cardview.R$style;
import androidx.cardview.R$styleable;
import android.view.View;
import android.graphics.drawable.Drawable;
import androidx.cardview.R$attr;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.graphics.Rect;
import android.widget.FrameLayout;

public class CardView extends FrameLayout
{
    private static final int[] COLOR_BACKGROUND_ATTR;
    private static final CardViewImpl IMPL;
    private final CardViewDelegate mCardViewDelegate;
    private boolean mCompatPadding;
    final Rect mContentPadding;
    private boolean mPreventCornerOverlap;
    final Rect mShadowBounds;
    int mUserSetMinHeight;
    int mUserSetMinWidth;
    
    static {
        COLOR_BACKGROUND_ATTR = new int[] { 16842801 };
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = new CardViewApi21Impl();
        }
        else if (Build$VERSION.SDK_INT >= 17) {
            IMPL = new CardViewApi17Impl();
        }
        else {
            IMPL = new CardViewBaseImpl();
        }
        CardView.IMPL.initStatic();
    }
    
    public CardView(final Context context) {
        this(context, null);
    }
    
    public CardView(final Context context, final AttributeSet set) {
        this(context, set, R$attr.cardViewStyle);
    }
    
    public CardView(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mContentPadding = new Rect();
        this.mShadowBounds = new Rect();
        this.mCardViewDelegate = new CardViewDelegate() {
            private Drawable mCardBackground;
            
            @Override
            public Drawable getCardBackground() {
                return this.mCardBackground;
            }
            
            @Override
            public View getCardView() {
                return (View)CardView.this;
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
            public void setCardBackground(final Drawable drawable) {
                this.mCardBackground = drawable;
                CardView.this.setBackgroundDrawable(drawable);
            }
            
            @Override
            public void setMinWidthHeightInternal(final int n, final int n2) {
                if (n > CardView.this.mUserSetMinWidth) {
                    CardView.access$101(CardView.this, n);
                }
                if (n2 > CardView.this.mUserSetMinHeight) {
                    CardView.access$201(CardView.this, n2);
                }
            }
            
            @Override
            public void setShadowPadding(final int n, final int n2, final int n3, final int n4) {
                CardView.this.mShadowBounds.set(n, n2, n3, n4);
                final CardView this$0 = CardView.this;
                CardView.access$001(this$0, this$0.mContentPadding.left + n, CardView.this.mContentPadding.top + n2, CardView.this.mContentPadding.right + n3, CardView.this.mContentPadding.bottom + n4);
            }
        };
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.CardView, n, R$style.CardView);
        ColorStateList list;
        if (obtainStyledAttributes.hasValue(R$styleable.CardView_cardBackgroundColor)) {
            list = obtainStyledAttributes.getColorStateList(R$styleable.CardView_cardBackgroundColor);
        }
        else {
            final TypedArray obtainStyledAttributes2 = this.getContext().obtainStyledAttributes(CardView.COLOR_BACKGROUND_ATTR);
            n = obtainStyledAttributes2.getColor(0, 0);
            obtainStyledAttributes2.recycle();
            final float[] array = new float[3];
            Color.colorToHSV(n, array);
            if (array[2] > 0.5f) {
                n = this.getResources().getColor(R$color.cardview_light_background);
            }
            else {
                n = this.getResources().getColor(R$color.cardview_dark_background);
            }
            list = ColorStateList.valueOf(n);
        }
        final float dimension = obtainStyledAttributes.getDimension(R$styleable.CardView_cardCornerRadius, 0.0f);
        final float dimension2 = obtainStyledAttributes.getDimension(R$styleable.CardView_cardElevation, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(R$styleable.CardView_cardMaxElevation, 0.0f);
        this.mCompatPadding = obtainStyledAttributes.getBoolean(R$styleable.CardView_cardUseCompatPadding, false);
        this.mPreventCornerOverlap = obtainStyledAttributes.getBoolean(R$styleable.CardView_cardPreventCornerOverlap, true);
        n = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPadding, 0);
        this.mContentPadding.left = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingLeft, n);
        this.mContentPadding.top = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingTop, n);
        this.mContentPadding.right = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingRight, n);
        this.mContentPadding.bottom = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_contentPaddingBottom, n);
        if (dimension2 > dimension3) {
            dimension3 = dimension2;
        }
        this.mUserSetMinWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_android_minWidth, 0);
        this.mUserSetMinHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CardView_android_minHeight, 0);
        obtainStyledAttributes.recycle();
        CardView.IMPL.initialize(this.mCardViewDelegate, context, list, dimension, dimension2, dimension3);
    }
    
    static /* synthetic */ void access$001(final CardView cardView, final int n, final int n2, final int n3, final int n4) {
        cardView.setPadding(n, n2, n3, n4);
    }
    
    static /* synthetic */ void access$101(final CardView cardView, final int minimumWidth) {
        cardView.setMinimumWidth(minimumWidth);
    }
    
    static /* synthetic */ void access$201(final CardView cardView, final int minimumHeight) {
        cardView.setMinimumHeight(minimumHeight);
    }
    
    public ColorStateList getCardBackgroundColor() {
        return CardView.IMPL.getBackgroundColor(this.mCardViewDelegate);
    }
    
    public float getCardElevation() {
        return CardView.IMPL.getElevation(this.mCardViewDelegate);
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
        return CardView.IMPL.getMaxElevation(this.mCardViewDelegate);
    }
    
    public boolean getPreventCornerOverlap() {
        return this.mPreventCornerOverlap;
    }
    
    public float getRadius() {
        return CardView.IMPL.getRadius(this.mCardViewDelegate);
    }
    
    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }
    
    protected void onMeasure(int measureSpec, int measureSpec2) {
        if (!(CardView.IMPL instanceof CardViewApi21Impl)) {
            final int mode = View$MeasureSpec.getMode(measureSpec);
            if (mode == Integer.MIN_VALUE || mode == 1073741824) {
                measureSpec = View$MeasureSpec.makeMeasureSpec(Math.max((int)Math.ceil(CardView.IMPL.getMinWidth(this.mCardViewDelegate)), View$MeasureSpec.getSize(measureSpec)), mode);
            }
            final int mode2 = View$MeasureSpec.getMode(measureSpec2);
            if (mode2 == Integer.MIN_VALUE || mode2 == 1073741824) {
                measureSpec2 = View$MeasureSpec.makeMeasureSpec(Math.max((int)Math.ceil(CardView.IMPL.getMinHeight(this.mCardViewDelegate)), View$MeasureSpec.getSize(measureSpec2)), mode2);
            }
            super.onMeasure(measureSpec, measureSpec2);
            return;
        }
        super.onMeasure(measureSpec, measureSpec2);
    }
    
    public void setCardBackgroundColor(final int n) {
        CardView.IMPL.setBackgroundColor(this.mCardViewDelegate, ColorStateList.valueOf(n));
    }
    
    public void setCardBackgroundColor(final ColorStateList list) {
        CardView.IMPL.setBackgroundColor(this.mCardViewDelegate, list);
    }
    
    public void setCardElevation(final float n) {
        CardView.IMPL.setElevation(this.mCardViewDelegate, n);
    }
    
    public void setContentPadding(final int n, final int n2, final int n3, final int n4) {
        this.mContentPadding.set(n, n2, n3, n4);
        CardView.IMPL.updatePadding(this.mCardViewDelegate);
    }
    
    public void setMaxCardElevation(final float n) {
        CardView.IMPL.setMaxElevation(this.mCardViewDelegate, n);
    }
    
    public void setMinimumHeight(final int mUserSetMinHeight) {
        super.setMinimumHeight(this.mUserSetMinHeight = mUserSetMinHeight);
    }
    
    public void setMinimumWidth(final int mUserSetMinWidth) {
        super.setMinimumWidth(this.mUserSetMinWidth = mUserSetMinWidth);
    }
    
    public void setPadding(final int n, final int n2, final int n3, final int n4) {
    }
    
    public void setPaddingRelative(final int n, final int n2, final int n3, final int n4) {
    }
    
    public void setPreventCornerOverlap(final boolean mPreventCornerOverlap) {
        if (mPreventCornerOverlap != this.mPreventCornerOverlap) {
            this.mPreventCornerOverlap = mPreventCornerOverlap;
            CardView.IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
        }
    }
    
    public void setRadius(final float n) {
        CardView.IMPL.setRadius(this.mCardViewDelegate, n);
    }
    
    public void setUseCompatPadding(final boolean mCompatPadding) {
        if (this.mCompatPadding != mCompatPadding) {
            this.mCompatPadding = mCompatPadding;
            CardView.IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
        }
    }
}
