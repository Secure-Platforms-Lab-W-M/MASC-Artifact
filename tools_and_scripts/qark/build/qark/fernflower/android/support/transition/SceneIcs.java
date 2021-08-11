package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class SceneIcs extends SceneImpl {
   ScenePort mScene;

   public void enter() {
      this.mScene.enter();
   }

   public void exit() {
      this.mScene.exit();
   }

   public ViewGroup getSceneRoot() {
      return this.mScene.getSceneRoot();
   }

   public void init(ViewGroup var1) {
      this.mScene = new ScenePort(var1);
   }

   public void init(ViewGroup var1, View var2) {
      this.mScene = new ScenePort(var1, var2);
   }

   public void setEnterAction(Runnable var1) {
      this.mScene.setEnterAction(var1);
   }

   public void setExitAction(Runnable var1) {
      this.mScene.setExitAction(var1);
   }
}
