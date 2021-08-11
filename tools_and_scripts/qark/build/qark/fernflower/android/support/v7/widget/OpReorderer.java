package android.support.v7.widget;

import java.util.List;

class OpReorderer {
   final OpReorderer.Callback mCallback;

   OpReorderer(OpReorderer.Callback var1) {
      this.mCallback = var1;
   }

   private int getLastMoveOutOfOrder(List var1) {
      boolean var3 = false;

      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         if (((AdapterHelper.UpdateOp)var1.get(var2)).cmd == 8) {
            if (var3) {
               return var2;
            }
         } else {
            var3 = true;
         }
      }

      return -1;
   }

   private void swapMoveAdd(List var1, int var2, AdapterHelper.UpdateOp var3, int var4, AdapterHelper.UpdateOp var5) {
      int var6 = 0;
      if (var3.itemCount < var5.positionStart) {
         var6 = 0 - 1;
      }

      if (var3.positionStart < var5.positionStart) {
         ++var6;
      }

      if (var5.positionStart <= var3.positionStart) {
         var3.positionStart += var5.itemCount;
      }

      if (var5.positionStart <= var3.itemCount) {
         var3.itemCount += var5.itemCount;
      }

      var5.positionStart += var6;
      var1.set(var2, var5);
      var1.set(var4, var3);
   }

   private void swapMoveOp(List var1, int var2, int var3) {
      AdapterHelper.UpdateOp var5 = (AdapterHelper.UpdateOp)var1.get(var2);
      AdapterHelper.UpdateOp var6 = (AdapterHelper.UpdateOp)var1.get(var3);
      int var4 = var6.cmd;
      if (var4 != 4) {
         switch(var4) {
         case 1:
            this.swapMoveAdd(var1, var2, var5, var3, var6);
            return;
         case 2:
            this.swapMoveRemove(var1, var2, var5, var3, var6);
            return;
         default:
         }
      } else {
         this.swapMoveUpdate(var1, var2, var5, var3, var6);
      }
   }

   void reorderOps(List var1) {
      while(true) {
         int var2 = this.getLastMoveOutOfOrder(var1);
         if (var2 == -1) {
            return;
         }

         this.swapMoveOp(var1, var2, var2 + 1);
      }
   }

   void swapMoveRemove(List var1, int var2, AdapterHelper.UpdateOp var3, int var4, AdapterHelper.UpdateOp var5) {
      AdapterHelper.UpdateOp var11 = null;
      boolean var6 = false;
      boolean var7;
      if (var3.positionStart < var3.itemCount) {
         var7 = false;
         if (var5.positionStart == var3.positionStart && var5.itemCount == var3.itemCount - var3.positionStart) {
            var6 = true;
         }
      } else {
         var7 = true;
         if (var5.positionStart == var3.itemCount + 1 && var5.itemCount == var3.positionStart - var3.itemCount) {
            var6 = true;
         }
      }

      if (var3.itemCount < var5.positionStart) {
         --var5.positionStart;
      } else if (var3.itemCount < var5.positionStart + var5.itemCount) {
         --var5.itemCount;
         var3.cmd = 2;
         var3.itemCount = 1;
         if (var5.itemCount == 0) {
            var1.remove(var4);
            this.mCallback.recycleUpdateOp(var5);
            return;
         }

         return;
      }

      if (var3.positionStart <= var5.positionStart) {
         ++var5.positionStart;
      } else if (var3.positionStart < var5.positionStart + var5.itemCount) {
         int var8 = var5.positionStart;
         int var9 = var5.itemCount;
         int var10 = var3.positionStart;
         var11 = this.mCallback.obtainUpdateOp(2, var3.positionStart + 1, var8 + var9 - var10, (Object)null);
         var5.itemCount = var3.positionStart - var5.positionStart;
      }

      if (var6) {
         var1.set(var2, var5);
         var1.remove(var4);
         this.mCallback.recycleUpdateOp(var3);
      } else {
         if (var7) {
            if (var11 != null) {
               if (var3.positionStart > var11.positionStart) {
                  var3.positionStart -= var11.itemCount;
               }

               if (var3.itemCount > var11.positionStart) {
                  var3.itemCount -= var11.itemCount;
               }
            }

            if (var3.positionStart > var5.positionStart) {
               var3.positionStart -= var5.itemCount;
            }

            if (var3.itemCount > var5.positionStart) {
               var3.itemCount -= var5.itemCount;
            }
         } else {
            if (var11 != null) {
               if (var3.positionStart >= var11.positionStart) {
                  var3.positionStart -= var11.itemCount;
               }

               if (var3.itemCount >= var11.positionStart) {
                  var3.itemCount -= var11.itemCount;
               }
            }

            if (var3.positionStart >= var5.positionStart) {
               var3.positionStart -= var5.itemCount;
            }

            if (var3.itemCount >= var5.positionStart) {
               var3.itemCount -= var5.itemCount;
            }
         }

         var1.set(var2, var5);
         if (var3.positionStart != var3.itemCount) {
            var1.set(var4, var3);
         } else {
            var1.remove(var4);
         }

         if (var11 != null) {
            var1.add(var2, var11);
         }
      }
   }

   void swapMoveUpdate(List var1, int var2, AdapterHelper.UpdateOp var3, int var4, AdapterHelper.UpdateOp var5) {
      AdapterHelper.UpdateOp var7 = null;
      AdapterHelper.UpdateOp var8 = null;
      if (var3.itemCount < var5.positionStart) {
         --var5.positionStart;
      } else if (var3.itemCount < var5.positionStart + var5.itemCount) {
         --var5.itemCount;
         var7 = this.mCallback.obtainUpdateOp(4, var3.positionStart, 1, var5.payload);
      }

      if (var3.positionStart <= var5.positionStart) {
         ++var5.positionStart;
      } else if (var3.positionStart < var5.positionStart + var5.itemCount) {
         int var6 = var5.positionStart + var5.itemCount - var3.positionStart;
         var8 = this.mCallback.obtainUpdateOp(4, var3.positionStart + 1, var6, var5.payload);
         var5.itemCount -= var6;
      }

      var1.set(var4, var3);
      if (var5.itemCount > 0) {
         var1.set(var2, var5);
      } else {
         var1.remove(var2);
         this.mCallback.recycleUpdateOp(var5);
      }

      if (var7 != null) {
         var1.add(var2, var7);
      }

      if (var8 != null) {
         var1.add(var2, var8);
      }
   }

   interface Callback {
      AdapterHelper.UpdateOp obtainUpdateOp(int var1, int var2, int var3, Object var4);

      void recycleUpdateOp(AdapterHelper.UpdateOp var1);
   }
}
