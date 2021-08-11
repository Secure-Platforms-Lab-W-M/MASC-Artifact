package android.support.transition;

abstract class TransitionManagerImpl {
   public abstract void setTransition(SceneImpl var1, SceneImpl var2, TransitionImpl var3);

   public abstract void setTransition(SceneImpl var1, TransitionImpl var2);

   public abstract void transitionTo(SceneImpl var1);
}
