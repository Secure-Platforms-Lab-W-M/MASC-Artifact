/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.LayoutTransition
 *  android.util.Log
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.R;
import android.support.transition.ViewGroupOverlayApi14;
import android.support.transition.ViewGroupOverlayImpl;
import android.support.transition.ViewGroupUtilsImpl;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(value=14)
class ViewGroupUtilsApi14
implements ViewGroupUtilsImpl {
    private static final int LAYOUT_TRANSITION_CHANGING = 4;
    private static final String TAG = "ViewGroupUtilsApi14";
    private static Method sCancelMethod;
    private static boolean sCancelMethodFetched;
    private static LayoutTransition sEmptyLayoutTransition;
    private static Field sLayoutSuppressedField;
    private static boolean sLayoutSuppressedFieldFetched;

    ViewGroupUtilsApi14() {
    }

    private static void cancelLayoutTransition(LayoutTransition layoutTransition) {
        Method method;
        if (!sCancelMethodFetched) {
            try {
                sCancelMethod = LayoutTransition.class.getDeclaredMethod("cancel", new Class[0]);
                sCancelMethod.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to access cancel method by reflection");
            }
            sCancelMethodFetched = true;
        }
        if ((method = sCancelMethod) != null) {
            try {
                method.invoke((Object)layoutTransition, new Object[0]);
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to invoke cancel method by reflection");
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to access cancel method by reflection");
            }
            return;
        }
    }

    @Override
    public ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup viewGroup) {
        return ViewGroupOverlayApi14.createFrom(viewGroup);
    }

    @Override
    public void suppressLayout(@NonNull ViewGroup viewGroup, boolean bl) {
        Field field;
        block15 : {
            boolean bl2;
            block14 : {
                block13 : {
                    if (sEmptyLayoutTransition == null) {
                        sEmptyLayoutTransition = new LayoutTransition(){

                            public boolean isChangingLayout() {
                                return true;
                            }
                        };
                        sEmptyLayoutTransition.setAnimator(2, null);
                        sEmptyLayoutTransition.setAnimator(0, null);
                        sEmptyLayoutTransition.setAnimator(1, null);
                        sEmptyLayoutTransition.setAnimator(3, null);
                        sEmptyLayoutTransition.setAnimator(4, null);
                    }
                    if (bl) {
                        LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
                        if (layoutTransition != null) {
                            if (layoutTransition.isRunning()) {
                                ViewGroupUtilsApi14.cancelLayoutTransition(layoutTransition);
                            }
                            if (layoutTransition != sEmptyLayoutTransition) {
                                viewGroup.setTag(R.id.transition_layout_save, (Object)layoutTransition);
                            }
                        }
                        viewGroup.setLayoutTransition(sEmptyLayoutTransition);
                        return;
                    }
                    viewGroup.setLayoutTransition(null);
                    if (!sLayoutSuppressedFieldFetched) {
                        try {
                            sLayoutSuppressedField = ViewGroup.class.getDeclaredField("mLayoutSuppressed");
                            sLayoutSuppressedField.setAccessible(true);
                        }
                        catch (NoSuchFieldException noSuchFieldException) {
                            Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to access mLayoutSuppressed field by reflection");
                        }
                        sLayoutSuppressedFieldFetched = true;
                    }
                    bl2 = false;
                    bl = false;
                    field = sLayoutSuppressedField;
                    if (field == null) break block14;
                    bl2 = field.getBoolean((Object)viewGroup);
                    if (!bl2) break block13;
                    bl = bl2;
                    try {
                        sLayoutSuppressedField.setBoolean((Object)viewGroup, false);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        Log.i((String)"ViewGroupUtilsApi14", (String)"Failed to get mLayoutSuppressed field by reflection");
                    }
                }
                bl = bl2;
                break block15;
                break block15;
            }
            bl = bl2;
        }
        if (bl) {
            viewGroup.requestLayout();
        }
        if ((field = (LayoutTransition)viewGroup.getTag(R.id.transition_layout_save)) != null) {
            viewGroup.setTag(R.id.transition_layout_save, (Object)null);
            viewGroup.setLayoutTransition((LayoutTransition)field);
            return;
        }
    }

}

