/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import androidx.appcompat.widget.TintResources;
import androidx.appcompat.widget.VectorEnabledTintResources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TintContextWrapper
extends ContextWrapper {
    private static final Object CACHE_LOCK = new Object();
    private static ArrayList<WeakReference<TintContextWrapper>> sCache;
    private final Resources mResources;
    private final Resources.Theme mTheme;

    private TintContextWrapper(Context context) {
        super(context);
        if (VectorEnabledTintResources.shouldBeUsed()) {
            VectorEnabledTintResources vectorEnabledTintResources = new VectorEnabledTintResources((Context)this, context.getResources());
            this.mResources = vectorEnabledTintResources;
            vectorEnabledTintResources = vectorEnabledTintResources.newTheme();
            this.mTheme = vectorEnabledTintResources;
            vectorEnabledTintResources.setTo(context.getTheme());
            return;
        }
        this.mResources = new TintResources((Context)this, context.getResources());
        this.mTheme = null;
    }

    private static boolean shouldWrap(Context context) {
        boolean bl = context instanceof TintContextWrapper;
        boolean bl2 = false;
        if (!bl && !(context.getResources() instanceof TintResources)) {
            if (context.getResources() instanceof VectorEnabledTintResources) {
                return false;
            }
            if (Build.VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed()) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Context wrap(Context object) {
        if (!TintContextWrapper.shouldWrap((Context)object)) {
            return object;
        }
        Object object2 = CACHE_LOCK;
        synchronized (object2) {
            if (sCache == null) {
                sCache = new ArrayList();
            } else {
                WeakReference<TintContextWrapper> weakReference;
                int n = sCache.size() - 1;
                do {
                    if (n < 0) break;
                    weakReference = sCache.get(n);
                    if (weakReference == null || weakReference.get() == null) {
                        sCache.remove(n);
                    }
                    --n;
                } while (true);
                for (n = TintContextWrapper.sCache.size() - 1; n >= 0; --n) {
                    weakReference = sCache.get(n);
                    weakReference = weakReference != null ? weakReference.get() : null;
                    if (weakReference == null || weakReference.getBaseContext() != object) continue;
                    return weakReference;
                }
            }
            object = new TintContextWrapper((Context)object);
            sCache.add(new WeakReference<Object>(object));
            return object;
        }
    }

    public AssetManager getAssets() {
        return this.mResources.getAssets();
    }

    public Resources getResources() {
        return this.mResources;
    }

    public Resources.Theme getTheme() {
        Resources.Theme theme;
        Resources.Theme theme2 = theme = this.mTheme;
        if (theme == null) {
            theme2 = super.getTheme();
        }
        return theme2;
    }

    public void setTheme(int n) {
        Resources.Theme theme = this.mTheme;
        if (theme == null) {
            super.setTheme(n);
            return;
        }
        theme.applyStyle(n, true);
    }
}

