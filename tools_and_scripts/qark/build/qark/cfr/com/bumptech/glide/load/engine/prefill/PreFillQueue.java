/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.prefill;

import com.bumptech.glide.load.engine.prefill.PreFillType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class PreFillQueue {
    private final Map<PreFillType, Integer> bitmapsPerType;
    private int bitmapsRemaining;
    private int keyIndex;
    private final List<PreFillType> keyList;

    public PreFillQueue(Map<PreFillType, Integer> object) {
        this.bitmapsPerType = object;
        this.keyList = new ArrayList<PreFillType>(object.keySet());
        for (Integer n : object.values()) {
            this.bitmapsRemaining += n.intValue();
        }
    }

    public int getSize() {
        return this.bitmapsRemaining;
    }

    public boolean isEmpty() {
        if (this.bitmapsRemaining == 0) {
            return true;
        }
        return false;
    }

    public PreFillType remove() {
        PreFillType preFillType = this.keyList.get(this.keyIndex);
        Integer n = this.bitmapsPerType.get(preFillType);
        if (n == 1) {
            this.bitmapsPerType.remove(preFillType);
            this.keyList.remove(this.keyIndex);
        } else {
            this.bitmapsPerType.put(preFillType, n - 1);
        }
        --this.bitmapsRemaining;
        int n2 = this.keyList.isEmpty() ? 0 : (this.keyIndex + 1) % this.keyList.size();
        this.keyIndex = n2;
        return preFillType;
    }
}

