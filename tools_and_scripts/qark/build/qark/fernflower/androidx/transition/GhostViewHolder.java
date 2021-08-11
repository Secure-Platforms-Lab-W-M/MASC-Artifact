package androidx.transition;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.transition.R.id;
import java.util.ArrayList;

class GhostViewHolder extends FrameLayout {
   private boolean mAttached;
   private ViewGroup mParent;

   GhostViewHolder(ViewGroup var1) {
      super(var1.getContext());
      this.setClipChildren(false);
      this.mParent = var1;
      var1.setTag(id.ghost_view_holder, this);
      ViewGroupUtils.getOverlay(this.mParent).add(this);
      this.mAttached = true;
   }

   static GhostViewHolder getHolder(ViewGroup var0) {
      return (GhostViewHolder)var0.getTag(id.ghost_view_holder);
   }

   private int getInsertIndex(ArrayList var1) {
      ArrayList var5 = new ArrayList();
      int var3 = 0;

      for(int var2 = this.getChildCount() - 1; var3 <= var2; var5.clear()) {
         int var4 = (var3 + var2) / 2;
         getParents(((GhostViewPort)this.getChildAt(var4)).mView, var5);
         if (isOnTop(var1, var5)) {
            var3 = var4 + 1;
         } else {
            var2 = var4 - 1;
         }
      }

      return var3;
   }

   private static void getParents(View var0, ArrayList var1) {
      ViewParent var2 = var0.getParent();
      if (var2 instanceof ViewGroup) {
         getParents((View)var2, var1);
      }

      var1.add(var0);
   }

   private static boolean isOnTop(View var0, View var1) {
      ViewGroup var4 = (ViewGroup)var0.getParent();
      int var3 = var4.getChildCount();
      if (VERSION.SDK_INT >= 21 && var0.getZ() != var1.getZ()) {
         return var0.getZ() > var1.getZ();
      } else {
         for(int var2 = 0; var2 < var3; ++var2) {
            View var5 = var4.getChildAt(ViewGroupUtils.getChildDrawingOrder(var4, var2));
            if (var5 == var0) {
               return false;
            }

            if (var5 == var1) {
               return true;
            }
         }

         return true;
      }
   }

   private static boolean isOnTop(ArrayList var0, ArrayList var1) {
      if (!var0.isEmpty() && !var1.isEmpty()) {
         if (var0.get(0) != var1.get(0)) {
            return true;
         } else {
            int var3 = Math.min(var0.size(), var1.size());

            for(int var2 = 1; var2 < var3; ++var2) {
               View var4 = (View)var0.get(var2);
               View var5 = (View)var1.get(var2);
               if (var4 != var5) {
                  return isOnTop(var4, var5);
               }
            }

            if (var1.size() == var3) {
               return true;
            } else {
               return false;
            }
         }
      } else {
         return true;
      }
   }

   void addGhostView(GhostViewPort var1) {
      ArrayList var3 = new ArrayList();
      getParents(var1.mView, var3);
      int var2 = this.getInsertIndex(var3);
      if (var2 >= 0 && var2 < this.getChildCount()) {
         this.addView(var1, var2);
      } else {
         this.addView(var1);
      }
   }

   public void onViewAdded(View var1) {
      if (this.mAttached) {
         super.onViewAdded(var1);
      } else {
         throw new IllegalStateException("This GhostViewHolder is detached!");
      }
   }

   public void onViewRemoved(View var1) {
      super.onViewRemoved(var1);
      if (this.getChildCount() == 1 && this.getChildAt(0) == var1 || this.getChildCount() == 0) {
         this.mParent.setTag(id.ghost_view_holder, (Object)null);
         ViewGroupUtils.getOverlay(this.mParent).remove(this);
         this.mAttached = false;
      }

   }

   void popToOverlayTop() {
      if (this.mAttached) {
         ViewGroupUtils.getOverlay(this.mParent).remove(this);
         ViewGroupUtils.getOverlay(this.mParent).add(this);
      } else {
         throw new IllegalStateException("This GhostViewHolder is detached!");
      }
   }
}
