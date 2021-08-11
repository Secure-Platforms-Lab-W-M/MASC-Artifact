/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory
 *  android.view.LayoutInflater$Factory2
 *  android.view.View
 */
package androidx.core.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.view.LayoutInflaterFactory;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat {
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;

    private LayoutInflaterCompat() {
    }

    private static void forceSetFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 factory2) {
        Object object;
        if (!sCheckedField) {
            try {
                object = LayoutInflater.class.getDeclaredField("mFactory2");
                sLayoutInflaterFactory2Field = object;
                object.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("forceSetFactory2 Could not find field 'mFactory2' on class ");
                stringBuilder.append(LayoutInflater.class.getName());
                stringBuilder.append("; inflation may have unexpected results.");
                Log.e((String)"LayoutInflaterCompatHC", (String)stringBuilder.toString(), (Throwable)noSuchFieldException);
            }
            sCheckedField = true;
        }
        if ((object = sLayoutInflaterFactory2Field) != null) {
            try {
                object.set((Object)layoutInflater, (Object)factory2);
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                object = new StringBuilder();
                object.append("forceSetFactory2 could not set the Factory2 on LayoutInflater ");
                object.append((Object)layoutInflater);
                object.append("; inflation may have unexpected results.");
                Log.e((String)"LayoutInflaterCompatHC", (String)object.toString(), (Throwable)illegalAccessException);
            }
        }
    }

    @Deprecated
    public static LayoutInflaterFactory getFactory(LayoutInflater layoutInflater) {
        if ((layoutInflater = layoutInflater.getFactory()) instanceof Factory2Wrapper) {
            return ((Factory2Wrapper)layoutInflater).mDelegateFactory;
        }
        return null;
    }

    @Deprecated
    public static void setFactory(LayoutInflater layoutInflater, LayoutInflaterFactory layoutInflaterFactory) {
        int n = Build.VERSION.SDK_INT;
        Factory2Wrapper factory2Wrapper = null;
        Factory2Wrapper factory2Wrapper2 = null;
        if (n >= 21) {
            if (layoutInflaterFactory != null) {
                factory2Wrapper2 = new Factory2Wrapper(layoutInflaterFactory);
            }
            layoutInflater.setFactory2((LayoutInflater.Factory2)factory2Wrapper2);
            return;
        }
        factory2Wrapper2 = factory2Wrapper;
        if (layoutInflaterFactory != null) {
            factory2Wrapper2 = new Factory2Wrapper(layoutInflaterFactory);
        }
        layoutInflater.setFactory2((LayoutInflater.Factory2)factory2Wrapper2);
        layoutInflaterFactory = layoutInflater.getFactory();
        if (layoutInflaterFactory instanceof LayoutInflater.Factory2) {
            LayoutInflaterCompat.forceSetFactory2(layoutInflater, (LayoutInflater.Factory2)layoutInflaterFactory);
            return;
        }
        LayoutInflaterCompat.forceSetFactory2(layoutInflater, factory2Wrapper2);
    }

    public static void setFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 factory2) {
        layoutInflater.setFactory2(factory2);
        if (Build.VERSION.SDK_INT < 21) {
            LayoutInflater.Factory factory = layoutInflater.getFactory();
            if (factory instanceof LayoutInflater.Factory2) {
                LayoutInflaterCompat.forceSetFactory2(layoutInflater, (LayoutInflater.Factory2)factory);
                return;
            }
            LayoutInflaterCompat.forceSetFactory2(layoutInflater, factory2);
        }
    }

    static class Factory2Wrapper
    implements LayoutInflater.Factory2 {
        final LayoutInflaterFactory mDelegateFactory;

        Factory2Wrapper(LayoutInflaterFactory layoutInflaterFactory) {
            this.mDelegateFactory = layoutInflaterFactory;
        }

        public View onCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
            return this.mDelegateFactory.onCreateView(view, string2, context, attributeSet);
        }

        public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
            return this.mDelegateFactory.onCreateView(null, string2, context, attributeSet);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.getClass().getName());
            stringBuilder.append("{");
            stringBuilder.append(this.mDelegateFactory);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

}

