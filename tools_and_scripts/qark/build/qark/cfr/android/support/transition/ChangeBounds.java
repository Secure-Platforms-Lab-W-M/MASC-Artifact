/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TypeEvaluator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Path
 *  android.graphics.PointF
 *  android.graphics.Rect
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.Property
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.ObjectAnimatorUtils;
import android.support.transition.PathMotion;
import android.support.transition.PropertyValuesHolderUtils;
import android.support.transition.RectEvaluator;
import android.support.transition.Styleable;
import android.support.transition.Transition;
import android.support.transition.TransitionListenerAdapter;
import android.support.transition.TransitionUtils;
import android.support.transition.TransitionValues;
import android.support.transition.ViewGroupUtils;
import android.support.transition.ViewUtils;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class ChangeBounds
extends Transition {
    private static final Property<View, PointF> BOTTOM_RIGHT_ONLY_PROPERTY;
    private static final Property<ViewBounds, PointF> BOTTOM_RIGHT_PROPERTY;
    private static final Property<Drawable, PointF> DRAWABLE_ORIGIN_PROPERTY;
    private static final Property<View, PointF> POSITION_PROPERTY;
    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
    private static final String PROPNAME_CLIP = "android:changeBounds:clip";
    private static final String PROPNAME_PARENT = "android:changeBounds:parent";
    private static final String PROPNAME_WINDOW_X = "android:changeBounds:windowX";
    private static final String PROPNAME_WINDOW_Y = "android:changeBounds:windowY";
    private static final Property<View, PointF> TOP_LEFT_ONLY_PROPERTY;
    private static final Property<ViewBounds, PointF> TOP_LEFT_PROPERTY;
    private static RectEvaluator sRectEvaluator;
    private static final String[] sTransitionProperties;
    private boolean mReparent = false;
    private boolean mResizeClip = false;
    private int[] mTempLocation = new int[2];

    static {
        sTransitionProperties = new String[]{"android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY"};
        DRAWABLE_ORIGIN_PROPERTY = new Property<Drawable, PointF>(PointF.class, "boundsOrigin"){
            private Rect mBounds = new Rect();

            public PointF get(Drawable drawable2) {
                drawable2.copyBounds(this.mBounds);
                return new PointF((float)this.mBounds.left, (float)this.mBounds.top);
            }

            public void set(Drawable drawable2, PointF pointF) {
                drawable2.copyBounds(this.mBounds);
                this.mBounds.offsetTo(Math.round(pointF.x), Math.round(pointF.y));
                drawable2.setBounds(this.mBounds);
            }
        };
        TOP_LEFT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "topLeft"){

            public PointF get(ViewBounds viewBounds) {
                return null;
            }

            public void set(ViewBounds viewBounds, PointF pointF) {
                viewBounds.setTopLeft(pointF);
            }
        };
        BOTTOM_RIGHT_PROPERTY = new Property<ViewBounds, PointF>(PointF.class, "bottomRight"){

            public PointF get(ViewBounds viewBounds) {
                return null;
            }

            public void set(ViewBounds viewBounds, PointF pointF) {
                viewBounds.setBottomRight(pointF);
            }
        };
        BOTTOM_RIGHT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "bottomRight"){

            public PointF get(View view) {
                return null;
            }

            public void set(View view, PointF pointF) {
                ViewUtils.setLeftTopRightBottom(view, view.getLeft(), view.getTop(), Math.round(pointF.x), Math.round(pointF.y));
            }
        };
        TOP_LEFT_ONLY_PROPERTY = new Property<View, PointF>(PointF.class, "topLeft"){

            public PointF get(View view) {
                return null;
            }

            public void set(View view, PointF pointF) {
                ViewUtils.setLeftTopRightBottom(view, Math.round(pointF.x), Math.round(pointF.y), view.getRight(), view.getBottom());
            }
        };
        POSITION_PROPERTY = new Property<View, PointF>(PointF.class, "position"){

            public PointF get(View view) {
                return null;
            }

            public void set(View view, PointF pointF) {
                int n = Math.round(pointF.x);
                int n2 = Math.round(pointF.y);
                ViewUtils.setLeftTopRightBottom(view, n, n2, view.getWidth() + n, view.getHeight() + n2);
            }
        };
        sRectEvaluator = new RectEvaluator();
    }

    public ChangeBounds() {
    }

    public ChangeBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.CHANGE_BOUNDS);
        boolean bl = TypedArrayUtils.getNamedBoolean((TypedArray)context, (XmlPullParser)((XmlResourceParser)attributeSet), "resizeClip", 0, false);
        context.recycle();
        this.setResizeClip(bl);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (!ViewCompat.isLaidOut(view) && view.getWidth() == 0 && view.getHeight() == 0) {
            return;
        }
        transitionValues.values.put("android:changeBounds:bounds", (Object)new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
        transitionValues.values.put("android:changeBounds:parent", (Object)transitionValues.view.getParent());
        if (this.mReparent) {
            transitionValues.view.getLocationInWindow(this.mTempLocation);
            transitionValues.values.put("android:changeBounds:windowX", this.mTempLocation[0]);
            transitionValues.values.put("android:changeBounds:windowY", this.mTempLocation[1]);
        }
        if (this.mResizeClip) {
            transitionValues.values.put("android:changeBounds:clip", (Object)ViewCompat.getClipBounds(view));
            return;
        }
    }

    private boolean parentMatches(View view, View view2) {
        if (this.mReparent) {
            boolean bl = true;
            boolean bl2 = true;
            TransitionValues transitionValues = this.getMatchedTransitionValues(view, true);
            if (transitionValues == null) {
                if (view != view2) {
                    bl2 = false;
                }
                return bl2;
            }
            bl2 = view2 == transitionValues.view ? bl : false;
            return bl2;
        }
        return true;
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Nullable
    @Override
    public Animator createAnimator(final @NonNull ViewGroup viewGroup, @Nullable TransitionValues object, @Nullable TransitionValues objectAnimator) {
        if (object == null) return null;
        if (objectAnimator == null) {
            return null;
        }
        ObjectAnimator objectAnimator2 = object.values;
        View view = objectAnimator.values;
        objectAnimator2 = (ObjectAnimator)objectAnimator2.get("android:changeBounds:parent");
        ViewGroup viewGroup2 = (ViewGroup)view.get("android:changeBounds:parent");
        if (objectAnimator2 == null) return null;
        if (viewGroup2 == null) {
            return null;
        }
        view = objectAnimator.view;
        if (this.parentMatches((View)objectAnimator2, (View)viewGroup2)) {
            viewGroup = (Rect)object.values.get("android:changeBounds:bounds");
            objectAnimator2 = (Rect)objectAnimator.values.get("android:changeBounds:bounds");
            int n = viewGroup.left;
            int n2 = objectAnimator2.left;
            int n3 = viewGroup.top;
            int n4 = objectAnimator2.top;
            int n5 = viewGroup.right;
            int n6 = objectAnimator2.right;
            int n7 = viewGroup.bottom;
            int n8 = objectAnimator2.bottom;
            int n9 = n5 - n;
            int n10 = n7 - n3;
            int n11 = n6 - n2;
            int n12 = n8 - n4;
            object = (Rect)object.values.get("android:changeBounds:clip");
            objectAnimator2 = (Rect)objectAnimator.values.get("android:changeBounds:clip");
            int n13 = 0;
            int n14 = 0;
            if (n9 != 0 && n10 != 0 || n11 != 0 && n12 != 0) {
                if (n != n2 || n3 != n4) {
                    n14 = 0 + 1;
                }
                if (n5 != n6 || n7 != n8) {
                    ++n14;
                }
            } else {
                n14 = n13;
            }
            if (object != null && !object.equals((Object)objectAnimator2) || object == null && objectAnimator2 != null) {
                ++n14;
            }
            if (n14 <= 0) return null;
            if (!this.mResizeClip) {
                viewGroup = view;
                ViewUtils.setLeftTopRightBottom((View)viewGroup, n, n3, n5, n7);
                if (n14 == 2) {
                    if (n9 == n11 && n10 == n12) {
                        object = this.getPathMotion().getPath(n, n3, n2, n4);
                        viewGroup = ObjectAnimatorUtils.ofPointF(viewGroup, POSITION_PROPERTY, (Path)object);
                    } else {
                        object = new ViewBounds((View)viewGroup);
                        viewGroup = this.getPathMotion().getPath(n, n3, n2, n4);
                        objectAnimator = ObjectAnimatorUtils.ofPointF(object, TOP_LEFT_PROPERTY, (Path)viewGroup);
                        viewGroup = this.getPathMotion().getPath(n5, n7, n6, n8);
                        objectAnimator2 = ObjectAnimatorUtils.ofPointF(object, BOTTOM_RIGHT_PROPERTY, (Path)viewGroup);
                        viewGroup = new AnimatorSet();
                        viewGroup.playTogether(new Animator[]{objectAnimator, objectAnimator2});
                        viewGroup.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter((ViewBounds)object){
                            private ViewBounds mViewBounds;
                            final /* synthetic */ ViewBounds val$viewBounds;
                            {
                                this.val$viewBounds = viewBounds;
                                this.mViewBounds = this.val$viewBounds;
                            }
                        });
                    }
                } else if (n == n2 && n3 == n4) {
                    object = this.getPathMotion().getPath(n5, n7, n6, n8);
                    viewGroup = ObjectAnimatorUtils.ofPointF(viewGroup, BOTTOM_RIGHT_ONLY_PROPERTY, (Path)object);
                } else {
                    viewGroup = this.getPathMotion().getPath(n, n3, n2, n4);
                    viewGroup = ObjectAnimatorUtils.ofPointF(view, TOP_LEFT_ONLY_PROPERTY, (Path)viewGroup);
                }
            } else {
                viewGroup2 = view;
                ViewUtils.setLeftTopRightBottom((View)viewGroup2, n, n3, n + Math.max(n9, n11), n3 + Math.max(n10, n12));
                if (n == n2 && n3 == n4) {
                    viewGroup = null;
                } else {
                    viewGroup = this.getPathMotion().getPath(n, n3, n2, n4);
                    viewGroup = ObjectAnimatorUtils.ofPointF(viewGroup2, POSITION_PROPERTY, (Path)viewGroup);
                }
                if (object == null) {
                    object = new Rect(0, 0, n9, n10);
                }
                objectAnimator = objectAnimator2 == null ? new Rect(0, 0, n11, n12) : objectAnimator2;
                if (!object.equals((Object)objectAnimator)) {
                    ViewCompat.setClipBounds((View)viewGroup2, (Rect)object);
                    object = ObjectAnimator.ofObject((Object)viewGroup2, (String)"clipBounds", (TypeEvaluator)sRectEvaluator, (Object[])new Object[]{object, objectAnimator});
                    object.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter((View)viewGroup2, (Rect)objectAnimator2, n2, n4, n6, n8){
                        private boolean mIsCanceled;
                        final /* synthetic */ int val$endBottom;
                        final /* synthetic */ int val$endLeft;
                        final /* synthetic */ int val$endRight;
                        final /* synthetic */ int val$endTop;
                        final /* synthetic */ Rect val$finalClip;
                        final /* synthetic */ View val$view;
                        {
                            this.val$view = view;
                            this.val$finalClip = rect;
                            this.val$endLeft = n;
                            this.val$endTop = n2;
                            this.val$endRight = n3;
                            this.val$endBottom = n4;
                        }

                        public void onAnimationCancel(Animator animator2) {
                            this.mIsCanceled = true;
                        }

                        public void onAnimationEnd(Animator animator2) {
                            if (!this.mIsCanceled) {
                                ViewCompat.setClipBounds(this.val$view, this.val$finalClip);
                                ViewUtils.setLeftTopRightBottom(this.val$view, this.val$endLeft, this.val$endTop, this.val$endRight, this.val$endBottom);
                                return;
                            }
                        }
                    });
                } else {
                    object = null;
                }
                viewGroup = TransitionUtils.mergeAnimators((Animator)viewGroup, (Animator)object);
            }
            if (!(view.getParent() instanceof ViewGroup)) return viewGroup;
            object = (ViewGroup)view.getParent();
            ViewGroupUtils.suppressLayout((ViewGroup)object, true);
            this.addListener(new TransitionListenerAdapter((ViewGroup)object){
                boolean mCanceled;
                final /* synthetic */ ViewGroup val$parent;
                {
                    this.val$parent = viewGroup;
                    this.mCanceled = false;
                }

                @Override
                public void onTransitionCancel(@NonNull Transition transition) {
                    ViewGroupUtils.suppressLayout(this.val$parent, false);
                    this.mCanceled = true;
                }

                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    if (!this.mCanceled) {
                        ViewGroupUtils.suppressLayout(this.val$parent, false);
                    }
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(@NonNull Transition transition) {
                    ViewGroupUtils.suppressLayout(this.val$parent, false);
                }

                @Override
                public void onTransitionResume(@NonNull Transition transition) {
                    ViewGroupUtils.suppressLayout(this.val$parent, true);
                }
            });
            return viewGroup;
        }
        int n = (Integer)object.values.get("android:changeBounds:windowX");
        int n15 = (Integer)object.values.get("android:changeBounds:windowY");
        int n16 = (Integer)objectAnimator.values.get("android:changeBounds:windowX");
        int n17 = (Integer)objectAnimator.values.get("android:changeBounds:windowY");
        if (n == n16) {
            if (n15 == n17) return null;
        }
        viewGroup.getLocationInWindow(this.mTempLocation);
        object = Bitmap.createBitmap((int)view.getWidth(), (int)view.getHeight(), (Bitmap.Config)Bitmap.Config.ARGB_8888);
        view.draw(new Canvas((Bitmap)object));
        object = new BitmapDrawable((Bitmap)object);
        float f = ViewUtils.getTransitionAlpha(view);
        ViewUtils.setTransitionAlpha(view, 0.0f);
        ViewUtils.getOverlay((View)viewGroup).add((Drawable)object);
        objectAnimator = this.getPathMotion();
        objectAnimator2 = this.mTempLocation;
        objectAnimator = objectAnimator.getPath(n - objectAnimator2[0], n15 - objectAnimator2[1], n16 - objectAnimator2[0], n17 - objectAnimator2[1]);
        objectAnimator = ObjectAnimator.ofPropertyValuesHolder((Object)object, (PropertyValuesHolder[])new PropertyValuesHolder[]{PropertyValuesHolderUtils.ofPointF(DRAWABLE_ORIGIN_PROPERTY, (Path)objectAnimator)});
        objectAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter((BitmapDrawable)object, view, f){
            final /* synthetic */ BitmapDrawable val$drawable;
            final /* synthetic */ float val$transitionAlpha;
            final /* synthetic */ View val$view;
            {
                this.val$drawable = bitmapDrawable;
                this.val$view = view;
                this.val$transitionAlpha = f;
            }

            public void onAnimationEnd(Animator animator2) {
                ViewUtils.getOverlay((View)viewGroup).remove((Drawable)this.val$drawable);
                ViewUtils.setTransitionAlpha(this.val$view, this.val$transitionAlpha);
            }
        });
        return objectAnimator;
    }

    public boolean getResizeClip() {
        return this.mResizeClip;
    }

    @Nullable
    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public void setResizeClip(boolean bl) {
        this.mResizeClip = bl;
    }

    private static class ViewBounds {
        private int mBottom;
        private int mBottomRightCalls;
        private int mLeft;
        private int mRight;
        private int mTop;
        private int mTopLeftCalls;
        private View mView;

        ViewBounds(View view) {
            this.mView = view;
        }

        private void setLeftTopRightBottom() {
            ViewUtils.setLeftTopRightBottom(this.mView, this.mLeft, this.mTop, this.mRight, this.mBottom);
            this.mTopLeftCalls = 0;
            this.mBottomRightCalls = 0;
        }

        void setBottomRight(PointF pointF) {
            this.mRight = Math.round(pointF.x);
            this.mBottom = Math.round(pointF.y);
            ++this.mBottomRightCalls;
            if (this.mTopLeftCalls == this.mBottomRightCalls) {
                this.setLeftTopRightBottom();
                return;
            }
        }

        void setTopLeft(PointF pointF) {
            this.mLeft = Math.round(pointF.x);
            this.mTop = Math.round(pointF.y);
            ++this.mTopLeftCalls;
            if (this.mTopLeftCalls == this.mBottomRightCalls) {
                this.setLeftTopRightBottom();
                return;
            }
        }
    }

}

