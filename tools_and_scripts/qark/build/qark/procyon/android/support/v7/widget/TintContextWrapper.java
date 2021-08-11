// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.content.res.AssetManager;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.Context;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.support.annotation.RestrictTo;
import android.content.ContextWrapper;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TintContextWrapper extends ContextWrapper
{
    private static final Object CACHE_LOCK;
    private static ArrayList<WeakReference<TintContextWrapper>> sCache;
    private final Resources mResources;
    private final Resources$Theme mTheme;
    
    static {
        CACHE_LOCK = new Object();
    }
    
    private TintContextWrapper(@NonNull final Context context) {
        super(context);
        if (VectorEnabledTintResources.shouldBeUsed()) {
            this.mResources = new VectorEnabledTintResources((Context)this, context.getResources());
            (this.mTheme = this.mResources.newTheme()).setTo(context.getTheme());
            return;
        }
        this.mResources = new TintResources((Context)this, context.getResources());
        this.mTheme = null;
    }
    
    private static boolean shouldWrap(@NonNull final Context context) {
        return !(context instanceof TintContextWrapper) && !(context.getResources() instanceof TintResources) && !(context.getResources() instanceof VectorEnabledTintResources) && (Build$VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed());
    }
    
    public static Context wrap(@NonNull final Context context) {
        if (shouldWrap(context)) {
        Label_0090_Outer:
            while (true) {
            Label_0120_Outer:
                while (true) {
                    int n = 0;
                Label_0183:
                    while (true) {
                    Label_0178:
                        while (true) {
                            Label_0171: {
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
                                            break Label_0171;
                                        }
                                        else {
                                            n = TintContextWrapper.sCache.size() - 1;
                                            if (n >= 0) {
                                                final WeakReference<TintContextWrapper> weakReference2 = TintContextWrapper.sCache.get(n);
                                                if (weakReference2 == null) {
                                                    break Label_0178;
                                                }
                                                final Object o = weakReference2.get();
                                                if (o != null && ((TintContextWrapper)o).getBaseContext() == context) {
                                                    return (Context)o;
                                                }
                                                break Label_0183;
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
                            continue Label_0090_Outer;
                        }
                        final Object o = null;
                        continue;
                    }
                    --n;
                    continue Label_0120_Outer;
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
