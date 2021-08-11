package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@TargetApi(14)
@RequiresApi(14)
class TransitionManagerIcs extends TransitionManagerImpl {
   private final TransitionManagerPort mTransitionManager = new TransitionManagerPort();

   public void setTransition(SceneImpl var1, SceneImpl var2, TransitionImpl var3) {
      TransitionManagerPort var4 = this.mTransitionManager;
      ScenePort var5 = ((SceneIcs)var1).mScene;
      ScenePort var7 = ((SceneIcs)var2).mScene;
      TransitionPort var6;
      if (var3 == null) {
         var6 = null;
      } else {
         var6 = ((TransitionIcs)var3).mTransition;
      }

      var4.setTransition(var5, var7, var6);
   }

   public void setTransition(SceneImpl var1, TransitionImpl var2) {
      TransitionManagerPort var3 = this.mTransitionManager;
      ScenePort var4 = ((SceneIcs)var1).mScene;
      TransitionPort var5;
      if (var2 == null) {
         var5 = null;
      } else {
         var5 = ((TransitionIcs)var2).mTransition;
      }

      var3.setTransition(var4, var5);
   }

   public void transitionTo(SceneImpl var1) {
      this.mTransitionManager.transitionTo(((SceneIcs)var1).mScene);
   }
}
