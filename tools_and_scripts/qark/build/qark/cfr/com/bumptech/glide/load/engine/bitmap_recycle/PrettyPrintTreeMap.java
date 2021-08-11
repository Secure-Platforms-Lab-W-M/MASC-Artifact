/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

class PrettyPrintTreeMap<K, V>
extends TreeMap<K, V> {
    PrettyPrintTreeMap() {
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("( ");
        for (Map.Entry entry : this.entrySet()) {
            stringBuilder.append('{');
            stringBuilder.append(entry.getKey());
            stringBuilder.append(':');
            stringBuilder.append(entry.getValue());
            stringBuilder.append("}, ");
        }
        if (!this.isEmpty()) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        }
        stringBuilder.append(" )");
        return stringBuilder.toString();
    }
}

