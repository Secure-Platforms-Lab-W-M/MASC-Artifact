// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.lang.reflect.Field;
import android.animation.LayoutTransition;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
class ViewGroupUtilsApi14 implements ViewGroupUtilsImpl
{
    private static final int LAYOUT_TRANSITION_CHANGING = 4;
    private static final String TAG = "ViewGroupUtilsApi14";
    private static Method sCancelMethod;
    private static boolean sCancelMethodFetched;
    private static LayoutTransition sEmptyLayoutTransition;
    private static Field sLayoutSuppressedField;
    private static boolean sLayoutSuppressedFieldFetched;
    
    private static void cancelLayoutTransition(final LayoutTransition layoutTransition) {
        if (!ViewGroupUtilsApi14.sCancelMethodFetched) {
            try {
                (ViewGroupUtilsApi14.sCancelMethod = LayoutTransition.class.getDeclaredMethod("cancel", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
            }
            ViewGroupUtilsApi14.sCancelMethodFetched = true;
        }
        final Method sCancelMethod = ViewGroupUtilsApi14.sCancelMethod;
        if (sCancelMethod != null) {
            try {
                sCancelMethod.invoke(layoutTransition, new Object[0]);
            }
            catch (InvocationTargetException ex2) {
                Log.i("ViewGroupUtilsApi14", "Failed to invoke cancel method by reflection");
            }
            catch (IllegalAccessException ex3) {
                Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
            }
        }
    }
    
    @Override
    public ViewGroupOverlayImpl getOverlay(@NonNull final ViewGroup viewGroup) {
        return ViewGroupOverlayApi14.createFrom(viewGroup);
    }
    
    @Override
    public void suppressLayout(@NonNull final ViewGroup viewGroup, boolean b) {
        if (ViewGroupUtilsApi14.sEmptyLayoutTransition == null) {
            (ViewGroupUtilsApi14.sEmptyLayoutTransition = new LayoutTransition() {
                public boolean isChangingLayout() {
                    return true;
                }
            }).setAnimator(2, (Animator)null);
            ViewGroupUtilsApi14.sEmptyLayoutTransition.setAnimator(0, (Animator)null);
            ViewGroupUtilsApi14.sEmptyLayoutTransition.setAnimator(1, (Animator)null);
            ViewGroupUtilsApi14.sEmptyLayoutTransition.setAnimator(3, (Animator)null);
            ViewGroupUtilsApi14.sEmptyLayoutTransition.setAnimator(4, (Animator)null);
        }
        if (b) {
            final LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
            if (layoutTransition != null) {
                if (layoutTransition.isRunning()) {
                    cancelLayoutTransition(layoutTransition);
                }
                if (layoutTransition != ViewGroupUtilsApi14.sEmptyLayoutTransition) {
                    viewGroup.setTag(R.id.transition_layout_save, (Object)layoutTransition);
                }
            }
            viewGroup.setLayoutTransition(ViewGroupUtilsApi14.sEmptyLayoutTransition);
            return;
        }
        viewGroup.setLayoutTransition((LayoutTransition)null);
        if (!ViewGroupUtilsApi14.sLayoutSuppressedFieldFetched) {
            try {
                (ViewGroupUtilsApi14.sLayoutSuppressedField = ViewGroup.class.getDeclaredField("mLayoutSuppressed")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.i("ViewGroupUtilsApi14", "Failed to access mLayoutSuppressed field by reflection");
            }
            ViewGroupUtilsApi14.sLayoutSuppressedFieldFetched = true;
        }
        final boolean b2 = false;
        b = false;
        final Field sLayoutSuppressedField = ViewGroupUtilsApi14.sLayoutSuppressedField;
        if (sLayoutSuppressedField != null) {
            try {
                final boolean boolean1 = sLayoutSuppressedField.getBoolean(viewGroup);
                if (boolean1) {
                    b = boolean1;
                    ViewGroupUtilsApi14.sLayoutSuppressedField.setBoolean(viewGroup, false);
                }
                b = boolean1;
            }
            catch (IllegalAccessException ex2) {
                Log.i("ViewGroupUtilsApi14", "Failed to get mLayoutSuppressed field by reflection");
            }
        }
        else {
            b = b2;
        }
        if (b) {
            viewGroup.requestLayout();
        }
        final LayoutTransition layoutTransition2 = (LayoutTransition)viewGroup.getTag(R.id.transition_layout_save);
        if (layoutTransition2 != null) {
            viewGroup.setTag(R.id.transition_layout_save, (Object)null);
            viewGroup.setLayoutTransition(layoutTransition2);
        }
    }
}
