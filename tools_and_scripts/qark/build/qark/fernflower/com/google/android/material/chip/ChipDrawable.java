package com.google.android.material.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.TintAwareDrawable;
import androidx.core.text.BidiFormatter;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.canvas.CanvasCompat;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public class ChipDrawable extends MaterialShapeDrawable implements TintAwareDrawable, Callback, TextDrawableHelper.TextDrawableDelegate {
   private static final boolean DEBUG = false;
   private static final int[] DEFAULT_STATE = new int[]{16842910};
   private static final String NAMESPACE_APP = "http://schemas.android.com/apk/res-auto";
   private static final ShapeDrawable closeIconRippleMask = new ShapeDrawable(new OvalShape());
   private int alpha = 255;
   private boolean checkable;
   private Drawable checkedIcon;
   private boolean checkedIconVisible;
   private ColorStateList chipBackgroundColor;
   private float chipCornerRadius;
   private float chipEndPadding;
   private Drawable chipIcon;
   private float chipIconSize;
   private ColorStateList chipIconTint;
   private boolean chipIconVisible;
   private float chipMinHeight;
   private final Paint chipPaint = new Paint(1);
   private float chipStartPadding;
   private ColorStateList chipStrokeColor;
   private float chipStrokeWidth;
   private ColorStateList chipSurfaceColor;
   private Drawable closeIcon;
   private CharSequence closeIconContentDescription;
   private float closeIconEndPadding;
   private Drawable closeIconRipple;
   private float closeIconSize;
   private float closeIconStartPadding;
   private int[] closeIconStateSet;
   private ColorStateList closeIconTint;
   private boolean closeIconVisible;
   private ColorFilter colorFilter;
   private ColorStateList compatRippleColor;
   private final Context context;
   private boolean currentChecked;
   private int currentChipBackgroundColor;
   private int currentChipStrokeColor;
   private int currentChipSurfaceColor;
   private int currentCompatRippleColor;
   private int currentCompositeSurfaceBackgroundColor;
   private int currentTextColor;
   private int currentTint;
   private final Paint debugPaint;
   private WeakReference delegate;
   private final FontMetrics fontMetrics = new FontMetrics();
   private boolean hasChipIconTint;
   private MotionSpec hideMotionSpec;
   private float iconEndPadding;
   private float iconStartPadding;
   private boolean isShapeThemingEnabled;
   private int maxWidth;
   private final PointF pointF = new PointF();
   private final RectF rectF = new RectF();
   private ColorStateList rippleColor;
   private final Path shapePath = new Path();
   private boolean shouldDrawText;
   private MotionSpec showMotionSpec;
   private CharSequence text;
   private final TextDrawableHelper textDrawableHelper;
   private float textEndPadding;
   private float textStartPadding;
   private ColorStateList tint;
   private PorterDuffColorFilter tintFilter;
   private Mode tintMode;
   private TruncateAt truncateAt;
   private boolean useCompatRipple;

   private ChipDrawable(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.tintMode = Mode.SRC_IN;
      this.delegate = new WeakReference((Object)null);
      this.initializeElevationOverlay(var1);
      this.context = var1;
      TextDrawableHelper var6 = new TextDrawableHelper(this);
      this.textDrawableHelper = var6;
      this.text = "";
      var6.getTextPaint().density = var1.getResources().getDisplayMetrics().density;
      this.debugPaint = null;
      if (false) {
         Style var5 = Style.STROKE;
         throw new NullPointerException();
      } else {
         this.setState(DEFAULT_STATE);
         this.setCloseIconState(DEFAULT_STATE);
         this.shouldDrawText = true;
         if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            closeIconRippleMask.setTint(-1);
         }

      }
   }

   private void applyChildDrawable(Drawable var1) {
      if (var1 != null) {
         var1.setCallback(this);
         DrawableCompat.setLayoutDirection(var1, DrawableCompat.getLayoutDirection(this));
         var1.setLevel(this.getLevel());
         var1.setVisible(this.isVisible(), false);
         if (var1 == this.closeIcon) {
            if (var1.isStateful()) {
               var1.setState(this.getCloseIconState());
            }

            DrawableCompat.setTintList(var1, this.closeIconTint);
         } else {
            if (var1.isStateful()) {
               var1.setState(this.getState());
            }

            Drawable var2 = this.chipIcon;
            if (var1 == var2 && this.hasChipIconTint) {
               DrawableCompat.setTintList(var2, this.chipIconTint);
            }

         }
      }
   }

   private void calculateChipIconBounds(Rect var1, RectF var2) {
      var2.setEmpty();
      if (this.showsChipIcon() || this.showsCheckedIcon()) {
         float var3 = this.chipStartPadding + this.iconStartPadding;
         if (DrawableCompat.getLayoutDirection(this) == 0) {
            var2.left = (float)var1.left + var3;
            var2.right = var2.left + this.chipIconSize;
         } else {
            var2.right = (float)var1.right - var3;
            var2.left = var2.right - this.chipIconSize;
         }

         var2.top = var1.exactCenterY() - this.chipIconSize / 2.0F;
         var2.bottom = var2.top + this.chipIconSize;
      }

   }

   private void calculateChipTouchBounds(Rect var1, RectF var2) {
      var2.set(var1);
      if (this.showsCloseIcon()) {
         float var3 = this.chipEndPadding + this.closeIconEndPadding + this.closeIconSize + this.closeIconStartPadding + this.textEndPadding;
         if (DrawableCompat.getLayoutDirection(this) == 0) {
            var2.right = (float)var1.right - var3;
            return;
         }

         var2.left = (float)var1.left + var3;
      }

   }

   private void calculateCloseIconBounds(Rect var1, RectF var2) {
      var2.setEmpty();
      if (this.showsCloseIcon()) {
         float var3 = this.chipEndPadding + this.closeIconEndPadding;
         if (DrawableCompat.getLayoutDirection(this) == 0) {
            var2.right = (float)var1.right - var3;
            var2.left = var2.right - this.closeIconSize;
         } else {
            var2.left = (float)var1.left + var3;
            var2.right = var2.left + this.closeIconSize;
         }

         var2.top = var1.exactCenterY() - this.closeIconSize / 2.0F;
         var2.bottom = var2.top + this.closeIconSize;
      }

   }

   private void calculateCloseIconTouchBounds(Rect var1, RectF var2) {
      var2.setEmpty();
      if (this.showsCloseIcon()) {
         float var3 = this.chipEndPadding + this.closeIconEndPadding + this.closeIconSize + this.closeIconStartPadding + this.textEndPadding;
         if (DrawableCompat.getLayoutDirection(this) == 0) {
            var2.right = (float)var1.right;
            var2.left = var2.right - var3;
         } else {
            var2.left = (float)var1.left;
            var2.right = (float)var1.left + var3;
         }

         var2.top = (float)var1.top;
         var2.bottom = (float)var1.bottom;
      }

   }

   private void calculateTextBounds(Rect var1, RectF var2) {
      var2.setEmpty();
      if (this.text != null) {
         float var3 = this.chipStartPadding + this.calculateChipIconWidth() + this.textStartPadding;
         float var4 = this.chipEndPadding + this.calculateCloseIconWidth() + this.textEndPadding;
         if (DrawableCompat.getLayoutDirection(this) == 0) {
            var2.left = (float)var1.left + var3;
            var2.right = (float)var1.right - var4;
         } else {
            var2.left = (float)var1.left + var4;
            var2.right = (float)var1.right - var3;
         }

         var2.top = (float)var1.top;
         var2.bottom = (float)var1.bottom;
      }

   }

   private float calculateTextCenterFromBaseline() {
      this.textDrawableHelper.getTextPaint().getFontMetrics(this.fontMetrics);
      return (this.fontMetrics.descent + this.fontMetrics.ascent) / 2.0F;
   }

   private boolean canShowCheckedIcon() {
      return this.checkedIconVisible && this.checkedIcon != null && this.checkable;
   }

   public static ChipDrawable createFromAttributes(Context var0, AttributeSet var1, int var2, int var3) {
      ChipDrawable var4 = new ChipDrawable(var0, var1, var2, var3);
      var4.loadFromAttributes(var1, var2, var3);
      return var4;
   }

   public static ChipDrawable createFromResource(Context var0, int var1) {
      AttributeSet var3 = DrawableUtils.parseDrawableXml(var0, var1, "chip");
      int var2 = var3.getStyleAttribute();
      var1 = var2;
      if (var2 == 0) {
         var1 = style.Widget_MaterialComponents_Chip_Entry;
      }

      return createFromAttributes(var0, var3, attr.chipStandaloneStyle, var1);
   }

   private void drawCheckedIcon(Canvas var1, Rect var2) {
      if (this.showsCheckedIcon()) {
         this.calculateChipIconBounds(var2, this.rectF);
         float var3 = this.rectF.left;
         float var4 = this.rectF.top;
         var1.translate(var3, var4);
         this.checkedIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
         this.checkedIcon.draw(var1);
         var1.translate(-var3, -var4);
      }

   }

   private void drawChipBackground(Canvas var1, Rect var2) {
      if (!this.isShapeThemingEnabled) {
         this.chipPaint.setColor(this.currentChipBackgroundColor);
         this.chipPaint.setStyle(Style.FILL);
         this.chipPaint.setColorFilter(this.getTintColorFilter());
         this.rectF.set(var2);
         var1.drawRoundRect(this.rectF, this.getChipCornerRadius(), this.getChipCornerRadius(), this.chipPaint);
      }

   }

   private void drawChipIcon(Canvas var1, Rect var2) {
      if (this.showsChipIcon()) {
         this.calculateChipIconBounds(var2, this.rectF);
         float var3 = this.rectF.left;
         float var4 = this.rectF.top;
         var1.translate(var3, var4);
         this.chipIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
         this.chipIcon.draw(var1);
         var1.translate(-var3, -var4);
      }

   }

   private void drawChipStroke(Canvas var1, Rect var2) {
      if (this.chipStrokeWidth > 0.0F && !this.isShapeThemingEnabled) {
         this.chipPaint.setColor(this.currentChipStrokeColor);
         this.chipPaint.setStyle(Style.STROKE);
         if (!this.isShapeThemingEnabled) {
            this.chipPaint.setColorFilter(this.getTintColorFilter());
         }

         this.rectF.set((float)var2.left + this.chipStrokeWidth / 2.0F, (float)var2.top + this.chipStrokeWidth / 2.0F, (float)var2.right - this.chipStrokeWidth / 2.0F, (float)var2.bottom - this.chipStrokeWidth / 2.0F);
         float var3 = this.chipCornerRadius - this.chipStrokeWidth / 2.0F;
         var1.drawRoundRect(this.rectF, var3, var3, this.chipPaint);
      }

   }

   private void drawChipSurface(Canvas var1, Rect var2) {
      if (!this.isShapeThemingEnabled) {
         this.chipPaint.setColor(this.currentChipSurfaceColor);
         this.chipPaint.setStyle(Style.FILL);
         this.rectF.set(var2);
         var1.drawRoundRect(this.rectF, this.getChipCornerRadius(), this.getChipCornerRadius(), this.chipPaint);
      }

   }

   private void drawCloseIcon(Canvas var1, Rect var2) {
      if (this.showsCloseIcon()) {
         this.calculateCloseIconBounds(var2, this.rectF);
         float var3 = this.rectF.left;
         float var4 = this.rectF.top;
         var1.translate(var3, var4);
         this.closeIcon.setBounds(0, 0, (int)this.rectF.width(), (int)this.rectF.height());
         if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            this.closeIconRipple.setBounds(this.closeIcon.getBounds());
            this.closeIconRipple.jumpToCurrentState();
            this.closeIconRipple.draw(var1);
         } else {
            this.closeIcon.draw(var1);
         }

         var1.translate(-var3, -var4);
      }

   }

   private void drawCompatRipple(Canvas var1, Rect var2) {
      this.chipPaint.setColor(this.currentCompatRippleColor);
      this.chipPaint.setStyle(Style.FILL);
      this.rectF.set(var2);
      if (!this.isShapeThemingEnabled) {
         var1.drawRoundRect(this.rectF, this.getChipCornerRadius(), this.getChipCornerRadius(), this.chipPaint);
      } else {
         this.calculatePathForSize(new RectF(var2), this.shapePath);
         super.drawShape(var1, this.chipPaint, this.shapePath, this.getBoundsAsRectF());
      }
   }

   private void drawDebug(Canvas var1, Rect var2) {
      Paint var3 = this.debugPaint;
      if (var3 != null) {
         var3.setColor(ColorUtils.setAlphaComponent(-16777216, 127));
         var1.drawRect(var2, this.debugPaint);
         if (this.showsChipIcon() || this.showsCheckedIcon()) {
            this.calculateChipIconBounds(var2, this.rectF);
            var1.drawRect(this.rectF, this.debugPaint);
         }

         if (this.text != null) {
            var1.drawLine((float)var2.left, var2.exactCenterY(), (float)var2.right, var2.exactCenterY(), this.debugPaint);
         }

         if (this.showsCloseIcon()) {
            this.calculateCloseIconBounds(var2, this.rectF);
            var1.drawRect(this.rectF, this.debugPaint);
         }

         this.debugPaint.setColor(ColorUtils.setAlphaComponent(-65536, 127));
         this.calculateChipTouchBounds(var2, this.rectF);
         var1.drawRect(this.rectF, this.debugPaint);
         this.debugPaint.setColor(ColorUtils.setAlphaComponent(-16711936, 127));
         this.calculateCloseIconTouchBounds(var2, this.rectF);
         var1.drawRect(this.rectF, this.debugPaint);
      }

   }

   private void drawText(Canvas var1, Rect var2) {
      if (this.text != null) {
         Align var5 = this.calculateTextOriginAndAlignment(var2, this.pointF);
         this.calculateTextBounds(var2, this.rectF);
         if (this.textDrawableHelper.getTextAppearance() != null) {
            this.textDrawableHelper.getTextPaint().drawableState = this.getState();
            this.textDrawableHelper.updateTextPaintDrawState(this.context);
         }

         this.textDrawableHelper.getTextPaint().setTextAlign(var5);
         boolean var3;
         if (Math.round(this.textDrawableHelper.getTextWidth(this.getText().toString())) > Math.round(this.rectF.width())) {
            var3 = true;
         } else {
            var3 = false;
         }

         int var4 = 0;
         if (var3) {
            var4 = var1.save();
            var1.clipRect(this.rectF);
         }

         CharSequence var7 = this.text;
         CharSequence var6 = var7;
         if (var3) {
            var6 = var7;
            if (this.truncateAt != null) {
               var6 = TextUtils.ellipsize(this.text, this.textDrawableHelper.getTextPaint(), this.rectF.width(), this.truncateAt);
            }
         }

         var1.drawText(var6, 0, var6.length(), this.pointF.x, this.pointF.y, this.textDrawableHelper.getTextPaint());
         if (var3) {
            var1.restoreToCount(var4);
         }
      }

   }

   private ColorFilter getTintColorFilter() {
      ColorFilter var1 = this.colorFilter;
      return (ColorFilter)(var1 != null ? var1 : this.tintFilter);
   }

   private static boolean hasState(int[] var0, int var1) {
      if (var0 == null) {
         return false;
      } else {
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if (var0[var2] == var1) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean isStateful(ColorStateList var0) {
      return var0 != null && var0.isStateful();
   }

   private static boolean isStateful(Drawable var0) {
      return var0 != null && var0.isStateful();
   }

   private static boolean isStateful(TextAppearance var0) {
      return var0 != null && var0.textColor != null && var0.textColor.isStateful();
   }

   private void loadFromAttributes(AttributeSet var1, int var2, int var3) {
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(this.context, var1, styleable.Chip, var2, var3);
      this.isShapeThemingEnabled = var4.hasValue(styleable.Chip_shapeAppearance);
      this.setChipSurfaceColor(MaterialResources.getColorStateList(this.context, var4, styleable.Chip_chipSurfaceColor));
      this.setChipBackgroundColor(MaterialResources.getColorStateList(this.context, var4, styleable.Chip_chipBackgroundColor));
      this.setChipMinHeight(var4.getDimension(styleable.Chip_chipMinHeight, 0.0F));
      if (var4.hasValue(styleable.Chip_chipCornerRadius)) {
         this.setChipCornerRadius(var4.getDimension(styleable.Chip_chipCornerRadius, 0.0F));
      }

      this.setChipStrokeColor(MaterialResources.getColorStateList(this.context, var4, styleable.Chip_chipStrokeColor));
      this.setChipStrokeWidth(var4.getDimension(styleable.Chip_chipStrokeWidth, 0.0F));
      this.setRippleColor(MaterialResources.getColorStateList(this.context, var4, styleable.Chip_rippleColor));
      this.setText(var4.getText(styleable.Chip_android_text));
      this.setTextAppearance(MaterialResources.getTextAppearance(this.context, var4, styleable.Chip_android_textAppearance));
      var2 = var4.getInt(styleable.Chip_android_ellipsize, 0);
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 == 3) {
               this.setEllipsize(TruncateAt.END);
            }
         } else {
            this.setEllipsize(TruncateAt.MIDDLE);
         }
      } else {
         this.setEllipsize(TruncateAt.START);
      }

      this.setChipIconVisible(var4.getBoolean(styleable.Chip_chipIconVisible, false));
      if (var1 != null && var1.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconEnabled") != null && var1.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconVisible") == null) {
         this.setChipIconVisible(var4.getBoolean(styleable.Chip_chipIconEnabled, false));
      }

      this.setChipIcon(MaterialResources.getDrawable(this.context, var4, styleable.Chip_chipIcon));
      if (var4.hasValue(styleable.Chip_chipIconTint)) {
         this.setChipIconTint(MaterialResources.getColorStateList(this.context, var4, styleable.Chip_chipIconTint));
      }

      this.setChipIconSize(var4.getDimension(styleable.Chip_chipIconSize, 0.0F));
      this.setCloseIconVisible(var4.getBoolean(styleable.Chip_closeIconVisible, false));
      if (var1 != null && var1.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconEnabled") != null && var1.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconVisible") == null) {
         this.setCloseIconVisible(var4.getBoolean(styleable.Chip_closeIconEnabled, false));
      }

      this.setCloseIcon(MaterialResources.getDrawable(this.context, var4, styleable.Chip_closeIcon));
      this.setCloseIconTint(MaterialResources.getColorStateList(this.context, var4, styleable.Chip_closeIconTint));
      this.setCloseIconSize(var4.getDimension(styleable.Chip_closeIconSize, 0.0F));
      this.setCheckable(var4.getBoolean(styleable.Chip_android_checkable, false));
      this.setCheckedIconVisible(var4.getBoolean(styleable.Chip_checkedIconVisible, false));
      if (var1 != null && var1.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconEnabled") != null && var1.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconVisible") == null) {
         this.setCheckedIconVisible(var4.getBoolean(styleable.Chip_checkedIconEnabled, false));
      }

      this.setCheckedIcon(MaterialResources.getDrawable(this.context, var4, styleable.Chip_checkedIcon));
      this.setShowMotionSpec(MotionSpec.createFromAttribute(this.context, var4, styleable.Chip_showMotionSpec));
      this.setHideMotionSpec(MotionSpec.createFromAttribute(this.context, var4, styleable.Chip_hideMotionSpec));
      this.setChipStartPadding(var4.getDimension(styleable.Chip_chipStartPadding, 0.0F));
      this.setIconStartPadding(var4.getDimension(styleable.Chip_iconStartPadding, 0.0F));
      this.setIconEndPadding(var4.getDimension(styleable.Chip_iconEndPadding, 0.0F));
      this.setTextStartPadding(var4.getDimension(styleable.Chip_textStartPadding, 0.0F));
      this.setTextEndPadding(var4.getDimension(styleable.Chip_textEndPadding, 0.0F));
      this.setCloseIconStartPadding(var4.getDimension(styleable.Chip_closeIconStartPadding, 0.0F));
      this.setCloseIconEndPadding(var4.getDimension(styleable.Chip_closeIconEndPadding, 0.0F));
      this.setChipEndPadding(var4.getDimension(styleable.Chip_chipEndPadding, 0.0F));
      this.setMaxWidth(var4.getDimensionPixelSize(styleable.Chip_android_maxWidth, Integer.MAX_VALUE));
      var4.recycle();
   }

   private boolean onStateChange(int[] var1, int[] var2) {
      boolean var9 = super.onStateChange(var1);
      boolean var7 = false;
      ColorStateList var12 = this.chipSurfaceColor;
      int var5;
      if (var12 != null) {
         var5 = var12.getColorForState(var1, this.currentChipSurfaceColor);
      } else {
         var5 = 0;
      }

      if (this.currentChipSurfaceColor != var5) {
         this.currentChipSurfaceColor = var5;
         var9 = true;
      }

      var12 = this.chipBackgroundColor;
      int var6;
      if (var12 != null) {
         var6 = var12.getColorForState(var1, this.currentChipBackgroundColor);
      } else {
         var6 = 0;
      }

      if (this.currentChipBackgroundColor != var6) {
         this.currentChipBackgroundColor = var6;
         var9 = true;
      }

      int var8 = MaterialColors.layer(var5, var6);
      var5 = this.currentCompositeSurfaceBackgroundColor;
      boolean var11 = true;
      boolean var13;
      if (var5 != var8) {
         var13 = true;
      } else {
         var13 = false;
      }

      boolean var14;
      if (this.getFillColor() == null) {
         var14 = true;
      } else {
         var14 = false;
      }

      if (var13 | var14) {
         this.currentCompositeSurfaceBackgroundColor = var8;
         this.setFillColor(ColorStateList.valueOf(var8));
         var9 = true;
      }

      var12 = this.chipStrokeColor;
      if (var12 != null) {
         var5 = var12.getColorForState(var1, this.currentChipStrokeColor);
      } else {
         var5 = 0;
      }

      if (this.currentChipStrokeColor != var5) {
         this.currentChipStrokeColor = var5;
         var9 = true;
      }

      if (this.compatRippleColor != null && RippleUtils.shouldDrawRippleCompat(var1)) {
         var5 = this.compatRippleColor.getColorForState(var1, this.currentCompatRippleColor);
      } else {
         var5 = 0;
      }

      boolean var10 = var9;
      if (this.currentCompatRippleColor != var5) {
         this.currentCompatRippleColor = var5;
         var10 = var9;
         if (this.useCompatRipple) {
            var10 = true;
         }
      }

      if (this.textDrawableHelper.getTextAppearance() != null && this.textDrawableHelper.getTextAppearance().textColor != null) {
         var5 = this.textDrawableHelper.getTextAppearance().textColor.getColorForState(var1, this.currentTextColor);
      } else {
         var5 = 0;
      }

      var9 = var10;
      if (this.currentTextColor != var5) {
         this.currentTextColor = var5;
         var9 = true;
      }

      if (!hasState(this.getState(), 16842912) || !this.checkable) {
         var11 = false;
      }

      var10 = var9;
      var13 = var7;
      if (this.currentChecked != var11) {
         var10 = var9;
         var13 = var7;
         if (this.checkedIcon != null) {
            float var3 = this.calculateChipIconWidth();
            this.currentChecked = var11;
            float var4 = this.calculateChipIconWidth();
            var9 = true;
            var10 = var9;
            var13 = var7;
            if (var3 != var4) {
               var13 = true;
               var10 = var9;
            }
         }
      }

      var12 = this.tint;
      if (var12 != null) {
         var6 = var12.getColorForState(var1, this.currentTint);
      } else {
         var6 = 0;
      }

      if (this.currentTint != var6) {
         this.currentTint = var6;
         this.tintFilter = DrawableUtils.updateTintFilter(this, this.tint, this.tintMode);
         var10 = true;
      }

      var9 = var10;
      if (isStateful(this.chipIcon)) {
         var9 = var10 | this.chipIcon.setState(var1);
      }

      var10 = var9;
      if (isStateful(this.checkedIcon)) {
         var10 = var9 | this.checkedIcon.setState(var1);
      }

      if (isStateful(this.closeIcon)) {
         int[] var15 = new int[var1.length + var2.length];
         System.arraycopy(var1, 0, var15, 0, var1.length);
         System.arraycopy(var2, 0, var15, var1.length, var2.length);
         var10 |= this.closeIcon.setState(var15);
      }

      var9 = var10;
      if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
         var9 = var10;
         if (isStateful(this.closeIconRipple)) {
            var9 = var10 | this.closeIconRipple.setState(var2);
         }
      }

      if (var9) {
         this.invalidateSelf();
      }

      if (var13) {
         this.onSizeChange();
      }

      return var9;
   }

   private void setChipSurfaceColor(ColorStateList var1) {
      if (this.chipSurfaceColor != var1) {
         this.chipSurfaceColor = var1;
         this.onStateChange(this.getState());
      }

   }

   private boolean showsCheckedIcon() {
      return this.checkedIconVisible && this.checkedIcon != null && this.currentChecked;
   }

   private boolean showsChipIcon() {
      return this.chipIconVisible && this.chipIcon != null;
   }

   private boolean showsCloseIcon() {
      return this.closeIconVisible && this.closeIcon != null;
   }

   private void unapplyChildDrawable(Drawable var1) {
      if (var1 != null) {
         var1.setCallback((Callback)null);
      }

   }

   private void updateCompatRippleColor() {
      ColorStateList var1;
      if (this.useCompatRipple) {
         var1 = RippleUtils.sanitizeRippleDrawableColor(this.rippleColor);
      } else {
         var1 = null;
      }

      this.compatRippleColor = var1;
   }

   private void updateFrameworkCloseIconRipple() {
      this.closeIconRipple = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(this.getRippleColor()), this.closeIcon, closeIconRippleMask);
   }

   float calculateChipIconWidth() {
      return !this.showsChipIcon() && !this.showsCheckedIcon() ? 0.0F : this.iconStartPadding + this.chipIconSize + this.iconEndPadding;
   }

   float calculateCloseIconWidth() {
      return this.showsCloseIcon() ? this.closeIconStartPadding + this.closeIconSize + this.closeIconEndPadding : 0.0F;
   }

   Align calculateTextOriginAndAlignment(Rect var1, PointF var2) {
      var2.set(0.0F, 0.0F);
      Align var4 = Align.LEFT;
      if (this.text != null) {
         float var3 = this.chipStartPadding + this.calculateChipIconWidth() + this.textStartPadding;
         if (DrawableCompat.getLayoutDirection(this) == 0) {
            var2.x = (float)var1.left + var3;
            var4 = Align.LEFT;
         } else {
            var2.x = (float)var1.right - var3;
            var4 = Align.RIGHT;
         }

         var2.y = (float)var1.centerY() - this.calculateTextCenterFromBaseline();
      }

      return var4;
   }

   public void draw(Canvas var1) {
      Rect var3 = this.getBounds();
      if (!var3.isEmpty()) {
         if (this.getAlpha() != 0) {
            int var2 = 0;
            if (this.alpha < 255) {
               var2 = CanvasCompat.saveLayerAlpha(var1, (float)var3.left, (float)var3.top, (float)var3.right, (float)var3.bottom, this.alpha);
            }

            this.drawChipSurface(var1, var3);
            this.drawChipBackground(var1, var3);
            if (this.isShapeThemingEnabled) {
               super.draw(var1);
            }

            this.drawChipStroke(var1, var3);
            this.drawCompatRipple(var1, var3);
            this.drawChipIcon(var1, var3);
            this.drawCheckedIcon(var1, var3);
            if (this.shouldDrawText) {
               this.drawText(var1, var3);
            }

            this.drawCloseIcon(var1, var3);
            this.drawDebug(var1, var3);
            if (this.alpha < 255) {
               var1.restoreToCount(var2);
            }

         }
      }
   }

   public int getAlpha() {
      return this.alpha;
   }

   public Drawable getCheckedIcon() {
      return this.checkedIcon;
   }

   public ColorStateList getChipBackgroundColor() {
      return this.chipBackgroundColor;
   }

   public float getChipCornerRadius() {
      return this.isShapeThemingEnabled ? this.getTopLeftCornerResolvedSize() : this.chipCornerRadius;
   }

   public float getChipEndPadding() {
      return this.chipEndPadding;
   }

   public Drawable getChipIcon() {
      Drawable var1 = this.chipIcon;
      return var1 != null ? DrawableCompat.unwrap(var1) : null;
   }

   public float getChipIconSize() {
      return this.chipIconSize;
   }

   public ColorStateList getChipIconTint() {
      return this.chipIconTint;
   }

   public float getChipMinHeight() {
      return this.chipMinHeight;
   }

   public float getChipStartPadding() {
      return this.chipStartPadding;
   }

   public ColorStateList getChipStrokeColor() {
      return this.chipStrokeColor;
   }

   public float getChipStrokeWidth() {
      return this.chipStrokeWidth;
   }

   public void getChipTouchBounds(RectF var1) {
      this.calculateChipTouchBounds(this.getBounds(), var1);
   }

   public Drawable getCloseIcon() {
      Drawable var1 = this.closeIcon;
      return var1 != null ? DrawableCompat.unwrap(var1) : null;
   }

   public CharSequence getCloseIconContentDescription() {
      return this.closeIconContentDescription;
   }

   public float getCloseIconEndPadding() {
      return this.closeIconEndPadding;
   }

   public float getCloseIconSize() {
      return this.closeIconSize;
   }

   public float getCloseIconStartPadding() {
      return this.closeIconStartPadding;
   }

   public int[] getCloseIconState() {
      return this.closeIconStateSet;
   }

   public ColorStateList getCloseIconTint() {
      return this.closeIconTint;
   }

   public void getCloseIconTouchBounds(RectF var1) {
      this.calculateCloseIconTouchBounds(this.getBounds(), var1);
   }

   public ColorFilter getColorFilter() {
      return this.colorFilter;
   }

   public TruncateAt getEllipsize() {
      return this.truncateAt;
   }

   public MotionSpec getHideMotionSpec() {
      return this.hideMotionSpec;
   }

   public float getIconEndPadding() {
      return this.iconEndPadding;
   }

   public float getIconStartPadding() {
      return this.iconStartPadding;
   }

   public int getIntrinsicHeight() {
      return (int)this.chipMinHeight;
   }

   public int getIntrinsicWidth() {
      return Math.min(Math.round(this.chipStartPadding + this.calculateChipIconWidth() + this.textStartPadding + this.textDrawableHelper.getTextWidth(this.getText().toString()) + this.textEndPadding + this.calculateCloseIconWidth() + this.chipEndPadding), this.maxWidth);
   }

   public int getMaxWidth() {
      return this.maxWidth;
   }

   public int getOpacity() {
      return -3;
   }

   public void getOutline(Outline var1) {
      if (this.isShapeThemingEnabled) {
         super.getOutline(var1);
      } else {
         Rect var2 = this.getBounds();
         if (!var2.isEmpty()) {
            var1.setRoundRect(var2, this.chipCornerRadius);
         } else {
            var1.setRoundRect(0, 0, this.getIntrinsicWidth(), this.getIntrinsicHeight(), this.chipCornerRadius);
         }

         var1.setAlpha((float)this.getAlpha() / 255.0F);
      }
   }

   public ColorStateList getRippleColor() {
      return this.rippleColor;
   }

   public MotionSpec getShowMotionSpec() {
      return this.showMotionSpec;
   }

   public CharSequence getText() {
      return this.text;
   }

   public TextAppearance getTextAppearance() {
      return this.textDrawableHelper.getTextAppearance();
   }

   public float getTextEndPadding() {
      return this.textEndPadding;
   }

   public float getTextStartPadding() {
      return this.textStartPadding;
   }

   public boolean getUseCompatRipple() {
      return this.useCompatRipple;
   }

   public void invalidateDrawable(Drawable var1) {
      Callback var2 = this.getCallback();
      if (var2 != null) {
         var2.invalidateDrawable(this);
      }

   }

   public boolean isCheckable() {
      return this.checkable;
   }

   @Deprecated
   public boolean isCheckedIconEnabled() {
      return this.isCheckedIconVisible();
   }

   public boolean isCheckedIconVisible() {
      return this.checkedIconVisible;
   }

   @Deprecated
   public boolean isChipIconEnabled() {
      return this.isChipIconVisible();
   }

   public boolean isChipIconVisible() {
      return this.chipIconVisible;
   }

   @Deprecated
   public boolean isCloseIconEnabled() {
      return this.isCloseIconVisible();
   }

   public boolean isCloseIconStateful() {
      return isStateful(this.closeIcon);
   }

   public boolean isCloseIconVisible() {
      return this.closeIconVisible;
   }

   boolean isShapeThemingEnabled() {
      return this.isShapeThemingEnabled;
   }

   public boolean isStateful() {
      return isStateful(this.chipSurfaceColor) || isStateful(this.chipBackgroundColor) || isStateful(this.chipStrokeColor) || this.useCompatRipple && isStateful(this.compatRippleColor) || isStateful(this.textDrawableHelper.getTextAppearance()) || this.canShowCheckedIcon() || isStateful(this.chipIcon) || isStateful(this.checkedIcon) || isStateful(this.tint);
   }

   public boolean onLayoutDirectionChanged(int var1) {
      boolean var3 = super.onLayoutDirectionChanged(var1);
      boolean var2 = var3;
      if (this.showsChipIcon()) {
         var2 = var3 | DrawableCompat.setLayoutDirection(this.chipIcon, var1);
      }

      var3 = var2;
      if (this.showsCheckedIcon()) {
         var3 = var2 | DrawableCompat.setLayoutDirection(this.checkedIcon, var1);
      }

      var2 = var3;
      if (this.showsCloseIcon()) {
         var2 = var3 | DrawableCompat.setLayoutDirection(this.closeIcon, var1);
      }

      if (var2) {
         this.invalidateSelf();
      }

      return true;
   }

   protected boolean onLevelChange(int var1) {
      boolean var3 = super.onLevelChange(var1);
      boolean var2 = var3;
      if (this.showsChipIcon()) {
         var2 = var3 | this.chipIcon.setLevel(var1);
      }

      var3 = var2;
      if (this.showsCheckedIcon()) {
         var3 = var2 | this.checkedIcon.setLevel(var1);
      }

      var2 = var3;
      if (this.showsCloseIcon()) {
         var2 = var3 | this.closeIcon.setLevel(var1);
      }

      if (var2) {
         this.invalidateSelf();
      }

      return var2;
   }

   protected void onSizeChange() {
      ChipDrawable.Delegate var1 = (ChipDrawable.Delegate)this.delegate.get();
      if (var1 != null) {
         var1.onChipDrawableSizeChange();
      }

   }

   public boolean onStateChange(int[] var1) {
      if (this.isShapeThemingEnabled) {
         super.onStateChange(var1);
      }

      return this.onStateChange(var1, this.getCloseIconState());
   }

   public void onTextSizeChange() {
      this.onSizeChange();
      this.invalidateSelf();
   }

   public void scheduleDrawable(Drawable var1, Runnable var2, long var3) {
      Callback var5 = this.getCallback();
      if (var5 != null) {
         var5.scheduleDrawable(this, var2, var3);
      }

   }

   public void setAlpha(int var1) {
      if (this.alpha != var1) {
         this.alpha = var1;
         this.invalidateSelf();
      }

   }

   public void setCheckable(boolean var1) {
      if (this.checkable != var1) {
         this.checkable = var1;
         float var2 = this.calculateChipIconWidth();
         if (!var1 && this.currentChecked) {
            this.currentChecked = false;
         }

         float var3 = this.calculateChipIconWidth();
         this.invalidateSelf();
         if (var2 != var3) {
            this.onSizeChange();
         }
      }

   }

   public void setCheckableResource(int var1) {
      this.setCheckable(this.context.getResources().getBoolean(var1));
   }

   public void setCheckedIcon(Drawable var1) {
      if (this.checkedIcon != var1) {
         float var2 = this.calculateChipIconWidth();
         this.checkedIcon = var1;
         float var3 = this.calculateChipIconWidth();
         this.unapplyChildDrawable(this.checkedIcon);
         this.applyChildDrawable(this.checkedIcon);
         this.invalidateSelf();
         if (var2 != var3) {
            this.onSizeChange();
         }
      }

   }

   @Deprecated
   public void setCheckedIconEnabled(boolean var1) {
      this.setCheckedIconVisible(var1);
   }

   @Deprecated
   public void setCheckedIconEnabledResource(int var1) {
      this.setCheckedIconVisible(this.context.getResources().getBoolean(var1));
   }

   public void setCheckedIconResource(int var1) {
      this.setCheckedIcon(AppCompatResources.getDrawable(this.context, var1));
   }

   public void setCheckedIconVisible(int var1) {
      this.setCheckedIconVisible(this.context.getResources().getBoolean(var1));
   }

   public void setCheckedIconVisible(boolean var1) {
      if (this.checkedIconVisible != var1) {
         boolean var3 = this.showsCheckedIcon();
         this.checkedIconVisible = var1;
         var1 = this.showsCheckedIcon();
         boolean var2;
         if (var3 != var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            if (var1) {
               this.applyChildDrawable(this.checkedIcon);
            } else {
               this.unapplyChildDrawable(this.checkedIcon);
            }

            this.invalidateSelf();
            this.onSizeChange();
         }
      }

   }

   public void setChipBackgroundColor(ColorStateList var1) {
      if (this.chipBackgroundColor != var1) {
         this.chipBackgroundColor = var1;
         this.onStateChange(this.getState());
      }

   }

   public void setChipBackgroundColorResource(int var1) {
      this.setChipBackgroundColor(AppCompatResources.getColorStateList(this.context, var1));
   }

   @Deprecated
   public void setChipCornerRadius(float var1) {
      if (this.chipCornerRadius != var1) {
         this.chipCornerRadius = var1;
         this.setShapeAppearanceModel(this.getShapeAppearanceModel().withCornerSize(var1));
      }

   }

   @Deprecated
   public void setChipCornerRadiusResource(int var1) {
      this.setChipCornerRadius(this.context.getResources().getDimension(var1));
   }

   public void setChipEndPadding(float var1) {
      if (this.chipEndPadding != var1) {
         this.chipEndPadding = var1;
         this.invalidateSelf();
         this.onSizeChange();
      }

   }

   public void setChipEndPaddingResource(int var1) {
      this.setChipEndPadding(this.context.getResources().getDimension(var1));
   }

   public void setChipIcon(Drawable var1) {
      Drawable var4 = this.getChipIcon();
      if (var4 != var1) {
         float var2 = this.calculateChipIconWidth();
         if (var1 != null) {
            var1 = DrawableCompat.wrap(var1).mutate();
         } else {
            var1 = null;
         }

         this.chipIcon = var1;
         float var3 = this.calculateChipIconWidth();
         this.unapplyChildDrawable(var4);
         if (this.showsChipIcon()) {
            this.applyChildDrawable(this.chipIcon);
         }

         this.invalidateSelf();
         if (var2 != var3) {
            this.onSizeChange();
         }
      }

   }

   @Deprecated
   public void setChipIconEnabled(boolean var1) {
      this.setChipIconVisible(var1);
   }

   @Deprecated
   public void setChipIconEnabledResource(int var1) {
      this.setChipIconVisible(var1);
   }

   public void setChipIconResource(int var1) {
      this.setChipIcon(AppCompatResources.getDrawable(this.context, var1));
   }

   public void setChipIconSize(float var1) {
      if (this.chipIconSize != var1) {
         float var2 = this.calculateChipIconWidth();
         this.chipIconSize = var1;
         var1 = this.calculateChipIconWidth();
         this.invalidateSelf();
         if (var2 != var1) {
            this.onSizeChange();
         }
      }

   }

   public void setChipIconSizeResource(int var1) {
      this.setChipIconSize(this.context.getResources().getDimension(var1));
   }

   public void setChipIconTint(ColorStateList var1) {
      this.hasChipIconTint = true;
      if (this.chipIconTint != var1) {
         this.chipIconTint = var1;
         if (this.showsChipIcon()) {
            DrawableCompat.setTintList(this.chipIcon, var1);
         }

         this.onStateChange(this.getState());
      }

   }

   public void setChipIconTintResource(int var1) {
      this.setChipIconTint(AppCompatResources.getColorStateList(this.context, var1));
   }

   public void setChipIconVisible(int var1) {
      this.setChipIconVisible(this.context.getResources().getBoolean(var1));
   }

   public void setChipIconVisible(boolean var1) {
      if (this.chipIconVisible != var1) {
         boolean var3 = this.showsChipIcon();
         this.chipIconVisible = var1;
         var1 = this.showsChipIcon();
         boolean var2;
         if (var3 != var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            if (var1) {
               this.applyChildDrawable(this.chipIcon);
            } else {
               this.unapplyChildDrawable(this.chipIcon);
            }

            this.invalidateSelf();
            this.onSizeChange();
         }
      }

   }

   public void setChipMinHeight(float var1) {
      if (this.chipMinHeight != var1) {
         this.chipMinHeight = var1;
         this.invalidateSelf();
         this.onSizeChange();
      }

   }

   public void setChipMinHeightResource(int var1) {
      this.setChipMinHeight(this.context.getResources().getDimension(var1));
   }

   public void setChipStartPadding(float var1) {
      if (this.chipStartPadding != var1) {
         this.chipStartPadding = var1;
         this.invalidateSelf();
         this.onSizeChange();
      }

   }

   public void setChipStartPaddingResource(int var1) {
      this.setChipStartPadding(this.context.getResources().getDimension(var1));
   }

   public void setChipStrokeColor(ColorStateList var1) {
      if (this.chipStrokeColor != var1) {
         this.chipStrokeColor = var1;
         if (this.isShapeThemingEnabled) {
            this.setStrokeColor(var1);
         }

         this.onStateChange(this.getState());
      }

   }

   public void setChipStrokeColorResource(int var1) {
      this.setChipStrokeColor(AppCompatResources.getColorStateList(this.context, var1));
   }

   public void setChipStrokeWidth(float var1) {
      if (this.chipStrokeWidth != var1) {
         this.chipStrokeWidth = var1;
         this.chipPaint.setStrokeWidth(var1);
         if (this.isShapeThemingEnabled) {
            super.setStrokeWidth(var1);
         }

         this.invalidateSelf();
      }

   }

   public void setChipStrokeWidthResource(int var1) {
      this.setChipStrokeWidth(this.context.getResources().getDimension(var1));
   }

   public void setCloseIcon(Drawable var1) {
      Drawable var4 = this.getCloseIcon();
      if (var4 != var1) {
         float var2 = this.calculateCloseIconWidth();
         if (var1 != null) {
            var1 = DrawableCompat.wrap(var1).mutate();
         } else {
            var1 = null;
         }

         this.closeIcon = var1;
         if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            this.updateFrameworkCloseIconRipple();
         }

         float var3 = this.calculateCloseIconWidth();
         this.unapplyChildDrawable(var4);
         if (this.showsCloseIcon()) {
            this.applyChildDrawable(this.closeIcon);
         }

         this.invalidateSelf();
         if (var2 != var3) {
            this.onSizeChange();
         }
      }

   }

   public void setCloseIconContentDescription(CharSequence var1) {
      if (this.closeIconContentDescription != var1) {
         this.closeIconContentDescription = BidiFormatter.getInstance().unicodeWrap(var1);
         this.invalidateSelf();
      }

   }

   @Deprecated
   public void setCloseIconEnabled(boolean var1) {
      this.setCloseIconVisible(var1);
   }

   @Deprecated
   public void setCloseIconEnabledResource(int var1) {
      this.setCloseIconVisible(var1);
   }

   public void setCloseIconEndPadding(float var1) {
      if (this.closeIconEndPadding != var1) {
         this.closeIconEndPadding = var1;
         this.invalidateSelf();
         if (this.showsCloseIcon()) {
            this.onSizeChange();
         }
      }

   }

   public void setCloseIconEndPaddingResource(int var1) {
      this.setCloseIconEndPadding(this.context.getResources().getDimension(var1));
   }

   public void setCloseIconResource(int var1) {
      this.setCloseIcon(AppCompatResources.getDrawable(this.context, var1));
   }

   public void setCloseIconSize(float var1) {
      if (this.closeIconSize != var1) {
         this.closeIconSize = var1;
         this.invalidateSelf();
         if (this.showsCloseIcon()) {
            this.onSizeChange();
         }
      }

   }

   public void setCloseIconSizeResource(int var1) {
      this.setCloseIconSize(this.context.getResources().getDimension(var1));
   }

   public void setCloseIconStartPadding(float var1) {
      if (this.closeIconStartPadding != var1) {
         this.closeIconStartPadding = var1;
         this.invalidateSelf();
         if (this.showsCloseIcon()) {
            this.onSizeChange();
         }
      }

   }

   public void setCloseIconStartPaddingResource(int var1) {
      this.setCloseIconStartPadding(this.context.getResources().getDimension(var1));
   }

   public boolean setCloseIconState(int[] var1) {
      if (!Arrays.equals(this.closeIconStateSet, var1)) {
         this.closeIconStateSet = var1;
         if (this.showsCloseIcon()) {
            return this.onStateChange(this.getState(), var1);
         }
      }

      return false;
   }

   public void setCloseIconTint(ColorStateList var1) {
      if (this.closeIconTint != var1) {
         this.closeIconTint = var1;
         if (this.showsCloseIcon()) {
            DrawableCompat.setTintList(this.closeIcon, var1);
         }

         this.onStateChange(this.getState());
      }

   }

   public void setCloseIconTintResource(int var1) {
      this.setCloseIconTint(AppCompatResources.getColorStateList(this.context, var1));
   }

   public void setCloseIconVisible(int var1) {
      this.setCloseIconVisible(this.context.getResources().getBoolean(var1));
   }

   public void setCloseIconVisible(boolean var1) {
      if (this.closeIconVisible != var1) {
         boolean var3 = this.showsCloseIcon();
         this.closeIconVisible = var1;
         var1 = this.showsCloseIcon();
         boolean var2;
         if (var3 != var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            if (var1) {
               this.applyChildDrawable(this.closeIcon);
            } else {
               this.unapplyChildDrawable(this.closeIcon);
            }

            this.invalidateSelf();
            this.onSizeChange();
         }
      }

   }

   public void setColorFilter(ColorFilter var1) {
      if (this.colorFilter != var1) {
         this.colorFilter = var1;
         this.invalidateSelf();
      }

   }

   public void setDelegate(ChipDrawable.Delegate var1) {
      this.delegate = new WeakReference(var1);
   }

   public void setEllipsize(TruncateAt var1) {
      this.truncateAt = var1;
   }

   public void setHideMotionSpec(MotionSpec var1) {
      this.hideMotionSpec = var1;
   }

   public void setHideMotionSpecResource(int var1) {
      this.setHideMotionSpec(MotionSpec.createFromResource(this.context, var1));
   }

   public void setIconEndPadding(float var1) {
      if (this.iconEndPadding != var1) {
         float var2 = this.calculateChipIconWidth();
         this.iconEndPadding = var1;
         var1 = this.calculateChipIconWidth();
         this.invalidateSelf();
         if (var2 != var1) {
            this.onSizeChange();
         }
      }

   }

   public void setIconEndPaddingResource(int var1) {
      this.setIconEndPadding(this.context.getResources().getDimension(var1));
   }

   public void setIconStartPadding(float var1) {
      if (this.iconStartPadding != var1) {
         float var2 = this.calculateChipIconWidth();
         this.iconStartPadding = var1;
         var1 = this.calculateChipIconWidth();
         this.invalidateSelf();
         if (var2 != var1) {
            this.onSizeChange();
         }
      }

   }

   public void setIconStartPaddingResource(int var1) {
      this.setIconStartPadding(this.context.getResources().getDimension(var1));
   }

   public void setMaxWidth(int var1) {
      this.maxWidth = var1;
   }

   public void setRippleColor(ColorStateList var1) {
      if (this.rippleColor != var1) {
         this.rippleColor = var1;
         this.updateCompatRippleColor();
         this.onStateChange(this.getState());
      }

   }

   public void setRippleColorResource(int var1) {
      this.setRippleColor(AppCompatResources.getColorStateList(this.context, var1));
   }

   void setShouldDrawText(boolean var1) {
      this.shouldDrawText = var1;
   }

   public void setShowMotionSpec(MotionSpec var1) {
      this.showMotionSpec = var1;
   }

   public void setShowMotionSpecResource(int var1) {
      this.setShowMotionSpec(MotionSpec.createFromResource(this.context, var1));
   }

   public void setText(CharSequence var1) {
      Object var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      if (!TextUtils.equals(this.text, (CharSequence)var2)) {
         this.text = (CharSequence)var2;
         this.textDrawableHelper.setTextWidthDirty(true);
         this.invalidateSelf();
         this.onSizeChange();
      }

   }

   public void setTextAppearance(TextAppearance var1) {
      this.textDrawableHelper.setTextAppearance(var1, this.context);
   }

   public void setTextAppearanceResource(int var1) {
      this.setTextAppearance(new TextAppearance(this.context, var1));
   }

   public void setTextEndPadding(float var1) {
      if (this.textEndPadding != var1) {
         this.textEndPadding = var1;
         this.invalidateSelf();
         this.onSizeChange();
      }

   }

   public void setTextEndPaddingResource(int var1) {
      this.setTextEndPadding(this.context.getResources().getDimension(var1));
   }

   public void setTextResource(int var1) {
      this.setText(this.context.getResources().getString(var1));
   }

   public void setTextStartPadding(float var1) {
      if (this.textStartPadding != var1) {
         this.textStartPadding = var1;
         this.invalidateSelf();
         this.onSizeChange();
      }

   }

   public void setTextStartPaddingResource(int var1) {
      this.setTextStartPadding(this.context.getResources().getDimension(var1));
   }

   public void setTintList(ColorStateList var1) {
      if (this.tint != var1) {
         this.tint = var1;
         this.onStateChange(this.getState());
      }

   }

   public void setTintMode(Mode var1) {
      if (this.tintMode != var1) {
         this.tintMode = var1;
         this.tintFilter = DrawableUtils.updateTintFilter(this, this.tint, var1);
         this.invalidateSelf();
      }

   }

   public void setUseCompatRipple(boolean var1) {
      if (this.useCompatRipple != var1) {
         this.useCompatRipple = var1;
         this.updateCompatRippleColor();
         this.onStateChange(this.getState());
      }

   }

   public boolean setVisible(boolean var1, boolean var2) {
      boolean var4 = super.setVisible(var1, var2);
      boolean var3 = var4;
      if (this.showsChipIcon()) {
         var3 = var4 | this.chipIcon.setVisible(var1, var2);
      }

      var4 = var3;
      if (this.showsCheckedIcon()) {
         var4 = var3 | this.checkedIcon.setVisible(var1, var2);
      }

      var3 = var4;
      if (this.showsCloseIcon()) {
         var3 = var4 | this.closeIcon.setVisible(var1, var2);
      }

      if (var3) {
         this.invalidateSelf();
      }

      return var3;
   }

   boolean shouldDrawText() {
      return this.shouldDrawText;
   }

   public void unscheduleDrawable(Drawable var1, Runnable var2) {
      Callback var3 = this.getCallback();
      if (var3 != null) {
         var3.unscheduleDrawable(this, var2);
      }

   }

   public interface Delegate {
      void onChipDrawableSizeChange();
   }
}
