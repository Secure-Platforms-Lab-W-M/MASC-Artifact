/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.GestureDetector$SimpleOnGestureListener
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewParent
 *  android.view.animation.Interpolator
 */
package android.support.v7.widget.helper;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.support.v7.widget.helper.ItemTouchUIUtilImpl;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper
extends RecyclerView.ItemDecoration
implements RecyclerView.OnChildAttachStateChangeListener {
    static final int ACTION_MODE_DRAG_MASK = 16711680;
    private static final int ACTION_MODE_IDLE_MASK = 255;
    static final int ACTION_MODE_SWIPE_MASK = 65280;
    public static final int ACTION_STATE_DRAG = 2;
    public static final int ACTION_STATE_IDLE = 0;
    public static final int ACTION_STATE_SWIPE = 1;
    static final int ACTIVE_POINTER_ID_NONE = -1;
    public static final int ANIMATION_TYPE_DRAG = 8;
    public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
    public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
    static final boolean DEBUG = false;
    static final int DIRECTION_FLAG_COUNT = 8;
    public static final int DOWN = 2;
    public static final int END = 32;
    public static final int LEFT = 4;
    private static final int PIXELS_PER_SECOND = 1000;
    public static final int RIGHT = 8;
    public static final int START = 16;
    static final String TAG = "ItemTouchHelper";
    public static final int UP = 1;
    int mActionState = 0;
    int mActivePointerId = -1;
    Callback mCallback;
    private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback;
    private List<Integer> mDistances;
    private long mDragScrollStartTimeInMs;
    float mDx;
    float mDy;
    GestureDetectorCompat mGestureDetector;
    float mInitialTouchX;
    float mInitialTouchY;
    float mMaxSwipeVelocity;
    private final RecyclerView.OnItemTouchListener mOnItemTouchListener;
    View mOverdrawChild;
    int mOverdrawChildPosition;
    final List<View> mPendingCleanup = new ArrayList<View>();
    List<RecoverAnimation> mRecoverAnimations = new ArrayList<RecoverAnimation>();
    RecyclerView mRecyclerView;
    final Runnable mScrollRunnable;
    RecyclerView.ViewHolder mSelected = null;
    int mSelectedFlags;
    float mSelectedStartX;
    float mSelectedStartY;
    private int mSlop;
    private List<RecyclerView.ViewHolder> mSwapTargets;
    float mSwipeEscapeVelocity;
    private final float[] mTmpPosition = new float[2];
    private Rect mTmpRect;
    VelocityTracker mVelocityTracker;

    public ItemTouchHelper(Callback callback) {
        this.mScrollRunnable = new Runnable(){

            @Override
            public void run() {
                if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
                    if (ItemTouchHelper.this.mSelected != null) {
                        ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                        itemTouchHelper.moveIfNecessary(itemTouchHelper.mSelected);
                    }
                    ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                    ViewCompat.postOnAnimation((View)ItemTouchHelper.this.mRecyclerView, this);
                    return;
                }
            }
        };
        this.mChildDrawingOrderCallback = null;
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        this.mOnItemTouchListener = new RecyclerView.OnItemTouchListener(){

            @Override
            public boolean onInterceptTouchEvent(RecyclerView object, MotionEvent motionEvent) {
                ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
                int n = motionEvent.getActionMasked();
                if (n == 0) {
                    ItemTouchHelper.this.mActivePointerId = motionEvent.getPointerId(0);
                    ItemTouchHelper.this.mInitialTouchX = motionEvent.getX();
                    ItemTouchHelper.this.mInitialTouchY = motionEvent.getY();
                    ItemTouchHelper.this.obtainVelocityTracker();
                    if (ItemTouchHelper.this.mSelected == null && (object = ItemTouchHelper.this.findAnimation(motionEvent)) != null) {
                        ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                        itemTouchHelper.mInitialTouchX -= object.mX;
                        itemTouchHelper = ItemTouchHelper.this;
                        itemTouchHelper.mInitialTouchY -= object.mY;
                        ItemTouchHelper.this.endRecoverAnimation(object.mViewHolder, true);
                        if (ItemTouchHelper.this.mPendingCleanup.remove((Object)object.mViewHolder.itemView)) {
                            ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, object.mViewHolder);
                        }
                        ItemTouchHelper.this.select(object.mViewHolder, object.mActionState);
                        object = ItemTouchHelper.this;
                        object.updateDxDy(motionEvent, object.mSelectedFlags, 0);
                    }
                } else if (n != 3 && n != 1) {
                    int n2;
                    if (ItemTouchHelper.this.mActivePointerId != -1 && (n2 = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId)) >= 0) {
                        ItemTouchHelper.this.checkSelectForSwipe(n, motionEvent, n2);
                    }
                } else {
                    object = ItemTouchHelper.this;
                    object.mActivePointerId = -1;
                    object.select(null, 0);
                }
                if (ItemTouchHelper.this.mVelocityTracker != null) {
                    ItemTouchHelper.this.mVelocityTracker.addMovement(motionEvent);
                }
                if (ItemTouchHelper.this.mSelected != null) {
                    return true;
                }
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean bl) {
                if (!bl) {
                    return;
                }
                ItemTouchHelper.this.select(null, 0);
            }

            @Override
            public void onTouchEvent(RecyclerView object, MotionEvent motionEvent) {
                ItemTouchHelper.this.mGestureDetector.onTouchEvent(motionEvent);
                if (ItemTouchHelper.this.mVelocityTracker != null) {
                    ItemTouchHelper.this.mVelocityTracker.addMovement(motionEvent);
                }
                if (ItemTouchHelper.this.mActivePointerId == -1) {
                    return;
                }
                int n = motionEvent.getActionMasked();
                int n2 = motionEvent.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                if (n2 >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(n, motionEvent, n2);
                }
                if ((object = ItemTouchHelper.this.mSelected) == null) {
                    return;
                }
                int n3 = 0;
                if (n != 6) {
                    switch (n) {
                        default: {
                            return;
                        }
                        case 3: {
                            if (ItemTouchHelper.this.mVelocityTracker == null) break;
                            ItemTouchHelper.this.mVelocityTracker.clear();
                            break;
                        }
                        case 2: {
                            if (n2 >= 0) {
                                ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                                itemTouchHelper.updateDxDy(motionEvent, itemTouchHelper.mSelectedFlags, n2);
                                ItemTouchHelper.this.moveIfNecessary((RecyclerView.ViewHolder)object);
                                ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                                ItemTouchHelper.this.mScrollRunnable.run();
                                ItemTouchHelper.this.mRecyclerView.invalidate();
                                return;
                            }
                            return;
                        }
                        case 1: 
                    }
                    ItemTouchHelper.this.select(null, 0);
                    ItemTouchHelper.this.mActivePointerId = -1;
                    return;
                }
                n = motionEvent.getActionIndex();
                if (motionEvent.getPointerId(n) == ItemTouchHelper.this.mActivePointerId) {
                    if (n == 0) {
                        n3 = 1;
                    }
                    ItemTouchHelper.this.mActivePointerId = motionEvent.getPointerId(n3);
                    object = ItemTouchHelper.this;
                    object.updateDxDy(motionEvent, object.mSelectedFlags, n);
                    return;
                }
            }
        };
        this.mCallback = callback;
    }

    private void addChildDrawingOrderCallback() {
        if (Build.VERSION.SDK_INT >= 21) {
            return;
        }
        if (this.mChildDrawingOrderCallback == null) {
            this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback(){

                @Override
                public int onGetChildDrawingOrder(int n, int n2) {
                    if (ItemTouchHelper.this.mOverdrawChild == null) {
                        return n2;
                    }
                    int n3 = ItemTouchHelper.this.mOverdrawChildPosition;
                    if (n3 == -1) {
                        ItemTouchHelper.this.mOverdrawChildPosition = n3 = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                    }
                    if (n2 == n - 1) {
                        return n3;
                    }
                    if (n2 < n3) {
                        return n2;
                    }
                    return n2 + 1;
                }
            };
        }
        this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
    }

    private int checkHorizontalSwipe(RecyclerView.ViewHolder viewHolder, int n) {
        if ((n & 12) != 0) {
            float f;
            float f2 = this.mDx;
            int n2 = 8;
            int n3 = f2 > 0.0f ? 8 : 4;
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null && this.mActivePointerId > -1) {
                velocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
                f = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
                f2 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                if (f <= 0.0f) {
                    n2 = 4;
                }
                f = Math.abs(f);
                if ((n2 & n) != 0 && n3 == n2 && f >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && f > Math.abs(f2)) {
                    return n2;
                }
            }
            f2 = this.mRecyclerView.getWidth();
            f = this.mCallback.getSwipeThreshold(viewHolder);
            if ((n & n3) != 0 && Math.abs(this.mDx) > f2 * f) {
                return n3;
            }
        }
        return 0;
    }

    private int checkVerticalSwipe(RecyclerView.ViewHolder viewHolder, int n) {
        if ((n & 3) != 0) {
            float f;
            float f2 = this.mDy;
            int n2 = 2;
            int n3 = f2 > 0.0f ? 2 : 1;
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null && this.mActivePointerId > -1) {
                velocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
                f2 = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
                f = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                if (f <= 0.0f) {
                    n2 = 1;
                }
                f = Math.abs(f);
                if ((n2 & n) != 0 && n2 == n3 && f >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && f > Math.abs(f2)) {
                    return n2;
                }
            }
            f2 = this.mRecyclerView.getHeight();
            f = this.mCallback.getSwipeThreshold(viewHolder);
            if ((n & n3) != 0 && Math.abs(this.mDy) > f2 * f) {
                return n3;
            }
        }
        return 0;
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(0);
            this.mCallback.clearView(this.mRecyclerView, recoverAnimation.mViewHolder);
        }
        this.mRecoverAnimations.clear();
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        this.releaseVelocityTracker();
    }

    private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder viewHolder) {
        Object object = viewHolder;
        View view = this.mSwapTargets;
        if (view == null) {
            this.mSwapTargets = new ArrayList<RecyclerView.ViewHolder>();
            this.mDistances = new ArrayList<Integer>();
        } else {
            view.clear();
            this.mDistances.clear();
        }
        int n = this.mCallback.getBoundingBoxMargin();
        int n2 = Math.round(this.mSelectedStartX + this.mDx) - n;
        int n3 = Math.round(this.mSelectedStartY + this.mDy) - n;
        int n4 = object.itemView.getWidth() + n2 + n * 2;
        int n5 = object.itemView.getHeight() + n3 + n * 2;
        int n6 = (n2 + n4) / 2;
        int n7 = (n3 + n5) / 2;
        object = this.mRecyclerView.getLayoutManager();
        int n8 = object.getChildCount();
        for (int i = 0; i < n8; ++i) {
            RecyclerView.ViewHolder viewHolder2;
            view = object.getChildAt(i);
            if (view == viewHolder.itemView || view.getBottom() < n3 || view.getTop() > n5 || view.getRight() < n2 || view.getLeft() > n4 || !this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, viewHolder2 = this.mRecyclerView.getChildViewHolder(view))) continue;
            int n9 = Math.abs(n6 - (view.getLeft() + view.getRight()) / 2);
            int n10 = Math.abs(n7 - (view.getTop() + view.getBottom()) / 2);
            int n11 = n9 * n9 + n10 * n10;
            n9 = this.mSwapTargets.size();
            int n12 = 0;
            for (n10 = 0; n10 < n9 && n11 > this.mDistances.get(n10); ++n10) {
                ++n12;
            }
            this.mSwapTargets.add(n12, viewHolder2);
            this.mDistances.add(n12, n11);
        }
        return this.mSwapTargets;
    }

    private RecyclerView.ViewHolder findSwipedView(MotionEvent motionEvent) {
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        int n = this.mActivePointerId;
        if (n == -1) {
            return null;
        }
        n = motionEvent.findPointerIndex(n);
        float f = motionEvent.getX(n);
        float f2 = this.mInitialTouchX;
        float f3 = motionEvent.getY(n);
        float f4 = this.mInitialTouchY;
        f = Math.abs(f - f2);
        f3 = Math.abs(f3 - f4);
        n = this.mSlop;
        if (f < (float)n && f3 < (float)n) {
            return null;
        }
        if (f > f3 && layoutManager.canScrollHorizontally()) {
            return null;
        }
        if (f3 > f && layoutManager.canScrollVertically()) {
            return null;
        }
        if ((motionEvent = this.findChildView(motionEvent)) == null) {
            return null;
        }
        return this.mRecyclerView.getChildViewHolder((View)motionEvent);
    }

    private void getSelectedDxDy(float[] arrf) {
        arrf[0] = (this.mSelectedFlags & 12) != 0 ? this.mSelectedStartX + this.mDx - (float)this.mSelected.itemView.getLeft() : this.mSelected.itemView.getTranslationX();
        if ((this.mSelectedFlags & 3) != 0) {
            arrf[1] = this.mSelectedStartY + this.mDy - (float)this.mSelected.itemView.getTop();
            return;
        }
        arrf[1] = this.mSelected.itemView.getTranslationY();
    }

    private static boolean hitTest(View view, float f, float f2, float f3, float f4) {
        if (f >= f3 && f <= (float)view.getWidth() + f3 && f2 >= f4 && f2 <= (float)view.getHeight() + f4) {
            return true;
        }
        return false;
    }

    private void initGestureDetector() {
        if (this.mGestureDetector != null) {
            return;
        }
        this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), (GestureDetector.OnGestureListener)new ItemTouchHelperGestureListener());
    }

    private void releaseVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
            return;
        }
    }

    private void setupCallbacks() {
        this.mSlop = ViewConfiguration.get((Context)this.mRecyclerView.getContext()).getScaledTouchSlop();
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this);
        this.initGestureDetector();
    }

    private int swipeIfNecessary(RecyclerView.ViewHolder viewHolder) {
        if (this.mActionState == 2) {
            return 0;
        }
        int n = this.mCallback.getMovementFlags(this.mRecyclerView, viewHolder);
        int n2 = (this.mCallback.convertToAbsoluteDirection(n, ViewCompat.getLayoutDirection((View)this.mRecyclerView)) & 65280) >> 8;
        if (n2 == 0) {
            return 0;
        }
        n = (65280 & n) >> 8;
        if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
            int n3 = this.checkHorizontalSwipe(viewHolder, n2);
            if (n3 > 0) {
                if ((n & n3) == 0) {
                    return Callback.convertToRelativeDirection(n3, ViewCompat.getLayoutDirection((View)this.mRecyclerView));
                }
                return n3;
            }
            if ((n2 = this.checkVerticalSwipe(viewHolder, n2)) > 0) {
                return n2;
            }
            return 0;
        }
        int n4 = this.checkVerticalSwipe(viewHolder, n2);
        if (n4 > 0) {
            return n4;
        }
        if ((n2 = this.checkHorizontalSwipe(viewHolder, n2)) > 0) {
            if ((n & n2) == 0) {
                return Callback.convertToRelativeDirection(n2, ViewCompat.getLayoutDirection((View)this.mRecyclerView));
            }
            return n2;
        }
        return 0;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            this.destroyCallbacks();
        }
        this.mRecyclerView = recyclerView;
        if (this.mRecyclerView != null) {
            recyclerView = recyclerView.getResources();
            this.mSwipeEscapeVelocity = recyclerView.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
            this.mMaxSwipeVelocity = recyclerView.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
            this.setupCallbacks();
            return;
        }
    }

    boolean checkSelectForSwipe(int n, MotionEvent motionEvent, int n2) {
        if (this.mSelected == null && n == 2 && this.mActionState != 2) {
            if (!this.mCallback.isItemViewSwipeEnabled()) {
                return false;
            }
            if (this.mRecyclerView.getScrollState() == 1) {
                return false;
            }
            RecyclerView.ViewHolder viewHolder = this.findSwipedView(motionEvent);
            if (viewHolder == null) {
                return false;
            }
            n = (65280 & this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolder)) >> 8;
            if (n == 0) {
                return false;
            }
            float f = motionEvent.getX(n2);
            float f2 = motionEvent.getY(n2);
            float f3 = Math.abs(f -= this.mInitialTouchX);
            float f4 = Math.abs(f2 -= this.mInitialTouchY);
            n2 = this.mSlop;
            if (f3 < (float)n2 && f4 < (float)n2) {
                return false;
            }
            if (f3 > f4) {
                if (f < 0.0f && (n & 4) == 0) {
                    return false;
                }
                if (f > 0.0f && (n & 8) == 0) {
                    return false;
                }
            } else {
                if (f2 < 0.0f && (n & 1) == 0) {
                    return false;
                }
                if (f2 > 0.0f && (n & 2) == 0) {
                    return false;
                }
            }
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.select(viewHolder, 1);
            return true;
        }
        return false;
    }

    int endRecoverAnimation(RecyclerView.ViewHolder viewHolder, boolean bl) {
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
            if (recoverAnimation.mViewHolder != viewHolder) continue;
            recoverAnimation.mOverridden |= bl;
            if (!recoverAnimation.mEnded) {
                recoverAnimation.cancel();
            }
            this.mRecoverAnimations.remove(i);
            return recoverAnimation.mAnimationType;
        }
        return 0;
    }

    RecoverAnimation findAnimation(MotionEvent motionEvent) {
        if (this.mRecoverAnimations.isEmpty()) {
            return null;
        }
        motionEvent = this.findChildView(motionEvent);
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(i);
            if (recoverAnimation.mViewHolder.itemView != motionEvent) continue;
            return recoverAnimation;
        }
        return null;
    }

    View findChildView(MotionEvent object) {
        float f = object.getX();
        float f2 = object.getY();
        object = this.mSelected;
        if (object != null && ItemTouchHelper.hitTest((View)(object = object.itemView), f, f2, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
            return object;
        }
        for (int i = this.mRecoverAnimations.size() - 1; i >= 0; --i) {
            object = this.mRecoverAnimations.get(i);
            View view = object.mViewHolder.itemView;
            if (!ItemTouchHelper.hitTest(view, f, f2, object.mX, object.mY)) continue;
            return view;
        }
        return this.mRecyclerView.findChildViewUnder(f, f2);
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        rect.setEmpty();
    }

    boolean hasRunningRecoverAnim() {
        int n = this.mRecoverAnimations.size();
        for (int i = 0; i < n; ++i) {
            if (this.mRecoverAnimations.get((int)i).mEnded) continue;
            return true;
        }
        return false;
    }

    void moveIfNecessary(RecyclerView.ViewHolder viewHolder) {
        if (this.mRecyclerView.isLayoutRequested()) {
            return;
        }
        if (this.mActionState != 2) {
            return;
        }
        float f = this.mCallback.getMoveThreshold(viewHolder);
        int n = (int)(this.mSelectedStartX + this.mDx);
        int n2 = (int)(this.mSelectedStartY + this.mDy);
        if ((float)Math.abs(n2 - viewHolder.itemView.getTop()) < (float)viewHolder.itemView.getHeight() * f && (float)Math.abs(n - viewHolder.itemView.getLeft()) < (float)viewHolder.itemView.getWidth() * f) {
            return;
        }
        List<RecyclerView.ViewHolder> list = this.findSwapTargets(viewHolder);
        if (list.size() == 0) {
            return;
        }
        if ((list = this.mCallback.chooseDropTarget(viewHolder, list, n, n2)) == null) {
            this.mSwapTargets.clear();
            this.mDistances.clear();
            return;
        }
        int n3 = list.getAdapterPosition();
        int n4 = viewHolder.getAdapterPosition();
        if (this.mCallback.onMove(this.mRecyclerView, viewHolder, (RecyclerView.ViewHolder)((Object)list))) {
            this.mCallback.onMoved(this.mRecyclerView, viewHolder, n4, (RecyclerView.ViewHolder)((Object)list), n3, n, n2);
            return;
        }
    }

    void obtainVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
    }

    @Override
    public void onChildViewDetachedFromWindow(View object) {
        this.removeChildDrawingOrderCallbackIfNecessary((View)object);
        object = this.mRecyclerView.getChildViewHolder((View)object);
        if (object == null) {
            return;
        }
        RecyclerView.ViewHolder viewHolder = this.mSelected;
        if (viewHolder != null && object == viewHolder) {
            this.select(null, 0);
            return;
        }
        this.endRecoverAnimation((RecyclerView.ViewHolder)object, false);
        if (this.mPendingCleanup.remove((Object)object.itemView)) {
            this.mCallback.clearView(this.mRecyclerView, (RecyclerView.ViewHolder)object);
            return;
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State arrf) {
        this.mOverdrawChildPosition = -1;
        float f = 0.0f;
        float f2 = 0.0f;
        if (this.mSelected != null) {
            this.getSelectedDxDy(this.mTmpPosition);
            arrf = this.mTmpPosition;
            f = arrf[0];
            f2 = arrf[1];
        }
        this.mCallback.onDraw(canvas, recyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f, f2);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State arrf) {
        float f = 0.0f;
        float f2 = 0.0f;
        if (this.mSelected != null) {
            this.getSelectedDxDy(this.mTmpPosition);
            arrf = this.mTmpPosition;
            f = arrf[0];
            f2 = arrf[1];
        }
        this.mCallback.onDrawOver(canvas, recyclerView, this.mSelected, this.mRecoverAnimations, this.mActionState, f, f2);
    }

    void postDispatchSwipe(final RecoverAnimation recoverAnimation, final int n) {
        this.mRecyclerView.post(new Runnable(){

            @Override
            public void run() {
                if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !recoverAnimation.mOverridden) {
                    if (recoverAnimation.mViewHolder.getAdapterPosition() != -1) {
                        RecyclerView.ItemAnimator itemAnimator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
                        if (itemAnimator == null || !itemAnimator.isRunning(null)) {
                            if (!ItemTouchHelper.this.hasRunningRecoverAnim()) {
                                ItemTouchHelper.this.mCallback.onSwiped(recoverAnimation.mViewHolder, n);
                                return;
                            }
                        }
                        ItemTouchHelper.this.mRecyclerView.post((Runnable)this);
                        return;
                    }
                    return;
                }
            }
        });
    }

    void removeChildDrawingOrderCallbackIfNecessary(View view) {
        if (view == this.mOverdrawChild) {
            this.mOverdrawChild = null;
            if (this.mChildDrawingOrderCallback != null) {
                this.mRecyclerView.setChildDrawingOrderCallback(null);
                return;
            }
            return;
        }
    }

    boolean scrollIfNecessary() {
        int n;
        int n2;
        if (this.mSelected == null) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        long l = System.currentTimeMillis();
        long l2 = this.mDragScrollStartTimeInMs;
        l2 = l2 == Long.MIN_VALUE ? 0L : l - l2;
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (this.mTmpRect == null) {
            this.mTmpRect = new Rect();
        }
        int n3 = 0;
        int n4 = 0;
        layoutManager.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
        if (layoutManager.canScrollHorizontally()) {
            n = (int)(this.mSelectedStartX + this.mDx);
            n2 = n - this.mTmpRect.left - this.mRecyclerView.getPaddingLeft();
            if (this.mDx < 0.0f && n2 < 0) {
                n3 = n2;
            } else if (this.mDx > 0.0f && (n2 = this.mSelected.itemView.getWidth() + n + this.mTmpRect.right - (this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight())) > 0) {
                n3 = n2;
            }
        }
        if (layoutManager.canScrollVertically()) {
            n = (int)(this.mSelectedStartY + this.mDy);
            n2 = n - this.mTmpRect.top - this.mRecyclerView.getPaddingTop();
            if (this.mDy < 0.0f && n2 < 0) {
                n4 = n2;
            } else if (this.mDy > 0.0f && (n2 = this.mSelected.itemView.getHeight() + n + this.mTmpRect.bottom - (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom())) > 0) {
                n4 = n2;
            }
        }
        if (n3 != 0) {
            n3 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), n3, this.mRecyclerView.getWidth(), l2);
        }
        if (n4 != 0) {
            n4 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), n4, this.mRecyclerView.getHeight(), l2);
        }
        if (n3 == 0 && n4 == 0) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            this.mDragScrollStartTimeInMs = l;
        }
        this.mRecyclerView.scrollBy(n3, n4);
        return true;
    }

    void select(RecyclerView.ViewHolder viewHolder, int n) {
        if (viewHolder == this.mSelected && n == this.mActionState) {
            return;
        }
        this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
        int n2 = this.mActionState;
        this.endRecoverAnimation(viewHolder, true);
        this.mActionState = n;
        if (n == 2) {
            this.mOverdrawChild = viewHolder.itemView;
            this.addChildDrawingOrderCallback();
        }
        final int n3 = 0;
        int n4 = 0;
        if (this.mSelected != null) {
            Object object = this.mSelected;
            if (object.itemView.getParent() != null) {
                float f;
                float f2;
                float f3;
                n3 = n2 == 2 ? 0 : this.swipeIfNecessary((RecyclerView.ViewHolder)object);
                this.releaseVelocityTracker();
                if (n3 != 4 && n3 != 8 && n3 != 16 && n3 != 32) {
                    switch (n3) {
                        default: {
                            f = 0.0f;
                            f3 = 0.0f;
                            break;
                        }
                        case 1: 
                        case 2: {
                            f3 = Math.signum(this.mDy);
                            f2 = this.mRecyclerView.getHeight();
                            f = 0.0f;
                            f3 *= f2;
                            break;
                        }
                    }
                } else {
                    f = Math.signum(this.mDx);
                    f2 = this.mRecyclerView.getWidth();
                    f3 = 0.0f;
                    f *= f2;
                }
                n4 = n2 == 2 ? 8 : (n3 > 0 ? 2 : 4);
                this.getSelectedDxDy(this.mTmpPosition);
                float[] arrf = this.mTmpPosition;
                f2 = arrf[0];
                float f4 = arrf[1];
                object = new RecoverAnimation((RecyclerView.ViewHolder)object, n4, n2, f2, f4, f, f3, (RecyclerView.ViewHolder)object){
                    final /* synthetic */ RecyclerView.ViewHolder val$prevSelected;
                    {
                        this.val$prevSelected = viewHolder2;
                        super(viewHolder, n, n2, f, f2, f3, f4);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator2) {
                        super.onAnimationEnd(animator2);
                        if (this.mOverridden) {
                            return;
                        }
                        if (n3 <= 0) {
                            ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, this.val$prevSelected);
                        } else {
                            ItemTouchHelper.this.mPendingCleanup.add(this.val$prevSelected.itemView);
                            this.mIsPendingCleanup = true;
                            int n = n3;
                            if (n > 0) {
                                ItemTouchHelper.this.postDispatchSwipe(this, n);
                            }
                        }
                        if (ItemTouchHelper.this.mOverdrawChild == this.val$prevSelected.itemView) {
                            ItemTouchHelper.this.removeChildDrawingOrderCallbackIfNecessary(this.val$prevSelected.itemView);
                            return;
                        }
                    }
                };
                object.setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, n4, f - f2, f3 - f4));
                this.mRecoverAnimations.add((RecoverAnimation)object);
                object.start();
                n4 = 1;
            } else {
                this.removeChildDrawingOrderCallbackIfNecessary(object.itemView);
                this.mCallback.clearView(this.mRecyclerView, (RecyclerView.ViewHolder)object);
            }
            this.mSelected = null;
        } else {
            n4 = n3;
        }
        if (viewHolder != null) {
            this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolder) & (1 << n * 8 + 8) - 1) >> this.mActionState * 8;
            this.mSelectedStartX = viewHolder.itemView.getLeft();
            this.mSelectedStartY = viewHolder.itemView.getTop();
            this.mSelected = viewHolder;
            if (n == 2) {
                this.mSelected.itemView.performHapticFeedback(0);
            }
        }
        boolean bl = false;
        viewHolder = this.mRecyclerView.getParent();
        if (viewHolder != null) {
            if (this.mSelected != null) {
                bl = true;
            }
            viewHolder.requestDisallowInterceptTouchEvent(bl);
        }
        if (n4 == 0) {
            this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
        }
        this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
        this.mRecyclerView.invalidate();
    }

    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasDragFlag(this.mRecyclerView, viewHolder)) {
            Log.e((String)"ItemTouchHelper", (String)"Start drag has been called but dragging is not enabled");
            return;
        }
        if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e((String)"ItemTouchHelper", (String)"Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
            return;
        }
        this.obtainVelocityTracker();
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        this.select(viewHolder, 2);
    }

    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, viewHolder)) {
            Log.e((String)"ItemTouchHelper", (String)"Start swipe has been called but swiping is not enabled");
            return;
        }
        if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e((String)"ItemTouchHelper", (String)"Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
            return;
        }
        this.obtainVelocityTracker();
        this.mDy = 0.0f;
        this.mDx = 0.0f;
        this.select(viewHolder, 1);
    }

    void updateDxDy(MotionEvent motionEvent, int n, int n2) {
        float f = motionEvent.getX(n2);
        float f2 = motionEvent.getY(n2);
        this.mDx = f - this.mInitialTouchX;
        this.mDy = f2 - this.mInitialTouchY;
        if ((n & 4) == 0) {
            this.mDx = Math.max(0.0f, this.mDx);
        }
        if ((n & 8) == 0) {
            this.mDx = Math.min(0.0f, this.mDx);
        }
        if ((n & 1) == 0) {
            this.mDy = Math.max(0.0f, this.mDy);
        }
        if ((n & 2) == 0) {
            this.mDy = Math.min(0.0f, this.mDy);
            return;
        }
    }

    public static abstract class Callback {
        private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
        public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
        public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
        private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000L;
        static final int RELATIVE_DIR_FLAGS = 3158064;
        private static final Interpolator sDragScrollInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return f * f * f * f * f;
            }
        };
        private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return f * f * f * f * (f -= 1.0f) + 1.0f;
            }
        };
        private static final ItemTouchUIUtil sUICallback = Build.VERSION.SDK_INT >= 21 ? new ItemTouchUIUtilImpl.Api21Impl() : new ItemTouchUIUtilImpl.BaseImpl();
        private int mCachedMaxScrollSpeed = -1;

        public static int convertToRelativeDirection(int n, int n2) {
            int n3 = n & 789516;
            if (n3 == 0) {
                return n;
            }
            n &= ~ n3;
            if (n2 == 0) {
                return n | n3 << 2;
            }
            return n | n3 << 1 & -789517 | (789516 & n3 << 1) << 2;
        }

        public static ItemTouchUIUtil getDefaultUIUtil() {
            return sUICallback;
        }

        private int getMaxDragScroll(RecyclerView recyclerView) {
            if (this.mCachedMaxScrollSpeed == -1) {
                this.mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame);
            }
            return this.mCachedMaxScrollSpeed;
        }

        public static int makeFlag(int n, int n2) {
            return n2 << n * 8;
        }

        public static int makeMovementFlags(int n, int n2) {
            return Callback.makeFlag(0, n2 | n) | Callback.makeFlag(1, n2) | Callback.makeFlag(2, n);
        }

        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            return true;
        }

        public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder viewHolder, List<RecyclerView.ViewHolder> list, int n, int n2) {
            int n3 = viewHolder.itemView.getWidth();
            int n4 = viewHolder.itemView.getHeight();
            RecyclerView.ViewHolder viewHolder2 = null;
            int n5 = -1;
            int n6 = n - viewHolder.itemView.getLeft();
            int n7 = n2 - viewHolder.itemView.getTop();
            int n8 = list.size();
            for (int i = 0; i < n8; ++i) {
                int n9;
                RecyclerView.ViewHolder viewHolder3 = list.get(i);
                if (n6 > 0 && (n9 = viewHolder3.itemView.getRight() - (n + n3)) < 0 && viewHolder3.itemView.getRight() > viewHolder.itemView.getRight() && (n9 = Math.abs(n9)) > n5) {
                    n5 = n9;
                    viewHolder2 = viewHolder3;
                }
                if (n6 < 0 && (n9 = viewHolder3.itemView.getLeft() - n) > 0 && viewHolder3.itemView.getLeft() < viewHolder.itemView.getLeft() && (n9 = Math.abs(n9)) > n5) {
                    n5 = n9;
                    viewHolder2 = viewHolder3;
                }
                if (n7 < 0 && (n9 = viewHolder3.itemView.getTop() - n2) > 0 && viewHolder3.itemView.getTop() < viewHolder.itemView.getTop() && (n9 = Math.abs(n9)) > n5) {
                    n5 = n9;
                    viewHolder2 = viewHolder3;
                }
                if (n7 <= 0 || (n9 = viewHolder3.itemView.getBottom() - (n2 + n4)) >= 0 || viewHolder3.itemView.getBottom() <= viewHolder.itemView.getBottom() || (n9 = Math.abs(n9)) <= n5) continue;
                n5 = n9;
                viewHolder2 = viewHolder3;
            }
            return viewHolder2;
        }

        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            sUICallback.clearView(viewHolder.itemView);
        }

        public int convertToAbsoluteDirection(int n, int n2) {
            int n3 = n & 3158064;
            if (n3 == 0) {
                return n;
            }
            n &= ~ n3;
            if (n2 == 0) {
                return n | n3 >> 2;
            }
            return n | n3 >> 1 & -3158065 | (3158064 & n3 >> 1) >> 2;
        }

        final int getAbsoluteMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.convertToAbsoluteDirection(this.getMovementFlags(recyclerView, viewHolder), ViewCompat.getLayoutDirection((View)recyclerView));
        }

        public long getAnimationDuration(RecyclerView object, int n, float f, float f2) {
            if ((object = object.getItemAnimator()) == null) {
                if (n == 8) {
                    return 200L;
                }
                return 250L;
            }
            if (n == 8) {
                return object.getMoveDuration();
            }
            return object.getRemoveDuration();
        }

        public int getBoundingBoxMargin() {
            return 0;
        }

        public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public abstract int getMovementFlags(RecyclerView var1, RecyclerView.ViewHolder var2);

        public float getSwipeEscapeVelocity(float f) {
            return f;
        }

        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public float getSwipeVelocityThreshold(float f) {
            return f;
        }

        boolean hasDragFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if ((16711680 & this.getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0) {
                return true;
            }
            return false;
        }

        boolean hasSwipeFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if ((65280 & this.getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0) {
                return true;
            }
            return false;
        }

        public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int n, int n2, int n3, long l) {
            n3 = this.getMaxDragScroll(recyclerView);
            int n4 = Math.abs(n2);
            int n5 = (int)Math.signum(n2);
            float f = Math.min(1.0f, (float)n4 * 1.0f / (float)n);
            n = (int)((float)(n5 * n3) * sDragViewScrollCapInterpolator.getInterpolation(f));
            f = l > 2000L ? 1.0f : (float)l / 2000.0f;
            if ((n = (int)((float)n * sDragScrollInterpolator.getInterpolation(f))) == 0) {
                if (n2 > 0) {
                    return 1;
                }
                return -1;
            }
            return n;
        }

        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        public boolean isLongPressDragEnabled() {
            return true;
        }

        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int n, boolean bl) {
            sUICallback.onDraw(canvas, recyclerView, viewHolder.itemView, f, f2, n, bl);
        }

        public void onChildDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int n, boolean bl) {
            sUICallback.onDrawOver(canvas, recyclerView, viewHolder.itemView, f, f2, n, bl);
        }

        void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, List<RecoverAnimation> list, int n, float f, float f2) {
            int n2;
            int n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                RecoverAnimation recoverAnimation = list.get(n2);
                recoverAnimation.update();
                int n4 = canvas.save();
                this.onChildDraw(canvas, recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                canvas.restoreToCount(n4);
            }
            if (viewHolder != null) {
                n2 = canvas.save();
                this.onChildDraw(canvas, recyclerView, viewHolder, f, f2, n, true);
                canvas.restoreToCount(n2);
                return;
            }
        }

        void onDrawOver(Canvas object, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, List<RecoverAnimation> list, int n, float f, float f2) {
            int n2;
            int n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                RecoverAnimation recoverAnimation = list.get(n2);
                int n4 = object.save();
                this.onChildDrawOver((Canvas)object, recyclerView, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                object.restoreToCount(n4);
            }
            if (viewHolder != null) {
                n2 = object.save();
                this.onChildDrawOver((Canvas)object, recyclerView, viewHolder, f, f2, n, true);
                object.restoreToCount(n2);
            }
            n2 = 0;
            for (n = n3 - 1; n >= 0; --n) {
                object = list.get(n);
                if (object.mEnded && !object.mIsPendingCleanup) {
                    list.remove(n);
                    continue;
                }
                if (object.mEnded) continue;
                n2 = 1;
            }
            if (n2 != 0) {
                recyclerView.invalidate();
                return;
            }
        }

        public abstract boolean onMove(RecyclerView var1, RecyclerView.ViewHolder var2, RecyclerView.ViewHolder var3);

        public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int n, RecyclerView.ViewHolder viewHolder2, int n2, int n3, int n4) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof ViewDropHandler) {
                ((ViewDropHandler)((Object)layoutManager)).prepareForDrop(viewHolder.itemView, viewHolder2.itemView, n3, n4);
                return;
            }
            if (layoutManager.canScrollHorizontally()) {
                if (layoutManager.getDecoratedLeft(viewHolder2.itemView) <= recyclerView.getPaddingLeft()) {
                    recyclerView.scrollToPosition(n2);
                }
                if (layoutManager.getDecoratedRight(viewHolder2.itemView) >= recyclerView.getWidth() - recyclerView.getPaddingRight()) {
                    recyclerView.scrollToPosition(n2);
                }
            }
            if (layoutManager.canScrollVertically()) {
                if (layoutManager.getDecoratedTop(viewHolder2.itemView) <= recyclerView.getPaddingTop()) {
                    recyclerView.scrollToPosition(n2);
                }
                if (layoutManager.getDecoratedBottom(viewHolder2.itemView) >= recyclerView.getHeight() - recyclerView.getPaddingBottom()) {
                    recyclerView.scrollToPosition(n2);
                    return;
                }
                return;
            }
        }

        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int n) {
            if (viewHolder != null) {
                sUICallback.onSelected(viewHolder.itemView);
                return;
            }
        }

        public abstract void onSwiped(RecyclerView.ViewHolder var1, int var2);

    }

    private class ItemTouchHelperGestureListener
    extends GestureDetector.SimpleOnGestureListener {
        ItemTouchHelperGestureListener() {
        }

        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        public void onLongPress(MotionEvent object) {
            Object object2 = ItemTouchHelper.this.findChildView((MotionEvent)object);
            if (object2 != null) {
                if ((object2 = ItemTouchHelper.this.mRecyclerView.getChildViewHolder((View)object2)) != null) {
                    if (!ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, (RecyclerView.ViewHolder)object2)) {
                        return;
                    }
                    if (object.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
                        int n = object.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                        float f = object.getX(n);
                        float f2 = object.getY(n);
                        object = ItemTouchHelper.this;
                        object.mInitialTouchX = f;
                        object.mInitialTouchY = f2;
                        object.mDy = 0.0f;
                        object.mDx = 0.0f;
                        if (object.mCallback.isLongPressDragEnabled()) {
                            ItemTouchHelper.this.select((RecyclerView.ViewHolder)object2, 2);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
        }
    }

    private static class RecoverAnimation
    implements Animator.AnimatorListener {
        final int mActionState;
        final int mAnimationType;
        boolean mEnded = false;
        private float mFraction;
        public boolean mIsPendingCleanup;
        boolean mOverridden = false;
        final float mStartDx;
        final float mStartDy;
        final float mTargetX;
        final float mTargetY;
        private final ValueAnimator mValueAnimator;
        final RecyclerView.ViewHolder mViewHolder;
        float mX;
        float mY;

        RecoverAnimation(RecyclerView.ViewHolder viewHolder, int n, int n2, float f, float f2, float f3, float f4) {
            this.mActionState = n2;
            this.mAnimationType = n;
            this.mViewHolder = viewHolder;
            this.mStartDx = f;
            this.mStartDy = f2;
            this.mTargetX = f3;
            this.mTargetY = f4;
            this.mValueAnimator = ValueAnimator.ofFloat((float[])new float[]{0.0f, 1.0f});
            this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    RecoverAnimation.this.setFraction(valueAnimator.getAnimatedFraction());
                }
            });
            this.mValueAnimator.setTarget((Object)viewHolder.itemView);
            this.mValueAnimator.addListener((Animator.AnimatorListener)this);
            this.setFraction(0.0f);
        }

        public void cancel() {
            this.mValueAnimator.cancel();
        }

        public void onAnimationCancel(Animator animator2) {
            this.setFraction(1.0f);
        }

        public void onAnimationEnd(Animator animator2) {
            if (!this.mEnded) {
                this.mViewHolder.setIsRecyclable(true);
            }
            this.mEnded = true;
        }

        public void onAnimationRepeat(Animator animator2) {
        }

        public void onAnimationStart(Animator animator2) {
        }

        public void setDuration(long l) {
            this.mValueAnimator.setDuration(l);
        }

        public void setFraction(float f) {
            this.mFraction = f;
        }

        public void start() {
            this.mViewHolder.setIsRecyclable(false);
            this.mValueAnimator.start();
        }

        public void update() {
            float f = this.mStartDx;
            float f2 = this.mTargetX;
            this.mX = f == f2 ? this.mViewHolder.itemView.getTranslationX() : f + this.mFraction * (f2 - f);
            f = this.mStartDy;
            f2 = this.mTargetY;
            if (f == f2) {
                this.mY = this.mViewHolder.itemView.getTranslationY();
                return;
            }
            this.mY = f + this.mFraction * (f2 - f);
        }

    }

    public static abstract class SimpleCallback
    extends Callback {
        private int mDefaultDragDirs;
        private int mDefaultSwipeDirs;

        public SimpleCallback(int n, int n2) {
            this.mDefaultSwipeDirs = n2;
            this.mDefaultDragDirs = n;
        }

        public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultDragDirs;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return SimpleCallback.makeMovementFlags(this.getDragDirs(recyclerView, viewHolder), this.getSwipeDirs(recyclerView, viewHolder));
        }

        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultSwipeDirs;
        }

        public void setDefaultDragDirs(int n) {
            this.mDefaultDragDirs = n;
        }

        public void setDefaultSwipeDirs(int n) {
            this.mDefaultSwipeDirs = n;
        }
    }

    public static interface ViewDropHandler {
        public void prepareForDrop(View var1, View var2, int var3, int var4);
    }

}

