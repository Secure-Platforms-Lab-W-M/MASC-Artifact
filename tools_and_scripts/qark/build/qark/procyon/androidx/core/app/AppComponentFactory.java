// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.app.Application;
import java.lang.reflect.InvocationTargetException;
import android.app.Activity;
import android.content.Intent;

public class AppComponentFactory extends android.app.AppComponentFactory
{
    public final Activity instantiateActivity(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateActivityCompat(classLoader, s, intent));
    }
    
    public Activity instantiateActivityCompat(ClassLoader ex, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            ex = (NoSuchMethodException)Class.forName(s, false, (ClassLoader)ex).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (Activity)ex;
        }
        catch (NoSuchMethodException ex) {}
        catch (InvocationTargetException ex2) {}
        throw new RuntimeException("Couldn't call constructor", ex);
    }
    
    public final Application instantiateApplication(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateApplicationCompat(classLoader, s));
    }
    
    public Application instantiateApplicationCompat(ClassLoader ex, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            ex = (NoSuchMethodException)Class.forName(s, false, (ClassLoader)ex).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (Application)ex;
        }
        catch (NoSuchMethodException ex) {}
        catch (InvocationTargetException ex2) {}
        throw new RuntimeException("Couldn't call constructor", ex);
    }
    
    public final ContentProvider instantiateProvider(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateProviderCompat(classLoader, s));
    }
    
    public ContentProvider instantiateProviderCompat(ClassLoader ex, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            ex = (NoSuchMethodException)Class.forName(s, false, (ClassLoader)ex).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (ContentProvider)ex;
        }
        catch (NoSuchMethodException ex) {}
        catch (InvocationTargetException ex2) {}
        throw new RuntimeException("Couldn't call constructor", ex);
    }
    
    public final BroadcastReceiver instantiateReceiver(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateReceiverCompat(classLoader, s, intent));
    }
    
    public BroadcastReceiver instantiateReceiverCompat(ClassLoader ex, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            ex = (NoSuchMethodException)Class.forName(s, false, (ClassLoader)ex).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (BroadcastReceiver)ex;
        }
        catch (NoSuchMethodException ex) {}
        catch (InvocationTargetException ex2) {}
        throw new RuntimeException("Couldn't call constructor", ex);
    }
    
    public final Service instantiateService(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateServiceCompat(classLoader, s, intent));
    }
    
    public Service instantiateServiceCompat(ClassLoader ex, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            ex = (NoSuchMethodException)Class.forName(s, false, (ClassLoader)ex).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            return (Service)ex;
        }
        catch (NoSuchMethodException ex) {}
        catch (InvocationTargetException ex2) {}
        throw new RuntimeException("Couldn't call constructor", ex);
    }
}
