/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.view.View
 */
package androidx.recyclerview.widget;

import android.graphics.Canvas;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchUIUtil {
    public void clearView(View var1);

    public void onDraw(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7);

    public void onDrawOver(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7);

    public void onSelected(View var1);
}

