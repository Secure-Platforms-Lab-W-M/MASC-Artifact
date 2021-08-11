package androidx.transition;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class TransitionManager {
   private static final String LOG_TAG = "TransitionManager";
   private static Transition sDefaultTransition = new AutoTransition();
   static ArrayList sPendingTransitions = new ArrayList();
   private static ThreadLocal sRunningTransitions = new ThreadLocal();
   private ArrayMap mScenePairTransitions = new ArrayMap();
   private ArrayMap mSceneTransitions = new ArrayMap();

   public static void beginDelayedTransition(ViewGroup var0) {
      beginDelayedTransition(var0, (Transition)null);
   }

   public static void beginDelayedTransition(ViewGroup var0, Transition var1) {
      if (!sPendingTransitions.contains(var0) && ViewCompat.isLaidOut(var0)) {
         sPendingTransitions.add(var0);
         Transition var2 = var1;
         if (var1 == null) {
            var2 = sDefaultTransition;
         }

         var1 = var2.clone();
         sceneChangeSetup(var0, var1);
         Scene.setCurrentScene(var0, (Scene)null);
         sceneChangeRunTransition(var0, var1);
      }

   }

   private static void changeScene(Scene var0, Transition var1) {
      ViewGroup var2 = var0.getSceneRoot();
      if (!sPendingTransitions.contains(var2)) {
         Scene var3 = Scene.getCurrentScene(var2);
         if (var1 == null) {
            if (var3 != null) {
               var3.exit();
            }

            var0.enter();
            return;
         }

         sPendingTransitions.add(var2);
         var1 = var1.clone();
         var1.setSceneRoot(var2);
         if (var3 != null && var3.isCreatedFromLayoutResource()) {
            var1.setCanRemoveViews(true);
         }

         sceneChangeSetup(var2, var1);
         var0.enter();
         sceneChangeRunTransition(var2, var1);
      }

   }

   public static void endTransitions(ViewGroup var0) {
      sPendingTransitions.remove(var0);
      ArrayList var2 = (ArrayList)getRunningTransitions().get(var0);
      if (var2 != null && !var2.isEmpty()) {
         var2 = new ArrayList(var2);

         for(int var1 = var2.size() - 1; var1 >= 0; --var1) {
            ((Transition)var2.get(var1)).forceToEnd(var0);
         }
      }

   }

   static ArrayMap getRunningTransitions() {
      WeakReference var0 = (WeakReference)sRunningTransitions.get();
      ArrayMap var2;
      if (var0 != null) {
         var2 = (ArrayMap)var0.get();
         if (var2 != null) {
            return var2;
         }
      }

      var2 = new ArrayMap();
      WeakReference var1 = new WeakReference(var2);
      sRunningTransitions.set(var1);
      return var2;
   }

   private Transition getTransition(Scene var1) {
      ViewGroup var2 = var1.getSceneRoot();
      if (var2 != null) {
         Scene var5 = Scene.getCurrentScene(var2);
         if (var5 != null) {
            ArrayMap var3 = (ArrayMap)this.mScenePairTransitions.get(var1);
            if (var3 != null) {
               Transition var6 = (Transition)var3.get(var5);
               if (var6 != null) {
                  return var6;
               }
            }
         }
      }

      Transition var4 = (Transition)this.mSceneTransitions.get(var1);
      return var4 != null ? var4 : sDefaultTransition;
   }

   // $FF: renamed from: go (androidx.transition.Scene) void
   public static void method_11(Scene var0) {
      changeScene(var0, sDefaultTransition);
   }

   // $FF: renamed from: go (androidx.transition.Scene, androidx.transition.Transition) void
   public static void method_12(Scene var0, Transition var1) {
      changeScene(var0, var1);
   }

   private static void sceneChangeRunTransition(ViewGroup var0, Transition var1) {
      if (var1 != null && var0 != null) {
         TransitionManager.MultiListener var2 = new TransitionManager.MultiListener(var1, var0);
         var0.addOnAttachStateChangeListener(var2);
         var0.getViewTreeObserver().addOnPreDrawListener(var2);
      }

   }

   private static void sceneChangeSetup(ViewGroup var0, Transition var1) {
      ArrayList var2 = (ArrayList)getRunningTransitions().get(var0);
      if (var2 != null && var2.size() > 0) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            ((Transition)var4.next()).pause(var0);
         }
      }

      if (var1 != null) {
         var1.captureValues(var0, true);
      }

      Scene var3 = Scene.getCurrentScene(var0);
      if (var3 != null) {
         var3.exit();
      }

   }

   public void setTransition(Scene var1, Scene var2, Transition var3) {
      ArrayMap var5 = (ArrayMap)this.mScenePairTransitions.get(var2);
      ArrayMap var4 = var5;
      if (var5 == null) {
         var4 = new ArrayMap();
         this.mScenePairTransitions.put(var2, var4);
      }

      var4.put(var1, var3);
   }

   public void setTransition(Scene var1, Transition var2) {
      this.mSceneTransitions.put(var1, var2);
   }

   public void transitionTo(Scene var1) {
      changeScene(var1, this.getTransition(var1));
   }

   private static class MultiListener implements OnPreDrawListener, OnAttachStateChangeListener {
      ViewGroup mSceneRoot;
      Transition mTransition;

      MultiListener(Transition var1, ViewGroup var2) {
         this.mTransition = var1;
         this.mSceneRoot = var2;
      }

      private void removeListeners() {
         this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
         this.mSceneRoot.removeOnAttachStateChangeListener(this);
      }

      public boolean onPreDraw() {
         this.removeListeners();
         if (!TransitionManager.sPendingTransitions.remove(this.mSceneRoot)) {
            return true;
         } else {
            final ArrayMap var4 = TransitionManager.getRunningTransitions();
            ArrayList var3 = (ArrayList)var4.get(this.mSceneRoot);
            ArrayList var2 = null;
            ArrayList var1;
            if (var3 == null) {
               var1 = new ArrayList();
               var4.put(this.mSceneRoot, var1);
            } else {
               var1 = var3;
               if (var3.size() > 0) {
                  var2 = new ArrayList(var3);
                  var1 = var3;
               }
            }

            var1.add(this.mTransition);
            this.mTransition.addListener(new TransitionListenerAdapter() {
               public void onTransitionEnd(Transition var1) {
                  ((ArrayList)var4.get(MultiListener.this.mSceneRoot)).remove(var1);
                  var1.removeListener(this);
               }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (var2 != null) {
               Iterator var5 = var2.iterator();

               while(var5.hasNext()) {
                  ((Transition)var5.next()).resume(this.mSceneRoot);
               }
            }

            this.mTransition.playTransition(this.mSceneRoot);
            return true;
         }
      }

      public void onViewAttachedToWindow(View var1) {
      }

      public void onViewDetachedFromWindow(View var1) {
         this.removeListeners();
         TransitionManager.sPendingTransitions.remove(this.mSceneRoot);
         ArrayList var2 = (ArrayList)TransitionManager.getRunningTransitions().get(this.mSceneRoot);
         if (var2 != null && var2.size() > 0) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ((Transition)var3.next()).resume(this.mSceneRoot);
            }
         }

         this.mTransition.clearValues(true);
      }
   }
}
