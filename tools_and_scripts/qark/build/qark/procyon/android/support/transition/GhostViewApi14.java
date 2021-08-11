// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.view.ViewParent;
import android.widget.FrameLayout;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.graphics.Matrix;
import android.support.annotation.RequiresApi;
import android.annotation.SuppressLint;
import android.view.View;

@SuppressLint({ "ViewConstructor" })
@RequiresApi(14)
class GhostViewApi14 extends View implements GhostViewImpl
{
    Matrix mCurrentMatrix;
    private int mDeltaX;
    private int mDeltaY;
    private final Matrix mMatrix;
    private final ViewTreeObserver$OnPreDrawListener mOnPreDrawListener;
    int mReferences;
    ViewGroup mStartParent;
    View mStartView;
    final View mView;
    
    GhostViewApi14(final View mView) {
        super(mView.getContext());
        this.mMatrix = new Matrix();
        this.mOnPreDrawListener = (ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
            public boolean onPreDraw() {
                final GhostViewApi14 this$0 = GhostViewApi14.this;
                this$0.mCurrentMatrix = this$0.mView.getMatrix();
                ViewCompat.postInvalidateOnAnimation(GhostViewApi14.this);
                if (GhostViewApi14.this.mStartParent != null && GhostViewApi14.this.mStartView != null) {
                    GhostViewApi14.this.mStartParent.endViewTransition(GhostViewApi14.this.mStartView);
                    ViewCompat.postInvalidateOnAnimation((View)GhostViewApi14.this.mStartParent);
                    final GhostViewApi14 this$2 = GhostViewApi14.this;
                    this$2.mStartParent = null;
                    this$2.mStartView = null;
                }
                return true;
            }
        };
        this.mView = mView;
        this.setLayerType(2, (Paint)null);
    }
    
    static GhostViewApi14 getGhostView(@NonNull final View view) {
        return (GhostViewApi14)view.getTag(R.id.ghost_view);
    }
    
    private static void setGhostView(@NonNull final View view, final GhostViewApi14 ghostViewApi14) {
        view.setTag(R.id.ghost_view, (Object)ghostViewApi14);
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setGhostView(this.mView, this);
        final int[] array = new int[2];
        final int[] array2 = new int[2];
        this.getLocationOnScreen(array);
        this.mView.getLocationOnScreen(array2);
        array2[0] -= (int)this.mView.getTranslationX();
        array2[1] -= (int)this.mView.getTranslationY();
        this.mDeltaX = array2[0] - array[0];
        this.mDeltaY = array2[1] - array[1];
        this.mView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        this.mView.setVisibility(4);
    }
    
    protected void onDetachedFromWindow() {
        this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        this.mView.setVisibility(0);
        setGhostView(this.mView, null);
        super.onDetachedFromWindow();
    }
    
    protected void onDraw(final Canvas canvas) {
        this.mMatrix.set(this.mCurrentMatrix);
        this.mMatrix.postTranslate((float)this.mDeltaX, (float)this.mDeltaY);
        canvas.setMatrix(this.mMatrix);
        this.mView.draw(canvas);
    }
    
    public void reserveEndViewTransition(final ViewGroup mStartParent, final View mStartView) {
        this.mStartParent = mStartParent;
        this.mStartView = mStartView;
    }
    
    public void setVisibility(int n) {
        super.setVisibility(n);
        final View mView = this.mView;
        if (n == 0) {
            n = 4;
        }
        else {
            n = 0;
        }
        mView.setVisibility(n);
    }
    
    static class Creator implements GhostViewImpl.Creator
    {
        private static FrameLayout findFrameLayout(ViewGroup viewGroup) {
            while (!(viewGroup instanceof FrameLayout)) {
                final ViewParent parent = viewGroup.getParent();
                if (!(parent instanceof ViewGroup)) {
                    return null;
                }
                viewGroup = (ViewGroup)parent;
            }
            return (FrameLayout)viewGroup;
        }
        
        @Override
        public GhostViewImpl addGhost(final View view, final ViewGroup viewGroup, final Matrix matrix) {
            final GhostViewApi14 ghostView = GhostViewApi14.getGhostView(view);
            GhostViewApi14 ghostViewApi14;
            if (ghostView == null) {
                final FrameLayout frameLayout = findFrameLayout(viewGroup);
                if (frameLayout == null) {
                    return null;
                }
                ghostViewApi14 = new GhostViewApi14(view);
                frameLayout.addView((View)ghostViewApi14);
            }
            else {
                ghostViewApi14 = ghostView;
            }
            ++ghostViewApi14.mReferences;
            return ghostViewApi14;
        }
        
        @Override
        public void removeGhost(final View view) {
            final GhostViewApi14 ghostView = GhostViewApi14.getGhostView(view);
            if (ghostView == null) {
                return;
            }
            --ghostView.mReferences;
            if (ghostView.mReferences > 0) {
                return;
            }
            final ViewParent parent = ghostView.getParent();
            if (parent instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup)parent;
                viewGroup.endViewTransition((View)ghostView);
                viewGroup.removeView((View)ghostView);
            }
        }
    }
}
