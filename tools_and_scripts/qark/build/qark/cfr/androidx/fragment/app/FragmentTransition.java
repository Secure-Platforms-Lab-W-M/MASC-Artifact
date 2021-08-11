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
package androidx.fragment.app;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransitionCompat21;
import androidx.fragment.app.FragmentTransitionImpl;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

class FragmentTransition {
    private static final int[] INVERSE_OPS = new int[]{0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 10};
    private static final FragmentTransitionImpl PLATFORM_IMPL;
    private static final FragmentTransitionImpl SUPPORT_IMPL;

    static {
        FragmentTransitionCompat21 fragmentTransitionCompat21 = Build.VERSION.SDK_INT >= 21 ? new FragmentTransitionCompat21() : null;
        PLATFORM_IMPL = fragmentTransitionCompat21;
        SUPPORT_IMPL = FragmentTransition.resolveSupportImpl();
    }

    private FragmentTransition() {
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> arrayList, ArrayMap<String, View> arrayMap, Collection<String> collection) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            View view = arrayMap.valueAt(i);
            if (!collection.contains(ViewCompat.getTransitionName(view))) continue;
            arrayList.add(view);
        }
    }

    private static void addToFirstInLastOut(BackStackRecord backStackRecord, FragmentTransaction.Op object, SparseArray<FragmentContainerTransition> sparseArray, boolean bl, boolean bl2) {
        Object object2;
        int n;
        Fragment fragment;
        block38 : {
            int n2;
            block39 : {
                boolean bl3;
                boolean bl4;
                int n3;
                block37 : {
                    int n4;
                    boolean bl5;
                    block33 : {
                        int n5;
                        int n6;
                        block34 : {
                            int n7;
                            int n8;
                            block35 : {
                                boolean bl6;
                                block36 : {
                                    fragment = object.mFragment;
                                    if (fragment == null) {
                                        return;
                                    }
                                    n2 = fragment.mContainerId;
                                    if (n2 == 0) {
                                        return;
                                    }
                                    n = bl ? INVERSE_OPS[object.mCmd] : object.mCmd;
                                    bl3 = false;
                                    n4 = 0;
                                    n3 = 0;
                                    bl4 = false;
                                    n7 = 0;
                                    n8 = 0;
                                    n5 = 0;
                                    n6 = 0;
                                    bl5 = false;
                                    bl6 = false;
                                    if (n == 1) break block33;
                                    if (n == 3) break block34;
                                    if (n == 4) break block35;
                                    if (n == 5) break block36;
                                    if (n == 6) break block34;
                                    if (n == 7) break block33;
                                    n = n4;
                                    break block37;
                                }
                                if (bl2) {
                                    bl3 = bl6;
                                    if (fragment.mHiddenChanged) {
                                        bl3 = bl6;
                                        if (!fragment.mHidden) {
                                            bl3 = bl6;
                                            if (fragment.mAdded) {
                                                bl3 = true;
                                            }
                                        }
                                    }
                                } else {
                                    bl3 = fragment.mHidden;
                                }
                                bl4 = true;
                                n = n4;
                                break block37;
                            }
                            if (bl2) {
                                n = n7;
                                if (fragment.mHiddenChanged) {
                                    n = n7;
                                    if (fragment.mAdded) {
                                        n = n7;
                                        if (fragment.mHidden) {
                                            n = 1;
                                        }
                                    }
                                }
                                n3 = n;
                            } else {
                                n = n8;
                                if (fragment.mAdded) {
                                    n = n8;
                                    if (!fragment.mHidden) {
                                        n = 1;
                                    }
                                }
                                n3 = n;
                            }
                            n = 1;
                            break block37;
                        }
                        if (bl2) {
                            n = !fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f ? 1 : n5;
                            n3 = n;
                        } else {
                            n = n6;
                            if (fragment.mAdded) {
                                n = n6;
                                if (!fragment.mHidden) {
                                    n = 1;
                                }
                            }
                            n3 = n;
                        }
                        n = 1;
                        break block37;
                    }
                    if (bl2) {
                        bl3 = fragment.mIsNewlyAdded;
                    } else {
                        bl3 = bl5;
                        if (!fragment.mAdded) {
                            bl3 = bl5;
                            if (!fragment.mHidden) {
                                bl3 = true;
                            }
                        }
                    }
                    bl4 = true;
                    n = n4;
                }
                object = object2 = (FragmentContainerTransition)sparseArray.get(n2);
                if (bl3) {
                    object = FragmentTransition.ensureContainer((FragmentContainerTransition)object2, sparseArray, n2);
                    object.lastIn = fragment;
                    object.lastInIsPop = bl;
                    object.lastInTransaction = backStackRecord;
                }
                if (!bl2 && bl4) {
                    if (object != null && object.firstOut == fragment) {
                        object.firstOut = null;
                    }
                    object2 = backStackRecord.mManager;
                    if (fragment.mState < 1 && object2.mCurState >= 1 && !backStackRecord.mReorderingAllowed) {
                        object2.makeActive(fragment);
                        object2.moveToState(fragment, 1);
                    }
                }
                object2 = object;
                if (n3 == 0) break block38;
                if (object == null) break block39;
                object2 = object;
                if (object.firstOut != null) break block38;
            }
            object2 = FragmentTransition.ensureContainer((FragmentContainerTransition)object, sparseArray, n2);
            object2.firstOut = fragment;
            object2.firstOutIsPop = bl;
            object2.firstOutTransaction = backStackRecord;
        }
        if (!bl2 && n != 0 && object2 != null && object2.lastIn == fragment) {
            object2.lastIn = null;
        }
    }

    public static void calculateFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean bl) {
        int n = backStackRecord.mOps.size();
        for (int i = 0; i < n; ++i) {
            FragmentTransition.addToFirstInLastOut(backStackRecord, (FragmentTransaction.Op)backStackRecord.mOps.get(i), sparseArray, false, bl);
        }
    }

    private static ArrayMap<String, String> calculateNameOverrides(int n, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n2, int n3) {
        ArrayMap<String, String> arrayMap = new ArrayMap<String, String>();
        --n3;
        while (n3 >= n2) {
            Object object = arrayList.get(n3);
            if (object.interactsWith(n)) {
                boolean bl = arrayList2.get(n3);
                if (object.mSharedElementSourceNames != null) {
                    ArrayList arrayList3;
                    ArrayList arrayList4;
                    int n4 = object.mSharedElementSourceNames.size();
                    if (bl) {
                        arrayList4 = object.mSharedElementSourceNames;
                        arrayList3 = object.mSharedElementTargetNames;
                    } else {
                        arrayList3 = object.mSharedElementSourceNames;
                        arrayList4 = object.mSharedElementTargetNames;
                    }
                    for (int i = 0; i < n4; ++i) {
                        object = (String)arrayList3.get(i);
                        String string2 = (String)arrayList4.get(i);
                        String string3 = arrayMap.remove(string2);
                        if (string3 != null) {
                            arrayMap.put((String)object, string3);
                            continue;
                        }
                        arrayMap.put((String)object, string2);
                    }
                }
            }
            --n3;
        }
        return arrayMap;
    }

    public static void calculatePopFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean bl) {
        if (!backStackRecord.mManager.mContainer.onHasView()) {
            return;
        }
        for (int i = backStackRecord.mOps.size() - 1; i >= 0; --i) {
            FragmentTransition.addToFirstInLastOut(backStackRecord, (FragmentTransaction.Op)backStackRecord.mOps.get(i), sparseArray, true, bl);
        }
    }

    static void callSharedElementStartEnd(Fragment object, Fragment object2, boolean bl, ArrayMap<String, View> arrayMap, boolean bl2) {
        object = bl ? object2.getEnterTransitionCallback() : object.getEnterTransitionCallback();
        if (object != null) {
            object2 = new ArrayList();
            ArrayList<String> arrayList = new ArrayList<String>();
            int n = arrayMap == null ? 0 : arrayMap.size();
            for (int i = 0; i < n; ++i) {
                arrayList.add(arrayMap.keyAt(i));
                object2.add(arrayMap.valueAt(i));
            }
            if (bl2) {
                object.onSharedElementStart((List<String>)arrayList, (List<View>)object2, null);
                return;
            }
            object.onSharedElementEnd((List<String>)arrayList, (List<View>)object2, null);
        }
    }

    private static boolean canHandleAll(FragmentTransitionImpl fragmentTransitionImpl, List<Object> list) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            if (fragmentTransitionImpl.canHandle(list.get(i))) continue;
            return false;
        }
        return true;
    }

    static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl object, ArrayMap<String, String> arrayMap, Object object2, FragmentContainerTransition object3) {
        Fragment fragment = object3.lastIn;
        View view = fragment.getView();
        if (!arrayMap.isEmpty() && object2 != null && view != null) {
            ArrayMap<String, View> arrayMap2 = new ArrayMap<String, View>();
            object.findNamedViews(arrayMap2, view);
            object = object3.lastInTransaction;
            if (object3.lastInIsPop) {
                object2 = fragment.getExitTransitionCallback();
                object = object.mSharedElementSourceNames;
            } else {
                object2 = fragment.getEnterTransitionCallback();
                object = object.mSharedElementTargetNames;
            }
            if (object != null) {
                arrayMap2.retainAll(object);
                arrayMap2.retainAll(arrayMap.values());
            }
            if (object2 != null) {
                object2.onMapSharedElements((List<String>)object, (Map<String, View>)arrayMap2);
                for (int i = object.size() - 1; i >= 0; --i) {
                    object3 = (String)object.get(i);
                    object2 = arrayMap2.get(object3);
                    if (object2 == null) {
                        object2 = FragmentTransition.findKeyForValue(arrayMap, (String)object3);
                        if (object2 == null) continue;
                        arrayMap.remove(object2);
                        continue;
                    }
                    if (object3.equals(ViewCompat.getTransitionName((View)object2)) || (object3 = FragmentTransition.findKeyForValue(arrayMap, (String)object3)) == null) continue;
                    arrayMap.put((String)object3, ViewCompat.getTransitionName((View)object2));
                }
                return arrayMap2;
            }
            FragmentTransition.retainValues(arrayMap, arrayMap2);
            return arrayMap2;
        }
        arrayMap.clear();
        return null;
    }

    private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl object, ArrayMap<String, String> arrayMap, Object object2, FragmentContainerTransition object3) {
        if (!arrayMap.isEmpty() && object2 != null) {
            object2 = object3.firstOut;
            ArrayMap<String, View> arrayMap2 = new ArrayMap<String, View>();
            object.findNamedViews(arrayMap2, object2.requireView());
            object = object3.firstOutTransaction;
            if (object3.firstOutIsPop) {
                object2 = object2.getEnterTransitionCallback();
                object = object.mSharedElementTargetNames;
            } else {
                object2 = object2.getExitTransitionCallback();
                object = object.mSharedElementSourceNames;
            }
            if (object != null) {
                arrayMap2.retainAll(object);
            }
            if (object2 != null) {
                object2.onMapSharedElements((List<String>)object, (Map<String, View>)arrayMap2);
                for (int i = object.size() - 1; i >= 0; --i) {
                    object3 = (String)object.get(i);
                    object2 = arrayMap2.get(object3);
                    if (object2 == null) {
                        arrayMap.remove(object3);
                        continue;
                    }
                    if (object3.equals(ViewCompat.getTransitionName((View)object2))) continue;
                    object3 = arrayMap.remove(object3);
                    arrayMap.put(ViewCompat.getTransitionName((View)object2), (String)object3);
                }
                return arrayMap2;
            }
            arrayMap.retainAll(arrayMap2.keySet());
            return arrayMap2;
        }
        arrayMap.clear();
        return null;
    }

    private static FragmentTransitionImpl chooseImpl(Fragment object, Fragment fragment) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if (object != null) {
            Object object2 = object.getExitTransition();
            if (object2 != null) {
                arrayList.add(object2);
            }
            if ((object2 = object.getReturnTransition()) != null) {
                arrayList.add(object2);
            }
            if ((object = object.getSharedElementReturnTransition()) != null) {
                arrayList.add(object);
            }
        }
        if (fragment != null) {
            object = fragment.getEnterTransition();
            if (object != null) {
                arrayList.add(object);
            }
            if ((object = fragment.getReenterTransition()) != null) {
                arrayList.add(object);
            }
            if ((object = fragment.getSharedElementEnterTransition()) != null) {
                arrayList.add(object);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        object = PLATFORM_IMPL;
        if (object != null && FragmentTransition.canHandleAll((FragmentTransitionImpl)object, arrayList)) {
            return PLATFORM_IMPL;
        }
        object = SUPPORT_IMPL;
        if (object != null && FragmentTransition.canHandleAll((FragmentTransitionImpl)object, arrayList)) {
            return SUPPORT_IMPL;
        }
        if (PLATFORM_IMPL == null && SUPPORT_IMPL == null) {
            return null;
        }
        throw new IllegalArgumentException("Invalid Transition types");
    }

    static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl fragmentTransitionImpl, Object object, Fragment fragment, ArrayList<View> arrayList, View view) {
        ArrayList<View> arrayList2 = null;
        if (object != null) {
            ArrayList<View> arrayList3 = new ArrayList<View>();
            if ((fragment = fragment.getView()) != null) {
                fragmentTransitionImpl.captureTransitioningViews(arrayList3, (View)fragment);
            }
            if (arrayList != null) {
                arrayList3.removeAll(arrayList);
            }
            arrayList2 = arrayList3;
            if (!arrayList3.isEmpty()) {
                arrayList3.add(view);
                fragmentTransitionImpl.addTargets(object, arrayList3);
                arrayList2 = arrayList3;
            }
        }
        return arrayList2;
    }

    private static Object configureSharedElementsOrdered(final FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, final View view, final ArrayMap<String, String> arrayMap, final FragmentContainerTransition fragmentContainerTransition, final ArrayList<View> arrayList, final ArrayList<View> arrayList2, final Object object, Object object2) {
        final Fragment fragment = fragmentContainerTransition.lastIn;
        final Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment != null) {
            if (fragment2 == null) {
                return null;
            }
            final boolean bl = fragmentContainerTransition.lastInIsPop;
            final Object object3 = arrayMap.isEmpty() ? null : FragmentTransition.getSharedElementTransition(fragmentTransitionImpl, fragment, fragment2, bl);
            ArrayMap<String, View> arrayMap2 = FragmentTransition.captureOutSharedElements(fragmentTransitionImpl, arrayMap, object3, fragmentContainerTransition);
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
                fragmentTransitionImpl.setSharedElementTargets(object3, view, arrayList);
                FragmentTransition.setOutEpicenter(fragmentTransitionImpl, object3, object2, arrayMap2, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
                if (object != null) {
                    fragmentTransitionImpl.setEpicenter(object, rect);
                }
                object2 = rect;
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
                    View view2 = FragmentTransition.captureInSharedElements(fragmentTransitionImpl, arrayMap, object3, fragmentContainerTransition);
                    if (view2 != null) {
                        arrayList2.addAll(view2.values());
                        arrayList2.add(view);
                    }
                    FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, view2, false);
                    Object object2 = object3;
                    if (object2 != null) {
                        fragmentTransitionImpl.swapSharedElementTargets(object2, arrayList, arrayList2);
                        view2 = FragmentTransition.getInEpicenterView(view2, fragmentContainerTransition, object, bl);
                        if (view2 != null) {
                            fragmentTransitionImpl.getBoundsOnScreen(view2, this.val$inEpicenter);
                        }
                    }
                }
            });
            return object3;
        }
        return null;
    }

    private static Object configureSharedElementsReordered(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, View view, ArrayMap<String, String> object, FragmentContainerTransition fragmentContainerTransition, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object object2, Object object3) {
        final Fragment fragment = fragmentContainerTransition.lastIn;
        final Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment != null) {
            fragment.requireView().setVisibility(0);
        }
        if (fragment != null) {
            if (fragment2 == null) {
                return null;
            }
            final boolean bl = fragmentContainerTransition.lastInIsPop;
            Object object4 = object.isEmpty() ? null : FragmentTransition.getSharedElementTransition(fragmentTransitionImpl, fragment, fragment2, bl);
            ArrayMap<String, View> arrayMap = FragmentTransition.captureOutSharedElements(fragmentTransitionImpl, object, object4, fragmentContainerTransition);
            final ArrayMap<String, View> arrayMap2 = FragmentTransition.captureInSharedElements(fragmentTransitionImpl, object, object4, fragmentContainerTransition);
            if (object.isEmpty()) {
                if (arrayMap != null) {
                    arrayMap.clear();
                }
                if (arrayMap2 != null) {
                    arrayMap2.clear();
                }
                object = null;
            } else {
                FragmentTransition.addSharedElementsWithMatchingNames(arrayList, arrayMap, object.keySet());
                FragmentTransition.addSharedElementsWithMatchingNames(arrayList2, arrayMap2, object.values());
                object = object4;
            }
            if (object2 == null && object3 == null && object == null) {
                return null;
            }
            FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, arrayMap, true);
            if (object != null) {
                arrayList2.add(view);
                fragmentTransitionImpl.setSharedElementTargets(object, view, arrayList);
                FragmentTransition.setOutEpicenter(fragmentTransitionImpl, object, object3, arrayMap, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
                view = new Rect();
                fragmentContainerTransition = FragmentTransition.getInEpicenterView(arrayMap2, fragmentContainerTransition, object2, bl);
                if (fragmentContainerTransition != null) {
                    fragmentTransitionImpl.setEpicenter(object2, (Rect)view);
                }
            } else {
                view = null;
                fragmentContainerTransition = null;
            }
            OneShotPreDrawListener.add((View)viewGroup, new Runnable((View)fragmentContainerTransition, fragmentTransitionImpl, (Rect)view){
                final /* synthetic */ Rect val$epicenter;
                final /* synthetic */ View val$epicenterView;
                final /* synthetic */ FragmentTransitionImpl val$impl;
                {
                    this.val$epicenterView = view;
                    this.val$impl = fragmentTransitionImpl;
                    this.val$epicenter = rect;
                }

                @Override
                public void run() {
                    FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, arrayMap2, false);
                    View view = this.val$epicenterView;
                    if (view != null) {
                        this.val$impl.getBoundsOnScreen(view, this.val$epicenter);
                    }
                }
            });
            return object;
        }
        return null;
    }

    private static void configureTransitionsOrdered(FragmentManager fragmentManager, int n, FragmentContainerTransition object, View view, ArrayMap<String, String> arrayMap, Callback object2) {
        fragmentManager = fragmentManager.mContainer.onHasView() ? (ViewGroup)fragmentManager.mContainer.onFindViewById(n) : null;
        if (fragmentManager == null) {
            return;
        }
        Fragment fragment = object.firstOut;
        Fragment fragment2 = object.lastIn;
        FragmentTransitionImpl fragmentTransitionImpl = FragmentTransition.chooseImpl(fragment, fragment2);
        if (fragmentTransitionImpl == null) {
            return;
        }
        boolean bl = object.lastInIsPop;
        boolean bl2 = object.firstOutIsPop;
        Object object3 = FragmentTransition.getEnterTransition(fragmentTransitionImpl, fragment2, bl);
        Object object4 = FragmentTransition.getExitTransition(fragmentTransitionImpl, fragment, bl2);
        Object object5 = new ArrayList<View>();
        ArrayList<View> arrayList = new ArrayList<View>();
        Object object6 = FragmentTransition.configureSharedElementsOrdered(fragmentTransitionImpl, (ViewGroup)fragmentManager, view, arrayMap, (FragmentContainerTransition)object, object5, arrayList, object3, object4);
        if (object3 == null && object6 == null && object4 == null) {
            return;
        }
        ArrayList<View> arrayList2 = FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, object4, fragment, object5, view);
        if (arrayList2 == null || arrayList2.isEmpty()) {
            object4 = null;
        }
        fragmentTransitionImpl.addTarget(object3, view);
        object = FragmentTransition.mergeTransitions(fragmentTransitionImpl, object3, object4, object6, fragment2, object.lastInIsPop);
        if (fragment != null && arrayList2 != null && (arrayList2.size() > 0 || object5.size() > 0)) {
            object5 = new CancellationSignal();
            object2.onStart(fragment, (CancellationSignal)object5);
            fragmentTransitionImpl.setListenerForTransitionEnd(fragment, object, (CancellationSignal)object5, new Runnable((Callback)object2, fragment, (CancellationSignal)object5){
                final /* synthetic */ Callback val$callback;
                final /* synthetic */ Fragment val$outFragment;
                final /* synthetic */ CancellationSignal val$signal;
                {
                    this.val$callback = callback;
                    this.val$outFragment = fragment;
                    this.val$signal = cancellationSignal;
                }

                @Override
                public void run() {
                    this.val$callback.onComplete(this.val$outFragment, this.val$signal);
                }
            });
        }
        if (object != null) {
            object2 = new ArrayList();
            fragmentTransitionImpl.scheduleRemoveTargets(object, object3, (ArrayList<View>)object2, object4, arrayList2, object6, arrayList);
            FragmentTransition.scheduleTargetChange(fragmentTransitionImpl, (ViewGroup)fragmentManager, fragment2, view, arrayList, object3, object2, object4, arrayList2);
            fragmentTransitionImpl.setNameOverridesOrdered((View)fragmentManager, arrayList, arrayMap);
            fragmentTransitionImpl.beginDelayedTransition((ViewGroup)fragmentManager, object);
            fragmentTransitionImpl.scheduleNameReset((ViewGroup)fragmentManager, arrayList, arrayMap);
            return;
        }
    }

    private static void configureTransitionsReordered(FragmentManager fragmentManager, int n, FragmentContainerTransition object, View object2, ArrayMap<String, String> arrayMap, Callback object3) {
        fragmentManager = fragmentManager.mContainer.onHasView() ? (ViewGroup)fragmentManager.mContainer.onFindViewById(n) : null;
        if (fragmentManager == null) {
            return;
        }
        Fragment fragment = object.firstOut;
        Object object4 = object.lastIn;
        FragmentTransitionImpl fragmentTransitionImpl = FragmentTransition.chooseImpl(fragment, (Fragment)object4);
        if (fragmentTransitionImpl == null) {
            return;
        }
        boolean bl = object.lastInIsPop;
        boolean bl2 = object.firstOutIsPop;
        ArrayList<View> arrayList = new ArrayList<View>();
        ArrayList<View> arrayList2 = new ArrayList<View>();
        Object object5 = FragmentTransition.getEnterTransition(fragmentTransitionImpl, (Fragment)object4, bl);
        ArrayList<View> arrayList3 = FragmentTransition.getExitTransition(fragmentTransitionImpl, fragment, bl2);
        Object object6 = FragmentTransition.configureSharedElementsReordered(fragmentTransitionImpl, (ViewGroup)fragmentManager, (View)object2, arrayMap, (FragmentContainerTransition)object, arrayList2, arrayList, object5, arrayList3);
        if (object5 == null && object6 == null && arrayList3 == null) {
            return;
        }
        object = arrayList3;
        arrayList3 = FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, object, fragment, arrayList2, (View)object2);
        object2 = FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, object5, (Fragment)object4, arrayList, (View)object2);
        FragmentTransition.setViewVisibility(object2, 4);
        object4 = FragmentTransition.mergeTransitions(fragmentTransitionImpl, object5, object, object6, (Fragment)object4, bl);
        if (fragment != null && arrayList3 != null && (arrayList3.size() > 0 || arrayList2.size() > 0)) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            object3.onStart(fragment, cancellationSignal);
            fragmentTransitionImpl.setListenerForTransitionEnd(fragment, object4, cancellationSignal, new Runnable((Callback)object3, fragment, cancellationSignal){
                final /* synthetic */ Callback val$callback;
                final /* synthetic */ Fragment val$outFragment;
                final /* synthetic */ CancellationSignal val$signal;
                {
                    this.val$callback = callback;
                    this.val$outFragment = fragment;
                    this.val$signal = cancellationSignal;
                }

                @Override
                public void run() {
                    this.val$callback.onComplete(this.val$outFragment, this.val$signal);
                }
            });
        }
        if (object4 != null) {
            FragmentTransition.replaceHide(fragmentTransitionImpl, object, fragment, arrayList3);
            object3 = fragmentTransitionImpl.prepareSetNameOverridesReordered(arrayList);
            fragmentTransitionImpl.scheduleRemoveTargets(object4, object5, (ArrayList<View>)object2, object, arrayList3, object6, arrayList);
            fragmentTransitionImpl.beginDelayedTransition((ViewGroup)fragmentManager, object4);
            fragmentTransitionImpl.setNameOverridesReordered((View)fragmentManager, arrayList2, arrayList, (ArrayList<String>)object3, (Map<String, String>)arrayMap);
            FragmentTransition.setViewVisibility(object2, 0);
            fragmentTransitionImpl.swapSharedElementTargets(object6, arrayList2, arrayList);
            return;
        }
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

    private static Object getEnterTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment object, boolean bl) {
        if (object == null) {
            return null;
        }
        object = bl ? object.getReenterTransition() : object.getEnterTransition();
        return fragmentTransitionImpl.cloneTransition(object);
    }

    private static Object getExitTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment object, boolean bl) {
        if (object == null) {
            return null;
        }
        object = bl ? object.getReturnTransition() : object.getExitTransition();
        return fragmentTransitionImpl.cloneTransition(object);
    }

    static View getInEpicenterView(ArrayMap<String, View> arrayMap, FragmentContainerTransition object, Object object2, boolean bl) {
        object = object.lastInTransaction;
        if (object2 != null && arrayMap != null && object.mSharedElementSourceNames != null && !object.mSharedElementSourceNames.isEmpty()) {
            object = bl ? (String)object.mSharedElementSourceNames.get(0) : (String)object.mSharedElementTargetNames.get(0);
            return arrayMap.get(object);
        }
        return null;
    }

    private static Object getSharedElementTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment object, Fragment fragment, boolean bl) {
        if (object != null && fragment != null) {
            object = bl ? fragment.getSharedElementReturnTransition() : object.getSharedElementEnterTransition();
            return fragmentTransitionImpl.wrapTransitionInSet(fragmentTransitionImpl.cloneTransition(object));
        }
        return null;
    }

    private static Object mergeTransitions(FragmentTransitionImpl fragmentTransitionImpl, Object object, Object object2, Object object3, Fragment fragment, boolean bl) {
        boolean bl2;
        boolean bl3 = bl2 = true;
        if (object != null) {
            bl3 = bl2;
            if (object2 != null) {
                bl3 = bl2;
                if (fragment != null) {
                    bl = bl ? fragment.getAllowReturnTransitionOverlap() : fragment.getAllowEnterTransitionOverlap();
                    bl3 = bl;
                }
            }
        }
        if (bl3) {
            return fragmentTransitionImpl.mergeTransitionsTogether(object2, object, object3);
        }
        return fragmentTransitionImpl.mergeTransitionsInSequence(object2, object, object3);
    }

    private static void replaceHide(FragmentTransitionImpl fragmentTransitionImpl, Object object, Fragment fragment, final ArrayList<View> arrayList) {
        if (fragment != null && object != null && fragment.mAdded && fragment.mHidden && fragment.mHiddenChanged) {
            fragment.setHideReplaced(true);
            fragmentTransitionImpl.scheduleHideFragmentView(object, fragment.getView(), arrayList);
            OneShotPreDrawListener.add((View)fragment.mContainer, new Runnable(){

                @Override
                public void run() {
                    FragmentTransition.setViewVisibility(arrayList, 4);
                }
            });
        }
    }

    private static FragmentTransitionImpl resolveSupportImpl() {
        try {
            FragmentTransitionImpl fragmentTransitionImpl = (FragmentTransitionImpl)Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return fragmentTransitionImpl;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static void retainValues(ArrayMap<String, String> arrayMap, ArrayMap<String, View> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            if (arrayMap2.containsKey(arrayMap.valueAt(i))) continue;
            arrayMap.removeAt(i);
        }
    }

    private static void scheduleTargetChange(final FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, final Fragment fragment, final View view, final ArrayList<View> arrayList, final Object object, final ArrayList<View> arrayList2, final Object object2, final ArrayList<View> arrayList3) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable(){

            @Override
            public void run() {
                ArrayList<View> arrayList4 = object;
                if (arrayList4 != null) {
                    fragmentTransitionImpl.removeTarget(arrayList4, view);
                    arrayList4 = FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, object, fragment, arrayList, view);
                    arrayList2.addAll(arrayList4);
                }
                if (arrayList3 != null) {
                    if (object2 != null) {
                        arrayList4 = new ArrayList<View>();
                        arrayList4.add(view);
                        fragmentTransitionImpl.replaceTargets(object2, arrayList3, arrayList4);
                    }
                    arrayList3.clear();
                    arrayList3.add(view);
                }
            }
        });
    }

    private static void setOutEpicenter(FragmentTransitionImpl fragmentTransitionImpl, Object object, Object object2, ArrayMap<String, View> view, boolean bl, BackStackRecord object3) {
        if (object3.mSharedElementSourceNames != null && !object3.mSharedElementSourceNames.isEmpty()) {
            object3 = bl ? (String)object3.mSharedElementTargetNames.get(0) : (String)object3.mSharedElementSourceNames.get(0);
            view = view.get(object3);
            fragmentTransitionImpl.setEpicenter(object, view);
            if (object2 != null) {
                fragmentTransitionImpl.setEpicenter(object2, view);
            }
        }
    }

    static void setViewVisibility(ArrayList<View> arrayList, int n) {
        if (arrayList == null) {
            return;
        }
        for (int i = arrayList.size() - 1; i >= 0; --i) {
            arrayList.get(i).setVisibility(n);
        }
    }

    static void startTransitions(FragmentManager fragmentManager, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2, boolean bl, Callback callback) {
        int n3;
        BackStackRecord backStackRecord;
        if (fragmentManager.mCurState < 1) {
            return;
        }
        SparseArray sparseArray = new SparseArray();
        for (n3 = n; n3 < n2; ++n3) {
            backStackRecord = arrayList.get(n3);
            if (arrayList2.get(n3).booleanValue()) {
                FragmentTransition.calculatePopFragments(backStackRecord, sparseArray, bl);
                continue;
            }
            FragmentTransition.calculateFragments(backStackRecord, sparseArray, bl);
        }
        if (sparseArray.size() != 0) {
            backStackRecord = new View(fragmentManager.mHost.getContext());
            n3 = sparseArray.size();
            for (int i = 0; i < n3; ++i) {
                int n4 = sparseArray.keyAt(i);
                ArrayMap<String, String> arrayMap = FragmentTransition.calculateNameOverrides(n4, arrayList, arrayList2, n, n2);
                FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition)sparseArray.valueAt(i);
                if (bl) {
                    FragmentTransition.configureTransitionsReordered(fragmentManager, n4, fragmentContainerTransition, (View)backStackRecord, arrayMap, callback);
                    continue;
                }
                FragmentTransition.configureTransitionsOrdered(fragmentManager, n4, fragmentContainerTransition, (View)backStackRecord, arrayMap, callback);
            }
        }
    }

    static boolean supportsTransition() {
        if (PLATFORM_IMPL == null && SUPPORT_IMPL == null) {
            return false;
        }
        return true;
    }

    static interface Callback {
        public void onComplete(Fragment var1, CancellationSignal var2);

        public void onStart(Fragment var1, CancellationSignal var2);
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

