// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import androidx.appcompat.R$styleable;
import android.util.AttributeSet;
import android.text.method.TransformationMethod;
import android.view.View;
import android.content.res.TypedArray;
import android.text.TextDirectionHeuristics;
import android.text.TextDirectionHeuristic;
import android.os.Build$VERSION;
import android.text.StaticLayout$Builder;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import android.util.Log;
import android.widget.TextView;
import android.text.TextPaint;
import android.content.Context;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import android.graphics.RectF;

class AppCompatTextViewAutoSizeHelper
{
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final String TAG = "ACTVAutoSizeHelper";
    private static final RectF TEMP_RECTF;
    static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0f;
    private static final int VERY_WIDE = 1048576;
    private static ConcurrentHashMap<String, Field> sTextViewFieldByNameCache;
    private static ConcurrentHashMap<String, Method> sTextViewMethodByNameCache;
    private float mAutoSizeMaxTextSizeInPx;
    private float mAutoSizeMinTextSizeInPx;
    private float mAutoSizeStepGranularityInPx;
    private int[] mAutoSizeTextSizesInPx;
    private int mAutoSizeTextType;
    private final Context mContext;
    private boolean mHasPresetAutoSizeValues;
    private boolean mNeedsAutoSizeText;
    private TextPaint mTempTextPaint;
    private final TextView mTextView;
    
    static {
        TEMP_RECTF = new RectF();
        AppCompatTextViewAutoSizeHelper.sTextViewMethodByNameCache = new ConcurrentHashMap<String, Method>();
        AppCompatTextViewAutoSizeHelper.sTextViewFieldByNameCache = new ConcurrentHashMap<String, Field>();
    }
    
    AppCompatTextViewAutoSizeHelper(final TextView mTextView) {
        this.mAutoSizeTextType = 0;
        this.mNeedsAutoSizeText = false;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mHasPresetAutoSizeValues = false;
        this.mTextView = mTextView;
        this.mContext = mTextView.getContext();
    }
    
    private static <T> T accessAndReturnWithDefault(Object value, final String s, final T t) {
        try {
            final Field textViewField = getTextViewField(s);
            if (textViewField == null) {
                return t;
            }
            value = textViewField.get(value);
            return (T)value;
        }
        catch (IllegalAccessException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to access TextView#");
            sb.append(s);
            sb.append(" member");
            Log.w("ACTVAutoSizeHelper", sb.toString(), (Throwable)ex);
            return t;
        }
    }
    
