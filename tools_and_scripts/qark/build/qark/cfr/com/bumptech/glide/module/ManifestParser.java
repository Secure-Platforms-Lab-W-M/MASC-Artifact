/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Bundle
 *  android.util.Log
 */
package com.bumptech.glide.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.bumptech.glide.module.GlideModule;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Deprecated
public final class ManifestParser {
    private static final String GLIDE_MODULE_VALUE = "GlideModule";
    private static final String TAG = "ManifestParser";
    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    private static GlideModule parseModule(String string2) {
        Class class_;
        StringBuilder stringBuilder;
        try {
            class_ = Class.forName(string2);
            stringBuilder = null;
            string2 = null;
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new IllegalArgumentException("Unable to find GlideModule implementation", classNotFoundException);
        }
        try {
            Object obj = class_.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            string2 = obj;
        }
        catch (InvocationTargetException invocationTargetException) {
            ManifestParser.throwInstantiateGlideModuleException(class_, invocationTargetException);
            string2 = stringBuilder;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            ManifestParser.throwInstantiateGlideModuleException(class_, noSuchMethodException);
        }
        catch (IllegalAccessException illegalAccessException) {
            ManifestParser.throwInstantiateGlideModuleException(class_, illegalAccessException);
        }
        catch (InstantiationException instantiationException) {
            ManifestParser.throwInstantiateGlideModuleException(class_, instantiationException);
        }
        if (string2 instanceof GlideModule) {
            return (GlideModule)((Object)string2);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Expected instanceof GlideModule, but found: ");
        stringBuilder.append((Object)string2);
        throw new RuntimeException(stringBuilder.toString());
    }

    private static void throwInstantiateGlideModuleException(Class<?> class_, Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to instantiate GlideModule implementation for ");
        stringBuilder.append(class_);
        throw new RuntimeException(stringBuilder.toString(), exception);
    }

    public List<GlideModule> parse() {
        ArrayList<GlideModule> arrayList;
        block8 : {
            if (Log.isLoggable((String)"ManifestParser", (int)3)) {
                Log.d((String)"ManifestParser", (String)"Loading Glide modules");
            }
            arrayList = new ArrayList<GlideModule>();
            ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
            if (applicationInfo.metaData != null) break block8;
            if (Log.isLoggable((String)"ManifestParser", (int)3)) {
                Log.d((String)"ManifestParser", (String)"Got null app info metadata");
                return arrayList;
            }
        }
        try {
            if (Log.isLoggable((String)"ManifestParser", (int)2)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Got app info metadata: ");
                stringBuilder.append((Object)applicationInfo.metaData);
                Log.v((String)"ManifestParser", (String)stringBuilder.toString());
            }
            for (String string2 : applicationInfo.metaData.keySet()) {
                if (!"GlideModule".equals(applicationInfo.metaData.get(string2))) continue;
                arrayList.add(ManifestParser.parseModule(string2));
                if (!Log.isLoggable((String)"ManifestParser", (int)3)) continue;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Loaded Glide module: ");
                stringBuilder.append(string2);
                Log.d((String)"ManifestParser", (String)stringBuilder.toString());
            }
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            throw new RuntimeException("Unable to find metadata to parse GlideModules", (Throwable)nameNotFoundException);
        }
        if (Log.isLoggable((String)"ManifestParser", (int)3)) {
            Log.d((String)"ManifestParser", (String)"Finished loading Glide modules");
        }
        return arrayList;
        return arrayList;
    }
}

