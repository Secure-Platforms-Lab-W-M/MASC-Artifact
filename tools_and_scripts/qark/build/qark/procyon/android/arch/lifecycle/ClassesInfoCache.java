// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import android.support.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class ClassesInfoCache
{
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static ClassesInfoCache sInstance;
    private final Map<Class, CallbackInfo> mCallbackMap;
    private final Map<Class, Boolean> mHasLifecycleMethods;
    
    static {
        ClassesInfoCache.sInstance = new ClassesInfoCache();
    }
    
    ClassesInfoCache() {
        this.mCallbackMap = new HashMap<Class, CallbackInfo>();
        this.mHasLifecycleMethods = new HashMap<Class, Boolean>();
    }
    
    private CallbackInfo createInfo(final Class clazz, @Nullable Method[] declaredMethods) {
        final Class superclass = clazz.getSuperclass();
        final HashMap<MethodReference, Lifecycle.Event> hashMap = new HashMap<MethodReference, Lifecycle.Event>();
        if (superclass != null) {
            final CallbackInfo info = this.getInfo(superclass);
            if (info != null) {
                hashMap.putAll((Map<?, ?>)info.mHandlerToEvent);
            }
        }
        final Class[] interfaces = clazz.getInterfaces();
        for (int length = interfaces.length, i = 0; i < length; ++i) {
            for (final Map.Entry<MethodReference, Lifecycle.Event> entry : this.getInfo(interfaces[i]).mHandlerToEvent.entrySet()) {
                this.verifyAndPutHandler(hashMap, entry.getKey(), entry.getValue(), clazz);
            }
        }
        if (declaredMethods == null) {
            declaredMethods = this.getDeclaredMethods(clazz);
        }
        boolean b = false;
        for (int length2 = declaredMethods.length, j = 0; j < length2; ++j) {
            final Method method = declaredMethods[j];
            final OnLifecycleEvent onLifecycleEvent = method.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent != null) {
                b = true;
                final Class<?>[] parameterTypes = method.getParameterTypes();
                int n = 0;
                if (parameterTypes.length > 0) {
                    n = 1;
                    if (!parameterTypes[0].isAssignableFrom(LifecycleOwner.class)) {
                        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                    }
                }
                final Lifecycle.Event value = onLifecycleEvent.value();
                if (parameterTypes.length > 1) {
                    n = 2;
                    if (!parameterTypes[1].isAssignableFrom(Lifecycle.Event.class)) {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    }
                    if (value != Lifecycle.Event.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                }
                if (parameterTypes.length > 2) {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
                this.verifyAndPutHandler(hashMap, new MethodReference(n, method), value, clazz);
            }
        }
        final CallbackInfo callbackInfo = new CallbackInfo(hashMap);
        this.mCallbackMap.put(clazz, callbackInfo);
        this.mHasLifecycleMethods.put(clazz, b);
        return callbackInfo;
    }
    
    private Method[] getDeclaredMethods(final Class clazz) {
        try {
            return clazz.getDeclaredMethods();
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", noClassDefFoundError);
        }
    }
    
    private void verifyAndPutHandler(final Map<MethodReference, Lifecycle.Event> map, final MethodReference methodReference, final Lifecycle.Event event, final Class clazz) {
        final Lifecycle.Event event2 = map.get(methodReference);
        if (event2 != null && event != event2) {
            throw new IllegalArgumentException("Method " + methodReference.mMethod.getName() + " in " + clazz.getName() + " already declared with different @OnLifecycleEvent value: previous" + " value " + event2 + ", new value " + event);
        }
        if (event2 == null) {
            map.put(methodReference, event);
        }
    }
    
    CallbackInfo getInfo(final Class clazz) {
        final CallbackInfo callbackInfo = this.mCallbackMap.get(clazz);
        if (callbackInfo != null) {
            return callbackInfo;
        }
        return this.createInfo(clazz, null);
    }
    
    boolean hasLifecycleMethods(final Class clazz) {
        if (this.mHasLifecycleMethods.containsKey(clazz)) {
            return this.mHasLifecycleMethods.get(clazz);
        }
        final Method[] declaredMethods = this.getDeclaredMethods(clazz);
        for (int length = declaredMethods.length, i = 0; i < length; ++i) {
            if (declaredMethods[i].getAnnotation(OnLifecycleEvent.class) != null) {
                this.createInfo(clazz, declaredMethods);
                return true;
            }
        }
        this.mHasLifecycleMethods.put(clazz, false);
        return false;
    }
    
    static class CallbackInfo
    {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers;
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;
        
        CallbackInfo(final Map<MethodReference, Lifecycle.Event> mHandlerToEvent) {
            this.mHandlerToEvent = mHandlerToEvent;
            this.mEventToHandlers = new HashMap<Lifecycle.Event, List<MethodReference>>();
            for (final Map.Entry<MethodReference, Lifecycle.Event> entry : mHandlerToEvent.entrySet()) {
                final Lifecycle.Event event = entry.getValue();
                List<MethodReference> list;
                if ((list = this.mEventToHandlers.get(event)) == null) {
                    list = new ArrayList<MethodReference>();
                    this.mEventToHandlers.put(event, list);
                }
                list.add(entry.getKey());
            }
        }
        
        private static void invokeMethodsForEvent(final List<MethodReference> list, final LifecycleOwner lifecycleOwner, final Lifecycle.Event event, final Object o) {
            if (list != null) {
                for (int i = list.size() - 1; i >= 0; --i) {
                    list.get(i).invokeCallback(lifecycleOwner, event, o);
                }
            }
        }
        
        void invokeCallbacks(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event, final Object o) {
            invokeMethodsForEvent(this.mEventToHandlers.get(event), lifecycleOwner, event, o);
            invokeMethodsForEvent(this.mEventToHandlers.get(Lifecycle.Event.ON_ANY), lifecycleOwner, event, o);
        }
    }
    
    static class MethodReference
    {
        final int mCallType;
        final Method mMethod;
        
        MethodReference(final int mCallType, final Method mMethod) {
            this.mCallType = mCallType;
            (this.mMethod = mMethod).setAccessible(true);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o == null || this.getClass() != o.getClass()) {
                    return false;
                }
                final MethodReference methodReference = (MethodReference)o;
                if (this.mCallType != methodReference.mCallType || !this.mMethod.getName().equals(methodReference.mMethod.getName())) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return this.mCallType * 31 + this.mMethod.getName().hashCode();
        }
        
        void invokeCallback(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event, final Object o) {
            try {
                switch (this.mCallType) {
                    case 0: {
                        this.mMethod.invoke(o, new Object[0]);
                        return;
                    }
                    case 1: {
                        goto Label_0061;
                        goto Label_0061;
                    }
                    case 2: {
                        break;
                    }
                    default: {
                        return;
                    }
                }
            }
            catch (InvocationTargetException ex) {
                throw new RuntimeException("Failed to call observer method", ex.getCause());
            }
            catch (IllegalAccessException ex2) {
                throw new RuntimeException(ex2);
            }
            this.mMethod.invoke(o, lifecycleOwner, event);
        }
    }
}
