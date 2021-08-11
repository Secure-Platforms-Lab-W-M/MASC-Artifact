/*
 * Decompiled with CFR 0_124.
 */
package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ClassesInfoCache {
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static ClassesInfoCache sInstance = new ClassesInfoCache();
    private final Map<Class<?>, CallbackInfo> mCallbackMap = new HashMap();
    private final Map<Class<?>, Boolean> mHasLifecycleMethods = new HashMap();

    ClassesInfoCache() {
    }

    private CallbackInfo createInfo(Class<?> class_, Method[] object) {
        int n;
        Object object222 = class_.getSuperclass();
        HashMap<MethodReference, Lifecycle.Event> hashMap = new HashMap<MethodReference, Lifecycle.Event>();
        if (object222 != null && (object222 = this.getInfo(object222)) != null) {
            hashMap.putAll(object222.mHandlerToEvent);
        }
        object222 = class_.getInterfaces();
        int n2 = object222.length;
        for (n = 0; n < n2; ++n) {
            for (Map.Entry<MethodReference, Lifecycle.Event> object3 : this.getInfo(object222[n]).mHandlerToEvent.entrySet()) {
                this.verifyAndPutHandler(hashMap, object3.getKey(), object3.getValue(), class_);
            }
        }
        if (object == null) {
            object = this.getDeclaredMethods(class_);
        }
        boolean bl = false;
        for (Object object222 : object) {
            OnLifecycleEvent onLifecycleEvent = object222.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent == null) continue;
            bl = true;
            Class<?>[] arrclass = object222.getParameterTypes();
            n = 0;
            if (arrclass.length > 0) {
                n = 1;
                if (!arrclass[0].isAssignableFrom(LifecycleOwner.class)) {
                    throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                }
            }
            Lifecycle.Event event = onLifecycleEvent.value();
            if (arrclass.length > 1) {
                n = 2;
                if (arrclass[1].isAssignableFrom(Lifecycle.Event.class)) {
                    if (event != Lifecycle.Event.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                } else {
                    throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                }
            }
            if (arrclass.length <= 2) {
                this.verifyAndPutHandler(hashMap, new MethodReference(n, (Method)object222), event, class_);
                continue;
            }
            throw new IllegalArgumentException("cannot have more than 2 params");
        }
        object = new CallbackInfo(hashMap);
        this.mCallbackMap.put(class_, (CallbackInfo)object);
        this.mHasLifecycleMethods.put(class_, bl);
        return object;
    }

    private Method[] getDeclaredMethods(Class<?> arrmethod) {
        try {
            arrmethod = arrmethod.getDeclaredMethods();
            return arrmethod;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", noClassDefFoundError);
        }
    }

    private void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> object, MethodReference object2, Lifecycle.Event event, Class<?> class_) {
        Lifecycle.Event event2 = object.get(object2);
        if (event2 != null && event != event2) {
            object = object2.mMethod;
            object2 = new StringBuilder();
            object2.append("Method ");
            object2.append(object.getName());
            object2.append(" in ");
            object2.append(class_.getName());
            object2.append(" already declared with different @OnLifecycleEvent value: previous value ");
            object2.append((Object)event2);
            object2.append(", new value ");
            object2.append((Object)event);
            throw new IllegalArgumentException(object2.toString());
        }
        if (event2 == null) {
            object.put((MethodReference)object2, (Lifecycle.Event)event);
        }
    }

    CallbackInfo getInfo(Class<?> class_) {
        CallbackInfo callbackInfo = this.mCallbackMap.get(class_);
        if (callbackInfo != null) {
            return callbackInfo;
        }
        return this.createInfo(class_, null);
    }

    boolean hasLifecycleMethods(Class<?> class_) {
        Method[] arrmethod = this.mHasLifecycleMethods.get(class_);
        if (arrmethod != null) {
            return arrmethod.booleanValue();
        }
        arrmethod = this.getDeclaredMethods(class_);
        int n = arrmethod.length;
        for (int i = 0; i < n; ++i) {
            if (arrmethod[i].getAnnotation(OnLifecycleEvent.class) == null) continue;
            this.createInfo(class_, arrmethod);
            return true;
        }
        this.mHasLifecycleMethods.put(class_, false);
        return false;
    }

    static class CallbackInfo {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers;
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;

        CallbackInfo(Map<MethodReference, Lifecycle.Event> arrayList) {
            this.mHandlerToEvent = arrayList;
            this.mEventToHandlers = new HashMap<Lifecycle.Event, List<MethodReference>>();
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : arrayList.entrySet()) {
                Lifecycle.Event event = entry.getValue();
                List<MethodReference> list = this.mEventToHandlers.get((Object)event);
                arrayList = list;
                if (list == null) {
                    arrayList = new ArrayList<MethodReference>();
                    this.mEventToHandlers.put(event, arrayList);
                }
                arrayList.add(entry.getKey());
            }
        }

        private static void invokeMethodsForEvent(List<MethodReference> list, LifecycleOwner lifecycleOwner, Lifecycle.Event event, Object object) {
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    list.get(i).invokeCallback(lifecycleOwner, event, object);
                }
            }
        }

        void invokeCallbacks(LifecycleOwner lifecycleOwner, Lifecycle.Event event, Object object) {
            CallbackInfo.invokeMethodsForEvent(this.mEventToHandlers.get((Object)event), lifecycleOwner, event, object);
            CallbackInfo.invokeMethodsForEvent(this.mEventToHandlers.get((Object)Lifecycle.Event.ON_ANY), lifecycleOwner, event, object);
        }
    }

    static class MethodReference {
        final int mCallType;
        final Method mMethod;

        MethodReference(int n, Method method) {
            this.mCallType = n;
            this.mMethod = method;
            method.setAccessible(true);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object != null) {
                if (this.getClass() != object.getClass()) {
                    return false;
                }
                object = (MethodReference)object;
                if (this.mCallType == object.mCallType && this.mMethod.getName().equals(object.mMethod.getName())) {
                    return true;
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return this.mCallType * 31 + this.mMethod.getName().hashCode();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        void invokeCallback(LifecycleOwner lifecycleOwner, Lifecycle.Event event, Object object) {
            try {
                int n = this.mCallType;
                if (n == 0) {
                    this.mMethod.invoke(object, new Object[0]);
                    return;
                }
                if (n == 1) {
                    this.mMethod.invoke(object, lifecycleOwner);
                    return;
                }
                if (n != 2) {
                    return;
                }
                this.mMethod.invoke(object, new Object[]{lifecycleOwner, event});
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new RuntimeException("Failed to call observer method", invocationTargetException.getCause());
            }
        }
    }

}

