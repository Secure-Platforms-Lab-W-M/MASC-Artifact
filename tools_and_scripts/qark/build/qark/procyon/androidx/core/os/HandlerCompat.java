// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.os;

import android.os.Message;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import android.os.Handler$Callback;
import android.os.Build$VERSION;
import android.os.Handler;
import android.os.Looper;

public final class HandlerCompat
{
    private static final String TAG = "HandlerCompat";
    
    private HandlerCompat() {
    }
    
    public static Handler createAsync(final Looper looper) {
        if (Build$VERSION.SDK_INT >= 28) {
            return Handler.createAsync(looper);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            try {
                return Handler.class.getDeclaredConstructor(Looper.class, Handler$Callback.class, Boolean.TYPE).newInstance(looper, null, true);
            }
            catch (InvocationTargetException ex) {
                final Throwable cause = ex.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                }
                if (cause instanceof Error) {
                    throw (Error)cause;
                }
                throw new RuntimeException(cause);
            }
            catch (NoSuchMethodException ex2) {}
            catch (InstantiationException ex3) {}
            catch (IllegalAccessException ex4) {}
            Log.v("HandlerCompat", "Unable to invoke Handler(Looper, Callback, boolean) constructor");
        }
        return new Handler(looper);
    }
    
    public static Handler createAsync(final Looper looper, final Handler$Callback handler$Callback) {
        if (Build$VERSION.SDK_INT >= 28) {
            return Handler.createAsync(looper, handler$Callback);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            try {
                return Handler.class.getDeclaredConstructor(Looper.class, Handler$Callback.class, Boolean.TYPE).newInstance(looper, handler$Callback, true);
            }
            catch (InvocationTargetException ex) {
                final Throwable cause = ex.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                }
                if (cause instanceof Error) {
                    throw (Error)cause;
                }
                throw new RuntimeException(cause);
            }
            catch (NoSuchMethodException ex2) {}
            catch (InstantiationException ex3) {}
            catch (IllegalAccessException ex4) {}
            Log.v("HandlerCompat", "Unable to invoke Handler(Looper, Callback, boolean) constructor");
        }
        return new Handler(looper, handler$Callback);
    }
    
    public static boolean postDelayed(final Handler handler, final Runnable runnable, final Object obj, final long n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return handler.postDelayed(runnable, obj, n);
        }
        final Message obtain = Message.obtain(handler, runnable);
        obtain.obj = obj;
        return handler.sendMessageDelayed(obtain, n);
    }
}
