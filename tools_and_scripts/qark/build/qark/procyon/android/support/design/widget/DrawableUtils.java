// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.util.Log;
import android.graphics.drawable.DrawableContainer$DrawableContainerState;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.drawable.DrawableContainer;
import java.lang.reflect.Method;

class DrawableUtils
{
    private static final String LOG_TAG = "DrawableUtils";
    private static Method sSetConstantStateMethod;
    private static boolean sSetConstantStateMethodFetched;
    
    private DrawableUtils() {
    }
    
    static boolean setContainerConstantState(final DrawableContainer drawableContainer, final Drawable$ConstantState drawable$ConstantState) {
        return setContainerConstantStateV9(drawableContainer, drawable$ConstantState);
    }
    
    private static boolean setContainerConstantStateV9(final DrawableContainer drawableContainer, final Drawable$ConstantState drawable$ConstantState) {
        if (!DrawableUtils.sSetConstantStateMethodFetched) {
            try {
                (DrawableUtils.sSetConstantStateMethod = DrawableContainer.class.getDeclaredMethod("setConstantState", DrawableContainer$DrawableContainerState.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.e("DrawableUtils", "Could not fetch setConstantState(). Oh well.");
            }
            DrawableUtils.sSetConstantStateMethodFetched = true;
        }
        final Method sSetConstantStateMethod = DrawableUtils.sSetConstantStateMethod;
        if (sSetConstantStateMethod != null) {
            try {
                sSetConstantStateMethod.invoke(drawableContainer, drawable$ConstantState);
                return true;
            }
            catch (Exception ex2) {
                Log.e("DrawableUtils", "Could not invoke setConstantState(). Oh well.");
                return false;
            }
        }
        return false;
    }
}
