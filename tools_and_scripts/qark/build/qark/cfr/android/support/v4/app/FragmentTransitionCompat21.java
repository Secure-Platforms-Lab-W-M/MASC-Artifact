/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.transition.Transition
 *  android.transition.Transition$EpicenterCallback
 *  android.transition.Transition$TransitionListener
 *  android.transition.TransitionManager
 *  android.transition.TransitionSet
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.v4.app;

import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.support.v4.app.OneShotPreDrawListener;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiresApi(value=21)
class FragmentTransitionCompat21 {
    FragmentTransitionCompat21() {
    }

    public static void addTarget(Object object, View view) {
        if (object != null) {
            ((Transition)object).addTarget(view);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void addTargets(Object object, ArrayList<View> arrayList) {
        block7 : {
            block6 : {
                if ((object = (Transition)object) == null) break block6;
                if (object instanceof TransitionSet) {
                    object = (TransitionSet)object;
                    int n = object.getTransitionCount();
                    int n2 = 0;
                    while (n2 < n) {
                        FragmentTransitionCompat21.addTargets((Object)object.getTransitionAt(n2), arrayList);
                        ++n2;
                    }
                    return;
                }
                if (!FragmentTransitionCompat21.hasSimpleTarget((Transition)object) && FragmentTransitionCompat21.isNullOrEmpty(object.getTargets())) break block7;
            }
            return;
        }
        int n = arrayList.size();
        int n3 = 0;
        while (n3 < n) {
            object.addTarget(arrayList.get(n3));
            ++n3;
        }
    }

    public static void beginDelayedTransition(ViewGroup viewGroup, Object object) {
        TransitionManager.beginDelayedTransition((ViewGroup)viewGroup, (Transition)((Transition)object));
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void bfsAddViewChildren(List<View> list, View view) {
        int n = list.size();
        if (!FragmentTransitionCompat21.containedBeforeIndex(list, view, n)) {
            list.add(view);
            for (int i = n; i < list.size(); ++i) {
                view = list.get(i);
                if (!(view instanceof ViewGroup)) continue;
                view = (ViewGroup)view;
                int n2 = view.getChildCount();
                for (int j = 0; j < n2; ++j) {
                    View view2 = view.getChildAt(j);
                    if (FragmentTransitionCompat21.containedBeforeIndex(list, view2, n)) continue;
                    list.add(view2);
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void captureTransitioningViews(ArrayList<View> arrayList, View view) {
        if (view.getVisibility() != 0) return;
        if (view instanceof ViewGroup) {
            if ((view = (ViewGroup)view).isTransitionGroup()) {
                arrayList.add(view);
                return;
            }
            int n = view.getChildCount();
            int n2 = 0;
            while (n2 < n) {
                FragmentTransitionCompat21.captureTransitioningViews(arrayList, view.getChildAt(n2));
                ++n2;
            }
            return;
        }
        arrayList.add(view);
    }

    public static Object cloneTransition(Object object) {
        Transition transition = null;
        if (object != null) {
            transition = ((Transition)object).clone();
        }
        return transition;
    }

    private static boolean containedBeforeIndex(List<View> list, View view, int n) {
        for (int i = 0; i < n; ++i) {
            if (list.get(i) != view) continue;
            return true;
        }
        return false;
    }

    private static String findKeyForValue(Map<String, String> object, String string2) {
        for (Map.Entry entry : object.entrySet()) {
            if (!string2.equals(entry.getValue())) continue;
            return (String)entry.getKey();
        }
        return null;
    }

    public static void findNamedViews(Map<String, View> map, View view) {
        if (view.getVisibility() == 0) {
            String string2 = view.getTransitionName();
            if (string2 != null) {
                map.put(string2, view);
            }
            if (view instanceof ViewGroup) {
                view = (ViewGroup)view;
                int n = view.getChildCount();
                for (int i = 0; i < n; ++i) {
                    FragmentTransitionCompat21.findNamedViews(map, view.getChildAt(i));
                }
            }
        }
    }

    public static void getBoundsOnScreen(View view, Rect rect) {
        int[] arrn = new int[2];
        view.getLocationOnScreen(arrn);
        rect.set(arrn[0], arrn[1], arrn[0] + view.getWidth(), arrn[1] + view.getHeight());
    }

    private static boolean hasSimpleTarget(Transition transition) {
        if (!(FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetIds()) && FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetNames()) && FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetTypes()))) {
            return true;
        }
        return false;
    }

    private static boolean isNullOrEmpty(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Object mergeTransitionsInSequence(Object object, Object object2, Object object3) {
        Object var3_3 = null;
        object = (Transition)object;
        object2 = (Transition)object2;
        object3 = (Transition)object3;
        if (object != null && object2 != null) {
            object = new TransitionSet().addTransition((Transition)object).addTransition((Transition)object2).setOrdering(1);
        } else if (object == null) {
            object = var3_3;
            if (object2 != null) {
                object = object2;
            }
        }
        if (object3 == null) {
            return object;
        }
        object2 = new TransitionSet();
        if (object != null) {
            object2.addTransition((Transition)object);
        }
        object2.addTransition((Transition)object3);
        return object2;
    }

    public static Object mergeTransitionsTogether(Object object, Object object2, Object object3) {
        TransitionSet transitionSet = new TransitionSet();
        if (object != null) {
            transitionSet.addTransition((Transition)object);
        }
        if (object2 != null) {
            transitionSet.addTransition((Transition)object2);
        }
        if (object3 != null) {
            transitionSet.addTransition((Transition)object3);
        }
        return transitionSet;
    }

    public static ArrayList<String> prepareSetNameOverridesReordered(ArrayList<View> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList<String>();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            View view = arrayList.get(i);
            arrayList2.add(view.getTransitionName());
            view.setTransitionName(null);
        }
        return arrayList2;
    }

    public static void removeTarget(Object object, View view) {
        if (object != null) {
            ((Transition)object).removeTarget(view);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void replaceTargets(Object object, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        List list;
        if ((object = (Transition)object) instanceof TransitionSet) {
            object = (TransitionSet)object;
            int n = object.getTransitionCount();
            int n2 = 0;
            while (n2 < n) {
                FragmentTransitionCompat21.replaceTargets((Object)object.getTransitionAt(n2), arrayList, arrayList2);
                ++n2;
            }
            return;
        }
        if (!FragmentTransitionCompat21.hasSimpleTarget((Transition)object) && (list = object.getTargets()) != null && list.size() == arrayList.size() && list.containsAll(arrayList)) {
            int n = arrayList2 == null ? 0 : arrayList2.size();
            for (int i = 0; i < n; ++i) {
                object.addTarget(arrayList2.get(i));
            }
            for (n = arrayList.size() - 1; n >= 0; --n) {
                object.removeTarget(arrayList.get(n));
            }
        }
    }

    public static void scheduleHideFragmentView(Object object, final View view, final ArrayList<View> arrayList) {
        ((Transition)object).addListener(new Transition.TransitionListener(){

            public void onTransitionCancel(Transition transition) {
            }

            public void onTransitionEnd(Transition transition) {
                transition.removeListener((Transition.TransitionListener)this);
                view.setVisibility(8);
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    ((View)arrayList.get(i)).setVisibility(0);
                }
            }

            public void onTransitionPause(Transition transition) {
            }

            public void onTransitionResume(Transition transition) {
            }

            public void onTransitionStart(Transition transition) {
            }
        });
    }

    public static void scheduleNameReset(ViewGroup viewGroup, final ArrayList<View> arrayList, final Map<String, String> map) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable(){

            @Override
            public void run() {
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    View view = (View)arrayList.get(i);
                    String string2 = view.getTransitionName();
                    view.setTransitionName((String)map.get(string2));
                }
            }
        });
    }

    public static void scheduleRemoveTargets(Object object, final Object object2, final ArrayList<View> arrayList, final Object object3, final ArrayList<View> arrayList2, final Object object4, final ArrayList<View> arrayList3) {
        ((Transition)object).addListener(new Transition.TransitionListener(){

            public void onTransitionCancel(Transition transition) {
            }

            public void onTransitionEnd(Transition transition) {
            }

            public void onTransitionPause(Transition transition) {
            }

            public void onTransitionResume(Transition transition) {
            }

            public void onTransitionStart(Transition transition) {
                if (object2 != null) {
                    FragmentTransitionCompat21.replaceTargets(object2, arrayList, null);
                }
                if (object3 != null) {
                    FragmentTransitionCompat21.replaceTargets(object3, arrayList2, null);
                }
                if (object4 != null) {
                    FragmentTransitionCompat21.replaceTargets(object4, arrayList3, null);
                }
            }
        });
    }

    public static void setEpicenter(Object object, final Rect rect) {
        if (object != null) {
            ((Transition)object).setEpicenterCallback(new Transition.EpicenterCallback(){

                public Rect onGetEpicenter(Transition transition) {
                    if (rect == null || rect.isEmpty()) {
                        return null;
                    }
                    return rect;
                }
            });
        }
    }

    public static void setEpicenter(Object object, View view) {
        if (view != null) {
            object = (Transition)object;
            final Rect rect = new Rect();
            FragmentTransitionCompat21.getBoundsOnScreen(view, rect);
            object.setEpicenterCallback(new Transition.EpicenterCallback(){

                public Rect onGetEpicenter(Transition transition) {
                    return rect;
                }
            });
        }
    }

    public static void setNameOverridesOrdered(View view, final ArrayList<View> arrayList, final Map<String, String> map) {
        OneShotPreDrawListener.add(view, new Runnable(){

            @Override
            public void run() {
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    View view = (View)arrayList.get(i);
                    String string2 = view.getTransitionName();
                    if (string2 == null) continue;
                    view.setTransitionName(FragmentTransitionCompat21.findKeyForValue(map, string2));
                }
            }
        });
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static void setNameOverridesReordered(View var0, final ArrayList<View> var1_1, final ArrayList<View> var2_2, final ArrayList<String> var3_3, Map<String, String> var4_4) {
        var7_5 = var2_2.size();
        var8_6 = new ArrayList<String>();
        var5_7 = 0;
        block0 : do {
            if (var5_7 >= var7_5) {
                OneShotPreDrawListener.add(var0, new Runnable(){

                    @Override
                    public void run() {
                        for (int i = 0; i < var7_5; ++i) {
                            ((View)var2_2.get(i)).setTransitionName((String)var3_3.get(i));
                            ((View)var1_1.get(i)).setTransitionName((String)var8_6.get(i));
                        }
                    }
                });
                return;
            }
            var10_10 = var1_1.get(var5_7);
            var9_9 = var10_10.getTransitionName();
            var8_6.add(var9_9);
            if (var9_9 == null) ** GOTO lbl17
            var10_10.setTransitionName(null);
            var10_10 = var4_4.get(var9_9);
            var6_8 = 0;
            do {
                block8 : {
                    if (var6_8 < var7_5) break block8;
lbl17: // 3 sources:
                    do {
                        ++var5_7;
                        continue block0;
                        break;
                    } while (true);
                }
                if (var10_10.equals(var3_3.get(var6_8))) {
                    var2_2.get(var6_8).setTransitionName(var9_9);
                    ** continue;
                }
                ++var6_8;
            } while (true);
            break;
        } while (true);
    }

    public static void setSharedElementTargets(Object object, View view, ArrayList<View> arrayList) {
        object = (TransitionSet)object;
        List list = object.getTargets();
        list.clear();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            FragmentTransitionCompat21.bfsAddViewChildren(list, arrayList.get(i));
        }
        list.add(view);
        arrayList.add(view);
        FragmentTransitionCompat21.addTargets(object, arrayList);
    }

    public static void swapSharedElementTargets(Object object, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        if ((object = (TransitionSet)object) != null) {
            object.getTargets().clear();
            object.getTargets().addAll(arrayList2);
            FragmentTransitionCompat21.replaceTargets(object, arrayList, arrayList2);
        }
    }

    public static Object wrapTransitionInSet(Object object) {
        if (object == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition((Transition)object);
        return transitionSet;
    }

}

