package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(19)
@RequiresApi(19)
class TransitionManagerStaticsKitKat extends TransitionManagerStaticsImpl {
   public void beginDelayedTransition(ViewGroup var1) {
      android.transition.TransitionManager.beginDelayedTransition(var1);
   }

   public void beginDelayedTransition(ViewGroup var1, TransitionImpl var2) {
      android.transition.Transition var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = ((TransitionKitKat)var2).mTransition;
      }

      android.transition.TransitionManager.beginDelayedTransition(var1, var3);
   }

   // $FF: renamed from: go (android.support.transition.SceneImpl) void
   public void method_13(SceneImpl var1) {
      android.transition.TransitionManager.go(((SceneWrapper)var1).mScene);
   }

   // $FF: renamed from: go (android.support.transition.SceneImpl, android.support.transition.TransitionImpl) void
   public void method_14(SceneImpl var1, TransitionImpl var2) {
      android.transition.Scene var3 = ((SceneWrapper)var1).mScene;
      android.transition.Transition var4;
      if (var2 == null) {
         var4 = null;
      } else {
         var4 = ((TransitionKitKat)var2).mTransition;
      }

      android.transition.TransitionManager.go(var3, var4);
   }
}
