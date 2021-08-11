/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 *  android.text.method.PasswordTransformationMethod
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.widget.TextView
 *  androidx.appcompat.R
 *  androidx.appcompat.R$styleable
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatTextViewAutoSizeHelper;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintInfo;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;
import java.util.Locale;

class AppCompatTextHelper {
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int TEXT_FONT_WEIGHT_UNSPECIFIED = -1;
    private boolean mAsyncFontPending;
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableStartTint;
    private TintInfo mDrawableTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mFontWeight = -1;
    private int mStyle = 0;
    private final TextView mView;

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(textView);
    }

    private void applyCompoundDrawableTint(Drawable drawable2, TintInfo tintInfo) {
        if (drawable2 != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
        }
    }

    private static TintInfo createTintInfo(Context context, AppCompatDrawableManager object, int n) {
        if ((context = object.getTintList(context, n)) != null) {
            object = new TintInfo();
            object.mHasTintList = true;
            object.mTintList = context;
            return object;
        }
        return null;
    }

    private void setCompoundDrawables(Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, Drawable drawable6, Drawable drawable7) {
        block13 : {
            block12 : {
                block11 : {
                    if (Build.VERSION.SDK_INT < 17 || drawable6 == null && drawable7 == null) break block11;
                    Drawable[] arrdrawable = this.mView.getCompoundDrawablesRelative();
                    TextView textView = this.mView;
                    drawable2 = drawable6 != null ? drawable6 : arrdrawable[0];
                    if (drawable3 == null) {
                        drawable3 = arrdrawable[1];
                    }
                    drawable4 = drawable7 != null ? drawable7 : arrdrawable[2];
                    if (drawable5 == null) {
                        drawable5 = arrdrawable[3];
                    }
                    textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
                    break block12;
                }
                if (drawable2 != null || drawable3 != null || drawable4 != null || drawable5 != null) break block13;
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 17 && ((drawable7 = this.mView.getCompoundDrawablesRelative())[0] != null || drawable7[2] != null)) {
            drawable4 = this.mView;
            drawable6 = drawable7[0];
            drawable2 = drawable3 != null ? drawable3 : drawable7[1];
            drawable3 = drawable7[2];
            if (drawable5 == null) {
                drawable5 = drawable7[3];
            }
            drawable4.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable6, drawable2, drawable3, drawable5);
            return;
        }
        drawable7 = this.mView.getCompoundDrawables();
        drawable6 = this.mView;
        if (drawable2 == null) {
            drawable2 = drawable7[0];
        }
        if (drawable3 == null) {
            drawable3 = drawable7[1];
        }
        if (drawable4 == null) {
            drawable4 = drawable7[2];
        }
        if (drawable5 == null) {
            drawable5 = drawable7[3];
        }
        drawable6.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
    }

    private void setCompoundTints() {
        TintInfo tintInfo;
        this.mDrawableLeftTint = tintInfo = this.mDrawableTint;
        this.mDrawableTopTint = tintInfo;
        this.mDrawableRightTint = tintInfo;
        this.mDrawableBottomTint = tintInfo;
        this.mDrawableStartTint = tintInfo;
        this.mDrawableEndTint = tintInfo;
    }

    private void setTextSizeInternal(int n, float f) {
        this.mAutoSizeTextHelper.setTextSizeInternal(n, f);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void updateTypefaceAndStyle(Context object, TintTypedArray tintTypedArray) {
        boolean bl;
        this.mStyle = tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
        int n = Build.VERSION.SDK_INT;
        boolean bl2 = false;
        if (n >= 28) {
            this.mFontWeight = n = tintTypedArray.getInt(R.styleable.TextAppearance_android_textFontWeight, -1);
            if (n != -1) {
                this.mStyle = this.mStyle & 2 | 0;
            }
        }
        if (!tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily) && !tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_typeface)) {
                this.mAsyncFontPending = false;
                n = tintTypedArray.getInt(R.styleable.TextAppearance_android_typeface, 1);
                if (n != 1) {
                    if (n == 2) {
                        this.mFontTypeface = Typeface.SERIF;
                        return;
                    }
                    if (n != 3) {
                        return;
                    }
                    this.mFontTypeface = Typeface.MONOSPACE;
                    return;
                }
                this.mFontTypeface = Typeface.SANS_SERIF;
            }
            return;
        }
        this.mFontTypeface = null;
        n = tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily) ? R.styleable.TextAppearance_fontFamily : R.styleable.TextAppearance_android_fontFamily;
        int n2 = this.mFontWeight;
        int n3 = this.mStyle;
        if (!object.isRestricted()) {
            object = new ApplyTextViewCallback(this, n2, n3);
            try {
                object = tintTypedArray.getFont(n, this.mStyle, (ResourcesCompat.FontCallback)object);
                if (object != null) {
                    if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
                        object = Typeface.create((Typeface)object, (int)0);
                        n2 = this.mFontWeight;
                        bl = (this.mStyle & 2) != 0;
                        this.mFontTypeface = Typeface.create((Typeface)object, (int)n2, (boolean)bl);
                    } else {
                        this.mFontTypeface = object;
                    }
                }
                bl = this.mFontTypeface == null;
                this.mAsyncFontPending = bl;
            }
            catch (Resources.NotFoundException notFoundException) {
            }
            catch (UnsupportedOperationException unsupportedOperationException) {
                // empty catch block
            }
        }
        if (this.mFontTypeface == null && (object = tintTypedArray.getString(n)) != null) {
            if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
                object = Typeface.create((String)object, (int)0);
                n = this.mFontWeight;
                bl = bl2;
                if ((2 & this.mStyle) != 0) {
                    bl = true;
                }
                this.mFontTypeface = Typeface.create((Typeface)object, (int)n, (boolean)bl);
                return;
            }
            this.mFontTypeface = Typeface.create((String)object, (int)this.mStyle);
        }
    }

    void applyCompoundDrawablesTints() {
        Drawable[] arrdrawable;
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            arrdrawable = this.mView.getCompoundDrawables();
            this.applyCompoundDrawableTint(arrdrawable[0], this.mDrawableLeftTint);
            this.applyCompoundDrawableTint(arrdrawable[1], this.mDrawableTopTint);
            this.applyCompoundDrawableTint(arrdrawable[2], this.mDrawableRightTint);
            this.applyCompoundDrawableTint(arrdrawable[3], this.mDrawableBottomTint);
        }
        if (Build.VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
            arrdrawable = this.mView.getCompoundDrawablesRelative();
            this.applyCompoundDrawableTint(arrdrawable[0], this.mDrawableStartTint);
            this.applyCompoundDrawableTint(arrdrawable[2], this.mDrawableEndTint);
        }
    }

    void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }

    int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    ColorStateList getCompoundDrawableTintList() {
        TintInfo tintInfo = this.mDrawableTint;
        if (tintInfo != null) {
            return tintInfo.mTintList;
        }
        return null;
    }

    PorterDuff.Mode getCompoundDrawableTintMode() {
        TintInfo tintInfo = this.mDrawableTint;
        if (tintInfo != null) {
            return tintInfo.mTintMode;
        }
        return null;
    }

    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    void loadFromAttributes(AttributeSet attributeSet, int n) {
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        Drawable drawable2 = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.AppCompatTextHelper, n, 0);
        int n2 = drawable2.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (drawable2.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, drawable2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (drawable2.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, drawable2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (drawable2.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, drawable2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (drawable2.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, drawable2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        if (Build.VERSION.SDK_INT >= 17) {
            if (drawable2.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
                this.mDrawableStartTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, drawable2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
            }
            if (drawable2.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
                this.mDrawableEndTint = AppCompatTextHelper.createTintInfo(context, appCompatDrawableManager, drawable2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
            }
        }
        drawable2.recycle();
        boolean bl = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean bl2 = false;
        boolean bl3 = false;
        int n3 = 0;
        int n4 = 0;
        ColorStateList colorStateList = null;
        Object object = null;
        String string = null;
        ColorStateList colorStateList2 = null;
        Object object2 = null;
        Object var17_17 = null;
        drawable2 = null;
        Object var16_18 = null;
        String string2 = null;
        Object object3 = null;
        Object object4 = null;
        TintTypedArray tintTypedArray = null;
        if (n2 != -1) {
            TintTypedArray tintTypedArray2 = TintTypedArray.obtainStyledAttributes(context, n2, R.styleable.TextAppearance);
            bl2 = bl3;
            n3 = n4;
            if (!bl) {
                bl2 = bl3;
                n3 = n4;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                    n3 = 1;
                    bl2 = tintTypedArray2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                }
            }
            this.updateTypefaceAndStyle(context, tintTypedArray2);
            object = colorStateList;
            object2 = colorStateList2;
            drawable2 = var16_18;
            if (Build.VERSION.SDK_INT < 23) {
                string2 = string;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColor)) {
                    string2 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColor);
                }
                object4 = var17_17;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                    object4 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                }
                object = string2;
                object2 = object4;
                drawable2 = var16_18;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                    drawable2 = tintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                    object2 = object4;
                    object = string2;
                }
            }
            object4 = tintTypedArray;
            if (tintTypedArray2.hasValue(R.styleable.TextAppearance_textLocale)) {
                object4 = tintTypedArray2.getString(R.styleable.TextAppearance_textLocale);
            }
            string2 = object3;
            if (Build.VERSION.SDK_INT >= 26) {
                string2 = object3;
                if (tintTypedArray2.hasValue(R.styleable.TextAppearance_fontVariationSettings)) {
                    string2 = tintTypedArray2.getString(R.styleable.TextAppearance_fontVariationSettings);
                }
            }
            tintTypedArray2.recycle();
        }
        tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.TextAppearance, n, 0);
        if (!bl && tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            bl2 = tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            n3 = 1;
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor)) {
                object = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                object2 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                drawable2 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
            }
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_textLocale)) {
            object4 = tintTypedArray.getString(R.styleable.TextAppearance_textLocale);
        }
        object3 = string2;
        if (Build.VERSION.SDK_INT >= 26) {
            object3 = string2;
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_fontVariationSettings)) {
                object3 = tintTypedArray.getString(R.styleable.TextAppearance_fontVariationSettings);
            }
        }
        if (Build.VERSION.SDK_INT >= 28 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle(context, tintTypedArray);
        tintTypedArray.recycle();
        if (object != null) {
            this.mView.setTextColor((ColorStateList)object);
        }
        if (object2 != null) {
            this.mView.setHintTextColor((ColorStateList)object2);
        }
        if (drawable2 != null) {
            this.mView.setLinkTextColor((ColorStateList)drawable2);
        }
        if (!bl && n3 != 0) {
            this.setAllCaps(bl2);
        }
        if ((drawable2 = this.mFontTypeface) != null) {
            if (this.mFontWeight == -1) {
                this.mView.setTypeface((Typeface)drawable2, this.mStyle);
            } else {
                this.mView.setTypeface((Typeface)drawable2);
            }
        }
        if (object3 != null) {
            this.mView.setFontVariationSettings((String)object3);
        }
        if (object4 != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                this.mView.setTextLocales(LocaleList.forLanguageTags((String)object4));
            } else if (Build.VERSION.SDK_INT >= 21) {
                drawable2 = object4.substring(0, object4.indexOf(44));
                this.mView.setTextLocale(Locale.forLanguageTag((String)drawable2));
            }
        }
        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, n);
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0 && (drawable2 = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes()).length > 0) {
            if ((float)this.mView.getAutoSizeStepGranularity() != -1.0f) {
                this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
                this.mView.setAutoSizeTextTypeUniformWithPresetSizes((int[])drawable2, 0);
            }
        }
        object3 = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.AppCompatTextView);
        object4 = null;
        attributeSet = null;
        n = R.styleable.AppCompatTextView_drawableLeftCompat;
        drawable2 = null;
        if ((n = object3.getResourceId(n, -1)) != -1) {
            attributeSet = appCompatDrawableManager.getDrawable(context, n);
        }
        if ((n = object3.getResourceId(R.styleable.AppCompatTextView_drawableTopCompat, -1)) != -1) {
            drawable2 = appCompatDrawableManager.getDrawable(context, n);
        }
        object2 = (n = object3.getResourceId(R.styleable.AppCompatTextView_drawableRightCompat, -1)) != -1 ? appCompatDrawableManager.getDrawable(context, n) : null;
        n = object3.getResourceId(R.styleable.AppCompatTextView_drawableBottomCompat, -1);
        object = n != -1 ? appCompatDrawableManager.getDrawable(context, n) : null;
        n = object3.getResourceId(R.styleable.AppCompatTextView_drawableStartCompat, -1);
        string2 = n != -1 ? appCompatDrawableManager.getDrawable(context, n) : null;
        n = object3.getResourceId(R.styleable.AppCompatTextView_drawableEndCompat, -1);
        if (n != -1) {
            object4 = appCompatDrawableManager.getDrawable(context, n);
        }
        this.setCompoundDrawables((Drawable)attributeSet, drawable2, (Drawable)object2, (Drawable)object, (Drawable)string2, (Drawable)object4);
        if (object3.hasValue(R.styleable.AppCompatTextView_drawableTint)) {
            attributeSet = object3.getColorStateList(R.styleable.AppCompatTextView_drawableTint);
            TextViewCompat.setCompoundDrawableTintList(this.mView, (ColorStateList)attributeSet);
        }
        if (object3.hasValue(R.styleable.AppCompatTextView_drawableTintMode)) {
            attributeSet = DrawableUtils.parseTintMode(object3.getInt(R.styleable.AppCompatTextView_drawableTintMode, -1), null);
            TextViewCompat.setCompoundDrawableTintMode(this.mView, (PorterDuff.Mode)attributeSet);
        }
        n = object3.getDimensionPixelSize(R.styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
        n3 = object3.getDimensionPixelSize(R.styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
        n4 = object3.getDimensionPixelSize(R.styleable.AppCompatTextView_lineHeight, -1);
        object3.recycle();
        if (n != -1) {
            TextViewCompat.setFirstBaselineToTopHeight(this.mView, n);
        }
        if (n3 != -1) {
            TextViewCompat.setLastBaselineToBottomHeight(this.mView, n3);
        }
        if (n4 != -1) {
            TextViewCompat.setLineHeight(this.mView, n4);
        }
    }

    void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            this.autoSizeText();
        }
    }

    void onSetCompoundDrawables() {
        this.applyCompoundDrawablesTints();
    }

    void onSetTextAppearance(Context object, int n) {
        ColorStateList colorStateList;
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes((Context)object, n, R.styleable.TextAppearance);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build.VERSION.SDK_INT < 23 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor) && (colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor)) != null) {
            this.mView.setTextColor(colorStateList);
        }
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textSize) && tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle((Context)object, tintTypedArray);
        if (Build.VERSION.SDK_INT >= 26 && tintTypedArray.hasValue(R.styleable.TextAppearance_fontVariationSettings) && (object = tintTypedArray.getString(R.styleable.TextAppearance_fontVariationSettings)) != null) {
            this.mView.setFontVariationSettings((String)object);
        }
        tintTypedArray.recycle();
        object = this.mFontTypeface;
        if (object != null) {
            this.mView.setTypeface((Typeface)object, this.mStyle);
        }
    }

    public void runOnUiThread(Runnable runnable) {
        this.mView.post(runnable);
    }

    void setAllCaps(boolean bl) {
        this.mView.setAllCaps(bl);
    }

    void setAutoSizeTextTypeUniformWithConfiguration(int n, int n2, int n3, int n4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
    }

    void setAutoSizeTextTypeUniformWithPresetSizes(int[] arrn, int n) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(arrn, n);
    }

    void setAutoSizeTextTypeWithDefaults(int n) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(n);
    }

    void setCompoundDrawableTintList(ColorStateList colorStateList) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        this.mDrawableTint.mTintList = colorStateList;
        TintInfo tintInfo = this.mDrawableTint;
        boolean bl = colorStateList != null;
        tintInfo.mHasTintList = bl;
        this.setCompoundTints();
    }

    void setCompoundDrawableTintMode(PorterDuff.Mode mode) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        this.mDrawableTint.mTintMode = mode;
        TintInfo tintInfo = this.mDrawableTint;
        boolean bl = mode != null;
        tintInfo.mHasTintMode = bl;
        this.setCompoundTints();
    }

    void setTextSize(int n, float f) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !this.isAutoSizeEnabled()) {
            this.setTextSizeInternal(n, f);
        }
    }

    public void setTypefaceByCallback(Typeface typeface) {
        if (this.mAsyncFontPending) {
            this.mView.setTypeface(typeface);
            this.mFontTypeface = typeface;
        }
    }

    private static class ApplyTextViewCallback
    extends ResourcesCompat.FontCallback {
        private final int mFontWeight;
        private final WeakReference<AppCompatTextHelper> mParent;
        private final int mStyle;

        ApplyTextViewCallback(AppCompatTextHelper appCompatTextHelper, int n, int n2) {
            this.mParent = new WeakReference<AppCompatTextHelper>(appCompatTextHelper);
            this.mFontWeight = n;
            this.mStyle = n2;
        }

        @Override
        public void onFontRetrievalFailed(int n) {
        }

        @Override
        public void onFontRetrieved(Typeface typeface) {
            AppCompatTextHelper appCompatTextHelper = this.mParent.get();
            if (appCompatTextHelper == null) {
                return;
            }
            Typeface typeface2 = typeface;
            if (Build.VERSION.SDK_INT >= 28) {
                int n = this.mFontWeight;
                typeface2 = typeface;
                if (n != -1) {
                    boolean bl = (this.mStyle & 2) != 0;
                    typeface2 = Typeface.create((Typeface)typeface, (int)n, (boolean)bl);
                }
            }
            appCompatTextHelper.runOnUiThread(new TypefaceApplyCallback(this.mParent, typeface2));
        }

        private class TypefaceApplyCallback
        implements Runnable {
            private final WeakReference<AppCompatTextHelper> mParent;
            private final Typeface mTypeface;

            TypefaceApplyCallback(WeakReference<AppCompatTextHelper> weakReference, Typeface typeface) {
                this.mParent = weakReference;
                this.mTypeface = typeface;
            }

            @Override
            public void run() {
                AppCompatTextHelper appCompatTextHelper = this.mParent.get();
                if (appCompatTextHelper == null) {
                    return;
                }
                appCompatTextHelper.setTypefaceByCallback(this.mTypeface);
            }
        }

    }

}

