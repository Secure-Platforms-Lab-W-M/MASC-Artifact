// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.util.Iterator;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.view.View$OnAttachStateChangeListener;
import java.util.Collection;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import java.lang.ref.WeakReference;
import android.view.ViewGroup;
import java.util.ArrayList;

public class TransitionManager
{
    private static final String LOG_TAG = "TransitionManager";
    private static Transition sDefaultTransition;
    private static ArrayList<ViewGroup> sPendingTransitions;
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions;
    private ArrayMap<Scene, ArrayMap<Scene, Transition>> mScenePairTransitions;
    private ArrayMap<Scene, Transition> mSceneTransitions;
    
    static {
        TransitionManager.sDefaultTransition = new AutoTransition();
        TransitionManager.sRunningTransitions = new ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>>();
        TransitionManager.sPendingTransitions = new ArrayList<ViewGroup>();
    }
    
    public TransitionManager() {
        this.mSceneTransitions = new ArrayMap<Scene, Transition>();
        this.mScenePairTransitions = new ArrayMap<Scene, ArrayMap<Scene, Transition>>();
    }
    
    public static void beginDelayedTransition(@NonNull final ViewGroup viewGroup) {
        beginDelayedTransition(viewGroup, null);
    }
    
    public static void beginDelayedTransition(@NonNull final ViewGroup viewGroup, @Nullable Transition transition) {
        if (!TransitionManager.sPendingTransitions.contains(viewGroup) && ViewCompat.isLaidOut((View)viewGroup)) {
            TransitionManager.sPendingTransitions.add(viewGroup);
            if (transition == null) {
                transition = TransitionManager.sDefaultTransition;
            }
            transition = transition.clone();
            sceneChangeSetup(viewGroup, transition);
            Scene.setCurrentScene((View)viewGroup, null);
            sceneChangeRunTransition(viewGroup, transition);
        }
    }
    
    private static void changeScene(final Scene scene, Transition clone) {
        final ViewGroup sceneRoot = scene.getSceneRoot();
        if (TransitionManager.sPendingTransitions.contains(sceneRoot)) {
            return;
        }
        if (clone == null) {
            scene.enter();
            return;
        }
        TransitionManager.sPendingTransitions.add(sceneRoot);
        clone = clone.clone();
        clone.setSceneRoot(sceneRoot);
        final Scene currentScene = Scene.getCurrentScene((View)sceneRoot);
        if (currentScene != null && currentScene.isCreatedFromLayoutResource()) {
            clone.setCanRemoveViews(true);
        }
        sceneChangeSetup(sceneRoot, clone);
        scene.enter();
        sceneChangeRunTransition(sceneRoot, clone);
    }
    
    public static void endTransitions(final ViewGroup viewGroup) {
        TransitionManager.sPendingTransitions.remove(viewGroup);
        final ArrayList<? extends Transition> list = getRunningTransitions().get(viewGroup);
        if (list != null && !list.isEmpty()) {
            final ArrayList list2 = new ArrayList<Transition>(list);
            for (int i = list2.size() - 1; i >= 0; --i) {
                list2.get(i).forceToEnd(viewGroup);
            }
        }
    }
    
