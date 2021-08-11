package android.support.v7.widget.helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface ItemTouchUIUtil {
   void clearView(View var1);

   void onDraw(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7);

   void onDrawOver(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7);

   void onSelected(View var1);
}