    private int[] cleanupAutoSizePresetSizes(int[] array) {
        final int length = array.length;
        if (length == 0) {
            return array;
        }
        Arrays.sort(array);
        final ArrayList<Comparable<? super Integer>> list = new ArrayList<Comparable<? super Integer>>();
        for (int i = 0; i < length; ++i) {
            final int n = array[i];
            if (n > 0 && Collections.binarySearch(list, n) < 0) {
                list.add(n);
            }
        }
        if (length == list.size()) {
            return array;
        }
        final int size = list.size();
        array = new int[size];
        for (int j = 0; j < size; ++j) {
            array[j] = list.get(j);
        }
        return array;
    }
    
    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mNeedsAutoSizeText = false;
    }
    
    private StaticLayout createStaticLayoutForMeasuring(final CharSequence charSequence, final Layout$Alignment alignment, final int n, int maxLines) {
        final StaticLayout$Builder obtain = StaticLayout$Builder.obtain(charSequence, 0, charSequence.length(), this.mTempTextPaint, n);
        final StaticLayout$Builder setHyphenationFrequency = obtain.setAlignment(alignment).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
        if (maxLines == -1) {
            maxLines = Integer.MAX_VALUE;
        }
        setHyphenationFrequency.setMaxLines(maxLines);
        try {
            TextDirectionHeuristic textDirectionHeuristic;
            if (Build$VERSION.SDK_INT >= 29) {
                textDirectionHeuristic = this.mTextView.getTextDirectionHeuristic();
            }
            else {
                textDirectionHeuristic = invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR);
            }
            obtain.setTextDirection(textDirectionHeuristic);
        }
        catch (ClassCastException ex) {
            Log.w("ACTVAutoSizeHelper", "Failed to obtain TextDirectionHeuristic, auto size may be incorrect");
        }
        return obtain.build();
    }
    
    private StaticLayout createStaticLayoutForMeasuringPre16(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final int n) {
        return new StaticLayout(charSequence, this.mTempTextPaint, n, layout$Alignment, (float)accessAndReturnWithDefault(this.mTextView, "mSpacingMult", 1.0f), (float)accessAndReturnWithDefault(this.mTextView, "mSpacingAdd", 0.0f), (boolean)accessAndReturnWithDefault(this.mTextView, "mIncludePad", true));
    }
    
    private StaticLayout createStaticLayoutForMeasuringPre23(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final int n) {
        return new StaticLayout(charSequence, this.mTempTextPaint, n, layout$Alignment, this.mTextView.getLineSpacingMultiplier(), this.mTextView.getLineSpacingExtra(), this.mTextView.getIncludeFontPadding());
    }
    
    private int findLargestTextSizeWhichFits(final RectF rectF) {
        final int length = this.mAutoSizeTextSizesInPx.length;
        if (length != 0) {
            int n = 0;
            int i = 0 + 1;
            int n2 = length - 1;
            while (i <= n2) {
                final int n3 = (i + n2) / 2;
                if (this.suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[n3], rectF)) {
                    final int n4 = n3 + 1;
                    n = i;
                    i = n4;
                }
                else {
                    n2 = (n = n3 - 1);
                }
            }
            return this.mAutoSizeTextSizesInPx[n];
        }
        throw new IllegalStateException("No available text sizes to choose from.");
    }
    
    private static Field getTextViewField(final String s) {
        try {
            Field field;
            if ((field = AppCompatTextViewAutoSizeHelper.sTextViewFieldByNameCache.get(s)) == null) {
                final Field declaredField = TextView.class.getDeclaredField(s);
                if ((field = declaredField) != null) {
                    declaredField.setAccessible(true);
                    AppCompatTextViewAutoSizeHelper.sTextViewFieldByNameCache.put(s, declaredField);
                    field = declaredField;
                }
            }
            return field;
        }
        catch (NoSuchFieldException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to access TextView#");
            sb.append(s);
            sb.append(" member");
            Log.w("ACTVAutoSizeHelper", sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    private static Method getTextViewMethod(final String s) {
        try {
            Method method;
            if ((method = AppCompatTextViewAutoSizeHelper.sTextViewMethodByNameCache.get(s)) == null) {
                final Method declaredMethod = TextView.class.getDeclaredMethod(s, (Class<?>[])new Class[0]);
                if ((method = declaredMethod) != null) {
                    declaredMethod.setAccessible(true);
                    AppCompatTextViewAutoSizeHelper.sTextViewMethodByNameCache.put(s, declaredMethod);
                    method = declaredMethod;
                }
            }
            return method;
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to retrieve TextView#");
            sb.append(s);
            sb.append("() method");
            Log.w("ACTVAutoSizeHelper", sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    private static <T> T invokeAndReturnWithDefault(Object invoke, String s, final T t) {
        final Object o = null;
        final boolean b = false;
        try {
            try {
                invoke = getTextViewMethod(s).invoke(invoke, new Object[0]);
                s = (String)(invoke = invoke);
                if (s != null) {
                    return (T)invoke;
                }
                invoke = s;
                if (false) {
                    return t;
                }
                return (T)invoke;
            }
            finally {
                if (false || b) {}
                invoke = o;
                // iftrue(Label_0120:, !true)
                return t;
                Label_0120: {
                    return (T)invoke;
                }
            }
        }
        catch (Exception ex) {}
    }
    
    private void setRawTextSize(final float textSize) {
        if (textSize != this.mTextView.getPaint().getTextSize()) {
            this.mTextView.getPaint().setTextSize(textSize);
            boolean inLayout = false;
            if (Build$VERSION.SDK_INT >= 18) {
                inLayout = this.mTextView.isInLayout();
            }
            if (this.mTextView.getLayout() != null) {
                this.mNeedsAutoSizeText = false;
                try {
                    final Method textViewMethod = getTextViewMethod("nullLayouts");
                    if (textViewMethod != null) {
                        textViewMethod.invoke(this.mTextView, new Object[0]);
                    }
                }
                catch (Exception ex) {
                    Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#nullLayouts() method", (Throwable)ex);
                }
                if (!inLayout) {
                    this.mTextView.requestLayout();
                }
                else {
                    this.mTextView.forceLayout();
                }
                this.mTextView.invalidate();
            }
        }
    }
    
    private boolean setupAutoSizeText() {
        if (this.supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                final int n = (int)Math.floor((this.mAutoSizeMaxTextSizeInPx - this.mAutoSizeMinTextSizeInPx) / this.mAutoSizeStepGranularityInPx) + 1;
                final int[] array = new int[n];
                for (int i = 0; i < n; ++i) {
                    array[i] = Math.round(this.mAutoSizeMinTextSizeInPx + i * this.mAutoSizeStepGranularityInPx);
                }
                this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(array);
            }
            this.mNeedsAutoSizeText = true;
        }
        else {
            this.mNeedsAutoSizeText = false;
        }
        return this.mNeedsAutoSizeText;
    }
    
    private void setupAutoSizeUniformPresetSizes(final TypedArray typedArray) {
        final int length = typedArray.length();
        final int[] array = new int[length];
        if (length > 0) {
            for (int i = 0; i < length; ++i) {
                array[i] = typedArray.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(array);
            this.setupAutoSizeUniformPresetSizesConfiguration();
        }
    }
    
    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        final int length = this.mAutoSizeTextSizesInPx.length;
        final boolean mHasPresetAutoSizeValues = length > 0;
        this.mHasPresetAutoSizeValues = mHasPresetAutoSizeValues;
        if (mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            final int[] mAutoSizeTextSizesInPx = this.mAutoSizeTextSizesInPx;
            this.mAutoSizeMinTextSizeInPx = (float)mAutoSizeTextSizesInPx[0];
            this.mAutoSizeMaxTextSizeInPx = (float)mAutoSizeTextSizesInPx[length - 1];
            this.mAutoSizeStepGranularityInPx = -1.0f;
        }
        return this.mHasPresetAutoSizeValues;
    }
    
    private boolean suggestedSizeFitsInSpace(final int n, final RectF rectF) {
        final CharSequence text = this.mTextView.getText();
        final TransformationMethod transformationMethod = this.mTextView.getTransformationMethod();
        CharSequence charSequence = text;
        if (transformationMethod != null) {
            final CharSequence transformation = transformationMethod.getTransformation(text, (View)this.mTextView);
            charSequence = text;
            if (transformation != null) {
                charSequence = transformation;
            }
        }
        int maxLines;
        if (Build$VERSION.SDK_INT >= 16) {
            maxLines = this.mTextView.getMaxLines();
        }
        else {
            maxLines = -1;
        }
        this.initTempTextPaint(n);
        final StaticLayout layout = this.createLayout(charSequence, invokeAndReturnWithDefault(this.mTextView, "getLayoutAlignment", Layout$Alignment.ALIGN_NORMAL), Math.round(rectF.right), maxLines);
        return (maxLines == -1 || (layout.getLineCount() <= maxLines && layout.getLineEnd(layout.getLineCount() - 1) == charSequence.length())) && layout.getHeight() <= rectF.bottom;
    }
    
    private boolean supportsAutoSizeText() {
        return this.mTextView instanceof AppCompatEditText ^ true;
    }
    
    private void validateAndSetAutoSizeTextTypeUniformConfiguration(final float mAutoSizeMinTextSizeInPx, final float mAutoSizeMaxTextSizeInPx, final float mAutoSizeStepGranularityInPx) throws IllegalArgumentException {
        if (mAutoSizeMinTextSizeInPx <= 0.0f) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Minimum auto-size text size (");
            sb.append(mAutoSizeMinTextSizeInPx);
            sb.append("px) is less or equal to (0px)");
            throw new IllegalArgumentException(sb.toString());
        }
        if (mAutoSizeMaxTextSizeInPx <= mAutoSizeMinTextSizeInPx) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Maximum auto-size text size (");
            sb2.append(mAutoSizeMaxTextSizeInPx);
            sb2.append("px) is less or equal to minimum auto-size text size (");
            sb2.append(mAutoSizeMinTextSizeInPx);
            sb2.append("px)");
            throw new IllegalArgumentException(sb2.toString());
        }
        if (mAutoSizeStepGranularityInPx > 0.0f) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = mAutoSizeMinTextSizeInPx;
            this.mAutoSizeMaxTextSizeInPx = mAutoSizeMaxTextSizeInPx;
            this.mAutoSizeStepGranularityInPx = mAutoSizeStepGranularityInPx;
            this.mHasPresetAutoSizeValues = false;
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("The auto-size step granularity (");
        sb3.append(mAutoSizeStepGranularityInPx);
        sb3.append("px) is less or equal to (0px)");
        throw new IllegalArgumentException(sb3.toString());
    }
    
    void autoSizeText() {
        if (!this.isAutoSizeEnabled()) {
            return;
        }
        Label_0219: {
            if (this.mNeedsAutoSizeText) {
                if (this.mTextView.getMeasuredHeight() <= 0) {
                    return;
                }
                if (this.mTextView.getMeasuredWidth() <= 0) {
                    return;
                }
                boolean b;
                if (Build$VERSION.SDK_INT >= 29) {
                    b = this.mTextView.isHorizontallyScrollable();
                }
                else {
                    b = invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", false);
                }
                int n;
                if (b) {
                    n = 1048576;
                }
                else {
                    n = this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft() - this.mTextView.getTotalPaddingRight();
                }
                final int n2 = this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom() - this.mTextView.getCompoundPaddingTop();
                if (n > 0) {
                    if (n2 <= 0) {
                        return;
                    }
                    synchronized (AppCompatTextViewAutoSizeHelper.TEMP_RECTF) {
                        AppCompatTextViewAutoSizeHelper.TEMP_RECTF.setEmpty();
                        AppCompatTextViewAutoSizeHelper.TEMP_RECTF.right = (float)n;
                        AppCompatTextViewAutoSizeHelper.TEMP_RECTF.bottom = (float)n2;
                        final float n3 = (float)this.findLargestTextSizeWhichFits(AppCompatTextViewAutoSizeHelper.TEMP_RECTF);
                        if (n3 != this.mTextView.getTextSize()) {
                            this.setTextSizeInternal(0, n3);
                        }
                        break Label_0219;
                    }
                }
                return;
            }
        }
        this.mNeedsAutoSizeText = true;
    }
    
    StaticLayout createLayout(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 23) {
            return this.createStaticLayoutForMeasuring(charSequence, layout$Alignment, n, n2);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            return this.createStaticLayoutForMeasuringPre23(charSequence, layout$Alignment, n);
        }
        return this.createStaticLayoutForMeasuringPre16(charSequence, layout$Alignment, n);
    }
    
    int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }
    
    int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }
    
    int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }
    
    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }
    
    int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }
    
    void initTempTextPaint(final int n) {
        final TextPaint mTempTextPaint = this.mTempTextPaint;
        if (mTempTextPaint == null) {
            this.mTempTextPaint = new TextPaint();
        }
        else {
            mTempTextPaint.reset();
        }
        this.mTempTextPaint.set(this.mTextView.getPaint());
        this.mTempTextPaint.setTextSize((float)n);
    }
    
    boolean isAutoSizeEnabled() {
        return this.supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }
    
    void loadFromAttributes(final AttributeSet set, int resourceId) {
        float dimension = -1.0f;
        float dimension2 = -1.0f;
        float dimension3 = -1.0f;
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(set, R$styleable.AppCompatTextView, resourceId, 0);
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizeTextType)) {
            this.mAutoSizeTextType = obtainStyledAttributes.getInt(R$styleable.AppCompatTextView_autoSizeTextType, 0);
        }
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizeStepGranularity)) {
            dimension3 = obtainStyledAttributes.getDimension(R$styleable.AppCompatTextView_autoSizeStepGranularity, -1.0f);
        }
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizeMinTextSize)) {
            dimension = obtainStyledAttributes.getDimension(R$styleable.AppCompatTextView_autoSizeMinTextSize, -1.0f);
        }
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizeMaxTextSize)) {
            dimension2 = obtainStyledAttributes.getDimension(R$styleable.AppCompatTextView_autoSizeMaxTextSize, -1.0f);
        }
        if (obtainStyledAttributes.hasValue(R$styleable.AppCompatTextView_autoSizePresetSizes)) {
            resourceId = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_autoSizePresetSizes, 0);
            if (resourceId > 0) {
                final TypedArray obtainTypedArray = obtainStyledAttributes.getResources().obtainTypedArray(resourceId);
                this.setupAutoSizeUniformPresetSizes(obtainTypedArray);
                obtainTypedArray.recycle();
            }
        }
        obtainStyledAttributes.recycle();
        if (this.supportsAutoSizeText()) {
            if (this.mAutoSizeTextType == 1) {
                if (!this.mHasPresetAutoSizeValues) {
                    final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    float applyDimension = dimension;
                    if (dimension == -1.0f) {
                        applyDimension = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                    }
                    float applyDimension2 = dimension2;
                    if (dimension2 == -1.0f) {
                        applyDimension2 = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                    }
                    float n = dimension3;
                    if (dimension3 == -1.0f) {
                        n = 1.0f;
                    }
                    this.validateAndSetAutoSizeTextTypeUniformConfiguration(applyDimension, applyDimension2, n);
                }
                this.setupAutoSizeText();
            }
        }
        else {
            this.mAutoSizeTextType = 0;
        }
    }
    
    void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (this.supportsAutoSizeText()) {
            final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(n4, (float)n, displayMetrics), TypedValue.applyDimension(n4, (float)n2, displayMetrics), TypedValue.applyDimension(n4, (float)n3, displayMetrics));
            if (this.setupAutoSizeText()) {
                this.autoSizeText();
            }
        }
    }
    
    void setAutoSizeTextTypeUniformWithPresetSizes(final int[] array, final int n) throws IllegalArgumentException {
        if (this.supportsAutoSizeText()) {
            final int length = array.length;
            if (length > 0) {
                final int[] array2 = new int[length];
                int[] copy;
                if (n == 0) {
                    copy = Arrays.copyOf(array, length);
                }
                else {
                    final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    int n2 = 0;
                    while (true) {
                        copy = array2;
                        if (n2 >= length) {
                            break;
                        }
                        array2[n2] = Math.round(TypedValue.applyDimension(n, (float)array[n2], displayMetrics));
                        ++n2;
                    }
                }
                this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(copy);
                if (!this.setupAutoSizeUniformPresetSizesConfiguration()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("None of the preset sizes is valid: ");
                    sb.append(Arrays.toString(array));
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            else {
                this.mHasPresetAutoSizeValues = false;
            }
            if (this.setupAutoSizeText()) {
                this.autoSizeText();
            }
        }
    }
    
    void setAutoSizeTextTypeWithDefaults(final int n) {
        if (this.supportsAutoSizeText()) {
            if (n != 0) {
                if (n != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown auto-size text type: ");
                    sb.append(n);
                    throw new IllegalArgumentException(sb.toString());
                }
                final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
                if (this.setupAutoSizeText()) {
                    this.autoSizeText();
                }
            }
            else {
                this.clearAutoSizeConfiguration();
            }
        }
    }
    
    void setTextSizeInternal(final int n, final float n2) {
        final Context mContext = this.mContext;
        Resources resources;
        if (mContext == null) {
            resources = Resources.getSystem();
        }
        else {
            resources = mContext.getResources();
        }
        this.setRawTextSize(TypedValue.applyDimension(n, n2, resources.getDisplayMetrics()));
    }
}
