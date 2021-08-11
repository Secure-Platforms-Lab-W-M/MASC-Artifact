// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.lang.reflect.Constructor;
import java.util.Map;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class Lifecycling
{
    private static Map<Class, Constructor<? extends GenericLifecycleObserver>> sCallbackCache;
    private static Constructor<? extends GenericLifecycleObserver> sREFLECTIVE;
    
    static {
        try {
            Lifecycling.sREFLECTIVE = ReflectiveGenericLifecycleObserver.class.getDeclaredConstructor(Object.class);
        }
        catch (NoSuchMethodException ex) {}
        Lifecycling.sCallbackCache = new HashMap<Class, Constructor<? extends GenericLifecycleObserver>>();
    }
    
    static String getAdapterName(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s.replace(".", "_"));
        sb.append("_LifecycleAdapter");
        return sb.toString();
    }
    
    @NonNull
    static GenericLifecycleObserver getCallback(final Object o) {
        if (o instanceof GenericLifecycleObserver) {
            return (GenericLifecycleObserver)o;
        }
        while (true) {
            while (true) {
                Label_0137: {
                    try {
                        final Class<?> class1 = o.getClass();
                        final Constructor<? extends GenericLifecycleObserver> constructor = Lifecycling.sCallbackCache.get(class1);
                        if (constructor != null) {
                            return (GenericLifecycleObserver)constructor.newInstance(o);
                        }
                        Constructor<? extends GenericLifecycleObserver> constructor2 = getGeneratedAdapterConstructor(class1);
                        if (constructor2 != null) {
                            if (constructor2.isAccessible()) {
                                break Label_0137;
                            }
                            constructor2.setAccessible(true);
                        }
                        else {
                            constructor2 = Lifecycling.sREFLECTIVE;
                        }
                        Lifecycling.sCallbackCache.put(class1, constructor2);
                        return (GenericLifecycleObserver)constructor2.newInstance(o);
                    }
                    catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                    catch (InstantiationException ex2) {
                        throw new RuntimeException(ex2);
                    }
                    catch (IllegalAccessException ex3) {
                        throw new RuntimeException(ex3);
                    }
                }
                continue;
            }
        }
    }
    
    @Nullable
    private static Constructor<? extends GenericLifecycleObserver> getGeneratedAdapterConstructor(final Class<?> clazz) {
        final Package package1 = clazz.getPackage();
        String name;
        if (package1 != null) {
            name = package1.getName();
        }
        else {
            name = "";
        }
        String s = clazz.getCanonicalName();
        if (s == null) {
            return null;
        }
        if (!name.isEmpty()) {
            s = s.substring(name.length() + 1);
        }
        final String adapterName = getAdapterName(s);
        try {
            String string;
            if (name.isEmpty()) {
                string = adapterName;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(name);
                sb.append(".");
                sb.append(adapterName);
                string = sb.toString();
            }
            return (Constructor<? extends GenericLifecycleObserver>)Class.forName(string).getDeclaredConstructor(clazz);
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
        catch (ClassNotFoundException ex2) {
            final Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getGeneratedAdapterConstructor(superclass);
            }
            return null;
        }
    }
}
