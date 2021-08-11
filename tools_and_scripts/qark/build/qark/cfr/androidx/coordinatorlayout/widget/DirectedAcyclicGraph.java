/*
 * Decompiled with CFR 0_124.
 */
package androidx.coordinatorlayout.widget;

import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class DirectedAcyclicGraph<T> {
    private final SimpleArrayMap<T, ArrayList<T>> mGraph = new SimpleArrayMap();
    private final Pools.Pool<ArrayList<T>> mListPool = new Pools.SimplePool<ArrayList<T>>(10);
    private final ArrayList<T> mSortResult = new ArrayList();
    private final HashSet<T> mSortTmpMarked = new HashSet();

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

    private ArrayList<T> getEmptyList() {
        ArrayList<T> arrayList;
        ArrayList arrayList2 = arrayList = this.mListPool.acquire();
        if (arrayList == null) {
            arrayList2 = new ArrayList();
        }
        return arrayList2;
    }

    private void poolList(ArrayList<T> arrayList) {
        arrayList.clear();
        this.mListPool.release(arrayList);
    }

    public void addEdge(T t, T t2) {
        if (this.mGraph.containsKey(t) && this.mGraph.containsKey(t2)) {
            ArrayList<T> arrayList;
            ArrayList<T> arrayList2 = arrayList = this.mGraph.get(t);
            if (arrayList == null) {
                arrayList2 = this.getEmptyList();
                this.mGraph.put(t, arrayList2);
            }
            arrayList2.add(t2);
            return;
        }
        throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
    }

    public void addNode(T t) {
        if (!this.mGraph.containsKey(t)) {
            this.mGraph.put(t, null);
        }
    }

    public void clear() {
        int n = this.mGraph.size();
        for (int i = 0; i < n; ++i) {
            ArrayList<T> arrayList = this.mGraph.valueAt(i);
            if (arrayList == null) continue;
            this.poolList(arrayList);
        }
        this.mGraph.clear();
    }

    public boolean contains(T t) {
        return this.mGraph.containsKey(t);
    }

    public List getIncomingEdges(T t) {
        return this.mGraph.get(t);
    }

    public List<T> getOutgoingEdges(T t) {
        ArrayList<T> arrayList = null;
        int n = this.mGraph.size();
        for (int i = 0; i < n; ++i) {
            ArrayList<T> arrayList2 = this.mGraph.valueAt(i);
            ArrayList<T> arrayList3 = arrayList;
            if (arrayList2 != null) {
                arrayList3 = arrayList;
                if (arrayList2.contains(t)) {
                    arrayList3 = arrayList;
                    if (arrayList == null) {
                        arrayList3 = new ArrayList<T>();
                    }
                    arrayList3.add(this.mGraph.keyAt(i));
                }
            }
            arrayList = arrayList3;
        }
        return arrayList;
    }

    public ArrayList<T> getSortedList() {
        this.mSortResult.clear();
        this.mSortTmpMarked.clear();
        int n = this.mGraph.size();
        for (int i = 0; i < n; ++i) {
            this.dfs(this.mGraph.keyAt(i), this.mSortResult, this.mSortTmpMarked);
        }
        return this.mSortResult;
    }

    public boolean hasOutgoingEdges(T t) {
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

