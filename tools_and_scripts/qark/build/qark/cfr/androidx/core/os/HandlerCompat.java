/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.util.Log
 */
package androidx.core.os;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class HandlerCompat {
    private static final String TAG = "HandlerCompat";

    private HandlerCompat() {
    }

    public static Handler createAsync(Looper looper) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Handler.createAsync((Looper)looper);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            try {
                Handler handler = Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, Boolean.TYPE).newInstance(new Object[]{looper, null, true});
                return handler;
            }
            catch (InvocationTargetException invocationTargetException) {
                Throwable throwable = invocationTargetException.getCause();
                if (!(throwable instanceof RuntimeException)) {
                    if (throwable instanceof Error) {
                        throw (Error)throwable;
                    }
                    throw new RuntimeException(throwable);
                }
                throw (RuntimeException)throwable;
            }
            catch (NoSuchMethodException noSuchMethodException) {
            }
            catch (InstantiationException instantiationException) {
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
            Log.v((String)"HandlerCompat", (String)"Unable to invoke Handler(Looper, Callback, boolean) constructor");
        }
        return new Handler(looper);
    }

    public static Handler createAsync(Looper looper, Handler.Callback callback) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Handler.createAsync((Looper)looper, (Handler.Callback)callback);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            try {
                Handler handler = Handler.class.getDeclaredConstructor(Looper.class, Handler.Callback.class, Boolean.TYPE).newInstance(new Object[]{looper, callback, true});
                return handler;
            }
            catch (InvocationTargetException invocationTargetException) {
                Throwable throwable = invocationTargetException.getCause();
                if (!(throwable instanceof RuntimeException)) {
                    if (throwable instanceof Error) {
                        throw (Error)throwable;
                    }
                    throw new RuntimeException(throwable);
                }
                throw (RuntimeException)throwable;
            }
            catch (NoSuchMethodException noSuchMethodException) {
            }
            catch (InstantiationException instantiationException) {
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
            Log.v((String)"HandlerCompat", (String)"Unable to invoke Handler(Looper, Callback, boolean) constructor");
        }
        return new Handler(looper, callback);
    }

    public static boolean postDelayed(Handler handler, Runnable runnable, Object object, long l) {
        if (Build.VERSION.SDK_INT >= 28) {
            return handler.postDelayed(runnable, object, l);
        }
        runnable = Message.obtain((Handler)handler, (Runnable)runnable);
        runnable.obj = object;
        return handler.sendMessageDelayed((Message)runnable, l);
    }
}

