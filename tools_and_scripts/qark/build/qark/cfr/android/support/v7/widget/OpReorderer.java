/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.widget;

import android.support.v7.widget.AdapterHelper;
import java.util.List;

class OpReorderer {
    final Callback mCallback;

    OpReorderer(Callback callback) {
        this.mCallback = callback;
    }

    private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> list) {
        boolean bl = false;
        for (int i = list.size() - 1; i >= 0; --i) {
            if (list.get((int)i).cmd == 8) {
                if (!bl) continue;
                return i;
            }
            bl = true;
        }
        return -1;
    }

    private void swapMoveAdd(List<AdapterHelper.UpdateOp> list, int n, AdapterHelper.UpdateOp updateOp, int n2, AdapterHelper.UpdateOp updateOp2) {
        int n3 = 0;
        if (updateOp.itemCount < updateOp2.positionStart) {
            n3 = 0 - 1;
        }
        if (updateOp.positionStart < updateOp2.positionStart) {
            ++n3;
        }
        if (updateOp2.positionStart <= updateOp.positionStart) {
            updateOp.positionStart += updateOp2.itemCount;
        }
        if (updateOp2.positionStart <= updateOp.itemCount) {
            updateOp.itemCount += updateOp2.itemCount;
        }
        updateOp2.positionStart += n3;
        list.set(n, updateOp2);
        list.set(n2, updateOp);
    }

    private void swapMoveOp(List<AdapterHelper.UpdateOp> list, int n, int n2) {
        AdapterHelper.UpdateOp updateOp = list.get(n);
        AdapterHelper.UpdateOp updateOp2 = list.get(n2);
        int n3 = updateOp2.cmd;
        if (n3 != 4) {
            switch (n3) {
                default: {
                    return;
                }
                case 2: {
                    this.swapMoveRemove(list, n, updateOp, n2, updateOp2);
                    return;
                }
                case 1: 
            }
            this.swapMoveAdd(list, n, updateOp, n2, updateOp2);
            return;
        }
        this.swapMoveUpdate(list, n, updateOp, n2, updateOp2);
    }

    void reorderOps(List<AdapterHelper.UpdateOp> list) {
        int n;
        while ((n = this.getLastMoveOutOfOrder(list)) != -1) {
            this.swapMoveOp(list, n, n + 1);
        }
    }

    void swapMoveRemove(List<AdapterHelper.UpdateOp> list, int n, AdapterHelper.UpdateOp updateOp, int n2, AdapterHelper.UpdateOp updateOp2) {
        boolean bl;
        AdapterHelper.UpdateOp updateOp3 = null;
        boolean bl2 = false;
        if (updateOp.positionStart < updateOp.itemCount) {
            bl = false;
            if (updateOp2.positionStart == updateOp.positionStart && updateOp2.itemCount == updateOp.itemCount - updateOp.positionStart) {
                bl2 = true;
            }
        } else {
            bl = true;
            if (updateOp2.positionStart == updateOp.itemCount + 1 && updateOp2.itemCount == updateOp.positionStart - updateOp.itemCount) {
                bl2 = true;
            }
        }
        if (updateOp.itemCount < updateOp2.positionStart) {
            --updateOp2.positionStart;
        } else if (updateOp.itemCount < updateOp2.positionStart + updateOp2.itemCount) {
            --updateOp2.itemCount;
            updateOp.cmd = 2;
            updateOp.itemCount = 1;
            if (updateOp2.itemCount == 0) {
                list.remove(n2);
                this.mCallback.recycleUpdateOp(updateOp2);
                return;
            }
            return;
        }
        if (updateOp.positionStart <= updateOp2.positionStart) {
            ++updateOp2.positionStart;
        } else if (updateOp.positionStart < updateOp2.positionStart + updateOp2.itemCount) {
            int n3 = updateOp2.positionStart;
            int n4 = updateOp2.itemCount;
            int n5 = updateOp.positionStart;
            updateOp3 = this.mCallback.obtainUpdateOp(2, updateOp.positionStart + 1, n3 + n4 - n5, null);
            updateOp2.itemCount = updateOp.positionStart - updateOp2.positionStart;
        }
        if (bl2) {
            list.set(n, updateOp2);
            list.remove(n2);
            this.mCallback.recycleUpdateOp(updateOp);
            return;
        }
        if (bl) {
            if (updateOp3 != null) {
                if (updateOp.positionStart > updateOp3.positionStart) {
                    updateOp.positionStart -= updateOp3.itemCount;
                }
                if (updateOp.itemCount > updateOp3.positionStart) {
                    updateOp.itemCount -= updateOp3.itemCount;
                }
            }
            if (updateOp.positionStart > updateOp2.positionStart) {
                updateOp.positionStart -= updateOp2.itemCount;
            }
            if (updateOp.itemCount > updateOp2.positionStart) {
                updateOp.itemCount -= updateOp2.itemCount;
            }
        } else {
            if (updateOp3 != null) {
                if (updateOp.positionStart >= updateOp3.positionStart) {
                    updateOp.positionStart -= updateOp3.itemCount;
                }
                if (updateOp.itemCount >= updateOp3.positionStart) {
                    updateOp.itemCount -= updateOp3.itemCount;
                }
            }
            if (updateOp.positionStart >= updateOp2.positionStart) {
                updateOp.positionStart -= updateOp2.itemCount;
            }
            if (updateOp.itemCount >= updateOp2.positionStart) {
                updateOp.itemCount -= updateOp2.itemCount;
            }
        }
        list.set(n, updateOp2);
        if (updateOp.positionStart != updateOp.itemCount) {
            list.set(n2, updateOp);
        } else {
            list.remove(n2);
        }
        if (updateOp3 != null) {
            list.add(n, updateOp3);
            return;
        }
    }

    void swapMoveUpdate(List<AdapterHelper.UpdateOp> list, int n, AdapterHelper.UpdateOp updateOp, int n2, AdapterHelper.UpdateOp updateOp2) {
        AdapterHelper.UpdateOp updateOp3 = null;
        AdapterHelper.UpdateOp updateOp4 = null;
        if (updateOp.itemCount < updateOp2.positionStart) {
            --updateOp2.positionStart;
        } else if (updateOp.itemCount < updateOp2.positionStart + updateOp2.itemCount) {
            --updateOp2.itemCount;
            updateOp3 = this.mCallback.obtainUpdateOp(4, updateOp.positionStart, 1, updateOp2.payload);
        }
        if (updateOp.positionStart <= updateOp2.positionStart) {
            ++updateOp2.positionStart;
        } else if (updateOp.positionStart < updateOp2.positionStart + updateOp2.itemCount) {
            int n3 = updateOp2.positionStart + updateOp2.itemCount - updateOp.positionStart;
            updateOp4 = this.mCallback.obtainUpdateOp(4, updateOp.positionStart + 1, n3, updateOp2.payload);
            updateOp2.itemCount -= n3;
        }
        list.set(n2, updateOp);
        if (updateOp2.itemCount > 0) {
            list.set(n, updateOp2);
        } else {
            list.remove(n);
            this.mCallback.recycleUpdateOp(updateOp2);
        }
        if (updateOp3 != null) {
            list.add(n, updateOp3);
        }
        if (updateOp4 != null) {
            list.add(n, updateOp4);
            return;
        }
    }

    static interface Callback {
        public AdapterHelper.UpdateOp obtainUpdateOp(int var1, int var2, int var3, Object var4);

        public void recycleUpdateOp(AdapterHelper.UpdateOp var1);
    }

}

