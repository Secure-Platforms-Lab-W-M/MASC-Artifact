/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
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
 *  android.view.animation.Interpolator
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.AnimationUtils;
import android.support.v4.math.MathUtils;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
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
    private float mCollapsedTextSize = 15.0f;
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
    private float mExpandedTextSize = 15.0f;
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
        block0 : {
            boolean bl = Build.VERSION.SDK_INT < 18;
            USE_SCALING_TEXTURE = bl;
            DEBUG_DRAW_PAINT = null;
            Paint paint = DEBUG_DRAW_PAINT;
            if (paint == null) break block0;
            paint.setAntiAlias(true);
            DEBUG_DRAW_PAINT.setColor(-65281);
        }
    }

    public CollapsingTextHelper(View view) {
        this.mView = view;
        this.mTextPaint = new TextPaint(129);
        this.mCollapsedBounds = new Rect();
        this.mExpandedBounds = new Rect();
        this.mCurrentBounds = new RectF();
    }

    private boolean areTypefacesDifferent(Typeface typeface, Typeface typeface2) {
        block3 : {
            block2 : {
                if (typeface != null && !typeface.equals((Object)typeface2)) break block2;
                if (typeface != null || typeface2 == null) break block3;
            }
            return true;
        }
        return false;
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
        this.calculateOffsets(this.mExpandedFraction);
    }

    private boolean calculateIsRtl(CharSequence charSequence) {
        int n = ViewCompat.getLayoutDirection(this.mView);
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        TextDirectionHeuristicCompat textDirectionHeuristicCompat = bl ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        return textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
    }

    private void calculateOffsets(float f) {
        this.interpolateBounds(f);
        this.mCurrentDrawX = CollapsingTextHelper.lerp(this.mExpandedDrawX, this.mCollapsedDrawX, f, this.mPositionInterpolator);
        this.mCurrentDrawY = CollapsingTextHelper.lerp(this.mExpandedDrawY, this.mCollapsedDrawY, f, this.mPositionInterpolator);
        this.setInterpolatedTextSize(CollapsingTextHelper.lerp(this.mExpandedTextSize, this.mCollapsedTextSize, f, this.mTextSizeInterpolator));
        if (this.mCollapsedTextColor != this.mExpandedTextColor) {
            this.mTextPaint.setColor(CollapsingTextHelper.blendColors(this.getCurrentExpandedTextColor(), this.getCurrentCollapsedTextColor(), f));
        } else {
            this.mTextPaint.setColor(this.getCurrentCollapsedTextColor());
        }
        this.mTextPaint.setShadowLayer(CollapsingTextHelper.lerp(this.mExpandedShadowRadius, this.mCollapsedShadowRadius, f, null), CollapsingTextHelper.lerp(this.mExpandedShadowDx, this.mCollapsedShadowDx, f, null), CollapsingTextHelper.lerp(this.mExpandedShadowDy, this.mCollapsedShadowDy, f, null), CollapsingTextHelper.blendColors(this.mExpandedShadowColor, this.mCollapsedShadowColor, f));
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }

    private void calculateUsingTextSize(float f) {
        float f2;
        if (this.mText == null) {
            return;
        }
        float f3 = this.mCollapsedBounds.width();
        float f4 = this.mExpandedBounds.width();
        boolean bl = false;
        boolean bl2 = false;
        if (CollapsingTextHelper.isClose(f, this.mCollapsedTextSize)) {
            f2 = this.mCollapsedTextSize;
            this.mScale = 1.0f;
            if (this.areTypefacesDifferent(this.mCurrentTypeface, this.mCollapsedTypeface)) {
                this.mCurrentTypeface = this.mCollapsedTypeface;
                bl2 = true;
            }
            f = f3;
        } else {
            f2 = this.mExpandedTextSize;
            if (this.areTypefacesDifferent(this.mCurrentTypeface, this.mExpandedTypeface)) {
                this.mCurrentTypeface = this.mExpandedTypeface;
                bl2 = true;
            } else {
                bl2 = bl;
            }
            this.mScale = CollapsingTextHelper.isClose(f, this.mExpandedTextSize) ? 1.0f : f / this.mExpandedTextSize;
            f = this.mCollapsedTextSize / this.mExpandedTextSize;
            f = f4 * f > f3 ? Math.min(f3 / f, f4) : f4;
        }
        boolean bl3 = true;
        if (f > 0.0f) {
            bl2 = this.mCurrentTextSize != f2 || this.mBoundsChanged || bl2;
            this.mCurrentTextSize = f2;
            this.mBoundsChanged = false;
        }
        if (this.mTextToDraw != null && !bl2) {
            return;
        }
        this.mTextPaint.setTextSize(this.mCurrentTextSize);
        this.mTextPaint.setTypeface(this.mCurrentTypeface);
        Object object = this.mTextPaint;
        if (this.mScale == 1.0f) {
            bl3 = false;
        }
        object.setLinearText(bl3);
        object = TextUtils.ellipsize((CharSequence)this.mText, (TextPaint)this.mTextPaint, (float)f, (TextUtils.TruncateAt)TextUtils.TruncateAt.END);
        if (!TextUtils.equals((CharSequence)object, (CharSequence)this.mTextToDraw)) {
            this.mTextToDraw = object;
            this.mIsRtl = this.calculateIsRtl(this.mTextToDraw);
            return;
        }
    }

    private void clearTexture() {
        Bitmap bitmap = this.mExpandedTitleTexture;
        if (bitmap != null) {
            bitmap.recycle();
            this.mExpandedTitleTexture = null;
            return;
        }
    }

    private void ensureExpandedTexture() {
        if (this.mExpandedTitleTexture == null && !this.mExpandedBounds.isEmpty()) {
            if (TextUtils.isEmpty((CharSequence)this.mTextToDraw)) {
                return;
            }
            this.calculateOffsets(0.0f);
            this.mTextureAscent = this.mTextPaint.ascent();
            this.mTextureDescent = this.mTextPaint.descent();
            TextPaint textPaint = this.mTextPaint;
            CharSequence charSequence = this.mTextToDraw;
            int n = Math.round(textPaint.measureText(charSequence, 0, charSequence.length()));
            int n2 = Math.round(this.mTextureDescent - this.mTextureAscent);
            if (n > 0) {
                if (n2 <= 0) {
                    return;
                }
                this.mExpandedTitleTexture = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
                textPaint = new Canvas(this.mExpandedTitleTexture);
                charSequence = this.mTextToDraw;
                textPaint.drawText(charSequence, 0, charSequence.length(), 0.0f, (float)n2 - this.mTextPaint.descent(), (Paint)this.mTextPaint);
                if (this.mTexturePaint == null) {
                    this.mTexturePaint = new Paint(3);
                    return;
                }
                return;
            }
            return;
        }
    }

    @ColorInt
    private int getCurrentCollapsedTextColor() {
        int[] arrn = this.mState;
        if (arrn != null) {
            return this.mCollapsedTextColor.getColorForState(arrn, 0);
        }
        return this.mCollapsedTextColor.getDefaultColor();
    }

    @ColorInt
    private int getCurrentExpandedTextColor() {
        int[] arrn = this.mState;
        if (arrn != null) {
            return this.mExpandedTextColor.getColorForState(arrn, 0);
        }
        return this.mExpandedTextColor.getDefaultColor();
    }

    private void interpolateBounds(float f) {
        this.mCurrentBounds.left = CollapsingTextHelper.lerp(this.mExpandedBounds.left, this.mCollapsedBounds.left, f, this.mPositionInterpolator);
        this.mCurrentBounds.top = CollapsingTextHelper.lerp(this.mExpandedDrawY, this.mCollapsedDrawY, f, this.mPositionInterpolator);
        this.mCurrentBounds.right = CollapsingTextHelper.lerp(this.mExpandedBounds.right, this.mCollapsedBounds.right, f, this.mPositionInterpolator);
        this.mCurrentBounds.bottom = CollapsingTextHelper.lerp(this.mExpandedBounds.bottom, this.mCollapsedBounds.bottom, f, this.mPositionInterpolator);
    }

    private static boolean isClose(float f, float f2) {
        if (Math.abs(f - f2) < 0.001f) {
            return true;
        }
        return false;
    }

    private static float lerp(float f, float f2, float f3, Interpolator interpolator) {
        if (interpolator != null) {
            f3 = interpolator.getInterpolation(f3);
        }
        return AnimationUtils.lerp(f, f2, f3);
    }

    private Typeface readFontFamilyTypeface(int n) {
        TypedArray typedArray;
        block4 : {
            typedArray = this.mView.getContext().obtainStyledAttributes(n, new int[]{16843692});
            String string2 = typedArray.getString(0);
            if (string2 == null) break block4;
            string2 = Typeface.create((String)string2, (int)0);
            return string2;
        }
        typedArray.recycle();
        return null;
        finally {
            typedArray.recycle();
        }
    }

    private static boolean rectEquals(Rect rect, int n, int n2, int n3, int n4) {
        if (rect.left == n && rect.top == n2 && rect.right == n3 && rect.bottom == n4) {
            return true;
        }
        return false;
    }

    private void setInterpolatedTextSize(float f) {
        this.calculateUsingTextSize(f);
        boolean bl = USE_SCALING_TEXTURE && this.mScale != 1.0f;
        this.mUseTexture = bl;
        if (this.mUseTexture) {
            this.ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.mView);
    }

    public void draw(Canvas canvas) {
        int n = canvas.save();
        if (this.mTextToDraw != null && this.mDrawTitle) {
            float f;
            float f2 = this.mCurrentDrawX;
            float f3 = this.mCurrentDrawY;
            boolean bl = this.mUseTexture && this.mExpandedTitleTexture != null;
            if (bl) {
                f = this.mTextureAscent;
                float f4 = this.mScale;
                float f5 = this.mTextureDescent;
                f *= f4;
            } else {
                f = this.mTextPaint.ascent();
                float f6 = this.mScale;
                this.mTextPaint.descent();
                float f7 = this.mScale;
                f *= f6;
            }
            f = bl ? f3 + f : f3;
            f3 = this.mScale;
            if (f3 != 1.0f) {
                canvas.scale(f3, f3, f2, f);
            }
            if (bl) {
                canvas.drawBitmap(this.mExpandedTitleTexture, f2, f, this.mTexturePaint);
            } else {
                CharSequence charSequence = this.mTextToDraw;
                canvas.drawText(charSequence, 0, charSequence.length(), f2, f, (Paint)this.mTextPaint);
            }
        }
        canvas.restoreToCount(n);
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
        Typeface typeface = this.mCollapsedTypeface;
        if (typeface != null) {
            return typeface;
        }
        return Typeface.DEFAULT;
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
        Typeface typeface = this.mExpandedTypeface;
        if (typeface != null) {
            return typeface;
        }
        return Typeface.DEFAULT;
    }

    float getExpansionFraction() {
        return this.mExpandedFraction;
    }

    CharSequence getText() {
        return this.mText;
    }

    final boolean isStateful() {
        block3 : {
            block2 : {
                ColorStateList colorStateList = this.mCollapsedTextColor;
                if (colorStateList != null && colorStateList.isStateful()) break block2;
                colorStateList = this.mExpandedTextColor;
                if (colorStateList == null || !colorStateList.isStateful()) break block3;
            }
            return true;
        }
        return false;
    }

    void onBoundsChanged() {
        boolean bl = this.mCollapsedBounds.width() > 0 && this.mCollapsedBounds.height() > 0 && this.mExpandedBounds.width() > 0 && this.mExpandedBounds.height() > 0;
        this.mDrawTitle = bl;
    }

    public void recalculate() {
        if (this.mView.getHeight() > 0 && this.mView.getWidth() > 0) {
            this.calculateBaseOffsets();
            this.calculateCurrentOffsets();
            return;
        }
    }

    void setCollapsedBounds(int n, int n2, int n3, int n4) {
        if (!CollapsingTextHelper.rectEquals(this.mCollapsedBounds, n, n2, n3, n4)) {
            this.mCollapsedBounds.set(n, n2, n3, n4);
            this.mBoundsChanged = true;
            this.onBoundsChanged();
            return;
        }
    }

    void setCollapsedTextAppearance(int n) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), n, R.styleable.TextAppearance);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.mCollapsedTextColor = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.mCollapsedTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mCollapsedTextSize);
        }
        this.mCollapsedShadowColor = tintTypedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.mCollapsedShadowDx = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.mCollapsedShadowDy = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.mCollapsedShadowRadius = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        tintTypedArray.recycle();
        if (Build.VERSION.SDK_INT >= 16) {
            this.mCollapsedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }

    void setCollapsedTextColor(ColorStateList colorStateList) {
        if (this.mCollapsedTextColor != colorStateList) {
            this.mCollapsedTextColor = colorStateList;
            this.recalculate();
            return;
        }
    }

    void setCollapsedTextGravity(int n) {
        if (this.mCollapsedTextGravity != n) {
            this.mCollapsedTextGravity = n;
            this.recalculate();
            return;
        }
    }

    void setCollapsedTextSize(float f) {
        if (this.mCollapsedTextSize != f) {
            this.mCollapsedTextSize = f;
            this.recalculate();
            return;
        }
    }

    void setCollapsedTypeface(Typeface typeface) {
        if (this.areTypefacesDifferent(this.mCollapsedTypeface, typeface)) {
            this.mCollapsedTypeface = typeface;
            this.recalculate();
            return;
        }
    }

    void setExpandedBounds(int n, int n2, int n3, int n4) {
        if (!CollapsingTextHelper.rectEquals(this.mExpandedBounds, n, n2, n3, n4)) {
            this.mExpandedBounds.set(n, n2, n3, n4);
            this.mBoundsChanged = true;
            this.onBoundsChanged();
            return;
        }
    }

    void setExpandedTextAppearance(int n) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), n, R.styleable.TextAppearance);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
            this.mExpandedTextColor = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize)) {
            this.mExpandedTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, (int)this.mExpandedTextSize);
        }
        this.mExpandedShadowColor = tintTypedArray.getInt(R.styleable.TextAppearance_android_shadowColor, 0);
        this.mExpandedShadowDx = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.mExpandedShadowDy = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.mExpandedShadowRadius = tintTypedArray.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        tintTypedArray.recycle();
        if (Build.VERSION.SDK_INT >= 16) {
            this.mExpandedTypeface = this.readFontFamilyTypeface(n);
        }
        this.recalculate();
    }

    void setExpandedTextColor(ColorStateList colorStateList) {
        if (this.mExpandedTextColor != colorStateList) {
            this.mExpandedTextColor = colorStateList;
            this.recalculate();
            return;
        }
    }

    void setExpandedTextGravity(int n) {
        if (this.mExpandedTextGravity != n) {
            this.mExpandedTextGravity = n;
            this.recalculate();
            return;
        }
    }

    void setExpandedTextSize(float f) {
        if (this.mExpandedTextSize != f) {
            this.mExpandedTextSize = f;
            this.recalculate();
            return;
        }
    }

    void setExpandedTypeface(Typeface typeface) {
        if (this.areTypefacesDifferent(this.mExpandedTypeface, typeface)) {
            this.mExpandedTypeface = typeface;
            this.recalculate();
            return;
        }
    }

    void setExpansionFraction(float f) {
        if ((f = MathUtils.clamp(f, 0.0f, 1.0f)) != this.mExpandedFraction) {
            this.mExpandedFraction = f;
            this.calculateCurrentOffsets();
            return;
        }
    }

    void setPositionInterpolator(Interpolator interpolator) {
        this.mPositionInterpolator = interpolator;
        this.recalculate();
    }

    final boolean setState(int[] arrn) {
        this.mState = arrn;
        if (this.isStateful()) {
            this.recalculate();
            return true;
        }
        return false;
    }

    void setText(CharSequence charSequence) {
        if (charSequence != null && charSequence.equals(this.mText)) {
            return;
        }
        this.mText = charSequence;
        this.mTextToDraw = null;
        this.clearTexture();
        this.recalculate();
    }

    void setTextSizeInterpolator(Interpolator interpolator) {
        this.mTextSizeInterpolator = interpolator;
        this.recalculate();
    }

    void setTypefaces(Typeface typeface) {
        this.mExpandedTypeface = typeface;
        this.mCollapsedTypeface = typeface;
        this.recalculate();
    }
}

