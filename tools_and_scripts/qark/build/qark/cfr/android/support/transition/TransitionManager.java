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
package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.AutoTransition;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionListenerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TransitionManager {
    private static final String LOG_TAG = "TransitionManager";
    private static Transition sDefaultTransition = new AutoTransition();
    private static ArrayList<ViewGroup> sPendingTransitions;
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions;
    private ArrayMap<Scene, ArrayMap<Scene, Transition>> mScenePairTransitions = new ArrayMap();
    private ArrayMap<Scene, Transition> mSceneTransitions = new ArrayMap();

    static {
        sRunningTransitions = new ThreadLocal();
        sPendingTransitions = new ArrayList();
    }

    public static void beginDelayedTransition(@NonNull ViewGroup viewGroup) {
        TransitionManager.beginDelayedTransition(viewGroup, null);
    }

    public static void beginDelayedTransition(@NonNull ViewGroup viewGroup, @Nullable Transition transition) {
        if (!sPendingTransitions.contains((Object)viewGroup) && ViewCompat.isLaidOut((View)viewGroup)) {
            sPendingTransitions.add(viewGroup);
            if (transition == null) {
                transition = sDefaultTransition;
            }
            transition = transition.clone();
            TransitionManager.sceneChangeSetup(viewGroup, transition);
            Scene.setCurrentScene((View)viewGroup, null);
            TransitionManager.sceneChangeRunTransition(viewGroup, transition);
            return;
        }
    }

    private static void changeScene(Scene scene, Transition transition) {
        ViewGroup viewGroup = scene.getSceneRoot();
        if (!sPendingTransitions.contains((Object)viewGroup)) {
            if (transition == null) {
                scene.enter();
                return;
            }
            sPendingTransitions.add(viewGroup);
            transition = transition.clone();
            transition.setSceneRoot(viewGroup);
            Scene scene2 = Scene.getCurrentScene((View)viewGroup);
            if (scene2 != null && scene2.isCreatedFromLayoutResource()) {
                transition.setCanRemoveViews(true);
            }
            TransitionManager.sceneChangeSetup(viewGroup, transition);
            scene.enter();
            TransitionManager.sceneChangeRunTransition(viewGroup, transition);
            return;
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
            return;
        }
    }

    static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        WeakReference weakReference = sRunningTransitions.get();
        if (weakReference == null || weakReference.get() == null) {
            weakReference = new WeakReference(new ArrayMap());
            sRunningTransitions.set(weakReference);
        }
        return weakReference.get();
    }

    private Transition getTransition(Scene object) {
        ArrayMap<Scene, Transition> arrayMap;
        Object object2 = object.getSceneRoot();
        if (object2 != null && (object2 = Scene.getCurrentScene((View)object2)) != null && (arrayMap = this.mScenePairTransitions.get(object)) != null && (object2 = arrayMap.get(object2)) != null) {
            return object2;
        }
        if ((object = this.mSceneTransitions.get(object)) != null) {
            return object;
        }
        return sDefaultTransition;
    }

    public static void go(@NonNull Scene scene) {
        TransitionManager.changeScene(scene, sDefaultTransition);
    }

    public static void go(@NonNull Scene scene, @Nullable Transition transition) {
        TransitionManager.changeScene(scene, transition);
    }

    private static void sceneChangeRunTransition(ViewGroup viewGroup, Transition object) {
        if (object != null && viewGroup != null) {
            object = new MultiListener((Transition)object, viewGroup);
            viewGroup.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)object);
            viewGroup.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)object);
            return;
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
        if ((object = Scene.getCurrentScene((View)object)) != null) {
            object.exit();
            return;
        }
    }

    public void setTransition(@NonNull Scene scene, @NonNull Scene arrayMap, @Nullable Transition transition) {
        ArrayMap arrayMap2 = this.mScenePairTransitions.get(arrayMap);
        if (arrayMap2 == null) {
            arrayMap2 = new ArrayMap();
            this.mScenePairTransitions.put((Scene)((Object)arrayMap), arrayMap2);
            arrayMap = arrayMap2;
        } else {
            arrayMap = arrayMap2;
        }
        arrayMap.put(scene, transition);
    }

    public void setTransition(@NonNull Scene scene, @Nullable Transition transition) {
        this.mSceneTransitions.put(scene, transition);
    }

    public void transitionTo(@NonNull Scene scene) {
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
            this.removeListeners();
            if (!sPendingTransitions.remove((Object)this.mSceneRoot)) {
                return true;
            }
            final ArrayMap<ViewGroup, ArrayList<Transition>> arrayMap = TransitionManager.getRunningTransitions();
            Object object = arrayMap.get((Object)this.mSceneRoot);
            ArrayList<Transition> arrayList = null;
            if (object == null) {
                object = new ArrayList();
                arrayMap.put(this.mSceneRoot, (ArrayList)object);
            } else if (object.size() > 0) {
                arrayList = new ArrayList<Transition>((Collection<Transition>)object);
            }
            object.add(this.mTransition);
            this.mTransition.addListener(new TransitionListenerAdapter(){

                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    ((ArrayList)arrayMap.get((Object)MultiListener.this.mSceneRoot)).remove(transition);
                }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (arrayList != null) {
                object = arrayList.iterator();
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
            sPendingTransitions.remove((Object)this.mSceneRoot);
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

