/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.v4.app;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.BackStackRecord;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.FragmentTransitionCompat21;
import android.support.v4.app.OneShotPreDrawListener;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

class FragmentTransition {
    private static final int[] INVERSE_OPS = new int[]{0, 3, 0, 1, 5, 4, 7, 6, 9, 8};

    FragmentTransition() {
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> arrayList, ArrayMap<String, View> arrayMap, Collection<String> collection) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            View view = arrayMap.valueAt(i);
            if (!collection.contains(ViewCompat.getTransitionName(view))) continue;
            arrayList.add(view);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void addToFirstInLastOut(BackStackRecord backStackRecord, BackStackRecord.Op object, SparseArray<FragmentContainerTransition> sparseArray, boolean bl, boolean bl2) {
        block20 : {
            block17 : {
                Object object2;
                Fragment fragment;
                boolean bl3;
                block18 : {
                    int n;
                    block19 : {
                        fragment = object.fragment;
                        if (fragment == null || (n = fragment.mContainerId) == 0) break block17;
                        int n2 = bl ? INVERSE_OPS[object.cmd] : object.cmd;
                        boolean bl4 = false;
                        boolean bl5 = false;
                        boolean bl6 = false;
                        boolean bl7 = false;
                        boolean bl8 = bl6;
                        boolean bl9 = bl4;
                        boolean bl10 = bl7;
                        bl3 = bl5;
                        switch (n2) {
                            default: {
                                bl3 = bl5;
                                bl10 = bl7;
                                bl9 = bl4;
                                bl8 = bl6;
                                break;
                            }
                            case 5: {
                                bl9 = bl2 ? fragment.mHiddenChanged && !fragment.mHidden && fragment.mAdded : fragment.mHidden;
                                bl10 = true;
                                bl8 = bl6;
                                bl3 = bl5;
                                break;
                            }
                            case 1: 
                            case 7: {
                                bl9 = bl2 ? fragment.mIsNewlyAdded : !fragment.mAdded && !fragment.mHidden;
                                bl10 = true;
                                bl8 = bl6;
                                bl3 = bl5;
                                break;
                            }
                            case 4: {
                                bl8 = bl2 ? fragment.mHiddenChanged && fragment.mAdded && fragment.mHidden : fragment.mAdded && !fragment.mHidden;
                                bl3 = true;
                                bl9 = bl4;
                                bl10 = bl7;
                            }
                            case 2: {
                                break;
                            }
                            case 3: 
                            case 6: {
                                bl8 = bl2 ? !fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f : fragment.mAdded && !fragment.mHidden;
                                bl3 = true;
                                bl9 = bl4;
                                bl10 = bl7;
                            }
                        }
                        object = object2 = (FragmentContainerTransition)sparseArray.get(n);
                        if (bl9) {
                            object = FragmentTransition.ensureContainer((FragmentContainerTransition)object2, sparseArray, n);
                            object.lastIn = fragment;
                            object.lastInIsPop = bl;
                            object.lastInTransaction = backStackRecord;
                        }
                        if (!bl2 && bl10) {
                            if (object != null && object.firstOut == fragment) {
                                object.firstOut = null;
                            }
                            object2 = backStackRecord.mManager;
                            if (fragment.mState < 1 && object2.mCurState >= 1 && !backStackRecord.mReorderingAllowed) {
                                object2.makeActive(fragment);
                                object2.moveToState(fragment, 1, 0, 0, false);
                            }
                        }
                        object2 = object;
                        if (!bl8) break block18;
                        if (object == null) break block19;
                        object2 = object;
                        if (object.firstOut != null) break block18;
                    }
                    object2 = FragmentTransition.ensureContainer((FragmentContainerTransition)object, sparseArray, n);
                    object2.firstOut = fragment;
                    object2.firstOutIsPop = bl;
                    object2.firstOutTransaction = backStackRecord;
                }
                if (!bl2 && bl3 && object2 != null && object2.lastIn == fragment) break block20;
            }
            return;
        }
        object2.lastIn = null;
    }

