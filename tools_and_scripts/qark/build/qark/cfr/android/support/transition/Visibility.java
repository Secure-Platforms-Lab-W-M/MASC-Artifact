/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.transition.AnimatorUtils;
import android.support.transition.AnimatorUtilsApi14;
import android.support.transition.Styleable;
import android.support.transition.Transition;
import android.support.transition.TransitionUtils;
import android.support.transition.TransitionValues;
import android.support.transition.ViewGroupOverlayImpl;
import android.support.transition.ViewGroupUtils;
import android.support.transition.ViewUtils;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public abstract class Visibility
extends Transition {
    public static final int MODE_IN = 1;
    public static final int MODE_OUT = 2;
    private static final String PROPNAME_PARENT = "android:visibility:parent";
    private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
    static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
    private static final String[] sTransitionProperties = new String[]{"android:visibility:visibility", "android:visibility:parent"};
    private int mMode = 3;

    public Visibility() {
    }

    public Visibility(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.VISIBILITY_TRANSITION);
        int n = TypedArrayUtils.getNamedInt((TypedArray)context, (XmlPullParser)((XmlResourceParser)attributeSet), "transitionVisibilityMode", 0, 0);
        context.recycle();
        if (n != 0) {
            this.setMode(n);
            return;
        }
    }

    private void captureValues(TransitionValues transitionValues) {
        int n = transitionValues.view.getVisibility();
        transitionValues.values.put("android:visibility:visibility", n);
        transitionValues.values.put("android:visibility:parent", (Object)transitionValues.view.getParent());
        int[] arrn = new int[2];
        transitionValues.view.getLocationOnScreen(arrn);
        transitionValues.values.put("android:visibility:screenLocation", arrn);
    }

    private VisibilityInfo getVisibilityChangeInfo(TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityInfo = new VisibilityInfo();
        visibilityInfo.mVisibilityChange = false;
        visibilityInfo.mFadeIn = false;
        if (transitionValues != null && transitionValues.values.containsKey("android:visibility:visibility")) {
            visibilityInfo.mStartVisibility = (Integer)transitionValues.values.get("android:visibility:visibility");
            visibilityInfo.mStartParent = (ViewGroup)transitionValues.values.get("android:visibility:parent");
        } else {
            visibilityInfo.mStartVisibility = -1;
            visibilityInfo.mStartParent = null;
        }
        if (transitionValues2 != null && transitionValues2.values.containsKey("android:visibility:visibility")) {
            visibilityInfo.mEndVisibility = (Integer)transitionValues2.values.get("android:visibility:visibility");
            visibilityInfo.mEndParent = (ViewGroup)transitionValues2.values.get("android:visibility:parent");
        } else {
            visibilityInfo.mEndVisibility = -1;
            visibilityInfo.mEndParent = null;
        }
        if (transitionValues != null && transitionValues2 != null) {
            if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent) {
                return visibilityInfo;
            }
            if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
                if (visibilityInfo.mStartVisibility == 0) {
                    visibilityInfo.mFadeIn = false;
                    visibilityInfo.mVisibilityChange = true;
                    return visibilityInfo;
                }
                if (visibilityInfo.mEndVisibility == 0) {
                    visibilityInfo.mFadeIn = true;
                    visibilityInfo.mVisibilityChange = true;
                    return visibilityInfo;
                }
                return visibilityInfo;
            }
            if (visibilityInfo.mEndParent == null) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
                return visibilityInfo;
            }
            if (visibilityInfo.mStartParent == null) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
                return visibilityInfo;
            }
            return visibilityInfo;
        }
        if (transitionValues == null && visibilityInfo.mEndVisibility == 0) {
            visibilityInfo.mFadeIn = true;
            visibilityInfo.mVisibilityChange = true;
            return visibilityInfo;
        }
        if (transitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
            visibilityInfo.mFadeIn = false;
            visibilityInfo.mVisibilityChange = true;
            return visibilityInfo;
        }
        return visibilityInfo;
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup viewGroup, @Nullable TransitionValues transitionValues, @Nullable TransitionValues transitionValues2) {
        VisibilityInfo visibilityInfo = this.getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (visibilityInfo.mVisibilityChange && (visibilityInfo.mStartParent != null || visibilityInfo.mEndParent != null)) {
            if (visibilityInfo.mFadeIn) {
                return this.onAppear(viewGroup, transitionValues, visibilityInfo.mStartVisibility, transitionValues2, visibilityInfo.mEndVisibility);
            }
            return this.onDisappear(viewGroup, transitionValues, visibilityInfo.mStartVisibility, transitionValues2, visibilityInfo.mEndVisibility);
        }
        return null;
    }

    public int getMode() {
        return this.mMode;
    }

    @Nullable
    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    @Override
    public boolean isTransitionRequired(TransitionValues object, TransitionValues transitionValues) {
        boolean bl;
        block5 : {
            block6 : {
                boolean bl2 = false;
                if (object == null && transitionValues == null) {
                    return false;
                }
                if (object != null && transitionValues != null && transitionValues.values.containsKey("android:visibility:visibility") != object.values.containsKey("android:visibility:visibility")) {
                    return false;
                }
                object = this.getVisibilityChangeInfo((TransitionValues)object, transitionValues);
                bl = bl2;
                if (!object.mVisibilityChange) break block5;
                if (object.mStartVisibility == 0) break block6;
                bl = bl2;
                if (object.mEndVisibility != 0) break block5;
            }
            bl = true;
        }
        return bl;
    }

    public boolean isVisible(TransitionValues transitionValues) {
        boolean bl = false;
        if (transitionValues == null) {
            return false;
        }
        int n = (Integer)transitionValues.values.get("android:visibility:visibility");
        transitionValues = (View)transitionValues.values.get("android:visibility:parent");
        boolean bl2 = bl;
        if (n == 0) {
            bl2 = bl;
            if (transitionValues != null) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public Animator onAppear(ViewGroup viewGroup, TransitionValues transitionValues, int n, TransitionValues transitionValues2, int n2) {
        if ((this.mMode & 1) == 1) {
            if (transitionValues2 == null) {
                return null;
            }
            if (transitionValues == null) {
                View view = (View)transitionValues2.view.getParent();
                if (this.getVisibilityChangeInfo((TransitionValues)this.getMatchedTransitionValues((View)view, (boolean)false), (TransitionValues)this.getTransitionValues((View)view, (boolean)false)).mVisibilityChange) {
                    return null;
                }
            }
            return this.onAppear(viewGroup, transitionValues2.view, transitionValues, transitionValues2);
        }
        return null;
    }

    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public Animator onDisappear(ViewGroup viewGroup, TransitionValues object, int n, TransitionValues transitionValues, int n2) {
        if ((this.mMode & 2) != 2) {
            return null;
        }
        View view = object != null ? object.view : null;
        int[] arrn = transitionValues != null ? transitionValues.view : null;
        Object var9_8 = null;
        Object var10_9 = null;
        Object var8_10 = null;
        if (arrn != null && arrn.getParent() != null) {
            if (n2 == 4) {
                view = var9_8;
            } else if (view == arrn) {
                view = var9_8;
            } else {
                arrn = var8_10;
            }
        } else if (arrn != null) {
            view = arrn;
            arrn = var8_10;
        } else if (view != null) {
            if (view.getParent() == null) {
                arrn = var8_10;
            } else if (view.getParent() instanceof View) {
                arrn = (View)view.getParent();
                if (!this.getVisibilityChangeInfo((TransitionValues)this.getTransitionValues((View)arrn, (boolean)true), (TransitionValues)this.getMatchedTransitionValues((View)arrn, (boolean)true)).mVisibilityChange) {
                    view = TransitionUtils.copyViewImage(viewGroup, view, (View)arrn);
                } else if (arrn.getParent() == null) {
                    n = arrn.getId();
                    if (n == -1 || viewGroup.findViewById(n) == null || !this.mCanRemoveViews) {
                        view = var10_9;
                    }
                } else {
                    view = var10_9;
                }
                arrn = var8_10;
            } else {
                view = var9_8;
                arrn = var8_10;
            }
        } else {
            arrn = var8_10;
            view = var9_8;
        }
        if (view != null && object != null) {
            arrn = (int[])object.values.get("android:visibility:screenLocation");
            n = arrn[0];
            n2 = arrn[1];
            arrn = new int[2];
            viewGroup.getLocationOnScreen(arrn);
            view.offsetLeftAndRight(n - arrn[0] - view.getLeft());
            view.offsetTopAndBottom(n2 - arrn[1] - view.getTop());
            arrn = ViewGroupUtils.getOverlay(viewGroup);
            arrn.add(view);
            viewGroup = this.onDisappear(viewGroup, view, (TransitionValues)object, transitionValues);
            if (viewGroup == null) {
                arrn.remove(view);
                return viewGroup;
            }
            viewGroup.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter((ViewGroupOverlayImpl)arrn, view){
                final /* synthetic */ View val$finalOverlayView;
                final /* synthetic */ ViewGroupOverlayImpl val$overlay;
                {
                    this.val$overlay = viewGroupOverlayImpl;
                    this.val$finalOverlayView = view;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$overlay.remove(this.val$finalOverlayView);
                }
            });
            return viewGroup;
        }
        if (arrn != null) {
            n = arrn.getVisibility();
            ViewUtils.setTransitionVisibility((View)arrn, 0);
            viewGroup = this.onDisappear(viewGroup, (View)arrn, (TransitionValues)object, transitionValues);
            if (viewGroup != null) {
                object = new DisappearListener((View)arrn, n2, true);
                viewGroup.addListener((Animator.AnimatorListener)object);
                AnimatorUtils.addPauseListener((Animator)viewGroup, (AnimatorListenerAdapter)object);
                this.addListener((Transition.TransitionListener)object);
                return viewGroup;
            }
            ViewUtils.setTransitionVisibility((View)arrn, n);
            return viewGroup;
        }
        return null;
    }

    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public void setMode(int n) {
        if ((n & -4) == 0) {
            this.mMode = n;
            return;
        }
        throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
    }

    private static class DisappearListener
    extends AnimatorListenerAdapter
    implements Transition.TransitionListener,
    AnimatorUtilsApi14.AnimatorPauseListenerCompat {
        boolean mCanceled = false;
        private final int mFinalVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final boolean mSuppressLayout;
        private final View mView;

        DisappearListener(View view, int n, boolean bl) {
            this.mView = view;
            this.mFinalVisibility = n;
            this.mParent = (ViewGroup)view.getParent();
            this.mSuppressLayout = bl;
            this.suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
                ViewGroup viewGroup = this.mParent;
                if (viewGroup != null) {
                    viewGroup.invalidate();
                }
            }
            this.suppressLayout(false);
        }

        private void suppressLayout(boolean bl) {
            ViewGroup viewGroup;
            if (this.mSuppressLayout && this.mLayoutSuppressed != bl && (viewGroup = this.mParent) != null) {
                this.mLayoutSuppressed = bl;
                ViewGroupUtils.suppressLayout(viewGroup, bl);
                return;
            }
        }

        public void onAnimationCancel(Animator animator2) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animator2) {
            this.hideViewWhenNotCanceled();
        }

        @Override
        public void onAnimationPause(Animator animator2) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
                return;
            }
        }

        public void onAnimationRepeat(Animator animator2) {
        }

        @Override
        public void onAnimationResume(Animator animator2) {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, 0);
                return;
            }
        }

        public void onAnimationStart(Animator animator2) {
        }

        @Override
        public void onTransitionCancel(@NonNull Transition transition) {
        }

        @Override
        public void onTransitionEnd(@NonNull Transition transition) {
            this.hideViewWhenNotCanceled();
            transition.removeListener(this);
        }

        @Override
        public void onTransitionPause(@NonNull Transition transition) {
            this.suppressLayout(false);
        }

        @Override
        public void onTransitionResume(@NonNull Transition transition) {
            this.suppressLayout(true);
        }

        @Override
        public void onTransitionStart(@NonNull Transition transition) {
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface Mode {
    }

    private static class VisibilityInfo {
        ViewGroup mEndParent;
        int mEndVisibility;
        boolean mFadeIn;
        ViewGroup mStartParent;
        int mStartVisibility;
        boolean mVisibilityChange;

        private VisibilityInfo() {
        }
    }

}

