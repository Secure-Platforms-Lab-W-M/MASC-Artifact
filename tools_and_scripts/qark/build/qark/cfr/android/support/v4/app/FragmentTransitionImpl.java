/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.v4.app;

import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v4.app.OneShotPreDrawListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public abstract class FragmentTransitionImpl {
    /*
     * Enabled aggressive block sorting
     */
    protected static void bfsAddViewChildren(List<View> list, View view) {
        int n = list.size();
        if (!FragmentTransitionImpl.containedBeforeIndex(list, view, n)) {
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
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public abstract void addTarget(Object var1, View var2);

    public abstract void addTargets(Object var1, ArrayList<View> var2);

    public abstract void beginDelayedTransition(ViewGroup var1, Object var2);

    public abstract boolean canHandle(Object var1);

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    void captureTransitioningViews(ArrayList<View> arrayList, View view) {
        if (view.getVisibility() != 0) return;
        if (view instanceof ViewGroup) {
            if (ViewGroupCompat.isTransitionGroup((ViewGroup)(view = (ViewGroup)view))) {
                arrayList.add(view);
                return;
            }
            int n = view.getChildCount();
            int n2 = 0;
            while (n2 < n) {
                this.captureTransitioningViews(arrayList, view.getChildAt(n2));
                ++n2;
            }
            return;
        }
        arrayList.add(view);
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

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void setNameOverridesReordered(View var1_1, final ArrayList<View> var2_2, final ArrayList<View> var3_3, final ArrayList<String> var4_4, Map<String, String> var5_5) {
        var8_6 = var3_3.size();
        var9_7 = new ArrayList<String>();
        var6_8 = 0;
        block0 : do {
            if (var6_8 >= var8_6) {
                OneShotPreDrawListener.add(var1_1, new Runnable(){

                    @Override
                    public void run() {
                        for (int i = 0; i < var8_6; ++i) {
                            ViewCompat.setTransitionName((View)var3_3.get(i), (String)var4_4.get(i));
                            ViewCompat.setTransitionName((View)var2_2.get(i), (String)var9_7.get(i));
                        }
                    }
                });
                return;
            }
            var11_11 = var2_2.get(var6_8);
            var10_10 = ViewCompat.getTransitionName((View)var11_11);
            var9_7.add(var10_10);
            if (var10_10 == null) ** GOTO lbl17
            ViewCompat.setTransitionName((View)var11_11, null);
            var11_11 = var5_5.get(var10_10);
            var7_9 = 0;
            do {
                block8 : {
                    if (var7_9 < var8_6) break block8;
lbl17: // 3 sources:
                    do {
                        ++var6_8;
                        continue block0;
                        break;
                    } while (true);
                }
                if (var11_11.equals(var4_4.get(var7_9))) {
                    ViewCompat.setTransitionName(var3_3.get(var7_9), var10_10);
                    ** continue;
                }
                ++var7_9;
            } while (true);
            break;
        } while (true);
    }

    public abstract void setSharedElementTargets(Object var1, View var2, ArrayList<View> var3);

    public abstract void swapSharedElementTargets(Object var1, ArrayList<View> var2, ArrayList<View> var3);

    public abstract Object wrapTransitionInSet(Object var1);

}

