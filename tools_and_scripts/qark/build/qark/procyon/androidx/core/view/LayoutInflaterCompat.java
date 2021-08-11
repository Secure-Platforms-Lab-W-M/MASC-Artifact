// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.os.Build$VERSION;
import android.view.LayoutInflater$Factory;
import android.util.Log;
import android.view.LayoutInflater$Factory2;
import android.view.LayoutInflater;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat
{
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;
    
    private LayoutInflaterCompat() {
    }
    
    private static void forceSetFactory2(final LayoutInflater layoutInflater, final LayoutInflater$Factory2 layoutInflater$Factory2) {
        if (!LayoutInflaterCompat.sCheckedField) {
            try {
                (LayoutInflaterCompat.sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("forceSetFactory2 Could not find field 'mFactory2' on class ");
                sb.append(LayoutInflater.class.getName());
                sb.append("; inflation may have unexpected results.");
                Log.e("LayoutInflaterCompatHC", sb.toString(), (Throwable)ex);
            }
            LayoutInflaterCompat.sCheckedField = true;
        }
        final Field sLayoutInflaterFactory2Field = LayoutInflaterCompat.sLayoutInflaterFactory2Field;
        if (sLayoutInflaterFactory2Field != null) {
            try {
                sLayoutInflaterFactory2Field.set(layoutInflater, layoutInflater$Factory2);
            }
            catch (IllegalAccessException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("forceSetFactory2 could not set the Factory2 on LayoutInflater ");
                sb2.append(layoutInflater);
                sb2.append("; inflation may have unexpected results.");
                Log.e("LayoutInflaterCompatHC", sb2.toString(), (Throwable)ex2);
            }
        }
    }
    
    @Deprecated
    public static LayoutInflaterFactory getFactory(final LayoutInflater layoutInflater) {
        final LayoutInflater$Factory factory = layoutInflater.getFactory();
        if (factory instanceof Factory2Wrapper) {
            return ((Factory2Wrapper)factory).mDelegateFactory;
        }
        return null;
    }
    
    @Deprecated
    public static void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final LayoutInflater$Factory2 layoutInflater$Factory2 = null;
        Object factory2 = null;
        if (sdk_INT >= 21) {
            if (layoutInflaterFactory != null) {
                factory2 = new Factory2Wrapper(layoutInflaterFactory);
            }
            layoutInflater.setFactory2((LayoutInflater$Factory2)factory2);
            return;
        }
        Object factory3 = layoutInflater$Factory2;
        if (layoutInflaterFactory != null) {
            factory3 = new Factory2Wrapper(layoutInflaterFactory);
        }
        layoutInflater.setFactory2((LayoutInflater$Factory2)factory3);
        final LayoutInflater$Factory factory4 = layoutInflater.getFactory();
        if (factory4 instanceof LayoutInflater$Factory2) {
            forceSetFactory2(layoutInflater, (LayoutInflater$Factory2)factory4);
            return;
        }
        forceSetFactory2(layoutInflater, (LayoutInflater$Factory2)factory3);
    }
    
    public static void setFactory2(final LayoutInflater layoutInflater, final LayoutInflater$Factory2 factory2) {
        layoutInflater.setFactory2(factory2);
        if (Build$VERSION.SDK_INT < 21) {
            final LayoutInflater$Factory factory3 = layoutInflater.getFactory();
            if (factory3 instanceof LayoutInflater$Factory2) {
                forceSetFactory2(layoutInflater, (LayoutInflater$Factory2)factory3);
                return;
            }
            forceSetFactory2(layoutInflater, factory2);
        }
    }
    
    static class Factory2Wrapper implements LayoutInflater$Factory2
    {
        final LayoutInflaterFactory mDelegateFactory;
        
        Factory2Wrapper(final LayoutInflaterFactory mDelegateFactory) {
            this.mDelegateFactory = mDelegateFactory;
        }
        
        public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
            return this.mDelegateFactory.onCreateView(view, s, context, set);
        }
        
        public View onCreateView(final String s, final Context context, final AttributeSet set) {
            return this.mDelegateFactory.onCreateView(null, s, context, set);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getClass().getName());
            sb.append("{");
            sb.append(this.mDelegateFactory);
            sb.append("}");
            return sb.toString();
        }
    }
}
