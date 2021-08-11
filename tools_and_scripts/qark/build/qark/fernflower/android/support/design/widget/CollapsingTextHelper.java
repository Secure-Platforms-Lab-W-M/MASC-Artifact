package android.support.design.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.v4.math.MathUtils;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.animation.Interpolator;

final class CollapsingTextHelper {
   private static final boolean DEBUG_DRAW = false;
   private static final Paint DEBUG_DRAW_PAINT;
   private static final boolean USE_SCALING_TEXTURE;
   private boolean mBoundsChanged;
   private final Rect mCollapsedBounds;
   private float mCollapsedDrawX;
   private float mCollapsedDrawY;
   private int mCollapsedShadowColor;
   private float mCollapsedShadowDx;
   private float mCollapsedShadowDy;
   private float mCollapsedShadowRadius;
   private ColorStateList mCollapsedTextColor;
   private int mCollapsedTextGravity = 16;
   private float mCollapsedTextSize = 15.0F;
   private Typeface mCollapsedTypeface;
   private final RectF mCurrentBounds;
   private float mCurrentDrawX;
   private float mCurrentDrawY;
   private float mCurrentTextSize;
   private Typeface mCurrentTypeface;
   private boolean mDrawTitle;
   private final Rect mExpandedBounds;
   private float mExpandedDrawX;
   private float mExpandedDrawY;
   private float mExpandedFraction;
   private int mExpandedShadowColor;
   private float mExpandedShadowDx;
   private float mExpandedShadowDy;
   private float mExpandedShadowRadius;
   private ColorStateList mExpandedTextColor;
   private int mExpandedTextGravity = 16;
   private float mExpandedTextSize = 15.0F;
   private Bitmap mExpandedTitleTexture;
   private Typeface mExpandedTypeface;
   private boolean mIsRtl;
   private Interpolator mPositionInterpolator;
   private float mScale;
   private int[] mState;
   private CharSequence mText;
   private final TextPaint mTextPaint;
   private Interpolator mTextSizeInterpolator;
   private CharSequence mTextToDraw;
   private float mTextureAscent;
   private float mTextureDescent;
   private Paint mTexturePaint;
   private boolean mUseTexture;
   private final View mView;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 18) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_SCALING_TEXTURE = var0;
      DEBUG_DRAW_PAINT = null;
      Paint var1 = DEBUG_DRAW_PAINT;
      if (var1 != null) {
         var1.setAntiAlias(true);
         DEBUG_DRAW_PAINT.setColor(-65281);
      }
   }

   public CollapsingTextHelper(View var1) {
      this.mView = var1;
      this.mTextPaint = new TextPaint(129);
      this.mCollapsedBounds = new Rect();
      this.mExpandedBounds = new Rect();
      this.mCurrentBounds = new RectF();
   }

   private boolean areTypefacesDifferent(Typeface var1, Typeface var2) {
      return var1 != null && !var1.equals(var2) || var1 == null && var2 != null;
   }

   private static int blendColors(int var0, int var1, float var2) {
      float var3 = 1.0F - var2;
      float var4 = (float)Color.alpha(var0);
      float var5 = (float)Color.alpha(var1);
      float var6 = (float)Color.red(var0);
      float var7 = (float)Color.red(var1);
      float var8 = (float)Color.green(var0);
      float var9 = (float)Color.green(var1);
      float var10 = (float)Color.blue(var0);
      float var11 = (float)Color.blue(var1);
      return Color.argb((int)(var4 * var3 + var5 * var2), (int)(var6 * var3 + var7 * var2), (int)(var8 * var3 + var9 * var2), (int)(var10 * var3 + var11 * var2));
   }

   private void calculateBaseOffsets() {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s2stmt(TypeTransformer.java:820)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:843)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   private void calculateCurrentOffsets() {
      this.calculateOffsets(this.mExpandedFraction);
   }

   private boolean calculateIsRtl(CharSequence var1) {
      int var3 = ViewCompat.getLayoutDirection(this.mView);
      boolean var2 = true;
      if (var3 != 1) {
         var2 = false;
      }

      TextDirectionHeuristicCompat var4;
      if (var2) {
         var4 = TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL;
      } else {
         var4 = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
      }

      return var4.isRtl((CharSequence)var1, 0, var1.length());
   }

   private void calculateOffsets(float var1) {
      this.interpolateBounds(var1);
      this.mCurrentDrawX = lerp(this.mExpandedDrawX, this.mCollapsedDrawX, var1, this.mPositionInterpolator);
      this.mCurrentDrawY = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, var1, this.mPositionInterpolator);
      this.setInterpolatedTextSize(lerp(this.mExpandedTextSize, this.mCollapsedTextSize, var1, this.mTextSizeInterpolator));
      if (this.mCollapsedTextColor != this.mExpandedTextColor) {
         this.mTextPaint.setColor(blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), var1));
      } else {
         this.mTextPaint.setColor(this.getCurrentCollapsedTextColor());
      }

      this.mTextPaint.setShadowLayer(lerp(this.mExpandedShadowRadius, this.mCollapsedShadowRadius, var1, (Interpolator)null), lerp(this.mExpandedShadowDx, this.mCollapsedShadowDx, var1, (Interpolator)null), lerp(this.mExpandedShadowDy, this.mCollapsedShadowDy, var1, (Interpolator)null), blendColors(this.mExpandedShadowColor, this.mCollapsedShadowColor, var1));
      ViewCompat.postInvalidateOnAnimation(this.mView);
   }

   private void calculateUsingTextSize(float var1) {
      if (this.mText != null) {
         float var3 = (float)this.mCollapsedBounds.width();
         float var4 = (float)this.mExpandedBounds.width();
         boolean var6 = false;
         boolean var5 = false;
         float var2;
         if (isClose(var1, this.mCollapsedTextSize)) {
            var2 = this.mCollapsedTextSize;
            this.mScale = 1.0F;
            if (this.areTypefacesDifferent(this.mCurrentTypeface, this.mCollapsedTypeface)) {
               this.mCurrentTypeface = this.mCollapsedTypeface;
               var5 = true;
            }

            var1 = var3;
         } else {
            var2 = this.mExpandedTextSize;
            if (this.areTypefacesDifferent(this.mCurrentTypeface, this.mExpandedTypeface)) {
               this.mCurrentTypeface = this.mExpandedTypeface;
               var5 = true;
            } else {
               var5 = var6;
            }

            if (isClose(var1, this.mExpandedTextSize)) {
               this.mScale = 1.0F;
            } else {
               this.mScale = var1 / this.mExpandedTextSize;
            }

            var1 = this.mCollapsedTextSize / this.mExpandedTextSize;
            if (var4 * var1 > var3) {
               var1 = Math.min(var3 / var1, var4);
            } else {
               var1 = var4;
            }
         }

         boolean var7 = true;
         if (var1 > 0.0F) {
            if (this.mCurrentTextSize == var2 && !this.mBoundsChanged && !var5) {
               var5 = false;
            } else {
               var5 = true;
            }

            this.mCurrentTextSize = var2;
            this.mBoundsChanged = false;
         }

         if (this.mTextToDraw == null || var5) {
            this.mTextPaint.setTextSize(this.mCurrentTextSize);
            this.mTextPaint.setTypeface(this.mCurrentTypeface);
            TextPaint var8 = this.mTextPaint;
            if (this.mScale == 1.0F) {
               var7 = false;
            }

            var8.setLinearText(var7);
            CharSequence var9 = TextUtils.ellipsize(this.mText, this.mTextPaint, var1, TruncateAt.END);
            if (!TextUtils.equals(var9, this.mTextToDraw)) {
               this.mTextToDraw = var9;
               this.mIsRtl = this.calculateIsRtl(this.mTextToDraw);
            }
         }
      }
   }

   private void clearTexture() {
      Bitmap var1 = this.mExpandedTitleTexture;
      if (var1 != null) {
         var1.recycle();
         this.mExpandedTitleTexture = null;
      }
   }

   private void ensureExpandedTexture() {
      if (this.mExpandedTitleTexture == null && !this.mExpandedBounds.isEmpty()) {
         if (!TextUtils.isEmpty(this.mTextToDraw)) {
            this.calculateOffsets(0.0F);
            this.mTextureAscent = this.mTextPaint.ascent();
            this.mTextureDescent = this.mTextPaint.descent();
            TextPaint var3 = this.mTextPaint;
            CharSequence var4 = this.mTextToDraw;
            int var1 = Math.round(var3.measureText(var4, 0, var4.length()));
            int var2 = Math.round(this.mTextureDescent - this.mTextureAscent);
            if (var1 > 0) {
               if (var2 > 0) {
                  this.mExpandedTitleTexture = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
                  Canvas var5 = new Canvas(this.mExpandedTitleTexture);
                  var4 = this.mTextToDraw;
                  var5.drawText(var4, 0, var4.length(), 0.0F, (float)var2 - this.mTextPaint.descent(), this.mTextPaint);
                  if (this.mTexturePaint == null) {
                     this.mTexturePaint = new Paint(3);
                  }
               }
            }
         }
      }
   }

   @ColorInt
   private int getCurrentCollapsedTextColor() {
      int[] var1 = this.mState;
      return var1 != null ? this.mCollapsedTextColor.getColorForState(var1, 0) : this.mCollapsedTextColor.getDefaultColor();
   }

   @ColorInt
   private int getCurrentExpandedTextColor() {
      int[] var1 = this.mState;
      return var1 != null ? this.mExpandedTextColor.getColorForState(var1, 0) : this.mExpandedTextColor.getDefaultColor();
   }

   private void interpolateBounds(float var1) {
      this.mCurrentBounds.left = lerp((float)this.mExpandedBounds.left, (float)this.mCollapsedBounds.left, var1, this.mPositionInterpolator);
      this.mCurrentBounds.top = lerp(this.mExpandedDrawY, this.mCollapsedDrawY, var1, this.mPositionInterpolator);
      this.mCurrentBounds.right = lerp((float)this.mExpandedBounds.right, (float)this.mCollapsedBounds.right, var1, this.mPositionInterpolator);
      this.mCurrentBounds.bottom = lerp((float)this.mExpandedBounds.bottom, (float)this.mCollapsedBounds.bottom, var1, this.mPositionInterpolator);
   }

   private static boolean isClose(float var0, float var1) {
      return Math.abs(var0 - var1) < 0.001F;
   }

   private static float lerp(float var0, float var1, float var2, Interpolator var3) {
      if (var3 != null) {
         var2 = var3.getInterpolation(var2);
      }

      return AnimationUtils.lerp(var0, var1, var2);
   }

   private Typeface readFontFamilyTypeface(int var1) {
      TypedArray var2 = this.mView.getContext().obtainStyledAttributes(var1, new int[]{16843692});

      Throwable var10000;
      label78: {
         boolean var10001;
         String var3;
         try {
            var3 = var2.getString(0);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label78;
         }

         if (var3 == null) {
            var2.recycle();
            return null;
         }

         Typeface var11;
         try {
            var11 = Typeface.create(var3, 0);
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         var2.recycle();
         return var11;
      }

      Throwable var10 = var10000;
      var2.recycle();
      throw var10;
   }

   private static boolean rectEquals(Rect var0, int var1, int var2, int var3, int var4) {
      return var0.left == var1 && var0.top == var2 && var0.right == var3 && var0.bottom == var4;
   }

   private void setInterpolatedTextSize(float var1) {
      this.calculateUsingTextSize(var1);
      boolean var2;
      if (USE_SCALING_TEXTURE && this.mScale != 1.0F) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mUseTexture = var2;
      if (this.mUseTexture) {
         this.ensureExpandedTexture();
      }

      ViewCompat.postInvalidateOnAnimation(this.mView);
   }

   public void draw(Canvas var1) {
      int var8 = var1.save();
      if (this.mTextToDraw != null && this.mDrawTitle) {
         float var4 = this.mCurrentDrawX;
         float var3 = this.mCurrentDrawY;
         boolean var7;
         if (this.mUseTexture && this.mExpandedTitleTexture != null) {
            var7 = true;
         } else {
            var7 = false;
         }

         float var2;
         float var5;
         float var6;
         if (var7) {
            var2 = this.mTextureAscent;
            var5 = this.mScale;
            var6 = this.mTextureDescent;
            var2 *= var5;
         } else {
            var2 = this.mTextPaint.ascent();
            var5 = this.mScale;
            this.mTextPaint.descent();
            var6 = this.mScale;
            var2 *= var5;
         }

         if (var7) {
            var2 += var3;
         } else {
            var2 = var3;
         }

         var3 = this.mScale;
         if (var3 != 1.0F) {
            var1.scale(var3, var3, var4, var2);
         }

         if (var7) {
            var1.drawBitmap(this.mExpandedTitleTexture, var4, var2, this.mTexturePaint);
         } else {
            CharSequence var9 = this.mTextToDraw;
            var1.drawText(var9, 0, var9.length(), var4, var2, this.mTextPaint);
         }
      }

      var1.restoreToCount(var8);
   }

   ColorStateList getCollapsedTextColor() {
      return this.mCollapsedTextColor;
   }

   int getCollapsedTextGravity() {
      return this.mCollapsedTextGravity;
   }

   float getCollapsedTextSize() {
      return this.mCollapsedTextSize;
   }

   Typeface getCollapsedTypeface() {
      Typeface var1 = this.mCollapsedTypeface;
      return var1 != null ? var1 : Typeface.DEFAULT;
   }

   ColorStateList getExpandedTextColor() {
      return this.mExpandedTextColor;
   }

   int getExpandedTextGravity() {
      return this.mExpandedTextGravity;
   }

   float getExpandedTextSize() {
      return this.mExpandedTextSize;
   }

   Typeface getExpandedTypeface() {
      Typeface var1 = this.mExpandedTypeface;
      return var1 != null ? var1 : Typeface.DEFAULT;
   }

   float getExpansionFraction() {
      return this.mExpandedFraction;
   }

   CharSequence getText() {
      return this.mText;
   }

   final boolean isStateful() {
      ColorStateList var1 = this.mCollapsedTextColor;
      if (var1 == null || !var1.isStateful()) {
         var1 = this.mExpandedTextColor;
         if (var1 == null || !var1.isStateful()) {
            return false;
         }
      }

      return true;
   }

   void onBoundsChanged() {
      boolean var1;
      if (this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.mDrawTitle = var1;
   }

   public void recalculate() {
      if (this.mView.getHeight() > 0 && this.mView.getWidth() > 0) {
         this.calculateBaseOffsets();
         this.calculateCurrentOffsets();
      }
   }

   void setCollapsedBounds(int var1, int var2, int var3, int var4) {
      if (!rectEquals(this.mCollapsedBounds, var1, var2, var3, var4)) {
         this.mCollapsedBounds.set(var1, var2, var3, var4);
         this.mBoundsChanged = true;
         this.onBoundsChanged();
      }
   }

   void setCollapsedTextAppearance(int var1) {
      TintTypedArray var2 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, R$styleable.TextAppearance);
      if (var2.hasValue(R$styleable.TextAppearance_android_textColor)) {
         this.mCollapsedTextColor = var2.getColorStateList(R$styleable.TextAppearance_android_textColor);
      }

      if (var2.hasValue(R$styleable.TextAppearance_android_textSize)) {
         this.mCollapsedTextSize = (float)var2.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, (int)this.mCollapsedTextSize);
      }

      this.mCollapsedShadowColor = var2.getInt(R$styleable.TextAppearance_android_shadowColor, 0);
      this.mCollapsedShadowDx = var2.getFloat(R$styleable.TextAppearance_android_shadowDx, 0.0F);
      this.mCollapsedShadowDy = var2.getFloat(R$styleable.TextAppearance_android_shadowDy, 0.0F);
      this.mCollapsedShadowRadius = var2.getFloat(R$styleable.TextAppearance_android_shadowRadius, 0.0F);
      var2.recycle();
      if (VERSION.SDK_INT >= 16) {
         this.mCollapsedTypeface = this.readFontFamilyTypeface(var1);
      }

      this.recalculate();
   }

   void setCollapsedTextColor(ColorStateList var1) {
      if (this.mCollapsedTextColor != var1) {
         this.mCollapsedTextColor = var1;
         this.recalculate();
      }
   }

   void setCollapsedTextGravity(int var1) {
      if (this.mCollapsedTextGravity != var1) {
         this.mCollapsedTextGravity = var1;
         this.recalculate();
      }
   }

   void setCollapsedTextSize(float var1) {
      if (this.mCollapsedTextSize != var1) {
         this.mCollapsedTextSize = var1;
         this.recalculate();
      }
   }

   void setCollapsedTypeface(Typeface var1) {
      if (this.areTypefacesDifferent(this.mCollapsedTypeface, var1)) {
         this.mCollapsedTypeface = var1;
         this.recalculate();
      }
   }

   void setExpandedBounds(int var1, int var2, int var3, int var4) {
      if (!rectEquals(this.mExpandedBounds, var1, var2, var3, var4)) {
         this.mExpandedBounds.set(var1, var2, var3, var4);
         this.mBoundsChanged = true;
         this.onBoundsChanged();
      }
   }

   void setExpandedTextAppearance(int var1) {
      TintTypedArray var2 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, R$styleable.TextAppearance);
      if (var2.hasValue(R$styleable.TextAppearance_android_textColor)) {
         this.mExpandedTextColor = var2.getColorStateList(R$styleable.TextAppearance_android_textColor);
      }

      if (var2.hasValue(R$styleable.TextAppearance_android_textSize)) {
         this.mExpandedTextSize = (float)var2.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, (int)this.mExpandedTextSize);
      }

      this.mExpandedShadowColor = var2.getInt(R$styleable.TextAppearance_android_shadowColor, 0);
      this.mExpandedShadowDx = var2.getFloat(R$styleable.TextAppearance_android_shadowDx, 0.0F);
      this.mExpandedShadowDy = var2.getFloat(R$styleable.TextAppearance_android_shadowDy, 0.0F);
      this.mExpandedShadowRadius = var2.getFloat(R$styleable.TextAppearance_android_shadowRadius, 0.0F);
      var2.recycle();
      if (VERSION.SDK_INT >= 16) {
         this.mExpandedTypeface = this.readFontFamilyTypeface(var1);
      }

      this.recalculate();
   }

   void setExpandedTextColor(ColorStateList var1) {
      if (this.mExpandedTextColor != var1) {
         this.mExpandedTextColor = var1;
         this.recalculate();
      }
   }

   void setExpandedTextGravity(int var1) {
      if (this.mExpandedTextGravity != var1) {
         this.mExpandedTextGravity = var1;
         this.recalculate();
      }
   }

   void setExpandedTextSize(float var1) {
      if (this.mExpandedTextSize != var1) {
         this.mExpandedTextSize = var1;
         this.recalculate();
      }
   }

   void setExpandedTypeface(Typeface var1) {
      if (this.areTypefacesDifferent(this.mExpandedTypeface, var1)) {
         this.mExpandedTypeface = var1;
         this.recalculate();
      }
   }

   void setExpansionFraction(float var1) {
      var1 = MathUtils.clamp(var1, 0.0F, 1.0F);
      if (var1 != this.mExpandedFraction) {
         this.mExpandedFraction = var1;
         this.calculateCurrentOffsets();
      }
   }

   void setPositionInterpolator(Interpolator var1) {
      this.mPositionInterpolator = var1;
      this.recalculate();
   }

   final boolean setState(int[] var1) {
      this.mState = var1;
      if (this.isStateful()) {
         this.recalculate();
         return true;
      } else {
         return false;
      }
   }

   void setText(CharSequence var1) {
      if (var1 == null || !var1.equals(this.mText)) {
         this.mText = var1;
         this.mTextToDraw = null;
         this.clearTexture();
         this.recalculate();
      }
   }

   void setTextSizeInterpolator(Interpolator var1) {
      this.mTextSizeInterpolator = var1;
      this.recalculate();
   }

   void setTypefaces(Typeface var1) {
      this.mExpandedTypeface = var1;
      this.mCollapsedTypeface = var1;
      this.recalculate();
   }
}
