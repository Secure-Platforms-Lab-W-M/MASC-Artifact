package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import androidx.cardview.R.attr;
import androidx.cardview.R.color;
import androidx.cardview.R.style;
import androidx.cardview.R.styleable;

public class CardView extends FrameLayout {
   private static final int[] COLOR_BACKGROUND_ATTR = new int[]{16842801};
   private static final CardViewImpl IMPL;
   private final CardViewDelegate mCardViewDelegate;
   private boolean mCompatPadding;
   final Rect mContentPadding;
   private boolean mPreventCornerOverlap;
   final Rect mShadowBounds;
   int mUserSetMinHeight;
   int mUserSetMinWidth;

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new CardViewApi21Impl();
      } else if (VERSION.SDK_INT >= 17) {
         IMPL = new CardViewApi17Impl();
      } else {
         IMPL = new CardViewBaseImpl();
      }

      IMPL.initStatic();
   }

   public CardView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public CardView(Context var1, AttributeSet var2) {
      this(var1, var2, attr.cardViewStyle);
   }

   public CardView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mContentPadding = new Rect();
      this.mShadowBounds = new Rect();
      this.mCardViewDelegate = new CardViewDelegate() {
         private Drawable mCardBackground;

         public Drawable getCardBackground() {
            return this.mCardBackground;
         }

         public View getCardView() {
            return CardView.this;
         }

         public boolean getPreventCornerOverlap() {
            return CardView.this.getPreventCornerOverlap();
         }

         public boolean getUseCompatPadding() {
            return CardView.this.getUseCompatPadding();
         }

         public void setCardBackground(Drawable var1) {
            this.mCardBackground = var1;
            CardView.this.setBackgroundDrawable(var1);
         }

         public void setMinWidthHeightInternal(int var1, int var2) {
            if (var1 > CardView.this.mUserSetMinWidth) {
               CardView.super.setMinimumWidth(var1);
            }

            if (var2 > CardView.this.mUserSetMinHeight) {
               CardView.super.setMinimumHeight(var2);
            }

         }

         public void setShadowPadding(int var1, int var2, int var3, int var4) {
            CardView.this.mShadowBounds.set(var1, var2, var3, var4);
            CardView var5 = CardView.this;
            var5.setPadding(var5.mContentPadding.left + var1, CardView.this.mContentPadding.top + var2, CardView.this.mContentPadding.right + var3, CardView.this.mContentPadding.bottom + var4);
         }
      };
      TypedArray var7 = var1.obtainStyledAttributes(var2, styleable.CardView, var3, style.CardView);
      ColorStateList var8;
      if (var7.hasValue(styleable.CardView_cardBackgroundColor)) {
         var8 = var7.getColorStateList(styleable.CardView_cardBackgroundColor);
      } else {
         TypedArray var9 = this.getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
         var3 = var9.getColor(0, 0);
         var9.recycle();
         float[] var10 = new float[3];
         Color.colorToHSV(var3, var10);
         if (var10[2] > 0.5F) {
            var3 = this.getResources().getColor(color.cardview_light_background);
         } else {
            var3 = this.getResources().getColor(color.cardview_dark_background);
         }

         var8 = ColorStateList.valueOf(var3);
      }

      float var6 = var7.getDimension(styleable.CardView_cardCornerRadius, 0.0F);
      float var5 = var7.getDimension(styleable.CardView_cardElevation, 0.0F);
      float var4 = var7.getDimension(styleable.CardView_cardMaxElevation, 0.0F);
      this.mCompatPadding = var7.getBoolean(styleable.CardView_cardUseCompatPadding, false);
      this.mPreventCornerOverlap = var7.getBoolean(styleable.CardView_cardPreventCornerOverlap, true);
      var3 = var7.getDimensionPixelSize(styleable.CardView_contentPadding, 0);
      this.mContentPadding.left = var7.getDimensionPixelSize(styleable.CardView_contentPaddingLeft, var3);
      this.mContentPadding.top = var7.getDimensionPixelSize(styleable.CardView_contentPaddingTop, var3);
      this.mContentPadding.right = var7.getDimensionPixelSize(styleable.CardView_contentPaddingRight, var3);
      this.mContentPadding.bottom = var7.getDimensionPixelSize(styleable.CardView_contentPaddingBottom, var3);
      if (var5 > var4) {
         var4 = var5;
      }

      this.mUserSetMinWidth = var7.getDimensionPixelSize(styleable.CardView_android_minWidth, 0);
      this.mUserSetMinHeight = var7.getDimensionPixelSize(styleable.CardView_android_minHeight, 0);
      var7.recycle();
      IMPL.initialize(this.mCardViewDelegate, var1, var8, var6, var5, var4);
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

   protected void onMeasure(int var1, int var2) {
      if (IMPL instanceof CardViewApi21Impl) {
         super.onMeasure(var1, var2);
      } else {
         int var3 = MeasureSpec.getMode(var1);
         if (var3 == Integer.MIN_VALUE || var3 == 1073741824) {
            var1 = MeasureSpec.makeMeasureSpec(Math.max((int)Math.ceil((double)IMPL.getMinWidth(this.mCardViewDelegate)), MeasureSpec.getSize(var1)), var3);
         }

         var3 = MeasureSpec.getMode(var2);
         if (var3 == Integer.MIN_VALUE || var3 == 1073741824) {
            var2 = MeasureSpec.makeMeasureSpec(Math.max((int)Math.ceil((double)IMPL.getMinHeight(this.mCardViewDelegate)), MeasureSpec.getSize(var2)), var3);
         }

         super.onMeasure(var1, var2);
      }
   }

   public void setCardBackgroundColor(int var1) {
      IMPL.setBackgroundColor(this.mCardViewDelegate, ColorStateList.valueOf(var1));
   }

   public void setCardBackgroundColor(ColorStateList var1) {
      IMPL.setBackgroundColor(this.mCardViewDelegate, var1);
   }

   public void setCardElevation(float var1) {
      IMPL.setElevation(this.mCardViewDelegate, var1);
   }

   public void setContentPadding(int var1, int var2, int var3, int var4) {
      this.mContentPadding.set(var1, var2, var3, var4);
      IMPL.updatePadding(this.mCardViewDelegate);
   }

   public void setMaxCardElevation(float var1) {
      IMPL.setMaxElevation(this.mCardViewDelegate, var1);
   }

   public void setMinimumHeight(int var1) {
      this.mUserSetMinHeight = var1;
      super.setMinimumHeight(var1);
   }

   public void setMinimumWidth(int var1) {
      this.mUserSetMinWidth = var1;
      super.setMinimumWidth(var1);
   }

   public void setPadding(int var1, int var2, int var3, int var4) {
   }

   public void setPaddingRelative(int var1, int var2, int var3, int var4) {
   }

   public void setPreventCornerOverlap(boolean var1) {
      if (var1 != this.mPreventCornerOverlap) {
         this.mPreventCornerOverlap = var1;
         IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
      }

   }

   public void setRadius(float var1) {
      IMPL.setRadius(this.mCardViewDelegate, var1);
   }

   public void setUseCompatPadding(boolean var1) {
      if (this.mCompatPadding != var1) {
         this.mCompatPadding = var1;
         IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
      }

   }
}
