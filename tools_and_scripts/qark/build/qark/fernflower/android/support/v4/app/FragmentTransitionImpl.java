package android.support.v4.app;

import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class FragmentTransitionImpl {
   protected static void bfsAddViewChildren(List var0, View var1) {
      int var4 = var0.size();
      if (!containedBeforeIndex(var0, var1, var4)) {
         var0.add(var1);

         for(int var2 = var4; var2 < var0.size(); ++var2) {
            var1 = (View)var0.get(var2);
            if (var1 instanceof ViewGroup) {
               ViewGroup var7 = (ViewGroup)var1;
               int var5 = var7.getChildCount();

               for(int var3 = 0; var3 < var5; ++var3) {
                  View var6 = var7.getChildAt(var3);
                  if (!containedBeforeIndex(var0, var6, var4)) {
                     var0.add(var6);
                  }
               }
            }
         }
      }

   }

   private static boolean containedBeforeIndex(List var0, View var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         if (var0.get(var3) == var1) {
            return true;
         }
      }

      return false;
   }

   static String findKeyForValue(Map var0, String var1) {
      Iterator var3 = var0.entrySet().iterator();

      Entry var2;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var2 = (Entry)var3.next();
      } while(!var1.equals(var2.getValue()));

      return (String)var2.getKey();
   }

   protected static boolean isNullOrEmpty(List var0) {
      return var0 == null || var0.isEmpty();
   }

   public abstract void addTarget(Object var1, View var2);

   public abstract void addTargets(Object var1, ArrayList var2);

   public abstract void beginDelayedTransition(ViewGroup var1, Object var2);

   public abstract boolean canHandle(Object var1);

   void captureTransitioningViews(ArrayList var1, View var2) {
      if (var2.getVisibility() == 0) {
         if (!(var2 instanceof ViewGroup)) {
            var1.add(var2);
            return;
         }

         ViewGroup var5 = (ViewGroup)var2;
         if (ViewGroupCompat.isTransitionGroup(var5)) {
            var1.add(var5);
         } else {
            int var4 = var5.getChildCount();

            for(int var3 = 0; var3 < var4; ++var3) {
               this.captureTransitioningViews(var1, var5.getChildAt(var3));
            }
         }
      }

   }

   public abstract Object cloneTransition(Object var1);

   void findNamedViews(Map var1, View var2) {
      if (var2.getVisibility() == 0) {
         String var5 = ViewCompat.getTransitionName(var2);
         if (var5 != null) {
            var1.put(var5, var2);
         }

         if (var2 instanceof ViewGroup) {
            ViewGroup var6 = (ViewGroup)var2;
            int var4 = var6.getChildCount();

            for(int var3 = 0; var3 < var4; ++var3) {
               this.findNamedViews(var1, var6.getChildAt(var3));
            }
         }
      }

   }

   protected void getBoundsOnScreen(View var1, Rect var2) {
      int[] var3 = new int[2];
      var1.getLocationOnScreen(var3);
      var2.set(var3[0], var3[1], var3[0] + var1.getWidth(), var3[1] + var1.getHeight());
   }

   public abstract Object mergeTransitionsInSequence(Object var1, Object var2, Object var3);

   public abstract Object mergeTransitionsTogether(Object var1, Object var2, Object var3);

   ArrayList prepareSetNameOverridesReordered(ArrayList var1) {
      ArrayList var4 = new ArrayList();
      int var3 = var1.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         View var5 = (View)var1.get(var2);
         var4.add(ViewCompat.getTransitionName(var5));
         ViewCompat.setTransitionName(var5, (String)null);
      }

      return var4;
   }

   public abstract void removeTarget(Object var1, View var2);

   public abstract void replaceTargets(Object var1, ArrayList var2, ArrayList var3);

   public abstract void scheduleHideFragmentView(Object var1, View var2, ArrayList var3);

   void scheduleNameReset(ViewGroup var1, final ArrayList var2, final Map var3) {
      OneShotPreDrawListener.add(var1, new Runnable() {
         public void run() {
            int var2x = var2.size();

            for(int var1 = 0; var1 < var2x; ++var1) {
               View var3x = (View)var2.get(var1);
               String var4 = ViewCompat.getTransitionName(var3x);
               ViewCompat.setTransitionName(var3x, (String)var3.get(var4));
            }

         }
      });
   }

   public abstract void scheduleRemoveTargets(Object var1, Object var2, ArrayList var3, Object var4, ArrayList var5, Object var6, ArrayList var7);

   public abstract void setEpicenter(Object var1, Rect var2);

   public abstract void setEpicenter(Object var1, View var2);

   void setNameOverridesOrdered(View var1, final ArrayList var2, final Map var3) {
      OneShotPreDrawListener.add(var1, new Runnable() {
         public void run() {
            int var2x = var2.size();

            for(int var1 = 0; var1 < var2x; ++var1) {
               View var3x = (View)var2.get(var1);
               String var4 = ViewCompat.getTransitionName(var3x);
               if (var4 != null) {
                  ViewCompat.setTransitionName(var3x, FragmentTransitionImpl.findKeyForValue(var3, var4));
               }
            }

         }
      });
   }

   void setNameOverridesReordered(View var1, final ArrayList var2, final ArrayList var3, final ArrayList var4, Map var5) {
      final int var8 = var3.size();
      final ArrayList var9 = new ArrayList();

      for(int var6 = 0; var6 < var8; ++var6) {
         View var11 = (View)var2.get(var6);
         String var10 = ViewCompat.getTransitionName(var11);
         var9.add(var10);
         if (var10 != null) {
            ViewCompat.setTransitionName(var11, (String)null);
            String var12 = (String)var5.get(var10);

            for(int var7 = 0; var7 < var8; ++var7) {
               if (var12.equals(var4.get(var7))) {
                  ViewCompat.setTransitionName((View)var3.get(var7), var10);
                  break;
               }
            }
         }
      }

      OneShotPreDrawListener.add(var1, new Runnable() {
         public void run() {
            for(int var1 = 0; var1 < var8; ++var1) {
               ViewCompat.setTransitionName((View)var3.get(var1), (String)var4.get(var1));
               ViewCompat.setTransitionName((View)var2.get(var1), (String)var9.get(var1));
            }

         }
      });
   }

   public abstract void setSharedElementTargets(Object var1, View var2, ArrayList var3);

   public abstract void swapSharedElementTargets(Object var1, ArrayList var2, ArrayList var3);

   public abstract Object wrapTransitionInSet(Object var1);
}
