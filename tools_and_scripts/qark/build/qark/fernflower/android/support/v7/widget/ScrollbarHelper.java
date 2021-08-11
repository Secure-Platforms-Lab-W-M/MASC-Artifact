package android.support.v7.widget;

import android.view.View;

class ScrollbarHelper {
   static int computeScrollExtent(RecyclerView.State var0, OrientationHelper var1, View var2, View var3, RecyclerView.LayoutManager var4, boolean var5) {
      if (var4.getChildCount() != 0 && var0.getItemCount() != 0 && var2 != null && var3 != null) {
         if (!var5) {
            return Math.abs(var4.getPosition(var2) - var4.getPosition(var3)) + 1;
         } else {
            int var6 = var1.getDecoratedEnd(var3);
            int var7 = var1.getDecoratedStart(var2);
            return Math.min(var1.getTotalSpace(), var6 - var7);
         }
      } else {
         return 0;
      }
   }

   static int computeScrollOffset(RecyclerView.State var0, OrientationHelper var1, View var2, View var3, RecyclerView.LayoutManager var4, boolean var5, boolean var6) {
      if (var4.getChildCount() != 0 && var0.getItemCount() != 0 && var2 != null) {
         if (var3 == null) {
            return 0;
         } else {
            int var8 = Math.min(var4.getPosition(var2), var4.getPosition(var3));
            int var9 = Math.max(var4.getPosition(var2), var4.getPosition(var3));
            if (var6) {
               var8 = Math.max(0, var0.getItemCount() - var9 - 1);
            } else {
               var8 = Math.max(0, var8);
            }

            if (!var5) {
               return var8;
            } else {
               var9 = Math.abs(var1.getDecoratedEnd(var3) - var1.getDecoratedStart(var2));
               int var10 = Math.abs(var4.getPosition(var2) - var4.getPosition(var3));
               float var7 = (float)var9 / (float)(var10 + 1);
               return Math.round((float)var8 * var7 + (float)(var1.getStartAfterPadding() - var1.getDecoratedStart(var2)));
            }
         }
      } else {
         return 0;
      }
   }

   static int computeScrollRange(RecyclerView.State var0, OrientationHelper var1, View var2, View var3, RecyclerView.LayoutManager var4, boolean var5) {
      if (var4.getChildCount() != 0 && var0.getItemCount() != 0 && var2 != null && var3 != null) {
         if (!var5) {
            return var0.getItemCount();
         } else {
            int var6 = var1.getDecoratedEnd(var3);
            int var7 = var1.getDecoratedStart(var2);
            int var8 = Math.abs(var4.getPosition(var2) - var4.getPosition(var3));
            return (int)((float)(var6 - var7) / (float)(var8 + 1) * (float)var0.getItemCount());
         }
      } else {
         return 0;
      }
   }
}
