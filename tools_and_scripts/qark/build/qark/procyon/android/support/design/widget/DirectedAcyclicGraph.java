// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.support.annotation.Nullable;
import java.util.List;
import android.support.annotation.NonNull;
import java.util.HashSet;
import android.support.v4.util.Pools;
import java.util.ArrayList;
import android.support.v4.util.SimpleArrayMap;

final class DirectedAcyclicGraph<T>
{
    private final SimpleArrayMap<T, ArrayList<T>> mGraph;
    private final Pools.Pool<ArrayList<T>> mListPool;
    private final ArrayList<T> mSortResult;
    private final HashSet<T> mSortTmpMarked;
    
    DirectedAcyclicGraph() {
        this.mListPool = new Pools.SimplePool<ArrayList<T>>(10);
        this.mGraph = new SimpleArrayMap<T, ArrayList<T>>();
        this.mSortResult = new ArrayList<T>();
        this.mSortTmpMarked = new HashSet<T>();
    }
    
    private void dfs(final T t, final ArrayList<T> list, final HashSet<T> set) {
        if (list.contains(t)) {
            return;
        }
        if (!set.contains(t)) {
            set.add(t);
            final ArrayList<T> list2 = this.mGraph.get(t);
            if (list2 != null) {
                for (int i = 0; i < list2.size(); ++i) {
                    this.dfs(list2.get(i), list, set);
                }
            }
            set.remove(t);
            list.add(t);
            return;
        }
        throw new RuntimeException("This graph contains cyclic dependencies");
    }
    
    @NonNull
    private ArrayList<T> getEmptyList() {
        final ArrayList<T> list = this.mListPool.acquire();
        if (list == null) {
            return new ArrayList<T>();
        }
        return list;
    }
    
    private void poolList(@NonNull final ArrayList<T> list) {
        list.clear();
        this.mListPool.release(list);
    }
    
    void addEdge(@NonNull final T t, @NonNull final T t2) {
        if (this.mGraph.containsKey(t) && this.mGraph.containsKey(t2)) {
            final ArrayList<T> list = this.mGraph.get(t);
            ArrayList<T> list2;
            if (list == null) {
                final ArrayList<T> emptyList = this.getEmptyList();
                this.mGraph.put(t, emptyList);
                list2 = emptyList;
            }
            else {
                list2 = list;
            }
            list2.add(t2);
            return;
        }
        throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
    }
    
    void addNode(@NonNull final T t) {
        if (!this.mGraph.containsKey(t)) {
            this.mGraph.put(t, null);
        }
    }
    
    void clear() {
        for (int i = 0; i < this.mGraph.size(); ++i) {
            final ArrayList<T> list = this.mGraph.valueAt(i);
            if (list != null) {
                this.poolList(list);
            }
        }
        this.mGraph.clear();
    }
    
    boolean contains(@NonNull final T t) {
        return this.mGraph.containsKey(t);
    }
    
    @Nullable
    List getIncomingEdges(@NonNull final T t) {
        return this.mGraph.get(t);
    }
    
    @Nullable
    List<T> getOutgoingEdges(@NonNull final T t) {
        ArrayList<T> list = null;
        for (int i = 0; i < this.mGraph.size(); ++i) {
            final ArrayList<T> list2 = this.mGraph.valueAt(i);
            if (list2 != null && list2.contains(t)) {
                if (list == null) {
                    list = new ArrayList<T>();
                }
                list.add(this.mGraph.keyAt(i));
            }
        }
        return list;
    }
    
    @NonNull
    ArrayList<T> getSortedList() {
        this.mSortResult.clear();
        this.mSortTmpMarked.clear();
        for (int i = 0; i < this.mGraph.size(); ++i) {
            this.dfs(this.mGraph.keyAt(i), this.mSortResult, this.mSortTmpMarked);
        }
        return this.mSortResult;
    }
    
    boolean hasOutgoingEdges(@NonNull final T t) {
        for (int i = 0; i < this.mGraph.size(); ++i) {
            final ArrayList<T> list = this.mGraph.valueAt(i);
            if (list != null && list.contains(t)) {
                return true;
            }
        }
        return false;
    }
    
    int size() {
        return this.mGraph.size();
    }
}
