// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import java.util.Collection;
import java.util.Iterator;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.view.View$OnAttachStateChangeListener;
import java.lang.ref.Reference;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.support.v4.util.ArrayMap;
import java.lang.ref.WeakReference;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(14)
@RequiresApi(14)
class TransitionManagerPort
{
    private static final String[] EMPTY_STRINGS;
    private static String LOG_TAG;
    private static TransitionPort sDefaultTransition;
    static ArrayList<ViewGroup> sPendingTransitions;
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<TransitionPort>>>> sRunningTransitions;
    ArrayMap<String, ArrayMap<ScenePort, TransitionPort>> mNameSceneTransitions;
    ArrayMap<ScenePort, ArrayMap<String, TransitionPort>> mSceneNameTransitions;
    ArrayMap<ScenePort, ArrayMap<ScenePort, TransitionPort>> mScenePairTransitions;
    ArrayMap<ScenePort, TransitionPort> mSceneTransitions;
    
    static {
        EMPTY_STRINGS = new String[0];
        TransitionManagerPort.LOG_TAG = "TransitionManager";
        TransitionManagerPort.sDefaultTransition = new AutoTransitionPort();
        TransitionManagerPort.sRunningTransitions = new ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<TransitionPort>>>>();
        TransitionManagerPort.sPendingTransitions = new ArrayList<ViewGroup>();
    }
    
    TransitionManagerPort() {
        this.mSceneTransitions = new ArrayMap<ScenePort, TransitionPort>();
        this.mScenePairTransitions = new ArrayMap<ScenePort, ArrayMap<ScenePort, TransitionPort>>();
        this.mSceneNameTransitions = new ArrayMap<ScenePort, ArrayMap<String, TransitionPort>>();
        this.mNameSceneTransitions = new ArrayMap<String, ArrayMap<ScenePort, TransitionPort>>();
    }
    
    public static void beginDelayedTransition(final ViewGroup viewGroup) {
        beginDelayedTransition(viewGroup, null);
    }
    
    public static void beginDelayedTransition(final ViewGroup viewGroup, TransitionPort clone) {
        if (!TransitionManagerPort.sPendingTransitions.contains(viewGroup) && ViewCompat.isLaidOut((View)viewGroup)) {
            TransitionManagerPort.sPendingTransitions.add(viewGroup);
            TransitionPort sDefaultTransition;
            if ((sDefaultTransition = clone) == null) {
                sDefaultTransition = TransitionManagerPort.sDefaultTransition;
            }
            clone = sDefaultTransition.clone();
            sceneChangeSetup(viewGroup, clone);
            ScenePort.setCurrentScene((View)viewGroup, null);
            sceneChangeRunTransition(viewGroup, clone);
        }
    }
    
