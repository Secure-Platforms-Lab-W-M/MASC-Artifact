// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.view.LayoutInflater$Factory;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater$Factory2;
import android.view.LayoutInflater;
import android.os.Build$VERSION;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat
{
    static final LayoutInflaterCompatBaseImpl IMPL;
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (LayoutInflaterCompatBaseImpl)new LayoutInflaterCompatApi21Impl();
            return;
        }
        IMPL = new LayoutInflaterCompatBaseImpl();
    }
    
    private LayoutInflaterCompat() {
    }
    
    static void forceSetFactory2(final LayoutInflater layoutInflater, final LayoutInflater$Factory2 layoutInflater$Factory2) {
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
        return LayoutInflaterCompat.IMPL.getFactory(layoutInflater);
    }
    
    @Deprecated
    public static void setFactory(@NonNull final LayoutInflater layoutInflater, @NonNull final LayoutInflaterFactory layoutInflaterFactory) {
        LayoutInflaterCompat.IMPL.setFactory(layoutInflater, layoutInflaterFactory);
    }
    
    public static void setFactory2(@NonNull final LayoutInflater layoutInflater, @NonNull final LayoutInflater$Factory2 layoutInflater$Factory2) {
        LayoutInflaterCompat.IMPL.setFactory2(layoutInflater, layoutInflater$Factory2);
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
    
    @RequiresApi(21)
    static class LayoutInflaterCompatApi21Impl extends LayoutInflaterCompatBaseImpl
    {
        @Override
        public void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
            Object factory2;
            if (layoutInflaterFactory != null) {
                factory2 = new Factory2Wrapper(layoutInflaterFactory);
            }
            else {
                factory2 = null;
            }
            layoutInflater.setFactory2((LayoutInflater$Factory2)factory2);
        }
        
        @Override
        public void setFactory2(final LayoutInflater layoutInflater, final LayoutInflater$Factory2 factory2) {
            layoutInflater.setFactory2(factory2);
        }
    }
    
    static class LayoutInflaterCompatBaseImpl
    {
        public LayoutInflaterFactory getFactory(final LayoutInflater layoutInflater) {
            final LayoutInflater$Factory factory = layoutInflater.getFactory();
            if (factory instanceof Factory2Wrapper) {
                return ((Factory2Wrapper)factory).mDelegateFactory;
            }
            return null;
        }
        
        public void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
            Object o;
            if (layoutInflaterFactory != null) {
                o = new Factory2Wrapper(layoutInflaterFactory);
            }
            else {
                o = null;
            }
            this.setFactory2(layoutInflater, (LayoutInflater$Factory2)o);
        }
        
        public void setFactory2(final LayoutInflater layoutInflater, final LayoutInflater$Factory2 factory2) {
            layoutInflater.setFactory2(factory2);
            final LayoutInflater$Factory factory3 = layoutInflater.getFactory();
            if (factory3 instanceof LayoutInflater$Factory2) {
                LayoutInflaterCompat.forceSetFactory2(layoutInflater, (LayoutInflater$Factory2)factory3);
                return;
            }
            LayoutInflaterCompat.forceSetFactory2(layoutInflater, factory2);
        }
    }
}
