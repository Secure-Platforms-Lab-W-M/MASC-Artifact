package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class TransitionManagerStaticsIcs extends TransitionManagerStaticsImpl {
   public void beginDelayedTransition(ViewGroup var1) {
      TransitionManagerPort.beginDelayedTransition(var1);
   }

   public void beginDelayedTransition(ViewGroup var1, TransitionImpl var2) {
      TransitionPort var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = ((TransitionIcs)var2).mTransition;
      }

      TransitionManagerPort.beginDelayedTransition(var1, var3);
   }

   // $FF: renamed from: go (android.support.transition.SceneImpl) void
   public void method_13(SceneImpl var1) {
      TransitionManagerPort.method_10(((SceneIcs)var1).mScene);
   }

   // $FF: renamed from: go (android.support.transition.SceneImpl, android.support.transition.TransitionImpl) void
   public void method_14(SceneImpl var1, TransitionImpl var2) {
      ScenePort var3 = ((SceneIcs)var1).mScene;
      TransitionPort var4;
      if (var2 == null) {
         var4 = null;
      } else {
         var4 = ((TransitionIcs)var2).mTransition;
      }

      TransitionManagerPort.method_11(var3, var4);
   }
}
