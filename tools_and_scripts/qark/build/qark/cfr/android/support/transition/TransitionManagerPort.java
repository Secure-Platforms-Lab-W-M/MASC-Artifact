/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnPreDrawListener
 */
package android.support.transition;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.transition.AutoTransitionPort;
import android.support.transition.ScenePort;
import android.support.transition.TransitionPort;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@TargetApi(value=14)
@RequiresApi(value=14)
class TransitionManagerPort {
    private static final String[] EMPTY_STRINGS = new String[0];
    private static String LOG_TAG = "TransitionManager";
    private static TransitionPort sDefaultTransition = new AutoTransitionPort();
    static ArrayList<ViewGroup> sPendingTransitions;
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<TransitionPort>>>> sRunningTransitions;
    ArrayMap<String, ArrayMap<ScenePort, TransitionPort>> mNameSceneTransitions = new ArrayMap();
    ArrayMap<ScenePort, ArrayMap<String, TransitionPort>> mSceneNameTransitions = new ArrayMap();
    ArrayMap<ScenePort, ArrayMap<ScenePort, TransitionPort>> mScenePairTransitions = new ArrayMap();
    ArrayMap<ScenePort, TransitionPort> mSceneTransitions = new ArrayMap();

    static {
        sRunningTransitions = new ThreadLocal();
        sPendingTransitions = new ArrayList();
    }

    TransitionManagerPort() {
    }

    public static void beginDelayedTransition(ViewGroup viewGroup) {
        TransitionManagerPort.beginDelayedTransition(viewGroup, null);
    }

    public static void beginDelayedTransition(ViewGroup viewGroup, TransitionPort transitionPort) {
        if (!sPendingTransitions.contains((Object)viewGroup) && ViewCompat.isLaidOut((View)viewGroup)) {
            sPendingTransitions.add(viewGroup);
            TransitionPort transitionPort2 = transitionPort;
            if (transitionPort == null) {
                transitionPort2 = sDefaultTransition;
            }
            transitionPort = transitionPort2.clone();
            TransitionManagerPort.sceneChangeSetup(viewGroup, transitionPort);
            ScenePort.setCurrentScene((View)viewGroup, null);
            TransitionManagerPort.sceneChangeRunTransition(viewGroup, transitionPort);
        }
    }

