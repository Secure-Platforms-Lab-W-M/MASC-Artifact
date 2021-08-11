package android.support.v4.app;

import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListener;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RequiresApi(21)
class FragmentTransitionCompat21 {
   public static void addTarget(Object var0, View var1) {
      if (var0 != null) {
         ((Transition)var0).addTarget(var1);
      }

   }

   public static void addTargets(Object var0, ArrayList var1) {
      Transition var4 = (Transition)var0;
      if (var4 != null) {
         int var2;
         int var3;
         if (var4 instanceof TransitionSet) {
            TransitionSet var5 = (TransitionSet)var4;
            var3 = var5.getTransitionCount();

            for(var2 = 0; var2 < var3; ++var2) {
               addTargets(var5.getTransitionAt(var2), var1);
            }
         } else if (!hasSimpleTarget(var4) && isNullOrEmpty(var4.getTargets())) {
            var3 = var1.size();

            for(var2 = 0; var2 < var3; ++var2) {
               var4.addTarget((View)var1.get(var2));
            }
         }

      }
   }

   public static void beginDelayedTransition(ViewGroup var0, Object var1) {
      TransitionManager.beginDelayedTransition(var0, (Transition)var1);
   }

   private static void bfsAddViewChildren(List var0, View var1) {
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

   public static void captureTransitioningViews(ArrayList var0, View var1) {
      if (var1.getVisibility() == 0) {
         if (var1 instanceof ViewGroup) {
            ViewGroup var4 = (ViewGroup)var1;
            if (var4.isTransitionGroup()) {
               var0.add(var4);
            } else {
               int var3 = var4.getChildCount();

               for(int var2 = 0; var2 < var3; ++var2) {
                  captureTransitioningViews(var0, var4.getChildAt(var2));
               }
            }

            return;
         }

         var0.add(var1);
      }

   }

   public static Object cloneTransition(Object var0) {
      Transition var1 = null;
      if (var0 != null) {
         var1 = ((Transition)var0).clone();
      }

      return var1;
   }

   private static boolean containedBeforeIndex(List var0, View var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         if (var0.get(var3) == var1) {
            return true;
         }
      }

      return false;
   }

