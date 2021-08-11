// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.os.Build$VERSION;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.support.annotation.RequiresApi;
import java.util.Map;
import java.util.List;
import android.util.SparseArray;
import android.support.v4.view.ViewCompat;
import java.util.Collection;
import android.support.v4.util.ArrayMap;
import android.view.View;
import java.util.ArrayList;

class FragmentTransition
{
    private static final int[] INVERSE_OPS;
    
    static {
        INVERSE_OPS = new int[] { 0, 3, 0, 1, 5, 4, 7, 6, 9, 8 };
    }
    
    private static void addSharedElementsWithMatchingNames(final ArrayList<View> list, final ArrayMap<String, View> arrayMap, final Collection<String> collection) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            final View view = arrayMap.valueAt(i);
            if (collection.contains(ViewCompat.getTransitionName(view))) {
                list.add(view);
            }
        }
    }
    
    private static void addToFirstInLastOut(final BackStackRecord backStackRecord, final BackStackRecord.Op op, final SparseArray<FragmentContainerTransition> sparseArray, final boolean b, final boolean b2) {
        final Fragment fragment = op.fragment;
        if (fragment == null) {
            return;
        }
        final int mContainerId = fragment.mContainerId;
        if (mContainerId == 0) {
            return;
        }
        int cmd;
        if (b) {
            cmd = FragmentTransition.INVERSE_OPS[op.cmd];
        }
        else {
            cmd = op.cmd;
        }
        final boolean b3 = false;
        final boolean b4 = false;
        final boolean b5 = false;
        final boolean b6 = false;
        final boolean b7 = false;
        final boolean b8 = false;
        boolean b9 = false;
        boolean b10 = false;
        int n2 = 0;
        boolean b11 = false;
        Label_0424: {
            Label_0373: {
                if (cmd != 1) {
                    if (cmd != 3) {
                        if (cmd == 4) {
                            int n;
                            if (b2) {
                                n = (b3 ? 1 : 0);
                                if (fragment.mHiddenChanged) {
                                    n = (b3 ? 1 : 0);
                                    if (fragment.mAdded) {
                                        n = (b3 ? 1 : 0);
                                        if (fragment.mHidden) {
                                            n = 1;
                                        }
                                    }
                                }
                            }
                            else {
                                n = (b4 ? 1 : 0);
                                if (fragment.mAdded) {
                                    n = (b4 ? 1 : 0);
                                    if (!fragment.mHidden) {
                                        n = 1;
                                    }
                                }
                            }
                            b9 = false;
                            b10 = true;
                            n2 = n;
                            b11 = false;
                            break Label_0424;
                        }
                        if (cmd == 5) {
                            if (b2) {
                                b9 = b8;
                                if (fragment.mHiddenChanged) {
                                    b9 = b8;
                                    if (!fragment.mHidden) {
                                        b9 = b8;
                                        if (fragment.mAdded) {
                                            b9 = true;
                                        }
                                    }
                                }
                            }
                            else {
                                b9 = fragment.mHidden;
                            }
                            b10 = false;
                            n2 = 0;
                            b11 = true;
                            break Label_0424;
                        }
                        if (cmd != 6) {
                            if (cmd != 7) {
                                b9 = false;
                                b10 = false;
                                n2 = 0;
                                b11 = false;
                                break Label_0424;
                            }
                            break Label_0373;
                        }
                    }
                    int n3;
                    if (b2) {
                        n3 = (((!fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f) || b5) ? 1 : 0);
                    }
                    else {
                        n3 = (b6 ? 1 : 0);
                        if (fragment.mAdded) {
                            n3 = (b6 ? 1 : 0);
                            if (!fragment.mHidden) {
                                n3 = 1;
                            }
                        }
                    }
                    b9 = false;
                    b10 = true;
                    n2 = n3;
                    b11 = false;
                    break Label_0424;
                }
            }
            if (b2) {
                b9 = fragment.mIsNewlyAdded;
            }
            else {
                b9 = b7;
                if (!fragment.mAdded) {
                    b9 = b7;
                    if (!fragment.mHidden) {
                        b9 = true;
                    }
                }
            }
            b10 = false;
            n2 = 0;
            b11 = true;
        }
        FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.get(mContainerId);
        if (b9) {
            fragmentContainerTransition = ensureContainer(fragmentContainerTransition, sparseArray, mContainerId);
            fragmentContainerTransition.lastIn = fragment;
            fragmentContainerTransition.lastInIsPop = b;
            fragmentContainerTransition.lastInTransaction = backStackRecord;
        }
        if (!b2 && b11) {
            if (fragmentContainerTransition != null && fragmentContainerTransition.firstOut == fragment) {
                fragmentContainerTransition.firstOut = null;
            }
            final FragmentManagerImpl mManager = backStackRecord.mManager;
            if (fragment.mState < 1 && mManager.mCurState >= 1 && !backStackRecord.mReorderingAllowed) {
                mManager.makeActive(fragment);
                mManager.moveToState(fragment, 1, 0, 0, false);
            }
        }
        Label_0601: {
            if (n2 != 0) {
                final FragmentContainerTransition fragmentContainerTransition2 = fragmentContainerTransition;
                if (fragmentContainerTransition2 != null) {
                    fragmentContainerTransition = fragmentContainerTransition2;
                    if (fragmentContainerTransition2.firstOut != null) {
                        break Label_0601;
                    }
                }
                fragmentContainerTransition = ensureContainer(fragmentContainerTransition2, sparseArray, mContainerId);
                fragmentContainerTransition.firstOut = fragment;
                fragmentContainerTransition.firstOutIsPop = b;
                fragmentContainerTransition.firstOutTransaction = backStackRecord;
            }
        }
        if (!b2 && b10 && fragmentContainerTransition != null && fragmentContainerTransition.lastIn == fragment) {
            fragmentContainerTransition.lastIn = null;
        }
    }
    
    public static void calculateFragments(final BackStackRecord backStackRecord, final SparseArray<FragmentContainerTransition> sparseArray, final boolean b) {
        for (int size = backStackRecord.mOps.size(), i = 0; i < size; ++i) {
            addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(i), sparseArray, false, b);
        }
    }
    
    private static ArrayMap<String, String> calculateNameOverrides(final int n, final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n2, int i) {
        final ArrayMap<Object, String> arrayMap = new ArrayMap<Object, String>();
        BackStackRecord backStackRecord;
        boolean booleanValue;
        int size;
        ArrayList<String> list3;
        ArrayList<String> list4;
        int j;
        String s;
        String s2;
        String s3;
        for (--i; i >= n2; --i) {
            backStackRecord = list.get(i);
            if (backStackRecord.interactsWith(n)) {
                booleanValue = list2.get(i);
                if (backStackRecord.mSharedElementSourceNames != null) {
                    size = backStackRecord.mSharedElementSourceNames.size();
                    if (booleanValue) {
                        list3 = backStackRecord.mSharedElementSourceNames;
                        list4 = backStackRecord.mSharedElementTargetNames;
                    }
                    else {
                        list4 = backStackRecord.mSharedElementSourceNames;
                        list3 = backStackRecord.mSharedElementTargetNames;
                    }
                    for (j = 0; j < size; ++j) {
                        s = list4.get(j);
                        s2 = list3.get(j);
                        s3 = arrayMap.remove(s2);
                        if (s3 != null) {
                            arrayMap.put(s, s3);
                        }
                        else {
                            arrayMap.put(s, s2);
                        }
                    }
                }
            }
        }
        return (ArrayMap<String, String>)arrayMap;
    }
    
    public static void calculatePopFragments(final BackStackRecord backStackRecord, final SparseArray<FragmentContainerTransition> sparseArray, final boolean b) {
        if (!backStackRecord.mManager.mContainer.onHasView()) {
            return;
        }
        for (int i = backStackRecord.mOps.size() - 1; i >= 0; --i) {
            addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(i), sparseArray, true, b);
        }
    }
    
    private static void callSharedElementStartEnd(final Fragment fragment, final Fragment fragment2, final boolean b, final ArrayMap<String, View> arrayMap, final boolean b2) {
        SharedElementCallback sharedElementCallback;
        if (b) {
            sharedElementCallback = fragment2.getEnterTransitionCallback();
        }
        else {
            sharedElementCallback = fragment.getEnterTransitionCallback();
        }
        if (sharedElementCallback != null) {
            final ArrayList<View> list = new ArrayList<View>();
            final ArrayList<String> list2 = new ArrayList<String>();
            int size;
            if (arrayMap == null) {
                size = 0;
            }
            else {
                size = arrayMap.size();
            }
            for (int i = 0; i < size; ++i) {
                list2.add(arrayMap.keyAt(i));
                list.add(arrayMap.valueAt(i));
            }
            if (b2) {
                sharedElementCallback.onSharedElementStart(list2, list, null);
                return;
            }
            sharedElementCallback.onSharedElementEnd(list2, list, null);
        }
    }
    
    @RequiresApi(21)
    private static ArrayMap<String, View> captureInSharedElements(final ArrayMap<String, String> arrayMap, final Object o, final FragmentContainerTransition fragmentContainerTransition) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final View view = lastIn.getView();
        if (arrayMap.isEmpty() || o == null || view == null) {
            arrayMap.clear();
            return null;
        }
        final ArrayMap<Object, View> arrayMap2 = new ArrayMap<Object, View>();
        FragmentTransitionCompat21.findNamedViews((Map<String, View>)arrayMap2, view);
        final BackStackRecord lastInTransaction = fragmentContainerTransition.lastInTransaction;
        SharedElementCallback sharedElementCallback;
        ArrayList<String> list;
        if (fragmentContainerTransition.lastInIsPop) {
            sharedElementCallback = lastIn.getExitTransitionCallback();
            list = lastInTransaction.mSharedElementSourceNames;
        }
        else {
            sharedElementCallback = lastIn.getEnterTransitionCallback();
            list = lastInTransaction.mSharedElementTargetNames;
        }
        if (list != null) {
            arrayMap2.retainAll(list);
        }
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(list, (Map<String, View>)arrayMap2);
            for (int i = list.size() - 1; i >= 0; --i) {
                final String s = list.get(i);
                final View view2 = arrayMap2.get(s);
                if (view2 == null) {
                    final String keyForValue = findKeyForValue(arrayMap, s);
                    if (keyForValue != null) {
                        arrayMap.remove(keyForValue);
                    }
                }
                else if (!s.equals(ViewCompat.getTransitionName(view2))) {
                    final String keyForValue2 = findKeyForValue(arrayMap, s);
                    if (keyForValue2 != null) {
                        arrayMap.put(keyForValue2, ViewCompat.getTransitionName(view2));
                    }
                }
            }
            return (ArrayMap<String, View>)arrayMap2;
        }
        retainValues(arrayMap, (ArrayMap<String, View>)arrayMap2);
        return (ArrayMap<String, View>)arrayMap2;
    }
    
    @RequiresApi(21)
    private static ArrayMap<String, View> captureOutSharedElements(final ArrayMap<String, String> arrayMap, final Object o, final FragmentContainerTransition fragmentContainerTransition) {
        if (arrayMap.isEmpty() || o == null) {
            arrayMap.clear();
            return null;
        }
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        final ArrayMap<Object, Object> arrayMap2 = new ArrayMap<Object, Object>();
        FragmentTransitionCompat21.findNamedViews((Map<String, View>)arrayMap2, firstOut.getView());
        final BackStackRecord firstOutTransaction = fragmentContainerTransition.firstOutTransaction;
        SharedElementCallback sharedElementCallback;
        ArrayList<String> list;
        if (fragmentContainerTransition.firstOutIsPop) {
            sharedElementCallback = firstOut.getEnterTransitionCallback();
            list = firstOutTransaction.mSharedElementTargetNames;
        }
        else {
            sharedElementCallback = firstOut.getExitTransitionCallback();
            list = firstOutTransaction.mSharedElementSourceNames;
        }
        arrayMap2.retainAll(list);
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(list, (Map<String, View>)arrayMap2);
            for (int i = list.size() - 1; i >= 0; --i) {
                final String s = list.get(i);
                final View view = arrayMap2.get(s);
                if (view == null) {
                    arrayMap.remove(s);
                }
                else if (!s.equals(ViewCompat.getTransitionName(view))) {
                    arrayMap.put(ViewCompat.getTransitionName(view), arrayMap.remove(s));
                }
            }
            return (ArrayMap<String, View>)arrayMap2;
        }
        arrayMap.retainAll(arrayMap2.keySet());
        return (ArrayMap<String, View>)arrayMap2;
    }
    
    @RequiresApi(21)
    private static ArrayList<View> configureEnteringExitingViews(final Object o, final Fragment fragment, final ArrayList<View> list, final View view) {
        ArrayList<View> list2 = null;
        if (o != null) {
            final ArrayList<View> list3 = new ArrayList<View>();
            final View view2 = fragment.getView();
            if (view2 != null) {
                FragmentTransitionCompat21.captureTransitioningViews(list3, view2);
            }
            if (list != null) {
                list3.removeAll(list);
            }
            list2 = list3;
            if (!list3.isEmpty()) {
                list3.add(view);
                FragmentTransitionCompat21.addTargets(o, list3);
                list2 = list3;
            }
        }
        return list2;
    }
    
    @RequiresApi(21)
    private static Object configureSharedElementsOrdered(final ViewGroup viewGroup, final View view, final ArrayMap<String, String> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final ArrayList<View> list, final ArrayList<View> list2, final Object o, Object o2) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        if (lastIn == null) {
            return null;
        }
        if (firstOut == null) {
            return null;
        }
        final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
        Object sharedElementTransition;
        if (arrayMap.isEmpty()) {
            sharedElementTransition = null;
        }
        else {
            sharedElementTransition = getSharedElementTransition(lastIn, firstOut, lastInIsPop);
        }
        final ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(arrayMap, sharedElementTransition, fragmentContainerTransition);
        if (arrayMap.isEmpty()) {
            sharedElementTransition = null;
        }
        else {
            list.addAll(captureOutSharedElements.values());
        }
        if (o == null && o2 == null && sharedElementTransition == null) {
            return null;
        }
        callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureOutSharedElements, true);
        if (sharedElementTransition != null) {
            final Rect rect = new Rect();
            FragmentTransitionCompat21.setSharedElementTargets(sharedElementTransition, view, list);
            setOutEpicenter(sharedElementTransition, o2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            if (o != null) {
                FragmentTransitionCompat21.setEpicenter(o, rect);
            }
            o2 = rect;
        }
        else {
            o2 = null;
        }
        OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
            @Override
            public void run() {
                final ArrayMap access$300 = captureInSharedElements(arrayMap, sharedElementTransition, fragmentContainerTransition);
                if (access$300 != null) {
                    list2.addAll(access$300.values());
                    list2.add(view);
                }
                callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, access$300, false);
                final Object val$finalSharedElementTransition = sharedElementTransition;
                if (val$finalSharedElementTransition != null) {
                    FragmentTransitionCompat21.swapSharedElementTargets(val$finalSharedElementTransition, list, list2);
                    final View access$301 = getInEpicenterView(access$300, fragmentContainerTransition, o, lastInIsPop);
                    if (access$301 != null) {
                        FragmentTransitionCompat21.getBoundsOnScreen(access$301, o2);
                    }
                }
            }
        });
        return sharedElementTransition;
    }
    
    @RequiresApi(21)
    private static Object configureSharedElementsReordered(final ViewGroup viewGroup, final View view, final ArrayMap<String, String> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final ArrayList<View> list, final ArrayList<View> list2, final Object o, final Object o2) {
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        if (lastIn != null) {
            lastIn.getView().setVisibility(0);
        }
        if (lastIn == null) {
            return null;
        }
        if (firstOut == null) {
            return null;
        }
        final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
        Object sharedElementTransition;
        if (arrayMap.isEmpty()) {
            sharedElementTransition = null;
        }
        else {
            sharedElementTransition = getSharedElementTransition(lastIn, firstOut, lastInIsPop);
        }
        final ArrayMap<String, View> captureOutSharedElements = captureOutSharedElements(arrayMap, sharedElementTransition, fragmentContainerTransition);
        final ArrayMap<String, View> captureInSharedElements = captureInSharedElements(arrayMap, sharedElementTransition, fragmentContainerTransition);
        Object o3;
        if (arrayMap.isEmpty()) {
            if (captureOutSharedElements != null) {
                captureOutSharedElements.clear();
            }
            if (captureInSharedElements != null) {
                captureInSharedElements.clear();
            }
            o3 = null;
        }
        else {
            addSharedElementsWithMatchingNames(list, captureOutSharedElements, arrayMap.keySet());
            addSharedElementsWithMatchingNames(list2, captureInSharedElements, arrayMap.values());
            o3 = sharedElementTransition;
        }
        if (o == null && o2 == null && o3 == null) {
            return null;
        }
        callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureOutSharedElements, true);
        Rect rect;
        View inEpicenterView;
        if (o3 != null) {
            list2.add(view);
            FragmentTransitionCompat21.setSharedElementTargets(o3, view, list);
            setOutEpicenter(o3, o2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            rect = new Rect();
            inEpicenterView = getInEpicenterView(captureInSharedElements, fragmentContainerTransition, o, lastInIsPop);
            if (inEpicenterView != null) {
                FragmentTransitionCompat21.setEpicenter(o, rect);
            }
        }
        else {
            rect = null;
            inEpicenterView = null;
        }
        OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
            @Override
            public void run() {
                callSharedElementStartEnd(lastIn, firstOut, lastInIsPop, captureInSharedElements, false);
                final View val$epicenterView = inEpicenterView;
                if (val$epicenterView != null) {
                    FragmentTransitionCompat21.getBoundsOnScreen(val$epicenterView, rect);
                }
            }
        });
        return o3;
    }
    
    @RequiresApi(21)
    private static void configureTransitionsOrdered(final FragmentManagerImpl fragmentManagerImpl, final int n, final FragmentContainerTransition fragmentContainerTransition, final View view, final ArrayMap<String, String> arrayMap) {
        ViewGroup viewGroup;
        if (fragmentManagerImpl.mContainer.onHasView()) {
            viewGroup = (ViewGroup)fragmentManagerImpl.mContainer.onFindViewById(n);
        }
        else {
            viewGroup = null;
        }
        if (viewGroup == null) {
            return;
        }
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
        final boolean firstOutIsPop = fragmentContainerTransition.firstOutIsPop;
        final Object enterTransition = getEnterTransition(lastIn, lastInIsPop);
        Object exitTransition = getExitTransition(firstOut, firstOutIsPop);
        final ArrayList<View> list = new ArrayList<View>();
        final ArrayList<View> list2 = new ArrayList<View>();
        final Object configureSharedElementsOrdered = configureSharedElementsOrdered(viewGroup, view, arrayMap, fragmentContainerTransition, list, list2, enterTransition, exitTransition);
        if (enterTransition == null && configureSharedElementsOrdered == null && exitTransition == null) {
            return;
        }
        final ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(exitTransition, firstOut, list, view);
        if (configureEnteringExitingViews == null || configureEnteringExitingViews.isEmpty()) {
            exitTransition = null;
        }
        FragmentTransitionCompat21.addTarget(enterTransition, view);
        final Object mergeTransitions = mergeTransitions(enterTransition, exitTransition, configureSharedElementsOrdered, lastIn, fragmentContainerTransition.lastInIsPop);
        if (mergeTransitions != null) {
            final ArrayList<View> list3 = new ArrayList<View>();
            FragmentTransitionCompat21.scheduleRemoveTargets(mergeTransitions, enterTransition, list3, exitTransition, configureEnteringExitingViews, configureSharedElementsOrdered, list2);
            scheduleTargetChange(viewGroup, lastIn, view, list2, enterTransition, list3, exitTransition, configureEnteringExitingViews);
            FragmentTransitionCompat21.setNameOverridesOrdered((View)viewGroup, list2, arrayMap);
            FragmentTransitionCompat21.beginDelayedTransition(viewGroup, mergeTransitions);
            FragmentTransitionCompat21.scheduleNameReset(viewGroup, list2, arrayMap);
        }
    }
    
    @RequiresApi(21)
    private static void configureTransitionsReordered(final FragmentManagerImpl fragmentManagerImpl, final int n, final FragmentContainerTransition fragmentContainerTransition, final View view, final ArrayMap<String, String> arrayMap) {
        ViewGroup viewGroup;
        if (fragmentManagerImpl.mContainer.onHasView()) {
            viewGroup = (ViewGroup)fragmentManagerImpl.mContainer.onFindViewById(n);
        }
        else {
            viewGroup = null;
        }
        if (viewGroup == null) {
            return;
        }
        final Fragment lastIn = fragmentContainerTransition.lastIn;
        final Fragment firstOut = fragmentContainerTransition.firstOut;
        final boolean lastInIsPop = fragmentContainerTransition.lastInIsPop;
        final boolean firstOutIsPop = fragmentContainerTransition.firstOutIsPop;
        final ArrayList<View> list = new ArrayList<View>();
        final ArrayList<View> list2 = new ArrayList<View>();
        final Object enterTransition = getEnterTransition(lastIn, lastInIsPop);
        final Object exitTransition = getExitTransition(firstOut, firstOutIsPop);
        final Object configureSharedElementsReordered = configureSharedElementsReordered(viewGroup, view, arrayMap, fragmentContainerTransition, list2, list, enterTransition, exitTransition);
        if (enterTransition == null && configureSharedElementsReordered == null && exitTransition == null) {
            return;
        }
        final Object o = exitTransition;
        final ArrayList<View> configureEnteringExitingViews = configureEnteringExitingViews(o, firstOut, list2, view);
        final ArrayList<View> configureEnteringExitingViews2 = configureEnteringExitingViews(enterTransition, lastIn, list, view);
        setViewVisibility(configureEnteringExitingViews2, 4);
        final Object mergeTransitions = mergeTransitions(enterTransition, o, configureSharedElementsReordered, lastIn, lastInIsPop);
        if (mergeTransitions != null) {
            replaceHide(o, firstOut, configureEnteringExitingViews);
            final ArrayList<String> prepareSetNameOverridesReordered = FragmentTransitionCompat21.prepareSetNameOverridesReordered(list);
            FragmentTransitionCompat21.scheduleRemoveTargets(mergeTransitions, enterTransition, configureEnteringExitingViews2, o, configureEnteringExitingViews, configureSharedElementsReordered, list);
            FragmentTransitionCompat21.beginDelayedTransition(viewGroup, mergeTransitions);
            FragmentTransitionCompat21.setNameOverridesReordered((View)viewGroup, list2, list, prepareSetNameOverridesReordered, arrayMap);
            setViewVisibility(configureEnteringExitingViews2, 0);
            FragmentTransitionCompat21.swapSharedElementTargets(configureSharedElementsReordered, list2, list);
        }
    }
    
    private static FragmentContainerTransition ensureContainer(final FragmentContainerTransition fragmentContainerTransition, final SparseArray<FragmentContainerTransition> sparseArray, final int n) {
        FragmentContainerTransition fragmentContainerTransition2 = fragmentContainerTransition;
        if (fragmentContainerTransition == null) {
            fragmentContainerTransition2 = new FragmentContainerTransition();
            sparseArray.put(n, (Object)fragmentContainerTransition2);
        }
        return fragmentContainerTransition2;
    }
    
    private static String findKeyForValue(final ArrayMap<String, String> arrayMap, final String s) {
        for (int size = arrayMap.size(), i = 0; i < size; ++i) {
            if (s.equals(arrayMap.valueAt(i))) {
                return arrayMap.keyAt(i);
            }
        }
        return null;
    }
    
    @RequiresApi(21)
    private static Object getEnterTransition(final Fragment fragment, final boolean b) {
        if (fragment == null) {
            return null;
        }
        Object o;
        if (b) {
            o = fragment.getReenterTransition();
        }
        else {
            o = fragment.getEnterTransition();
        }
        return FragmentTransitionCompat21.cloneTransition(o);
    }
    
    @RequiresApi(21)
    private static Object getExitTransition(final Fragment fragment, final boolean b) {
        if (fragment == null) {
            return null;
        }
        Object o;
        if (b) {
            o = fragment.getReturnTransition();
        }
        else {
            o = fragment.getExitTransition();
        }
        return FragmentTransitionCompat21.cloneTransition(o);
    }
    
    private static View getInEpicenterView(final ArrayMap<String, View> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final Object o, final boolean b) {
        final BackStackRecord lastInTransaction = fragmentContainerTransition.lastInTransaction;
        if (o != null && arrayMap != null && lastInTransaction.mSharedElementSourceNames != null && !lastInTransaction.mSharedElementSourceNames.isEmpty()) {
            String s;
            if (b) {
                s = lastInTransaction.mSharedElementSourceNames.get(0);
            }
            else {
                s = lastInTransaction.mSharedElementTargetNames.get(0);
            }
            return arrayMap.get(s);
        }
        return null;
    }
    
    @RequiresApi(21)
    private static Object getSharedElementTransition(final Fragment fragment, final Fragment fragment2, final boolean b) {
        if (fragment != null && fragment2 != null) {
            Object o;
            if (b) {
                o = fragment2.getSharedElementReturnTransition();
            }
            else {
                o = fragment.getSharedElementEnterTransition();
            }
            return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(o));
        }
        return null;
    }
    
    @RequiresApi(21)
    private static Object mergeTransitions(final Object o, final Object o2, final Object o3, final Fragment fragment, final boolean b) {
        int n2;
        final int n = n2 = 1;
        if (o != null) {
            n2 = n;
            if (o2 != null) {
                n2 = n;
                if (fragment != null) {
                    boolean b2;
                    if (b) {
                        b2 = fragment.getAllowReturnTransitionOverlap();
                    }
                    else {
                        b2 = fragment.getAllowEnterTransitionOverlap();
                    }
                    n2 = (b2 ? 1 : 0);
                }
            }
        }
        if (n2 != 0) {
            return FragmentTransitionCompat21.mergeTransitionsTogether(o2, o, o3);
        }
        return FragmentTransitionCompat21.mergeTransitionsInSequence(o2, o, o3);
    }
    
    @RequiresApi(21)
    private static void replaceHide(final Object o, final Fragment fragment, final ArrayList<View> list) {
        if (fragment != null && o != null && fragment.mAdded && fragment.mHidden && fragment.mHiddenChanged) {
            fragment.setHideReplaced(true);
            FragmentTransitionCompat21.scheduleHideFragmentView(o, fragment.getView(), list);
            OneShotPreDrawListener.add((View)fragment.mContainer, new Runnable() {
                @Override
                public void run() {
                    setViewVisibility(list, 4);
                }
            });
        }
    }
    
    private static void retainValues(final ArrayMap<String, String> arrayMap, final ArrayMap<String, View> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            if (!arrayMap2.containsKey(arrayMap.valueAt(i))) {
                arrayMap.removeAt(i);
            }
        }
    }
    
    @RequiresApi(21)
    private static void scheduleTargetChange(final ViewGroup viewGroup, final Fragment fragment, final View view, final ArrayList<View> list, final Object o, final ArrayList<View> list2, final Object o2, final ArrayList<View> list3) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable() {
            @Override
            public void run() {
                final Object val$enterTransition = o;
                if (val$enterTransition != null) {
                    FragmentTransitionCompat21.removeTarget(val$enterTransition, view);
                    list2.addAll(configureEnteringExitingViews(o, fragment, list, view));
                }
                if (list3 != null) {
                    if (o2 != null) {
                        final ArrayList<View> list = new ArrayList<View>();
                        list.add(view);
                        FragmentTransitionCompat21.replaceTargets(o2, list3, list);
                    }
                    list3.clear();
                    list3.add(view);
                }
            }
        });
    }
    
    @RequiresApi(21)
    private static void setOutEpicenter(final Object o, final Object o2, final ArrayMap<String, View> arrayMap, final boolean b, final BackStackRecord backStackRecord) {
        if (backStackRecord.mSharedElementSourceNames != null && !backStackRecord.mSharedElementSourceNames.isEmpty()) {
            String s;
            if (b) {
                s = backStackRecord.mSharedElementTargetNames.get(0);
            }
            else {
                s = backStackRecord.mSharedElementSourceNames.get(0);
            }
            final View view = arrayMap.get(s);
            FragmentTransitionCompat21.setEpicenter(o, view);
            if (o2 != null) {
                FragmentTransitionCompat21.setEpicenter(o2, view);
            }
        }
    }
    
    private static void setViewVisibility(final ArrayList<View> list, final int visibility) {
        if (list == null) {
            return;
        }
        for (int i = list.size() - 1; i >= 0; --i) {
            list.get(i).setVisibility(visibility);
        }
    }
    
    static void startTransitions(final FragmentManagerImpl fragmentManagerImpl, final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n, final int n2, final boolean b) {
        if (fragmentManagerImpl.mCurState < 1) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            final SparseArray sparseArray = new SparseArray();
            for (int i = n; i < n2; ++i) {
                final BackStackRecord backStackRecord = list.get(i);
                if (list2.get(i)) {
                    calculatePopFragments(backStackRecord, (SparseArray<FragmentContainerTransition>)sparseArray, b);
                }
                else {
                    calculateFragments(backStackRecord, (SparseArray<FragmentContainerTransition>)sparseArray, b);
                }
            }
            if (sparseArray.size() != 0) {
                final View view = new View(fragmentManagerImpl.mHost.getContext());
                for (int size = sparseArray.size(), j = 0; j < size; ++j) {
                    final int key = sparseArray.keyAt(j);
                    final ArrayMap<String, String> calculateNameOverrides = calculateNameOverrides(key, list, list2, n, n2);
                    final FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.valueAt(j);
                    if (b) {
                        configureTransitionsReordered(fragmentManagerImpl, key, fragmentContainerTransition, view, calculateNameOverrides);
                    }
                    else {
                        configureTransitionsOrdered(fragmentManagerImpl, key, fragmentContainerTransition, view, calculateNameOverrides);
                    }
                }
            }
        }
    }
    
    static class FragmentContainerTransition
    {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;
    }
}
