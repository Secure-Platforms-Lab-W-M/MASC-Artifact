/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorInflater
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.LayoutTransition
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationSet
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Transformation
 *  androidx.fragment.R
 *  androidx.fragment.R$anim
 *  androidx.fragment.R$id
 */
package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentTransition;

class FragmentAnim {
    private FragmentAnim() {
    }

    static void animateRemoveFragment(final Fragment fragment, AnimationOrAnimator object, final FragmentTransition.Callback callback) {
        final View view = fragment.mView;
        final ViewGroup viewGroup = fragment.mContainer;
        viewGroup.startViewTransition(view);
        final CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(){

            @Override
            public void onCancel() {
                if (fragment.getAnimatingAway() != null) {
                    View view = fragment.getAnimatingAway();
                    fragment.setAnimatingAway(null);
                    view.clearAnimation();
                }
                fragment.setAnimator(null);
            }
        });
        callback.onStart(fragment, cancellationSignal);
        if (object.animation != null) {
            object = new EndViewTransitionAnimation(object.animation, viewGroup, view);
            fragment.setAnimatingAway(fragment.mView);
            object.setAnimationListener(new Animation.AnimationListener(){

                public void onAnimationEnd(Animation animation) {
                    viewGroup.post(new Runnable(){

                        @Override
                        public void run() {
                            if (fragment.getAnimatingAway() != null) {
                                fragment.setAnimatingAway(null);
                                callback.onComplete(fragment, cancellationSignal);
                            }
                        }
                    });
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

            });
            fragment.mView.startAnimation((Animation)object);
            return;
        }
        Animator animator = object.animator;
        fragment.setAnimator(object.animator);
        animator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator) {
                viewGroup.endViewTransition(view);
                animator = fragment.getAnimator();
                fragment.setAnimator(null);
                if (animator != null && viewGroup.indexOfChild(view) < 0) {
                    callback.onComplete(fragment, cancellationSignal);
                }
            }
        });
        animator.setTarget((Object)fragment.mView);
        animator.start();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static AnimationOrAnimator loadAnimation(Context context, FragmentContainer object, Fragment fragment, boolean bl) {
        int n;
        int n2;
        block14 : {
            int n3;
            boolean bl2;
            block15 : {
                int n4;
                n2 = fragment.getNextTransition();
                n3 = fragment.getNextAnim();
                fragment.setNextAnim(0);
                object = object.onFindViewById(fragment.mContainerId);
                if (object != null && object.getTag(R.id.visible_removing_fragment_view_tag) != null) {
                    object.setTag(R.id.visible_removing_fragment_view_tag, (Object)null);
                }
                if (fragment.mContainer != null && fragment.mContainer.getLayoutTransition() != null) {
                    return null;
                }
                object = fragment.onCreateAnimation(n2, bl, n3);
                if (object != null) {
                    return new AnimationOrAnimator((Animation)object);
                }
                object = fragment.onCreateAnimator(n2, bl, n3);
                if (object != null) {
                    return new AnimationOrAnimator((Animator)object);
                }
                if (n3 == 0) break block14;
                bl2 = "anim".equals(context.getResources().getResourceTypeName(n3));
                n = n4 = 0;
                if (!bl2) break block15;
                try {
                    object = AnimationUtils.loadAnimation((Context)context, (int)n3);
                    if (object != null) {
                        return new AnimationOrAnimator((Animation)object);
                    }
                    n = 1;
                }
                catch (RuntimeException runtimeException) {
                    n = n4;
                }
                catch (Resources.NotFoundException notFoundException) {
                    throw notFoundException;
                }
                if (n != 0) break block14;
            }
            try {
                object = AnimatorInflater.loadAnimator((Context)context, (int)n3);
                if (object != null) {
                    return new AnimationOrAnimator((Animator)object);
                }
            }
            catch (RuntimeException runtimeException) {
                if (bl2) {
                    throw runtimeException;
                }
                Animation animation = AnimationUtils.loadAnimation((Context)context, (int)n3);
                if (animation == null) break block14;
                return new AnimationOrAnimator(animation);
            }
        }
        if (n2 == 0) {
            return null;
        }
        n = FragmentAnim.transitToAnimResourceId(n2, bl);
        if (n >= 0) return new AnimationOrAnimator(AnimationUtils.loadAnimation((Context)context, (int)n));
        return null;
    }

    private static int transitToAnimResourceId(int n, boolean bl) {
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    return -1;
                }
                n = bl ? R.anim.fragment_close_enter : R.anim.fragment_close_exit;
                return n;
            }
            n = bl ? R.anim.fragment_fade_enter : R.anim.fragment_fade_exit;
            return n;
        }
        n = bl ? R.anim.fragment_open_enter : R.anim.fragment_open_exit;
        return n;
    }

    static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        AnimationOrAnimator(Animator animator) {
            this.animation = null;
            this.animator = animator;
            if (animator != null) {
                return;
            }
            throw new IllegalStateException("Animator cannot be null");
        }

        AnimationOrAnimator(Animation animation) {
            this.animation = animation;
            this.animator = null;
            if (animation != null) {
                return;
            }
            throw new IllegalStateException("Animation cannot be null");
        }
    }

    private static class EndViewTransitionAnimation
    extends AnimationSet
    implements Runnable {
        private boolean mAnimating = true;
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;

        EndViewTransitionAnimation(Animation animation, ViewGroup viewGroup, View view) {
            super(false);
            this.mParent = viewGroup;
            this.mChild = view;
            this.addAnimation(animation);
            this.mParent.post((Runnable)this);
        }

        public boolean getTransformation(long l, Transformation transformation) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(l, transformation)) {
                this.mEnded = true;
                OneShotPreDrawListener.add((View)this.mParent, this);
            }
            return true;
        }

        public boolean getTransformation(long l, Transformation transformation, float f) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(l, transformation, f)) {
                this.mEnded = true;
                OneShotPreDrawListener.add((View)this.mParent, this);
            }
            return true;
        }

        @Override
        public void run() {
            if (!this.mEnded && this.mAnimating) {
                this.mAnimating = false;
                this.mParent.post((Runnable)this);
                return;
            }
            this.mParent.endViewTransition(this.mChild);
            this.mTransitionEnded = true;
        }
    }

}

