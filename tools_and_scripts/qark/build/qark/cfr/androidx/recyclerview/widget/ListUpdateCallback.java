/*
 * Decompiled with CFR 0_124.
 */
package androidx.recyclerview.widget;

public interface ListUpdateCallback {
    public void onChanged(int var1, int var2, Object var3);

    public void onInserted(int var1, int var2);

    public void onMoved(int var1, int var2);

    public void onRemoved(int var1, int var2);
}

