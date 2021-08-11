/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.GhostViewImpl;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(value=21)
class GhostViewApi21
implements GhostViewImpl {
    private static final String TAG = "GhostViewApi21";
    private static Method sAddGhostMethod;
    private static boolean sAddGhostMethodFetched;
    private static Class<?> sGhostViewClass;
    private static boolean sGhostViewClassFetched;
    private static Method sRemoveGhostMethod;
    private static boolean sRemoveGhostMethodFetched;
    private final View mGhostView;

    private GhostViewApi21(@NonNull View view) {
        this.mGhostView = view;
    }

    private static void fetchAddGhostMethod() {
        if (!sAddGhostMethodFetched) {
            try {
                GhostViewApi21.fetchGhostViewClass();
                sAddGhostMethod = sGhostViewClass.getDeclaredMethod("addGhost", View.class, ViewGroup.class, Matrix.class);
                sAddGhostMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"GhostViewApi21", (String)"Failed to retrieve addGhost method", (Throwable)noSuchMethodException);
            }
            sAddGhostMethodFetched = true;
            return;
        }
    }

    private static void fetchGhostViewClass() {
        if (!sGhostViewClassFetched) {
            try {
                sGhostViewClass = Class.forName("android.view.GhostView");
            }
            catch (ClassNotFoundException classNotFoundException) {
                Log.i((String)"GhostViewApi21", (String)"Failed to retrieve GhostView class", (Throwable)classNotFoundException);
            }
            sGhostViewClassFetched = true;
            return;
        }
    }

    private static void fetchRemoveGhostMethod() {
        if (!sRemoveGhostMethodFetched) {
            try {
                GhostViewApi21.fetchGhostViewClass();
                sRemoveGhostMethod = sGhostViewClass.getDeclaredMethod("removeGhost", View.class);
                sRemoveGhostMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"GhostViewApi21", (String)"Failed to retrieve removeGhost method", (Throwable)noSuchMethodException);
            }
            sRemoveGhostMethodFetched = true;
            return;
        }
    }

    @Override
    public void reserveEndViewTransition(ViewGroup viewGroup, View view) {
    }

    @Override
    public void setVisibility(int n) {
        this.mGhostView.setVisibility(n);
    }

    static class Creator
    implements GhostViewImpl.Creator {
        Creator() {
        }

        @Override
        public GhostViewImpl addGhost(View object, ViewGroup viewGroup, Matrix matrix) {
            GhostViewApi21.fetchAddGhostMethod();
            if (sAddGhostMethod != null) {
                try {
                    object = new GhostViewApi21((View)sAddGhostMethod.invoke(null, new Object[]{object, viewGroup, matrix}));
                    return object;
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw new RuntimeException(invocationTargetException.getCause());
                }
                catch (IllegalAccessException illegalAccessException) {
                    return null;
                }
            }
            return null;
        }

        @Override
        public void removeGhost(View view) {
            GhostViewApi21.fetchRemoveGhostMethod();
            if (sRemoveGhostMethod != null) {
                try {
                    sRemoveGhostMethod.invoke(null, new Object[]{view});
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw new RuntimeException(invocationTargetException.getCause());
                }
                catch (IllegalAccessException illegalAccessException) {
                    // empty catch block
                }
                return;
            }
        }
    }

}

