package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewPropertyAnimator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultItemAnimator extends SimpleItemAnimator {
   private static final boolean DEBUG = false;
   private static TimeInterpolator sDefaultInterpolator;
   ArrayList mAddAnimations = new ArrayList();
   ArrayList mAdditionsList = new ArrayList();
   ArrayList mChangeAnimations = new ArrayList();
   ArrayList mChangesList = new ArrayList();
   ArrayList mMoveAnimations = new ArrayList();
   ArrayList mMovesList = new ArrayList();
   private ArrayList mPendingAdditions = new ArrayList();
   private ArrayList mPendingChanges = new ArrayList();
   private ArrayList mPendingMoves = new ArrayList();
   private ArrayList mPendingRemovals = new ArrayList();
   ArrayList mRemoveAnimations = new ArrayList();

   private void animateRemoveImpl(final RecyclerView.ViewHolder var1) {
      final View var2 = var1.itemView;
      final ViewPropertyAnimator var3 = var2.animate();
      this.mRemoveAnimations.add(var1);
      var3.setDuration(this.getRemoveDuration()).alpha(0.0F).setListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            var3.setListener((AnimatorListener)null);
            var2.setAlpha(1.0F);
            DefaultItemAnimator.this.dispatchRemoveFinished(var1);
            DefaultItemAnimator.this.mRemoveAnimations.remove(var1);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
         }

         public void onAnimationStart(Animator var1x) {
            DefaultItemAnimator.this.dispatchRemoveStarting(var1);
         }
      }).start();
   }

   private void endChangeAnimation(List var1, RecyclerView.ViewHolder var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         DefaultItemAnimator.ChangeInfo var4 = (DefaultItemAnimator.ChangeInfo)var1.get(var3);
         if (this.endChangeAnimationIfNecessary(var4, var2) && var4.oldHolder == null && var4.newHolder == null) {
            var1.remove(var4);
         }
      }

   }

   private void endChangeAnimationIfNecessary(DefaultItemAnimator.ChangeInfo var1) {
      if (var1.oldHolder != null) {
         this.endChangeAnimationIfNecessary(var1, var1.oldHolder);
      }

      if (var1.newHolder != null) {
         this.endChangeAnimationIfNecessary(var1, var1.newHolder);
      }
   }

   private boolean endChangeAnimationIfNecessary(DefaultItemAnimator.ChangeInfo var1, RecyclerView.ViewHolder var2) {
      boolean var3 = false;
      if (var1.newHolder == var2) {
         var1.newHolder = null;
      } else {
         if (var1.oldHolder != var2) {
            return false;
         }

         var1.oldHolder = null;
         var3 = true;
      }

      var2.itemView.setAlpha(1.0F);
      var2.itemView.setTranslationX(0.0F);
      var2.itemView.setTranslationY(0.0F);
      this.dispatchChangeFinished(var2, var3);
      return true;
   }

   private void resetAnimation(RecyclerView.ViewHolder var1) {
      if (sDefaultInterpolator == null) {
         sDefaultInterpolator = (new ValueAnimator()).getInterpolator();
      }

      var1.itemView.animate().setInterpolator(sDefaultInterpolator);
      this.endAnimation(var1);
   }

   public boolean animateAdd(RecyclerView.ViewHolder var1) {
      this.resetAnimation(var1);
      var1.itemView.setAlpha(0.0F);
      this.mPendingAdditions.add(var1);
      return true;
   }

   void animateAddImpl(final RecyclerView.ViewHolder var1) {
      final View var2 = var1.itemView;
      final ViewPropertyAnimator var3 = var2.animate();
      this.mAddAnimations.add(var1);
      var3.alpha(1.0F).setDuration(this.getAddDuration()).setListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            var2.setAlpha(1.0F);
         }

         public void onAnimationEnd(Animator var1x) {
            var3.setListener((AnimatorListener)null);
            DefaultItemAnimator.this.dispatchAddFinished(var1);
            DefaultItemAnimator.this.mAddAnimations.remove(var1);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
         }

         public void onAnimationStart(Animator var1x) {
            DefaultItemAnimator.this.dispatchAddStarting(var1);
         }
      }).start();
   }

   public boolean animateChange(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, int var3, int var4, int var5, int var6) {
      if (var1 == var2) {
         return this.animateMove(var1, var3, var4, var5, var6);
      } else {
         float var7 = var1.itemView.getTranslationX();
         float var8 = var1.itemView.getTranslationY();
         float var9 = var1.itemView.getAlpha();
         this.resetAnimation(var1);
         int var10 = (int)((float)(var5 - var3) - var7);
         int var11 = (int)((float)(var6 - var4) - var8);
         var1.itemView.setTranslationX(var7);
         var1.itemView.setTranslationY(var8);
         var1.itemView.setAlpha(var9);
         if (var2 != null) {
            this.resetAnimation(var2);
            var2.itemView.setTranslationX((float)(-var10));
            var2.itemView.setTranslationY((float)(-var11));
            var2.itemView.setAlpha(0.0F);
         }

         this.mPendingChanges.add(new DefaultItemAnimator.ChangeInfo(var1, var2, var3, var4, var5, var6));
         return true;
      }
   }

   void animateChangeImpl(final DefaultItemAnimator.ChangeInfo var1) {
      RecyclerView.ViewHolder var2 = var1.oldHolder;
      final View var3 = null;
      final View var5;
      if (var2 == null) {
         var5 = null;
      } else {
         var5 = var2.itemView;
      }

      RecyclerView.ViewHolder var4 = var1.newHolder;
      if (var4 != null) {
         var3 = var4.itemView;
      }

      if (var5 != null) {
         final ViewPropertyAnimator var7 = var5.animate().setDuration(this.getChangeDuration());
         this.mChangeAnimations.add(var1.oldHolder);
         var7.translationX((float)(var1.toX - var1.fromX));
         var7.translationY((float)(var1.toY - var1.fromY));
         var7.alpha(0.0F).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               var7.setListener((AnimatorListener)null);
               var5.setAlpha(1.0F);
               var5.setTranslationX(0.0F);
               var5.setTranslationY(0.0F);
               DefaultItemAnimator.this.dispatchChangeFinished(var1.oldHolder, true);
               DefaultItemAnimator.this.mChangeAnimations.remove(var1.oldHolder);
               DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator var1x) {
               DefaultItemAnimator.this.dispatchChangeStarting(var1.oldHolder, true);
            }
         }).start();
      }

      if (var3 != null) {
         final ViewPropertyAnimator var6 = var3.animate();
         this.mChangeAnimations.add(var1.newHolder);
         var6.translationX(0.0F).translationY(0.0F).setDuration(this.getChangeDuration()).alpha(1.0F).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               var6.setListener((AnimatorListener)null);
               var3.setAlpha(1.0F);
               var3.setTranslationX(0.0F);
               var3.setTranslationY(0.0F);
               DefaultItemAnimator.this.dispatchChangeFinished(var1.newHolder, false);
               DefaultItemAnimator.this.mChangeAnimations.remove(var1.newHolder);
               DefaultItemAnimator.this.dispatchFinishedWhenDone();
            }

            public void onAnimationStart(Animator var1x) {
               DefaultItemAnimator.this.dispatchChangeStarting(var1.newHolder, false);
            }
         }).start();
      }
   }

   public boolean animateMove(RecyclerView.ViewHolder var1, int var2, int var3, int var4, int var5) {
      View var8 = var1.itemView;
      var2 += (int)var1.itemView.getTranslationX();
      var3 += (int)var1.itemView.getTranslationY();
      this.resetAnimation(var1);
      int var6 = var4 - var2;
      int var7 = var5 - var3;
      if (var6 == 0 && var7 == 0) {
         this.dispatchMoveFinished(var1);
         return false;
      } else {
         if (var6 != 0) {
            var8.setTranslationX((float)(-var6));
         }

         if (var7 != 0) {
            var8.setTranslationY((float)(-var7));
         }

         this.mPendingMoves.add(new DefaultItemAnimator.MoveInfo(var1, var2, var3, var4, var5));
         return true;
      }
   }

   void animateMoveImpl(final RecyclerView.ViewHolder var1, final int var2, final int var3, int var4, int var5) {
      final View var6 = var1.itemView;
      var2 = var4 - var2;
      var3 = var5 - var3;
      if (var2 != 0) {
         var6.animate().translationX(0.0F);
      }

      if (var3 != 0) {
         var6.animate().translationY(0.0F);
      }

      final ViewPropertyAnimator var7 = var6.animate();
      this.mMoveAnimations.add(var1);
      var7.setDuration(this.getMoveDuration()).setListener(new AnimatorListenerAdapter() {
         public void onAnimationCancel(Animator var1x) {
            if (var2 != 0) {
               var6.setTranslationX(0.0F);
            }

            if (var3 != 0) {
               var6.setTranslationY(0.0F);
            }
         }

         public void onAnimationEnd(Animator var1x) {
            var7.setListener((AnimatorListener)null);
            DefaultItemAnimator.this.dispatchMoveFinished(var1);
            DefaultItemAnimator.this.mMoveAnimations.remove(var1);
            DefaultItemAnimator.this.dispatchFinishedWhenDone();
         }

         public void onAnimationStart(Animator var1x) {
            DefaultItemAnimator.this.dispatchMoveStarting(var1);
         }
      }).start();
   }

   public boolean animateRemove(RecyclerView.ViewHolder var1) {
      this.resetAnimation(var1);
      this.mPendingRemovals.add(var1);
      return true;
   }

   public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder var1, @NonNull List var2) {
      return !var2.isEmpty() || super.canReuseUpdatedViewHolder(var1, var2);
   }

   void cancelAll(List var1) {
      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         ((RecyclerView.ViewHolder)var1.get(var2)).itemView.animate().cancel();
      }

   }

   void dispatchFinishedWhenDone() {
      if (!this.isRunning()) {
         this.dispatchAnimationsFinished();
      }
   }

   public void endAnimation(RecyclerView.ViewHolder var1) {
      View var4 = var1.itemView;
      var4.animate().cancel();

      int var2;
      for(var2 = this.mPendingMoves.size() - 1; var2 >= 0; --var2) {
         if (((DefaultItemAnimator.MoveInfo)this.mPendingMoves.get(var2)).holder == var1) {
            var4.setTranslationY(0.0F);
            var4.setTranslationX(0.0F);
            this.dispatchMoveFinished(var1);
            this.mPendingMoves.remove(var2);
         }
      }

      this.endChangeAnimation(this.mPendingChanges, var1);
      if (this.mPendingRemovals.remove(var1)) {
         var4.setAlpha(1.0F);
         this.dispatchRemoveFinished(var1);
      }

      if (this.mPendingAdditions.remove(var1)) {
         var4.setAlpha(1.0F);
         this.dispatchAddFinished(var1);
      }

      ArrayList var5;
      for(var2 = this.mChangesList.size() - 1; var2 >= 0; --var2) {
         var5 = (ArrayList)this.mChangesList.get(var2);
         this.endChangeAnimation(var5, var1);
         if (var5.isEmpty()) {
            this.mChangesList.remove(var2);
         }
      }

      for(var2 = this.mMovesList.size() - 1; var2 >= 0; --var2) {
         var5 = (ArrayList)this.mMovesList.get(var2);

         for(int var3 = var5.size() - 1; var3 >= 0; --var3) {
            if (((DefaultItemAnimator.MoveInfo)var5.get(var3)).holder == var1) {
               var4.setTranslationY(0.0F);
               var4.setTranslationX(0.0F);
               this.dispatchMoveFinished(var1);
               var5.remove(var3);
               if (var5.isEmpty()) {
                  this.mMovesList.remove(var2);
               }
               break;
            }
         }
      }

      for(var2 = this.mAdditionsList.size() - 1; var2 >= 0; --var2) {
         var5 = (ArrayList)this.mAdditionsList.get(var2);
         if (var5.remove(var1)) {
            var4.setAlpha(1.0F);
            this.dispatchAddFinished(var1);
            if (var5.isEmpty()) {
               this.mAdditionsList.remove(var2);
            }
         }
      }

      this.mRemoveAnimations.remove(var1);
      this.mAddAnimations.remove(var1);
      this.mChangeAnimations.remove(var1);
      this.mMoveAnimations.remove(var1);
      this.dispatchFinishedWhenDone();
   }

   public void endAnimations() {
      int var1;
      for(var1 = this.mPendingMoves.size() - 1; var1 >= 0; --var1) {
         DefaultItemAnimator.MoveInfo var3 = (DefaultItemAnimator.MoveInfo)this.mPendingMoves.get(var1);
         View var4 = var3.holder.itemView;
         var4.setTranslationY(0.0F);
         var4.setTranslationX(0.0F);
         this.dispatchMoveFinished(var3.holder);
         this.mPendingMoves.remove(var1);
      }

      for(var1 = this.mPendingRemovals.size() - 1; var1 >= 0; --var1) {
         this.dispatchRemoveFinished((RecyclerView.ViewHolder)this.mPendingRemovals.get(var1));
         this.mPendingRemovals.remove(var1);
      }

      for(var1 = this.mPendingAdditions.size() - 1; var1 >= 0; --var1) {
         RecyclerView.ViewHolder var6 = (RecyclerView.ViewHolder)this.mPendingAdditions.get(var1);
         var6.itemView.setAlpha(1.0F);
         this.dispatchAddFinished(var6);
         this.mPendingAdditions.remove(var1);
      }

      for(var1 = this.mPendingChanges.size() - 1; var1 >= 0; --var1) {
         this.endChangeAnimationIfNecessary((DefaultItemAnimator.ChangeInfo)this.mPendingChanges.get(var1));
      }

      this.mPendingChanges.clear();
      if (this.isRunning()) {
         int var2;
         ArrayList var7;
         for(var1 = this.mMovesList.size() - 1; var1 >= 0; --var1) {
            var7 = (ArrayList)this.mMovesList.get(var1);

            for(var2 = var7.size() - 1; var2 >= 0; --var2) {
               DefaultItemAnimator.MoveInfo var8 = (DefaultItemAnimator.MoveInfo)var7.get(var2);
               View var5 = var8.holder.itemView;
               var5.setTranslationY(0.0F);
               var5.setTranslationX(0.0F);
               this.dispatchMoveFinished(var8.holder);
               var7.remove(var2);
               if (var7.isEmpty()) {
                  this.mMovesList.remove(var7);
               }
            }
         }

         for(var1 = this.mAdditionsList.size() - 1; var1 >= 0; --var1) {
            var7 = (ArrayList)this.mAdditionsList.get(var1);

            for(var2 = var7.size() - 1; var2 >= 0; --var2) {
               RecyclerView.ViewHolder var9 = (RecyclerView.ViewHolder)var7.get(var2);
               var9.itemView.setAlpha(1.0F);
               this.dispatchAddFinished(var9);
               var7.remove(var2);
               if (var7.isEmpty()) {
                  this.mAdditionsList.remove(var7);
               }
            }
         }

         for(var1 = this.mChangesList.size() - 1; var1 >= 0; --var1) {
            var7 = (ArrayList)this.mChangesList.get(var1);

            for(var2 = var7.size() - 1; var2 >= 0; --var2) {
               this.endChangeAnimationIfNecessary((DefaultItemAnimator.ChangeInfo)var7.get(var2));
               if (var7.isEmpty()) {
                  this.mChangesList.remove(var7);
               }
            }
         }

         this.cancelAll(this.mRemoveAnimations);
         this.cancelAll(this.mMoveAnimations);
         this.cancelAll(this.mAddAnimations);
         this.cancelAll(this.mChangeAnimations);
         this.dispatchAnimationsFinished();
      }
   }

   public boolean isRunning() {
      return !this.mPendingAdditions.isEmpty() || !this.mPendingChanges.isEmpty() || !this.mPendingMoves.isEmpty() || !this.mPendingRemovals.isEmpty() || !this.mMoveAnimations.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mAddAnimations.isEmpty() || !this.mChangeAnimations.isEmpty() || !this.mMovesList.isEmpty() || !this.mAdditionsList.isEmpty() || !this.mChangesList.isEmpty();
   }

   public void runPendingAnimations() {
      boolean var1 = this.mPendingRemovals.isEmpty() ^ true;
      boolean var2 = this.mPendingMoves.isEmpty() ^ true;
      boolean var3 = this.mPendingChanges.isEmpty() ^ true;
      boolean var4 = this.mPendingAdditions.isEmpty() ^ true;
      if (var1 || var2 || var4 || var3) {
         Iterator var11 = this.mPendingRemovals.iterator();

         while(var11.hasNext()) {
            this.animateRemoveImpl((RecyclerView.ViewHolder)var11.next());
         }

         this.mPendingRemovals.clear();
         Runnable var12;
         final ArrayList var13;
         if (var2) {
            var13 = new ArrayList();
            var13.addAll(this.mPendingMoves);
            this.mMovesList.add(var13);
            this.mPendingMoves.clear();
            var12 = new Runnable() {
               public void run() {
                  Iterator var1 = var13.iterator();

                  while(var1.hasNext()) {
                     DefaultItemAnimator.MoveInfo var2 = (DefaultItemAnimator.MoveInfo)var1.next();
                     DefaultItemAnimator.this.animateMoveImpl(var2.holder, var2.fromX, var2.fromY, var2.toX, var2.toY);
                  }

                  var13.clear();
                  DefaultItemAnimator.this.mMovesList.remove(var13);
               }
            };
            if (var1) {
               ViewCompat.postOnAnimationDelayed(((DefaultItemAnimator.MoveInfo)var13.get(0)).holder.itemView, var12, this.getRemoveDuration());
            } else {
               var12.run();
            }
         }

         if (var3) {
            var13 = new ArrayList();
            var13.addAll(this.mPendingChanges);
            this.mChangesList.add(var13);
            this.mPendingChanges.clear();
            var12 = new Runnable() {
               public void run() {
                  Iterator var1 = var13.iterator();

                  while(var1.hasNext()) {
                     DefaultItemAnimator.ChangeInfo var2 = (DefaultItemAnimator.ChangeInfo)var1.next();
                     DefaultItemAnimator.this.animateChangeImpl(var2);
                  }

                  var13.clear();
                  DefaultItemAnimator.this.mChangesList.remove(var13);
               }
            };
            if (var1) {
               ViewCompat.postOnAnimationDelayed(((DefaultItemAnimator.ChangeInfo)var13.get(0)).oldHolder.itemView, var12, this.getRemoveDuration());
            } else {
               var12.run();
            }
         }

         if (var4) {
            var13 = new ArrayList();
            var13.addAll(this.mPendingAdditions);
            this.mAdditionsList.add(var13);
            this.mPendingAdditions.clear();
            var12 = new Runnable() {
               public void run() {
                  Iterator var1 = var13.iterator();

                  while(var1.hasNext()) {
                     RecyclerView.ViewHolder var2 = (RecyclerView.ViewHolder)var1.next();
                     DefaultItemAnimator.this.animateAddImpl(var2);
                  }

                  var13.clear();
                  DefaultItemAnimator.this.mAdditionsList.remove(var13);
               }
            };
            if (!var1 && !var2 && !var3) {
               var12.run();
            } else {
               long var9 = 0L;
               long var5;
               if (var1) {
                  var5 = this.getRemoveDuration();
               } else {
                  var5 = 0L;
               }

               long var7;
               if (var2) {
                  var7 = this.getMoveDuration();
               } else {
                  var7 = 0L;
               }

               if (var3) {
                  var9 = this.getChangeDuration();
               }

               var7 = Math.max(var7, var9);
               ViewCompat.postOnAnimationDelayed(((RecyclerView.ViewHolder)var13.get(0)).itemView, var12, var7 + var5);
            }
         }
      }
   }

   private static class ChangeInfo {
      public int fromX;
      public int fromY;
      public RecyclerView.ViewHolder newHolder;
      public RecyclerView.ViewHolder oldHolder;
      public int toX;
      public int toY;

      private ChangeInfo(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2) {
         this.oldHolder = var1;
         this.newHolder = var2;
      }

      ChangeInfo(RecyclerView.ViewHolder var1, RecyclerView.ViewHolder var2, int var3, int var4, int var5, int var6) {
         this(var1, var2);
         this.fromX = var3;
         this.fromY = var4;
         this.toX = var5;
         this.toY = var6;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("ChangeInfo{oldHolder=");
         var1.append(this.oldHolder);
         var1.append(", newHolder=");
         var1.append(this.newHolder);
         var1.append(", fromX=");
         var1.append(this.fromX);
         var1.append(", fromY=");
         var1.append(this.fromY);
         var1.append(", toX=");
         var1.append(this.toX);
         var1.append(", toY=");
         var1.append(this.toY);
         var1.append('}');
         return var1.toString();
      }
   }

   private static class MoveInfo {
      public int fromX;
      public int fromY;
      public RecyclerView.ViewHolder holder;
      public int toX;
      public int toY;

      MoveInfo(RecyclerView.ViewHolder var1, int var2, int var3, int var4, int var5) {
         this.holder = var1;
         this.fromX = var2;
         this.fromY = var3;
         this.toX = var4;
         this.toY = var5;
      }
   }
}
