// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.content.res.AssetManager;
import android.os.Build$VERSION;
import android.content.Context;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.content.ContextWrapper;

public class TintContextWrapper extends ContextWrapper
{
    private static final Object CACHE_LOCK;
    private static ArrayList<WeakReference<TintContextWrapper>> sCache;
    private final Resources mResources;
    private final Resources$Theme mTheme;
    
    static {
        CACHE_LOCK = new Object();
    }
    
    private TintContextWrapper(final Context context) {
        super(context);
        if (VectorEnabledTintResources.shouldBeUsed()) {
            final VectorEnabledTintResources mResources = new VectorEnabledTintResources((Context)this, context.getResources());
            this.mResources = mResources;
            (this.mTheme = mResources.newTheme()).setTo(context.getTheme());
            return;
        }
        this.mResources = new TintResources((Context)this, context.getResources());
        this.mTheme = null;
    }
    
    private static boolean shouldWrap(final Context context) {
        final boolean b = context instanceof TintContextWrapper;
        boolean b2 = false;
        if (b || context.getResources() instanceof TintResources) {
            return false;
        }
        if (context.getResources() instanceof VectorEnabledTintResources) {
            return false;
        }
        if (Build$VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed()) {
            b2 = true;
        }
        return b2;
    }
    
    public static Context wrap(final Context context) {
        if (shouldWrap(context)) {
        Label_0087_Outer:
            while (true) {
            Label_0117_Outer:
                while (true) {
                    int n = 0;
                Label_0180:
                    while (true) {
                    Label_0175:
                        while (true) {
                            Label_0168: {
                                synchronized (TintContextWrapper.CACHE_LOCK) {
                                    if (TintContextWrapper.sCache == null) {
                                        TintContextWrapper.sCache = new ArrayList<WeakReference<TintContextWrapper>>();
                                    }
                                    else {
                                        n = TintContextWrapper.sCache.size() - 1;
                                        if (n >= 0) {
                                            final WeakReference<TintContextWrapper> weakReference = TintContextWrapper.sCache.get(n);
                                            if (weakReference == null || weakReference.get() == null) {
                                                TintContextWrapper.sCache.remove(n);
                                            }
                                            break Label_0168;
                                        }
                                        else {
                                            n = TintContextWrapper.sCache.size() - 1;
                                            if (n >= 0) {
                                                final WeakReference<TintContextWrapper> weakReference2 = TintContextWrapper.sCache.get(n);
                                                if (weakReference2 == null) {
                                                    break Label_0175;
                                                }
                                                final Object o = weakReference2.get();
                                                if (o != null && ((TintContextWrapper)o).getBaseContext() == context) {
                                                    return (Context)o;
                                                }
                                                break Label_0180;
                                            }
                                        }
                                    }
                                    final TintContextWrapper tintContextWrapper = new TintContextWrapper(context);
                                    TintContextWrapper.sCache.add(new WeakReference<TintContextWrapper>(tintContextWrapper));
                                    return (Context)tintContextWrapper;
                                }
                                break;
                            }
                            --n;
                            continue Label_0087_Outer;
                        }
                        final Object o = null;
                        continue;
                    }
                    --n;
                    continue Label_0117_Outer;
                }
            }
        }
        return context;
    }
    
    public AssetManager getAssets() {
        return this.mResources.getAssets();
    }
    
    public Resources getResources() {
        return this.mResources;
    }
    
    public Resources$Theme getTheme() {
        Resources$Theme resources$Theme;
        if ((resources$Theme = this.mTheme) == null) {
            resources$Theme = super.getTheme();
        }
        return resources$Theme;
    }
    
    public void setTheme(final int theme) {
        final Resources$Theme mTheme = this.mTheme;
        if (mTheme == null) {
            super.setTheme(theme);
            return;
        }
        mTheme.applyStyle(theme, true);
    }
}
