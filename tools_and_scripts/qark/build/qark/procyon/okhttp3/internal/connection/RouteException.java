// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.connection;

import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import java.lang.reflect.Method;

public final class RouteException extends RuntimeException
{
    private static final Method addSuppressedExceptionMethod;
    private IOException lastException;
    
    static {
        while (true) {
            try {
                final Method declaredMethod = Throwable.class.getDeclaredMethod("addSuppressed", Throwable.class);
                addSuppressedExceptionMethod = declaredMethod;
            }
            catch (Exception ex) {
                final Method declaredMethod = null;
                continue;
            }
            break;
        }
    }
    
    public RouteException(final IOException lastException) {
        super(lastException);
        this.lastException = lastException;
    }
    
    private void addSuppressedIfPossible(final IOException ex, final IOException ex2) {
        if (RouteException.addSuppressedExceptionMethod == null) {
            return;
        }
        try {
            RouteException.addSuppressedExceptionMethod.invoke(ex, ex2);
        }
        catch (IllegalAccessException ex3) {}
        catch (InvocationTargetException ex4) {}
    }
    
    public void addConnectException(final IOException lastException) {
        this.addSuppressedIfPossible(lastException, this.lastException);
        this.lastException = lastException;
    }
    
    public IOException getLastConnectException() {
        return this.lastException;
    }
}
