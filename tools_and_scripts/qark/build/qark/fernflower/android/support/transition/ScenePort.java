package android.support.transition;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
final class ScenePort {
   private Context mContext;
   Runnable mEnterAction;
   Runnable mExitAction;
   private View mLayout;
   private int mLayoutId = -1;
   private ViewGroup mSceneRoot;

   public ScenePort(ViewGroup var1) {
      this.mSceneRoot = var1;
   }

   private ScenePort(ViewGroup var1, int var2, Context var3) {
      this.mContext = var3;
      this.mSceneRoot = var1;
      this.mLayoutId = var2;
   }

   public ScenePort(ViewGroup var1, View var2) {
      this.mSceneRoot = var1;
      this.mLayout = var2;
   }

   static ScenePort getCurrentScene(View var0) {
      return (ScenePort)var0.getTag(R$id.transition_current_scene);
   }

   public static ScenePort getSceneForLayout(ViewGroup var0, int var1, Context var2) {
      return new ScenePort(var0, var1, var2);
   }

   static void setCurrentScene(View var0, ScenePort var1) {
      var0.setTag(R$id.transition_current_scene, var1);
   }

   public void enter() {
      if (this.mLayoutId > 0 || this.mLayout != null) {
         this.getSceneRoot().removeAllViews();
         if (this.mLayoutId > 0) {
            LayoutInflater.from(this.mContext).inflate(this.mLayoutId, this.mSceneRoot);
         } else {
            this.mSceneRoot.addView(this.mLayout);
         }
      }

      if (this.mEnterAction != null) {
         this.mEnterAction.run();
      }

      setCurrentScene(this.mSceneRoot, this);
   }

   public void exit() {
      if (getCurrentScene(this.mSceneRoot) == this && this.mExitAction != null) {
         this.mExitAction.run();
      }

   }

   public ViewGroup getSceneRoot() {
      return this.mSceneRoot;
   }

   boolean isCreatedFromLayoutResource() {
      return this.mLayoutId > 0;
   }

   public void setEnterAction(Runnable var1) {
      this.mEnterAction = var1;
   }

   public void setExitAction(Runnable var1) {
      this.mExitAction = var1;
   }
}
