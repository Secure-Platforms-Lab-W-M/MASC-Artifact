/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package androidx.recyclerview.widget;

import android.view.View;
import androidx.core.os.TraceCompat;
import androidx.recyclerview.widget.AdapterHelper;
import androidx.recyclerview.widget.ChildHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

final class GapWorker
implements Runnable {
    static final ThreadLocal<GapWorker> sGapWorker = new ThreadLocal();
    static Comparator<Task> sTaskComparator = new Comparator<Task>(){

        @Override
        public int compare(Task task, Task task2) {
            RecyclerView recyclerView = task.view;
            int n = 1;
            int n2 = recyclerView == null ? 1 : 0;
            int n3 = task2.view == null;
            if (n2 != n3) {
                if (task.view == null) {
                    return 1;
                }
                return -1;
            }
            if (task.immediate != task2.immediate) {
                n2 = n;
                if (task.immediate) {
                    n2 = -1;
                }
                return n2;
            }
            n2 = task2.viewVelocity - task.viewVelocity;
            if (n2 != 0) {
                return n2;
            }
            n2 = task.distanceToItem - task2.distanceToItem;
            if (n2 != 0) {
                return n2;
            }
            return 0;
        }
    };
    long mFrameIntervalNs;
    long mPostTimeNs;
    ArrayList<RecyclerView> mRecyclerViews = new ArrayList();
    private ArrayList<Task> mTasks = new ArrayList();

    GapWorker() {
    }

    private void buildTaskList() {
        int n;
        Object object;
        int n2;
        int n3 = this.mRecyclerViews.size();
        int n4 = 0;
        for (n = 0; n < n3; ++n) {
            object = this.mRecyclerViews.get(n);
            n2 = n4;
            if (object.getWindowVisibility() == 0) {
                object.mPrefetchRegistry.collectPrefetchPositionsFromView((RecyclerView)object, false);
                n2 = n4 + object.mPrefetchRegistry.mCount;
            }
            n4 = n2;
        }
        this.mTasks.ensureCapacity(n4);
        n = 0;
        for (n4 = 0; n4 < n3; ++n4) {
            int n5;
            RecyclerView recyclerView = this.mRecyclerViews.get(n4);
            if (recyclerView.getWindowVisibility() != 0) {
                n5 = n;
            } else {
                LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = recyclerView.mPrefetchRegistry;
                int n6 = Math.abs(layoutPrefetchRegistryImpl.mPrefetchDx) + Math.abs(layoutPrefetchRegistryImpl.mPrefetchDy);
                n2 = 0;
                do {
                    n5 = n;
                    if (n2 >= layoutPrefetchRegistryImpl.mCount * 2) break;
                    if (n >= this.mTasks.size()) {
                        object = new Task();
                        this.mTasks.add((Task)object);
                    } else {
                        object = this.mTasks.get(n);
                    }
                    n5 = layoutPrefetchRegistryImpl.mPrefetchArray[n2 + 1];
                    boolean bl = n5 <= n6;
                    object.immediate = bl;
                    object.viewVelocity = n6;
                    object.distanceToItem = n5;
                    object.view = recyclerView;
                    object.position = layoutPrefetchRegistryImpl.mPrefetchArray[n2];
                    ++n;
                    n2 += 2;
                } while (true);
            }
            n = n5;
        }
        Collections.sort(this.mTasks, sTaskComparator);
    }

    private void flushTaskWithDeadline(Task object, long l) {
        long l2 = object.immediate ? Long.MAX_VALUE : l;
        object = this.prefetchPositionWithDeadline(object.view, object.position, l2);
        if (object != null && object.mNestedRecyclerView != null && object.isBound() && !object.isInvalid()) {
            this.prefetchInnerRecyclerViewWithDeadline(object.mNestedRecyclerView.get(), l);
        }
    }

    private void flushTasksWithDeadline(long l) {
        for (int i = 0; i < this.mTasks.size(); ++i) {
            Task task = this.mTasks.get(i);
            if (task.view == null) {
                return;
            }
            this.flushTaskWithDeadline(task, l);
            task.clear();
        }
    }

    static boolean isPrefetchPositionAttached(RecyclerView recyclerView, int n) {
        int n2 = recyclerView.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < n2; ++i) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(recyclerView.mChildHelper.getUnfilteredChildAt(i));
            if (viewHolder.mPosition != n || viewHolder.isInvalid()) continue;
            return true;
        }
        return false;
    }

    private void prefetchInnerRecyclerViewWithDeadline(RecyclerView recyclerView, long l) {
        if (recyclerView == null) {
            return;
        }
        if (recyclerView.mDataSetHasChangedAfterLayout && recyclerView.mChildHelper.getUnfilteredChildCount() != 0) {
            recyclerView.removeAndRecycleViews();
        }
        LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl = recyclerView.mPrefetchRegistry;
        layoutPrefetchRegistryImpl.collectPrefetchPositionsFromView(recyclerView, true);
        if (layoutPrefetchRegistryImpl.mCount != 0) {
            int n;
            try {
                TraceCompat.beginSection("RV Nested Prefetch");
                recyclerView.mState.prepareForNestedPrefetch(recyclerView.mAdapter);
                n = 0;
            }
            catch (Throwable throwable) {
                TraceCompat.endSection();
                throw throwable;
            }
            do {
                if (n >= layoutPrefetchRegistryImpl.mCount * 2) break;
                this.prefetchPositionWithDeadline(recyclerView, layoutPrefetchRegistryImpl.mPrefetchArray[n], l);
                n += 2;
            } while (true);
            TraceCompat.endSection();
            return;
        }
    }

    private RecyclerView.ViewHolder prefetchPositionWithDeadline(RecyclerView recyclerView, int n, long l) {
        RecyclerView.ViewHolder viewHolder;
        block6 : {
            if (GapWorker.isPrefetchPositionAttached(recyclerView, n)) {
                return null;
            }
            RecyclerView.Recycler recycler = recyclerView.mRecycler;
            recyclerView.onEnterLayoutOrScroll();
            viewHolder = recycler.tryGetViewHolderForPositionByDeadline(n, false, l);
            if (viewHolder == null) break block6;
            if (viewHolder.isBound() && !viewHolder.isInvalid()) {
                recycler.recycleView(viewHolder.itemView);
                break block6;
            }
            recycler.addViewHolderToRecycledViewPool(viewHolder, false);
        }
        return viewHolder;
        finally {
            recyclerView.onExitLayoutOrScroll(false);
        }
    }

    public void add(RecyclerView recyclerView) {
        this.mRecyclerViews.add(recyclerView);
    }

    void postFromTraversal(RecyclerView recyclerView, int n, int n2) {
        if (recyclerView.isAttachedToWindow() && this.mPostTimeNs == 0L) {
            this.mPostTimeNs = recyclerView.getNanoTime();
            recyclerView.post((Runnable)this);
        }
        recyclerView.mPrefetchRegistry.setPrefetchVector(n, n2);
    }

    void prefetch(long l) {
        this.buildTaskList();
        this.flushTasksWithDeadline(l);
    }

    public void remove(RecyclerView recyclerView) {
        this.mRecyclerViews.remove(recyclerView);
    }

    @Override
    public void run() {
        block9 : {
            TraceCompat.beginSection("RV Prefetch");
            boolean bl = this.mRecyclerViews.isEmpty();
            if (!bl) break block9;
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
            return;
        }
        int n = this.mRecyclerViews.size();
        long l = 0L;
        for (int i = 0; i < n; ++i) {
            long l2;
            block10 : {
                RecyclerView recyclerView = this.mRecyclerViews.get(i);
                l2 = l;
                if (recyclerView.getWindowVisibility() != 0) break block10;
                l2 = Math.max(recyclerView.getDrawingTime(), l);
            }
            l = l2;
        }
        if (l == 0L) {
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
            return;
        }
        try {
            this.prefetch(TimeUnit.MILLISECONDS.toNanos(l) + this.mFrameIntervalNs);
            return;
        }
        catch (Throwable throwable) {
            throw throwable;
        }
        finally {
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
        }
    }

    static class LayoutPrefetchRegistryImpl
    implements RecyclerView.LayoutManager.LayoutPrefetchRegistry {
        int mCount;
        int[] mPrefetchArray;
        int mPrefetchDx;
        int mPrefetchDy;

        LayoutPrefetchRegistryImpl() {
        }

        @Override
        public void addPosition(int n, int n2) {
            if (n >= 0) {
                if (n2 >= 0) {
                    int n3 = this.mCount * 2;
                    int[] arrn = this.mPrefetchArray;
                    if (arrn == null) {
                        this.mPrefetchArray = arrn = new int[4];
                        Arrays.fill(arrn, -1);
                    } else if (n3 >= arrn.length) {
                        arrn = this.mPrefetchArray;
                        int[] arrn2 = new int[n3 * 2];
                        this.mPrefetchArray = arrn2;
                        System.arraycopy(arrn, 0, arrn2, 0, arrn.length);
                    }
                    arrn = this.mPrefetchArray;
                    arrn[n3] = n;
                    arrn[n3 + 1] = n2;
                    ++this.mCount;
                    return;
                }
                throw new IllegalArgumentException("Pixel distance must be non-negative");
            }
            throw new IllegalArgumentException("Layout positions must be non-negative");
        }

        void clearPrefetchPositions() {
            int[] arrn = this.mPrefetchArray;
            if (arrn != null) {
                Arrays.fill(arrn, -1);
            }
            this.mCount = 0;
        }

        void collectPrefetchPositionsFromView(RecyclerView recyclerView, boolean bl) {
            this.mCount = 0;
            int[] arrn = this.mPrefetchArray;
            if (arrn != null) {
                Arrays.fill(arrn, -1);
            }
            arrn = recyclerView.mLayout;
            if (recyclerView.mAdapter != null && arrn != null && arrn.isItemPrefetchEnabled()) {
                if (bl) {
                    if (!recyclerView.mAdapterHelper.hasPendingUpdates()) {
                        arrn.collectInitialPrefetchPositions(recyclerView.mAdapter.getItemCount(), this);
                    }
                } else if (!recyclerView.hasPendingAdapterUpdates()) {
                    arrn.collectAdjacentPrefetchPositions(this.mPrefetchDx, this.mPrefetchDy, recyclerView.mState, this);
                }
                if (this.mCount > arrn.mPrefetchMaxCountObserved) {
                    arrn.mPrefetchMaxCountObserved = this.mCount;
                    arrn.mPrefetchMaxObservedInInitialPrefetch = bl;
                    recyclerView.mRecycler.updateViewCacheSize();
                }
            }
        }

        boolean lastPrefetchIncludedPosition(int n) {
            if (this.mPrefetchArray != null) {
                int n2 = this.mCount;
                for (int i = 0; i < n2 * 2; i += 2) {
                    if (this.mPrefetchArray[i] != n) continue;
                    return true;
                }
            }
            return false;
        }

        void setPrefetchVector(int n, int n2) {
            this.mPrefetchDx = n;
            this.mPrefetchDy = n2;
        }
    }

    static class Task {
        public int distanceToItem;
        public boolean immediate;
        public int position;
        public RecyclerView view;
        public int viewVelocity;

        Task() {
        }

        public void clear() {
            this.immediate = false;
            this.viewVelocity = 0;
            this.distanceToItem = 0;
            this.view = null;
            this.position = 0;
        }
    }

}

