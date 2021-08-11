package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(21)
@RequiresApi(21)
class SceneStaticsApi21 extends SceneStaticsImpl {
   public SceneImpl getSceneForLayout(ViewGroup var1, int var2, Context var3) {
      SceneApi21 var4 = new SceneApi21();
      var4.mScene = android.transition.Scene.getSceneForLayout(var1, var2, var3);
      return var4;
   }
}
