// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.text;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import android.text.TextDirectionHeuristics;
import androidx.core.util.ObjectsCompat;
import android.text.PrecomputedText$Params;
import android.text.TextDirectionHeuristic;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.text.Layout$Alignment;
import android.text.StaticLayout;
import android.text.StaticLayout$Builder;
import android.os.Build$VERSION;
import android.text.TextUtils;
import java.util.ArrayList;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import android.text.SpannableString;
import android.text.PrecomputedText;
import java.util.concurrent.Executor;
import android.text.Spannable;

public class PrecomputedTextCompat implements Spannable
{
    private static final char LINE_FEED = '\n';
    private static Executor sExecutor;
    private static final Object sLock;
    private final int[] mParagraphEnds;
    private final Params mParams;
    private final Spannable mText;
    private final PrecomputedText mWrapped;
    
    static {
        sLock = new Object();
        PrecomputedTextCompat.sExecutor = null;
    }
    
    private PrecomputedTextCompat(final PrecomputedText mText, final Params mParams) {
        this.mText = (Spannable)mText;
        this.mParams = mParams;
        this.mParagraphEnds = null;
        this.mWrapped = null;
    }
    
    private PrecomputedTextCompat(final CharSequence charSequence, final Params mParams, final int[] mParagraphEnds) {
        this.mText = (Spannable)new SpannableString(charSequence);
        this.mParams = mParams;
        this.mParagraphEnds = mParagraphEnds;
        this.mWrapped = null;
    }
    
