package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(19)
@RequiresApi(19)
class SceneStaticsKitKat extends SceneStaticsImpl {
   public SceneImpl getSceneForLayout(ViewGroup var1, int var2, Context var3) {
      SceneKitKat var4 = new SceneKitKat();
      var4.mScene = android.transition.Scene.getSceneForLayout(var1, var2, var3);
      return var4;
   }
}
