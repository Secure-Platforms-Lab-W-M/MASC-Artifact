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
package android.support.v4.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat {
    static final LayoutInflaterCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new LayoutInflaterCompatApi21Impl() : new LayoutInflaterCompatBaseImpl();
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;

    private LayoutInflaterCompat() {
    }

    static void forceSetFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 factory2) {
        Object object;
        if (!sCheckedField) {
            try {
                sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2");
                sLayoutInflaterFactory2Field.setAccessible(true);
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
                return;
            }
        }
    }

    @Deprecated
    public static LayoutInflaterFactory getFactory(LayoutInflater layoutInflater) {
        return IMPL.getFactory(layoutInflater);
    }

    @Deprecated
    public static void setFactory(@NonNull LayoutInflater layoutInflater, @NonNull LayoutInflaterFactory layoutInflaterFactory) {
        IMPL.setFactory(layoutInflater, layoutInflaterFactory);
    }

    public static void setFactory2(@NonNull LayoutInflater layoutInflater, @NonNull LayoutInflater.Factory2 factory2) {
        IMPL.setFactory2(layoutInflater, factory2);
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

    @RequiresApi(value=21)
    static class LayoutInflaterCompatApi21Impl
    extends LayoutInflaterCompatBaseImpl {
        LayoutInflaterCompatApi21Impl() {
        }

        @Override
        public void setFactory(LayoutInflater layoutInflater, LayoutInflaterFactory object) {
            object = object != null ? new Factory2Wrapper((LayoutInflaterFactory)object) : null;
            layoutInflater.setFactory2((LayoutInflater.Factory2)object);
        }

        @Override
        public void setFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 factory2) {
            layoutInflater.setFactory2(factory2);
        }
    }

    static class LayoutInflaterCompatBaseImpl {
        LayoutInflaterCompatBaseImpl() {
        }

        public LayoutInflaterFactory getFactory(LayoutInflater layoutInflater) {
            if ((layoutInflater = layoutInflater.getFactory()) instanceof Factory2Wrapper) {
                return ((Factory2Wrapper)layoutInflater).mDelegateFactory;
            }
            return null;
        }

        public void setFactory(LayoutInflater layoutInflater, LayoutInflaterFactory object) {
            object = object != null ? new Factory2Wrapper((LayoutInflaterFactory)object) : null;
            this.setFactory2(layoutInflater, (LayoutInflater.Factory2)object);
        }

        public void setFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 factory2) {
            layoutInflater.setFactory2(factory2);
            LayoutInflater.Factory factory = layoutInflater.getFactory();
            if (factory instanceof LayoutInflater.Factory2) {
                LayoutInflaterCompat.forceSetFactory2(layoutInflater, (LayoutInflater.Factory2)factory);
                return;
            }
            LayoutInflaterCompat.forceSetFactory2(layoutInflater, factory2);
        }
    }

}

