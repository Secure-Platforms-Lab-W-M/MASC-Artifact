package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(19)
@RequiresApi(19)
class SceneKitKat extends SceneWrapper {
   private static Field sEnterAction;
   private static Method sSetCurrentScene;
   private View mLayout;

   private void invokeEnterAction() {
      if (sEnterAction == null) {
         try {
            sEnterAction = android.transition.Scene.class.getDeclaredField("mEnterAction");
            sEnterAction.setAccessible(true);
         } catch (NoSuchFieldException var2) {
            throw new RuntimeException(var2);
         }
      }

      IllegalAccessException var10000;
      label32: {
         Runnable var1;
         boolean var10001;
         try {
            var1 = (Runnable)sEnterAction.get(this.mScene);
         } catch (IllegalAccessException var4) {
            var10000 = var4;
            var10001 = false;
            break label32;
         }

         if (var1 == null) {
            return;
         }

         try {
            var1.run();
            return;
         } catch (IllegalAccessException var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      IllegalAccessException var5 = var10000;
      throw new RuntimeException(var5);
   }

   private void updateCurrentScene(View var1) {
      if (sSetCurrentScene == null) {
         try {
            sSetCurrentScene = android.transition.Scene.class.getDeclaredMethod("setCurrentScene", View.class, android.transition.Scene.class);
            sSetCurrentScene.setAccessible(true);
         } catch (NoSuchMethodException var4) {
            throw new RuntimeException(var4);
         }
      }

      Object var5;
      try {
         sSetCurrentScene.invoke((Object)null, var1, this.mScene);
         return;
      } catch (IllegalAccessException var2) {
         var5 = var2;
      } catch (InvocationTargetException var3) {
         var5 = var3;
      }

      throw new RuntimeException((Throwable)var5);
   }

   public void enter() {
      if (this.mLayout != null) {
         ViewGroup var1 = this.getSceneRoot();
         var1.removeAllViews();
         var1.addView(this.mLayout);
         this.invokeEnterAction();
         this.updateCurrentScene(var1);
      } else {
         this.mScene.enter();
      }
   }

   public void init(ViewGroup var1) {
      this.mScene = new android.transition.Scene(var1);
   }

   public void init(ViewGroup var1, View var2) {
      if (var2 instanceof ViewGroup) {
         this.mScene = new android.transition.Scene(var1, (ViewGroup)var2);
      } else {
         this.mScene = new android.transition.Scene(var1);
         this.mLayout = var2;
      }
   }
}
