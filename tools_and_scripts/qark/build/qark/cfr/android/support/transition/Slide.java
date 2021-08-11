/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.DecelerateInterpolator
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.transition.SidePropagation;
import android.support.transition.Styleable;
import android.support.transition.TransitionPropagation;
import android.support.transition.TransitionValues;
import android.support.transition.TranslationAnimationCreator;
import android.support.transition.Visibility;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class Slide
extends Visibility {
    private static final String PROPNAME_SCREEN_POSITION = "android:slide:screenPosition";
    private static final TimeInterpolator sAccelerate;
    private static final CalculateSlide sCalculateBottom;
    private static final CalculateSlide sCalculateEnd;
    private static final CalculateSlide sCalculateLeft;
    private static final CalculateSlide sCalculateRight;
    private static final CalculateSlide sCalculateStart;
    private static final CalculateSlide sCalculateTop;
    private static final TimeInterpolator sDecelerate;
    private CalculateSlide mSlideCalculator = sCalculateBottom;
    private int mSlideEdge = 80;

    static {
        sDecelerate = new DecelerateInterpolator();
        sAccelerate = new AccelerateInterpolator();
        sCalculateLeft = new CalculateSlideHorizontal(){

            @Override
            public float getGoneX(ViewGroup viewGroup, View view) {
                return view.getTranslationX() - (float)viewGroup.getWidth();
            }
        };
        sCalculateStart = new CalculateSlideHorizontal(){

            @Override
            public float getGoneX(ViewGroup viewGroup, View view) {
                int n = ViewCompat.getLayoutDirection((View)viewGroup);
                boolean bl = true;
                if (n != 1) {
                    bl = false;
                }
                if (bl) {
                    return view.getTranslationX() + (float)viewGroup.getWidth();
                }
                return view.getTranslationX() - (float)viewGroup.getWidth();
            }
        };
        sCalculateTop = new CalculateSlideVertical(){

            @Override
            public float getGoneY(ViewGroup viewGroup, View view) {
                return view.getTranslationY() - (float)viewGroup.getHeight();
            }
        };
        sCalculateRight = new CalculateSlideHorizontal(){

            @Override
            public float getGoneX(ViewGroup viewGroup, View view) {
                return view.getTranslationX() + (float)viewGroup.getWidth();
            }
        };
        sCalculateEnd = new CalculateSlideHorizontal(){

            @Override
            public float getGoneX(ViewGroup viewGroup, View view) {
                int n = ViewCompat.getLayoutDirection((View)viewGroup);
                boolean bl = true;
                if (n != 1) {
                    bl = false;
                }
                if (bl) {
                    return view.getTranslationX() - (float)viewGroup.getWidth();
                }
                return view.getTranslationX() + (float)viewGroup.getWidth();
            }
        };
        sCalculateBottom = new CalculateSlideVertical(){

            @Override
            public float getGoneY(ViewGroup viewGroup, View view) {
                return view.getTranslationY() + (float)viewGroup.getHeight();
            }
        };
    }

    public Slide() {
        this.setSlideEdge(80);
    }

    public Slide(int n) {
        this.setSlideEdge(n);
    }

    public Slide(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.SLIDE);
        int n = TypedArrayUtils.getNamedInt((TypedArray)context, (XmlPullParser)attributeSet, "slideEdge", 0, 80);
        context.recycle();
        this.setSlideEdge(n);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        int[] arrn = new int[2];
        view.getLocationOnScreen(arrn);
        transitionValues.values.put("android:slide:screenPosition", arrn);
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        this.captureValues(transitionValues);
    }

    public int getSlideEdge() {
        return this.mSlideEdge;
    }

    @Override
    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues arrn, TransitionValues transitionValues) {
        if (transitionValues == null) {
            return null;
        }
        arrn = (int[])transitionValues.values.get("android:slide:screenPosition");
        float f = view.getTranslationX();
        float f2 = view.getTranslationY();
        float f3 = this.mSlideCalculator.getGoneX(viewGroup, view);
        float f4 = this.mSlideCalculator.getGoneY(viewGroup, view);
        return TranslationAnimationCreator.createAnimation(view, transitionValues, arrn[0], arrn[1], f3, f4, f, f2, sDecelerate);
    }

    @Override
    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues arrn) {
        if (transitionValues == null) {
            return null;
        }
        arrn = (int[])transitionValues.values.get("android:slide:screenPosition");
        float f = view.getTranslationX();
        float f2 = view.getTranslationY();
        float f3 = this.mSlideCalculator.getGoneX(viewGroup, view);
        float f4 = this.mSlideCalculator.getGoneY(viewGroup, view);
        return TranslationAnimationCreator.createAnimation(view, transitionValues, arrn[0], arrn[1], f, f2, f3, f4, sAccelerate);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setSlideEdge(int n) {
        if (n != 3) {
            if (n != 5) {
                if (n != 48) {
                    if (n != 80) {
                        if (n != 8388611) {
                            if (n != 8388613) throw new IllegalArgumentException("Invalid slide direction");
                            this.mSlideCalculator = sCalculateEnd;
                        } else {
                            this.mSlideCalculator = sCalculateStart;
                        }
                    } else {
                        this.mSlideCalculator = sCalculateBottom;
                    }
                } else {
                    this.mSlideCalculator = sCalculateTop;
                }
            } else {
                this.mSlideCalculator = sCalculateRight;
            }
        } else {
            this.mSlideCalculator = sCalculateLeft;
        }
        this.mSlideEdge = n;
        SidePropagation sidePropagation = new SidePropagation();
        sidePropagation.setSide(n);
        this.setPropagation(sidePropagation);
    }

    private static interface CalculateSlide {
        public float getGoneX(ViewGroup var1, View var2);

        public float getGoneY(ViewGroup var1, View var2);
    }

    private static abstract class CalculateSlideHorizontal
    implements CalculateSlide {
        private CalculateSlideHorizontal() {
        }

        @Override
        public float getGoneY(ViewGroup viewGroup, View view) {
            return view.getTranslationY();
        }
    }

    private static abstract class CalculateSlideVertical
    implements CalculateSlide {
        private CalculateSlideVertical() {
        }

        @Override
        public float getGoneX(ViewGroup viewGroup, View view) {
            return view.getTranslationX();
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface GravityFlag {
    }

}

