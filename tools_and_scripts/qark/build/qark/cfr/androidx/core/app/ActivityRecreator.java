/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.app.Application$ActivityLifecycleCallbacks
 *  android.content.res.Configuration
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Looper
 *  android.util.Log
 */
package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

final class ActivityRecreator {
    private static final String LOG_TAG = "ActivityRecreator";
    protected static final Class<?> activityThreadClass;
    private static final Handler mainHandler;
    protected static final Field mainThreadField;
    protected static final Method performStopActivity2ParamsMethod;
    protected static final Method performStopActivity3ParamsMethod;
    protected static final Method requestRelaunchActivityMethod;
    protected static final Field tokenField;

    static {
        mainHandler = new Handler(Looper.getMainLooper());
        activityThreadClass = ActivityRecreator.getActivityThreadClass();
        mainThreadField = ActivityRecreator.getMainThreadField();
        tokenField = ActivityRecreator.getTokenField();
        performStopActivity3ParamsMethod = ActivityRecreator.getPerformStopActivity3Params(activityThreadClass);
        performStopActivity2ParamsMethod = ActivityRecreator.getPerformStopActivity2Params(activityThreadClass);
        requestRelaunchActivityMethod = ActivityRecreator.getRequestRelaunchActivityMethod(activityThreadClass);
    }

    private ActivityRecreator() {
    }

    private static Class<?> getActivityThreadClass() {
        try {
            Class class_ = Class.forName("android.app.ActivityThread");
            return class_;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Field getMainThreadField() {
        try {
            Field field = Activity.class.getDeclaredField("mMainThread");
            field.setAccessible(true);
            return field;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Method getPerformStopActivity2Params(Class<?> class_) {
        if (class_ == null) {
            return null;
        }
        try {
            class_ = class_.getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE);
            class_.setAccessible(true);
            return class_;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Method getPerformStopActivity3Params(Class<?> class_) {
        if (class_ == null) {
            return null;
        }
        try {
            class_ = class_.getDeclaredMethod("performStopActivity", IBinder.class, Boolean.TYPE, String.class);
            class_.setAccessible(true);
            return class_;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static Method getRequestRelaunchActivityMethod(Class<?> class_) {
        if (ActivityRecreator.needsRelaunchCall()) {
            if (class_ == null) {
                return null;
            }
            try {
                class_ = class_.getDeclaredMethod("requestRelaunchActivity", IBinder.class, List.class, List.class, Integer.TYPE, Boolean.TYPE, Configuration.class, Configuration.class, Boolean.TYPE, Boolean.TYPE);
                class_.setAccessible(true);
                return class_;
            }
            catch (Throwable throwable) {
                return null;
            }
        }
        return null;
    }

    private static Field getTokenField() {
        try {
            Field field = Activity.class.getDeclaredField("mToken");
            field.setAccessible(true);
            return field;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static boolean needsRelaunchCall() {
        if (Build.VERSION.SDK_INT != 26 && Build.VERSION.SDK_INT != 27) {
            return false;
        }
        return true;
    }

    protected static boolean queueOnStopIfNecessary(final Object object, Activity activity) {
        Object object2;
        block3 : {
            try {
                object2 = tokenField.get((Object)activity);
                if (object2 == object) break block3;
                return false;
            }
            catch (Throwable throwable) {
                Log.e((String)"ActivityRecreator", (String)"Exception while fetching field values", (Throwable)throwable);
                return false;
            }
        }
        object = mainThreadField.get((Object)activity);
        mainHandler.postAtFrontOfQueue(new Runnable(){

            @Override
            public void run() {
                block5 : {
                    try {
                        if (ActivityRecreator.performStopActivity3ParamsMethod != null) {
                            ActivityRecreator.performStopActivity3ParamsMethod.invoke(object, object2, false, "AppCompat recreation");
                        } else {
                            ActivityRecreator.performStopActivity2ParamsMethod.invoke(object, object2, false);
                        }
                    }
                    catch (Throwable throwable) {
                        Log.e((String)"ActivityRecreator", (String)"Exception while invoking performStopActivity", (Throwable)throwable);
                        return;
                    }
                    catch (RuntimeException runtimeException) {
                        if (runtimeException.getClass() != RuntimeException.class || runtimeException.getMessage() == null || !runtimeException.getMessage().startsWith("Unable to stop")) break block5;
                        throw runtimeException;
                    }
                }
            }
        });
        return true;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    static boolean recreate(Activity activity) {
        LifecycleCheckCallbacks lifecycleCheckCallbacks;
        Application application;
        block11 : {
            Object object2;
            Object object;
            if (Build.VERSION.SDK_INT >= 28) {
                activity.recreate();
                return true;
            }
            if (ActivityRecreator.needsRelaunchCall() && requestRelaunchActivityMethod == null) {
                return false;
            }
            if (performStopActivity2ParamsMethod == null && performStopActivity3ParamsMethod == null) {
                return false;
            }
            try {
                object2 = tokenField.get((Object)activity);
                if (object2 == null) {
                    return false;
                }
                object = mainThreadField.get((Object)activity);
                if (object == null) {
                    return false;
                }
                application = activity.getApplication();
                lifecycleCheckCallbacks = new LifecycleCheckCallbacks(activity);
                application.registerActivityLifecycleCallbacks((Application.ActivityLifecycleCallbacks)lifecycleCheckCallbacks);
                mainHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        lifecycleCheckCallbacks.currentlyRecreatingToken = object2;
                    }
                });
            }
            catch (Throwable throwable) {
                return false;
            }
            if (ActivityRecreator.needsRelaunchCall()) {
                requestRelaunchActivityMethod.invoke(object, object2, null, null, 0, false, null, null, false, false);
                break block11;
            }
            activity.recreate();
            {
                catch (Throwable throwable) {
                    mainHandler.post(new Runnable(application, lifecycleCheckCallbacks){
                        final /* synthetic */ Application val$application;
                        final /* synthetic */ LifecycleCheckCallbacks val$callbacks;
                        {
                            this.val$application = application;
                            this.val$callbacks = lifecycleCheckCallbacks;
                        }

                        @Override
                        public void run() {
                            this.val$application.unregisterActivityLifecycleCallbacks((Application.ActivityLifecycleCallbacks)this.val$callbacks);
                        }
                    });
                    throw throwable;
                }
            }
        }
        mainHandler.post(new );
        return true;
    }

    private static final class LifecycleCheckCallbacks
    implements Application.ActivityLifecycleCallbacks {
        Object currentlyRecreatingToken;
        private Activity mActivity;
        private boolean mDestroyed = false;
        private boolean mStarted = false;
        private boolean mStopQueued = false;

        LifecycleCheckCallbacks(Activity activity) {
            this.mActivity = activity;
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
            if (this.mActivity == activity) {
                this.mActivity = null;
                this.mDestroyed = true;
            }
        }

        public void onActivityPaused(Activity activity) {
            if (this.mDestroyed && !this.mStopQueued && !this.mStarted && ActivityRecreator.queueOnStopIfNecessary(this.currentlyRecreatingToken, activity)) {
                this.mStopQueued = true;
                this.currentlyRecreatingToken = null;
            }
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
            if (this.mActivity == activity) {
                this.mStarted = true;
            }
        }

        public void onActivityStopped(Activity activity) {
        }
    }

}

