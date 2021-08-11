/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.hardware.display.DisplayManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.Display
 *  android.view.WindowManager
 */
package androidx.core.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public final class DisplayManagerCompat {
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap<Context, DisplayManagerCompat> sInstances = new WeakHashMap();
    private final Context mContext;

    private DisplayManagerCompat(Context context) {
        this.mContext = context;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static DisplayManagerCompat getInstance(Context context) {
        WeakHashMap<Context, DisplayManagerCompat> weakHashMap = sInstances;
        synchronized (weakHashMap) {
            DisplayManagerCompat displayManagerCompat;
            DisplayManagerCompat displayManagerCompat2 = displayManagerCompat = sInstances.get((Object)context);
            if (displayManagerCompat == null) {
                displayManagerCompat2 = new DisplayManagerCompat(context);
                sInstances.put(context, displayManagerCompat2);
            }
            return displayManagerCompat2;
        }
    }

    public Display getDisplay(int n) {
        if (Build.VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplay(n);
        }
        Display display = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
        if (display.getDisplayId() == n) {
            return display;
        }
        return null;
    }

    public Display[] getDisplays() {
        if (Build.VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplays();
        }
        return new Display[]{((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay()};
    }

    public Display[] getDisplays(String string2) {
        if (Build.VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplays(string2);
        }
        if (string2 == null) {
            return new Display[0];
        }
        return new Display[]{((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay()};
    }
}

