package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class SceneStaticsIcs extends SceneStaticsImpl {
   public SceneImpl getSceneForLayout(ViewGroup var1, int var2, Context var3) {
      SceneIcs var4 = new SceneIcs();
      var4.mScene = ScenePort.getSceneForLayout(var1, var2, var3);
      return var4;
   }
}
