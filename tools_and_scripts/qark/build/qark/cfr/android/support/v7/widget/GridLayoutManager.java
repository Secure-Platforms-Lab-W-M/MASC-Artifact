/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseIntArray
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;
import java.util.List;

public class GridLayoutManager
extends LinearLayoutManager {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets = new Rect();
    boolean mPendingSpanCountChange = false;
    final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
    final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
    View[] mSet;
    int mSpanCount = -1;
    SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();

    public GridLayoutManager(Context context, int n) {
        super(context);
        this.setSpanCount(n);
    }

    public GridLayoutManager(Context context, int n, int n2, boolean bl) {
        super(context, n2, bl);
        this.setSpanCount(n);
    }

    public GridLayoutManager(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.setSpanCount(GridLayoutManager.getProperties((Context)context, (AttributeSet)attributeSet, (int)n, (int)n2).spanCount);
    }

    private void assignSpans(RecyclerView.Recycler recycler, RecyclerView.State state, int n, int n2, boolean bl) {
        int n3;
        int n4;
        if (bl) {
            n4 = 0;
            n2 = n;
            n3 = 1;
            n = n4;
        } else {
            --n;
            n2 = -1;
            n3 = -1;
        }
        n4 = 0;
        while (n != n2) {
            View view = this.mSet[n];
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.mSpanSize = this.getSpanSize(recycler, state, this.getPosition(view));
            layoutParams.mSpanIndex = n4;
            n4 += layoutParams.mSpanSize;
            n += n3;
        }
    }

    private void cachePreLayoutSpanMapping() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            int n2 = layoutParams.getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(n2, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(n2, layoutParams.getSpanIndex());
        }
    }

    private void calculateItemBorders(int n) {
        this.mCachedBorders = GridLayoutManager.calculateItemBorders(this.mCachedBorders, this.mSpanCount, n);
    }

    static int[] calculateItemBorders(int[] arrn, int n, int n2) {
        if (arrn == null || arrn.length != n + 1 || arrn[arrn.length - 1] != n2) {
            arrn = new int[n + 1];
        }
        arrn[0] = 0;
        int n3 = n2 / n;
        int n4 = n2 % n;
        int n5 = 0;
        n2 = 0;
        for (int i = 1; i <= n; ++i) {
            int n6 = n3;
            if ((n2 += n4) > 0 && n - n2 < n4) {
                ++n6;
                n2 -= n;
            }
            arrn[i] = n5 += n6;
        }
        return arrn;
    }

    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }

    private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int n) {
        int n2 = n == 1 ? 1 : 0;
        n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (n2 != 0) {
            while (n > 0 && anchorInfo.mPosition > 0) {
                --anchorInfo.mPosition;
                n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
            }
            return;
        }
        int n3 = state.getItemCount();
        int n4 = anchorInfo.mPosition;
        n2 = n;
        n = n4;
        while (n < n3 - 1 && (n4 = this.getSpanIndex(recycler, state, n + 1)) > n2) {
            ++n;
            n2 = n4;
        }
        anchorInfo.mPosition = n;
    }

    private void ensureViewSet() {
        View[] arrview = this.mSet;
        if (arrview != null && arrview.length == this.mSpanCount) {
            return;
        }
        this.mSet = new View[this.mSpanCount];
    }

    private int getSpanGroupIndex(RecyclerView.Recycler object, RecyclerView.State state, int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanGroupIndex(n, this.mSpanCount);
        }
        int n2 = object.convertPreLayoutPositionToPostLayout(n);
        if (n2 == -1) {
            object = new StringBuilder();
            object.append("Cannot find span size for pre layout position. ");
            object.append(n);
            Log.w((String)"GridLayoutManager", (String)object.toString());
            return 0;
        }
        return this.mSpanSizeLookup.getSpanGroupIndex(n2, this.mSpanCount);
    }

    private int getSpanIndex(RecyclerView.Recycler object, RecyclerView.State state, int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(n, this.mSpanCount);
        }
        int n2 = this.mPreLayoutSpanIndexCache.get(n, -1);
        if (n2 != -1) {
            return n2;
        }
        n2 = object.convertPreLayoutPositionToPostLayout(n);
        if (n2 == -1) {
            object = new StringBuilder();
            object.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            object.append(n);
            Log.w((String)"GridLayoutManager", (String)object.toString());
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanIndex(n2, this.mSpanCount);
    }

    private int getSpanSize(RecyclerView.Recycler object, RecyclerView.State state, int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(n);
        }
        int n2 = this.mPreLayoutSpanSizeCache.get(n, -1);
        if (n2 != -1) {
            return n2;
        }
        n2 = object.convertPreLayoutPositionToPostLayout(n);
        if (n2 == -1) {
            object = new StringBuilder();
            object.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            object.append(n);
            Log.w((String)"GridLayoutManager", (String)object.toString());
            return 1;
        }
        return this.mSpanSizeLookup.getSpanSize(n2);
    }

    private void guessMeasurement(float f, int n) {
        this.calculateItemBorders(Math.max(Math.round((float)this.mSpanCount * f), n));
    }

    private void measureChild(View view, int n, boolean bl) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int n2 = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        int n3 = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
        int n4 = this.getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
            n = GridLayoutManager.getChildMeasureSpec(n4, n, n3, layoutParams.width, false);
            n2 = GridLayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.getHeightMode(), n2, layoutParams.height, true);
        } else {
            n2 = GridLayoutManager.getChildMeasureSpec(n4, n, n2, layoutParams.height, false);
            n = GridLayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.getWidthMode(), n3, layoutParams.width, true);
        }
        this.measureChildWithDecorationsAndMargin(view, n, n2, bl);
    }

    private void measureChildWithDecorationsAndMargin(View view, int n, int n2, boolean bl) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
        bl = bl ? this.shouldReMeasureChild(view, n, n2, layoutParams) : this.shouldMeasureChild(view, n, n2, layoutParams);
        if (bl) {
            view.measure(n, n2);
            return;
        }
    }

    private void updateMeasurements() {
        int n = this.getOrientation() == 1 ? this.getWidth() - this.getPaddingRight() - this.getPaddingLeft() : this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
        this.calculateItemBorders(n);
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override
    void collectPrefetchPositionsForLayoutState(RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int n = this.mSpanCount;
        for (int i = 0; i < this.mSpanCount && layoutState.hasMore(state) && n > 0; ++i) {
            int n2 = layoutState.mCurrentPosition;
            layoutPrefetchRegistry.addPosition(n2, Math.max(0, layoutState.mScrollingOffset));
            n -= this.mSpanSizeLookup.getSpanSize(n2);
            layoutState.mCurrentPosition += layoutState.mItemDirection;
        }
    }

    @Override
    View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state, int n, int n2, int n3) {
        this.ensureLayoutState();
        View view = null;
        View view2 = null;
        int n4 = this.mOrientationHelper.getStartAfterPadding();
        int n5 = this.mOrientationHelper.getEndAfterPadding();
        int n6 = n2 > n ? 1 : -1;
        while (n != n2) {
            View view3 = this.getChildAt(n);
            int n7 = this.getPosition(view3);
            if (n7 >= 0 && n7 < n3 && this.getSpanIndex(recycler, state, n7) == 0) {
                if (((RecyclerView.LayoutParams)view3.getLayoutParams()).isItemRemoved()) {
                    if (view == null) {
                        view = view3;
                    }
                } else {
                    if (this.mOrientationHelper.getDecoratedStart(view3) < n5 && this.mOrientationHelper.getDecoratedEnd(view3) >= n4) {
                        return view3;
                    }
                    if (view2 == null) {
                        view2 = view3;
                    }
                }
            }
            n += n6;
        }
        if (view2 != null) {
            return view2;
        }
        return view;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    @Override
    public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    @Override
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    int getSpaceForSpanRange(int n, int n2) {
        if (this.mOrientation == 1 && this.isLayoutRTL()) {
            int[] arrn = this.mCachedBorders;
            int n3 = this.mSpanCount;
            return arrn[n3 - n] - arrn[n3 - n - n2];
        }
        int[] arrn = this.mCachedBorders;
        return arrn[n + n2] - arrn[n];
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }

    @Override
    void layoutChunk(RecyclerView.Recycler object, RecyclerView.State object2, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult layoutChunkResult) {
        int n;
        int n2;
        int n3;
        View view;
        int n4;
        int n5 = this.mOrientationHelper.getModeInOther();
        int n6 = n5 != 1073741824 ? 1 : 0;
        int n7 = this.getChildCount() > 0 ? this.mCachedBorders[this.mSpanCount] : 0;
        if (n6 != 0) {
            this.updateMeasurements();
        }
        boolean bl = layoutState.mItemDirection == 1;
        int n8 = this.mSpanCount;
        if (!bl) {
            n8 = this.getSpanIndex((RecyclerView.Recycler)object, (RecyclerView.State)object2, layoutState.mCurrentPosition) + this.getSpanSize((RecyclerView.Recycler)object, (RecyclerView.State)object2, layoutState.mCurrentPosition);
            n3 = 0;
            n2 = 0;
        } else {
            n3 = 0;
            n2 = 0;
        }
        while (n3 < this.mSpanCount && layoutState.hasMore((RecyclerView.State)object2) && n8 > 0) {
            n4 = layoutState.mCurrentPosition;
            n = this.getSpanSize((RecyclerView.Recycler)object, (RecyclerView.State)object2, n4);
            if (n <= this.mSpanCount) {
                if ((n8 -= n) < 0 || (view = layoutState.next((RecyclerView.Recycler)object)) == null) break;
                n2 += n;
                this.mSet[n3] = view;
                ++n3;
                continue;
            }
            object = new StringBuilder();
            object.append("Item at position ");
            object.append(n4);
            object.append(" requires ");
            object.append(n);
            object.append(" spans but GridLayoutManager has only ");
            object.append(this.mSpanCount);
            object.append(" spans.");
            throw new IllegalArgumentException(object.toString());
        }
        n4 = n8;
        if (n3 == 0) {
            layoutChunkResult.mFinished = true;
            return;
        }
        this.assignSpans((RecyclerView.Recycler)object, (RecyclerView.State)object2, n3, n2, bl);
        n8 = 0;
        float f = 0.0f;
        for (n2 = 0; n2 < n3; ++n2) {
            object = this.mSet[n2];
            if (layoutState.mScrapList == null) {
                if (bl) {
                    this.addView((View)object);
                } else {
                    this.addView((View)object, 0);
                }
            } else if (bl) {
                this.addDisappearingView((View)object);
            } else {
                this.addDisappearingView((View)object, 0);
            }
            this.calculateItemDecorationsForChild((View)object, this.mDecorInsets);
            this.measureChild((View)object, n5, false);
            n = this.mOrientationHelper.getDecoratedMeasurement((View)object);
            if (n > n8) {
                n8 = n;
            }
            object2 = (LayoutParams)object.getLayoutParams();
            float f2 = (float)this.mOrientationHelper.getDecoratedMeasurementInOther((View)object) * 1.0f / (float)object2.mSpanSize;
            if (f2 <= f) continue;
            f = f2;
        }
        if (n6 != 0) {
            this.guessMeasurement(f, n7);
            n8 = 0;
            for (n2 = 0; n2 < n3; ++n2) {
                object = this.mSet[n2];
                this.measureChild((View)object, 1073741824, true);
                n6 = this.mOrientationHelper.getDecoratedMeasurement((View)object);
                if (n6 <= n8) continue;
                n8 = n6;
            }
            n = n8;
        } else {
            n = n8;
        }
        n8 = n5;
        for (n2 = 0; n2 < n3; ++n2) {
            object = this.mSet[n2];
            if (this.mOrientationHelper.getDecoratedMeasurement((View)object) == n) continue;
            object2 = (LayoutParams)object.getLayoutParams();
            view = object2.mDecorInsets;
            n7 = view.top + view.bottom + object2.topMargin + object2.bottomMargin;
            n6 = view.left + view.right + object2.leftMargin + object2.rightMargin;
            n5 = this.getSpaceForSpanRange(object2.mSpanIndex, object2.mSpanSize);
            if (this.mOrientation == 1) {
                n6 = GridLayoutManager.getChildMeasureSpec(n5, 1073741824, n6, object2.width, false);
                n7 = View.MeasureSpec.makeMeasureSpec((int)(n - n7), (int)1073741824);
            } else {
                n6 = View.MeasureSpec.makeMeasureSpec((int)(n - n6), (int)1073741824);
                n7 = GridLayoutManager.getChildMeasureSpec(n5, 1073741824, n7, object2.height, false);
            }
            this.measureChildWithDecorationsAndMargin((View)object, n6, n7, true);
        }
        layoutChunkResult.mConsumed = n;
        n8 = 0;
        n6 = 0;
        n7 = 0;
        n2 = 0;
        if (this.mOrientation == 1) {
            if (layoutState.mLayoutDirection == -1) {
                n2 = layoutState.mOffset;
                n7 = n2 - n;
            } else {
                n7 = layoutState.mOffset;
                n2 = n7 + n;
            }
        } else if (layoutState.mLayoutDirection == -1) {
            n6 = layoutState.mOffset;
            n8 = n6 - n;
        } else {
            n8 = layoutState.mOffset;
            n6 = n8 + n;
        }
        n4 = 0;
        while (n4 < n3) {
            object = this.mSet[n4];
            object2 = (LayoutParams)object.getLayoutParams();
            if (this.mOrientation == 1) {
                if (this.isLayoutRTL()) {
                    n6 = this.getPaddingLeft() + this.mCachedBorders[this.mSpanCount - object2.mSpanIndex];
                    n5 = n6 - this.mOrientationHelper.getDecoratedMeasurementInOther((View)object);
                    n8 = n7;
                    n7 = n2;
                    n2 = n5;
                } else {
                    n8 = this.getPaddingLeft() + this.mCachedBorders[object2.mSpanIndex];
                    n5 = this.mOrientationHelper.getDecoratedMeasurementInOther((View)object);
                    n6 = n8;
                    n8 = n7;
                    n7 = n2;
                    n2 = n6;
                    n6 = n5 += n8;
                }
            } else {
                n2 = this.getPaddingTop() + this.mCachedBorders[object2.mSpanIndex];
                n7 = this.mOrientationHelper.getDecoratedMeasurementInOther((View)object);
                n5 = n2;
                n7 += n2;
                n2 = n8;
                n8 = n5;
            }
            this.layoutDecoratedWithMargins((View)object, n2, n8, n6, n7);
            if (object2.isItemRemoved() || object2.isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            layoutChunkResult.mFocusable |= object.hasFocusable();
            n5 = n4 + 1;
            n4 = n8;
            n8 = n2;
            n2 = n7;
            n7 = n4;
            n4 = n5;
        }
        Arrays.fill((Object[])this.mSet, null);
    }

    @Override
    void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int n) {
        super.onAnchorReady(recycler, state, anchorInfo, n);
        this.updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            this.ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, n);
        }
        this.ensureViewSet();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public View onFocusSearchFailed(View var1_1, int var2_2, RecyclerView.Recycler var3_3, RecyclerView.State var4_4) {
        var24_5 = this.findContainingItemView(var1_1);
        if (var24_5 == null) {
            return null;
        }
        var22_6 = (LayoutParams)var24_5.getLayoutParams();
        var16_7 = var22_6.mSpanIndex;
        var17_8 = var22_6.mSpanIndex + var22_6.mSpanSize;
        if (super.onFocusSearchFailed(var1_1, var2_2, var3_3, var4_4) == null) {
            return null;
        }
        var21_9 = this.convertFocusDirectionToLayoutDirection(var2_2) == 1;
        var2_2 = var21_9 != this.mShouldReverseLayout ? 1 : 0;
        if (var2_2 != 0) {
            var2_2 = this.getChildCount() - 1;
            var7_10 = -1;
            var8_11 = -1;
        } else {
            var2_2 = 0;
            var7_10 = 1;
            var8_11 = this.getChildCount();
        }
        var9_12 = this.mOrientation == 1 && this.isLayoutRTL() != false ? 1 : 0;
        var23_13 = null;
        var22_6 = null;
        var11_14 = this.getSpanGroupIndex(var3_3, var4_4, var2_2);
        var5_16 = -1;
        var6_17 = 0;
        var14_18 = -1;
        var13_19 = 0;
        var10_20 = var2_2;
        var1_1 = var24_5;
        for (var12_15 = var2_2; var12_15 != var8_11; var12_15 += var7_10) {
            block18 : {
                block21 : {
                    block22 : {
                        block23 : {
                            block19 : {
                                block20 : {
                                    block17 : {
                                        block16 : {
                                            var2_2 = this.getSpanGroupIndex(var3_3, var4_4, var12_15);
                                            var24_5 = this.getChildAt(var12_15);
                                            if (var24_5 == var1_1) break;
                                            if (!var24_5.hasFocusable() || var2_2 == var11_14) break block16;
                                            if (var23_13 != null) {
                                                break;
                                            }
                                            ** GOTO lbl-1000
                                        }
                                        var25_25 = (LayoutParams)var24_5.getLayoutParams();
                                        var18_22 = var25_25.mSpanIndex;
                                        var19_23 = var25_25.mSpanIndex + var25_25.mSpanSize;
                                        if (var24_5.hasFocusable() && var18_22 == var16_7 && var19_23 == var17_8) {
                                            return var24_5;
                                        }
                                        if ((!var24_5.hasFocusable() || var23_13 != null) && (var24_5.hasFocusable() || var22_6 != null)) break block17;
                                        var2_2 = 1;
                                        break block18;
                                    }
                                    var2_2 = Math.max(var18_22, var16_7);
                                    var20_24 = Math.min(var19_23, var17_8);
                                    var15_21 = 0;
                                    var20_24 -= var2_2;
                                    if (!var24_5.hasFocusable()) break block19;
                                    if (var20_24 <= var6_17) break block20;
                                    var2_2 = 1;
                                    break block18;
                                }
                                if (var20_24 != var6_17) ** GOTO lbl-1000
                                var2_2 = var18_22 > var5_16 ? 1 : 0;
                                if (var9_12 == var2_2) {
                                    var2_2 = 1;
                                } else lbl-1000: // 2 sources:
                                {
                                    var2_2 = var15_21;
                                }
                                break block18;
                            }
                            if (var23_13 != null) break block21;
                            var2_2 = 1;
                            if (!this.isViewPartiallyVisible(var24_5, false, true)) break block22;
                            if (var20_24 <= var13_19) break block23;
                            var2_2 = 1;
                            break block18;
                        }
                        if (var20_24 != var13_19) ** GOTO lbl-1000
                        if (var18_22 <= var14_18) {
                            var2_2 = 0;
                        }
                        if (var9_12 == var2_2) {
                            var2_2 = 1;
                        } else lbl-1000: // 2 sources:
                        {
                            var2_2 = var15_21;
                        }
                        break block18;
                    }
                    var2_2 = var15_21;
                    break block18;
                }
                var2_2 = var15_21;
            }
            var15_21 = var6_17;
            if (var2_2 != 0) {
                if (var24_5.hasFocusable()) {
                    var5_16 = var25_25.mSpanIndex;
                    var2_2 = Math.min(var19_23, var17_8) - Math.max(var18_22, var16_7);
                    var23_13 = var24_5;
                } else {
                    var14_18 = var25_25.mSpanIndex;
                    var2_2 = Math.min(var19_23, var17_8);
                    var6_17 = Math.max(var18_22, var16_7);
                    var22_6 = var24_5;
                    var13_19 = var2_2 - var6_17;
                    var2_2 = var15_21;
                }
            } else lbl-1000: // 2 sources:
            {
                var2_2 = var6_17;
            }
            var6_17 = var2_2;
        }
        if (var23_13 == null) return var22_6;
        return var23_13;
    }

    @Override
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewGroup.LayoutParams layoutParams = object.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem((View)object, accessibilityNodeInfoCompat);
            return;
        }
        object = (LayoutParams)layoutParams;
        int n = this.getSpanGroupIndex(recycler, state, object.getViewLayoutPosition());
        if (this.mOrientation == 0) {
            int n2 = object.getSpanIndex();
            int n3 = object.getSpanSize();
            boolean bl = this.mSpanCount > 1 && object.getSpanSize() == this.mSpanCount;
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n2, n3, n, 1, bl, false));
            return;
        }
        int n4 = object.getSpanIndex();
        int n5 = object.getSpanSize();
        boolean bl = this.mSpanCount > 1 && object.getSpanSize() == this.mSpanCount;
        accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(n, 1, n4, n5, bl, false));
    }

    @Override
    public void onItemsAdded(RecyclerView recyclerView, int n, int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsChanged(RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsMoved(RecyclerView recyclerView, int n, int n2, int n3) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsRemoved(RecyclerView recyclerView, int n, int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onItemsUpdated(RecyclerView recyclerView, int n, int n2, Object object) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            this.cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        this.clearPreLayoutSpanMappingCache();
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }

    @Override
    public int scrollHorizontallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollHorizontallyBy(n, recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int n, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollVerticallyBy(n, recycler, state);
    }

    @Override
    public void setMeasuredDimension(Rect arrn, int n, int n2) {
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension((Rect)arrn, n, n2);
        }
        int n3 = this.getPaddingLeft() + this.getPaddingRight();
        int n4 = this.getPaddingTop() + this.getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = GridLayoutManager.chooseSize(n2, arrn.height() + n4, this.getMinimumHeight());
            arrn = this.mCachedBorders;
            n = GridLayoutManager.chooseSize(n, arrn[arrn.length - 1] + n3, this.getMinimumWidth());
        } else {
            n = GridLayoutManager.chooseSize(n, arrn.width() + n3, this.getMinimumWidth());
            arrn = this.mCachedBorders;
            n2 = GridLayoutManager.chooseSize(n2, arrn[arrn.length - 1] + n4, this.getMinimumHeight());
        }
        this.setMeasuredDimension(n, n2);
    }

    public void setSpanCount(int n) {
        if (n == this.mSpanCount) {
            return;
        }
        this.mPendingSpanCountChange = true;
        if (n >= 1) {
            this.mSpanCount = n;
            this.mSpanSizeLookup.invalidateSpanIndexCache();
            this.requestLayout();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Span count should be at least 1. Provided ");
        stringBuilder.append(n);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public void setStackFromEnd(boolean bl) {
        if (!bl) {
            super.setStackFromEnd(false);
            return;
        }
        throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        if (this.mPendingSavedState == null && !this.mPendingSpanCountChange) {
            return true;
        }
        return false;
    }

    public static final class DefaultSpanSizeLookup
    extends SpanSizeLookup {
        @Override
        public int getSpanIndex(int n, int n2) {
            return n % n2;
        }

        @Override
        public int getSpanSize(int n) {
            return 1;
        }
    }

    public static class LayoutParams
    extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        int mSpanIndex = -1;
        int mSpanSize = 0;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public int getSpanIndex() {
            return this.mSpanIndex;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }
    }

    public static abstract class SpanSizeLookup {
        private boolean mCacheSpanIndices = false;
        final SparseIntArray mSpanIndexCache = new SparseIntArray();

        int findReferenceIndexFromCache(int n) {
            int n2 = 0;
            int n3 = this.mSpanIndexCache.size() - 1;
            while (n2 <= n3) {
                int n4 = n2 + n3 >>> 1;
                if (this.mSpanIndexCache.keyAt(n4) < n) {
                    n2 = n4 + 1;
                    continue;
                }
                n3 = n4 - 1;
            }
            n = n2 - 1;
            if (n >= 0 && n < this.mSpanIndexCache.size()) {
                return this.mSpanIndexCache.keyAt(n);
            }
            return -1;
        }

        int getCachedSpanIndex(int n, int n2) {
            if (!this.mCacheSpanIndices) {
                return this.getSpanIndex(n, n2);
            }
            int n3 = this.mSpanIndexCache.get(n, -1);
            if (n3 != -1) {
                return n3;
            }
            n2 = this.getSpanIndex(n, n2);
            this.mSpanIndexCache.put(n, n2);
            return n2;
        }

        public int getSpanGroupIndex(int n, int n2) {
            int n3 = 0;
            int n4 = 0;
            int n5 = this.getSpanSize(n);
            for (int i = 0; i < n; ++i) {
                int n6 = this.getSpanSize(i);
                if ((n3 += n6) == n2) {
                    n3 = 0;
                    ++n4;
                    continue;
                }
                if (n3 <= n2) continue;
                n3 = n6;
                ++n4;
            }
            if (n3 + n5 > n2) {
                return n4 + 1;
            }
            return n4;
        }

        public int getSpanIndex(int n, int n2) {
            int n3;
            int n4 = this.getSpanSize(n);
            if (n4 == n2) {
                return 0;
            }
            int n5 = 0;
            int n6 = 0;
            if (this.mCacheSpanIndices && this.mSpanIndexCache.size() > 0 && (n3 = this.findReferenceIndexFromCache(n)) >= 0) {
                n5 = this.mSpanIndexCache.get(n3) + this.getSpanSize(n3);
                n6 = n3 + 1;
            }
            while (n6 < n) {
                n3 = this.getSpanSize(n6);
                if ((n5 += n3) == n2) {
                    n5 = 0;
                } else if (n5 > n2) {
                    n5 = n3;
                }
                ++n6;
            }
            if (n5 + n4 <= n2) {
                return n5;
            }
            return 0;
        }

        public abstract int getSpanSize(int var1);

        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }

        public void setSpanIndexCacheEnabled(boolean bl) {
            this.mCacheSpanIndices = bl;
        }
    }

}

