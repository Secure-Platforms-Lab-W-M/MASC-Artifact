/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class FragmentTransitionImpl {
    protected static void bfsAddViewChildren(List<View> list, View view) {
        int n = list.size();
        if (FragmentTransitionImpl.containedBeforeIndex(list, view, n)) {
            return;
        }
        list.add(view);
        for (int i = n; i < list.size(); ++i) {
            view = list.get(i);
            if (!(view instanceof ViewGroup)) continue;
            view = (ViewGroup)view;
            int n2 = view.getChildCount();
            for (int j = 0; j < n2; ++j) {
                View view2 = view.getChildAt(j);
                if (FragmentTransitionImpl.containedBeforeIndex(list, view2, n)) continue;
                list.add(view2);
            }
        }
    }

    private static boolean containedBeforeIndex(List<View> list, View view, int n) {
        for (int i = 0; i < n; ++i) {
            if (list.get(i) != view) continue;
            return true;
        }
        return false;
    }

    static String findKeyForValue(Map<String, String> object, String string2) {
        for (Map.Entry entry : object.entrySet()) {
            if (!string2.equals(entry.getValue())) continue;
            return (String)entry.getKey();
        }
        return null;
    }

    protected static boolean isNullOrEmpty(List list) {
        if (list != null && !list.isEmpty()) {
            return false;
        }
        return true;
    }

    public abstract void addTarget(Object var1, View var2);

    public abstract void addTargets(Object var1, ArrayList<View> var2);

    public abstract void beginDelayedTransition(ViewGroup var1, Object var2);

    public abstract boolean canHandle(Object var1);

    void captureTransitioningViews(ArrayList<View> arrayList, View view) {
        if (view.getVisibility() == 0) {
            if (view instanceof ViewGroup) {
                if (ViewGroupCompat.isTransitionGroup((ViewGroup)(view = (ViewGroup)view))) {
                    arrayList.add(view);
                } else {
                    int n = view.getChildCount();
                    for (int i = 0; i < n; ++i) {
                        this.captureTransitioningViews(arrayList, view.getChildAt(i));
                    }
                }
                return;
            }
            arrayList.add(view);
        }
    }

    public abstract Object cloneTransition(Object var1);

    void findNamedViews(Map<String, View> map, View view) {
        if (view.getVisibility() == 0) {
            String string2 = ViewCompat.getTransitionName(view);
            if (string2 != null) {
                map.put(string2, view);
            }
            if (view instanceof ViewGroup) {
                view = (ViewGroup)view;
                int n = view.getChildCount();
                for (int i = 0; i < n; ++i) {
                    this.findNamedViews(map, view.getChildAt(i));
                }
            }
        }
    }

    protected void getBoundsOnScreen(View view, Rect rect) {
        int[] arrn = new int[2];
        view.getLocationOnScreen(arrn);
        rect.set(arrn[0], arrn[1], arrn[0] + view.getWidth(), arrn[1] + view.getHeight());
    }

    public abstract Object mergeTransitionsInSequence(Object var1, Object var2, Object var3);

    public abstract Object mergeTransitionsTogether(Object var1, Object var2, Object var3);

    ArrayList<String> prepareSetNameOverridesReordered(ArrayList<View> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList<String>();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            View view = arrayList.get(i);
            arrayList2.add(ViewCompat.getTransitionName(view));
            ViewCompat.setTransitionName(view, null);
        }
        return arrayList2;
    }

    public abstract void removeTarget(Object var1, View var2);

    public abstract void replaceTargets(Object var1, ArrayList<View> var2, ArrayList<View> var3);

    public abstract void scheduleHideFragmentView(Object var1, View var2, ArrayList<View> var3);

    void scheduleNameReset(ViewGroup viewGroup, final ArrayList<View> arrayList, final Map<String, String> map) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable(){

            @Override
            public void run() {
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    View view = (View)arrayList.get(i);
                    String string2 = ViewCompat.getTransitionName(view);
                    ViewCompat.setTransitionName(view, (String)map.get(string2));
                }
            }
        });
    }

    public abstract void scheduleRemoveTargets(Object var1, Object var2, ArrayList<View> var3, Object var4, ArrayList<View> var5, Object var6, ArrayList<View> var7);

    public abstract void setEpicenter(Object var1, Rect var2);

    public abstract void setEpicenter(Object var1, View var2);

    public void setListenerForTransitionEnd(Fragment fragment, Object object, CancellationSignal cancellationSignal, Runnable runnable) {
        runnable.run();
    }

    void setNameOverridesOrdered(View view, final ArrayList<View> arrayList, final Map<String, String> map) {
        OneShotPreDrawListener.add(view, new Runnable(){

            @Override
            public void run() {
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    View view = (View)arrayList.get(i);
                    String string2 = ViewCompat.getTransitionName(view);
                    if (string2 == null) continue;
                    ViewCompat.setTransitionName(view, FragmentTransitionImpl.findKeyForValue(map, string2));
                }
            }
        });
    }

    void setNameOverridesReordered(View view, final ArrayList<View> arrayList, final ArrayList<View> arrayList2, final ArrayList<String> arrayList3, Map<String, String> map) {
        final int n = arrayList2.size();
        final ArrayList<String> arrayList4 = new ArrayList<String>();
        block0 : for (int i = 0; i < n; ++i) {
            Object object = arrayList.get(i);
            String string2 = ViewCompat.getTransitionName((View)object);
            arrayList4.add(string2);
            if (string2 == null) continue;
            ViewCompat.setTransitionName((View)object, null);
            object = map.get(string2);
            for (int j = 0; j < n; ++j) {
                if (!object.equals(arrayList3.get(j))) continue;
                ViewCompat.setTransitionName(arrayList2.get(j), string2);
                continue block0;
            }
        }
        OneShotPreDrawListener.add(view, new Runnable(){

            @Override
            public void run() {
                for (int i = 0; i < n; ++i) {
                    ViewCompat.setTransitionName((View)arrayList2.get(i), (String)arrayList3.get(i));
                    ViewCompat.setTransitionName((View)arrayList.get(i), (String)arrayList4.get(i));
                }
            }
        });
    }

    public abstract void setSharedElementTargets(Object var1, View var2, ArrayList<View> var3);

    public abstract void swapSharedElementTargets(Object var1, ArrayList<View> var2, ArrayList<View> var3);

    public abstract Object wrapTransitionInSet(Object var1);

}

