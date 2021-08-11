/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 */
package android.support.transition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.transition.ViewGroupOverlayApi14;
import android.support.transition.ViewOverlayImpl;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@RequiresApi(value=14)
class ViewOverlayApi14
implements ViewOverlayImpl {
    protected OverlayViewGroup mOverlayViewGroup;

    ViewOverlayApi14(Context context, ViewGroup viewGroup, View view) {
        this.mOverlayViewGroup = new OverlayViewGroup(context, viewGroup, view, this);
    }

    static ViewOverlayApi14 createFrom(View view) {
        ViewGroup viewGroup = ViewOverlayApi14.getContentView(view);
        if (viewGroup != null) {
            int n = viewGroup.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view2 = viewGroup.getChildAt(i);
                if (!(view2 instanceof OverlayViewGroup)) continue;
                return ((OverlayViewGroup)view2).mViewOverlay;
            }
            return new ViewGroupOverlayApi14(viewGroup.getContext(), viewGroup, view);
        }
        return null;
    }

    static ViewGroup getContentView(View view) {
        while (view != null) {
            if (view.getId() == 16908290 && view instanceof ViewGroup) {
                return (ViewGroup)view;
            }
            if (!(view.getParent() instanceof ViewGroup)) continue;
            view = (ViewGroup)view.getParent();
        }
        return null;
    }

    @Override
    public void add(@NonNull Drawable drawable2) {
        this.mOverlayViewGroup.add(drawable2);
    }

    @Override
    public void clear() {
        this.mOverlayViewGroup.clear();
    }

    ViewGroup getOverlayView() {
        return this.mOverlayViewGroup;
    }

    boolean isEmpty() {
        return this.mOverlayViewGroup.isEmpty();
    }

    @Override
    public void remove(@NonNull Drawable drawable2) {
        this.mOverlayViewGroup.remove(drawable2);
    }

    static class OverlayViewGroup
    extends ViewGroup {
        static Method sInvalidateChildInParentFastMethod;
        ArrayList<Drawable> mDrawables = null;
        ViewGroup mHostView;
        View mRequestingView;
        ViewOverlayApi14 mViewOverlay;

        static {
            try {
                sInvalidateChildInParentFastMethod = ViewGroup.class.getDeclaredMethod("invalidateChildInParentFast", Integer.TYPE, Integer.TYPE, Rect.class);
            }
            catch (NoSuchMethodException noSuchMethodException) {}
        }

        OverlayViewGroup(Context context, ViewGroup viewGroup, View view, ViewOverlayApi14 viewOverlayApi14) {
            super(context);
            this.mHostView = viewGroup;
            this.mRequestingView = view;
            this.setRight(viewGroup.getWidth());
            this.setBottom(viewGroup.getHeight());
            viewGroup.addView((View)this);
            this.mViewOverlay = viewOverlayApi14;
        }

        private void getOffset(int[] arrn) {
            int[] arrn2 = new int[2];
            int[] arrn3 = new int[2];
            this.mHostView.getLocationOnScreen(arrn2);
            this.mRequestingView.getLocationOnScreen(arrn3);
            arrn[0] = arrn3[0] - arrn2[0];
            arrn[1] = arrn3[1] - arrn2[1];
        }

        public void add(Drawable drawable2) {
            if (this.mDrawables == null) {
                this.mDrawables = new ArrayList();
            }
            if (!this.mDrawables.contains((Object)drawable2)) {
                this.mDrawables.add(drawable2);
                this.invalidate(drawable2.getBounds());
                drawable2.setCallback((Drawable.Callback)this);
                return;
            }
        }

        public void add(View view) {
            if (view.getParent() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view.getParent();
                if (viewGroup != this.mHostView && viewGroup.getParent() != null && ViewCompat.isAttachedToWindow((View)viewGroup)) {
                    int[] arrn = new int[2];
                    int[] arrn2 = new int[2];
                    viewGroup.getLocationOnScreen(arrn);
                    this.mHostView.getLocationOnScreen(arrn2);
                    ViewCompat.offsetLeftAndRight(view, arrn[0] - arrn2[0]);
                    ViewCompat.offsetTopAndBottom(view, arrn[1] - arrn2[1]);
                }
                viewGroup.removeView(view);
                if (view.getParent() != null) {
                    viewGroup.removeView(view);
                }
            }
            super.addView(view, this.getChildCount() - 1);
        }

        public void clear() {
            this.removeAllViews();
            ArrayList<Drawable> arrayList = this.mDrawables;
            if (arrayList != null) {
                arrayList.clear();
                return;
            }
        }

        protected void dispatchDraw(Canvas canvas) {
            Object object = new int[2];
            int[] arrn = new int[2];
            this.mHostView.getLocationOnScreen((int[])object);
            this.mRequestingView.getLocationOnScreen(arrn);
            int n = 0;
            canvas.translate((float)(arrn[0] - object[0]), (float)(arrn[1] - object[1]));
            canvas.clipRect(new Rect(0, 0, this.mRequestingView.getWidth(), this.mRequestingView.getHeight()));
            super.dispatchDraw(canvas);
            object = this.mDrawables;
            if (object != null) {
                n = object.size();
            }
            for (int i = 0; i < n; ++i) {
                this.mDrawables.get(i).draw(canvas);
            }
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public void invalidateChildFast(View arrn, Rect rect) {
            if (this.mHostView != null) {
                int n = arrn.getLeft();
                int n2 = arrn.getTop();
                arrn = new int[2];
                this.getOffset(arrn);
                rect.offset(arrn[0] + n, arrn[1] + n2);
                this.mHostView.invalidate(rect);
                return;
            }
        }

        public ViewParent invalidateChildInParent(int[] arrn, Rect rect) {
            if (this.mHostView != null) {
                rect.offset(arrn[0], arrn[1]);
                if (this.mHostView instanceof ViewGroup) {
                    arrn[0] = 0;
                    arrn[1] = 0;
                    int[] arrn2 = new int[2];
                    this.getOffset(arrn2);
                    rect.offset(arrn2[0], arrn2[1]);
                    return super.invalidateChildInParent(arrn, rect);
                }
                this.invalidate(rect);
            }
            return null;
        }

        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        protected ViewParent invalidateChildInParentFast(int n, int n2, Rect rect) {
            if (this.mHostView instanceof ViewGroup && sInvalidateChildInParentFastMethod != null) {
                try {
                    this.getOffset(new int[2]);
                    sInvalidateChildInParentFastMethod.invoke((Object)this.mHostView, new Object[]{n, n2, rect});
                }
                catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
                catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            }
            return null;
        }

        public void invalidateDrawable(@NonNull Drawable drawable2) {
            this.invalidate(drawable2.getBounds());
        }

        boolean isEmpty() {
            ArrayList<Drawable> arrayList;
            if (this.getChildCount() == 0 && ((arrayList = this.mDrawables) == null || arrayList.size() == 0)) {
                return true;
            }
            return false;
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        }

        public void remove(Drawable drawable2) {
            ArrayList<Drawable> arrayList = this.mDrawables;
            if (arrayList != null) {
                arrayList.remove((Object)drawable2);
                this.invalidate(drawable2.getBounds());
                drawable2.setCallback(null);
                return;
            }
        }

        public void remove(View view) {
            super.removeView(view);
            if (this.isEmpty()) {
                this.mHostView.removeView((View)this);
                return;
            }
        }

        protected boolean verifyDrawable(@NonNull Drawable drawable2) {
            ArrayList<Drawable> arrayList;
            if (!(super.verifyDrawable(drawable2) || (arrayList = this.mDrawables) != null && arrayList.contains((Object)drawable2))) {
                return false;
            }
            return true;
        }

        static class TouchInterceptor
        extends View {
            TouchInterceptor(Context context) {
                super(context);
            }
        }

    }

}

