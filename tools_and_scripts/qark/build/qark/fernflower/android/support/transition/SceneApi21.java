package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(21)
@RequiresApi(21)
class SceneApi21 extends SceneWrapper {
   public void enter() {
      this.mScene.enter();
   }

   public void init(ViewGroup var1) {
      this.mScene = new android.transition.Scene(var1);
   }

   public void init(ViewGroup var1, View var2) {
      this.mScene = new android.transition.Scene(var1, var2);
   }
}
