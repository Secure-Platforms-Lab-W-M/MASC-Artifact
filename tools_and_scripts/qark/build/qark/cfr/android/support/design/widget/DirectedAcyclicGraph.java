/*
 * Decompiled with CFR 0_124.
 */
package android.support.design.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pools;
import android.support.v4.util.SimpleArrayMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

final class DirectedAcyclicGraph<T> {
    private final SimpleArrayMap<T, ArrayList<T>> mGraph = new SimpleArrayMap();
    private final Pools.Pool<ArrayList<T>> mListPool = new Pools.SimplePool<ArrayList<T>>(10);
    private final ArrayList<T> mSortResult = new ArrayList();
    private final HashSet<T> mSortTmpMarked = new HashSet();

    DirectedAcyclicGraph() {
    }

    private void dfs(T t, ArrayList<T> arrayList, HashSet<T> hashSet) {
        if (arrayList.contains(t)) {
            return;
        }
        if (!hashSet.contains(t)) {
            hashSet.add(t);
            ArrayList<T> arrayList2 = this.mGraph.get(t);
            if (arrayList2 != null) {
                int n = arrayList2.size();
                for (int i = 0; i < n; ++i) {
                    this.dfs(arrayList2.get(i), arrayList, hashSet);
                }
            }
            hashSet.remove(t);
            arrayList.add(t);
            return;
        }
        throw new RuntimeException("This graph contains cyclic dependencies");
    }

    @NonNull
    private ArrayList<T> getEmptyList() {
        ArrayList<T> arrayList = this.mListPool.acquire();
        if (arrayList == null) {
            return new ArrayList();
        }
        return arrayList;
    }

    private void poolList(@NonNull ArrayList<T> arrayList) {
        arrayList.clear();
        this.mListPool.release(arrayList);
    }

    void addEdge(@NonNull T arrayList, @NonNull T t) {
        if (this.mGraph.containsKey(arrayList) && this.mGraph.containsKey(t)) {
            ArrayList<T> arrayList2 = this.mGraph.get(arrayList);
            if (arrayList2 == null) {
                arrayList2 = this.getEmptyList();
                this.mGraph.put((Object)arrayList, arrayList2);
                arrayList = arrayList2;
            } else {
                arrayList = arrayList2;
            }
            arrayList.add(t);
            return;
        }
        throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
    }

    void addNode(@NonNull T t) {
        if (!this.mGraph.containsKey(t)) {
            this.mGraph.put(t, null);
            return;
        }
    }

    void clear() {
        int n = this.mGraph.size();
        for (int i = 0; i < n; ++i) {
            ArrayList<T> arrayList = this.mGraph.valueAt(i);
            if (arrayList == null) continue;
            this.poolList(arrayList);
        }
        this.mGraph.clear();
    }

    boolean contains(@NonNull T t) {
        return this.mGraph.containsKey(t);
    }

    @Nullable
    List getIncomingEdges(@NonNull T t) {
        return this.mGraph.get(t);
    }

    @Nullable
    List<T> getOutgoingEdges(@NonNull T t) {
        ArrayList<T> arrayList = null;
        int n = this.mGraph.size();
        for (int i = 0; i < n; ++i) {
            ArrayList<T> arrayList2 = this.mGraph.valueAt(i);
            if (arrayList2 == null || !arrayList2.contains(t)) continue;
            if (arrayList == null) {
                arrayList = new ArrayList<T>();
            }
            arrayList.add(this.mGraph.keyAt(i));
        }
        return arrayList;
    }

    @NonNull
    ArrayList<T> getSortedList() {
        this.mSortResult.clear();
        this.mSortTmpMarked.clear();
        int n = this.mGraph.size();
        for (int i = 0; i < n; ++i) {
            this.dfs(this.mGraph.keyAt(i), this.mSortResult, this.mSortTmpMarked);
        }
        return this.mSortResult;
    }

    boolean hasOutgoingEdges(@NonNull T t) {
        int n = this.mGraph.size();
        for (int i = 0; i < n; ++i) {
            ArrayList<T> arrayList = this.mGraph.valueAt(i);
            if (arrayList == null || !arrayList.contains(t)) continue;
            return true;
        }
        return false;
    }

    int size() {
        return this.mGraph.size();
    }
}

