package androidx.recyclerview.widget;

import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdapterHelper implements OpReorderer.Callback {
   private static final boolean DEBUG = false;
   static final int POSITION_TYPE_INVISIBLE = 0;
   static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
   private static final String TAG = "AHT";
   final AdapterHelper.Callback mCallback;
   final boolean mDisableRecycler;
   private int mExistingUpdateTypes;
   Runnable mOnItemProcessedCallback;
   final OpReorderer mOpReorderer;
   final ArrayList mPendingUpdates;
   final ArrayList mPostponedList;
   private Pools.Pool mUpdateOpPool;

   AdapterHelper(AdapterHelper.Callback var1) {
      this(var1, false);
   }

   AdapterHelper(AdapterHelper.Callback var1, boolean var2) {
      this.mUpdateOpPool = new Pools.SimplePool(30);
      this.mPendingUpdates = new ArrayList();
      this.mPostponedList = new ArrayList();
      this.mExistingUpdateTypes = 0;
      this.mCallback = var1;
      this.mDisableRecycler = var2;
      this.mOpReorderer = new OpReorderer(this);
   }

   private void applyAdd(AdapterHelper.UpdateOp var1) {
      this.postponeAndUpdateViewHolders(var1);
   }

   private void applyMove(AdapterHelper.UpdateOp var1) {
      this.postponeAndUpdateViewHolders(var1);
   }

   private void applyRemove(AdapterHelper.UpdateOp var1) {
      int var8 = var1.positionStart;
      int var6 = 0;
      int var5 = var1.positionStart + var1.itemCount;
      byte var7 = -1;

      int var11;
      for(int var2 = var1.positionStart; var2 < var5; var2 = var11) {
         boolean var3 = false;
         boolean var4 = false;
         byte var10;
         if (this.mCallback.findViewHolder(var2) == null && !this.canFindInPreLayout(var2)) {
            if (var7 == 1) {
               this.postponeAndUpdateViewHolders(this.obtainUpdateOp(2, var8, var6, (Object)null));
               var4 = true;
            }

            var10 = 0;
         } else {
            if (var7 == 0) {
               this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(2, var8, var6, (Object)null));
               var3 = true;
            }

            byte var12 = 1;
            var4 = var3;
            var10 = var12;
         }

         if (var4) {
            var11 = var2 - var6;
            var5 -= var6;
            var2 = 1;
         } else {
            ++var6;
            var11 = var2;
            var2 = var6;
         }

         ++var11;
         var6 = var2;
         var7 = var10;
      }

      AdapterHelper.UpdateOp var9 = var1;
      if (var6 != var1.itemCount) {
         this.recycleUpdateOp(var1);
         var9 = this.obtainUpdateOp(2, var8, var6, (Object)null);
      }

      if (var7 == 0) {
         this.dispatchAndUpdateViewHolders(var9);
      } else {
         this.postponeAndUpdateViewHolders(var9);
      }
   }

   private void applyUpdate(AdapterHelper.UpdateOp var1) {
      int var3 = var1.positionStart;
      int var4 = 0;
      int var9 = var1.positionStart;
      int var10 = var1.itemCount;
      byte var8 = -1;

      byte var5;
      for(int var2 = var1.positionStart; var2 < var9 + var10; var8 = var5) {
         int var6;
         int var7;
         if (this.mCallback.findViewHolder(var2) == null && !this.canFindInPreLayout(var2)) {
            var6 = var3;
            int var13 = var4;
            if (var8 == 1) {
               this.postponeAndUpdateViewHolders(this.obtainUpdateOp(4, var3, var4, var1.payload));
               var13 = 0;
               var6 = var2;
            }

            byte var12 = 0;
            var3 = var6;
            var7 = var13;
            var5 = var12;
         } else {
            var6 = var3;
            var7 = var4;
            if (var8 == 0) {
               this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(4, var3, var4, var1.payload));
               var7 = 0;
               var6 = var2;
            }

            var5 = 1;
            var3 = var6;
         }

         var4 = var7 + 1;
         ++var2;
      }

      AdapterHelper.UpdateOp var11 = var1;
      if (var4 != var1.itemCount) {
         Object var14 = var1.payload;
         this.recycleUpdateOp(var1);
         var11 = this.obtainUpdateOp(4, var3, var4, var14);
      }

      if (var8 == 0) {
         this.dispatchAndUpdateViewHolders(var11);
      } else {
         this.postponeAndUpdateViewHolders(var11);
      }
   }

   private boolean canFindInPreLayout(int var1) {
      int var4 = this.mPostponedList.size();

      for(int var2 = 0; var2 < var4; ++var2) {
         AdapterHelper.UpdateOp var7 = (AdapterHelper.UpdateOp)this.mPostponedList.get(var2);
         if (var7.cmd == 8) {
            if (this.findPositionOffset(var7.itemCount, var2 + 1) == var1) {
               return true;
            }
         } else if (var7.cmd == 1) {
            int var5 = var7.positionStart;
            int var6 = var7.itemCount;

            for(int var3 = var7.positionStart; var3 < var5 + var6; ++var3) {
               if (this.findPositionOffset(var3, var2 + 1) == var1) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void dispatchAndUpdateViewHolders(AdapterHelper.UpdateOp var1) {
      if (var1.cmd != 1 && var1.cmd != 8) {
         int var6 = this.updatePositionWithPostponed(var1.positionStart, var1.cmd);
         int var7 = 1;
         int var2 = var1.positionStart;
         int var3 = var1.cmd;
         byte var4;
         if (var3 != 2) {
            if (var3 != 4) {
               StringBuilder var12 = new StringBuilder();
               var12.append("op should be remove or update.");
               var12.append(var1);
               throw new IllegalArgumentException(var12.toString());
            }

            var4 = 1;
         } else {
            var4 = 0;
         }

         for(int var5 = 1; var5 < var1.itemCount; var7 = var3) {
            int var8 = this.updatePositionWithPostponed(var1.positionStart + var4 * var5, var1.cmd);
            boolean var13 = false;
            int var11 = var1.cmd;
            boolean var10 = false;
            boolean var9 = false;
            if (var11 != 2) {
               if (var11 == 4) {
                  var13 = var9;
                  if (var8 == var6 + 1) {
                     var13 = true;
                  }
               }
            } else {
               var13 = var10;
               if (var8 == var6) {
                  var13 = true;
               }
            }

            if (var13) {
               var3 = var7 + 1;
            } else {
               AdapterHelper.UpdateOp var15 = this.obtainUpdateOp(var1.cmd, var6, var7, var1.payload);
               this.dispatchFirstPassAndUpdateViewHolders(var15, var2);
               this.recycleUpdateOp(var15);
               var3 = var2;
               if (var1.cmd == 4) {
                  var3 = var2 + var7;
               }

               var6 = var8;
               byte var14 = 1;
               var2 = var3;
               var3 = var14;
            }

            ++var5;
         }

         Object var16 = var1.payload;
         this.recycleUpdateOp(var1);
         if (var7 > 0) {
            var1 = this.obtainUpdateOp(var1.cmd, var6, var7, var16);
            this.dispatchFirstPassAndUpdateViewHolders(var1, var2);
            this.recycleUpdateOp(var1);
         }

      } else {
         throw new IllegalArgumentException("should not dispatch add or move for pre layout");
      }
   }

   private void postponeAndUpdateViewHolders(AdapterHelper.UpdateOp var1) {
      this.mPostponedList.add(var1);
      int var2 = var1.cmd;
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 4) {
               if (var2 == 8) {
                  this.mCallback.offsetPositionsForMove(var1.positionStart, var1.itemCount);
               } else {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Unknown update op type for ");
                  var3.append(var1);
                  throw new IllegalArgumentException(var3.toString());
               }
            } else {
               this.mCallback.markViewHoldersUpdated(var1.positionStart, var1.itemCount, var1.payload);
            }
         } else {
            this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(var1.positionStart, var1.itemCount);
         }
      } else {
         this.mCallback.offsetPositionsForAdd(var1.positionStart, var1.itemCount);
      }
   }

   private int updatePositionWithPostponed(int var1, int var2) {
      int var4 = this.mPostponedList.size() - 1;

      int var3;
      AdapterHelper.UpdateOp var6;
      for(var3 = var1; var4 >= 0; var3 = var1) {
         var6 = (AdapterHelper.UpdateOp)this.mPostponedList.get(var4);
         if (var6.cmd == 8) {
            int var5;
            if (var6.positionStart < var6.itemCount) {
               var1 = var6.positionStart;
               var5 = var6.itemCount;
            } else {
               var1 = var6.itemCount;
               var5 = var6.positionStart;
            }

            if (var3 >= var1 && var3 <= var5) {
               if (var1 == var6.positionStart) {
                  if (var2 == 1) {
                     ++var6.itemCount;
                  } else if (var2 == 2) {
                     --var6.itemCount;
                  }

                  var1 = var3 + 1;
               } else {
                  if (var2 == 1) {
                     ++var6.positionStart;
                  } else if (var2 == 2) {
                     --var6.positionStart;
                  }

                  var1 = var3 - 1;
               }
            } else {
               var1 = var3;
               if (var3 < var6.positionStart) {
                  if (var2 == 1) {
                     ++var6.positionStart;
                     ++var6.itemCount;
                     var1 = var3;
                  } else {
                     var1 = var3;
                     if (var2 == 2) {
                        --var6.positionStart;
                        --var6.itemCount;
                        var1 = var3;
                     }
                  }
               }
            }
         } else if (var6.positionStart <= var3) {
            if (var6.cmd == 1) {
               var1 = var3 - var6.itemCount;
            } else {
               var1 = var3;
               if (var6.cmd == 2) {
                  var1 = var3 + var6.itemCount;
               }
            }
         } else if (var2 == 1) {
            ++var6.positionStart;
            var1 = var3;
         } else {
            var1 = var3;
            if (var2 == 2) {
               --var6.positionStart;
               var1 = var3;
            }
         }

         --var4;
      }

      for(var1 = this.mPostponedList.size() - 1; var1 >= 0; --var1) {
         var6 = (AdapterHelper.UpdateOp)this.mPostponedList.get(var1);
         if (var6.cmd == 8) {
            if (var6.itemCount == var6.positionStart || var6.itemCount < 0) {
               this.mPostponedList.remove(var1);
               this.recycleUpdateOp(var6);
            }
         } else if (var6.itemCount <= 0) {
            this.mPostponedList.remove(var1);
            this.recycleUpdateOp(var6);
         }
      }

      return var3;
   }

   AdapterHelper addUpdateOp(AdapterHelper.UpdateOp... var1) {
      Collections.addAll(this.mPendingUpdates, var1);
      return this;
   }

   public int applyPendingUpdatesToPosition(int var1) {
      int var5 = this.mPendingUpdates.size();
      int var4 = 0;

      int var2;
      for(var2 = var1; var4 < var5; var2 = var1) {
         AdapterHelper.UpdateOp var6 = (AdapterHelper.UpdateOp)this.mPendingUpdates.get(var4);
         var1 = var6.cmd;
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 8) {
                  var1 = var2;
               } else if (var6.positionStart == var2) {
                  var1 = var6.itemCount;
               } else {
                  int var3 = var2;
                  if (var6.positionStart < var2) {
                     var3 = var2 - 1;
                  }

                  var1 = var3;
                  if (var6.itemCount <= var3) {
                     var1 = var3 + 1;
                  }
               }
            } else {
               var1 = var2;
               if (var6.positionStart <= var2) {
                  if (var6.positionStart + var6.itemCount > var2) {
                     return -1;
                  }

                  var1 = var2 - var6.itemCount;
               }
            }
         } else {
            var1 = var2;
            if (var6.positionStart <= var2) {
               var1 = var2 + var6.itemCount;
            }
         }

         ++var4;
      }

      return var2;
   }

   void consumePostponedUpdates() {
      int var2 = this.mPostponedList.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         this.mCallback.onDispatchSecondPass((AdapterHelper.UpdateOp)this.mPostponedList.get(var1));
      }

      this.recycleUpdateOpsAndClearList(this.mPostponedList);
      this.mExistingUpdateTypes = 0;
   }

   void consumeUpdatesInOnePass() {
      this.consumePostponedUpdates();
      int var2 = this.mPendingUpdates.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         AdapterHelper.UpdateOp var4 = (AdapterHelper.UpdateOp)this.mPendingUpdates.get(var1);
         int var3 = var4.cmd;
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 4) {
                  if (var3 == 8) {
                     this.mCallback.onDispatchSecondPass(var4);
                     this.mCallback.offsetPositionsForMove(var4.positionStart, var4.itemCount);
                  }
               } else {
                  this.mCallback.onDispatchSecondPass(var4);
                  this.mCallback.markViewHoldersUpdated(var4.positionStart, var4.itemCount, var4.payload);
               }
            } else {
               this.mCallback.onDispatchSecondPass(var4);
               this.mCallback.offsetPositionsForRemovingInvisible(var4.positionStart, var4.itemCount);
            }
         } else {
            this.mCallback.onDispatchSecondPass(var4);
            this.mCallback.offsetPositionsForAdd(var4.positionStart, var4.itemCount);
         }

         Runnable var5 = this.mOnItemProcessedCallback;
         if (var5 != null) {
            var5.run();
         }
      }

      this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
      this.mExistingUpdateTypes = 0;
   }

   void dispatchFirstPassAndUpdateViewHolders(AdapterHelper.UpdateOp var1, int var2) {
      this.mCallback.onDispatchFirstPass(var1);
      int var3 = var1.cmd;
      if (var3 != 2) {
         if (var3 == 4) {
            this.mCallback.markViewHoldersUpdated(var2, var1.itemCount, var1.payload);
         } else {
            throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
         }
      } else {
         this.mCallback.offsetPositionsForRemovingInvisible(var2, var1.itemCount);
      }
   }

   int findPositionOffset(int var1) {
      return this.findPositionOffset(var1, 0);
   }

   int findPositionOffset(int var1, int var2) {
      int var5 = this.mPostponedList.size();
      int var4 = var2;

      for(var2 = var1; var4 < var5; var2 = var1) {
         AdapterHelper.UpdateOp var6 = (AdapterHelper.UpdateOp)this.mPostponedList.get(var4);
         if (var6.cmd == 8) {
            if (var6.positionStart == var2) {
               var1 = var6.itemCount;
            } else {
               int var3 = var2;
               if (var6.positionStart < var2) {
                  var3 = var2 - 1;
               }

               var1 = var3;
               if (var6.itemCount <= var3) {
                  var1 = var3 + 1;
               }
            }
         } else {
            var1 = var2;
            if (var6.positionStart <= var2) {
               if (var6.cmd == 2) {
                  if (var2 < var6.positionStart + var6.itemCount) {
                     return -1;
                  }

                  var1 = var2 - var6.itemCount;
               } else {
                  var1 = var2;
                  if (var6.cmd == 1) {
                     var1 = var2 + var6.itemCount;
                  }
               }
            }
         }

         ++var4;
      }

      return var2;
   }

   boolean hasAnyUpdateTypes(int var1) {
      return (this.mExistingUpdateTypes & var1) != 0;
   }

   boolean hasPendingUpdates() {
      return this.mPendingUpdates.size() > 0;
   }

   boolean hasUpdates() {
      return !this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty();
   }

   public AdapterHelper.UpdateOp obtainUpdateOp(int var1, int var2, int var3, Object var4) {
      AdapterHelper.UpdateOp var5 = (AdapterHelper.UpdateOp)this.mUpdateOpPool.acquire();
      if (var5 == null) {
         return new AdapterHelper.UpdateOp(var1, var2, var3, var4);
      } else {
         var5.cmd = var1;
         var5.positionStart = var2;
         var5.itemCount = var3;
         var5.payload = var4;
         return var5;
      }
   }

   boolean onItemRangeChanged(int var1, int var2, Object var3) {
      boolean var4 = false;
      if (var2 < 1) {
         return false;
      } else {
         this.mPendingUpdates.add(this.obtainUpdateOp(4, var1, var2, var3));
         this.mExistingUpdateTypes |= 4;
         if (this.mPendingUpdates.size() == 1) {
            var4 = true;
         }

         return var4;
      }
   }

   boolean onItemRangeInserted(int var1, int var2) {
      boolean var3 = false;
      if (var2 < 1) {
         return false;
      } else {
         this.mPendingUpdates.add(this.obtainUpdateOp(1, var1, var2, (Object)null));
         this.mExistingUpdateTypes |= 1;
         if (this.mPendingUpdates.size() == 1) {
            var3 = true;
         }

         return var3;
      }
   }

   boolean onItemRangeMoved(int var1, int var2, int var3) {
      boolean var4 = false;
      if (var1 == var2) {
         return false;
      } else if (var3 == 1) {
         this.mPendingUpdates.add(this.obtainUpdateOp(8, var1, var2, (Object)null));
         this.mExistingUpdateTypes |= 8;
         if (this.mPendingUpdates.size() == 1) {
            var4 = true;
         }

         return var4;
      } else {
         throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
      }
   }

   boolean onItemRangeRemoved(int var1, int var2) {
      boolean var3 = false;
      if (var2 < 1) {
         return false;
      } else {
         this.mPendingUpdates.add(this.obtainUpdateOp(2, var1, var2, (Object)null));
         this.mExistingUpdateTypes |= 2;
         if (this.mPendingUpdates.size() == 1) {
            var3 = true;
         }

         return var3;
      }
   }

   void preProcess() {
      this.mOpReorderer.reorderOps(this.mPendingUpdates);
      int var2 = this.mPendingUpdates.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         AdapterHelper.UpdateOp var4 = (AdapterHelper.UpdateOp)this.mPendingUpdates.get(var1);
         int var3 = var4.cmd;
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 4) {
                  if (var3 == 8) {
                     this.applyMove(var4);
                  }
               } else {
                  this.applyUpdate(var4);
               }
            } else {
               this.applyRemove(var4);
            }
         } else {
            this.applyAdd(var4);
         }

         Runnable var5 = this.mOnItemProcessedCallback;
         if (var5 != null) {
            var5.run();
         }
      }

      this.mPendingUpdates.clear();
   }

   public void recycleUpdateOp(AdapterHelper.UpdateOp var1) {
      if (!this.mDisableRecycler) {
         var1.payload = null;
         this.mUpdateOpPool.release(var1);
      }

   }

   void recycleUpdateOpsAndClearList(List var1) {
      int var3 = var1.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         this.recycleUpdateOp((AdapterHelper.UpdateOp)var1.get(var2));
      }

      var1.clear();
   }

   void reset() {
      this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
      this.recycleUpdateOpsAndClearList(this.mPostponedList);
      this.mExistingUpdateTypes = 0;
   }

   interface Callback {
      RecyclerView.ViewHolder findViewHolder(int var1);

      void markViewHoldersUpdated(int var1, int var2, Object var3);

      void offsetPositionsForAdd(int var1, int var2);

      void offsetPositionsForMove(int var1, int var2);

      void offsetPositionsForRemovingInvisible(int var1, int var2);

      void offsetPositionsForRemovingLaidOutOrNewView(int var1, int var2);

      void onDispatchFirstPass(AdapterHelper.UpdateOp var1);

      void onDispatchSecondPass(AdapterHelper.UpdateOp var1);
   }

   static class UpdateOp {
      static final int ADD = 1;
      static final int MOVE = 8;
      static final int POOL_SIZE = 30;
      static final int REMOVE = 2;
      static final int UPDATE = 4;
      int cmd;
      int itemCount;
      Object payload;
      int positionStart;

      UpdateOp(int var1, int var2, int var3, Object var4) {
         this.cmd = var1;
         this.positionStart = var2;
         this.itemCount = var3;
         this.payload = var4;
      }

      String cmdToString() {
         int var1 = this.cmd;
         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 4) {
                  return var1 != 8 ? "??" : "mv";
               } else {
                  return "up";
               }
            } else {
               return "rm";
            }
         } else {
            return "add";
         }
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null) {
            if (this.getClass() != var1.getClass()) {
               return false;
            } else {
               AdapterHelper.UpdateOp var4 = (AdapterHelper.UpdateOp)var1;
               int var2 = this.cmd;
               if (var2 != var4.cmd) {
                  return false;
               } else if (var2 == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == var4.positionStart && this.positionStart == var4.itemCount) {
                  return true;
               } else if (this.itemCount != var4.itemCount) {
                  return false;
               } else if (this.positionStart != var4.positionStart) {
                  return false;
               } else {
                  Object var3 = this.payload;
                  if (var3 != null) {
                     if (!var3.equals(var4.payload)) {
                        return false;
                     }
                  } else if (var4.payload != null) {
                     return false;
                  }

                  return true;
               }
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append("[");
         var1.append(this.cmdToString());
         var1.append(",s:");
         var1.append(this.positionStart);
         var1.append("c:");
         var1.append(this.itemCount);
         var1.append(",p:");
         var1.append(this.payload);
         var1.append("]");
         return var1.toString();
      }
   }
}