    public static PrecomputedTextCompat create(final CharSequence charSequence, final Params params) {
        while (true) {
            Preconditions.checkNotNull(charSequence);
            Preconditions.checkNotNull(params);
            while (true) {
                int i = 0;
                Label_0217: {
                    try {
                        TraceCompat.beginSection("PrecomputedText");
                        final ArrayList<Integer> list = new ArrayList<Integer>();
                        final int length = charSequence.length();
                        i = 0;
                        while (i < length) {
                            i = TextUtils.indexOf(charSequence, '\n', i, length);
                            if (i >= 0) {
                                break Label_0217;
                            }
                            i = length;
                            list.add(i);
                        }
                        final int[] array = new int[list.size()];
                        for (i = 0; i < list.size(); ++i) {
                            array[i] = list.get(i);
                        }
                        if (Build$VERSION.SDK_INT >= 23) {
                            StaticLayout$Builder.obtain(charSequence, 0, charSequence.length(), params.getTextPaint(), Integer.MAX_VALUE).setBreakStrategy(params.getBreakStrategy()).setHyphenationFrequency(params.getHyphenationFrequency()).setTextDirection(params.getTextDirection()).build();
                        }
                        else if (Build$VERSION.SDK_INT >= 21) {
                            new StaticLayout(charSequence, params.getTextPaint(), Integer.MAX_VALUE, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        }
                        return new PrecomputedTextCompat(charSequence, params, array);
                    }
                    finally {
                        TraceCompat.endSection();
                    }
                }
                ++i;
                continue;
            }
        }
    }
    
    public static Future<PrecomputedTextCompat> getTextFuture(final CharSequence charSequence, Params params, final Executor executor) {
        params = (Params)new PrecomputedTextFutureTask(params, charSequence);
        final Executor executor2 = executor;
        if (executor == null) {
            synchronized (PrecomputedTextCompat.sLock) {
                if (PrecomputedTextCompat.sExecutor == null) {
                    PrecomputedTextCompat.sExecutor = Executors.newFixedThreadPool(1);
                }
                final Executor sExecutor = PrecomputedTextCompat.sExecutor;
            }
        }
        executor2.execute((Runnable)params);
        return (Future<PrecomputedTextCompat>)params;
    }
    
    public char charAt(final int n) {
        return this.mText.charAt(n);
    }
    
    public int getParagraphCount() {
        return this.mParagraphEnds.length;
    }
    
    public int getParagraphEnd(final int n) {
        Preconditions.checkArgumentInRange(n, 0, this.getParagraphCount(), "paraIndex");
        return this.mParagraphEnds[n];
    }
    
    public int getParagraphStart(final int n) {
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
        final Spannable mText = this.mText;
        if (mText instanceof PrecomputedText) {
            return (PrecomputedText)mText;
        }
        return null;
    }
    
    public int getSpanEnd(final Object o) {
        return this.mText.getSpanEnd(o);
    }
    
    public int getSpanFlags(final Object o) {
        return this.mText.getSpanFlags(o);
    }
    
    public int getSpanStart(final Object o) {
        return this.mText.getSpanStart(o);
    }
    
    public <T> T[] getSpans(final int n, final int n2, final Class<T> clazz) {
        return (T[])this.mText.getSpans(n, n2, (Class)clazz);
    }
    
    public int length() {
        return this.mText.length();
    }
    
    public int nextSpanTransition(final int n, final int n2, final Class clazz) {
        return this.mText.nextSpanTransition(n, n2, clazz);
    }
    
    public void removeSpan(final Object o) {
        if (!(o instanceof MetricAffectingSpan)) {
            this.mText.removeSpan(o);
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
    }
    
    public void setSpan(final Object o, final int n, final int n2, final int n3) {
        if (!(o instanceof MetricAffectingSpan)) {
            this.mText.setSpan(o, n, n2, n3);
            return;
        }
        throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
    }
    
    public CharSequence subSequence(final int n, final int n2) {
        return this.mText.subSequence(n, n2);
    }
    
    @Override
    public String toString() {
        return this.mText.toString();
    }
    
    public static final class Params
    {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        private final TextPaint mPaint;
        private final TextDirectionHeuristic mTextDir;
        final PrecomputedText$Params mWrapped;
        
        public Params(final PrecomputedText$Params precomputedText$Params) {
            this.mPaint = precomputedText$Params.getTextPaint();
            this.mTextDir = precomputedText$Params.getTextDirection();
            this.mBreakStrategy = precomputedText$Params.getBreakStrategy();
            this.mHyphenationFrequency = precomputedText$Params.getHyphenationFrequency();
            this.mWrapped = null;
        }
        
        Params(final TextPaint mPaint, final TextDirectionHeuristic mTextDir, final int mBreakStrategy, final int mHyphenationFrequency) {
            this.mWrapped = null;
            this.mPaint = mPaint;
            this.mTextDir = mTextDir;
            this.mBreakStrategy = mBreakStrategy;
            this.mHyphenationFrequency = mHyphenationFrequency;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Params)) {
                return false;
            }
            final Params params = (Params)o;
            return this.equalsWithoutTextDirection(params) && (Build$VERSION.SDK_INT < 18 || this.mTextDir == params.getTextDirection());
        }
        
        public boolean equalsWithoutTextDirection(final Params params) {
            final PrecomputedText$Params mWrapped = this.mWrapped;
            if (mWrapped != null) {
                return mWrapped.equals((Object)params.mWrapped);
            }
            if (Build$VERSION.SDK_INT >= 23) {
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
            if (Build$VERSION.SDK_INT >= 21) {
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
            if (Build$VERSION.SDK_INT >= 24) {
                if (!this.mPaint.getTextLocales().equals((Object)params.getTextPaint().getTextLocales())) {
                    return false;
                }
            }
            else if (Build$VERSION.SDK_INT >= 17 && !this.mPaint.getTextLocale().equals(params.getTextPaint().getTextLocale())) {
                return false;
            }
            if (this.mPaint.getTypeface() == null) {
                if (params.getTextPaint().getTypeface() != null) {
                    return false;
                }
            }
            else if (!this.mPaint.getTypeface().equals((Object)params.getTextPaint().getTypeface())) {
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
        
        @Override
        public int hashCode() {
            if (Build$VERSION.SDK_INT >= 24) {
                return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getLetterSpacing(), this.mPaint.getFlags(), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build$VERSION.SDK_INT >= 21) {
                return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getLetterSpacing(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build$VERSION.SDK_INT >= 18) {
                return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build$VERSION.SDK_INT >= 17) {
                return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("textSize=");
            sb2.append(this.mPaint.getTextSize());
            sb.append(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(", textScaleX=");
            sb3.append(this.mPaint.getTextScaleX());
            sb.append(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(", textSkewX=");
            sb4.append(this.mPaint.getTextSkewX());
            sb.append(sb4.toString());
            if (Build$VERSION.SDK_INT >= 21) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(", letterSpacing=");
                sb5.append(this.mPaint.getLetterSpacing());
                sb.append(sb5.toString());
                final StringBuilder sb6 = new StringBuilder();
                sb6.append(", elegantTextHeight=");
                sb6.append(this.mPaint.isElegantTextHeight());
                sb.append(sb6.toString());
            }
            if (Build$VERSION.SDK_INT >= 24) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(", textLocale=");
                sb7.append(this.mPaint.getTextLocales());
                sb.append(sb7.toString());
            }
            else if (Build$VERSION.SDK_INT >= 17) {
                final StringBuilder sb8 = new StringBuilder();
                sb8.append(", textLocale=");
                sb8.append(this.mPaint.getTextLocale());
                sb.append(sb8.toString());
            }
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(", typeface=");
            sb9.append(this.mPaint.getTypeface());
            sb.append(sb9.toString());
            if (Build$VERSION.SDK_INT >= 26) {
                final StringBuilder sb10 = new StringBuilder();
                sb10.append(", variationSettings=");
                sb10.append(this.mPaint.getFontVariationSettings());
                sb.append(sb10.toString());
            }
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(", textDir=");
            sb11.append(this.mTextDir);
            sb.append(sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(", breakStrategy=");
            sb12.append(this.mBreakStrategy);
            sb.append(sb12.toString());
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(", hyphenationFrequency=");
            sb13.append(this.mHyphenationFrequency);
            sb.append(sb13.toString());
            sb.append("}");
            return sb.toString();
        }
        
        public static class Builder
        {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;
            
            public Builder(final TextPaint mPaint) {
                this.mPaint = mPaint;
                if (Build$VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                }
                else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                if (Build$VERSION.SDK_INT >= 18) {
                    this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                    return;
                }
                this.mTextDir = null;
            }
            
            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            
            public Builder setBreakStrategy(final int mBreakStrategy) {
                this.mBreakStrategy = mBreakStrategy;
                return this;
            }
            
            public Builder setHyphenationFrequency(final int mHyphenationFrequency) {
                this.mHyphenationFrequency = mHyphenationFrequency;
                return this;
            }
            
            public Builder setTextDirection(final TextDirectionHeuristic mTextDir) {
                this.mTextDir = mTextDir;
                return this;
            }
        }
    }
    
    private static class PrecomputedTextFutureTask extends FutureTask<PrecomputedTextCompat>
    {
        PrecomputedTextFutureTask(final Params params, final CharSequence charSequence) {
            super(new PrecomputedTextCallback(params, charSequence));
        }
        
        private static class PrecomputedTextCallback implements Callable<PrecomputedTextCompat>
        {
            private Params mParams;
            private CharSequence mText;
            
            PrecomputedTextCallback(final Params mParams, final CharSequence mText) {
                this.mParams = mParams;
                this.mText = mText;
            }
            
            @Override
            public PrecomputedTextCompat call() throws Exception {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }
        }
    }
}
