/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.database.DataSetObserver
 *  android.graphics.drawable.Drawable
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.text.method.SingleLineTransformationMethod
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.widget.TextView
 */
package android.support.v4.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Locale;

@ViewPager.DecorView
public class PagerTitleStrip
extends ViewGroup {
    private static final int[] ATTRS = new int[]{16842804, 16842901, 16842904, 16842927};
    private static final float SIDE_ALPHA = 0.6f;
    private static final int[] TEXT_ATTRS = new int[]{16843660};
    private static final int TEXT_SPACING = 16;
    TextView mCurrText;
    private int mGravity;
    private int mLastKnownCurrentPage = -1;
    float mLastKnownPositionOffset = -1.0f;
    TextView mNextText;
    private int mNonPrimaryAlpha;
    private final PageListener mPageListener;
    ViewPager mPager;
    TextView mPrevText;
    private int mScaledTextSpacing;
    int mTextColor;
    private boolean mUpdatingPositions;
    private boolean mUpdatingText;
    private WeakReference<PagerAdapter> mWatchingAdapter;

    public PagerTitleStrip(Context context) {
        this(context, null);
    }

    public PagerTitleStrip(Context context, AttributeSet attributeSet) {
        int n;
        TextView textView;
        super(context, attributeSet);
        this.mPageListener = new PageListener();
        this.mPrevText = textView = new TextView(context);
        this.addView((View)textView);
        this.mCurrText = textView = new TextView(context);
        this.addView((View)textView);
        this.mNextText = textView = new TextView(context);
        this.addView((View)textView);
        attributeSet = context.obtainStyledAttributes(attributeSet, ATTRS);
        int n2 = attributeSet.getResourceId(0, 0);
        if (n2 != 0) {
            TextViewCompat.setTextAppearance(this.mPrevText, n2);
            TextViewCompat.setTextAppearance(this.mCurrText, n2);
            TextViewCompat.setTextAppearance(this.mNextText, n2);
        }
        if ((n = attributeSet.getDimensionPixelSize(1, 0)) != 0) {
            this.setTextSize(0, n);
        }
        if (attributeSet.hasValue(2)) {
            n = attributeSet.getColor(2, 0);
            this.mPrevText.setTextColor(n);
            this.mCurrText.setTextColor(n);
            this.mNextText.setTextColor(n);
        }
        this.mGravity = attributeSet.getInteger(3, 80);
        attributeSet.recycle();
        this.mTextColor = this.mCurrText.getTextColors().getDefaultColor();
        this.setNonPrimaryAlpha(0.6f);
        this.mPrevText.setEllipsize(TextUtils.TruncateAt.END);
        this.mCurrText.setEllipsize(TextUtils.TruncateAt.END);
        this.mNextText.setEllipsize(TextUtils.TruncateAt.END);
        boolean bl = false;
        if (n2 != 0) {
            attributeSet = context.obtainStyledAttributes(n2, TEXT_ATTRS);
            bl = attributeSet.getBoolean(0, false);
            attributeSet.recycle();
        }
        if (bl) {
            PagerTitleStrip.setSingleLineAllCaps(this.mPrevText);
            PagerTitleStrip.setSingleLineAllCaps(this.mCurrText);
            PagerTitleStrip.setSingleLineAllCaps(this.mNextText);
        } else {
            this.mPrevText.setSingleLine();
            this.mCurrText.setSingleLine();
            this.mNextText.setSingleLine();
        }
        this.mScaledTextSpacing = (int)(16.0f * context.getResources().getDisplayMetrics().density);
    }

    private static void setSingleLineAllCaps(TextView textView) {
        textView.setTransformationMethod((TransformationMethod)new SingleLineAllCapsTransform(textView.getContext()));
    }

    int getMinHeight() {
        Drawable drawable2 = this.getBackground();
        if (drawable2 != null) {
            return drawable2.getIntrinsicHeight();
        }
        return 0;
    }

    public int getTextSpacing() {
        return this.mScaledTextSpacing;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Object object = this.getParent();
        if (object instanceof ViewPager) {
            object = (ViewPager)((Object)object);
            PagerAdapter pagerAdapter = object.getAdapter();
            object.setInternalPageChangeListener(this.mPageListener);
            object.addOnAdapterChangeListener(this.mPageListener);
            this.mPager = object;
            object = this.mWatchingAdapter;
            object = object != null ? object.get() : null;
            this.updateAdapter((PagerAdapter)object, pagerAdapter);
            return;
        }
        throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewPager viewPager = this.mPager;
        if (viewPager != null) {
            this.updateAdapter(viewPager.getAdapter(), null);
            this.mPager.setInternalPageChangeListener(null);
            this.mPager.removeOnAdapterChangeListener(this.mPageListener);
            this.mPager = null;
            return;
        }
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        if (this.mPager != null) {
            float f = this.mLastKnownPositionOffset;
            if (f < 0.0f) {
                f = 0.0f;
            }
            this.updateTextPositions(this.mLastKnownCurrentPage, f, true);
            return;
        }
    }

    protected void onMeasure(int n, int n2) {
        if (View.MeasureSpec.getMode((int)n) == 1073741824) {
            int n3 = this.getPaddingTop() + this.getPaddingBottom();
            int n4 = PagerTitleStrip.getChildMeasureSpec((int)n2, (int)n3, (int)-2);
            int n5 = View.MeasureSpec.getSize((int)n);
            n = PagerTitleStrip.getChildMeasureSpec((int)n, (int)((int)((float)n5 * 0.2f)), (int)-2);
            this.mPrevText.measure(n, n4);
            this.mCurrText.measure(n, n4);
            this.mNextText.measure(n, n4);
            if (View.MeasureSpec.getMode((int)n2) == 1073741824) {
                n = View.MeasureSpec.getSize((int)n2);
            } else {
                n = this.mCurrText.getMeasuredHeight();
                n = Math.max(this.getMinHeight(), n + n3);
            }
            this.setMeasuredDimension(n5, View.resolveSizeAndState((int)n, (int)n2, (int)(this.mCurrText.getMeasuredState() << 16)));
            return;
        }
        throw new IllegalStateException("Must measure with an exact width");
    }

    public void requestLayout() {
        if (!this.mUpdatingText) {
            super.requestLayout();
            return;
        }
    }

    public void setGravity(int n) {
        this.mGravity = n;
        this.requestLayout();
    }

    public void setNonPrimaryAlpha(@FloatRange(from=0.0, to=1.0) float f) {
        this.mNonPrimaryAlpha = (int)(255.0f * f) & 255;
        int n = this.mNonPrimaryAlpha << 24 | this.mTextColor & 16777215;
        this.mPrevText.setTextColor(n);
        this.mNextText.setTextColor(n);
    }

    public void setTextColor(@ColorInt int n) {
        this.mTextColor = n;
        this.mCurrText.setTextColor(n);
        n = this.mNonPrimaryAlpha << 24 | this.mTextColor & 16777215;
        this.mPrevText.setTextColor(n);
        this.mNextText.setTextColor(n);
    }

    public void setTextSize(int n, float f) {
        this.mPrevText.setTextSize(n, f);
        this.mCurrText.setTextSize(n, f);
        this.mNextText.setTextSize(n, f);
    }

    public void setTextSpacing(int n) {
        this.mScaledTextSpacing = n;
        this.requestLayout();
    }

    void updateAdapter(PagerAdapter object, PagerAdapter pagerAdapter) {
        if (object != null) {
            object.unregisterDataSetObserver(this.mPageListener);
            this.mWatchingAdapter = null;
        }
        if (pagerAdapter != null) {
            pagerAdapter.registerDataSetObserver(this.mPageListener);
            this.mWatchingAdapter = new WeakReference<PagerAdapter>(pagerAdapter);
        }
        if ((object = this.mPager) != null) {
            this.mLastKnownCurrentPage = -1;
            this.mLastKnownPositionOffset = -1.0f;
            this.updateText(object.getCurrentItem(), pagerAdapter);
            this.requestLayout();
            return;
        }
    }

    void updateText(int n, PagerAdapter object) {
        int n2 = object != null ? object.getCount() : 0;
        this.mUpdatingText = true;
        CharSequence charSequence = null;
        if (n >= 1 && object != null) {
            charSequence = object.getPageTitle(n - 1);
        }
        this.mPrevText.setText(charSequence);
        TextView textView = this.mCurrText;
        charSequence = object != null && n < n2 ? object.getPageTitle(n) : null;
        textView.setText(charSequence);
        charSequence = null;
        object = n + 1 < n2 && object != null ? object.getPageTitle(n + 1) : charSequence;
        this.mNextText.setText((CharSequence)object);
        n2 = View.MeasureSpec.makeMeasureSpec((int)Math.max(0, (int)((float)(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight()) * 0.8f)), (int)Integer.MIN_VALUE);
        int n3 = View.MeasureSpec.makeMeasureSpec((int)Math.max(0, this.getHeight() - this.getPaddingTop() - this.getPaddingBottom()), (int)Integer.MIN_VALUE);
        this.mPrevText.measure(n2, n3);
        this.mCurrText.measure(n2, n3);
        this.mNextText.measure(n2, n3);
        this.mLastKnownCurrentPage = n;
        if (!this.mUpdatingPositions) {
            this.updateTextPositions(n, this.mLastKnownPositionOffset, false);
        }
        this.mUpdatingText = false;
    }

    void updateTextPositions(int n, float f, boolean bl) {
        if (n != this.mLastKnownCurrentPage) {
            this.updateText(n, this.mPager.getAdapter());
        } else if (!bl && f == this.mLastKnownPositionOffset) {
            return;
        }
        this.mUpdatingPositions = true;
        int n2 = this.mPrevText.getMeasuredWidth();
        int n3 = this.mCurrText.getMeasuredWidth();
        int n4 = this.mNextText.getMeasuredWidth();
        int n5 = n3 / 2;
        int n6 = this.getWidth();
        int n7 = this.getHeight();
        int n8 = this.getPaddingLeft();
        int n9 = this.getPaddingRight();
        n = this.getPaddingTop();
        int n10 = this.getPaddingBottom();
        int n11 = n9 + n5;
        float f2 = f + 0.5f;
        if (f2 > 1.0f) {
            f2 -= 1.0f;
        }
        n5 = n6 - n11 - (int)((float)(n6 - (n8 + n5) - n11) * f2) - n3 / 2;
        n3 = n5 + n3;
        int n12 = this.mPrevText.getBaseline();
        int n13 = this.mCurrText.getBaseline();
        n11 = this.mNextText.getBaseline();
        int n14 = Math.max(Math.max(n12, n13), n11);
        n12 = n14 - n12;
        n13 = n14 - n13;
        n11 = n14 - n11;
        n14 = this.mPrevText.getMeasuredHeight();
        int n15 = this.mCurrText.getMeasuredHeight();
        int n16 = this.mNextText.getMeasuredHeight();
        n14 = Math.max(Math.max(n12 + n14, n13 + n15), n11 + n16);
        n15 = this.mGravity & 112;
        if (n15 != 16) {
            if (n15 != 80) {
                n10 = n + n12;
                n7 = n + n13;
                n += n11;
            } else {
                n = n7 - n10 - n14;
                n10 = n + n12;
                n7 = n + n13;
                n += n11;
            }
        } else {
            n = (n7 - n - n10 - n14) / 2;
            n10 = n + n12;
            n7 = n + n13;
            n += n11;
        }
        TextView textView = this.mCurrText;
        textView.layout(n5, n7, n3, n7 + textView.getMeasuredHeight());
        n7 = Math.min(n8, n5 - this.mScaledTextSpacing - n2);
        textView = this.mPrevText;
        textView.layout(n7, n10, n7 + n2, n10 + textView.getMeasuredHeight());
        n7 = Math.max(n6 - n9 - n4, this.mScaledTextSpacing + n3);
        textView = this.mNextText;
        textView.layout(n7, n, n7 + n4, n + textView.getMeasuredHeight());
        this.mLastKnownPositionOffset = f;
        this.mUpdatingPositions = false;
    }

    private class PageListener
    extends DataSetObserver
    implements ViewPager.OnPageChangeListener,
    ViewPager.OnAdapterChangeListener {
        private int mScrollState;

        PageListener() {
        }

        @Override
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            PagerTitleStrip.this.updateAdapter(pagerAdapter, pagerAdapter2);
        }

        public void onChanged() {
            PagerTitleStrip pagerTitleStrip = PagerTitleStrip.this;
            pagerTitleStrip.updateText(pagerTitleStrip.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
            float f = PagerTitleStrip.this.mLastKnownPositionOffset;
            float f2 = 0.0f;
            if (f >= 0.0f) {
                f2 = PagerTitleStrip.this.mLastKnownPositionOffset;
            }
            pagerTitleStrip = PagerTitleStrip.this;
            pagerTitleStrip.updateTextPositions(pagerTitleStrip.mPager.getCurrentItem(), f2, true);
        }

        @Override
        public void onPageScrollStateChanged(int n) {
            this.mScrollState = n;
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
            if (f > 0.5f) {
                ++n;
            }
            PagerTitleStrip.this.updateTextPositions(n, f, false);
        }

        @Override
        public void onPageSelected(int n) {
            if (this.mScrollState == 0) {
                PagerTitleStrip pagerTitleStrip = PagerTitleStrip.this;
                pagerTitleStrip.updateText(pagerTitleStrip.mPager.getCurrentItem(), PagerTitleStrip.this.mPager.getAdapter());
                float f = PagerTitleStrip.this.mLastKnownPositionOffset;
                float f2 = 0.0f;
                if (f >= 0.0f) {
                    f2 = PagerTitleStrip.this.mLastKnownPositionOffset;
                }
                pagerTitleStrip = PagerTitleStrip.this;
                pagerTitleStrip.updateTextPositions(pagerTitleStrip.mPager.getCurrentItem(), f2, true);
                return;
            }
        }
    }

    private static class SingleLineAllCapsTransform
    extends SingleLineTransformationMethod {
        private Locale mLocale;

        SingleLineAllCapsTransform(Context context) {
            this.mLocale = context.getResources().getConfiguration().locale;
        }

        public CharSequence getTransformation(CharSequence charSequence, View view) {
            if ((charSequence = super.getTransformation(charSequence, view)) != null) {
                return charSequence.toString().toUpperCase(this.mLocale);
            }
            return null;
        }
    }

}