    static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>> weakReference = TransitionManager.sRunningTransitions.get();
        if (weakReference == null || weakReference.get() == null) {
            weakReference = new WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>(new ArrayMap<ViewGroup, ArrayList<Transition>>());
            TransitionManager.sRunningTransitions.set(weakReference);
        }
        return (ArrayMap<ViewGroup, ArrayList<Transition>>)weakReference.get();
    }
    
    private Transition getTransition(final Scene scene) {
        final ViewGroup sceneRoot = scene.getSceneRoot();
        if (sceneRoot != null) {
            final Scene currentScene = Scene.getCurrentScene((View)sceneRoot);
            if (currentScene != null) {
                final ArrayMap<Object, Transition> arrayMap = this.mScenePairTransitions.get(scene);
                if (arrayMap != null) {
                    final Transition transition = arrayMap.get(currentScene);
                    if (transition != null) {
                        return transition;
                    }
                }
            }
        }
        final Transition transition2 = this.mSceneTransitions.get(scene);
        if (transition2 != null) {
            return transition2;
        }
        return TransitionManager.sDefaultTransition;
    }
    
    public static void go(@NonNull final Scene scene) {
        changeScene(scene, TransitionManager.sDefaultTransition);
    }
    
    public static void go(@NonNull final Scene scene, @Nullable final Transition transition) {
        changeScene(scene, transition);
    }
    
    private static void sceneChangeRunTransition(final ViewGroup viewGroup, final Transition transition) {
        if (transition != null && viewGroup != null) {
            final MultiListener multiListener = new MultiListener(transition, viewGroup);
            viewGroup.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)multiListener);
            viewGroup.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)multiListener);
        }
    }
    
    private static void sceneChangeSetup(final ViewGroup viewGroup, final Transition transition) {
        final ArrayList<Transition> list = getRunningTransitions().get(viewGroup);
        if (list != null && list.size() > 0) {
            final Iterator<Transition> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next().pause((View)viewGroup);
            }
        }
        if (transition != null) {
            transition.captureValues(viewGroup, true);
        }
        final Scene currentScene = Scene.getCurrentScene((View)viewGroup);
        if (currentScene != null) {
            currentScene.exit();
        }
    }
    
    public void setTransition(@NonNull final Scene scene, @NonNull final Scene scene2, @Nullable final Transition transition) {
        final ArrayMap<Scene, Transition> arrayMap = this.mScenePairTransitions.get(scene2);
        ArrayMap<Scene, Transition> arrayMap3;
        if (arrayMap == null) {
            final ArrayMap<Scene, Transition> arrayMap2 = new ArrayMap<Scene, Transition>();
            this.mScenePairTransitions.put(scene2, arrayMap2);
            arrayMap3 = arrayMap2;
        }
        else {
            arrayMap3 = arrayMap;
        }
        arrayMap3.put(scene, transition);
    }
    
    public void setTransition(@NonNull final Scene scene, @Nullable final Transition transition) {
        this.mSceneTransitions.put(scene, transition);
    }
    
    public void transitionTo(@NonNull final Scene scene) {
        changeScene(scene, this.getTransition(scene));
    }
    
    private static class MultiListener implements ViewTreeObserver$OnPreDrawListener, View$OnAttachStateChangeListener
    {
        ViewGroup mSceneRoot;
        Transition mTransition;
        
        MultiListener(final Transition mTransition, final ViewGroup mSceneRoot) {
            this.mTransition = mTransition;
            this.mSceneRoot = mSceneRoot;
        }
        
        private void removeListeners() {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
            this.mSceneRoot.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        }
        
        public boolean onPreDraw() {
            this.removeListeners();
            if (!TransitionManager.sPendingTransitions.remove(this.mSceneRoot)) {
                return true;
            }
            final ArrayMap<ViewGroup, ArrayList<Transition>> runningTransitions = TransitionManager.getRunningTransitions();
            ArrayList<Transition> list = runningTransitions.get(this.mSceneRoot);
            ArrayList<Transition> list2 = null;
            if (list == null) {
                list = new ArrayList<Transition>();
                runningTransitions.put(this.mSceneRoot, list);
            }
            else if (list.size() > 0) {
                list2 = new ArrayList<Transition>(list);
            }
            list.add(this.mTransition);
            this.mTransition.addListener((Transition.TransitionListener)new TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(@NonNull final Transition transition) {
                    ((ArrayList)runningTransitions.get(MultiListener.this.mSceneRoot)).remove(transition);
                }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (list2 != null) {
                final Iterator<Transition> iterator = list2.iterator();
                while (iterator.hasNext()) {
                    iterator.next().resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.playTransition(this.mSceneRoot);
            return true;
        }
        
        public void onViewAttachedToWindow(final View view) {
        }
        
        public void onViewDetachedFromWindow(final View view) {
            this.removeListeners();
            TransitionManager.sPendingTransitions.remove(this.mSceneRoot);
            final ArrayList<Transition> list = TransitionManager.getRunningTransitions().get(this.mSceneRoot);
            if (list != null && list.size() > 0) {
                final Iterator<Transition> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }
    }
}
