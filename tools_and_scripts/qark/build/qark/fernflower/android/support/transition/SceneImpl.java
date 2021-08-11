package android.support.transition;

import android.view.View;
import android.view.ViewGroup;

abstract class SceneImpl {
   public abstract void enter();

   public abstract void exit();

   public abstract ViewGroup getSceneRoot();

   public abstract void init(ViewGroup var1);

   public abstract void init(ViewGroup var1, View var2);

   public abstract void setEnterAction(Runnable var1);

   public abstract void setExitAction(Runnable var1);
}
