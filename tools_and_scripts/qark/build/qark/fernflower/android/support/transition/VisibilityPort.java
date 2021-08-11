package android.support.transition;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
abstract class VisibilityPort extends TransitionPort {
   private static final String PROPNAME_PARENT = "android:visibility:parent";
   private static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
   private static final String[] sTransitionProperties = new String[]{"android:visibility:visibility", "android:visibility:parent"};

   private void captureValues(TransitionValues var1) {
      int var2 = var1.view.getVisibility();
      var1.values.put("android:visibility:visibility", var2);
      var1.values.put("android:visibility:parent", var1.view.getParent());
   }

   private VisibilityPort.VisibilityInfo getVisibilityChangeInfo(TransitionValues var1, TransitionValues var2) {
      VisibilityPort.VisibilityInfo var3 = new VisibilityPort.VisibilityInfo();
      var3.visibilityChange = false;
      var3.fadeIn = false;
      if (var1 != null) {
         var3.startVisibility = (Integer)var1.values.get("android:visibility:visibility");
         var3.startParent = (ViewGroup)var1.values.get("android:visibility:parent");
      } else {
         var3.startVisibility = -1;
         var3.startParent = null;
      }

      if (var2 != null) {
         var3.endVisibility = (Integer)var2.values.get("android:visibility:visibility");
         var3.endParent = (ViewGroup)var2.values.get("android:visibility:parent");
      } else {
         var3.endVisibility = -1;
         var3.endParent = null;
      }

      if (var1 != null && var2 != null) {
         if (var3.startVisibility == var3.endVisibility && var3.startParent == var3.endParent) {
            return var3;
         }

         if (var3.startVisibility != var3.endVisibility) {
            if (var3.startVisibility == 0) {
               var3.fadeIn = false;
               var3.visibilityChange = true;
            } else if (var3.endVisibility == 0) {
               var3.fadeIn = true;
               var3.visibilityChange = true;
            }
         } else if (var3.startParent != var3.endParent) {
            if (var3.endParent == null) {
               var3.fadeIn = false;
               var3.visibilityChange = true;
            } else if (var3.startParent == null) {
               var3.fadeIn = true;
               var3.visibilityChange = true;
            }
         }
      }

      if (var1 == null) {
         var3.fadeIn = true;
         var3.visibilityChange = true;
         return var3;
      } else if (var2 == null) {
         var3.fadeIn = false;
         var3.visibilityChange = true;
         return var3;
      } else {
         return var3;
      }
   }

   public void captureEndValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      int var5 = -1;
      Object var8 = null;
      VisibilityPort.VisibilityInfo var9 = this.getVisibilityChangeInfo(var2, var3);
      Animator var6 = (Animator)var8;
      if (var9.visibilityChange) {
         boolean var4 = false;
         if (this.mTargets.size() > 0 || this.mTargetIds.size() > 0) {
            View var11;
            if (var2 != null) {
               var11 = var2.view;
            } else {
               var11 = null;
            }

            View var7;
            if (var3 != null) {
               var7 = var3.view;
            } else {
               var7 = null;
            }

            int var10;
            if (var11 != null) {
               var10 = var11.getId();
            } else {
               var10 = -1;
            }

            if (var7 != null) {
               var5 = var7.getId();
            }

            if (!this.isValidTarget(var11, (long)var10) && !this.isValidTarget(var7, (long)var5)) {
               var4 = false;
            } else {
               var4 = true;
            }
         }

         if (!var4 && var9.startParent == null) {
            var6 = (Animator)var8;
            if (var9.endParent == null) {
               return var6;
            }
         }

         if (!var9.fadeIn) {
            return this.onDisappear(var1, var2, var9.startVisibility, var3, var9.endVisibility);
         }

         var6 = this.onAppear(var1, var2, var9.startVisibility, var3, var9.endVisibility);
      }

      return var6;
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }

   public boolean isVisible(TransitionValues var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2 = (Integer)var1.values.get("android:visibility:visibility");
         View var4 = (View)var1.values.get("android:visibility:parent");
         boolean var3;
         if (var2 == 0 && var4 != null) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return null;
   }

   public Animator onDisappear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      return null;
   }

   private static class VisibilityInfo {
      ViewGroup endParent;
      int endVisibility;
      boolean fadeIn;
      ViewGroup startParent;
      int startVisibility;
      boolean visibilityChange;

      VisibilityInfo() {
      }
   }
}
