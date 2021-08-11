package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(19)
@RequiresApi(19)
abstract class SceneWrapper extends SceneImpl {
   android.transition.Scene mScene;

   public void exit() {
      this.mScene.exit();
   }

   public ViewGroup getSceneRoot() {
      return this.mScene.getSceneRoot();
   }

   public void setEnterAction(Runnable var1) {
      this.mScene.setEnterAction(var1);
   }

   public void setExitAction(Runnable var1) {
      this.mScene.setExitAction(var1);
   }
}