    private static void changeScene(ScenePort scenePort, TransitionPort object) {
        ViewGroup viewGroup = scenePort.getSceneRoot();
        TransitionPort transitionPort = null;
        if (object != null) {
            transitionPort = object.clone();
            transitionPort.setSceneRoot(viewGroup);
        }
        if ((object = ScenePort.getCurrentScene((View)viewGroup)) != null && object.isCreatedFromLayoutResource()) {
            transitionPort.setCanRemoveViews(true);
        }
        TransitionManagerPort.sceneChangeSetup(viewGroup, transitionPort);
        scenePort.enter();
        TransitionManagerPort.sceneChangeRunTransition(viewGroup, transitionPort);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static TransitionPort getDefaultTransition() {
        return sDefaultTransition;
    }

    static ArrayMap<ViewGroup, ArrayList<TransitionPort>> getRunningTransitions() {
        WeakReference weakReference2;
        block3 : {
            WeakReference weakReference2;
            block2 : {
                WeakReference<ArrayMap<ViewGroup, ArrayList<TransitionPort>>> weakReference3 = sRunningTransitions.get();
                if (weakReference3 == null) break block2;
                weakReference2 = weakReference3;
                if (weakReference3.get() != null) break block3;
            }
            weakReference2 = new WeakReference(new ArrayMap());
            sRunningTransitions.set(weakReference2);
        }
        return weakReference2.get();
    }

    private TransitionPort getTransition(ScenePort object) {
        ArrayMap<ScenePort, TransitionPort> arrayMap;
        Object object2 = object.getSceneRoot();
        if (object2 != null && (object2 = ScenePort.getCurrentScene((View)object2)) != null && (arrayMap = this.mScenePairTransitions.get(object)) != null && (object2 = arrayMap.get(object2)) != null) {
            return object2;
        }
        if ((object = this.mSceneTransitions.get(object)) != null) {
            return object;
        }
        return sDefaultTransition;
    }

    public static void go(ScenePort scenePort) {
        TransitionManagerPort.changeScene(scenePort, sDefaultTransition);
    }

    public static void go(ScenePort scenePort, TransitionPort transitionPort) {
        TransitionManagerPort.changeScene(scenePort, transitionPort);
    }

    private static void sceneChangeRunTransition(ViewGroup viewGroup, TransitionPort object) {
        if (object != null && viewGroup != null) {
            object = new MultiListener((TransitionPort)object, viewGroup);
            viewGroup.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)object);
            viewGroup.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)object);
        }
    }

    private static void sceneChangeSetup(ViewGroup object, TransitionPort transitionPort) {
        ArrayList<TransitionPort> arrayList = TransitionManagerPort.getRunningTransitions().get(object);
        if (arrayList != null && arrayList.size() > 0) {
            arrayList = arrayList.iterator();
            while (arrayList.hasNext()) {
                ((TransitionPort)arrayList.next()).pause((View)object);
            }
        }
        if (transitionPort != null) {
            transitionPort.captureValues((ViewGroup)object, true);
        }
        if ((object = ScenePort.getCurrentScene((View)object)) != null) {
            object.exit();
        }
    }

    public TransitionPort getNamedTransition(ScenePort object, String string2) {
        if ((object = this.mSceneNameTransitions.get(object)) != null) {
            return (TransitionPort)object.get(string2);
        }
        return null;
    }

    public TransitionPort getNamedTransition(String object, ScenePort scenePort) {
        if ((object = this.mNameSceneTransitions.get(object)) != null) {
            return (TransitionPort)object.get(scenePort);
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String[] getTargetSceneNames(ScenePort arrstring) {
        ArrayMap<String, TransitionPort> arrayMap = this.mSceneNameTransitions.get(arrstring);
        if (arrayMap == null) {
            return EMPTY_STRINGS;
        }
        int n = arrayMap.size();
        String[] arrstring2 = new String[n];
        int n2 = 0;
        do {
            arrstring = arrstring2;
            if (n2 >= n) return arrstring;
            arrstring2[n2] = arrayMap.keyAt(n2);
            ++n2;
        } while (true);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setDefaultTransition(TransitionPort transitionPort) {
        sDefaultTransition = transitionPort;
    }

    public void setTransition(ScenePort scenePort, ScenePort scenePort2, TransitionPort transitionPort) {
        ArrayMap<ScenePort, TransitionPort> arrayMap;
        ArrayMap arrayMap2 = arrayMap = this.mScenePairTransitions.get(scenePort2);
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap();
            this.mScenePairTransitions.put(scenePort2, arrayMap2);
        }
        arrayMap2.put(scenePort, transitionPort);
    }

    public void setTransition(ScenePort scenePort, TransitionPort transitionPort) {
        this.mSceneTransitions.put(scenePort, transitionPort);
    }

    public void setTransition(ScenePort scenePort, String string2, TransitionPort transitionPort) {
        ArrayMap<String, TransitionPort> arrayMap;
        ArrayMap arrayMap2 = arrayMap = this.mSceneNameTransitions.get(scenePort);
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap();
            this.mSceneNameTransitions.put(scenePort, arrayMap2);
        }
        arrayMap2.put(string2, transitionPort);
    }

    public void setTransition(String string2, ScenePort scenePort, TransitionPort transitionPort) {
        ArrayMap<ScenePort, TransitionPort> arrayMap;
        ArrayMap arrayMap2 = arrayMap = this.mNameSceneTransitions.get(string2);
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap();
            this.mNameSceneTransitions.put(string2, arrayMap2);
        }
        arrayMap2.put(scenePort, transitionPort);
    }

    public void transitionTo(ScenePort scenePort) {
        TransitionManagerPort.changeScene(scenePort, this.getTransition(scenePort));
    }

    private static class MultiListener
    implements ViewTreeObserver.OnPreDrawListener,
    View.OnAttachStateChangeListener {
        ViewGroup mSceneRoot;
        TransitionPort mTransition;

        MultiListener(TransitionPort transitionPort, ViewGroup viewGroup) {
            this.mTransition = transitionPort;
            this.mSceneRoot = viewGroup;
        }

        private void removeListeners() {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
            this.mSceneRoot.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean onPreDraw() {
            Object object;
            this.removeListeners();
            TransitionManagerPort.sPendingTransitions.remove((Object)this.mSceneRoot);
            final ArrayMap<ViewGroup, ArrayList<TransitionPort>> arrayMap = TransitionManagerPort.getRunningTransitions();
            ArrayList<TransitionPort> arrayList = arrayMap.get((Object)this.mSceneRoot);
            ArrayList<TransitionPort> arrayList2 = null;
            if (arrayList == null) {
                object = new ArrayList();
                arrayMap.put(this.mSceneRoot, (ArrayList<TransitionPort>)object);
            } else {
                object = arrayList;
                if (arrayList.size() > 0) {
                    arrayList2 = new ArrayList<TransitionPort>(arrayList);
                    object = arrayList;
                }
            }
            object.add(this.mTransition);
            this.mTransition.addListener(new TransitionPort.TransitionListenerAdapter(){

                @Override
                public void onTransitionEnd(TransitionPort transitionPort) {
                    ((ArrayList)arrayMap.get((Object)MultiListener.this.mSceneRoot)).remove(transitionPort);
                }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (arrayList2 != null) {
                object = arrayList2.iterator();
                while (object.hasNext()) {
                    ((TransitionPort)object.next()).resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.playTransition(this.mSceneRoot);
            return true;
        }

        public void onViewAttachedToWindow(View view) {
        }

        public void onViewDetachedFromWindow(View iterator) {
            this.removeListeners();
            TransitionManagerPort.sPendingTransitions.remove((Object)this.mSceneRoot);
            iterator = TransitionManagerPort.getRunningTransitions().get((Object)this.mSceneRoot);
            if (iterator != null && iterator.size() > 0) {
                iterator = iterator.iterator();
                while (iterator.hasNext()) {
                    ((TransitionPort)iterator.next()).resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }

    }

}