    private static void changeScene(final ScenePort scenePort, final TransitionPort transitionPort) {
        final ViewGroup sceneRoot = scenePort.getSceneRoot();
        TransitionPort clone = null;
        if (transitionPort != null) {
            clone = transitionPort.clone();
            clone.setSceneRoot(sceneRoot);
        }
        final ScenePort currentScene = ScenePort.getCurrentScene((View)sceneRoot);
        if (currentScene != null && currentScene.isCreatedFromLayoutResource()) {
            clone.setCanRemoveViews(true);
        }
        sceneChangeSetup(sceneRoot, clone);
        scenePort.enter();
        sceneChangeRunTransition(sceneRoot, clone);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static TransitionPort getDefaultTransition() {
        return TransitionManagerPort.sDefaultTransition;
    }
    
    static ArrayMap<ViewGroup, ArrayList<TransitionPort>> getRunningTransitions() {
        final WeakReference<ArrayMap<ViewGroup, ArrayList<TransitionPort>>> weakReference = TransitionManagerPort.sRunningTransitions.get();
        if (weakReference != null) {
            final Reference<T> reference = (Reference<T>)weakReference;
            if (weakReference.get() != null) {
                return (ArrayMap<ViewGroup, ArrayList<TransitionPort>>)reference.get();
            }
        }
        final Reference<T> reference = (Reference<T>)new WeakReference<Object>(new ArrayMap<ViewGroup, ArrayList<TransitionPort>>());
        TransitionManagerPort.sRunningTransitions.set((WeakReference<ArrayMap<ViewGroup, ArrayList<TransitionPort>>>)reference);
        return (ArrayMap<ViewGroup, ArrayList<TransitionPort>>)reference.get();
    }
    
    private TransitionPort getTransition(final ScenePort scenePort) {
        final ViewGroup sceneRoot = scenePort.getSceneRoot();
        if (sceneRoot != null) {
            final ScenePort currentScene = ScenePort.getCurrentScene((View)sceneRoot);
            if (currentScene != null) {
                final ArrayMap<Object, TransitionPort> arrayMap = this.mScenePairTransitions.get(scenePort);
                if (arrayMap != null) {
                    final TransitionPort transitionPort = arrayMap.get(currentScene);
                    if (transitionPort != null) {
                        return transitionPort;
                    }
                }
            }
        }
        final TransitionPort transitionPort2 = this.mSceneTransitions.get(scenePort);
        if (transitionPort2 != null) {
            return transitionPort2;
        }
        return TransitionManagerPort.sDefaultTransition;
    }
    
    public static void go(final ScenePort scenePort) {
        changeScene(scenePort, TransitionManagerPort.sDefaultTransition);
    }
    
    public static void go(final ScenePort scenePort, final TransitionPort transitionPort) {
        changeScene(scenePort, transitionPort);
    }
    
    private static void sceneChangeRunTransition(final ViewGroup viewGroup, final TransitionPort transitionPort) {
        if (transitionPort != null && viewGroup != null) {
            final MultiListener multiListener = new MultiListener(transitionPort, viewGroup);
            viewGroup.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)multiListener);
            viewGroup.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)multiListener);
        }
    }
    
    private static void sceneChangeSetup(final ViewGroup viewGroup, final TransitionPort transitionPort) {
        final ArrayList<TransitionPort> list = getRunningTransitions().get(viewGroup);
        if (list != null && list.size() > 0) {
            final Iterator<TransitionPort> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next().pause((View)viewGroup);
            }
        }
        if (transitionPort != null) {
            transitionPort.captureValues(viewGroup, true);
        }
        final ScenePort currentScene = ScenePort.getCurrentScene((View)viewGroup);
        if (currentScene != null) {
            currentScene.exit();
        }
    }
    
    public TransitionPort getNamedTransition(final ScenePort scenePort, final String s) {
        final ArrayMap<Object, TransitionPort> arrayMap = this.mSceneNameTransitions.get(scenePort);
        if (arrayMap != null) {
            return (TransitionPort)arrayMap.get(s);
        }
        return null;
    }
    
    public TransitionPort getNamedTransition(final String s, final ScenePort scenePort) {
        final ArrayMap<Object, TransitionPort> arrayMap = this.mNameSceneTransitions.get(s);
        if (arrayMap != null) {
            return (TransitionPort)arrayMap.get(scenePort);
        }
        return null;
    }
    
    public String[] getTargetSceneNames(final ScenePort scenePort) {
        final ArrayMap<String, ArrayMap<String, ArrayMap<String, ArrayMap>>> arrayMap = this.mSceneNameTransitions.get(scenePort);
        String[] empty_STRINGS;
        if (arrayMap == null) {
            empty_STRINGS = TransitionManagerPort.EMPTY_STRINGS;
        }
        else {
            final int size = arrayMap.size();
            final String[] array = new String[size];
            int n = 0;
            while (true) {
                empty_STRINGS = array;
                if (n >= size) {
                    break;
                }
                array[n] = arrayMap.keyAt(n);
                ++n;
            }
        }
        return empty_STRINGS;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setDefaultTransition(final TransitionPort sDefaultTransition) {
        TransitionManagerPort.sDefaultTransition = sDefaultTransition;
    }
    
    public void setTransition(final ScenePort scenePort, final ScenePort scenePort2, final TransitionPort transitionPort) {
        ArrayMap<ScenePort, TransitionPort> arrayMap;
        if ((arrayMap = this.mScenePairTransitions.get(scenePort2)) == null) {
            arrayMap = new ArrayMap<ScenePort, TransitionPort>();
            this.mScenePairTransitions.put(scenePort2, arrayMap);
        }
        arrayMap.put(scenePort, transitionPort);
    }
    
    public void setTransition(final ScenePort scenePort, final TransitionPort transitionPort) {
        this.mSceneTransitions.put(scenePort, transitionPort);
    }
    
    public void setTransition(final ScenePort scenePort, final String s, final TransitionPort transitionPort) {
        ArrayMap<String, TransitionPort> arrayMap;
        if ((arrayMap = this.mSceneNameTransitions.get(scenePort)) == null) {
            arrayMap = new ArrayMap<String, TransitionPort>();
            this.mSceneNameTransitions.put(scenePort, arrayMap);
        }
        arrayMap.put(s, transitionPort);
    }
    
    public void setTransition(final String s, final ScenePort scenePort, final TransitionPort transitionPort) {
        ArrayMap<ScenePort, TransitionPort> arrayMap;
        if ((arrayMap = this.mNameSceneTransitions.get(s)) == null) {
            arrayMap = new ArrayMap<ScenePort, TransitionPort>();
            this.mNameSceneTransitions.put(s, arrayMap);
        }
        arrayMap.put(scenePort, transitionPort);
    }
    
    public void transitionTo(final ScenePort scenePort) {
        changeScene(scenePort, this.getTransition(scenePort));
    }
    
    private static class MultiListener implements ViewTreeObserver$OnPreDrawListener, View$OnAttachStateChangeListener
    {
        ViewGroup mSceneRoot;
        TransitionPort mTransition;
        
        MultiListener(final TransitionPort mTransition, final ViewGroup mSceneRoot) {
            this.mTransition = mTransition;
            this.mSceneRoot = mSceneRoot;
        }
        
        private void removeListeners() {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
            this.mSceneRoot.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        }
        
        public boolean onPreDraw() {
            this.removeListeners();
            TransitionManagerPort.sPendingTransitions.remove(this.mSceneRoot);
            final ArrayMap<ViewGroup, ArrayList<TransitionPort>> runningTransitions = TransitionManagerPort.getRunningTransitions();
            final ArrayList<TransitionPort> list = runningTransitions.get(this.mSceneRoot);
            ArrayList<TransitionPort> list2 = null;
            ArrayList<TransitionPort> list3;
            if (list == null) {
                list3 = new ArrayList<TransitionPort>();
                runningTransitions.put(this.mSceneRoot, list3);
            }
            else {
                list3 = list;
                if (list.size() > 0) {
                    list2 = new ArrayList<TransitionPort>(list);
                    list3 = list;
                }
            }
            list3.add(this.mTransition);
            this.mTransition.addListener((TransitionPort.TransitionListener)new TransitionPort.TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(final TransitionPort transitionPort) {
                    ((ArrayList)runningTransitions.get(MultiListener.this.mSceneRoot)).remove(transitionPort);
                }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (list2 != null) {
                final Iterator<TransitionPort> iterator = list2.iterator();
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
            TransitionManagerPort.sPendingTransitions.remove(this.mSceneRoot);
            final ArrayList<TransitionPort> list = TransitionManagerPort.getRunningTransitions().get(this.mSceneRoot);
            if (list != null && list.size() > 0) {
                final Iterator<TransitionPort> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }
    }
}
