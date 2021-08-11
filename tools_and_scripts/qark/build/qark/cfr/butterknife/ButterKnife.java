/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.util.Log
 *  android.view.View
 *  android.view.Window
 */
package butterknife;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import butterknife.Unbinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ButterKnife {
    static final Map<Class<?>, Constructor<? extends Unbinder>> BINDINGS;
    private static final String TAG = "ButterKnife";
    private static boolean debug;

    static {
        debug = false;
        BINDINGS = new LinkedHashMap();
    }

    private ButterKnife() {
        throw new AssertionError((Object)"No instances.");
    }

    public static Unbinder bind(Activity activity) {
        return ButterKnife.bind((Object)activity, activity.getWindow().getDecorView());
    }

    public static Unbinder bind(Dialog dialog) {
        return ButterKnife.bind((Object)dialog, dialog.getWindow().getDecorView());
    }

    public static Unbinder bind(View view) {
        return ButterKnife.bind((Object)view, view);
    }

    public static Unbinder bind(Object object, Activity activity) {
        return ButterKnife.bind(object, activity.getWindow().getDecorView());
    }

    public static Unbinder bind(Object object, Dialog dialog) {
        return ButterKnife.bind(object, dialog.getWindow().getDecorView());
    }

    public static Unbinder bind(Object object, View object2) {
        Class class_ = object.getClass();
        if (debug) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Looking up binding for ");
            stringBuilder.append(class_.getName());
            Log.d((String)"ButterKnife", (String)stringBuilder.toString());
        }
        if ((class_ = ButterKnife.findBindingConstructorForClass(class_)) == null) {
            return Unbinder.EMPTY;
        }
        try {
            object = (Unbinder)class_.newInstance(object, object2);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
            Throwable throwable = invocationTargetException.getCause();
            if (!(throwable instanceof RuntimeException)) {
                if (throwable instanceof Error) {
                    throw (Error)throwable;
                }
                throw new RuntimeException("Unable to create binding instance.", throwable);
            }
            throw (RuntimeException)throwable;
        }
        catch (InstantiationException instantiationException) {
            object2 = new StringBuilder();
            object2.append("Unable to invoke ");
            object2.append(class_);
            throw new RuntimeException(object2.toString(), instantiationException);
        }
        catch (IllegalAccessException illegalAccessException) {
            object2 = new StringBuilder();
            object2.append("Unable to invoke ");
            object2.append(class_);
            throw new RuntimeException(object2.toString(), illegalAccessException);
        }
    }

    private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> class_) {
        Constructor<? extends Unbinder> constructor = BINDINGS.get(class_);
        if (constructor == null && !BINDINGS.containsKey(class_)) {
            String string2 = class_.getName();
            if (!(string2.startsWith("android.") || string2.startsWith("java.") || string2.startsWith("androidx."))) {
                constructor = class_.getClassLoader();
                Object object = new StringBuilder();
                object.append(string2);
                object.append("_ViewBinding");
                constructor = object = constructor.loadClass(object.toString()).getConstructor(class_, View.class);
                try {
                    if (debug) {
                        Log.d((String)"ButterKnife", (String)"HIT: Loaded binding class and constructor.");
                        constructor = object;
                    }
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    constructor = new StringBuilder();
                    constructor.append("Unable to find binding constructor for ");
                    constructor.append(string2);
                    throw new RuntimeException(constructor.toString(), noSuchMethodException);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    if (debug) {
                        constructor = new StringBuilder();
                        constructor.append("Not found. Trying superclass ");
                        constructor.append(class_.getSuperclass().getName());
                        Log.d((String)"ButterKnife", (String)constructor.toString());
                    }
                    constructor = ButterKnife.findBindingConstructorForClass(class_.getSuperclass());
                }
                BINDINGS.put(class_, constructor);
                return constructor;
            }
            if (debug) {
                Log.d((String)"ButterKnife", (String)"MISS: Reached framework class. Abandoning search.");
            }
            return null;
        }
        if (debug) {
            Log.d((String)"ButterKnife", (String)"HIT: Cached in binding map.");
        }
        return constructor;
    }

    public static void setDebug(boolean bl) {
        debug = bl;
    }
}

