// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import android.support.v4.os.TraceCompat;
import android.support.annotation.Nullable;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

final class GapWorker implements Runnable
{
    static final ThreadLocal<GapWorker> sGapWorker;
    static Comparator<Task> sTaskComparator;
    long mFrameIntervalNs;
    long mPostTimeNs;
    ArrayList<RecyclerView> mRecyclerViews;
    private ArrayList<Task> mTasks;
    
    static {
        sGapWorker = new ThreadLocal<GapWorker>();
        GapWorker.sTaskComparator = new Comparator<Task>() {
            @Override
            public int compare(final Task task, final Task task2) {
                final RecyclerView view = task.view;
                final boolean b = true;
                if (view == null != (task2.view == null)) {
                    if (task.view == null) {
                        return 1;
                    }
                    return -1;
                }
                else {
                    if (task.immediate != task2.immediate) {
                        int n = b ? 1 : 0;
                        if (task.immediate) {
                            n = -1;
                        }
                        return n;
                    }
                    final int n2 = task2.viewVelocity - task.viewVelocity;
                    if (n2 != 0) {
                        return n2;
                    }
                    final int n3 = task.distanceToItem - task2.distanceToItem;
                    if (n3 != 0) {
                        return n3;
                    }
                    return 0;
                }
            }
        };
    }
    
    GapWorker() {
        this.mRecyclerViews = new ArrayList<RecyclerView>();
        this.mTasks = new ArrayList<Task>();
    }
    
    private void buildTaskList() {
        final int size = this.mRecyclerViews.size();
        int n = 0;
        for (int i = 0; i < size; ++i) {
            final RecyclerView recyclerView = this.mRecyclerViews.get(i);
            if (recyclerView.getWindowVisibility() == 0) {
                recyclerView.mPrefetchRegistry.collectPrefetchPositionsFromView(recyclerView, false);
                n += recyclerView.mPrefetchRegistry.mCount;
            }
        }
        this.mTasks.ensureCapacity(n);
        int n2 = 0;
        int n3;
        for (int j = 0; j < size; ++j, n2 = n3) {
            final RecyclerView view = this.mRecyclerViews.get(j);
            if (view.getWindowVisibility() != 0) {
                n3 = n2;
            }
            else {
                final LayoutPrefetchRegistryImpl mPrefetchRegistry = view.mPrefetchRegistry;
                final int viewVelocity = Math.abs(mPrefetchRegistry.mPrefetchDx) + Math.abs(mPrefetchRegistry.mPrefetchDy);
                int n4 = 0;
                while (true) {
                    n3 = n2;
                    if (n4 >= mPrefetchRegistry.mCount * 2) {
                        break;
                    }
                    Task task;
                    if (n2 >= this.mTasks.size()) {
                        task = new Task();
                        this.mTasks.add(task);
                    }
                    else {
                        task = this.mTasks.get(n2);
                    }
                    final int distanceToItem = mPrefetchRegistry.mPrefetchArray[n4 + 1];
                    task.immediate = (distanceToItem <= viewVelocity);
                    task.viewVelocity = viewVelocity;
                    task.distanceToItem = distanceToItem;
                    task.view = view;
                    task.position = mPrefetchRegistry.mPrefetchArray[n4];
                    ++n2;
                    n4 += 2;
                }
            }
        }
        Collections.sort(this.mTasks, GapWorker.sTaskComparator);
    }
    
    private void flushTaskWithDeadline(final Task task, final long n) {
        long n2;
        if (task.immediate) {
            n2 = Long.MAX_VALUE;
        }
        else {
            n2 = n;
        }
        final RecyclerView.ViewHolder prefetchPositionWithDeadline = this.prefetchPositionWithDeadline(task.view, task.position, n2);
        if (prefetchPositionWithDeadline == null || prefetchPositionWithDeadline.mNestedRecyclerView == null) {
            return;
        }
        if (!prefetchPositionWithDeadline.isBound()) {
            return;
        }
        if (!prefetchPositionWithDeadline.isInvalid()) {
            this.prefetchInnerRecyclerViewWithDeadline((RecyclerView)prefetchPositionWithDeadline.mNestedRecyclerView.get(), n);
        }
    }
    
