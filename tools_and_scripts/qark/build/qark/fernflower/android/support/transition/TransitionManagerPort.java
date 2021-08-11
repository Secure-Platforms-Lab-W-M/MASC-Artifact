package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(14)
@RequiresApi(14)
class TransitionManagerPort {
   private static final String[] EMPTY_STRINGS = new String[0];
   private static String LOG_TAG = "TransitionManager";
   private static TransitionPort sDefaultTransition = new AutoTransitionPort();
   static ArrayList sPendingTransitions = new ArrayList();
   private static ThreadLocal sRunningTransitions = new ThreadLocal();
   ArrayMap mNameSceneTransitions = new ArrayMap();
   ArrayMap mSceneNameTransitions = new ArrayMap();
   ArrayMap mScenePairTransitions = new ArrayMap();
   ArrayMap mSceneTransitions = new ArrayMap();

   public static void beginDelayedTransition(ViewGroup var0) {
      beginDelayedTransition(var0, (TransitionPort)null);
   }

   public static void beginDelayedTransition(ViewGroup var0, TransitionPort var1) {
      if (!sPendingTransitions.contains(var0) && ViewCompat.isLaidOut(var0)) {
         sPendingTransitions.add(var0);
         TransitionPort var2 = var1;
         if (var1 == null) {
            var2 = sDefaultTransition;
         }

         var1 = var2.clone();
         sceneChangeSetup(var0, var1);
         ScenePort.setCurrentScene(var0, (ScenePort)null);
         sceneChangeRunTransition(var0, var1);
      }

   }

   private static void changeScene(ScenePort var0, TransitionPort var1) {
      ViewGroup var3 = var0.getSceneRoot();
      TransitionPort var2 = null;
      if (var1 != null) {
         var2 = var1.clone();
         var2.setSceneRoot(var3);
      }

      ScenePort var4 = ScenePort.getCurrentScene(var3);
      if (var4 != null && var4.isCreatedFromLayoutResource()) {
         var2.setCanRemoveViews(true);
      }

      sceneChangeSetup(var3, var2);
      var0.enter();
      sceneChangeRunTransition(var3, var2);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static TransitionPort getDefaultTransition() {
      return sDefaultTransition;
   }

   static ArrayMap getRunningTransitions() {
      WeakReference var1 = (WeakReference)sRunningTransitions.get();
      WeakReference var0;
      if (var1 != null) {
         var0 = var1;
         if (var1.get() != null) {
            return (ArrayMap)var0.get();
         }
      }

      var0 = new WeakReference(new ArrayMap());
      sRunningTransitions.set(var0);
      return (ArrayMap)var0.get();
   }

   private TransitionPort getTransition(ScenePort var1) {
      ViewGroup var2 = var1.getSceneRoot();
      if (var2 != null) {
         ScenePort var5 = ScenePort.getCurrentScene(var2);
         if (var5 != null) {
            ArrayMap var3 = (ArrayMap)this.mScenePairTransitions.get(var1);
            if (var3 != null) {
               TransitionPort var6 = (TransitionPort)var3.get(var5);
               if (var6 != null) {
                  return var6;
               }
            }
         }
      }

      TransitionPort var4 = (TransitionPort)this.mSceneTransitions.get(var1);
      return var4 != null ? var4 : sDefaultTransition;
   }

   // $FF: renamed from: go (android.support.transition.ScenePort) void
   public static void method_10(ScenePort var0) {
      changeScene(var0, sDefaultTransition);
   }

   // $FF: renamed from: go (android.support.transition.ScenePort, android.support.transition.TransitionPort) void
   public static void method_11(ScenePort var0, TransitionPort var1) {
      changeScene(var0, var1);
   }

   private static void sceneChangeRunTransition(ViewGroup var0, TransitionPort var1) {
      if (var1 != null && var0 != null) {
         TransitionManagerPort.MultiListener var2 = new TransitionManagerPort.MultiListener(var1, var0);
         var0.addOnAttachStateChangeListener(var2);
         var0.getViewTreeObserver().addOnPreDrawListener(var2);
      }

   }

   private static void sceneChangeSetup(ViewGroup var0, TransitionPort var1) {
      ArrayList var2 = (ArrayList)getRunningTransitions().get(var0);
      if (var2 != null && var2.size() > 0) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            ((TransitionPort)var4.next()).pause(var0);
         }
      }

      if (var1 != null) {
         var1.captureValues(var0, true);
      }

