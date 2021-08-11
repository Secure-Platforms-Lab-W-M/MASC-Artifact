// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.support.annotation.RestrictTo;
import android.graphics.drawable.Drawable;
import android.content.res.Resources$NotFoundException;
import android.support.v7.appcompat.R;
import android.content.res.ColorStateList;
import android.content.Context;
import android.os.Build$VERSION;
import android.widget.TextView;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@RequiresApi(9)
class AppCompatTextHelper
{
    @NonNull
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mStyle;
    final TextView mView;
    
    AppCompatTextHelper(final TextView mView) {
        this.mStyle = 0;
        this.mView = mView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
    }
    
    static AppCompatTextHelper create(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }
    
    protected static TintInfo createTintInfo(final Context context, final AppCompatDrawableManager appCompatDrawableManager, final int n) {
        final ColorStateList tintList = appCompatDrawableManager.getTintList(context, n);
        if (tintList != null) {
            final TintInfo tintInfo = new TintInfo();
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = tintList;
            return tintInfo;
        }
        return null;
    }
    
    private void setTextSizeInternal(final int n, final float n2) {
        this.mAutoSizeTextHelper.setTextSizeInternal(n, n2);
    }
    
    private void updateTypefaceAndStyle(final Context context, final TintTypedArray tintTypedArray) {
        this.mStyle = tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
        if (!tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily) && !tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
            return;
        }
        this.mFontTypeface = null;
        int n;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily)) {
            n = R.styleable.TextAppearance_android_fontFamily;
        }
        else {
            n = R.styleable.TextAppearance_fontFamily;
        }
        if (!context.isRestricted()) {
            try {
                this.mFontTypeface = tintTypedArray.getFont(n, this.mStyle, this.mView);
            }
            catch (UnsupportedOperationException | Resources$NotFoundException ex) {}
        }
        if (this.mFontTypeface == null) {
            this.mFontTypeface = Typeface.create(tintTypedArray.getString(n), this.mStyle);
        }
    }
    
    final void applyCompoundDrawableTint(final Drawable drawable, final TintInfo tintInfo) {
        if (drawable != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        }
    }
    
    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint == null && this.mDrawableTopTint == null && this.mDrawableRightTint == null && this.mDrawableBottomTint == null) {
            return;
        }
        final Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
        this.applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
        this.applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
        this.applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
        this.applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
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
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }
    
    void loadFromAttributes(final AttributeSet set, final int n) {
        final Context context = this.mView.getContext();
        final AppCompatDrawableManager value = AppCompatDrawableManager.get();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.AppCompatTextHelper, n, 0);
        final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        obtainStyledAttributes.recycle();
        final boolean b = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        final boolean b2 = false;
        boolean allCaps = false;
        final boolean b3 = false;
        boolean b4 = false;
        ColorStateList colorStateList = null;
        final ColorStateList list = null;
        final ColorStateList list2 = null;
        ColorStateList colorStateList2 = null;
        final ColorStateList list3 = null;
        ColorStateList colorStateList3 = null;
        final ColorStateList list4 = null;
        final ColorStateList list5 = null;
        ColorStateList colorStateList6;
        if (resourceId != -1) {
            final TintTypedArray obtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, resourceId, R.styleable.TextAppearance);
            if (!b && obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                allCaps = obtainStyledAttributes2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                b4 = true;
            }
            this.updateTypefaceAndStyle(context, obtainStyledAttributes2);
            if (Build$VERSION.SDK_INT < 23) {
                ColorStateList colorStateList4;
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColor)) {
                    colorStateList4 = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColor);
                }
                else {
                    colorStateList4 = list2;
                }
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                    colorStateList3 = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                }
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                    final ColorStateList colorStateList5 = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                    colorStateList = colorStateList4;
                    colorStateList2 = colorStateList3;
                    colorStateList6 = colorStateList5;
                }
                else {
                    colorStateList = colorStateList4;
                    colorStateList2 = colorStateList3;
                    colorStateList6 = list5;
                }
            }
            else {
                colorStateList6 = list5;
            }
            obtainStyledAttributes2.recycle();
        }
        else {
            colorStateList6 = list4;
            colorStateList2 = list3;
            colorStateList = list;
            b4 = b3;
            allCaps = b2;
        }
        final TintTypedArray obtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.TextAppearance, n, 0);
        if (!b && obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            b4 = true;
            allCaps = obtainStyledAttributes3.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        }
        if (Build$VERSION.SDK_INT < 23) {
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColor)) {
                colorStateList = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                colorStateList2 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                colorStateList6 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
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
        if (colorStateList6 != null) {
            this.mView.setLinkTextColor(colorStateList6);
        }
        if (!b && b4) {
            this.setAllCaps(allCaps);
        }
        final Typeface mFontTypeface = this.mFontTypeface;
        if (mFontTypeface != null) {
            this.mView.setTypeface(mFontTypeface, this.mStyle);
        }
        this.mAutoSizeTextHelper.loadFromAttributes(set, n);
        if (Build$VERSION.SDK_INT < 26) {
            return;
        }
        if (this.mAutoSizeTextHelper.getAutoSizeTextType() == 0) {
            return;
        }
        final int[] autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
        if (autoSizeTextAvailableSizes.length <= 0) {
            return;
        }
        if (this.mView.getAutoSizeStepGranularity() != -1.0f) {
            this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            return;
        }
        this.mView.setAutoSizeTextTypeUniformWithPresetSizes(autoSizeTextAvailableSizes, 0);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT < 26) {
            this.autoSizeText();
        }
    }
    
    void onSetTextAppearance(final Context context, final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build$VERSION.SDK_INT < 23) {
            if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor)) {
                final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
                if (colorStateList != null) {
                    this.mView.setTextColor(colorStateList);
                }
            }
        }
        this.updateTypefaceAndStyle(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        final Typeface mFontTypeface = this.mFontTypeface;
        if (mFontTypeface != null) {
            this.mView.setTypeface(mFontTypeface, this.mStyle);
        }
    }
    
    void setAllCaps(final boolean allCaps) {
        this.mView.setAllCaps(allCaps);
    }
    
    void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
    }
    
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] array, final int n) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
    }
    
    void setAutoSizeTextTypeWithDefaults(final int autoSizeTextTypeWithDefaults) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(autoSizeTextTypeWithDefaults);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setTextSize(final int n, final float n2) {
        if (Build$VERSION.SDK_INT >= 26) {
            return;
        }
        if (!this.isAutoSizeEnabled()) {
            this.setTextSizeInternal(n, n2);
        }
    }
}
