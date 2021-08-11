package com.google.android.material.internal;

import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import androidx.core.math.MathUtils;
import androidx.core.text.TextDirectionHeuristicCompat;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.resources.CancelableFontCallback;
import com.google.android.material.resources.TextAppearance;

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
   private float collapsedTextSize = 15.0F;
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
   private float expandedTextSize = 15.0F;
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
      boolean var0;
      if (VERSION.SDK_INT < 18) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_SCALING_TEXTURE = var0;
      DEBUG_DRAW_PAINT = null;
      if (false) {
         throw new NullPointerException();
      }
   }

   public CollapsingTextHelper(View var1) {
      this.view = var1;
      this.textPaint = new TextPaint(129);
      this.tmpPaint = new TextPaint(this.textPaint);
      this.collapsedBounds = new Rect();
      this.expandedBounds = new Rect();
      this.currentBounds = new RectF();
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
      this.calculateOffsets(this.expandedFraction);
   }

   private boolean calculateIsRtl(CharSequence var1) {
      int var3 = ViewCompat.getLayoutDirection(this.view);
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
      this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, var1, this.positionInterpolator);
      this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, var1, this.positionInterpolator);
      this.setInterpolatedTextSize(lerp(this.expandedTextSize, this.collapsedTextSize, var1, this.textSizeInterpolator));
      if (this.collapsedTextColor != this.expandedTextColor) {
         this.textPaint.setColor(blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), var1));
      } else {
         this.textPaint.setColor(this.getCurrentCollapsedTextColor());
      }

      this.textPaint.setShadowLayer(lerp(this.expandedShadowRadius, this.collapsedShadowRadius, var1, (TimeInterpolator)null), lerp(this.expandedShadowDx, this.collapsedShadowDx, var1, (TimeInterpolator)null), lerp(this.expandedShadowDy, this.collapsedShadowDy, var1, (TimeInterpolator)null), blendColors(this.getCurrentColor(this.expandedShadowColor), this.getCurrentColor(this.collapsedShadowColor), var1));
      ViewCompat.postInvalidateOnAnimation(this.view);
   }

   private void calculateUsingTextSize(float var1) {
      if (this.text != null) {
         float var3 = (float)this.collapsedBounds.width();
         float var4 = (float)this.expandedBounds.width();
         boolean var6 = false;
         boolean var5 = false;
         float var2;
         Typeface var8;
         Typeface var9;
         if (isClose(var1, this.collapsedTextSize)) {
            var2 = this.collapsedTextSize;
            this.scale = 1.0F;
            var8 = this.currentTypeface;
            var9 = this.collapsedTypeface;
            if (var8 != var9) {
               this.currentTypeface = var9;
               var5 = true;
            }

            var1 = var3;
         } else {
            var2 = this.expandedTextSize;
            var8 = this.currentTypeface;
            var9 = this.expandedTypeface;
            var5 = var6;
            if (var8 != var9) {
               this.currentTypeface = var9;
               var5 = true;
            }

            if (isClose(var1, this.expandedTextSize)) {
               this.scale = 1.0F;
            } else {
               this.scale = var1 / this.expandedTextSize;
            }

            var1 = this.collapsedTextSize / this.expandedTextSize;
            if (var4 * var1 > var3) {
               var1 = Math.min(var3 / var1, var4);
            } else {
               var1 = var4;
            }
         }

         boolean var7 = true;
         var6 = var5;
         if (var1 > 0.0F) {
            if (this.currentTextSize == var2 && !this.boundsChanged && !var5) {
               var5 = false;
            } else {
               var5 = true;
            }

            this.currentTextSize = var2;
            this.boundsChanged = false;
            var6 = var5;
         }

         if (this.textToDraw == null || var6) {
            this.textPaint.setTextSize(this.currentTextSize);
            this.textPaint.setTypeface(this.currentTypeface);
            TextPaint var10 = this.textPaint;
            if (this.scale == 1.0F) {
               var7 = false;
            }

            var10.setLinearText(var7);
            CharSequence var11 = TextUtils.ellipsize(this.text, this.textPaint, var1, TruncateAt.END);
            if (!TextUtils.equals(var11, this.textToDraw)) {
               this.textToDraw = var11;
               this.isRtl = this.calculateIsRtl(var11);
            }
         }

      }
   }

   private void clearTexture() {
      Bitmap var1 = this.expandedTitleTexture;
      if (var1 != null) {
         var1.recycle();
         this.expandedTitleTexture = null;
      }

   }

   private void ensureExpandedTexture() {
      if (this.expandedTitleTexture == null && !this.expandedBounds.isEmpty()) {
         if (!TextUtils.isEmpty(this.textToDraw)) {
            this.calculateOffsets(0.0F);
            this.textureAscent = this.textPaint.ascent();
            this.textureDescent = this.textPaint.descent();
            TextPaint var3 = this.textPaint;
            CharSequence var4 = this.textToDraw;
            int var1 = Math.round(var3.measureText(var4, 0, var4.length()));
            int var2 = Math.round(this.textureDescent - this.textureAscent);
            if (var1 > 0) {
               if (var2 > 0) {
                  this.expandedTitleTexture = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
                  Canvas var5 = new Canvas(this.expandedTitleTexture);
                  var4 = this.textToDraw;
                  var5.drawText(var4, 0, var4.length(), 0.0F, (float)var2 - this.textPaint.descent(), this.textPaint);
                  if (this.texturePaint == null) {
                     this.texturePaint = new Paint(3);
                  }

               }
            }
         }
      }
   }

   private int getCurrentColor(ColorStateList var1) {
      if (var1 == null) {
         return 0;
      } else {
         int[] var2 = this.state;
         return var2 != null ? var1.getColorForState(var2, 0) : var1.getDefaultColor();
      }
   }

   private int getCurrentExpandedTextColor() {
      return this.getCurrentColor(this.expandedTextColor);
   }

   private void getTextPaintCollapsed(TextPaint var1) {
      var1.setTextSize(this.collapsedTextSize);
      var1.setTypeface(this.collapsedTypeface);
   }

   private void getTextPaintExpanded(TextPaint var1) {
      var1.setTextSize(this.expandedTextSize);
      var1.setTypeface(this.expandedTypeface);
   }

   private void interpolateBounds(float var1) {
      this.currentBounds.left = lerp((float)this.expandedBounds.left, (float)this.collapsedBounds.left, var1, this.positionInterpolator);
      this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, var1, this.positionInterpolator);
      this.currentBounds.right = lerp((float)this.expandedBounds.right, (float)this.collapsedBounds.right, var1, this.positionInterpolator);
      this.currentBounds.bottom = lerp((float)this.expandedBounds.bottom, (float)this.collapsedBounds.bottom, var1, this.positionInterpolator);
   }

   private static boolean isClose(float var0, float var1) {
      return Math.abs(var0 - var1) < 0.001F;
   }

   private static float lerp(float var0, float var1, float var2, TimeInterpolator var3) {
      float var4 = var2;
      if (var3 != null) {
         var4 = var3.getInterpolation(var2);
      }

      return AnimationUtils.lerp(var0, var1, var4);
   }

   private static boolean rectEquals(Rect var0, int var1, int var2, int var3, int var4) {
      return var0.left == var1 && var0.top == var2 && var0.right == var3 && var0.bottom == var4;
   }

   private boolean setCollapsedTypefaceInternal(Typeface var1) {
      CancelableFontCallback var2 = this.collapsedFontCallback;
      if (var2 != null) {
         var2.cancel();
      }

      if (this.collapsedTypeface != var1) {
         this.collapsedTypeface = var1;
         return true;
      } else {
         return false;
      }
   }

   private boolean setExpandedTypefaceInternal(Typeface var1) {
      CancelableFontCallback var2 = this.expandedFontCallback;
      if (var2 != null) {
         var2.cancel();
      }

      if (this.expandedTypeface != var1) {
         this.expandedTypeface = var1;
         return true;
      } else {
         return false;
      }
   }

   private void setInterpolatedTextSize(float var1) {
      this.calculateUsingTextSize(var1);
      boolean var2;
      if (USE_SCALING_TEXTURE && this.scale != 1.0F) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.useTexture = var2;
      if (var2) {
         this.ensureExpandedTexture();
      }

      ViewCompat.postInvalidateOnAnimation(this.view);
   }

   public float calculateCollapsedTextWidth() {
      if (this.text == null) {
         return 0.0F;
      } else {
         this.getTextPaintCollapsed(this.tmpPaint);
         TextPaint var1 = this.tmpPaint;
         CharSequence var2 = this.text;
         return var1.measureText(var2, 0, var2.length());
      }
   }

   public void draw(Canvas var1) {
      int var8 = var1.save();
      if (this.textToDraw != null && this.drawTitle) {
         float var4 = this.currentDrawX;
         float var3 = this.currentDrawY;
         boolean var7;
         if (this.useTexture && this.expandedTitleTexture != null) {
            var7 = true;
         } else {
            var7 = false;
         }

         float var2;
         float var5;
         float var6;
         if (var7) {
            var2 = this.textureAscent;
            var5 = this.scale;
            var6 = this.textureDescent;
            var2 *= var5;
         } else {
            var2 = this.textPaint.ascent();
            var5 = this.scale;
            this.textPaint.descent();
            var6 = this.scale;
            var2 *= var5;
         }

         if (var7) {
            var2 += var3;
         } else {
            var2 = var3;
         }

         var3 = this.scale;
         if (var3 != 1.0F) {
            var1.scale(var3, var3, var4, var2);
         }

         if (var7) {
            var1.drawBitmap(this.expandedTitleTexture, var4, var2, this.texturePaint);
         } else {
            CharSequence var9 = this.textToDraw;
            var1.drawText(var9, 0, var9.length(), var4, var2, this.textPaint);
         }
      }

      var1.restoreToCount(var8);
   }

   public void getCollapsedTextActualBounds(RectF var1) {
      boolean var3 = this.calculateIsRtl(this.text);
      Rect var4 = this.collapsedBounds;
      float var2;
      if (!var3) {
         var2 = (float)var4.left;
      } else {
         var2 = (float)var4.right - this.calculateCollapsedTextWidth();
      }

      var1.left = var2;
      var1.top = (float)this.collapsedBounds.top;
      if (!var3) {
         var2 = var1.left + this.calculateCollapsedTextWidth();
      } else {
         var2 = (float)this.collapsedBounds.right;
      }

      var1.right = var2;
      var1.bottom = (float)this.collapsedBounds.top + this.getCollapsedTextHeight();
   }

   public ColorStateList getCollapsedTextColor() {
      return this.collapsedTextColor;
   }

   public int getCollapsedTextGravity() {
      return this.collapsedTextGravity;
   }

   public float getCollapsedTextHeight() {
      this.getTextPaintCollapsed(this.tmpPaint);
      return -this.tmpPaint.ascent();
   }

   public float getCollapsedTextSize() {
      return this.collapsedTextSize;
   }

   public Typeface getCollapsedTypeface() {
      Typeface var1 = this.collapsedTypeface;
      return var1 != null ? var1 : Typeface.DEFAULT;
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
      return -this.tmpPaint.ascent();
   }

   public float getExpandedTextSize() {
      return this.expandedTextSize;
   }

   public Typeface getExpandedTypeface() {
      Typeface var1 = this.expandedTypeface;
      return var1 != null ? var1 : Typeface.DEFAULT;
   }

   public float getExpansionFraction() {
      return this.expandedFraction;
   }

   public CharSequence getText() {
      return this.text;
   }

   public final boolean isStateful() {
      ColorStateList var1 = this.collapsedTextColor;
      if (var1 == null || !var1.isStateful()) {
         var1 = this.expandedTextColor;
         if (var1 == null || !var1.isStateful()) {
            return false;
         }
      }

      return true;
   }

   void onBoundsChanged() {
      boolean var1;
      if (this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.drawTitle = var1;
   }

   public void recalculate() {
      if (this.view.getHeight() > 0 && this.view.getWidth() > 0) {
         this.calculateBaseOffsets();
         this.calculateCurrentOffsets();
      }

   }

   public void setCollapsedBounds(int var1, int var2, int var3, int var4) {
      if (!rectEquals(this.collapsedBounds, var1, var2, var3, var4)) {
         this.collapsedBounds.set(var1, var2, var3, var4);
         this.boundsChanged = true;
         this.onBoundsChanged();
      }

   }

   public void setCollapsedBounds(Rect var1) {
      this.setCollapsedBounds(var1.left, var1.top, var1.right, var1.bottom);
   }

   public void setCollapsedTextAppearance(int var1) {
      TextAppearance var2 = new TextAppearance(this.view.getContext(), var1);
      if (var2.textColor != null) {
         this.collapsedTextColor = var2.textColor;
      }

      if (var2.textSize != 0.0F) {
         this.collapsedTextSize = var2.textSize;
      }

      if (var2.shadowColor != null) {
         this.collapsedShadowColor = var2.shadowColor;
      }

      this.collapsedShadowDx = var2.shadowDx;
      this.collapsedShadowDy = var2.shadowDy;
      this.collapsedShadowRadius = var2.shadowRadius;
      CancelableFontCallback var3 = this.collapsedFontCallback;
      if (var3 != null) {
         var3.cancel();
      }

      this.collapsedFontCallback = new CancelableFontCallback(new CancelableFontCallback.ApplyFont() {
         public void apply(Typeface var1) {
            CollapsingTextHelper.this.setCollapsedTypeface(var1);
         }
      }, var2.getFallbackFont());
      var2.getFontAsync(this.view.getContext(), this.collapsedFontCallback);
      this.recalculate();
   }

   public void setCollapsedTextColor(ColorStateList var1) {
      if (this.collapsedTextColor != var1) {
         this.collapsedTextColor = var1;
         this.recalculate();
      }

   }

   public void setCollapsedTextGravity(int var1) {
      if (this.collapsedTextGravity != var1) {
         this.collapsedTextGravity = var1;
         this.recalculate();
      }

   }

   public void setCollapsedTextSize(float var1) {
      if (this.collapsedTextSize != var1) {
         this.collapsedTextSize = var1;
         this.recalculate();
      }

   }

   public void setCollapsedTypeface(Typeface var1) {
      if (this.setCollapsedTypefaceInternal(var1)) {
         this.recalculate();
      }

   }

   public void setExpandedBounds(int var1, int var2, int var3, int var4) {
      if (!rectEquals(this.expandedBounds, var1, var2, var3, var4)) {
         this.expandedBounds.set(var1, var2, var3, var4);
         this.boundsChanged = true;
         this.onBoundsChanged();
      }

   }

   public void setExpandedBounds(Rect var1) {
      this.setExpandedBounds(var1.left, var1.top, var1.right, var1.bottom);
   }

   public void setExpandedTextAppearance(int var1) {
      TextAppearance var2 = new TextAppearance(this.view.getContext(), var1);
      if (var2.textColor != null) {
         this.expandedTextColor = var2.textColor;
      }

      if (var2.textSize != 0.0F) {
         this.expandedTextSize = var2.textSize;
      }

      if (var2.shadowColor != null) {
         this.expandedShadowColor = var2.shadowColor;
      }

      this.expandedShadowDx = var2.shadowDx;
      this.expandedShadowDy = var2.shadowDy;
      this.expandedShadowRadius = var2.shadowRadius;
      CancelableFontCallback var3 = this.expandedFontCallback;
      if (var3 != null) {
         var3.cancel();
      }

      this.expandedFontCallback = new CancelableFontCallback(new CancelableFontCallback.ApplyFont() {
         public void apply(Typeface var1) {
            CollapsingTextHelper.this.setExpandedTypeface(var1);
         }
      }, var2.getFallbackFont());
      var2.getFontAsync(this.view.getContext(), this.expandedFontCallback);
      this.recalculate();
   }

   public void setExpandedTextColor(ColorStateList var1) {
      if (this.expandedTextColor != var1) {
         this.expandedTextColor = var1;
         this.recalculate();
      }

   }

   public void setExpandedTextGravity(int var1) {
      if (this.expandedTextGravity != var1) {
         this.expandedTextGravity = var1;
         this.recalculate();
      }

   }

   public void setExpandedTextSize(float var1) {
      if (this.expandedTextSize != var1) {
         this.expandedTextSize = var1;
         this.recalculate();
      }

   }

   public void setExpandedTypeface(Typeface var1) {
      if (this.setExpandedTypefaceInternal(var1)) {
         this.recalculate();
      }

   }

   public void setExpansionFraction(float var1) {
      var1 = MathUtils.clamp(var1, 0.0F, 1.0F);
      if (var1 != this.expandedFraction) {
         this.expandedFraction = var1;
         this.calculateCurrentOffsets();
      }

   }

   public void setPositionInterpolator(TimeInterpolator var1) {
      this.positionInterpolator = var1;
      this.recalculate();
   }

   public final boolean setState(int[] var1) {
      this.state = var1;
      if (this.isStateful()) {
         this.recalculate();
         return true;
      } else {
         return false;
      }
   }

   public void setText(CharSequence var1) {
      if (var1 == null || !TextUtils.equals(this.text, var1)) {
         this.text = var1;
         this.textToDraw = null;
         this.clearTexture();
         this.recalculate();
      }

   }

   public void setTextSizeInterpolator(TimeInterpolator var1) {
      this.textSizeInterpolator = var1;
      this.recalculate();
   }

   public void setTypefaces(Typeface var1) {
      boolean var2 = this.setCollapsedTypefaceInternal(var1);
      boolean var3 = this.setExpandedTypefaceInternal(var1);
      if (var2 || var3) {
         this.recalculate();
      }

   }
}
