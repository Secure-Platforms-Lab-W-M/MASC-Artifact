/*
 * Decompiled with CFR 0_124.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.ReflectiveGenericLifecycleObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
class Lifecycling {
    private static Map<Class, Constructor<? extends GenericLifecycleObserver>> sCallbackCache;
    private static Constructor<? extends GenericLifecycleObserver> sREFLECTIVE;

    static {
        try {
            sREFLECTIVE = ReflectiveGenericLifecycleObserver.class.getDeclaredConstructor(Object.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        sCallbackCache = new HashMap<Class, Constructor<? extends GenericLifecycleObserver>>();
    }

    Lifecycling() {
    }

    static String getAdapterName(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2.replace(".", "_"));
        stringBuilder.append("_LifecycleAdapter");
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @NonNull
    static GenericLifecycleObserver getCallback(Object object) {
        if (object instanceof GenericLifecycleObserver) {
            return (GenericLifecycleObserver)object;
        }
        try {
            Class class_ = object.getClass();
            Constructor<? extends GenericLifecycleObserver> constructor = sCallbackCache.get(class_);
            if (constructor != null) {
                return constructor.newInstance(object);
            }
            constructor = Lifecycling.getGeneratedAdapterConstructor(class_);
            if (constructor != null) {
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
            } else {
                constructor = sREFLECTIVE;
            }
            sCallbackCache.put(class_, constructor);
            return constructor.newInstance(object);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException(invocationTargetException);
        }
        catch (InstantiationException instantiationException) {
            throw new RuntimeException(instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Nullable
    private static Constructor<? extends GenericLifecycleObserver> getGeneratedAdapterConstructor(Class<?> class_) {
        Object object;
        String string2;
        block6 : {
            object = class_.getPackage();
            object = object != null ? object.getName() : "";
            string2 = class_.getCanonicalName();
            if (string2 == null) {
                return null;
            }
            if (!object.isEmpty()) {
                string2 = string2.substring(object.length() + 1);
            }
            string2 = Lifecycling.getAdapterName(string2);
            if (!object.isEmpty()) break block6;
            object = string2;
            return Class.forName((String)object).getDeclaredConstructor(class_);
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append(".");
            stringBuilder.append(string2);
            object = stringBuilder.toString();
            return Class.forName((String)object).getDeclaredConstructor(class_);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new RuntimeException(noSuchMethodException);
        }
        catch (ClassNotFoundException classNotFoundException) {
            class_ = class_.getSuperclass();
            if (class_ == null) return null;
            return Lifecycling.getGeneratedAdapterConstructor(class_);
        }
    }
}

