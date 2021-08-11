/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.AppComponentFactory
 *  android.app.Application
 *  android.app.Service
 *  android.content.BroadcastReceiver
 *  android.content.ContentProvider
 *  android.content.Intent
 */
package androidx.core.app;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;

public class CoreComponentFactory
extends AppComponentFactory {
    static <T> T checkCompatWrapper(T t) {
        Object object;
        if (t instanceof CompatWrapped && (object = ((CompatWrapped)t).getWrapper()) != null) {
            return (T)object;
        }
        return t;
    }

    public Activity instantiateActivity(ClassLoader classLoader, String string, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateActivity(classLoader, string, intent));
    }

    public Application instantiateApplication(ClassLoader classLoader, String string) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateApplication(classLoader, string));
    }

    public ContentProvider instantiateProvider(ClassLoader classLoader, String string) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateProvider(classLoader, string));
    }

    public BroadcastReceiver instantiateReceiver(ClassLoader classLoader, String string, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateReceiver(classLoader, string, intent));
    }

    public Service instantiateService(ClassLoader classLoader, String string, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(super.instantiateService(classLoader, string, intent));
    }

    public static interface CompatWrapped {
        public Object getWrapper();
    }

}

