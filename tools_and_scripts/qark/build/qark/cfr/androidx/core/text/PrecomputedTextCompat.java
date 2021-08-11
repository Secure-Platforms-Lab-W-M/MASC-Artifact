/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 *  android.text.Layout
 *  android.text.Layout$Alignment
 *  android.text.PrecomputedText
 *  android.text.PrecomputedText$Params
 *  android.text.Spannable
 *  android.text.SpannableString
 *  android.text.StaticLayout
 *  android.text.StaticLayout$Builder
 *  android.text.TextDirectionHeuristic
 *  android.text.TextDirectionHeuristics
 *  android.text.TextPaint
 *  android.text.TextUtils
 *  android.text.style.MetricAffectingSpan
 */
package androidx.core.text;

import android.graphics.Typeface;
import android.os.Build;
import android.os.LocaleList;
import android.text.Layout;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import androidx.core.os.TraceCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class PrecomputedTextCompat
implements Spannable {
    private static final char LINE_FEED = '\n';
    private static Executor sExecutor;
    private static final Object sLock;
    private final int[] mParagraphEnds;
    private final Params mParams;
    private final Spannable mText;
    private final PrecomputedText mWrapped;

    static {
        sLock = new Object();
        sExecutor = null;
    }

    private PrecomputedTextCompat(PrecomputedText precomputedText, Params params) {
        this.mText = precomputedText;
        this.mParams = params;
        this.mParagraphEnds = null;
        this.mWrapped = null;
    }

    private PrecomputedTextCompat(CharSequence charSequence, Params params, int[] arrn) {
        this.mText = new SpannableString(charSequence);
        this.mParams = params;
        this.mParagraphEnds = arrn;
        this.mWrapped = null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static PrecomputedTextCompat create(CharSequence var0, Params var1_2) {
        block8 : {
            Preconditions.checkNotNull(var0);
            Preconditions.checkNotNull(var1_2);
            try {
                TraceCompat.beginSection("PrecomputedText");
                var4_3 = new ArrayList<Integer>();
                var3_4 = var0.length();
                var2_5 = 0;
                while (var2_5 < var3_4) {
                    var2_5 = (var2_5 = TextUtils.indexOf((CharSequence)var0, (char)'\n', (int)var2_5, (int)var3_4)) < 0 ? var3_4 : ++var2_5;
                    var4_3.add(var2_5);
                }
                var5_6 = new int[var4_3.size()];
                break block8;
            }
            catch (Throwable var0_1) {}
            throw var0_1;
        }
        for (var2_5 = 0; var2_5 < var4_3.size(); ++var2_5) {
            var5_6[var2_5] = (Integer)var4_3.get(var2_5);
        }
        ** try [egrp 1[TRYBLOCK] [5 : 112->206)] { 
lbl21: // 1 sources:
        if (Build.VERSION.SDK_INT >= 23) {
            StaticLayout.Builder.obtain((CharSequence)var0, (int)0, (int)var0.length(), (TextPaint)var1_2.getTextPaint(), (int)Integer.MAX_VALUE).setBreakStrategy(var1_2.getBreakStrategy()).setHyphenationFrequency(var1_2.getHyphenationFrequency()).setTextDirection(var1_2.getTextDirection()).build();
            return new PrecomputedTextCompat((CharSequence)var0, var1_2, var5_6);
        } else {
            if (Build.VERSION.SDK_INT < 21) return new PrecomputedTextCompat((CharSequence)var0, var1_2, var5_6);
            new StaticLayout((CharSequence)var0, var1_2.getTextPaint(), Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        return new PrecomputedTextCompat((CharSequence)var0, var1_2, var5_6);
lbl27: // 1 sources:
        finally {
            TraceCompat.endSection();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Future<PrecomputedTextCompat> getTextFuture(CharSequence object, Params object2, Executor object3) {
        object2 = new PrecomputedTextFutureTask((Params)object2, (CharSequence)object);
        object = object3;
        if (object3 == null) {
            object3 = sLock;
            synchronized (object3) {
                if (sExecutor == null) {
                    sExecutor = Executors.newFixedThreadPool(1);
                }
                object = sExecutor;
            }
        }
        object.execute((Runnable)object2);
        return object2;
    }

    public char charAt(int n) {
        return this.mText.charAt(n);
    }

    public int getParagraphCount() {
        return this.mParagraphEnds.length;
    }

    public int getParagraphEnd(int n) {
        Preconditions.checkArgumentInRange(n, 0, this.getParagraphCount(), "paraIndex");
        return this.mParagraphEnds[n];
    }

    public int getParagraphStart(int n) {
        Preconditions.checkArgumentInRange(n, 0, this.getParagraphCount(), "paraIndex");
        if (n == 0) {
            return 0;
        }
        return this.mParagraphEnds[n - 1];
    }

    public Params getParams() {
        return this.mParams;
    }

    public PrecomputedText getPrecomputedText() {
        Spannable spannable = this.mText;
        if (spannable instanceof PrecomputedText) {
            return (PrecomputedText)spannable;
        }
        return null;
    }

    public int getSpanEnd(Object object) {
        return this.mText.getSpanEnd(object);
    }

    public int getSpanFlags(Object object) {
        return this.mText.getSpanFlags(object);
    }

    public int getSpanStart(Object object) {
        return this.mText.getSpanStart(object);
    }

    public <T> T[] getSpans(int n, int n2, Class<T> class_) {
        return this.mText.getSpans(n, n2, class_);
    }

    public int length() {
        return this.mText.length();
    }

    public int nextSpanTransition(int n, int n2, Class class_) {
        return this.mText.nextSpanTransition(n, n2, class_);
    }

    public void removeSpan(Object object) {
        if (!(object instanceof MetricAffectingSpan)) {
            this.mText.removeSpan(object);
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
    }

    public void setSpan(Object object, int n, int n2, int n3) {
        if (!(object instanceof MetricAffectingSpan)) {
            this.mText.setSpan(object, n, n2, n3);
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
    }

    public CharSequence subSequence(int n, int n2) {
        return this.mText.subSequence(n, n2);
    }

    public String toString() {
        return this.mText.toString();
    }

    public static final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        private final TextPaint mPaint;
        private final TextDirectionHeuristic mTextDir;
        final PrecomputedText.Params mWrapped;

        public Params(PrecomputedText.Params params) {
            this.mPaint = params.getTextPaint();
            this.mTextDir = params.getTextDirection();
            this.mBreakStrategy = params.getBreakStrategy();
            this.mHyphenationFrequency = params.getHyphenationFrequency();
            this.mWrapped = null;
        }

        Params(TextPaint textPaint, TextDirectionHeuristic textDirectionHeuristic, int n, int n2) {
            this.mWrapped = null;
            this.mPaint = textPaint;
            this.mTextDir = textDirectionHeuristic;
            this.mBreakStrategy = n;
            this.mHyphenationFrequency = n2;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Params)) {
                return false;
            }
            if (!this.equalsWithoutTextDirection((Params)(object = (Params)object))) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 18 && this.mTextDir != object.getTextDirection()) {
                return false;
            }
            return true;
        }

        public boolean equalsWithoutTextDirection(Params params) {
            PrecomputedText.Params params2 = this.mWrapped;
            if (params2 != null) {
                return params2.equals((Object)params.mWrapped);
            }
            if (Build.VERSION.SDK_INT >= 23) {
                if (this.mBreakStrategy != params.getBreakStrategy()) {
                    return false;
                }
                if (this.mHyphenationFrequency != params.getHyphenationFrequency()) {
                    return false;
                }
            }
            if (this.mPaint.getTextSize() != params.getTextPaint().getTextSize()) {
                return false;
            }
            if (this.mPaint.getTextScaleX() != params.getTextPaint().getTextScaleX()) {
                return false;
            }
            if (this.mPaint.getTextSkewX() != params.getTextPaint().getTextSkewX()) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                if (this.mPaint.getLetterSpacing() != params.getTextPaint().getLetterSpacing()) {
                    return false;
                }
                if (!TextUtils.equals((CharSequence)this.mPaint.getFontFeatureSettings(), (CharSequence)params.getTextPaint().getFontFeatureSettings())) {
                    return false;
                }
            }
            if (this.mPaint.getFlags() != params.getTextPaint().getFlags()) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 24 ? !this.mPaint.getTextLocales().equals((Object)params.getTextPaint().getTextLocales()) : Build.VERSION.SDK_INT >= 17 && !this.mPaint.getTextLocale().equals(params.getTextPaint().getTextLocale())) {
                return false;
            }
            if (this.mPaint.getTypeface() == null ? params.getTextPaint().getTypeface() != null : !this.mPaint.getTypeface().equals((Object)params.getTextPaint().getTypeface())) {
                return false;
            }
            return true;
        }

        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        public int hashCode() {
            if (Build.VERSION.SDK_INT >= 24) {
                return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), this.mPaint.getFlags(), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
            }
            if (Build.VERSION.SDK_INT >= 21) {
                return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
            }
            if (Build.VERSION.SDK_INT >= 18) {
                return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
            }
            if (Build.VERSION.SDK_INT >= 17) {
                return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
            }
            return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), this.mPaint.getFlags(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency});
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("{");
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("textSize=");
            stringBuilder2.append(this.mPaint.getTextSize());
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textScaleX=");
            stringBuilder2.append(this.mPaint.getTextScaleX());
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textSkewX=");
            stringBuilder2.append(this.mPaint.getTextSkewX());
            stringBuilder.append(stringBuilder2.toString());
            if (Build.VERSION.SDK_INT >= 21) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", letterSpacing=");
                stringBuilder2.append(this.mPaint.getLetterSpacing());
                stringBuilder.append(stringBuilder2.toString());
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", elegantTextHeight=");
                stringBuilder2.append(this.mPaint.isElegantTextHeight());
                stringBuilder.append(stringBuilder2.toString());
            }
            if (Build.VERSION.SDK_INT >= 24) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", textLocale=");
                stringBuilder2.append((Object)this.mPaint.getTextLocales());
                stringBuilder.append(stringBuilder2.toString());
            } else if (Build.VERSION.SDK_INT >= 17) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", textLocale=");
                stringBuilder2.append(this.mPaint.getTextLocale());
                stringBuilder.append(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", typeface=");
            stringBuilder2.append((Object)this.mPaint.getTypeface());
            stringBuilder.append(stringBuilder2.toString());
            if (Build.VERSION.SDK_INT >= 26) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(", variationSettings=");
                stringBuilder2.append(this.mPaint.getFontVariationSettings());
                stringBuilder.append(stringBuilder2.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", textDir=");
            stringBuilder2.append((Object)this.mTextDir);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", breakStrategy=");
            stringBuilder2.append(this.mBreakStrategy);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(", hyphenationFrequency=");
            stringBuilder2.append(this.mHyphenationFrequency);
            stringBuilder.append(stringBuilder2.toString());
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        public static class Builder {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;

            public Builder(TextPaint textPaint) {
                this.mPaint = textPaint;
                if (Build.VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                } else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                if (Build.VERSION.SDK_INT >= 18) {
                    this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                    return;
                }
                this.mTextDir = null;
            }

            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }

            public Builder setBreakStrategy(int n) {
                this.mBreakStrategy = n;
                return this;
            }

            public Builder setHyphenationFrequency(int n) {
                this.mHyphenationFrequency = n;
                return this;
            }

            public Builder setTextDirection(TextDirectionHeuristic textDirectionHeuristic) {
                this.mTextDir = textDirectionHeuristic;
                return this;
            }
        }

    }

    private static class PrecomputedTextFutureTask
    extends FutureTask<PrecomputedTextCompat> {
        PrecomputedTextFutureTask(Params params, CharSequence charSequence) {
            super(new PrecomputedTextCallback(params, charSequence));
        }

        private static class PrecomputedTextCallback
        implements Callable<PrecomputedTextCompat> {
            private Params mParams;
            private CharSequence mText;

            PrecomputedTextCallback(Params params, CharSequence charSequence) {
                this.mParams = params;
                this.mText = charSequence;
            }

            @Override
            public PrecomputedTextCompat call() throws Exception {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }
        }

    }

}

