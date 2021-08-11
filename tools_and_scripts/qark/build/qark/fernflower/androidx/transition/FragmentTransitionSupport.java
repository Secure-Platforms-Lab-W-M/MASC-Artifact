package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransitionImpl;
import java.util.ArrayList;
import java.util.List;

public class FragmentTransitionSupport extends FragmentTransitionImpl {
   private static boolean hasSimpleTarget(Transition var0) {
      return !isNullOrEmpty(var0.getTargetIds()) || !isNullOrEmpty(var0.getTargetNames()) || !isNullOrEmpty(var0.getTargetTypes());
   }

   public void addTarget(Object var1, View var2) {
      if (var1 != null) {
         ((Transition)var1).addTarget(var2);
      }

   }

   public void addTargets(Object var1, ArrayList var2) {
      Transition var5 = (Transition)var1;
      if (var5 != null) {
         int var3;
         int var4;
         if (var5 instanceof TransitionSet) {
            TransitionSet var6 = (TransitionSet)var5;
            var4 = var6.getTransitionCount();

            for(var3 = 0; var3 < var4; ++var3) {
               this.addTargets(var6.getTransitionAt(var3), var2);
            }
         } else if (!hasSimpleTarget(var5) && isNullOrEmpty(var5.getTargets())) {
            var4 = var2.size();

            for(var3 = 0; var3 < var4; ++var3) {
               var5.addTarget((View)var2.get(var3));
            }
         }

      }
   }

   public void beginDelayedTransition(ViewGroup var1, Object var2) {
      TransitionManager.beginDelayedTransition(var1, (Transition)var2);
   }

   public boolean canHandle(Object var1) {
      return var1 instanceof Transition;
   }

   public Object cloneTransition(Object var1) {
      Transition var2 = null;
      if (var1 != null) {
         var2 = ((Transition)var1).clone();
      }

      return var2;
   }

   public Object mergeTransitionsInSequence(Object var1, Object var2, Object var3) {
      Object var4 = null;
      var1 = (Transition)var1;
      Transition var5 = (Transition)var2;
      Transition var7 = (Transition)var3;
      if (var1 != null && var5 != null) {
         var1 = (new TransitionSet()).addTransition((Transition)var1).addTransition(var5).setOrdering(1);
      } else if (var1 == null) {
         var1 = var4;
         if (var5 != null) {
            var1 = var5;
         }
      }

      if (var7 != null) {
         TransitionSet var6 = new TransitionSet();
         if (var1 != null) {
            var6.addTransition((Transition)var1);
         }

         var6.addTransition(var7);
         return var6;
      } else {
         return var1;
      }
   }

   public Object mergeTransitionsTogether(Object var1, Object var2, Object var3) {
      TransitionSet var4 = new TransitionSet();
      if (var1 != null) {
         var4.addTransition((Transition)var1);
      }

      if (var2 != null) {
         var4.addTransition((Transition)var2);
      }

      if (var3 != null) {
         var4.addTransition((Transition)var3);
      }

      return var4;
   }

   public void removeTarget(Object var1, View var2) {
      if (var1 != null) {
         ((Transition)var1).removeTarget(var2);
      }

   }

   public void replaceTargets(Object var1, ArrayList var2, ArrayList var3) {
      Transition var7 = (Transition)var1;
      int var4;
      int var5;
      if (var7 instanceof TransitionSet) {
         TransitionSet var8 = (TransitionSet)var7;
         var5 = var8.getTransitionCount();

         for(var4 = 0; var4 < var5; ++var4) {
            this.replaceTargets(var8.getTransitionAt(var4), var2, var3);
         }
      } else if (!hasSimpleTarget(var7)) {
         List var6 = var7.getTargets();
         if (var6.size() == var2.size() && var6.containsAll(var2)) {
            if (var3 == null) {
               var4 = 0;
            } else {
               var4 = var3.size();
            }

            for(var5 = 0; var5 < var4; ++var5) {
               var7.addTarget((View)var3.get(var5));
            }

            for(var4 = var2.size() - 1; var4 >= 0; --var4) {
               var7.removeTarget((View)var2.get(var4));
            }
         }
      }

   }

   public void scheduleHideFragmentView(Object var1, final View var2, final ArrayList var3) {
      ((Transition)var1).addListener(new Transition.TransitionListener() {
         public void onTransitionCancel(Transition var1) {
         }

         public void onTransitionEnd(Transition var1) {
            var1.removeListener(this);
            var2.setVisibility(8);
            int var3x = var3.size();

            for(int var2x = 0; var2x < var3x; ++var2x) {
               ((View)var3.get(var2x)).setVisibility(0);
            }

         }

         public void onTransitionPause(Transition var1) {
         }

         public void onTransitionResume(Transition var1) {
         }

         public void onTransitionStart(Transition var1) {
         }
      });
   }

   public void scheduleRemoveTargets(Object var1, final Object var2, final ArrayList var3, final Object var4, final ArrayList var5, final Object var6, final ArrayList var7) {
      ((Transition)var1).addListener(new TransitionListenerAdapter() {
         public void onTransitionEnd(Transition var1) {
            var1.removeListener(this);
         }

         public void onTransitionStart(Transition var1) {
            Object var2x = var2;
            if (var2x != null) {
               FragmentTransitionSupport.this.replaceTargets(var2x, var3, (ArrayList)null);
            }

            var2x = var4;
            if (var2x != null) {
               FragmentTransitionSupport.this.replaceTargets(var2x, var5, (ArrayList)null);
            }

            var2x = var6;
            if (var2x != null) {
               FragmentTransitionSupport.this.replaceTargets(var2x, var7, (ArrayList)null);
            }

         }
      });
   }

   public void setEpicenter(Object var1, final Rect var2) {
      if (var1 != null) {
         ((Transition)var1).setEpicenterCallback(new Transition.EpicenterCallback() {
            public Rect onGetEpicenter(Transition var1) {
               Rect var2x = var2;
               return var2x != null && !var2x.isEmpty() ? var2 : null;
            }
         });
      }

   }

   public void setEpicenter(Object var1, View var2) {
      if (var2 != null) {
         Transition var4 = (Transition)var1;
         final Rect var3 = new Rect();
         this.getBoundsOnScreen(var2, var3);
         var4.setEpicenterCallback(new Transition.EpicenterCallback() {
            public Rect onGetEpicenter(Transition var1) {
               return var3;
            }
         });
      }

   }

   public void setSharedElementTargets(Object var1, View var2, ArrayList var3) {
      TransitionSet var7 = (TransitionSet)var1;
      List var6 = var7.getTargets();
      var6.clear();
      int var5 = var3.size();

      for(int var4 = 0; var4 < var5; ++var4) {
         bfsAddViewChildren(var6, (View)var3.get(var4));
      }

      var6.add(var2);
      var3.add(var2);
      this.addTargets(var7, var3);
   }

   public void swapSharedElementTargets(Object var1, ArrayList var2, ArrayList var3) {
      TransitionSet var4 = (TransitionSet)var1;
      if (var4 != null) {
         var4.getTargets().clear();
         var4.getTargets().addAll(var3);
         this.replaceTargets(var4, var2, var3);
      }

   }

   public Object wrapTransitionInSet(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         TransitionSet var2 = new TransitionSet();
         var2.addTransition((Transition)var1);
         return var2;
      }
   }
}
