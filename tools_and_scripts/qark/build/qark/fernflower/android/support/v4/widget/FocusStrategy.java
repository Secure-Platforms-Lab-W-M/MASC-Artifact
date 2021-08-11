package android.support.v4.widget;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {
   private static boolean beamBeats(int var0, @NonNull Rect var1, @NonNull Rect var2, @NonNull Rect var3) {
      boolean var5 = beamsOverlap(var0, var1, var2);
      boolean var6 = beamsOverlap(var0, var1, var3);
      boolean var4 = false;
      if (!var6) {
         if (!var5) {
            return false;
         } else if (!isToDirectionOf(var0, var1, var3)) {
            return true;
         } else if (var0 != 17) {
            if (var0 == 66) {
               return true;
            } else {
               if (majorAxisDistance(var0, var1, var2) < majorAxisDistanceToFarEdge(var0, var1, var3)) {
                  var4 = true;
               }

               return var4;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private static boolean beamsOverlap(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      if (var0 != 17) {
         if (var0 == 33) {
            return var2.right >= var1.left && var2.left <= var1.right;
         }

         if (var0 != 66) {
            if (var0 != 130) {
               throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }

            return var2.right >= var1.left && var2.left <= var1.right;
         }
      }

      if (var2.bottom >= var1.top && var2.top <= var1.bottom) {
         return true;
      } else {
         return false;
      }
   }

   public static Object findNextFocusInAbsoluteDirection(@NonNull Object var0, @NonNull FocusStrategy.CollectionAdapter var1, @NonNull FocusStrategy.BoundsAdapter var2, @Nullable Object var3, @NonNull Rect var4, int var5) {
      Rect var10 = new Rect(var4);
      if (var5 != 17) {
         if (var5 != 33) {
            if (var5 != 66) {
               if (var5 != 130) {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               }

               var10.offset(0, -(var4.height() + 1));
            } else {
               var10.offset(-(var4.width() + 1), 0);
            }
         } else {
            var10.offset(0, var4.height() + 1);
         }
      } else {
         var10.offset(var4.width() + 1, 0);
      }

      Object var8 = null;
      int var7 = var1.size(var0);
      Rect var11 = new Rect();

      for(int var6 = 0; var6 < var7; ++var6) {
         Object var9 = var1.get(var0, var6);
         if (var9 != var3) {
            var2.obtainBounds(var9, var11);
            if (isBetterCandidate(var5, var4, var11, var10)) {
               var10.set(var11);
               var8 = var9;
            }
         }
      }

      return var8;
   }

   public static Object findNextFocusInRelativeDirection(@NonNull Object var0, @NonNull FocusStrategy.CollectionAdapter var1, @NonNull FocusStrategy.BoundsAdapter var2, @Nullable Object var3, int var4, boolean var5, boolean var6) {
      int var8 = var1.size(var0);
      ArrayList var9 = new ArrayList(var8);

      for(int var7 = 0; var7 < var8; ++var7) {
         var9.add(var1.get(var0, var7));
      }

      Collections.sort(var9, new FocusStrategy.SequentialComparator(var5, var2));
      if (var4 != 1) {
         if (var4 == 2) {
            return getNextFocusable(var3, var9, var6);
         } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
         }
      } else {
         return getPreviousFocusable(var3, var9, var6);
      }
   }

   private static Object getNextFocusable(Object var0, ArrayList var1, boolean var2) {
      int var4 = var1.size();
      int var3;
      if (var0 == null) {
         var3 = -1;
      } else {
         var3 = var1.lastIndexOf(var0);
      }

      ++var3;
      if (var3 < var4) {
         return var1.get(var3);
      } else {
         return var2 && var4 > 0 ? var1.get(0) : null;
      }
   }

   private static Object getPreviousFocusable(Object var0, ArrayList var1, boolean var2) {
      int var4 = var1.size();
      int var3;
      if (var0 == null) {
         var3 = var4;
      } else {
         var3 = var1.indexOf(var0);
      }

      --var3;
      if (var3 >= 0) {
         return var1.get(var3);
      } else {
         return var2 && var4 > 0 ? var1.get(var4 - 1) : null;
      }
   }

   private static int getWeightedDistanceFor(int var0, int var1) {
      return var0 * 13 * var0 + var1 * var1;
   }

   private static boolean isBetterCandidate(int var0, @NonNull Rect var1, @NonNull Rect var2, @NonNull Rect var3) {
      boolean var5 = isCandidate(var1, var2, var0);
      boolean var4 = false;
      if (!var5) {
         return false;
      } else if (!isCandidate(var1, var3, var0)) {
         return true;
      } else if (beamBeats(var0, var1, var2, var3)) {
         return true;
      } else if (beamBeats(var0, var1, var3, var2)) {
         return false;
      } else {
         if (getWeightedDistanceFor(majorAxisDistance(var0, var1, var2), minorAxisDistance(var0, var1, var2)) < getWeightedDistanceFor(majorAxisDistance(var0, var1, var3), minorAxisDistance(var0, var1, var3))) {
            var4 = true;
         }

         return var4;
      }
   }

   private static boolean isCandidate(@NonNull Rect var0, @NonNull Rect var1, int var2) {
      if (var2 != 17) {
         if (var2 != 33) {
            if (var2 != 66) {
               if (var2 != 130) {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               } else {
                  return (var0.top < var1.top || var0.bottom <= var1.top) && var0.bottom < var1.bottom;
               }
            } else {
               return (var0.left < var1.left || var0.right <= var1.left) && var0.right < var1.right;
            }
         } else {
            return (var0.bottom > var1.bottom || var0.top >= var1.bottom) && var0.top > var1.top;
         }
      } else {
         return (var0.right > var1.right || var0.left >= var1.right) && var0.left > var1.left;
      }
   }

   private static boolean isToDirectionOf(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      if (var0 != 17) {
         if (var0 != 33) {
            if (var0 != 66) {
               if (var0 == 130) {
                  return var1.bottom <= var2.top;
               } else {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               }
            } else {
               return var1.right <= var2.left;
            }
         } else {
            return var1.top >= var2.bottom;
         }
      } else {
         return var1.left >= var2.right;
      }
   }

   private static int majorAxisDistance(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      return Math.max(0, majorAxisDistanceRaw(var0, var1, var2));
   }

   private static int majorAxisDistanceRaw(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      if (var0 != 17) {
         if (var0 != 33) {
            if (var0 != 66) {
               if (var0 == 130) {
                  return var2.top - var1.bottom;
               } else {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               }
            } else {
               return var2.left - var1.right;
            }
         } else {
            return var1.top - var2.bottom;
         }
      } else {
         return var1.left - var2.right;
      }
   }

   private static int majorAxisDistanceToFarEdge(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      return Math.max(1, majorAxisDistanceToFarEdgeRaw(var0, var1, var2));
   }

   private static int majorAxisDistanceToFarEdgeRaw(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      if (var0 != 17) {
         if (var0 != 33) {
            if (var0 != 66) {
               if (var0 == 130) {
                  return var2.bottom - var1.bottom;
               } else {
                  throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
               }
            } else {
               return var2.right - var1.right;
            }
         } else {
            return var1.top - var2.top;
         }
      } else {
         return var1.left - var2.left;
      }
   }

   private static int minorAxisDistance(int var0, @NonNull Rect var1, @NonNull Rect var2) {
      if (var0 != 17) {
         if (var0 == 33) {
            return Math.abs(var1.left + var1.width() / 2 - (var2.left + var2.width() / 2));
         }

         if (var0 != 66) {
            if (var0 != 130) {
               throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }

            return Math.abs(var1.left + var1.width() / 2 - (var2.left + var2.width() / 2));
         }
      }

      return Math.abs(var1.top + var1.height() / 2 - (var2.top + var2.height() / 2));
   }

   public interface BoundsAdapter {
      void obtainBounds(Object var1, Rect var2);
   }

   public interface CollectionAdapter {
      Object get(Object var1, int var2);

      int size(Object var1);
   }

   private static class SequentialComparator implements Comparator {
      private final FocusStrategy.BoundsAdapter mAdapter;
      private final boolean mIsLayoutRtl;
      private final Rect mTemp1 = new Rect();
      private final Rect mTemp2 = new Rect();

      SequentialComparator(boolean var1, FocusStrategy.BoundsAdapter var2) {
         this.mIsLayoutRtl = var1;
         this.mAdapter = var2;
      }

      public int compare(Object var1, Object var2) {
         Rect var6 = this.mTemp1;
         Rect var7 = this.mTemp2;
         this.mAdapter.obtainBounds(var1, var6);
         this.mAdapter.obtainBounds(var2, var7);
         int var4 = var6.top;
         int var5 = var7.top;
         byte var3 = -1;
         if (var4 < var5) {
            return -1;
         } else if (var6.top > var7.top) {
            return 1;
         } else if (var6.left < var7.left) {
            if (this.mIsLayoutRtl) {
               var3 = 1;
            }

            return var3;
         } else if (var6.left > var7.left) {
            return this.mIsLayoutRtl ? -1 : 1;
         } else if (var6.bottom < var7.bottom) {
            return -1;
         } else if (var6.bottom > var7.bottom) {
            return 1;
         } else if (var6.right < var7.right) {
            if (this.mIsLayoutRtl) {
               var3 = 1;
            }

            return var3;
         } else if (var6.right > var7.right) {
            return this.mIsLayoutRtl ? -1 : 1;
         } else {
            return 0;
         }
      }
   }
}
