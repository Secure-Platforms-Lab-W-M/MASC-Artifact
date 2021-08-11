package androidx.recyclerview.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DiffUtil {
   private static final Comparator SNAKE_COMPARATOR = new Comparator() {
      public int compare(DiffUtil.Snake var1, DiffUtil.Snake var2) {
         int var3 = var1.field_210 - var2.field_210;
         return var3 == 0 ? var1.field_211 - var2.field_211 : var3;
      }
   };

   private DiffUtil() {
   }

   public static DiffUtil.DiffResult calculateDiff(DiffUtil.Callback var0) {
      return calculateDiff(var0, true);
   }

   public static DiffUtil.DiffResult calculateDiff(DiffUtil.Callback var0, boolean var1) {
      int var2 = var0.getOldListSize();
      int var3 = var0.getNewListSize();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      var6.add(new DiffUtil.Range(0, var2, 0, var3));
      var2 = var2 + var3 + Math.abs(var2 - var3);
      int[] var7 = new int[var2 * 2];
      int[] var8 = new int[var2 * 2];
      ArrayList var9 = new ArrayList();

      while(!var6.isEmpty()) {
         DiffUtil.Range var10 = (DiffUtil.Range)var6.remove(var6.size() - 1);
         DiffUtil.Snake var11 = diffPartial(var0, var10.oldListStart, var10.oldListEnd, var10.newListStart, var10.newListEnd, var7, var8, var2);
         if (var11 != null) {
            if (var11.size > 0) {
               var5.add(var11);
            }

            var11.field_210 += var10.oldListStart;
            var11.field_211 += var10.newListStart;
            DiffUtil.Range var4;
            if (var9.isEmpty()) {
               var4 = new DiffUtil.Range();
            } else {
               var4 = (DiffUtil.Range)var9.remove(var9.size() - 1);
            }

            var4.oldListStart = var10.oldListStart;
            var4.newListStart = var10.newListStart;
            if (var11.reverse) {
               var4.oldListEnd = var11.field_210;
               var4.newListEnd = var11.field_211;
            } else if (var11.removal) {
               var4.oldListEnd = var11.field_210 - 1;
               var4.newListEnd = var11.field_211;
            } else {
               var4.oldListEnd = var11.field_210;
               var4.newListEnd = var11.field_211 - 1;
            }

            var6.add(var4);
            if (var11.reverse) {
               if (var11.removal) {
                  var10.oldListStart = var11.field_210 + var11.size + 1;
                  var10.newListStart = var11.field_211 + var11.size;
               } else {
                  var10.oldListStart = var11.field_210 + var11.size;
                  var10.newListStart = var11.field_211 + var11.size + 1;
               }
            } else {
               var10.oldListStart = var11.field_210 + var11.size;
               var10.newListStart = var11.field_211 + var11.size;
            }

            var6.add(var10);
         } else {
            var9.add(var10);
         }
      }

      Collections.sort(var5, SNAKE_COMPARATOR);
      return new DiffUtil.DiffResult(var0, var5, var7, var8, var1);
   }

   private static DiffUtil.Snake diffPartial(DiffUtil.Callback var0, int var1, int var2, int var3, int var4, int[] var5, int[] var6, int var7) {
      int var10 = var2 - var1;
      int var12 = var4 - var3;
      if (var2 - var1 >= 1 && var4 - var3 >= 1) {
         int var13 = var10 - var12;
         int var14 = (var10 + var12 + 1) / 2;
         Arrays.fill(var5, var7 - var14 - 1, var7 + var14 + 1, 0);
         Arrays.fill(var6, var7 - var14 - 1 + var13, var7 + var14 + 1 + var13, var10);
         boolean var8;
         if (var13 % 2 != 0) {
            var8 = true;
         } else {
            var8 = false;
         }

         int var9 = 0;

         for(var4 = var10; var9 <= var14; var4 = var4) {
            int var11;
            boolean var16;
            DiffUtil.Snake var17;
            for(var10 = -var9; var10 <= var9; var10 += 2) {
               if (var10 != -var9 && (var10 == var9 || var5[var7 + var10 - 1] >= var5[var7 + var10 + 1])) {
                  var2 = var5[var7 + var10 - 1] + 1;
                  var16 = true;
               } else {
                  var2 = var5[var7 + var10 + 1];
                  var16 = false;
               }

               for(var11 = var2 - var10; var2 < var4 && var11 < var12 && var0.areItemsTheSame(var1 + var2, var3 + var11); ++var11) {
                  ++var2;
               }

               var5[var7 + var10] = var2;
               if (var8 && var10 >= var13 - var9 + 1 && var10 <= var13 + var9 - 1 && var5[var7 + var10] >= var6[var7 + var10]) {
                  var17 = new DiffUtil.Snake();
                  var17.field_210 = var6[var7 + var10];
                  var17.field_211 = var17.field_210 - var10;
                  var17.size = var5[var7 + var10] - var6[var7 + var10];
                  var17.removal = var16;
                  var17.reverse = false;
                  return var17;
               }
            }

            for(var10 = -var9; var10 <= var9; var10 += 2) {
               int var15 = var10 + var13;
               if (var15 == var9 + var13 || var15 != -var9 + var13 && var6[var7 + var15 - 1] < var6[var7 + var15 + 1]) {
                  var4 = var6[var7 + var15 - 1];
                  var16 = false;
               } else {
                  var4 = var6[var7 + var15 + 1] - 1;
                  var16 = true;
               }

               for(var11 = var4 - var15; var4 > 0 && var11 > 0 && var0.areItemsTheSame(var1 + var4 - 1, var3 + var11 - 1); --var11) {
                  --var4;
               }

               var6[var7 + var15] = var4;
               if (!var8 && var10 + var13 >= -var9 && var10 + var13 <= var9 && var5[var7 + var15] >= var6[var7 + var15]) {
                  var17 = new DiffUtil.Snake();
                  var17.field_210 = var6[var7 + var15];
                  var17.field_211 = var17.field_210 - var15;
                  var17.size = var5[var7 + var15] - var6[var7 + var15];
                  var17.removal = var16;
                  var17.reverse = true;
                  return var17;
               }
            }

            ++var9;
         }

         throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
      } else {
         return null;
      }
   }

   public abstract static class Callback {
      public abstract boolean areContentsTheSame(int var1, int var2);

      public abstract boolean areItemsTheSame(int var1, int var2);

      public Object getChangePayload(int var1, int var2) {
         return null;
      }

      public abstract int getNewListSize();

      public abstract int getOldListSize();
   }

   public static class DiffResult {
      private static final int FLAG_CHANGED = 2;
      private static final int FLAG_IGNORE = 16;
      private static final int FLAG_MASK = 31;
      private static final int FLAG_MOVED_CHANGED = 4;
      private static final int FLAG_MOVED_NOT_CHANGED = 8;
      private static final int FLAG_NOT_CHANGED = 1;
      private static final int FLAG_OFFSET = 5;
      public static final int NO_POSITION = -1;
      private final DiffUtil.Callback mCallback;
      private final boolean mDetectMoves;
      private final int[] mNewItemStatuses;
      private final int mNewListSize;
      private final int[] mOldItemStatuses;
      private final int mOldListSize;
      private final List mSnakes;

      DiffResult(DiffUtil.Callback var1, List var2, int[] var3, int[] var4, boolean var5) {
         this.mSnakes = var2;
         this.mOldItemStatuses = var3;
         this.mNewItemStatuses = var4;
         Arrays.fill(var3, 0);
         Arrays.fill(this.mNewItemStatuses, 0);
         this.mCallback = var1;
         this.mOldListSize = var1.getOldListSize();
         this.mNewListSize = var1.getNewListSize();
         this.mDetectMoves = var5;
         this.addRootSnake();
         this.findMatchingItems();
      }

      private void addRootSnake() {
         DiffUtil.Snake var1;
         if (this.mSnakes.isEmpty()) {
            var1 = null;
         } else {
            var1 = (DiffUtil.Snake)this.mSnakes.get(0);
         }

         if (var1 == null || var1.field_210 != 0 || var1.field_211 != 0) {
            var1 = new DiffUtil.Snake();
            var1.field_210 = 0;
            var1.field_211 = 0;
            var1.removal = false;
            var1.size = 0;
            var1.reverse = false;
            this.mSnakes.add(0, var1);
         }

      }

      private void dispatchAdditions(List var1, ListUpdateCallback var2, int var3, int var4, int var5) {
         if (!this.mDetectMoves) {
            var2.onInserted(var3, var4);
         } else {
            --var4;

            for(; var4 >= 0; --var4) {
               int var6 = this.mNewItemStatuses[var5 + var4] & 31;
               if (var6 != 0) {
                  if (var6 != 4 && var6 != 8) {
                     if (var6 != 16) {
                        StringBuilder var10 = new StringBuilder();
                        var10.append("unknown flag for pos ");
                        var10.append(var5 + var4);
                        var10.append(" ");
                        var10.append(Long.toBinaryString((long)var6));
                        throw new IllegalStateException(var10.toString());
                     }

                     var1.add(new DiffUtil.PostponedUpdate(var5 + var4, var3, false));
                  } else {
                     int var7 = this.mNewItemStatuses[var5 + var4] >> 5;
                     var2.onMoved(removePostponedUpdate(var1, var7, true).currentPos, var3);
                     if (var6 == 4) {
                        var2.onChanged(var3, 1, this.mCallback.getChangePayload(var7, var5 + var4));
                     }
                  }
               } else {
                  var2.onInserted(var3, 1);

                  DiffUtil.PostponedUpdate var9;
                  for(Iterator var8 = var1.iterator(); var8.hasNext(); ++var9.currentPos) {
                     var9 = (DiffUtil.PostponedUpdate)var8.next();
                  }
               }
            }

         }
      }

      private void dispatchRemovals(List var1, ListUpdateCallback var2, int var3, int var4, int var5) {
         if (!this.mDetectMoves) {
            var2.onRemoved(var3, var4);
         } else {
            --var4;

            for(; var4 >= 0; --var4) {
               int var6 = this.mOldItemStatuses[var5 + var4] & 31;
               if (var6 != 0) {
                  if (var6 != 4 && var6 != 8) {
                     if (var6 != 16) {
                        StringBuilder var10 = new StringBuilder();
                        var10.append("unknown flag for pos ");
                        var10.append(var5 + var4);
                        var10.append(" ");
                        var10.append(Long.toBinaryString((long)var6));
                        throw new IllegalStateException(var10.toString());
                     }

                     var1.add(new DiffUtil.PostponedUpdate(var5 + var4, var3 + var4, true));
                  } else {
                     int var7 = this.mOldItemStatuses[var5 + var4] >> 5;
                     DiffUtil.PostponedUpdate var11 = removePostponedUpdate(var1, var7, false);
                     var2.onMoved(var3 + var4, var11.currentPos - 1);
                     if (var6 == 4) {
                        var2.onChanged(var11.currentPos - 1, 1, this.mCallback.getChangePayload(var5 + var4, var7));
                     }
                  }
               } else {
                  var2.onRemoved(var3 + var4, 1);

                  DiffUtil.PostponedUpdate var9;
                  for(Iterator var8 = var1.iterator(); var8.hasNext(); --var9.currentPos) {
                     var9 = (DiffUtil.PostponedUpdate)var8.next();
                  }
               }
            }

         }
      }

      private void findAddition(int var1, int var2, int var3) {
         if (this.mOldItemStatuses[var1 - 1] == 0) {
            this.findMatchingItem(var1, var2, var3, false);
         }
      }

      private boolean findMatchingItem(int var1, int var2, int var3, boolean var4) {
         int var5;
         int var6;
         int var7;
         if (var4) {
            var6 = var2 - 1;
            var7 = var2 - 1;
            var2 = var1;
            var5 = var7;
         } else {
            var6 = var1 - 1;
            var7 = var1 - 1;
            var5 = var2;
            var2 = var7;
         }

         var7 = var5;

         for(var5 = var2; var3 >= 0; --var3) {
            DiffUtil.Snake var12 = (DiffUtil.Snake)this.mSnakes.get(var3);
            int var8 = var12.field_210;
            int var9 = var12.size;
            int var10 = var12.field_211;
            int var11 = var12.size;
            byte var13 = 8;
            if (var4) {
               --var5;

               while(var5 >= var8 + var9) {
                  if (this.mCallback.areItemsTheSame(var5, var6)) {
                     if (!this.mCallback.areContentsTheSame(var5, var6)) {
                        var13 = 4;
                     }

                     this.mNewItemStatuses[var6] = var5 << 5 | 16;
                     this.mOldItemStatuses[var5] = var6 << 5 | var13;
                     return true;
                  }

                  --var5;
               }
            } else {
               for(var5 = var7 - 1; var5 >= var10 + var11; --var5) {
                  if (this.mCallback.areItemsTheSame(var6, var5)) {
                     if (!this.mCallback.areContentsTheSame(var6, var5)) {
                        var13 = 4;
                     }

                     this.mOldItemStatuses[var1 - 1] = var5 << 5 | 16;
                     this.mNewItemStatuses[var5] = var1 - 1 << 5 | var13;
                     return true;
                  }
               }
            }

            var5 = var12.field_210;
            var7 = var12.field_211;
         }

         return false;
      }

      private void findMatchingItems() {
         int var2 = this.mOldListSize;
         int var1 = this.mNewListSize;

         for(int var3 = this.mSnakes.size() - 1; var3 >= 0; --var3) {
            DiffUtil.Snake var9 = (DiffUtil.Snake)this.mSnakes.get(var3);
            int var7 = var9.field_210;
            int var8 = var9.size;
            int var5 = var9.field_211;
            int var6 = var9.size;
            int var4;
            if (this.mDetectMoves) {
               while(true) {
                  var4 = var1;
                  if (var2 <= var7 + var8) {
                     while(var4 > var5 + var6) {
                        this.findRemoval(var2, var4, var3);
                        --var4;
                     }
                     break;
                  }

                  this.findAddition(var2, var1, var3);
                  --var2;
               }
            }

            for(var1 = 0; var1 < var9.size; ++var1) {
               var4 = var9.field_210 + var1;
               var5 = var9.field_211 + var1;
               byte var10;
               if (this.mCallback.areContentsTheSame(var4, var5)) {
                  var10 = 1;
               } else {
                  var10 = 2;
               }

               this.mOldItemStatuses[var4] = var5 << 5 | var10;
               this.mNewItemStatuses[var5] = var4 << 5 | var10;
            }

            var2 = var9.field_210;
            var1 = var9.field_211;
         }

      }

      private void findRemoval(int var1, int var2, int var3) {
         if (this.mNewItemStatuses[var2 - 1] == 0) {
            this.findMatchingItem(var1, var2, var3, true);
         }
      }

      private static DiffUtil.PostponedUpdate removePostponedUpdate(List var0, int var1, boolean var2) {
         for(int var3 = var0.size() - 1; var3 >= 0; --var3) {
            DiffUtil.PostponedUpdate var5 = (DiffUtil.PostponedUpdate)var0.get(var3);
            if (var5.posInOwnerList == var1 && var5.removal == var2) {
               var0.remove(var3);

               for(var1 = var3; var1 < var0.size(); ++var1) {
                  DiffUtil.PostponedUpdate var6 = (DiffUtil.PostponedUpdate)var0.get(var1);
                  int var4 = var6.currentPos;
                  byte var7;
                  if (var2) {
                     var7 = 1;
                  } else {
                     var7 = -1;
                  }

                  var6.currentPos = var4 + var7;
               }

               return var5;
            }
         }

         return null;
      }

      public int convertNewPositionToOld(int var1) {
         if (var1 >= 0 && var1 < this.mNewListSize) {
            var1 = this.mNewItemStatuses[var1];
            return (var1 & 31) == 0 ? -1 : var1 >> 5;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Index out of bounds - passed position = ");
            var2.append(var1);
            var2.append(", new list size = ");
            var2.append(this.mNewListSize);
            throw new IndexOutOfBoundsException(var2.toString());
         }
      }

      public int convertOldPositionToNew(int var1) {
         if (var1 >= 0 && var1 < this.mOldListSize) {
            var1 = this.mOldItemStatuses[var1];
            return (var1 & 31) == 0 ? -1 : var1 >> 5;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Index out of bounds - passed position = ");
            var2.append(var1);
            var2.append(", old list size = ");
            var2.append(this.mOldListSize);
            throw new IndexOutOfBoundsException(var2.toString());
         }
      }

      public void dispatchUpdatesTo(ListUpdateCallback var1) {
         BatchingListUpdateCallback var10;
         if (var1 instanceof BatchingListUpdateCallback) {
            var10 = (BatchingListUpdateCallback)var1;
         } else {
            var10 = new BatchingListUpdateCallback(var1);
         }

         ArrayList var8 = new ArrayList();
         int var4 = this.mOldListSize;
         int var3 = this.mNewListSize;
         int var2 = this.mSnakes.size();
         --var2;

         while(var2 >= 0) {
            DiffUtil.Snake var9 = (DiffUtil.Snake)this.mSnakes.get(var2);
            int var6 = var9.size;
            int var7 = var9.field_210 + var6;
            int var5 = var9.field_211 + var6;
            if (var7 < var4) {
               this.dispatchRemovals(var8, var10, var7, var4 - var7, var7);
            }

            if (var5 < var3) {
               this.dispatchAdditions(var8, var10, var7, var3 - var5, var5);
            }

            for(var3 = var6 - 1; var3 >= 0; --var3) {
               if ((this.mOldItemStatuses[var9.field_210 + var3] & 31) == 2) {
                  var10.onChanged(var9.field_210 + var3, 1, this.mCallback.getChangePayload(var9.field_210 + var3, var9.field_211 + var3));
               }
            }

            var4 = var9.field_210;
            var3 = var9.field_211;
            --var2;
         }

         var10.dispatchLastEvent();
      }

      public void dispatchUpdatesTo(RecyclerView.Adapter var1) {
         this.dispatchUpdatesTo((ListUpdateCallback)(new AdapterListUpdateCallback(var1)));
      }

      List getSnakes() {
         return this.mSnakes;
      }
   }

   public abstract static class ItemCallback {
      public abstract boolean areContentsTheSame(Object var1, Object var2);

      public abstract boolean areItemsTheSame(Object var1, Object var2);

      public Object getChangePayload(Object var1, Object var2) {
         return null;
      }
   }

   private static class PostponedUpdate {
      int currentPos;
      int posInOwnerList;
      boolean removal;

      public PostponedUpdate(int var1, int var2, boolean var3) {
         this.posInOwnerList = var1;
         this.currentPos = var2;
         this.removal = var3;
      }
   }

   static class Range {
      int newListEnd;
      int newListStart;
      int oldListEnd;
      int oldListStart;

      public Range() {
      }

      public Range(int var1, int var2, int var3, int var4) {
         this.oldListStart = var1;
         this.oldListEnd = var2;
         this.newListStart = var3;
         this.newListEnd = var4;
      }
   }

   static class Snake {
      boolean removal;
      boolean reverse;
      int size;
      // $FF: renamed from: x int
      int field_210;
      // $FF: renamed from: y int
      int field_211;
   }
}
