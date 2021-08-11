/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.RectF
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.Layout
 *  android.text.Layout$Alignment
 *  android.text.StaticLayout
 *  android.text.StaticLayout$Builder
 *  android.text.TextDirectionHeuristic
 *  android.text.TextDirectionHeuristics
 *  android.text.TextPaint
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.TypedValue
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatEditText;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

class AppCompatTextViewAutoSizeHelper {
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final String TAG = "ACTVAutoSizeHelper";
    private static final RectF TEMP_RECTF = new RectF();
    static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0f;
    private static final int VERY_WIDE = 1048576;
    private static Hashtable<String, Method> sTextViewMethodByNameCache = new Hashtable();
    private float mAutoSizeMaxTextSizeInPx = -1.0f;
    private float mAutoSizeMinTextSizeInPx = -1.0f;
    private float mAutoSizeStepGranularityInPx = -1.0f;
    private int[] mAutoSizeTextSizesInPx = new int[0];
    private int mAutoSizeTextType = 0;
    private final Context mContext;
    private boolean mHasPresetAutoSizeValues = false;
    private boolean mNeedsAutoSizeText = false;
    private TextPaint mTempTextPaint;
    private final TextView mTextView;

    AppCompatTextViewAutoSizeHelper(TextView textView) {
        this.mTextView = textView;
        this.mContext = this.mTextView.getContext();
    }

