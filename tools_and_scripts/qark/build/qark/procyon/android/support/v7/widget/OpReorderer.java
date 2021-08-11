// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import java.util.List;

class OpReorderer
{
    final Callback mCallback;
    
    OpReorderer(final Callback mCallback) {
        this.mCallback = mCallback;
    }
    
    private int getLastMoveOutOfOrder(final List<AdapterHelper.UpdateOp> list) {
        int n = 0;
        for (int i = list.size() - 1; i >= 0; --i) {
            if (list.get(i).cmd == 8) {
                if (n != 0) {
                    return i;
                }
            }
            else {
                n = 1;
            }
        }
        return -1;
    }
    
    private void swapMoveAdd(final List<AdapterHelper.UpdateOp> list, final int n, final AdapterHelper.UpdateOp updateOp, final int n2, final AdapterHelper.UpdateOp updateOp2) {
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
    
    private void swapMoveOp(final List<AdapterHelper.UpdateOp> list, final int n, final int n2) {
        final AdapterHelper.UpdateOp updateOp = list.get(n);
        final AdapterHelper.UpdateOp updateOp2 = list.get(n2);
        final int cmd = updateOp2.cmd;
        if (cmd == 4) {
            this.swapMoveUpdate(list, n, updateOp, n2, updateOp2);
            return;
        }
        switch (cmd) {
            default: {}
            case 2: {
                this.swapMoveRemove(list, n, updateOp, n2, updateOp2);
            }
            case 1: {
                this.swapMoveAdd(list, n, updateOp, n2, updateOp2);
            }
        }
    }
    
    void reorderOps(final List<AdapterHelper.UpdateOp> list) {
        while (true) {
            final int lastMoveOutOfOrder = this.getLastMoveOutOfOrder(list);
            if (lastMoveOutOfOrder == -1) {
                break;
            }
            this.swapMoveOp(list, lastMoveOutOfOrder, lastMoveOutOfOrder + 1);
        }
    }
    
    void swapMoveRemove(final List<AdapterHelper.UpdateOp> list, final int n, final AdapterHelper.UpdateOp updateOp, final int n2, final AdapterHelper.UpdateOp updateOp2) {
        AdapterHelper.UpdateOp obtainUpdateOp = null;
        boolean b = false;
        boolean b2;
        if (updateOp.positionStart < updateOp.itemCount) {
            b2 = false;
            if (updateOp2.positionStart == updateOp.positionStart && updateOp2.itemCount == updateOp.itemCount - updateOp.positionStart) {
                b = true;
            }
        }
        else {
            b2 = true;
            if (updateOp2.positionStart == updateOp.itemCount + 1 && updateOp2.itemCount == updateOp.positionStart - updateOp.itemCount) {
                b = true;
            }
        }
        if (updateOp.itemCount < updateOp2.positionStart) {
            --updateOp2.positionStart;
        }
        else if (updateOp.itemCount < updateOp2.positionStart + updateOp2.itemCount) {
            --updateOp2.itemCount;
            updateOp.cmd = 2;
            updateOp.itemCount = 1;
            if (updateOp2.itemCount == 0) {
                list.remove(n2);
                this.mCallback.recycleUpdateOp(updateOp2);
            }
            return;
        }
        if (updateOp.positionStart <= updateOp2.positionStart) {
            ++updateOp2.positionStart;
        }
        else if (updateOp.positionStart < updateOp2.positionStart + updateOp2.itemCount) {
            obtainUpdateOp = this.mCallback.obtainUpdateOp(2, updateOp.positionStart + 1, updateOp2.positionStart + updateOp2.itemCount - updateOp.positionStart, null);
            updateOp2.itemCount = updateOp.positionStart - updateOp2.positionStart;
        }
        if (b) {
            list.set(n, updateOp2);
            list.remove(n2);
            this.mCallback.recycleUpdateOp(updateOp);
            return;
        }
        if (b2) {
            if (obtainUpdateOp != null) {
                if (updateOp.positionStart > obtainUpdateOp.positionStart) {
                    updateOp.positionStart -= obtainUpdateOp.itemCount;
                }
                if (updateOp.itemCount > obtainUpdateOp.positionStart) {
                    updateOp.itemCount -= obtainUpdateOp.itemCount;
                }
            }
            if (updateOp.positionStart > updateOp2.positionStart) {
                updateOp.positionStart -= updateOp2.itemCount;
            }
            if (updateOp.itemCount > updateOp2.positionStart) {
                updateOp.itemCount -= updateOp2.itemCount;
            }
        }
        else {
            if (obtainUpdateOp != null) {
                if (updateOp.positionStart >= obtainUpdateOp.positionStart) {
                    updateOp.positionStart -= obtainUpdateOp.itemCount;
                }
                if (updateOp.itemCount >= obtainUpdateOp.positionStart) {
                    updateOp.itemCount -= obtainUpdateOp.itemCount;
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
        }
        else {
            list.remove(n2);
        }
        if (obtainUpdateOp != null) {
            list.add(n, obtainUpdateOp);
        }
    }
    
    void swapMoveUpdate(final List<AdapterHelper.UpdateOp> list, final int n, final AdapterHelper.UpdateOp updateOp, final int n2, final AdapterHelper.UpdateOp updateOp2) {
        AdapterHelper.UpdateOp obtainUpdateOp = null;
        AdapterHelper.UpdateOp obtainUpdateOp2 = null;
        if (updateOp.itemCount < updateOp2.positionStart) {
            --updateOp2.positionStart;
        }
        else if (updateOp.itemCount < updateOp2.positionStart + updateOp2.itemCount) {
            --updateOp2.itemCount;
            obtainUpdateOp = this.mCallback.obtainUpdateOp(4, updateOp.positionStart, 1, updateOp2.payload);
        }
        if (updateOp.positionStart <= updateOp2.positionStart) {
            ++updateOp2.positionStart;
        }
        else if (updateOp.positionStart < updateOp2.positionStart + updateOp2.itemCount) {
            final int n3 = updateOp2.positionStart + updateOp2.itemCount - updateOp.positionStart;
            obtainUpdateOp2 = this.mCallback.obtainUpdateOp(4, updateOp.positionStart + 1, n3, updateOp2.payload);
            updateOp2.itemCount -= n3;
        }
        list.set(n2, updateOp);
        if (updateOp2.itemCount > 0) {
            list.set(n, updateOp2);
        }
        else {
            list.remove(n);
            this.mCallback.recycleUpdateOp(updateOp2);
        }
        if (obtainUpdateOp != null) {
            list.add(n, obtainUpdateOp);
        }
        if (obtainUpdateOp2 != null) {
            list.add(n, obtainUpdateOp2);
        }
    }
    
    interface Callback
    {
        AdapterHelper.UpdateOp obtainUpdateOp(final int p0, final int p1, final int p2, final Object p3);
        
        void recycleUpdateOp(final AdapterHelper.UpdateOp p0);
    }
}
