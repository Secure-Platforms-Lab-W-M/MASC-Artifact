/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.method.PasswordTransformationMethod
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextHelperV17;
import android.support.v7.widget.AppCompatTextViewAutoSizeHelper;
import android.support.v7.widget.TintInfo;
import android.support.v7.widget.TintTypedArray;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

@RequiresApi(value=9)
class AppCompatTextHelper {
    @NonNull
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mStyle = 0;
    final TextView mView;

    AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
    }

    static AppCompatTextHelper create(TextView textView) {
        if (Build.VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }

    protected static TintInfo createTintInfo(Context context, AppCompatDrawableManager object, int n) {
        if ((context = object.getTintList(context, n)) != null) {
            object = new TintInfo();
            object.mHasTintList = true;
            object.mTintList = context;
            return object;
        }
        return null;
    }

    private void setTextSizeInternal(int n, float f) {
        this.mAutoSizeTextHelper.setTextSizeInternal(n, f);
    }

    private void updateTypefaceAndStyle(Context context, TintTypedArray tintTypedArray) {
        this.mStyle = tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
        if (!tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily) && !tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
            return;
        }
        this.mFontTypeface = null;
        int n = tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily) ? R.styleable.TextAppearance_android_fontFamily : R.styleable.TextAppearance_fontFamily;
        if (!context.isRestricted()) {
            try {
                this.mFontTypeface = tintTypedArray.getFont(n, this.mStyle, this.mView);
            }
            catch (Resources.NotFoundException | UnsupportedOperationException object) {
                // empty catch block
            }
        }
        if (this.mFontTypeface == null) {
            this.mFontTypeface = Typeface.create((String)tintTypedArray.getString(n), (int)this.mStyle);
            return;
        }
    }

    final void applyCompoundDrawableTint(Drawable drawable2, TintInfo tintInfo) {
        if (drawable2 != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable2, tintInfo, this.mView.getDrawableState());
            return;
        }
    }

    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint == null && this.mDrawableTopTint == null && this.mDrawableRightTint == null && this.mDrawableBottomTint == null) {
            return;
        }
        Drawable[] arrdrawable = this.mView.getCompoundDrawables();
        this.applyCompoundDrawableTint(arrdrawable[0], this.mDrawableLeftTint);
        this.applyCompoundDrawableTint(arrdrawable[1], this.mDrawableTopTint);
        this.applyCompoundDrawableTint(arrdrawable[2], this.mDrawableRightTint);
        this.applyCompoundDrawableTint(arrdrawable[3], this.mDrawableBottomTint);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
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

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    void loadFromAttributes(AttributeSet arrn, int n) {
        Context context = this.mView.getContext();
        Object object = AppCompatDrawableManager.get();
        Object object2 = TintTypedArray.obtainStyledAttributes(context, (AttributeSet)arrn, R.styleable.AppCompatTextHelper, n, 0);
        int n2 = object2.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)object, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)object, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)object, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (object2.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = AppCompatTextHelper.createTintInfo(context, (AppCompatDrawableManager)object, object2.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        object2.recycle();
        boolean bl = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        object2 = null;
        TintTypedArray tintTypedArray = null;
        ColorStateList colorStateList = null;
        object = null;
        Object var15_14 = null;
        TintTypedArray tintTypedArray2 = null;
        TintTypedArray tintTypedArray3 = null;
        TintTypedArray tintTypedArray4 = null;
        if (n2 != -1) {
            tintTypedArray = TintTypedArray.obtainStyledAttributes(context, n2, R.styleable.TextAppearance);
            if (!bl && tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                bl3 = tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                bl5 = true;
            }
            this.updateTypefaceAndStyle(context, tintTypedArray);
            if (Build.VERSION.SDK_INT < 23) {
                object = tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor) ? tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor) : colorStateList;
                if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                    tintTypedArray2 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                }
                if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                    tintTypedArray4 = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                    object2 = object;
                    object = tintTypedArray2;
                    tintTypedArray2 = tintTypedArray4;
                } else {
                    object2 = object;
                    object = tintTypedArray2;
                    tintTypedArray2 = tintTypedArray4;
                }
            } else {
                tintTypedArray2 = tintTypedArray4;
            }
            tintTypedArray.recycle();
        } else {
            tintTypedArray2 = tintTypedArray3;
            object = var15_14;
            object2 = tintTypedArray;
            bl5 = bl4;
            bl3 = bl2;
        }
        tintTypedArray4 = TintTypedArray.obtainStyledAttributes(context, (AttributeSet)arrn, R.styleable.TextAppearance, n, 0);
        if (!bl && tintTypedArray4.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            bl5 = true;
            bl3 = tintTypedArray4.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (tintTypedArray4.hasValue(R.styleable.TextAppearance_android_textColor)) {
                object2 = tintTypedArray4.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (tintTypedArray4.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                object = tintTypedArray4.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
            if (tintTypedArray4.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                tintTypedArray2 = tintTypedArray4.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
            }
        }
        this.updateTypefaceAndStyle(context, tintTypedArray4);
        tintTypedArray4.recycle();
        if (object2 != null) {
            this.mView.setTextColor((ColorStateList)object2);
        }
        if (object != null) {
            this.mView.setHintTextColor((ColorStateList)object);
        }
        if (tintTypedArray2 != null) {
            this.mView.setLinkTextColor((ColorStateList)tintTypedArray2);
        }
        if (!bl && bl5) {
            this.setAllCaps(bl3);
        }
        if ((object = this.mFontTypeface) != null) {
            this.mView.setTypeface((Typeface)object, this.mStyle);
        }
        this.mAutoSizeTextHelper.loadFromAttributes((AttributeSet)arrn, n);
        if (Build.VERSION.SDK_INT >= 26) {
            if (this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
                arrn = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
                if (arrn.length > 0) {
                    if ((float)this.mView.getAutoSizeStepGranularity() != -1.0f) {
                        this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
                        return;
                    }
                    this.mView.setAutoSizeTextTypeUniformWithPresetSizes(arrn, 0);
                    return;
                }
                return;
            }
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT < 26) {
            this.autoSizeText();
            return;
        }
    }

    void onSetTextAppearance(Context context, int n) {
        ColorStateList colorStateList;
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, n, R.styleable.TextAppearance);
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build.VERSION.SDK_INT < 23 && tintTypedArray.hasValue(R.styleable.TextAppearance_android_textColor) && (colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor)) != null) {
            this.mView.setTextColor(colorStateList);
        }
        this.updateTypefaceAndStyle(context, tintTypedArray);
        tintTypedArray.recycle();
        context = this.mFontTypeface;
        if (context != null) {
            this.mView.setTypeface((Typeface)context, this.mStyle);
            return;
        }
    }

    void setAllCaps(boolean bl) {
        this.mView.setAllCaps(bl);
    }

    void setAutoSizeTextTypeUniformWithConfiguration(int n, int n2, int n3, int n4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
    }

    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] arrn, int n) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(arrn, n);
    }

    void setAutoSizeTextTypeWithDefaults(int n) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(n);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void setTextSize(int n, float f) {
        if (Build.VERSION.SDK_INT < 26) {
            if (!this.isAutoSizeEnabled()) {
                this.setTextSizeInternal(n, f);
                return;
            }
            return;
        }
    }
}

