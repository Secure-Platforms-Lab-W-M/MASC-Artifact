/*
 * Decompiled with CFR 0_124.
 */
package androidx.fragment.app;

import androidx.collection.SimpleArrayMap;
import androidx.fragment.app.Fragment;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class FragmentFactory {
    private static final SimpleArrayMap<String, Class<?>> sClassMap = new SimpleArrayMap();

    static boolean isFragmentClass(ClassLoader classLoader, String string2) {
        try {
            boolean bl = Fragment.class.isAssignableFrom(FragmentFactory.loadClass(classLoader, string2));
            return bl;
        }
        catch (ClassNotFoundException classNotFoundException) {
            return false;
        }
    }

    private static Class<?> loadClass(ClassLoader classLoader, String string2) throws ClassNotFoundException {
        Class class_;
        Class class_2 = class_ = sClassMap.get(string2);
        if (class_ == null) {
            class_2 = Class.forName(string2, false, classLoader);
            sClassMap.put(string2, class_2);
        }
        return class_2;
    }

    public static Class<? extends Fragment> loadFragmentClass(ClassLoader object, String string2) {
        try {
            object = FragmentFactory.loadClass((ClassLoader)object, string2);
            return object;
        }
        catch (ClassCastException classCastException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": make sure class is a valid subclass of Fragment");
            throw new Fragment.InstantiationException(stringBuilder.toString(), classCastException);
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": make sure class name exists");
            throw new Fragment.InstantiationException(stringBuilder.toString(), classNotFoundException);
        }
    }

    public Fragment instantiate(ClassLoader object, String string2) {
        try {
            object = FragmentFactory.loadFragmentClass((ClassLoader)object, string2).getConstructor(new Class[0]).newInstance(new Object[0]);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": calling Fragment constructor caused an exception");
            throw new Fragment.InstantiationException(stringBuilder.toString(), invocationTargetException);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": could not find Fragment constructor");
            throw new Fragment.InstantiationException(stringBuilder.toString(), noSuchMethodException);
        }
        catch (IllegalAccessException illegalAccessException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": make sure class name exists, is public, and has an empty constructor that is public");
            throw new Fragment.InstantiationException(stringBuilder.toString(), illegalAccessException);
        }
        catch (InstantiationException instantiationException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to instantiate fragment ");
            stringBuilder.append(string2);
            stringBuilder.append(": make sure class name exists, is public, and has an empty constructor that is public");
            throw new Fragment.InstantiationException(stringBuilder.toString(), instantiationException);
        }
    }
}

