// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.graphics.Paint;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TimeInterpolator;
import android.os.Build$VERSION;
import android.view.animation.Interpolator;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import java.lang.ref.WeakReference;

public final class ViewPropertyAnimatorCompat
{
    static final int LISTENER_TAG_ID = 2113929216;
    private static final String TAG = "ViewAnimatorCompat";
    Runnable mEndAction;
    int mOldLayerType;
    Runnable mStartAction;
    private WeakReference<View> mView;
    
    ViewPropertyAnimatorCompat(final View view) {
        this.mStartAction = null;
        this.mEndAction = null;
        this.mOldLayerType = -1;
        this.mView = new WeakReference<View>(view);
    }
    
    private void setListenerInternal(final View view, final ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (viewPropertyAnimatorListener != null) {
            view.animate().setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator animator) {
                    viewPropertyAnimatorListener.onAnimationCancel(view);
                }
                
                public void onAnimationEnd(final Animator animator) {
                    viewPropertyAnimatorListener.onAnimationEnd(view);
                }
                
                public void onAnimationStart(final Animator animator) {
                    viewPropertyAnimatorListener.onAnimationStart(view);
                }
            });
            return;
        }
        view.animate().setListener((Animator$AnimatorListener)null);
    }
    
    public ViewPropertyAnimatorCompat alpha(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().alpha(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat alphaBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().alphaBy(n);
            return this;
        }
        return this;
    }
    
    public void cancel() {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().cancel();
        }
    }
    
    public long getDuration() {
        final View view = this.mView.get();
        if (view != null) {
            return view.animate().getDuration();
        }
        return 0L;
    }
    
    public Interpolator getInterpolator() {
        final View view = this.mView.get();
        if (view != null && Build$VERSION.SDK_INT >= 18) {
            return (Interpolator)view.animate().getInterpolator();
        }
        return null;
    }
    
    public long getStartDelay() {
        final View view = this.mView.get();
        if (view != null) {
            return view.animate().getStartDelay();
        }
        return 0L;
    }
    
    public ViewPropertyAnimatorCompat rotation(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().rotation(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat rotationBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().rotationBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat rotationX(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().rotationX(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat rotationXBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().rotationXBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat rotationY(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().rotationY(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat rotationYBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().rotationYBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat scaleX(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().scaleX(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat scaleXBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().scaleXBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat scaleY(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().scaleY(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat scaleYBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().scaleYBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat setDuration(final long duration) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().setDuration(duration);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat setInterpolator(final Interpolator interpolator) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().setInterpolator((TimeInterpolator)interpolator);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat setListener(final ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            this.setListenerInternal(view, viewPropertyAnimatorListener);
            return this;
        }
        view.setTag(2113929216, (Object)viewPropertyAnimatorListener);
        this.setListenerInternal(view, new ViewPropertyAnimatorListenerApi14(this));
        return this;
    }
    
    public ViewPropertyAnimatorCompat setStartDelay(final long startDelay) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().setStartDelay(startDelay);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat setUpdateListener(final ViewPropertyAnimatorUpdateListener viewPropertyAnimatorUpdateListener) {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            final ValueAnimator$AnimatorUpdateListener valueAnimator$AnimatorUpdateListener = null;
            Object updateListener;
            if (viewPropertyAnimatorUpdateListener != null) {
                updateListener = new ValueAnimator$AnimatorUpdateListener() {
                    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                        viewPropertyAnimatorUpdateListener.onAnimationUpdate(view);
                    }
                };
            }
            else {
                updateListener = valueAnimator$AnimatorUpdateListener;
            }
            view.animate().setUpdateListener((ValueAnimator$AnimatorUpdateListener)updateListener);
            return this;
        }
        return this;
    }
    
    public void start() {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().start();
        }
    }
    
    public ViewPropertyAnimatorCompat translationX(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().translationX(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat translationXBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().translationXBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat translationY(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().translationY(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat translationYBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().translationYBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat translationZ(final float n) {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            view.animate().translationZ(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat translationZBy(final float n) {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            view.animate().translationZBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat withEndAction(final Runnable mEndAction) {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            view.animate().withEndAction(mEndAction);
            return this;
        }
        this.setListenerInternal(view, new ViewPropertyAnimatorListenerApi14(this));
        this.mEndAction = mEndAction;
        return this;
    }
    
    public ViewPropertyAnimatorCompat withLayer() {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            view.animate().withLayer();
            return this;
        }
        this.mOldLayerType = view.getLayerType();
        this.setListenerInternal(view, new ViewPropertyAnimatorListenerApi14(this));
        return this;
    }
    
    public ViewPropertyAnimatorCompat withStartAction(final Runnable mStartAction) {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            view.animate().withStartAction(mStartAction);
            return this;
        }
        this.setListenerInternal(view, new ViewPropertyAnimatorListenerApi14(this));
        this.mStartAction = mStartAction;
        return this;
    }
    
    public ViewPropertyAnimatorCompat x(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().x(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat xBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().xBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat y(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().y(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat yBy(final float n) {
        final View view = this.mView.get();
        if (view != null) {
            view.animate().yBy(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat z(final float n) {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            view.animate().z(n);
            return this;
        }
        return this;
    }
    
    public ViewPropertyAnimatorCompat zBy(final float n) {
        final View view = this.mView.get();
        if (view == null) {
            return this;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            view.animate().zBy(n);
            return this;
        }
        return this;
    }
    
    static class ViewPropertyAnimatorListenerApi14 implements ViewPropertyAnimatorListener
    {
        boolean mAnimEndCalled;
        ViewPropertyAnimatorCompat mVpa;
        
        ViewPropertyAnimatorListenerApi14(final ViewPropertyAnimatorCompat mVpa) {
            this.mVpa = mVpa;
        }
        
        @Override
        public void onAnimationCancel(final View view) {
            final Object tag = view.getTag(2113929216);
            ViewPropertyAnimatorListener viewPropertyAnimatorListener = null;
            if (tag instanceof ViewPropertyAnimatorListener) {
                viewPropertyAnimatorListener = (ViewPropertyAnimatorListener)tag;
            }
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationCancel(view);
            }
        }
        
        @Override
        public void onAnimationEnd(final View view) {
            if (this.mVpa.mOldLayerType > -1) {
                view.setLayerType(this.mVpa.mOldLayerType, (Paint)null);
                this.mVpa.mOldLayerType = -1;
            }
            if (Build$VERSION.SDK_INT < 16 && this.mAnimEndCalled) {
                return;
            }
            if (this.mVpa.mEndAction != null) {
                final Runnable mEndAction = this.mVpa.mEndAction;
                this.mVpa.mEndAction = null;
                mEndAction.run();
            }
            final Object tag = view.getTag(2113929216);
            ViewPropertyAnimatorListener viewPropertyAnimatorListener = null;
            if (tag instanceof ViewPropertyAnimatorListener) {
                viewPropertyAnimatorListener = (ViewPropertyAnimatorListener)tag;
            }
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationEnd(view);
            }
            this.mAnimEndCalled = true;
        }
        
        @Override
        public void onAnimationStart(final View view) {
            this.mAnimEndCalled = false;
            if (this.mVpa.mOldLayerType > -1) {
                view.setLayerType(2, (Paint)null);
            }
            if (this.mVpa.mStartAction != null) {
                final Runnable mStartAction = this.mVpa.mStartAction;
                this.mVpa.mStartAction = null;
                mStartAction.run();
            }
            final Object tag = view.getTag(2113929216);
            ViewPropertyAnimatorListener viewPropertyAnimatorListener = null;
            if (tag instanceof ViewPropertyAnimatorListener) {
                viewPropertyAnimatorListener = (ViewPropertyAnimatorListener)tag;
            }
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationStart(view);
            }
        }
    }
}
