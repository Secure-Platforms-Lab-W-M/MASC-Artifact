/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.Pools;
import android.support.v7.widget.RecyclerView;

class ViewInfoStore {
    private static final boolean DEBUG = false;
    @VisibleForTesting
    final ArrayMap<RecyclerView.ViewHolder, InfoRecord> mLayoutHolderMap = new ArrayMap();
    @VisibleForTesting
    final LongSparseArray<RecyclerView.ViewHolder> mOldChangedHolders = new LongSparseArray();

    ViewInfoStore() {
    }

    private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder object, int n) {
        block5 : {
            block8 : {
                int n2;
                InfoRecord infoRecord;
                block7 : {
                    block6 : {
                        n2 = this.mLayoutHolderMap.indexOfKey(object);
                        if (n2 < 0) {
                            return null;
                        }
                        infoRecord = this.mLayoutHolderMap.valueAt(n2);
                        if (infoRecord == null || (infoRecord.flags & n) == 0) break block5;
                        infoRecord.flags &= ~ n;
                        if (n != 4) break block6;
                        object = infoRecord.preInfo;
                        break block7;
                    }
                    if (n != 8) break block8;
                    object = infoRecord.postInfo;
                }
                if ((infoRecord.flags & 12) == 0) {
                    this.mLayoutHolderMap.removeAt(n2);
                    InfoRecord.recycle(infoRecord);
                    return object;
                }
                return object;
            }
            throw new IllegalArgumentException("Must provide flag PRE or POST");
        }
        return null;
    }

    void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder object, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(object);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put((RecyclerView.ViewHolder)object, infoRecord);
            object = infoRecord;
        } else {
            object = infoRecord;
        }
        object.flags |= 2;
        object.preInfo = itemHolderInfo;
    }

    void addToDisappearedInLayout(RecyclerView.ViewHolder object) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(object);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put((RecyclerView.ViewHolder)object, infoRecord);
            object = infoRecord;
        } else {
            object = infoRecord;
        }
        object.flags |= 1;
    }

    void addToOldChangeHolders(long l, RecyclerView.ViewHolder viewHolder) {
        this.mOldChangedHolders.put(l, viewHolder);
    }

    void addToPostLayout(RecyclerView.ViewHolder object, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(object);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put((RecyclerView.ViewHolder)object, infoRecord);
            object = infoRecord;
        } else {
            object = infoRecord;
        }
        object.postInfo = itemHolderInfo;
        object.flags |= 8;
    }

    void addToPreLayout(RecyclerView.ViewHolder object, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(object);
        if (infoRecord == null) {
            infoRecord = InfoRecord.obtain();
            this.mLayoutHolderMap.put((RecyclerView.ViewHolder)object, infoRecord);
            object = infoRecord;
        } else {
            object = infoRecord;
        }
        object.preInfo = itemHolderInfo;
        object.flags |= 4;
    }

    void clear() {
        this.mLayoutHolderMap.clear();
        this.mOldChangedHolders.clear();
    }

    RecyclerView.ViewHolder getFromOldChangeHolders(long l) {
        return this.mOldChangedHolders.get(l);
    }

    boolean isDisappearing(RecyclerView.ViewHolder object) {
        if ((object = this.mLayoutHolderMap.get(object)) != null && (object.flags & 1) != 0) {
            return true;
        }
        return false;
    }

    boolean isInPreLayout(RecyclerView.ViewHolder object) {
        if ((object = this.mLayoutHolderMap.get(object)) != null && (object.flags & 4) != 0) {
            return true;
        }
        return false;
    }

    void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(RecyclerView.ViewHolder viewHolder) {
        this.removeFromDisappearedInLayout(viewHolder);
    }

    @Nullable
    RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder viewHolder) {
        return this.popFromLayoutStep(viewHolder, 8);
    }

    @Nullable
    RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder viewHolder) {
        return this.popFromLayoutStep(viewHolder, 4);
    }

    void process(ProcessCallback processCallback) {
        for (int i = this.mLayoutHolderMap.size() - 1; i >= 0; --i) {
            RecyclerView.ViewHolder viewHolder = this.mLayoutHolderMap.keyAt(i);
            InfoRecord infoRecord = this.mLayoutHolderMap.removeAt(i);
            if ((infoRecord.flags & 3) == 3) {
                processCallback.unused(viewHolder);
            } else if ((infoRecord.flags & 1) != 0) {
                if (infoRecord.preInfo == null) {
                    processCallback.unused(viewHolder);
                } else {
                    processCallback.processDisappeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
                }
            } else if ((infoRecord.flags & 14) == 14) {
                processCallback.processAppeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
            } else if ((infoRecord.flags & 12) == 12) {
                processCallback.processPersistent(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
            } else if ((infoRecord.flags & 4) != 0) {
                processCallback.processDisappeared(viewHolder, infoRecord.preInfo, null);
            } else if ((infoRecord.flags & 8) != 0) {
                processCallback.processAppeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
            } else {
                int n = infoRecord.flags;
            }
            InfoRecord.recycle(infoRecord);
        }
    }

    void removeFromDisappearedInLayout(RecyclerView.ViewHolder object) {
        if ((object = this.mLayoutHolderMap.get(object)) == null) {
            return;
        }
        object.flags &= -2;
    }

    void removeViewHolder(RecyclerView.ViewHolder object) {
        for (int i = this.mOldChangedHolders.size() - 1; i >= 0; --i) {
            if (object != this.mOldChangedHolders.valueAt(i)) continue;
            this.mOldChangedHolders.removeAt(i);
            break;
        }
        if ((object = this.mLayoutHolderMap.remove(object)) != null) {
            InfoRecord.recycle((InfoRecord)object);
            return;
        }
    }

    static class InfoRecord {
        static final int FLAG_APPEAR = 2;
        static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
        static final int FLAG_APPEAR_PRE_AND_POST = 14;
        static final int FLAG_DISAPPEARED = 1;
        static final int FLAG_POST = 8;
        static final int FLAG_PRE = 4;
        static final int FLAG_PRE_AND_POST = 12;
        static Pools.Pool<InfoRecord> sPool = new Pools.SimplePool<InfoRecord>(20);
        int flags;
        @Nullable
        RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
        @Nullable
        RecyclerView.ItemAnimator.ItemHolderInfo preInfo;

        private InfoRecord() {
        }

        static void drainCache() {
            while (sPool.acquire() != null) {
            }
        }

        static InfoRecord obtain() {
            InfoRecord infoRecord = sPool.acquire();
            if (infoRecord == null) {
                return new InfoRecord();
            }
            return infoRecord;
        }

        static void recycle(InfoRecord infoRecord) {
            infoRecord.flags = 0;
            infoRecord.preInfo = null;
            infoRecord.postInfo = null;
            sPool.release(infoRecord);
        }
    }

    static interface ProcessCallback {
        public void processAppeared(RecyclerView.ViewHolder var1, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

        public void processDisappeared(RecyclerView.ViewHolder var1, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var2, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo var3);

        public void processPersistent(RecyclerView.ViewHolder var1, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var2, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var3);

        public void unused(RecyclerView.ViewHolder var1);
    }

}

