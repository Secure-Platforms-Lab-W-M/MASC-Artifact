// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import java.lang.ref.WeakReference;
import androidx.core.widget.TextViewCompat;
import androidx.core.widget.AutoSizeableTextView;
import java.util.Locale;
import android.os.LocaleList;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.graphics.PorterDuff$Mode;
import android.content.res.Resources$NotFoundException;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.R$styleable;
import android.os.Build$VERSION;
import android.content.res.ColorStateList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.graphics.Typeface;

class AppCompatTextHelper
{
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
    private int mFontWeight;
    private int mStyle;
    private final TextView mView;
    
    AppCompatTextHelper(final TextView mView) {
        this.mStyle = 0;
        this.mFontWeight = -1;
        this.mView = mView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(mView);
    }
    
    private void applyCompoundDrawableTint(final Drawable drawable, final TintInfo tintInfo) {
        if (drawable != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        }
    }
    
    private static TintInfo createTintInfo(final Context context, final AppCompatDrawableManager appCompatDrawableManager, final int n) {
        final ColorStateList tintList = appCompatDrawableManager.getTintList(context, n);
        if (tintList != null) {
            final TintInfo tintInfo = new TintInfo();
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = tintList;
            return tintInfo;
        }
        return null;
    }
    
    private void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, final Drawable drawable6) {
        if (Build$VERSION.SDK_INT >= 17 && (drawable5 != null || drawable6 != null)) {
            final Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            final TextView mView = this.mView;
            if (drawable5 != null) {
                drawable = drawable5;
            }
            else {
                drawable = compoundDrawablesRelative[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawablesRelative[1];
            }
            if (drawable6 != null) {
                drawable3 = drawable6;
            }
            else {
                drawable3 = compoundDrawablesRelative[2];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawablesRelative[3];
            }
            mView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
        else if (drawable != null || drawable2 != null || drawable3 != null || drawable4 != null) {
            if (Build$VERSION.SDK_INT >= 17) {
                final Drawable[] compoundDrawablesRelative2 = this.mView.getCompoundDrawablesRelative();
                if (compoundDrawablesRelative2[0] != null || compoundDrawablesRelative2[2] != null) {
                    final TextView mView2 = this.mView;
                    drawable5 = compoundDrawablesRelative2[0];
                    if (drawable2 != null) {
                        drawable = drawable2;
                    }
                    else {
                        drawable = compoundDrawablesRelative2[1];
                    }
                    drawable2 = compoundDrawablesRelative2[2];
                    if (drawable4 == null) {
                        drawable4 = compoundDrawablesRelative2[3];
                    }
                    mView2.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable5, drawable, drawable2, drawable4);
                    return;
                }
            }
            final Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            final TextView mView3 = this.mView;
            if (drawable == null) {
                drawable = compoundDrawables[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawables[1];
            }
            if (drawable3 == null) {
                drawable3 = compoundDrawables[2];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawables[3];
            }
            mView3.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
    }
    
    private void setCompoundTints() {
        final TintInfo mDrawableTint = this.mDrawableTint;
        this.mDrawableLeftTint = mDrawableTint;
        this.mDrawableTopTint = mDrawableTint;
        this.mDrawableRightTint = mDrawableTint;
        this.mDrawableBottomTint = mDrawableTint;
        this.mDrawableStartTint = mDrawableTint;
        this.mDrawableEndTint = mDrawableTint;
    }
    
    private void setTextSizeInternal(final int n, final float n2) {
        this.mAutoSizeTextHelper.setTextSizeInternal(n, n2);
    }
    
    private void updateTypefaceAndStyle(final Context context, final TintTypedArray tintTypedArray) {
        this.mStyle = tintTypedArray.getInt(R$styleable.TextAppearance_android_textStyle, this.mStyle);
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = false;
        if (sdk_INT >= 28 && (this.mFontWeight = tintTypedArray.getInt(R$styleable.TextAppearance_android_textFontWeight, -1)) != -1) {
            this.mStyle = ((this.mStyle & 0x2) | 0x0);
        }
        if (!tintTypedArray.hasValue(R$styleable.TextAppearance_android_fontFamily) && !tintTypedArray.hasValue(R$styleable.TextAppearance_fontFamily)) {
            if (tintTypedArray.hasValue(R$styleable.TextAppearance_android_typeface)) {
                this.mAsyncFontPending = false;
                final int int1 = tintTypedArray.getInt(R$styleable.TextAppearance_android_typeface, 1);
                if (int1 != 1) {
                    if (int1 == 2) {
                        this.mFontTypeface = Typeface.SERIF;
                        return;
                    }
                    if (int1 != 3) {
                        return;
                    }
                    this.mFontTypeface = Typeface.MONOSPACE;
                }
                else {
                    this.mFontTypeface = Typeface.SANS_SERIF;
                }
            }
            return;
        }
        this.mFontTypeface = null;
        int n;
        if (tintTypedArray.hasValue(R$styleable.TextAppearance_fontFamily)) {
            n = R$styleable.TextAppearance_fontFamily;
        }
        else {
            n = R$styleable.TextAppearance_android_fontFamily;
        }
        final int mFontWeight = this.mFontWeight;
        final int mStyle = this.mStyle;
        if (!context.isRestricted()) {
        Label_0295_Outer:
            while (true) {
                final ApplyTextViewCallback applyTextViewCallback = new ApplyTextViewCallback(this, mFontWeight, mStyle);
                while (true) {
                Label_0400:
                    while (true) {
                        Label_0394: {
                            try {
                                final Typeface font = tintTypedArray.getFont(n, this.mStyle, applyTextViewCallback);
                                if (font != null) {
                                    if (Build$VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
                                        final Typeface create = Typeface.create(font, 0);
                                        final int mFontWeight2 = this.mFontWeight;
                                        if ((this.mStyle & 0x2) == 0x0) {
                                            break Label_0394;
                                        }
                                        final boolean b2 = true;
                                        this.mFontTypeface = Typeface.create(create, mFontWeight2, b2);
                                    }
                                    else {
                                        this.mFontTypeface = font;
                                    }
                                }
                                if (this.mFontTypeface != null) {
                                    break Label_0400;
                                }
                                final boolean mAsyncFontPending = true;
                                this.mAsyncFontPending = mAsyncFontPending;
                            }
                            catch (Resources$NotFoundException ex) {}
                            catch (UnsupportedOperationException ex2) {}
                            break;
                        }
                        final boolean b2 = false;
                        continue Label_0295_Outer;
                    }
                    final boolean mAsyncFontPending = false;
                    continue;
                }
            }
        }
        if (this.mFontTypeface == null) {
            final String string = tintTypedArray.getString(n);
            if (string != null) {
                if (Build$VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
                    final Typeface create2 = Typeface.create(string, 0);
                    final int mFontWeight3 = this.mFontWeight;
                    boolean b3 = b;
                    if ((0x2 & this.mStyle) != 0x0) {
                        b3 = true;
                    }
                    this.mFontTypeface = Typeface.create(create2, mFontWeight3, b3);
                    return;
                }
                this.mFontTypeface = Typeface.create(string, this.mStyle);
            }
        }
    }
    
    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            final Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            this.applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            this.applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            this.applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            this.applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
        if (Build$VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
            final Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            this.applyCompoundDrawableTint(compoundDrawablesRelative[0], this.mDrawableStartTint);
            this.applyCompoundDrawableTint(compoundDrawablesRelative[2], this.mDrawableEndTint);
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
        final TintInfo mDrawableTint = this.mDrawableTint;
        if (mDrawableTint != null) {
            return mDrawableTint.mTintList;
        }
        return null;
    }
    
    PorterDuff$Mode getCompoundDrawableTintMode() {
        final TintInfo mDrawableTint = this.mDrawableTint;
        if (mDrawableTint != null) {
            return mDrawableTint.mTintMode;
        }
        return null;
    }
    
    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }
    
    void loadFromAttributes(final AttributeSet set, int n) {
        final Context context = this.mView.getContext();
        final AppCompatDrawableManager value = AppCompatDrawableManager.get();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R$styleable.AppCompatTextHelper, n, 0);
        final int resourceId = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        if (Build$VERSION.SDK_INT >= 17) {
            if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableStart)) {
                this.mDrawableStartTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableStart, 0));
            }
            if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextHelper_android_drawableEnd)) {
                this.mDrawableEndTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextHelper_android_drawableEnd, 0));
            }
        }
        obtainStyledAttributes.recycle();
        final boolean b = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean allCaps = false;
        final boolean b2 = false;
        boolean b3 = false;
        final boolean b4 = false;
        final ColorStateList list = null;
        ColorStateList colorStateList = null;
        final ColorStateList list2 = null;
        final ColorStateList list3 = null;
        ColorStateList colorStateList2 = null;
        final ColorStateList list4 = null;
        ColorStateList linkTextColor = null;
        final ColorStateList list5 = null;
        String string = null;
        final String s = null;
        String s2 = null;
        final String s3 = null;
        if (resourceId != -1) {
            final TintTypedArray obtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, resourceId, R$styleable.TextAppearance);
            allCaps = b2;
            b3 = b4;
            if (!b) {
                allCaps = b2;
                b3 = b4;
                if (obtainStyledAttributes2.hasValue(R$styleable.TextAppearance_textAllCaps)) {
                    b3 = true;
                    allCaps = obtainStyledAttributes2.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
                }
            }
            this.updateTypefaceAndStyle(context, obtainStyledAttributes2);
            colorStateList = list;
            colorStateList2 = list3;
            linkTextColor = list5;
            if (Build$VERSION.SDK_INT < 23) {
                ColorStateList colorStateList3 = list2;
                if (obtainStyledAttributes2.hasValue(R$styleable.TextAppearance_android_textColor)) {
                    colorStateList3 = obtainStyledAttributes2.getColorStateList(R$styleable.TextAppearance_android_textColor);
                }
                ColorStateList colorStateList4 = list4;
                if (obtainStyledAttributes2.hasValue(R$styleable.TextAppearance_android_textColorHint)) {
                    colorStateList4 = obtainStyledAttributes2.getColorStateList(R$styleable.TextAppearance_android_textColorHint);
                }
                colorStateList = colorStateList3;
                colorStateList2 = colorStateList4;
                linkTextColor = list5;
                if (obtainStyledAttributes2.hasValue(R$styleable.TextAppearance_android_textColorLink)) {
                    linkTextColor = obtainStyledAttributes2.getColorStateList(R$styleable.TextAppearance_android_textColorLink);
                    colorStateList2 = colorStateList4;
                    colorStateList = colorStateList3;
                }
            }
            s2 = s3;
            if (obtainStyledAttributes2.hasValue(R$styleable.TextAppearance_textLocale)) {
                s2 = obtainStyledAttributes2.getString(R$styleable.TextAppearance_textLocale);
            }
            string = s;
            if (Build$VERSION.SDK_INT >= 26) {
                string = s;
                if (obtainStyledAttributes2.hasValue(R$styleable.TextAppearance_fontVariationSettings)) {
                    string = obtainStyledAttributes2.getString(R$styleable.TextAppearance_fontVariationSettings);
                }
            }
            obtainStyledAttributes2.recycle();
        }
        final TintTypedArray obtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, set, R$styleable.TextAppearance, n, 0);
        if (!b && obtainStyledAttributes3.hasValue(R$styleable.TextAppearance_textAllCaps)) {
            allCaps = obtainStyledAttributes3.getBoolean(R$styleable.TextAppearance_textAllCaps, false);
            b3 = true;
        }
        if (Build$VERSION.SDK_INT < 23) {
            if (obtainStyledAttributes3.hasValue(R$styleable.TextAppearance_android_textColor)) {
                colorStateList = obtainStyledAttributes3.getColorStateList(R$styleable.TextAppearance_android_textColor);
            }
            if (obtainStyledAttributes3.hasValue(R$styleable.TextAppearance_android_textColorHint)) {
                colorStateList2 = obtainStyledAttributes3.getColorStateList(R$styleable.TextAppearance_android_textColorHint);
            }
            if (obtainStyledAttributes3.hasValue(R$styleable.TextAppearance_android_textColorLink)) {
                linkTextColor = obtainStyledAttributes3.getColorStateList(R$styleable.TextAppearance_android_textColorLink);
            }
        }
        if (obtainStyledAttributes3.hasValue(R$styleable.TextAppearance_textLocale)) {
            s2 = obtainStyledAttributes3.getString(R$styleable.TextAppearance_textLocale);
        }
        String string2 = string;
        if (Build$VERSION.SDK_INT >= 26) {
            string2 = string;
            if (obtainStyledAttributes3.hasValue(R$styleable.TextAppearance_fontVariationSettings)) {
                string2 = obtainStyledAttributes3.getString(R$styleable.TextAppearance_fontVariationSettings);
            }
        }
        if (Build$VERSION.SDK_INT >= 28) {
            if (obtainStyledAttributes3.hasValue(R$styleable.TextAppearance_android_textSize)) {
                if (obtainStyledAttributes3.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, -1) == 0) {
                    this.mView.setTextSize(0, 0.0f);
                }
            }
        }
        this.updateTypefaceAndStyle(context, obtainStyledAttributes3);
        obtainStyledAttributes3.recycle();
        if (colorStateList != null) {
            this.mView.setTextColor(colorStateList);
        }
        if (colorStateList2 != null) {
            this.mView.setHintTextColor(colorStateList2);
        }
        if (linkTextColor != null) {
            this.mView.setLinkTextColor(linkTextColor);
        }
        if (!b && b3) {
            this.setAllCaps(allCaps);
        }
        final Typeface mFontTypeface = this.mFontTypeface;
        if (mFontTypeface != null) {
            if (this.mFontWeight == -1) {
                this.mView.setTypeface(mFontTypeface, this.mStyle);
            }
            else {
                this.mView.setTypeface(mFontTypeface);
            }
        }
        if (string2 != null) {
            this.mView.setFontVariationSettings(string2);
        }
        if (s2 != null) {
            if (Build$VERSION.SDK_INT >= 24) {
                this.mView.setTextLocales(LocaleList.forLanguageTags(s2));
            }
            else if (Build$VERSION.SDK_INT >= 21) {
                this.mView.setTextLocale(Locale.forLanguageTag(s2.substring(0, s2.indexOf(44))));
            }
        }
        this.mAutoSizeTextHelper.loadFromAttributes(set, n);
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            if (this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
                final int[] autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
                if (autoSizeTextAvailableSizes.length > 0) {
                    if (this.mView.getAutoSizeStepGranularity() != -1.0f) {
                        this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
                    }
                    else {
                        this.mView.setAutoSizeTextTypeUniformWithPresetSizes(autoSizeTextAvailableSizes, 0);
                    }
                }
            }
        }
        final TintTypedArray obtainStyledAttributes4 = TintTypedArray.obtainStyledAttributes(context, set, R$styleable.AppCompatTextView);
        Drawable drawable = null;
        Drawable drawable2 = null;
        n = R$styleable.AppCompatTextView_drawableLeftCompat;
        Drawable drawable3 = null;
        n = obtainStyledAttributes4.getResourceId(n, -1);
        if (n != -1) {
            drawable2 = value.getDrawable(context, n);
        }
        n = obtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableTopCompat, -1);
        if (n != -1) {
            drawable3 = value.getDrawable(context, n);
        }
        n = obtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableRightCompat, -1);
        Drawable drawable4;
        if (n != -1) {
            drawable4 = value.getDrawable(context, n);
        }
        else {
            drawable4 = null;
        }
        n = obtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableBottomCompat, -1);
        Drawable drawable5;
        if (n != -1) {
            drawable5 = value.getDrawable(context, n);
        }
        else {
            drawable5 = null;
        }
        n = obtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableStartCompat, -1);
        Drawable drawable6;
        if (n != -1) {
            drawable6 = value.getDrawable(context, n);
        }
        else {
            drawable6 = null;
        }
        n = obtainStyledAttributes4.getResourceId(R$styleable.AppCompatTextView_drawableEndCompat, -1);
        if (n != -1) {
            drawable = value.getDrawable(context, n);
        }
        this.setCompoundDrawables(drawable2, drawable3, drawable4, drawable5, drawable6, drawable);
        if (obtainStyledAttributes4.hasValue(R$styleable.AppCompatTextView_drawableTint)) {
            TextViewCompat.setCompoundDrawableTintList(this.mView, obtainStyledAttributes4.getColorStateList(R$styleable.AppCompatTextView_drawableTint));
        }
        if (obtainStyledAttributes4.hasValue(R$styleable.AppCompatTextView_drawableTintMode)) {
            TextViewCompat.setCompoundDrawableTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes4.getInt(R$styleable.AppCompatTextView_drawableTintMode, -1), null));
        }
        n = obtainStyledAttributes4.getDimensionPixelSize(R$styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
        final int dimensionPixelSize = obtainStyledAttributes4.getDimensionPixelSize(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
        final int dimensionPixelSize2 = obtainStyledAttributes4.getDimensionPixelSize(R$styleable.AppCompatTextView_lineHeight, -1);
        obtainStyledAttributes4.recycle();
        if (n != -1) {
            TextViewCompat.setFirstBaselineToTopHeight(this.mView, n);
        }
        if (dimensionPixelSize != -1) {
            TextViewCompat.setLastBaselineToBottomHeight(this.mView, dimensionPixelSize);
        }
        if (dimensionPixelSize2 != -1) {
            TextViewCompat.setLineHeight(this.mView, dimensionPixelSize2);
        }
    }
    
    void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            this.autoSizeText();
        }
    }
    
    void onSetCompoundDrawables() {
        this.applyCompoundDrawablesTints();
    }
    
    void onSetTextAppearance(final Context context, final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, n, R$styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R$styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(obtainStyledAttributes.getBoolean(R$styleable.TextAppearance_textAllCaps, false));
        }
        if (Build$VERSION.SDK_INT < 23 && obtainStyledAttributes.hasValue(R$styleable.TextAppearance_android_textColor)) {
            final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R$styleable.TextAppearance_android_textColor);
            if (colorStateList != null) {
                this.mView.setTextColor(colorStateList);
            }
        }
        if (obtainStyledAttributes.hasValue(R$styleable.TextAppearance_android_textSize) && obtainStyledAttributes.getDimensionPixelSize(R$styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle(context, obtainStyledAttributes);
        if (Build$VERSION.SDK_INT >= 26 && obtainStyledAttributes.hasValue(R$styleable.TextAppearance_fontVariationSettings)) {
            final String string = obtainStyledAttributes.getString(R$styleable.TextAppearance_fontVariationSettings);
            if (string != null) {
                this.mView.setFontVariationSettings(string);
            }
        }
        obtainStyledAttributes.recycle();
        final Typeface mFontTypeface = this.mFontTypeface;
        if (mFontTypeface != null) {
            this.mView.setTypeface(mFontTypeface, this.mStyle);
        }
    }
    
    public void runOnUiThread(final Runnable runnable) {
        this.mView.post(runnable);
    }
    
    void setAllCaps(final boolean allCaps) {
        this.mView.setAllCaps(allCaps);
    }
    
    void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
    }
    
    void setAutoSizeTextTypeUniformWithPresetSizes(final int[] array, final int n) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
    }
    
    void setAutoSizeTextTypeWithDefaults(final int autoSizeTextTypeWithDefaults) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(autoSizeTextTypeWithDefaults);
    }
    
    void setCompoundDrawableTintList(final ColorStateList mTintList) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        this.mDrawableTint.mTintList = mTintList;
        this.mDrawableTint.mHasTintList = (mTintList != null);
        this.setCompoundTints();
    }
    
    void setCompoundDrawableTintMode(final PorterDuff$Mode mTintMode) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        this.mDrawableTint.mTintMode = mTintMode;
        this.mDrawableTint.mHasTintMode = (mTintMode != null);
        this.setCompoundTints();
    }
    
    void setTextSize(final int n, final float n2) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !this.isAutoSizeEnabled()) {
            this.setTextSizeInternal(n, n2);
        }
    }
    
    public void setTypefaceByCallback(final Typeface typeface) {
        if (this.mAsyncFontPending) {
            this.mView.setTypeface(typeface);
            this.mFontTypeface = typeface;
        }
    }
    
    private static class ApplyTextViewCallback extends FontCallback
    {
        private final int mFontWeight;
        private final WeakReference<AppCompatTextHelper> mParent;
        private final int mStyle;
        
        ApplyTextViewCallback(final AppCompatTextHelper appCompatTextHelper, final int mFontWeight, final int mStyle) {
            this.mParent = new WeakReference<AppCompatTextHelper>(appCompatTextHelper);
            this.mFontWeight = mFontWeight;
            this.mStyle = mStyle;
        }
        
        @Override
        public void onFontRetrievalFailed(final int n) {
        }
        
        @Override
        public void onFontRetrieved(final Typeface typeface) {
            final AppCompatTextHelper appCompatTextHelper = this.mParent.get();
            if (appCompatTextHelper == null) {
                return;
            }
            Typeface create = typeface;
            if (Build$VERSION.SDK_INT >= 28) {
                final int mFontWeight = this.mFontWeight;
                create = typeface;
                if (mFontWeight != -1) {
                    create = Typeface.create(typeface, mFontWeight, (this.mStyle & 0x2) != 0x0);
                }
            }
            appCompatTextHelper.runOnUiThread(new TypefaceApplyCallback(this.mParent, create));
        }
        
        private class TypefaceApplyCallback implements Runnable
        {
            private final WeakReference<AppCompatTextHelper> mParent;
            private final Typeface mTypeface;
            
            TypefaceApplyCallback(final WeakReference<AppCompatTextHelper> mParent, final Typeface mTypeface) {
                this.mParent = mParent;
                this.mTypeface = mTypeface;
            }
            
            @Override
            public void run() {
                final AppCompatTextHelper appCompatTextHelper = this.mParent.get();
                if (appCompatTextHelper == null) {
                    return;
                }
                appCompatTextHelper.setTypefaceByCallback(this.mTypeface);
            }
        }
    }
}
