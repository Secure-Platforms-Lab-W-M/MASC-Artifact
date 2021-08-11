// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.content.Context;
import android.util.Log;
import android.view.ViewConfiguration;
import android.os.Build$VERSION;
import java.lang.reflect.Method;

@Deprecated
public final class ViewConfigurationCompat
{
    private static final String TAG = "ViewConfigCompat";
    private static Method sGetScaledScrollFactorMethod;
    
    static {
        if (Build$VERSION.SDK_INT == 25) {
            try {
                ViewConfigurationCompat.sGetScaledScrollFactorMethod = ViewConfiguration.class.getDeclaredMethod("getScaledScrollFactor", (Class<?>[])new Class[0]);
            }
            catch (Exception ex) {
                Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
            }
        }
    }
    
    private ViewConfigurationCompat() {
    }
    
    private static float getLegacyScrollFactor(final ViewConfiguration viewConfiguration, final Context context) {
        if (Build$VERSION.SDK_INT >= 25) {
            final Method sGetScaledScrollFactorMethod = ViewConfigurationCompat.sGetScaledScrollFactorMethod;
            if (sGetScaledScrollFactorMethod != null) {
                try {
                    return (float)(int)sGetScaledScrollFactorMethod.invoke(viewConfiguration, new Object[0]);
                }
                catch (Exception ex) {
                    Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
                }
            }
        }
        final TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(16842829, typedValue, true)) {
            return typedValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return 0.0f;
    }
    
    public static float getScaledHorizontalScrollFactor(@NonNull final ViewConfiguration viewConfiguration, @NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 26) {
            return viewConfiguration.getScaledHorizontalScrollFactor();
        }
        return getLegacyScrollFactor(viewConfiguration, context);
    }
    
    @Deprecated
    public static int getScaledPagingTouchSlop(final ViewConfiguration viewConfiguration) {
        return viewConfiguration.getScaledPagingTouchSlop();
    }
    
    public static float getScaledVerticalScrollFactor(@NonNull final ViewConfiguration viewConfiguration, @NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 26) {
            return viewConfiguration.getScaledVerticalScrollFactor();
        }
        return getLegacyScrollFactor(viewConfiguration, context);
    }
    
    @Deprecated
    public static boolean hasPermanentMenuKey(final ViewConfiguration viewConfiguration) {
        return viewConfiguration.hasPermanentMenuKey();
    }
}
