package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(19)
@RequiresApi(19)
class TransitionManagerKitKat extends TransitionManagerImpl {
   private final android.transition.TransitionManager mTransitionManager = new android.transition.TransitionManager();

   public void setTransition(SceneImpl var1, SceneImpl var2, TransitionImpl var3) {
      android.transition.TransitionManager var4 = this.mTransitionManager;
      android.transition.Scene var5 = ((SceneWrapper)var1).mScene;
      android.transition.Scene var7 = ((SceneWrapper)var2).mScene;
      android.transition.Transition var6;
      if (var3 == null) {
         var6 = null;
      } else {
         var6 = ((TransitionKitKat)var3).mTransition;
      }

      var4.setTransition(var5, var7, var6);
   }

   public void setTransition(SceneImpl var1, TransitionImpl var2) {
      android.transition.TransitionManager var3 = this.mTransitionManager;
      android.transition.Scene var4 = ((SceneWrapper)var1).mScene;
      android.transition.Transition var5;
      if (var2 == null) {
         var5 = null;
      } else {
         var5 = ((TransitionKitKat)var2).mTransition;
      }

      var3.setTransition(var4, var5);
   }

   public void transitionTo(SceneImpl var1) {
      this.mTransitionManager.transitionTo(((SceneWrapper)var1).mScene);
   }
}