    private void flushTasksWithDeadline(final long n) {
        for (int i = 0; i < this.mTasks.size(); ++i) {
            final Task task = this.mTasks.get(i);
            if (task.view == null) {
                return;
            }
            this.flushTaskWithDeadline(task, n);
            task.clear();
        }
    }
    
    static boolean isPrefetchPositionAttached(final RecyclerView recyclerView, final int n) {
        for (int unfilteredChildCount = recyclerView.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final RecyclerView.ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(recyclerView.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt.mPosition == n && !childViewHolderInt.isInvalid()) {
                return true;
            }
        }
        return false;
    }
    
    private void prefetchInnerRecyclerViewWithDeadline(@Nullable final RecyclerView recyclerView, final long n) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.mDataSetHasChangedAfterLayout) {
            if (recyclerView.mChildHelper.getUnfilteredChildCount() != 0) {
                recyclerView.removeAndRecycleViews();
            }
        }
        final LayoutPrefetchRegistryImpl mPrefetchRegistry = recyclerView.mPrefetchRegistry;
        mPrefetchRegistry.collectPrefetchPositionsFromView(recyclerView, true);
        if (mPrefetchRegistry.mCount != 0) {
            try {
                TraceCompat.beginSection("RV Nested Prefetch");
                recyclerView.mState.prepareForNestedPrefetch(recyclerView.mAdapter);
                for (int i = 0; i < mPrefetchRegistry.mCount * 2; i += 2) {
                    this.prefetchPositionWithDeadline(recyclerView, mPrefetchRegistry.mPrefetchArray[i], n);
                }
            }
            finally {
                TraceCompat.endSection();
            }
        }
    }
    
    private RecyclerView.ViewHolder prefetchPositionWithDeadline(final RecyclerView recyclerView, final int n, final long n2) {
        if (isPrefetchPositionAttached(recyclerView, n)) {
            return null;
        }
        final RecyclerView.Recycler mRecycler = recyclerView.mRecycler;
        try {
            recyclerView.onEnterLayoutOrScroll();
            final RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline = mRecycler.tryGetViewHolderForPositionByDeadline(n, false, n2);
            if (tryGetViewHolderForPositionByDeadline != null) {
                if (tryGetViewHolderForPositionByDeadline.isBound() && !tryGetViewHolderForPositionByDeadline.isInvalid()) {
                    mRecycler.recycleView(tryGetViewHolderForPositionByDeadline.itemView);
                }
                else {
                    mRecycler.addViewHolderToRecycledViewPool(tryGetViewHolderForPositionByDeadline, false);
                }
            }
            return tryGetViewHolderForPositionByDeadline;
        }
        finally {
            recyclerView.onExitLayoutOrScroll(false);
        }
    }
    
    public void add(final RecyclerView recyclerView) {
        this.mRecyclerViews.add(recyclerView);
    }
    
    void postFromTraversal(final RecyclerView recyclerView, final int n, final int n2) {
        if (recyclerView.isAttachedToWindow()) {
            if (this.mPostTimeNs == 0L) {
                this.mPostTimeNs = recyclerView.getNanoTime();
                recyclerView.post((Runnable)this);
            }
        }
        recyclerView.mPrefetchRegistry.setPrefetchVector(n, n2);
    }
    
    void prefetch(final long n) {
        this.buildTaskList();
        this.flushTasksWithDeadline(n);
    }
    
    public void remove(final RecyclerView recyclerView) {
        this.mRecyclerViews.remove(recyclerView);
    }
    
    @Override
    public void run() {
        try {
            TraceCompat.beginSection("RV Prefetch");
            if (this.mRecyclerViews.isEmpty()) {
                return;
            }
            final int size = this.mRecyclerViews.size();
            long max = 0L;
            for (int i = 0; i < size; ++i) {
                final RecyclerView recyclerView = this.mRecyclerViews.get(i);
                if (recyclerView.getWindowVisibility() == 0) {
                    max = Math.max(recyclerView.getDrawingTime(), max);
                }
            }
            if (max == 0L) {
                return;
            }
            this.prefetch(TimeUnit.MILLISECONDS.toNanos(max) + this.mFrameIntervalNs);
        }
        finally {
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
        }
    }
    
    static class LayoutPrefetchRegistryImpl implements LayoutPrefetchRegistry
    {
        int mCount;
        int[] mPrefetchArray;
        int mPrefetchDx;
        int mPrefetchDy;
        
        @Override
        public void addPosition(final int n, final int n2) {
            if (n < 0) {
                throw new IllegalArgumentException("Layout positions must be non-negative");
            }
            if (n2 >= 0) {
                final int n3 = this.mCount * 2;
                final int[] mPrefetchArray = this.mPrefetchArray;
                if (mPrefetchArray == null) {
                    Arrays.fill(this.mPrefetchArray = new int[4], -1);
                }
                else if (n3 >= mPrefetchArray.length) {
                    final int[] mPrefetchArray2 = this.mPrefetchArray;
                    System.arraycopy(mPrefetchArray2, 0, this.mPrefetchArray = new int[n3 * 2], 0, mPrefetchArray2.length);
                }
                final int[] mPrefetchArray3 = this.mPrefetchArray;
                mPrefetchArray3[n3] = n;
                mPrefetchArray3[n3 + 1] = n2;
                ++this.mCount;
                return;
            }
            throw new IllegalArgumentException("Pixel distance must be non-negative");
        }
        
        void clearPrefetchPositions() {
            final int[] mPrefetchArray = this.mPrefetchArray;
            if (mPrefetchArray != null) {
                Arrays.fill(mPrefetchArray, -1);
            }
            this.mCount = 0;
        }
        
        void collectPrefetchPositionsFromView(final RecyclerView recyclerView, final boolean mPrefetchMaxObservedInInitialPrefetch) {
            this.mCount = 0;
            final int[] mPrefetchArray = this.mPrefetchArray;
            if (mPrefetchArray != null) {
                Arrays.fill(mPrefetchArray, -1);
            }
            final RecyclerView.LayoutManager mLayout = recyclerView.mLayout;
            if (recyclerView.mAdapter == null || mLayout == null) {
                return;
            }
            if (!mLayout.isItemPrefetchEnabled()) {
                return;
            }
            if (mPrefetchMaxObservedInInitialPrefetch) {
                if (!recyclerView.mAdapterHelper.hasPendingUpdates()) {
                    mLayout.collectInitialPrefetchPositions(recyclerView.mAdapter.getItemCount(), (RecyclerView.LayoutManager.LayoutPrefetchRegistry)this);
                }
            }
            else if (!recyclerView.hasPendingAdapterUpdates()) {
                mLayout.collectAdjacentPrefetchPositions(this.mPrefetchDx, this.mPrefetchDy, recyclerView.mState, (RecyclerView.LayoutManager.LayoutPrefetchRegistry)this);
            }
            if (this.mCount > mLayout.mPrefetchMaxCountObserved) {
                mLayout.mPrefetchMaxCountObserved = this.mCount;
                mLayout.mPrefetchMaxObservedInInitialPrefetch = mPrefetchMaxObservedInInitialPrefetch;
                recyclerView.mRecycler.updateViewCacheSize();
            }
        }
        
        boolean lastPrefetchIncludedPosition(final int n) {
            if (this.mPrefetchArray != null) {
                for (int mCount = this.mCount, i = 0; i < mCount * 2; i += 2) {
                    if (this.mPrefetchArray[i] == n) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        void setPrefetchVector(final int mPrefetchDx, final int mPrefetchDy) {
            this.mPrefetchDx = mPrefetchDx;
            this.mPrefetchDy = mPrefetchDy;
        }
    }
    
    static class Task
    {
        public int distanceToItem;
        public boolean immediate;
        public int position;
        public RecyclerView view;
        public int viewVelocity;
        
        public void clear() {
            this.immediate = false;
            this.viewVelocity = 0;
            this.distanceToItem = 0;
            this.view = null;
            this.position = 0;
        }
    }
}
