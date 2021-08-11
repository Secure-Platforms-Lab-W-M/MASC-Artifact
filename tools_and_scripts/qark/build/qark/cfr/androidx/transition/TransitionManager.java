/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnPreDrawListener
 */
package androidx.transition;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;
import androidx.transition.AutoTransition;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TransitionManager {
    private static final String LOG_TAG = "TransitionManager";
    private static Transition sDefaultTransition = new AutoTransition();
    static ArrayList<ViewGroup> sPendingTransitions;
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions;
    private ArrayMap<Scene, ArrayMap<Scene, Transition>> mScenePairTransitions = new ArrayMap();
    private ArrayMap<Scene, Transition> mSceneTransitions = new ArrayMap();

    static {
        sRunningTransitions = new ThreadLocal();
        sPendingTransitions = new ArrayList();
    }

    public static void beginDelayedTransition(ViewGroup viewGroup) {
        TransitionManager.beginDelayedTransition(viewGroup, null);
    }

    public static void beginDelayedTransition(ViewGroup viewGroup, Transition transition) {
        if (!sPendingTransitions.contains((Object)viewGroup) && ViewCompat.isLaidOut((View)viewGroup)) {
            sPendingTransitions.add(viewGroup);
            Transition transition2 = transition;
            if (transition == null) {
                transition2 = sDefaultTransition;
            }
            transition = transition2.clone();
            TransitionManager.sceneChangeSetup(viewGroup, transition);
            Scene.setCurrentScene(viewGroup, null);
            TransitionManager.sceneChangeRunTransition(viewGroup, transition);
        }
    }

    private static void changeScene(Scene scene, Transition transition) {
        ViewGroup viewGroup = scene.getSceneRoot();
        if (!sPendingTransitions.contains((Object)viewGroup)) {
            Scene scene2 = Scene.getCurrentScene(viewGroup);
            if (transition == null) {
                if (scene2 != null) {
                    scene2.exit();
                }
                scene.enter();
                return;
            }
            sPendingTransitions.add(viewGroup);
            transition = transition.clone();
            transition.setSceneRoot(viewGroup);
            if (scene2 != null && scene2.isCreatedFromLayoutResource()) {
                transition.setCanRemoveViews(true);
            }
            TransitionManager.sceneChangeSetup(viewGroup, transition);
            scene.enter();
            TransitionManager.sceneChangeRunTransition(viewGroup, transition);
        }
    }

    public static void endTransitions(ViewGroup viewGroup) {
        sPendingTransitions.remove((Object)viewGroup);
        ArrayList<Transition> arrayList = TransitionManager.getRunningTransitions().get((Object)viewGroup);
        if (arrayList != null && !arrayList.isEmpty()) {
            arrayList = new ArrayList<Transition>(arrayList);
            for (int i = arrayList.size() - 1; i >= 0; --i) {
                arrayList.get(i).forceToEnd(viewGroup);
            }
        }
    }

    static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        Object object = sRunningTransitions.get();
        if (object != null && (object = object.get()) != null) {
            return object;
        }
        object = new ArrayMap<ViewGroup, ArrayList<Transition>>();
        WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>> weakReference = new WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>((ArrayMap<ViewGroup, ArrayList<Transition>>)object);
        sRunningTransitions.set(weakReference);
        return object;
    }

    private Transition getTransition(Scene object) {
        ArrayMap<Scene, Transition> arrayMap;
        Object object2 = object.getSceneRoot();
        if (object2 != null && (object2 = Scene.getCurrentScene((ViewGroup)object2)) != null && (arrayMap = this.mScenePairTransitions.get(object)) != null && (object2 = arrayMap.get(object2)) != null) {
            return object2;
        }
        if ((object = this.mSceneTransitions.get(object)) != null) {
            return object;
        }
        return sDefaultTransition;
    }

    public static void go(Scene scene) {
        TransitionManager.changeScene(scene, sDefaultTransition);
    }

    public static void go(Scene scene, Transition transition) {
        TransitionManager.changeScene(scene, transition);
    }

    private static void sceneChangeRunTransition(ViewGroup viewGroup, Transition object) {
        if (object != null && viewGroup != null) {
            object = new MultiListener((Transition)object, viewGroup);
            viewGroup.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)object);
            viewGroup.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)object);
        }
    }

    private static void sceneChangeSetup(ViewGroup object, Transition transition) {
        ArrayList<Transition> arrayList = TransitionManager.getRunningTransitions().get(object);
        if (arrayList != null && arrayList.size() > 0) {
            arrayList = arrayList.iterator();
            while (arrayList.hasNext()) {
                ((Transition)arrayList.next()).pause((View)object);
            }
        }
        if (transition != null) {
            transition.captureValues((ViewGroup)object, true);
        }
        if ((object = Scene.getCurrentScene((ViewGroup)object)) != null) {
            object.exit();
        }
    }

    public void setTransition(Scene scene, Scene scene2, Transition transition) {
        ArrayMap<Scene, Transition> arrayMap;
        ArrayMap arrayMap2 = arrayMap = this.mScenePairTransitions.get(scene2);
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap();
            this.mScenePairTransitions.put(scene2, arrayMap2);
        }
        arrayMap2.put(scene, transition);
    }

    public void setTransition(Scene scene, Transition transition) {
        this.mSceneTransitions.put(scene, transition);
    }

    public void transitionTo(Scene scene) {
        TransitionManager.changeScene(scene, this.getTransition(scene));
    }

    private static class MultiListener
    implements ViewTreeObserver.OnPreDrawListener,
    View.OnAttachStateChangeListener {
        ViewGroup mSceneRoot;
        Transition mTransition;

        MultiListener(Transition transition, ViewGroup viewGroup) {
            this.mTransition = transition;
            this.mSceneRoot = viewGroup;
        }

        private void removeListeners() {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
            this.mSceneRoot.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
        }

        public boolean onPreDraw() {
            Object object;
            this.removeListeners();
            if (!TransitionManager.sPendingTransitions.remove((Object)this.mSceneRoot)) {
                return true;
            }
            final ArrayMap<ViewGroup, ArrayList<Transition>> arrayMap = TransitionManager.getRunningTransitions();
            ArrayList<Transition> arrayList = arrayMap.get((Object)this.mSceneRoot);
            ArrayList<Transition> arrayList2 = null;
            if (arrayList == null) {
                object = new ArrayList();
                arrayMap.put(this.mSceneRoot, (ArrayList<Transition>)object);
            } else {
                object = arrayList;
                if (arrayList.size() > 0) {
                    arrayList2 = new ArrayList<Transition>(arrayList);
                    object = arrayList;
                }
            }
            object.add(this.mTransition);
            this.mTransition.addListener(new TransitionListenerAdapter(){

                @Override
                public void onTransitionEnd(Transition transition) {
                    ((ArrayList)arrayMap.get((Object)MultiListener.this.mSceneRoot)).remove(transition);
                    transition.removeListener(this);
                }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (arrayList2 != null) {
                object = arrayList2.iterator();
                while (object.hasNext()) {
                    ((Transition)object.next()).resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.playTransition(this.mSceneRoot);
            return true;
        }

        public void onViewAttachedToWindow(View view) {
        }

        public void onViewDetachedFromWindow(View iterator) {
            this.removeListeners();
            TransitionManager.sPendingTransitions.remove((Object)this.mSceneRoot);
            iterator = TransitionManager.getRunningTransitions().get((Object)this.mSceneRoot);
            if (iterator != null && iterator.size() > 0) {
                iterator = iterator.iterator();
                while (iterator.hasNext()) {
                    ((Transition)iterator.next()).resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }

    }

}

