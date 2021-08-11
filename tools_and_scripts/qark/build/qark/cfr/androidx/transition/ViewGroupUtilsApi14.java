/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.LayoutTransition
 *  android.util.Log
 *  android.view.ViewGroup
 *  androidx.transition.R
 *  androidx.transition.R$id
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.util.Log;
import android.view.ViewGroup;
import androidx.transition.R;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi14 {
    private static final int LAYOUT_TRANSITION_CHANGING = 4;
    private static final String TAG = "ViewGroupUtilsApi14";
    private static Method sCancelMethod;
    private static boolean sCancelMethodFetched;
    private static LayoutTransition sEmptyLayoutTransition;
    private static Field sLayoutSuppressedField;
    private static boolean sLayoutSuppressedFieldFetched;

    private ViewGroupUtilsApi14() {
    }

    private static void cancelLayoutTransition(LayoutTransition layoutTransition) {
        Method method;
        if (!sCancelMethodFetched) {
            try {
                sCancelMethod = method = LayoutTransition.class.getDeclaredMethod("cancel", new Class[0]);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to access cancel method by reflection");
            }
            sCancelMethodFetched = true;
        }
        if ((method = sCancelMethod) != null) {
            try {
                method.invoke((Object)layoutTransition, new Object[0]);
                return;
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to invoke cancel method by reflection");
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to access cancel method by reflection");
            }
        }
    }

    static void suppressLayout(ViewGroup viewGroup, boolean bl) {
        Object object;
        if (sEmptyLayoutTransition == null) {
            object = new LayoutTransition(){

                public boolean isChangingLayout() {
                    return true;
                }
            };
            sEmptyLayoutTransition = object;
            object.setAnimator(2, null);
            sEmptyLayoutTransition.setAnimator(0, null);
            sEmptyLayoutTransition.setAnimator(1, null);
            sEmptyLayoutTransition.setAnimator(3, null);
            sEmptyLayoutTransition.setAnimator(4, null);
        }
        if (bl) {
            object = viewGroup.getLayoutTransition();
            if (object != null) {
                if (object.isRunning()) {
                    ViewGroupUtilsApi14.cancelLayoutTransition((LayoutTransition)object);
                }
                if (object != sEmptyLayoutTransition) {
                    viewGroup.setTag(R.id.transition_layout_save, object);
                }
            }
            viewGroup.setLayoutTransition(sEmptyLayoutTransition);
            return;
        }
        viewGroup.setLayoutTransition(null);
        if (!sLayoutSuppressedFieldFetched) {
            try {
                sLayoutSuppressedField = object = ViewGroup.class.getDeclaredField("mLayoutSuppressed");
                object.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to access mLayoutSuppressed field by reflection");
            }
            sLayoutSuppressedFieldFetched = true;
        }
        bl = false;
        boolean bl2 = false;
        object = sLayoutSuppressedField;
        if (object != null) {
            block14 : {
                bl = bl2;
                bl2 = object.getBoolean((Object)viewGroup);
                if (!bl2) break block14;
                bl = bl2;
                try {
                    sLayoutSuppressedField.setBoolean((Object)viewGroup, false);
                }
                catch (IllegalAccessException illegalAccessException) {
                    Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to get mLayoutSuppressed field by reflection");
                }
            }
            bl = bl2;
        }
        if (bl) {
            viewGroup.requestLayout();
        }
        if ((object = (LayoutTransition)viewGroup.getTag(R.id.transition_layout_save)) != null) {
            viewGroup.setTag(R.id.transition_layout_save, (Object)null);
            viewGroup.setLayoutTransition((LayoutTransition)object);
        }
    }

}