    public static void calculateFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean bl) {
        int n = backStackRecord.mOps.size();
        for (int i = 0; i < n; ++i) {
            FragmentTransition.addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(i), sparseArray, false, bl);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static ArrayMap<String, String> calculateNameOverrides(int var0, ArrayList<BackStackRecord> var1_1, ArrayList<Boolean> var2_2, int var3_3, int var4_4) {
        var10_5 = new ArrayMap<String, String>();
        --var4_4;
        block0 : do {
            if (var4_4 < var3_3) return var10_5;
            var11_11 = var1_1.get(var4_4);
            if (!var11_11.interactsWith(var0)) ** GOTO lbl19
            var7_8 = var2_2.get(var4_4);
            if (var11_11.mSharedElementSourceNames == null) ** GOTO lbl19
            var6_7 = var11_11.mSharedElementSourceNames.size();
            if (var7_8) {
                var9_10 = var11_11.mSharedElementSourceNames;
                var8_9 = var11_11.mSharedElementTargetNames;
            } else {
                var8_9 = var11_11.mSharedElementSourceNames;
                var9_10 = var11_11.mSharedElementTargetNames;
            }
            var5_6 = 0;
            do {
                block9 : {
                    if (var5_6 < var6_7) break block9;
lbl19: // 3 sources:
                    --var4_4;
                    continue block0;
                }
                var11_11 = var8_9.get(var5_6);
                var12_12 = var9_10.get(var5_6);
                var13_13 = var10_5.remove(var12_12);
                if (var13_13 != null) {
                    var10_5.put((String)var11_11, var13_13);
                } else {
                    var10_5.put((String)var11_11, var12_12);
                }
                ++var5_6;
            } while (true);
            break;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void calculatePopFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean bl) {
        if (backStackRecord.mManager.mContainer.onHasView()) {
            for (int i = backStackRecord.mOps.size() - 1; i >= 0; --i) {
                FragmentTransition.addToFirstInLastOut(backStackRecord, backStackRecord.mOps.get(i), sparseArray, true, bl);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void callSharedElementStartEnd(Fragment object, Fragment arrayList, boolean bl, ArrayMap<String, View> arrayMap, boolean bl2) {
        object = bl ? arrayList.getEnterTransitionCallback() : object.getEnterTransitionCallback();
        if (object != null) {
            arrayList = new ArrayList();
            ArrayList<String> arrayList2 = new ArrayList<String>();
            int n = arrayMap == null ? 0 : arrayMap.size();
            for (int i = 0; i < n; ++i) {
                arrayList2.add(arrayMap.keyAt(i));
                arrayList.add(arrayMap.valueAt(i));
            }
            if (!bl2) {
                object.onSharedElementEnd(arrayList2, arrayList, null);
                return;
            }
            object.onSharedElementStart(arrayList2, arrayList, null);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @RequiresApi(value=21)
    private static ArrayMap<String, View> captureInSharedElements(ArrayMap<String, String> arrayMap, Object arrayList, FragmentContainerTransition object) {
        Object object2 = object.lastIn;
        View view = object2.getView();
        if (arrayMap.isEmpty() || arrayList == null || view == null) {
            arrayMap.clear();
            return null;
        }
        View view2 = new View();
        FragmentTransitionCompat21.findNamedViews(view2, view);
        arrayList = object.lastInTransaction;
        if (object.lastInIsPop) {
            object = object2.getExitTransitionCallback();
            arrayList = arrayList.mSharedElementSourceNames;
        } else {
            object = object2.getEnterTransitionCallback();
            arrayList = arrayList.mSharedElementTargetNames;
        }
        if (arrayList != null) {
            view2.retainAll(arrayList);
        }
        if (object == null) {
            FragmentTransition.retainValues(arrayMap, view2);
            return view2;
        }
        object.onMapSharedElements((List<String>)arrayList, (Map<String, View>)view2);
        int n = arrayList.size() - 1;
        do {
            object = view2;
            if (n < 0) return object;
            object2 = arrayList.get(n);
            object = view2.get(object2);
            if (object == null) {
                object = FragmentTransition.findKeyForValue(arrayMap, (String)object2);
                if (object != null) {
                    arrayMap.remove(object);
                }
            } else if (!object2.equals(ViewCompat.getTransitionName((View)object)) && (object2 = FragmentTransition.findKeyForValue(arrayMap, (String)object2)) != null) {
                arrayMap.put((String)object2, ViewCompat.getTransitionName((View)object));
            }
            --n;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    @RequiresApi(value=21)
    private static ArrayMap<String, View> captureOutSharedElements(ArrayMap<String, String> arrayMap, Object arrayList, FragmentContainerTransition view) {
        if (arrayMap.isEmpty() || arrayList == null) {
            arrayMap.clear();
            return null;
        }
        Object object = view.firstOut;
        View view2 = new View();
        FragmentTransitionCompat21.findNamedViews(view2, object.getView());
        arrayList = view.firstOutTransaction;
        if (view.firstOutIsPop) {
            view = object.getEnterTransitionCallback();
            arrayList = arrayList.mSharedElementTargetNames;
        } else {
            view = object.getExitTransitionCallback();
            arrayList = arrayList.mSharedElementSourceNames;
        }
        view2.retainAll(arrayList);
        if (view == null) {
            arrayMap.retainAll(view2.keySet());
            return view2;
        }
        view.onMapSharedElements((List<String>)arrayList, (Map<String, View>)view2);
        int n = arrayList.size() - 1;
        do {
            view = view2;
            if (n < 0) return view;
            object = arrayList.get(n);
            view = view2.get(object);
            if (view == null) {
                arrayMap.remove(object);
            } else if (!object.equals(ViewCompat.getTransitionName(view))) {
                object = arrayMap.remove(object);
                arrayMap.put(ViewCompat.getTransitionName(view), (String)object);
            }
            --n;
        } while (true);
    }

    @RequiresApi(value=21)
    private static ArrayList<View> configureEnteringExitingViews(Object object, Fragment fragment, ArrayList<View> arrayList, View view) {
        ArrayList<View> arrayList2 = null;
        if (object != null) {
            ArrayList<View> arrayList3 = new ArrayList<View>();
            if ((fragment = fragment.getView()) != null) {
                FragmentTransitionCompat21.captureTransitioningViews(arrayList3, (View)fragment);
            }
            if (arrayList != null) {
                arrayList3.removeAll(arrayList);
            }
            arrayList2 = arrayList3;
            if (!arrayList3.isEmpty()) {
                arrayList3.add(view);
                FragmentTransitionCompat21.addTargets(object, arrayList3);
                arrayList2 = arrayList3;
            }
        }
        return arrayList2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @RequiresApi(value=21)
    private static Object configureSharedElementsOrdered(ViewGroup viewGroup, final View view, final ArrayMap<String, String> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final ArrayList<View> arrayList, final ArrayList<View> arrayList2, final Object object, Object object2) {
        final Fragment fragment = fragmentContainerTransition.lastIn;
        final Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment == null || fragment2 == null) {
            return null;
        }
        final boolean bl = fragmentContainerTransition.lastInIsPop;
        final Object object3 = arrayMap.isEmpty() ? null : FragmentTransition.getSharedElementTransition(fragment, fragment2, bl);
        ArrayMap<String, View> arrayMap2 = FragmentTransition.captureOutSharedElements(arrayMap, object3, fragmentContainerTransition);
        if (arrayMap.isEmpty()) {
            object3 = null;
        } else {
            arrayList.addAll(arrayMap2.values());
        }
        if (object == null && object2 == null && object3 == null) {
            return null;
        }
        FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, arrayMap2, true);
        if (object3 != null) {
            Rect rect = new Rect();
            FragmentTransitionCompat21.setSharedElementTargets(object3, view, arrayList);
            FragmentTransition.setOutEpicenter(object3, object2, arrayMap2, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            object2 = rect;
            if (object != null) {
                FragmentTransitionCompat21.setEpicenter(object, rect);
                object2 = rect;
            }
        } else {
            object2 = null;
        }
        OneShotPreDrawListener.add((View)viewGroup, new Runnable((Rect)object2){
            final /* synthetic */ Rect val$inEpicenter;
            {
                this.val$inEpicenter = rect;
            }

            @Override
            public void run() {
                ArrayMap arrayMap2 = FragmentTransition.captureInSharedElements(arrayMap, object3, fragmentContainerTransition);
                if (arrayMap2 != null) {
                    arrayList2.addAll(arrayMap2.values());
                    arrayList2.add(view);
                }
                FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, arrayMap2, false);
                if (object3 != null) {
                    FragmentTransitionCompat21.swapSharedElementTargets(object3, arrayList, arrayList2);
                    arrayMap2 = FragmentTransition.getInEpicenterView(arrayMap2, fragmentContainerTransition, object, bl);
                    if (arrayMap2 != null) {
                        FragmentTransitionCompat21.getBoundsOnScreen((View)arrayMap2, this.val$inEpicenter);
                    }
                }
            }
        });
        return object3;
    }

    /*
     * Enabled aggressive block sorting
     */
    @RequiresApi(value=21)
    private static Object configureSharedElementsReordered(ViewGroup viewGroup, View object, ArrayMap<String, String> rect, FragmentContainerTransition fragmentContainerTransition, ArrayList<View> rect2, ArrayList<View> arrayList, Object object2, Object object3) {
        final Fragment fragment = fragmentContainerTransition.lastIn;
        final Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment != null) {
            fragment.getView().setVisibility(0);
        }
        if (fragment == null || fragment2 == null) {
            return null;
        }
        final boolean bl = fragmentContainerTransition.lastInIsPop;
        Object object4 = rect.isEmpty() ? null : FragmentTransition.getSharedElementTransition(fragment, fragment2, bl);
        ArrayMap<String, View> arrayMap = FragmentTransition.captureOutSharedElements(rect, object4, fragmentContainerTransition);
        final ArrayMap<String, View> arrayMap2 = FragmentTransition.captureInSharedElements(rect, object4, fragmentContainerTransition);
        if (rect.isEmpty()) {
            rect = null;
            if (arrayMap != null) {
                arrayMap.clear();
            }
            object4 = rect;
            if (arrayMap2 != null) {
                arrayMap2.clear();
                object4 = rect;
            }
        } else {
            FragmentTransition.addSharedElementsWithMatchingNames(rect2, arrayMap, rect.keySet());
            FragmentTransition.addSharedElementsWithMatchingNames(arrayList, arrayMap2, rect.values());
        }
        if (object2 == null && object3 == null && object4 == null) {
            return null;
        }
        FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, arrayMap, true);
        if (object4 != null) {
            arrayList.add((View)object);
            FragmentTransitionCompat21.setSharedElementTargets(object4, (View)object, rect2);
            FragmentTransition.setOutEpicenter(object4, object3, arrayMap, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            rect2 = new Rect();
            fragmentContainerTransition = FragmentTransition.getInEpicenterView(arrayMap2, fragmentContainerTransition, object2, bl);
            object = fragmentContainerTransition;
            rect = rect2;
            if (fragmentContainerTransition != null) {
                FragmentTransitionCompat21.setEpicenter(object2, rect2);
                rect = rect2;
                object = fragmentContainerTransition;
            }
        } else {
            rect = null;
            object = null;
        }
        OneShotPreDrawListener.add((View)viewGroup, new Runnable((View)object, rect){
            final /* synthetic */ Rect val$epicenter;
            final /* synthetic */ View val$epicenterView;
            {
                this.val$epicenterView = view;
                this.val$epicenter = rect;
            }

            @Override
            public void run() {
                FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, arrayMap2, false);
                if (this.val$epicenterView != null) {
                    FragmentTransitionCompat21.getBoundsOnScreen(this.val$epicenterView, this.val$epicenter);
                }
            }
        });
        return object4;
    }

    /*
     * Enabled aggressive block sorting
     */
    @RequiresApi(value=21)
    private static void configureTransitionsOrdered(FragmentManagerImpl object, int n, FragmentContainerTransition object2, View view, ArrayMap<String, String> arrayMap) {
        Fragment fragment;
        Object object3;
        ViewGroup viewGroup;
        Object object4;
        ArrayList<View> arrayList;
        Object object5;
        block5 : {
            block4 : {
                viewGroup = null;
                if (!object.mContainer.onHasView()) return;
                viewGroup = (ViewGroup)object.mContainer.onFindViewById(n);
                if (viewGroup == null) break block4;
                fragment = object2.lastIn;
                object4 = object2.firstOut;
                boolean bl = object2.lastInIsPop;
                boolean bl2 = object2.firstOutIsPop;
                object5 = FragmentTransition.getEnterTransition(fragment, bl);
                object = FragmentTransition.getExitTransition((Fragment)object4, bl2);
                ArrayList<View> arrayList2 = new ArrayList<View>();
                arrayList = new ArrayList<View>();
                object3 = FragmentTransition.configureSharedElementsOrdered(viewGroup, view, arrayMap, (FragmentContainerTransition)object2, arrayList2, arrayList, object5, object);
                if (object5 == null && object3 == null && object == null) break block4;
                if ((object4 = FragmentTransition.configureEnteringExitingViews(object, (Fragment)object4, arrayList2, view)) == null || object4.isEmpty()) {
                    object = null;
                }
                FragmentTransitionCompat21.addTarget(object5, view);
                object2 = FragmentTransition.mergeTransitions(object5, object, object3, fragment, object2.lastInIsPop);
                if (object2 != null) break block5;
            }
            return;
        }
        ArrayList<View> arrayList3 = new ArrayList<View>();
        FragmentTransitionCompat21.scheduleRemoveTargets(object2, object5, arrayList3, object, object4, object3, arrayList);
        FragmentTransition.scheduleTargetChange(viewGroup, fragment, view, arrayList, object5, arrayList3, object, object4);
        FragmentTransitionCompat21.setNameOverridesOrdered((View)viewGroup, arrayList, arrayMap);
        FragmentTransitionCompat21.beginDelayedTransition(viewGroup, object2);
        FragmentTransitionCompat21.scheduleNameReset(viewGroup, arrayList, arrayMap);
    }

    /*
     * Enabled aggressive block sorting
     */
    @RequiresApi(value=21)
    private static void configureTransitionsReordered(FragmentManagerImpl arrayList, int n, FragmentContainerTransition object, View arrayList2, ArrayMap<String, String> arrayMap) {
        ViewGroup viewGroup;
        Object object2;
        Object object3;
        Object object4;
        ArrayList<View> arrayList3;
        Object object5;
        ArrayList<View> arrayList4;
        block3 : {
            block2 : {
                viewGroup = null;
                if (!arrayList.mContainer.onHasView()) return;
                viewGroup = (ViewGroup)arrayList.mContainer.onFindViewById(n);
                if (viewGroup == null) break block2;
                object5 = object.lastIn;
                object4 = object.firstOut;
                boolean bl = object.lastInIsPop;
                boolean bl2 = object.firstOutIsPop;
                arrayList = new ArrayList();
                arrayList3 = new ArrayList<View>();
                object2 = FragmentTransition.getEnterTransition((Fragment)object5, bl);
                object3 = FragmentTransition.getExitTransition((Fragment)object4, bl2);
                object = FragmentTransition.configureSharedElementsReordered(viewGroup, arrayList2, arrayMap, (FragmentContainerTransition)object, arrayList3, arrayList, object2, object3);
                if (object2 == null && object == null && object3 == null) break block2;
                arrayList4 = FragmentTransition.configureEnteringExitingViews(object3, (Fragment)object4, arrayList3, arrayList2);
                arrayList2 = FragmentTransition.configureEnteringExitingViews(object2, (Fragment)object5, arrayList, arrayList2);
                FragmentTransition.setViewVisibility(arrayList2, 4);
                if ((object5 = FragmentTransition.mergeTransitions(object2, object3, object, (Fragment)object5, bl)) != null) break block3;
            }
            return;
        }
        FragmentTransition.replaceHide(object3, (Fragment)object4, arrayList4);
        object4 = FragmentTransitionCompat21.prepareSetNameOverridesReordered(arrayList);
        FragmentTransitionCompat21.scheduleRemoveTargets(object5, object2, arrayList2, object3, arrayList4, object, arrayList);
        FragmentTransitionCompat21.beginDelayedTransition(viewGroup, object5);
        FragmentTransitionCompat21.setNameOverridesReordered((View)viewGroup, arrayList3, arrayList, object4, arrayMap);
        FragmentTransition.setViewVisibility(arrayList2, 0);
        FragmentTransitionCompat21.swapSharedElementTargets(object, arrayList3, arrayList);
    }

    private static FragmentContainerTransition ensureContainer(FragmentContainerTransition fragmentContainerTransition, SparseArray<FragmentContainerTransition> sparseArray, int n) {
        FragmentContainerTransition fragmentContainerTransition2 = fragmentContainerTransition;
        if (fragmentContainerTransition == null) {
            fragmentContainerTransition2 = new FragmentContainerTransition();
            sparseArray.put(n, (Object)fragmentContainerTransition2);
        }
        return fragmentContainerTransition2;
    }

    private static String findKeyForValue(ArrayMap<String, String> arrayMap, String string2) {
        int n = arrayMap.size();
        for (int i = 0; i < n; ++i) {
            if (!string2.equals(arrayMap.valueAt(i))) continue;
            return arrayMap.keyAt(i);
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @RequiresApi(value=21)
    private static Object getEnterTransition(Fragment object, boolean bl) {
        if (object == null) {
            return null;
        }
        if (bl) {
            object = object.getReenterTransition();
            do {
                return FragmentTransitionCompat21.cloneTransition(object);
                break;
            } while (true);
        }
        object = object.getEnterTransition();
        return FragmentTransitionCompat21.cloneTransition(object);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @RequiresApi(value=21)
    private static Object getExitTransition(Fragment object, boolean bl) {
        if (object == null) {
            return null;
        }
        if (bl) {
            object = object.getReturnTransition();
            do {
                return FragmentTransitionCompat21.cloneTransition(object);
                break;
            } while (true);
        }
        object = object.getExitTransition();
        return FragmentTransitionCompat21.cloneTransition(object);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static View getInEpicenterView(ArrayMap<String, View> arrayMap, FragmentContainerTransition object, Object object2, boolean bl) {
        object = object.lastInTransaction;
        if (object2 == null || arrayMap == null || object.mSharedElementSourceNames == null || object.mSharedElementSourceNames.isEmpty()) return null;
        if (bl) {
            object = object.mSharedElementSourceNames.get(0);
            do {
                return arrayMap.get(object);
                break;
            } while (true);
        }
        object = object.mSharedElementTargetNames.get(0);
        return arrayMap.get(object);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @RequiresApi(value=21)
    private static Object getSharedElementTransition(Fragment object, Fragment fragment, boolean bl) {
        if (object == null || fragment == null) {
            return null;
        }
        if (bl) {
            object = fragment.getSharedElementReturnTransition();
            do {
                return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(object));
                break;
            } while (true);
        }
        object = object.getSharedElementEnterTransition();
        return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(object));
    }

    /*
     * Enabled aggressive block sorting
     */
    @RequiresApi(value=21)
    private static Object mergeTransitions(Object object, Object object2, Object object3, Fragment fragment, boolean bl) {
        boolean bl2;
        boolean bl3 = bl2 = true;
        if (object == null) return FragmentTransitionCompat21.mergeTransitionsTogether(object2, object, object3);
        bl3 = bl2;
        if (object2 != null) {
            bl3 = bl2;
            if (fragment != null) {
                bl3 = bl ? fragment.getAllowReturnTransitionOverlap() : fragment.getAllowEnterTransitionOverlap();
            }
        }
        if (bl3) {
            return FragmentTransitionCompat21.mergeTransitionsTogether(object2, object, object3);
        }
        return FragmentTransitionCompat21.mergeTransitionsInSequence(object2, object, object3);
    }

    @RequiresApi(value=21)
    private static void replaceHide(Object object, Fragment fragment, final ArrayList<View> arrayList) {
        if (fragment != null && object != null && fragment.mAdded && fragment.mHidden && fragment.mHiddenChanged) {
            fragment.setHideReplaced(true);
            FragmentTransitionCompat21.scheduleHideFragmentView(object, fragment.getView(), arrayList);
            OneShotPreDrawListener.add((View)fragment.mContainer, new Runnable(){

                @Override
                public void run() {
                    FragmentTransition.setViewVisibility(arrayList, 4);
                }
            });
        }
    }

    private static void retainValues(ArrayMap<String, String> arrayMap, ArrayMap<String, View> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            if (arrayMap2.containsKey(arrayMap.valueAt(i))) continue;
            arrayMap.removeAt(i);
        }
    }

    @RequiresApi(value=21)
    private static void scheduleTargetChange(ViewGroup viewGroup, final Fragment fragment, final View view, final ArrayList<View> arrayList, final Object object, final ArrayList<View> arrayList2, final Object object2, final ArrayList<View> arrayList3) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable(){

            @Override
            public void run() {
                ArrayList arrayList4;
                if (object != null) {
                    FragmentTransitionCompat21.removeTarget(object, view);
                    arrayList4 = FragmentTransition.configureEnteringExitingViews(object, fragment, arrayList, view);
                    arrayList2.addAll(arrayList4);
                }
                if (arrayList3 != null) {
                    if (object2 != null) {
                        arrayList4 = new ArrayList();
                        arrayList4.add((View)view);
                        FragmentTransitionCompat21.replaceTargets(object2, arrayList3, arrayList4);
                    }
                    arrayList3.clear();
                    arrayList3.add(view);
                }
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     */
    @RequiresApi(value=21)
    private static void setOutEpicenter(Object object, Object object2, ArrayMap<String, View> view, boolean bl, BackStackRecord object3) {
        if (object3.mSharedElementSourceNames != null && !object3.mSharedElementSourceNames.isEmpty()) {
            object3 = bl ? object3.mSharedElementTargetNames.get(0) : object3.mSharedElementSourceNames.get(0);
            view = (View)view.get(object3);
            FragmentTransitionCompat21.setEpicenter(object, view);
            if (object2 != null) {
                FragmentTransitionCompat21.setEpicenter(object2, view);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void setViewVisibility(ArrayList<View> arrayList, int n) {
        if (arrayList != null) {
            for (int i = arrayList.size() - 1; i >= 0; --i) {
                arrayList.get(i).setVisibility(n);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    static void startTransitions(FragmentManagerImpl fragmentManagerImpl, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2, boolean bl) {
        BackStackRecord backStackRecord;
        SparseArray sparseArray;
        int n3;
        block9 : {
            block8 : {
                if (fragmentManagerImpl.mCurState < 1 || Build.VERSION.SDK_INT < 21) break block8;
                sparseArray = new SparseArray();
                for (n3 = n; n3 < n2; ++n3) {
                    backStackRecord = arrayList.get(n3);
                    if (arrayList2.get(n3).booleanValue()) {
                        FragmentTransition.calculatePopFragments(backStackRecord, sparseArray, bl);
                        continue;
                    }
                    FragmentTransition.calculateFragments(backStackRecord, sparseArray, bl);
                }
                if (sparseArray.size() != 0) break block9;
            }
            return;
        }
        backStackRecord = new View(fragmentManagerImpl.mHost.getContext());
        int n4 = sparseArray.size();
        n3 = 0;
        while (n3 < n4) {
            int n5 = sparseArray.keyAt(n3);
            ArrayMap<String, String> arrayMap = FragmentTransition.calculateNameOverrides(n5, arrayList, arrayList2, n, n2);
            FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.valueAt(n3);
            if (bl) {
                FragmentTransition.configureTransitionsReordered(fragmentManagerImpl, n5, fragmentContainerTransition, (View)backStackRecord, arrayMap);
            } else {
                FragmentTransition.configureTransitionsOrdered(fragmentManagerImpl, n5, fragmentContainerTransition, (View)backStackRecord, arrayMap);
            }
            ++n3;
        }
    }

    static class FragmentContainerTransition {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;

        FragmentContainerTransition() {
        }
    }

}

