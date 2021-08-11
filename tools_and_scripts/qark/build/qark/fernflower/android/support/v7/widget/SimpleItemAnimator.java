package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class SimpleItemAnimator extends RecyclerView.ItemAnimator {
   private static final boolean DEBUG = false;
   private static final String TAG = "SimpleItemAnimator";
   boolean mSupportsChangeAnimations = true;

   public abstract boolean animateAdd(RecyclerView.ViewHolder var1);

   public boolean animateAppearance(@NonNull RecyclerView.ViewHolder var1, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo var2, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var3) {
      return var2 == null || var2.left == var3.left && var2.top == var3.top ? this.animateAdd(var1) : this.animateMove(var1, var2.left, var2.top, var3.left, var3.top);
   }

   public abstract boolean animateChange(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, int var3, int var4, int var5, int var6);

   public boolean animateChange(@NonNull RecyclerView.ViewHolder var1, @NonNull RecyclerView.ViewHolder var2, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var3, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var4) {
      int var7 = var3.left;
      int var8 = var3.top;
      int var5;
      int var6;
      if (var2.shouldIgnore()) {
         var5 = var3.left;
         var6 = var3.top;
      } else {
         var5 = var4.left;
         var6 = var4.top;
      }

      return this.animateChange(var1, var2, var7, var8, var5, var6);
   }

   public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder var1, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var2, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo var3) {
      int var6 = var2.left;
      int var7 = var2.top;
      View var8 = var1.itemView;
      int var4;
      if (var3 == null) {
         var4 = var8.getLeft();
      } else {
         var4 = var3.left;
      }

      int var5;
      if (var3 == null) {
         var5 = var8.getTop();
      } else {
         var5 = var3.top;
      }

      if (var1.isRemoved() || var6 == var4 && var7 == var5) {
         return this.animateRemove(var1);
      } else {
         var8.layout(var4, var5, var8.getWidth() + var4, var8.getHeight() + var5);
         return this.animateMove(var1, var6, var7, var4, var5);
      }
   }

   public abstract boolean animateMove(RecyclerView.ViewHolder var1, int var2, int var3, int var4, int var5);

   public boolean animatePersistence(@NonNull RecyclerView.ViewHolder var1, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var2, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var3) {
      if (var2.left == var3.left && var2.top == var3.top) {
         this.dispatchMoveFinished(var1);
         return false;
      } else {
         return this.animateMove(var1, var2.left, var2.top, var3.left, var3.top);
      }
   }

   public abstract boolean animateRemove(RecyclerView.ViewHolder var1);

   public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder var1) {
      return !this.mSupportsChangeAnimations || var1.isInvalid();
   }

   public final void dispatchAddFinished(RecyclerView.ViewHolder var1) {
      this.onAddFinished(var1);
      this.dispatchAnimationFinished(var1);
   }

   public final void dispatchAddStarting(RecyclerView.ViewHolder var1) {
      this.onAddStarting(var1);
   }

   public final void dispatchChangeFinished(RecyclerView.ViewHolder var1, boolean var2) {
      this.onChangeFinished(var1, var2);
      this.dispatchAnimationFinished(var1);
   }

   public final void dispatchChangeStarting(RecyclerView.ViewHolder var1, boolean var2) {
      this.onChangeStarting(var1, var2);
   }

   public final void dispatchMoveFinished(RecyclerView.ViewHolder var1) {
      this.onMoveFinished(var1);
      this.dispatchAnimationFinished(var1);
   }

   public final void dispatchMoveStarting(RecyclerView.ViewHolder var1) {
      this.onMoveStarting(var1);
   }

   public final void dispatchRemoveFinished(RecyclerView.ViewHolder var1) {
      this.onRemoveFinished(var1);
      this.dispatchAnimationFinished(var1);
   }

   public final void dispatchRemoveStarting(RecyclerView.ViewHolder var1) {
      this.onRemoveStarting(var1);
   }

   public boolean getSupportsChangeAnimations() {
      return this.mSupportsChangeAnimations;
   }

   public void onAddFinished(RecyclerView.ViewHolder var1) {
   }

   public void onAddStarting(RecyclerView.ViewHolder var1) {
   }

   public void onChangeFinished(RecyclerView.ViewHolder var1, boolean var2) {
   }

   public void onChangeStarting(RecyclerView.ViewHolder var1, boolean var2) {
   }

   public void onMoveFinished(RecyclerView.ViewHolder var1) {
   }

   public void onMoveStarting(RecyclerView.ViewHolder var1) {
   }

   public void onRemoveFinished(RecyclerView.ViewHolder var1) {
   }

   public void onRemoveStarting(RecyclerView.ViewHolder var1) {
   }

   public void setSupportsChangeAnimations(boolean var1) {
      this.mSupportsChangeAnimations = var1;
   }
}