      ScenePort var3 = ScenePort.getCurrentScene(var0);
      if (var3 != null) {
         var3.exit();
      }

   }

   public TransitionPort getNamedTransition(ScenePort var1, String var2) {
      ArrayMap var3 = (ArrayMap)this.mSceneNameTransitions.get(var1);
      return var3 != null ? (TransitionPort)var3.get(var2) : null;
   }

   public TransitionPort getNamedTransition(String var1, ScenePort var2) {
      ArrayMap var3 = (ArrayMap)this.mNameSceneTransitions.get(var1);
      return var3 != null ? (TransitionPort)var3.get(var2) : null;
   }

   public String[] getTargetSceneNames(ScenePort var1) {
      ArrayMap var5 = (ArrayMap)this.mSceneNameTransitions.get(var1);
      String[] var6;
      if (var5 == null) {
         var6 = EMPTY_STRINGS;
      } else {
         int var3 = var5.size();
         String[] var4 = new String[var3];
         int var2 = 0;

         while(true) {
            var6 = var4;
            if (var2 >= var3) {
               break;
            }

            var4[var2] = (String)var5.keyAt(var2);
            ++var2;
         }
      }

      return var6;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setDefaultTransition(TransitionPort var1) {
      sDefaultTransition = var1;
   }

   public void setTransition(ScenePort var1, ScenePort var2, TransitionPort var3) {
      ArrayMap var5 = (ArrayMap)this.mScenePairTransitions.get(var2);
      ArrayMap var4 = var5;
      if (var5 == null) {
         var4 = new ArrayMap();
         this.mScenePairTransitions.put(var2, var4);
      }

      var4.put(var1, var3);
   }

   public void setTransition(ScenePort var1, TransitionPort var2) {
      this.mSceneTransitions.put(var1, var2);
   }

   public void setTransition(ScenePort var1, String var2, TransitionPort var3) {
      ArrayMap var5 = (ArrayMap)this.mSceneNameTransitions.get(var1);
      ArrayMap var4 = var5;
      if (var5 == null) {
         var4 = new ArrayMap();
         this.mSceneNameTransitions.put(var1, var4);
      }

      var4.put(var2, var3);
   }

   public void setTransition(String var1, ScenePort var2, TransitionPort var3) {
      ArrayMap var5 = (ArrayMap)this.mNameSceneTransitions.get(var1);
      ArrayMap var4 = var5;
      if (var5 == null) {
         var4 = new ArrayMap();
         this.mNameSceneTransitions.put(var1, var4);
      }

      var4.put(var2, var3);
   }

   public void transitionTo(ScenePort var1) {
      changeScene(var1, this.getTransition(var1));
   }

   private static class MultiListener implements OnPreDrawListener, OnAttachStateChangeListener {
      ViewGroup mSceneRoot;
      TransitionPort mTransition;

      MultiListener(TransitionPort var1, ViewGroup var2) {
         this.mTransition = var1;
         this.mSceneRoot = var2;
      }

      private void removeListeners() {
         this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
         this.mSceneRoot.removeOnAttachStateChangeListener(this);
      }

      public boolean onPreDraw() {
         this.removeListeners();
         TransitionManagerPort.sPendingTransitions.remove(this.mSceneRoot);
         final ArrayMap var4 = TransitionManagerPort.getRunningTransitions();
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
         this.mTransition.addListener(new TransitionPort.TransitionListenerAdapter() {
            public void onTransitionEnd(TransitionPort var1) {
               ((ArrayList)var4.get(MultiListener.this.mSceneRoot)).remove(var1);
            }
         });
         this.mTransition.captureValues(this.mSceneRoot, false);
         if (var2 != null) {
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               ((TransitionPort)var5.next()).resume(this.mSceneRoot);
            }
         }

         this.mTransition.playTransition(this.mSceneRoot);
         return true;
      }

      public void onViewAttachedToWindow(View var1) {
      }

      public void onViewDetachedFromWindow(View var1) {
         this.removeListeners();
         TransitionManagerPort.sPendingTransitions.remove(this.mSceneRoot);
         ArrayList var2 = (ArrayList)TransitionManagerPort.getRunningTransitions().get(this.mSceneRoot);
         if (var2 != null && var2.size() > 0) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               ((TransitionPort)var3.next()).resume(this.mSceneRoot);
            }
         }

         this.mTransition.clearValues(true);
      }
   }
}
