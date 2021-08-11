/*
 * Decompiled with CFR 0_124.
 */
package android.arch.lifecycle;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ReflectiveGenericLifecycleObserver
implements GenericLifecycleObserver {
    private static final int CALL_TYPE_NO_ARG = 0;
    private static final int CALL_TYPE_PROVIDER = 1;
    private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
    static final Map<Class, CallbackInfo> sInfoCache = new HashMap<Class, CallbackInfo>();
    private final CallbackInfo mInfo;
    private final Object mWrapped;

    ReflectiveGenericLifecycleObserver(Object object) {
        this.mWrapped = object;
        this.mInfo = ReflectiveGenericLifecycleObserver.getInfo(this.mWrapped.getClass());
    }

    private static CallbackInfo createInfo(Class class_) {
        int n;
        Method[] arrmethod = class_.getSuperclass();
        Object object = new HashMap<MethodReference, Lifecycle.Event>();
        if (arrmethod != null && (arrmethod = ReflectiveGenericLifecycleObserver.getInfo(arrmethod)) != null) {
            object.putAll(arrmethod.mHandlerToEvent);
        }
        arrmethod = class_.getDeclaredMethods();
        Class<?>[] object22 = class_.getInterfaces();
        int n2 = object22.length;
        for (n = 0; n < n2; ++n) {
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : ReflectiveGenericLifecycleObserver.getInfo(object22[n]).mHandlerToEvent.entrySet()) {
                ReflectiveGenericLifecycleObserver.verifyAndPutHandler(object, entry.getKey(), entry.getValue(), class_);
            }
        }
        for (Method method : arrmethod) {
            OnLifecycleEvent onLifecycleEvent = method.getAnnotation(OnLifecycleEvent.class);
            if (onLifecycleEvent == null) continue;
            Class<?>[] arrclass = method.getParameterTypes();
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
                ReflectiveGenericLifecycleObserver.verifyAndPutHandler(object, new MethodReference(n, method), event, class_);
                continue;
            }
            throw new IllegalArgumentException("cannot have more than 2 params");
        }
        object = new CallbackInfo((Map<MethodReference, Lifecycle.Event>)object);
        sInfoCache.put(class_, (CallbackInfo)object);
        return object;
    }

    private static CallbackInfo getInfo(Class class_) {
        CallbackInfo callbackInfo = sInfoCache.get(class_);
        if (callbackInfo != null) {
            return callbackInfo;
        }
        return ReflectiveGenericLifecycleObserver.createInfo(class_);
    }

    /*
     * Exception decompiling
     */
    private void invokeCallback(MethodReference var1_1, LifecycleOwner var2_4, Lifecycle.Event var3_5) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    private void invokeCallbacks(CallbackInfo callbackInfo, LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        this.invokeMethodsForEvent(callbackInfo.mEventToHandlers.get((Object)event), lifecycleOwner, event);
        this.invokeMethodsForEvent(callbackInfo.mEventToHandlers.get((Object)Lifecycle.Event.ON_ANY), lifecycleOwner, event);
    }

    private void invokeMethodsForEvent(List<MethodReference> list, LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                this.invokeCallback(list.get(i), lifecycleOwner, event);
            }
            return;
        }
    }

    private static void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> object, MethodReference object2, Lifecycle.Event event, Class class_) {
        Lifecycle.Event event2 = object.get(object2);
        if (event2 != null && event != event2) {
            object = object2.mMethod;
            object2 = new StringBuilder();
            object2.append("Method ");
            object2.append(object.getName());
            object2.append(" in ");
            object2.append(class_.getName());
            object2.append(" already declared with different @OnLifecycleEvent value: previous");
            object2.append(" value ");
            object2.append((Object)event2);
            object2.append(", new value ");
            object2.append((Object)event);
            throw new IllegalArgumentException(object2.toString());
        }
        if (event2 == null) {
            object.put((MethodReference)object2, (Lifecycle.Event)event);
            return;
        }
    }

    @Override
    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        this.invokeCallbacks(this.mInfo, lifecycleOwner, event);
    }

    static class CallbackInfo {
        final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers;
        final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;

        CallbackInfo(Map<MethodReference, Lifecycle.Event> list) {
            this.mHandlerToEvent = list;
            this.mEventToHandlers = new HashMap<Lifecycle.Event, List<MethodReference>>();
            for (Map.Entry<MethodReference, Lifecycle.Event> entry : list.entrySet()) {
                Lifecycle.Event event = entry.getValue();
                list = this.mEventToHandlers.get((Object)event);
                if (list == null) {
                    list = new ArrayList<MethodReference>();
                    this.mEventToHandlers.put(event, list);
                }
                list.add(entry.getKey());
            }
        }
    }

    static class MethodReference {
        final int mCallType;
        final Method mMethod;

        MethodReference(int n, Method method) {
            this.mCallType = n;
            this.mMethod = method;
            this.mMethod.setAccessible(true);
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
    }

}