   private static String findKeyForValue(Map var0, String var1) {
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

   public static void findNamedViews(Map var0, View var1) {
      if (var1.getVisibility() == 0) {
         String var4 = var1.getTransitionName();
         if (var4 != null) {
            var0.put(var4, var1);
         }

         if (var1 instanceof ViewGroup) {
            ViewGroup var5 = (ViewGroup)var1;
            int var3 = var5.getChildCount();

            for(int var2 = 0; var2 < var3; ++var2) {
               findNamedViews(var0, var5.getChildAt(var2));
            }
         }
      }

   }

   public static void getBoundsOnScreen(View var0, Rect var1) {
      int[] var2 = new int[2];
      var0.getLocationOnScreen(var2);
      var1.set(var2[0], var2[1], var2[0] + var0.getWidth(), var2[1] + var0.getHeight());
   }

   private static boolean hasSimpleTarget(Transition var0) {
      return !isNullOrEmpty(var0.getTargetIds()) || !isNullOrEmpty(var0.getTargetNames()) || !isNullOrEmpty(var0.getTargetTypes());
   }

   private static boolean isNullOrEmpty(List var0) {
      return var0 == null || var0.isEmpty();
   }

   public static Object mergeTransitionsInSequence(Object var0, Object var1, Object var2) {
      Object var3 = null;
      var0 = (Transition)var0;
      Transition var4 = (Transition)var1;
      Transition var6 = (Transition)var2;
      if (var0 != null && var4 != null) {
         var0 = (new TransitionSet()).addTransition((Transition)var0).addTransition(var4).setOrdering(1);
      } else if (var0 == null) {
         var0 = var3;
         if (var4 != null) {
            var0 = var4;
         }
      }

      if (var6 != null) {
         TransitionSet var5 = new TransitionSet();
         if (var0 != null) {
            var5.addTransition((Transition)var0);
         }

         var5.addTransition(var6);
         return var5;
      } else {
         return var0;
      }
   }

   public static Object mergeTransitionsTogether(Object var0, Object var1, Object var2) {
      TransitionSet var3 = new TransitionSet();
      if (var0 != null) {
         var3.addTransition((Transition)var0);
      }

      if (var1 != null) {
         var3.addTransition((Transition)var1);
      }

      if (var2 != null) {
         var3.addTransition((Transition)var2);
      }

      return var3;
   }

   public static ArrayList prepareSetNameOverridesReordered(ArrayList var0) {
      ArrayList var3 = new ArrayList();
      int var2 = var0.size();

      for(int var1 = 0; var1 < var2; ++var1) {
         View var4 = (View)var0.get(var1);
         var3.add(var4.getTransitionName());
         var4.setTransitionName((String)null);
      }

      return var3;
   }

   public static void removeTarget(Object var0, View var1) {
      if (var0 != null) {
         ((Transition)var0).removeTarget(var1);
      }

   }

   public static void replaceTargets(Object var0, ArrayList var1, ArrayList var2) {
      Transition var6 = (Transition)var0;
      int var3;
      int var4;
      if (var6 instanceof TransitionSet) {
         TransitionSet var7 = (TransitionSet)var6;
         var4 = var7.getTransitionCount();

         for(var3 = 0; var3 < var4; ++var3) {
            replaceTargets(var7.getTransitionAt(var3), var1, var2);
         }
      } else if (!hasSimpleTarget(var6)) {
         List var5 = var6.getTargets();
         if (var5 != null && var5.size() == var1.size() && var5.containsAll(var1)) {
            if (var2 == null) {
               var3 = 0;
            } else {
               var3 = var2.size();
            }

            for(var4 = 0; var4 < var3; ++var4) {
               var6.addTarget((View)var2.get(var4));
            }

            for(var3 = var1.size() - 1; var3 >= 0; --var3) {
               var6.removeTarget((View)var1.get(var3));
            }
         }
      }

   }

   public static void scheduleHideFragmentView(Object var0, final View var1, final ArrayList var2) {
      ((Transition)var0).addListener(new TransitionListener() {
         public void onTransitionCancel(Transition var1x) {
         }

         public void onTransitionEnd(Transition var1x) {
            var1x.removeListener(this);
            var1.setVisibility(8);
            int var3 = var2.size();

            for(int var2x = 0; var2x < var3; ++var2x) {
               ((View)var2.get(var2x)).setVisibility(0);
            }

         }

         public void onTransitionPause(Transition var1x) {
         }

         public void onTransitionResume(Transition var1x) {
         }

         public void onTransitionStart(Transition var1x) {
         }
      });
   }

   public static void scheduleNameReset(ViewGroup var0, final ArrayList var1, final Map var2) {
      OneShotPreDrawListener.add(var0, new Runnable() {
         public void run() {
            int var2x = var1.size();

            for(int var1x = 0; var1x < var2x; ++var1x) {
               View var3 = (View)var1.get(var1x);
               String var4 = var3.getTransitionName();
               var3.setTransitionName((String)var2.get(var4));
            }

         }
      });
   }

   public static void scheduleRemoveTargets(Object var0, final Object var1, final ArrayList var2, final Object var3, final ArrayList var4, final Object var5, final ArrayList var6) {
      ((Transition)var0).addListener(new TransitionListener() {
         public void onTransitionCancel(Transition var1x) {
         }

         public void onTransitionEnd(Transition var1x) {
         }

         public void onTransitionPause(Transition var1x) {
         }

         public void onTransitionResume(Transition var1x) {
         }

         public void onTransitionStart(Transition var1x) {
            Object var2x = var1;
            if (var2x != null) {
               FragmentTransitionCompat21.replaceTargets(var2x, var2, (ArrayList)null);
            }

            var2x = var3;
            if (var2x != null) {
               FragmentTransitionCompat21.replaceTargets(var2x, var4, (ArrayList)null);
            }

            var2x = var5;
            if (var2x != null) {
               FragmentTransitionCompat21.replaceTargets(var2x, var6, (ArrayList)null);
            }

         }
      });
   }

   public static void setEpicenter(Object var0, final Rect var1) {
      if (var0 != null) {
         ((Transition)var0).setEpicenterCallback(new EpicenterCallback() {
            public Rect onGetEpicenter(Transition var1x) {
               Rect var2 = var1;
               return var2 != null && !var2.isEmpty() ? var1 : null;
            }
         });
      }

   }

   public static void setEpicenter(Object var0, View var1) {
      if (var1 != null) {
         Transition var3 = (Transition)var0;
         final Rect var2 = new Rect();
         getBoundsOnScreen(var1, var2);
         var3.setEpicenterCallback(new EpicenterCallback() {
            public Rect onGetEpicenter(Transition var1) {
               return var2;
            }
         });
      }

   }

   public static void setNameOverridesOrdered(View var0, final ArrayList var1, final Map var2) {
      OneShotPreDrawListener.add(var0, new Runnable() {
         public void run() {
            int var2x = var1.size();

            for(int var1x = 0; var1x < var2x; ++var1x) {
               View var3 = (View)var1.get(var1x);
               String var4 = var3.getTransitionName();
               if (var4 != null) {
                  var3.setTransitionName(FragmentTransitionCompat21.findKeyForValue(var2, var4));
               }
            }

         }
      });
   }

   public static void setNameOverridesReordered(View var0, final ArrayList var1, final ArrayList var2, final ArrayList var3, Map var4) {
      final int var7 = var2.size();
      final ArrayList var8 = new ArrayList();

      for(int var5 = 0; var5 < var7; ++var5) {
         View var10 = (View)var1.get(var5);
         String var9 = var10.getTransitionName();
         var8.add(var9);
         if (var9 != null) {
            var10.setTransitionName((String)null);
            String var11 = (String)var4.get(var9);

            for(int var6 = 0; var6 < var7; ++var6) {
               if (var11.equals(var3.get(var6))) {
                  ((View)var2.get(var6)).setTransitionName(var9);
                  break;
               }
            }
         }
      }

      OneShotPreDrawListener.add(var0, new Runnable() {
         public void run() {
            for(int var1x = 0; var1x < var7; ++var1x) {
               ((View)var2.get(var1x)).setTransitionName((String)var3.get(var1x));
               ((View)var1.get(var1x)).setTransitionName((String)var8.get(var1x));
            }

         }
      });
   }

   public static void setSharedElementTargets(Object var0, View var1, ArrayList var2) {
      TransitionSet var6 = (TransitionSet)var0;
      List var5 = var6.getTargets();
      var5.clear();
      int var4 = var2.size();

      for(int var3 = 0; var3 < var4; ++var3) {
         bfsAddViewChildren(var5, (View)var2.get(var3));
      }

      var5.add(var1);
      var2.add(var1);
      addTargets(var6, var2);
   }

   public static void swapSharedElementTargets(Object var0, ArrayList var1, ArrayList var2) {
      TransitionSet var3 = (TransitionSet)var0;
      if (var3 != null) {
         var3.getTargets().clear();
         var3.getTargets().addAll(var2);
         replaceTargets(var3, var1, var2);
      }

   }

   public static Object wrapTransitionInSet(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         TransitionSet var1 = new TransitionSet();
         var1.addTransition((Transition)var0);
         return var1;
      }
   }
}
