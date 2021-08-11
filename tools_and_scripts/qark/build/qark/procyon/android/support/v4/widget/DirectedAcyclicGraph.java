// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import android.support.annotation.Nullable;
import java.util.List;
import android.support.annotation.NonNull;
import java.util.HashSet;
import android.support.v4.util.Pools;
import java.util.ArrayList;
import android.support.v4.util.SimpleArrayMap;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY })
public final class DirectedAcyclicGraph<T>
{
    private final SimpleArrayMap<T, ArrayList<T>> mGraph;
    private final Pools.Pool<ArrayList<T>> mListPool;
    private final ArrayList<T> mSortResult;
    private final HashSet<T> mSortTmpMarked;
    
    public DirectedAcyclicGraph() {
        this.mListPool = new Pools.SimplePool<ArrayList<T>>(10);
        this.mGraph = new SimpleArrayMap<T, ArrayList<T>>();
        this.mSortResult = new ArrayList<T>();
        this.mSortTmpMarked = new HashSet<T>();
    }
    
    private void dfs(final T t, final ArrayList<T> list, final HashSet<T> set) {
        if (list.contains(t)) {
            return;
        }
        if (set.contains(t)) {
            throw new RuntimeException("This graph contains cyclic dependencies");
        }
        set.add(t);
        final ArrayList<T> list2 = this.mGraph.get(t);
        if (list2 != null) {
            for (int i = 0; i < list2.size(); ++i) {
                this.dfs(list2.get(i), list, set);
            }
        }
        set.remove(t);
        list.add(t);
    }
    
    @NonNull
    private ArrayList<T> getEmptyList() {
        ArrayList<T> list;
        if ((list = this.mListPool.acquire()) == null) {
            list = new ArrayList<T>();
        }
        return list;
    }
    
    private void poolList(@NonNull final ArrayList<T> list) {
        list.clear();
        this.mListPool.release(list);
    }
    
    public void addEdge(@NonNull final T t, @NonNull final T t2) {
        if (!this.mGraph.containsKey(t) || !this.mGraph.containsKey(t2)) {
            throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
        }
        ArrayList<T> emptyList;
        if ((emptyList = this.mGraph.get(t)) == null) {
            emptyList = this.getEmptyList();
            this.mGraph.put(t, emptyList);
        }
        emptyList.add(t2);
    }
    
    public void addNode(@NonNull final T t) {
        if (!this.mGraph.containsKey(t)) {
            this.mGraph.put(t, null);
        }
    }
    
    public void clear() {
        for (int i = 0; i < this.mGraph.size(); ++i) {
            final ArrayList<T> list = this.mGraph.valueAt(i);
            if (list != null) {
                this.poolList(list);
            }
        }
        this.mGraph.clear();
    }
    
    public boolean contains(@NonNull final T t) {
        return this.mGraph.containsKey(t);
    }
    
    @Nullable
    public List getIncomingEdges(@NonNull final T t) {
        return this.mGraph.get(t);
    }
    
    @Nullable
    public List<T> getOutgoingEdges(@NonNull final T t) {
        ArrayList<T> list = null;
        ArrayList<T> list3;
        for (int i = 0; i < this.mGraph.size(); ++i, list = list3) {
            final ArrayList<T> list2 = this.mGraph.valueAt(i);
            list3 = list;
            if (list2 != null) {
                list3 = list;
                if (list2.contains(t)) {
                    if ((list3 = list) == null) {
                        list3 = new ArrayList<T>();
                    }
                    list3.add(this.mGraph.keyAt(i));
                }
            }
        }
        return list;
    }
    
    @NonNull
    public ArrayList<T> getSortedList() {
        this.mSortResult.clear();
        this.mSortTmpMarked.clear();
        for (int i = 0; i < this.mGraph.size(); ++i) {
            this.dfs(this.mGraph.keyAt(i), this.mSortResult, this.mSortTmpMarked);
        }
        return this.mSortResult;
    }
    
    public boolean hasOutgoingEdges(@NonNull final T t) {
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