    private int[] cleanupAutoSizePresetSizes(int[] arrn) {
        int n;
        int n2 = arrn.length;
        if (n2 == 0) {
            return arrn;
        }
        Arrays.sort(arrn);
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (n = 0; n < n2; ++n) {
            int n3 = arrn[n];
            if (n3 <= 0 || Collections.binarySearch(arrayList, n3) >= 0) continue;
            arrayList.add(n3);
        }
        if (n2 == arrayList.size()) {
            return arrn;
        }
        n2 = arrayList.size();
        arrn = new int[n2];
        for (n = 0; n < n2; ++n) {
            arrn[n] = (Integer)arrayList.get(n);
        }
        return arrn;
    }

    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mNeedsAutoSizeText = false;
    }

    @TargetApi(value=23)
    private StaticLayout createStaticLayoutForMeasuring(CharSequence charSequence, Layout.Alignment alignment, int n, int n2) {
        TextDirectionHeuristic textDirectionHeuristic = this.invokeAndReturnWithDefault((Object)this.mTextView, "getTextDirectionHeuristic", (T)TextDirectionHeuristics.FIRSTSTRONG_LTR);
        charSequence = StaticLayout.Builder.obtain((CharSequence)charSequence, (int)0, (int)charSequence.length(), (TextPaint)this.mTempTextPaint, (int)n).setAlignment(alignment).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
        if (n2 == -1) {
            n2 = Integer.MAX_VALUE;
        }
        return charSequence.setMaxLines(n2).setTextDirection(textDirectionHeuristic).build();
    }

    @TargetApi(value=14)
    private StaticLayout createStaticLayoutForMeasuringPre23(CharSequence charSequence, Layout.Alignment alignment, int n) {
        boolean bl;
        float f;
        float f2;
        if (Build.VERSION.SDK_INT >= 16) {
            f = this.mTextView.getLineSpacingMultiplier();
            f2 = this.mTextView.getLineSpacingExtra();
            bl = this.mTextView.getIncludeFontPadding();
        } else {
            f = this.invokeAndReturnWithDefault((Object)this.mTextView, "getLineSpacingMultiplier", Float.valueOf(1.0f)).floatValue();
            f2 = this.invokeAndReturnWithDefault((Object)this.mTextView, "getLineSpacingExtra", Float.valueOf(0.0f)).floatValue();
            bl = this.invokeAndReturnWithDefault((Object)this.mTextView, "getIncludeFontPadding", true);
        }
        return new StaticLayout(charSequence, this.mTempTextPaint, n, alignment, f, f2, bl);
    }

    private int findLargestTextSizeWhichFits(RectF rectF) {
        int n = this.mAutoSizeTextSizesInPx.length;
        if (n != 0) {
            int n2 = 0;
            int n3 = 0 + 1;
            --n;
            while (n3 <= n) {
                n2 = (n3 + n) / 2;
                if (this.suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[n2], rectF)) {
                    int n4 = n2 + 1;
                    n2 = n3;
                    n3 = n4;
                    continue;
                }
                n2 = n = n2 - 1;
            }
            return this.mAutoSizeTextSizesInPx[n2];
        }
        throw new IllegalStateException("No available text sizes to choose from.");
    }

    @Nullable
    private Method getTextViewMethod(@NonNull String string2) {
        Method method;
        block4 : {
            block5 : {
                try {
                    method = sTextViewMethodByNameCache.get(string2);
                    if (method != null) break block4;
                }
                catch (Exception exception) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to retrieve TextView#");
                    stringBuilder.append(string2);
                    stringBuilder.append("() method");
                    Log.w((String)"ACTVAutoSizeHelper", (String)stringBuilder.toString(), (Throwable)exception);
                    return null;
                }
                method = TextView.class.getDeclaredMethod(string2, new Class[0]);
                if (method == null) break block5;
                method.setAccessible(true);
                sTextViewMethodByNameCache.put(string2, method);
                return method;
            }
            return method;
        }
        return method;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private <T> T invokeAndReturnWithDefault(@NonNull Object object, @NonNull String object2, @NonNull T t) {
        Throwable throwable222;
        boolean bl;
        block4 : {
            Object var6_6 = null;
            bl = false;
            try {
                object = object2 = (object = this.getTextViewMethod((String)object2).invoke(object, new Object[0]));
                if (object2 != null) return (T)object;
                object = object2;
                if (!false) return (T)object;
                return t;
            }
            catch (Throwable throwable222) {
                break block4;
            }
            catch (Exception exception) {
                boolean bl2;
                bl = bl2 = true;
                StringBuilder stringBuilder = new StringBuilder();
                bl = bl2;
                stringBuilder.append("Failed to invoke TextView#");
                bl = bl2;
                stringBuilder.append((String)object2);
                bl = bl2;
                stringBuilder.append("() method");
                bl = bl2;
                Log.w((String)"ACTVAutoSizeHelper", (String)stringBuilder.toString(), (Throwable)exception);
                object = var6_6;
                if (false) return (T)object;
                object = var6_6;
                if (!true) return (T)object;
                return t;
            }
        }
        if (false) throw throwable222;
        if (!bl) throw throwable222;
        throw throwable222;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void setRawTextSize(float f) {
        if (f == this.mTextView.getPaint().getTextSize()) {
            return;
        }
        this.mTextView.getPaint().setTextSize(f);
        boolean bl = false;
        if (Build.VERSION.SDK_INT >= 18) {
            bl = this.mTextView.isInLayout();
        }
        if (this.mTextView.getLayout() == null) {
            return;
        }
        this.mNeedsAutoSizeText = false;
        try {
            Method method = this.getTextViewMethod("nullLayouts");
            if (method != null) {
                method.invoke((Object)this.mTextView, new Object[0]);
            }
        }
        catch (Exception exception) {
            Log.w((String)"ACTVAutoSizeHelper", (String)"Failed to invoke TextView#nullLayouts() method", (Throwable)exception);
        }
        if (!bl) {
            this.mTextView.requestLayout();
        } else {
            this.mTextView.forceLayout();
        }
        this.mTextView.invalidate();
    }

    private boolean setupAutoSizeText() {
        if (this.supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                int n = 1;
                float f = Math.round(this.mAutoSizeMinTextSizeInPx);
                while (Math.round(this.mAutoSizeStepGranularityInPx + f) <= Math.round(this.mAutoSizeMaxTextSizeInPx)) {
                    ++n;
                    f += this.mAutoSizeStepGranularityInPx;
                }
                int[] arrn = new int[n];
                f = this.mAutoSizeMinTextSizeInPx;
                for (int i = 0; i < n; ++i) {
                    arrn[i] = Math.round(f);
                    f += this.mAutoSizeStepGranularityInPx;
                }
                this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(arrn);
            }
            this.mNeedsAutoSizeText = true;
        } else {
            this.mNeedsAutoSizeText = false;
        }
        return this.mNeedsAutoSizeText;
    }

    private void setupAutoSizeUniformPresetSizes(TypedArray typedArray) {
        int n = typedArray.length();
        int[] arrn = new int[n];
        if (n > 0) {
            for (int i = 0; i < n; ++i) {
                arrn[i] = typedArray.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(arrn);
            this.setupAutoSizeUniformPresetSizesConfiguration();
            return;
        }
    }

    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        int n = this.mAutoSizeTextSizesInPx.length;
        boolean bl = n > 0;
        this.mHasPresetAutoSizeValues = bl;
        if (this.mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            int[] arrn = this.mAutoSizeTextSizesInPx;
            this.mAutoSizeMinTextSizeInPx = arrn[0];
            this.mAutoSizeMaxTextSizeInPx = arrn[n - 1];
            this.mAutoSizeStepGranularityInPx = -1.0f;
        }
        return this.mHasPresetAutoSizeValues;
    }

    private boolean suggestedSizeFitsInSpace(int n, RectF rectF) {
        CharSequence charSequence = this.mTextView.getText();
        int n2 = Build.VERSION.SDK_INT >= 16 ? this.mTextView.getMaxLines() : -1;
        TextPaint textPaint = this.mTempTextPaint;
        if (textPaint == null) {
            this.mTempTextPaint = new TextPaint();
        } else {
            textPaint.reset();
        }
        this.mTempTextPaint.set(this.mTextView.getPaint());
        this.mTempTextPaint.setTextSize((float)n);
        textPaint = this.invokeAndReturnWithDefault((Object)this.mTextView, "getLayoutAlignment", (T)Layout.Alignment.ALIGN_NORMAL);
        textPaint = Build.VERSION.SDK_INT >= 23 ? this.createStaticLayoutForMeasuring(charSequence, (Layout.Alignment)textPaint, Math.round(rectF.right), n2) : this.createStaticLayoutForMeasuringPre23(charSequence, (Layout.Alignment)textPaint, Math.round(rectF.right));
        if (n2 != -1) {
            if (textPaint.getLineCount() <= n2) {
                if (textPaint.getLineEnd(textPaint.getLineCount() - 1) != charSequence.length()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if ((float)textPaint.getHeight() > rectF.bottom) {
            return false;
        }
        return true;
    }

    private boolean supportsAutoSizeText() {
        if (!(this.mTextView instanceof AppCompatEditText)) {
            return true;
        }
        return false;
    }

    private void validateAndSetAutoSizeTextTypeUniformConfiguration(float f, float f2, float f3) throws IllegalArgumentException {
        if (f > 0.0f) {
            if (f2 > f) {
                if (f3 > 0.0f) {
                    this.mAutoSizeTextType = 1;
                    this.mAutoSizeMinTextSizeInPx = f;
                    this.mAutoSizeMaxTextSizeInPx = f2;
                    this.mAutoSizeStepGranularityInPx = f3;
                    this.mHasPresetAutoSizeValues = false;
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("The auto-size step granularity (");
                stringBuilder.append(f3);
                stringBuilder.append("px) is less or equal to (0px)");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Maximum auto-size text size (");
            stringBuilder.append(f2);
            stringBuilder.append("px) is less or equal to minimum auto-size ");
            stringBuilder.append("text size (");
            stringBuilder.append(f);
            stringBuilder.append("px)");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Minimum auto-size text size (");
        stringBuilder.append(f);
        stringBuilder.append("px) is less or equal to (0px)");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void autoSizeText() {
        if (!this.isAutoSizeEnabled()) {
            return;
        }
        if (this.mNeedsAutoSizeText) {
            if (this.mTextView.getMeasuredHeight() <= 0) {
                return;
            }
            if (this.mTextView.getMeasuredWidth() <= 0) {
                return;
            }
            int n = this.invokeAndReturnWithDefault((Object)this.mTextView, "getHorizontallyScrolling", false) != false ? 1048576 : this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft() - this.mTextView.getTotalPaddingRight();
            int n2 = this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom() - this.mTextView.getCompoundPaddingTop();
            if (n <= 0) {
                return;
            }
            if (n2 <= 0) {
                return;
            }
            RectF rectF = TEMP_RECTF;
            synchronized (rectF) {
                TEMP_RECTF.setEmpty();
                AppCompatTextViewAutoSizeHelper.TEMP_RECTF.right = n;
                AppCompatTextViewAutoSizeHelper.TEMP_RECTF.bottom = n2;
                float f = this.findLargestTextSizeWhichFits(TEMP_RECTF);
                if (f != this.mTextView.getTextSize()) {
                    this.setTextSizeInternal(0, f);
                }
            }
        }
        this.mNeedsAutoSizeText = true;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    boolean isAutoSizeEnabled() {
        if (this.supportsAutoSizeText() && this.mAutoSizeTextType != 0) {
            return true;
        }
        return false;
    }

    void loadFromAttributes(AttributeSet attributeSet, int n) {
        float f = -1.0f;
        float f2 = -1.0f;
        float f3 = -1.0f;
        if ((attributeSet = this.mContext.obtainStyledAttributes(attributeSet, R.styleable.AppCompatTextView, n, 0)).hasValue(R.styleable.AppCompatTextView_autoSizeTextType)) {
            this.mAutoSizeTextType = attributeSet.getInt(R.styleable.AppCompatTextView_autoSizeTextType, 0);
        }
        if (attributeSet.hasValue(R.styleable.AppCompatTextView_autoSizeStepGranularity)) {
            f3 = attributeSet.getDimension(R.styleable.AppCompatTextView_autoSizeStepGranularity, -1.0f);
        }
        if (attributeSet.hasValue(R.styleable.AppCompatTextView_autoSizeMinTextSize)) {
            f = attributeSet.getDimension(R.styleable.AppCompatTextView_autoSizeMinTextSize, -1.0f);
        }
        if (attributeSet.hasValue(R.styleable.AppCompatTextView_autoSizeMaxTextSize)) {
            f2 = attributeSet.getDimension(R.styleable.AppCompatTextView_autoSizeMaxTextSize, -1.0f);
        }
        if (attributeSet.hasValue(R.styleable.AppCompatTextView_autoSizePresetSizes) && (n = attributeSet.getResourceId(R.styleable.AppCompatTextView_autoSizePresetSizes, 0)) > 0) {
            TypedArray typedArray = attributeSet.getResources().obtainTypedArray(n);
            this.setupAutoSizeUniformPresetSizes(typedArray);
            typedArray.recycle();
        }
        attributeSet.recycle();
        if (this.supportsAutoSizeText()) {
            if (this.mAutoSizeTextType == 1) {
                if (!this.mHasPresetAutoSizeValues) {
                    attributeSet = this.mContext.getResources().getDisplayMetrics();
                    if (f == -1.0f) {
                        f = TypedValue.applyDimension((int)2, (float)12.0f, (DisplayMetrics)attributeSet);
                    }
                    if (f2 == -1.0f) {
                        f2 = TypedValue.applyDimension((int)2, (float)112.0f, (DisplayMetrics)attributeSet);
                    }
                    if (f3 == -1.0f) {
                        f3 = 1.0f;
                    }
                    this.validateAndSetAutoSizeTextTypeUniformConfiguration(f, f2, f3);
                }
                this.setupAutoSizeText();
                return;
            }
            return;
        }
        this.mAutoSizeTextType = 0;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeUniformWithConfiguration(int n, int n2, int n3, int n4) throws IllegalArgumentException {
        if (this.supportsAutoSizeText()) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension((int)n4, (float)n, (DisplayMetrics)displayMetrics), TypedValue.applyDimension((int)n4, (float)n2, (DisplayMetrics)displayMetrics), TypedValue.applyDimension((int)n4, (float)n3, (DisplayMetrics)displayMetrics));
            if (this.setupAutoSizeText()) {
                this.autoSizeText();
                return;
            }
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] arrn, int n) throws IllegalArgumentException {
        if (this.supportsAutoSizeText()) {
            int n2 = arrn.length;
            if (n2 > 0) {
                Object object;
                int[] arrn2 = new int[n2];
                if (n == 0) {
                    object = Arrays.copyOf(arrn, n2);
                } else {
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    int n3 = 0;
                    do {
                        object = arrn2;
                        if (n3 >= n2) break;
                        arrn2[n3] = Math.round(TypedValue.applyDimension((int)n, (float)arrn[n3], (DisplayMetrics)displayMetrics));
                        ++n3;
                    } while (true);
                }
                this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes((int[])object);
                if (!this.setupAutoSizeUniformPresetSizesConfiguration()) {
                    object = new StringBuilder();
                    object.append("None of the preset sizes is valid: ");
                    object.append(Arrays.toString(arrn));
                    throw new IllegalArgumentException(object.toString());
                }
            } else {
                this.mHasPresetAutoSizeValues = false;
            }
            if (this.setupAutoSizeText()) {
                this.autoSizeText();
                return;
            }
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void setAutoSizeTextTypeWithDefaults(int n) {
        if (this.supportsAutoSizeText()) {
            switch (n) {
                default: {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown auto-size text type: ");
                    stringBuilder.append(n);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                case 1: {
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension((int)2, (float)12.0f, (DisplayMetrics)displayMetrics), TypedValue.applyDimension((int)2, (float)112.0f, (DisplayMetrics)displayMetrics), 1.0f);
                    if (this.setupAutoSizeText()) {
                        this.autoSizeText();
                        return;
                    }
                    return;
                }
                case 0: 
            }
            this.clearAutoSizeConfiguration();
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    void setTextSizeInternal(int n, float f) {
        Context context = this.mContext;
        context = context == null ? Resources.getSystem() : context.getResources();
        this.setRawTextSize(TypedValue.applyDimension((int)n, (float)f, (DisplayMetrics)context.getDisplayMetrics()));
    }
}

